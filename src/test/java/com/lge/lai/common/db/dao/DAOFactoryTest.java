package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class DAOFactoryTest {

    @Test
    public void checkInstanceNotNull() {
        assertNotNull(DAOFactory.getInstance("LGAppIF.db"));
    }
}