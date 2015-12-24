/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

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
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	public static void createInstallerModule(String sourceDirectory,String destinationPath) {
		handler.createInstallerModule(sourceDirectory, destinationPath);
	}
}