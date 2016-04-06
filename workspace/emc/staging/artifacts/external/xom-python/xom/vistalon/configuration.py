'''
Created on May 10, 2015

@author: Pete
'''
import system, time
from ils.labData.selector import configureSelector
from ils.diagToolkit.common import updateFamilyPriority
log = system.util.getLogger("com.xom.configuration")

# This should be the only place in the project that we hard-code the provider and database.
# These should only be used if there is not a SFC or diag toolkit scope available.
def getTagProvider():
    return 'XOM'

def getHistoryProvider():
    return 'XOMhistory'

def getDatabase():
    return 'XOM'

# This is called from the event handler on the grade tag that is site specific.  All of the actions that take place on a grade change 
# should be listed here. (The oldGrade and newGrade arguments are qualified values.   This has nothing to do with diag or 
# sfcs and so we always want the production database instance. Do not run if this is the initial change on startup. 
def gradeChange(tagPath, oldGrade, newGrade, initialChange):
    log.info("A grade change was detected for %s from %s to %s (Initial: %s)!" % (tagPath, str(oldGrade), str(newGrade), str(initialChange)))
    
    if initialChange:
        log.info("...ignoring initial grade change...")
        return
    
    # This is called from a tag change script  and has nothing to do with diag or sfcs and so we 
    # always want the production database instance. Do not run if this is the initial change on startup
    
    if newGrade.quality.isGood():
        database = getDatabase()
        tagProvider = getTagProvider()

        # The recipe family is the same as the unit.  The convention is that the grade tags are in a folder site/UNIT, so get the 
        # unit/family out of the tagPath    
        from ils.labData.limits import parseRecipeFamilyFromGradeTagPath
        recipeFamily=parseRecipeFamilyFromGradeTagPath(tagPath)
        log.info("Loading recipe for recipe family: %s" % recipeFamily)
        
        handleGradeChange(recipeFamily, newGrade.value, tagProvider, database)


# This is called from a tag change script on the rx-configuration OPC tag and decodes the configuration into 
# the individual configuration memory tags in local which in turn trigger selector reconfiguration.
# oldConfig and newConfig are both qualified values.
# Because this is an OPC tag, it should receive a value on startup that should trigger this,, and this should all happen BEFORE the 
def rxConfigDecoder(tagPath, oldConfig, newConfig, initialChange):
    log.info("In rxConfigDecoder decoding a config value: %s" % (str(newConfig.value)))

    newConfig = round(newConfig.value)
    provider = getTagProvider()
    if newConfig == 1 or newConfig == 2:
        log.info("...configuring for single Rx...")
        singleRxGrade=True
        seriesRxGrade=False
        cRxGrade=False
        splitFeedGrade=False
    elif newConfig == 3:
        log.info("...configuring for C-Rx...")
        singleRxGrade=False
        seriesRxGrade=False
        cRxGrade=True
        splitFeedGrade=False
    elif newConfig == 4:
        log.info("...configuring for series Rx...")
        singleRxGrade=False
        seriesRxGrade=True
        cRxGrade=False
        splitFeedGrade=False
    elif newConfig == 5:
        log.info("...configuring for split-feed Rx...")
        singleRxGrade=False
        seriesRxGrade=False
        cRxGrade=False
        splitFeedGrade=True
    else:
        log.error("Unexpected configuration received: %i" % (newConfig))
        return

    system.tag.write("[%s]Recipe/Local/C-RX-GRADE" % (provider), cRxGrade)
    system.tag.write("[%s]Recipe/Local/SERIES-RX-GRADE" % (provider), seriesRxGrade)
    system.tag.write("[%s]Recipe/Local/SINGLE-RX-GRADE" % (provider), singleRxGrade)
    system.tag.write("[%s]Recipe/Local/SPLIT-FEED-GRADE" % (provider), splitFeedGrade)
    
    log.info("Done decoding reactor configuration!")


# This is called from a tag change script on the POLYMER-LAB-DATA-SOURCE, a memory tag which comes
# directly from recipe.  This procedure decodes the integer value (0=Rx, 1=FD) into the boolean memory
# tag POLY-PROP-FROM-FD.
def polymerLabDataSourceDecoder(tagPath, oldConfig, newConfig, initialChange):
    log.info("In polymerLabDataSourceDecoder decoding value: %s" % (str(newConfig.value)))

    newConfig = round(newConfig.value)
    provider = getTagProvider()
    if newConfig == 0:
        log.info("...configuring for getting poly properties from Rx...")
        polyPropFromFlashDrum=False
    elif newConfig == 1:
        log.info("...configuring for getting poly properties from the Flash Drum...")
        polyPropFromFlashDrum=True
    else:
        log.error("Unexpected configuration received: %i" % (newConfig))
        return

    system.tag.write("[%s]Recipe/Local/POLY-PROP-FROM-FD" % (provider), polyPropFromFlashDrum)
    
    log.info("Done decoding Polymer Lab Data Source!")


# This is called on startup and from a tag change script on the grade UDT tags
def handleGradeChange(recipeFamily, grade, tagProvider, database):
    from ils.labData.limits import updateSQCLimitsFromRecipe
    log.info("...restoring SQC limits from recipe for %s to %s!" % (recipeFamily, str(grade)))
    updateSQCLimitsFromRecipe(recipeFamily, grade, database)
        
    # This is here to set the local recipe on startup.  This will also run when a recipe downloads, but I don't think it 
    # will interfere with normal processing.  
    from ils.recipeToolkit.startup import restoreLocalRecipe
    log.info("...restoring local recipe data for %s - %s..." % (recipeFamily, str(grade)))
    restoreLocalRecipe(recipeFamily, grade, tagProvider, database)
        

# This configures the Vistalon lab data selectors.  There are a number of tags that I believe get set from recipe
# data that drive this from a value changed event handler.
#
# Mooney and Mooney Relaxation samples are always taken at reactor(s)
# if poly-prop-from-fd is true then polymer C2 and ENB samples are taken from flash drum (use flash drum tags);
# else from reactor  (use R1 or R2 tags as appropriate) 
# if series rx, then use R2 tags; else use R1 tags
# update sample action delays for sample location
def polymerComponentSampleLocation(tagPath, previousValue, currentValue, initialChange):
    log.info("In polymerComponentSampleLocation(), detected a change to %s from %s to %s (Initial: %s)..." % (tagPath, str(previousValue), str(currentValue), str(initialChange)))
    
    #----------------------------------------------------------------
    # I have no idea how this works without a tag provider.  This is triggered from a tag change script, so it should
    # require a provider.
    def setSelectorProcessing(state):
        log.trace("Setting selector processing to: %s" % (str(state)))
        
        tags=[]
        values=[]
        for selector in ["RLA3/mooney-lab-data", "RLA3/mlr-lab-data", "RLA3/mlr-raw-lab-data", "RLA3/c2-lab-data", 
                         "RLA3/c2-lab-data-for-r1-nlc", "RLA3/c9-lab-data","RLA3/c9-lab-data-for-r1-nlc",\
                         "VFU/spec-c2-lab-data","VFU/spec-c9-lab-data"]:
            tags.append("LabData/" + selector + "/processingEnabled")
            values.append(state)
        
        system.tag.writeAll(tags, values)
    #----------------------------------------------------------------
    def setElapsedTimeVariable(timerName, val):        
        log.error("***** NEED TO IMPLEMENT WRITE TO AN ELAPSED TIME VARIABLE *****")
#        conclude that the elapsed-time-minutes of ethylene-elapsed-timer = fd-sample-delay-minutes;
    #----------------------------------------------------------------
    
    # Read the unit configuration tags
    seriesRxGrade=system.tag.read("Recipe/Local/SERIES-RX-GRADE")
    cRxGrade=system.tag.read("Recipe/Local/C-RX-GRADE")
    polyPropertiesFromFlashDrum=system.tag.read("Recipe/Local/POLY-PROP-FROM-FD")

    log.info("Setting selector sample location...")
    log.info("  Series Rx Grade: %s" % (str(seriesRxGrade)))
    log.info("  C-Rx Grade:      %s" % (str(cRxGrade)))
    log.info("  Flash Drum:      %s" % (str(polyPropertiesFromFlashDrum)))
    log.info("  Initial change:  %s" % (str(initialChange)))
    
    if str(seriesRxGrade.quality) != "Good":
        log.error("Unable to process selector configuration because the SERIES-RX-GRADE tag is bad")
        return
    
    if str(cRxGrade.quality) != "Good":
        log.error("Unable to process selector configuration because the C-RX-GRADE tag is bad")
        return
    
    if str(polyPropertiesFromFlashDrum.quality) != "Good":
        log.error("Unable to process selector configuration because the POLY-PROP-FROM-FD tag is bad")
        return
    
    seriesRxGrade=seriesRxGrade.value
    cRxGrade=cRxGrade.value
    polyPropertiesFromFlashDrum=polyPropertiesFromFlashDrum.value

    # First set the visibility of some lab data tables
    from ils.common.cast import toBit
    seriesRxGradeBit=toBit(seriesRxGrade)
    SQL = "update LtDisplayTable set DisplayFlag = %i where OldTableName in ('RX-SERIES-DATA', 'RX-DELTA-DATA')" % (seriesRxGradeBit)
    print SQL
    rows = system.db.runUpdateQuery(SQL)
    print "Updated %i rows" % (rows)
    
    # Read some constants that are used during the configuration - these are memory tags so they pretty much have to be good
    
    rxSampleDelayMinutes=system.tag.read("Site/RX-SAMPLE-DELAY-MINUTES").value
    rxSampleSqcFamilyPriority=system.tag.read("Site/RX-SAMPLE-SQC-FAMILY-PRIORITY").value
    fdSampleDelayMinutes=system.tag.read("Site/FD-SAMPLE-DELAY-MINUTES").value
    fdSampleSqcFamilyPriority=system.tag.read("Site/FD-SAMPLE-SQC-FAMILY-PRIORITY").value
    mooneyCrxElapsedTimeMinutes=system.tag.read("Site/MOONEY-CR-X-ELAPSED-TIME-MINUTES").value
    mooneyCstrElapsedTimeMinutes=system.tag.read("Site/MOONEY-CSTR-ELAPSED-TIME-MINUTES").value
    
    # Turn off processing so that lab values don't get into memory again    
    setSelectorProcessing(False)
    
    # Time in seconds - give the statement above time to percolate
    time.sleep(2.0)
    
    from ils.labData.selector import updateSelectorDisplayTableDescription
    
    if not(seriesRxGrade):
        log.info("...configuring selectors for a non series grade...")
        configureSelector("RLA3", "MOONEY-LAB-DATA",       "R1-ML-LAB-DATA")
        configureSelector("RLA3", "mooney-lab-data-sqc",   "r1-ml-lab-data-sqc")
        updateSelectorDisplayTableDescription("MOONEY-LAB-DATA", "R1-ML-LAB-DATA")
        
        configureSelector("RLA3", "mlr-lab-data",          "r1-mlra-lab-data")
        configureSelector("RLA3", "mlr-lab-data-sqc",      "r1-mlra-lab-data-sqc")
        updateSelectorDisplayTableDescription("MLR-LAB-DATA", "R1-MLRA-LAB-DATA")
        
        configureSelector("RLA3", "mlr-raw-lab-data",      "r1-raw-mlra-lab-data")
        updateSelectorDisplayTableDescription("MLR-RAW-LAB-DATA", "R1-RAW-MLRA-LAB-DATA")
        
        # Configure the mooney ETV time delay based on whether we are running the tubular or stirred tank reactor. 
        if cRxGrade: 
            setElapsedTimeVariable("Mooney-Elapsed-Timer", mooneyCrxElapsedTimeMinutes) 
        else:
            setElapsedTimeVariable("Mooney-Elapsed-Timer", mooneyCstrElapsedTimeMinutes)

        # Configure lab data and sqc families for reactor or flash drum content sampling.
        if polyPropertiesFromFlashDrum:
            configureSelector("RLA3", "c2-lab-data",               "fd-c2-lab-data")
            configureSelector("RLA3", "c2-lab-data-sqc",           "fd-c2-lab-data-sqc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA", "FD-C2-LAB-DATA")
            
            configureSelector("RLA3", "c2-lab-data-for-r1-nlc",    "fd-c2-lab-data-for-r1-nlc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA-FOR-R1-NLC", "FD-C2-LAB-DATA-FOR-NLC")
            
            configureSelector("VFU", "spec-c2-lab-data",           "fd-spec-c2-lab-data")
            configureSelector("VFU", "spec-c2-lab-data-validity",  "fd-spec-c2-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C2-LAB-DATA", "FD-SPEC-C2-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data",               "fd-c9-lab-data")
            configureSelector("RLA3", "c9-lab-data-sqc",           "fd-c9-lab-data-sqc")
            updateSelectorDisplayTableDescription("C9-LAB-DATA", "FD-C9-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data-for-r1-nlc",    "fd-c9-lab-data-for-r1-nlc")
            updateSelectorDisplayTableDescription("C9-LAB-DATA-FOR-R1-NLC", "FD-C9-LAB-DATA-FOR-R1-NLC")
            
            configureSelector("VFU", "spec-c9-lab-data",           "fd-spec-c9-lab-data")
            configureSelector("VFU", "spec-c9-lab-data-validity",  "fd-spec-c9-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C9-LAB-DATA", "FD-SPEC-C9-LAB-DATA")
            
            setElapsedTimeVariable("ethylene-elapsed-timer", fdSampleDelayMinutes)
            setElapsedTimeVariable("cstr-enb-elapsed-timer", fdSampleDelayMinutes)

            updateFamilyPriority("Cstr_C2_Problem", fdSampleSqcFamilyPriority)
            updateFamilyPriority("Cstr_Enb_Problem", fdSampleSqcFamilyPriority)
        else:
            configureSelector("RLA3", "c2-lab-data",               "r1-c2-lab-data")
            configureSelector("RLA3", "c2-lab-data-sqc",           "r1-c2-lab-data-sqc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA", "R1-C2-LAB-DATA")
            
            configureSelector("RLA3", "c2-lab-data-for-r1-nlc",    "r1-c2-lab-data-for-r1-nlc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA-FOR-R1-NLC", "R1-C2-LAB-DATA-FOR-R1-NLC")
            
            configureSelector("VFU", "spec-c2-lab-data",           "r1-spec-c2-lab-data")
            configureSelector("VFU", "spec-c2-lab-data-validity",  "r1-spec-c2-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C2-LAB-DATA", "R1-SPEC-C2-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data",               "r1-c9-lab-data")
            configureSelector("RLA3", "c9-lab-data-sqc",           "r1-c9-lab-data-sqc")
            updateSelectorDisplayTableDescription("C9-LAB-DATA", "R1-C9-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data-for-r1-nlc",    "r1-c9-lab-data-for-r1-nlc")
            updateSelectorDisplayTableDescription("C9-LAB-DATA-FOR-R1-NLC", "R1-C9-LAB-DATA-FOR-R1-NLC")
            
            configureSelector("VFU", "spec-c9-lab-data",           "r1-spec-c9-lab-data")
            configureSelector("VFU", "spec-c9-lab-data-validity",  "r1-spec-c9-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C9-LAB-DATA", "R1-SPEC-C9-LAB-DATA")
 
            setElapsedTimeVariable("ethylene-elapsed-timer", rxSampleDelayMinutes)
            setElapsedTimeVariable("cstr-enb-elapsed-timer", rxSampleDelayMinutes)

            updateFamilyPriority("Cstr_C2_Problem", rxSampleSqcFamilyPriority)
            updateFamilyPriority("Cstr_Enb_Problem", rxSampleSqcFamilyPriority)

    else:   # Must be a series configuratiion
        log.info("...configuring selectors for a series grade...")
        configureSelector("RLA3", "mooney-lab-data",               "r2-ml-lab-data")
        configureSelector("RLA3", "mooney-lab-data-sqc",           "r2-ml-lab-data-sqc")
        updateSelectorDisplayTableDescription("MOONEY-LAB-DATA", "R2-ML-LAB-DATA")
        
        configureSelector("RLA3", "mlr-lab-data",                  "r2-mlra-lab-data")
        configureSelector("RLA3", "mlr-lab-data-sqc",              "r2-mlra-lab-data-sqc")
        updateSelectorDisplayTableDescription("MLR-LAB-DATA", "R2-MLRA-LAB-DATA")
        
        configureSelector("RLA3", "mlr-raw-lab-data",              "r2-raw-mlra-lab-data")
        updateSelectorDisplayTableDescription("MLR-RAW-LAB-DATA", "R2-RAW-MLRA-LAB-DATA")
        
        configureSelector("RLA3", "c2-lab-data-for-r1-nlc",        "r1-c2-lab-data-for-r1-nlc")
        updateSelectorDisplayTableDescription("C2-LAB-DATA-FOR-R1-NLC", "R1-C2-LAB-DATA-FOR-R1-NLC")
        
        configureSelector("RLA3", "c9-lab-data-for-r1-nlc",        "r1-c9-lab-data-for-r1-nlc")      
        updateSelectorDisplayTableDescription("C9-LAB-DATA-FOR-R1-NLC", "R1-C9-LAB-DATA-FOR-R1-NLC")

        # Configure the mooney ETV time delay the stirred tank reactor.
#        conclude that the elapsed-time-minutes of mooney-elapsed-timer = mooney-cstr-elapsed-time-minutes; 

        if polyPropertiesFromFlashDrum:
            configureSelector("RLA3", "c2-lab-data",               "fd-c2-lab-data")
            configureSelector("RLA3", "c2-lab-data-sqc",           "fd-c2-lab-data-sqc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA", "FD-C2-LAB-DATA")
            
            configureSelector("VFU", "spec-c2-lab-data",           "fd-spec-c2-lab-data")
            configureSelector("VFU", "spec-c2-lab-data-validity",  "fd-spec-c2-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C2-LAB-DATA", "FD-SPEC-C2-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data",               "fd-c9-lab-data")
            configureSelector("RLA3", "c9-lab-data-sqc",           "fd-c9-lab-data-sqc") 
            updateSelectorDisplayTableDescription("C9-LAB-DATA", "FD-C9-LAB-DATA")
            
            configureSelector("VFU", "spec-c9-lab-data",           "fd-spec-c9-lab-data")
            configureSelector("VFU", "spec-c9-lab-data-validity",  "fd-spec-c9-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C9-LAB-DATA", "FD-SPEC-C9-LAB-DATA")
            
#           conclude that the elapsed-time-minutes of ethylene-elapsed-timer = fd-sample-delay-minutes; 
#           conclude that the elapsed-time-minutes of cstr-enb-elapsed-timer = fd-sample-delay-minutes; 

            fdSampleSqcFamilyPriority = 3   #TODO Translate this from a tag somewhere TBD
            updateFamilyPriority("Cstr_C2_Problem", fdSampleSqcFamilyPriority)
            updateFamilyPriority("Cstr_Enb_Problem", fdSampleSqcFamilyPriority)
        
        else:
            configureSelector("RLA3", "c2-lab-data",               "r2-c2-lab-data")
            configureSelector("RLA3", "c2-lab-data-sqc",           "r2-c2-lab-data-sqc")
            updateSelectorDisplayTableDescription("C2-LAB-DATA", "R2-C2-LAB-DATA")
            
            configureSelector("VFU", "spec-c2-lab-data",           "r2-spec-c2-lab-data")
            configureSelector("VFU", "spec-c2-lab-data-validity",  "r2-spec-c2-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C2-LAB-DATA", "R2-SPEC-C2-LAB-DATA")
            
            configureSelector("RLA3", "c9-lab-data",               "r2-c9-lab-data")
            configureSelector("RLA3", "c9-lab-data-sqc",           "r2-c9-lab-data-sqc")
            updateSelectorDisplayTableDescription("C9-LAB-DATA", "R2-C9-LAB-DATA")
            
            configureSelector("VFU", "spec-c9-lab-data",           "r2-spec-c9-lab-data")
            configureSelector("VFU", "spec-c9-lab-data-validity",  "r2-spec-c9-lab-data-validity")
            updateSelectorDisplayTableDescription("SPEC-C9-LAB-DATA", "R2-SPEC-C9-LAB-DATA")

#            conclude that the elapsed-time-minutes of ethylene-elapsed-timer = rx-sample-delay-minutes;
#            conclude that the elapsed-time-minutes of cstr-enb-elapsed-timer = rx-sample-delay-minutes; 
            rxSampleSqcFamilyPriority = 2   #TODO Translate this from a tag somewhere TBD
            updateFamilyPriority("Cstr_C2_Problem", rxSampleSqcFamilyPriority)
            updateFamilyPriority("Cstr_Enb_Problem", rxSampleSqcFamilyPriority)

    # Time in seconds - give all of the changes we made time to get where they are going
    time.sleep(2.0)

    setSelectorProcessing(True)
    print "Done setting the Lab Data selectors"
