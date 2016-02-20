package com.ils.installermodule.setup;

import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;
import org.apache.wicket.model.Model;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SetupPanel extends ConfigPanel {

    IConfigPage configPage;

    public SetupPanel(IConfigPage configPage){
        super("ils.setuppanel.title");

        this.configPage = configPage;

        SetupWizard wizard = new SetupWizard("setupWizard", configPage, new Model<>(new SetupItem()));
        add(wizard);
    }

    @Override
    public String[] getMenuPath() {
        return null;
    }
}
