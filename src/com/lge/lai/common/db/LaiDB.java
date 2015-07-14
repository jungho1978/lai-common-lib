package com.lge.lai.common.db;

import com.lge.lai.common.data.Data;
import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.dao.DAOFactory;
import com.lge.lai.common.db.dto.Asb;
import com.lge.lai.common.db.dto.AsbCategory;
import com.lge.lai.common.db.dto.AsbMime;
import com.lge.lai.common.db.dto.AsbUri;

public class LaiDB {
	private static final String UPDATED_BY_MANIFEST = "manifest";
	private static final String UPDATED_BY_DOCUMENT = "document";
	
	private static final String DEFAULT_DESC = "description";
	
	private static DAOFactory daoFactory = DAOFactory.getInstance("LGAppIF.db");
	
	public static void write(FeatureProvider provider) {
		if (provider == null) {
			throw new LaiDBException("nothing to write");
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
				Asb asb = new Asb(version, type, DEFAULT_DESC, packageName, className, actionName, UPDATED_BY_MANIFEST);
				long asbId = daoFactory.getAsbDAO().create(asb);
				
				for (String category : feature.categories) {
					AsbCategory asbCategory = new AsbCategory(version, type, DEFAULT_DESC, packageName, className, actionName, category, UPDATED_BY_MANIFEST);
					daoFactory.getAsbCategoryDAO(asbId).create(asbCategory);
				}
				
				for (Data data : feature.datas) {
					if (!isEmpty(data.mimeType)) {
						AsbMime asbMime = new AsbMime(version, type, DEFAULT_DESC, packageName, className, actionName, data.mimeType, UPDATED_BY_MANIFEST);
						daoFactory.getAsbMimeDAO(asbId).create(asbMime);
					}
					if (!isEmpty(data.scheme)) {
						AsbUri asbUri = new AsbUri(version, type, DEFAULT_DESC, packageName, className, actionName, data.scheme, DEFAULT_DESC, UPDATED_BY_MANIFEST);
						daoFactory.getAsbUriDAO(asbId).create(asbUri);
					}
				}
			}
		}
	}
	
	public static void delete()
	
	private static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}
}
