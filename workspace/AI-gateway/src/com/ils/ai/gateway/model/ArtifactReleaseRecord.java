/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistentRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.localdb.persistence.StringField;

import simpleorm.dataset.SFieldFlags;

/**
 * For certain artifacts we keep track of the "installed" or down-loaded released versions.
 * This applies to artifacts that are, themselves, separately versioned.
 * Keys are product name, step type, artifact name
 * For documentation relating to the SimpleORM data model:
 * @See: http://simpleorm.org/sorm/whitepaper.html
 */
public class ArtifactReleaseRecord extends PersistentRecord {
	private static final long serialVersionUID = 7143351320983681281L;

	public static final String TABLE_NAME = "ILS_Artifact_Releases";
	
	public static final RecordMeta<ArtifactReleaseRecord> META = new RecordMeta<>(ArtifactReleaseRecord.class, TABLE_NAME);

	public static final StringField ProductName = new StringField(META, "ProductName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField Type = new StringField(META, "Type",SFieldFlags.SPRIMARY_KEY );
	public static final StringField SubType = new StringField(META, "SubType",SFieldFlags.SPRIMARY_KEY );
	public static final StringField ArtifactName = new StringField(META, "ArtifactName",SFieldFlags.SPRIMARY_KEY );
	public static final StringField Release = new StringField(META, "Release",SFieldFlags.SMANDATORY).setDefault("");
	
	public RecordMeta<?> getMeta() {return META; }
	
	public String getArtifactName() { return getString(ArtifactName); }
	public String getProductName() { return getString(ProductName); }
	public String getPropertyName() { return getString(ArtifactName); }
	public String getType() { return getString(Type); }
	public String getSubType() { return getString(SubType); }
	public String getRelease() { return getString(Release); }
	public void setArtifactName(String name) { setString(ArtifactName,name); }
	public void setProductName(String str) { setString(ProductName,str); }
	public void setPropertyName(String str) { setString(ArtifactName,str); }
	public void setRelease(String rel) { setString(Release,rel); }
	public void setType(String str) { setString(Type,str); }
	public void setSubType(String str) { setString(SubType,str); }
}
