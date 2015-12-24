/**
 *   (c) 2016  ILS Automation. All rights reserved.  
 */
package com.ils.mb.gateway;

import com.ils.mb.common.MasterBuilderScriptingInterface;
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
	 * Zero.
	 */
	@Override
	public void nop() {}

	
}
