package com.lge.lai.common.db;

import java.util.List;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.dao.AsbCategoryDAO;
import com.lge.lai.common.db.dao.AsbDAO;
import com.lge.lai.common.db.dao.AsbMimeDAO;
import com.lge.lai.common.db.dao.AsbUriDAO;
import com.lge.lai.common.db.dao.DAOFactory;
import com.lge.lai.common.db.dao.ProviderDAO;
import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbCategory;
import com.lge.lai.common.db.dto.AsbMime;
import com.lge.lai.common.db.dto.AsbUri;
import com.lge.lai.common.db.dto.Provider;

public class LGAppInterfaceDB {
    private static final String UPDATED_BY_MANIFEST = "manifest";
    private static final String UPDATED_BY_DOCUMENT = "document";

    private static final String DEFAULT_DESC = "description";
    private static final String DEFAULT_TABLE = "table";
    private static final String DEFAULT_PRIMARY = "_id";

    private static DAOFactory daoFactory = DAOFactory.getInstance("LGAppIF.db");

    public static void write(FeatureProvider fp) {
        if (fp == null) {
            throw new LGAppInterfaceDBException("nothing to write");
        }

        String packageName = fp.packageName;
        String versionName = fp.versionName;

        for (Feature feature : fp.features) {
            String className = feature.className;
            String type = feature.type;
            if (type.equalsIgnoreCase("provider")) {
                createProviderFeature(packageName, versionName, feature);
            } else {
                createASBFeature(packageName, versionName, feature);
            }
        }
    }

    public static FeatureProvider find(String packageName, String versionName) {
        if (isEmpty(packageName) || isEmpty(versionName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        FeatureProvider fp = new FeatureProvider();
        fp.packageName = packageName;
        fp.versionName = versionName;

        listProviderFeature(packageName, versionName, fp);
        listASBFeature(packageName, versionName, fp);

        return fp;
    }

    private static void deleteASBFeature(String packageName, String versionName) {
        AsbDAO asbDAO = daoFactory.getAsbDAO();
        List<Asb> asbRows = (List)asbDAO.list(packageName, versionName);
        for (Asb asb : asbRows) {
            long id = asb.getId();
            asbDAO.delete(id);
        }
    }

    private static void createProviderFeature(String packageName, String versionName,
            Feature feature) {
        Provider provider = new Provider(versionName, feature.type, DEFAULT_DESC, packageName,
                feature.className, DEFAULT_TABLE, feature.readPermission, feature.writePermission,
                feature.authorities, DEFAULT_PRIMARY, UPDATED_BY_MANIFEST);
        daoFactory.getProviderDAO().create(provider);
    }

    private static void createASBFeature(String packageName, String versionName, Feature feature) {
        String type = feature.type;
        String className = feature.className;
        String actionName = feature.actionName;

        Asb asb = new Asb(versionName, type, DEFAULT_DESC, packageName, className, actionName,
                UPDATED_BY_MANIFEST);
        long asbId = daoFactory.getAsbDAO().create(asb);
        if (asbId == -1) {
            return;
        }

        for (String category : feature.categories) {
            AsbCategory asbCategory = new AsbCategory(versionName, type, DEFAULT_DESC, packageName,
                    className, actionName, category, UPDATED_BY_MANIFEST);
            daoFactory.getAsbCategoryDAO(asbId).create(asbCategory);
        }

        for (String mimeType : feature.mimeTypes) {
            AsbMime asbMime = new AsbMime(versionName, type, DEFAULT_DESC, packageName, className,
                    actionName, mimeType, UPDATED_BY_MANIFEST);
            daoFactory.getAsbMimeDAO(asbId).create(asbMime);
        }

        for (String uri : feature.schemes) {
            AsbUri asbUri = new AsbUri(versionName, type, DEFAULT_DESC, packageName, className,
                    actionName, uri, DEFAULT_DESC, UPDATED_BY_MANIFEST);
            daoFactory.getAsbUriDAO(asbId).create(asbUri);
        }
    }

    private static void listProviderFeature(String packageName, String versionName,
            FeatureProvider fp) {
        ProviderDAO providerDAO = daoFactory.getProviderDAO();
        List<Provider> providerRows = (List)providerDAO.list(packageName, versionName);

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

    private static void listASBFeature(String packageName, String versionName, FeatureProvider fp) {
        AsbDAO asbDAO = daoFactory.getAsbDAO();
        List<Asb> asbRows = (List)asbDAO.list(packageName, versionName);

        for (Asb asb : asbRows) {
            long asbId = asb.getId();
            Feature feature = new Feature();
            feature.type = asb.type;
            feature.className = asb.className;
            feature.actionName = asb.actionName;

            AsbCategoryDAO categoryDAO = daoFactory.getAsbCategoryDAO(asbId);
            List<AsbCategory> categoryRows = (List)categoryDAO.list();
            for (AsbCategory category : categoryRows) {
                feature.addCategory(category.category);
            }

            AsbMimeDAO mimeDAO = daoFactory.getAsbMimeDAO(asbId);
            List<AsbMime> mimeRows = (List)mimeDAO.list();
            for (AsbMime mime : mimeRows) {
                feature.addMimeType(mime.mimeType);
            }

            AsbUriDAO uriDAO = daoFactory.getAsbUriDAO(asbId);
            List<AsbUri> uriRows = (List)uriDAO.list();
            for (AsbUri uri : uriRows) {
                feature.addScheme(uri.uri);
            }

            fp.addFeature(feature);
        }
    }

    public static void delete(String packageName, String versionName) {
        if (isEmpty(packageName) || isEmpty(versionName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        deleteProviderFeature(packageName, versionName);
        deleteASBFeature(packageName, versionName);
    }

    private static void deleteProviderFeature(String packageName, String versionName) {
        ProviderDAO providerDAO = daoFactory.getProviderDAO();
        List<Provider> providerRows = (List)providerDAO.list(packageName, versionName);
        for (Provider provider : providerRows) {
            long id = provider.getId();
            providerDAO.delete(id);
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }
}