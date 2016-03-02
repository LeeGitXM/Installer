/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

import org.w3c.dom.Document;

import com.inductiveautomation.ignition.common.project.Project;

/**
 *  This class maintains parameters for a single panel.
 *  It is maintained by the singleton InstallerDataHandler in a dictionary
 *  keyed by the panel index.
 *  to populate.
 */
public class PanelData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	protected FINAL int currentVersion;
	protected final String subtype;
	protected final WizardStepType type;     // Artifact type
	protected final int version;
	
	public int getCurrentVersion() {return currentVersion;}
	public String getSubtype() {return subtype;}
	public WizardStepType getType() {return type;}
	public int getVersion() {return version;}


}

