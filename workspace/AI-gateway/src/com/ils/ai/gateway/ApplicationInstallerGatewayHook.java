/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ils.ai.gateway.model.ArtifactReleaseRecord;
import com.ils.ai.gateway.model.InstalledVersionRecord;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.ProductPropertyRecord;
import com.ils.ai.gateway.panel.ConfigurationPanel;
import com.ils.common.persistence.ToolkitRecord;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.models.ConfigCategory;
import com.inductiveautomation.ignition.gateway.web.models.IConfigTab;


public class ApplicationInstallerGatewayHook extends AbstractGatewayModuleHook {
	private final static String CLSS = "ApplicationInstallerGatewayHook";
	public static final String BUNDLE_NAME = "ApplicationInstaller";
	public static final String BUNDLE_ROOT = "ils";
	private static final String CATEGORY_NAME = "InstallerCategory";
	public static final ConfigCategory installerCategory = new ConfigCategory(CATEGORY_NAME,BUNDLE_ROOT+".menu.category");

    private static ApplicationInstallerGatewayHook INSTANCE = null;
    private GatewayContext context = null;
    private final LoggerEx log = LogUtil.getLogger(getClass().getPackage().getName());
    
    static {
    	BundleUtil.get().addBundle(BUNDLE_ROOT, ApplicationInstallerGatewayHook.class, BUNDLE_NAME);
    }

    @Override
    public boolean isFreeModule() { return true; }
    
    @Override
    public void setup(GatewayContext gatewayContext) {
        INSTANCE = this;
        this.context = gatewayContext;
        PersistenceHandler.getInstance().setContext(context);
    	InstallerDataHandler handler = InstallerDataHandler.getInstance();
        handler.setContext(context);
        String title = handler.getTitle(new InstallerData());
        ConfigurationPanel.setTitle(title);
       
        
		// Create ProductProperties and ProductVersion tables in the internal database if necessary.
        try {
            context.getSchemaUpdater().updatePersistentRecords(ArtifactReleaseRecord.META);
            context.getSchemaUpdater().updatePersistentRecords(InstalledVersionRecord.META);
            context.getSchemaUpdater().updatePersistentRecords(ProductPropertyRecord.META);
            context.getSchemaUpdater().updatePersistentRecords(ToolkitRecord.META);
        }
        catch (SQLException sqle) {
            log.errorf("%s.setup: Error generating product tables (%s).",CLSS,sqle.getLocalizedMessage());
        }
    }

    public static ApplicationInstallerGatewayHook getInstance(){
        return INSTANCE;
    }
    
    public GatewayContext getContext() {
    	return this.context;
    }
    
    @Override
    public List<ConfigCategory> getConfigCategories() {
    	List<ConfigCategory> categories = new ArrayList<>();
    	categories.add(installerCategory);
    	return categories;
    }
    
    /**
     * Add a configuration panel that starts the install.
     * Give it a custom title derived from the package.
     */
    @Override
    public List<IConfigTab> getConfigPanels() {
    	List<IConfigTab>panels = new ArrayList<>();
    	panels.add(ConfigurationPanel.MENU_ENTRY);
    	return panels;
    }
 
    @Override
    public void startup(LicenseState licenseState) {

    }

    @Override
    public void shutdown() {
    }
}
