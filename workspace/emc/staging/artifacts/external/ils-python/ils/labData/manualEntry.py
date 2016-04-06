'''
Created on Apr 30, 2015

@author: Pete
'''
import system
log = system.util.getLogger("com.ils.labData")

# Everything in this module runs in the client, so the use of loggers isn't too important since nothing will go 
# to the wrapper log anyway.

# This is called from the "Manual Entry" button on the "Lab Data Table Chooser" screen
def launchChooser(rootContainer):
    print "Launching the Manual Lab Data Entry Chooser Screen"
    post = rootContainer.selectedPost
    window=system.nav.openWindow("Lab Data/Manual Entry Value Chooser",{"post": post})
    system.nav.centerWindow(window)

  
def chooserInitialization(rootContainer):
    print "In ils.labData.manualEntry.chooserInitialization()..."
    post = rootContainer.post
    
    communicationHealthy = system.tag.read("Configuration/LabData/communicationHealthy").value
    manualEntryPermitted = system.tag.read("Configuration/LabData/manualEntryPermitted").value
    from ils.common.user import isAE
    isAE = isAE()
    
    if not(communicationHealthy) or manualEntryPermitted or isAE:
        # Select every lab data value EXCEPT derived date
        SQL = "select V.ValueName, V.ValueId "\
            " from LtValue V, TkUnit U, TkPost P "\
            " where V.UnitId = U.UnitId "\
            " and U.PostId = P.PostId "\
            " and P.Post = '%s' "\
            " and V.ValueId not in (select ValueId from LtDerivedValue) "\
            " order by ValueName" % (post) 
    else: 
        SQL = "select V.ValueName, V.ValueId "\
            " from LtLocalValue LV, LtValue V, TkUnit U, TkPost P "\
            " where LV.ValueId = V.ValueId "\
            " and V.UnitId = U.UnitId "\
            " and U.PostId = P.PostId "\
            " and P.Post = '%s' "\
            " order by ValueName" % (post) 
    
    pds = system.db.runQuery(SQL)
    
    chooseList = rootContainer.getComponent("List")
    chooseList.data = pds
    chooseList.selectedIndex = -1

# This is call from the "Enter Data" button on the "Manual Entry Value Chooser" screen
def launchEntryForm(rootContainer):
    print "Launching the Manual Lab Data Entry Form"
    chooseList = rootContainer.getComponent("List")
    ds=chooseList.data
    idx=chooseList.selectedIndex
    if idx < 0:
        system.gui.warningBox("Please select a Lab Parameter and then press 'Enter Data'")
        return
    valueName=ds.getValueAt(idx,'ValueName')
    valueId=ds.getValueAt(idx,'ValueId')
    
    print "Editing %s - %s" % (valueName, str(valueId))
    
    window=system.nav.openWindow("Lab Data/Manual Entry",{"valueName": valueName, "valueId":valueId})
    system.nav.centerWindow(window)
    
def entryFormInitialization(rootContainer):
    print "In ils.labData.manualEntry.entryFormInitialization()..."
    
    valueId = rootContainer.valueId
    valueName = rootContainer.valueName
    
    # Fetch the unit for this value
    SQL = "select UnitName from TkUnit U, LtValue V "\
        " where V.UnitId = U.UnitId and V.ValueId = %s" % (str(valueId))
    pds = system.db.runQuery(SQL)
    if len(pds) != 1:
        system.gui.errorBox("Error fetching the unit for this lab data!")
        return
    record=pds[0]
    unitName = record["UnitName"]
    rootContainer.unitName = unitName
    
    # Fetch the limits for this value
    SQL = "select * from LtLimit where ValueId = %s" % (str(valueId))
    print SQL
    pds = system.db.runQuery(SQL)

    if len(pds) == 1:
        record=pds[0]
        
        # Validity Limits
        if record["UpperValidityLimit"] == None:
            rootContainer.upperValidityLimitEnabled=False
        else:
            rootContainer.upperValidityLimitEnabled=True
            rootContainer.upperValidityLimit = record["UpperValidityLimit"]

        if record["LowerValidityLimit"] == None:
            rootContainer.lowerValidityLimitEnabled=False
        else:
            rootContainer.lowerValidityLimitEnabled=True
            rootContainer.lowerValidityLimit = record["LowerValidityLimit"]

        # SQC Limits
        if record["UpperSQCLimit"] == None:
            rootContainer.upperSQCLimitEnabled=False
        else:
            rootContainer.upperSQCLimitEnabled=True
            rootContainer.upperSQCLimit = record["UpperSQCLimit"]

        if record["LowerSQCLimit"] == None:
            rootContainer.lowerSQCLimitEnabled=False
        else:
            rootContainer.lowerSQCLimitEnabled=True
            rootContainer.lowerSQCLimit = record["LowerSQCLimit"]
        
        # Release Limits
        if record["UpperReleaseLimit"] == None:
            rootContainer.upperReleaseLimitEnabled=False
        else:
            rootContainer.upperReleaseLimitEnabled=True
            rootContainer.upperReleaseLimit = record["UpperReleaseLimit"]

        if record["LowerReleaseLimit"] == None:
            rootContainer.lowerReleaseLimitEnabled=False
        else:
            rootContainer.lowerReleaseLimitEnabled=True
            rootContainer.lowerReleaseLimit = record["LowerReleaseLimit"]
        
    else:
        print "Error fetching limits "
        rootContainer.upperValidityLimitEnabled=False
        rootContainer.lowerValidityLimitEnabled=False
        rootContainer.upperSQCLimitEnabled=False
        rootContainer.lowerSQCLimitEnabled=False
        rootContainer.upperReleaseLimitEnabled=False
        rootContainer.lowerReleaseLimitEnabled=False

# This is called when the operator presses the 'Enter' button on the Manual Entry screen
def entryFormEnterData(rootContainer, db = ""):
    print "In ils.labData.limits.manualEntry.entryFormEnterData()"
    
    sampleTime = rootContainer.getComponent("Sample Time").date
    sampleValue = rootContainer.getComponent("Lab Value Field").floatValue
    
    valueId = rootContainer.valueId
    valueName = rootContainer.valueName
    unitName = rootContainer.unitName
    
    upperValidityLimit = rootContainer.upperValidityLimit
    lowerValidityLimit = rootContainer.lowerValidityLimit

    print "The validity limits are from ", lowerValidityLimit, " to ", upperValidityLimit
    
    if lowerValidityLimit != None and lowerValidityLimit != "":
        if sampleValue < lowerValidityLimit:
            system.gui.errorBox("The value you entered, %s, must be greater than the lower validity limit, %s, please correct and press 'Enter'" % (str(sampleValue), str(lowerValidityLimit)))
            return
    
    if upperValidityLimit != None and upperValidityLimit != "":
        if sampleValue > upperValidityLimit:
            system.gui.errorBox("The value you entered, %s, must be less than the upper validity limit, %s, please correct and press 'Enter'" % (str(sampleValue), str(upperValidityLimit)))
            return

    # Check for an exact duplicate with the same value and time
    SQL = "select count(*) from LtHistory where ValueId = ? and SampleTime = ? and rawValue = ?" 
    print SQL
    pds = system.db.runPrepQuery(SQL, [valueId, sampleTime, sampleValue])
    count = pds[0][0]
    if count > 0:
        system.gui.warningBox("This result has already been entered!")
        return

    # Store the value locally in the X O M database
    from ils.labData.scanner import storeValue 
    storeValue(valueId, valueName, sampleValue, sampleTime, log, db)
    
    # Store the value in the Lab Data UDT memory tags, which are local to Ignition
    from ils.common.config import getTagProvider
    provider = getTagProvider()
    
    from ils.labData.scanner import updateTags
    tags, tagValues = updateTags(provider, unitName, valueName, sampleValue, sampleTime, True, True, [], [], log)
    print "Writing ", tagValues, " to ", tags
    system.tag.writeAll(tags, tagValues)
    
    # If the lab datum is "local" then write the value to PHD (use a regular OPC write, so we won't 
    # capture the sample time)
    SQL = "select LV.ItemId, WL.ServerName "\
        " from LtLocalValue LV, TkWriteLocation WL "\
        " where LV.ValueId = %s "\
        " and LV.WriteLocationId = WL.WriteLocationId" % (str(valueId))
 
    pds = system.db.runQuery(SQL, db)
    if len(pds) != 0:
        record = pds[0]
        itemId = record["ItemId"]
        serverName = record["ServerName"]
        
        # Check if writing is enabled
        labDataWriteEnabled=system.tag.read("[]Configuration/LabData/labDataWriteEnabled").value
        globalWriteEnabled=True
        writeEnabled = labDataWriteEnabled and globalWriteEnabled
        
        if writeEnabled:
            print "Writing local value %s for %s to %s" % (str(sampleValue), valueName, itemId)
            returnQuality = system.opc.writeValue(serverName, itemId, sampleValue)
            if returnQuality.isGood():
                print "Write <%s> to %s-%s for %s local lab data was successful" % (str(sampleValue), serverName, itemId, valueName)
            else:
                print "ERROR: Write <%s> to %s-%s for %s local lab data failed" % (str(sampleValue), serverName, itemId, valueName)
        else:
            print "*** Skipping *** Write of local value %s for %s to %s" % (str(sampleValue), valueName, itemId)
    else:
        print "Skipping write of manual lab data because it is not LOCAL (%s %s %s %s)" % (str(sampleValue), serverName, itemId, valueName)
    
    # There is a cache of last values but we can't update it from here because the cache is in the gateway...
    
    system.gui.messageBox("Lab value of %s has been stored for %s!" % (str(sampleValue), valueName))
    