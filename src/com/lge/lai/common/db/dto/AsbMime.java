package com.lge.lai.common.db.dto;

public class AsbMime extends Asb {
	public String mimeType;

	public AsbMime(String version, String type, String desc, String packageName,
			String className, String actionName, String mimeType,
			String updatedBy) {
		super(version, type, desc, packageName, className, actionName, updatedBy);
		this.mimeType = mimeType;
	}
}
