package com.lge.lai.common.db.dto;

public class AsbUri extends Asb {
	public String uri;
	
	public AsbUri(String version, String type, String packageName,
			String className, String actionName, String uri,
			String updatedBy) {
		super(version, type, packageName, className, actionName, updatedBy);
		this.uri = uri;
	}
}
