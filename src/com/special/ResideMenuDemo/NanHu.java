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

import android.widget.RadioGroup.OnCheckedChangeListener;
//import baidumapsdk.demo.LocationDemo.MyLocationListenner;



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
//import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
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

//不用
//import com.baidu.lbsapi.controller.PanoramaController;
//import com.baidu.lbsapi.panoramadata.IndoorPanorama;
//import com.baidu.lbsapi.panoramaview.*;
//import com.baidu.lbsapi.BMapManager;
//import com.baidu.pplatform.comapi.basestruct.GeoPoint;


/**
 * 演示覆盖物的用法
 */
public class NanHu extends FragmentActivity {

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
	
	private static final LatLng GEO_ZNUFE = new LatLng(30.480549,114.39558);//南湖会堂
	
	
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markb);
	BitmapDescriptor bdC = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markc);
	BitmapDescriptor bdD= BitmapDescriptorFactory
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


	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nanhu);
		
		/*MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_ZNUFE);
		SupportMapFragment map1 = (SupportMapFragment) (getSupportFragmentManager()
				.findFragmentById(R.id.map1));
		map1.getBaiduMap().setMapStatus(u1);  
		*/
		
		//MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_ZNUFE);
		//mMapView.getMap().setMapStatus(u1);
		//SupportMapFragment map1 = (SupportMapFragment) (getSupportFragmentManager()
		//		.findFragmentById(R.id.map1));
		
		mMapView = (MapView) findViewById(R.id.bmapView1);	
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
					button.setText("文治楼");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							StreeViewDemo.lat = 30.474460;//纬度 30.474460，114.388560
						    StreeViewDemo.lon = 114.388560;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				} else if (marker == mMarkerB) {//有更改
					button.setText("文澜楼");
					listener = new OnInfoWindowClickListener() {
						//private double lon;

						public void onInfoWindowClick() {
							
							//mPanoView.setPanorama(30.481024,114.39697);
							System.out.println("这不科学啊");
						    StreeViewDemo.lat = 30.474060;//纬度  30.474060，114.393350
						    StreeViewDemo.lon = 114.393350;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
			                //finish();
			                System.out.println("这不科学啊ncncncnc");
			                
							
							//marker.setIcon(bd);
							//mBaiduMap.hideInfoWindow();
							
							//mPanoView.setPanorama(114.387825,30.476232);
					/*System.out.println("这不科学啊1");
					button.setOnClickListener(new OnClickListener(){
						
						public void onClick(View v){			
							Intent it = new Intent(OverlayDemo.this,BMapApiDemoMain.class);
			                startActivity(it);
			                finish();
			                System.out.println("这不科学啊");*/
						}
					};
				} else if (marker == mMarkerC) {
					button.setText("南附楼");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							
							//30.474277,114.380274
							StreeViewDemo.lat = 30.474277;//30.474540，114.389090
						    StreeViewDemo.lon = 114.380274;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerD) {
						button.setText("图书馆");
						listener = new OnInfoWindowClickListener() {
							public void onInfoWindowClick() {
								//30.474190,114.384890
								StreeViewDemo.lat = 30.474190;//30.474540，114.389090
							    StreeViewDemo.lon = 114.384890;//经度
								Intent it = new Intent(NanHu.this,StreeViewDemo.class);
				                startActivity(it);
							}
						};	
				}else if (marker == mMarkerE) {
					button.setText("艺体（旁边是新体）");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.471345,114.384566
							StreeViewDemo.lat = 30.471345;//
						    StreeViewDemo.lon = 114.384566;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerF) {
					button.setText("南湖校区北门");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.478440,114.388610
							StreeViewDemo.lat = 30.478440;//
						    StreeViewDemo.lon = 114.388610;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerG) {
					button.setText("南湖校区东门");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.474670,114.394070
							StreeViewDemo.lat = 30.474670;//
						    StreeViewDemo.lon = 114.394070;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerH) {
					button.setText("文泉楼");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.471380,114.384000
							StreeViewDemo.lat = 30.471380;//
						    StreeViewDemo.lon = 114.384000;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerI) {
					button.setText("临湖公寓");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.472270,114.390500
							StreeViewDemo.lat = 30.472270;//
						    StreeViewDemo.lon = 114.390500;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
			                startActivity(it);
						}
					};
				}else if (marker == mMarkerJ) {
					button.setText("南苑");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							//30.471800，114.389060
							StreeViewDemo.lat = 30.471800;//
						    StreeViewDemo.lon = 114.389060;//经度
							Intent it = new Intent(NanHu.this,StreeViewDemo.class);
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
		LatLng llA = new LatLng(30.480479,114.395047);//文治楼
		LatLng llB = new LatLng(30.479996,114.39986);//文澜楼
		LatLng llC = new LatLng(30.480463,114.386709);//南附楼
		LatLng llD = new LatLng(30.480277,114.39136);//图书馆
		LatLng llE = new LatLng(30.477475,114.391045);//艺体
		LatLng llF = new LatLng(30.485015,114.395126);//北门
		LatLng llG = new LatLng(30.480596,114.400584);//东门
		LatLng llH = new LatLng(30.477483,114.390463);//文泉楼
		LatLng llI = new LatLng(30.478254,114.396995);//临湖公寓
		LatLng llJ = new LatLng(30.47781,114.395548);//南苑
		//LatLng llK = new LatLng(30.47746 ,114.391036  );//新体
		//LatLng llL = new LatLng(30.479195,114.390997  );//南湖校区研究生院
		//LatLng llM = new LatLng(30.480214,114.392445  );//泊威尔健身俱乐部
		//LatLng llN = new LatLng(30.479926,114.394676  );//文清楼

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
				.zIndex(5);
		mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
		OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdC)
				.perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
		mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
		OverlayOptions ooD = new MarkerOptions().position(llD).icon(bdD)
				.perspective(false).zIndex(7);
		mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
		//E
		OverlayOptions ooE = new MarkerOptions().position(llE).icon(bdE)
				.zIndex(9);
		mMarkerE = (Marker) (mBaiduMap.addOverlay(ooE));
		OverlayOptions ooF = new MarkerOptions().position(llF).icon(bdF)
				.zIndex(9);
		mMarkerF = (Marker) (mBaiduMap.addOverlay(ooF));
		OverlayOptions ooG = new MarkerOptions().position(llG).icon(bdG)
				.zIndex(9);
		mMarkerG = (Marker) (mBaiduMap.addOverlay(ooG));
		OverlayOptions ooH = new MarkerOptions().position(llH).icon(bdH)
				.zIndex(9);
		mMarkerH = (Marker) (mBaiduMap.addOverlay(ooH));
		OverlayOptions ooI = new MarkerOptions().position(llI).icon(bdI)
				.zIndex(9);
		mMarkerI = (Marker) (mBaiduMap.addOverlay(ooI));
		OverlayOptions ooJ = new MarkerOptions().position(llJ).icon(bdJ)
				.zIndex(9);
		mMarkerJ = (Marker) (mBaiduMap.addOverlay(ooJ));
		/*OverlayOptions ooK = new MarkerOptions().position(llK).icon(bdA)
				.zIndex(9);
		mMarkerK = (Marker) (mBaiduMap.addOverlay(ooK));
		OverlayOptions ooL = new MarkerOptions().position(llL).icon(bdA)
				.zIndex(9);
		mMarkerL = (Marker) (mBaiduMap.addOverlay(ooL));
		OverlayOptions ooM = new MarkerOptions().position(llM).icon(bdA)
				.zIndex(9);
		mMarkerM = (Marker) (mBaiduMap.addOverlay(ooM));
		OverlayOptions ooN = new MarkerOptions().position(llN).icon(bdA)
				.zIndex(9);
		mMarkerN = (Marker) (mBaiduMap.addOverlay(ooN));
		 */
		// add ground overlay(什么用？)
		LatLng southwest = new LatLng(30.480432,114.388029);//114.388029,30.480432
		LatLng northeast = new LatLng(30.484026,1114.397497);//114.397497,30.484026
		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
				.include(southwest).build();

		//OverlayOptions ooGround = new GroundOverlayOptions()
			//	.positionFromBounds(bounds).image(bdGround).transparency(0.8f);
		//mBaiduMap.addOverlay(ooGround);

		//MapStatusUpdate u = MapStatusUpdateFactory
		//		.newLatLng(bounds.getCenter());
		//mBaiduMap.setMapStatus(u);
		LatLng znufe = new LatLng(30.480549,114.39558);//30.480549,114.39558
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(znufe,17);//zoomTo(24.0f);//缩放
		mBaiduMap.setMapStatus(msu);
	}
	
	
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

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		initOverlay();
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
		bdD.recycle();
		bdE.recycle();
		bdF.recycle();
		bdG.recycle();
		bdH.recycle();
		bdI.recycle();
		bdJ.recycle();
		//bdGround.recycle();
	}

}
