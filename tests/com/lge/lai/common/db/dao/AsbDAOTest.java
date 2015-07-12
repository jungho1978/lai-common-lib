package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.Asb;

public class AsbDAOTest {
    DAOFactory daoFactory;
    
    @Before
    public void setUp() {
        daoFactory = DAOFactory.getInstance("LGAppIF.db");
    }

    @Test
    public void allOperations() {
        String version = "1.0.0";
        String type = "activity";
        String desc = "Description";
        String packageName = "com.example.helloworld";
        String actionName = "android.intent.action.GET_CONTENT";
        String updatedBy = "manifest";
        String className = packageName + ".MainActivity";
        Asb asb = new Asb(version, type, desc, packageName, className, actionName, updatedBy);

        AsbDAO asbDAO = daoFactory.getAsbDAO();
        long asbId = 0L;
        try {
            asbId = asbDAO.create(asb);
            Asb createdAsb = asbDAO.find(asbId);
            if (!createdAsb.equals(asb)) {
                fail("created object is not same with expected object");
            }
        } catch (DAOException e) {
            fail("creation failed: " + e);
        } finally {
            try {
                asbDAO.delete(asbId);
            } catch (DAOException e) {
                fail("deletion failed: " + e);
            }
        }
    }

}
