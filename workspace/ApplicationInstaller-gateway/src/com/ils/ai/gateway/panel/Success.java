/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;


import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;

/**
 * Display a final summary sheet. This cannot have an associated .html
 * page, else we error out with class-not-found when the module is deleted.
 */
public class Success extends ConfigPanel {
	private static final long serialVersionUID = 6298822354426150044L;

	public Success(){
		super("ils.success.title");
		ComponentHierarchyIterator walker = visitChildren();
		while(walker.hasNext()) {
			Component c = walker.next();
			System.out.println(c.getMarkupId() +": "+c.getClass().getName());
			if(c instanceof Label ) {
				Label label = (Label)c;
				label.setEscapeModelStrings(false);
				System.out.println(label.getDefaultModelObjectAsString());
				Label replacement = new Label(c.getId(),"Replacement <u>text</u>");
				label.replaceWith(replacement);
				label.setEscapeModelStrings(false);
				add(label);
				label.render();
				break;
			}
			else if(c instanceof MultiLineLabel ) {
				MultiLineLabel label = (MultiLineLabel)c;
				System.out.println(label.getDefaultModelObjectAsString());
			}
		}
		
		info("Success");
    }

    @Override
    public String[] getMenuPath() {
        return null;
    }

}
