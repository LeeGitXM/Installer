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
 * Validate that the supplied name does not already exist as a current project.
 * The normal: textfield.add(new ProjectNameValidator(context)); didn't work for us.
 * We need validation BEFORE, but not AFTER the project is created.
 */
public class ProjectNameValidator  {

	/**
	 * @return an error message in the case of a match with an existing project.
	 *         If there is no problem, then return null.
	 */
	public String validate(TextField<String> validatable) {
		String result =null;
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		// getProjectsLite does not include resources with the project
		List<Project> projects = context.getProjectManager().getProjectsLite(ProjectVersion.Staging);
		String name = validatable.getValue();
		for(Project project:projects) {
			System.out.println(String.format("ProjectNameValidator.validate %s vs %s",name,project.getName()));
			if( project.getName().equalsIgnoreCase(name) ) {
				result = "There is an existing project with this name. Choose another.";
				break;
			}
		}
		return result;
	}
}
