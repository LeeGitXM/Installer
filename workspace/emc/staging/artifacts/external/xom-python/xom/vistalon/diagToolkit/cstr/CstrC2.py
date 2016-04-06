# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is percentage change in Main C3 flow required to get overall C2 back on target.  This will appear as incremental output to the destination, a percentage change tag.  
'''

import system
import ils.recipeToolkit.gains as gains
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application,fd, provider, database):
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
    log.info("In %s, the grade is: %s" % (__name__, str(grade)))

    tagName="[%s]LabData/RLA3/ETHYLENE-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation, recommendations
    pv=pv.value
    
    tagName="[%s]LabData/RLA3/C2-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR,"%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation, recommendations
    sp=sp.value
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations
    
    gainC3 = gains.get(family, grade, "C2-C3", database)
    gainR2c3 = gains.get(family, grade, "C2-R2C3", database)
    gainCat = gains.get(family, grade, "C2-CAT", database)
    gainH2 = gains.get(family, grade, "C2-H2", database)
    gainC9 = gains.get(family, grade, "C2-C9", database)
    
    log.trace("C2-C3 gain: %s" % (str(gainC3)))
    log.trace("C2-R2C3 gain: %s" % (str(gainR2c3)))
    log.trace("C2-CAT gain: %s" % (str(gainCat)))
    log.trace("C2-H2 gain: %s" % (str(gainH2)))
    log.trace("C2-C9 gain: %s" % (str(gainC9)))

    delta = (pv - sp)
    log.trace("Delta between PV and SP is: %s" % (str(delta)))

    recommendations.append({"QuantOutput": "VRF403R1_TARGET", "Value": gainC3 * delta})
    recommendations.append({"QuantOutput": "VCF000R1_TARGET", "Value": gainCat * delta})
    recommendations.append({"QuantOutput": "VCF120R1_TARGET", "Value": gainH2 * delta})
    recommendations.append({"QuantOutput": "VRF409R1_TARGET", "Value": gainC9 * delta})

    tagName="[%s]Recipe/Local/SPLIT_FEED_GRADE" % (provider)
    if system.tag.read(tagName).getValue():
        r2C3PctChg=gainC3 * delta
    else:
        r2C3PctChg=gainR2c3 * delta
        
    recommendations.append({"QuantOutput": "VRF503R1_TARGET", "Value": r2C3PctChg})
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use fb data of gain=%s, %s, %s, ff data of "\
                           "gain = %s or %s as appropriate, PV = %s, SP = %s" % 
                           (__name__, str(gainC3), str(gainC9), str(gainR2c3), str(gainCat), str(gainH2), 
                            str(pv), str(sp)), log)
    
    log.trace("%s calculated the following recommendations: %s" % (__name__, str(recommendations)))

    return textRecommendation,recommendations
