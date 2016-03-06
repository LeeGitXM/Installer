/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.inductiveautomation.ignition.gateway.localdb.persistence.IntField;
import com.inductiveautomation.ignition.gateway.localdb.persistence.LongField;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.StringField;

import simpleorm.dataset.SFieldFlags;

/**
 * Save and access current version number for product artifacts.
 * Keys are product name, artifact type and artifact subtype.
 * There is also an auto-generated primary key
 */
public class InstalledVersionsRecord extends PersistentRecord {
	private static final long serialVersionUID = 8143351320983681282L;

	public static final String TABLE_NAME = "ILS_Installed_Versions";
	
	public static final RecordMeta<InstalledVersionsRecord> META = new RecordMeta<>(InstalledVersionsRecord.class, TABLE_NAME);
	
	public static final StringField ProductName = new StringField(META, "ProductName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField Type = new StringField(META, "Type",SFieldFlags.SPRIMARY_KEY );
	public static final StringField SubType = new StringField(META, "SubType",SFieldFlags.SPRIMARY_KEY );
	public static final IntField Version = new IntField(META, "Version",SFieldFlags.SMANDATORY).setDefault(-2);
	
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
