/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.DirectTextConfigTab;
import com.ils.ai.gateway.InstallWizard;
import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;

/**
 * The ConfigurationPanel implements a menu selection on the Gateway configuration page's
 * left-side selection area.
 * 
 * TabName is menuLocation.getRight()
 * CategoryName is menuLoction.getLeft()
 * Pair.of(category.getName().toLowerCase(), name.toLowerCase())
 */
public class ConfigurationPanel extends ConfigPanel {
	private static final long serialVersionUID = 7356170712706984265L;
	private final IConfigPage page;
	public static final String BUNDLE_NAME = "ApplicationInstaller";
	public static final String BUNDLE_ROOT = "ils";
	static {
    	BundleUtil.get().addBundle(BUNDLE_ROOT, ApplicationInstallerGatewayHook.class, BUNDLE_NAME);
    }
	
	public static DirectTextConfigTab MENU_ENTRY = DirectTextConfigTab.tabBuilder()
	    			.category(ApplicationInstallerGatewayHook.installerCategory)
	    			.name("installer")
	    			.i18n("ils.settings.title")
	    			.page(ConfigurationPanel.class)
	    			.terms(new String[] {"install","installer"})
	    			.build();
    

    public ConfigurationPanel(IConfigPage ipage){
        super("ils.configpanel.title");
        this.page = ipage;
        InstallerData data = new InstallerData();
        InstallWizard wizard = new InstallWizard("setupWizard", page, new Model<>(data));
        add(wizard);
    }
    
    public static void setTitle(String title) {
    	MENU_ENTRY.setTitle(title);
    }

    @Override
    public Pair<String,String> getMenuLocation() {
    	return MENU_ENTRY.getMenuLocation();
    }
    
}
