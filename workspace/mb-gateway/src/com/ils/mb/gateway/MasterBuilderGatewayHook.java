/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.mb.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
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
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistenceSession;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordMeta;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.project.ProjectManager;
import com.inductiveautomation.ignition.gateway.project.records.ProjectChangeRecord;
import com.inductiveautomation.ignition.gateway.project.records.ProjectRecord;
import com.inductiveautomation.ignition.gateway.project.records.ProjectResourceRecord;
import com.inductiveautomation.ignition.gateway.util.RecordInstanceForeignKey;
import com.inductiveautomation.ignition.gateway.web.models.KeyValue;

import simpleorm.dataset.SRecordInstance;


/**
 * This is root node for specialty code dealing with the gateway. On startup
 * we obtain the gateway context. It serves as our entry point into the
 * Ignition core.
 */
public class MasterBuilderGatewayHook extends AbstractGatewayModuleHook   {
	public static String TAG = "MasterBuilderGatewayHook";
	private static final String PROJECT_PATH = "project/MasterBuilder.proj"; // Relative to bundle
	private transient MasterBuilderRpcDispatcher dispatcher = null;
	private transient GatewayContext context = null;
	private final LoggerEx log;
	private Project internalProject = null;
	private ProjectManager pmgr = null;
	private GatewayRequestHandler requestHandler = null;
	
	public MasterBuilderGatewayHook() {
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

		// These are all useful for paths into the installation area,
		//log.infof("HOOK: home directory: %s", context.getHome().getAbsolutePath());   // data
		//log.infof("HOOK: lib directory: %s", context.getLibDir().getAbsolutePath());  // lib
		//log.infof("HOOK: log directory: %s", context.getLogsDir().getAbsolutePath());  // log
		
		Thread runner = new Thread((new ProjectCreator()));
		runner.start();
	}

	@Override
	public void shutdown() {
		if( internalProject!=null ) {
			Thread runner = new Thread((new ProjectRemover()));
			runner.start();
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
		mgr.addScriptModule(MasterBuilderProperties.SCRIPT_PACKAGE,GatewayScriptFunctions.class);
	}
	
	// ======================== Create Project =============================
	/**
	 * Load the project that is supposed to exist at the specified path 
	 * relative to the installed module.
	 */
	private class ProjectCreator implements Runnable {
		public void run() {
			JarUtility jarUtil = new JarUtility(context);
			Path internalPath = jarUtil.internalModuleContaining(".master-builder");
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

	// ======================== Delete Project =============================
	/**
	 * Delete our internal project by deleting the persistent records.
	 */
	private class ProjectRemover implements Runnable {
		public void run() {
			Long projectId = new Long(internalProject.getId());
			PersistenceSession session = context.getPersistenceInterface().getSession();
			ProjectRecord toRemove = session.find(ProjectRecord.META,projectId);

			// Close the session, as another session needs to be opened for the foreign key check
			session.close();

			// First, see if the project can be deleted and that there are no foreign key references.
			List<RecordInstanceForeignKey> referencing = context.getSchemaUpdater().findReferencingRecords(toRemove);
			StringBuilder sb = new StringBuilder();
			for (RecordInstanceForeignKey rfk : referencing) {
				SRecordInstance res = rfk.getRecord();
				RecordMeta<?> meta = (RecordMeta<?>) res.getMeta();

				// Project resource records and project change records will be deleted below before the project record
				// is deleted. But any other foreign key should prevent the project record from being deleted.
				if (!ProjectResourceRecord.META.equals(meta) && !ProjectChangeRecord.META.equals(meta)) {
					String itemTypeName = meta.getTableName();
					String itemName = RecordMeta.getRecordNameIfExists(rfk);
					sb.append(itemTypeName).append(":");
					sb.append(itemName).append(";");
				}
			}
			if (sb.length() > 0) {
				log.warnf("%s.ProjectRemover: Unable to delete project due to presence of foreign keys (%s)",TAG, sb.toString());
			} 
			else {
				// Open the session again
				session = context.getPersistenceInterface().getSession();
				toRemove = session.find(ProjectRecord.META, projectId);

				// Delete associated resource records
				session.rawUpdateDB("DELETE FROM " + ProjectResourceRecord.META.getTableName() + " WHERE "
						+ ProjectResourceRecord.ProjectId.getColumnName() + "=?", projectId);

				// Delete associated project change records
				session.rawUpdateDB(
						"DELETE FROM " + ProjectChangeRecord.META.getTableName() + " WHERE " + ProjectChangeRecord.ProjectId
						.getColumnName() + "=?", projectId);

				toRemove.deleteRecord();
				session.commit();
				session.close();
			}
		}
	}
}

