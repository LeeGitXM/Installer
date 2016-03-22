/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.SQuery;
import com.ils.ai.gateway.model.UserSourceProfileRecord;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class ProjectStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private String fullProjectName    = "UNUSED";
	private String partialProjectName = "UNUSED";
	private String globalProjectName  = "UNUSED";
	private String fullProjectLocation    = "";
	private String partialProjectLocation = "";
	private String globalProjectLocation  = "";
	private Project selectedProject = null;     // Project to be merged
	
	public ProjectStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
		
		// Search for the various artifact types
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
        for(Artifact art:artifacts) {
        	if( art.getSubtype().equalsIgnoreCase("full")) {
        		fullProjectName = art.getName();
        		fullProjectLocation = art.getLocation();
        	}
        	else if( art.getSubtype().equalsIgnoreCase("partial")) {
        		partialProjectName = art.getName();
        		partialProjectLocation = art.getLocation();
        	}
        	else if( art.getSubtype().equalsIgnoreCase("global")) {
        		globalProjectName = art.getName();
        		globalProjectLocation = art.getLocation();
        	}
        }
	
        List<UserSourceProfileRecord> profiles = context.getPersistenceInterface().query(new SQuery<UserSourceProfileRecord>(UserSourceProfileRecord.META));
		// New Project form
        WebMarkupContainer full = new WebMarkupContainer("full");
        full.setVisible(!fullProjectLocation.isEmpty());
		Form<InstallerData> newProjectForm = new Form<InstallerData>("newForm", new CompoundPropertyModel<InstallerData>(data));
        Label fullProject = new Label("fullProject",fullProjectName);
        newProjectForm.add(fullProject);
        TextField<String> newname = new TextField<String>("newName", Model.of(suggestedName(fullProjectName,panelData.getCurrentVersion())));
        newProjectForm.add(newname);

        newProjectForm.add(new Button("new") {
			private static final long serialVersionUID = 4110558774811578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				ProjectNameValidator validator = new ProjectNameValidator();
				String result = validator.validate(newname);
				if( result==null ) {
					result = handler.loadArtifactAsProject(fullProjectLocation,newname.getValue(),data);
					if( result==null ) {
						PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
						info(String.format("Project %s loaded successfully", newname.getValue()));
					}
					else {
						warn(result);
					}
				}
				else {
					warn(result);
				}
			}
        });
		full.add(newProjectForm);
        add(full);
		
        // Merge project form
		WebMarkupContainer partial = new WebMarkupContainer("partial");
		partial.setVisible(!partialProjectLocation.isEmpty());
		
		Form<InstallerData> mergeProjectForm = new Form<InstallerData>("mergeForm", new CompoundPropertyModel<InstallerData>(data));

        Label partialProject = new Label("partialProject",partialProjectName);
        mergeProjectForm.add(partialProject);

		ProjectList projects = new ProjectList("projects", new PropertyModel<Project>(this, "selectedProject"), getProjects());
		mergeProjectForm.add(projects);
		TextField<String> mergename = new TextField<String>("mergeName", Model.of(""));
		mergeProjectForm.add(mergename);
		mergeProjectForm.add(new Button("merge") {
			private static final long serialVersionUID = 4110668774811578782L;

			public void onSubmit() {
				ProjectNameValidator validator = new ProjectNameValidator();
				String result = validator.validate(mergename);
				if( result==null ) {
					InstallerDataHandler handler = InstallerDataHandler.getInstance();
					result = handler.mergeWithProjectFromLocation(selectedProject,partialProjectLocation,mergename.getValue(),data);
					if( result==null ) {
						PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
						info(String.format("Project %s merged successfully", mergename.getValue()));
					}
					else {
						warn(result);
					}
				}
				else {
					warn(result);
				}
            }
        });
		partial.add(mergeProjectForm);
		add(partial);
		
		// Global project form
		WebMarkupContainer global = new WebMarkupContainer("global");
		global.setVisible(!globalProjectLocation.isEmpty());
		Form<InstallerData> globalProjectForm = new Form<InstallerData>("globalForm", new CompoundPropertyModel<InstallerData>(data));
		Label globalProject = new Label("globalProject",globalProjectName);
		globalProjectForm.add(globalProject);

		TextField<String> globalname = new TextField<String>("globalName", Model.of(""));
		globalProjectForm.add(globalname);

		globalProjectForm.add(new Button("mergeGlobal") {
			private static final long serialVersionUID = 4110888774811578782L;

			public void onSubmit() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				String result = handler.mergeWithGlobalProjectFromLocation(globalProjectLocation,newname.getValue(),data);
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					info(String.format("Global project updated successfully"));
				}
				else {
					warn(result);
				}
			}
		});
		global.add(globalProjectForm);
		add(global);
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
	private List<Project> getProjects() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
	}
	// Underscore is the only acceptable delimiter
	private String suggestedName(String root,int version) {
		if( version<0 ) version = 0; // Unset
		return String.format("%s_%d", root,version);
	}
}