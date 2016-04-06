# Copyright 2015 ILS Automation. All rights reserved.

'''
  Output is the percentage change in the R1 ENB flow and the percentage change in the R2 ENB flow required to get the delta ENB back on target. 
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
        postApplicationMessage(application, QUEUE_ERROR, "%s - unable to read the current grade: %s" % (__name__, str(grade.quality)), log)
        return textRecommendation, recommendations

    grade=grade.value
    log.info("In %s - the grade is: %s" % (__name__, str(grade)))


    tagName="[%s]LabData/RLA3/DENB-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName="[%s]LabData/RLA3/DC9-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp=sp.getValue()

    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations

    gainR1enb = gains.get(family, grade, "DENB-R1ENB", database)
    gainR2enb = gains.get(family, grade, "DENB-R2ENB", database)
    
    log.trace("In %s:" % (__name__))
    log.trace("   DENB-R1ENB gain: %s" % (str(gainR1enb)))
    log.trace("   DENB-R2ENB gain: %s" % (str(gainR2enb)))

    postApplicationMessage(application, QUEUE_INFO, "%s will use R1 ENB gain = %s, R2 ENB gain = %s, PV = %s and SP = %s." % (__name__, str(gainR1enb),str(gainR2enb),str(pv),str(sp)), log)
    delta = (pv - sp)
    log.trace("   pv is %s, sp is %s, delta is %s" % (str(pv), str(sp), str(delta)))
    
    recommendations.append({"QuantOutput": "VRF409R1_TARGET", "Value": gainR1enb / 0.2 * delta})
    recommendations.append({"QuantOutput": "VRF209R1_TARGET", "Value": gainR2enb / 0.2 * delta})

    log.trace("   calculated the following recommendations: %s" % (str(recommendations)))

    return textRecommendation,recommendations
