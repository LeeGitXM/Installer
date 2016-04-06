package com.ils.ai.gateway.utility;

import java.io.File;

import org.python.core.PyInteger;
import org.python.core.PyString;
import org.python.core.PyStringMap;

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
	final PyStringMap pyLocals;
    final PyStringMap pyGlobals;
	
	public TagUtility(GatewayContext ctx) {
		this.context = ctx;
		this.pyLocals = context.getScriptManager().createLocalsMap();
        this.pyGlobals = context.getScriptManager().getGlobals();
	}

	public void importFromFile(File file,String source) throws Exception {
		pyLocals.__setitem__("tagFileName", new PyString(file.toPath().toString()));
        pyLocals.__setitem__("tagProviderName", new PyString(source));
        pyLocals.__setitem__("tagMode", new PyInteger(0));
        String code = "system.tag.loadFromFile(tagFileName, tagProviderName, tagMode)";
        context.getScriptManager().runCode(code, pyLocals, pyGlobals, "SDKCode");
	}    
}
