/**
 * Copyright 2016-2022. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.ArrayList;
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
import com.ils.ai.gateway.panel.TagWithDropdownStep.ProviderList;
import com.ils.ai.gateway.panel.TagWithDropdownStep.ProviderRenderer;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.sqltags.TagProviderMetaImpl;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.common.tags.model.TagProvider;
import com.inductiveautomation.ignition.common.tags.model.TagProviderProps;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Also known as TagGroup
 */
public class ScanClassWithDropdownStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 2204950686203860253L;
	private String providerName = "";
	private TagProviderMeta provider=null;

	public ScanClassWithDropdownStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		
        List<String> tagGroups = dataHandler.getArtifactNames(index, data);
        add(new ListView<String>("taggroups", tagGroups) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
            
		// Display the pull down
        System.out.println("Constructing a Toolkit Record Handler...");
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(dataHandler.getContext());
		List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
    
    	// Create the wicket widgets
		System.out.println("Constructing provider label...");
    	Label providerLabel = new Label("providerLabel", "Tag Provider: ");
    	add(providerLabel);
    	System.out.println("Constructing dropdown...");
    	ProviderList providers = new ProviderList("providers", new PropertyModel<TagProviderMeta>(this, "provider"), getProviderList());
		add(providers);
        
        System.out.println("Adding button...");
        add(new Button("install") {
			private static final long serialVersionUID = 4110668774811578782L;
			
			@Override
            public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	// They must select a tag provider from the dropdown
            	if (providerName.equalsIgnoreCase("")){
            		error(failure.insert(0,"Please select a tag provider from the dropdown").toString());
            	}
            	else {
            		for(String name:names) {
	            		System.out.println("ScanClassStep: processing: "+name+" for tag provider: "+providerName);
	            		String result = dataHandler.loadArtifactAsTagGroup(index,providerName,name,data);
	            		System.out.println("...result: "+result);
	            		if( result==null ) {
	            			PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
	            			panelData.setCurrentVersion(futureVersion);
	            			info(String.format("Successfully loaded tag group %s", name));
	            		}
	            		else error(result);
            		}
            	}
            }
        });
    }

	// ================================= Classes for Listing Tag Provider  ==============================
	public class ProviderList extends DropDownChoice<TagProviderMeta> {
		private static final long serialVersionUID = -1021505223044346435L;

		public ProviderList(String key,PropertyModel<TagProviderMeta>model,List<TagProviderMeta> list) {
			super(key,model,list,new ProviderRenderer());
			//System.out.println("Constrtucting a ProviderList...");
		}

		@Override
		public boolean wantOnSelectionChangedNotifications() { return true; }

		@Override
		protected void onSelectionChanged(final TagProviderMeta newSelection) {
			System.out.println("...selection changed to <" + newSelection.getName() + ">...");
			super.onSelectionChanged(newSelection);
			System.out.println("...handled!");
			providerName = newSelection.getName();
		} 
	}
	public class ProviderRenderer implements IChoiceRenderer<TagProviderMeta> {
		private static final long serialVersionUID = -700778014486584571L;
		
		@Override
		public Object getDisplayValue(TagProviderMeta provider) {
			//System.out.println("Getting a display value: " + provider.getName());
			return provider.getName();
		}

		@Override
		public String getIdValue(TagProviderMeta provider, int i) {
			//System.out.println("Getting a ID value: " + i + " " + provider.getName());
			return provider.getName();
		}
	}

	private List<TagProviderMeta> getProviderList() {
		//System.out.println("Getting a provider list...");
		List<TagProviderMeta> result = new ArrayList<>();
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		//System.out.println("...getting choices from tag manager...");
		List<TagProvider> tagProviders = context.getTagManager().getTagProviders();
	    //System.out.println("...iterating...");
	    for (TagProvider tagProvider : tagProviders) {
	    	try {
	    		TagProviderProps props = tagProvider.getPropertiesAsync().get();
	    		TagProviderMeta meta = new TagProviderMetaImpl(props.getName(),props.getDescription());
	    		//System.out.println("...adding: <" + props.getName() + ">");
	    		result.add(meta);
	    	}
	    	catch(Exception ex) {
	    		System.out.println("DefinitionStop: Exception "+ex.getLocalizedMessage());
	    	}
	    }
	    return result;
	}
	
	private TagProviderMeta getDefaultProvider(String name ) {
		//System.out.println("Getting a default value...");
		TagProviderMeta result = null;
		for(TagProviderMeta meta:getProviderList() ) {
			if(meta.getName().equalsIgnoreCase(name)) {
				result = meta;
				break;
			}
		}
		return result;
	}
	
}
