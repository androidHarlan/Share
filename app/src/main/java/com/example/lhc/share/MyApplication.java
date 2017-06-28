package com.example.lhc.share;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;



public class MyApplication extends Application {
	@Override
	public void onCreate() {

		super.onCreate();

	}



	//各个平台的配置，建议放在全局Application或者程序入口
	{

		PlatformConfig.setWeixin("wxe5555eacc57486ba", "dc060136c977e70ffe62df5bb60cf2f7");
		PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
	}

}
