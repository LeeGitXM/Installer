package com.ils.installermodule.setup;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class StyledPanel extends GatewayWizardStep {

    public StyledPanel(Model<SetupItem> dataModel){
        super(null, BundleUtil.get().getString("ils.welcome.title"), dataModel);
    }

    @Override
    public boolean isLastStep() {
        return true;
    }

    @Override
    public IDynamicWizardStep next() {
        return null;
    }
}
