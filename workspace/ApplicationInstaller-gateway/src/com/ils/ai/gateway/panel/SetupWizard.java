package com.ils.ai.gateway.panel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizard;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SetupWizard extends GatewayWizard {
	private static final long serialVersionUID = 7625405250885635937L;

	public SetupWizard(String id, IConfigPage configPage, Model<InstallerData> dataModel){
        super(id, configPage, dataModel);

        GatewayWizardModel wizardModel = new GatewayWizardModel(new WelcomeStep(dataModel));
        this.init(wizardModel);
    }

    @Override
    public void onFinish(IModel iModel, IConfigPage iConfigPage) {

        ApplicationInstallerGatewayHook.getInstance().uninstallMenuNodes(true);
        iConfigPage.setConfigPanel(new Success());
    }
}
