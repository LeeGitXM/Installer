/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.wicket.model.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.panel.BasicInstallerStep;
import com.ils.ai.gateway.utility.FileUtility;
import com.ils.ai.gateway.utility.JarUtility;
import com.ils.ai.gateway.utility.ScanClassUtility;
import com.ils.ai.gateway.utility.TagUtility;
import com.ils.ai.gateway.utility.XMLUtility;
import com.ils.common.db.DBUtility;
import com.inductiveautomation.ignition.common.sqltags.model.ScanClass;
import com.inductiveautomation.ignition.common.sqltags.model.TagProviderMeta;
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
 *  WARNING: This class is not serializable and cannot be assigned as an 
 *           instance variable for any pagoe or nested class within a page.
 */
public class InstallerDataHandler {
	private final static String CLSS = "InstallerDataHandler";
	private static final long serialVersionUID = -9021431638644580809L;
	private static final String PREFERENCES_NAME = "InstallerPreferences";
	private static InstallerDataHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;
	private final Preferences prefs;
	private DBUtility dbUtil;
	private FileUtility fileUtil;
	private JarUtility jarUtil = null;
	private ScanClassUtility scanUtil = null;
	private TagUtility tagUtil = null;
	private XMLUtility xmlUtil = null;
	private final InstallerStepFactory stepFactory;
	
    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private InstallerDataHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
		this.prefs = Preferences.userRoot().node(PREFERENCES_NAME);
		this.stepFactory = new InstallerStepFactory();
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
	public String backup(OutputStream outstream,InstallerData model) {
		String result = null;
		if( context!=null ) {
			try {
				log.infof("%s.backup: starting backup ...",CLSS);
				BackupServlet.generateBackup(SRContext.get(), outstream, BackupType.DATA_ONLY);
				log.infof("%s.backup: completed backup.",CLSS);
			}
			catch(Exception ex) {
				result = String.format("InstallerDataHandler.backup: Backup failed with exception (%s)",ex.getMessage());
				log.warn(result);
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
	 * @param model
	 * @return a list of the names of the artifacts associated with this step
	 */
	public byte[] getArtifactAsBytes(int panelIndex,String artifactName,InstallerData model) {
		byte[] bytes = null;
		log.infof("%s.getArtifactAsBytes: panel %d, getting %s",CLSS,panelIndex,artifactName);
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int acount = artifactNodes.getLength();
			int index = 0;
			while(index<acount) {
				Node artifactNode = artifactNodes.item(index);
				if( artifactName.equalsIgnoreCase(xmlUtil.attributeValue(artifactNode, "name")) ) {
					NodeList locationNodes = panel.getElementsByTagName("location");
					int lcount = locationNodes.getLength();
					if( lcount>0) {
						Node locationNode = locationNodes.item(index);
						String internalPath = locationNode.getTextContent();
						Path path = getPathToModule(model);
						bytes = jarUtil.readFileAsBytesFromJar(internalPath,path);
						log.infof("%s.getArtifactAsBytes: panel %d:%s %s returned %d bytes",CLSS,panelIndex,artifactName,path.toString(),bytes.length);
					}
					else {
						log.warnf("%s.getArtifactAsBytes: no location element for panel %d:%s",CLSS,panelIndex,artifactName);
					}
					break;
				}
				index++;
			}
		}
		return bytes;
	}
	/**
	 * @param model
	 * @return a list of the names of the artifacts associated with this step
	 */
	public File getArtifactAsTemporaryFile(int panelIndex,String artifactName,InstallerData model) {
		byte[] bytes = null;
		log.infof("%s.getArtifactAsFile: panel %d, getting %s",CLSS,panelIndex,artifactName);
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int acount = artifactNodes.getLength();
			int index = 0;
			while(index<acount) {
				Node artifactNode = artifactNodes.item(index);
				if( artifactName.equalsIgnoreCase(xmlUtil.attributeValue(artifactNode, "name")) ) {
					NodeList locationNodes = panel.getElementsByTagName("location");
					int lcount = locationNodes.getLength();
					if( lcount>0) {
						Node locationNode = locationNodes.item(index);
						String internalPath = locationNode.getTextContent();
						Path path = getPathToModule(model);
						bytes = jarUtil.readFileAsBytesFromJar(internalPath,path);
						log.infof("%s.getArtifactAsBytes: panel %d:%s %s returned %d bytes",CLSS,panelIndex,artifactName,path.toString(),bytes.length);
					}
					else {
						log.warnf("%s.getArtifactAsBytes: no location element for panel %d:%s",CLSS,panelIndex,artifactName);
					}
					break;
				}
				index++;
			}
		}
		File file = null;
		try {
			file = File.createTempFile(artifactName, "xml");
			Files.write(file.toPath(),bytes, StandardOpenOption.DELETE_ON_CLOSE);
		}
		catch(IOException ioe) {
			log.warnf("%s.getArtifactAsTemporaryFile: IOException creating temporary file for panel %d:%s (%s)",CLSS,
					panelIndex,artifactName,ioe.getLocalizedMessage());
		}
		return file;
	}
	/**
	 * The algorithm here assumes that the number of locations and artifacts is the same.
	 * (It should be).
	 * @param model
	 * @return the location string for a named artifact.
	 */
	public String getArtifactLocation(int panelIndex,String artifactName,InstallerData model) {
		String internalPath = null;
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int acount = artifactNodes.getLength();
			int index = 0;
			while(index<acount) {
				Node artifactNode = artifactNodes.item(index);
				if( artifactName.equalsIgnoreCase(xmlUtil.attributeValue(artifactNode, "name")) ) {
					NodeList locationNodes = panel.getElementsByTagName("location");
					int lcount = locationNodes.getLength();
					if( lcount>0) {
						Node locationNode = locationNodes.item(index);
						internalPath = locationNode.getTextContent();
					}
					break;
				}
				index++;
			}
		}
		return internalPath;
	}
	/**
	 * @param model
	 * @return a list of the artifacts associated with this step
	 */
	public List<Artifact> getArtifacts(int panelIndex,InstallerData model) {
		List<Artifact> artifacts = new ArrayList<>();
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int count = artifactNodes.getLength();
			int index = 0;
			while(index<count) {
				Node artifactNode = artifactNodes.item(index);
				Artifact artifact = new Artifact();
				artifact.setName(xmlUtil.attributeValue(artifactNode, "name"));
				artifact.setType(xmlUtil.attributeValue(artifactNode, "type"));
				artifact.setSubtype(xmlUtil.attributeValue(artifactNode, "subtype"));
				artifact.setRelease(xmlUtil.attributeValue(artifactNode, "release"));
				// Location is an element
				NodeList locations = ((Element)artifactNode).getElementsByTagName("location");
				int ncount = locations.getLength();
				if(ncount>0) {  // There should be only one location
					Node locationNode = locations.item(0);
					artifact.setLocation(locationNode.getTextContent());
				}
				artifacts.add(artifact);
				index++;
			}
		}
		return artifacts;
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
					if( bom!=null ) {
						model.setBillOfMaterials(bom);
					}
					else {
						log.warnf("%s.getBillOfMaterials: Failed to create XML document.",CLSS);
					}
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
		Path path = null;
		String pathString = model.getModulePath();
		if( pathString==null ) {
			path = jarUtil.internalModuleContaining(InstallerConstants.MODULE_MARKER);
			if( path==null ) {
				log.warnf("%s.getPathToModule: Could not find path to module",CLSS);
			}
			else {
				model.setModulePath(path.toString());
			}
		}
		else {
			path = Paths.get(pathString);
		}
		return path;
	}

	// Return attributes of a particular panel. When it is created, fill in attributes,
	// else simply return the value from the map.
	public PanelData getPanelData(int panelIndex,InstallerData model) {
		Integer key = new Integer(panelIndex);
		PanelData data = model.getPanelMap().get(key);
		if( data==null ) {
			data = new PanelData();
			Element panelElement = getPanelElement(panelIndex,model);
		
			if( panelElement!=null ) {
				String product = "UNKNOWN";
				for(PropertyItem prop:getProperties(model)) {
					if( prop.getName().equalsIgnoreCase("product")){
							product = prop.getValue();
							break;
					}
				}

				PanelType type = PanelType.CONCLUSION;  // Because we have to set it to something
				String val = xmlUtil.attributeValue(panelElement, "type");
				try {
					type = PanelType.valueOf(val.toUpperCase());
				}
				catch(IllegalArgumentException iae) {
					log.warnf("%s.getPanelData: Could not convert %s into a PanelType",CLSS,val);
				}
				val = xmlUtil.attributeValue(panelElement, "essential");
				
				data.setEssential(false);
				if( "true".equalsIgnoreCase(val)) data.setEssential(true);
				
				String subtype = xmlUtil.attributeValue(panelElement, "subtype");
				int version = InstallerConstants.UNSET;  
				String versString = xmlUtil.attributeValue(panelElement, "version");
				if(versString!=null && !versString.isEmpty()) {
					try {
						version = Integer.parseInt(versString);
					}
					catch(NumberFormatException nfe) {
						log.warnf("%s.getPanelData: Could not convert %s to int (%s)",CLSS,versString,nfe.getLocalizedMessage());
					}
				}
				data.setType(type);
				if(subtype==null || subtype.length()==0) subtype = type.name();
				data.setSubtype(subtype);
				data.setVersion(version);
				data.setCurrentVersion(PersistenceHandler.getInstance().getStepVersion(product,type,subtype));
				// The title is a child element
				NodeList titles = panelElement.getElementsByTagName("title");
				if( titles.getLength()>0) {
					Node titleElement = titles.item(0);
					data.setTitle(titleElement.getTextContent());
				}
			}
			model.getPanelMap().put(key,data);
		}
		
		return data;
	}

	// Return property name value pairs associated with a particular panel
	public List<PropertyItem> getPanelProperties(int panelIndex,InstallerData model) {
		List<PropertyItem> properties = new ArrayList<>();
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList propertyNodes = panel.getElementsByTagName("property");
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
	
	public String getPreference(String key) {
		return prefs.get(key, "");
	}
	
	public String getProductName(InstallerData model) {
		String productName= model.getProductName();
		if(productName==null || productName.isEmpty()) {
			for(PropertyItem prop:getProperties(model)) {
				if(prop.getName().equalsIgnoreCase("product")) {
					productName  = prop.getValue();
					model.setProductName(productName);
					break;
				};
			}
		}
		return productName;
	}


	// Return property name value pairs
	// We only want properties that are direct children
	public List<PropertyItem> getProperties(InstallerData model) {
		List<PropertyItem> properties = new ArrayList<>();
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) {
			Element root = bom.getDocumentElement();
			NodeList children = root.getChildNodes();
			int count = children.getLength();
			int index = 0;
			while(index<count) {
				Node propertyNode = children.item(index);
				if( propertyNode.getNodeName().equalsIgnoreCase("property") ) {
					String name = xmlUtil.attributeValue(propertyNode, "name");
					String value = propertyNode.getTextContent();
					properties.add(new PropertyItem(name,value));
				}
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
	public PanelType getStepType(int panelIndex,InstallerData model) {
		PanelType type = PanelType.WELCOME;    // If all else fails
		Node panelElement = getPanelElement(panelIndex,model);
		if( panelElement!=null ) {
			String name = xmlUtil.attributeValue(panelElement, "type");
			try {
				type = PanelType.valueOf(name.toUpperCase());
			}
			catch(IllegalArgumentException iae) {
				log.warnf("%s.getStepType: Could not convert %s into a WizardStepType",CLSS,name);
			}
		}
		return type;
	}
	public String getStepSubtype(int panelIndex,InstallerData model) {
		String subtype = "";    // If all else fails, set to same as type
		Node panelElement = getPanelElement(panelIndex,model);
		if( panelElement!=null ) {
			subtype = xmlUtil.attributeValue(panelElement, "subtype");
			if(subtype==null || subtype.isEmpty() ) {
				subtype = getStepType(panelIndex,model).name();
			}
		}
		return subtype;
	}

	public int getStepVersion(int index,InstallerData model) {
		Element panel = getPanelElement(index,model);
		int version = InstallerConstants.UNSET;   // An error
		if( panel!=null) {
			String versString = xmlUtil.attributeValue(panel, "version");
			if(versString!=null && !versString.isEmpty() ) {
				try {
					version = Integer.parseInt(versString);
				}
				catch(NumberFormatException nfe) {
					log.warnf("%s.getStepVersion: Could not convert %s to int (%s)",CLSS,versString,nfe.getLocalizedMessage());
				}
			}
		}
		return version;
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
	public BasicInstallerStep getWizardStep(int index,BasicInstallerStep prior,Model<InstallerData> dataModel) {
		PanelType stepType = getStepType(index,dataModel.getObject());
		String title = getStepTitle(index,dataModel.getObject());
		BasicInstallerStep step = stepFactory.createStep(index,prior,stepType,title,dataModel);
		return step;
	}
	public String loadArtifactAsModule(int panelIndex,String artifactName,InstallerData model) {
		String result = null;
		byte[] bytes = getArtifactAsBytes(panelIndex,artifactName,model);
		if( bytes!=null && bytes.length>0 ) {
			// If we have data, we had to have a path
			String filename = getArtifactLocation(panelIndex,artifactName,model);
			int pos = filename.lastIndexOf("/");
			if (pos>0 ) filename = filename.substring(pos+1);
			try {
				log.infof("%s.loadArtifactAsModule: Installing %d bytes as %s",CLSS,bytes.length,filename);
				context.getModuleManager().installModule(filename, bytes);	
			}
			catch( Exception ex) {
				result = String.format( "Failed to install %s (%s)", filename,ex.getMessage());
				log.warn(result);
			}
		}
		else {
			result = String.format( "Failed to find %s module in bundle",artifactName);
			log.warn(result);
		}
		return result;
	}
	
	public String loadArtifactAsScanClass(int panelIndex,TagProviderMeta provider,String artifactName,InstallerData model) {
		String result = null;
		if( provider==null || provider.getName()==null || provider.getName().isEmpty() ) {
			result = "A tag provider must be selected before installing the scan classes";
		}
		else {
			String providerName = provider.getName();
			byte[] bytes = getArtifactAsBytes(panelIndex,artifactName,model);
			if( bytes!=null && bytes.length>0 ) {
				List<ScanClass> scanClasses = scanUtil.listFromBytes(providerName,bytes);
				try {
					log.infof("%s.loadArtifactAsScanClass: Installing %d bytes as %s",CLSS,bytes.length,artifactName);
					context.getTagManager().getTagProvider(providerName).addScanClasses(scanClasses);	
				}
				catch( Exception ex) {
					result = String.format( "Failed to install %s (%s)", artifactName,ex.getMessage());
					log.warn(result);
				}
			}
			else {
				result = String.format( "Failed to find %s resource in bundle",artifactName);
				log.warn(result);
			}
		}
		return result;
	}
	
	public String loadArtifactAsTags(int panelIndex,TagProviderMeta provider,String artifactName,InstallerData model,TempFileTaskProgressListener listener) {
		String result = null;
		if( provider==null || provider.getName()==null || provider.getName().isEmpty() ) {
			result = "A tag provider must be selected before installing the tags";
		}
		else {
			String providerName = provider.getName();
			File file = getArtifactAsTemporaryFile(panelIndex,artifactName,model);
			if( file != null ) {
				try {
					log.infof("%s.loadArtifactAsTags: Installing %s as %s",CLSS,file.getName(),artifactName);
					listener.setTempFile(file);
					tagUtil.listFromFile(file,providerName,listener);
				}
				catch( Exception ex) {
					result = String.format( "Failed to install %s (%s)", artifactName,ex.getMessage());
					log.warn(result);
				}
			}
			else {
				result = String.format( "Failed to find %s tags in bundle",artifactName);
				log.warn(result);
			}
		}
		return result;
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
		this.scanUtil = new ScanClassUtility();
		this.tagUtil  = new TagUtility(context);
		this.xmlUtil  = new XMLUtility();
	}
	
	public void setPreference(String key,String value) {
		prefs.put(key,value);
	}
}

