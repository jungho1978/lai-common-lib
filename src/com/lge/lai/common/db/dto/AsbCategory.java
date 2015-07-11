package com.lge.lai.common.db.dto;

public class AsbCategory extends Asb {
	public String cateogry;
	
	public AsbCategory(String version, String type, String packageName,
			String className, String actionName, String category,
			String updatedBy) {
		super(version, type, packageName, className, actionName, updatedBy);
		this.cateogry = category;
	}
}
