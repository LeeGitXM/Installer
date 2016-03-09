/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

import com.ils.ai.gateway.InstallerConstants;

/**
 *  This class maintains parameters for a single panel.
 *  It is maintained by the singleton InstallerDataHandler in a dictionary
 *  keyed by the panel index.
 *  to populate.
 */
public class PanelData implements Serializable  {
	private static final long serialVersionUID = 7650179030090459881L;
	private int currentVersion = InstallerConstants.UNSET;
	private boolean essential = false;
	private String subtype = "";
	private String title = "";
	private PanelType type = PanelType.CONCLUSION;     // Artifact type
	private int version = InstallerConstants.UNSET;
	
	public int getCurrentVersion() {return currentVersion;}
	public void setCurrentVersion(int vers) { this.currentVersion = vers; }
	public boolean isEssential() {return essential;}
	public void setEssential(boolean flag) { this.essential = flag; }	
	public String getSubtype() {return subtype;}
	public void setSubtype(String st) { this.subtype = st; }
	public String getTitle() {return title;}
	public void setTitle(String text) { this.title = text; }
	public PanelType getType() {return type;}
	public void setType(PanelType typ) { this.type = typ; }
	public int getVersion() {return version;}
	public void setVersion(int vers) {this.version = vers; }
}

