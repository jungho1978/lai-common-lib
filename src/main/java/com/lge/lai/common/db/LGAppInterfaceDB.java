package com.lge.lai.common.db;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.dao.ASBCategoryDAO;
import com.lge.lai.common.db.dao.ASBDAO;
import com.lge.lai.common.db.dao.ASBMimeDAO;
import com.lge.lai.common.db.dao.ASBUriDAO;
import com.lge.lai.common.db.dao.BaseDAO;
import com.lge.lai.common.db.dao.DAOFactory;
import com.lge.lai.common.db.dao.ProviderDAO;
import com.lge.lai.common.db.dto.ASB;
import com.lge.lai.common.db.dto.ASBCategory;
import com.lge.lai.common.db.dto.ASBMime;
import com.lge.lai.common.db.dto.ASBUri;
import com.lge.lai.common.db.dto.Provider;

public class LGAppInterfaceDB {
    static Logger LOGGER = LogManager.getLogger(LGAppInterfaceDB.class.getName());
    
    private static final String UPDATED_BY_MANIFEST = "manifest";
    private static final String UPDATED_BY_DOCUMENT = "document";

    private static final String DEFAULT_DESC = "description";
    private static final String DEFAULT_TABLE = "table";
    private static final String DEFAULT_PRIMARY = "_id";

    private static DAOFactory daoFactory = DAOFactory.getInstance("LGAppIF.db");

    public static void write(FeatureProvider fp) {
        write(fp, "");
    }

    public static void write(FeatureProvider fp, String manifestPath) {
        if (fp == null) {
            throw new LGAppInterfaceDBException("nothing to write");
        }

        String packageName = fp.packageName;
        String versionName = fp.versionName;

        for (Feature feature : fp.features) {
            String type = feature.type;
            if (type.equalsIgnoreCase("provider")) {
                long providerId = createProviderFeature(packageName, versionName, feature);
                if (!isEmpty(manifestPath)) {
                    updateManifestPath(type, providerId, manifestPath);
                }
            } else {
                long asbId = createASBFeature(packageName, versionName, feature);
                if (!manifestPath.isEmpty()) {
                    updateManifestPath(type, asbId, manifestPath);
                }
            }
        }
    }

    public static FeatureProvider find(String packageName, String versionName) {
        if (isEmpty(packageName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        FeatureProvider fp = new FeatureProvider();
        fp.packageName = packageName;
        fp.versionName = versionName;

        listProviderFeature(packageName, versionName, fp);
        listASBFeature(packageName, versionName, fp);

        return fp;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void deleteASBFeature(String packageName, String versionName) {
        ASBDAO asbDAO = daoFactory.getASBDAO();
        List<ASB> asbRows = (List)asbDAO.list(ASBDAO.Where.PACKAGE_AND_VERSION, packageName,
                versionName);
        for (ASB asb : asbRows) {
            long id = asb.getId();
            asbDAO.delete(id);
        }
    }

    private static long createProviderFeature(String packageName, String versionName,
            Feature feature) {
        Provider provider = new Provider(versionName, feature.type, DEFAULT_DESC, packageName,
                feature.className, DEFAULT_TABLE, feature.readPermission, feature.writePermission,
                feature.authorities, DEFAULT_PRIMARY, UPDATED_BY_MANIFEST);
        return daoFactory.getProviderDAO().create(provider);
    }

    private static long createASBFeature(String packageName, String versionName, Feature feature) {
        String type = feature.type;
        String className = feature.className;
        String actionName = feature.actionName;

        ASB asb = new ASB(versionName, type, DEFAULT_DESC, packageName, className, actionName,
                UPDATED_BY_MANIFEST);
        long asbId = daoFactory.getASBDAO().create(asb);
        if (asbId == -1) {
            return -1;
        }

        for (String category : feature.categories) {
            ASBCategory asbCategory = new ASBCategory(versionName, type, DEFAULT_DESC, packageName,
                    className, actionName, category, UPDATED_BY_MANIFEST);
            daoFactory.getASBCategoryDAO(asbId).create(asbCategory);
        }

        for (String mimeType : feature.mimeTypes) {
            ASBMime asbMime = new ASBMime(versionName, type, DEFAULT_DESC, packageName, className,
                    actionName, mimeType, UPDATED_BY_MANIFEST);
            daoFactory.getASBMimeDAO(asbId).create(asbMime);
        }

        for (String uri : feature.schemes) {
            ASBUri asbUri = new ASBUri(versionName, type, DEFAULT_DESC, packageName, className,
                    actionName, uri, DEFAULT_DESC, UPDATED_BY_MANIFEST);
            daoFactory.getASBUriDAO(asbId).create(asbUri);
        }

        return asbId;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void listProviderFeature(String packageName, String versionName,
            FeatureProvider fp) {
        ProviderDAO providerDAO = daoFactory.getProviderDAO();
        List<Provider> providerRows = (List)providerDAO.list(ProviderDAO.Where.PACKAGE_AND_VERSION,
                packageName, versionName);

        for (Provider provider : providerRows) {
            Feature feature = new Feature();
            feature.type = provider.type;
            feature.className = provider.className;
            feature.readPermission = provider.readPermission;
            feature.writePermission = provider.writePermission;
            feature.authorities = provider.authorities;
            fp.addFeature(feature);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void listASBFeature(String packageName, String versionName, FeatureProvider fp) {
        ASBDAO asbDAO = daoFactory.getASBDAO();
        List<ASB> asbRows = (List)asbDAO.list(ASBDAO.Where.PACKAGE_AND_VERSION, packageName,
                versionName);

        for (ASB asb : asbRows) {
            long asbId = asb.getId();
            Feature feature = new Feature();
            feature.type = asb.type;
            feature.className = asb.className;
            feature.actionName = asb.actionName;

            ASBCategoryDAO categoryDAO = daoFactory.getASBCategoryDAO(asbId);
            List<ASBCategory> categoryRows = (List)categoryDAO.list();
            for (ASBCategory category : categoryRows) {
                feature.addCategory(category.category);
            }

            ASBMimeDAO mimeDAO = daoFactory.getASBMimeDAO(asbId);
            List<ASBMime> mimeRows = (List)mimeDAO.list();
            for (ASBMime mime : mimeRows) {
                feature.addMimeType(mime.mimeType);
            }

            ASBUriDAO uriDAO = daoFactory.getASBUriDAO(asbId);
            List<ASBUri> uriRows = (List)uriDAO.list();
            for (ASBUri uri : uriRows) {
                feature.addScheme(uri.uri);
            }

            fp.addFeature(feature);
        }
    }

    public static void delete(String packageName, String versionName) {
        if (isEmpty(packageName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        deleteProviderFeature(packageName, versionName);
        deleteASBFeature(packageName, versionName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void deleteProviderFeature(String packageName, String versionName) {
        ProviderDAO providerDAO = daoFactory.getProviderDAO();
        List<Provider> providerRows = (List)providerDAO.list(ProviderDAO.Where.PACKAGE_AND_VERSION,
                packageName, versionName);
        for (Provider provider : providerRows) {
            long id = provider.getId();
            providerDAO.delete(id);
        }
    }

    private static void updateManifestPath(String type, long id, String manifestPath) {
        if (id == -1) {
            LOGGER.error("Cannot update it becuase id value is negative");
            return;
        }
        if (type.equals("provider")) {
            ProviderDAO providerDAO = daoFactory.getProviderDAO();
            providerDAO.update(id, manifestPath);
        } else {
            ASBDAO asbDAO = daoFactory.getASBDAO();
            asbDAO.update(id, manifestPath);
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }
}