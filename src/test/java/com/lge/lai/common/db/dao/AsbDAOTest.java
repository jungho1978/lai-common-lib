package com.lge.lai.common.db.dao;

import static org.junit.Assert.fail;

import java.util.List;

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
        long asbId = asbDAO.create(asb);
        if (asbId == -1) {
            fail("duplicated entry exists");
        }
        Asb insertedRow = (Asb)asbDAO.find(asbId);
        if (!insertedRow.equals(asb)) {
            fail("created object is not same with expected object");
        }
        asbDAO.delete(asbId);
    }
}