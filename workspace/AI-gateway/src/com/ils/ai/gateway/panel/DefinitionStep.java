/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.InstallerConstants;
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
 * In particular, we define tag providers and database sources and DBMSs for production and, optionally, 
 * secondary modes.
 */
public class DefinitionStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private boolean showProductionProvider = false;
	private boolean showSecondaryProvider  = false;
	private boolean showProductionDBMS = false;
	private boolean showSecondaryDBMS  = false;
	private boolean showProductionDatabase = false;
	private boolean showSecondaryDatabase  = false;
	private SerializableDatasourceMeta productionDatabase=null;
	private SerializableDatasourceMeta secondaryDatabase=null;
	private String productionDBMS=null;
	private String secondaryDBMS=null;
	private TagProviderMeta productionProvider=null;
	private TagProviderMeta secondaryProvider=null;
	private boolean saved = false;
	private boolean valid = false;

	public DefinitionStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble).setEscapeModelStrings(false));

		// Display the pull downs based on panel properties.
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
		List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
    
    	for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER)) {
    			if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) showProductionProvider = true;
    			else if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryProvider = true;
    		}
    		if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DBMS)) {
    			if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) showProductionDBMS = true;
    			else if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryDBMS = true;
    		}
    		if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE)) {
    			if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) showProductionDatabase = true;
    			else if(prop.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryDatabase = true;
    		}
    	}
		
    	// Create all the wicket widgets, just don't make them all visible
    	Label productionProviderLabel = new Label("productionProviderLabel","Production Tag Provider: ");
    	add(productionProviderLabel);
    	ProviderList productionProviders = new ProviderList("productionProviders", new PropertyModel<TagProviderMeta>(this, "productionProvider"), getProviderList());
		add(productionProviders);
		Label secondaryProviderLabel = new Label("secondaryProviderLabel","secondary Tag Provider: ");
    	add(secondaryProviderLabel);
    	ProviderList secondaryProviders = new ProviderList("secondaryProviders", new PropertyModel<TagProviderMeta>(this, "secondaryProvider"), getProviderList());
		add(secondaryProviders);
    	Label productionDatabaseLabel = new Label("productionDatabaseLabel","Production Database: ");
    	add(productionDatabaseLabel);
    	SourceList productionDatabases = new SourceList("productionDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "productionDatabase"), getDatasourceList());
		add(productionDatabases);
		Label secondaryDatabaseLabel = new Label("secondaryDatabaseLabel","secondary Database: ");
    	add(secondaryDatabaseLabel);
    	SourceList secondaryDatabases = new SourceList("secondaryDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "secondaryDatabase"), getDatasourceList());
		add(secondaryDatabases);
		Label productionDBMSLabel = new Label("productionDBMSLabel","Production DBMS: ");
    	add(productionDBMSLabel);
    	DBMSList productionDBMSs = new DBMSList("productionDBMSs", new PropertyModel<String>(this, "productionDBMS"), getDBMSList());
		add(productionDBMSs);
		Label secondaryDBMSLabel = new Label("secondaryDBMSLabel","secondary DBMS: ");
    	add(secondaryDBMSLabel);
    	DBMSList secondaryDBMSs = new DBMSList("secondaryDBMSs", new PropertyModel<String>(this, "secondaryDBMS"), getDBMSList());
		add(secondaryDBMSs);
		
		// Adjust visibility
		productionDatabaseLabel.setVisible(showProductionDatabase);
		secondaryDatabaseLabel.setVisible(showSecondaryDatabase);
		productionDBMSLabel.setVisible(showProductionDBMS);
		secondaryDBMSLabel.setVisible(showSecondaryDBMS);
		productionProviderLabel.setVisible(showProductionProvider);
		secondaryProviderLabel.setVisible(showSecondaryProvider);
		
		productionDatabases.setVisible(showProductionDatabase);
		secondaryDatabases.setVisible(showSecondaryDatabase);
		productionDBMSs.setVisible(showProductionDBMS);
		secondaryDBMSs.setVisible(showSecondaryDBMS);
		productionProviders.setVisible(showProductionProvider);
		secondaryProviders.setVisible(showSecondaryProvider);
		
		// Select Defaults
		productionDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE));
		secondaryDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ALTERNATE_DATABASE));
		productionDBMS= getDefaultDBMS(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DBMS));
		secondaryDBMS = getDefaultDBMS(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_DBMS));
		productionProvider= getDefaultProvider(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER));
		secondaryProvider= getDefaultProvider(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_PROVIDER));
		
		// Check validity with no action
		valid = true;
		if( (showProductionDatabase && productionDatabase==null) ||
			(showSecondaryDatabase  && secondaryDatabase==null) ||
			(showProductionDBMS     && productionDBMS==null) ||
			(showSecondaryDBMS      && secondaryDBMS==null) ||
			(showProductionProvider && productionProvider==null) ||
			(showSecondaryProvider  && secondaryProvider==null)    )  {
			valid = false;
		}
		
		// Save selections
		add(new Button("save") {
			private static final long serialVersionUID = 3996079889888596264L;

			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
				
				if( productionDatabase!=null ) toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE,productionDatabase.getName());
				if( secondaryDatabase!=null )  toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ALTERNATE_DATABASE,secondaryDatabase.getName());
				if( productionDBMS!=null )     toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DBMS,productionDBMS);
				if( secondaryDBMS!=null )      toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_DBMS,secondaryDBMS);
				if( productionProvider!=null ) toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER,productionProvider.getName());
				if( secondaryProvider!=null )  toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_PROVIDER,secondaryProvider.getName());
				
				// Check validity
				StringBuilder msg = new StringBuilder();
				if( showProductionDatabase && productionDatabase==null )msg.append("Production database is not defined. ");
				if( showSecondaryDatabase  && secondaryDatabase==null)  msg.append("secondary database is not defined. ");
				if( showProductionDBMS     && productionDBMS==null)     msg.append("Production DBMS is not defined. ");
				if( showSecondaryDBMS      && secondaryDBMS==null)      msg.append("secondary DBMS is not defined. ");
				if( showProductionProvider && productionProvider==null) msg.append("Production tag provider is not defined. ");
				if( showSecondaryProvider  && secondaryProvider==null)  msg.append("secondary tag provider is not defined. ");
				if( msg.length()==0 ) {
					if(showProductionDatabase&&showSecondaryDatabase&&
					  productionDatabase.getName().equalsIgnoreCase(secondaryDatabase.getName())) {
						msg.append("Production and secondary databases may not be the same. ");
					}
					if(showProductionProvider&&showSecondaryProvider&&
							  productionProvider.getName().equalsIgnoreCase(secondaryProvider.getName())) {
								msg.append("Production and secondary tag providers may not be the same. ");
					}
				}
				
				if( msg.length()==0) {
					valid = true;
					info("Datasource and tag provider definitions are complete.");
				}
				else {
					valid = false;  
					error(msg.toString());
				}
				
				saved = true;
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
	// ================================= Classes for Listing DBMS  ==============================
		public class DBMSList extends DropDownChoice<String> {
			private static final long serialVersionUID = -8014391900949792300L;

			public DBMSList(String key,PropertyModel<String>model,List<String> list) {
				super(key,model,list,new DBMSRenderer());
			}

			@Override
			public boolean wantOnSelectionChangedNotifications() { return true; }

			@Override
			protected void onSelectionChanged(final String newSelection) {
				super.onSelectionChanged(newSelection);
			} 
		}
		public class DBMSRenderer implements IChoiceRenderer<String> {
			private static final long serialVersionUID = -8009556857350933828L;

			@Override
			public Object getDisplayValue(String source) {
				return source;
			}

			@Override
			public String getIdValue(String source, int i) {
				return source;
			}
		}

		private List<String> getDBMSList() {
			InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
			return dataHandler.getDBMSList();
		}
		private String getDefaultDBMS(String name ) {
			String result = null;
			for(String meta:getDBMSList() ) {
				if(meta.equalsIgnoreCase(name)) {
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
	
	// If they've pressed "Save", allow user to proceed,
	// no matter what -- they've been warned.
	@Override
	public boolean isNextAvailable() {
		return valid || saved;
	}
}
