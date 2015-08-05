package com.lge.lai.common.checker;

import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
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
import com.lge.lai.common.db.dto.ASBCategory;
import com.lge.lai.common.db.dto.ASBUri;

public class InterfaceChecker {
    static Logger LOGGER = LogManager.getLogger(InterfaceChecker.class.getName());
    
    private static String TESTCASE_EXECUTOR = "TestCaseExecutorMain";

    private DAOFactory daoFactory;
    private Class<?> clazz;
    private boolean IGNORE_ASSERTION;
    
    public InterfaceChecker(Class<?> clazz) {
        this.clazz = clazz;
        daoFactory = DAOFactory.getInstance("LGAppIF.db");
        
        if (this.clazz.getSimpleName().equals(TESTCASE_EXECUTOR)) {
            IGNORE_ASSERTION = true;
        }
    }
    
    public boolean validates(Intent intent) {
        String caller = getCallerMethodName();
        LOGGER.info(caller);
        return validates(caller, intent);
    }

    public boolean validates(String caller, Intent intent) {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(caller);
        } catch (Exception e) {
            e.printStackTrace();
            return assertFail("Cannot find method to test");
        }

        ActionWith actionWith = m.getAnnotation(ActionWith.class);
        return validates(actionWith.toPackage(), actionWith.callType(), actionWith.componentType(), intent);
    }
    
    public boolean validates(String toPackage, Call callType, Component componentType, Intent intent) {
        CallerInfo callerInfo = new CallerInfo(toPackage, callType, componentType);
        if (callType == Call.EXPLICIT) {
            return processExplicitCall(callerInfo, intent);
        } else {
            return processImplicitCall(callerInfo, intent);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean processExplicitCall(CallerInfo callerInfo, Intent intent) {
        ComponentName componentName = intent.getComponent();
        String packageName = componentName.getPackageName();
        String className = componentName.getClassName();

        if (!packageName.equals(callerInfo.toPackage)) {
            return assertFail("Different with toPackage: " + packageName);
        }

        if (callerInfo.componentType == Component.PROVIDER) {
            // Will be implemented
            return false;
        } else {
            ASBDAO asbDAO = daoFactory.getASBDAO();
            List<ASB> asbRows = (List)asbDAO.list(Where.PACKAGE_ONLY, packageName);
            for (ASB asb : asbRows) {
                if (asb.packageName.equals(packageName) && className.endsWith(asb.className)) {
                    return true;
                }
            }
            return assertFail("Interface validation error: " + callerInfo);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean processImplicitCall(CallerInfo callerInfo, Intent intent) {
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        String scheme = intent.getScheme();
        String mimeType = intent.getType();

        if (isEmpty(action)) {
            fail("Action must be filled in");
        }

        ASBDAO asbDAO = daoFactory.getASBDAO();
        List<ASB> asbRows = (List)asbDAO.list(Where.PACKAGE_AND_ACTION, callerInfo.toPackage, action);
        if (asbRows.size() <= 0) {
            return assertFail("Interface validation error: " + callerInfo);
        }

//        if (categories == null && isEmpty(scheme) && isEmpty(mimeType)) {
//            return true; // Success
//        }

        for (ASB asb : asbRows) {
            if (checkCategories(asb.getId(), categories) && checkScheme(asb.getId(), scheme)
                    && checkMimeType(asb.getId(), mimeType)) {
                return true;
            }
        }
        return assertFail("Interface validation error: " + callerInfo);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean checkCategories(long id, Set<String> categories) {
        ASBCategoryDAO categoryDAO = daoFactory.getASBCategoryDAO(id);
        List<ASBCategory> categoryRows = (List)categoryDAO.list();
        if (categoryRows.size() <= 0 && 
                (categories == null || categories.size() <= 0)) {
            return true;
        }
        
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
        ASBUriDAO uriDAO = daoFactory.getASBUriDAO(id);
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
        ASBMimeDAO mimeDAO = daoFactory.getASBMimeDAO(id);
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
    
    private boolean assertFail(String message) {
        LOGGER.error(message);
        if (!IGNORE_ASSERTION) {
            fail(message);
        }
        return false;
    }
}
