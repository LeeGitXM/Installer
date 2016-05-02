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
	public final static String SCRIPT_PACKAGE = "system.ils.installer";   // Python package scripting
	public final static String TIMESTAMP_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS";  // Format for writing timestamps
	
	// Properties that deal with the internals of the module
	public final static String BOM_LOCATION = "artifacts/bom.xml";       // Location of bom within jar
	public final static String MODULE_MARKER = ".application-installer"; // Name of file that marks the module as "the one"
	
	// These are the standard properties
	public final static String PROPERTY_DATE    = "date";
	public final static String PROPERTY_PRODUCT = "product";
	public final static String PROPERTY_RELEASE = "release"; 
	public final static String PROPERTY_VERSION = "version"; 
	
	// Don't allow error messages longer than this ...
	public final static int MAX_ERROR_LENGTH = 4000;
	// Indicator for an integer that has never been set.
	public final static int UNSET = -2;
}
