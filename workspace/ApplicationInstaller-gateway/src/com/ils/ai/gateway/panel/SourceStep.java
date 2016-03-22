/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.Artifact;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SourceStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;

	public SourceStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
        List<Artifact> packages = handler.getArtifacts(index, data);
        add(new ListView<Artifact>("packages", packages) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<Artifact> item) {
				Artifact artifact = item.getModelObject();
                item.add(new Label("package", artifact.getName()));
                // Get the existing release number from the internal database
                PersistenceHandler dbhandler = PersistenceHandler.getInstance();
                String release = dbhandler.getArtifactRelease(data.getProductName(),panelData.getType(), 
                							panelData.getSubtype(), artifact.getName());
                item.add(new Label("existing", release));
                item.add(new Label("release", artifact.getRelease()));
                item.add(new Link<Void>("download") {
					private static final long serialVersionUID = 7970706631398925536L;

					@Override
					public void onClick() {
						AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
							@Override
		                    public void write(OutputStream output) throws IOException {
								InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		                        byte[] bytes = dataHandler.getArtifactAsBytes(index,artifact.getName(),data);
		                        if( bytes!=null ) {
		                        	output.write(bytes);
		                        	PersistenceHandler.getInstance().setArtifactRelease(data.getProductName(), panelData.getType(), 
		                        			panelData.getSubtype(), artifact.getName(), artifact.getRelease());
		                        }
		                    }
							@Override
							public String getContentType () {
								InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		                        String mime = dataHandler.getArtifactMimeType(index,artifact.getName(),data);
		                        return mime;
							}
						};

						ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream,fileNameFromLocation(artifact.getLocation()));
						getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
					}
				});
            }
        });
	}
}
