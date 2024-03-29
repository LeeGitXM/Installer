/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.common.persistence.InternalDatabaseHandler;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistenceSession;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 *  This can be used to fetch information from the internal Ignition database. 
 *  We use a Persistent Object interface.
 *   
 *  This class is a singleton for easy access throughout the wizard screens.
 *  WARNING: This class is not serializable and cannot be assigned as an 
 *           instance variable for any page or nested class within a page.
 */
public class PersistenceHandler {
	private final static String CLSS = "PersistenceHandler";
	private static PersistenceHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;
	private InternalDatabaseHandler internalDatabaseHandler;
    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private PersistenceHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
		internalDatabaseHandler = new InternalDatabaseHandler();
	}
	

	/**
	 * Static method to create and/or fetch the single instance.
	 */
	public static PersistenceHandler getInstance() {
		if( instance==null) {
			synchronized(PersistenceHandler.class) {
				instance = new PersistenceHandler();
			}
		}
		return instance;
	}
	
	/**
	 * This step is necessary before the instance is useful. Most other 
	 * properties are initialized lazily.
	 */
	public void setContext(GatewayContext ctx) { 
		this.context=ctx;
	}
	
	/**
	 * Create an alarm journal entry with the given name, but only if it does not already exist.
	 * Create a private session and run in its own thread.
	 * @param name
	 */
	public void addNamedAlarmJournal(String name) {
		PersistenceSession session = null;
		String SQL = "";
		try {
			session = context.getLocalPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			SQL = "SELECT COUNT(*) FROM AlarmJournalSettings WHERE Name = ?" ;
			PreparedStatement statement = cxn.prepareStatement(SQL);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int rows = rs.getInt(1);   // 1-based
			rs.close();
			statement.close();

			if(rows==0) {
				long id = getDatasourceId(cxn);
				long rowid = getMaxRowId("AlarmJournalSettings","AlarmJournalSettings_Id",cxn) + 1;
				SQL = "INSERT INTO AlarmJournalSettings(AlarmJournalSettings_Id,Name,DatasourceId) VALUES(?,?,?)" ;
				statement = cxn.prepareStatement(SQL);
				statement.setLong(1,rowid);
				statement.setString(2,name);
				statement.setLong(3,id);
				statement.executeUpdate();
				statement.close();
			}
		}
		catch(SQLException sqle) {
			log.warn("\n"+SQL+"\n");
			log.warn(String.format("%s.addNamedAlarmJournal: Exception (%s)",CLSS,sqle.getMessage()),sqle);
		}
		finally {
			if(session!=null) session.close();
		}

	}
	/**
	 * Create an alarm notification profile entry with the given name, but only if it does not already exist.
	 * @param name
	 */
	public void addNamedAlarmProfile(String name) {
		PersistenceSession session = null;
		String SQL = "";
		try {
			session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			SQL = "SELECT COUNT(*) FROM AlarmNotificationProfiles WHERE Name = ?" ;
			PreparedStatement statement = cxn.prepareStatement(SQL);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int rows = rs.getInt(1);   // 1-based
			rs.close();
			statement.close();

			if(rows==0) {
				long rowid = getMaxRowId("AlarmNotificationProfiles","AlarmNotificationProfiles_Id",cxn) + 1;
				SQL = "INSERT INTO AlarmNotificationProfiles(AlarmNotificationProfiles_Id,Name,Type) VALUES(?,?,?)" ;
				statement = cxn.prepareStatement(SQL);
				statement.setLong(1,rowid);
				statement.setString(2,name);
				statement.setString(3,"EmailNotificationProfileType");
				statement.executeUpdate();
				statement.close();
			}
		}
		catch(SQLException sqle) {
			log.warn("\n"+SQL+"\n");
			log.warnf("%s.addNamedAlarmProfile: Exception (%s)",CLSS,sqle.getMessage());
		}
		finally {
			if(session!=null) session.close();
		}

	}
	/**
	 * Create an alarm call roster entry with the given name, but only if it does not already exist.
	 * @param name
	 */
	public void addNamedAlarmCallRoster(String name) {
		PersistenceSession session = null;
		String SQL = "";
		try {
			session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			SQL = "SELECT COUNT(*) FROM Roster WHERE Name = ?" ;
			PreparedStatement statement = cxn.prepareStatement(SQL);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int rowCount = rs.getInt(1);   // 1-based
			rs.close();
			statement.close();

			if(rowCount==0) {
				long rowid = getMaxRowId("Roster","Roster_Id",cxn) + 1;
				SQL = "INSERT INTO Roster(Roster_Id,Name) VALUES(?,?)" ;
				statement = cxn.prepareStatement(SQL);
				statement.setLong(1,rowid);
				statement.setString(2,name);
				statement.executeUpdate();
				statement.close();
			}
		}
		catch(SQLException sqle) {
			log.warn("\n"+SQL+"\n");
			log.warnf("%s.addNamedAlarmCallRoster: Exception (%s)",CLSS,sqle.getMessage());
		}
		finally {
			if(session!=null) session.close();
		}
	}
	/**
	 * Create a SMTP entry with the given name, but only if it does not already exist. Fill it with 
	 * reasonable defaults.
	 * @param name
	 */
	public void addNamedSMTPProfile(String name) {
		PersistenceSession session = null;
		String SQL = "";

		try {
			session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			SQL = "SELECT COUNT(*) FROM SMTPSettings WHERE Name = ?" ;

			PreparedStatement statement = cxn.prepareStatement(SQL);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			rs.next();
			int rowCount = rs.getInt(1);   // 1-based
			rs.close();
			statement.close();

			if(rowCount==0) {
				long rowid = getMaxRowId("SMTPSettings","SMTPSettings_Id",cxn) + 1;
				SQL = "INSERT INTO SMTPSettings(SMTPSettings_Id,Name,Hostname,Port) VALUES(?,?,'localhost',25)" ;
				statement = cxn.prepareStatement(SQL);
				statement.setLong(1,rowid);
				statement.setString(2,name);
				statement.executeUpdate();
				statement.close();
			}
		}
		catch(SQLException sqle) {
			log.warn("\n"+SQL+"\n");
			log.warnf("%s.addNamedSMTPProfile: Exception (%s)",CLSS,sqle.getMessage());
		}
		finally {
			if(session!=null) session.close();
		}

	}
	public void setAllowUserAdmin(String value) {
		PersistenceSession session = null;
		String SQL = "Update SysProps set AllowUserAdmin = ?";

		try {
			session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			PreparedStatement statement = cxn.prepareStatement(SQL);
			statement.setBoolean(1,(value.equalsIgnoreCase("true")));
			statement.executeUpdate();
			statement.close();
		}
		catch(SQLException sqle) {
			log.warnf("%s.setAllowUserAdmin: Exception (%s)",CLSS,sqle.getMessage());
		}
		finally {
			if(session!=null) session.close();
		}
	}
	/**
	 * @return a list of properties containing administrative user names and profiles.
	 *         We'll use the first one in the list.
	 *         Properties fields are: "Name" and "ProfileId"
	 */
	public List<Properties> getAdministrativeUsers() {
		List<Properties> propertyList = new ArrayList<>();
		try {
			PersistenceSession session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			String SQL = 
					"SELECT USER.UserName,ROLE.profileId " +
					"FROM  InternalUserTable USER, InternalAuthMappingTable MAP,InternalRoleTable ROLE " +
					"WHERE ROLE.roleName = 'Administrator' " +
					"  AND ROLE.roleId = MAP.roleId" +
					"  AND MAP.userId = USER.userId";
			log.info("\n"+SQL+"\n");
			Statement statement = cxn.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			while (rs.next()) {
				Properties props = new Properties();
				props.setProperty("Name", rs.getString(1));
				props.setProperty("ProfileId", rs.getString(2));
				log.infof("%s.getAdminstrativeUsers: %s (%s)",CLSS,rs.getString(1),rs.getString(2));
				propertyList.add(props); 
			}
			rs.close();
			statement.close();
			session.close();
		}
		catch(SQLException sqle) {
			log.warnf("%s.getAdministrativeUsers: Exception finding admin users (%s)",CLSS,sqle.getMessage());
		}
		return propertyList;
	}
	
	/**
	 * @return a list of authentication profile names. We add a blank entry to the top
	 *         to correspond to "no selection".
	 */
	public synchronized List<String> getProfileNames() {
		List<String> profiles = new ArrayList<>();
		profiles.add("");  // "no selection"
		try {
			PersistenceSession session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			String SQL = "SELECT NAME FROM AUTHPROFILES";
			Statement statement = cxn.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			while (rs.next()) {
				profiles.add(rs.getString("NAME"));  // column 3
			}
			rs.close();
			statement.close();
			session.close();
		}
		catch(SQLException sqle) {
			log.warnf("%s.getProfileNames: Exception finding profiles (%s)",CLSS,sqle.getMessage());
		}
		return profiles;
	}
	/**
	 * @return the last downloaded release number of the specified artifact
	 * On a failure to find the property, an empty string is returned.
	 */
	public String getArtifactRelease(String productName,PanelType type,String subtype,String artifactName) {
		log.infof("%s.getArtifactRelease: Retrieving %s:%s:%s:%s",CLSS,productName,type.name(),subtype,artifactName);
		String value = "";
		try {
			ArtifactReleaseRecord record = context.getPersistenceInterface().find(ArtifactReleaseRecord.META, productName,type.name(),
																												subtype,artifactName);
			if( record!=null) value =  record.getRelease();
		}
		catch(Exception ex) {
			log.warnf("%s.getArtifactRelease: Exception retrieving %s:%s:%s:%s (%s)",CLSS,productName,type.name(),subtype,artifactName,ex.getMessage());
		}
		return value;
	}
	/**
	 * Set the value of a product property. Keys are product name and property name.
	 */
	public void setArtifactRelease(String productName,PanelType type,String subtype,String artifactName,String release) {
		log.infof("%s.setArtifactRelease: Setting %s:%s:%s:%s=%s",CLSS,productName,type.name(),subtype,artifactName,release);
		try {
			ArtifactReleaseRecord record = context.getPersistenceInterface().find(ArtifactReleaseRecord.META, productName,type.name(),
																												subtype,artifactName);
			if( record==null ) record = context.getPersistenceInterface().createNew(ArtifactReleaseRecord.META);
			
			if( record!=null) {
				record.setProductName(productName);
				record.setType(type.name());
				record.setSubType(subtype);
				record.setArtifactName(artifactName);
				record.setRelease(release);
				context.getPersistenceInterface().save(record);
			}
			else {
				log.warnf("%s.setArtifactRelease: %s:%s:%s:%s=%s - failed to create persistence record",CLSS,productName,type.name(),subtype,artifactName,release);
			} 
		}
		catch(Exception ex) {
			log.warnf("%s.setArtifactRelease: Exception setting %s:%s:%s:%s=%s (%s)",CLSS,productName,type.name(),subtype,artifactName,release,ex.getMessage());
		}
	}

	/**
	 * @return the value of a product property. Keys are product name and property name.
	 * On a failure to find the property, an empty string is returned.
	 */
	public String getProductProperty(String productName,String propertyName) {
		log.infof("%s.getProductProperty: Retrieving %s,",CLSS,propertyName);
		String value = "";
		try {
			ProductPropertyRecord record = context.getPersistenceInterface().find(ProductPropertyRecord.META, productName,propertyName);
			if( record!=null) value =  record.getValue();
		}
		catch(Exception ex) {
			log.warnf("%s.getProductProperty: Exception retrieving %s (%s)",CLSS,propertyName,ex.getMessage());
		}
		return value;
	}
	
	/**
	 * Set the value of a product property. Keys are product name and property name.
	 */
	public void setProductProperty(String productName,String propertyName, String value) {
		log.infof("%s.setProductProperty: Setting %s:%s=%s",CLSS,productName,propertyName,value);
		try {
			ProductPropertyRecord record = context.getPersistenceInterface().find(ProductPropertyRecord.META, productName,propertyName);
			if( record==null ) record = context.getPersistenceInterface().createNew(ProductPropertyRecord.META);
			
			if( record!=null) {
				record.setProductName(productName);
				record.setPropertyName(propertyName);
				record.setValue(value);
				context.getPersistenceInterface().save(record);
			}
			else {
				log.warnf("%s.setProductProperty: %s.%s=%s - failed to create persistence record",CLSS,productName,propertyName,value);
			} 
		}
		catch(Exception ex) {
			log.warnf("%s.setProductProperty: Exception setting %s:%s=%s (%s)",CLSS,productName,propertyName,value,ex.getMessage());
		}
	}
	
	// This goes a little overboard by setting all projects.
	public void setDefaultDatasourceForProject() {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String datasource = toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE);
		internalDatabaseHandler.setProjectDatasource(datasource);
	}
	public void setDefaultDatasourceForProvider() {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String datasource = toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE);
		internalDatabaseHandler.setProviderDatasource(datasource);
	}
	
	/**
	 * @return the current installed version of an installation step for a specified product.
	 * Keys are product name, artifact type and artifact sub-type.
	 * On a failure to find the version, return -1;.
	 */
	public int getStepVersion(String productName,PanelType type,String subtype) {
		int version = InstallerConstants.UNSET;
		log.infof("%s.getStepVersion: Retrieving %s:%s:%s,",CLSS,productName,type.name(),subtype);
		try {
			InstalledVersionRecord record = context.getPersistenceInterface().find(InstalledVersionRecord.META, productName,type.name(),subtype);
			if( record!=null) version =  record.getVersion();
		}
		catch(Exception ex) {
			log.warnf("%s.getStepVersion: Exception retrieving %s:%s:%s (%s)",CLSS,productName,type.name(),subtype,ex.getMessage());
		}
		return version;
	}
	

	/**
	 * Set the value of a product version. Keys are product name,artifact type and artifact sub-type..
	 */
	public void setStepVersion(String productName,PanelType type,String subtype,int version)  {;
		log.infof("%s.setStepVersion: Setting %s:%s:%s=%d",CLSS,productName,type.name(),subtype,version);
		try {
			InstalledVersionRecord record = context.getPersistenceInterface().find(InstalledVersionRecord.META,productName,type.name(),subtype);
			if( record==null ) record = context.getPersistenceInterface().createNew(InstalledVersionRecord.META);
			
			if( record==null) record = context.getPersistenceInterface().createNew(InstalledVersionRecord.META);
			if( record!=null) {
				record.setProductName(productName);
				record.setType(type.name());
				record.setSubType(subtype);
				record.setVersion(version);
				context.getPersistenceInterface().save(record);
			}
			else {
				log.warnf("%s.setStepVersion: %s:%s:%s=%d - failed to create persistence record (%s)",CLSS,productName,type,subtype,version,
						InstalledVersionRecord.META.quoteName);
			} 
		}
		catch(Exception ex) {
			log.warnf("%s.setStepVersion: Exception setting %s:%s:%s=%d (%s)",CLSS,productName,type.name(),subtype,version,ex.getMessage());
		}
	}
	
	/**
	 * Query the internal database for a profile that contains all the listed roles.
	 * @return true if such a profile exists.
	 */
	public boolean validateRoleList(List<PropertyItem> roles) {
		boolean valid = false;
		try {
			PersistenceSession session = context.getPersistenceInterface().getSession();
			Connection cxn = session.getJdbcConnection();
			StringBuilder inClause = new StringBuilder("(");
			int count = 0;
			for(PropertyItem item:roles) {
				if( count>0 ) inClause.append(",");
				inClause.append(String.format("'%s'", item.getName()));
				count++;
			}
			inClause.append(")");
			String SQL = 
					"SELECT COUNT(ROLE.profileId) FROM InternalRoleTable ROLE " +
					"WHERE ROLE.roleName IN " + inClause.toString() +
					"  GROUP BY (ROLE.profileId)";
			log.info("\n"+SQL+"\n");
			Statement statement = cxn.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			while (rs.next()) {
				int rowCount = rs.getInt(1);   // 1-based
				if(rowCount>=count) {
					valid = true;
					break;
				}
			}
			rs.close();
			statement.close();
			session.close();
		}
		catch(SQLException sqle) {
			log.warnf("%s.validateRoleList: Exception counting roles (%s)",CLSS,sqle.getMessage());
		}
		return valid;
	}
	
	/**
	 * @param connection a SQLite connection
	 * @return the id associated with the currently configured datasource
	 */
	private long getDatasourceId(Connection cxn) {
		long id = -1;
		String SQL = "";
		try {
			SQL = "SELECT datasources_id FROM datasources DS"
				+ " INNER JOIN ils_toolkit_properties TOOL ON TOOL.value = DS.name"
				+ " AND TOOL.name='Database'";
			Statement statement = cxn.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			rs.next();
			id = rs.getLong(1);   // 1-based
			rs.close();
			statement.close();
		}
		catch(SQLException sqle) {
			log.warn(String.format("\n%s\n",SQL));
			log.warn(String.format("%s.getDatasourceId: Exception (%s)",CLSS,sqle.getMessage()),sqle);
		}
		return id;
	}
	/**
	 * @param tableName
	 * @param keyColumn
	 * @param connection a SQLite connection
	 * @return the table's max row id. This so we can do inserts without violating primary key
	 *         constraints. The tables we encounter have neither INTEGER keys, nor AUTOINCREMENT.
	 */
	private long getMaxRowId(String tableName, String keyColumn, Connection cxn) {
		long id = 0;
		String SQL = "";
		try {
			SQL = String.format("SELECT max(%s) FROM %s",keyColumn,tableName);
			Statement statement = cxn.createStatement();
			ResultSet rs = statement.executeQuery(SQL);
			if( !rs.isClosed() ) {
				rs.next();
				id = rs.getLong(1);   // 1-based
				rs.close();
			}
			statement.close();
		}
		catch(SQLException sqle) {
			log.warn(String.format("\n%s\n",SQL));
			log.warn(String.format("%s.getMaxRowId: Exception (%s)",CLSS,sqle.getMessage()),sqle);
		}
		return id;
	}
}

