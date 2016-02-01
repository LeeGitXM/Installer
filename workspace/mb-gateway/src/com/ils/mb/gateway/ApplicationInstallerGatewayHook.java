/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.mb.gateway;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.xml.sax.SAXException;

import com.ils.mb.common.MasterBuilderProperties;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistenceInterface;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.project.ProjectManager;
import com.inductiveautomation.ignition.gateway.project.records.ProjectRecord;
import com.inductiveautomation.ignition.gateway.web.models.KeyValue;


/**
 * This version of the builder hook is designed for use with an
 * application installer. It is NOT for use with the master builder.
 */
public class ApplicationInstallerGatewayHook extends AbstractGatewayModuleHook   {
	public static String TAG = "ApplicationInstallerGatewayHook";
	private final String PROJECT_PATH = "project/ApplicationInstaller.proj"; // Relative to bundle
	private transient MasterBuilderRpcDispatcher dispatcher = null;
	private transient GatewayContext context = null;
	private final LoggerEx log;
	private Project internalProject = null;
	private ProjectManager pmgr = null;
	private GatewayRequestHandler requestHandler = null;
	
	public ApplicationInstallerGatewayHook() {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		log.debugf("%s.initializing ...",TAG);
	}
		
	// NOTE: During this period, the module status is LOADED, not RUNNING
	@Override
	public void setup(GatewayContext ctxt) {
		this.context = ctxt;
		this.pmgr = context.getProjectManager();
	}

	@Override
	public void startup(LicenseState licenseState) {

		this.requestHandler = new GatewayRequestHandler(context);
	    this.dispatcher = new MasterBuilderRpcDispatcher(requestHandler);
	    GatewayScriptFunctions.setRequestHandler(requestHandler);
		log.infof("%s.startup: complete.",TAG);
		
		Thread runner = new Thread((new ProjectCreator()));
		runner.start();
	}

	@Override
	public void shutdown() {
		if( internalProject!=null ) {
			// Remove persistent project record
			PersistenceInterface pi = context.getPersistenceInterface();
			ProjectRecord pr = (ProjectRecord)pi.find(ProjectRecord.META, internalProject.getUuid());
			if( pr!=null ) {
				pr.deleteRecord();
				try {
					pi.notifyRecordDeleted(ProjectRecord.META, new KeyValue(pr));
				}
				catch(Exception ex) {
					log.infof("%s.shutdown: Exception deleting project (%s)",TAG,ex.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	public Object getRPCHandler(ClientReqSession session, Long projectId) {
		log.debugf("%s.getRPCHandler - request for project %s",TAG,projectId.toString());
		return dispatcher;
	}
	
	
	@Override
	public void initializeScriptManager(ScriptManager mgr) {
		super.initializeScriptManager(mgr);
		mgr.addScriptModule(MasterBuilderProperties.AI_SCRIPT_PACKAGE,GatewayScriptFunctions.class);
	}
	
	// ======================== Create Project =============================

	/**
	 * Load the project that is supposed to exist at the specified path 
	 * relative to the installed module.
	 */
	private class ProjectCreator implements Runnable {
		public void run() {
			JarUtility jarUtil = new JarUtility(context);
			Path internalPath = jarUtil.internalModuleContaining(".application-installer");
			InputStream projectReader = null;
			JarFile jar = null;
			try {
				
				jar = new JarFile(internalPath.toFile());
				JarEntry entry = jar.getJarEntry(PROJECT_PATH);
				if( entry!=null ) {
					projectReader = jar.getInputStream(entry);
					internalProject = Project.fromXML(projectReader);
					pmgr.addProject(internalProject, true);
				}
			}
			catch(SAXException saxe) {
				log.infof("%s.ProjectCreator: SAX exception creating project (%s)",TAG,saxe.getLocalizedMessage());
			}
			catch(IOException ioe) {
				log.infof("%s.ProjectCreator: IO error reading %s (%s)",TAG,PROJECT_PATH,ioe.getLocalizedMessage());
			}
			catch(Exception ex) {
				log.infof("%s.ProjectCreator: Exception creating project (%s)",TAG,ex.getLocalizedMessage());
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
	}		
}
