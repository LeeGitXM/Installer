/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.markup.html.form.TextField;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;


/**
 * Find an unused project name given a root name that is a current project.
 * The 
 */
public class ProjectNameFinder  {

	/**
	 * @return an error message in the case of a match with an existing project.
	 *         If there is no problem, then return null.
	 */
	public String findUnused(String currentName) {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		// getProjectsLite does not include resources with the project
		List<Project> projects = context.getProjectManager().getProjectsLite(ProjectVersion.Staging);
		int index = 0;
		String proposed = currentName;
		for(;;) {
			boolean found = false;
			for(Project project:projects) {
				if( project.getName().equalsIgnoreCase(proposed) ) {
					found = true;
					proposed = proposed+nextSuffix(index);
					break;
				}
			}
			if( !found ) break;
			index++;
		}
		return proposed;
	}
	
	private String nextSuffix(int index) {
		String suffix = "_bak";
		if( index>0)  {
			suffix = String.format("_bak%d", index);
		}
		return suffix;
	}
}
