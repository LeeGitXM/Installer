package com.ils.ai.gateway.panel;

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

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class ProjectStep extends InstallerStep {
	private Project existingProject = null;
	
	public ProjectStep(int index,InstallerStep previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

		add(new Label("preamble",preamble));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
		// New Project form
        Form<InstallerData> newProjectForm = new Form<InstallerData>("newForm", new CompoundPropertyModel<InstallerData>(data));
        TextField<String> newname = new TextField<String>("newName", Model.of(""));
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

		 // The name of the dropdown must correspond to a getter in the model object.
		mergeProjectForm.add(new DropDownChoice<Project>("project", new LoadableDetachableModel<List<Project>>() {
			private static final long serialVersionUID = -4849880268083610852L;

			@Override
			protected List<Project> load() {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
			}
		}, 
		new IChoiceRenderer<Project>(){
			private static final long serialVersionUID = 4630298960032443090L;

			@Override
			public Object getDisplayValue(Project project) {
				existingProject = project;
				return project.getName();
			}

			@Override
			public String getIdValue(Project project, int i) {
				return new Long(project.getId()).toString();
			}
		}));
		mergeProjectForm.add(new Button("merge") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(mergeProjectForm);
		
		if(!panelData.isMergable() ) mergeProjectForm.setVisible(false);
	}

}