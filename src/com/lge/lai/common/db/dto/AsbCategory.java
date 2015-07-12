package com.lge.lai.common.db.dto;

import com.google.common.collect.ComparisonChain;


public class AsbCategory extends Asb {
	public String category;
	
	public AsbCategory(String version, String type, String desc, String packageName,
			String className, String actionName, String category,
			String updatedBy) {
		super(version, type, desc, packageName, className, actionName, updatedBy);
		this.category = category;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof AsbCategory) {
	        AsbCategory other = (AsbCategory)obj;
	        return ComparisonChain.start()
	                .compare(version, other.version)
	                .compare(type, other.type)
	                .compare(packageName, other.packageName)
	                .compare(className, other.className)
	                .compare(actionName, other.actionName)
	                .compare(category, other.category)
	                .result() == 0;
	    } else {
	        return false;
	    }
	}
}
