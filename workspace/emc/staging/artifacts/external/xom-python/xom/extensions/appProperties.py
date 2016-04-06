'''
  Designer/client scope extension functions dealing with Application instances.
'''
import system
import com.ils.blt.common.ApplicationRequestHandler as ApplicationRequestHandler

handler = ApplicationRequestHandler()

def rename(uuid,oldName,newName):
    db   = handler.getDatabaseForUUID(uuid)
    SQL = "UPDATE DtApplication SET ApplicationName= '%s' WHERE ApplicationName = '%s'" % (newName,oldName)
    system.db.runUpdateQuery(SQL,db)
    
# These methods are usually called in Designer scope. However, we may be using either the
# production or isolation databases. The Gateway makes this call when converting into
# isolation mode. 
#
# The aux data structure is a Python list of three dictionaries. These are:
# properties, lists and maplists.
# 
# Fill the aux structure with values from the database
def getAux(uuid,aux):
    applicationId = -1
    name = handler.getApplicationName(uuid)
    db   = handler.getDatabaseForUUID(uuid)
    
    properties = aux[0]
    lists      = aux[1]
    maplists   = aux[2]
    
    print "appProperties.getAux  ...the application name is: ", name, ", database is: ",db
    
    SQL = "SELECT A.ApplicationId, A.Description, P.Post, U.UnitName, Q.QueueKey MessageQueue, A.IncludeInMainMenu, L.LookupName GroupRampMethod "\
          " FROM DtApplication A, TkPost P, TkUnit U, QueueMaster Q, Lookup L "\
          " WHERE A.ApplicationName = '%s' " \
          " and A.UnitId = U.UnitId "\
          " and U.PostId = P.PostId "\
          " and A.GroupRampMethodId = L.LookupId "\
          " and A.MessageQueueId = Q.QueueId" % (name)

    pds = system.db.runQuery(SQL,db)
    
    if len(pds) == 0:
        print "Warning: %s was not found in the application table" % (name)
    
    if len(pds) > 1:
        print "Error: more than one application record was found for %s - the last one will be used!" % (name)

    for record in pds:
        applicationId = record["ApplicationId"]
        properties["Description"]=str(record["Description"])
        properties["Post"]=str(record["Post"])
        properties["Unit"]=str(record["UnitName"])
        properties["MessageQueue"]=str(record["MessageQueue"])
        properties["IncludeInMainMenu"]=str(record["IncludeInMainMenu"])
        properties["GroupRampMethod"]=record["GroupRampMethod"]

    # Fetch the list of posts
    SQL = "SELECT Post "\
          " FROM TkPost "\
          " ORDER BY Post"
    ds = system.db.runQuery(SQL,db)
    
    posts = []
    for record in ds:
        posts.append(str(record["Post"]))
    lists["Posts"] = posts
    
    # Fetch the list of units
    SQL = "SELECT UnitName "\
          " FROM TkUnit "\
          " ORDER BY UnitName"
    ds = system.db.runQuery(SQL,db)
    units = []
    for record in ds:
        units.append(str(record["UnitName"]))
    lists["Units"] = units
    
    # Fetch the list of Ramp Methods
    SQL = "SELECT LookupName "\
          " FROM Lookup "\
          " where LookupTypeCode = 'GroupRampMethod' "\
          " ORDER BY LookupName"
    ds = system.db.runQuery(SQL,db)
    methods = []
    for record in ds:
        methods.append(str(record["LookupName"]))
    lists["GroupRampMethods"] = methods
    
    # Fetch the list of Feedback Methods
    SQL = "SELECT LookupName "\
          " FROM Lookup "\
          " where LookupTypeCode = 'FeedbackMethod' "\
          " ORDER BY LookupName"
    ds = system.db.runQuery(SQL,db)
    methods = []
    for record in ds:
        methods.append(str(record["LookupName"]))
    lists["FeedbackMethods"] = methods
    
    # Fetch the list of queues
    SQL = "SELECT QueueKey "\
          " FROM QueueMaster "\
          " ORDER BY QueueKey"
    ds = system.db.runQuery(SQL,db)
    queues = []
    for record in ds:
        queues.append(str(record["QueueKey"]))
    lists["MessageQueues"] = queues
    
    # Fetch the list of Quant outputs
    SQL = "SELECT QuantOutputId, QuantOutputName QuantOutput, TagPath, MostNegativeIncrement, MostPositiveIncrement, MinimumIncrement, SetpointHighLimit,"\
          "SetpointLowLimit, L.LookupName FeedbackMethod, IncrementalOutput "\
          " FROM DtQuantOutput QO, Lookup L  "\
          " WHERE ApplicationId = %s "\
          " and QO.FeedbackMethodId = L.LookupId "\
          " ORDER BY QuantOutput" % (str(applicationId))
    ds = system.db.runQuery(SQL,db)

    maplist = []
    for record in ds:
        rec = {}
        rec["QuantOutputId"]=str(record["QuantOutputId"])
        rec["QuantOutput"]=str(record["QuantOutput"]) 
        rec["TagPath"]=str(record["TagPath"])
        rec["MostNegativeIncrement"]=str(record["MostNegativeIncrement"])
        rec["MostPositiveIncrement"]=str(record["MostPositiveIncrement"])
        rec["MinimumIncrement"]=str( record["MinimumIncrement"])
        rec["SetpointHighLimit"]=str( record["SetpointHighLimit"])
        rec["SetpointLowLimit"]=str(record["SetpointLowLimit"])
        rec["FeedbackMethod"]=str(record["FeedbackMethod"])
        rec["IncrementalOutput"]=str(record["IncrementalOutput"])
        maplist.append(rec)
        
    maplists["QuantOutputs"]=maplist
    
    print "appProperties.getAux: properties: ", properties
    print "appProperties.getAux: lists     : ", lists
    print "appProperties.getAux: maplists  : ", maplists
    print "  ...leaving getAux()!"


# Set values in the database from contents of the aux container
def setAux(uuid,aux):
    applicationName = handler.getApplicationName(uuid)
    db   = handler.getDatabaseForUUID(uuid)
    print "appProperties.setAux  ...the application name is: ", applicationName,", database: ",db
    
    properties = aux[0]
    lists      = aux[1]
    maplists   = aux[2]
    print "Saving properties: ", properties
    print "Saving lists:      ", lists
    print "Saving maplists:   ", maplists
    
    SQL = "select ApplicationId from DtApplication where ApplicationName = '%s'" % (applicationName)
    applicationId = system.db.runScalarQuery(SQL,db)
    
    print "The application Id is: ", applicationId
    
    SQL = "select PostId from TkPost where Post = '%s'" % (properties.get("Post",""))
    postId = system.db.runScalarQuery(SQL,db)
    
    SQL = "select UnitId from TkUnit where UnitName = '%s'" % (properties.get("Unit",""))
    unitId = system.db.runScalarQuery(SQL,db)
    
    SQL = "select QueueId from QueueMaster where QueueKey = '%s'" % (properties.get("MessageQueue",""))
    messageQueueId = system.db.runScalarQuery(SQL,db)
    
    SQL = "select LookupId from Lookup where LookupTypeCode = 'GroupRampMethod' and LookupName = '%s'" % (properties.get("GroupRampMethod",""))
    groupRampMethodId = system.db.runScalarQuery(SQL,db)
    
    if applicationId == None:
        SQL = "insert into DtApplication (ApplicationName, Description, MessageQueueId, GroupRampMethodId, UnitId) "\
            "values (?, ?, ?, ?, ?)"
        applicationId = system.db.runPrepUpdate(SQL, [applicationName, properties.get("Description",""),  
                messageQueueId, groupRampMethodId, unitId], db, getKey=1)
        print "Inserted a new application with id: ", applicationId
    else:
        SQL = "Update DtApplication set ApplicationName = ?, Description = ?, UnitId = ?, MessageQueueId = ?, GroupRampMethodId = ?" \
            " where ApplicationId = ? "
        system.db.runPrepUpdate(SQL, [applicationName, properties.get("Description",""), unitId, 
                messageQueueId, groupRampMethodId, applicationId],db)
        print "Updated an existing application with id: ", applicationId
    
    # Before we add any new quant outputs, fetch the ones that are already there so we can see if the user deleted any
    SQL = "select QuantOutputId from DtQuantOutput where ApplicationId = %s" % (str(applicationId))
    pds = system.db.runQuery(SQL,db)
    quantOutputIds = []
    for record in pds:
        quantOutputId = record["QuantOutputId"]
        quantOutputIds.append(quantOutputId)
    print "The list of existing Quant Output Ids is: ", quantOutputIds
    
    # Now process the quant outputs that are in the list.
    # The list is a list of dictionaries
    outputList = maplists.get("QuantOutputs",[])
    for record in outputList:
        print record
        quantOutputId=record.get("QuantOutputId", -1)
        quantOutputId = int(quantOutputId)
        
        # Update the list of ids so I know which ones to delete at the end
        if quantOutputId in quantOutputIds:
            quantOutputIds.remove(quantOutputId)

        quantOutput=record.get("QuantOutput", "")
        print "Saving Quant Output: ", quantOutput
        tagPath=record.get("TagPath", "")
        feedbackMethod=record.get("FeedbackMethod", 'Simple Sum')
        setpointLowLimit=str(record.get("SetpointLowLimit", 0.0))
        setpointHighLimit=str(record.get("SetpointHighLimit", 100.0))
        minimumIncrement=str(record.get("MinimumIncrement", 0.01))
        mostPositiveIncrement=str(record.get("MostPositiveIncrement", 10.0))
        mostNegativeIncrement=str(record.get("MostNegativeIncrement", -10.0))
        incrementalOutput=record.get("IncrementalOutput", 1)

        SQL = "select LookupId from Lookup where LookupTypeCode = 'FeedbackMethod' and LookupName = '%s'" % (feedbackMethod)
        feedbackMethodId = system.db.runScalarQuery(SQL,db)
        
        print "Id: ", quantOutputId
        print "Tagpath: ", tagPath
        print "Minimum Increment:", minimumIncrement
        print "Most Negative Increment:", mostNegativeIncrement
        print "Most Positive Increment:", mostPositiveIncrement
        
        if quantOutputId < 0:
            print "...inserting..."
            SQL = "insert into DtQuantOutput (QuantOutputName, ApplicationId, TagPath, MostNegativeIncrement, MostPositiveIncrement,"\
                " MinimumIncrement, SetpointHighLimit, SetpointLowLimit, FeedbackMethodId, IncrementalOutput) values (?,?,?,?,?,?,?,?,?,?)"
            system.db.runPrepUpdate(SQL,[quantOutput, applicationId, tagPath, mostNegativeIncrement, mostPositiveIncrement, \
                minimumIncrement, setpointHighLimit, setpointLowLimit, feedbackMethodId, incrementalOutput],db)
        else:
            print "...updating..."
            SQL = "update DtQuantOutput set QuantOutputName = ?, TagPath = ?, MostNegativeIncrement = ?, MostPositiveIncrement = ?,"\
                " MinimumIncrement = ?, SetpointHighLimit = ?, SetpointLowLimit = ?, FeedbackMethodId = ?, IncrementalOutput = ? "\
                " where QuantOutputId = ?"
            system.db.runPrepUpdate(SQL,[quantOutput, tagPath, mostNegativeIncrement, mostPositiveIncrement, \
                minimumIncrement, setpointHighLimit, setpointLowLimit, feedbackMethodId, incrementalOutput, quantOutputId],db)
    
    # Now see if there are some Quant Outputs in the database that were not in the list
    print "Quant Outputs to delete: ", quantOutputIds
    for quantOutputId in quantOutputIds:
        print "Delete ", quantOutputId
        system.db.runUpdateQuery("delete from DtQuantOutput where QuantOutputId = %s" % (str(quantOutputId)),db)