# Copyright 2015 ILS Automation. All rights reserved.

import system

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    log.info("In C2ChangeFeeds.calculate()")

    tagName = "[%s]DiagnosticToolkit/CRX/CRX-HB-9/value" % (provider)
    PV = system.tag.read(tagName)
    if not (PV.quality.isGood()):
        log.error("In C2ChangeFeeds.calculate() - PV is bad (%s) %s" % (tagName, str(PV.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]Site/CRX/FRNT-AVG-C2-TARGET" % (provider)
    SP = system.tag.read(tagName)
    if not (SP.quality.isGood()):
        log.error("In C2ChangeFeeds.calculate() - SP is bad (%s) %s" % (tagName, str(SP.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]Site/CRX/GAIN-FRNT-C2-MAIN-FD" % (provider)
    K_MFd = system.tag.read(tagName)
    if not (K_MFd.quality.isGood()):
        log.error("In C2ChangeFeeds.calculate() - K_MFd is bad (%s) %s" % (tagName, str(K_MFd.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]Site/CRX/GAIN-FRNT-C2-SDSTRM-FD" % (provider)
    K_SSFd = system.tag.read(tagName)
    if not (K_SSFd.quality.isGood()):
        log.error("In C2ChangeFeeds.calculate() - K_SSFd is bad (%s) %s" % (tagName, str(K_SSFd.quality)))
        return textRecommendation,recommendations

    error = PV.value - SP.value
 
    log.trace("In C2ChangeFeeds.calculate().  Inputs are: ")
    log.trace("  PV and SP are %s and %s" % (str(PV.value), str(SP.value)))
    log.trace("  Gains are: K_MFd and K_SSFd are %s and %s" % (str(K_MFd.value), str(K_SSFd.value)))
    log.trace("  calculated error is %s" % (str(error)))

    recommendations.append({"QuantOutput":"VRC032_TARGET", "Value":K_MFd.value * error})
    recommendations.append({"QuantOutput":"VRC232_TARGET", "Value":-1.0 * K_MFd.value * error})
    recommendations.append({"QuantOutput":"VRF214_TARGET", "Value":K_SSFd.value * error})
    recommendations.append({"QuantOutput":"VRF224_TARGET", "Value":K_SSFd.value * error})

#    system.util.invokeAsynchronous(message.insert(application,"%s has updated %s outputs."%("C2ChangeFeeds.calculate",lib.getBlockName(fd)),str(EMCConstants.INFORMATION),fd)
#    outputMessage.create(fd)
    return textRecommendation,recommendations
