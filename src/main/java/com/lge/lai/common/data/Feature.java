package com.lge.lai.common.data;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Feature {
    public String type;
    public String className;
    public String actionName;
    public List<String> categories = Lists.newArrayList();
    public List<String> schemes = Lists.newArrayList();
    public List<String> hosts = Lists.newArrayList();
    public List<String> ports = Lists.newArrayList();
    public List<String> paths = Lists.newArrayList();
    public List<String> pathPatterns = Lists.newArrayList();
    public List<String> pathPrefixes = Lists.newArrayList();
    public List<String> mimeTypes = Lists.newArrayList();

    // For content provider
    public String authorities;
    public String readPermission;
    public String writePermission;

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public String getCategories() {
        return getDataValues("category");
    }

    public void addScheme(String scheme) {
        this.schemes.add(scheme);
    }

    public String getSchemes() {
        return getDataValues("scheme");
    }

    public void addHost(String host) {
        this.hosts.add(host);
    }

    public String getHosts() {
        return getDataValues("host");
    }

    public void addPort(String port) {
        this.ports.add(port);
    }

    public String getPorts() {
        return getDataValues("port");
    }

    public void addPath(String path) {
        this.paths.add(path);
    }

    public String getPaths() {
        return getDataValues("path");
    }

    public void addPathPattern(String pathPattern) {
        this.pathPatterns.add(pathPattern);
    }

    public String getPathPatterns() {
        return getDataValues("pathPattern");
    }

    public void addPathPrefix(String pathPrefix) {
        this.pathPrefixes.add(pathPrefix);
    }

    public String getPathPrefixes() {
        return getDataValues("pathPrefix");
    }

    public void addMimeType(String mimeType) {
        this.mimeTypes.add(mimeType);
    }

    public String getMimeTypes() {
        return getDataValues("mimeType");
    }

    private String getDataValues(String type) {
        List<String> datas;
        if (type.equalsIgnoreCase("category")) {
            datas = categories;
        } else if (type.equalsIgnoreCase("scheme")) {
            datas = schemes;
        } else if (type.equalsIgnoreCase("host")) {
            datas = hosts;
        } else if (type.equalsIgnoreCase("port")) {
            datas = ports;
        } else if (type.equalsIgnoreCase("path")) {
            datas = paths;
        } else if (type.equalsIgnoreCase("pathPattern")) {
            datas = pathPatterns;
        } else if (type.equalsIgnoreCase("pathPrefix")) {
            datas = pathPrefixes;
        } else if (type.equalsIgnoreCase("mimeType")) {
            datas = mimeTypes;
        } else {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < datas.size(); i++) {
            builder.append(datas.get(i));
            if (i != datas.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Feature) {
            Feature other = (Feature)obj;

            return ComparisonChain.start()
                    .compare(type, other.type, STRING_COMPARATOR)
                    .compare(className, other.className, STRING_COMPARATOR)
                    .compare(actionName, other.actionName, STRING_COMPARATOR)
                    .compare(schemes, other.schemes, STRING_LIST_COMPARATOR)
                    .compare(hosts, other.hosts, STRING_LIST_COMPARATOR)
                    .compare(ports, other.ports, STRING_LIST_COMPARATOR)
                    .compare(paths, other.paths, STRING_LIST_COMPARATOR)
                    .compare(pathPatterns, other.pathPatterns, STRING_LIST_COMPARATOR)
                    .compare(pathPrefixes, other.pathPrefixes, STRING_LIST_COMPARATOR)
                    .compare(mimeTypes, other.mimeTypes, STRING_LIST_COMPARATOR).result() == 0;
        } else {
            return false;
        }
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
        for (String scheme : schemes) {
            builder.append("[scheme]\t\t" + scheme + '\n');
        }
        for (String host : hosts) {
            builder.append("[host]\t\t" + host + '\n');
        }
        for (String port : ports) {
            builder.append("[port]\t\t" + port + '\n');
        }
        for (String pathPattern : pathPatterns) {
            builder.append("[pathPattern]\t\t" + pathPattern + '\n');
        }
        for (String pathPrefix : pathPrefixes) {
            builder.append("[pathPrefix]\t\t" + pathPrefix + '\n');
        }
        for (String mimeType : mimeTypes) {
            builder.append("[mimeType]\t\t" + mimeType + '\n');
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

    private static Comparator<List<String>> STRING_LIST_COMPARATOR = new Comparator<List<String>>() {
        @Override
        public int compare(List<String> myStrings, List<String> yourStrings) {
            if (myStrings.size() != yourStrings.size()) {
                return -1;
            }
            for (String str : yourStrings) {
                if (!myStrings.contains(str)) {
                    return -1;
                }
            }
            return 0;
        }
    };
    
    private static Comparator<String> STRING_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String myString, String yourString) {
            if (myString != null && yourString == null) {
                return -1;
            }
            if (myString == null && yourString != null) {
                return -1;
            }
            if (myString == null && yourString == null) {
                return 0;
            }
            if (yourString.equals(myString)) {
                return 0;
            }
            return -1;
        }
    };
}
