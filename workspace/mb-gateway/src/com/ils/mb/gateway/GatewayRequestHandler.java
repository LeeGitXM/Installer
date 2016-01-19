/**
 *   (c) 2016  ILS Automation. All rights reserved.  
 */
package com.ils.mb.gateway;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import com.ils.common.db.DBUtility;
import com.ils.mb.common.MasterBuilderProperties;
import com.ils.mb.common.MasterBuilderScriptingInterface;
import com.inductiveautomation.ignition.common.model.ApplicationScope;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.clientcomm.GatewaySessionManager;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/**
 *  This class is a common point for managing requests in the gateway dealing with the
 *  master builders. It is designed for use by Java code in the  
 *  Gateway RPC handler, as well as Python scripting.
 *  
 *  Unlike its counterpart class in the Designer/Client scopes, this class accesses
 *  methods without RPC calls.
 */
public class GatewayRequestHandler implements MasterBuilderScriptingInterface {
	private final static String TAG = "GatewayRequestHandler";
	private final LoggerEx log;
	private final GatewayContext context;
	private final DBUtility dbUtil;
	private final FileUtility fileUtil;
	private final JarUtility jarUtil;
	private final Preferences prefs;
	private final GatewaySessionManager sessionManager;
	/**
	 * Constructor:
	 */
	public GatewayRequestHandler(GatewayContext ctx)  {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.context = ctx;
		this.dbUtil = new DBUtility(context);
		this.fileUtil = new FileUtility(context);
		this.jarUtil = new JarUtility(context);
		this.prefs = Preferences.userRoot().node(MasterBuilderProperties.PREFERENCES_NAME);
		this.sessionManager = context.getGatewaySessionManager();
	}

	// =============================== Master Builder Interface ===============================
	/**
	 * Copy a file. Retain permissions.
	 * @param source full path for the source file.
	 * @param destination full path for the destination file.
	 */
	public void copyFile(String source,String destination) {
		// In case we've been fed a Windows path, convert
		// We're expecting an absolute path.
		source = source.replace("\\", "/");
		destination = destination.replace("\\", "/");
		String status = fileUtil.copyFile(source,destination);
		if( status==null ) pushStatus(String.format("Created %s",destination),true);
		else pushStatus(status,false);
		
	}
	/**
	 * Clear the destination directory, then copy the contents of the 
	 * MasterBuilder module into it. The MasterBuilder contents are read
	 * from the Ignition installation area. 
	 * 
	 * @param destinationPath directory to be created as a valid Ignition module.
	 */
	@Override
	public void copyMasterToDirectory(String destination) {
		log.infof("%s.copyMasterToDirectory: Copying to ... %s",TAG,destination);
		String status = null;
		// In case we've been fed a Windows path, convert
		// We're expecting an absolute path.
		destination = destination.replace("\\", "/");
		
		Path path = null;
		// Clear the destination
		try {
			path = Paths.get(destination);
			fileUtil.deleteDirectory(path);  // Delete directory and contents
			// Create the destination directory
			try {
				Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxr-x");
				FileAttribute<Set<PosixFilePermission>> attrs =PosixFilePermissions.asFileAttribute(perms);
				Files.createDirectory(path, attrs);
				// Find the master builder module (indicated by an internal marker - name can vary)
				Path internalPath = jarUtil.internalModuleContaining(".master-builder");
				if( internalPath!=null ) {
					jarUtil.unJarToDirectory(internalPath, path);
					// Finally delete the marker file from the destination
					Path markerPath = Paths.get(path.toString(), ".master-builder");
					Files.delete(markerPath);
				}
				else {
					status = String.format("%s.copyMasterToDirectory: Master-builder jar not found in Ignition lib area",TAG);
					log.infof(status);
				}
			}
			catch(UnsupportedOperationException uoe) {
				status = String.format("%s.copyMasterToDirectory: Creation of %s unsupported (%s)",TAG,destination,uoe.getLocalizedMessage());
				log.info(status);
			}
			catch(FileAlreadyExistsException faoe) {
				status = String.format("%s.copyMasterToDirectory: Programming error - %s not deleted (%s)",TAG,destination,faoe.getLocalizedMessage());
				log.info(status);
			}
			catch(IOException ioe) {
				status = String.format("%s.copyMasterToDirectory: Unable to delete %s (%s)",TAG,destination,ioe.getLocalizedMessage());
				log.info(status);
			}
			catch(SecurityException se) {
				status = String.format("%s.copyMasterToDirectory: Permission problem creating %s (%s)",TAG,destination,se.getLocalizedMessage());
				log.info(status);
			}
		}
		catch( InvalidPathException ipe) {
			status = String.format("%s.copyMasterToDirectory: Supplied path (%s) is invalid (%s)",TAG,destination,ipe.getLocalizedMessage());
			log.info(status);
		}
		catch( IOException ioe) {
			status = String.format("%s.copyMasterToDirectory: Unable to delete ... %s (%s)",TAG,destination,ioe.getLocalizedMessage());
			log.info(status);
		}

		
		if( status==null ) pushStatus(String.format("Copied master builder module to %s",destination),true);
		else pushStatus(status,false);
	}
	/**
	 * Create a .modl file from the contents of a specified directory. 
	 * Note: A .modl file is simply a .jar file.
	 * @param sourceDirectory pre-existing directory containing contents
	 *        of the module file.
	 * @param destinationPath file to be created as a valid Ignition module.
	 */
	@Override
	public void createInstallerModule(String sourceDirectory,String destinationPath) {
		// Run in the background
		IgnitionModule module = new IgnitionModule(this,sourceDirectory,destinationPath);
		new Thread(module).start();
	}
	/**
	 * @return a list of the names of currently connected data sources.
	 */
	@Override
	public List<String> getDatabaseNames() {
		return dbUtil.getDatasourceNames();
	}
	/**
	 * @return the value of a Java preference used by the framework.
	 *         Execute this locally.
	 */
	@Override
	public String getPreference(String key) {
		return prefs.get(key,"");
	}
	/**
	 * @return a list of the names of projects currently loaded into the Gateway.
	 */
	@Override
	public List<String> getProjectNames() {
		List<String> result = new ArrayList<>();
		List<Project> projects = context.getProjectManager().getProjectsFull(ProjectVersion.Staging);
		for( Project proj:projects) {
			result.add(proj.getName());
		}
		return result;
	}
	/**
	 * Set the value of a Java preference used by the master builder.
	 * @param the value of a Java preference used by the builder.
	 */
	@Override
	public void setPreference(String key,String value) {
		prefs.put(key,value);
	}
	
	/**
	 * With each request we push notify the client/designer with the results
	 */
	public void pushStatus(String message,boolean success) {
		String key = MasterBuilderProperties.SUCCESS_NOTIFICATION;
		if( !success ) key = MasterBuilderProperties.FAIL_NOTIFICATION;
		message = "  Status: "+ message;
		// Assume any exception is a systemic thing ...
		try {
			sessionManager.sendNotification(ApplicationScope.DESIGNER,MasterBuilderProperties.MODULE_ID,
					key,message);
		}
		catch( Exception ex) {
			log.warnf("%s.pushStatus: Exception sending notification %s (%s)",TAG,message,ex.getMessage());
		}
	}
	
	@Override
	public void stringToFile(String text,String destinationPath) {
		fileUtil.stringToFile(text, destinationPath);
	}
}
