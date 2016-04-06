# Copyright 2015 ILS Automation. All rights reserved.

'''
     Output is the change in the main ethylene flow and R2 ethylene flow, both in KLB/hr, to get Delta ML back on target.  The Delta ML gain 
     is percent change in the current ethylene flow to change the Delta ML 1 point. 
'''

import ils.recipeToolkit.gains as gains
import system
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    textRecommendation=""
    recommendations=[]

    unit="RLA3"
    family=unit
    
    qv = system.tag.read("[%s]Site/%s/Grade/Grade" % (provider,unit))
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - unable to read the current grade: %s" % (__name__, str(qv.quality)), log)
        return textRecommendation, recommendations
    grade=qv.value
    
    tagName="[%s]LabData/RLA3/DML-LAB-DATA-SQC/target" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    sp=qv.value
    
    tagName="[%s]LabData/RLA3/DML-FILTERED-VALUE/filteredValue" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    pv=qv.value
    
    # The alias for this is VRC062
    tagName="[%s]DiagnosticToolkit/CSTR/VRF402RP-1/sp/value" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - mainC2 is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    mainC2=qv.value
    
    # The alias for this is VRC262
    tagName="[%s]DiagnosticToolkit/CSTR/VRF502RP-1/sp/value" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - r2C2 is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    r2C2=qv.value
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations
    
    gainR1c2 = gains.get(family, grade, "DML-R1C2", database)
    gainR2c2 = gains.get(family, grade, "DML-R2C2", database)
    gainR1c3 = gains.get(family, grade, "DML-R1C3", database)
    gainNh3 = gains.get(family, grade, "DML-NH3", database)
    gainH2 = gains.get(family, grade, "DML-H2", database)
    gainR1enb = gains.get(family, grade, "DML-R1ENB", database)
    gainR2enb = gains.get(family, grade, "DML-R2ENB", database)
    
    log.trace("in CstrDML.calculate:")
    log.trace("   R1C2 gain: %s" % (str(gainR1c2)))
    log.trace("   R2C2 gain: %s" % (str(gainR2c2)))
    log.trace("   R1C3 gain: %s" % (str(gainR1c3)))
    log.trace("   NH3 gain: %s" % (str(gainNh3)))
    log.trace("   H2 gain: %s" % (str(gainH2)))
    log.trace("   R1C9 gain: %s" % (str(gainR1enb)))
    log.trace("   R2C9 gain: %s" % (str(gainR2enb)))
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use PV=%s, SP=%s" % (__name__, str(pv), str(sp)), log)
     
    error = (pv - sp)
    log.trace("In %s - pv is %s, sp is %s, error is %s" % (__name__, str(pv), str(sp), str(error)))    
    
    # This was a parameter in G2 so I made a site tag in Ignition.  Nobody else uses it, I probably could have just made a constant
    tagName="[%s]Site/CSTR/DML-ERROR-RATE-LIMIT" % (provider)
    qv = system.tag.read(tagName)
    if not (qv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - DML Error Rate Limit is bad (%s) %s" % (__name__, tagName, str(qv.quality)), log)
        return textRecommendation, recommendations
    dmlErrorRateLimit=qv.value

    #  Limit error to xx Mooney Points thus allowing a change of only 1.0 point in Polysplit as set on a per grade basis via recipe. 
    if error > dmlErrorRateLimit:
        error = dmlErrorRateLimit

    if error < -1.0 * dmlErrorRateLimit:
        error = -1.0 * dmlErrorRateLimit 
    
    #  Calculate the change in R1 and R2 C2= flows [Klb/hr]
    recommendations.append({"QuantOutput": "VRF402RP_TARGET", "Value": gainR1c2 * error / 100.0 * mainC2})
    recommendations.append({"QuantOutput": "VRF502RP_TARGET", "Value": gainR2c2 * error / 100.0 * r2C2})
    
    #  Calculate the change in R1 C3=, NH3/V and H2/V [%]
    recommendations.append({"QuantOutput": "VRF403R1_TARGET", "Value": gainR1c3 * error})
    recommendations.append({"QuantOutput": "VCF211R1_TARGET", "Value": gainNh3 * error})
    recommendations.append({"QuantOutput": "VCF120R1_TARGET", "Value": gainH2 * error})
    
    #  Calculate the change in R1 ENB and R2 ENB [%] 
    recommendations.append({"QuantOutput": "VRF409R1_TARGET", "Value": gainR1enb * error})
    recommendations.append({"QuantOutput": "VRF209R1_TARGET", "Value": gainR2enb * error})
    log.trace("   calculated the following recommendations: %s" % (str(recommendations)))

    return textRecommendation,recommendations
