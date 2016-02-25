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
	private Path modulePath = null;
	private Project project = null;


	public Document getBillOfMaterials() {return bom;}
	public Path getModulePath() {return this.modulePath;}
    public Project getProject() {return project;}
    public void setBillOfMaterials(Document doc) {this.bom = doc;}
    public void setModulePath(Path path) {this.modulePath = path;}
    public void setProject(Project project) {this.project = project;}
}

