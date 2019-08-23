/**
 * Copyright 2016-2017. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;


import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.ProjectNameFinder;

/**
 */
public class ProjectStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private String fullProjectName        = "UNUSED";
	private String fullProjectComment     = "Install the specified project into the gateway, overwriting any existing project of the same name.";
	private String fullProjectLocation        = "";
	private boolean backupProject = false;
	public Form<InstallerData> loadProjectForm = null;
	
	public ProjectStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// The only artifacts are projects to load.
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
		for(Artifact art:artifacts) {
			fullProjectName = dataHandler.scrubName(art.getName());
			fullProjectLocation = art.getLocation();
			if( !art.getComment().isEmpty() ) fullProjectComment = art.getComment();
			break;
		}

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
		Form<InstallerData> newProjectForm = new Form<InstallerData>("loadProject", new CompoundPropertyModel<InstallerData>(data));
		Label fullProject = new Label("fullProject",fullProjectName);
		newProjectForm.add(fullProject);
		Label fullProjectCommentLabel = new Label("fullProjectComment",fullProjectComment);
		newProjectForm.add(fullProjectCommentLabel);

		newProjectForm.add(new Button("new") {
			private static final long serialVersionUID = 4110558774811578782L;

			public void onSubmit() {
				String result = null;
				InstallerDataHandler handler = InstallerDataHandler.getInstance();
				//System.out.println(String.format("ProjectStep.onSubmit: Creating full project with profile %s",profileName));
				if(backupProject) result = createBackup(fullProjectName);
				if( result==null) {
					result = handler.loadArtifactAsProject(fullProjectLocation,fullProjectName,data);
					//System.out.println(String.format("ProjectStep.onSubmit: Created full project %s",fullProjectName));
				}

				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
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

	//===============================================================================
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