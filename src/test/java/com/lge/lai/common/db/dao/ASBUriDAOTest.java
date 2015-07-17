package com.lge.lai.common.db.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.ASB;
import com.lge.lai.common.db.dto.ASBUri;

public class ASBUriDAOTest {
    DAOFactory daoFactory;
    ASB asb;
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
        asb = new ASB(version, type, desc, packageName, className, actionName, updatedBy);

        ASBDAO asbDAO = daoFactory.getASBDAO();
        asbId = asbDAO.create(asb);
        if (asbId == -1) {
            fail("duplicated entry exists");
        }
    }

    @After
    public void tearDown() {
        ASBDAO asbDAO = daoFactory.getASBDAO();
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
        ASBUri asbUri = new ASBUri(asb.versionName, asb.type, asb.desc, asb.packageName, asb.className,
                asb.actionName, uri, uriDesc, asb.updatedBy);

        ASBUriDAO asbUriDAO = daoFactory.getASBUriDAO(asbId);
        long asbUriId = asbUriDAO.create(asbUri);
        if (asbUriId == -1) {
            fail("duplicated entry exists");
        }
        ASBUri insertedRow = (ASBUri)asbUriDAO.find(asbUriId);
        if (!insertedRow.equals(asbUri)) {
            fail("created object is not same with expected object");
        }
        asbUriDAO.delete(asbUriId);
    }
}