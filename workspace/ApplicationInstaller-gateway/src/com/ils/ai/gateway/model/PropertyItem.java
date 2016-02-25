/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

/**
 *  This class is designed for use in displaying properties in table form
 */
public class PropertyItem  {

	private final String name;
	private final String value;

	/**
	 * .
	 */
	public PropertyItem(String nam,String val) {
		this.name = nam;
		this.value = val;
	}
	public String getName() {return this.name;}
	public String getValue() {return this.value;}
}

