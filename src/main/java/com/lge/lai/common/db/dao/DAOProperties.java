package com.lge.lai.common.db.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DAOProperties {
    private static final String PROPERTIES_FILE = "db.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);

        try {
            PROPERTIES.load(propertiesFile);
        } catch (IOException e) {
            throw new DAOConfigurationException("Cannot load properies file '" + PROPERTIES_FILE
                    + "'.", e);
        }
    }

    private String specificKey;

    public DAOProperties(String specificKey) throws DAOConfigurationException {
        this.specificKey = specificKey;
    }

    public String getProperty(String key, boolean mandatory) throws DAOConfigurationException {
        String fullKey = specificKey + "." + key;
        String property = PROPERTIES.getProperty(fullKey);

        if (property == null || property.trim().length() == 0) {
            if (mandatory) {
                throw new DAOConfigurationException("Required proeprty '" + fullKey
                        + "' is missing in properties file '" + PROPERTIES_FILE + "'.");
            } else {
                property = null;
            }
        }
        return property;
    }
}