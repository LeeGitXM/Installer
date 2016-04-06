'''
Created on Jul 21, 2015

@author: Pete
'''
import system, string
from ils.migration.common import lookupOPCServerAndScanClass
from ils.migration.common import lookupMessageQueue
from ils.common.database import lookup

def load(rootContainer):
    filename=rootContainer.getComponent("File Field").text
    if not(system.file.fileExists(filename)):
        system.gui.messageBox("Yes, the file exists")
        return
    
    contents = system.file.readFileAsString(filename, "US-ASCII")
    records = contents.split('\n')
    
    ds=parseRecords(records,"")
    table=rootContainer.getComponent("Power Table")
    table.data=ds
        
    print "Done Loading!"

def parseRecords(records,recordType):        
    print "Parsing %s records... " % (recordType)

    i = 0
    numTokens=100
    data = []    
    for line in records:
        line=line[:len(line)-1] #Strip off the last character which is some sort of CRLF
        tokens = line.split(',')

        if i == 0:
            line = "status,skip," + line
            line=line.rstrip(',')
            header = line.split(',')
            numTokens=len(header)
        else:
            if recordType == "" or string.upper(tokens[0]) == recordType:
                line = " ,False," + line
                tokens = line.split(',')
                print "Tokens: ", tokens
                if len(tokens) != numTokens:
                    for j in range(len(tokens), numTokens):
                        tokens.append("")
                print "Line %i now has %i tokens" % (i, len(tokens))
                data.append(tokens)
                
        i = i + 1

    print "Header: ", header
    print "Data: ", data
        
    ds = system.dataset.toDataSet(header, data)
    print "   ...parsed %i %s records!" % (len(data), recordType)
    return ds

def clearStatus(rootContainer):
    table=rootContainer.getComponent("Power Table")
    ds=table.data
    for row in range(ds.rowCount):
        ds=system.dataset.setValue(ds, row, "status", "")
    table.data=ds
    
def createTags(rootContainer):
    table=rootContainer.getComponent("Power Table")
    ds=table.data
    site = rootContainer.getComponent("Site").text
    provider = rootContainer.getComponent("Tag Provider").text
    rootFolder = rootContainer.getComponent("Root Folder").text
    folderFilter = rootContainer.getComponent("Filters").getComponent("Folder").text
    classFilter = rootContainer.getComponent("Filters").getComponent("Class").text
    itemIdPrefix = system.tag.read("[" + provider + "]Configuration/DiagnosticToolkit/itemIdPrefix").value

    for row in range(ds.rowCount):
        status = ""
        folder = ds.getValueAt(row, "Folder")
        className =  ds.getValueAt(row, "class")
        skip = ds.getValueAt(row, "skip")
        if (string.upper(skip) == "TRUE"):
            status = "Skipped"
        elif (folderFilter == "" or folder == folderFilter) and (classFilter == "" or className == classFilter):
            
            className =  ds.getValueAt(row, "class")
            outputName = ds.getValueAt(row, "name")
            outputNames = ds.getValueAt(row, "names")
            gsiInterface = ds.getValueAt(row, "gsi-interface")
            itemId = ds.getValueAt(row, "itemId")
#            conditionalItemId = ds.getValueAt(row, "Conditional ItemId")
            
            print "---------------------------"
            print "Folder: ", folder
            print "Class: ", className
            print "Name: ", outputName
            print "Names: ", outputNames
            print "GSI Interface: ", gsiInterface
            print "Item Id: ", itemId
            
            itemId = itemIdPrefix + itemId
            serverName, scanClass, writeLocationId = lookupOPCServerAndScanClass(site, gsiInterface)
            path = rootFolder + "/" + folder
            
            print folder, outputName, itemId, serverName
            
            parentPath = '[' + provider + ']' + path    
            tagPath = parentPath + "/" + outputName
            tagExists = system.tag.exists(tagPath)
        
            if tagExists:
                print tagPath, " already exists!"
                status = "Exists"
            else:
                if className == "OPC-TEXT-OUTPUT":
                    createOutput(parentPath, outputName, itemId, serverName, scanClass, outputNames, "String")
                    status = "Created"
                elif className == "FLOAT-PARAMETER":
                    createParameter(parentPath, outputName, scanClass, "Float8")
                    status = "Created"
                elif className == "OPC-FLOAT-OUTPUT":
                    createOutput(parentPath, outputName, itemId, serverName, scanClass, outputNames, "Float")
                    status = "Created"
                elif className == "OPC-FLOAT-BAD-FLAG":
                    createBadFlag(parentPath, outputName, itemId, serverName, scanClass, outputNames, "Float")
                    status = "Created"
                elif className == "OPC-TEXT-BAD-FLAG":
                    createBadFlag(parentPath, outputName, itemId, serverName, scanClass, outputNames, "String")
                    status = "Created"
                elif className == "OPC-TEXT-CONDITIONAL-FLOAT-OUTPUT":
                    # This column doesn't sound right for the permissive item id, but I think the columns just got a little 
                    # screwed up during export so the header doesn't match the column
                    permissiveItemId = ds.getValueAt(row, "mode-item-Id")
                    createConditionalOutut(parentPath, outputName, itemId, permissiveItemId, serverName, 
                                            scanClass, outputNames, "Float", "String")
                    status = "Created"
                elif className == "OPC-TEXT-CONDITIONAL-TEXT-OUTPUT":
                    # This column doesn't sound right for the permissive item id, but I think the columns just got a little 
                    # screwed up during export so the header doesn't match the column
                    permissiveItemId = ds.getValueAt(row, "mode-item-Id")
                    createConditionalOutut(parentPath, outputName, itemId, permissiveItemId, serverName, 
                                            scanClass, outputNames, "String", "String")
                    status = "Created"
                elif className == "OPC-PKS-CONTROLLER":
                    modeItemId = ds.getValueAt(row, "mode-item-id")
                    permissiveItemId = ds.getValueAt(row, "mode-permissive-item-id")
                    spItemId = ds.getValueAt(row, "write-target-item-id")
                    # For som ereason that I can't figure out, I couldn't use the column name for this one column...
                    windupItemId = ds.getValueAt(row, 12)
                    print "Output Disposability: ", windupItemId
#                    windupItemId = ds.getValueAt(row, "output-disposability-item-id")
                    createPKSController(parentPath, outputName, itemId, modeItemId, permissiveItemId, spItemId, windupItemId, 
                                        serverName, scanClass, outputNames)
                    status = "Created"                
                elif className == "OPC-PKS-ACE-CONTROLLER":
                    modeItemId = ds.getValueAt(row, "mode-item-id")
                    permissiveItemId = ds.getValueAt(row, "mode-permissive-item-id")
                    spItemId = ds.getValueAt(row, "write-target-item-id")
                    # For som ereason that I can't figure out, I couldn't use the column name for this one column...
                    windupItemId = ds.getValueAt(row, 12)
                    print "Output Disposability: ", windupItemId
#                    windupItemId = ds.getValueAt(row, "output-disposability-item-id")
                    createPKSACEController(parentPath, outputName, itemId, modeItemId, permissiveItemId, spItemId, windupItemId, 
                                        serverName, scanClass, outputNames)
                    status = "Created"
                else:
                    print "Undefined class: ", className
                    status = "Error"

        if status != "":
            ds=system.dataset.setValue(ds, row, "status", status)
    table.data=ds

def createParameter(parentPath, tagName, scanClass, dataType):
    print "Creating a memory tag named: %s, Path: %s, Scan Class: %s" % (tagName, parentPath, scanClass)
    system.tag.addTag(parentPath=parentPath, name=tagName, tagType="MEMORY", dataType=dataType)
    

def createOutput(parentPath, outputName, itemId, serverName, scanClass, names, dataType):
    UDTType='Basic IO/OPC Output'

    print "Creating a %s, Name: %s, Path: %s, Item Id: %s, Scan Class: %s, Server: %s" % (UDTType, outputName, parentPath, itemId, scanClass, serverName)
    system.tag.addTag(parentPath=parentPath, name=outputName, tagType="UDT_INST", 
            attributes={"UDTParentType":UDTType}, 
            parameters={"itemId":itemId, "serverName":serverName, "scanClassName":scanClass, "alternateNames": names})
    
    if string.upper(dataType) == "STRING":
        system.tag.editTag(tagPath=parentPath + "/" + outputName,
                           overrides={"value":{"DataType":"String"}, "writeValue": {"DataType":"String"}})

def createBadFlag(parentPath, outputName, itemId, serverName, scanClass, names, dataType):
    UDTType='Basic IO/OPC Tag Bad Flag'

    print "Creating a %s, Name: %s, Path: %s, Item Id: %s, Scan Class: %s, Server: %s" % (UDTType, outputName, parentPath, itemId, scanClass, serverName)
    system.tag.addTag(parentPath=parentPath, name=outputName, tagType="UDT_INST", 
            attributes={"UDTParentType":UDTType}, 
            parameters={"itemId":itemId, "serverName":serverName, "scanClassName":scanClass, "alternateNames": names})
    
    if string.upper(dataType) == "STRING":
        system.tag.editTag(tagPath=parentPath + "/" + outputName,
                           overrides={"value":{"DataType":"String"}, "writeValue": {"DataType":"String"}})

def createConditionalOutut(parentPath, outputName, itemId, permissiveItemId, serverName, scanClass, names, dataType, permissiveDataType):
    UDTType='Basic IO/OPC Conditional Output'

    print "Creating a %s, Name: %s, Path: %s, Item Id: %s, Scan Class: %s, Server: %s" % (UDTType, outputName, parentPath, itemId, scanClass, serverName)
    system.tag.addTag(parentPath=parentPath, name=outputName, tagType="UDT_INST", 
            attributes={"UDTParentType":UDTType}, 
            parameters={"itemId":itemId, "serverName":serverName, "scanClassName":scanClass, "alternateNames": names,
                        "permissiveItemId": permissiveItemId})
    
    # The default type for the value is Float
    if string.upper(dataType) == "STRING":
        system.tag.editTag(tagPath=parentPath + "/" + outputName,
                           overrides={"value":{"DataType":"String"}, "writeValue": {"DataType":"String"}})

    # The default type for the permissive is string
    if string.upper(dataType) == "FLOAT":
        system.tag.editTag(tagPath=parentPath + "/" + outputName,
                           overrides={"permissive":{"DataType":"Float8"}, 
                                      "permissiveValue": {"DataType":"Float8"},
                                      "permissiveAsFound": {"DataType":"Float8"}})


def createPKSController(parentPath, outputName, itemId, modeItemId, permissiveItemId, spItemId, windupItemId, 
                        serverName, scanClass, names):
    UDTType='Controllers/PKS Controller'

    print "Creating a %s, Name: %s, Path: %s, SP Item Id: %s, Scan Class: %s, Server: %s" % (UDTType, outputName, parentPath, spItemId, scanClass, serverName)
    # Because this generic controller definition is being used by the Diagnostic Toolkit it does not use the PV and OP attributes.  
    # There are OPC tags and just to make sure we don't wreak havoc with the OPC server, these should be disabled
    system.tag.addTag(parentPath=parentPath, name=outputName, tagType="UDT_INST", 
                        attributes={"UDTParentType":UDTType}, 
                        parameters={"itemId":itemId, "serverName":serverName, "scanClassName":scanClass, "spItemId":spItemId,
                                "modeItemId":modeItemId, "permissiveItemId":permissiveItemId,
                                "windupItemId":windupItemId,
                                "alternateNames": names},
                        overrides={"op": {"Enabled":"false"}})
            

def createPKSACEController(parentPath, outputName, itemId, modeItemId, permissiveItemId, spItemId, windupItemId, 
                        serverName, scanClass, names):
    UDTType='Controllers/PKS ACE Controller'

    print "Creating a %s, Name: %s, Path: %s, SP Item Id: %s, Scan Class: %s, Server: %s" % (UDTType, outputName, parentPath, spItemId, scanClass, serverName)
    # Because this generic controller definition is being used by the Diagnostic Toolkit it does not use the PV and OP attributes.  
    # There are OPC tags and just to make sure we don't wreak havoc with the OPC server, these should be disabled
    system.tag.addTag(parentPath=parentPath, name=outputName, tagType="UDT_INST", 
                        attributes={"UDTParentType":UDTType}, 
                        parameters={"itemId":itemId, "serverName":serverName, "scanClassName":scanClass, "spItemId":spItemId,
                                "modeItemId":modeItemId, "permissiveItemId":permissiveItemId,
                                "windupItemId":windupItemId,
                                "alternateNames": names},
                        overrides={"op": {"Enabled":"false"}})

