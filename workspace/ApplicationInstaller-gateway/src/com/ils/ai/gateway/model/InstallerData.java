/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

import com.inductiveautomation.ignition.common.project.Project;

/**
 *  This class is the keepers of all knowledge for the install process.
 *  It is a serializable "bean" that relies on the Singleton InstallerDataHandler
 *  to populate.
 */
public class InstallerData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	private Project project = null;

    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	public InstallerData() {

	}
	
    public Project getProject() {return project;}
    public void setProject(Project project) {this.project = project;}
}

