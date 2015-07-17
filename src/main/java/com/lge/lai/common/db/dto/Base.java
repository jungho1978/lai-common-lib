package com.lge.lai.common.db.dto;

import java.sql.Timestamp;

public class Base {
    public String versionName;
    public String type;
    public String desc;
    public String packageName;
    public String updatedBy;
    public String manifestPath;
    public Timestamp ctime;

    public Base(String versionName, String type, String desc, String packageName, String updatedBy) {
        this.versionName = versionName;
        this.type = type;
        this.desc = desc;
        this.packageName = packageName;
        this.updatedBy = updatedBy;
    }
    
    public void setManifestPath(String path) {
        this.manifestPath = path;
    }
}
