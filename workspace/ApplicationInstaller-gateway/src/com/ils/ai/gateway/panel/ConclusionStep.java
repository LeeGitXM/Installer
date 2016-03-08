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
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class ConclusionStep extends BasicInstallerStep {
	private static final long serialVersionUID = -3742149120641480873L;


	public ConclusionStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble));

		// Install properties into internal database
		add(new Button("save") {
			private static final long serialVersionUID = 4330778774811578782L;

			public void onSubmit() {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				PersistenceHandler dbHandler = PersistenceHandler.getInstance();
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
				List<PropertyItem> properties = dataHandler.getProperties(data);

				// For starters get the product name
				String productName = "";
				for(PropertyItem prop:properties) {
					if(prop.getName().equalsIgnoreCase("product")) {
						productName  = prop.getValue();
						break;
					};
				}
				if( productName.isEmpty() ) {
					warn("Product name is missing from configured properties. No properties update possible.");
					return;
				}
				for(PropertyItem prop:properties) {
					if(prop.getName().equalsIgnoreCase("product")) continue;
					dbHandler.setProductProperty(productName, prop.getName(), prop.getValue());
				}
				info("You have reached an informed conclusion");
			}
		});
	}
}
