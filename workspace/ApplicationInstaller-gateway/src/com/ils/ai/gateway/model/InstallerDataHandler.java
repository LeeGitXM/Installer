/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.io.OutputStream;
import java.nio.file.Path;
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
 *  This can be used to fetch information from the install bundle
 *  as the installation progresses. 
 *   
 *  This class is a singleton for easy access throughout the wizard screens.
 */
public class InstallerDataHandler {
	private final static String CLSS = "InstallerDataHandler";
	private static final long serialVersionUID = -9021431638644580809L;
	private static InstallerDataHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;
	private DBUtility dbUtil;
	private FileUtility fileUtil;
	private JarUtility jarUtil = null;
	private XMLUtility xmlUtil = null;
	private final WizardStepFactory stepFactory;
    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private InstallerDataHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
		this.stepFactory = new WizardStepFactory();
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
	 * Perform a gateway backup directed toward the supplied path.
	 */
	public String backup(String path) {
		String result = null;
		if( context!=null ) {
			try {
				OutputStream os = null;
				BackupServlet.generateBackup((SRContext)context, os, BackupType.DATA_ONLY);
			}
			catch(Exception ex) {
				result = String.format("InstallerDataHandler.backup: Backup failed with exception (%s)",ex.getMessage());
			}
		}
		return result;
	}
	/**
	 * @param model
	 * @return a list of the names of the artifacts associated with this step
	 */
	public List<String> getArtifactNames(int panelIndex,InstallerData model) {
		List<String> names = new ArrayList<>();
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int count = artifactNodes.getLength();
			int index = 0;
			while(index<count) {
				Node artifactNode = artifactNodes.item(index);
				String name = xmlUtil.attributeValue(artifactNode, "name");
				names.add(name);
				index++;
			}
		}
		return names;
	}
	/**
	 * Search the installer module for the bill of materials.
	 * We do not retain the document, we simply return it.
	 * @return the bill of materials, an XML file.
	 */
	public Document getBillOfMaterials(InstallerData model) {
		Document bom = model.getBillOfMaterials();
		if( bom==null) {
			Path path = getPathToModule(model);
			if( path!=null ) {
				String contents = jarUtil.readFileFromJar(InstallerConstants.BOM_LOCATION,path);
				if( contents!=null ) {
					contents = contents.trim().replaceFirst("^([\\W]+)<","<");  // Get rid of any junk
					log.infof("%s.getBillOfMaterials: Contents = \n%s\n",CLSS,contents);
					bom = xmlUtil.documentFromBytes(contents.getBytes());
				}
				else {
					log.warnf("%s.getBillOfMaterials: Failed to read XML from module.",CLSS);
				}
			}
		}
		return bom;
	}
	
	public Element getPanelElement(int panelIndex,InstallerData model) {
		Node panelElement = null;
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) {
			NodeList elements = bom.getElementsByTagName("panel");
			int count = elements.getLength();
			if( count>0 && panelIndex<count ) {
				panelElement = elements.item(panelIndex);
			}
			else {
				log.warnf("%s.getPanelElement: Insufficient panels in BOM (%d, need %d).",CLSS,count,panelIndex);
			}
		}
		else {
			log.warnf("%s.getPanelElement: Failed to read BOM from module.",CLSS);
		}
		return (Element)panelElement;
	}
	
	public Path getPathToModule(InstallerData model) {
		Path path = model.getModulePath();
		if( path==null ) {
			path = jarUtil.internalModuleContaining(InstallerConstants.MODULE_MARKER);
			if( path==null ) {
				log.warnf("%s.getPathToModule: Could not find path to module",CLSS);
			}
		}
		return path;
	}
	
	// Format the properties for display
	public List<PropertyItem> getProperties(InstallerData model) {
		List<PropertyItem> properties = new ArrayList<>();
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) {
			NodeList propertyNodes = bom.getElementsByTagName("property");
			int count = propertyNodes.getLength();
			int index = 0;
			while(index<count) {
				Node propertyNode = propertyNodes.item(index);
				String name = xmlUtil.attributeValue(propertyNode, "name");
				String value = propertyNode.getTextContent();
				properties.add(new PropertyItem(name,value));
				index++;
			}
		}
		return properties;
	}
	
	public int getStepCount(InstallerData model) {
		int count = 0;
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) { 
			NodeList elements = bom.getElementsByTagName("panel");
			count = elements.getLength();
		}
		return count;
	}
	/**
	 * Read the title from the bill of materials.
	 * @param model
	 * @return
	 */
	public String getTitle(InstallerData model) {
		String title = "No title in bill of materials";
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) {
			NodeList elements = bom.getElementsByTagName("title");
			int count = elements.getLength();
			if( count>0 ) {
				Node element = elements.item(0);
				title = element.getTextContent();
			}
		}
		else {
			title = "Missing bill of materials";
		}
		return title;
	}
	public String getStepPreamble(int panelIndex,InstallerData model) {
		String text = "";    // If all else fails
		Element panelElement = getPanelElement(panelIndex,model);
		if( panelElement!=null ) {
			NodeList elements = panelElement.getElementsByTagName("preamble");
			int count = elements.getLength();
			if( count>0  ) {
				Node element = elements.item(0);
				text = element.getTextContent();
			}
			else {
				log.warnf("%s.getStepTitle: No preamble element in panel %d.",CLSS,count,panelIndex);
			}
		}
		return text;
	}
	public String getStepTitle(int panelIndex,InstallerData model) {
		String title = "-- no title --";    // If all else fails
		Element panelElement = getPanelElement(panelIndex,model);
		if( panelElement!=null ) {
			NodeList elements = panelElement.getElementsByTagName("title");
			int count = elements.getLength();
			if( count>0  ) {
				Node element = elements.item(0);
				title = element.getTextContent();
			}
			else {
				log.warnf("%s.getStepTitle: No title element in panel %d.",CLSS,count,panelIndex);
			}
		}
		return title;
	}
	
	public WizardStepType getStepType(int panelIndex,InstallerData model) {
		WizardStepType type = WizardStepType.WELCOME;    // If all else fails
		Node panelElement = getPanelElement(panelIndex,model);
		if( panelElement!=null ) {
			String name = xmlUtil.attributeValue(panelElement, "type");
			try {
				type = WizardStepType.valueOf(name.toUpperCase());
			}
			catch(IllegalArgumentException iae) {
				log.warnf("%s.getStepType: Could not convert %s into a WizardStepType",CLSS,name);
			}
		}
		return type;
	}
	
	public InstallWizardStep getWizardStep(int index,InstallWizardStep prior,Model<InstallerData> dataModel) {
		WizardStepType stepType = getStepType(index,dataModel.getObject());
		String title = getStepTitle(index,dataModel.getObject());
		InstallWizardStep step = stepFactory.createStep(index,prior,stepType,title,dataModel);
		return step;
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

