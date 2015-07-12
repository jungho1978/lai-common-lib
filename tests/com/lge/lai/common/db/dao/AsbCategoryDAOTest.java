package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbCategory;

public class AsbCategoryDAOTest {
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
        String category = "android.intent.category.MAIN";
        AsbCategory asbCategory = new AsbCategory(asb.version, asb.type, asb.desc, asb.packageName, asb.className, asb.actionName, category, asb.updatedBy);
        
        AsbCategoryDAO asbCategoryDAO = daoFactory.getAsbCategoryDAO(asbId);
        long asbCategoryId = 0L;
        try {
            asbCategoryId = asbCategoryDAO.create(asbCategory);
            AsbCategory createdAsbCategory = asbCategoryDAO.find(asbCategoryId);
            if (!createdAsbCategory.equals(asbCategory)) {
                fail("created object is not same with expected object");
            }
        } catch (DAOException e) {
            fail("creating asbCategory failed: " + e);
        } finally {
            try {
                asbCategoryDAO.delete(asbCategoryId);
            } catch (DAOException e) {
                fail("deleting asbCategory failed: " + e);
            }
        }
    }
}