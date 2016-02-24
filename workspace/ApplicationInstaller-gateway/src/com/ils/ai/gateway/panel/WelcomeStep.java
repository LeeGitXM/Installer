package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class WelcomeStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;


	public WelcomeStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        InstallerData data = dataModel.getObject();
        String preamble = InstallerDataHandler.getInstance().getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        RepeatingView listItems = new RepeatingView("properties");
        List<String> properties = InstallerDataHandler.getInstance().getProperties(data);
        for(String prop:properties) {
        	listItems.add(new Label(listItems.newChildId(), prop));
        }
        add(listItems);
    }
	
}
