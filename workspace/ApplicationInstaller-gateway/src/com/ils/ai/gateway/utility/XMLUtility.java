/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.ai.gateway.utility;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/**
 * A class with some utility methods used by the Application Installer.
 * These deal with XML files. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class XMLUtility {
	private final String TAG = "XMLUtility";
	private final LoggerEx log;

	public XMLUtility() {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}

}