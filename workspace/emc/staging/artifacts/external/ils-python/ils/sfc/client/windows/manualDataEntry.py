'''
Created on Jul 28, 2015

@author: rforbes
'''
    
def sendData(window):
    '''Send data to gateway. If configured, check that all values have been entered, and
       don't send and warn if they have not. Return true if data was sent.'''
    from ils.sfc.common.util import isEmpty
    from ils.sfc.common.constants import DATA
    import system.gui.warningBox
    from ils.sfc.client.windowUtil import sendWindowResponse
    table = window.getRootContainer().getComponent('table')
    dataset = table.data
    
    # anywhere units are specified, check if they also exist in recipe data
    for row in range(dataset.rowCount):
        units = dataset.getValueAt(row, 2)
        if not isEmpty(units):
            recipeUnits = dataset.getValueAt(row, 8)
            key = dataset.getValueAt(row, 5)
            if isEmpty(recipeUnits):
                system.gui.messageBox("Unit %s is specified but recipe data %s has no units. No conversion will be done." % (units, key), 'Warning')
     
    requireAllInputs = window.getRootContainer().data.getValueAt(0,'requireAllInputs')
    allInputsOk = True
    if requireAllInputs:
        for row in range(dataset.rowCount):
            value = dataset.getValueAt(row, 1)
            if (value == None) or (len(value.strip()) == 0):
                allInputsOk = False
                break
    if allInputsOk:
        response = {DATA: dataset}
        sendWindowResponse(window, response)    
        return True
    else:
        system.gui.messageBox("All inputs are required", "Warning")
        return False
