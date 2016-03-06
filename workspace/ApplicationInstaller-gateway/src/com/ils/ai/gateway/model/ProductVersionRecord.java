/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.inductiveautomation.ignition.gateway.localdb.persistence.IdentityField;
import com.inductiveautomation.ignition.gateway.localdb.persistence.IntField;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.StringField;

import simpleorm.dataset.SFieldFlags;

/**
 * Save and access current version number for product artifacts.
 * Keys are product name, artifact type and artifact subtype.
 */
public class ProductVersionRecord extends PersistentRecord {
	private static final long serialVersionUID = 8143351320983681282L;

	public static final String TABLE_NAME = "ILS_Installed_Versions";
	
	public static final RecordMeta<ProductVersionRecord> META = new RecordMeta<>(ProductVersionRecord.class, TABLE_NAME);
	static SFieldFlags[] primary = {SFieldFlags.SPRIMARY_KEY,SFieldFlags.SMANDATORY};
	static SFieldFlags[] secondary = {SFieldFlags.SMANDATORY};
	
	public static final IdentityField Id = new IdentityField(META);
	public static final StringField ProductName = new StringField(META, "ProductName",SFieldFlags.SMANDATORY );
	public static final StringField Type = new StringField(META, "Type",SFieldFlags.SMANDATORY );
	public static final StringField SubType = new StringField(META, "SubType",SFieldFlags.SMANDATORY );
	public static final IntField Version = new IntField(META, "Version",secondary).setDefault(-2);
	
	public RecordMeta<?> getMeta() {return META; }
	
	public String getProductName() { return getString(ProductName); }
	public String getType() { return getString(Type); }
	public String getSubType() { return getString(SubType); }
	public int getVersion() { return getInt(Version); }
	public void setProductName(String str) { setString(ProductName,str); }
	public void setType(String str) { setString(Type,str); }
	public void setSubType(String str) { setString(SubType,str); }
	public void setVersion(int vers) { setInt(Version,vers); }
}
