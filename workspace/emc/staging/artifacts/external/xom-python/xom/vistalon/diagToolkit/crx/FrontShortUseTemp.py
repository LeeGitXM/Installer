# Copyright 2015 ILS Automation. All rights reserved.

import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    log.info("In FrontShortUseTempOutputs.calculate()")

    tagName = "[%s]DiagnosticToolkit/CRX/CRX-HB-8/value" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        log.error("In FrontShortUseTempOutputs.calculate() - pv is bad (%s) %s" % (tagName, str(pv.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]Site/CRX/FRNT-LNGTH-TARGET" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        log.error("In FrontShortUseTempOutputs.calculate() - sp is bad (%s) %s" %(tagName, str(sp.quality)))
        return textRecommendation,recommendations
    
    tagName = "[%s]Site/CRX/GAIN-LNGTH-TEMP" % (provider)
    K = system.tag.read(tagName)
    if not (K.quality.isGood()):
        log.error("In FrontShortUseTempOutputs.calculate() - K is bad (%s) %s" % (tagName, str(K.quality)))
        return textRecommendation,recommendations
    
    tagName = "[%s]Site/CRX/GAIN-CAT-TEMP" % (provider)
    KCat = system.tag.read(tagName)
    if not (KCat.quality.isGood()):
        log.error("In FrontShortUseTempOutputs.calculate() - KCat is bad (%s) %s" %(tagName, str(KCat.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]Site/CRX/GAIN-CAT-C3" % (provider)
    KC3 = system.tag.read(tagName)
    if not (KC3.quality.isGood()):
        log.error("In FrontShortUseTempOutputs.calculate() - KC3 is bad (%s) %s" %(tagName, str(KC3.quality)))
        return textRecommendation,recommendations

    deltam =(pv.value - sp.value)
   
    log.trace("PV value is %s with SP value at %s" % (str(pv.value),str(sp.value)))
    log.trace("Gains are:  K = %s, KCat= %s and KC3 = %s" % (str(K.value),str(KCat.value),str(KC3.value)))
    log.trace("Error term for output is: %s" % (str(deltam)))
   
    recommendations.append({"QuantOutput":"VCC205_TARGET", "Value":deltam *  K.value})
    recommendations.append({"QuantOutput":"VCF000R1_TARGET", "Value":deltam *  K.value * KCat.value})
    recommendations.append({"QuantOutput":"VRC023_TARGET", "Value":deltam *  K.value * KCat.value * KC3.value})

#   outputMessage.create(fd)

    return textRecommendation,recommendations