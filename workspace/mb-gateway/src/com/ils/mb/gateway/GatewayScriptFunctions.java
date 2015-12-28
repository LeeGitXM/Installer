/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.gateway;

import com.ils.mb.gateway.GatewayRequestHandler;


/**
 *  This class exposes the methods available to a gateway script for Master Builder
 *  purposes. 
 *  
 *  These methods mimic MasterBuilderScripting, but must be defined as static methods.
 */
public class GatewayScriptFunctions   {

	private static GatewayRequestHandler handler = null;
	
	public static void setRequestHandler(GatewayRequestHandler h) { handler=h; }
	
	// =============================== Master Builder Functions ==============================
	public static void createInstallerModule(String sourceDirectory,String destinationPath) {
		handler.createInstallerModule(sourceDirectory, destinationPath);
	}
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this locally.
	 */
	public static String getPreference(String key) {
		return handler.getPreference(key);
	}
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	public static void setPreference(String key,String value) {
		handler.setPreference(key,value);
	}
}