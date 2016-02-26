package com.ils.installermodule.setup;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SelectFile extends GatewayWizardStep {

    private static String fileName = "testfile.txt";

    public SelectFile(Model<SetupItem> dataModel) {
        super(null, BundleUtil.get().getString("ils.project.title"), dataModel);

        add(new Link<Void>("saveButton") {
            @Override
            public void onClick() {
                AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(OutputStream output) throws IOException {
                        InputStream input = getClass().getResourceAsStream(fileName);
                        output.write(IOUtils.toByteArray(input));
                    }
                };

                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream, fileName);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        });
    }

    @Override
    public boolean isLastStep() {
        return false;
    }

    @Override
    public IDynamicWizardStep next() {
        Model<SetupItem> defaultModel = (Model<SetupItem>) this.getDefaultModel();
        return new StyledPanel(defaultModel);
    }

}