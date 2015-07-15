package com.lge.lai.common.db.dto;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class AsbCategory extends Asb {
    public long id;
    public String category;

    public AsbCategory(String versionName, String type, String desc, String packageName,
            String className, String actionName, String category, String updatedBy) {
        super(versionName, type, desc, packageName, className, actionName, updatedBy);
        this.category = category;
    }

    public AsbCategory(String versionName, String type, String desc, String packageName,
            String className, String actionName, String category, String updatedBy, long id) {
        super(versionName, type, desc, packageName, className, actionName, updatedBy);
        this.id = id;
        this.category = category;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("versionName", versionName)
                .add("type", type)
                .add("desc", desc)
                .add("packageName", packageName)
                .add("className", className)
                .add("actionName", actionName)
                .add("category", category)
                .add("updatedBy", updatedBy)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AsbCategory) {
            AsbCategory other = (AsbCategory)obj;
            return ComparisonChain.start()
                    .compare(versionName, other.versionName)
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
