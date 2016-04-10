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
import com.ils.ai.gateway.model.PropertyItem;

/**
 * The purpose of this step is to validate that an authentication profile exists with the required
 * user roles and users with those roles.
 * 
 * See Persistence Records:
 * InternalRoleRecord
 * InternalUserRoleMapping
 * UserSourceProfileRecord
 */
public class AuthenticationStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;

	public AuthenticationStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
        super(index,previous, title, dataModel); 
       
        InstallerDataHandler handler = InstallerDataHandler.getInstance();
        add(new Label("preamble",preamble).setEscapeModelStrings(false));
        
        List<PropertyItem> roles = handler.getAuthenticationRoles(panelIndex,data);
		add(new ListView<PropertyItem>("roles", roles) {
			private static final long serialVersionUID = -4610581829738917953L;

			protected void populateItem(ListItem<PropertyItem> item) {
                PropertyItem property = (PropertyItem) item.getModelObject();
                item.add(new Label("name", property.getName()));
                item.add(new Label("role", property.getValue()));
            }
        });
		
        add(new Button("validate") {
			private static final long serialVersionUID = 4110778774811578782L;
			
			public void onSubmit() {
				PersistenceHandler ph = PersistenceHandler.getInstance();
				boolean valid = ph.validateRoleList(roles);
				if( valid ) info("Validated: One or more authentication profiles that contain all the required roles");
				else warn("There is no authentication profile that contains all the required roles");
			}
        });
    }
}
