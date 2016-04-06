'''
Created on Dec 17, 2015

@author: rforbes
'''

def activate(scopeContext, stepProperties, deactivate): 
    from ils.sfc.gateway.api import getChartLogger    
    from ils.sfc.gateway.util import handleUnexpectedGatewayError
    chartScope = scopeContext.getChartScope()
    stepScope = scopeContext.getStepScope()
    chartLogger = getChartLogger(chartScope)
    try:
        pass
    except:
        handleUnexpectedGatewayError(chartScope, 'Unexpected error in confirmControllers.py', chartLogger)
    finally:
        return True