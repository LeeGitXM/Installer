package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class LicenseStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "license.html";


	public LicenseStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        // View release notes
        add(new Link<Void>("view") {
			private static final long serialVersionUID = -1024177445288843210L;

			@Override
        	public void onClick() {

        		AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
        			private static final long serialVersionUID = 3787754864513466176L;

        			@Override
        			public void write(OutputStream output) throws IOException {
        				String result = handler.backup(output,data);
        				output.flush();
        				if(result==null) info("Backup completed successfully");
        				else warn(result);
        			}
        			
        		};

        		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
        		getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        	}
        });
        
    }
}
