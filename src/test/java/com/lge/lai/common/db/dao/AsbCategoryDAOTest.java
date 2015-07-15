package com.lge.lai.common.db.dao;

import static org.junit.Assert.fail;

import java.util.List;

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
        asbId = asbDAO.create(asb);
        if (asbId == -1) {
            fail("duplicated entry exists");
        }
    }

    @After
    public void tearDown() {
        AsbDAO asbDAO = daoFactory.getAsbDAO();
        asbDAO.delete(asbId);
    }

    @Test
    public void allOperations() {
        String category = "android.intent.category.MAIN";
        AsbCategory asbCategory = new AsbCategory(asb.versionName, asb.type, asb.desc, asb.packageName,
                asb.className, asb.actionName, category, asb.updatedBy);

        AsbCategoryDAO asbCategoryDAO = daoFactory.getAsbCategoryDAO(asbId);
        long asbCategoryId = asbCategoryDAO.create(asbCategory);
        if (asbCategoryId == -1) {
            fail("duplicated entry exists");
        }
        AsbCategory insertedRow = (AsbCategory)asbCategoryDAO.find(asbCategoryId);
        if (!insertedRow.equals(asbCategory)) {
            fail("created object is not same with expected object");
        }
        asbCategoryDAO.delete(asbCategoryId);
    }
}