package com.ils.ai.gateway.utility;

import java.io.File;

import org.python.core.PyString;
import org.python.core.PyStringMap;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/** 
 * Given a JSON or XML file that is an export of a Tag collection, 
 * create the tags. Unfortunately a GatewayContext doesn't
 * have a method to get a TypeManager.
 * 
 * @See 
 */
public class TagUtility{
	private final String CLSS = "TagUtility";
	private final LoggerEx log;
	GatewayContext context = null;
	final PyStringMap pyLocals;
    final PyStringMap pyGlobals;
	
	public TagUtility(GatewayContext ctx) {
		this.context = ctx;
		this.pyLocals = context.getScriptManager().createLocalsMap();
        this.pyGlobals = context.getScriptManager().getGlobals();
        this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}

	// Import tag definitions from a json or xml file.
	// The base is usually "[default]" (depending on how the tag file was exported).
	public void importTagsFromFile(File file,String base) throws Exception {
		pyLocals.__setitem__("filePath", new PyString(file.toPath().toString()));
        pyLocals.__setitem__("basePath", new PyString(base));
        pyLocals.__setitem__("collisionPolicy", new PyString("o"));   // Overwrite
        String code = "system.tag.importTags(filePath, basePath, collisionPolicy)";
        log.info(String.format("%s.importTagsFromFile:%s %s to %s" , CLSS,code,file.toPath().toString(),base));
        context.getScriptManager().runCode(code, pyLocals, pyGlobals, "SDKCode");
	}    
}
