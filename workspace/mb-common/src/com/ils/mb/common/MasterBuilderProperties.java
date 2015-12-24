/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.mb.common;


/**
 *  Define properties that are common to all scopes.
 */
public interface MasterBuilderProperties   {
	public final static String MODULE_ID = "com.ils.mb";       // See module.xml
	public final static String MODULE_NAME = "MasterBuilder";  // See module.xml
	public final static String SCRIPT_PACKAGE = "system.ils.mb";   // Python package scripting
	public final static String TIMESTAMP_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS";  // Format for writing timestamps
}
