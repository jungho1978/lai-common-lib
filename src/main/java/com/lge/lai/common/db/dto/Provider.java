package com.lge.lai.common.db.dto;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Provider extends Base {
    public long id;
    public String className;
    public String tableName;
    public String readPermission;
    public String writePermission;
    public String authorities;
    public String primaryKey;

    public Provider(String version, String type, String desc, String packageName, String className,
            String tableName, String readPermission, String writePermission, String authorities,
            String primaryKey, String updatedBy) {
        super(version, type, desc, packageName, updatedBy);
        this.className = className;
        this.tableName = tableName;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.authorities = authorities;
        this.primaryKey = primaryKey;
    }
    
    public Provider(String versionName, String type, String desc, String packageName, String className,
            String tableName, String readPermisson, String writePermission, String authorities,
            String primaryKey, String updatedBy, long id) {
        super(versionName, type, desc, packageName, updatedBy);
        this.id = id;
        this.className = className;
        this.tableName = tableName;
        this.readPermission = readPermisson;
        this.writePermission = writePermission;
        this.authorities = authorities;
        this.primaryKey = primaryKey;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("versionName", versionName)
                .add("type", type)
                .add("desc", desc)
                .add("packageName", packageName)
                .add("className", className)
                .add("tableName", tableName)
                .add("readPermisson", readPermission)
                .add("writePermission", writePermission)
                .add("authorities", authorities)
                .add("prmiaryKey", primaryKey)
                .add("updatedBy", updatedBy)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Provider) {
            Provider other = (Provider)obj;
            return ComparisonChain.start()
                    .compare(versionName, other.versionName)
                    .compare(type, other.type)
                    .compare(packageName, other.packageName)
                    .compare(className, other.className)
                    .compare(tableName, other.tableName)
                    .compare(readPermission, other.readPermission)
                    .compare(writePermission, other.writePermission)
                    .compare(authorities, other.authorities)
                    .compare(primaryKey, other.primaryKey)
                    .result() == 0;
        } else {
            return false;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
