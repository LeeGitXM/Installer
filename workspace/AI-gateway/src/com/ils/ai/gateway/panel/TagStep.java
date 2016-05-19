/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.File;
import java.util.List;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.xml.sax.SAXException;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class TagStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 5388412865553172897L;
	private String provider = "";
	private String statusString = "";
	private final AbstractAjaxTimerBehavior timer;
	private transient AjaxRequestHandler handler = null;

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

		// =================================== Status Label =====================================
		final Model<String> statusModel = new Model<String>();
		Label statusLabel = new Label("status",statusModel);
		statusLabel.setOutputMarkupId(true);
		add(statusLabel);


		
		// =================================== Timer =====================================
		// Drive the progress bar with a timer
		timer = new AbstractAjaxTimerBehavior(Duration.ONE_SECOND) {
			private static final long serialVersionUID = 9100894639351169111L;

			@Override
			protected void onTimer(AjaxRequestTarget target) {
				System.out.println("TagStep: timer status = "+statusString);
				statusModel.setObject(statusString);
			}
		};
		add(timer);
	
        add(new Button("install") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<String> names = dataHandler.getArtifactNames(index, data);
	
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	for(String artifactName:names) {
            		String result = null;
            		List<File> files = dataHandler.getArtifactAsListOfTagFiles(panelIndex, artifactName, data);
            		int count = 0;
            		for( File file:files ) {
            			try {
            				statusString = String.format("Installed ~ %d tags",count);
            				statusModel.setObject(statusString);
            				System.out.println("TagStep: processing status = "+statusString);
            				count = count + InstallerDataHandler.TAG_CHUNK_SIZE;
            				dataHandler.tagUtil.importFromFile(file,provider);
            				setResponsePage(getPage());    // Supposedly this causes a page refresh()
            				notifyAll();
            				Thread.yield();
            			}
            			catch( SAXException saxe) {
            				result = String.format( "Error with %s file format after ~%d tags (%s)", artifactName,count,saxe.getLocalizedMessage());
            			}
            			catch( Exception ex) {
            				result = String.format( "Failed to install %s after ~%d tags - see wrapper.log for details", artifactName,count);
            				statusString = String.format("EXCEPTION: file %s (%s)",file.getAbsolutePath(),ex.getMessage());
            			}
            		}
            		if( result==null ) {
            			if(success.length()>0) success.append(", ");
            			success.append(artifactName);
            		}
            		else {
            			if(failure.length()>0) failure.append(", ");
            			failure.append(String.format("%s(%s)", artifactName,result));
            		}
            	}
            	if(failure.length()==0 ) {
            		PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
            		panelData.setCurrentVersion(futureVersion);
            		info(success.insert(0,"Successfully loaded tags: ").toString());
            	}
            	else {
            		warn(failure.insert(0,"Failed to load: ").toString());
            	}
            	timer.stop(handler);
            }
        });
    }
	@Override
	public void onInitialize() {
		super.onInitialize();
		handler = new AjaxRequestHandler(TagStep.this.getPage());
		timer.restart(handler);
	}
}
