package com.ils.ai.gateway;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.panel.BasicInstallerStep;
import com.ils.ai.gateway.panel.Success;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizard;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class InstallWizard extends GatewayWizard {
	private static final long serialVersionUID = 7625405250885635937L;

	public InstallWizard(String id, IConfigPage configPage, Model<InstallerData> dataModel){
		super(id, configPage, dataModel);
		
        BasicInstallerStep step = InstallerDataHandler.getInstance().getWizardStep(0,null,dataModel);

		GatewayWizardModel wizardModel = new GatewayWizardModel(step);
		this.init(wizardModel);
	}

	@Override
	public void onFinish(IModel iModel, IConfigPage iConfigPage) {
		iConfigPage.setConfigPanel(new Success());
		ApplicationInstallerGatewayHook.getInstance().uninstallMenuNodes(true);
	}
}
