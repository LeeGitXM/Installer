package com.ils.ai.gateway.model;

import java.io.Serializable;


//====================================== ProjectResourceKey =================================
/**
 * Class for keyed storage by resourceType, folderPath. We use these two values as a key
 * for replacement by one project for another.
 */
public class ProjectResourceKey implements Serializable {
	private static final long serialVersionUID = 6331391411649992726L;
	private String resourceType;
	private String resourcePath;
	
	/**
	 * Constructor: No arg version required for serialization.
	 */
	public ProjectResourceKey() {
	}

	public ProjectResourceKey(String type,String path) {
		this.resourceType = type;
		this.resourcePath = path;
	}
	public String getResourceType() { return resourceType; }
	public String getResourcePath() { return resourcePath; }
	public void setResourceType(String type) { this.resourceType=type; }
	public void setResourcePath(String path) { this.resourcePath=path; }

	// So that class may be used as a map key
	// Same projectId and resourceId is sufficient to prove equality
	@Override
	public boolean equals(Object arg) {
		boolean result = false;
		if( arg instanceof ProjectResourceKey) {
			ProjectResourceKey that = (ProjectResourceKey)arg;
			if( (this.getResourceType().equals(that.getResourceType())) &&
					(this.getResourcePath().equals(that.getResourcePath()))   ) {
				result = true;
			}
		}
		return result;
	}
	@Override
	public int hashCode() {
		return resourcePath.hashCode()+resourceType.hashCode();
	}
}
