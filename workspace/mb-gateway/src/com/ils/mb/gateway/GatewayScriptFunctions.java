/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.gateway;

import org.python.core.PyDictionary;

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
	public static void copyFile(String sourcePath,String destinationPath) {
		handler.copyFile(sourcePath, destinationPath);
	}
	public static void copyMasterToDirectory(String destinationPath) {
		handler.copyMasterToDirectory(destinationPath);
	}
	public static void createInstallerModule(String sourceDirectory,String destinationPath) {
		handler.createInstallerModule(sourceDirectory, destinationPath);
	}
	/**
	 * Delete a directory and all files underneath it.
	 * @param path directory path.
	 */
	public static void deleteDirectory(String path) {
		handler.deleteDirectory(path);
	}
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this locally.
	 */
	public static String getPreference(String key) {
		return handler.getPreference(key);
	}
	/**
	 * @return the named resource from the named project. The resource is 
	 *         guaranteed to be a PyDictionary.
	 */
	public static PyDictionary getProjectResource(String projectName,String type) {
		return handler.getProjectResource(projectName, type);
	}
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	public static void setPreference(String key,String value) {
		handler.setPreference(key,value);
	}

	public static void stringToFile(String text,String destinationPath) {
		handler.stringToFile(text, destinationPath);
	}
}