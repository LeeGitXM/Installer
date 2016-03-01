package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class BackupStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "ignition-backup.gwbk";

	public BackupStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        InstallerData data = dataModel.getObject();
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        //Form form = new Form("backupForm");
        Link<?> link = new Link<Void>("backup") {
			private static final long serialVersionUID = 1L;
			String result = null;

			@Override
            public void onClick() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 3787754864513466176L;
					
					@Override
                    public void write(OutputStream output) throws IOException {
						InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
						System.out.println("BACKUPSTEP Starting ...");
                        result = dataHandler.backup(output,data);
                        System.out.println("BACKUPSTEP complete");
                        if(result==null) BackupStep.this.info("Backup completed successfully");
                        else BackupStep.this.warn(result);
                        System.out.println("BACKUPSTEP result:"+result);
                    }
                };
                
                ResourceStreamRequestHandler handle = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handle);
                // Note: info() or warn() here causes exception on next page.
                BackupStep.this.warn("DONE");
            }
        };
        add(link);
        //add(form);
    }
}
