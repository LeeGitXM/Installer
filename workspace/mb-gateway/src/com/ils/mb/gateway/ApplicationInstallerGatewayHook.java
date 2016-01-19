/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.mb.gateway;

import com.ils.mb.common.MasterBuilderProperties;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;


/**
 * This version of the builder hook is designed for use with an
 * application installer. It is NOT for use with the master builder.
 */
public class ApplicationInstallerGatewayHook extends AbstractGatewayModuleHook   {
	public static String TAG = "ApplicationInstallerGatewayHook";
	private transient MasterBuilderRpcDispatcher dispatcher = null;
	private transient GatewayContext context = null;
	private final LoggerEx log;
	private GatewayRequestHandler requestHandler = null;
	
	public ApplicationInstallerGatewayHook() {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		log.debugf("%s.initializing ...",TAG);
	}
		
	// NOTE: During this period, the module status is LOADED, not RUNNING
	@Override
	public void setup(GatewayContext ctxt) {
		this.context = ctxt;
	}

	@Override
	public void startup(LicenseState licenseState) {

		this.requestHandler = new GatewayRequestHandler(context);
	    this.dispatcher = new MasterBuilderRpcDispatcher(requestHandler);
	    GatewayScriptFunctions.setRequestHandler(requestHandler);
		log.infof("%s.startup: complete.",TAG);
		
		// These are all useful for paths into the installation area,
		log.infof("HOOK: home directory: %s", context.getHome().getAbsolutePath());   // data
		log.infof("HOOK: lib directory: %s", context.getLibDir().getAbsolutePath());  // lib
		log.infof("HOOK: log directory: %s", context.getLogsDir().getAbsolutePath());  // log
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Object getRPCHandler(ClientReqSession session, Long projectId) {
		log.debugf("%s.getRPCHandler - request for project %s",TAG,projectId.toString());
		return dispatcher;
	}
	
	
	@Override
	public void initializeScriptManager(ScriptManager mgr) {
		super.initializeScriptManager(mgr);
		mgr.addScriptModule(MasterBuilderProperties.AI_SCRIPT_PACKAGE,GatewayScriptFunctions.class);
	}
}
