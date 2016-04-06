'''
Created on Jul 6, 2015

@author: Pete
'''

import system
from ils.labData.common import postMessage
log = system.util.getLogger("com.ils.labData.customValidation")

#
# All Lab Data validation callbacks generically take two arguments, the lab data name and the raw value.
# They return a single boolean: True if it passed validation, False otherwise.
#


#--------------------------------------------------------
# The production version of this is in the global project
#--------------------------------------------------------
# This callback expects a single value in the data dictionary
# This callback is called by several DCS lab datums
def labBalerInService(valueName, rawValue):
    log.trace("In labBalerInService with %s - %s" % (valueName, str(rawValue)))
 
    from ils.common.associations import fetchSources
    sources = fetchSources("LabData/VFU/" + valueName, "Lab Baler Service Data")
    log.trace("   fetched %i sources..." % (len(sources)))
    
    # Fetch all of the labBalerService Data associated with the lab data item
    for source in sources:
        log.trace("   ...checking source: %s" % (source))
        # First read the baler rate
        tagPath = source + "/balerRate"
        tagExists=system.tag.exists(tagPath)
        if not(tagExists):
            log.trace("Failed validation because the baler rate tag <%s> does not exist" % (tagPath))
            postMessage("%s failed validation because the baler rate tag <%s> does not exist" % (valueName, tagPath), "Error")
            return False
        
        qv=system.tag.read(tagPath)
        if not(qv.quality.isGood()):
            log.trace("Failed validation because the baler rate tag <%s> quality is bad!" % (tagPath))
            postMessage("%s failed validation because the baler rate tag <%s> quality is bad!" % (valueName, tagPath), "Error")
            return False
        
        balerRate = qv.value
        
        # Now read the baler rate limit
        tagPath = source + "/lowLimit"
        tagExists=system.tag.exists(tagPath)
        if not(tagExists):
            log.trace("Failed validation because the baler rate limit tag <%s> does not exist" % (tagPath))
            postMessage("%s failed validation because the baler rate limit tag <%s> does not exist" % (valueName, tagPath), "Error")
            return False
        
        qv=system.tag.read(tagPath)
        if not(qv.quality.isGood()):
            log.trace("Failed validation because the baler rate limit tag <%s> quality is bad!" % (tagPath))
            postMessage("%s failed validation because the baler rate limit tag <%s> quality is bad!" % (valueName, tagPath), "Error")
            return False
        
        balerRateLimit = qv.value
        
        if balerRate < balerRateLimit:
            log.trace("Failed validation because the baler rate <%s> is less than the baler rate limit <%s>" % (str(balerRate), str(balerRateLimit)))
            postMessage("%s failed validation because the baler rate <%s> is less than the baler rate limit <%s>" % (valueName, str(balerRate), str(balerRateLimit)), "Error")
            return False
  
    log.trace("... %s passed custom validation because the baler rate <%s> is greater than the baler rate limit <%s>" % (valueName, str(balerRate), str(balerRateLimit)))
    return True
