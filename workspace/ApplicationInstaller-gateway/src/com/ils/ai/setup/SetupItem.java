package com.ils.ai.setup;

import com.inductiveautomation.ignition.common.project.Project;

import java.io.Serializable;

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
