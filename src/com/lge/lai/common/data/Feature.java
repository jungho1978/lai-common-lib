package com.lge.lai.common.data;

import java.util.ArrayList;
import java.util.List;

public class Feature {
    public String type;
    public String className;
    public String actionName;
    public List<String> categories = new ArrayList<String>();
    public List<Data> datas = new ArrayList<Data>();

    // For content provider
    public String authorities;
    public String readPermission;
    public String writePermission;

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public void addData(Data data) {
        this.datas.add(data);
    }

    public String getCategories() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            builder.append(categories.get(i));
            if (i != categories.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    public String getDatas() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < datas.size(); i++) {
            builder.append(datas.get(i).toString());
            if (i != datas.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[type]\t\t\t" + type + '\n');
        builder.append("[className]\t\t" + className + '\n');
        builder.append("[action]\t\t" + actionName + '\n');
        for (String category : categories) {
            builder.append("[category]\t\t" + category + '\n');
        }
        for (Data data : datas) {
            builder.append(data.toString());
        }
        insertNewLineIfNeeded(builder);
        if (type.equals("provider")) {
            builder.append("[authorities]\t\t" + authorities + '\n');
            builder.append("[readPermission]\t" + readPermission + '\n');
            builder.append("[writePermission]\t" + writePermission + '\n');
        }
        return builder.toString();
    }

    private void insertNewLineIfNeeded(StringBuilder builder) {
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
            builder.append('\n');
        }
    }
}
