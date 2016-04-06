#  Copyright 2014 ILS Automation
#
import system.util

def getClassName():
    return "Action"

# Implement a block that can execute custom functions. These
# functions are Python modules in the project or global scope.
#
from ils.block import basicblock


class Action(basicblock.BasicBlock):
    def __init__(self):
        basicblock.BasicBlock.__init__(self)
        self.initialize()
        
    # Set attributes custom to this class.
    # Default the trigger to TRUE
    def initialize(self):
        self.className = 'xom.block.action.Action'
        self.properties['Script'] = {'value':'xom.actions.demo.act','editable':'True'}
        self.properties['Trigger'] = {'value':'TRUE','editable':'True','type':'TRUTHVALUE'}
        self.inports = [{'name':'in','type':'truthvalue'}]
        self.outports= [{'name':'out','type':'truthvalue'}]
        
    # Return a dictionary describing how to draw an icon
    # in the palette and how to create a view from it.
    def getPrototype(self):
        proto = {}
        proto['iconPath']= "Block/icons/palette/action.png"
        proto['label']   = "Action"
        proto['tooltip']        = "Execute a user-defined script"
        proto['tabName']        = 'Misc'
        proto['viewBackgroundColor'] = '0xF0F0F0'
        proto['viewIcon']      = "Block/icons/embedded/gear.png"
        proto['blockClass']     = self.getClassName()
        proto['blockStyle']     = 'square'
        proto['viewHeight']     = 70
        proto['viewWidth']      = 70
        proto['inports']        = self.getInputPorts()
        proto['outports']       = self.getOutputPorts()
        proto['receiveEnabled']  = 'false'
        proto['transmitEnabled'] = 'false'
        return proto
            
    # Called when a value has arrived on one of our input ports
    # If the value matches the trigger (case insensitive),
    # then evaluate the function. The output retains the 
    # timestamp of the input.
    def acceptValue(self,port,value,quality,time):
        trigger = self.properties.get('Trigger',{}).get("value","").lower()
        text = str(value).lower()
        if text == trigger:
            self.state = "TRUE"
            function = self.properties.get('Script',{}).get("value","")
            print self.getClassName(),"acceptValue: exec ",function
            if len(function) > 0:
                packName = self.packageFromModule(function)
                statement='import '+packName+';'+function+"(self);"
                try:
                    exec(statement)
                except Exception, e:
                    cause = e.getCause()
                    print self.getClassName(),"acceptValue: exec ",function," exception: ",cause.getMessage()
        else:
            self.state = "FALSE"
        self.postValue('out',value,quality,time)
        
    # Trigger property and connection notifications on the block
    def notifyOfStatus(self):
        self.handler.sendConnectionNotification(self.uuid, 'out', self.state,'good',0)  