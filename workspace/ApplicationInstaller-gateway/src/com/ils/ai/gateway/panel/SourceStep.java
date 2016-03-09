/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SourceStep extends BasicInstallerStep {
	private static final long serialVersionUID = -3742149120641480873L;

	public SourceStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));
		
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
        List<String> packages = handler.getArtifactNames(index, data);
        add(new ListView<String>("package", packages) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });

        // Get the resource name and file name from the first artifact (there should only be one)
        String artifactName = "source";
        String fileName = "source.zip";
        List<Artifact> artifacts = handler.getArtifacts(panelIndex, data);
        if(!artifacts.isEmpty()) {
        	Artifact art = artifacts.get(0);
        	artifactName = art.getName();
        	fileName = art.getLocation();
        	int pos = fileName.lastIndexOf("/");
        	if( pos>0 ) fileName=fileName.substring(pos+1);
        }
        final String aname = artifactName;
        final String fname = fileName;
		// Download source
		add(new Link<Void>("download") {
			private static final long serialVersionUID = -279565247005738138L;
			@Override
			public void onClick() {

				AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1730037915972320415L;

					@Override
					public void write(OutputStream output) throws IOException {
						InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
						byte[] bytes = dataHandler.getArtifactAsBytes(panelIndex,aname,data);
						if( bytes!=null ) {
							output.write(bytes);
						}
					}

					@Override
					public String getContentType () {
						return "application/zip";
					}
				};

				ResourceStreamRequestHandler requestHandler = new ResourceStreamRequestHandler(rstream, fname);
				getRequestCycle().scheduleRequestHandlerAfterCurrent(requestHandler);
				PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
				info("Source download is complete");
			}
		});
	}
}
