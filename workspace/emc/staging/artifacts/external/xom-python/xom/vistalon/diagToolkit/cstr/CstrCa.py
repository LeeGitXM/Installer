# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is the change in the flash drum calcium targets to get the product calcium laboratory value back on target.   
'''

import system
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    log.info("In %s" % (__name__))
    textRecommendation=""
    recommendations=[]
  
    tagName="[%s]LabData/RLA3/CA-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName="[%s]LabData/RLA3/CA-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp=sp.getValue()
    
    tagName="[%s]DiagnosticToolkit/CSTR/VFS009ME/value" % (provider)
    castDisp = system.tag.read(tagName)
    if not (castDisp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - castDisp is bad (%s) %s" % (__name__, tagName, str(castDisp.quality)), log)
        return textRecommendation,recommendations
    castDisp=castDisp.getValue()
    
    tagName="[%s]DiagnosticToolkit/CSTR/VFS000ME/value" % (provider)
    d20 = system.tag.read(tagName)
    if not (d20.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - d20 is bad (%s) %s" % (__name__, tagName, str(d20.quality)), log)
        return textRecommendation,recommendations
    d20=round(d20.getValue())
    
    tagName="[%s]DiagnosticToolkit/CSTR/VFS100ME/value" % (provider)
    d20a = system.tag.read(tagName)
    if not (d20a.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - d20a is bad (%s) %s" % (__name__, tagName, str(d20a.quality)), log)
        return textRecommendation,recommendations
    d20a=round(d20a.getValue())
    
    tagName="[%s]DiagnosticToolkit/CSTR/VFS200ME/value" % (provider)
    d20b = system.tag.read(tagName)
    if not (d20b.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - d20b is bad (%s) %s" % (__name__, tagName, str(d20b.quality)), log)
        return textRecommendation,recommendations
    d20b=round(d20b.getValue())
    
    #system.util.invokeAsynchronous(message.insert(application,"The CSTR Calcium problem will use data of PV = %s and SP = %s at %s."%(str(pv),str(sp),"the current real time as a time stamp"),str(EMCConstants.INFORMATION),fd)
    delta = (pv - sp)
    log.trace("Delta between PV and SP is: %s" % (str(delta)))
    
    if ((abs(d20 - 1.0) < 0.1) and (abs(castDisp - 3) < 0.1 or abs(castDisp) < 0.1)):
        recommendations.append({"QuantOutput": "VFA009Z_TARGET", "Value":-.25*delta})

    if ((abs(d20a - 1.0) < 0.1) and (abs(castDisp - 1) < 0.1 or abs(castDisp) < 0.1)):
        recommendations.append({"QuantOutput": "VFA109Z_TARGET", "Value":-.25*delta})

    if ((abs(d20b - 1.0) < 0.1) and (abs(castDisp - 2) < 0.1 or abs(castDisp) < 0.1)):
        recommendations.append({"QuantOutput": "VFA209Z_TARGET", "Value":-0.25*delta})

    postApplicationMessage(application, QUEUE_INFO, "%s will use data of PV=%s and SP=%s" % (__name__, str(pv), str(sp)), log)
    
    log.trace("%s calculated the following recommendations: %s" % (__name__, str(recommendations)))

    return textRecommendation,recommendations
