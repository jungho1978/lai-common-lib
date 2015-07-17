package com.lge.lai.common.checker;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import android.content.ComponentName;
import android.content.Intent;

import com.lge.lai.common.annotation.ActionWith;
import com.lge.lai.common.annotation.ActionWith.Call;
import com.lge.lai.common.annotation.ActionWith.Component;
import com.lge.lai.common.db.dao.ASBCategoryDAO;
import com.lge.lai.common.db.dao.ASBDAO;
import com.lge.lai.common.db.dao.ASBDAO.Where;
import com.lge.lai.common.db.dao.ASBMimeDAO;
import com.lge.lai.common.db.dao.ASBUriDAO;
import com.lge.lai.common.db.dao.DAOFactory;
import com.lge.lai.common.db.dto.ASB;
import com.lge.lai.common.db.dto.ASBUri;

public class InterfaceChecker {
    static Logger LOGGER = LogManager.getLogger(InterfaceChecker.class.getName());

    private DAOFactory daoFactory;
    private Class<?> clazz;

    public InterfaceChecker(Class<?> clazz) {
        this.clazz = clazz;
        daoFactory = DAOFactory.getInstance("LGAppIF.db");
    }

    public void validates(Intent intent) {
        String caller = getCallerMethodName();
        LOGGER.info(caller);
        validates(caller, intent);
    }

    public void validates(String caller, Intent intent) {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(caller);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Cannot find method to test");
        }

        ActionWith actionWith = m.getAnnotation(ActionWith.class);
        LOGGER.info("[ActionWith] " + actionWith.toPackage());
        LOGGER.info("[ActionWith] " + actionWith.callType());
        LOGGER.info("[ActionWith] " + actionWith.componentType());

        if (actionWith.callType() == Call.EXPLICIT) {
            processExplicitCall(actionWith, intent);
        } else {
            processImplicitCall(actionWith, intent);
        }
    }

    @SuppressWarnings("unchecked")
    private void processExplicitCall(ActionWith actionWith, Intent intent) {
        ComponentName componentName = intent.getComponent();
        String packageName = componentName.getPackageName();
        String className = componentName.getClassName();

        if (!packageName.equals(actionWith.toPackage())) {
            fail("Different with toPackage: " + packageName);
        }

        if (actionWith.componentType() == Component.PROVIDER) {
            // Will be implemented
        } else {
            ASBDAO asbDAO = daoFactory.getAsbDAO();
            List<ASB> asbRows = (List)asbDAO.list(Where.PACKAGE_ONLY, packageName);
            for (ASB asb : asbRows) {
                if (asb.packageName.equals(packageName) && className.endsWith(asb.className)) {
                    return;
                }
            }
            fail("Interface validation error: " + actionWith);
        }
    }

    @SuppressWarnings("unchecked")
    private void processImplicitCall(ActionWith actionWith, Intent intent) {
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        String scheme = intent.getScheme();
        String mimeType = intent.getType();

        if (isEmpty(action)) {
            fail("Action must be filled in");
        }

        ASBDAO asbDAO = daoFactory.getAsbDAO();
        List<ASB> asbRows = (List)asbDAO.list(Where.PACKAGE_AND_ACTION, actionWith.toPackage(),
                action);
        if (asbRows.size() <= 0) {
            fail("Interface validation error: " + actionWith);
        }

        if (categories == null && isEmpty(scheme) && isEmpty(mimeType)) {
            return; // Success
        }

        for (ASB asb : asbRows) {
            if (checkCategories(asb.getId(), categories)
                    && checkScheme(asb.getId(), scheme)
                    && checkMimeType(asb.getId(), mimeType)) {
                return;
            }
        }
        fail("Interface validation error: " + actionWith);
    }

    private boolean checkCategories(long id, Set<String> categories) {
        ASBCategoryDAO categoryDAO = daoFactory.getAsbCategoryDAO(id);
        if (categories != null && categories.size() >= 1) {
            for (String category : categories) {
                if (categoryDAO.list(category).size() >= 1) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
    
    private boolean checkScheme(long id, String scheme) {
        ASBUriDAO uriDAO = daoFactory.getAsbUriDAO(id);
        if (!isEmpty(scheme)) {
            if (uriDAO.list(scheme).size() >= 1) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
    
    private boolean checkMimeType(long id, String mimeType) {
        ASBMimeDAO mimeDAO = daoFactory.getAsbMimeDAO(id);
        if (!isEmpty(mimeType)) {
            if (mimeDAO.list(mimeType).size() >= 1) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private String getCallerMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTraceElements[3];
        return e.getMethodName();
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
