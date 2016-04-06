
'''
 Called for the DO_CAT diagnosis. 
'''

import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]

    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA/value" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        log.error("In Cat.calculate() - the PV is bad (%s) %s" % (tagName, str(pv.quality)))
        return textRecommendation,recommendations
    pv = pv.value

    tagName = "[%s]LabData/RLA3/MOONEY-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        log.error("In Cat.calculate() - the SP is bad (%s) %s" % (tagName, str(sp.quality)))
        return textRecommendation,recommendations
    sp = sp.value

    tagName = "[%s]DiagnosticToolkit/CRX/VRF403Z/sp/value" % (provider)
    mainC3sp = system.tag.read(tagName)
    if not (mainC3sp.quality.isGood()):
        log.error("In Cat.calculate() - the SP is bad (%s) %s" % (tagName, str(mainC3sp.quality)))
        return textRecommendation,recommendations
    mainC3sp = mainC3sp.value

    #TODO Need to read this from somewhere
    tagName = "[%s]Site/CRX/GAIN-CAT-C3" % (provider)
    gainCat = system.tag.read(tagName)
    if not (gainCat.quality.isGood()):
        log.error("In Cat.calculate() - the gainCat is bad (%s) %s" % (tagName, str(gainCat.quality)))
        return textRecommendation,recommendations
    gainCat = gainCat.value

    if sp <= 0.01:
        log.error("In Cat.calculate() - the Mooney target is incorrectly defined as %s" % (str(sp))) 
        return textRecommendation,recommendations

    deltaCat = 100.0 * (((pv/sp) ^ 0.333) - 1.0);
    deltaC3 = (gainCat / 100.0) * deltaCat *  mainC3sp;
   
    log.info("In Cat.calculate() - Delta CAT: %s, Delta C3: %s" % (str(deltaCat),str(deltaC3)))

    recommendations.append({"QuantOutput":"VCF000R1_TARGET", "Value": deltaCat})
    recommendations.append({"QuantOutput":"VRC023_TARGET", "Value": deltaC3})

    return textRecommendation,recommendations
