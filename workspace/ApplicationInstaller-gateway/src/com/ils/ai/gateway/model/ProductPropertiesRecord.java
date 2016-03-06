/**
 *   (c) 2015  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.StringField;

import simpleorm.dataset.SFieldFlags;

/**
 * Save and access properties of installed products. Keys are product name, property name
 * For documentation relating to the SimpleORM data model:
 * @See: http://simpleorm.org/sorm/whitepaper.html
 */
public class ProductPropertiesRecord extends PersistentRecord {
	private static final long serialVersionUID = 8143351320983681281L;

	public static final String TABLE_NAME = "ILS_Product_Properties";
	
	public static final RecordMeta<ProductPropertiesRecord> META = new RecordMeta<>(ProductPropertiesRecord.class, TABLE_NAME);

	public static final StringField ProductName = new StringField(META, "ProductName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField PropertyName = new StringField(META, "PropertyName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField Value = new StringField(META, "Value",SFieldFlags.SMANDATORY).setDefault("");
	
	public RecordMeta<?> getMeta() {return META; }
	
	public String getProductName() { return getString(ProductName); }
	public String getPropertyName() { return getString(PropertyName); }
	public String getValue() { return getString(Value); }
	public void setProductName(String str) { setString(ProductName,str); }
	public void setPropertyName(String str) { setString(PropertyName,str); }
	public void setValue(String str) { setString(Value,str); }
}
