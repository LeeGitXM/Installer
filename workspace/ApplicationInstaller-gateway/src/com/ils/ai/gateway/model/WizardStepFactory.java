/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.panel.InstallWizardStep;
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

			case BACKUP:
				break;
			case CONCLUSION:
				break;
			case DATABASE:
				break;
			case EXTERNAL:
				break;
			case ICONS:
				break;
			case LICENSE:
				break;
			case MERGEPROJECT:
				break;
			case MODULE:
				break;
			case PROJECT:
				break;
			case PROPERTIES:
				break;
			case SCANCLASS:
				break;
			case SOURCE:
				break;
			case TAGS:
				break;
			case TRANSACTIONGROUPS:
				break;
			case WELCOME: step = new WelcomeStep(panelIndex,prior,title,model);
				break;
		}
		return step;
	}
}

