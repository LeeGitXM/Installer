/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.InstallWizard;
import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;

/**
 * The ConfigurationPanel implements a menu selection on the Gateway configuration page's
 * left-side selection area.
 * 
 * Created by travis.cox on 2/17/2016.
 */
@SuppressWarnings("serial")
public class ConfigurationPanel extends ConfigPanel {
	
	IConfigPage configPage;

    public ConfigurationPanel(IConfigPage configPage){
        super("ils.configpanel.title");

        this.configPage = configPage;
        InstallerData data = new InstallerData();

        InstallWizard wizard = new InstallWizard("setupWizard", configPage, new Model<>(data));
        add(wizard);
    }

    @Override
    public String[] getMenuPath() {
        return null;
    }
    
}
