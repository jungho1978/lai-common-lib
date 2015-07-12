package com.lge.lai.common.db.dto;

import com.google.common.collect.ComparisonChain;

public class Asb extends Base {
    public String className;
    public String actionName;

    public Asb(String version, String type, String desc, String packageName, String className,
            String actionName, String updatedBy) {
        super(version, type, desc, packageName, updatedBy);
        this.className = className;
        this.actionName = actionName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Asb) {
            Asb other = (Asb)obj;
            return ComparisonChain.start()
                    .compare(version, other.version)
                    .compare(type, other.type)
                    .compare(packageName, other.packageName)
                    .compare(className, other.className)
                    .compare(actionName, other.actionName)
                    .result() == 0;
        } else {
            return false;
        }
    }
}
