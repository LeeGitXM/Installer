# Copyright 2015 ILS Automation. All rights reserved.

'''
  Output is the percentage change in the main propylene flow and the percentage change in the R2 propylene flow required to get the delta ethylene back on target.   
'''

import system
import ils.recipeToolkit.gains as gains
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    textRecommendation=""
    recommendations=[]

    unit="RLA3"
    family=unit
    grade = system.tag.read("[%s]Site/%s/Grade/Grade" % (provider,unit))
    if not (grade.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - unable to read the current grade: %s" %(__name__, str(grade.quality)), log)
        return textRecommendation, recommendations

    grade=grade.value
    log.info("In %s, the grade is: %s" % (__name__, str(grade)))
        
    tagName="[%s]LabData/RLA3/DC2-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    
    tagName="[%s]LabData/RLA3/DC2-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations
    
    gainR1c3 = gains.get(family, grade, "DC2-R1C3", database)
    gainR2c3 = gains.get(family, grade, "DC2-R2C3", database)
    
    log.trace("In %s:" % (__name__)) 
    log.trace("   DC2-R1C3 gain: %s" % (str(gainR1c3)))
    log.trace("   DC2-R2C3 gain: %s" % (str(gainR2c3)))
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use main propylene gain = %s, Rx 2 propylene gain = %s, PV = %s and SP = %s" % (__name__, str(gainR1c3),str(gainR2c3),str(pv),str(sp)),log)
                                   
    delta = (pv.value - sp.value)
    log.trace("   pv is %s, sp is %s, delta is %s" % (str(pv), str(sp), str(delta)))  
    
    recommendations.append({"QuantOutput": "VRF403R1_TARGET", "Value": gainR1c3 / 0.5 * delta})
    recommendations.append({"QuantOutput": "VRF503R1_TARGET", "Value": gainR2c3 / 0.5 * delta})

    log.trace("   calculated the following recommendations: %s" % (str(recommendations)))

    return textRecommendation,recommendations