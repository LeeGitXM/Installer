/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

import java.util.List;

import com.ils.mb.common.MasterBuilderRequestHandler;


/**
 *  This class exposes the methods available to a designer/client scope for the
 *  Master Builder. It supports "in spirit", the MasterBuilderScriptingInterface. 
 *  
 */
public class MasterBuilderScriptFunctions   {

	private static MasterBuilderRequestHandler handler = new MasterBuilderRequestHandler();
	
	// =============================== Master Builder Functions ==============================
	/**
	 * Clear the destination directory, then copy the contents of the 
	 * MasterBuilder module into it. The MasterBuilder contents are read
	 * from the Ignition installation area. 
	 * 
	 * @param destinationPath directory to be created as a valid Ignition module.
	 */
	public static void copyMasterToDirectory(String destinationPath) {
		handler.copyMasterToDirectory(destinationPath);
	}
	/**
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	public static void createInstallerModule(String sourceDirectory,String destinationPath) {
		handler.createInstallerModule(sourceDirectory, destinationPath);
	}
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	public static List<String> getDatabaseNames() {
		return handler.getDatabaseNames();
	}
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this locally.
	 */
	public static String getPreference(String key) {
		return handler.getPreference(key);
	}
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	public static List<String> getProjectNames() {
		return handler.getProjectNames();
	}
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	public static void setPreference(String key,String value) {
		handler.setPreference(key,value);
	}
}