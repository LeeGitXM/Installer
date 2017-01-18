/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import org.apache.wicket.model.Model;

import com.ils.ai.gateway.panel.AuthenticationStep;
import com.ils.ai.gateway.panel.BackupStep;
import com.ils.ai.gateway.panel.BasicInstallerPanel;
import com.ils.ai.gateway.panel.ClearStep;
import com.ils.ai.gateway.panel.DatabaseStep;
import com.ils.ai.gateway.panel.DefinitionStep;
import com.ils.ai.gateway.panel.DocumentationStep;
import com.ils.ai.gateway.panel.FileStep;
import com.ils.ai.gateway.panel.IconStep;
import com.ils.ai.gateway.panel.LicenseStep;
import com.ils.ai.gateway.panel.ModuleStep;
import com.ils.ai.gateway.panel.ProjectStep;
import com.ils.ai.gateway.panel.PythonStep;
import com.ils.ai.gateway.panel.ScanClassStep;
import com.ils.ai.gateway.panel.SiteStep;
import com.ils.ai.gateway.panel.SourceStep;
import com.ils.ai.gateway.panel.SummaryStep;
import com.ils.ai.gateway.panel.TagStep;
import com.ils.ai.gateway.panel.ToolkitStep;
import com.ils.ai.gateway.panel.WelcomeStep;

/**
 *  Given a panel type and data model, create the wizard panel.
 */
public class InstallerPanelFactory  {


	/**
	 * Create a wizard step panel.
	 * @param panelType
	 * @return
	 */
	public BasicInstallerPanel createPanel(int panelIndex,BasicInstallerPanel prior,PanelType stepType,String title,Model<InstallerData> model) {
		BasicInstallerPanel step = null;
		switch( stepType)  {

			case AUTHENTICATION:     step = new AuthenticationStep(panelIndex,prior,title,model);
				break;
			case BACKUP:     step = new BackupStep(panelIndex,prior,title,model);
				break;
			case CLEAR:        step = new ClearStep(panelIndex,prior,title,model);
				break;
			case DOCUMENTATION: step = new DocumentationStep(panelIndex,prior,title,model);
				break;
			case DATABASE: step = new DatabaseStep(panelIndex,prior,title,model);
				break;
			case DEFINITION: step = new DefinitionStep(panelIndex,prior,title,model);
				break;	
			case FILE:  step = new FileStep(panelIndex,prior,title,model);
				break;
			case ICON:  step = new IconStep(panelIndex,prior,title,model);
				break;
			case LICENSE: step = new LicenseStep(panelIndex,prior,title,model);
				break;
			case MODULE:  step = new ModuleStep(panelIndex,prior,title,model);
				break;
			case PROJECT: step = new ProjectStep(panelIndex,prior,title,model);
				break;
			case PROPERTY:
				break;
			case PYTHON: step = new PythonStep(panelIndex,prior,title,model);
				break;
			case SCANCLASS: step = new ScanClassStep(panelIndex,prior,title,model);
				break;
			case SITE:      step = new SiteStep(panelIndex,prior,title,model);
				break;
			case SOURCE: step = new SourceStep(panelIndex,prior,title,model);
				break;
			case SUMMARY: step = new SummaryStep(panelIndex,prior,title,model);
				break;
			case TAG: step = new TagStep(panelIndex,prior,title,model);
				break;
			case TOOLKIT: step = new ToolkitStep(panelIndex,prior,title,model);
				break;
			case WELCOME: step = new WelcomeStep(panelIndex,prior,title,model);
				break;
		}
		return step;
	}
}

