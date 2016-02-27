/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.nio.file.Path;

import org.w3c.dom.Document;

import com.inductiveautomation.ignition.common.project.Project;

/**
 *  This class is the keeper of all knowledge for the install process.
 *  It is a serializable "bean" that relies on the Singleton InstallerDataHandler
 *  to populate.
 */
public class InstallerData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	private Document bom = null;
	private String modulePath = null;
	private Project project = null;
	private boolean skipCurrent = false;

	public Document getBillOfMaterials() {return bom;}
	public String getModulePath() {return this.modulePath;}
    public Project getProject() {return project;}
    public boolean skipCurrent() { return skipCurrent; }
    public void setBillOfMaterials(Document doc) {this.bom = doc;}
    public void setSkipCurrent(boolean flag) { this.skipCurrent = flag; }
    public void setModulePath(String path) {this.modulePath = path;}
    public void setProject(Project project) {this.project = project;}
}

