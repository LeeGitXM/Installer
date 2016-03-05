/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.panel.BackupStep;
import com.ils.ai.gateway.panel.ConclusionStep;
import com.ils.ai.gateway.panel.InstallerStep;
import com.ils.ai.gateway.panel.LicenseStep;
import com.ils.ai.gateway.panel.ModuleStep;
import com.ils.ai.gateway.panel.ProjectStep;
import com.ils.ai.gateway.panel.TagStep;
import com.ils.ai.gateway.panel.ToolkitStep;
import com.ils.ai.gateway.panel.WelcomeStep;

/**
 *  Given a panel type and data model, create the wizard step.
 */
public class InstallerStepFactory  {


	/**
	 * Create a wizard step panel.
	 * @param panelType
	 * @return
	 */
	public InstallerStep createStep(int panelIndex,InstallerStep prior,PanelType stepType,String title,Model<InstallerData> model) {
		InstallerStep step = null;
		switch( stepType)  {

			case BACKUP:     step = new BackupStep(panelIndex,prior,title,model);
				break;
			case CONCLUSION: step = new ConclusionStep(panelIndex,prior,title,model);
				break;
			case DATABASE:
				break;
			case EXTERNAL:
				break;
			case ICONS:
				break;
			case LICENSE: step = new LicenseStep(panelIndex,prior,title,model);
				break;
			case MODULE: step = new ModuleStep(panelIndex,prior,title,model);
				break;
			case PROJECT: step = new ProjectStep(panelIndex,prior,title,model);
				break;
			case PROPERTIES:
				break;
			case SCANCLASS:
				break;
			case SOURCE:
				break;
			case TAGS: step = new TagStep(panelIndex,prior,title,model);
				break;
			case TOOLKIT: step = new ToolkitStep(panelIndex,prior,title,model);
				break;
			case TRANSACTIONGROUPS:
				break;
			case WELCOME: step = new WelcomeStep(panelIndex,prior,title,model);
				break;
		}
		return step;
	}
}

