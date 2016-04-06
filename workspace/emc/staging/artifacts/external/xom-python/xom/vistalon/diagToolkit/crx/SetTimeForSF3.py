# Copyright 2015 ILS Automation. All rights reserved.
'''
 Set mooney reset time for sf-3 
'''
import system
import datetime

def setTime(block):
    log = system.util.getLogger("project.vistalon.crx")

    log.info("In SetTimeForSF3()")
    log.trace("Default database is %s" % (block.handler.getDefaultDatabase(block.parentuuid)))
    log.trace("Default tag provider is %s" % (block.handler.getDefaultTagProvider(block.parentuuid)))
    provider = block.handler.getDefaultTagProvider(block.parentuuid)
    currentTime = datetime.datetime.now()
    log.trace("setting mooney reset time for sf-3 to %s" % (currentTime))
    system.tag.write("[%s]Site/CRX/MOONEY-RESET-TIME-FOR-SF-3/value" % (provider), currentTime)
    system.tag.write("[%s]Site/CRX/MOONEY-RESET-TIME-FOR-SF-3/badValue" % (provider), False)

