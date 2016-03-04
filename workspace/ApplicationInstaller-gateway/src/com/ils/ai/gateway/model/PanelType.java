/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

/**
 * This enumeration contains names of our repertoire of panel 
 * classes for the application installer.
 */
public enum PanelType
{
            BACKUP,
            CONCLUSION,
            DATABASE,
            EXTERNAL,       // Python, jar files
            ICONS, 
            LICENSE,
            MODULE,         // 
            PROPERTIES, 
            PROJECT,       // Replacement or global
            SCANCLASS,
            SOURCE,
            TAGS,
            TOOLKIT,
            TRANSACTIONGROUPS,
            WELCOME
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
