/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.panel.ProjectStep.ProjectList;
import com.inductiveautomation.ignition.common.project.Project;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class DatabaseStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private String datasource = "";
	private boolean hasAlter = false;
	private boolean hasCreate = false;
	private String createName    = "";
	private String alterName = "";

	public DatabaseStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Search for the various artifact types
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
        for(Artifact art:artifacts) {
        	if( art.getSubtype().equalsIgnoreCase("create")) {
        		hasCreate = true;
        		createName = art.getName();
        	}
        	else if( art.getSubtype().equalsIgnoreCase("alter")) {
        		hasAlter = true;
        		alterName = art.getName();
        	}
        }
        
        datasource = dataHandler.datasourceNameFromProperties(index, data);
		add(new Label("datasource",datasource));
        

		// Create database form
        WebMarkupContainer create = new WebMarkupContainer("create");
        create.setVisible(hasCreate);
		Form<InstallerData> createSchemaForm = new Form<InstallerData>("createForm", new CompoundPropertyModel<InstallerData>(data));
        
		createSchemaForm.add(new Button("create") {
			private static final long serialVersionUID = 4330778774811578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.executeSQLFromArtifact(datasource,index,createName,data);
				if( result==null || result.isEmpty()) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					info(String.format("Datasource %s schema created successfully", datasource));
				}
				else {
					warn(result);
				}
            }
        });
		create.add(createSchemaForm);
        add(create);
		
        // Alter database form
		WebMarkupContainer alter = new WebMarkupContainer("alter");
		alter.setVisible(hasAlter);
		
		Form<InstallerData> alterSchemaForm = new Form<InstallerData>("alterForm", new CompoundPropertyModel<InstallerData>(data));

		alterSchemaForm.add(new Button("alter") {
			private static final long serialVersionUID = 4880778774811578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.executeSQLFromArtifact(datasource,index,alterName,data);
				if( result==null || result.isEmpty()) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					info(String.format("Datasource %s schema updated successfully", datasource));
				}
				else {
					warn(result);
				}
            }
        });
		alter.add(alterSchemaForm);
		add(alter);
	}

	

}
