/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PanelData;
import com.ils.ai.gateway.model.PropertyItem;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SummaryStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;


	public SummaryStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble).setEscapeModelStrings(false));

		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		// By definition, this is the last panel. So we know the count.
		int pindex = 0;
		
		String site= dataModel.getObject().getSiteName();
		List<PropertyItem> panels = new ArrayList<>();
		while( pindex<panelIndex ) {
			PanelData pdata = handler.getPanelData(pindex,data);
			// Create a PropertyItem for everything that is essential and matches the current site
			if(site==null || site.isEmpty() || pdata.getSiteNames().size()==0 || pdata.getSiteNames().contains(site) ) {
				List<String> features = dataModel.getObject().getFeatures();
				// Now weed out panels that correspond to features we don't have
				if( pdata.getFeatures().isEmpty() || pdata.matchFeature(features) || pdata.matchMissingFeature(features)) {
					if(pdata.isEssential() && pdata.getVersion()!=InstallerConstants.UNSET) {
						System.out.println(String.format("SummaryStep: %s (%d vs %d)", pdata.getTitle(),pdata.getCurrentVersion(),pdata.getVersion()));
						String value = "true";  // Up-to-date
						if(pdata.getCurrentVersion()<pdata.getVersion() ) value = "false";
						PropertyItem pi = new PropertyItem(pdata.getTitle(),value);
						panels.add(pi);
					}
				}
			}
			pindex++;
		}
       
		add(new ListView<PropertyItem>("panels", panels) {
			private static final long serialVersionUID = -4610581829738917953L;

			protected void populateItem(ListItem<PropertyItem> item) {
                PropertyItem property = (PropertyItem) item.getModelObject();
                item.add(new Label("name", property.getName()));
                
                // Image checkImage = new Image("check", new ContextRelativeResource("/images/check.png"));
                Image checkImage = new Image("check", "/images/check.png");
                item.add(checkImage);
                boolean check = property.getValue().equalsIgnoreCase("true");
                if(!check) checkImage.setVisible(false);
            }
        });
	}
}
