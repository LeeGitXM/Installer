/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

/**
 *  This class is the keeper of all knowledge for the install process.
 *  It is a serializable "bean" that relies on the Singleton InstallerDataHandler
 *  to populate.
 */
public class InstallerData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	private Document bom = null;
	private String modulePath = null;
	private String productName = "";
	private boolean ignoringOptional  = false;
	private boolean ignoringCurrent   = false;   // Ignore artifacts that are up-to-date
	private final Map<Integer,PanelData> panelMap = new HashMap<>();

	public Document getBillOfMaterials() {return bom;}
	public String getModulePath() {return this.modulePath;}
	public Map<Integer,PanelData> getPanelMap() { return panelMap; }
	public String getProductName() {return this.productName;}
    public boolean isIgnoringCurrent() { return ignoringCurrent; }
    public boolean isIgnoringOptional() { return ignoringOptional; }
    public void setBillOfMaterials(Document doc) {this.bom = doc;}
    public void setIgnoringCurrent(boolean flag) { this.ignoringCurrent = flag; }
    public void setIgnoringOptional(boolean flag) { this.ignoringOptional = flag; }
    public void setModulePath(String path) {this.modulePath = path;}
    public void setProductName(String name) {this.productName = name;}
}

