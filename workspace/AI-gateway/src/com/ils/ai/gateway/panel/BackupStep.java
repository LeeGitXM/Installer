/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class BackupStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "ignition-backup.gwbk";

	public BackupStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        add(new Label("preamble",preamble).setEscapeModelStrings(false));
        
        // Note: Tried as a "Link" class to same effect within a form and not. 
        //       Status bars do not appear.
        Form<InstallerData> form = new Form<InstallerData>("backupForm", new CompoundPropertyModel<InstallerData>(data));
        Button button = new Button("backup") {
			private static final long serialVersionUID = 1L;
			String result = null;

			@Override
            public void onSubmit() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 3787754864513466176L;
					
					@Override
                    public void write(OutputStream output) throws IOException {
						InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
                        result = dataHandler.backup(output,data);;
                        if(result==null) info("Backup completed successfully");
                        else error(result);
                        //System.out.println("BACKUPSTEP result:"+result);
                    }
                };
                
                //System.out.println("BACKUPSTEP Starting ...");
                ResourceStreamRequestHandler handle = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handle);
                
                BackupStep.this.error("DONE"); // Works without getRequestCycle.scheduleAfter
                //System.out.println("BACKUPSTEP DONE");
            }
        };
        form.add(button);
        add(form);
    }
}
