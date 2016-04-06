'''
Created on Dec 17, 2015

@author: rforbes
'''

import system
from com.sun.rowset.internal import Row

def activate(scopeContext, stepProperties, deactivate): 
    from system.ils.sfc.common.Constants import BUTTON_LABEL, RECIPE_LOCATION, MONITOR_DOWNLOADS_CONFIG, DATA_ID, DOWNLOAD_STATUS, \
        WRITE_CONFIRMED, POSITION, SCALE, TIMER_KEY, WINDOW_TITLE, PROMPT, KEY
    from ils.sfc.common.constants import PV_VALUE, PV_MONITOR_ACTIVE, PV_MONITOR_STATUS, STEP_PENDING, PV_NOT_MONITORED, WINDOW_ID
    from system.ils.sfc import getMonitorDownloadsConfig
    from ils.sfc.gateway.downloads import handleTimer
    from ils.sfc.gateway.monitoring import createMonitoringMgr
    from ils.sfc.gateway.util import sendMessageToClient, getStepProperty, transferStepPropertiesToMessage, handleUnexpectedGatewayError, getControlPanelId, createWindowRecord
    from system.ils.sfc import getProviderName, getDatabaseName
    from ils.sfc.gateway.api import getIsolationMode, getChartLogger, getProject, s88GetFullTagPath
    from ils.sfc.gateway.recipe import RecipeData, splitKey

    try:
        chartScope = scopeContext.getChartScope()
        stepScope = scopeContext.getStepScope()
        logger = getChartLogger(chartScope)
        logger.trace("In monitorDownload.activate()...")
        timer, timerAttribute = handleTimer(chartScope, stepScope, stepProperties, logger)
        recipeLocation = getStepProperty(stepProperties, RECIPE_LOCATION)
        configJson = getStepProperty(stepProperties, MONITOR_DOWNLOADS_CONFIG)
        monitorDownloadsConfig = getMonitorDownloadsConfig(configJson)
        isolationMode = getIsolationMode(chartScope) 
        providerName = getProviderName(isolationMode)
        database = getDatabaseName(isolationMode)
        
        print "Using database: ", database
    
#        mgr = createMonitoringMgr(chartScope, stepScope, recipeLocation, timer, timerAttribute, monitorDownloadsConfig, logger, providerName)
        
        # Insert a window record into the database
        controlPanelId = getControlPanelId(chartScope)
        buttonLabel = getStepProperty(stepProperties, BUTTON_LABEL)
        position = getStepProperty(stepProperties, POSITION)
        scale = getStepProperty(stepProperties, SCALE)
        title = getStepProperty(stepProperties, WINDOW_TITLE)
        windowType = "SFC/MonitorDownloads"
        windowId = createWindowRecord(controlPanelId, windowType, buttonLabel, position, scale, title, database)
        stepScope[WINDOW_ID] = windowId     # This step completes as soon as the GUI is posted do I doubt I need to save this.
        print "Inserted a window with id: ", windowId
        
        print "Getting the full timer tagPath..."
        timerKeyAndAttribute = getStepProperty(stepProperties, TIMER_KEY)
        timerKey, timerAttribute = splitKey(timerKeyAndAttribute)
        timerTagPath = s88GetFullTagPath(chartScope, stepScope, timerKey, recipeLocation) + '/value'
        
        SQL = "insert into SfcDownloadGUI (windowId, state, LastUpdated, timerTagPath) values ('%s', 'created', CURRENT_TIMESTAMP, '%s')" % (windowId, timerTagPath)
        system.db.runUpdateQuery(SQL, database)
        
        # Reset the recipe data download and PV monitoring attributes
        for row in monitorDownloadsConfig.rows:
            logger.trace("Resetting recipe data with key: %s" % (row.key))
            rd = RecipeData(chartScope, stepScope, recipeLocation, row.key)
            print "Recipe Data: ", rd
            
            # Initialize properties used by the write output process
            rd.set(DOWNLOAD_STATUS, STEP_PENDING)
            rd.set(WRITE_CONFIRMED, None)
                
            # Initialize properties used by a PV monitoring process
            rd.set(PV_MONITOR_ACTIVE, False)
            rd.set(PV_VALUE, None)
            rd.set(PV_MONITOR_STATUS, PV_NOT_MONITORED)
            rd.set("stepTimestamp", "")
            
            fullTagPath=s88GetFullTagPath(chartScope, stepScope, row.key, recipeLocation)
            
            SQL = "insert into SfcDownloadGUITable (windowId, recipeDataPath, labelAttribute) "\
                "values ('%s', '%s', '%s')" % (windowId, fullTagPath, row.labelAttribute)

            system.db.runUpdateQuery(SQL, database)
    
        payload = dict()
#        payload[DATA_ID] = mgr.getTimerId()
        payload[WINDOW_ID] = windowId
            
        transferStepPropertiesToMessage(stepProperties, payload)
        project = getProject(chartScope)
        sendMessageToClient(project, 'sfcOpenWindow', payload)
        logger.trace("...leaving monitorDownload.activate()")      
    except:
        handleUnexpectedGatewayError(chartScope, 'Unexpected error in monitorDownload.py', logger)

    return True