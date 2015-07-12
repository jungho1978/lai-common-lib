package com.lge.lai.common.db.dto;

import com.google.common.collect.ComparisonChain;

public class AsbUri extends Asb {
    public String uri;
    public String uriDesc;

    public AsbUri(String version, String type, String desc, String packageName, String className,
            String actionName, String uri, String uriDesc, String updatedBy) {
        super(version, type, desc, packageName, className, actionName, updatedBy);
        this.uri = uri;
        this.uriDesc = uriDesc;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AsbUri) {
            AsbUri other = (AsbUri)obj;
            return ComparisonChain.start()
                    .compare(version, other.version)
                    .compare(type, other.type)
                    .compare(packageName, other.packageName)
                    .compare(className, other.className)
                    .compare(actionName, other.actionName)
                    .compare(uri, other.uri)
                    .result() == 0;
        } else {
            return false;
        }
    }
}
