/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class DatabaseStep extends BasicInstallerStep {
	private static final long serialVersionUID = -3742149120641480873L;
	private TagProviderMeta selectedConnection = null;

	public DatabaseStep(int index,BasicInstallerStep previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	

        final DatabaseStep thisPage = this;
        
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        

	}

	

}
