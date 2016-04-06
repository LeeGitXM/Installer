# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is percentage change in flows required to get MLR back on target.  Note that the MLR corrected for ML in DCS is used in this application.  
      This will appear as incremental output to the destination, a percentage change tag. Gains are change in manip var per 10 MLR points.  
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
    
    # Validate that the grade is legit
    valid = gains.validate(family, grade, database)
    if not(valid):
        postApplicationMessage(application, QUEUE_ERROR, "%s - grade (%s) does not exist in the gain table" % (__name__, str(grade)), log)
        return textRecommendation, recommendations
    
    log.info("In %s, the grade is: %s" % (__name__, str(grade)))
    
    tagName = "[%s]LabData/RLA3/MLR-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not(pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV (%s) is bad (%s)" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName = "[%s]LabData/RLA3/MLR-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not(sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP (%s) is bad (%s)" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp=sp.getValue()
    
    log.trace("In %s: using pv: %s & sp: %s as the basis for calculations" % (__name__, str(pv), str(sp)))

    gainCat = gains.get(family, grade, "MLR-CAT", database)
    gainH2 = gains.get(family, grade, "MLR-H2", database)
    gainC3 = gains.get(family, grade, "MLR-C3", database)
    gainNh3 = gains.get(family, grade, "MLR-NH3", database)
    gainAlkyl = gains.get(family, grade, "MLR-AL", database)
    gainTemp = gains.get(family, grade, "MLR-TEMP", database)
    
    log.trace(" MlrCat gain: %s" % (str(gainCat)))
    log.trace(" MlrH2 gain: %s" % (str(gainH2)))
    log.trace(" MlrC3 gain: %s" % (str(gainC3)))
    log.trace(" MlrNH3 gain: %s" % (str(gainNh3)))
    log.trace(" MlrAl gain: %s" % (str(gainAlkyl)))
    log.trace(" MlrTemp gain: %s" % (str(gainTemp)))
    
    postApplicationMessage(application, QUEUE_ERROR, "%s will use data of cat gain = %s, H2 gain = %s, C3= gain = %s, NH3 gain = %s, alkyl gain = %s, temperature gain =%s, PV = %s and SP = %s." % (__name__, str(gainCat), str(gainH2), str(gainC3), str(gainNh3), str(gainAlkyl), str(gainTemp), str(pv), str(sp)), log)

    delta = (pv - sp) / 10.0
    log.trace(" Calculated delta as: %s" % (str(delta)))
    
    recommendations.append({"QuantOutput": "VCF000R1_TARGET", "Value": gainCat * delta})
    recommendations.append({"QuantOutput": "VCF120R1_TARGET", "Value": gainH2 * delta})
    recommendations.append({"QuantOutput": "VCF211R1_TARGET", "Value": gainNh3 * delta})
    recommendations.append({"QuantOutput": "VCF262R1_TARGET", "Value": gainAlkyl * delta})
    recommendations.append({"QuantOutput": "VRF403R1_TARGET", "Value": gainC3 * delta}) 
    
    # Limit the Rx temperature changes to 1.0 FDeg 
    out5 = gainTemp * delta
    if out5 > 1.0:
        out5 = 1.0

    if out5 < -1.0:
        out5 = -1.0

    recommendations.append({"QuantOutput": "VRT700S_TARGET", "Value": out5})
    log.info("%s calculated the following recommendations: %s" % (__name__, str(recommendations)))

    return textRecommendation,recommendations
