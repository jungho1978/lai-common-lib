package com.lge.lai.common.db;

import java.util.List;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.dao.AsbCategoryDAO;
import com.lge.lai.common.db.dao.AsbDAO;
import com.lge.lai.common.db.dao.AsbMimeDAO;
import com.lge.lai.common.db.dao.AsbUriDAO;
import com.lge.lai.common.db.dao.DAOFactory;
import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbCategory;
import com.lge.lai.common.db.dto.AsbMime;
import com.lge.lai.common.db.dto.AsbUri;

public class LGAppInterfaceDB {
    private static final String UPDATED_BY_MANIFEST = "manifest";
    private static final String UPDATED_BY_DOCUMENT = "document";

    private static final String DEFAULT_DESC = "description";

    private static DAOFactory daoFactory = DAOFactory.getInstance("LGAppIF.db");

    public static void write(FeatureProvider provider) {
        if (provider == null) {
            throw new LGAppInterfaceDBException("nothing to write");
        }

        String packageName = provider.packageName;
        String version = provider.versionName;

        for (Feature feature : provider.features) {
            String className = feature.className;
            String type = feature.type;
            if (type.equalsIgnoreCase("provider")) {
                // will be implemented
            } else {
                String actionName = feature.actionName;
                Asb asb = new Asb(version, type, DEFAULT_DESC, packageName, className, actionName,
                        UPDATED_BY_MANIFEST);
                long asbId = daoFactory.getAsbDAO().create(asb);

                for (String category : feature.categories) {
                    AsbCategory asbCategory = new AsbCategory(version, type, DEFAULT_DESC,
                            packageName, className, actionName, category, UPDATED_BY_MANIFEST);
                    daoFactory.getAsbCategoryDAO(asbId).create(asbCategory);
                }

                for (String mimeType : feature.mimeTypes) {
                    AsbMime asbMime = new AsbMime(version, type, DEFAULT_DESC, packageName,
                            className, actionName, mimeType, UPDATED_BY_MANIFEST);
                    daoFactory.getAsbMimeDAO(asbId).create(asbMime);
                }

                for (String uri : feature.schemes) {
                    AsbUri asbUri = new AsbUri(version, type, DEFAULT_DESC, packageName, className,
                            actionName, uri, DEFAULT_DESC, UPDATED_BY_MANIFEST);
                    daoFactory.getAsbUriDAO(asbId).create(asbUri);
                }
            }
        }
    }

    public static FeatureProvider find(String packageName, String versionName) {
        if (isEmpty(packageName) || isEmpty(versionName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        FeatureProvider provider = new FeatureProvider();
        provider.packageName = packageName;
        provider.versionName = versionName;

        AsbDAO asbDAO = daoFactory.getAsbDAO();
        List<Asb> asbRows = (List)asbDAO.list(packageName, versionName);
        if (asbRows.size() <= 0) {
            throw new LGAppInterfaceDBException("listing object failed");
        }

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

            AsbMimeDAO mimeDAO = daoFactory.getAsbMimeDAO(asb.getId());
            List<AsbMime> mimeRows = (List)mimeDAO.list();
            for (AsbMime mime : mimeRows) {
                feature.addMimeType(mime.mimeType);
            }

            AsbUriDAO uriDAO = daoFactory.getAsbUriDAO(asb.getId());
            List<AsbUri> uriRows = (List)uriDAO.list();
            for (AsbUri uri : uriRows) {
                feature.addScheme(uri.uri);
            }

            provider.addFeature(feature);
        }

        return provider;
    }

    public static void delete(String packageName, String versionName) {
        if (isEmpty(packageName) || isEmpty(versionName)) {
            throw new LGAppInterfaceDBException("argument error");
        }

        AsbDAO asbDAO = daoFactory.getAsbDAO();
        List<Asb> asbRows = (List)asbDAO.list(packageName, versionName);
        for (Asb asb : asbRows) {
            long id = asb.getId();
            asbDAO.delete(id);
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

}
