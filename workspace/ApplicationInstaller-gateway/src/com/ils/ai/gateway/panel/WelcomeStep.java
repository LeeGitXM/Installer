package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.PropertyItem;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class WelcomeStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private final CheckBox checkbox;
	private static String fileName = "ReleaseNotes.pdf";
	
	public WelcomeStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final WelcomeStep thisStep = this;
        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        List<PropertyItem> properties = handler.getProperties(data);
        add(new ListView<PropertyItem>("properties", properties) {
			private static final long serialVersionUID = -4610581829738917953L;

			protected void populateItem(ListItem<PropertyItem> item) {
                PropertyItem property = (PropertyItem) item.getModelObject();
                item.add(new Label("name", property.getName()));
                item.add(new Label("value", property.getValue()));
                item.add(new Label("previous", property.getPrevious()));
            }
        });
        
        // Set whether or not to skip panels that are up-to-date
        checkbox = new CheckBox("skip", Model.of(Boolean.FALSE));
        Form<?> form = new Form<Void>("skipPanelsForm") {
			private static final long serialVersionUID = -7113329313634987198L;

			@Override
			protected void onSubmit() {
				boolean skip = checkbox.getModelObject().booleanValue();
				thisStep.info(String.format("Skip up-to-date = %s",(skip?"TRUE":"FALSE")));
				data.setSkipCurrent(skip);
			}
		};
		add(form);
		form.add(checkbox);
		
        // View release notes
        add(new Link<Void>("notes") {
			private static final long serialVersionUID = -8430219201330058910L;

			@Override
            public void onClick() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 3787754864513466176L;

					@Override
                    public void write(OutputStream output) throws IOException {
                        byte[] bytes = handler.getArtifactAsBytes(index,"notes",data);
                        if( bytes!=null ) {
                        	output.write(bytes);
                        }
                    }
					@Override
					public String getContentType () {
						return "application/pdf";
					}
                };
                
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        });
    }
	
}
