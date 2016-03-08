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
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class ScanClassStep extends BasicInstallerStep {
	private static final long serialVersionUID = 2204950686203860253L;
	private TagProviderMeta selectedProvider = null;

	public ScanClassStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final ScanClassStep thisPage = this;
        
		add(new Label("preamble",preamble));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));;
        
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        
        ProviderList providers = new ProviderList("providers", new PropertyModel<TagProviderMeta>(this, "selectedProvider"), getProviderList());
		add(providers);
		
        List<String> scanClasses = handler.getArtifactNames(index, data);
        add(new ListView<String>("scanclasses", scanClasses) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
        
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	
            	for(String name:names) {
            		String result = dataHandler.loadArtifactAsScanClass(index,selectedProvider.getName(),name,data);
            		if( result==null ) {
            			thisPage.info(String.format("Successfully loaded scanclass", name));
            			PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            		}
            		else thisPage.warn(result);
            	}
            }
        });
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

			@Override
			public Object getDisplayValue(TagProviderMeta provider) {
				return provider.getName();
			}

			@Override
			public String getIdValue(TagProviderMeta provider, int i) {
				return provider.getDisplayPath();
			}
		}
	private List<TagProviderMeta> getProviderList() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getTagManager().getProviderInformation();
	}
}
