package com.ils.ai.gateway;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.models.IConfigMenuNode;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;
/**
 * A label for the gateway configuration panel that uses a simple
 * string rather than a resource bundle.
 */
public class InstallerLabelConfigMenuNode implements IConfigMenuNode {
	private static final long serialVersionUID = -2640968227454231794L;
	private String key, label;
	
	public InstallerLabelConfigMenuNode(String key, String label){
		this.key = key;
		this.label = label;
	}

	@Override
	public String getMenuKey() {
		return key;
	}

	@Override
	public Class<? extends ConfigPanel> getPanelClass() {
		return null;
	}

	@Override
	public int getPosition() {
		return 700;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public Component newMenuComponent(String id, IConfigPage configPage) {
		return new Label(id, label);
	}

	@Override
	public void setPath(String[] arg0) {
		// noop		
	}

}