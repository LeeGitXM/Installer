/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * 
 */
public class PythonStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3732149120641480873L;
	private Artifact currentArtifact = null;


	public PythonStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Create a subpanel for each artifact (up to a maximum of three)
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
		
		currentArtifact = null;
		if( artifacts.size()>0 ) currentArtifact = artifacts.get(0);

		// First form
		WebMarkupContainer first = new WebMarkupContainer("first");
		first.setVisible(currentArtifact!=null);
		Form<InstallerData> firstForm = new Form<InstallerData>("firstForm", new CompoundPropertyModel<InstallerData>(data));
		firstForm.add(new Button("firstButton") {
			private static final long serialVersionUID = 4880228774822578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.executePythonFromArtifact(currentArtifact);
				if( result==null || result.isEmpty()) {
					info(String.format("%s complete.", currentArtifact.getName()));
				}
				else {
					error(result);
				}
			}
		});
		first.add(firstForm);
		add(first);

		currentArtifact = null;
		if( artifacts.size()>0 ) currentArtifact = artifacts.get(0);

		// Second form
		WebMarkupContainer second = new WebMarkupContainer("second");
		second.setVisible(currentArtifact!=null);
		Form<InstallerData> secondForm = new Form<InstallerData>("secondForm", new CompoundPropertyModel<InstallerData>(data));
		firstForm.add(new Button("secondButton") {
			private static final long serialVersionUID = 4880228774822578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.executePythonFromArtifact(currentArtifact);
				if( result==null || result.isEmpty()) {
					info(String.format("%s complete.", currentArtifact.getName()));
				}
				else {
					error(result);
				}
			}
		});
		second.add(secondForm);
		add(second);

		currentArtifact = null;
		if( artifacts.size()>2 ) currentArtifact = artifacts.get(2);

		// Third form
		WebMarkupContainer third = new WebMarkupContainer("first");
		first.setVisible(currentArtifact!=null);
		Form<InstallerData> thirdForm = new Form<InstallerData>("thirdForm", new CompoundPropertyModel<InstallerData>(data));
		thirdForm.add(new Button("thirdButton") {
			private static final long serialVersionUID = 4880228774822578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.executePythonFromArtifact(currentArtifact);
				if( result==null || result.isEmpty()) {
					info(String.format("%s complete.", currentArtifact.getName()));
				}
				else {
					error(result);
				}
			}
		});
		first.add(thirdForm);
		add(third);
		
	}
}
