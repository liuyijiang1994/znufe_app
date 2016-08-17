/**
 *  ClassName: LocationHelper.java
 *  created on 2012-3-17
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.common;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

/**
 * 位置服务工具类<br/>
 * 注意：需要添加权限&lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/&gt;
 * @author qjyong
 */
public class LocationHelper {
	public static final String TAG = "LocationHelper";
	
	private static LocationManager locationManager;
	private static LocationListener network_listener;
	@SuppressWarnings("unused")
	private static LocationListener gps_listener;
	
	private static final int CHECK_INTERVAL = 1000 * 30;
	
	private static Location currentLocation;
	
	private LocationHelper(Context ctx) {}
	
	public interface LocationCallback{
		public void onLocation(Location location);
	}
	
	
	
	/**
	 * 获取当前的位置信息
	 * @param ctx
	 * @return
	 */
	public static void getLocation(Context ctx, LocationCallback callback){
		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
		registerLocationListener(callback);
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	@Deprecated
	public static Location getLocation(Context ctx){
		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();  
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 查询精度：高
        criteria.setAltitudeRequired(false);  //是否查询海拨：否
        criteria.setBearingRequired(false);  //是否查询方位角 : 否
        criteria.setCostAllowed(false);  //是否允许付费：否 
        criteria.setPowerRequirement(Criteria.POWER_LOW);  //电量要求低
        // 获得当前的位置提供者  
        String provider = locationManager.getBestProvider(criteria, true);  
        // 获得当前的位置  
        Location location = locationManager.getLastKnownLocation(provider);  
        
        //经度
        //location.getLongitude();
        //纬度
        //location.getLatitude();
        Log.d(TAG, location.getProvider());
        
        return location;
	}
	
	//一般来说NETWORK得到的位置精度一般在500-1000米，GPS得到的精度一般在5-50米
	private static void registerLocationListener(LocationCallback callback){
		network_listener = new MyLocationListner(callback);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, network_listener);
		
		//gps_listener = new MyLocationListner(callback);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, gps_listener);
	}
	
	/**
	 * 打开GPS
	 * @param ctx
	 */
	public static void openGPS(Context ctx){
		if(isGPSEnable(ctx)){
			toggleGPS(ctx);
		}
	}
	
	/**
	 * 切换GPS的开关状态
	 * @param ctx
	 */
	public static void toggleGPS(Context ctx) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx, 0, gpsIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 判断GPS是否可用，支持Android2.2以前的版本
	 * @param ctx
	 * @return
	 */
	public static boolean isGPSEnable(Context ctx) {
		//2.2以后版本可以直接使用以下这行代码来判断
		//boolean flag = android.provider.Settings.Secure.isLocationProviderEnabled(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		
		//String str = Settings.System.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		String str = Settings.Secure.getString(ctx.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		Log.d(TAG, str);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}
	
	private static class MyLocationListner implements LocationListener{
		private LocationCallback callback;
		
		public MyLocationListner(LocationCallback callback){
			this.callback = callback;
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the location provider.
			Log.v(TAG, "当前定位服务提供者:"+location.getProvider());
			
			if(isBetterLocation(location, currentLocation)){
				Log.v(TAG, "当前location是最好的");
				currentLocation = location;
			}else{
				Log.v(TAG, "当前location不是最好的");
			}
			
			showLocation(currentLocation);
			
			callback.onLocation(currentLocation);
			
			//if(LocationManager.NETWORK_PROVIDER.equals(location.getProvider())){
				locationManager.removeUpdates(this);
			//}
		}
	 
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	 
		public void onProviderEnabled(String provider) {
		}
	 
		public void onProviderDisabled(String provider) {
		}
	};
	
	private static void showLocation(Location location){
		//纬度
		Log.v(TAG,"Latitude:" + location.getLatitude());
		//经度
		Log.v(TAG,"Longitude:" + location.getLongitude());
		//精确度
		Log.v(TAG,"Accuracy:" + location.getAccuracy());
	}

	protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}
 
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;
 
		// If it's been more than two minutes since the current location,
		// use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}
 
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
 
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());
 
		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}
 
	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
