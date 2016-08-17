package com.special.ResideMenuDemo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
//import com.baidu.lbsapi.BMapManager;
//import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

}




