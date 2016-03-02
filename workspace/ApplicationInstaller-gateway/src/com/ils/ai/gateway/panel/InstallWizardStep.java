/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PanelData;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;


public class InstallWizardStep extends GatewayWizardStep {
	private static final long serialVersionUID = 6830153148651712890L;
;
	protected final int panelIndex;
	protected final InstallWizardStep prior;
	protected final Model<InstallerData> dataModel;
	protected final PanelData panelData;


	public InstallWizardStep(int index,InstallWizardStep previous,String title,Model<InstallerData> model) {
		super(previous,title, model);
		this.panelIndex = index;
		this.prior = previous;
		this.dataModel = model;
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		this.panelData = dataHandler.getPanelData(panelIndex, dataModel.getObject());
	}


	@Override
	public boolean isLastStep() {
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		boolean last = panelIndex+1>=handler.getStepCount(dataModel.getObject());
		return last;
	}

	@Override
	public IDynamicWizardStep previous() {
		return prior;
	}

	@Override
	public IDynamicWizardStep next() {
		IDynamicWizardStep next = null;
		if( !isLastStep() ) {
			InstallerDataHandler handler = InstallerDataHandler.getInstance();
			next = handler.getWizardStep(panelIndex+1,this,dataModel);
		}
		return next;
	}
}
