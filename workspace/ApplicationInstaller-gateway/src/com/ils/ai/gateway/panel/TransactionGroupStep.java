/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.ai.gateway.panel.ProjectStep.ProjectList;
import com.ils.ai.gateway.panel.ProjectStep.ProjectRenderer;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class TransactionGroupStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 2204950686203860253L;
	private Project selectedProject = null;     // Project to be merged

	public TransactionGroupStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final TransactionGroupStep thisPage = this;
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		ProjectList projects = new ProjectList("projects", new PropertyModel<Project>(this, "selectedProject"), getProjects());
		add(projects);
		
        List<String> transactionGroups = dataHandler.getArtifactNames(index, data);
        add(new ListView<String>("groups", transactionGroups) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
        
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			@Override
            public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	if( selectedProject==null ) {
            		warn("You must select a project.");
            		return;
            	}
            	
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	for(String name:names) {
            		String result = dataHandler.loadArtifactAsTransactionGroup(index,selectedProject.getName(),name,data);
            		if( result==null ) {
            			if(success.length()>0) success.append(", ");
            			success.append(name);
            		}
            		else {
            			if(failure.length()>0) failure.append(", ");
            			failure.append(String.format("%s(%s)", name,result));
            		}
            	}
            	if(failure.length()==0 ) {
            		thisPage.info(success.insert(0,"Successfully loaded: ").toString());
            		PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            	}
            	else {
            		thisPage.warn(failure.insert(0,"Failed to load: ").toString());
            	}
            }
        });
    }
	// ================================= Classes for Listing Projects  ==============================
		public class ProjectList extends DropDownChoice<Project> {
			private static final long serialVersionUID = -6178535065911396528L;
			
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
			private static final long serialVersionUID = 4730298960032443090L;

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
}
