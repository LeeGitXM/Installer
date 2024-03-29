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
	private List<String> features = new ArrayList<>();
	private List<String> missingFeatures = new ArrayList<>();
	private String subtype = "";
	private String title = "";
	private PanelType type = PanelType.SUMMARY;     // Artifact type
	private int version = InstallerConstants.UNSET;
	
	public void addFeature(String feature) { features.add(feature); }
 	public int getCurrentVersion() {return currentVersion;}
 	public List<String> getFeatures() {return features;}
	public int getItertion()   {return iteration;}
	public int getIterations() {return iterations;}
	// Missing features are explicitly NOT wanted in the current installation
	public List<String> getMissingFeatures() {return missingFeatures;}
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
	public void subtractFeature(String feature) { missingFeatures.add(feature); }
	
	/**
	 * Test features. If the features list is empty, return false.
	 * @param panelFeatures
	 * @return true if one or more features in the panel list
	 *         match one of the features assigned to the install.
	 */
	public boolean matchFeature(List<String> definedFeatures) {
		for(String panelFeature:features) {
			for(String installedFeature:definedFeatures) {
				if( panelFeature.equalsIgnoreCase(installedFeature)) return true;
			}
		}
		return false;
	}
	/**
	 * Test for absence of features. Return true if the feature that we describe as
	 * "must be missing" does not exist in the installed feature set. If the missing
	 * features list is empty, we return true.
	 * @param panelFeatures
	 * @return true if none of the features in the install list
	 *         match one of the features designated as must be missing.
	 */
	public boolean verifyMissingFeature(List<String> definedFeatures) {
		for(String panelNonFeature:missingFeatures) {
			for(String installedFeature:definedFeatures) {
				if( panelNonFeature.equalsIgnoreCase(installedFeature)) return false;
			}
		}
		return true;
	}
}

