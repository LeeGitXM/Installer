package com.ils.ai.gateway;

import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.panel.SetupPanel;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.components.LabelConfigMenuNode;
import com.inductiveautomation.ignition.gateway.web.components.LinkConfigMenuNode;

public class ApplicationInstallerGatewayHook extends AbstractGatewayModuleHook {

    public static final String ROOT_NODE = "ils";
    public static final String SETUP_NODE = "setup";

    private static ApplicationInstallerGatewayHook INSTANCE = null;
    private GatewayContext context = null;

    @Override
    public void setup(GatewayContext gatewayContext) {
        INSTANCE = this;
        this.context = gatewayContext;
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        handler.setContext(context);
        BundleUtil.get().addBundle("ils", ApplicationInstallerGatewayHook.class, "ApplicationInstallerProperties");
        LabelConfigMenuNode rootNode = new LabelConfigMenuNode(ROOT_NODE, "ils.menu.root");
        rootNode.setPosition(700);
        LinkConfigMenuNode setupNode = new LinkConfigMenuNode(SETUP_NODE, "ils.menu.root.setup", SetupPanel.class);
        gatewayContext.getConfigMenuModel().addConfigMenuNode(null, rootNode);
        gatewayContext.getConfigMenuModel().addConfigMenuNode(new String[]{ ROOT_NODE }, setupNode);
    }

    public static ApplicationInstallerGatewayHook getInstance(){
        return INSTANCE;
    }

    public GatewayContext getContext(){
        return context;
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
                context.getModuleManager().uninstallModule(ApplicationInstallerProperties.MODULE_ID);
            } 
            catch (Exception ignored) {}
            //BundleUtil.get().removeBundle("ils");
        }
    }
}
