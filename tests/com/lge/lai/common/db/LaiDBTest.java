package com.lge.lai.common.db;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lge.lai.common.data.Data;
import com.lge.lai.common.data.Feature;
import com.lge.lai.common.data.FeatureProvider;

public class LaiDBTest {

	@Test(expected=LaiDBException.class)
	public void checkThrowException() {
		LaiDB.write(null);
	}

	@Test
	public void writeDatabase() {
		FeatureProvider provider = new FeatureProvider();
		provider.packageName = "com.example.helloworld";
		provider.versionName = "1.0.0";
		
		Feature feature = new Feature();
		feature.type = "activity";
		feature.className = provider.packageName + ".MainActivity";
		feature.actionName = "android.intent.ACTION.GET_CONTENT";
		feature.addCategory(new String("android.intent.CATEGORY.MAIN"));
		feature.addCategory(new String("android.intent.CATEGORY.DEFAULT"));
		
		Data data = new Data();
		data.mimeType = "image/*";
		data.scheme = "http";
		feature.addData(data);
		data = new Data();
		data.mimeType = "audio/*";
		data.scheme = "file";
		feature.addData(data);
		
		provider.addFeature(feature);
		
		LaiDB.write(provider);
	}
}
