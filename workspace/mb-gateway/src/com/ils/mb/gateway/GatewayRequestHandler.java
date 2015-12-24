/**
 *   (c) 2016  ILS Automation. All rights reserved.  
 */
package com.ils.mb.gateway;

import com.ils.mb.common.MasterBuilderScriptingInterface;
import com.ils.mb.gateway.jar.IgnitionModule;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 *  This class is a common point for managing requests in the gateway dealing with the
 *  master builders. It is designed for use by Java code in the  
 *  Gateway RPC handler, as well as Python scripting.
 *  
 *  Unlike its counterpart class in the Designer/Client scopes, this class accesses
 *  methods without RPC calls.
 */
public class GatewayRequestHandler implements MasterBuilderScriptingInterface {
	private final static String TAG = "GatewayRequestHandler";
	private final LoggerEx log;
	private final GatewayContext context;
	
	/**
	 * Constructor:
	 */
	public GatewayRequestHandler(GatewayContext ctx)  {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.context = ctx;
		
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
		// Run in the background
		IgnitionModule module = new IgnitionModule(sourceDirectory,destinationPath);
		new Thread(module).start();
	}

	
}
