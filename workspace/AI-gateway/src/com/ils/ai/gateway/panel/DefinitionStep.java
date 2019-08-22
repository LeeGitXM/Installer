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
import com.inductiveautomation.ignition.common.tags.model.TagProvider;
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
	private TagProvider productionProvider=null;
	private TagProvider secondaryProvider=null;
	private boolean saved = false;
	private boolean valid = false;

	public DefinitionStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble).setEscapeModelStrings(false));

		// Display the pull downs based on panel properties.
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
		List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
    
		// If the property already has a value, don't show the field.
    	for(PropertyItem prop:properties) {
    		String type = prop.getType();
			if(type==null || type.isEmpty()) continue;
			// If the property has a fixed value, set the defaults directly, don't show.
			if( prop.getValue()!=null && !prop.getValue().isEmpty() ) {
				String value = prop.getValue();
				if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))  toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))      toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,true),value);
	    		}
	    		else if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DBMS)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,true),value);
	    		}
	    		else if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,false),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true),value);
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true),value);
	    		}
			}
			else {
				if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))  showProductionProvider = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryProvider = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) showSecondaryProvider = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) showProductionProvider = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))      showProductionProvider = true;
	    		}
	    		else if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DBMS)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) showProductionDBMS = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryDBMS = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) showSecondaryDBMS = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) showProductionDBMS = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) showProductionDBMS = true;
	    		}
	    		else if(prop.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE)) {
	    			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) showProductionDatabase = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) showSecondaryDatabase = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) showSecondaryDatabase = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) showProductionDatabase = true;
	    			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) showProductionDatabase = true;
	    		}
			}
    		
    	}
		
    	// Create all the wicket widgets, just don't make them all visible
    	
    	Label productionProviderLabel = new Label("productionProviderLabel",dataHandler.getLabel(properties,true)+" Tag Provider: ");
    	add(productionProviderLabel);
    	ProviderList productionProviders = new ProviderList("productionProviders", new PropertyModel<TagProvider>(this, "productionProvider"), getProviderList());
		add(productionProviders);
		Label secondaryProviderLabel = new Label("secondaryProviderLabel",dataHandler.getLabel(properties,false)+" Tag Provider: ");
    	add(secondaryProviderLabel);
    	ProviderList secondaryProviders = new ProviderList("secondaryProviders", new PropertyModel<TagProvider>(this, "secondaryProvider"), getProviderList());
		add(secondaryProviders);
    	Label productionDatabaseLabel = new Label("productionDatabaseLabel",dataHandler.getLabel(properties,true)+" Database: ");
    	add(productionDatabaseLabel);
    	SourceList productionDatabases = new SourceList("productionDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "productionDatabase"), getDatasourceList());
		add(productionDatabases);
		Label secondaryDatabaseLabel = new Label("secondaryDatabaseLabel",dataHandler.getLabel(properties,false)+" Database: ");
    	add(secondaryDatabaseLabel);
    	SourceList secondaryDatabases = new SourceList("secondaryDatabases", new PropertyModel<SerializableDatasourceMeta>(this, "secondaryDatabase"), getDatasourceList());
		add(secondaryDatabases);
		Label productionDBMSLabel = new Label("productionDBMSLabel",dataHandler.getLabel(properties,true)+" DBMS: ");
    	add(productionDBMSLabel);
    	DBMSList productionDBMSs = new DBMSList("productionDBMSs", new PropertyModel<String>(this, "productionDBMS"), getDBMSList());
		add(productionDBMSs);
		Label secondaryDBMSLabel = new Label("secondaryDBMSLabel",dataHandler.getLabel(properties,false)+" DBMS: ");
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
		productionDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true)));
		secondaryDatabase= getDefaultDatasource(toolkitHandler.getToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,false)));
		productionDBMS= getDefaultDBMS(toolkitHandler.getToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,true)));
		secondaryDBMS = getDefaultDBMS(toolkitHandler.getToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,false)));
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
				
				if( productionDatabase!=null ) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,true),productionDatabase.getName());
				if( secondaryDatabase!=null )  toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DATABASE,false),secondaryDatabase.getName());
				if( productionDBMS!=null )     toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,true),productionDBMS);
				if( secondaryDBMS!=null )      toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_DBMS,false),secondaryDBMS);
				if( productionProvider!=null ) toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,true),productionProvider.getName());
				if( secondaryProvider!=null )  toolkitHandler.setToolkitProperty(dataHandler.getToolkitTag(properties,InstallerConstants.PROPERTY_PROVIDER,false),secondaryProvider.getName());
				
				// Check validity
				StringBuilder msg = new StringBuilder();
				if( showProductionDatabase && productionDatabase==null )msg.append("Production database is not defined. ");
				if( showSecondaryDatabase  && secondaryDatabase==null)  msg.append("secondary database is not defined. ");
				if( showProductionDBMS     && productionDBMS==null)     msg.append("Production DBMS is not defined. ");
				if( showSecondaryDBMS      && secondaryDBMS==null)      msg.append("secondary DBMS is not defined. ");
				if( showProductionProvider && productionProvider==null) msg.append("Production tag provider is not defined. ");
				if( showSecondaryProvider  && secondaryProvider==null)  msg.append("secondary tag provider is not defined. ");
				if( msg.length()==0 ) {
					/*
					 * We want to allow this in the EMC installation.
					if(showPoductionDatabase&&showSecondaryDatabase&&
					  productionDatabase.getName().equalsIgnoreCase(secondaryDatabase.getName())) {
						msg.append("Production and secondary databases may not be the same. ");
					}
					*/
					if(showProductionProvider&&showSecondaryProvider&&
							  productionProvider.getName().equalsIgnoreCase(secondaryProvider.getName())) {
								msg.append("Production and secondary tag providers may not be the same. ");
					}
				}
				
				// If there are scripts attached to the properties, execute them. The scripts rely on the property value 
				// being set. We don't bother setting unless there is a script involved.
				// If the property has a value already, use it.
				for(PropertyItem property:properties) {
					String type = property.getType();
					if(type==null || type.isEmpty()) continue;
		    		String script = property.getScript();
		    		if( !script.isEmpty() ) {
		        		if(property.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER)) {
		        			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))  property.setValue(productionProvider.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) property.setValue(secondaryProvider.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) property.setValue(secondaryProvider.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) property.setValue(productionProvider.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))      property.setValue(productionProvider.getName());
		        		}
		        		else if(property.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DBMS)) {
		        			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) property.setValue(productionDBMS);
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) property.setValue(secondaryDBMS);
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) property.setValue(secondaryDBMS);
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) property.setValue(productionDBMS);
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) property.setValue(productionDBMS);
		        		}
		        		else if(property.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE)) {
		        			if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION)) property.setValue(productionDatabase.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY)) property.setValue(secondaryDatabase.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) property.setValue(secondaryDatabase.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) property.setValue(productionDatabase.getName());
		        			else if(type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC)) property.setValue(productionDatabase.getName());
		        		}
		    			String result = dataHandler.executePythonFromProperty(property);
		    			//System.out.println(String.format("DefinitionStep: Executing script: "+script));
		    			if( !result.isEmpty()) {
		    				//System.out.println(String.format("DefinitionStep: Script error: \n"+result));
		    				msg.append(result);
		    				break;
		    			}
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
	public class ProviderList extends DropDownChoice<TagProvider> {
		private static final long serialVersionUID = -1021505223044346435L;

		public ProviderList(String key,PropertyModel<TagProvider>model,List<TagProvider> list) {
			super(key,model,list,new ProviderRenderer());
		}

		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		protected void onSelectionChanged(final TagProvider newSelection) {
			super.onSelectionChanged(newSelection);
		} 
	}
	public class ProviderRenderer implements IChoiceRenderer<TagProvider> {
		private static final long serialVersionUID = -700778014486584571L;

		@Override
		public Object getDisplayValue(TagProvider provider) {
			return provider.getName();
		}

		@Override
		public String getIdValue(TagProvider provider, int i) {
			return provider.getName();
		}
	}

	private List<TagProvider> getProviderList() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getTagManager().getTagProviders();
	}
	
	private TagProvider getDefaultProvider(String name ) {
		TagProvider result = null;
		for(TagProvider provider:getProviderList() ) {
			if(provider.getName().equalsIgnoreCase(name)) {
				result = provider;
				break;
			}
		}
		return result;
	}
	
	// If they've pressed "Save" and made all selections,
	// allow user to proceed,
	@Override
	public boolean isNextAvailable() {
		return valid && saved;
	}
}
