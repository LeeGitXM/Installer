/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.panel.InstallWizardStep;
import com.ils.ai.gateway.utility.FileUtility;
import com.ils.ai.gateway.utility.JarUtility;
import com.ils.ai.gateway.utility.XMLUtility;
import com.ils.common.db.DBUtility;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.SRContext;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.servlets.BackupServlet;
import com.inductiveautomation.ignition.gateway.util.BackupRestoreDelegate.BackupType;

/**
 *  This can be used to fetch information from the internal Ignition database. 
 *  We use a Persistent Object interface.
 *   
 *  This class is a singleton for easy access throughout the wizard screens.
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
}

