/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *   @See http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
 */
package com.ils.ai.gateway.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;

/**
 * A class with some utility methods used by the Application Installer.
 * These deal with XML files. These methods 
 * are typically designed to return an error string, where a null implies success.
 */
public class XMLUtility {
	private final String CLSS = "XMLUtility";
	private final LoggerEx log;

	public XMLUtility() {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
	}
	
	public Document documentFromBytes(byte[] bytes) {
		Document xml = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    try {
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	xml = builder.parse(new ByteArrayInputStream(bytes));
	    }
	    catch(ParserConfigurationException pce) {
	    	log.warnf("%s.documentFromBytes: Failed to create builder (%s)",CLSS,pce.getLocalizedMessage());
	    }
	    catch(SAXException saxe) {
	    	log.warnf("%s.documentFromBytes: Illegal XML document (%s)",CLSS,saxe.getLocalizedMessage());
	    }
	    catch(IOException ioe) {
	    	log.warnf("%s.documentFromBytes: IOException parsing XML (%s)",CLSS,ioe.getLocalizedMessage());
	    }
	    
	    return xml;
	}
	
	// =========================  Helper Functions ==============================
	public String attributeValue(Node element,String name) {
		String value = "";
		NamedNodeMap attributes = element.getAttributes();
		Node node = attributes.getNamedItem(name);
		if( node!=null ) value = node.getNodeValue();
		return value;
	}

}