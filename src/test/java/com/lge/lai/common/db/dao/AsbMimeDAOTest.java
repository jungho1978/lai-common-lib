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
        asbId = asbDAO.create(asb);
        if (asbId == -1) {
            fail("duplicate entry exists");
        }
    }

    @After
    public void tearDown() {
        AsbDAO asbDAO = daoFactory.getAsbDAO();
        asbDAO.delete(asbId);
    }

    @Test
    public void allOperations() {
        String mimeType = "image/*";
        AsbMime asbMime = new AsbMime(asb.versionName, asb.type, asb.desc, asb.packageName,
                asb.className, asb.actionName, mimeType, asb.updatedBy);

        AsbMimeDAO asbMimeDAO = daoFactory.getAsbMimeDAO(asbId);
        long asbMimeId = asbMimeDAO.create(asbMime);
        if (asbMimeId == -1) {
            fail("duplicated entry exists");
        }
        AsbMime insertedRow = (AsbMime)asbMimeDAO.find(asbMimeId);
        if (!insertedRow.equals(asbMime)) {
            fail("created object is not same with expected object");
        }
        asbMimeDAO.delete(asbMimeId);
    }
}