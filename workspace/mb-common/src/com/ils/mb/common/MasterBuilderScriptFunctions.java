/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

import java.util.List;

import javax.swing.JTextField;

import org.python.core.PyDictionary;

import com.ils.mb.common.notification.NotificationHandler;


/**
 *  This class exposes the methods available to a designer/client scope for the
 *  Master Builder. It supports "in spirit", the MasterBuilderScriptingInterface. 
 *  
 */
public class MasterBuilderScriptFunctions   {
	private static NotificationHandler notificationHandler = null;
	private static MasterBuilderRequestHandler handler = null;
	private static RepositoryScriptingInterface hook = null;
	
	// =============================== Master Builder Designer ===============================
	public static void setNotificationHandler(NotificationHandler nh) {
		notificationHandler = nh;
	}
	public static void setRequestHandler(MasterBuilderRequestHandler rh) {
		handler = rh;
	}
	/**
	 * Add a text field to the list of components that are updated via push notification
	 * from the Gateway.
	 */
	public static void registerStatusReceiver(JTextField textField) {
		if( notificationHandler!=null) notificationHandler.registerStatusReceiver(textField);
	}
	// =============================== Master Builder Interface ==============================
	/**
	 * Copy a file. Retain permissions.
	 * @param sourcePath full path for the source file.
	 * @param destinationPath full path for the destination file.
	 */
	public static void copyFile(String sourcePath,String destinationPath) {
		handler.copyFile(sourcePath, destinationPath);
	}
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
	 * Delete a directory and all files underneath it.
	 * @param path directory path.
	 */
	public static void deleteDirectory(String path) {
		handler.deleteDirectory(path);
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
	/**
	 * @return the named resource from the named project. The resource is 
	 *         guaranteed to be a PyDictionary.
	 */
	public static PyDictionary getProjectResource(String projectName,String type) {
		return handler.getProjectResource(projectName, type);
	}
	/**
	 * Write a string to a file.
	 * @param text the string to write.
	 * @param destinationPath full path for the destination file.
	 */
	public static void stringToFile(String text,String destinationPath) {
		handler.stringToFile(text, destinationPath);
	}
	// =============================== Repository Interface ==============================
	/**
	 * This must be executed before any other methods.
	 */
	public static void setHook(RepositoryScriptingInterface h) { hook = h; }
	/**
	 * Retrieve a value from the repository.
	 * @return the value associated with the supplied key.
	 */
	public static Object retrieve(String key) {
		return hook.retrieveFromRepository(key);
	}

	/**
	 * Add or replace an entry in the save area (repository)
	 */
	public static void store(String key,Object value) {
		hook.storeIntoRepository(key, value);
	}
	/**
	 * Remove an entry from the repository
	 */
	public static void remove(String key) {
		hook.removeFromRepository(key);
	}
}