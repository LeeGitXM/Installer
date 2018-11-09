/**
 * Copyright 2016-2017. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

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
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class DefaultsStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 9066858944253432239L;
	private String partialProjectName     = "UNUSED";
	private String partialProjectComment  = "Merge script resources from the source project into the selected existing project (if they don't already exist).";
	private String partialProjectLocation     = "";
	private Project selectedProject = null;                 // Project to be merged
	public Form<InstallerData> mergeProjectForm = null;
	public ProjectList projects;
	
	public DefaultsStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// There is only one artifact
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
		for(Artifact art:artifacts) {
			if( art.getSubtype().equalsIgnoreCase("partial")) {
				partialProjectName = art.getName();
				partialProjectLocation = art.getLocation();
				if( !art.getComment().isEmpty() ) partialProjectComment = art.getComment();
			}
		}


		// Merge project form
		WebMarkupContainer partial = new WebMarkupContainer("partial");
		partial.setVisible(!partialProjectLocation.isEmpty());

		mergeProjectForm = new Form<InstallerData>("mergeForm", new CompoundPropertyModel<InstallerData>(data));

		Label partialProject = new Label("partialProject",partialProjectName);
		mergeProjectForm.add(partialProject);
		Label partialProjectCommentLabel = new Label("partialProjectComment",partialProjectComment);
		mergeProjectForm.add(partialProjectCommentLabel);

		projects = new ProjectList("projects", new PropertyModel<Project>(this, "selectedProject"), getProjects());
		mergeProjectForm.add(projects);

		mergeProjectForm.add(new Button("merge") {
			private static final long serialVersionUID = 4110668774811578782L;

			public void onSubmit() {
				String result = null;

				InstallerDataHandler handler = InstallerDataHandler.getInstance();

				if( result==null ) {
					if( selectedProject==null) selectedProject = getProject("");
					if( selectedProject!=null) {
						System.out.println(String.format("DefaultsStep.onSubmit: Merging project %s with %s =========================================",
								selectedProject.getName(),partialProjectName));
						result = handler.mergeWithDefaultsFromArtifact(selectedProject,partialProjectLocation,partialProjectName,data);
						System.out.println(String.format("DefaultsStep.onSubmit: Merged project name is now %s",selectedProject.getName()));
					}
					else {
						result = "No project selected as the target of the merge";
					}
				}
				if( result==null ) {
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					panelData.setCurrentVersion(futureVersion);
					info(String.format("Project %s merged successfully", partialProjectName));
				}
				else {
					error(result);
				}
			}
		});
		partial.add(mergeProjectForm);
		add(partial);
	}


	// ================================= Classes for Listing Projects  ==============================
	public class ProjectList extends DropDownChoice<Project> {
		private static final long serialVersionUID = -6176535065911396528L;

		public ProjectList(String key,PropertyModel<Project>model,List<Project> list) {
			super(key,model,list,new ProjectRenderer());
			model.setObject(getProject(""));
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
			return new Long(project.getId()).toString();
		}
	}

	//===============================================================================
	private List<Project> getProjects() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
	}
	private Project getProject(String name) {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getProjectManager().getProject(name, ApplicationScope.ALL, ProjectVersion.Published);
	}
}