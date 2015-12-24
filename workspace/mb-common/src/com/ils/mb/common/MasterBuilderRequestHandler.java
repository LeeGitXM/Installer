/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.mb.common;

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

	/**
	 * Constructor:
	 */
	public MasterBuilderRequestHandler()  {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}

	// =============================== Master Builder Interface ===============================
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
					MasterBuilderProperties.MODULE_ID, "createInstallerModule",sourceDirectory,destinationPath);
		}
		catch(Exception ge) {
			log.infof("%s.createInstallerModule: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
}
