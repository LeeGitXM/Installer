# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is percentage change in Main ENB flow required to get overall ENB back on target.  This will appear as incremental output to the destination, a percentage change tag.  
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
    
    log.info("In %s, the grade is: %s" % (__name__, str(grade)))
    
    tagName="[%s]LabData/RLA3/ENB-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName="[%s]LabData/RLA3/C9-LAB-DATA-SQC/target" % (provider)
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

    gainEnb = gains.get(family, grade, "ENB-ENB", database)
    gainCat = gains.get(family, grade, "ENB-CAT", database)
    gainH2 = gains.get(family, grade, "ENB-H2", database)
    gainC3 = gains.get(family, grade, "ENB-C3", database)
    
    log.trace("ENB-ENB gain: %s" % (str(gainEnb)))
    log.trace("ENB-CAT gain: %s" % (str(gainCat)))
    log.trace("ENB-H2 gain: %s" % (str(gainH2)))
    log.trace("ENB-C3 gain: %s" % (str(gainC3)))
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use fb gain = %s, ff gain = %s, %s or %s as appropriate, PV = %s and SP = %s." % (__name__, str(gainEnb), str(gainCat), str(gainH2), str(gainC3), str(pv), str(sp)), log)
    
    delta = (pv - sp)
    log.trace("Delta between PV and SP is: %s" % (str(delta)))
    
    recommendations.append({"QuantOutput":"VRF409R1_TARGET", "Value":gainEnb*delta})
    recommendations.append({"QuantOutput":"VCF000R1_TARGET", "Value":gainCat*delta})
    recommendations.append({"QuantOutput":"VCF120R1_TARGET", "Value":gainH2*delta})
    recommendations.append({"QuantOutput":"VRF403R1_TARGET", "Value":gainC3*delta})

    log.trace("In %s calculated the following recommendations: %s" % (__name__, str(recommendations)))

    return textRecommendation,recommendations
