/**
 *   (c) 2016-2018  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
import com.ils.ai.gateway.utility.CSVUtility;
import com.ils.ai.gateway.utility.FileUtility;
import com.ils.ai.gateway.utility.JarUtility;
import com.ils.ai.gateway.utility.PythonUtility;
import com.ils.ai.gateway.utility.ScanClassUtility;
import com.ils.ai.gateway.utility.TagUtility;
import com.ils.ai.gateway.utility.TransactionGroupUtility;
import com.ils.ai.gateway.utility.XMLUtility;
import com.ils.common.db.DBMS;
import com.ils.common.db.DBUtility;
import com.ils.common.persistence.ToolkitProperties;
import com.ils.common.persistence.ToolkitRecordHandler;
import com.inductiveautomation.ignition.common.model.ApplicationScope;
import com.inductiveautomation.ignition.common.project.GlobalProps;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectPublishMode;
import com.inductiveautomation.ignition.common.project.ProjectResource;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.common.script.JythonExecException;
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
 *           instance variable for any page or nested class within a page.
 */
public class InstallerDataHandler {
	private final static String CLSS = "InstallerDataHandler";
	private static final String FILE_SEPARATOR = "/";
	private static final String PREFERENCES_NAME = "InstallerPreferences";
	public static final int TAG_CHUNK_SIZE = 1000;
	public static final String PRE_HP_PATTERN = "<Property name=\"SQLBindingDatasource\">";
	public static final String PRE_SQL_PATTERN = "<Property name=\"PrimaryHistoryProvider\">";
	public static final String POST_PATTERN = "</Property>";
	private static final String TAGS_HEADER =  "<Tags>";
	private static final String TAGS_TRAILER = "</Tags>";
	private static InstallerDataHandler instance = null;
	
	private final LoggerEx log;
	private GatewayContext context = null;
	private final Preferences prefs;
	private CSVUtility csvUtil;
	private DBUtility dbUtil;
	private FileUtility fileUtil;
	private JarUtility jarUtil = null;
	private PythonUtility pyUtil = null;
	private ScanClassUtility scanUtil = null;
	public TagUtility tagUtil = null;
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
	 * Search the artifact for preference elements. Analyze and apply them.
	 * @param index
	 * @param model
	 */
	public void applyPreferences(int panelIndex,InstallerData model) {
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList preferenceNodes = panel.getElementsByTagName("preference");
			int count = preferenceNodes.getLength();
			int index = 0;
			while(index<count) {
				Node preferenceNode = preferenceNodes.item(index);
				String name = xmlUtil.attributeValue(preferenceNode, "name");
				String key  = xmlUtil.attributeValue(preferenceNode, "key");
				String type = xmlUtil.attributeValue(preferenceNode, "type");
				if( name!=null && !name.isEmpty() && key!=null && !key.isEmpty()) {
					String value = preferenceNode.getTextContent();
					if( value==null || value.isEmpty() ) {
						removePreference(name,key);
					}
					else if( type!=null && !type.isEmpty() ) {
						if( type.equalsIgnoreCase("home")) {
							String root = System.getProperty("user.home");
							// This works nicely for Mac and Linux, is a disaster for Windows. We'll just use C:\
							if( System.getProperty("os.name").startsWith("Windows")) {
								root = "C:";
							}
							setPreference(name,key,root+FILE_SEPARATOR+value);
						}
						else if( type.equalsIgnoreCase("lib")) {
							setPreference(name,key,context.getLibDir().getAbsolutePath()+FILE_SEPARATOR+value);
						}
						else if( type.equalsIgnoreCase("user-lib")) {
							setPreference(name,key,context.getUserlibDir().getAbsolutePath()+FILE_SEPARATOR+value);
						}
						else {
							log.infof("%s.applyPreferences: Preference %s on panel %d, unrecognized type (%s)", CLSS,key,panelIndex,type);
						}
					}
					else {
						setPreference(name,key,value);
					}
				}
				else {
					log.infof("%s.applyPreferences: Preference on panel %d found with missing name or key", CLSS,panelIndex);
				}

				index++;
			}
		}
		else {
			log.infof("%s.applyPreferences: Panel %d not found", CLSS,panelIndex);
		}
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
	public String copyProjectToBackup(String oldName,String backupName,InstallerData model) {
		String result = null;
		
		ProjectManager pmgr = getContext().getProjectManager();
		try {
			pmgr.copyProject(oldName, backupName, true); // Will overwrite, name, true); 
			Project backup = pmgr.getProject(backupName, ApplicationScope.ALL, ProjectVersion.Published);
			backup.setEnabled(false);
			GlobalProps props = pmgr.getProps(backup.getId(), ProjectVersion.Published);
			String adminProfile = getAdministrativeProfile(model);
			String adminUser = getAdministrativeUser(model);
			AuthenticatedUser user = new BasicAuthenticatedUser(props.getAuthProfileName(),adminProfile,adminUser,props.getRequiredRoles());
			pmgr.saveProject(backup, user, "n/a", 
					String.format("ILS Automation Installer: updated from %s",oldName), false);
		}
		catch(Exception ex) {
			result = String.format("Exception copying project %s (%s)",oldName,ex.getLocalizedMessage());
		}
		return result;
		
	}
	
	/**
	 * Inspect the properties for the specified panel looking for a "database" property. 
	 * First check for a list associated with the site, then from the definition page.
	 * Finally, if the property has no value, return the name in the property. 
	 * @return
	 */
	public List<String> datasourceNamesFromProperties(int index,InstallerData model) {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		List<String> datasources = new ArrayList<>();
        // If the production provider property has a value, use it. Otherwise get the toolkit property
		List<PropertyItem> properties = getPanelProperties(index, model);
		for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("database")) {
    			if(prop.getType().equalsIgnoreCase("production")) {
    				datasources = model.getCurrentSiteProductionDatasources();
    				if(datasources.isEmpty()) {
    					if(prop.getValue()==null || prop.getValue().isEmpty()) {
        					datasources.add(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE));
        				}
        				else {
        					datasources.add(prop.getValue());
        				}
    				}
    			}
    			else if(prop.getType().equalsIgnoreCase("isolation")) {
    				datasources = model.getCurrentSiteTestDatasources();
    				if(datasources.isEmpty()) {
    					if(prop.getValue()==null || prop.getValue().isEmpty()) {
    						datasources.add(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE));
    					}
    					else {
    						datasources.add(prop.getValue());
    					}
    				}
    			}
    			else {
    				datasources.add(prop.getValue());
    			}
    		}
    	}
		return datasources;
	}
	public String deleteFilesReferencedInArtifact(int panelIndex,Artifact artifact,InstallerData model) {
		String result = null;
		String type = artifact.getType();         // file or directory
		String subtype = artifact.getSubtype();   // lib/user-lib/home
		String prefix = System.getProperty("user.home");
		// This works nicely for Mac and Linux, is a disaster for Windows. We'll just use C:\
		if( System.getProperty("os.name").startsWith("Windows")) {
			prefix = "C:";
		}
		if( subtype.equalsIgnoreCase("lib"))            prefix = context.getLibDir().getAbsolutePath();
		else if( subtype.equalsIgnoreCase("user-lib"))  prefix = context.getUserlibDir().getAbsolutePath();
		String destination = artifact.getDestination();
		Path path = Paths.get(prefix, destination);
		if( type.equalsIgnoreCase("file") ) {
			result = fileUtil.deleteFile(path);
		}
		else {
			result = fileUtil.deleteDirectory(path);
		}
		return result;
	}
	/**
	 * Execute a python method associated with the artifact. The argument
	 * registers the user response.
	 * @param art the artifact
	 * @param response user response
	 * @return an empty string on success, else an error description
	 */
	public String executePython(String pythonPath) {
		String result = "";
		log.infof("%s.executePython: %s", CLSS,pythonPath);
		if( !pythonPath.isEmpty() ) {
			try {
				pyUtil.execute(pythonPath);
			}
			catch(JythonExecException jee) {
				result = String.format("%s execution exception (%s)",pythonPath,jee.getLocalizedMessage());
			}
		}
		return result;
	}
	/**
	 * Execute a python method associated with the artifact. The argument
	 * registers the user response.
	 * @param art the artifact
	 * @param response user response
	 * @return an empty string on success, else an error description
	 */
	public String executePythonFromArtifact(Artifact art,boolean response) {
		String result = "";
		String pythonPath = art.getScript();
		if( !pythonPath.isEmpty() ) {
			log.infof("%s.executePythonFromArtifact: %s (%s)", CLSS,pythonPath,(response?"true":"false"));
			try {
				pyUtil.processFlag(pythonPath,response);
			}
			catch(JythonExecException jee) {
				result = String.format("%s execution exception (%s)",pythonPath,jee.getLocalizedMessage());
			}
		}
		return result;
	}
	/**
	 * Execute the python referenced in a property, if any. The argument is the property value.
	 * @param property the property
	 * @return an empty string on success, else an error description
	 */
	public String executePythonFromProperty(PropertyItem property) {
		String result = "";
		String pythonPath = property.getScript();
		if( !pythonPath.isEmpty() ) {
			log.infof("%s.executePythonFromProperty: %s (%s-%s)", CLSS,pythonPath,property.getName(),property.getValue());
			try {
				pyUtil.updateValue(pythonPath,property.getValue().toString());
			}
			catch(JythonExecException jee) {
				result = String.format("%s execution exception (%s)",pythonPath,jee.getLocalizedMessage());
			}
		}
		return result;
	}
	public String executeSQLFromArtifact(String datasource,int panelIndex,String artifactName,InstallerData model) {
		String result = null;
		boolean debug = false;
		// Search for DBMS
		DBMS dbms = DBMS.ANSI;  // Default
		List<PropertyItem> properties =  getPanelProperties(panelIndex,model);
		for( PropertyItem property:properties) {
			if( "dbms".equalsIgnoreCase(property.getName()) ) {
				try {
					dbms = DBMS.valueOf(property.getType().toUpperCase());
				}
				catch(IllegalArgumentException iae) {
					log.infof("%s.executeSQLFromArtifact: Unrecognized DBMS type %s (ignored)", CLSS,property.getType());
				}
			}
			if( "debug".equalsIgnoreCase(property.getName()) ) {
				debug = property.getValue().equalsIgnoreCase("true");
			}
		}
		byte[] bytes = getArtifactAsBytes(panelIndex,artifactName,model);
		if( bytes!=null && bytes.length>0 ) {
			// Do our best to group multi-line statements into legal SQL
			// Combine multiple lines into single SQL statements
			Scanner scanner = new Scanner(new String(bytes));
			List<String> statements = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			while(scanner.hasNextLine()) {
				// Accumulate until we get to a statement terminator
				String line = scanner.nextLine();
				if( line.endsWith("\r")) line = line.substring(0, line.length()-1);
				
				// No matter what, a line of "go" is a terminator meaning "execute"
				if( line.trim().equalsIgnoreCase("go") ) {
					statements.add(sb.toString());
					if( debug ) log.info(sb.toString());
					sb.setLength(0);
				}
				else if( !dbms.equals(DBMS.SQLSERVER) && line.endsWith(";")) {
					sb.append(line.substring(0,line.length()-1));
					statements.add(sb.toString());
					if( debug ) log.info(sb.toString());
					sb.setLength(0);
				}

				else {
					sb.append(line);
					sb.append("\n");
				}
			}
			scanner.close();
			
			try {
				String statementArray[] = new String[statements.size()];
				if( debug ) log.info("------- execute SQL ----------");
				result = dbUtil.executeMultilineSQL(statements.toArray(statementArray), datasource);
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
	 * Search the internal database for a user in the specified profile that has admin
	 * privileges. This is the user that we want to make the owner of our projects.
	 * @param model the bean holder of all shared information.
	 * @return the profile of the first user with admin privileges.
	 */
	public String getAdministrativeProfile(InstallerData model) {
		getAdministrativeUser(model);    // Has side effect of setting the profile
		return model.getAdministrativeProfile();
	}
	
	/**
	 * Search the internal database for a user in the specified profile that has admin
	 * privileges. This is the user that we want to make the owner of our projects.
	 * @param model the bean holder of all shared information.
	 * @return
	 */
	public String getAdministrativeUser(InstallerData model) {
		String admin = "admin";
		model.setAdministrativeProfile("1");
		model.setAdministrativeUser(admin);
		List<Properties> propertyList = PersistenceHandler.getInstance().getAdministrativeUsers();
		// Just use the first one ...
		if( !propertyList.isEmpty()) {
			Properties props = propertyList.get(0);
			admin = props.getProperty("Name");
			model.setAdministrativeProfile(props.getProperty("ProfileId"));
			model.setAdministrativeUser(admin);
		}
		return admin;
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
	 * @return the contents of the csv artifact as a list of string lists.
	 */
	public List<List<String>> getArtifactAsCSV(int panelIndex,InstallerData model) {
		List<List<String>> csv = new ArrayList<>();;
		log.infof("%s.getArtifactAsCSV: panel %d",CLSS,panelIndex);
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList artifactNodes = panel.getElementsByTagName("artifact");
			int acount = artifactNodes.getLength();
			int index = 0;
			while(index<acount) {
				Node artifactNode = artifactNodes.item(index);
				if( "csv".equalsIgnoreCase(xmlUtil.attributeValue(artifactNode, "type")) ) {
					NodeList locationNodes = panel.getElementsByTagName("location");
					int lcount = locationNodes.getLength();
					if( lcount>0) {
						Node locationNode = locationNodes.item(index);
						String internalPath = locationNode.getTextContent();
						Path path = getPathToModule(model);
						byte[] bytes = jarUtil.readFileAsBytesFromJar(internalPath,path);
						csv = csvUtil.listFromBytes(bytes);
						log.infof("%s.getArtifactAsCSV: panel %d %s returned %d rows in csv",CLSS,panelIndex,path.toString(),csv.size());
					}
					else {
						log.warnf("%s.getArtifactAsCSV: no location element for panel %d",CLSS,panelIndex);
					}
					break;
				}
				index++;
			}
		}
		return csv;
	}
	/**
	 * Assume that the artifact is a list of tags in XML format. Split the list into sets of approximately 1000
	 * tags for chunked processing. We have found that the gateway times out if we attempt large numbers of tag imports.
	 * 
	 * Use the "historyprovider" property to define the provider for tags with history.
	 * <Property name="PrimaryHistoryProvider">BEDB</Property>
	 */
	public List<File> getArtifactAsListOfTagFiles(int panelIndex,String artifactName,InstallerData model) {
		List<File> files = new ArrayList<>();
		String contents = "";
		int total = 0;
		List<Integer> chunkSizes = new ArrayList<>();
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
						contents = jarUtil.readFileFromJar(internalPath,path);
						log.infof("%s.getArtifactAsListOfTagFiles: panel %d:%s %s returned %d bytes",CLSS,panelIndex,artifactName,path.toString(),contents.length());
					}
					else {
						log.warnf("%s.getArtifactAsListOfTagFiles: no location element for panel %d:%s",CLSS,panelIndex,artifactName);
					}
					break;
				}
				index++;
				total++;
			}
		}
		// If a history provider is specified, modify the tag file accordingly.
		String historyprovider = historyProviderNameFromProperties(panelIndex, model);
		String patternString = PRE_HP_PATTERN+"[^<]*"+POST_PATTERN;
		Pattern hppattern = null;
		try {
			hppattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		}
		catch(PatternSyntaxException pse) {
			log.warnf("%s.getArtifactAsListOfTagFiles: Syntax exception creating pattern for history provider (%s)",CLSS,pse.getLocalizedMessage());
		}
		patternString = PRE_SQL_PATTERN+"[^<]*"+POST_PATTERN;
		Pattern sqlpattern = null;
		try {
			sqlpattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		}
		catch(PatternSyntaxException pse) {
			log.warnf("%s.getArtifactAsListOfTagFiles: Syntax exception creating pattern for history provider (%s)",CLSS,pse.getLocalizedMessage());
		}
		
		// Strip header and trailer. Break on new Tag. We
		// make the rash assumption that the XML format 
		// matches what is dumped out by the Ignition export.
		Scanner scanner = new Scanner(contents);
		scanner.useDelimiter("<Tag ");
		if( scanner.hasNext() ) scanner.next();    // Skip over header
		StringBuffer sb = new StringBuffer();
		sb.append(TAGS_HEADER);
		int count = 0;
		while( scanner.hasNext() ) {
			String next = scanner.next();
			if( next.startsWith("   <Tag") && count>=TAG_CHUNK_SIZE) {
				File file = null;
				if( !sb.toString().endsWith(TAGS_TRAILER)) sb.append(TAGS_TRAILER);
				try {
					file = File.createTempFile("tagartifacts",".xml");
					// Before writing the file, do a global edit on history provider
					if( !historyprovider.isEmpty() ) {
						sb = replaceHistoryProviders(sb,historyprovider,hppattern);
						sb = replaceSQLProvider(sb,sqlpattern);
					}
					Files.write(file.toPath(),sb.toString().getBytes(),StandardOpenOption.TRUNCATE_EXISTING);
					files.add(file);
					chunkSizes.add(new Integer(count));
				}
				catch(IOException ioe) {
					log.warnf("%s.getArtifactAsListOfTagFiles: IOException creating temporary file for panel %d:%s (%s)",CLSS,
							                                                          panelIndex,artifactName,ioe.getLocalizedMessage());
				}
				count = 0;
				sb = new StringBuffer();
				sb.append(TAGS_HEADER);
			}
			sb.append("<Tag ");
			sb.append(next);
			count++;
		}
		scanner.close();
		
		if( count>0 ) {
			// One last chunk
			File file = null;
			try {
				file = File.createTempFile("tagartifacts",".xml");
				if( !historyprovider.isEmpty() ) {
					sb = replaceHistoryProviders(sb,historyprovider,hppattern);
					sb = replaceSQLProvider(sb,sqlpattern);
				}
				Files.write(file.toPath(),sb.toString().getBytes(),StandardOpenOption.TRUNCATE_EXISTING);
				files.add(file);
				chunkSizes.add(new Integer(count));
			}
			catch(IOException ioe) {
				log.warnf("%s.getArtifactAsListOfTagFiles: IOException creating final temporary file for panel %d:%s (%s)",CLSS,
						                                                          panelIndex,artifactName,ioe.getLocalizedMessage());
			}		
		}
		model.setChunkTotal(total);
		model.setChunkCounts(chunkSizes.toArray(new Integer[chunkSizes.size()]));
		return files;
	}
	
	private StringBuffer replaceHistoryProviders(StringBuffer sb,String hp, Pattern pattern) {
		StringBuffer buf = new StringBuffer(); 
		if( pattern!=null) {
			String[] outsides = pattern.split(sb.toString());
			buf.append(outsides[0]);
			int index = 1;
			while(index<outsides.length) {
				buf.append(PRE_HP_PATTERN);
				buf.append(hp);
				buf.append(POST_PATTERN);
				buf.append(outsides[index]);
				index++;
			} 
		}
		return buf;
	}
	// We use the project default provider
	private StringBuffer replaceSQLProvider(StringBuffer sb, Pattern pattern) {
		StringBuffer buf = new StringBuffer(); 
		if( pattern!=null) {
			String[] outsides = pattern.split(sb.toString());
			buf.append(outsides[0]);
			int index = 1;
			while(index<outsides.length) {
				buf.append(PRE_SQL_PATTERN);
				buf.append(POST_PATTERN);
				buf.append(outsides[index]);
				index++;
			} 
		}
		return buf;
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
						log.infof("%s.getArtifactAsTemporaryFile: panel %d:%s %s returned %d bytes",CLSS,panelIndex,artifactName,path.toString(),bytes.length);
					}
					else {
						log.warnf("%s.getArtifactAsTemporaryFile: no location element for panel %d:%s",CLSS,panelIndex,artifactName);
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
	 * (It should be). We are only given the name of the artifact.
	 * @param model
	 * @return the location string for a named artifact.
	 */
	public Element getArtifactLocation(int panelIndex,String artifactName,InstallerData model) {
		Element locationElement = null;
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
						locationElement = (Element)locationNodes.item(index);
					}
					break;
				}
				index++;
			}
		}
		return locationElement;
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
							else if( extension.equalsIgnoreCase("CSV"))  mime = "text/plain";
							else if( extension.equalsIgnoreCase("DOC"))  mime = "application/msword";
							else if( extension.equalsIgnoreCase("DOCX")) mime = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
							else if( extension.equalsIgnoreCase("JSON")) mime = "application/json";
							else if( extension.equalsIgnoreCase("PROJ")) mime = "application/xml";    // Ignition project
							else if( extension.equalsIgnoreCase("TXT"))  mime = "text/plain";
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
				// Comment is an element
				NodeList comments = ((Element)artifactNode).getElementsByTagName("comment");
				ncount = comments.getLength();
				if(ncount>0) {  // There should be only one comment
					Node commentNode = comments.item(0);
					artifact.setComment(commentNode.getTextContent());
				}
				// Destination is an element
				NodeList destinations = ((Element)artifactNode).getElementsByTagName("destination");
				ncount = destinations.getLength();
				if(ncount>0) {  // There should be only one destination
					Node destinationNode = destinations.item(0);
					artifact.setDestination(destinationNode.getTextContent());
				}
				// Script is an element
				NodeList scripts = ((Element)artifactNode).getElementsByTagName("script");
				ncount = scripts.getLength();
				if(ncount>0) {  // There should be only one script
					Node scriptNode = scripts.item(0);
					artifact.setScript(scriptNode.getTextContent());
				}
				artifacts.add(artifact);
				index++;
			}
		}
		return artifacts;
	}
	// Return authentication roles gleaned from this panel's properties
	public List<PropertyItem> getAuthenticationRoles(int panelIndex,InstallerData model) {
		List<PropertyItem> roles = new ArrayList<>();
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList propertyNodes = panel.getElementsByTagName("role");
			int count = propertyNodes.getLength();
			int index = 0;
			while(index<count) {
				Node propertyNode = propertyNodes.item(index);
				String name = xmlUtil.attributeValue(propertyNode, "name");
				String value = propertyNode.getTextContent();
				PropertyItem item = new PropertyItem(name,value);
				roles.add(item);
				index++;
			}
		}
		return roles;
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
	 * The DBMS list is simply a fixed list of strings.
	 */
	public List<String> getDBMSList() {
		List<String> results = new ArrayList<>();
		results.add("ANSI");
		results.add("MYSQL");
		results.add("ORACLE");
		results.add("POSTGRESS");
		results.add("SQLSERVER");
		return results;
	}
	/**
	 * Beginning with the specified panel, search sequentially for a panel that meets the filter criteria
	 * indicated in the data model. Note that for a feature FALSE, that feature must not be present.
	 * @param index one more than current panel
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
				(pd.getCurrentVersion()!=pd.getVersion() || ignoreCurrent==false ||
				 pd.getCurrentVersion()==InstallerConstants.UNSET) ) {
				// Test for proper site and/or features
				String site = data.getSiteName();
				List<String> features = data.getFeatures();
				
				// Feature check passes if 
				// 1) Panel has no features or
				// 2) All of panels features are included in the master data list and
				// 3) None of the panels feature are included in the master "exclude" list.
				boolean featuresOK = true;  // If there aren't any features, go ahead
				if( !pd.getFeatures().isEmpty() && !pd.matchFeature(features)) {  
					featuresOK = false;
				}
				if( !pd.getMissingFeatures().isEmpty() && !pd.verifyMissingFeature(features)) {  // If there aren't any features, go ahead
					featuresOK = false;
				}

				if( (site.isEmpty() || pd.getSiteNames().isEmpty() || pd.getSiteNames().contains(site)) && featuresOK)  {
					String title = getStepTitle(index,data);
					PanelType type = getStepType(index,data);
					BasicInstallerPanel panel = stepFactory.createPanel(index,prior,type,title,dataModel); 
					return panel;
				}
			}
			index++;
		}
		// If we get here, then return a conclusion. This shouldn't happen if BOM is well-formed
		String title = getStepTitle(count-1,data);
		BasicInstallerPanel step = stepFactory.createPanel(index,prior,PanelType.SUMMARY,title,dataModel);
		log.infof("%s.getNextPanel: %s (%s)",CLSS,title,step.getClass().getName());
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
	

	// Scan the property list looking for a type that is primary, or not,
	// as directed. Return a one word description for use when page is rendered.
	public String getLabel(List<PropertyItem> properties,boolean isPrimary) {
		for(PropertyItem prop:properties) {
    		String type = prop.getType();
    		if( isPrimary ) {
    			if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))        return "Production";
    			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) return "Batch Expert";
    			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))        return "Pysfc";
    		}
    		else {
    			if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY))      return "Secondary";
    			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) return "Isolation";
    		}
		}
    	return "???";
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
				// Analyze for Site-specificity
				data.setSiteNames(getSiteNames(panelIndex,model));
				// Analyze for features and non-features
				List<PropertyItem> properties = getPanelProperties(panelIndex,model);
				for(PropertyItem property:properties) {
					if( property.getType().equalsIgnoreCase("feature")) {
						String feature = property.getName();
						String value = property.getValue();
						boolean requiresFeature = false;
						if( value!=null && value.equalsIgnoreCase("true")) requiresFeature = true;
						if( requiresFeature ) {
							log.infof("%s.getPanelData: %d %s requires feature %s (%s)",CLSS,panelIndex,type.name(),feature,value);
							data.addFeature(feature);
						}
						else {
							log.infof("%s.getPanelData: %d %s must NOT have feature %s (%s)",CLSS,panelIndex,type.name(),feature,value);
							data.subtractFeature(feature);
						}
					}
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
				
				PropertyItem item = new PropertyItem(name,"");
				item.setType(xmlUtil.attributeValue(propertyNode, "type"));
				// Script is an element. For now we don't allow both scripts and fixed values
				NodeList scripts = ((Element)propertyNode).getElementsByTagName("script");
				int ncount = scripts.getLength();
				if(ncount>0) {  // There should be only one comment
					Node scriptNode = scripts.item(0);
					item.setScript(scriptNode.getTextContent());
				}
				else {
					String value = propertyNode.getTextContent();
					item.setValue(value);
				}
				properties.add(item);
				index++;
			}
		}
		return properties;
	}
	// Return the leaf of the path. 
	private String getNameFromPath(String path) {
		// Ignore trailing /
		if( path.endsWith("/")) path = path.substring(0,path.length()-1);
		String name = "";
		int pos = path.lastIndexOf("/");
		if( pos>=0 ) {
			name = path.substring(pos+1);
		}
		return name;
	}
	// Return the path except for the name. We include the trailing / on the result.
	// Ignore it on input.
	private String getParentPath(String path) {
		if(path.endsWith("/")) path = path.substring(0,path.length()-1);
		String parent = "";
		int pos = path.lastIndexOf("/");
		if( pos>=0 ) {
			parent = path.substring(0,pos+1);
		}
		return parent;
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
					String value = xmlUtil.attributeValue(propertyNode, "value");
					PropertyItem property = new PropertyItem(name,"");
					// Script is an optional element
					NodeList scripts = ((Element)propertyNode).getElementsByTagName("script");
					int ncount = scripts.getLength();
					if(ncount>0) {  // There should be only one script
						Node scriptNode = scripts.item(0);
						property.setScript(scriptNode.getTextContent());
					}
					else {
						// For now we assume that, if there is a script, there is no fixed value
						// or has been set as an attribute.
						value = propertyNode.getTextContent();
						property.setValue(value);
					}

					properties.add(property);
				}
				index++;
			}
		}
		return properties;
	}
	
	// Return site names gleaned from this panel's properties
	public List<String> getSiteNames(int panelIndex,InstallerData model) {
		List<String> sites = new ArrayList<>();
		List<PropertyItem> properties = getPanelProperties(panelIndex,model);
		for(PropertyItem property:properties) {
			if( property.getName().equalsIgnoreCase("site")) sites.add(property.getValue());
		}
		return sites;
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
	// Scan the property list looking for a type that is primary, or not,
	// as directed. Return the corresponding toolkit properties tag.
	public String getToolkitTag(List<PropertyItem> properties,String property,boolean isPrimary) {
		for(PropertyItem prop:properties) {
    		String type = prop.getType();
    		if( property.equalsIgnoreCase(InstallerConstants.PROPERTY_DATABASE))  {
    			if( isPrimary ) {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))        return ToolkitProperties.TOOLKIT_PROPERTY_DATABASE;
    				else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) return ToolkitProperties.TOOLKIT_PROPERTY_BE_DATABASE;
        			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))        return ToolkitProperties.TOOLKIT_PROPERTY_PYSFC_DATABASE;
    			}
    			else {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY))      return ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_DATABASE;
        			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) return ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DATABASE;
    			}
    		}
    		else if( property.equalsIgnoreCase(InstallerConstants.PROPERTY_DBMS))  {
    			if( isPrimary ) {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))        return ToolkitProperties.TOOLKIT_PROPERTY_DBMS;
    				else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_BATCH_EXPERT)) return ToolkitProperties.TOOLKIT_PROPERTY_BE_DBMS;
        			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PYSFC))        return ToolkitProperties.TOOLKIT_PROPERTY_PYSFC_DBMS;
    			}
    			else {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY))      return ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_DBMS;
        			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) return ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_DBMS;
    			}
    		}
    		else if( property.equalsIgnoreCase(InstallerConstants.PROPERTY_PROVIDER))  {
    			if( isPrimary ) {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_PRODUCTION))        return ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER;
    			}
    			else {
    				if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_SECONDARY))      return ToolkitProperties.TOOLKIT_PROPERTY_SECONDARY_PROVIDER;
        			else if( type.equalsIgnoreCase(InstallerConstants.PROPERTY_TYPE_ISOLATION)) return ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER;
    			}
    		}
		}
    	return "???";
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

	/**
	 * "external" means outside of an Ignition project. We are actually installing the files inside the
	 * Gateway install directory. This method is used for both "external" and "file" panels (making
	 * one of them redundant).
	 * NOTE:  context.getHome() is the "data" subdirectory.
	 * @param panelIndex
	 * @param artifact
	 * @param model
	 * @return
	 */
	public String loadArtifactAsFiles(int panelIndex,Artifact artifact,InstallerData model) {
		String result = null;
		String fromRoot   = artifact.getLocation();  // Add a trailing /
		if( !fromRoot.endsWith(FILE_SEPARATOR)) fromRoot = fromRoot+FILE_SEPARATOR;
		
		String type = artifact.getType();         // text or binary
		String subtype = artifact.getSubtype();   // lib/user-lib/home
		String toRoot = System.getProperty("user.home");
		// This works nicely for Mac and Linux, is a disaster for Windows. We'll just use C:\
		if( System.getProperty("os.name").startsWith("Windows")) {
			toRoot = "C:";
		}
		
		if( subtype.equalsIgnoreCase("lib"))            toRoot = context.getLibDir().getAbsolutePath();
		else if( subtype.equalsIgnoreCase("user-lib"))  toRoot = context.getUserlibDir().getAbsolutePath();
		String destination = artifact.getDestination();
		toRoot = String.format("%s%s%s", toRoot,FILE_SEPARATOR,destination);  // Destination is relative to root
		System.out.println(String.format("InstallerDataHandler.loadArtifactAsFiles: %s -> %s",artifact.getLocation(),toRoot));

		// Now process the files -- we really don't care if the artifact is a directory or single file
		List<JarEntry> entries = jarUtil.filesInJarSubpath(getPathToModule(model),fromRoot );
		for(JarEntry entry:entries) {
			String name = entry.getName();
			String subpath = name.substring(fromRoot.length());
			String path = String.format("%s%s%s", toRoot,FILE_SEPARATOR,subpath);
			if( type.equalsIgnoreCase("text")) {
				String contents = jarUtil.readFileFromJar(name,getPathToModule(model));
				fileUtil.stringToFile(contents, path);     // Creates intervening directories
			}
			else {
				byte[] contents = jarUtil.readFileAsBytesFromJar(name,getPathToModule(model));
				fileUtil.bytesToFile(contents, path);     // Creates intervening directories
			}
		}
		return result;
	}
	public String loadArtifactAsIconCollection(int panelIndex,Artifact artifact,InstallerData model) {
		String result = null;
		String root   = artifact.getLocation();  // Includes trailing /
		if( !root.endsWith(FILE_SEPARATOR)) root = root+FILE_SEPARATOR;
		ImageManager mgr = context.getImageManager();
		// First of all we create all intervening directories
		List<JarEntry> entries = jarUtil.directoriesInJarSubpath(getPathToModule(model),root );
		for(JarEntry entry:entries) {
			String name = entry.getName();
			String iconPath = name.substring(root.length());
			if(iconPath.length()==0) continue;
			// NOTE: Instead of File.separator, the XML should always use "/"
			if(iconPath.endsWith(FILE_SEPARATOR)) iconPath=iconPath.substring(0,iconPath.length()-1);
			try {
				int pos = iconPath.lastIndexOf(FILE_SEPARATOR);
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
			Element location = getArtifactLocation(panelIndex,artifactName,model);
			String filename = location.getTextContent();
			int pos = filename.lastIndexOf(FILE_SEPARATOR);
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

	/**
	 * Create a new project. Leave it disabled. 
	 * NOTE: This fails if the project has no resources.
	 * @param location
	 * @param name
	 * @param profile
	 * @param model
	 * @return
	 */
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
				project.setEnabled(false);
				
				ProjectResource propsResource = project.getResourceOfType(GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE);
				long propsResoureceId = -1;
				if( propsResource!=null ) {
					propsResoureceId = propsResource.getResourceId();
				}
				else {
					// Look for an unused resourceId
					propsResoureceId = 1L;
					while(project.getResource(propsResoureceId)!=null ) {
						propsResoureceId = propsResoureceId + 1;
					}
				}
				GlobalProps globalProps = project.decodeOrCreate(GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE, context.createDeserializer(), GlobalProps.class);
				ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(getContext());
				globalProps.setAuthProfileName(profile);
				globalProps.setDefaultDatasourceName(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_DATABASE));
				globalProps.setDefaultSQLTagsProviderName(toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER));

				XMLSerializer serializer = new XMLSerializer();
				serializer.getClassNameMap().addDefaults();
				serializer.addObject(globalProps);
				byte[] bytes = serializer.serializeBinary(true);
				ProjectResource resource = new ProjectResource(propsResoureceId, GlobalProps.MODULE_ID, GlobalProps.RESOURCE_TYPE, null, ApplicationScope.GATEWAY, bytes);
				project.putResource(resource, true);

				pmgr.addProject(project, true);
				
				// Re-constitute the project from the project manager. Without this step, we end
				// up with a project containing some duplicated resources. We don't know why this works ...
				project = pmgr.getProject(project.getId(),ApplicationScope.GATEWAY,ProjectVersion.Staging);
				project.setEnabled(false);
				/*
				for(ProjectResource res:project.getResources() ) {
					System.out.println(String.format("InstallerDataHandler: res %d.%d %s (%s) = %s",project.getId(),res.getResourceId(),res.getName(),
							res.getResourceType(),(project.isResourceDirty(res))?"DIRTY":"CLEAN"));
				}
				*/
				String adminProfile = getAdministrativeProfile(model);
				String adminUser = getAdministrativeUser(model);
				AuthenticatedUser user = new BasicAuthenticatedUser(globalProps.getAuthProfileName(),
											adminProfile,adminUser,globalProps.getRequiredRoles());
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
	// Site entries obtained from "site" elements within the panel XML.
	// TODO: A deep analysis of the entries
	public void loadSiteEntries(int panelIndex,InstallerData model) {
		List<SiteEntry> entries = new ArrayList<>();
		
		Element panel = getPanelElement(panelIndex,model);
		if( panel!=null ) {
			NodeList siteNodes = panel.getElementsByTagName("site");
			int count = siteNodes.getLength();
			int index = 0;
			while(index<count) {
				Node siteNode = siteNodes.item(index);
				String entryName = xmlUtil.attributeValue(siteNode, "name");
				SiteEntry se = new SiteEntry();
				se.setSiteName(entryName);
				// Add embedded properties and artifacts
				List<PropertyItem> properties = new ArrayList<>(); 
				NodeList children = siteNode.getChildNodes();
				int childCount = children.getLength();
				int childIndex = 0;
				while(childIndex<childCount) {
					Node childNode = children.item(childIndex);
					// Add embedded properties
					if( childNode.getNodeName().equalsIgnoreCase("property") ) {
						String name = xmlUtil.attributeValue(childNode, "name");
						String type = xmlUtil.attributeValue(childNode, "type");
						String value = childNode.getTextContent();
						properties.add(new PropertyItem(name,type,value));
					}
					childIndex++;
				}
				se.setProperties(properties);
				entries.add(se);
				index++;
			}
		
			
		}
		model.setSiteEntries(entries);
	}
	public String loadArtifactAsTags(int panelIndex,String providerName,String artifactName,InstallerData model) {
		String result = null;
		List<File> files = getArtifactAsListOfTagFiles(panelIndex,artifactName,model);
		int count = 1;
		for( File file: files ) {
			try {
				log.infof("%s.loadArtifactAsTags: %s: installing tags %d-%d",CLSS,artifactName,count,count+TAG_CHUNK_SIZE-1);
				count = count + TAG_CHUNK_SIZE;
				tagUtil.importFromFile(file,providerName);
			}
			catch( SAXException saxe) {
				result = String.format( "Error with %s file format (%s)", artifactName,saxe.getLocalizedMessage());
			}
			catch( Exception ex) {
				result = String.format( "Failed to install %s - see wrapper.log for details", artifactName);
				log.warn("InstallerDataHandler.loadArtifactAsTags: "+file.getAbsolutePath()+" EXCEPTION",ex);
			}
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

	// Start with the existing global project and merge in resources from a project in the bundle.
	public String mergeWithGlobalProjectFromArtifact(String location,InstallerData model) {
		String result = null;

		ProjectManager pmgr = getContext().getProjectManager();
		Path internalPath = getPathToModule(model);
		InputStream projectReader = null;
		JarFile jar = null;
		try {
			Project globalProject = pmgr.getGlobalProject(ApplicationScope.ALL);
			jar = new JarFile(internalPath.toFile());
			JarEntry entry = jar.getJarEntry(location);
			if( entry!=null ) {
				projectReader = jar.getInputStream(entry);
				Project updateProject = Project.fromXML(projectReader);
				mergeProject(globalProject,updateProject);
				pmgr.addProject(globalProject, true);    // Overwrite the global project
				// Re-constitute the project from the project manager. Without this step, we end
				// up with a project containing some duplicated resources. We don't know why this works ...
				globalProject = pmgr.getGlobalProject(ApplicationScope.GATEWAY);
				globalProject.setEnabled(true);
				
				GlobalProps globalProps = pmgr.getProps(globalProject.getId(), ProjectVersion.Staging);
				String adminProfile = getAdministrativeProfile(model);
				String adminUser = getAdministrativeUser(model);
				AuthenticatedUser user = new BasicAuthenticatedUser(globalProps.getAuthProfileName(),adminProfile,adminUser,globalProps.getRequiredRoles());
				
				pmgr.saveProject(globalProject, user, "n/a", 
						String.format("ILS Automation Installer: global updated from %s",updateProject.getName()), true);  // Publish
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
	/**
	 *  Start with an existing project and copy resources into it from a named partial project.
	 *  Leave it disabled.
	 * @param mainLite a "lite" version of the project. It does not have all the resources.
	 * @param location
	 * @param name
	 * @param model
	 * @return
	 */
	
	public String mergeWithProjectFromArtifact(Project mainLite,String location,String name,InstallerData model) {
		String result = null;
		if( mainLite!=null) {
			ProjectManager pmgr = getContext().getProjectManager();
			Path internalPath = getPathToModule(model);
			InputStream projectReader = null;
			JarFile jar = null;
			try {
				Project main = pmgr.getProject(mainLite.getName(), ApplicationScope.ALL, ProjectVersion.Published);
				String description = main.getDescription();
				main.setDescription(updateProjectDescription(description,model));
				main.setEnabled(false);
				
				jar = new JarFile(internalPath.toFile());
				JarEntry entry = jar.getJarEntry(location);
				if( entry!=null ) {
					projectReader = jar.getInputStream(entry);
					Project updateProject = Project.fromXML(projectReader);
					mergeProject(main,updateProject);
					try {
						pmgr.addProject(main, true);      // Overwrite itself
						main = pmgr.getProject(main.getId(),ApplicationScope.GATEWAY,ProjectVersion.Staging);
						main.setEnabled(false);
					}
					// We get an error re-constituting the project in staging scope. It appears not to matter with us.
					catch(Exception ex) {
						log.errorf("InstallerDataHandler.mergeProjectWithArtifact: Exception when re-constituting project (%s)",ex.getLocalizedMessage());
					}
					
					GlobalProps props = pmgr.getProps(main.getId(), ProjectVersion.Published);
					
					String adminProfile = getAdministrativeProfile(model);
					String adminUser = getAdministrativeUser(model);
					AuthenticatedUser user = new BasicAuthenticatedUser(props.getAuthProfileName(),adminProfile,adminUser,props.getRequiredRoles());
					try {
						pmgr.saveProject(main, user, "n/a", 
							String.format("ILS Automation Installer: updated from %s",updateProject.getName()), false);
					}
					// We get an error notifying project listeners. It appears not to matter with us.
					catch(Exception ex) {
						log.errorf("InstallerDataHandler.mergeProjectWithArtifact: Exception when saving merged project (%s)",ex.getLocalizedMessage());
					}
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
	 *  Start with an existing project and copy scripting resources into it from a named partial project.
	 *  Only insert new resources if the existing project does not already have that resource. Leave it disabled.
	 *  We only care about project script resources.
	 * @param mainLite a "lite" version of the project. It does not have all the resources.
	 * @param location
	 * @param name
	 * @param model
	 * @return
	 */
	
	public String mergeWithDefaultsFromArtifact(Project mainLite,String location,String name,InstallerData model) {
		String result = null;
		if( mainLite!=null) {
			ProjectManager pmgr = getContext().getProjectManager();
			Path internalPath = getPathToModule(model);
			InputStream projectReader = null;
			JarFile jar = null;
			try {
				Project main = pmgr.getProject(mainLite.getName(), ApplicationScope.ALL, ProjectVersion.Published);
				String description = main.getDescription();
				main.setDescription(updateProjectDescription(description,model));
				main.setEnabled(false);
				for( ProjectResource pr:main.getResourcesOfType("",InstallerConstants.SCRIPT_RESOURCE)) {
					String path = main.getFolderPath(pr.getResourceId());
					log.debugf("InstallerDataHandler.mergeWithDefaultsFromArtifact: main published %s (%s) %s",pr.getResourceType(),pr.getName(),path);
				}
				
				jar = new JarFile(internalPath.toFile());
				JarEntry entry = jar.getJarEntry(location);
				if( entry!=null ) {
					projectReader = jar.getInputStream(entry);
					Project defaultsProject = Project.fromXML(projectReader);
					for( ProjectResource pr:defaultsProject.getResourcesOfType("",InstallerConstants.SCRIPT_RESOURCE)) {
						String path = defaultsProject.getFolderPath(pr.getResourceId());
						log.debugf("InstallerDataHandler.mergeWithDefaultsFromArtifact: defaults %s (%s) %s",pr.getResourceType(),pr.getName(),path);
					}
					// Remove resources from defaults that are already in main
					int count = scrubDefaultsProject(defaultsProject,main);
					// Nothing to do if there are no novel resources 
					if( count >0 ) {
						// List remaining scripting resources:
						for( ProjectResource pr:main.getResources()) {
							String path = defaultsProject.getFolderPath(pr.getResourceId());
							if( path.startsWith("n/a ") ) continue;  // Unknown where these come from
							log.infof("InstallerDataHandler.mergeWithDefaultsFromArtifact: inserting %s (%s) %s",pr.getResourceType(),pr.getName(),path);
						}
						try {
							mergeProject(main,defaultsProject);
							pmgr.addProject(main, true);      // Overwrite itself
							main = pmgr.getProject(main.getId(),ApplicationScope.GATEWAY,ProjectVersion.Published);
							main.setEnabled(false);
						}
						// We get an error re-constituting the project in staging scope. It appears not to matter with us.
						catch(Exception ex) {
							log.errorf("InstallerDataHandler.mergeProjectWithArtifact: Exception when re-constituting project (%s)",ex.getLocalizedMessage());
						}
						
						GlobalProps props = pmgr.getProps(main.getId(), ProjectVersion.Published);
						
						String adminProfile = getAdministrativeProfile(model);
						String adminUser = getAdministrativeUser(model);
						AuthenticatedUser user = new BasicAuthenticatedUser(props.getAuthProfileName(),adminProfile,adminUser,props.getRequiredRoles());
						try {
							pmgr.saveProject(main, user, "n/a", 
								String.format("ILS Automation Installer: updated from %s",defaultsProject.getName()), false);
						}
						// We get an error notifying project listeners. It appears not to matter with us.
						catch(Exception ex) {
							log.errorf("InstallerDataHandler.mergeProjectWithArtifact: Exception when saving merged project (%s)",ex.getLocalizedMessage());
						}
					}
					else {
						log.info("InstallerDataHandler.mergeWithDefaultsFromArtifact: defaults has no scripts new to this project");
					}
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
	 * Inspect the properties for the specified panel looking for a "historyprovider" property. If the property
	 * has no value, return the name specified as a toolkit property.
	 * @return
	 */
	public String historyProviderNameFromProperties(int index,InstallerData model) {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String datasource = "";
        // If the production provider property has a value, use it. Otherwise get the toolkit property
		List<PropertyItem> properties = getPanelProperties(index, model);
		for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("historyprovider")) {
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
    			else {
    				datasource = prop.getValue();
    			}
    		}
    	}
		return datasource;
	}
	/**
	 * Inspect the properties for the specified panel looking for a "provider" property. If the property
	 * has no value, return the name specified as a toolkit property.
	 * @return
	 */
	public String providerNameFromProperties(int index,InstallerData model) {
		ToolkitRecordHandler toolkitHandler = new ToolkitRecordHandler(context);
		String providerName = "";
        // If the production provider property has a value, use it. Otherwise get the toolkit property
		List<PropertyItem> properties = getPanelProperties(index, model);
		for(PropertyItem prop:properties) {
    		if(prop.getName().equalsIgnoreCase("provider")) {
    			if(prop.getType().equalsIgnoreCase("production")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					providerName= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_PROVIDER);
    				}
    				else {
    					providerName = prop.getValue();
    				}
    			}
    			else if(prop.getType().equalsIgnoreCase("isolation")) {
    				if(prop.getValue()==null || prop.getValue().isEmpty()) {
    					providerName= toolkitHandler.getToolkitProperty(ToolkitProperties.TOOLKIT_PROPERTY_ISOLATION_PROVIDER);
    				}
    				else {
    					providerName = prop.getValue();
    				}
    			}
    			else {
    				providerName = prop.getValue();
    			}
    		}
    	}
		return providerName;
	}
	/**
	 * This step is necessary before the instance is useful. Most other
	 * properties are initialized lazily.
	 */
	public void setContext(GatewayContext ctx) { 
		this.context=ctx;
		this.csvUtil= new CSVUtility();
		this.dbUtil = new DBUtility(context);
		this.fileUtil = new FileUtility();
		this.jarUtil  = new JarUtility(context);
		this.pyUtil   = new PythonUtility(context);
		this.scanUtil = new ScanClassUtility();
		this.tagUtil  = new TagUtility(context);
		this.transactionUtil = new TransactionGroupUtility(context);
		this.xmlUtil  = new XMLUtility();
	
				
	}
	public void removePreference(String name, String key) {
		Preferences preferences = Preferences.userRoot().node(name);
		preferences.remove(key);
	}
	
	public void setPreference(String key,String value) {
		prefs.put(key,value);
	}
	
	public void setPreference(String name, String key,String value) {
		Preferences preferences = Preferences.userRoot().node(name);
		preferences.put(key,value);
	}
	
	// Merge resources from the partial project into the original.
	// In Ignition there are separate folder hierarchies by resource type.
	// The path names can be duplicated.
	// main - the original project that needs to get updated
	// updater - the partial project with new resources
	private void mergeProject(Project main,Project updater) {
		log.infof("%s.mergeProject: Merging resources from %s into %s",CLSS,updater.getTitle(),main.getTitle());
		Map<ProjectResourceKey,UUID> mainFolders = new HashMap<>();
		Map<ProjectResourceKey,Long> updateResources = new HashMap<>();       
		Map<String,Long> updateSingletons = new HashMap<>();  // Unnamed resources are "singletons"
		
		// Create map of resources to update
		for( ProjectResource res:updater.getResources() ) {
			if( res.getName()==null || res.getName().isEmpty() ) {
				updateSingletons.put(res.getResourceType(), new Long(res.getResourceId()));
				//log.infof("%s.mergeProject: Update singleton %s",CLSS,res.getResourceType());
			}
			else if( !res.isFolder() ) {
				String path = updater.getFolderPath(res.getResourceId());
				ProjectResourceKey key = new ProjectResourceKey(res.getResourceType(),path);
				//log.infof("%s.mergeProject: Update %s at %s",CLSS,res.getResourceType(),path);
				updateResources.put(key, new Long(res.getResourceId()));
			}
		}
		
		// Create list of resources in the main that are in the update list or junk
		// Along the way build a map of folders. 
		List<ProjectResource> toBeDeleted = new ArrayList<>();
		for( ProjectResource res:main.getResources() ) {
			if( res.getName()==null || res.getName().isEmpty() ) {
				if( updateSingletons.get(res.getResourceType())!=null ) {
					//log.infof("%s.mergeProject: Clear singleton %s",CLSS,res.getResourceType());
					toBeDeleted.add(res);
				}
			}
			else if( !res.isFolder() ) {
				String path = main.getFolderPath(res.getResourceId());
				if(path.startsWith("n/a ")){
					log.infof("%s.mergeProject:  Clear junk %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),path);
					toBeDeleted.add(res);
				}
				else {
					updateFolderMap(main,mainFolders,res);
					ProjectResourceKey key = new ProjectResourceKey(res.getResourceType(),path);
					if( updateResources.get(key)!=null ) {
						//log.infof("%s.mergeProject: Clear %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),path);
						toBeDeleted.add(res);
					}
					else{
						//log.infof("%s.mergeProject: Retaining %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),path);
					}
				}
			}
		}
		// Now do the actual deletion
		for( ProjectResource res:toBeDeleted ) {
			main.deleteResource(res.getResourceId());
		}
		
		// For starters, add in the replaced singletons
		// No need to worry about paths and parents(%)
		for( String type : updateSingletons.keySet() ) {
			long id = (updateSingletons.get(type)).longValue();
			main.putResource(updater.getResource(id));
		}
		
		// Now add the "normal" resources 
		for( ProjectResourceKey key:updateResources.keySet() ) {
			ProjectResource res = updater.getResource(updateResources.get(key).longValue());
			// For each resource to be added we need to get the id of the enclosing parent
			String parentPath = getParentPath(key.getResourcePath());
			ProjectResourceKey folderKey = new ProjectResourceKey(key.getResourceType(),parentPath);
			UUID uuid = mainFolders.get(folderKey);
			if( uuid==null ) uuid = returnFolderForResource(main,res.getModuleId(),folderKey,mainFolders);
			if( uuid!=null ) {
				res.setParentUuid(uuid);
				try {
					main.putResource(res);
					log.infof("%s.mergeProject: Update %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),key.getResourcePath());
				}
				catch(Exception ex) {
					log.errorf("%s.mergeProject: EXCEPTION %s for %s (%s)",CLSS,ex.getMessage(),parentPath,res.getResourceType());
				}
			}
			else {
				log.errorf("%s.mergeProject: ERROR unable to create parent folder for %s (%s)",CLSS,parentPath,res.getResourceType());
			}
		}
	}
	
	private UUID returnFolderForResource(Project proj,String moduleId,ProjectResourceKey leaf, Map<ProjectResourceKey,UUID> folderMap) {
		log.infof("%s.returnFolderForResource: leaf (%s) at %s",CLSS, leaf.getResourceType(),leaf.getResourcePath());
		Stack<ProjectResourceKey> stack = searchKeysForAncestor(leaf,folderMap);
		// The first key should exist, the rest not so much
		ProjectManager pmgr = getContext().getProjectManager();
		ProjectResourceKey key = stack.pop();
		UUID parent = folderMap.get(key);
		UUID uuid = null;
		if( parent!=null ) {
			while(!stack.isEmpty()) {
				key = stack.pop();
				log.infof("%s.returnFolderForResource: Popped to create %s at %s",CLSS,key.getResourceType(),key.getResourcePath());
				try {
					long resid = pmgr.getNewResourceId().longValue();
					uuid = UUID.randomUUID();
					byte[] data = asBytes(uuid);
					String name = getNameFromPath(key.getResourcePath());
					ProjectResource folder = new ProjectResource(resid,moduleId,ProjectResource.FOLDER_RESOURCE_TYPE,name,ApplicationScope.ALL,data);
					folder.setParentUuid(parent);
					proj.putResource(folder);
					folderMap.put(key, uuid);  
				}
				catch(Exception ex) {
					log.errorf("%s.mergeProject: ERROR unable to create resource id for %s",CLSS,key.getResourcePath());
					break;
				}
			}
		}
		return uuid;
	}
	
	// Push a keys for folders that we must create onto stack. By definition the input node is required.
	private Stack<ProjectResourceKey> searchKeysForAncestor(ProjectResourceKey leaf,Map<ProjectResourceKey,UUID> folderMap) {
		Stack<ProjectResourceKey> stack = new Stack<>();	
		ProjectResourceKey ancestor = leaf;
		String type = ancestor.getResourceType();
		String path = ancestor.getResourcePath();
		while(folderMap.get(ancestor)==null) {
			stack.push(ancestor);
			log.infof("%s.searchKeysForAncestor: pushed %s at %s",CLSS,ancestor.getResourceType(),ancestor.getResourcePath());
			path = getParentPath(path);
			if( path.length() ==0 ) {
				log.errorf("%s.searchKeysForAncestor: ERROR failed to find existing root for %s at %s",type,leaf.getResourcePath()); 
				break;
			}
			ancestor = new ProjectResourceKey(type,path);
		}
		stack.push(ancestor);   // This one can be found
		return stack;
	}
	
	public byte[] asBytes(UUID uuid) {
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    return bb.array();
	}
	// The first argument is a project with a list of standard script resources that must appear in every 
	// project. Here we delete any of the standard resources that would otherwise
	// overwrite resources in the main project.
	// Return the number of scripting resources remaining in the defaults project
	private int scrubDefaultsProject(Project defaultsProject,Project main) {
		Map<ProjectResourceKey,ProjectResource> mainResources = new HashMap<>();
		// Create map of resources.
		for( ProjectResource res:main.getResourcesOfType("",InstallerConstants.SCRIPT_RESOURCE) ) {
			String path = main.getFolderPath(res.getResourceId());
			ProjectResourceKey key = new ProjectResourceKey(res.getResourceType(),path);
			log.debugf("%s.scrubDefaultsProject: Main %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),path);
			mainResources.put(key, res);
		}
		// Delete any resources in the default project that are already in the main or are not scripting
		List<ProjectResource> toBeDeleted = new ArrayList<>();
		int count = 0;
		for( ProjectResource res:defaultsProject.getResources() ) {
			String path = defaultsProject.getFolderPath(res.getResourceId());
			// Don't delete folders from the original as deleting the folder orphans its children
			ProjectResourceKey key = new ProjectResourceKey(res.getResourceType(),path);
			if( mainResources.get(key)!=null && !res.getResourceType().equals(ProjectResource.FOLDER_RESOURCE_TYPE) ) {
				log.debugf("%s.scrubDefaultsProject: Defaults deleting %s (%s) as %s",CLSS,res.getResourceType(),res.getName(),path);
				toBeDeleted.add(res);
			}
			else if( !res.getResourceType().equals(ProjectResource.FOLDER_RESOURCE_TYPE) ){
				log.infof("%s.scrubDefaultsProject: Defaults adding %s (%s) at %s",CLSS,res.getResourceType(),res.getName(),path);
				count++;
			}
		}
		for( ProjectResource res:toBeDeleted ) {
			defaultsProject.deleteResource(res.getResourceId());
		}
		return count;
	}
	// Update the table of folder UUIDs by resource type and path.
	// Update towards root as necessary. Path includes trailing, but not leading /.
	private void updateFolderMap(Project proj, Map<ProjectResourceKey,UUID> folderMap, ProjectResource child) {
		String path = getParentPath(proj.getFolderPath(child.getResourceId()));
		String type = child.getResourceType();
		ProjectResourceKey key = new ProjectResourceKey(type,path);
		while( folderMap.get(key) == null ) {
			UUID uuid = child.getParentUuid();
			if( uuid ==null ) break;
			ProjectResource folder = proj.findFolder(uuid);
			if( folder==null ) break;
			folderMap.put(key, uuid);
			//log.infof("%s.updateFolderMap: Added folder for type %s at %s",CLSS,type,path);
			child = folder;
			path = getParentPath(path);
			key = new ProjectResourceKey(type,path);
		}	
	}
	
	// Alter a project description to add its derivation
	// Replace anything after a double dash
	private String updateProjectDescription(String desc,InstallerData data) {
		int pos = desc.indexOf("--");
		if( pos>0 ) desc = desc.substring(0,pos);
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

