/**
 *  ClassName: HomeActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.home;

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
 * 首页
 * @author qjyong
 */
public class HomeActivity extends TabActivity{
	
	/** tab标签名*/
	public final static String TAB_TAG_HEADLINES = "headlines";
	public final static String TAB_TAG_TEAHOUSE = "teahouse";
	public final static String TAB_TAG_FOCUS = "focus";
	public final static String TAB_TAG_ENTERTAINMENT = "entertainment";
	public final static String TAB_TAG_EMOTIONAL = "emotional";
	
	public static TabHost tabHost;
	
	private RadioButton btn_headlines;
	private RadioButton btn_teahouse;
	private RadioButton btn_focus;
	private RadioButton btn_entertainment;
	private RadioButton btn_emotional;
	
	private Intent headlines_intent;
	private Intent teahouse_intent;
	private Intent focus_intent;
	private Intent entertainment_intent;
	private Intent emotional_intent;

	
	protected MainActivity myParent;
	
	
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tab_home);
		myParent = (MainActivity)this.getParent();
		
		//////////////////// init ///////////////////////////
		headlines_intent = new Intent(this, HeadlinesActivity.class);
		
		teahouse_intent = new Intent(this, TeahouseActivity.class);
		teahouse_intent.putExtra("url", Constants.URL_HOME_TEAHOUSE);
		
		focus_intent = new Intent(this, FocusActivity.class);
		focus_intent.putExtra("url", Constants.URL_HOME_FOCUS);
		
		entertainment_intent = new Intent(this, EntertainmentActivity.class);
		entertainment_intent.putExtra("url", Constants.URL_HOME_ENTERTAINMENT);
		
		emotional_intent = new Intent(this, EmotionalActivity.class);
		emotional_intent.putExtra("url", Constants.URL_HOME_EMOTIONAL);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_HEADLINES).setIndicator(TAB_TAG_HEADLINES).setContent(headlines_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_TEAHOUSE).setIndicator(TAB_TAG_TEAHOUSE).setContent(teahouse_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FOCUS).setIndicator(TAB_TAG_FOCUS).setContent(focus_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_ENTERTAINMENT).setIndicator(TAB_TAG_ENTERTAINMENT).setContent(entertainment_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_EMOTIONAL).setIndicator(TAB_TAG_EMOTIONAL).setContent(emotional_intent));
	
		////////////////////// find View ////////////////////////////
		btn_headlines = (RadioButton)this.findViewById(R.id.btn_home_headlines);
		btn_teahouse = (RadioButton)this.findViewById(R.id.btn_home_teahouse);
		btn_focus = (RadioButton)this.findViewById(R.id.btn_home_focus);
		btn_entertainment = (RadioButton)this.findViewById(R.id.btn_home_entertainment);
		btn_emotional = (RadioButton)this.findViewById(R.id.btn_home_emotional);
		
		/*修改二级栏目名称*/
		SharedPreferences sp = getSharedPreferences("top_name",MODE_PRIVATE);
		btn_headlines.setText(sp.getString("index1","头条"));
		btn_teahouse.setText(sp.getString("index2", "茶座"));
		btn_focus.setText(sp.getString("index3","焦点"));
		btn_entertainment.setText(sp.getString("index4", "娱乐"));
		btn_emotional.setText(sp.getString("index5","情感"));
				
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_headlines.setOnClickListener(listener);
		btn_teahouse.setOnClickListener(listener);
		btn_focus.setOnClickListener(listener);
		btn_entertainment.setOnClickListener(listener);
		btn_emotional.setOnClickListener(listener);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("homeactivity", "home--resume");
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
					HomeActivity.this.getParent().showDialog(Constants.DIALOG_EXITAPP_ID);
				}else{
					Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					HomeActivity.this.startActivityForResult(intent, 200);
				}
			}
		});
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_home_headlines:
				tabHost.setCurrentTabByTag(TAB_TAG_HEADLINES);
				break;
			case R.id.btn_home_teahouse:
				tabHost.setCurrentTabByTag(TAB_TAG_TEAHOUSE);
				break;
			case R.id.btn_home_focus:
				tabHost.setCurrentTabByTag(TAB_TAG_FOCUS);
				break;
			case R.id.btn_home_entertainment:
				tabHost.setCurrentTabByTag(TAB_TAG_ENTERTAINMENT);
				break;
			case R.id.btn_home_emotional:
				tabHost.setCurrentTabByTag(TAB_TAG_EMOTIONAL);
				break;
			}
		}
	}
}
