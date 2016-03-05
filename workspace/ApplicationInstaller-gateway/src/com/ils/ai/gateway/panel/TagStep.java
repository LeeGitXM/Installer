/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class TagStep extends InstallerStep {
	private Project existingProject = null;
	
	public TagStep(int index,InstallerStep previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

        add(new Label("preamble",preamble));
        
		// New Project form
        Form<InstallerData> form = new Form<InstallerData>("newForm", new CompoundPropertyModel<InstallerData>(data));
		form.add(new Button("new") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(form);
		
        // Merge project form
		 form = new Form<InstallerData>("mergeForm", new CompoundPropertyModel<InstallerData>(data));

		 // The name of the dropdown must correspond to a getter in the model object.
		 form.add(new DropDownChoice<Project>("project", new LoadableDetachableModel<List<Project>>() {
			private static final long serialVersionUID = -4849880268083610852L;

			@Override
			protected List<Project> load() {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
			}
		}, 
		new IChoiceRenderer<Project>(){
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
        form.add(new Button("merge") {
			private static final long serialVersionUID = 4110778774811578782L;

			public void onSubmit() {
            	
            }
        });
		add(form);
	}

}