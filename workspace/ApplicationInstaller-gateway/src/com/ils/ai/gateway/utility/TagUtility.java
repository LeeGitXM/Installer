package com.ils.ai.gateway.utility;

import java.io.File;

import com.inductiveautomation.ignition.common.gui.progress.TaskProgressListener;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/** 
 * Given an XML file that is an export of a Tag collection, 
 * create the tags. Unfortunately a GatewayContext doesn't
 * have a method to get a TypeManager.
 * 
 * @See 
 */
public class TagUtility{
	GatewayContext context = null;
	
	public TagUtility(GatewayContext ctx) {
		this.context = ctx;
	}

	public void listFromFile(File file,String source,TaskProgressListener listener) throws Exception {
		//TypeManager typeManager = context.getTagManager().getTypeManager(source);
		//TagImporter importer = TagImporterFactory.createFor(file, typeManager);
		//importer.runImport(listener);
	}
	     
}
