/**
 *   (c) 2016-2017  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ils.ai.gateway.InstallerConstants;

/**
 *  This class is a bean that contains information about an artifact.
 *  Generally release and version are not used at the same time.
 */
public class Artifact implements Serializable  {
	private static final long serialVersionUID = 7650179030090459881L;
	private String comment = "";          // Textual commentary relating to this artifact
	private String destination = "";      // Where we put it in the user's system
	private String location = "";         // Where we find it in the release bundle
	private String name = "";
	private String release = "";
	private String script = "";
	private String subtype = "";
	private String type = "";         // Artifact type
	private int version = InstallerConstants.UNSET;
	private List<PropertyItem> properties = new ArrayList<>(); // Site-wide properties

	public String getComment() {return comment;}
	public String getDestination() {return destination;}
	public String getLocation() {return location;}
	public String getName() {return name;}
	public List<PropertyItem> getProperties() {return this.properties;}
	public String getRelease() {return release;}
	public String getScript() {return script;}
	public String getSubtype() {return subtype;}
	public String getType() {return type;}
	public int getVersion() { return version; }
	
	public void setComment(String text) { this.comment = text; }
	public void setDestination(String dest) { this.destination = dest; }
	public void setLocation(String loc) { this.location = loc; }
	public void setName(String nam) { this.name = nam; }
	public void setProperties(List<PropertyItem> props) {this.properties = props;}
	public void setRelease(String rel) { this.release = rel; }
	public void setScript(String s) { this.script = s; }
	public void setSubtype(String st) { this.subtype = st; }
	public void setType(String typ) { this.type = typ; }
	public void setVersion(int vers) { this.version=vers; }
}

