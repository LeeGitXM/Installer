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

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class IconStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;


	public IconStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final IconStep thisPage = this;
        //ystem.out.println(String.format("IconStep: %s",title));
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        List<String> icons = handler.getArtifactNames(index, data);
        add(new ListView<String>("icons", icons) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
        
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
            	// Each artifact location is a root of a directory in the bundle
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	for(Artifact art:artifacts) {
            		String result = dataHandler.loadArtifactAsIconCollection(index,art,data);
            		if( result==null ) {
            			if(success.length()>0) success.append(", ");
            			success.append(art.getName());
            		}
            		else {
            			if(failure.length()>0) failure.append(", ");
            			failure.append(String.format("%s(%s)", art.getName(),result));
            		}
            	}
            	if(failure.length()==0 ) {
            		PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            		panelData.setCurrentVersion(futureVersion);
            		thisPage.info(success.insert(0,"Successfully loaded: ").toString());
            	}
            	else {
            		thisPage.error(failure.insert(0,"Failed to load: ").toString());
            	}
            }
        });
    }
}
