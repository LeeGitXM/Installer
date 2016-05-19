/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.wicket.Application;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.time.Duration;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PanelData;
import com.ils.ai.gateway.model.PanelType;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;


/**
 * This class represents a panel in the Installer wizard.
 */
public class BasicInstallerPanel extends GatewayWizardStep {
	private static final long serialVersionUID = 6830153148651712890L;
	protected static final int UNSET = InstallerConstants.UNSET;  // For integer parameters that have no value.
	protected final int panelIndex;
	protected final BasicInstallerPanel prior;
	protected final Model<InstallerData> dataModel;
	protected final InstallerData data;
	protected final PanelData panelData;
	protected final String product;
	protected String preamble = "";
	protected String subtype = "";
	protected PanelType type = PanelType.SUMMARY;
	protected int futureVersion = InstallerConstants.UNSET;
	
	protected String currentVersionString = "";
	protected String futureVersionString = "";


	public BasicInstallerPanel(int index,BasicInstallerPanel previous,String title,Model<InstallerData> model) {
		super(previous,title, model);
		this.panelIndex = index;
		this.prior = previous;
		this.dataModel = model;
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		this.data = dataModel.getObject();
		this.panelData = dataHandler.getPanelData(panelIndex,data);
		int vers = panelData.getCurrentVersion();
		if(vers!=UNSET) currentVersionString = String.valueOf(vers);
		this.futureVersion = panelData.getVersion();
		if( futureVersion!=UNSET ) futureVersionString = String.valueOf(futureVersion);
		Application.get().getRequestCycleSettings().setTimeout(Duration.minutes(5));
		
		// Retrieve some generic attributes ...
		preamble = dataHandler.getStepPreamble(index, data);
		product  = dataHandler.getProductName(data);
		type     = dataHandler.getStepType(panelIndex, data);
		subtype  = dataHandler.getStepSubtype(panelIndex, data);
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
			next = handler.getNextPanel(panelIndex+1,this,dataModel);
		}
		return next;
	}
	
	protected String fileNameFromLocation(String loc) {
		String fname = loc;
		int pos = fname.lastIndexOf(File.separator);
		if( pos>0 ) fname = fname.substring(pos+1);
		return fname;
	}
}
