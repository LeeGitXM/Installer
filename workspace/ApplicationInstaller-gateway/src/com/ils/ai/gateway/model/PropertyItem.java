/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

import java.io.Serializable;

/**
 *  This class is designed for use in displaying properties in table form
 */
public class PropertyItem implements Serializable {
	private static final long serialVersionUID = 773235520456516156L;
	private String name = "";
	private String value = "";
	private String previous = "";  // Hold a prior version of the same property

	/**
	 * 
	 */
	public PropertyItem(String nam,String val) {
		this.name = nam;
		this.value = val;
	}
	public String getName() {return this.name;}
	public String getValue() {return this.value;}
	public String getPrevious() {return this.previous; }
	public void setPrevious(String prior) {this.previous=prior; }
}

