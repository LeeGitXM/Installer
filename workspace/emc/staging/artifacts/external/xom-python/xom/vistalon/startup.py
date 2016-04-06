'''
Created on Oct 5, 2014

@author: Pete
'''

import system
log = system.util.getLogger("com.ils.vistalon")

def gateway():
    
    #------------------------------------------------------------------------------------------------
    # Putting this in its own function allows the other startups to proceed while this sleeps.
    def doit():
        # Give the modules time to complete initialization.  Delays are always a bad / tricky thing.
        # I'm not sure if this is really required, but it sure makes it easier to follow the log messages during startup
        # where BLT, SFC, and lab data are all intermingled.
        import time
        time.sleep(5) 
        
        import system
        log = system.util.getLogger("com.ils.vistalon")
        
        print "Starting the deferred startup..."
        
        # Start all of the packages used at the site
        
        import ils.recipeToolkit.startup as recipeToolkitStartup
        recipeToolkitStartup.gateway()
    
        import ils.diagToolkit.startup as diagToolkitStartup
        diagToolkitStartup.gateway()
        
        import ils.uir.startup as uirStartup
        uirStartup.gateway()
        
        import ils.labData.startup as labDataStartup
        labDataStartup.gateway()
        
        import ils.common.startup as commonStartup
        commonStartup.gateway() 
        
        #
        # Now perform very specific startup for Vistalon
        #
    
        from xom.vistalon.configuration import getTagProvider
        provider = getTagProvider()
        
        from xom.vistalon.configuration import getHistoryProvider
        historyProvider = getHistoryProvider()
        
        from xom.vistalon.configuration import getDatabase
        database = getDatabase()
        
        createTags("[" + provider + "]", log)
        
        # I think we need to make sure the grade is correct which will update the reactor configuration 
        # tags in recipe/local that determine series, split feed, single, or c-rx before we configure 
        # the selectors.

        from xom.vistalon.configuration import handleGradeChange
        for unit in ['RLA3', 'VFU']:
            grade=system.tag.read("[" + provider + "]Site/" + unit + "/Grade/grade").value
            log.info("Updating Vistalon grade parameters for %s - %s..." % (unit, str(grade)))
            handleGradeChange(unit, grade, provider, database)

        # Make sure the Polymer lab data source is configured correctly.  There is a tag change script that detects 
        # changes but this explicitly forces the logic.
        log.info("Configuring the Polymer Lab Data Source...")
        tagPath = "[%s]Recipe/Local/POLYMER-LAB-DATA-SOURCE" % (provider)
        polymerLabDataSource=system.tag.read(tagPath)
        from xom.vistalon.configuration import polymerLabDataSourceDecoder
        polymerLabDataSourceDecoder(tagPath, -1, polymerLabDataSource, True)
        
        log.info("Configuring Vistalon lab data selectors...")
        from xom.vistalon.configuration import polymerComponentSampleLocation
        polymerComponentSampleLocation(tagPath="", previousValue="", currentValue="", initialChange=False)
        
        log.info("Restoring Vistalon lab data...")
        from ils.labData.startup import restoreHistory
        restoreHistory(provider, historyProvider, daysToRestore=21)
        
        # Create the loggers that are used for Diagnostic - do this last so I don't screw up the log messages
        print "Creating the Vistalon diagnostic loggers...."
        log = system.util.getLogger("project.vistalon.cstr")
        log.info("Initializing") 
        
        log = system.util.getLogger("project.vistalon.crx")
        log.info("Initializing") 
        
        print "Done with Vistalon startup..."

    #---------------------------------------------------------------------------------------------------------
    from xom.vistalon.version import version
    version, revisionDate = version()
        
    log.info("Starting Vistalon version %s - %s" % (version, revisionDate))
    system.util.invokeAsynchronous(doit)
    
     
#
def createTags(tagProvider, log):
    print "Creating global constant memory tags...."
    headers = ['Path', 'Name', 'Data Type', 'Value']
    data = []

    # Create site specific Vistalon "Local" recipe tags
    path = tagProvider + "Recipe/Local/"
    data.append([path + "CATOUT-RECIPE-STATUS/", "CAST-TIME-TO-CLOSED", "Float8", "0.0"])
    data.append([path + "CATOUT-RECIPE-STATUS/", "IRG-TIME-TO-CLOSED", "Float8", "0.0"])
    data.append([path + "CATOUT-RECIPE-STATUS/", "OIL-TIME-TO-CLOSED", "Float8", "0.0"])
    data.append([path, "DML-ERROR-RATE-LIMIT", "Float8", "0.0"])
    data.append([path, "DML-SQC-FLAG", "Float8", "0.0"])
    data.append([path, "MLR-GRADE-FLAG", "Float8", "0.0"])
    
    # This tag needs an event handling script 
    data.append([path, "POLYMER-LAB-DATA-SOURCE", "Float8", "0.0"])
    data.append([path, "POLY-PROP-FROM-FD", "Boolean", "False"])
    data.append([path, "POLYSPLIT-SQC-FLAG", "Float8", "0.0"])
    data.append([path, "PROD-CA-SQC-FLAG", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "CA-TARGET", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "E202-BYPASS-DELTA-TIME", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "E202-BYPASS-POSITION", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "E204-LEVEL", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "E204-TEMP", "Float8", "0.0"])
    data.append([path + "RX-RECIPE/", "GEL-DELAY", "Float8", "0.0"])
    data.append([path + "SERIES-PERMISSIVES/", "C3-TO-R2-OK-FLAG", "Float8", "0.0"])
    data.append([path + "SERIES-PERMISSIVES/", "C9-TO-R2-OK-FLAG", "Float8", "0.0"])
    data.append([path + "SERIES-PERMISSIVES/", "NO-C3-MIN-RATE", "Float8", "0.0"])
    data.append([path + "SERIES-PERMISSIVES/", "NO-C9-MIN-RATE", "Float8", "0.0"])
    data.append([path + "SERIES-PERMISSIVES/", "SERIES-MIN-C6-TO-R2", "Float8", "0.0"])

    data.append([path, "VFU-FTNIR-GRADE", "Float8", "0.0"])
    data.append([path, "VFU-FTNIR-BIAS-UPDATE", "Float8", "0.0"])
    data.append([path, "VFU-BALER-TEMP-CHK", "Float8", "0.0"])
    
    # Create site specific Vistalon configuration tags that are used by various toolkits
    
    # This tag needs an event handling script
    data.append([path, "SERIES-RX-GRADE", "Boolean", "False"])
    data.append([path, "SINGLE-RX-GRADE", "Boolean", "False"])
    data.append([path, "SPLIT-FEED-GRADE", "Boolean", "False"])
    data.append([path, "C-RX-GRADE", "Boolean", "False"])
    
    # Create site specific Vistalon "Local" recipe tags
    path = tagProvider + "Site/"
    data.append([path, "RX-SAMPLE-DELAY-MINUTES", "Float8", "50.0"])
    data.append([path, "RX-SAMPLE-SQC-FAMILY-PRIORITY", "Float8", "10.0"])
    data.append([path, "FD-SAMPLE-DELAY-MINUTES", "Float8", "105.0"])
    data.append([path, "FD-SAMPLE-SQC-FAMILY-PRIORITY", "Float8", "11.0"])
    data.append([path, "MOONEY-CR-X-ELAPSED-TIME-MINUTES", "Float8", "1.0"])
    data.append([path, "MOONEY-CSTR-ELAPSED-TIME-MINUTES", "Float8", "50.0"])
    
    ds = system.dataset.toDataSet(headers, data)
    
    from ils.common.tagFactory import createConfigurationTags
    createConfigurationTags(ds, log)