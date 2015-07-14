package com.lge.lai.common.db.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbMime;

public class AsbMimeDAOTest {
    DAOFactory daoFactory;
    Asb asb;
    long asbId;

    @Before
    public void setUp() {
        daoFactory = DAOFactory.getInstance("LGAppIF.db");

        String version = "1.0.0";
        String type = "activity";
        String desc = "Description";
        String packageName = "com.example.helloworld";
        String actionName = "android.intent.action.GET_CONTENT";
        String updatedBy = "manifest";
        String className = packageName + ".MainActivity";
        asb = new Asb(version, type, desc, packageName, className, actionName, updatedBy);

        AsbDAO asbDAO = daoFactory.getAsbDAO();
        try {
            asbId = asbDAO.create(asb);
        } catch (DAOException e) {
            fail("creating asb failed: " + e);
        }
    }

    @After
    public void tearDown() {
        AsbDAO asbDAO = daoFactory.getAsbDAO();
        try {
            asbDAO.delete(asbId);
        } catch (DAOException e) {
            fail("deleting asb failed: " + e);
        }
    }

    @Test
    public void allOperations() {
        String mimeType = "image/*";
        AsbMime asbMime = new AsbMime(asb.version, asb.type, asb.desc, asb.packageName,
                asb.className, asb.actionName, mimeType, asb.updatedBy);

        AsbMimeDAO asbMimeDAO = daoFactory.getAsbMimeDAO(asbId);
        long asbMimeId = 0L;
        try {
            asbMimeId = asbMimeDAO.create(asbMime);
            AsbMime insertedRow = (AsbMime)asbMimeDAO.find(asbMimeId);
            if (!insertedRow.equals(asbMime)) {
                fail("created object is not same with expected object");
            }
        } catch (DAOException e) {
            fail("creating asbMime failed: " + e);
        } finally {
            try {
                asbMimeDAO.delete(asbMimeId);
            } catch (DAOException e) {
                fail("deleting asbMime failed: " + e);
            }
        }
    }
}