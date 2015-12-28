/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

import java.util.List;

/**
 *  Define the methods available to Python scripting 
 *  in both designer and gateway scopes.
 */
public interface MasterBuilderScriptingInterface   {
	/**
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	public void createInstallerModule(String sourceDirectory,String destinationPath);
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	public List<String> getDatabaseNames();
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	public List<String> getProjectNames();
}
