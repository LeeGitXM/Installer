'''
Created on Jun 21, 2015

@author: Pete
'''
import system, datetime
log = system.util.getLogger("com.ils.labData.derivedValues")

def mlr(dataDictionary):
#    tagProvider="XOM"
    log.trace("In derived value callback mlr() - the data dictionary is: %s" % (str(dataDictionary)))
    
    if len(dataDictionary) != 3:
        txt = "The data dictionary for the derived callback mlr() requires exactly 3 elements: " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    # The derived value framework is responsible for packaging the trigger and related values into the data dictionary.
    mlrRawDict=dataDictionary.get("MLR-RAW-LAB-DATA", None)
    if mlrRawDict == None:
        txt = "Derived value callback mlr() is missing related data named: MLR-RAW-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    mooneyDict=dataDictionary.get("MOONEY-LAB-DATA", None)
    if mooneyDict == None:
        txt = "Derived value callback mlr() is missing related data named: MOONEY-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    mlrRaw=mlrRawDict.get("rawValue", 0.0)
    mooney=mooneyDict.get("rawValue", 0.0)

    log.trace("  mlr() is using mlrRaw=%f, mooneyRaw=%f" % (mlrRaw, mooney))

#    qv=system.tag.read("[%s]Site/RLA3/MLR-SLOPE" % (tagProvider))
    qv=system.tag.read("Site/RLA3/MLR-SLOPE")
    if not(qv.quality.isGood()):
        txt = "Derived value callback mlr() requires MLR-SLOPE, which is not good: %s" % (str(qv))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    mlrSlope=qv.value
    log.trace("  mlr() is using MLR-SLOPE=%f" % (mlrSlope))
    
    # Previously, mstBasis was a global named parameter that was calculated every 10 minutes by a 
    # S88 recipe whose only job was to read the current grade and then fetch from recipe the 
    # high and low limits for mooney and then calculate the midpoint.  The first step is to read the grade.

#    qv=system.tag.read("[%s]Site/RLA3/Grade/grade" % (tagProvider))
    qv=system.tag.read("Site/RLA3/Grade/grade")
    if not(qv.quality.isGood()):
        txt = "Derived value callback mlr() requires GRADE, which is not good: %s" % (str(qv))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    grade=qv.value
    
    tot = 0.0
    for param in ['Rx ML Low Limit', 'Rx ML High Limit']:
        SQL = "select GD.RecommendedValue "\
            " from RtRecipeFamily RF, RtValueDefinition VD, RtGradeDetail GD, RtGradeMaster GM "\
            " where RF.RecipeFamilyId = VD.RecipeFamilyId "\
            " and VD.ValueId = GD.valueId "\
            " and GM.RecipeFamilyId = GD.RecipeFamilyId "\
            " and GM.Grade = GD.Grade "\
            " and GM.Version = GD.Version "\
            " and GM.Active = 1 "\
            " and VD.Description = '%s'"\
            " and RF.RecipeFamilyName = 'RLA3'"\
            " and GM.Grade = '%s'" % (param, grade)
        val = system.db.runScalarQuery(SQL)
        log.trace("  mlr() fetched %s=%s" % (param, str(val)))
        tot = tot + float(val)
    mstBasis = tot / 2.0
    log.trace("  mlr() is using mstBasis=%f" % (mstBasis))
    
    if mlrSlope > 0.0:
        mlrCorr=mlrRaw + mlrSlope * (mstBasis - mooney)
    else:
        mlrCorr=mlrRaw

    log.trace("  mlr calculated() mlr as: %s" % (str(mlrCorr)))
    return {"status": "success", "value": mlrCorr}


# This can be used in conjunction with a single related value and calculates the difference
# between the trigger and the related value (trigger - related)
def rxPropertyDiff(dataDictionary):
    log.trace("In derived value callback rxPropertyDiff() - the data dictionary is: %s" % (str(dataDictionary)))
    if len(dataDictionary) != 2:
        txt = "The data dictionary for the derived callback rxPropertyDiff() requires exactly 2 elements: %s" % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    foundTrigger = False
    foundRelated = False
    for d in dataDictionary.values():
        trigger=d.get("trigger", False)
        if trigger:
            foundTrigger = True
            triggerValue = d.get("rawValue", 0.0)
        else:
            foundRelated = True
            relatedValue = d.get("rawValue", 0.0)
    
    if not(foundTrigger and foundRelated):
        txt = "The data dictionary for the derived callback rxPropertyDiff() is missing either the trigger or the related data in the data dictionary: " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
        
    derivedVal = relatedValue - triggerValue 

    log.trace("Calculated rxPropertyDiff as: %s" % (str(derivedVal)))
    return {"status": "success", "value":derivedVal}


def rxMlPropDiff(dataDictionary):
    log.trace("In derived value callback rxMlPropDiff() - the data dictionary is: %s" % (str(dataDictionary)))

    grade=system.tag.read("Site/RLA3Grade/grade").value
    
    r1MlLabDataValue=dataDictionary.get("R1-ML-LAB-DATA").get("rawValue")
    mooneyRawValue=dataDictionary.get("MOONEY-LAB-DATA").get("rawValue")
    
    if grade == 706:
        log.trace("Grade 706 is special - using the mooney target as the raw value")
        mooneyTarget=system.tag.read("LabData/RLA3/MOONEY-LAB-DATA-SQC/target")
        if mooneyTarget.quality.isGood():
            derivedVal = mooneyTarget - r1MlLabDataValue
            log.trace("Calculated ")
        else:
            txt="Error collecting the mooney target value for rxMlPropDiff() calculation"
            returnDictionary={"status":"error", "value":None, "errorMessage":txt}
            return returnDictionary
    else:
        derivedVal = mooneyRawValue - r1MlLabDataValue

    log.trace("Calculated rxMlPropDiff as: %s" % (str(derivedVal)))

    return {"status": "success", "value":derivedVal}


# The derived value is simply the sum of the raw values in the data dictionary.
# The order is not important! (This could be given a generic name)
def c9InCrumb(dataDictionary):
    log.trace("In derived value callback c9InCrumb() - the data dictionary is: %s" % (str(dataDictionary)))
    
    derivedVal = 0.0
    for tagDictionary in dataDictionary.values():
        log.trace("   Tag dictionary: %s" % (str(tagDictionary)))
        rawValue=tagDictionary.get("rawValue")
        log.trace("   Adding raw value: %s" % (str(rawValue)))
        derivedVal = derivedVal + rawValue

    log.trace("   Calculated c9InCrumb as: %f" % (derivedVal))

    return {"status": "success", "value":derivedVal}

# This procedure calculates the C9 and C2= content in Rx-2 from the R1 C9 and C2= lab data and the R2 (overall) C9 and C2= lab data. 
# This procedure is started by a rule looking for a new R2 (overall) C2= value.
def rx2c2(dataDictionary):
    historyProvider='XOMhistory'
    log.trace("In derived value callback rx2c2() - the data dictionary is: %s" % (str(dataDictionary)))

    if len(dataDictionary) != 6:
        txt = "The data dictionary for the derived callback rx2c2() requires exactly 6 elements: %s" % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary

    aDict=dataDictionary.get("R1-C2-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing related data named: R1-C2-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r1c2=aDict.get("rawValue", 0.0)
    
    aDict=dataDictionary.get("R2-C2-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing related data named: R2-C2-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r2c2=aDict.get("rawValue", 0.0)

    aDict=dataDictionary.get("R1-C9-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing related data named: R1-C9-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r1c9=aDict.get("rawValue", 0.0)

    aDict=dataDictionary.get("R2-C9-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing related data named: R2-C9-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r2c9=aDict.get("rawValue", 0.0)

    aDict=dataDictionary.get("RX2-C9-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing trigger data named: RX2-C9-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    rx2c9=aDict.get("rawValue", 0.0)

    aDict=dataDictionary.get("DC2-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c2() is missing related data named: DC2-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    sampleTime=aDict.get("sampleTime", 0.0)

    # Fetch the time weighted average in a 10 minute around the sample time of DC2-LAB-DATA.  (This is the lab datum that was passed from the 
    # G2 rule to the rx-property-incremental-calc() procedure).  I'm not entirely certain if I should use the report time or the sample time.
    startTime = sampleTime - datetime.timedelta(minutes=5)
    endTime = sampleTime + datetime.timedelta(minutes=5)
    tagPath="[%s]Site/RLA3/VRF911R1" % (historyProvider)
    ds=system.tag.queryTagHistory(paths=[tagPath], startDate=startTime, endDate=endTime, returnSize=1, aggregationMode="Average")

    polysplit=ds.getValueAt(0,1)

    log.trace("  rx2c2() is using r1c2=%f, r2c2=%f, r1c9=%f, r2c9=%f, rx2c9=%f, polysplit:%f" % (r1c2, r2c2, r1c9, r2c9, rx2c9, polysplit))
    
    rx2c2 = (r2c9 - polysplit * r1c9) / (1.0 - polysplit)

    # Calculate the Rx-2 C2= based on the lab data. Here, the result is on a C9 basis.
    rx2c2 = (r2c2 * (1.0 - r2c9/100.0) - polysplit * r1c2 * (1.0 - r1c9 / 100.0)) / (1.0 - polysplit)

    # Convert the Rx-2 C2= to a C9 free basis to be consistent with lab data.
    rx2c2 = rx2c2 / (1.0 - rx2c9 / 100.0)
    
    log.trace("  rx2c2() calculated rx2c2 as: %s" % (str(rx2c2)))

    return {"status": "success", "value":rx2c2}


# This procedure calculates the C9 and C2= content in Rx-2 from the R1 C9 and C2= lab data and the R2 (overall) C9 and C2= lab data. 
# This procedure is started by a rule looking for a new R2 (overall) C2= value.
def rx2c9(dataDictionary):    
    log.trace("In derived value callback rx2c9() - the data dictionary is: %s" % (str(dataDictionary)))

    if len(dataDictionary) != 3:
        txt = "The data dictionary for the derived callback rx2c9() requires exactly 3 elements: %s" % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    
    # The derived value framework is responsible for packaging the trigger and related values into the data dictionary.
    aDict=dataDictionary.get("R1-C9-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c9() is missing related data named: R1-C9-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r1c9=aDict.get("rawValue", 0.0)

    aDict=dataDictionary.get("R2-C9-LAB-DATA", None)
    if aDict == None:
        txt = "Derived value callback rx2c9() is missing related data named: R2-C9-LAB-DATA, the data dictionary is: %s " % (str(dataDictionary))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    r2c9=aDict.get("rawValue", 0.0)

    qv=system.tag.read("Site/RLA3/VRF911R1")
    if not(qv.quality.isGood()):
        txt = "Derived value callback rx2c9() requires VRF911R1 (Polysplit), which is not good: %s" % (str(qv))
        log.error(txt)
        returnDictionary={"status":"Error", "value":None, "errorMessage":txt}
        return returnDictionary
    polysplit=qv.value

    log.trace("  rx2c9() is using r1c9=%f, r2c9=%f, polysplit:%f" % (r1c9, r2c9, polysplit))
    
    rx2c9 = (r2c9 - polysplit * r1c9) / (1.0 - polysplit)
    
    log.trace("  rx2c9() calculated rx2c9 as: %s" % (str(rx2c9)))

    return {"status": "success", "value":rx2c9}
