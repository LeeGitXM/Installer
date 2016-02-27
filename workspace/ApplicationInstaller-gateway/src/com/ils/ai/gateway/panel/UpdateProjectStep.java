package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class UpdateProjectStep extends InstallWizardStep {
	public UpdateProjectStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel) {
		super(index,previous, title, dataModel);

        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
		Form<InstallerData> form = new Form<InstallerData>("submitForm", new CompoundPropertyModel<InstallerData>(data));

		form.add(new Label("projectLabel", BundleUtil.get().getString("ils.project.select")));
		form.add(new DropDownChoice<Project>("project", new LoadableDetachableModel<List<Project>>() {
			private static final long serialVersionUID = -4849880268083610852L;

			@Override
			protected List<Project> load() {
				List<Project> list = new ArrayList<Project>();
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
			}
		}, 
				new IChoiceRenderer<Project>(){
			@Override
			public Object getDisplayValue(Project project) {
				return project.getName();
			}

			@Override
			public String getIdValue(Project project, int i) {
				return new Long(project.getId()).toString();
			}
		}));

		add(form);
	}
}