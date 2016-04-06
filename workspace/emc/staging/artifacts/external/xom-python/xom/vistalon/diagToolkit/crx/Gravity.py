
import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    rampCat = -0.5

    tagName = "[%s]Site/CRX/GAIN-CAT-C3" % (provider)
    gainCat = system.tag.read(tagName)
    if not (gainCat.quality.isGood()):
        log.error("In Gravity.calculate() - the gainCat is bad (%s) %s" % (tagName, str(gainCat.quality)))
        return textRecommendation,recommendations
    gainCat = gainCat.value
    
    tagName = "[%s]DiagnosticToolkit/CRX/VRF403Z/sp/value" % (provider)
    mainC3Sp = system.tag.read(tagName)
    if not (mainC3Sp.quality.isGood()):
        log.error("In Gravity.calculate() - the mainC3Sp is bad (%s) %s" % (tagName, str(mainC3Sp.quality)))
        return textRecommendation,recommendations
    mainC3Sp = mainC3Sp.value

    log.trace("The Gravity.calculate() used gainCat=%s, mainC3Sp=%s" % (str(gainCat), str(mainC3Sp)))

    print "***** NEED TO DO SOMETHING TO BYPASS OUTPUT LIMITS *****"

    recommendations.append({"QuantOutput":"VCF000R1_TARGET", "Value": rampCat})
    recommendations.append({"QuantOutput":"VRC023_TARGET", "Value": (gainCat / 100.0) * rampCat * mainC3Sp})


    return textRecommendation,recommendations