/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class ProjectStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private Artifact selectedArtifact = null;
	private Project selectedProject = null;
	
	public ProjectStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
		
	
		// New Project form
		Form<InstallerData> newProjectForm = new Form<InstallerData>("newForm", new CompoundPropertyModel<InstallerData>(data));

		ArtifactList artifacts = new ArtifactList("fullArtifacts", new PropertyModel<Artifact>(this, "selectedArtifact"), getFullArtifacts());
		newProjectForm.add(artifacts);

        TextField<String> newname = new TextField<String>("newName", Model.of(""));
        
        newname.add(new ProjectNameValidator());
        newProjectForm.add(newname);
        newProjectForm.add(new Button("new") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(newProjectForm);
		
        // Merge project form
		Form<InstallerData> mergeProjectForm = new Form<InstallerData>("mergeForm", new CompoundPropertyModel<InstallerData>(data));
		TextField<String> mergename = new TextField<String>("mergeName", Model.of(""));
		mergeProjectForm.add(mergename);
		artifacts = new ArtifactList("partialArtifacts", new PropertyModel<Artifact>(this, "selectedArtifact"), getPartialArtifacts());
		mergeProjectForm.add(artifacts);

		ProjectList projects = new ProjectList("projects", new PropertyModel<Project>(this, "selectedProject"), getProjects());
		mergeProjectForm.add(projects);
		mergeProjectForm.add(new Button("merge") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(mergeProjectForm);
		
		// Global project form
		Form<InstallerData> globalProjectForm = new Form<InstallerData>("globalForm", new CompoundPropertyModel<InstallerData>(data));
		TextField<String> globalname = new TextField<String>("globalName", Model.of(""));
		globalProjectForm.add(globalname);
		artifacts = new ArtifactList("globalArtifacts", new PropertyModel<Artifact>(this, "selectedArtifact"), getGlobalArtifacts());
		globalProjectForm.add(artifacts);

		globalProjectForm.add(new Button("mergeGlobal") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(globalProjectForm);
	}
	
	// ================================= Classes for listing Artifacts ==============================
	public class ArtifactList extends DropDownChoice<Artifact> {
		private static final long serialVersionUID = 7739671575309150757L;
		
		// The filter is the value of the archive subtype to use
		// We update the text field with a proposed name based on the selection.
		public ArtifactList(String key,PropertyModel<Artifact>model,List<Artifact> list) {
			super(key,model,list,new ArtifactRenderer());
		}
		
		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }
		
		@Override
		protected void onSelectionChanged(final Artifact newSelection) {
			super.onSelectionChanged(newSelection);
		}
	}
	
	public class ArtifactRenderer implements IChoiceRenderer<Artifact> {
		private static final long serialVersionUID = -7461307371369030148L;

		@Override
		public Object getDisplayValue(Artifact artifact) {
			return artifact.getName();
		}

		@Override
		public String getIdValue(Artifact artifact, int i) {
			return String.valueOf(i);
		}
	}
	// ================================= Classes for Listing Projects  ==============================
	public class ProjectList extends DropDownChoice<Project> {
		private static final long serialVersionUID = -6176535065911396528L;
		
		public ProjectList(String key,PropertyModel<Project>model,List<Project> list) {
			super(key,model,list,new ProjectRenderer());
		}
		
		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }
		
		@Override
		protected void onSelectionChanged(final Project newSelection) {
			super.onSelectionChanged(newSelection);
		}
	}

	public class ProjectRenderer implements IChoiceRenderer<Project> {
		private static final long serialVersionUID = 4630298960032443090L;

		@Override
		public Object getDisplayValue(Project project) {
			return project.getName();
		}

		@Override
		public String getIdValue(Project project, int i) {
			return new Long(project.getId()).toString();
		}
	}
	//===============================================================================
	private List<Artifact> getFullArtifacts() {
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> results = new ArrayList<>();
		List<Artifact> artifacts = dataHandler.getArtifacts(panelIndex,data);
		for(Artifact art:artifacts) {
			if(art.getSubtype().equalsIgnoreCase("full")) {
				results.add(art);
			}
		}
		return results;
	}
	private List<Artifact> getGlobalArtifacts() {
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> results = new ArrayList<>();
		List<Artifact> artifacts = dataHandler.getArtifacts(panelIndex,data);
		for(Artifact art:artifacts) {
			if(art.getSubtype().equalsIgnoreCase("global")) {
				results.add(art);
			}
		}
		return results;
	}
	private List<Artifact> getPartialArtifacts() {
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> results = new ArrayList<>();
		List<Artifact> artifacts = dataHandler.getArtifacts(panelIndex,data);
		for(Artifact art:artifacts) {
			if(art.getSubtype().equalsIgnoreCase("partial")) {
				results.add(art);
			}
		}
		return results;
	}
	private List<Project> getProjects() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
	}
}