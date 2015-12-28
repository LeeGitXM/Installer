/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.gateway;

import java.util.List;

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
	@Override
	public void createInstallerModule(String sourceDirectory,String destinationPath) {
		requestHandler.createInstallerModule(sourceDirectory,destinationPath);
	}
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	public List<String> getDatabaseNames() {
		return requestHandler.getDatabaseNames();
	}
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	public List<String> getProjectNames() {
		return requestHandler.getProjectNames();
	}
}