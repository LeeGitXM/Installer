package com.ils.ai.gateway.panel;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class ModuleStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;


	public ModuleStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
    }
}
