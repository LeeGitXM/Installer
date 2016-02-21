/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.nio.file.Path;

import org.w3c.dom.Document;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.utility.FileUtility;
import com.ils.ai.gateway.utility.JarUtility;
import com.ils.ai.gateway.utility.XMLUtility;
import com.ils.common.db.DBUtility;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 *  This can be used to fetch information from the install bundle
 *  as the installation progresses. 
 *   
 *  This class is a singleton for easy access throughout the wizard screens.
 */
public class InstallerDataHandler {
	private static final long serialVersionUID = -9021431638644580809L;
	private static InstallerDataHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;
	private DBUtility dbUtil;
	private FileUtility fileUtil;
	private JarUtility jarUtil = null;
	private XMLUtility xmlUtil = null;
	private Path modulePath  = null;
    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private InstallerDataHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
		
	}
	

	/**
	 * Static method to create and/or fetch the single instance.
	 */
	public static InstallerDataHandler getInstance() {
		if( instance==null) {
			synchronized(InstallerDataHandler.class) {
				instance = new InstallerDataHandler();
			}
		}
		return instance;
	}
	
	/**
	 * Search the installer module for the bill of materials.
	 * We do not retain the document, we simply return it.
	 * @return the bill of materials, an XML file.
	 */
	public Document getBillOfMaterials() {
		Document bom = null;
		Path path = getPathToModule();
		if( path!=null ) {
			path = jarUtil.internalModuleContaining(InstallerConstants.MODULE_MARKER);
		}
		return bom;
	}
	
	public Path getPathToModule() {
		Path path = null;
		return path;
	}
	
	/**
	 * This step is necessary before the instance is useful. Most other 
	 * properties are initialized lazily.
	 */
	public void setContext(GatewayContext ctx) { 
		this.context=ctx;
		this.dbUtil = new DBUtility(context);
		this.fileUtil = new FileUtility();
		this.jarUtil  = new JarUtility(context);
		this.xmlUtil  = new XMLUtility();
	}
}

