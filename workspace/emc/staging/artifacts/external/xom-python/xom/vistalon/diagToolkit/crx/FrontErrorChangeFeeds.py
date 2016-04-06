
#  This procedure used by SF-1, SF-3a and SF-3d. 
  
import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    log.info("In FrontErrorChangeFeeds.calculate()")

    tagName = "[%s]Site/CRX/FRNT-LNGTH-TARGET" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - sp is bad (%s) %s" %(tagName, str(sp.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/CRX-HB-8/value" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - pv is bad (%s) %s" % (tagName, str(pv.quality)))
        return textRecommendation,recommendations
    
    tagName = "[%s]DiagnosticToolkit/CRX/VRF402Z/sp/value" % (provider)
    mainc2 = system.tag.read(tagName)
    if not (mainc2.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - mainc2 is bad (%s) %s" % (tagName, str(mainc2.quality)))
        return textRecommendation,recommendations
    
    tagName = "[%s]DiagnosticToolkit/CRX/VRF214/sp/value" % (provider)
    sdstrm1 = system.tag.read(tagName)
    if not (sdstrm1.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - sdstrm1 is bad (%s) %s" %(tagName, str(sdstrm1.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/VRF224/sp/value" % (provider)
    sdstrm2= system.tag.read(tagName)
    if not (sdstrm2.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - sdstrm2 is bad (%s) %s" %(tagName, str(sdstrm2.quality)))
        return textRecommendation,recommendations
    
    tagName = "[%s]DiagnosticToolkit/CRX/VRF503R-2/sp/value" % (provider)
    sdstrmc3c2 = system.tag.read(tagName)
    if not (sdstrmc3c2.quality.isGood()):
        log.error("In FrontErrorChangeFeeds.calculate() - sdstrmc3c2 is bad (%s) %s" %(tagName, str(sdstrmc3c2.quality)))
        return textRecommendation,recommendations

    if abs(pv.value) < 0.01:
        return textRecommendation, recommendations

    deltam =  -(pv.value - sp.value) / pv.value
    log.trace("PV value is %s with SP value at %s" % (str(pv.value),str(sp.value)))
    log.trace("Error term for output is: %s" % (str(deltam)))
   
    recommendations.append({"QuantOutput":"VRC032_TARGET", "Value":deltam * mainc2.value})
    recommendations.append({"QuantOutput":"VRF214_TARGET", "Value":deltam * sdstrm1.value})
    recommendations.append({"QuantOutput":"VRF224_TARGET", "Value":deltam * sdstrm2.value})
    recommendations.append({"QuantOutput":"VRC232_TARGET", "Value":-1.0 * deltam * mainc2.value})
    recommendations.append({"QuantOutput":"VRC023_TARGET", "Value":deltam * mainc2.value * sdstrmc3c2.value})

    return textRecommendation, recommendations
