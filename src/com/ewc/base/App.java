package com.ewc.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.ewc.tools.PhotoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class App extends Application{
	public static App ctx;
	public static boolean debug = true;

	private List<Activity> list = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());  
		ctx = this;
		initImageLoader(ctx);


	}
	
	public static App getInstance() {
		if (null == ctx) {
			ctx = new App();
		}
		return ctx;
	}
	public void addActivity(Activity activity) {
		list.add(activity);
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"ewc/Cache");
		ImageLoaderConfiguration config = PhotoUtils.getImageLoaderConfig(
				context, cacheDir);
		ImageLoader.getInstance().init(config);
	}

}
