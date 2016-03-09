/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;

/**
 */
public class DocumentationStep extends BasicInstallerStep {
	private static final long serialVersionUID = -3742149120641480873L;


	public DocumentationStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
        
        final DocumentationStep thisPage = this;
        
		add(new Label("preamble",preamble));

        
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        List<String> modules = handler.getArtifactNames(index, data);
        add(new ListView<String>("documents", modules) {
			private static final long serialVersionUID = 8682507940096836472L;

			protected void populateItem(ListItem<String> item) {
                String text = (String) item.getModelObject();
                item.add(new Label("name", text));
            }
        });
   
    }
}
