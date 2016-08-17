package com.special.ResideMenuDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//import com.baidu.lbsapi.controller.PanoramaController;
//import com.baidu.lbsapi.panoramaview.PanoramaView;







import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
//import com.baidu.lbsapi.panoramaview.PanoramaView;
//import com.baidu.lbsapi.panoramaviewdemo.PanoramaDemoActivityMain2;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.special.ResideMenuDemo.NanHu.MyLocationListenner;

//不用
//import com.baidu.lbsapi.controller.PanoramaController;
//import com.baidu.lbsapi.panoramadata.IndoorPanorama;
//import com.baidu.lbsapi.panoramaview.*;
//import com.baidu.lbsapi.BMapManager;
//import com.baidu.pplatform.comapi.basestruct.GeoPoint;


/**
 * 演示覆盖物的用法
 */
public class ShouYi extends FragmentActivity {

	/**
	 * MapView 是地图主控件
	 */
	//private PanoramaView mPanoView;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private Marker mMarkerE;
	private Marker mMarkerF;
	private Marker mMarkerG;
	private Marker mMarkerH;
	private Marker mMarkerI;
	private Marker mMarkerJ;
	//private Marker mMarkerK;
	//private Marker mMarkerL;
	//private Marker mMarkerM;
	//private Marker mMarkerN;
	private InfoWindow mInfoWindow;
	
	//全景图
	//private PanoramaController panoController;
	//private PanoramaView mPanoView;
	
	//private static final LatLng GEO_ZNUFE = new LatLng(30.480549,114.39558);//南湖会堂
	boolean isFirstLoc = true;// 是否首次定位
	
	
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markb);
	BitmapDescriptor bdC = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markc);
	BitmapDescriptor bdD = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markd);
	BitmapDescriptor bdE = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marke);
	BitmapDescriptor bdF = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markf);
	BitmapDescriptor bdG = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markg);
	BitmapDescriptor bdH = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markh);
	BitmapDescriptor bdI = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marki);
	BitmapDescriptor bdJ = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markj);

	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	//BitmapDescriptor bdGround = BitmapDescriptorFactory
	//		.fromResource(R.drawable.ground_overlay);

	//locate
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		//private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		// 初始化全局 bitmap 信息，不用时及时 recycle
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shouyi);
		
		/*MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_ZNUFE);
		SupportMapFragment map1 = (SupportMapFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.map1));
		map1.getBaiduMap().setMapStatus(u1);  
		*/
		
		//MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_ZNUFE);
		//mMapView.getMap().setMapStatus(u1);
		//SupportMapFragment map1 = (SupportMapFragment) (getSupportFragmentManager()
		//		.findFragmentById(R.id.map1));
		
		mMapView = (MapView) findViewById(R.id.bmapView2);	
		mBaiduMap = mMapView.getMap();
		//LatLng znufe = new LatLng(40.480549,104.39558);//30.480549,114.39558
		//MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(znufe,14);//zoomTo(24.0f);//缩放
		//mBaiduMap.setMapStatus(msu);
		
		//全景图
		//mPanoView = (PanoramaView)findViewById(R.id.panorama);
		//mPanoView.setPanoramaImageLevel(5);
		//mPanoView.setPanoramaViewListener(this);
		//panoController = new PanoramaController();
		
		//定位
				Button btn_locate = (Button)findViewById(R.id.btn_locate2);
				mLocClient = new LocationClient(this);
				// 开启定位图层
					btn_locate.setOnClickListener(new OnClickListener(){
						
						@Override
						public void onClick(View v) {
						mBaiduMap.setMyLocationEnabled(true);
						// 定位初始化			
						mLocClient.registerLocationListener(myListener);
						LocationClientOption option = new LocationClientOption();
						option.setOpenGps(true);// 打开gps
						option.setCoorType("bd09ll"); // 设置坐标类型
						option.setScanSpan(1000);
						mLocClient.setLocOption(option);
						mLocClient.start();
						}
					});
		
		initOverlay();
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				button.setTextColor(Color.BLACK);
				final LatLng ll = marker.getPosition();//LatLng是表示地理坐标的类
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				//p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA) {
					button.setText("图书馆");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.536880，114.312550
							StreeViewDemo.lat = 30.536880;//
						    StreeViewDemo.lon = 114.312550;//经度
							Intent it = new Intent(ShouYi.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				} else if (marker == mMarkerB) {//有更改
					button.setText("首义校区体育馆");
					//30.535226,114.312441
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							
							StreeViewDemo.lat = 30.535226;//
						    StreeViewDemo.lon = 114.312441;//经度
							Intent it = new Intent(ShouYi.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				} else if (marker == mMarkerC) {
					button.setText("中南楼");
					listener = new OnInfoWindowClickListener() {
						//30.539430，114.313300
						public void onInfoWindowClick() {
							StreeViewDemo.lat = 30.539430;
						    StreeViewDemo.lon = 114.313300;
							Intent it = new Intent(ShouYi.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerD) {
					button.setText("文泽楼");
					listener = new OnInfoWindowClickListener() {
						//30.539380，114.314170
						public void onInfoWindowClick() {
							StreeViewDemo.lat = 30.539380;
						    StreeViewDemo.lon = 114.314170;
							Intent it = new Intent(ShouYi.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerE) {
					button.setText("中南大教育超市首义店");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.539296,114.314946
							marker.remove();
							mBaiduMap.hideInfoWindow();
						}
					};
				}else if (marker == mMarkerF) {
					button.setText("文津楼");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.538310,114.313830
							StreeViewDemo.lat = 30.538310;
						    StreeViewDemo.lon = 114.313830;
							Intent it = new Intent(ShouYi.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}
				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	public void initOverlay() {
		// add marker overlay
		LatLng P1 = new LatLng(30.544043,114.321227);//首义校区图书馆
		LatLng P2 = new LatLng(30.540971,114.318998);//体育馆
		LatLng P3 = new LatLng(30.545162,114.319871);//中南楼
		LatLng P4 = new LatLng(30.545123,114.320738);//文泽楼
		LatLng P5 = new LatLng(30.545085,114.321496);//中南大教育超市
		LatLng P6 = new LatLng(30.54405,114.3204);//文津楼

		OverlayOptions ooA = new MarkerOptions().position(P1).icon(bdA)
				.zIndex(9);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		OverlayOptions ooB = new MarkerOptions().position(P2).icon(bdB)
				.zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		OverlayOptions ooC = new MarkerOptions().position(P3).icon(bdC)
				.perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
		mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
		OverlayOptions ooD = new MarkerOptions().position(P4).icon(bdD)
				.perspective(false).zIndex(7);
		mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
		//E
		OverlayOptions ooE = new MarkerOptions().position(P5).icon(bdE)
				.zIndex(9);
		mMarkerE = (Marker) (mBaiduMap.addOverlay(ooE));
		OverlayOptions ooF = new MarkerOptions().position(P6).icon(bdF)
				.zIndex(9);
		mMarkerF = (Marker) (mBaiduMap.addOverlay(ooF));
		
		

		// add ground overlay(什么用？)
		LatLng southwest = new LatLng(30.543817,114.317606);
		LatLng northeast = new LatLng(30.546033,114.323095);
		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
				.include(southwest).build();

		//OverlayOptions ooGround = new GroundOverlayOptions()
			//	.positionFromBounds(bounds).image(bdGround).transparency(0.8f);
		//mBaiduMap.addOverlay(ooGround);

		//MapStatusUpdate u = MapStatusUpdateFactory
		//		.newLatLng(bounds.getCenter());
		//mBaiduMap.setMapStatus(u);
		LatLng znufe = new LatLng(30.543232,114.319215);//114.319215,30.543232
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(znufe,17);//zoomTo(24.0f);//缩放
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	//public void clearOverlay(View view) {
	//	mBaiduMap.clear();
	//}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	//public void resetOverlay(View view) {
	//	clearOverlay(null);
	//	initOverlay();
	//}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());//谷城县111.658659,32.268513
				//location.getLatitude(),
				//location.getLongitude()
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	
	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();
		bdB.recycle();
		bdC.recycle();
		bdD.recycle();
		bd.recycle();
		//bdGround.recycle();
	}

}
