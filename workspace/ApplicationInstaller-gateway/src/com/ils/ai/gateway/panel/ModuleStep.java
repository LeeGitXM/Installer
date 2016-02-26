package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;

/**
 */
public class ModuleStep extends InstallWizardStep {
	private static final long serialVersionUID = -3742149120641480873L;


	public ModuleStep(int index,InstallWizardStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        InstallerData data = dataModel.getObject();
        String preamble = handler.getStepPreamble(index, data);
        add(new Label("preamble",preamble));
        
        List<String> modules = handler.getArtifactNames(index, data);
        add(new ListView<String>("modules", modules) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("text", text));
            }
        });
        
        add(new Button("doit") {
            public void onSubmit() {
                info("Doit was pressed!");
            }
        });
    }
}
