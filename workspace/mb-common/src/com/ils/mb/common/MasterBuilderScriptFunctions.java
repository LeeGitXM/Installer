/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.common;

import com.ils.mb.common.MasterBuilderRequestHandler;


/**
 *  This class exposes the methods available to a designer/client scope for the
 *  Master Builder. It supports "in spirit", the MasterBuilderScriptingInterface. 
 *  
 */
public class MasterBuilderScriptFunctions   {

	private static MasterBuilderRequestHandler handler = new MasterBuilderRequestHandler();
	
	// =============================== Master Builder Functions ==============================
	/**
	 * No-op.
	 */
	public static void nop( ) {
		handler.nop();
	}
}