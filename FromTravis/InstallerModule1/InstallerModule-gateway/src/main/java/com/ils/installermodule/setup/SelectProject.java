package com.ils.installermodule.setup;

import com.ils.installermodule.GatewayHook;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SelectProject extends GatewayWizardStep {

    public SelectProject(Model<SetupItem> dataModel) {
        super(null, BundleUtil.get().getString("ils.project.title"), dataModel);

        Form<SetupItem> form = new Form<SetupItem>("submitForm", new CompoundPropertyModel<SetupItem>((SetupItem) dataModel.getObject()));

        form.add(new Label("projectLabel", BundleUtil.get().getString("ils.project.title")));
        form.add(new DropDownChoice<Project>("project", new LoadableDetachableModel<List<Project>>() {
            @Override
            protected List<Project> load() {
                List<Project> list = new ArrayList<Project>();
                GatewayContext context = GatewayHook.getInstance().getContext();
                return context.getProjectManager().getProjectsLite(ProjectVersion.Published);
            }
        }, new IChoiceRenderer<Project>(){

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

    @Override
    public boolean isLastStep() {
        return true;
    }

    @Override
    public IDynamicWizardStep next() {
        return null;
    }

}