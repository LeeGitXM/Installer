/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

/**
 *  This class is the keeper of all knowledge for the install process.
 *  It is a serializable "bean" that relies on the Singleton InstallerDataHandler
 *  to populate.
 */
public class InstallerData implements Serializable  {
	private static final long serialVersionUID = -7650179030090459881L;
	private String administrativeProfile = "1";
	private String administrativeUser = null;
	private Document bom = null;
	private int chunkedTotal = 0;    // From the latest chunked operation
	private Integer[] chunkCounts = new Integer[0];
	private String modulePath = null;
	private String productName = "";
	private String siteName = "";
	private boolean ignoringOptional  = false;
	private boolean ignoringCurrent   = false;   // Ignore artifacts that are up-to-date
	private final Map<Integer,PanelData> panelMap = new HashMap<>();
	private final Map<String,Boolean> featureMap = new HashMap<>();
	private List<SiteEntry> siteEntries = new ArrayList<>();

	public String getAdministrativeProfile() {return administrativeProfile;}
	public String getAdministrativeUser() {return administrativeUser;}
	public Document getBillOfMaterials() {return bom;}
	public int getChunkedTotal() { return chunkedTotal; }
	public Integer[] getChunkCounts() { return chunkCounts; }
	public String getModulePath() {return this.modulePath;}
	public Map<Integer,PanelData> getPanelMap() { return panelMap; }
	public String getProductName() {return this.productName;}
	public  List<SiteEntry> getSiteEntries() {return this.siteEntries;}
	public String getSiteName() {return this.siteName;}
	// Default is false, should the feature not be found. Compare is case-insensitive.
	public boolean hasFeature(String name) {
		Boolean has = featureMap.get(name.toLowerCase());
		if( has==null ) return false;
		return has.booleanValue();
	}
    public boolean isIgnoringCurrent() { return ignoringCurrent; }
    public boolean isIgnoringOptional() { return ignoringOptional; }
	public void setAdministrativeProfile(String profile) {administrativeProfile=profile;}
	public void setAdministrativeUser(String user) {administrativeUser=user;}
    public void setBillOfMaterials(Document doc) {this.bom = doc;}
    public void setChunkTotal(int total) { this.chunkedTotal = total; }
    public void setChunkCounts(Integer[] counts) { this.chunkCounts = counts; }
    public void setFeature(String name,boolean has) {
    	featureMap.put(name.toLowerCase(), new Boolean(has));
    }
    public void setIgnoringCurrent(boolean flag) { this.ignoringCurrent = flag; }
    public void setIgnoringOptional(boolean flag) { this.ignoringOptional = flag; }
    public void setModulePath(String path) {this.modulePath = path;}
    public void setProductName(String name) {this.productName = name;}
    public void setSiteEntries(List<SiteEntry> entries) {this.siteEntries = entries;}
    public void setSiteName(String name) {this.siteName = name;}
    
    // ================================ Helper Methods involving Sites ===============================
    /**
     * These are "global" to the site. 
     *  @return a list of production datasources for the current site 
     */
	public List<String> getCurrentSiteProductionDatasources() {
		List<String> datasources = new ArrayList<>();
		if( !siteName.isEmpty() ) {
			for(SiteEntry se:siteEntries) {
				if( se.getSiteName().equals(siteName) ) {
					return se.getProductionDatasources();
				}
			}
		}
		return datasources;
	}
	// @return a list of active features
	public List<String> getFeatures() {
		List<String> feats = new ArrayList<>();
		for( String key:featureMap.keySet()) {
			if( featureMap.get(key) ) {
				feats.add(key);
			}
		}
		return feats;
	}
	// The SiteEntries *should* be a unique list
	public List<String> getSiteNames() {
		List<String> siteNames = new ArrayList<>();
		for(SiteEntry entry:siteEntries) {
			String site = entry.getSiteName();
			if( !siteNames.contains(site)) siteNames.add(site);
		}
		return siteNames;
	}
	public List<String> getCurrentSiteTestDatasources() {
		List<String> datasources = new ArrayList<>();
		if( !siteName.isEmpty() ) {
			for(SiteEntry se:siteEntries) {
				if( se.getSiteName().equals(siteName) ) {
					return se.getIsolationDatasources();
				}
			}
		}
		return datasources;
	}
}

