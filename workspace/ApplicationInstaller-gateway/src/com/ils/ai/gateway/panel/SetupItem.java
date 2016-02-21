package com.ils.ai.gateway.panel;

import java.io.Serializable;

import com.inductiveautomation.ignition.common.project.Project;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class SetupItem implements Serializable {

    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
