/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.ai.gateway;


/**
 *  Define properties that are common to all scopes.
 */
public interface InstallerConstants   {
	public final static String MODULE_ID = "com.ils.installer";       // See module.xml
	public final static String MODULE_NAME = "ApplicationInstaller";  // See module.xml
	public final static String PREFERENCES_NAME = "ApplicationInstaller";
	public final static String SCRIPT_PACKAGE   = "system.ils.installer";   // Python package scripting
	public final static String SCRIPT_RESOURCE  = "sr.script.project";      // Name of the internal scripting resource
	public final static String TIMESTAMP_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS";  // Format for writing timestamps
	
	// Properties that deal with the internals of the module
	public final static String BOM_LOCATION = "artifacts/bom.xml";       // Location of bom within jar
	public final static String MODULE_MARKER = ".application-installer"; // Name of file that marks the module as "the one"
	
	// These are standard properties
	public final static String LOGGING_DATABASE = "loggingdatabase";
	public final static String PROPERTY_DATABASE = "database";
	public final static String PROPERTY_DATE    = "date";
	public final static String PROPERTY_DBMS    = "DBMS";
	public final static String PROPERTY_PRODUCT = "product";
	public final static String PROPERTY_PROVIDER = "provider";
	public final static String PROPERTY_RELEASE = "release"; 
	public final static String PROPERTY_VERSION = "version"; 

	// Property types
	public final static String PROPERTY_TYPE_PRODUCTION = "production";
	public final static String PROPERTY_TYPE_ISOLATION  = "isolation";
	public final static String PROPERTY_TYPE_SECONDARY  = "secondary";
	public final static String PROPERTY_TYPE_BATCH_EXPERT  = "batchexpert";
	public final static String PROPERTY_TYPE_PYSFC         = "pysfc";
	// Property types for setting Gateway defaults
	public final static String PROPERTY_TYPE_ALARM_JOURNAL    = "alarmjournal";
	public final static String PROPERTY_TYPE_ALARM_PROFILE    = "alarmprofile";
	public final static String PROPERTY_TYPE_ALLOW_USER_ADMIN = "allowuseradmin";
	public final static String PROPERTY_TYPE_PROJECT_DEFAULT_DATASOURCE = "projectdefaultdatasource";
	public final static String PROPERTY_TYPE_PROVIDER_DEFAULT_DATASOURCE = "providerdefaultdatasource";
	public final static String PROPERTY_TYPE_ONCALL_ROSTER    = "oncallroster";
	public final static String PROPERTY_TYPE_SMTP_PROFILE     = "smtpprofile";
	
	// Don't allow error messages longer than this ...
	public final static int MAX_ERROR_LENGTH = 4000;
	// Indicator for an integer that has never been set.
	public final static int UNSET = -2;
}
