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
public class BackupStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "ignition-backup.gwbk";

	public BackupStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        add(new Link<Void>("backup") {
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 3787754864513466176L;

					@Override
                    public void write(OutputStream output) throws IOException {
                        handler.backup(output,data);
                    }
                };
                
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        });
    }
}
