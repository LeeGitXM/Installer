/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ils.ai.gateway.InstallerConstants;


/**
 *  This class holds the details of a project for a site. A particular
 *  site may have many projects defined. but the combination of site name
 *  and project name must be unique. 
 *  
 */
public class SiteEntry implements Serializable  {
	private static final long serialVersionUID = -5650159030090459881L;

	private String siteName = "";
	private List<Artifact> artifacts = new ArrayList<>();      // Site-specific artifacts
	private List<PropertyItem> properties = new ArrayList<>(); // Site-wide properties

	public List<Artifact> getArtifacts() {return this.artifacts;}
	public List<PropertyItem> getProperties() {return this.properties;}
	public String getSiteName() {return this.siteName;}
	public void setArtifacts(List<Artifact> facts) {this.artifacts = facts;}
    public void setProperties(List<PropertyItem> props) {this.properties = props;}
    public void setSiteName(String name) {this.siteName = name;}
    
    // ================================== Helper Methods ==========================
    // We allow multiple properties defining production databases. Here we iterate through them.
    public List<String> getIsolationDatasources() {
    	List<String> result = new ArrayList<>();
    	for(PropertyItem pi:properties) {
    		if( pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE) &&
    			pi.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION) ) result.add(pi.getValue());	
    	}
    	return result;
    }
    public List<String> getIsolationProviders() {
    	List<String> result = new ArrayList<>();
    	for(PropertyItem pi:properties) {
    		if( pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER) &&
    			pi.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION) ) result.add(pi.getValue());	
    	}
    	return result;
    }
    public List<String> getProductionDatasources() {
    	List<String> result = new ArrayList<>();
    	for(PropertyItem pi:properties) {
    		if( pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE) &&
    			pi.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION) ) result.add(pi.getValue());	
    	}
    	return result;
    }
    public List<String> getProductionProviders() {
    	List<String> result = new ArrayList<>();
    	for(PropertyItem pi:properties) {
    		if( pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER) &&
    			pi.getType().equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION) ) result.add(pi.getValue());	
    	}
    	return result;
    }
}

