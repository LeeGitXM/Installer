package com.ils.ai.gateway.model;

import java.io.File;

import com.inductiveautomation.ignition.common.gui.progress.TaskProgressListener;

public interface TempFileTaskProgressListener extends TaskProgressListener {
	public void setTempFile(File file);
}
