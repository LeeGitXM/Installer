'''
    Output is % change in either H2 or cat flow req'd to change ml target.  This will appear as incremental output to
    the destination, a % change tag.
'''

import system
import ils.recipeToolkit.gains as gains
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    textRecommendation=""
    recommendations=[]
    opt = "None"
    minerr = 0.5
    unit="RLA3"
    family=unit
    
    qv = system.tag.read("[%s]Site/%s/Grade/Grade" % (provider,unit))
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - unable to read the current grade: %s" % (__name__, str(qv.quality)), log)
        return textRecommendation, recommendations
    grade=qv.value
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations
    
    
    # Get the current time using Java's Date class
    from java.util import Date
    now = Date()
    
    qv = system.tag.read("[%s]Site/%s/Grade/catInHours" % (provider,unit))
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - unable to read the catInHours: %s" % (__name__, str(qv.quality)), log)
        return textRecommendation, recommendations
    gradeRunHours = qv.getValue()
    
    # These are memory tags so don't need to check quality
    hoursToAverageProductionMl=system.tag.read("[%S]Site/RLA3/HOURS-TO-AVERAGE-PRODUCTION-ML").getValue()
    hoursToOffsetRxMl=system.tag.read("[%S]Site/RLA3/HOURS-TO-OFFSET-RX-ML").getValue()

    rangeHours= min(gradeRunHours, hoursToAverageProductionMl)
    tagName="[%s]LabData/RLA3/PROD-ML-LAB-DATA/value" % (provider)
    ds = system.tag.queryTagHistory(paths=[tagName],aggregationMode="Average", rangeHours=-1*rangeHours, endDate=now)
    prodAvg=ds.getValueAt(0,0)
    
    #TODO Need to figure out this
    prodStdDev = 0.001
    
    rangeHours= min(gradeRunHours, hoursToOffsetRxMl)
    tagName="[%s]LabData/RLA3/MOONEY-LAB-DATA/value" % (provider)
    ds = system.tag.queryTagHistory(paths=[tagName],aggregationMode="Average", rangeHours=-1*rangeHours, endDate=now)
    rxAvg=ds.getValueAt(0,0)
    
    #TODO Need to figure out this
    rxStdDev = 0.001
    
    bias = 1.0
    maxrxsd = 1.0
    ctlgain = 1.0
    rxMooneyOk = True
    
    # prodAvg = averageOverTime("[]LabData/RLA3/PROD-ML-LAB-DATA/value",min(hoursToAvgProdMlGda,gradeRunHours)*3600)
    # prodStdDev = standardDeviationOverTime("[]LabData/RLA3/PROD-ML-LAB-DATA/value",min(hoursToAvgProdMlGda,gradeRunHours)*3600)
    # rxAvg = averageOverTime("[]LabData/RLA3/MOONEY-LAB-DATA",min(hoursToOffsetRxMlGda + hoursToAvgProdMlGda,gradeRunHours)*3600,min(hoursToOffsetRxMlGda,gradeRunHours)*3600)      
    # rxStdDev = standardDeviationOverTime("[]LabData/RLA3/MOONEY-LAB-DATA",min(hoursToOffsetRxMlGda + hoursToAvgProdMlGda,gradeRunHours)*3600,min(hoursToOffsetRxMlGda,gradeRunHours)*3600)
    # bias = mooneyBiasGda
    # maxrxsd = lib.maxMstVar(round(system.tag.read("[]DiagnosticToolkit/CSTR/RLA3-Current-Grade/value").getValue()))
    # ctlgain = lib.mlBstBiasCtlGain(round(system.tag.read("[]DiagnosticToolkit/CSTR/RLA3-Current-Grade/value").getValue()))
    # maxrxsd = lib.maxMstVar(3666)
    # ctlgain = lib.mlBstBiasCtlGain(3666)

    # Read values from lab data 
    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA-SQC/upperSQCLimit" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - rxLimitHi is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    rxLimHi = qv.value
    
    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA-SQC/lowerSQCLimit" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - rxLimitLo is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    rxLimLo = qv.value

    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA-SQC/target" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - rxSP is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    rxSp = qv.value
      
    tagName = "[%s]LabData/RLA3/PROD-ML-LAB-DATA-SQC/target" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - prodSP is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    prodSp = qv.value

    tagName = "[%s]LabData/RLA3/PROD-ML-LAB-DATA/value" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - prodPV is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    prodPv = qv.value

    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA/value" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - rxPV is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    rxPv = qv.value
    
    # Calc various control terms.  Zero outputs for increment calculations.     Set absolute outputs to current value. If set to zero, then incorrect recommendation will be made. 
    rxErr = rxAvg - rxSp
    prodErr = prodAvg - prodSp
    ctlOutput = ctlgain * round(rxErr - prodErr)
    
    #     Make a bunch of messages to see what type of recommendation to make. Helps track problems in future review. 
    log.trace("Avg Product Mooney is %s." % (str(prodAvg)))
    log.trace("Product Mooney target is %s." % (str(prodSp)))
    log.trace("Avg Reactor Mooney is %s." % (str(rxAvg)))
    log.trace("Reactor Mooney target is %s." % (str(rxSp)))
    log.trace("Prod Mooney std dev is %s." % (str(prodStdDev)))
    log.trace("Reactor Mooney std dev is %s." % (str(rxStdDev)))
#   log.trace("Number of Prod Ml datapoints is [the number of history datapoints in prod-ml-lab-data during the last min (hours-to-avg-prod-ml-gda , grade-run-hours) hours]."
#   log.trace("Number of Rx Ml datapoints is [the number of history datapoints in mooney-lab-data between min (hours-to-offset-rx-ml-gda + hours-to-avg-prod-ml-gda , grade-run-hours) hours ago and min (hours-to-offset-rx-ml-gda , grade-run-hours) hours ago]."
    log.trace("Bias of %s points." % (str(bias)))
    log.trace("ML High Lim is %s." % (str(rxLimHi)))
    log.trace("ML Low Lim is %s." % (str(rxLimLo)))
    
    # No Out
    if abs(ctlOutput) < minerr:
        message="Less than minimum control action.  No changes are recommended now."

    # ProdCross
    elif prodErr * (prodPv - prodSp) < 0.0:
        message="The current product Ml value %s and the long term avg %s are on opposite sides of the target %s.  No output is suggested." % (str(prodPv),str(prodAvg),str(prodSp))

    # RxCross
    elif rxErr * (rxPv - rxSp) < 0.0:
        message="The current reactor Ml value %s and the long term avg %s are on opposite sides of the target %s.  No output is suggested." % (str(rxPv),str(rxAvg),str(rxSp))

    # Vary
    elif rxStdDev > maxrxsd:
        message="%s - The reactor Ml is swinging too much.  No output is suggested."

    # Rx Unknown
    elif rxMooneyOk != True:
        message="New Rx Ml Target should be %s, a change from %s.  "\
                 "Limits for Rx Mooney should be %s and %s.  "\
                 "Please download the new limits from the normal setpoint workspace,  "\
                 "A separate change for H2 to achieve this limit change is NOT suggested now because the current state of the reactor Mooney control is unknown." \
                 % (str(rxSp + ctlOutput), str(rxSp), str(rxLimLo + ctlOutput), str(rxLimHi + ctlOutput))

        recommendations.append({"QuantOutput": "ML_TARGET_HILIM", "Value": rxLimHi + ctlOutput})
        recommendations.append({"QuantOutput": "ML_TARGET_LOLIM", "Value": rxLimLo + ctlOutput})
    else:
        newRxSp = rxSp + ctlOutput
        gainCat = gains.get(family, grade, "ML-CAT", database)
        gainH2 = gains.get(family, grade, "ML-H2", database)
        gainC3 = gains.get(family, grade, "ML-C3", database)
        message="New Rx Ml Target should be %s, a change from %s.  "\
                 "Limits for Rx Mooney should be %s and %s.  "\
                 "A %s percent change in H2 is needed.  "\
                 "Both the limit change and the change for H2 required to achieve this limit change will appear on the setpoints screen." \
                 % (__name__, str(newRxSp), str(rxSp), str(rxLimLo + ctlOutput), str(rxLimHi + ctlOutput), str(-1.0 * gainH2 * ctlOutput))
        
        recommendations.append({"QuantOutput": "VCF000R1_TARGET", "Value": -1.0 * gainCat * ctlOutput})
        recommendations.append({"QuantOutput": "VCF120R1_TARGET", "Value": -1.0 * gainH2 * ctlOutput})
        recommendations.append({"QuantOutput": "VRF403R1_TARGET", "Value": -1.0 * gainC3 * ctlOutput})
        recommendations.append({"QuantOutput": "ML_TARGET_HILIM", "Value": rxLimHi + ctlOutput})
        recommendations.append({"QuantOutput": "ML_TARGET_LOLIM", "Value": rxLimLo + ctlOutput})

    postApplicationMessage(application, QUEUE_INFO, "%s - %s" % (__name__, message), log) 

    return textRecommendation,recommendations
