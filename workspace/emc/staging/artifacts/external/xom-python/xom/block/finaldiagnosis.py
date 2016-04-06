#  Copyright 2014 ILS Automation
#
import system, string, time

def getClassName():
    return "FinalDiagnosis"

# A FinalDiagnosis block receives a truth-value from an SQC
# or other block upstream and deduces the reason for the issue.
#
from ils.block import basicblock
#import ils.diagToolkit.finalDiagnosis as fd

callback = "fd.evaluate"

class FinalDiagnosis(basicblock.BasicBlock):
    def __init__(self):
        basicblock.BasicBlock.__init__(self)
        self.initialize()
        self.state = "UNKNOWN"
    
    # Set attributes custom to this class
    def initialize(self):
        self.className = 'xom.block.finaldiagnosis.FinalDiagnosis'
        self.properties['Label'] = {'value':'FinalDiagnosis','editable':'True'}
        
        self.inports = [{'name':'in','type':'TRUTHVALUE'}]
        self.outports= [{'name':'out','type':'TRUTHVALUE'},{'name':'diagnosis','type':'TEXT'}]

# These properties were in G2 but they are dynamic, or at least have nothing to do with block execution.
# These are now implemented in the database. There is a custom editor on a FinalDiagnosis block to handle these.
#        self.properties['CalculationMethod'] = {'value':'','editable':'True'}
#        self.properties['Explanation'] = {'value':'','editable':'True'}
#        self.properties['PostTextRecommendation'] = {'value':'False','editable':'True','type':'BOOLEAN'}
#        self.properties['Priority'] = {'value':'1.0','editable':'True','type':'DOUBLE'}
#        self.properties['TextRecommendation'] = {'value':'','editable':'True'}
#        self.properties['TextRecommendationCallback'] = {'value':'','editable':'True'}
        # Value is seconds
#        self.properties['RecommendationRefreshInterval'] = {'value':'10000000.','editable':'True','type':'TIME'}
#        self.properties['Targets'] = {'value':'','editable':'True','type':'LIST'}
        
#        self.properties['LogToDatabase'] = {'value':'False','editable':'True','type':'BOOLEAN'}
#        self.properties['ManualMove'] = {'value':'False','editable':'True','type':'BOOLEAN'}
#        self.properties['ManualMoveValue'] = {'value':'0.0','editable':'True','type':'DOUBLE'}
#        self.properties['ManualTextRequired'] = {'value':'False','editable':'True','type':'BOOLEAN'}
#        self.properties['Multiplier'] = {'value':'1.0','editable':'True','type':'DOUBLE'}   
#        self.properties['TrapInsignificantConditions'] = {'value':'True','editable':'True','type':'BOOLEAN'}     
        
    # Return a dictionary describing how to draw an icon
    # in the palette and how to create a view from it.
    def getPrototype(self):
        proto = {}
        proto['auxData'] = True
        proto['iconPath']= "Block/icons/palette/final_diagnosis.png"
        proto['label']   = "FinDiagnosis"
        proto['tooltip']        = "Conclude a diagnosis based on input"
        proto['tabName']        = 'Analysis'
        proto['viewBackgroundColor'] = '0xFCFEFE'
        proto['viewLabel']      = "Final\nDiagnosis"
        proto['blockClass']     = self.getClassName()
        proto['blockStyle']     = 'square'
        proto['viewFontSize']       = 14
        proto['viewHeight']     = 80
        proto['inports']        = self.getInputPorts()
        proto['outports']       = self.getOutputPorts()
        proto['viewWidth']      = 100
        proto['editorClass']   = "com.ils.blt.designer.config.FinalDiagnosisConfiguration"
        proto['transmitEnabled']= True
        return proto
            
    # Called when a value has arrived on one of our input ports
    # It is our diagnosis. Set the property then evaluate.
    def acceptValue(self,port,value,quality,ts):
        newState = str(value).upper()
        if newState == self.state:
            return
        
        self.state = newState
        print "FinalDiagnosis.acceptValue: ",self.state

        if self.state != "UNKNOWN":
            print "Clearing the watermark"
            system.ils.blt.diagram.clearWatermark(self.parentuuid)
        
        handler = self.handler
        
        # On startup, it is possible for a block to get a value before
        # all resources (like the parent application) have been loaded. 
        if handler.getApplication(self.parentuuid)==None or handler.getFamily(self.parentuuid)==None:
            print "FinalDiagnosis.acceptValue: Parent application or family not loaded yet, ignoring state change"
            self.state = "UNKNOWN"
            return

        database = handler.getDefaultDatabase(self.parentuuid)
        provider = handler.getDefaultTagProvider(self.parentuuid)
        
        print "Using database: %s and tag provider: %s " % (database, provider)
        
        applicationName = handler.getApplication(self.parentuuid).getName()
        familyName = handler.getFamily(self.parentuuid).getName()
        print "Application: %s\nFamily: %s" % (applicationName, familyName)
        # TODO Need to find how to get these
        finalDiagnosis = handler.getBlock(self.parentuuid, self.uuid)
        finalDiagnosisName = finalDiagnosis.getName()
        print "Final Diagnosis: %s" % (finalDiagnosisName)

        if self.state == "TRUE":
            print "The diagnosis just became TRUE"
            # Notify inhibit blocks to temporarily halt updates to SQC
            # handler.sendTimestampedSignal(self.parentuuid, "inhibit", "", "",time)
            from ils.diagToolkit.finalDiagnosis import postDiagnosisEntry
            postDiagnosisEntry(applicationName, familyName, finalDiagnosisName, self.uuid, self.parentuuid, database, provider)  
        else:
            print "The diagnosis just became FALSE"
            from ils.diagToolkit.finalDiagnosis import clearDiagnosisEntry
            clearDiagnosisEntry(applicationName, familyName, finalDiagnosisName, database, provider)
        

        # Pass the input through to the output
        self.postValue('out',value,quality,ts)
        # Notifications on the signal link
        #self.postValue('send','inhibit','good')
        #self.postValue('send','reset','good')
        print "FinalDiagnosis.acceptValue: COMPLETE"
    
# Trigger property and connection notifications on the block
    def notifyOfStatus(self):
        self.handler.sendConnectionNotification(self.uuid, 'out', self.state,'good',0)