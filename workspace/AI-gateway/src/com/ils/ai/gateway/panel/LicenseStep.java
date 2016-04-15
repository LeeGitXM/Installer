/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class LicenseStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "license.html";
	private boolean accepted = false;

	public LicenseStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Accept license
		CheckBox checkbox = new CheckBox("accept", Model.of(Boolean.FALSE)) {
			private static final long serialVersionUID = -890605923748905601L;

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			// We don't care what the value is. As long as they click on the box, we're good.
			// The value is "on" for selected, null for not.
			@Override
			public void onSelectionChanged() {
				if(getValue()!=null) {
					accepted = true;
					PersistenceHandler.getInstance().setStepVersion(product, type, subtype, futureVersion);
					LicenseStep.this.info(String.format("License terms have been accepted."));
				}
			}
		};
		add(checkbox);

		// View license
		add(new Link<Void>("view") {
			private static final long serialVersionUID = -279565247005738138L;
			@Override
			public void onClick() {

				AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1730037915972320415L;

					@Override
					public void write(OutputStream output) throws IOException {
						InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
						byte[] bytes = dataHandler.getArtifactAsBytes(panelIndex,"license",data);
						if( bytes!=null ) {
							output.write(bytes);
						}
					}

					@Override
					public String getContentType () {
						return "text/html";
					}
				};

				ResourceStreamRequestHandler requestHandler = new ResourceStreamRequestHandler(rstream, fileName);
				getRequestCycle().scheduleRequestHandlerAfterCurrent(requestHandler);
			}
		});
	}
	
	@Override
	public boolean isNextAvailable() {
		return accepted || panelData.getCurrentVersion() >= futureVersion ;
	}
}
