package com.ils.ai.gateway.utility;

import java.io.File;

import org.python.core.PyInteger;
import org.python.core.PyString;
import org.python.core.PyStringMap;

import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/** 
 * Given an XML file that is an export of a TransactionGroup, 
 * create the group.
 * 
 * @See 
 */
public class TransactionGroupUtility{
	GatewayContext context = null;
	final PyStringMap pyLocals;
    final PyStringMap pyGlobals;
	
	public TransactionGroupUtility(GatewayContext ctx) {
		this.context = ctx;
		this.pyLocals = context.getScriptManager().createLocalsMap();
        this.pyGlobals = context.getScriptManager().getGlobals();
	}

	public void importFromFile(File file,String projectName) throws Exception {
		pyLocals.__setitem__("groupFileName", new PyString(file.toPath().toString()));
        pyLocals.__setitem__("groupProject", new PyString(projectName));
        pyLocals.__setitem__("groupMode", new PyInteger(0));
        String code = "system.groups.loadFromFile(groupFileName, groupProject, groupMode)";
        context.getScriptManager().runCode(code, pyLocals, pyGlobals, "SDKCode");
	}    
}
