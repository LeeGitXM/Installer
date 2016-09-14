/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private int iteration  = 0;    // Zero-based
	private int iterations = 1;
	private  List<String> siteNames = new ArrayList<>();
	private String subtype = "";
	private String title = "";
	private PanelType type = PanelType.SUMMARY;     // Artifact type
	private int version = InstallerConstants.UNSET;
	
	public int getCurrentVersion() {return currentVersion;}
	public int getItertion()   {return iteration;}
	public int getIterations() {return iterations;}
	public List<String> getSiteNames() {return siteNames;}
	public String getSubtype() {return subtype;}
	public String getTitle() {return title;}
	public PanelType getType() {return type;}
	public int getVersion() {return version;}
	public boolean isEssential() {return essential;}
	
	public void setCurrentVersion(int vers) { this.currentVersion = vers; }
	public void setEssential(boolean flag) { this.essential = flag; }
	public void setIteration(int iter) { this.iteration = iter; }
	public void setIterations(int count) { this.iterations = count; }
	public void setSiteNames(List<String> names) { this.siteNames = names; }
	public void setSubtype(String st) { this.subtype = st; }
	public void setTitle(String text) { this.title = text; }
	public void setType(PanelType typ) { this.type = typ; }
	public void setVersion(int vers) {this.version = vers; }
}

