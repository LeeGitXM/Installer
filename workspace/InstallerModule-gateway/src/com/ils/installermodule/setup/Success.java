package com.ils.installermodule.setup;

import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class Success extends ConfigPanel {

    public Success(){
        super("ils.success.title");
    }

    @Override
    public String[] getMenuPath() {
        return null;
    }
}
