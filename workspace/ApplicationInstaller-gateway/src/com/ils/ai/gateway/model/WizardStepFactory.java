/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.panel.BackupStep;
import com.ils.ai.gateway.panel.ConclusionStep;
import com.ils.ai.gateway.panel.InstallWizardStep;
import com.ils.ai.gateway.panel.LicenseStep;
import com.ils.ai.gateway.panel.ModuleStep;
import com.ils.ai.gateway.panel.UpdateProjectStep;
import com.ils.ai.gateway.panel.WelcomeStep;

/**
 *  Given a panel type and data model, create the wizard step.
 */
public class WizardStepFactory  {


	/**
	 * Create a wizard step panel.
	 * @param panelType
	 * @return
	 */
	public InstallWizardStep createStep(int panelIndex,InstallWizardStep prior,WizardStepType stepType,String title,Model<InstallerData> model) {
		InstallWizardStep step = null;
		switch( stepType)  {

			case BACKUP:     step = new BackupStep(panelIndex,prior,title,model);
				break;
			case CONCLUSION: step = new ConclusionStep(panelIndex,prior,title,model);
				break;
			case DATABASE:
				break;
			case EXTERNAL:
				break;
			case GLOBAL:
				break;
			case ICONS:
				break;
			case LICENSE: step = new LicenseStep(panelIndex,prior,title,model);
				break;
			case MODULE: step = new ModuleStep(panelIndex,prior,title,model);
				break;
			case PROJECT: step = new UpdateProjectStep(panelIndex,prior,title,model);
				break;
			case PROPERTIES:
				break;
			case SCANCLASS:
				break;
			case SOURCE:
				break;
			case TAGS:
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

