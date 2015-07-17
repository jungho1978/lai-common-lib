package com.lge.lai.common.db.dto;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class ASBMime extends ASB {
    public long id;
    public String mimeType;

    public ASBMime(String versionName, String type, String desc, String packageName, String className,
            String actionName, String mimeType, String updatedBy) {
        super(versionName, type, desc, packageName, className, actionName, updatedBy);
        this.mimeType = mimeType;
    }

    public ASBMime(String versionName, String type, String desc, String packageName, String className,
            String actionName, String mimeType, String updatedBy, long id) {
        super(versionName, type, desc, packageName, className, actionName, updatedBy);
        this.id = id;
        this.mimeType = mimeType;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("versionName", versionName)
                .add("type", type)
                .add("desc", desc)
                .add("packageName", packageName)
                .add("className", className)
                .add("actionName", actionName)
                .add("mimeType", mimeType)
                .add("updatedBy", updatedBy)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ASBMime) {
            ASBMime other = (ASBMime)obj;
            return ComparisonChain.start()
                    .compare(versionName, other.versionName)
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
