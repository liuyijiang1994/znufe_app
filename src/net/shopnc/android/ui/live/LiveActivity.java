/**
 *  ClassName: LiveActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.live;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.more.LoginActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * "生活"界面
 * @author qjyong
 */
public class LiveActivity extends TabActivity{
	
	/** tab标签名*/	
	public final static String TAB_TAG_HOUSEDECORATE = "housedecorate";
	public final static String TAB_TAG_FOOD = "food";
	public final static String TAB_TAG_CAR = "car";
	public final static String TAB_TAG_MARRIAGE = "marriage";
	public final static String TAB_TAG_BABY = "baby";
	public final static String TAB_TAG_WOMAN = "woman";
	
	public static TabHost tabHost;
	
	
	private RadioButton btn_housedecorate;
	private RadioButton btn_food;
	private RadioButton btn_car;
	private RadioButton btn_marriage;
	private RadioButton btn_baby;
	private RadioButton btn_woman;
	
	
	private Intent housedecorate_intent;
	private Intent food_intent;
	private Intent car_intent;
	private Intent marriage_intent;
	private Intent baby_intent;
	private Intent woman_intent;

	protected MainActivity myParent;
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tab_live);
		myParent = (MainActivity)this.getParent();
		
		//////////////////// init ///////////////////////////
	
		
		housedecorate_intent = new Intent(this,HouseDecorateActivity.class);
		housedecorate_intent.putExtra("url", Constants.URL_LIVE_FIRST);
		
		food_intent = new Intent(this, FoodActivity.class);
		food_intent.putExtra("url", Constants.URL_LIVE_SECOND);
		
		car_intent = new Intent(this, CarActivity.class);
		car_intent.putExtra("url", Constants.URL_LIVE_THIRD);
		
		marriage_intent = new Intent(this, MarriageActivity.class);
		marriage_intent.putExtra("url", Constants.URL_LIVE_FORTH);
		
		baby_intent = new Intent(this, BabyActivity.class);
		baby_intent.putExtra("url", Constants.URL_LIVE_FIFTH);
		
		woman_intent = new Intent(this, WomanActivity.class);
		woman_intent.putExtra("url", Constants.URL_LIVE_SIXTH);
		
		tabHost = this.getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_HOUSEDECORATE).setIndicator(TAB_TAG_HOUSEDECORATE).setContent(housedecorate_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FOOD).setIndicator(TAB_TAG_FOOD).setContent(food_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_CAR).setIndicator(TAB_TAG_CAR).setContent(car_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_MARRIAGE).setIndicator(TAB_TAG_MARRIAGE).setContent(marriage_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_BABY).setIndicator(TAB_TAG_BABY).setContent(baby_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_WOMAN).setIndicator(TAB_TAG_WOMAN).setContent(woman_intent));
		////////////////////// find View ////////////////////////////
		
		btn_housedecorate = (RadioButton)this.findViewById(R.id.btn_live_housedecorate);
		btn_food = (RadioButton)this.findViewById(R.id.btn_live_food);
		btn_car = (RadioButton)this.findViewById(R.id.btn_live_car);
		btn_marriage = (RadioButton)this.findViewById(R.id.btn_live_marriage);
		btn_baby = (RadioButton)this.findViewById(R.id.btn_live_baby);
		btn_woman = (RadioButton)this.findViewById(R.id.btn_live_woman);
		
		/*修改二级栏目名称*/
		SharedPreferences sp = getSharedPreferences("top_name",MODE_PRIVATE);
		btn_housedecorate.setText(sp.getString("second1","家装"));
		btn_food.setText(sp.getString("second2", "美食"));
		btn_car.setText(sp.getString("second3","汽车"));
		btn_marriage.setText(sp.getString("second4", "婚嫁"));
		btn_baby.setText(sp.getString("second5","育婴"));
		btn_woman.setText(sp.getString("second6","女性"));
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		
		btn_housedecorate.setOnClickListener(listener);
		btn_food.setOnClickListener(listener);
		btn_car.setOnClickListener(listener);
		btn_marriage.setOnClickListener(listener);
		btn_baby.setOnClickListener(listener);
		btn_woman.setOnClickListener(listener);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("liveactivity", "live--resume");
		
		//退出
		ImageButton btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		final MyApp myApp = (MyApp)getApplication();
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
			btn_right.setBackgroundResource(R.drawable.btn_exit_normal);
		}else{
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
		
		btn_right.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
					LiveActivity.this.getParent().showDialog(Constants.DIALOG_EXITAPP_ID);
				}else{
					LiveActivity.this.startActivityForResult(new Intent(LiveActivity.this, LoginActivity.class), 200);
				}
			}
		});
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_live_housedecorate:
				tabHost.setCurrentTabByTag(TAB_TAG_HOUSEDECORATE);
				break;
			case R.id.btn_live_food:
				tabHost.setCurrentTabByTag(TAB_TAG_FOOD);
				break;
			case R.id.btn_live_car:
				tabHost.setCurrentTabByTag(TAB_TAG_CAR);
				break;
			case R.id.btn_live_marriage:
				tabHost.setCurrentTabByTag(TAB_TAG_MARRIAGE);
				break;
			case R.id.btn_live_baby:
				tabHost.setCurrentTabByTag(TAB_TAG_BABY);
				break;
			case R.id.btn_live_woman:
				tabHost.setCurrentTabByTag(TAB_TAG_WOMAN);
				break;
			}
		}
	}
}
