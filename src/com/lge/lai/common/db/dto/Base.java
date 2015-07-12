package com.lge.lai.common.db.dto;

import java.sql.Timestamp;

public class Base {
	public String version;
	public String type;
	public String desc;
	public String packageName;
	public String updatedBy;
	public Timestamp ctime;
	
	public Base(String version, String type, String desc, String packageName, String updatedBy) {
		this.version = version;
		this.type = type;
		this.desc = desc;
		this.packageName = packageName;
		this.updatedBy = updatedBy;
	}
}
