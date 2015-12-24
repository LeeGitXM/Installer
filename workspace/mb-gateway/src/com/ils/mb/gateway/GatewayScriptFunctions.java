/**
 *   (c) 2016  ILS Automation. All rights reserved.
 */
package com.ils.mb.gateway;

import com.ils.mb.gateway.GatewayRequestHandler;


/**
 *  This class exposes the methods available to a gateway script for Master Builder
 *  purposes. 
 *  
 *  These methods mimic MasterBuilderScripting, but must be defined as static methods.
 */
public class GatewayScriptFunctions   {

	private static GatewayRequestHandler handler = null;
	
	public static void setRequestHandler(GatewayRequestHandler h) { handler=h; }
	
	// =============================== Master Builder Functions ==============================
	/**
	 * No-op.
	 */
	public static void nop( ) {
		handler.nop();
	}

}