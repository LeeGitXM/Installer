package com.ils.ai.gateway.panel;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;


public class InstallWizardStep extends GatewayWizardStep {
	private static final long serialVersionUID = 6830153148651712890L;
	protected final int panelIndex;
	protected final InstallWizardStep prior;
	protected final Model<InstallerData> dataModel;
	// Make transient so that class can be serialized
	protected transient final InstallerDataHandler handler;

	public InstallWizardStep(int index,InstallWizardStep previous,String title,Model<InstallerData> model) {
        super(previous,title, model);
        this.panelIndex = index;
        this.prior = previous;
        this.dataModel = model;
        this.handler = InstallerDataHandler.getInstance();
	}

    
	@Override
    public boolean isLastStep() {
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
    	if( !isLastStep() ) next = handler.getWizardStep(panelIndex+1,this,dataModel);
    	return next;
    }
}
