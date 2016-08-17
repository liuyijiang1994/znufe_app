
package com.special.ResideMenuDemo;

import com.tencent.street.StreetThumbListener;
import com.tencent.street.StreetViewListener;
import com.tencent.street.StreetViewShow;

import com.tencent.street.map.basemap.GeoPoint;
import com.tencent.street.overlay.ItemizedOverlay;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * 请在StreetViewShow.getInstance().showStreetView()方法输入申请的key，如下初始化所示，否则无法显示街景    
 *
 */
public class StreeViewDemo extends Activity implements StreetViewListener {
	private ViewGroup mView;

	private ImageView mImage;

	private Handler mHandler;

	private View mStreetView;

	 static double lat;
	
	 static double lon;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.streetviewdemo);
		mView = (LinearLayout)findViewById(R.id.layout);
		mImage = (ImageView)findViewById(R.id.image);
		//30.474670,114.394070南湖校区东大门
		//30.478440,114.388610南湖校区北门
		//30.540119,114.313310首义校区	
		
				
				System.out.println("1");
				onok();
			
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				mImage.setImageBitmap((Bitmap)msg.obj);
				
			}
		};
		
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onok() {
		GeoPoint center = new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6));
		String key = "RE7BZ-QWJAS-RXLOV-6YHEA-CP2M3-32BLB";
		StreetViewShow.getInstance().showStreetView(getApplicationContext(), center, 100, StreeViewDemo.this, -170, 0, key);
		System.out.println("2");
		StreetViewShow.getInstance().requestStreetThumb("10041002111120153536407",//"10011505120412110900000",
				new StreetThumbListener() {
			
			@Override
			public void onGetThumbFail() {
				System.out.println("fail");
			}

			@Override
			public void onGetThumb(Bitmap bitmap) {
				System.out.println("3");
				Message msg = new Message();
				msg.obj = bitmap;
				
				mHandler.sendMessage(msg);
			}
		});System.out.println("2.5");
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onViewReturn(final View v) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStreetView = v;
				mView.addView(mStreetView);
			}
		});
	}

	@Override
	public void onNetError() {
		//        LogUtil.logStreet("onNetError");
	}

	@Override
	public void onDataError() {
		//        LogUtil.logStreet("onDataError");
	}

	StreetOverlay overlay;

	@Override
	public ItemizedOverlay getOverlay() {
		/*if (overlay == null) {//  30478360,114394277
            ArrayList<StreetPoiData> pois = new ArrayList<StreetPoiData>();
            pois.add(new StreetPoiData(39984066, 116307968, getBm(R.drawable.poi_center),
                    getBm(R.drawable.poi_center_pressed), 0));
            pois.add(new StreetPoiData(39984166, 11630800, getBm(R.drawable.pin_green),
                    getBm(R.drawable.pin_green_pressed), 40));
            pois.add(new StreetPoiData(39984000, 116307968, getBm(R.drawable.pin_yellow),
                    getBm(R.drawable.pin_yellow_pressed), 80));
            pois.add(new StreetPoiData(39984066, 116308088, getBm(R.drawable.pin_red),
                    getBm(R.drawable.pin_red_pressed), 120));
            overlay = new StreetOverlay(pois);
            overlay.populate();
        }*/
		return overlay;
	}

	private Bitmap getBm(int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inScaled = false;

		return BitmapFactory.decodeResource(getResources(), resId, options);
	}

	@Override
	public void onLoaded() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStreetView.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onAuthFail() {
		//        LogUtil.logStreet("onAuthFail");
	}
}
