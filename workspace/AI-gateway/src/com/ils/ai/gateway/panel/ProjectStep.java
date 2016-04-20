/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
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
import com.ils.ai.gateway.model.ProjectNameFinder;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.user.UserSourceProfileRecord;

import simpleorm.dataset.SFieldMeta;
import simpleorm.dataset.SQuery;

/**
 */
public class ProjectStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private final static String AUTH_PREFERENCE_NAME = "authenticationProfile";
	private String fullProjectName        = "UNUSED";
	private String partialProjectName     = "UNUSED";
	private String globalProjectName      = "UNUSED";
	private String fullProjectLocation        = "";
	private String partialProjectLocation     = "";
	private String globalProjectLocation      = "";
	private String profileName= null;                       // Authentication profile
	private Project selectedProject = null;                 // Project to be merged
	private boolean backupProject = false;
	
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
        
        // Set the default profile
        PersistenceHandler ph = PersistenceHandler.getInstance();
        profileName = dataHandler.getPreference(AUTH_PREFERENCE_NAME);
        DropDownChoice<String> profiles=new DropDownChoice<String>("profiles", new PropertyModel<String>(this, "profileName"), ph.getProfileNames()) {
			private static final long serialVersionUID = 2602629544295913483L;
			
		
			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return profileName;
			}
        	@Override
        	public void onSelectionChanged(String newSelection) {
        		profileName = newSelection;
        		InstallerDataHandler.getInstance().setPreference(AUTH_PREFERENCE_NAME,profileName);
        	}
			@Override
			protected boolean wantOnSelectionChangedNotifications() {return true;}
        };
		add(profiles);

		// Set whether or not to skip panels that are up-to-date
		// Accept license
		String backup = dataHandler.getPreference("backupCheckbox");
		backupProject = backup.equalsIgnoreCase("true");
		CheckBox checkbox = new CheckBox("backup", (backupProject?Model.of(Boolean.TRUE):Model.of(Boolean.FALSE))) {
			private static final long serialVersionUID = -1551368213086376136L;

			protected boolean wantOnSelectionChangedNotifications() {return true;}
			// The value is "on" for selected, null for not.
			@Override
			public void onSelectionChanged() {
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				if(getValue()==null) {
					backupProject = false;
					handler.setPreference("backupCheckbox","false");
				}
				else {
					backupProject = true;
					handler.setPreference("backupCheckbox","true");
				}
			}
		};
		add(checkbox);
		
		// New Project form
        WebMarkupContainer full = new WebMarkupContainer("full");
        full.setVisible(!fullProjectLocation.isEmpty());
		Form<InstallerData> newProjectForm = new Form<InstallerData>("newForm", new CompoundPropertyModel<InstallerData>(data));
        Label fullProject = new Label("fullProject",fullProjectName);
        newProjectForm.add(fullProject);
        
        newProjectForm.add(new Button("new") {
			private static final long serialVersionUID = 4110558774811578782L;

			public void onSubmit() {
				String result = null;
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				System.out.println(String.format("ProjectStep.onsubmit newForm: %s (profile %s)", fullProjectName,profileName));
				if( profileName==null || profileName.isEmpty() ) {
					result = "Please select an authentication profile";
				}
				else {
					System.out.println(String.format("ProjectStep.onSubmit: Creating full project with profile %s",profileName));
					if(backupProject) result = createBackup(fullProjectName);
					if( result==null) {
						result = handler.loadArtifactAsProject(fullProjectLocation,fullProjectName,profileName,data);
					}
				}
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
					handler.setPreference(AUTH_PREFERENCE_NAME,profileName);
					info(String.format("Project %s loaded successfully", fullProjectName));
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

		mergeProjectForm.add(new Button("merge") {
			private static final long serialVersionUID = 4110668774811578782L;

			public void onSubmit() {
				String result = null;

				if(backupProject) result = createBackup(fullProjectName);
				InstallerDataHandler handler = InstallerDataHandler.getInstance();

				if( result==null ) {
					result = handler.mergeWithProjectFromLocation(selectedProject,partialProjectLocation,partialProjectName,data);
				}
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
					info(String.format("Project %s merged successfully", partialProjectName));
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
				String result = handler.mergeWithGlobalProjectFromLocation(globalProjectLocation,data);
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
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
	// Find an unused name and copy the original to it.
	private String createBackup(String oldName) {
		ProjectNameFinder finder = new ProjectNameFinder();
		String backupName = finder.findUnused(oldName);
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		String error = handler.copyProjectToBackup(oldName,backupName);
		if( error!=null ) error = String.format("Backup failed (%s)",error);
		return error;
	}
}