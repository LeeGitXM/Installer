/**
 * Copyright 2019. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 * 
 */
public class PythonStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3732149120641480873L;


	public PythonStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Create a subpanel for each artifact
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
		add(new ListView<Artifact>("scripts", artifacts) {
			private static final long serialVersionUID = -4610581829722917953L;

			protected void populateItem(ListItem<Artifact> item) {
				Artifact art = item.getModelObject();
                item.add(new Label("title",art.getName()));
                
                // location is the script
                Form<InstallerData> form = new Form<InstallerData>("pythonform", new CompoundPropertyModel<InstallerData>(data));
                PythonButton btn = new PythonButton("button",art);
                form.add(btn);
                form.add(new Label("comment", art.getComment()));
                item.add(form);
                
            }
        });
	}
	public class PythonButton extends Button {
		private static final long serialVersionUID = 4880228774822578782L;
		private final Artifact artifact;
		
		public PythonButton(String id,Artifact art) {
			super(id);
			this.artifact = art;
			if( !art.getType().isEmpty() ) {
				String buttonTitle = art.getType().substring(0,1).toUpperCase();
				if( art.getType().length()>1 ) {
					buttonTitle = buttonTitle+art.getType().substring(1).toLowerCase();
				}
				Model<String> model = new Model<String>(buttonTitle);
				this.setModel(model);
			}
		}

		@Override
		public void onSubmit() {
			InstallerDataHandler handler = InstallerDataHandler.getInstance();
			String result = handler.executePython(artifact.getScript());
			if( result==null || result.isEmpty()) {
				PythonStep.this.info(String.format("%s complete.", artifact.getName()));
				PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
				panelData.setCurrentVersion(futureVersion);
			}
			else {
				PythonStep.this.error(result);
			}
		}
	}
}
