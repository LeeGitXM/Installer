package com.ils.ai.gateway;

import java.sql.SQLException;

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
import com.inductiveautomation.ignition.gateway.web.components.LinkConfigMenuNode;

public class ApplicationInstallerGatewayHook extends AbstractGatewayModuleHook {
	private final static String CLSS = "ApplicationInstallerGatewayHook";
	public static final String ROOT_NODE = "ils";
    public static final String SETUP_NODE = "setup";

    private static ApplicationInstallerGatewayHook INSTANCE = null;
    private GatewayContext context = null;
    private final LoggerEx log = LogUtil.getLogger(getClass().getPackage().getName());
    
    static {
    	BundleUtil.get().addBundle("ils", ApplicationInstallerGatewayHook.class, "ApplicationInstaller");
    }

    @Override
    public void setup(GatewayContext gatewayContext) {
        INSTANCE = this;
        this.context = gatewayContext;
        PersistenceHandler.getInstance().setContext(context);
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        handler.setContext(context);
        String title = handler.getTitle(new InstallerData());
        log.infof("%s.setup: Custom title = %s",CLSS,title);
        InstallerLabelConfigMenuNode rootNode = new InstallerLabelConfigMenuNode(ROOT_NODE, title);
        LinkConfigMenuNode setupNode = new LinkConfigMenuNode(SETUP_NODE, "ils.menu.root.setup", ConfigurationPanel.class);
        log.infof("%s.setup: ils.configpanel.title = %s",CLSS,BundleUtil.get().getString("ils.configpanel.title"));
        gatewayContext.getConfigMenuModel().addConfigMenuNode(null, rootNode);
        gatewayContext.getConfigMenuModel().addConfigMenuNode(new String[]{ ROOT_NODE }, setupNode);
        
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
    public void startup(LicenseState licenseState) {

    }

    @Override
    public void shutdown() {
        uninstallMenuNodes(false);
    }

    public void uninstallMenuNodes(boolean uninstallModule){
        context.getConfigMenuModel().removeConfigMenuNode(new String[]{ ROOT_NODE });
        context.getConfigMenuModel().removeConfigMenuNode(new String[]{ ROOT_NODE, SETUP_NODE });

        if(uninstallModule) {
            try {
                context.getModuleManager().uninstallModule(InstallerConstants.MODULE_ID);
            } 
            catch (Exception ignored) {}
        }
    }
}
