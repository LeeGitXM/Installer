'''
Created on Dec 17, 2015

@author: rforbes
'''

def activate(scopeContext, stepProperties, deactivate):
    from ils.sfc.gateway.util import getStepProperty, handleUnexpectedGatewayError
    from ils.sfc.gateway.api import getDatabaseName, s88Set, getChartLogger
    from system.ils.sfc.common.Constants import SQL, RECIPE_LOCATION, KEY
    import system.db
    
    try:
        chartScope = scopeContext.getChartScope()
        stepScope = scopeContext.getStepScope()
        chartLogger = getChartLogger(chartScope)
        database = getDatabaseName(chartScope)
        sql = getStepProperty(stepProperties, SQL) 
        result = system.db.runQuery(sql, database) # returns a PyDataSet
        jsonResult = system.util.jsonEncode(result)
        recipeLocation = getStepProperty(stepProperties, RECIPE_LOCATION) 
        key = getStepProperty(stepProperties, KEY) 
        s88Set(chartScope, stepScope, key, jsonResult, recipeLocation)
    except:
        handleUnexpectedGatewayError(chartScope, 'Unexpected error in rawQuery.py', chartLogger)
    finally:
        return True