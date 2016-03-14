/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.File;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.TempFileTaskProgressListener;

/**
 */
public class TagStep extends BasicInstallerPanel implements TempFileTaskProgressListener {
	private static final long serialVersionUID = 5388412865553172897L;
	private String provider = "";

	public TagStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        provider = dataHandler.providerNameFromProperties(index, data);
		add(new Label("provider",provider));
		
		List<String> resources = dataHandler.getArtifactNames(index,data);
		add(new ListView<String>("tags",resources) {
			private static final long serialVersionUID = -7571784271601338236L;

			protected void populateItem(ListItem<String> item) {
				String text = (String)item.getModelObject();
				item.add(new Label("name",text));
			}
		});
       
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
            	
            	for(String name:names) {
            		dataHandler.loadArtifactAsTags(index,provider,name,data,TagStep.this);
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
