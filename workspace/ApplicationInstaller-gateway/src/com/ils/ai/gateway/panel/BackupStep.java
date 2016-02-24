package com.ils.ai.gateway.panel;

import java.io.File;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class BackupStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private FileUploadField saveField;
	private String UPLOAD_FOLDER = "/tmp";

	public BackupStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        InstallerData data = dataModel.getObject();
        String preamble = InstallerDataHandler.getInstance().getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        
        saveField = new FileUploadField("save");
        Form<?> form = new Form<Void>("form") {
        	@Override
        	protected void onSubmit() {

        		final FileUpload uploadedFile = saveField.getFileUpload();
        		if (uploadedFile != null) {

        			// write to a new file
        			File newFile = new File(UPLOAD_FOLDER
        					+ uploadedFile.getClientFileName());

        			if (newFile.exists()) {
        				newFile.delete();
        			}

        			try {
        				newFile.createNewFile();
        				uploadedFile.writeTo(newFile);

        				info("saved file: " + uploadedFile.getClientFileName());
        			} catch (Exception e) {
        				throw new IllegalStateException("Error");
        			}
        		}

        	}

        };

   		// Enable multipart mode (need for uploads file)
   		form.setMultiPart(true);

   		form.add(saveField);

   		add(form);
        
        warn("This is a warning that something is amiss");
    }
}
