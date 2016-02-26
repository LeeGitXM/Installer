package com.ils.installermodule.setup;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class WelcomeStep extends GatewayWizardStep {

    public WelcomeStep(Model<SetupItem> dataModel){
        super(null, BundleUtil.get().getString("ils.welcome.title"), dataModel);
    }

    @Override
    public boolean isLastStep() {
        return false;
    }

    @Override
    public IDynamicWizardStep next() {
        Model<SetupItem> defaultModel = (Model<SetupItem>) this.getDefaultModel();
        return new SelectProject(defaultModel);
    }
}
