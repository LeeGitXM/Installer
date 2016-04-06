#  Copyright 2014 ILS Automation

import system

def getClassName():
    return "SQCDiagnosis"

# A SQCDiagnosis block receives a truth-value from an SQC
# or other block upstream and deduces the reason for the issue.
#
from ils.block import basicblock


class SQCDiagnosis(basicblock.BasicBlock):
    def __init__(self):
        basicblock.BasicBlock.__init__(self)
        self.initialize()
    
    # Set attributes custom to this class
    def initialize(self):
        self.className = 'xom.block.sqcdiagnosis.SQCDiagnosis'
        self.properties['Label'] = {'value':'SQCDiagnosis','editable':'True'}
        self.properties['TagPath'] = { 'value':'','binding':'','bindingType':'TAG_WRITE','editable':'True'}
    
        self.inports = [{'name':'in','type':'truthvalue'}]
        self.outports= [{'name':'out','type':'truthvalue'},{'name':'diagnosis','type':'text'}]
        
    # Return a dictionary describing how to draw an icon
    # in the palette and how to create a view from it.
    def getPrototype(self):
        proto = {}
        proto['iconPath']= "Block/icons/palette/SQC_diagnosis.png"
        proto['label']   = "SQCDiagnosis"
        proto['tooltip']        = "Conclude a diagnosis from an upstream SQC block based on input"
        proto['tabName']        = 'Analysis'
        proto['viewBackgroundColor'] = '0xFCFEFE'
        proto['viewLabel']      = "SQC\nDiagnosis"
        proto['blockClass']     = self.getClassName()
        proto['blockStyle']     = 'square'
        proto['viewFontSize']       = 14
        proto['viewHeight']     = 80
        proto['inports']        = self.getInputPorts()
        proto['outports']       = self.getOutputPorts()
        proto['viewWidth']      = 100
        return proto
            
    # Called when a value has arrived on one of our input ports
    # For now we record it to a tag and pass it through to the output
    def acceptValue(self,port,value,quality,time):
        if not self.state==str(value):
            print "Accepting a new value <%s> for an SQC diagnosis block..." % (str(value))
            self.state = str(value)
        
            # Write to the tag, if it exists
            prop = self.properties['TagPath']
            path = prop['binding']
            if len(path)>0:
                self.handler.updateTag(self.parentuuid,path,str(value),quality,time)
            
        # Pass the input through to the output
        self.postValue('out',value,quality,time)
                
        handler = self.handler
        database = handler.getDefaultDatabase(self.parentuuid)

        print "Using database: %s " % (database)
        
        sqcDiagnosis = handler.getBlock(self.parentuuid, self.uuid)
        sqcDiagnosisName = sqcDiagnosis.getName()
        tokens=sqcDiagnosisName.split('-GDA')
        print tokens
        sqcDiagnosisName=tokens[0]
        blockId=self.uuid
        print "Block: %s received value %s" % (sqcDiagnosisName, str(blockId))
        
        print "Updating a SQC diagnosis by id..."
        SQL = "update DtSQCDiagnosis set SQCDiagnosisName = '%s', Status = '%s' where BlockId = '%s'" % (sqcDiagnosisName, str(value), str(blockId))
        print SQL
        try:
            rows=system.db.runUpdateQuery(SQL, database)
            if rows > 0:
                print "...success"
                return
        
            # The block Id could not be found - see if the block name exists.
            print "...that didn't work, try updating by name..."
            SQL = "update DtSQCDiagnosis set BlockId = '%s', Status = '%s' where SQCDiagnosisName = '%s'" % (str(blockId), str(value), sqcDiagnosisName)
            print SQL
            rows=system.db.runUpdateQuery(SQL, database)
            if rows > 0:
                print "...success"
                return
        
            # The name couldn't be found either so this must be a totally new SQC diagnosis which we have never seen before
            print "...that didn't work either, try inserting a new record, this must be a new block..."
        
            applicationName = handler.getApplication(self.parentuuid).getName()
            familyName = handler.getFamily(self.parentuuid).getName()
            from ils.diagToolkit.common import fetchFamilyId
            familyId = fetchFamilyId(familyName, database)
            if familyId == None:
                print "ERROR - unable to insert the SQC diagnosis into the database because the family <%s> is undefined" % (familyName)
                return
        
            print "Application: %s\nFamily: %s (%i)" % (applicationName, familyName, familyId)
        
            SQL = "insert into DtSQCDiagnosis (BlockId, Status, SQCDiagnosisName, FamilyId) values ('%s', '%s', '%s', %s)" % (str(blockId), str(value), sqcDiagnosisName, str(familyId))
            print SQL
            rows=system.db.runUpdateQuery(SQL, database)
            if rows > 0:
                print "...success"
                return
        
            print "ERROR"
        except:
            print "SQCDiagnosis: SQL Error"

    # Trigger property and connection notifications on the block
    def notifyOfStatus(self):
        self.handler.sendConnectionNotification(self.uuid, 'out', self.state,'good',0)
           
    def reset(self):
       basicblock.BasicBlock.reset(self) 
       self.state = 'UNKNOWN'
       