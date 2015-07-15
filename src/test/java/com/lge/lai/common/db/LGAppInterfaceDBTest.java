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
        FeatureProvider fp = new FeatureProvider();
        fp.packageName = "com.example.helloworld";
        fp.versionName = "1.0.0";

        Feature asbFeature = new Feature();
        asbFeature.type = "activity";
        asbFeature.className = fp.packageName + ".MainActivity";
        asbFeature.actionName = "android.intent.ACTION.GET_CONTENT";
        asbFeature.addCategory("android.intent.CATEGORY.MAIN");
        asbFeature.addCategory("android.intent.CATEGORY.DEFAULT");
        asbFeature.addScheme("http");
        asbFeature.addScheme("file");
        asbFeature.addMimeType("image/*");
        asbFeature.addMimeType("audio/*");
        fp.addFeature(asbFeature);
        
        Feature providerFeature = new Feature();
        providerFeature.type = "provider";
        providerFeature.className = fp.packageName + ".MainProvider";
        providerFeature.readPermission = "android.permission.READ";
        providerFeature.writePermission = "android.permission.WRITE";
        providerFeature.authorities = "hellworld.authrotieis";
        fp.addFeature(providerFeature);
        
        return fp;
    }
}
