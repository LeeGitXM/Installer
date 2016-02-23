package com.ils.ai.gateway.panel;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;


public class InstallWizardStep extends GatewayWizardStep {
	private static final long serialVersionUID = 6830153148651712890L;
	private final int panelIndex;
	private final InstallWizardStep prior;
	private final Model<InstallerData> dataModel;

	public InstallWizardStep(int index,InstallWizardStep previous,String title,Model<InstallerData> model) {
        super(previous,title, model);
        this.panelIndex = index;
        this.prior = previous;
        this.dataModel = model;
        
	}

    
	@Override
    public boolean isLastStep() {
        return (panelIndex>=InstallerDataHandler.getInstance().getStepCount(dataModel.getObject()));
    }

    @Override
    public IDynamicWizardStep previous() {
        return prior;
    }
    
    @Override
    public IDynamicWizardStep next() {
    	return InstallerDataHandler.getInstance().getWizardStep(panelIndex+1,this,dataModel);
    }
}
