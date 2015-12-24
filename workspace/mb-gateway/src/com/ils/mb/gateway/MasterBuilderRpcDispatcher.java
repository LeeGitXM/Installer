/**
 *   (c) 2014-2015  ILS Automation. All rights reserved.
 */
package com.ils.mb.gateway;

import com.ils.mb.common.MasterBuilderScriptingInterface;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;


/**
 *  The RPC Dispatcher is the point of entry for incoming RCP requests.
 *  Its purpose is simply to parse out a request and send it to the
 *  right handler. This class supports the aggregate of RPC interfaces.
 */
public class MasterBuilderRpcDispatcher implements MasterBuilderScriptingInterface {
	private final static String TAG = "MasterBuilderRpcDispatcher";
	private final LoggerEx log;
	private final GatewayRequestHandler requestHandler;
	
	public MasterBuilderRpcDispatcher(GatewayRequestHandler grh) {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.requestHandler=grh;
	}
	
	//========================== Master Buiukder Scripting Interface ======================
	/**
	 * Nothing.
	 */
	@Override
	public void nop() {
		log.infof("%s.nop ...",TAG);
		requestHandler.nop();
	}
}