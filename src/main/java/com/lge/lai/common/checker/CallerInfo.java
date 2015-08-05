package com.lge.lai.common.checker;

import com.google.common.base.Objects;
import com.lge.lai.common.annotation.ActionWith.Call;
import com.lge.lai.common.annotation.ActionWith.Component;

public class CallerInfo {
    public String toPackage;
    public Call callType;
    public Component componentType;

    public CallerInfo(String toPackage, Call callType, Component componentType) {
        this.toPackage = toPackage;
        this.callType = callType;
        this.componentType = componentType;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("toPackage", toPackage)
                .add("callType", callType)
                .add("componentType", componentType)
                .toString();
    }
}
