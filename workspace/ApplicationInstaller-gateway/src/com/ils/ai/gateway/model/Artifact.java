/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

import com.ils.ai.gateway.InstallerConstants;

/**
 *  This class is a bean that contains information about an artifact.
 */
public class Artifact implements Serializable  {
	private static final long serialVersionUID = 7650179030090459881L;
	private String location = "";
	private String name = "";
	private String subtype = "";
	private String type = "";     // Artifact type

	public String getLocation() {return location;}
	public void setLocation(String loc) { this.location = loc; }
	public String getName() {return name;}
	public void setName(String nam) { this.name = nam; }
	public String getSubtype() {return subtype;}
	public void setSubtype(String st) { this.subtype = st; }
	public String getType() {return type;}
	public void setType(String typ) { this.type = typ; }
}

