/**
 *   (c) 2016-2017  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

/**
 *  This class is designed for use in displaying properties in table form
 */
public class PropertyItem implements Serializable {
	private static final long serialVersionUID = 773235520456516156L;
	private String name = "";
	private String type = "";
	private String value = "";
	private String previous = "";  // Hold a prior version of the same property
	private String script = "";

	public PropertyItem(String nam,String val) {
		this.name = nam;
		this.value = val;
	}
	
	public PropertyItem(String nam,String typ,String val) {
		this.name = nam;
		this.type = typ;
		this.value = val;
	}
	public String getName() {return this.name;}
	public String getScript() {return script;}
	public String getValue() {return this.value;}
	public String getType() {return this.type; }
	public void setType(String typ) {this.type=typ; }
	public String getPrevious() {return this.previous; }
	public void setPrevious(String prior) {this.previous=prior; }
	public void setScript(String s) { this.script = s; }
}

