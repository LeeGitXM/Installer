/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import com.ils.ai.gateway.InstallerConstants;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 *  This can be used to fetch information from the internal Ignition database. 
 *  We use a Persistent Object interface.
 *   
 *  This class is a singleton for easy access throughout the wizard screens.
 *  WARNING: This class is not serializable and cannot be assigned as an 
 *           instance variable for any pagoe or nested class within a page.
 */
public class PersistenceHandler {
	private final static String CLSS = "PersistenceHandler";
	private static PersistenceHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;

    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private PersistenceHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
	}
	

	/**
	 * Static method to create and/or fetch the single instance.
	 */
	public static PersistenceHandler getInstance() {
		if( instance==null) {
			synchronized(PersistenceHandler.class) {
				instance = new PersistenceHandler();
			}
		}
		return instance;
	}
	
	/**
	 * This step is necessary before the instance is useful. Most other 
	 * properties are initialized lazily.
	 */
	public void setContext(GatewayContext ctx) { 
		this.context=ctx;
	}
	
	/**
	 * @return the value of a product property. Keys are product name and property name.
	 * On a failure to find the property, an empty string is returned.
	 */
	public String getProductProperty(String productName,String propertyName) {
		String value = "";
		try {
			ProductPropertiesRecord record = context.getPersistenceInterface().find(ProductPropertiesRecord.META, productName,propertyName);
			if( record!=null) value =  record.getValue();
		}
		catch(Exception ex) {
			log.warnf("%s.getProductProperty: Exception retrieving %s (%s),",CLSS,propertyName,ex.getMessage());
		}
		return value;
	}
	
	/**
	 * Set the value of a product property. Keys are product name and property name.
	 */
	public void setProductProperty(String productName,String propertyName, String value) {
		try {
			ProductPropertiesRecord record = context.getPersistenceInterface().find(ProductPropertiesRecord.META, productName,propertyName);
			if( record==null) record = context.getPersistenceInterface().createNew(ProductPropertiesRecord.META);
			if( record!=null) {
				record.setProductName(productName);
				record.setPropertyName(propertyName);
				record.setValue(value);
				context.getPersistenceInterface().save(record);
			}
			else {
				log.warnf("%s.setProductProperty: %s.%s=%s - failed to create persistence record (%s)",CLSS,productName,propertyName,value,
						ProductPropertiesRecord.META.quoteName);
			} 
		}
		catch(Exception ex) {
			log.warnf("%s.setProductProperty: Exception setting %s:%s=%s (%s),",CLSS,productName,propertyName,value,ex.getMessage());
		}
	}
	/**
	 * @return the current installed version of an installation step for a specified product.
	 * Keys are product name, artifact type and artifact sub-type.
	 * On a failure to find the version, return -1;.
	 */
	public int getStepVersion(String productName,PanelType type,String subtype) {
		int version = InstallerConstants.UNSET;
		try {
			ProductVersionRecord record = context.getPersistenceInterface().find(ProductVersionRecord.META, productName,type.name(),subtype);
			if( record!=null) version =  record.getVersion();
		}
		catch(Exception ex) {
			log.warnf("%s.getStepVersion: Exception retrieving %s:%s:%s (%s),",CLSS,productName,type.name(),subtype,ex.getMessage());
		}
		return version;
	}
	
	/**
	 * Set the value of a product version. Keys are product name,artifact type and artifact sub-type..
	 */
	public void setStepVersion(String productName,PanelType type,String subtype,int version)  {
		try {
			ProductVersionRecord record = context.getPersistenceInterface().find(ProductVersionRecord.META, productName,type.name(),subtype);
			if( record==null) record = context.getPersistenceInterface().createNew(ProductVersionRecord.META);
			if( record!=null) {
				record.setProductName(productName);
				record.setType(type.name());
				record.setSubType(subtype);
				record.setVersion(version);
				context.getPersistenceInterface().save(record);
			}
			else {
				log.warnf("%s.setStepVersion: %s:%s:%s=%d - failed to create persistence record (%s)",CLSS,productName,type,subtype,version,
						ProductVersionRecord.META.quoteName);
			} 
		}
		catch(Exception ex) {
			log.warnf("%s.setStepVersion: Exception setting %s:%s:%s=%d (%s),",CLSS,productName,type.name(),subtype,version,ex.getMessage());
		}
	}
}

