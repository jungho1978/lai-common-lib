package com.lge.lai.common.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOFactory {
    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_USER = "username";
    private static final String PROPERTY_PASSWORD = "password";

    private static final String DRIVER = "com.mysql.jdbc.Driver";

    public static DAOFactory getInstance(String name) throws DAOConfigurationException {
        if (name == null) {
            throw new DAOConfigurationException("Database name is null");
        }

        DAOProperties properties = new DAOProperties(name);
        String url = properties.getProperty(PROPERTY_URL, true);
        String username = properties.getProperty(PROPERTY_USER, true);
        String password = properties.getProperty(PROPERTY_PASSWORD, true);

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DAOConfigurationException("Driver class '" + DRIVER + "' is missing.", e);
        }

        return new DriverManagerDAOFactory(url, username, password);
    }

    abstract Connection getConnection() throws SQLException;

    public AsbDAO getAsbDAO() {
        return new AsbDAO(this);
    }

    public AsbCategoryDAO getAsbCategoryDAO(long asbId) {
        return new AsbCategoryDAO(asbId, this);
    }

    public AsbMimeDAO getAsbMimeDAO(long asbId) {
        return new AsbMimeDAO(asbId, this);
    }

    public AsbUriDAO getAsbUriDAO(long asbId) {
        return new AsbUriDAO(asbId, this);
    }
    
    public ProviderDAO getProviderDAO() {
        return new ProviderDAO(this);
    }
}

class DriverManagerDAOFactory extends DAOFactory {
    private String url;
    private String username;
    private String password;

    public DriverManagerDAOFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}