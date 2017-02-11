/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * 
 */
public class FeatureStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3732149120641480873L;


	public FeatureStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Create a subpanel for each artifact (up to a maximum of three)
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
		add(new ListView<Artifact>("scripts", artifacts) {
			private static final long serialVersionUID = -4610581829722917953L;

			protected void populateItem(ListItem<Artifact> item) {
				Artifact art = item.getModelObject();
                item.add(new Label("title",art.getName()));
                
                // location is the script
                Form<InstallerData> form = new Form<InstallerData>("featureform", new CompoundPropertyModel<InstallerData>(data));
                FeatureCheckBox checkbox = new FeatureCheckBox("checkbox",art);
                form.add(checkbox);
                form.add(new Label("comment", art.getComment()));
                item.add(form);
                
            }
        });
	}
	public class FeatureCheckBox extends CheckBox {
		private static final long serialVersionUID = 4880228774822578782L;
		private final Artifact artifact;
		
		public FeatureCheckBox(String id,Artifact art) {
			super(id);
			this.artifact = art;
			
			// Set the model value from preferences
			String feature = artifact.getName().toUpperCase();
			// Set whether or not this installation applies a given feature
			InstallerDataHandler handler = InstallerDataHandler.getInstance();
	        String hasFeature = handler.getPreference("feature"+feature);
	        System.out.println(String.format("FeatureStep:  has feature "+feature+"="+hasFeature));
	        if( hasFeature.isEmpty() ) hasFeature = "false";   // Default to false
	        data.setFeature(feature,hasFeature.equalsIgnoreCase("true"));
			Model<Boolean> model = (hasFeature.equalsIgnoreCase("true")?Model.of(Boolean.TRUE):Model.of(Boolean.FALSE));
			this.setModel(model);
		}
		
		@Override
		protected boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		public void onSelectionChanged() {
			InstallerDataHandler handler = InstallerDataHandler.getInstance();

			boolean hasFeature = (getValue()!=null);		
			String feature = artifact.getName().toUpperCase();
			System.out.println(String.format("FeatureStep:  now has feature "+feature+"="+(hasFeature?"true":"false")));
			handler.setPreference("feature"+feature, (hasFeature?"true":"false"));
			// Use the destination string, if any, to inform the application of feature.
			handler.executePythonFromArtifact(artifact,hasFeature); 
			data.setFeature(feature,hasFeature);
		}
	}
}
