# Copyright 2015 ILS Automation. All rights reserved.

'''
 Output is the incremental change in oil to cement ratio to get the oil content back on target.   
'''

import system
from ils.diagToolkit.recommendation import postApplicationMessage
from ils.constants.constants import QUEUE_ERROR, QUEUE_INFO

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.cstr")
    textRecommendation=""
    recommendations=[]
    log.info("In %s" % (__name__))

    tagName = "[%s]LabData/RLA3/OIL-FILTERED-VALUE/filteredValue" % (provider)
    pv = system.tag.read(tagName)
    if not (pv.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - pv is bad (%s) %s" % (__name__, tagName, str(pv.quality)), log)
        return textRecommendation,recommendations
    pv=pv.getValue()
    
    tagName = "[%s]LabData/RLA3/OIL-LAB-DATA-SQC/target" % (provider)
    sp = system.tag.read(tagName)
    if not (sp.quality.isGood()):
        postApplicationMessage(application, QUEUE_ERROR, "%s - sp is bad (%s) %s" % (__name__, tagName, str(sp.quality)), log)
        return textRecommendation,recommendations
    sp=sp.getValue()
    
    postApplicationMessage(application, QUEUE_INFO, "%s will use data of PV = %s and SP = %s." % (__name__, str(pv), str(sp)), log) 
                              
    delta = (pv - sp)
    log.trace("In %s: PV is %s and SP is %s" % (__name__, str(pv), str(sp)))
    log.trace("   Delta between PV and SP is: %s" % (str(delta)))
    
    # The gain from error to output should be 1.0 to get all the way to the target value. 
    recommendations.append({"QuantOutput":"VDF030R_TARGET","Value":-delta})
    log.trace("   calculated the following recommendations: %s" % (str(recommendations)))

    return textRecommendation,recommendations