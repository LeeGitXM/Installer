/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.mb.common;

import java.util.ArrayList;
import java.util.List;

import org.python.core.PyDictionary;

import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnectionManager;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;


/**
 *  This class is a common point for managing requests to the gateway dealing with the
 *  MasterBuilder, installer creation.
 *  
 *  Each request is relayed to the Gateway scope via an RPC call.
 */
public class MasterBuilderRequestHandler implements MasterBuilderScriptingInterface {
	private final static String TAG = "MasterBuilderRequestHandler";
	private final LoggerEx log;
	private final String moduleId;

	/**
	 * Constructor:
	 */
	public MasterBuilderRequestHandler(String mid)  {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.moduleId = mid;
	}

	// =============================== Master Builder Interface ===============================
	/**
	 * Copy a file. Retain permissions.
	 * @param sourcePath full path for the source file.
	 * @param destinationPath full path for the destination file.
	 */
	@Override
	public void copyFile(String sourcePath,String destinationPath) {
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "copyFile",sourcePath,destinationPath);
		}
		catch(Exception ge) {
			log.infof("%s.copyFile: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	/**
	 * Clear the destination directory, then copy the contents of the 
	 * MasterBuilder module into it. The MasterBuilder contents are read
	 * from the Ignition installation area. 
	 * 
	 * @param destinationPath directory to be created as a valid Ignition module.
	 */
	@Override
	public void copyMasterToDirectory(String destinationPath) {
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "copyMasterToDirectory",destinationPath);
		}
		catch(Exception ge) {
			log.infof("%s.copyMasterToDirectory: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	/**
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	@Override
	public void createInstallerModule(String sourceDirectory,String destinationPath) {
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "createInstallerModule",sourceDirectory,destinationPath);
		}
		catch(Exception ge) {
			log.infof("%s.createInstallerModule: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	/**
	 * Delete a directory and all files underneath it.
	 * @param path directory path.
	 */
	@Override
	public void deleteDirectory(String path) {
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "deleteDirectory",path);
		}
		catch(Exception ge) {
			log.infof("%s.deleteDirectory: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getDatabaseNames() {
		List<String> names = new ArrayList<>();
		try {
			names = (List<String>) GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "getDatabaseNames");
		}
		catch(Exception ge) {
			log.infof("%s.getDatabaseNames: GatewayException (%s)",TAG,ge.getMessage());
		}
		return names;
	}
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this on the gateway (in case it's another machine).
	 */
	@Override
	public String getPreference(String key) {
		String value = "";
		try {
			value = (String) GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "getPreference",key);
		}
		catch(Exception ge) {
			log.infof("%s.getPreference: GatewayException (%s)",TAG,ge.getMessage());
		}
		return value;
	}
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProjectNames() {
		List<String> names = new ArrayList<>();
		try {
			names = (List<String>) GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "getProjectNames");
		}
		catch(Exception ge) {
			log.infof("%s.getProjectNames: GatewayException (%s)",TAG,ge.getMessage());
		}
		return names;
	}
	/**
	 * The PyDictionary returned is directly convertible to a Python dictionary.
	 * @return the named resource from the named project. The resource is 
	 *         guaranteed to be a PyDictionary.
	 */
	public PyDictionary getProjectResource(String projectName,String type) {
		PyDictionary dict = new PyDictionary();
		try {
			dict = (PyDictionary) GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "getProjectResource",projectName,type);
		}
		catch(Exception ge) {
			log.infof("%s.getProjectResource: GatewayException (%s)",TAG,ge.getMessage());
		}
		return dict;
	}
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	@Override
	public void setPreference(String key,String value) {
		if( value==null ) value="";
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "setPreference",key,value);
		}
		catch(Exception ge) {
			log.infof("%s.setPreference: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	/**
	 * Write a string to a file.
	 * @param text the string to write.
	 * @param destinationPath full path for the destination file.
	 */
	public void stringToFile(String text,String destinationPath) {
		if( text==null ) text="";
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					moduleId, "stringToFile",text,destinationPath);
		}
		catch(Exception ge) {
			log.infof("%s.stringToFile: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
}
