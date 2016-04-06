# Copyright 2015 ILS Automation. All rights reserved.
'''
A taper resulting from a given feed source rises from the scallop depth of the previous zone to the taper tip from the current feed source.  
The tapers are uniform when each rises to the same height, independent of depth.  Thus the target rise of each taper is the difference between 
average height for both tapers and the depth prior to that taper.  Since rise is assumed directly proportional to feed, the feed change to 
correct the rise error is also directly proportional to the rise error to be corrected.  The constant of proportionality is dynamic -- it is 
the negative of the current ratio of feed to rise.     
'''
# from ils.diagToolkit.util import outputMessage

import system
from ils.blt import functions
def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")
    textRecommendation=""
    recommendations=[]
    log.info("In SwapFrontFeedsOutputs.calculate()")
   
    tagName = "[%s]DiagnosticToolkit/CRX/Zone2/HeightAtInlet" % (provider)
    height2 = system.tag.read(tagName)
    if not (height2.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - height2 is bad (%s) %s" % (tagName, str(height2.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/Zone3/HeightAtInlet" % (provider)
    height3 = system.tag.read(tagName)
    if not (height3.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - height3 is bad (%s) %s" % (tagName, str(height3.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/Zone1/DepthAtOutlet" % (provider)
    depth1 = system.tag.read(tagName)
    if not (depth1.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - depth1 is bad (%s) %s" % (tagName, str(depth1.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/Zone2/DepthAtOutlet" % (provider)
    depth2 = system.tag.read(tagName)
    if not (depth2.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - depth2 is bad (%s) %s" % (tagName, str(depth2.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/Zone2/SdstrmMonomerFlow" % (provider)
    sdstrm1PV = system.tag.read(tagName)
    if not (sdstrm1PV.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - sdstrm1PV is bad (%s) %s" % (tagName, str(sdstrm1PV.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/Zone3/SdstrmMonomerFlow" % (provider)
    sdstrm2PV = system.tag.read(tagName)
    if not (sdstrm2PV.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - sdstrm2PV is bad (%s) %s" % (tagName, str(sdstrm2PV.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/VRF214/sp/value" % (provider)
    sdstrm1SP = system.tag.read(tagName)
    if not (sdstrm1SP.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - sdstrm1SP is bad (%s) %s" % (tagName, str(sdstrm1SP.quality)))
        return textRecommendation,recommendations

    tagName = "[%s]DiagnosticToolkit/CRX/VRF224/sp/value" % (provider)
    sdstrm2SP = system.tag.read(tagName)
    if not (sdstrm2SP.quality.isGood()):
        log.error("In SwapFrontFeedsOutputs.calculate() - sdstrm2SP is bad (%s) %s" % (tagName, str(sdstrm2SP.quality)))
        return textRecommendation,recommendations

    avgHeight = functions.average(height2.value,height3.value)
   
    log.trace("In SwapFrontFeedsOutputs.calculate().  Inputs are: ")
    log.trace("  height2 and height3 are %s and %s" % (str(height2.value), str(height3.value)))
    log.trace("  depth1 and depth2 are %s and %s" % (str(depth1.value), str(depth2.value)))
    log.trace("  sdstrm1PV and sdstrm1SP are %s and %s" % (str(sdstrm1PV.value), str(sdstrm1SP.value)))
    log.trace("  sdstrm2PV and sdstrm2SP are %s and %s" % (str(sdstrm2PV.value), str(sdstrm2SP.value)))
    log.trace("  calculated avgHeight is %s" % (str(avgHeight)))

    risePV1 = height2.value - depth1.value
    riseSP1 = avgHeight - depth1.value
    deltaFd1 =  -sdstrm1PV.value * (risePV1 - riseSP1) / risePV1
   
    log.infof("deltaFd_1 has the value %s",deltaFd1)
   
    risePV2 = height3.value - depth2.value
    riseSP2 = avgHeight - depth2.value
    deltaFd2 =  -sdstrm2PV.value * (risePV2 - riseSP2) / risePV2
   
    log.infof("deltaFd_2 has the value %s",deltaFd2)
   
    deltaFd = min(abs(deltaFd1),abs(deltaFd2))
   
    log.infof("deltaFd has the value %s",deltaFd)
   
#  Since the sign of deltaFd has been dropped, reconstruct it by the ratio term below.  Also allow for the error in the sidestream feed controller.  Output is incremental. 

    recommendations.append({"QuantOutput":"VRF214_TARGET", "Value":sdstrm1PV.value + deltaFd * (deltaFd1 / abs(deltaFd1)) - sdstrm1SP.value})
    recommendations.append({"QuantOutput":"VRF224_TARGET", "Value":sdstrm2PV.value + deltaFd * (deltaFd2 / abs(deltaFd2)) - sdstrm2SP.value})

#    log.infof("%s has updated %s outputs.","SwapFrontFeedsOutputs.calculate",lib.getBlockName(fd))
#   write the msg for the recommendation msg handler 
#   outputMessage.create(fd)
   
    return textRecommendation,recommendations