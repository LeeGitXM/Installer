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
	private final InstallWizardStep next;

	public InstallWizardStep(int index,InstallWizardStep previous,String title,Model<InstallerData> dataModel) {
        super(previous,title, dataModel);
        this.panelIndex = index;
        this.prior = previous;
        this.next = InstallerDataHandler.getInstance().getWizardStep(panelIndex+1,this,dataModel);
	}

    
	@Override
    public boolean isLastStep() {
        return next==null;
    }

    @Override
    public IDynamicWizardStep previous() {
        return prior;
    }
    
    @Override
    public IDynamicWizardStep next() {
        return next;
    }
}
