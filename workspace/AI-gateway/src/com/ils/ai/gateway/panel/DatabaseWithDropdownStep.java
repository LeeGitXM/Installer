/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.datasource.SerializableDatasourceMeta;
import com.inductiveautomation.ignition.common.sqltags.TagProviderMetaImpl;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.common.tags.model.TagProvider;
import com.inductiveautomation.ignition.common.tags.model.TagProviderProps;
import com.inductiveautomation.ignition.gateway.datasource.Datasource;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * The definition step is where we define various parameters that are used in subsequent screens.
 * In particular, we define tag providers and database sources and DBMSs for production and, optionally, 
 * secondary modes.
 * 
 * Recognized database options are: production, isolation, secondary, batchexpert, pysfc
 * Recognized tag otions
 */
public class DatabaseWithDropdownStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private SerializableDatasourceMeta productionDatabase=null;
	private String clearName = "";
	private String createName = "";
	private boolean valid = false;

	public DatabaseWithDropdownStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 
		System.out.println("Constructing a DatabaseWithDropdownStep...");
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
		
		// Search for the various artifact types
		System.out.println("Searching for artifacts...");
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        List<Artifact> artifacts = dataHandler.getArtifacts(index, data);
        for(Artifact art:artifacts) {
        	if( art.getSubtype().equalsIgnoreCase("clear")) {
        		clearName = art.getName();
        	}
        	else if( art.getSubtype().equalsIgnoreCase("create")) {
        		createName = art.getName();
        	}
        }
		
		// Display the pull down
        System.out.println("Constructing a Toolkit Record Handler...");
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
		List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
    
    	// Create the wicket widgets
		System.out.println("Constructing dropdown...");
    	Label databaseLabel = new Label("databaseLabel", "Database: ");
    	add(databaseLabel);
    	SourceList productionDatabases = new SourceList("productionDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "productionDatabase"), getDatasourceList());
		add(productionDatabases);

		// Select Defaults
		//System.out.println("Getting default database...");
		//productionDatabase = getDefaultDatasource(toolkitHandler.getToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true)));
		
		// Check validity with no action
		valid = true;
		if( productionDatabase==null )  {
			valid = false;
		}
		
		// Clear database form
		System.out.println("Constructing CLEAR form...");
        WebMarkupContainer clear = new WebMarkupContainer("clear");
        clear.setVisible(true);
		Form<InstallerData> clearSchemaForm = new Form<InstallerData>("clearForm", new CompoundPropertyModel<InstallerData>(data));
        
		clearSchemaForm.add(new Button("clear") {
			private static final long serialVersionUID = 4330228774822578782L;
			

			public void onSubmit() {
				StringBuilder failure = new StringBuilder("");
				if (productionDatabase == null) {
					error(failure.insert(0,"Please select a database from the dropdown").toString());
				}
				else {
					System.out.println("Executing CLEAR on "+productionDatabase.getName());
					InstallerDataHandler handler = InstallerDataHandler.getInstance();
					String result = handler.executeSQLFromArtifact(productionDatabase.getName(),index,clearName,data);
					if( result==null || result.isEmpty()) {
						info(String.format("Datasource %s cleared (tables dropped).", productionDatabase.getName()));
					}
					else {
						error(result);
					}
				}
            }
        });
		clear.add(clearSchemaForm);
        add(clear);
        
		// Create database form
        System.out.println("Constructing CREATE form...");
        WebMarkupContainer create = new WebMarkupContainer("create");
        create.setVisible(true);
		Form<InstallerData> createSchemaForm = new Form<InstallerData>("createForm", new CompoundPropertyModel<InstallerData>(data));
        
		createSchemaForm.add(new Button("create") {
			private static final long serialVersionUID = 4330778774811578782L;

			public void onSubmit() {
				StringBuilder failure = new StringBuilder("");
				if (productionDatabase == null) {
					error(failure.insert(0,"Please select a database from the dropdown").toString());
				}
				else {
					System.out.println("Executing CREATE on "+productionDatabase.getName());
					InstallerDataHandler handler = InstallerDataHandler.getInstance();
					String result = handler.executeSQLFromArtifact(productionDatabase.getName(),index,createName,data);
					if( result==null || result.isEmpty()) {
						PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
						panelData.setCurrentVersion(futureVersion);
						info(String.format("Datasource %s schema created successfully", productionDatabase.getName()));
					}
					else {
						if(result.length()>InstallerConstants.MAX_ERROR_LENGTH ) {
							result = result.substring(0,InstallerConstants.MAX_ERROR_LENGTH)+"...";
						}
						error(result);
					}
				}
            }
        });
		create.add(createSchemaForm);
        add(create);
	}


	// ================================= Classes for Listing Database Connections  ==============================
	public class SourceList extends DropDownChoice<SerializableDatasourceMeta> {
		private static final long serialVersionUID = -7914391900949792300L;

		public SourceList(String key,PropertyModel<SerializableDatasourceMeta>model,List<SerializableDatasourceMeta> list) {
			super(key,model,list,new DatasourceRenderer());
		}

		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		protected void onSelectionChanged(final SerializableDatasourceMeta newSelection) {
			super.onSelectionChanged(newSelection);
		} 
	}
	public class DatasourceRenderer implements IChoiceRenderer<SerializableDatasourceMeta> {
		private static final long serialVersionUID = -1709556857350933828L;

		@Override
		public Object getDisplayValue(SerializableDatasourceMeta source) {
			return source.getName();
		}

		@Override
		public String getIdValue(SerializableDatasourceMeta source, int i) {
			return source.getName().toString();
		}
	}

	private List<SerializableDatasourceMeta> getDatasourceList() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		List<Datasource> dataSources = context.getDatasourceManager().getDatasources();
		List<SerializableDatasourceMeta> results = new ArrayList<>();
		for(Datasource dataSource:dataSources) {
			results.add(new SerializableDatasourceMeta(dataSource));
		}
		return results;
	}
	private SerializableDatasourceMeta getDefaultDatasource(String name ) {
		SerializableDatasourceMeta result = null;
		for(SerializableDatasourceMeta meta:getDatasourceList() ) {
			if(meta.getName().equalsIgnoreCase(name)) {
				result = meta;
				break;
			}
		}
		return result;
	}

	
	// If they've pressed "Save" and made all selections,
	// allow user to proceed,
	//@Override
	//public boolean isNextAvailable() {
	//	return true;
	//}
}
