/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

/**
 *  This class holds the details of a project for a site. A particular
 *  site may have many projects defined. but the combination of site name
 *  and project name must be unique. 
 *  
 *  An instance of this class is equivalent to a line in the site data
 *  .csv file.
 */
public class SiteEntry implements Serializable  {
	private static final long serialVersionUID = -5650159030090459881L;
	private String artifactName = "";
	private String datasource = "";
	private String projectName = "";
	private String provider = "";
	private String siteName = "";
	private String testDatasource = "";
	private String testProvider = "";

	public String getArtifactName() {return this.artifactName;}
	public String getDatasource() {return this.datasource;}
	public String getProjectName() {return this.projectName;}
	public String getProvider() {return this.provider;}
	public String getSiteName() {return this.siteName;}
	public String getTestDatasource() {return this.testDatasource;}
	public String getTestProvider() {return this.testProvider;}
    public void setArtifactName(String name) {this.artifactName = name;}
    public void setDatasource(String name) {this.datasource = name;}
    public void setProjectName(String name) {this.projectName = name;}
    public void setProvider(String name) {this.provider = name;}
    public void setSiteName(String name) {this.siteName = name;}
    public void setTestDatasource(String name) {this.testDatasource = name;}
    public void setTestProvider(String name) {this.testProvider = name;}
}

