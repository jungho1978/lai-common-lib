package com.lge.lai.common.db.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbUri;

public class AsbUriDAOTest {
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
            fail("duplicated entry exists");
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
        String uri = "https:///www.google.com";
        String uriDesc = "URI description";
        AsbUri asbUri = new AsbUri(asb.versionName, asb.type, asb.desc, asb.packageName, asb.className,
                asb.actionName, uri, uriDesc, asb.updatedBy);

        AsbUriDAO asbUriDAO = daoFactory.getAsbUriDAO(asbId);
        long asbUriId = asbUriDAO.create(asbUri);
        if (asbUriId == -1) {
            fail("duplicated entry exists");
        }
        AsbUri insertedRow = (AsbUri)asbUriDAO.find(asbUriId);
        if (!insertedRow.equals(asbUri)) {
            fail("created object is not same with expected object");
        }
        asbUriDAO.delete(asbUriId);
    }
}