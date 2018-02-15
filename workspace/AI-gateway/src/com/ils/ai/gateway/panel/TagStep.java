/**
 * Copyright 2016-2018. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.xml.sax.SAXException;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class TagStep extends BasicInstallerPanel {
	private static final long serialVersionUID = 5388412865553172897L;
	private Label providerLabel = null;
	private String provider = "";
	private String statusString = "";

	public TagStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
        
        InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        provider = dataHandler.providerNameFromProperties(index, data);
        providerLabel = new Label("provider",provider);
		add(providerLabel);
		
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
	
            	StringBuilder success = new StringBuilder("");
            	StringBuilder failure = new StringBuilder("");
            	
            	
            	for(String artifactName:names) {
            		String result = null;
            		List<File> files = dataHandler.getArtifactAsListOfTagFiles(panelIndex, artifactName, data);
            		long count = 0;
            		statusString = String.format("installed ~ %d tags",count);
            		
            		for( File file:files ) {
            			try {
            				dataHandler.tagUtil.importFromFile(file,provider);
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
            		}
            		if( result==null ) {
            			if(success.length()>0) success.append(", ");
            			success.append(String.format("%s(%s)", artifactName,statusString));
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
            		error(failure.insert(0,"Failed to load: ").toString());
            	}
            }
        });
    }
	
	/**
	 * Analyze the XML file counting <tag> elements.
	 * @param path
	 * @return the tag count
	 */
	private long getTagCount(Path path) {
		long count = 0;
		try {
			Stream<String> lines = Files.lines(path);
			count = lines.filter(line->line.indexOf("</Tag>")>0).count();
			lines.close();
		}
		catch(IOException ioe) {
			System.out.println("TagStep.getTagCount: Exception "+ioe.getMessage());
		}
		return count;
	}
}
