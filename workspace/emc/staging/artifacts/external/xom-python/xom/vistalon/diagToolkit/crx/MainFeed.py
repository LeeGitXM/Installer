# Copyright 2015 ILS Automation. All rights reserved.
from com.inductiveautomation.ignition.common.util import LogUtil
from ils.diagToolkit.util import outputMessage
from ils.blt import lib
from com.inductiveautomation.ignition.common.util import LoggerEx
from ils.diagToolkit import recommendation
from ils.queue import message
from ils.constants.enumerations import EMCConstants
def calculate(application,fd):
   log = LogUtil.getLogger("project.vistalon.crx")
   textRecommendation=""
   recommendations=[]
   debugMode = False
   if lib.getDebugMode(application):
      debugMode = True

   if debugMode:
      log.infof("In %s with  . .",lib.getName(application))

   newSc2CalcFlag = useNewSc2Calc
   try:
      pv = system.tag.read("[]UnitParameter/CRx/CNTR_AVG_TPR_TIP_HT/value").getValue()
      sp = system.tag.read("[]UnitParameter/CRx/CNTR_AVG_TPR_TIP_HT_TARGET").getValue()
      k = gainCntrTprTipMainFd
      spc2 = system.tag.read("[]UnitParameter/CRx/MAIN-C2").getValue()
      spc3 = system.tag.read("[]UnitParameter/CRx/MAIN-C3").getValue()
   except:
      system.util.invokeAsynchronous(message.insert(application,"%s could not get data with which to update %s outputs at %s."%(lib.getName(application),lib.getBlockName(fd),"the current real time as a time stamp"),str(EMCConstants.WARNING),fd)
      
      return textRecommendation,recommendations

   # Start "New Code for Modified SC2 Final Diagnostic".
   #   	This code addition modifies the SC2 calc in an attempt to better deal with the problem SC2 is trying to solve (Low Center C2= content with Front-of-Structure looking OK and overall C2= content good).  For this case we want to add more C2= to the center of the molecule (SS3 through SS6), but must also stay overall C2= Neutral (i.e. must reduce the Main C2= addition to offset that which we added to the SS).   	The main difference in this modification is that since the change in the SS C2= is made to the "Total SS C2= Controller", we need to make a corresponding reduction in the rates of both SS1 and SS2 to keep them C2= level when the SS C2= concentration goes up due to the increase in Total SS C2=.  Additionally, we reduce the Main C3= flow (which is NOT on Ratio to Main C2=) to also keep the molecule C3= neutral in the face of the increase in the SS C2= rate (which brings a change to the SS C3= via the active SS C3=-to-C2= Ratio control.)  	Modifications have been coded in such a way that this new calc can be optionally exercized by setting the value of the logical parameter "USE-NEW-SC2-CALC" to TRUE.  
   if debugMode:
      log.infof("Use NEW SC-2 Calc is %s. ",str(newSc2CalcFlag))

   # Collect additional data needed for the new Modified SC2 Calc.
   if newSc2CalcFlag:
      try:
         ss1 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-1").getValue()
         ss2 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-2").getValue()
         ss3 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-3").getValue()
         ss4 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-4").getValue()
         ss5 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-5").getValue()
         ss6 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-6").getValue()
         ssc2 = system.tag.read("[]DiagnosticToolkit/CRx/SDSTRM-C2").getValue()
         ssc3c2r = system.tag.read("[]DiagnosticToolkit/CRx/VRF503R-2").getValue()
      except:
         system.util.invokeAsynchronous(message.insert(application,"%s could not get data with which to update New %s outputs at %s."%(lib.getName(application),lib.getBlockName(fd),"the current real time as a time stamp"),str(EMCConstants.WARNING),fd)
         
         return textRecommendation,recommendations


   # End "New Code for Modified SC2 Final Diagnostic".
   deltam = k * (pv - sp) / 100.0
   mainC2QuantRecDef = recommendation.defineQuantOutput(fd,"vrc032_target")
   mainC3QuantRecDef = recommendation.defineQuantOutput(fd,"vrc023_target")
   sdstrmC2QuantRecDef = recommendation.defineQuantOutput(fd,"vrc232_target")
   # Define and Initialize SS1 and SS2 outputs to "0". (Will caclulate real moves later if option is exercised.) 
   sdstrm1QuantRecDef = recommendation.defineQuantOutput(fd,"vrf214_target")
   # New Code
   sdstrm2QuantRecDef = recommendation.defineQuantOutput(fd,"vrf224_target")
   # New Code
   sdstrm1QuantRecDef["Value"]=0.0
   recommendations.append(sdstrm1QuantRecDef)

   # New Code
   sdstrm2QuantRecDef["Value"]=0.0
   recommendations.append(sdstrm2QuantRecDef)

   # New Code
   mainC2QuantRecDef["Value"]=deltam * spc2
   recommendations.append(mainC2QuantRecDef)

   mainC3QuantRecDef["Value"]=deltam * spc3
   recommendations.append(mainC3QuantRecDef)

   sdstrmC2QuantRecDef["Value"]= -1.0 * deltam * spc2
   recommendations.append(sdstrmC2QuantRecDef)

   # Start "New Code for Modified SC2 Final Diagnostic".
   # Make modifications to the original SC2 calc if "Modified SC2" option is selected. (SideStream C2  
   if newSc2CalcFlag:
      sstot = ss1 + ss2 + ss3 + ss4 + ss5 + ss6
      sswfc2 = ssc2 / sstot
      f1 = ss1 / sstot
      f2 = ss2 / sstot
      ssc2new = ssc2 - deltam * spc2
      alpha = (deltam * spc2) / (ssc2new - ssc2 * (f1 + f2))
      mainC3QuantRecDef["Value"]=deltam * spc2 * ssc3c2r
      recommendations.append(mainC3QuantRecDef)

      sdstrm1QuantRecDef["Value"]=alpha * ss1
      recommendations.append(sdstrm1QuantRecDef)

      sdstrm2QuantRecDef["Value"]=alpha * ss2
      recommendations.append(sdstrm2QuantRecDef)

      sstotnew = sstot + alpha * (ss1 + ss2)
      sswfc2new = ssc2new / sstotnew
      if debugMode:
         log.infof("K = %s, PV = %s, SP = %s, DeltaM = %s.",str(k),str(pv),str(sp),str(deltam))
         log.infof("Old Total SS Flow is %s. ",str(sstot))
         log.infof("Old SS Wt Fraction C2=  is %s. ",str(sswfc2))
         log.infof("SS1 Fraction is %s, SS2 Fraction is %s, ALPHA is %s.",str(f1),str(f2),str(alpha))
         log.infof("New Total SS Flow is %s. ",str(sstotnew))
         log.infof("New SS Wt Fraction C2=  is %s. ",str(sswfc2new))


   # End "New Code for Modified SC2 Final Diagnostic" .
   system.util.invokeAsynchronous(message.insert(application,"%s has updated %s outputs."%(lib.getName(application),lib.getBlockName(fd)),str(EMCConstants.INFORMATION),fd)
   outputMessage.create(fd)
   try:
      frntLngth = system.tag.read("[]DiagnosticToolkit/CRx/CRX_HB-8/value").getValue()
      frntTarget = system.tag.read("[]UnitParameter/CRx/FRNT_LNGTH_TARGET").getValue()
   except:

      return textRecommendation,recommendations
   textRecommendation = "[get-from-text (the text of the text-recommendation of FD, 2, length-of-text (the text of the text-recommendation of FD) - 1)]%s%s     The center is %s which <= than the %s limit.  The front is %s which is >= the [frnt-target + the bandwidth of frnt_lngth_above_setpoint-gda as dd.d]."%(str(cr),str(cr),str(PV),"SP - the bandwidth of cntr_avg_tpr_tip_ht_very_low-gda as dd.d",str(frnt-lngth))
   if debugMode:
      log.infof("%shas completed!",lib.getName(application))
   return textRecommendation,recommendations
