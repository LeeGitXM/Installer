# Copyright 2015 ILS Automation. All rights reserved.
'''
 Set subdiagnoses to ensure that SF-3a action is done once and only once under these conditions. 
'''
import system
from xom.vistalon.diagToolkit.crx import FrontErrorChangeFeeds

def calculate(application, fd, provider, database):
    log = system.util.getLogger("project.vistalon.crx")

    log.info("In FrontShortTempInhibited.calculate()")

    system.tag.write("[%s]Site/CRX/PREMIX-LINE-FRESH-PARAMETER" % (provider), False)

    textRecommendation, recommendations = FrontErrorChangeFeeds.calculate(application, fd, provider, database)

    return textRecommendation,recommendations