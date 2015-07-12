package com.lge.lai.common.db.dto;

public class AsbUri extends Asb {
    public String uri;
    public String uriDesc;

    public AsbUri(String version, String type, String desc, String packageName, String className,
            String actionName, String uri, String uriDesc, String updatedBy) {
        super(version, type, desc, packageName, className, actionName, updatedBy);
        this.uri = uri;
        this.uriDesc = uriDesc;
    }
}
