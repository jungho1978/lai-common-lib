package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class DAOPropertiesTest {
    DAOProperties properties;

    @Before
    public void setUp() {
        properties = new DAOProperties("LGAppIF.db");
    }

    @Test
    public void checkInstanceNotNull() {
        assertNotNull(properties);
    }

    @Test
    public void getUrlAsProperty() {
        assertThat(properties.getProperty("url", true), either(is("jdbc:mysql://10.168.142.78"))
                .or(is("jdbc:mysql://905205.iptime.org")));
    }

    @Test
    public void getUsernameAsProperty() {
        assertThat(properties.getProperty("username", true), either(is("ase")).or(is("guest")));
    }

    @Test
    public void getPasswordAsProperty() {
        assertThat(properties.getProperty("password", true), either(is("2014ase")).or(is("guest")));
    }
}