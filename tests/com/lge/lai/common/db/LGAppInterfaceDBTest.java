package com.lge.lai.common.db;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;
import com.lge.lai.common.db.dao.DAOException;
import com.lge.lai.common.db.dto.Asb;

public class LGAppInterfaceDBTest {

    @Test(expected = LGAppInterfaceDBException.class)
    public void checkThrowException() {
        LGAppInterfaceDB.write(null);
    }

    @Test
    public void allOperations() {
        FeatureProvider provider = getDummyData();
        try {
            LGAppInterfaceDB.write(provider);
            FeatureProvider insertedProvider = LGAppInterfaceDB.find(provider.packageName,
                    provider.versionName);
            if (!insertedProvider.equals(provider)) {
                fail("inserted object is not same with expected object");
            }
        } catch (LGAppInterfaceDBException e) {
            fail("LGAppIF DB opertaion exception occurred");
        } finally {
            try {
                LGAppInterfaceDB.delete(provider.packageName, provider.versionName);
            } catch (LGAppInterfaceDBException e) {
                fail("LGAppIF DB delete operation exception occurred");
            }
        }
    }

    private FeatureProvider getDummyData() {
        FeatureProvider provider = new FeatureProvider();
        provider.packageName = "com.example.helloworld";
        provider.versionName = "1.0.0";

        Feature feature = new Feature();
        feature.type = "activity";
        feature.className = provider.packageName + ".MainActivity";
        feature.actionName = "android.intent.ACTION.GET_CONTENT";
        feature.addCategory("android.intent.CATEGORY.MAIN");
        feature.addCategory("android.intent.CATEGORY.DEFAULT");
        feature.addScheme("http");
        feature.addScheme("file");
        feature.addMimeType("image/*");
        feature.addMimeType("audio/*");

        provider.addFeature(feature);
        return provider;
    }
}
