package com.kelly.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PhoneInofUtil {
	
	/**
	 * get the screen height by pixel
	 * @param activity
	 * @return
	 */
	public static int getSreenHeight(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * get the screen width by pixel
	 * @param activity
	 * @return
	 */
	public static int getScreenWidth(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * get the screen density
	 * @param activity
	 * @return
	 */
	public static int getScreenDensity(Activity activity){
		DisplayMetrics dm=new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}
	
}
