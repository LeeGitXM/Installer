/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * Take properties from the bill of materials and add to the internal database.
 */
public class ToolkitStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;

	public ToolkitStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel); 	

		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

		// Install properties into internal database
		add(new Button("install") {
			private static final long serialVersionUID = 4330778774811578782L;
			
			public void onSubmit() {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
				InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
            	List<PropertyItem> properties = dataHandler.getPanelProperties(index, data);
            	PersistenceHandler dbHandler = PersistenceHandler.getInstance();
            	
            	// Special types get special handling. Otherwise we simply set a toolkit property		
            	for(PropertyItem prop:properties) {
            		String type = prop.getType();
            		String value = prop.getValue();
            		if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ALARM_PROFILE)) {
            			dbHandler.addNamedAlarmProfile(value);
            		}
            		else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ALLOW_USER_ADMIN)) {
            			dbHandler.setAllowUserAdmin(value);
            		}
            		else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ONCALL_ROSTER)) {
            			dbHandler.addNamedAlarmCallRoster(value);
            		}
            		else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SMTP_PROFILE)) {
            			dbHandler.addNamedSMTPProfile(value);
            		}
            		else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PROJECT_DEFAULT_DATASOURCE)) {
            			dbHandler.setDefaultDatasourceForProject();
            		}
            		else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PROVIDER_DEFAULT_DATASOURCE)) {
            			dbHandler.setDefaultDatasourceForProvider();
            		}
            		else {
            			toolkitHandler.setToolkitProperty(prop.getName(),value);
            		}
            	}
            	dbHandler.setStepVersion(product, type, subtype, futureVersion);
            	panelData.setCurrentVersion(futureVersion);
            	ToolkitStep.this.info(String.format("Update of internal database complete."));
            }
        });
	}
}
