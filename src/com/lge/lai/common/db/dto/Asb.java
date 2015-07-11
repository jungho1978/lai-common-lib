package com.lge.lai.common.db.dto;

public class Asb extends Base {
	public String className;
	public String actionName;

	public Asb(String version, String type, String packageName,
			String className, String actionName, String updatedBy) {
		super(version, type, packageName, updatedBy);
		this.className = className;
		this.actionName = actionName;
	}
}
