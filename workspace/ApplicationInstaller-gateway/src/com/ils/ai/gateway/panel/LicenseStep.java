package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
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
	private boolean accepted = false;
	private final CheckBox checkbox;

	public LicenseStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        // Accept license
        checkbox = new CheckBox("accept", Model.of(Boolean.FALSE));
        Form<?> form = new Form<Void>("acceptForm") {
			private static final long serialVersionUID = -7113329313634987198L;

			@Override
			protected void onSubmit() {
				accepted = checkbox.getModelObject().booleanValue();
				if( accepted ) info("License accepted");
			}
		};
		add(form);
		form.add(checkbox);
		
        // View license
        add(new Link<Void>("view") {
			private static final long serialVersionUID = -279565247005738138L;

			@Override
            public void onClick() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1730037915972320415L;

					@Override
                    public void write(OutputStream output) throws IOException {
                        byte[] bytes = handler.getArtifactAsBytes(index,"license",data);
                        if( bytes!=null ) {
                        	output.write(bytes);
                        }
                    }
					
					@Override
					public String getContentType () {
						return "text/html";
					}
                };
                
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        });
        
    }
}
