/**
 * Copyright 2016-2017. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.inductiveautomation.ignition.common.model.ApplicationScope;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.RuntimeProject;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.project.ProjectManager;

/**
 */
public class ProjectStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private final static String AUTH_PREFERENCE_NAME = "authenticationProfile";
	private String fullProjectName        = "UNUSED";
	private String partialProjectName     = "UNUSED";
	private String globalProjectName      = "UNUSED";
	private String fullProjectComment     = "Install the specified project into the gateway, overwriting any existing project of the same name.";
	private String partialProjectComment  = "Merge new or modified project resources from the source project into the selected existing project." +
			                                "The name and permissions associated with the existing project do not change.";
	private String globalProjectComment   = "Merge new or modified project resources from the source project into the global project.";
	private String fullProjectLocation        = "";
	private String partialProjectLocation     = "";
	private String globalProjectLocation      = "";
	private String profileName= null;  // Authentication profile
	private Project selectedProject = null;                 // Project to be merged
	private boolean backupProject = false;
	public Form<InstallerData> mergeProjectForm = null;
	public ProjectList projects;
	
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
				if( !art.getComment().isEmpty() ) fullProjectComment = art.getComment();
			}
			else if( art.getSubtype().equalsIgnoreCase("partial")) {
				partialProjectName = art.getName();
				partialProjectLocation = art.getLocation();
				if( !art.getComment().isEmpty() ) partialProjectComment = art.getComment();
			}
			else if( art.getSubtype().equalsIgnoreCase("global")) {
				globalProjectName = art.getName();
				globalProjectLocation = art.getLocation();
				if( !art.getComment().isEmpty() ) globalProjectComment = art.getComment();
			}
		}

		// Set the default profile
		PersistenceHandler ph = PersistenceHandler.getInstance();
		profileName = dataHandler.getPreference(AUTH_PREFERENCE_NAME);
		DropDownChoice<String>profiles=new DropDownChoice<String>("profiles", new PropertyModel<String>(this, "profileName"), ph.getProfileNames()) {
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
		Label fullProjectCommentLabel = new Label("fullProjectComment",fullProjectComment);
		newProjectForm.add(fullProjectCommentLabel);

		newProjectForm.add(new Button("new") {
			private static final long serialVersionUID = 4110558774811578782L;

			public void onSubmit() {
				String result = null;
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				if( profileName==null || profileName.isEmpty() ) {
					result = "Please select an authentication profile";
				}
				else {
					//System.out.println(String.format("ProjectStep.onSubmit: Creating full project with profile %s",profileName));
					if(backupProject) result = createBackup(fullProjectName);
					if( result==null) {
						result = handler.loadArtifactAsProject(fullProjectLocation,fullProjectName,profileName,data);
						//System.out.println(String.format("ProjectStep.onSubmit: Created full project %s",fullProjectName));
						ProjectStep parent = ProjectStep.this;
						parent.projects = new ProjectList("projects", new PropertyModel<Project>(parent, "selectedProject"), parent.getProjects());
						parent.mergeProjectForm.addOrReplace(parent.projects);
					}
				}
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
					handler.setPreference(AUTH_PREFERENCE_NAME,profileName);
					info(String.format("Project %s loaded successfully", fullProjectName));
				}
				else {
					error(result);
				}
			}
		});
		full.add(newProjectForm);
		add(full);
	}


	// ================================= Classes for Listing Projects  ==============================
	public class ProjectList extends DropDownChoice<Project> {
		private static final long serialVersionUID = -6176535065911396528L;

		public ProjectList(String key,PropertyModel<Project>model,List<Project> list) {
			super(key,model,list,new ProjectRenderer());
			model.setObject(getProject(fullProjectName));
		}

		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		public void onSelectionChanged(final Project newSelection) {
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
			return project.getName();
		}
	}

	//===============================================================================
	private List<Project> getProjects() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		ProjectManager projManager = context.getProjectManager();
		List<String> names = projManager.getProjectNames();
		List<Project> result = new ArrayList<>();
		for(String name:names ) {
			Optional<RuntimeProject> optional = projManager.getProject(name);
			result.add(optional.get());
		}
		return result;
	}
	private Project getProject(String name) {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		Optional<RuntimeProject> optional = context.getProjectManager().getProject(name, ApplicationScope.ALL);
		return optional.get();
	}
	// Find an unused name and copy the original to it.
	private String createBackup(String oldName) {
		ProjectNameFinder finder = new ProjectNameFinder();
		String backupName = finder.findUnused(oldName);
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		String error = handler.copyProjectToBackup(oldName,backupName,data);
		if( error!=null ) error = String.format("Backup failed (%s)",error);
		return error;
	}
}