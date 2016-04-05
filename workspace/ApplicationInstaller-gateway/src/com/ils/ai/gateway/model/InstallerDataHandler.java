/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.prefs.Preferences;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ils.ai.gateway.InstallerConstants;
import com.ils.ai.gateway.panel.BasicInstallerPanel;
import com.ils.ai.gateway.utility.FileUtility;
import com.ils.ai.gateway.utility.JarUtility;
import com.ils.ai.gateway.utility.ScanClassUtility;
import com.ils.ai.gateway.utility.TagUtility;
import com.ils.ai.gateway.utility.TransactionGroupUtility;
import com.ils.ai.gateway.utility.XMLUtility;
import com.ils.common.db.DBUtility;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.model.ApplicationScope;
import com.inductiveautomation.ignition.common.project.GlobalProps;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectPublishMode;
import com.inductiveautomation.ignition.common.project.ProjectResource;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.common.sqltags.model.ScanClass;
import com.inductiveautomation.ignition.common.user.AuthenticatedUser;
import com.inductiveautomation.ignition.common.user.BasicAuthenticatedUser;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.common.xmlserialization.SerializationException;
import com.inductiveautomation.ignition.common.xmlserialization.deserialization.XMLDeserializer;
import com.inductiveautomation.ignition.common.xmlserialization.serialization.XMLSerializer;
import com.inductiveautomation.ignition.gateway.SRContext;
import com.inductiveautomation.ignition.gateway.images.ImageFormat;
import com.inductiveautomation.ignition.gateway.images.ImageManager;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.project.ProjectManager;
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
	private TransactionGroupUtility transactionUtil = null;
	private XMLUtility xmlUtil = null;
	private final InstallerPanelFactory stepFactory;
	
    
	/**
	 * Constructor is private per Singleton pattern.
	 */
	private InstallerDataHandler() {
		log = LogUtil.getLogger(getClass().getPackage().getName());
		this.prefs = Preferences.userRoot().node(PREFERENCES_NAME);
		this.stepFactory = new InstallerPanelFactory();
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
	 * Create a new project with resources copied from am existing one.
	 * We are guaranteed that the new project does not yet exist.
	 * Save the newly copied project.
	 * @param oldName
	 * @param backupName
	 * @return
	 */
	public String copyProjectToBackup(String oldName,String backupName) {
		String result = null;
		
		ProjectManager pmgr = getContext().getProjectManager();
		try {
			pmgr.copyProject(oldName, backupName, true); // Will overwrite, name, true); 
			Project backup = pmgr.getProject(backupName, ApplicationScope.ALL, ProjectVersion.Published);
			GlobalProps props = pmgr.getProps(backup.getId(), ProjectVersion.Published);
			AuthenticatedUser user = new BasicAuthenticatedUser(props.getAuthProfileName(),"1","admin",props.getRequiredRoles());
			pmgr.saveProject(backup, user, "n/a", 
					String.format("ILS Automation Installer: updated from %s",oldName), false);
		}
		catch(Exception ex) {
			result = String.format("Exception copying project %s (%s)",oldName,ex.getLocalizedMessage());
		}
		return result;
		
	}
	/**
	 * Inspect the properties for the specified panel looking for a "database" property. If the property
	 * has no value, return the name specified as a toolkit property.
	 * @return
	 */
	public String datasourceNameFromProperties(int index,InstallerData model) {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String datasource = "";
        // If the production provider property has a value, use it. Otherwise get the toolkit property
		List<PropertyItem> properties = getPanelProperties(index, model);
		for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("database")) {
    			if(prop.getType().equalsIgnoreCase("production")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					datasource= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE);
    				}
    				else {
    					datasource = prop.getValue();
    				}
    			}
    			else if(prop.getType().equalsIgnoreCase("isolation")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					datasource= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE);
    				}
    				else {
    					datasource = prop.getValue();
    				}
    			}
    		}
    	}
		return datasource;
	}
	
	public String executeSQLFromArtifact(String datasource,int panelIndex,String artifactName,InstallerData model) {
		String result = null;
		byte[] bytes = getArtifactAsBytes(panelIndex,artifactName,model);
		if( bytes!=null && bytes.length>0 ) {
			// If we have data, we had to have a path
			String sql = new String(bytes);
			String[] lines = sql.split(";\n");
			if( lines.length<2 ) {
				lines = sql.split(";\r\n");
				if( lines.length<2 ) {
					lines = sql.split("GO\n");
					if( lines.length<2 ) {
						lines = sql.split("GO\r\n");
					}
				}
			}
			try {
				result = dbUtil.executeMultilineSQL(lines, datasource);
			}
			catch( Exception ex) {
				result = String.format( "Exception executing SQL (%s)",ex.getMessage());
				log.warn(result);
			}
		}
		else {
			result = String.format( "Failed to find %s sql in bundle",artifactName);
			log.warn(result);
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
	 * @return the contents of the specified artifact as a byte array.
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
	 * @return a reference to a temporary file that contains the contents of the specified
	 *         artifact. The file should be automatically deleted on closure.
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
			file = File.createTempFile("tagartifacts",".xml");
			Files.write(file.toPath(),bytes,StandardOpenOption.TRUNCATE_EXISTING);
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
	 * We use a simplified mime type based on the file extension of the artifact's location.
	 * @param model
	 * @return the mimetype of the specified artifact.
	 */
	public String getArtifactMimeType(int panelIndex,String artifactName,InstallerData model) {
		String mime = "application/zip"; 
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
						String filepath = locationNode.getTextContent();
						int pos = filepath.lastIndexOf(".");
						if( pos>0 ) {
							String extension = filepath.substring(pos+1);
							if( extension.equalsIgnoreCase("PDF"))       mime = "application/pdf";
							else if( extension.equalsIgnoreCase("DOC"))  mime = "application/msword";
							else if( extension.equalsIgnoreCase("DOCX")) mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
							else if( extension.equalsIgnoreCase("VSD"))  mime = "application/visio";
							else if( extension.equalsIgnoreCase("XLS"))  mime = "application/vnd.ms-exel";
							else if( extension.equalsIgnoreCase("XLSX")) mime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
							else if( extension.equalsIgnoreCase("XML"))  mime = "application/xml";
							else if( extension.equalsIgnoreCase("ZIP"))  mime = "application/zip";
							else {
								log.warnf("%s.getArtifactMimeType: panel %d:%s %s has unrecognized file extension",CLSS,panelIndex,artifactName,filepath);
							}
						}
						else {
							log.warnf("%s.getArtifactMimeType: panel %d:%s %s has no file extension",CLSS,panelIndex,artifactName,filepath);
						}
					}
					else {
						log.warnf("%s.getArtifactMimeType: no location element for panel %d:%s",CLSS,panelIndex,artifactName);
					}
					break;
				}
				index++;
			}
		}
		return mime;
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
	public GatewayContext getContext() { return context; }
	
	/**
	 * Beginning with the specified panel, search sequentially for a panel that meets the filter criteria
	 * indicated in the data model.
	 * @param index
	 * @param prior
	 * @param dataModel
	 * @return
	 */
	public BasicInstallerPanel getNextPanel(int index,BasicInstallerPanel prior,Model<InstallerData> dataModel) {
		InstallerData data = dataModel.getObject();
		boolean ignoreOptional = data.isIgnoringOptional();
		boolean ignoreCurrent   = data.isIgnoringCurrent();
		int count = getStepCount(data);
		while(index<count) {
			PanelData pd = getPanelData(index,data);
			if( (pd.isEssential() || ignoreOptional==false) &&
				(pd.getCurrentVersion()!=pd.getVersion() || ignoreCurrent==false) ) {
				String title = getStepTitle(index,data);
				PanelType type = getStepType(index,data);
				BasicInstallerPanel panel = stepFactory.createPanel(index,prior,type,title,dataModel); 
				return panel;
			}
			index++;
		}
		// If we get here, then return a conclusion. This shouldn't happen if BOM is well-formed
		String title = getStepTitle(count-1,data);
		BasicInstallerPanel step = stepFactory.createPanel(index,prior,PanelType.SUMMARY,title,dataModel);
		return step;
	}
	
	/**
	 * The epilog is a panel with final instructions. There should be only one.
	 * @param model
	 * @return
	 */
	public Element getEpilogElement(InstallerData model) {
		Node epilogElement = null;
		Document bom = getBillOfMaterials(model);
		if( bom!=null ) {
			NodeList elements = bom.getElementsByTagName("epilog");
			int count = elements.getLength();
			if( count>0  ) {
				epilogElement = elements.item(0);  
			}
			else {
				log.warnf("%s.getEpilogElement: No epilog in Bill of Materials.",CLSS);
			}
		}
		else {
			log.warnf("%s.getEpilogElement: Failed to read BOM from module.",CLSS);
		}
		return (Element)epilogElement;
	}
	public String getFinalPreamble(InstallerData model) {
		String text = "";    // If all else fails
		Element finalElement = getEpilogElement(model);
		if( finalElement!=null ) {
			NodeList elements = finalElement.getElementsByTagName("preamble");
			int count = elements.getLength();
			if( count>0  ) {
				Node element = elements.item(0);
				text = element.getTextContent();
			}
			else {
				log.warnf("%s.getFinalPreamble: No preamble element in epilog.",CLSS,count);
			}
		}
		return text;
	}
	public String getFinalTitle(InstallerData model) {
		String text = "";    // If all else fails
		Element finalElement = getEpilogElement(model);
		if( finalElement!=null ) {
			NodeList elements = finalElement.getElementsByTagName("title");
			int count = elements.getLength();
			if( count>0  ) {
				Node element = elements.item(0);
				text = element.getTextContent();
			}
			else {
				log.warnf("%s.getFinalTitle: No title element in epilog.",CLSS,count);
			}
		}
		return text;
	}
	// Return property name value pairs associated with a particular panel
	public List<PropertyItem> getFinalNotes(InstallerData model) {
		List<PropertyItem> notes = new ArrayList<>();
		Element epilog = getEpilogElement(model);
		if( epilog!=null ) {
			NodeList propertyNodes = epilog.getElementsByTagName("note");
			int count = propertyNodes.getLength();
			int index = 0;
			while(index<count) {
				Node propertyNode = propertyNodes.item(index);
				String name = xmlUtil.attributeValue(propertyNode, "name");
				String value = propertyNode.getTextContent();
				PropertyItem item = new PropertyItem(name,value);
				notes.add(item);
				index++;
			}
		}
		return notes;
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

				PanelType type = PanelType.SUMMARY;  // Because we have to set it to something
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
				PropertyItem item = new PropertyItem(name,value);
				item.setType(xmlUtil.attributeValue(propertyNode, "type"));
				properties.add(item);
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

	public String loadArtifactAsExternalFiles(int panelIndex,Artifact artifact,InstallerData model) {
		String result = null;
		String fromRoot   = artifact.getLocation();  // Add a trailing /
		if( !fromRoot.endsWith(File.separator)) fromRoot = fromRoot+File.separator;
		String toRoot="";
		// Prepend the actual path to the Gateway directory
		String type = artifact.getType();
		if( type.equalsIgnoreCase("python") ) {
			toRoot = context.getUserlibDir().getAbsolutePath()+File.separator+"pylib";  // User-lib/pylib full path
		}	
		else {
			toRoot = context.getLibDir().getAbsolutePath();  // lib full path
		}
		
		
		// Now process the files
		List<JarEntry> entries = jarUtil.filesInJarSubpath(getPathToModule(model),fromRoot );
		for(JarEntry entry:entries) {
			String name = entry.getName();
			String contents = jarUtil.readFileFromJar(name,getPathToModule(model));
			String subpath = name.substring(fromRoot.length());
			String path = String.format("%s%s%s", toRoot,File.separator,subpath);
			fileUtil.stringToFile(contents, path);     // Creates intervening directories
		}
		return result;
	}
	public String loadArtifactAsIconCollection(int panelIndex,Artifact artifact,InstallerData model) {
		String result = null;
		String root   = artifact.getLocation();  // Includes trailing /
		if( !root.endsWith(File.separator)) root = root+File.separator;
		ImageManager mgr = context.getImageManager();
		// First of all we create all intervening directories
		List<JarEntry> entries = jarUtil.directoriesInJarSubpath(getPathToModule(model),root );
		for(JarEntry entry:entries) {
			String name = entry.getName();
			String iconPath = name.substring(root.length());
			if(iconPath.length()==0) continue;
			if(iconPath.endsWith(File.separator)) iconPath=iconPath.substring(0,iconPath.length()-1);
			try {
				int pos = iconPath.lastIndexOf(File.separator);
				String fname = iconPath;
				String dir = null;
				if( pos>=0 ) {
					fname = iconPath.substring(pos+1);
					dir   = iconPath.substring(0,pos+1);
				}
				mgr.insertImageFolder(fname, dir);
			}
			catch(SQLException sqle) {
				log.infof("%s.loadArtifactAsIconCollection: Error making folder %s (%s)",CLSS,iconPath,sqle.getLocalizedMessage());
			}
		}
		
		// Now process the files
		entries = jarUtil.filesInJarSubpath(getPathToModule(model),root );
		for(JarEntry entry:entries) {
			String iconPath = entry.getName().substring(root.length());
			if(iconPath.length()==0) continue;

			int pos = iconPath.substring(0,iconPath.length()-1).lastIndexOf("/");
			String fname = iconPath;
			String dir = null;
			if( pos>=0 ) {
				fname = iconPath.substring(pos+1);
				dir   = iconPath.substring(0,pos+1);
			}
			ImageFormat type = ImageFormat.PNG;
			pos = fname.indexOf(".");
			if(pos>0) {
				String extension = fname.substring(pos+1);
				if( extension.equalsIgnoreCase("GIF")) type = ImageFormat.GIF;
				else if( extension.equalsIgnoreCase("JPG")) type = ImageFormat.JPEG;
			}
			byte[] bytes = jarUtil.readFileAsBytesFromJar(entry.getName(), getPathToModule(model));
			if( bytes.length>0 ) {

				try {
					InputStream in = new ByteArrayInputStream(bytes);
					BufferedImage readImage = ImageIO.read(in);
					int h = readImage.getHeight();
					int w = readImage.getWidth();
					mgr.insertImage(fname, "",type,dir,bytes,w,h,bytes.length);
				}
				catch(SQLException sqle) {
					log.infof("%s.loadArtifactAsIconCollection: Error making folder %s (%s)",CLSS,iconPath,sqle.getLocalizedMessage());
				}
				catch (Exception ex) {
					log.infof("%s.loadArtifactAsIconCollection: Error analyzing image %s (%s)",CLSS,iconPath,ex.getLocalizedMessage());
				}
			}
			else {
				log.infof("%s.loadArtifactAsIconCollection: Failed to convert %s into byte array",CLSS,iconPath);
			}
		}
		return result;
	}
	
	public String loadArtifactAsModule(int panelIndex,String artifactName,InstallerData model) {
		String result = null;
		byte[] bytes = getArtifactAsBytes(panelIndex,artifactName,model);
		if( bytes!=null && bytes.length>0 ) {
			// If we have data, we had to have a path
			String filename = getArtifactLocation(panelIndex,artifactName,model);
			int pos = filename.lastIndexOf(File.separator);
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
	
	public String loadArtifactAsProject(String location,String name,String profile,InstallerData model) {
		String result = null;
		Path internalPath = getPathToModule(model);
		InputStream projectReader = null;
		JarFile jar = null;
		try {
			jar = new JarFile(internalPath.toFile());
			JarEntry entry = jar.getJarEntry(location);
			if( entry!=null ) {
				projectReader = jar.getInputStream(entry);
				ProjectManager pmgr = getContext().getProjectManager();
				Project project = Project.fromXML(projectReader);
				project.setName(name);
				String description = project.getDescription();
				project.setDescription(updateProjectDescription(description,model));
				
				ProjectResource propsResource = project.getResourceOfType(GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE);
				
				GlobalProps globalProps = project.decodeOrCreate(GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE, context.createDeserializer(), GlobalProps.class);
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(getContext());
				globalProps.setAuthProfileName(profile);
				globalProps.setDefaultDatasourceName(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE));
				globalProps.setDefaultSQLTagsProviderName(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER));

				XMLSerializer serializer = new XMLSerializer();
				serializer.getClassNameMap().addDefaults();
				serializer.addObject(globalProps);
				byte[] bytes = serializer.serializeBinary(true);
				ProjectResource resource = new ProjectResource(propsResource.getResourceId(), GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE, null, ApplicationScope.ALL, bytes);
				project.putResource(resource, true);
				
				AuthenticatedUser user = new BasicAuthenticatedUser(globalProps.getAuthProfileName(),"1","admin",globalProps.getRequiredRoles());
				pmgr.saveProject(project, user, "n/a", "ILS Automation Installer: New project", true);
			}
			else {
				result = String.format("Project location %s does not match a path in the release bundle", location);
			}
		}
		catch(SAXException saxe) {
			result = String.format("SAX error loading project %s (%s)", name,saxe.getLocalizedMessage());
		}
		catch(IOException ioe) {
			result = String.format("IO error reading project %s (%s)", name,ioe.getLocalizedMessage());
		}
		catch(Exception ex) {
			result = String.format("Exception loading project %s (%s)",name,ex.getLocalizedMessage());
		}
		finally{
			if(projectReader!=null) {
				try {
					projectReader.close();
				}
				catch(IOException ignore) {}
			}
			if( jar!=null ) {
				try {
					jar.close();
				}
				catch(IOException ignore) {}
			}
		}
		return result;
	}
	
	public String loadArtifactAsScanClass(int panelIndex,String providerName,String artifactName,InstallerData model) {
		String result = null;
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

		return result;
	}
	
	public String loadArtifactAsTags(int panelIndex,String providerName,String artifactName,InstallerData model) {
		String result = null;

		File file = getArtifactAsTemporaryFile(panelIndex,artifactName,model);
		if( file != null ) {
			try {
				log.infof("%s.loadArtifactAsTags: Installing %s as %s",CLSS,file.getName(),artifactName);
				tagUtil.importFromFile(file,providerName);
			}
			catch( SAXException saxe) {
				result = String.format( "Error with %s file format (%s)", artifactName,saxe.getLocalizedMessage());
			}
			catch( Exception ex) {
				result = String.format( "Failed to install %s - see wrapper.log for details", artifactName);
				log.warn("InstallerDataHandler.loadArtifactAsTags: EXCEPTION",ex);
			}
		}
		else {
			result = String.format( "Failed to find %s tags in bundle",artifactName);
		}

		return result;
	}
	/*
	 * UNUSED: TransactionGroups are project resources, not independent ones.
	 */
	public String loadArtifactAsTransactionGroup(int panelIndex,String projectName,String artifactName,InstallerData model) {
		String result = null;

		File file = getArtifactAsTemporaryFile(panelIndex,artifactName,model);
		if( file != null ) {
			try {
				log.infof("%s.loadArtifactAsTransactionGroup: Installing %s as %s",CLSS,file.getName(),artifactName);
				transactionUtil.importFromFile(file,projectName);
			}
			catch( SAXException saxe) {
				result = String.format( "Error with %s file format (%s)", artifactName,saxe.getLocalizedMessage());
			}
			catch( Exception ex) {
				result = String.format( "Failed to install %s - see wrapper.log for details", artifactName);
				log.warn("InstallerDataHandler.loadArtifactAsTransactionGroup: EXCEPTION",ex);
			}
		}
		else {
			result = String.format( "Failed to find %s transaction group in bundle",artifactName);
		}
		return result;
	}

	// Start with an existing project and create a new one with resources overridden from another.
	public String mergeWithGlobalProjectFromLocation(String location,InstallerData model) {
		String result = null;

		ProjectManager pmgr = getContext().getProjectManager();
		Path internalPath = getPathToModule(model);
		InputStream projectReader = null;
		JarFile jar = null;
		try {
			Project mergee = pmgr.getGlobalProject(ApplicationScope.ALL);
			jar = new JarFile(internalPath.toFile());
			JarEntry entry = jar.getJarEntry(location);
			if( entry!=null ) {
				projectReader = jar.getInputStream(entry);
				Project standard = Project.fromXML(projectReader);
				mergee.applyDiff(standard);  // Standard overwrites
				GlobalProps props = pmgr.getProps(mergee.getId(), ProjectVersion.Published);
				AuthenticatedUser user = new BasicAuthenticatedUser(props.getAuthProfileName(),"1","admin",props.getRequiredRoles());
				pmgr.saveProject(mergee, user, "n/a", 
						String.format("ILS Automation Installer: global updated from %s",standard.getName()), false);
			}
			else {
				result = String.format("Project location %s does not match a path in the release bundle", location);
			}
		}
		catch(SAXException saxe) {
			result = String.format("SAX error loading project %s (%s)", location,saxe.getLocalizedMessage());
		}
		catch(IOException ioe) {
			result = String.format("IO error reading project %s (%s)", location,ioe.getLocalizedMessage());
		}
		catch(Exception ex) {
			result = String.format("Exception loading project %s (%s)",location,ex.getLocalizedMessage());
		}
		finally{
			if(projectReader!=null) {
				try {
					projectReader.close();
				}
				catch(IOException ignore) {}
			}
			if( jar!=null ) {
				try {
					jar.close();
				}
				catch(IOException ignore) {}
			}
		}


		return result;
	}
	// Start with an existing project and create a new one with resources overridden from another.
	public String mergeWithProjectFromLocation(Project existing,String location,String name,InstallerData model) {
		String result = null;
		if( existing!=null) {
			ProjectManager pmgr = getContext().getProjectManager();
			Path internalPath = getPathToModule(model);
			InputStream projectReader = null;
			JarFile jar = null;
			try {
				pmgr.copyProject(existing.getName(), name, true); // Will overwrite
				Project mergee = pmgr.getProject(name, ApplicationScope.ALL, ProjectVersion.Published);
				String description = mergee.getDescription();
				mergee.setDescription(updateProjectDescription(description,model));
				
				jar = new JarFile(internalPath.toFile());
				JarEntry entry = jar.getJarEntry(location);
				if( entry!=null ) {
					projectReader = jar.getInputStream(entry);
					Project standard = Project.fromXML(projectReader);
					mergee.applyDiff(standard);
					c
				}
				else {
					result = String.format("Project location %s does not match a path in the release bundle", location);
				}
			}
			catch(SAXException saxe) {
				result = String.format("SAX error loading project %s (%s)", name,saxe.getLocalizedMessage());
			}
			catch(IOException ioe) {
				result = String.format("IO error reading project %s (%s)", name,ioe.getLocalizedMessage());
			}
			catch(Exception ex) {
				result = String.format("Exception loading project %s (%s)",name,ex.getLocalizedMessage());
			}
			finally{
				if(projectReader!=null) {
					try {
						projectReader.close();
					}
					catch(IOException ignore) {}
				}
				if( jar!=null ) {
					try {
						jar.close();
					}
					catch(IOException ignore) {}
				}
			}
		}
		else {
			result = String.format("You must select an existing project to be the target of the merge");
		}
		return result;
	}
	/**
	 * Inspect the properties for the specified panel looking for a "provider" property. If the property
	 * has no value, return the name specified as a toolkit property.
	 * @return
	 */
	public String providerNameFromProperties(int index,InstallerData model) {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String datasource = "";
        // If the production provider property has a value, use it. Otherwise get the toolkit property
		List<PropertyItem> properties = getPanelProperties(index, model);
		for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("provider")) {
    			if(prop.getType().equalsIgnoreCase("production")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					datasource= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER);
    				}
    				else {
    					datasource = prop.getValue();
    				}
    			}
    			else if(prop.getType().equalsIgnoreCase("isolation")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					datasource= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER);
    				}
    				else {
    					datasource = prop.getValue();
    				}
    			}
    		}
    	}
		return datasource;
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
		this.transactionUtil = new TransactionGroupUtility(context);
		this.xmlUtil  = new XMLUtility();
	
				
	}
	
	public void setPreference(String key,String value) {
		prefs.put(key,value);
	}
	
	// Alter a project description to add its derivation
	// Replace anything after a double dash
	private String updateProjectDescription(String desc,InstallerData data) {
		int pos = desc.indexOf("--");
		if( pos>0 ) desc = desc.substring(pos);
		String product = "";
		String release = "";
		String date = "";
		for(PropertyItem pi:getProperties(data)) {
			if(pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_PRODUCT)) product = pi.getValue();
			else if(pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_DATE)) date = pi.getValue();
			else if(pi.getName().equalsIgnoreCase(InstallerConstants.PROPERTY_RELEASE)) release = pi.getValue();
		}
		return String.format("%s -- %s, %s, %s --", desc,product,release,date);
	}
	private void dumpXML(String label,ProjectResource res) {
		System.out.println(label);
		InputStream is = new ByteArrayInputStream(res.getData());
		XMLDeserializer deserializer = new XMLDeserializer();
		deserializer.getClassNameMap().addDefaults();
		XMLSerializer serializer = new XMLSerializer();
		serializer.getClassNameMap().addDefaults();
		try {
			String xml = deserializer.transcodeToXML(is, serializer);
			System.out.println(xml);
		}
		catch(SerializationException sex) {
			System.out.println(String.format("%s.dumpXML: Exception deserializing (%s)",CLSS, sex.getMessage()));
		}
		
	}
	
	/**
	 * Replace the global properties resource in the project.
	 * @param proj
	 * @param props
	 */
	private void updateGlobalProps(ProjectManager pmgr, Project proj, GlobalProps props) {
		ProjectResource globalPropsRes = proj.getResourceOfType("", "sr.global.props");
		dumpXML("updateGlobalProps: original",globalPropsRes);
		proj.deleteResource(globalPropsRes.getResourceId());
		try {
			props.setPublishMode(ProjectPublishMode.Auto);
			long resid = pmgr.getNewResourceId();
			XMLSerializer serializer = new XMLSerializer();
			serializer.getClassNameMap().addDefaults();
			serializer.addObject(props);
			
			String newXML = serializer.serializeXML();
			System.out.println(newXML);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zipOut = new GZIPOutputStream(baos);
			IOUtils.copy(new ByteArrayInputStream(newXML.getBytes("UTF-8")), zipOut);
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			xml = xml+"<objects>\n";
			xml = xml+"<o cls=\"com.inductiveautomation.ignition.common.project.GlobalProps\">\n";
			xml = xml+"<o-c m=\"setAuthProfileName\" s=\"1;str\"><str id=\"0\">default</str></o-c>\n";
			xml = xml+"<o-c m=\"setDefaultDatasourceName\" s=\"1;str\"><ref>0</ref></o-c>\n";
			xml = xml+"<o-c m=\"setDefaultSQLTagsProviderName\" s=\"1;str\"><ref>0</ref></o-c>\n";
			xml = xml+"</o>\n";
			xml = xml+"</objects>\n";

			//byte[] bytes = baos.toByteArray();
			byte[] bytes = xml.getBytes();
			ProjectResource resource = new ProjectResource(resid,"","sr.global.props","GlobalProps",
										ApplicationScope.ALL,bytes);
			zipOut.close();
			dumpXML("updateGlobalProps: new (from res)",resource);
			System.out.println(String.format("Resource is %d bytes",bytes.length));
			proj.putResource(resource);
		}
		catch(Exception ex) {
			System.out.println(String.format("%s.updateGlobalProps: Exception getting resource ID (%s)",CLSS, ex.getMessage()));
		}
	}
}

