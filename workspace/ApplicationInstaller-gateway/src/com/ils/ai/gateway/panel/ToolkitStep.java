/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Take properties from the bill of materials and add to the internal database.
 */
public class ToolkitStep extends BasicInstallerStep {
	private static final long serialVersionUID = -3742149120641480873L;

	public ToolkitStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Install properties into internal database
		add(new Button("install") {
			private static final long serialVersionUID = 4330778774811578782L;
			
			public void onSubmit() {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
            	
            	
            	for(PropertyItem prop:properties) {
            		toolkitHandler.setToolkitProperty(prop.getName(), prop.getValue());
            	}
            	ToolkitStep.this.info(String.format("Update of internal database complete."));
            	PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            }
        });
	}
}
