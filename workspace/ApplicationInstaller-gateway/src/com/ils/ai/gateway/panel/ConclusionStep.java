/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PanelData;
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

		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		// By definition, this is the last panel. So we know the count.
		int pindex = 0;
		List<PropertyItem> panels = new ArrayList<>();
		while( pindex<panelIndex ) {
			PanelData pdata = handler.getPanelData(pindex,data);
			// Create a PropertyItem for everything that is essential
			if(pdata.isEssential() && pdata.getVersion()!=InstallerConstants.UNSET) {
				String value = "true";  // Up-to-date
				if(pdata.getCurrentVersion()<pdata.getVersion() ) value = "false";
				PropertyItem pi = new PropertyItem(pdata.getTitle(),value);
				panels.add(pi);
			}
			pindex++;
		}
        
		
		
		add(new ListView<PropertyItem>("panels", panels) {
			private static final long serialVersionUID = -4610581829738917953L;

			protected void populateItem(ListItem<PropertyItem> item) {
                PropertyItem property = (PropertyItem) item.getModelObject();
                item.add(new Label("name", property.getName()));
                
                Image checkImage = new Image("check", new ContextRelativeResource("images/check.png"));
                item.add(checkImage);
                boolean check = property.getValue().equalsIgnoreCase("true");
                if(!check) checkImage.setVisible(false);
            }
        });
		
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
				if( !productName.isEmpty() ) {
					for(PropertyItem prop:properties) {
						if(prop.getName().equalsIgnoreCase("product")) continue;
						dbHandler.setProductProperty(productName, prop.getName(), prop.getValue());
					}
					info("You have reached an informed conclusion");
				}
				else {
					warn("Product name is missing from configured properties. No properties update possible.");
				}

			}
		});
	}
}
