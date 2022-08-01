/**
 *   (c) 2016-2017  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

/**
 * This enumeration contains names of our repertoire of panel 
 * classes for the application installer.
 */
public enum PanelType
{
			AUTHENTICATION,
			BACKUP,
			CLEAR,
            DATABASE,
            DATABASE_DROPDOWN,
            DEFINITION,
            DOCUMENTATION,
            FEATURE, 
            FILE, 
            ICON, 
            LICENSE,
            MODULE,         //
            PROPERTY, 
            PROJECT,       // Replacement or new
            PYTHON,
            SCANCLASS,
            SCANCLASS_DROPDOWN,
            SITE,
            SOURCE,
            SUMMARY,
            TAG,
            TAG_DROPDOWN,
            TOOLKIT,
            WELCOME,
            WELCOME_WITHOUT_CHECKBOXES
            ;
   
 /**
  * @return  a comma-separated list of all step types in a single String.
  */
  public static String names()
  {
    StringBuffer names = new StringBuffer();
    for (PanelType type : PanelType.values()) {
      names.append(type.name()+", ");
    }
    return names.substring(0, names.length()-2);
  }
}
