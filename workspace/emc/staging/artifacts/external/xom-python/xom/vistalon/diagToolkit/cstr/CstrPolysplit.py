# Copyright 2015 ILS Automation. All rights reserved.

'''
  Output is the change in the main ethylene flow and R2 ethylene flow, both in KLB/hr, to move Polysplit back toward the target.  The polysplit gain has the units weight percent of current flow per 0.5 polysplit percent change  
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
        postApplicationMessage(application, QUEUE_ERROR, "In diagnostic calculation method: %s - unable to read the current grade: %s" % (__name__, str(grade.quality)))
        return textRecommendation, recommendations
    grade=grade.value
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "In diagnostic calculation method: %s - grade (%s) does not exist in the gain table" % (__name__, str(grade)))
        return textRecommendation, recommendations
    
    log.info("In %s: the grade is: %s" % (__name__, str(grade)))

    tagName = "[%s]LabData/RLA3/POLYSPLIT-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp = sp.getValue()
    
    tagName = "[%s]LabData/RLA3/POLYSPLIT-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv = pv.getValue()
    
    # The alias for this tag is "S88-RC-MAIN/C2-RATE-RAMPER"
    tagName = "[%s]SFC IO/Rate Change/VRF402RP-2/value" % (provider)
    currentR1C2 = system.tag.read(tagName)
    if not (currentR1C2.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - R1C2 is bad (%s) %s" % (__name__, tagName, str(currentR1C2.quality)), log)
        return textRecommendation,recommendations
    
    # The alias for this tag is "S88-RC-MAIN/C2-R2-RAMPER"
    tagName = "[%s]SFC IO/Rate Change/VRF502RP-2/value" % (provider)
    currentR2C2 = system.tag.read(tagName)
    if not (currentR2C2.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - R2C2 is bad (%s) %s" % (__name__, tagName, str(currentR2C2.quality)), log)
        return textRecommendation,recommendations

    postApplicationMessage(application, QUEUE_INFO, "%s will use data of PV = %s, SP = %s, R1_C2 = %s, and R2_C2 = %s." % (__name__, str(pv), str(sp), str(currentR1C2), str(currentR2C2)), log)
    
    gainR1c2 = gains.get(family, grade, "PS-R1C2", database)
    gainR2c2 = gains.get(family, grade, "PS-R2C2", database)

    log.trace("In %s..." % (__name__, ))
    log.trace("   PS-R1C2 gain: %s" % (str(gainR1c2)))
    log.trace("   PS-R2C2 gain: %s" % (str(gainR2c2)))

    delta = (pv - sp)
    log.trace("   pv=%s and sp=%s for delta=%s" % (str(pv), str(sp), str(delta)))
    
    #  Limit error to 1.0 Polysplit Points 
    if delta > 1.0:
        delta = 1.0

    if delta < -1.0:
        delta = -1.0

    #  Calculate the change in R1 and R2 C2= flows. [Klb/hr] 
    delta1 = gainR1c2 / 0.5 * delta / 100.0 * currentR1C2.value
    delta2 = gainR2c2 / 0.5 * delta / 100.0 * currentR2C2.value
    
    recommendations.append({"QuantOutput":"VRF402RP_TARGET","Value":delta1})
    recommendations.append({"QuantOutput":"VRF502RP_TARGET","Value":delta2})
    log.trace("   calculated the following recommendations: %s" % (str(recommendations)))

    return textRecommendation,recommendations