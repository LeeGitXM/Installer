# Copyright 2015 ILS Automation. All rights reserved.

'''
      Output is the change in the weight percent stabilizer to get the laboratory value back on target.   
'''

import system
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    textRecommendation=""
    recommendations=[]

    tagName = "[%s]LabData/RLA3/STAB-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - SP is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp=sp.getValue()
    
    tagName = "[%s]LabData/RLA3/STAB-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName = "[%s]LabData/RLA3/STAB-LAB-DATA-SQC/lowerSQCLimit" % (provider)
    pvll = system.tag.read(tagName)
    if not (pvll.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - PV Lower Limit is bad (%s) %s" % (__name__, tagName, str(pvll.quality)), log)
        return textRecommendation,recommendations
    pvll=pvll.getValue()
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use data of PV = %s and SP = %s." % (__name__, str(pv), str(sp)), log)
    
    pctchg = (sp - pvll) * 100.0 / sp / 2.0
    delta = (pv - sp)
    recommendations.append({"QuantOutput":"VMF223R1_TARGET","Value":-pctchg*delta/0.01})
    
    log.trace("%s calculated the following recommendations: %s" % (__name__, str(recommendations)))

    return textRecommendation,recommendations