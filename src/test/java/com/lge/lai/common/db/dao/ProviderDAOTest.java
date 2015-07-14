package com.lge.lai.common.db.dao;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.lge.lai.common.db.dto.Provider;

public class ProviderDAOTest {
    DAOFactory daoFactory;

    @Before
    public void setUp() {
        daoFactory = DAOFactory.getInstance("LGAppIF.db");
    }

    @Test
    public void allOperations() {
        String version = "1.0.0";
        String type = "provider";
        String desc = "Description";
        String packageName = "com.example.helloworld";
        String className = packageName + ".MainProvider";
        String tableName = "table";
        String readPermission = "android.permission.READ";
        String writePermission = "android.permission.WRITE";
        String authorities = "helloworld";
        String primaryKey = "_id";
        String updatedBy = "manifest";
        Provider provider = new Provider(version, type, desc, packageName, className, tableName,
                readPermission, writePermission, authorities, primaryKey, updatedBy);

        ProviderDAO providerDAO = daoFactory.getProviderDAO();
        long providerId = 0L;
        try {
            providerId = providerDAO.create(provider);
            Provider insertedRow = (Provider)providerDAO.find(providerId);
            if (!insertedRow.equals(provider)) {
                fail("created object is not same with expected object");
            }
        } catch (DAOException e) {
            fail("creating failed: " + e);
        } finally {
            try {
                providerDAO.delete(providerId);
            } catch (DAOException e) {
                fail("deleting failed: " + e);
            }
        }
    }
}