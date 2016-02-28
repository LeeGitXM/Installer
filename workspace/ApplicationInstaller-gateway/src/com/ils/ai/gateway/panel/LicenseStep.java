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
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class LicenseStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private static String fileName = "license.html";
	private boolean accepted = false;

	public LicenseStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	

		InstallerData data = dataModel.getObject();

		String preamble = handler.getStepPreamble(panelIndex, data);
		add(new Label("preamble",preamble));

		// Accept license
		CheckBox checkbox = new CheckBox("accept", Model.of(Boolean.FALSE)) {
			private static final long serialVersionUID = -890605923748905601L;

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			// We don't care what the value is. As long as they click on the box, we're good.
			@Override
			public void onSelectionChanged() {
				System.out.println(String.format("%s.onSelectionChanged: %s","CheckBox",(this.getModel().getObject().booleanValue()?"TRUE":"FALSE")));
				LicenseStep.this.info(String.format("License terms have been accepted."));
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
						byte[] bytes = handler.getArtifactAsBytes(panelIndex,"license",data);
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


	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
	}

}
