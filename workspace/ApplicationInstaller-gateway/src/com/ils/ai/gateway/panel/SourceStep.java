/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

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
            }
        });

        /*
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
		*/
	}
}
