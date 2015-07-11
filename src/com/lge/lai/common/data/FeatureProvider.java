/*
 * A Department, Mobile Communication Company, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright (c) 2015 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a retrieval system, or
 * transmitted by any means without prior written Permission of LG Electronics Inc.
 */
package com.lge.lai.common.data;

import java.util.ArrayList;
import java.util.List;

public class FeatureProvider {
    public String packageName;
    public String versionName;
    public List<Feature> features = new ArrayList<Feature>();

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public void addAllFeature(List<Feature> features) {
        this.features.addAll(features);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[package]\t\t" + packageName + '\n');
        builder.append("[version]\t\t" + versionName + '\n');
        for (Feature feature : features) {
            builder.append("---------------------------------------------------------------" + '\n');
            builder.append(feature.toString());
        }
        return builder.toString();
    }
}
