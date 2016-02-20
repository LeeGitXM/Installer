/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.ai.gateway;


/**
 *  Define properties that are common to all scopes.
 */
public interface ApplicationInstallerProperties   {
	public final static String MODULE_ID = "com.ils.installer";       // See module.xml
	public final static String MODULE_NAME = "ApplicationInstaller";  // See module.xml
	public final static String PREFERENCES_NAME = "ApplicationInstaller";  
	public final static String SCRIPT_PACKAGE = "system.ils.installer";   // Python package scripting
	public final static String TIMESTAMP_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS";  // Format for writing timestamps
}
