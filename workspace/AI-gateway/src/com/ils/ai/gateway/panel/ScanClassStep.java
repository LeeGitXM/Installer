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
public class ScanClassStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 2204950686203860253L;
	private String project = "";

	public ScanClassStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        project = dataHandler.projectNameFromProperties(index, data);
		add(new Label("project",project));
		
        List<String> scanClasses = dataHandler.getArtifactNames(index, data);
        add(new ListView<String>("scanclasses", scanClasses) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
        
        add(new Button("install") {
			private static final long serialVersionUID = 4110668774811578782L;
			
			@Override
            public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	for(String name:names) {
            		System.out.println("ScanClassStep: processing: "+name);
            		String result = dataHandler.loadArtifactAsTagGroup(index,project,name,data);
            		System.out.println("...result: "+result);
            		if( result==null ) {
            			PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            			panelData.setCurrentVersion(futureVersion);
            			info(String.format("Successfully loaded scanclass %s", name));
            		}
            		else error(result);
            	}
            }
        });
    }

}
