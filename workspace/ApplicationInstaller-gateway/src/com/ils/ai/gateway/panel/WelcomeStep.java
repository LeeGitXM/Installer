package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class WelcomeStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "ReleaseNotes.pdf";

	
	public WelcomeStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        InstallerData data = dataModel.getObject();
        
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        List<PropertyItem> properties = handler.getProperties(data);
        // Get current values of the product properties.
        PersistenceHandler dbHandler = PersistenceHandler.getInstance();
        // For starters get the product name
		String productName = "";
		for(PropertyItem prop:properties) {
			if(prop.getName().equalsIgnoreCase("product")) {
				productName  = prop.getValue();
				break;
			};
		}
		if( !productName.isEmpty() ) {
			for(PropertyItem prop:properties) {
				 String val = dbHandler.getProductProperty(productName, prop.getName());
				 if( val!= null) prop.setPrevious(val);
			}
		}
        
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
		// Accept license
        String current = handler.getPreference("currentCheckbox");
		CheckBox checkbox = new CheckBox("current", (current.equalsIgnoreCase("true")?Model.of(Boolean.TRUE):Model.of(Boolean.FALSE))) {
			private static final long serialVersionUID = -890605923748905601L;
			

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			// We don't care what the value is. As long as they click on the box, we're good.
			// The value is "on" for selected, null for not.
			@Override
			public void onSelectionChanged() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
				if(getValue()==null) {
					data.setIgnoringCurrent(false);
					dataHandler.setPreference("currentCheckbox","false");
				}
				else {
					data.setIgnoringCurrent(true);
					dataHandler.setPreference("currentCheckbox","true");
				}
			}
		};
		add(checkbox);
		
		// Essential checkbox
		String essential = handler.getPreference("essentialCheckbox");
		checkbox = new CheckBox("essential", (essential.equalsIgnoreCase("true")?Model.of(Boolean.TRUE):Model.of(Boolean.FALSE))) {
			private static final long serialVersionUID = -890605923748905601L;

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			// We don't care what the value is. As long as they click on the box, we're good.
			// The value is "on" for selected, null for not.
			@Override
			public void onSelectionChanged() {
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
				if(getValue()==null) {
					data.setIgnoringOptional(false);
					dataHandler.setPreference("essentialCheckbox","false");
				}
				else {
					data.setIgnoringOptional(true);
					dataHandler.setPreference("essentialCheckbox","true");
				}
			}
		};
		add(checkbox);
		
        // View release notes
        add(new Link<Void>("notes") {
			private static final long serialVersionUID = -8430219201330058910L;

			@Override
            public void onClick() {
				
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 3787754864513466176L;

					@Override
                    public void write(OutputStream output) throws IOException {
						InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
                        byte[] bytes = dataHandler.getArtifactAsBytes(index,"notes",data);
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
