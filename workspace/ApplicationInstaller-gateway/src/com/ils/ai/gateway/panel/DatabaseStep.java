/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class DatabaseStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private String datasource = "";

	public DatabaseStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
        datasource = dataHandler.datasourceNameFromProperties(index, data);
		add(new Label("datasource",datasource));
        

	}

	

}
