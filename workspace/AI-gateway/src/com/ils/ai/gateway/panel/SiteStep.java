/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.SiteEntry;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 * A panel to control installation of files at specific sites.
 */
public class SiteStep extends BasicInstallerPanel {
	private static final long serialVersionUID = -3742149120641480873L;
	private String selectedSite = "";

	public SiteStep(int index,BasicInstallerPanel previous,String title, Model<InstallerData> dataModel){
		super(index,previous, title, dataModel);
		
		add(new Label("preamble",preamble).setEscapeModelStrings(false));
		add(new Label("currentVersion",currentVersionString));
		add(new Label("futureVersion",futureVersionString));

    	// Create all the wicket widgets, just don't make them all visible
    	Label productionProviderLabel = new Label("siteLabel","Site: ");
    	add(productionProviderLabel);

		InstallerDataHandler handler = InstallerDataHandler.getInstance();
        handler.loadArtifactAsSiteEntries(index, data);
        DropDownChoice<String> sites = new DropDownChoice<String>("sites", new PropertyModel<String>(this, "selectedSite"), data.getUniqueSiteNames()) {
			private static final long serialVersionUID = 2602629544295913383L;
			
			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return selectedSite;
			}

        	@Override
        	public void onSelectionChanged(String newSelection) {
        		selectedSite = newSelection;
        		data.setSiteName(selectedSite);
        		defineDatabaseAndProvider(selectedSite,data.getSiteEntries());
        	}
			@Override
			protected boolean wantOnSelectionChangedNotifications() {return true;}
        };
        // Default the site to the first choice in the list.
        if(!data.getSiteEntries().isEmpty()) {
        	selectedSite = data.getSiteEntries().get(0).getSiteName();
            data.setSiteName(selectedSite);
            defineDatabaseAndProvider(selectedSite,data.getSiteEntries());
        }
		add(sites);
        
	}

	/**
	 * The data model isn't quite correct. Loop through the site elements. 
	 * Last database and provider definitions win.
	 */
	private void defineDatabaseAndProvider(String site,List<SiteEntry> siteEntries) {
		GatewayContext context =  InstallerDataHandler.getInstance().getContext();
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		for(SiteEntry se:siteEntries) {
			if( site.equalsIgnoreCase(se.getSiteName()) ) {
				toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE,se.getDatasource());
				toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER,se.getProvider());
				toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE,se.getTestDatasource());
				toolkitHandler.setToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER,se.getTestProvider());
			}
		}
	}

	
	// Do not allow user to proceed until site is selected.
	@Override
	public boolean isNextAvailable() {
		return !selectedSite.isEmpty();
	}
}
