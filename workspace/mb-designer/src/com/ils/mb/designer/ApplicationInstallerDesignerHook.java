/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.designer;

import java.util.HashMap;
import java.util.Map;

import com.ils.mb.common.MasterBuilderProperties;
import com.ils.mb.common.MasterBuilderRequestHandler;
import com.ils.mb.common.MasterBuilderScriptFunctions;
import com.ils.mb.common.RepositoryScriptingInterface;
import com.ils.mb.common.notification.NotificationHandler;
import com.inductiveautomation.ignition.client.gateway_interface.GatewayConnectionManager;
import com.inductiveautomation.ignition.common.expressions.ExpressionFunctionManager;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;

/**
/**
 * This version of the client hook is designed for use with an
 * application installer. It is NOT for use with the master builder.
 */
public class ApplicationInstallerDesignerHook extends AbstractDesignerModuleHook implements RepositoryScriptingInterface  {
	private NotificationHandler notificationHandler = null;
	private final Map<String,Object> repository;

	/**
	 * Constructor:
	 */
	public ApplicationInstallerDesignerHook() {
		this.repository = new HashMap<>();
	}
	
	@Override
	public void initializeScriptManager(ScriptManager mgr) {
		super.initializeScriptManager(mgr);
		mgr.addScriptModule(MasterBuilderProperties.AI_SCRIPT_PACKAGE,MasterBuilderScriptFunctions.class);
	}
	
	@Override
	public void startup(DesignerContext ctx, LicenseState activationState) throws Exception {
		super.startup(ctx, activationState);
		notificationHandler =new NotificationHandler(ctx);
		GatewayConnectionManager.getInstance().addPushNotificationListener(notificationHandler);
		MasterBuilderScriptFunctions.setNotificationHandler(notificationHandler);
		MasterBuilderScriptFunctions.setRequestHandler(new MasterBuilderRequestHandler(MasterBuilderProperties.AI_MODULE_ID));
		MasterBuilderScriptFunctions.setHook(this);
	}
	
	@Override
	public void configureFunctionFactory(ExpressionFunctionManager factory) {
		super.configureFunctionFactory(factory);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		notificationHandler.clear();
	}
	// ======================================= Repository Scripting Interface ==============================
	// This interface allows the hook to serve as a data store for clients during a single client session.
	@Override
	public Object retrieveFromRepository(String key) {
		return repository.get(key);
	}

	@Override
	public void storeIntoRepository(String key, Object value) {
		repository.put(key, value);
	}

	@Override
	public void removeFromRepository(String key) {
		repository.remove(key);
	}
}
