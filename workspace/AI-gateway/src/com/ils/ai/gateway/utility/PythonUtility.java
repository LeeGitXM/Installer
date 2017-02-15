/**
 * Copyright 2017 ILS Automation. All rights reserved.
 */
 package com.ils.ai.gateway.utility;


import org.python.core.CompileMode;
import org.python.core.CompilerFlags;
import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyObject;
import org.python.core.PyStringMap;

import com.inductiveautomation.ignition.common.script.JythonExecException;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;

/** 
 * Execute the supplied python module. We compile every time as
 * there is little likelihood of the module being run twice.
 * 
 * We require that the method return an empty string on success and an
 * error message on failure.
 */
public class PythonUtility{
	private static final String RESULT_NAME = "pyCallResult";
	
	private final GatewayContext context;
	private final Class<?> returnType = String.class;
	
	public PythonUtility(GatewayContext ctx) {
		this.context = ctx;
	}

	/**
	 * Construct import and executable path from method name. The method name
	 * may optionally include parentheses. Path may be specified with either dots or slashes.
	 * There is no return expected. Bad results are gleaned from the exception.
	 * @param methodName
	 * @throws JythonExecException
	 */
	public void execute(String methodName) throws JythonExecException {
		methodName = methodName.replace("/", ".");
		StringBuffer buf = new StringBuffer();
		int pindex = methodName.lastIndexOf("(");
		if( pindex>0 ) methodName = methodName.substring(0,pindex);   // Strip parentheses.
		
		int dotIndex = methodName.lastIndexOf(".");
		if(dotIndex != -1) {
			buf.append("import ");
			buf.append(methodName.substring(0, dotIndex));
			buf.append("; ");
		}
		buf.append(methodName);
		buf.append("()");  // No arguments

		String script = buf.toString();
		System.out.println(String.format("PythonUtility.execute %s",script));
		PyCode compiledCode = Py.compile_flags(script, "ils", CompileMode.exec, CompilerFlags.getCompilerFlags());
		ScriptManager scriptManager = context.getScriptManager();
		PyStringMap pyLocals = scriptManager.createLocalsMap();
	    PyStringMap pyGlobals= scriptManager.getGlobals();
	    scriptManager.runCode(compiledCode, pyLocals, pyGlobals);
	}
	
	/**
	 * Construct import and executable path from method name. The method name
	 * may optionally include parentheses. Path may be specified with either dots or slashes.
	 * The python method takes a single boolean argument:  "flag". There is no return value.
	 * @param methodName
	 * @param flag user response, true or false
	 * @throws JythonExecException
	 */
	public void processFlag(String methodName,boolean flag) throws JythonExecException {
		methodName = methodName.replace("/", ".");
		StringBuffer buf = new StringBuffer();
		int pindex = methodName.lastIndexOf("(");
		if( pindex>0 ) methodName = methodName.substring(0,pindex);   // Strip parentheses.
		
		int dotIndex = methodName.lastIndexOf(".");
		if(dotIndex != -1) {
			buf.append("import ");
			buf.append(methodName.substring(0, dotIndex));
			buf.append("; ");
		}
		buf.append(methodName);
		buf.append("(flag)");  // Single arguments

		String script = buf.toString();
		System.out.println(String.format("PythonUtility.processFlag %s",script));
		PyCode compiledCode = Py.compile_flags(script, "ils", CompileMode.exec, CompilerFlags.getCompilerFlags());
		ScriptManager scriptManager = context.getScriptManager();
		PyStringMap pyLocals = scriptManager.createLocalsMap();
		pyLocals.__setitem__("flag", Py.java2py(flag));
		
	    PyStringMap pyGlobals= scriptManager.getGlobals();
	    scriptManager.runCode(compiledCode, pyLocals, pyGlobals);
	} 
	
	/**
	 * Construct import and executable path from method name. The method name
	 * may optionally include parentheses. Path may be specified with either dots or slashes.
	 * The python method takes a single string arguments: "value". There is no return expected.
	 * @param methodName
	 * @param value name of the property
	 * @throws JythonExecException
	 */
	public void updateValue(String methodName,String value) throws JythonExecException {
		methodName = methodName.replace("/", ".");
		StringBuffer buf = new StringBuffer();
		int pindex = methodName.lastIndexOf("(");
		if( pindex>0 ) methodName = methodName.substring(0,pindex);   // Strip parentheses.
		
		int dotIndex = methodName.lastIndexOf(".");
		if(dotIndex != -1) {
			buf.append("import ");
			buf.append(methodName.substring(0, dotIndex));
			buf.append("; ");
		}
		buf.append(methodName);
		buf.append("(value)");  // Single argument

		String script = buf.toString();
		System.out.println(String.format("PythonUtility.updateValue %s(%s)",script,value));
		PyCode compiledCode = Py.compile_flags(script, "ils", CompileMode.exec, CompilerFlags.getCompilerFlags());
		ScriptManager scriptManager = context.getScriptManager();
		PyStringMap pyLocals = scriptManager.createLocalsMap();
		pyLocals.__setitem__("value", Py.java2py(value));
		
	    PyStringMap pyGlobals= scriptManager.getGlobals();
	    scriptManager.runCode(compiledCode, pyLocals, pyGlobals);
	}  
}
