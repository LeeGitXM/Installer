/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.File;
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
import com.ils.ai.gateway.model.TempFileTaskProgressListener;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 */
public class TagStep extends BasicInstallerStep implements TempFileTaskProgressListener {
	private static final long serialVersionUID = 5388412865553172897L;
	private TagProviderMeta selectedProvider = null;

	public TagStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final TagStep thisPage = this;
        
		add(new Label("preamble",preamble));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));;
        
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        ProviderList providers = new ProviderList("providers", new PropertyModel<TagProviderMeta>(this, "selectedProvider"), getProviderList());
		add(providers);
		
        List<String> resources = handler.getArtifactNames(index, data);
        add(new ListView<String>("tags", resources) {
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
            		dataHandler.loadArtifactAsTags(index,selectedProvider,name,data,TagStep.this);
            		/*
            		if( result==null ) {
            			thisPage.info(String.format("Successfully loaded tag resource %s", name));
            			PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            		}
            		else thisPage.warn(result);
            		*/
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
			return provider.getName();
		}
	}
	
	private List<TagProviderMeta> getProviderList() {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		return context.getTagManager().getProviderInformation();
	}
	
//  ===================================== Task Progress Listener  ====================================
	// Retain the path to the temp file with data so that it can be deleted
	@Override
	public void setTempFile(File file) {
		
	}
	@Override
	public void setIndeterminate(boolean flag) {
	}
	@Override
	public void setNote(String text) {
		System.out.println("TagStep.setNote = "+text);
	}
	@Override
	public void setProgress(int progress) {
		System.out.println("TagStep.setProgress = "+progress);
		
	}
	@Override
	public void setProgressMax(int maxProgress) {
		System.out.println("TagStep.setProgressMax = "+maxProgress);
	}
	@Override
	public boolean isCanceled() {
		return false;
	}

}
