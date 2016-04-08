/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;

/**
 * Display a final instruction sheet for post-installation cleanup. 
 */
public class FinalPage extends ConfigPanel {
	private static final long serialVersionUID = 6299922354426150044L;
	private final InstallerData data;

	public FinalPage(Model<InstallerData> model){
		super("ils.success.title");
		this.data = model.getObject();
		
		InstallerDataHandler handler = InstallerDataHandler.getInstance();
		String title = handler.getFinalTitle(data);
		add(new Label("title",title).setEscapeModelStrings(false));
		
		String preamble = handler.getFinalPreamble(data);
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		
		List<PropertyItem> notes = handler.getFinalNotes(data);
		add(new ListView<PropertyItem>("notes", notes) {
			private static final long serialVersionUID = -4610581829738917953L;

			protected void populateItem(ListItem<PropertyItem> item) {
                PropertyItem property = (PropertyItem) item.getModelObject();
                item.add(new Label("name", property.getName()));
                item.add(new Label("note", property.getValue()));
            }
        });
    }

    @Override
    public String[] getMenuPath() {
        return null;
    }

}
