package com.ils.ai.gateway.panel;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class WelcomeStep extends GatewayWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;

	public WelcomeStep(Model<InstallerData> dataModel){
        super(null, BundleUtil.get().getString("ils.welcome.title"), dataModel);
    }

    @Override
    public boolean isLastStep() {
        return false;
    }

    @Override
    public IDynamicWizardStep next() {
        Model<InstallerData> defaultModel = (Model<InstallerData>) this.getDefaultModel();
        return new SelectProject(defaultModel);
    }
}
