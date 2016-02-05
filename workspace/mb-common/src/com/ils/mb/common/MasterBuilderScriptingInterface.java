/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

import java.util.List;

import org.python.core.PyDictionary;
import org.w3c.dom.Document;

/**
 *  Define the methods available to Python scripting 
 *  in both designer and gateway scopes.
 */
public interface MasterBuilderScriptingInterface   {
	/**
	 * Copy a file. Retain permissions.
	 * @param sourcePath full path for the source file.
	 * @param destinationPath full path for the destination file.
	 */
	public void copyFile(String sourcePath,String destinationPath);
	/**
	 * Clear the destination directory, then copy the contents of the 
	 * MasterBuilder module into it. The MasterBuilder contents are read
	 * from the Ignition installation area. 
	 * 
	 * @param destinationPath directory to be created as a valid Ignition module.
	 */
	public void copyMasterToDirectory(String destinationPath);
	/**
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	public void createInstallerModule(String sourceDirectory,String destinationPath);
	/**
	 * Delete a directory and all files underneath it.
	 * @param path directory path.
	 */
	public void deleteDirectory(String path);
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	public List<String> getDatabaseNames();
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this locally.
	 */
	public String getPreference(String key);
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	public List<String> getProjectNames();
	/**
	 * Return a resource from the named project. The resource is 
	 *         guaranteed to be a PyDictionary. Assume the resource type
	 *         is sufficient to identify the resource.
	 * @param projectName
	 * @param resource type
	 * @return the resource as a dictionary
	 */
	public PyDictionary getDictionaryResource(String projectName,String type);
	/**
	 * Return a named window resource from the named project. Window resources
	 * are searched for the supplied name. If successful the returned string 
	 * will be valid XML. Otherwise the string will be empty.
	 * @param projectName
	 * @param windowName
	 * @return an XML string representing the window.
	 */
	public String getWindowResource(String projectName,String windowName);
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	public void setPreference(String key,String value);
	/**
	 * Write a string to a file.
	 * @param text the string to write.
	 * @param destinationPath full path for the destination file.
	 */
	public void stringToFile(String text,String destinationPath);
}
