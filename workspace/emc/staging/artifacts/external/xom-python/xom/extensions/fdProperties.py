'''
  Designer/client scope extension functions dealing with FinalDiagnosis instances.
'''
import system
import com.ils.blt.common.ApplicationRequestHandler as ApplicationRequestHandler

handler = ApplicationRequestHandler()

# These methods are usually called in Designer scope. However, we may be using either the
# production or isolation databases. The Gateway makes this call when converting into
# isolation mode.

def rename(uuid,oldName,newName):
    db   = handler.getDatabaseForUUID(uuid)
    SQL = "UPDATE DtFinalDiagnosis SET FinalDiagnosisName= '%s' WHERE FinalDiagnosisName = '%s'" % (newName,oldName)
    system.db.runUpdateQuery(SQL,db)
    

# NOTE: The UUID supplied is from the parent, a diagram. The database interactions
#       are all based on a the block name which is  the data structure.
#
# The aux data structure is a Python list of three dictionaries. These are:
# properties, lists and maplists.
# 
# Fill the aux structure with values from the database.
def getAux(uuid,aux):
    app = handler.getApplicationName(uuid)
    family = handler.getFamilyName(uuid)
    db   = handler.getDatabaseForUUID(uuid)
    
    properties = aux[0]
    lists = aux[1]
    name = properties.get("Name","")
    
    print "fdProperties.getAux()  ...the diagnosis name is: ", family,"/",name

    SQL = "SELECT FD.FinalDiagnosisPriority,FD.CalculationMethod,FD.PostTextRecommendation,"\
          "       FD.TextRecommendationCallback,FD.RefreshRate,FD.TextRecommendation,"\
          "       FD.Active,FD.Explanation,FD.TrapInsignificantRecommendations, "\
          "       FD.FinalDiagnosisId "\
          " FROM DtFinalDiagnosis FD,DtFamily FAM,DtApplication APP "\
          " WHERE APP.applicationId = FAM.applicationId"\
          "   AND APP.applicationName = '%s' "\
          "   AND FAM.familyId = FD.familyId "\
          "   AND FAM.familyName = '%s'"\
          "   AND FAM.familyId = FD.familyId " \
          "   AND FD.finalDiagnosisName = '%s'"\
           % (app,family,name)
    ds = system.db.runQuery(SQL,db)
    finalDiagnosisId = "NONE"
    for rec in ds:
        finalDiagnosisId                               = rec["FinalDiagnosisId"]
        properties["Priority"]                         = rec["FinalDiagnosisPriority"]
        properties["CalculationMethod"]                = rec["CalculationMethod"]
        properties["TextRecommendation"]               = rec["TextRecommendation"]
        properties["PostTextRecommendation"]           = rec["PostTextRecommendation"]
        properties["TextRecommendationCallback"]       = rec["TextRecommendationCallback"]
        properties["RefreshRate"]                      = rec["RefreshRate"]
        properties["Active"]                           = rec["Active"]
        properties["Explanation"]                      = rec["Explanation"]
        properties["TrapInsignificantRecommendations"] = rec["TrapInsignificantRecommendations"]
        
    SQL = "select ApplicationId from DtApplication where ApplicationName = '%s'" % (app)
    applicationId = system.db.runScalarQuery(SQL,db)
    
    # Create lists of QuantOutputs
    # First is the list of all names for the Application
    SQL = "SELECT QuantOutputName "\
          " FROM DtQuantOutput "\
          " WHERE applicationId=%s" % (str(applicationId))
    ds = system.db.runQuery(SQL,db)
    outputs = []
    for record in ds:
        outputs.append(str(record["QuantOutputName"]))
    lists["QuantOutputs"] = outputs
    
    # Next get the list that is used by the diagnosis, if it exists
    outputs = []
    if finalDiagnosisId != "NONE":
        SQL = "SELECT QO.QuantOutputName "\
            " FROM DtQuantOutput QO,DtRecommendationDefinition REC "\
            " WHERE QO.quantOutputId = REC.quantOutputId "\
            "  AND REC.finalDiagnosisId = %s" %(finalDiagnosisId)
        ds = system.db.runQuery(SQL,db)
    
        for record in ds:
            outputs.append(str(record["QuantOutputName"]))
            
    lists["OutputsInUse"] = outputs
    
    print "fdProperties.getAux: properties: ", properties
    print "fdProperties.getAux: lists     : ", lists
    

def setAux(uuid,aux):
    app  = handler.getApplicationName(uuid)
    family = handler.getFamilyName(uuid)
    db   = handler.getDatabaseForUUID(uuid)
    properties = aux[0]
    lists = aux[1]
    name = properties.get("Name","")
    print "fdProperties.setAux()  ...the application/family/diagnosis name is: ",app,"/",family,"/",name
    print "fdProperties.setAux: properties: ", properties
    print "fdProperties.setAux: lists     : ", lists
    
    SQL = "select ApplicationId from DtApplication where ApplicationName = '%s'" % (app)
    applicationId = system.db.runScalarQuery(SQL,db)
    if applicationId == None:
        SQL = "insert into DtApplication (ApplicationName) values (?)"
        applicationId = system.db.runPrepUpdate(SQL, [app], db, getKey=1)
    
    SQL = "SELECT familyId FROM DtFamily "\
          " WHERE ApplicationId = %s"\
          "  AND familyName = '%s'" % (applicationId,family)
    familyId = system.db.runScalarQuery(SQL,db)
    if familyId == None:
        SQL = "INSERT INTO DtFamily (applicationId,familyName,familyPriority) "\
               " VALUES (?, ?, 0.0)"
        familyId = system.db.runPrepUpdate(SQL, [applicationId, family], db, getKey=1)
        
    SQL = "SELECT finalDiagnosisId FROM DtFinalDiagnosis "\
          " WHERE FamilyId = %s"\
          "  AND finalDiagnosisName = '%s'" % (familyId,name)
    fdId = system.db.runScalarQuery(SQL,db)
    if fdId == None:
        SQL = "INSERT INTO DtFinalDiagnosis (familyId,finalDiagnosisName,finalDiagnosisPriority,calculationMethod,"\
               "postTextRecommendation,textRecommendationCallback,refreshRate,textRecommendation,active,explanation,trapInsignificantRecommendations)"\
               " VALUES (?,?,?,?,?,?,?,?,?,?,?)"
        fdId = system.db.runPrepUpdate(SQL, [familyId, name,properties.get("Priority","0.0"),properties.get("CalculationMethod",""),\
                                             properties.get("PostTextRecommendation","0"),properties.get("TextRecommendationCallback",""),\
                                             properties.get("RefreshRate","1.0"),properties.get("TextRecommendation",""),properties.get("Active","0"),properties.get("Explanation","0"),\
                                             properties.get("TrapInsignificantRecommendations","1")], db, getKey=1)
        print "Inserted a new final diagnosis with id: ", fdId
    else:
        SQL = "UPDATE DtFinalDiagnosis SET familyId=?,finalDiagnosisPriority=?,calculationMethod=?," \
            "postTextRecommendation=?,textRecommendationCallback=?,refreshRate=?,textRecommendation=?,active=?,explanation=?,trapInsignificantRecommendations=?"\
            " WHERE finalDiagnosisId = ?"
        system.db.runPrepUpdate(SQL, [familyId, properties.get("Priority","0.0"), properties.get("CalculationMethod",""),\
                                      properties.get("PostTextRecommendation","0"),properties.get("TextRecommendationCallback",""),\
                                      properties.get("RefreshRate","1.0"),properties.get("TextRecommendation",""),properties.get("Active","0"),properties.get("Explanation","0"),\
                                      properties.get("TrapInsignificantRecommendations","1"),fdId],db)
        print "Updated an existing final diagnosis with id: ", fdId
    # Update the list of outputs used
    SQL = "DELETE FROM DtRecommendationDefinition WHERE finalDiagnosisId = %s" % (str(fdId))
    system.db.runUpdateQuery(SQL,db)
    
    olist = lists.get("OutputsInUse")
    instr = None
    for output in olist:
        if instr == None:
            instr = ""
        else:
            instr = instr+","
        instr = instr+"'"+output+"'"
    
    SQL = "INSERT INTO DtRecommendationDefinition(finalDiagnosisId,quantOutputId) "\
          "SELECT %s,quantOutputId FROM DtQuantOutput QO"\
          " WHERE QO.applicationID = %s "\
          "  AND QO.quantOutputName IN (%s)" \
          % (fdId,applicationId,instr)
          
    system.db.runUpdateQuery(SQL,db)
    #print "fdProperties.setAux: ", SQL