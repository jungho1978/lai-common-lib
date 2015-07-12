package com.lge.lai.common.data;

public class Data {
    public String scheme;
    public String host;
    public String port;
    public String path;
    public String pathPrefix;
    public String pathPattern;
    public String mimeType;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!isEmpty(scheme)) {
            builder.append("[scheme]\t\t" + scheme);
        }
        if (!isEmpty(host)) {
            insertNewLineIfNeeded(builder);
            builder.append("[host]\t\t\t" + host);
        }
        if (!isEmpty(port)) {
            insertNewLineIfNeeded(builder);
            builder.append("[port]\t\t\t" + port);
        }
        if (!isEmpty(path)) {
            insertNewLineIfNeeded(builder);
            builder.append("[path]\t\t\t" + path);
        }
        if (!isEmpty(pathPrefix)) {
            insertNewLineIfNeeded(builder);
            builder.append("[pathPrefix]\t\t" + pathPrefix);
        }
        if (!isEmpty(pathPattern)) {
            insertNewLineIfNeeded(builder);
            builder.append("[pathPattern]\t\t" + pathPattern);
        }
        if (!isEmpty(mimeType)) {
            insertNewLineIfNeeded(builder);
            builder.append("[mime]\t\t\t" + mimeType);
        }
        return builder.toString();
    }

    private void insertNewLineIfNeeded(StringBuilder builder) {
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
            builder.append('\n');
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
