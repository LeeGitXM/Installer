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
	 * SPlaceholder.
	 */
	@Override
	public void nop( ) {
		try {
			GatewayConnectionManager.getInstance().getGatewayInterface().moduleInvoke(
					MasterBuilderProperties.MODULE_ID, "nop");
			log.debugf("%s.nop ...",TAG);
		}
		catch(Exception ge) {
			log.infof("%s.nop: GatewayException (%s)",TAG,ge.getMessage());
		}
	}
	
}
