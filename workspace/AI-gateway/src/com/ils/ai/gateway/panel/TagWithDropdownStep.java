/**
 * Copyright 2016-2018. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.xml.sax.SAXException;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.ai.gateway.panel.DatabaseWithDropdownStep.SourceList;
import com.ils.ai.gateway.panel.DefinitionStep.ProviderRenderer;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.datasource.SerializableDatasourceMeta;
import com.inductiveautomation.ignition.common.sqltags.TagProviderMetaImpl;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.common.tags.model.TagProvider;
import com.inductiveautomation.ignition.common.tags.model.TagProviderProps;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class TagWithDropdownStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 5388412865553172897L;
	private String base = "";
	private String tagType = "";
	private String statusString = "";
	private String providerName = "";
	private TagProviderMeta provider=null;

	public TagWithDropdownStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        tagType = dataHandler.getTagTypeFromProperties(index, data);
        System.out.println("TagWithDropdownStep: processing " + tagType);
        		
		List<String> resources = dataHandler.getArtifactNames(index,data);
		add(new ListView<String>("tags",resources) {
			private static final long serialVersionUID = -7571784271601338236L;

			protected void populateItem(ListItem<String> item) {
				String text = (String)item.getModelObject();
				item.add(new Label("name",text));
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
			private static final long serialVersionUID = 4110778774811578782L;
			
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
	                if (tagType.equalsIgnoreCase("tag")) {
	                	base = "[" + providerName + "]";
	                }
	                else {
	                	base = "[" + providerName + "]_types_";
	                }
	
	
	            	for(String artifactName:names) {
	            		String result = null;
	            		File file = dataHandler.getArtifactAsTemporaryFile(panelIndex, artifactName, data);
	            		long count = 0;
	            		statusString = String.format("installed ~ %d tags",count);
	
	            		try {
	            			dataHandler.tagUtil.importTagsFromFile(file,base);
	            			count = getTagCount(file.toPath());
	            			statusString = String.format("~ %d tags",count);
	
	            			System.out.println("TagStep: processing status = "+statusString);
	
	            			setResponsePage(getPage());    // Supposedly this causes a page refresh()
	            			Thread.yield();
	            		}
	            		catch( SAXException saxe) {
	            			result = String.format( "Error with %s file format after ~%d tags (%s)", artifactName,count,saxe.getLocalizedMessage());
	            		}
	            		catch( Exception ex) {
	            			result = String.format( "Failed to install %s after ~%d tags - see wrapper.log for details", artifactName,count);
	            			statusString = String.format("EXCEPTION: file %s (%s)",file.getAbsolutePath(),ex.getMessage());
	            		}
	            	
	            		if( result==null ) {
	            			if(success.length()>0) success.append(", ");
	            			success.append(String.format("%s(%s)", artifactName,statusString));
	            		}
	            		else {
	            			if(failure.length()>0) failure.append(", ");
	            			failure.append(String.format("%s(%s)", artifactName,result));
	            		}
	
	            		if(failure.length()==0 ) {
	            			PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
	            			panelData.setCurrentVersion(futureVersion);
	            			info(success.insert(0,"Successfully loaded tags: ").toString());
	            		}
	            		else {
	            			error(failure.insert(0,"Failed to load: ").toString());
	            		}
	            	}
            	}
			}
        });
    }
	
	/**
	 * Analyze the  file counting <tag> elements for .xml
	 * or "tagType" for .json.
	 * @param path
	 * @return the tag count
	 */
	private long getTagCount(Path path) {
		long count = 0;
		try {
			Stream<String> lines = Files.lines(path);
			count = lines.filter(line->line.indexOf("tagType")>0).count();
			if( count==0 ) {
				count = lines.filter(line->line.indexOf("</Tag>")>0).count();
			}
			lines.close();
		}
		catch(IOException ioe) {
			//System.out.println("TagStep.getTagCount: Exception "+ioe.getMessage());
		}
		return count;
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
