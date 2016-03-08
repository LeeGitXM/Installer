/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.ils.ai.gateway.InstallerConstants;
import com.inductiveautomation.ignition.gateway.localdb.persistence.IntField;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.StringField;

import simpleorm.dataset.SFieldFlags;

/**
 * For certain artifacts we keep track of the "installed" or down-loaded versions. 
 * Keys are product name, artifact name
 * For documentation relating to the SimpleORM data model:
 * @See: http://simpleorm.org/sorm/whitepaper.html
 */
public class ArtifactVersionsRecord extends PersistentRecord {
	private static final long serialVersionUID = 7143351320983681281L;

	public static final String TABLE_NAME = "ILS_Artifact_Version";
	
	public static final RecordMeta<ArtifactVersionsRecord> META = new RecordMeta<>(ArtifactVersionsRecord.class, TABLE_NAME);

	public static final StringField ProductName = new StringField(META, "ProductName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField PropertyName = new StringField(META, "ArtifactName",SFieldFlags.SPRIMARY_KEY );
	public static final IntField Version = new IntField(META, "Version",SFieldFlags.SMANDATORY).setDefault(InstallerConstants.UNSET);
	
	public RecordMeta<?> getMeta() {return META; }
	
	public String getProductName() { return getString(ProductName); }
	public String getPropertyName() { return getString(PropertyName); }
	public int getVersion() { return getInt(Version); }
	public void setProductName(String str) { setString(ProductName,str); }
	public void setPropertyName(String str) { setString(PropertyName,str); }
	public void setVersion(int vers) { setInt(Version,vers); }
}
