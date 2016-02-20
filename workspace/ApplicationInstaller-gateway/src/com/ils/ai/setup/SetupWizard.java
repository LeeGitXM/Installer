package com.ils.ai.setup;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizard;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SetupWizard extends GatewayWizard {
	private static final long serialVersionUID = 7625405250885635937L;

	public SetupWizard(String id, IConfigPage configPage, Model<SetupItem> dataModel){
        super(id, configPage, dataModel);

        GatewayWizardModel wizardModel = new GatewayWizardModel(new WelcomeStep(dataModel));
        this.init(wizardModel);
    }

    @Override
    public void onFinish(IModel iModel, IConfigPage iConfigPage) {
        SetupItem setup = (SetupItem) iModel.getObject();

        ApplicationInstallerGatewayHook.getInstance().uninstallMenuNodes(true);
        iConfigPage.setConfigPanel(new Success());
    }
}
