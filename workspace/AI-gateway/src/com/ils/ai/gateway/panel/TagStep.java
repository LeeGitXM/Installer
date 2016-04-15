/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class TagStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 5388412865553172897L;
	private String provider = "";

	public TagStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        final TagStep thisPage = this;
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        provider = dataHandler.providerNameFromProperties(index, data);
		add(new Label("provider",provider));
		
		List<String> resources = dataHandler.getArtifactNames(index,data);
		add(new ListView<String>("tags",resources) {
			private static final long serialVersionUID = -7571784271601338236L;

			protected void populateItem(ListItem<String> item) {
				String text = (String)item.getModelObject();
				item.add(new Label("name",text));
			}
		});
       
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	for(String name:names) {
            		String result = dataHandler.loadArtifactAsTags(index,provider,name,data);
            		if( result==null ) {
            			if(success.length()>0) success.append(", ");
            			success.append(name);
            		}
            		else {
            			if(failure.length()>0) failure.append(", ");
            			failure.append(String.format("%s(%s)", name,result));
            		}
            	}
            	if(failure.length()==0 ) {
            		PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            		thisPage.info(success.insert(0,"Successfully loaded: ").toString());
            	}
            	else {
            		thisPage.warn(failure.insert(0,"Failed to load: ").toString());
            	}
            }
        });
    }

}
