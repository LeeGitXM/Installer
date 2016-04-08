/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.datasource.SerializableDatasourceMeta;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.gateway.datasource.Datasource;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * The definition step is where we define various parameters that are used in subsequent screens.
 * In particular, we define tag providers and database sources for production and, optionally, 
 * isolation modes.
 */
public class DefinitionStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private boolean showProductionProvider = false;
	private boolean showIsolationProvider  = false;
	private boolean showProductionDatabase = false;
	private boolean showIsolationDatabase  = false;
	private SerializableDatasourceMeta productionDatabase=null;
	private SerializableDatasourceMeta isolationDatabase=null;
	private TagProviderMeta productionProvider=null;
	private TagProviderMeta isolationProvider=null;
	private boolean valid = false;

	public DefinitionStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble).setEscapeModelStrings(false));

		// Display the pull downs based on panel properties.
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
		List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
    
    	for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("provider")) {
    			if(prop.getType().equalsIgnoreCase("production")) showProductionProvider = true;
    			else if(prop.getType().equalsIgnoreCase("isolation")) showIsolationProvider = true;
    		}
    		else if(prop.getName().equalsIgnoreCase("database")) {
    			if(prop.getType().equalsIgnoreCase("production")) showProductionDatabase = true;
    			else if(prop.getType().equalsIgnoreCase("isolation")) showIsolationDatabase = true;
    		}
    	}
		
    	// Create all the wicket widgets, just don't make them all visible
    	Label productionProviderLabel = new Label("productionProviderLabel","Production Tag Provider: ");
    	add(productionProviderLabel);
    	ProviderList productionProviders = new ProviderList("productionProviders", new PropertyModel<TagProviderMeta>(this, "productionProvider"), getProviderList());
		add(productionProviders);
		Label isolationProviderLabel = new Label("isolationProviderLabel","Isolation Tag Provider: ");
    	add(isolationProviderLabel);
    	ProviderList isolationProviders = new ProviderList("isolationProviders", new PropertyModel<TagProviderMeta>(this, "isolationProvider"), getProviderList());
		add(isolationProviders);
    	Label productionDatabaseLabel = new Label("productionDatabaseLabel","Production Database: ");
    	add(productionDatabaseLabel);
    	SourceList productionDatabases = new SourceList("productionDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "productionDatabase"), getDatasourceList());
		add(productionDatabases);
		Label isolationDatabaseLabel = new Label("isolationDatabaseLabel","Isolation Database: ");
    	add(isolationDatabaseLabel);
    	SourceList isolationDatabases = new SourceList("isolationDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "isolationDatabase"), getDatasourceList());
		add(isolationDatabases);
		
		
		// Adjust visibility
		productionDatabaseLabel.setVisible(showProductionDatabase);
		isolationDatabaseLabel.setVisible(showIsolationDatabase);
		
		productionProviderLabel.setVisible(showProductionProvider);
		isolationProviderLabel.setVisible(showIsolationProvider);
		productionDatabases.setVisible(showProductionDatabase);
		isolationDatabases.setVisible(showIsolationDatabase);
		productionProviders.setVisible(showProductionProvider);
		isolationProviders.setVisible(showIsolationProvider);
		
		// Select Defaults
		productionDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE));
		isolationDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE));
		productionProvider= getDefaultProvider(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER));
		isolationProvider= getDefaultProvider(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER));
		
		// Check validity with no action
		valid = true;
		if( (showProductionDatabase && productionDatabase==null) ||
			(showIsolationDatabase  && isolationDatabase==null) ||
			(showProductionProvider && productionProvider==null) ||
			(showIsolationProvider  && isolationProvider==null)    )  {
			valid = false;
		}
		
		// Save selections
		add(new Button("save") {
			private static final long serialVersionUID = 3996079889888596264L;

			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
				
				if( productionDatabase!=null ) toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE,productionDatabase.getName());
				if( isolationDatabase!=null )  toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE,isolationDatabase.getName());
				if( productionProvider!=null ) toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER,productionProvider.getName());
				if( isolationProvider!=null )  toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER,isolationProvider.getName());
				
				// Check validity
				StringBuilder msg = new StringBuilder();
				if( showProductionDatabase && productionDatabase==null )msg.append("Production database is not defined. ");
				if( showIsolationDatabase  && isolationDatabase==null)  msg.append("Isolation database is not defined. ");
				if( showProductionProvider && productionProvider==null) msg.append("Production tag provider is not defined. ");
				if( showIsolationProvider  && isolationProvider==null)  msg.append("Isolation tag provider is not defined. ");
				if( msg.length()==0 ) {
					if(showProductionDatabase&&showIsolationDatabase&&
					  productionDatabase.getName().equalsIgnoreCase(isolationDatabase.getName())) {
						msg.append("Production and isolation databases may not be the same. ");
					}
					if(showProductionProvider&&showIsolationProvider&&
							  productionProvider.getName().equalsIgnoreCase(isolationProvider.getName())) {
								msg.append("Production and isolation tag providers may not be the same. ");
					}
				}
				
				if( msg.length()==0) {
					valid = true;
					info("Datasource and tag provider definitions are complete.");
				}
				else {
					valid = false;
					warn(msg.toString());
				}
            }
        });
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

	// ================================= Classes for Listing Tag Provider  ==============================
	public class ProviderList extends DropDownChoice<TagProviderMeta> {
		private static final long serialVersionUID = -1021505223044346435L;

		public ProviderList(String key,PropertyModel<TagProviderMeta>model,List<TagProviderMeta> list) {
			super(key,model,list,new ProviderRenderer());
		}

		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		protected void onSelectionChanged(final TagProviderMeta newSelection) {
			super.onSelectionChanged(newSelection);
		} 
	}
	public class ProviderRenderer implements IChoiceRenderer<TagProviderMeta> {
		private static final long serialVersionUID = -700778014486584571L;

		@Override
		public Object getDisplayValue(TagProviderMeta provider) {
			return provider.getName();
		}

		@Override
		public String getIdValue(TagProviderMeta provider, int i) {
			return provider.getName();
		}
	}

	private List<TagProviderMeta> getProviderList() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getTagManager().getProviderInformation();
	}
	
	private TagProviderMeta getDefaultProvider(String name ) {
		TagProviderMeta result = null;
		for(TagProviderMeta meta:getProviderList() ) {
			if(meta.getName().equalsIgnoreCase(name)) {
				result = meta;
				break;
			}
		}
		return result;
	}
	
	
	@Override
	public boolean isNextAvailable() {
		return valid ;
	}
}
