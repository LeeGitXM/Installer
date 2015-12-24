/**
 *   (c) 2014  ILS Automation. All rights reserved.
 */
package com.ils.mb.designer;


import com.ils.mb.common.MasterBuilderProperties;
import com.ils.mb.common.MasterBuilderScriptFunctions;
import com.inductiveautomation.ignition.common.expressions.ExpressionFunctionManager;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.inductiveautomation.ignition.designer.model.DesignerContext;

/**
 *  This is the class that is instantiated on startup of the designer. Its purpose
 *  is to populate the project tree with a node for workspace creation.
 */
public class MasterBuilderDesignerHook extends AbstractDesignerModuleHook  {
	private final String TAG = "MasterBuilderDesignerHook";
	private final LoggerEx log;
	

	/**
	 * Constructor:
	 */
	public MasterBuilderDesignerHook() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
	}
	
	@Override
	public void initializeScriptManager(ScriptManager mgr) {
		super.initializeScriptManager(mgr);
		mgr.addScriptModule(MasterBuilderProperties.SCRIPT_PACKAGE,MasterBuilderScriptFunctions.class);
	}
	
	@Override
	public void startup(DesignerContext ctx, LicenseState activationState) throws Exception {
		super.startup(ctx, activationState);
	}
	
	@Override
	public void configureFunctionFactory(ExpressionFunctionManager factory) {
		super.configureFunctionFactory(factory);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
	}
	
}
