package com.ils.ai.gateway.utility;

import java.io.File;

import com.inductiveautomation.ignition.common.gui.progress.TaskProgressListener;
import com.inductiveautomation.ignition.common.sqltags.importexport.TagImporter;
import com.inductiveautomation.ignition.common.sqltags.importexport.TagImporterFactory;
import com.inductiveautomation.ignition.common.sqltags.model.udt.TypeManager;
import com.inductiveautomation.ignition.gateway.sqltags.execution.ComplexTypeManager;

/** Given an XML file that is an export of a Tag collection, 
 * create the tags.
 * 
 * @See 
 */
public class TagUtility{

	public void listFromFile(File file,TaskProgressListener listener) throws Exception {
		TypeManager typeManager = new ComplexTypeManager();
		TagImporter importer = TagImporterFactory.createFor(file, typeManager);
		importer.runImport(listener);
	}
	     
}
