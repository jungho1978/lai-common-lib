package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;

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
        assertEquals("jdb:mysql://905205.iptime.org", properties.getProperty("url", true));
    }
    
    @Test
    public void getUsernameAsProperty() {
        assertEquals("guest", properties.getProperty("username", true));
    }
    
    @Test
    public void getPasswordAsProperty() {
        assertEquals("guest", properties.getProperty("password", true));
    }
}
