package com.lge.lai.common.db.dto;

import com.google.common.collect.ComparisonChain;

public class AsbMime extends Asb {
    public long id;
    public String mimeType;

    public AsbMime(String version, String type, String desc, String packageName, String className,
            String actionName, String mimeType, String updatedBy) {
        super(version, type, desc, packageName, className, actionName, updatedBy);
        this.mimeType = mimeType;
    }

    public AsbMime(String version, String type, String desc, String packageName, String className,
            String actionName, String mimeType, String updatedBy, long id) {
        super(version, type, desc, packageName, className, actionName, updatedBy);
        this.id = id;
        this.mimeType = mimeType;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AsbMime) {
            AsbMime other = (AsbMime)obj;
            return ComparisonChain.start()
                    .compare(version, other.version)
                    .compare(type, other.type)
                    .compare(packageName, other.packageName)
                    .compare(className, other.className)
                    .compare(actionName, other.actionName)
                    .compare(mimeType, other.mimeType)
                    .result() == 0;
        } else {
            return false;
        }
    }
}
