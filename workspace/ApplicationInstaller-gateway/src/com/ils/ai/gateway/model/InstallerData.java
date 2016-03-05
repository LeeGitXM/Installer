/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.inductiveautomation.ignition.common.project.Project;

/**
 *  This class is the keeper of all knowledge for the install process. Note that 
 *  the artifact and project storage is for DropDownChoice components.
 *  It is a serializable "bean" that relies on the Singleton InstallerDataHandler
 *  to populate.
 */
public class InstallerData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	private Artifact artifact = null;
	private Document bom = null;
	private String modulePath = null;
	private String productName = "";
	private Project project = null;
	private boolean ignoringOptional  = false;
	private boolean ignoringCurrent   = false;   // Ignore artifacts that are up-to-date
	private final Map<Integer,PanelData> panelMap = new HashMap<>();

	public Artifact getArtifact() {return artifact;}
	public Document getBillOfMaterials() {return bom;}
	public String getModulePath() {return this.modulePath;}
	public Map<Integer,PanelData> getPanelMap() { return panelMap; }
	public String getProductName() {return this.productName;}
    public Project getProject() {return project;}
    public boolean isIgnoringCurrent() { return ignoringCurrent; }
    public boolean isIgnoringOptional() { return ignoringOptional; }
    public void setArtifact(Artifact art) {this.artifact = art;}
    public void setBillOfMaterials(Document doc) {this.bom = doc;}
    public void setIgnoringCurrent(boolean flag) { this.ignoringCurrent = flag; }
    public void setIgnoringOptional(boolean flag) { this.ignoringOptional = flag; }
    public void setModulePath(String path) {this.modulePath = path;}
    public void setProject(Project project) {this.project = project;}
    public void setProductName(String name) {this.productName = name;}
}

