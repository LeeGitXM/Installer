/**
 *   (c) 2016  ILS Automation. All rights reserved. 
 */
package com.ils.ai.gateway.model;

/**
 * This enumeration contains names of our repertoire of panel 
 * classes for the application installer.
 */
public enum WizardStepType
{
            BACKUP,
            CONCLUSION,
            DATABASE,
            EXTERNAL,       // Python, jar files
            GLOBAL,         // Global project
            ICONS, 
            LICENSE,
            MODULE,         // 
            PROPERTIES, 
            PROJECT,       // Replacement or global
            SCANCLASS,
            SOURCE,
            TAGS,
            TOOLIT,
            TRANSACTIONGROUPS,
            WELCOME
            ;
   
 /**
  * @return  a comma-separated list of all step types in a single String.
  */
  public static String names()
  {
    StringBuffer names = new StringBuffer();
    for (WizardStepType type : WizardStepType.values()) {
      names.append(type.name()+", ");
    }
    return names.substring(0, names.length()-2);
  }
}
