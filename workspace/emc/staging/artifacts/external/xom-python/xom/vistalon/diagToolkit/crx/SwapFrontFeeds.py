# Copyright 2015 ILS Automation. All rights reserved.
from com.inductiveautomation.ignition.common.util import LogUtil
from ils.diagToolkit.util import outputMessage
from ils.blt import lib
from com.ils.blt import functions
from com.inductiveautomation.ignition.common.util import LoggerEx
from ils.diagToolkit import recommendation
from ils.queue import message
from ils.constants.enumerations import EMCConstants
def calculate(application,fd):
   log = LogUtil.getLogger("project.vistalon.crx")
   textRecommendation=""
   recommendations=[]
   debugMode = lib.getDebugMode(application)
   try:
      height2 = system.tag.read("[]Site/CRx/ZONE2/HeightAtInlet").getValue()
      height3 = system.tag.read("[]Site/CRx/ZONE3/HeightAtInlet").getValue()
      depth1 = system.tag.read("[]Site/CRx/ZONE1/DepthAtOutlet").getValue()
      depth2 = system.tag.read("[]Site/CRx/ZONE2/DepthAtOutlet").getValue()
      sdstrm1Pv = system.tag.read("[]Site/CRx/ZONE2/SdstrmMonomerFlow").getValue()
      sdstrm2Pv = system.tag.read("[]Site/CRx/ZONE3/SdstrmMonomerFlow").getValue()
      sdstrm1Sp = system.tag.read("[]Site/CRx/VRF214/sp").getValue()
      sdstrm2Sp = system.tag.read("[]Site/CRx/VRF224/sp").getValue()
   except:
      system.util.invokeAsynchronous(message.insert(application,"%s could not get data with which to update %s outputs at %s."%("SwapFrontFeedsOutputs.calculate",lib.getBlockName(fd),"the current real time as a time stamp"),str(EMCConstants.WARNING),fd)
      
      return textRecommendation,recommendations

   avgheight = functions.average(height2,height3)
   system.util.invokeAsynchronous(message.insert(application,"AvgHeight has the value %s"%(avgheight),str(EMCConstants.INFORMATION),fd)
   #     A taper resulting from a given feed source rises from the scallop depth of the previous zone to the taper tip from the current feed source.  The tapers are uniform when each rises to the same height, independent of depth.  Thus the target rise of each taper is the difference between average height for both tapers and the depth prior to that taper.  Since rise is assumed directly proportional to feed, the feed change to correct the rise error is also directly proportional to the rise error to be corrected.  The constant of proportionality is dynamic -- it is the negative of the current ratio of feed to rise.   
   risepv1 = height2 - depth1
   risesp1 = avgheight - depth1
   deltafd1 =  -sdstrm1Pv * (risepv1 - risesp1) / risepv1
   log.infof("DeltaFd_1 has the value %s",deltafd1)
   risepv2 = height3 - depth2
   risesp2 = avgheight - depth2
   deltafd2 =  -sdstrm2Pv * (risepv2 - risesp2) / risepv2
   log.infof("DeltaFd_2 has the value %s",deltafd2)
   deltafd = min(abs(deltafd1),abs(deltafd2))
   log.infof("DeltaFd has the value %s",deltafd)
   #     Since the sign of DeltaFd has been dropped, reconstruct it by the ratio term below.  Also allow for the error in the sidestream feed controller.  Output is incremental. 
   sdstrm1QuantRecDef = recommendation.defineQuantOutput(fd,"vrf214_target")
   sdstrm2QuantRecDef = recommendation.defineQuantOutput(fd,"vrf224_target")
   sdstrm1QuantRecDef["Value"]=sdstrm1Pv + deltafd * (deltafd1 / abs(deltafd1)) - sdstrm1Sp
   recommendations.append(sdstrm1QuantRecDef)

   sdstrm2QuantRecDef["Value"]=sdstrm2Pv + deltafd * (deltafd2 / abs(deltafd2)) - sdstrm2Sp
   recommendations.append(sdstrm2QuantRecDef)

   log.infof("%s has updated %s outputs.","SwapFrontFeedsOutputs.calculate",lib.getBlockName(fd))
   #      write the msg for the recommendation msg handler 
   outputMessage.create(fd)
   
   return textRecommendation,recommendations