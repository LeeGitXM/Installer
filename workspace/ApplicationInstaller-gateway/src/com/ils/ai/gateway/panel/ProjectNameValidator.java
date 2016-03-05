/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway.panel;

import java.util.List;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.ils.ai.gateway.ApplicationInstallerGatewayHook;
import com.inductiveautomation.ignition.common.project.Project;
import com.inductiveautomation.ignition.common.project.ProjectVersion;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;


/**
 * Validate that the supplied name does not already exist as a current project.
 * To use: textfield.add(new ProjectNameValidator(context));
 */
public class ProjectNameValidator implements IValidator<String> {
	private static final long serialVersionUID = 6930153148651712890L;


	
	


	public ProjectNameValidator() {
	}

	/**
	 * See if the 
	 * @param validatable
	 */
	@Override
	public void validate(IValidatable<String> validatable) {
		GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
		// getProjectsLite does not include resources with the project
		List<Project> projects = context.getProjectManager().getProjectsLite(ProjectVersion.Staging);
		String name = validatable.getValue();
		for(Project project:projects) {
			System.out.println(String.format("ProjectNameValidator.validate %s vs %s",name,project.getName()));
			if( project.getName().equalsIgnoreCase(name) ) {
				error(validatable,"There is an existing project with this name. Choose another.");
				break;
			}
		}
	}
	
	

	private void error(IValidatable<String> validatable, String errorKey) {
		ValidationError error = new ValidationError();
		error.addKey(validatable.getValue() + ": " + errorKey);
		validatable.error(error);
	}
}
