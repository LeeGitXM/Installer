/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.mb.gateway;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * A class with some utility methods used by the Master Builder.
 * These deal with XML files. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class XMLUtility {
	private final String TAG = "FileUtility";
	private final LoggerEx log;
	private final GatewayContext context;

	public XMLUtility(GatewayContext ctx) {
		this.context = ctx;
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}

}