/**
 *  ClassName: DistrictActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.info;

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
 * 信息
 * @author qjyong
 */
public class InfoActivity extends TabActivity{
	
	/** tab标签名*/
	public final static String TAB_TAG_FIRST = "first";
	public final static String TAB_TAG_SECOND = "second";
	public final static String TAB_TAG_THIRD = "third";
	public final static String TAB_TAG_FORTH = "forth";
	public final static String TAB_TAG_FIFTH = "fifth";
	public final static String TAB_TAG_SIXTH = "sixth";
	
	public static TabHost tabHost;
	
	private RadioButton btn_first;
	private RadioButton btn_second;
	private RadioButton btn_third;
	private RadioButton btn_forth;
	private RadioButton btn_fifth;
	private RadioButton btn_sixth;
	
	private Intent first_intent;
	private Intent second_intent;
	private Intent third_intent;
	private Intent forth_intent;
	private Intent fifth_intent;
	private Intent sixth_intent;

	protected MainActivity myParent;
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tab_info);
		myParent = (MainActivity)this.getParent();
		
		//////////////////// init ///////////////////////////
		first_intent = new Intent(this, OneActivity.class);
		first_intent.putExtra("url", Constants.URL_INFO_01);
		
		second_intent = new Intent(this, TwoActivity.class);
		second_intent.putExtra("url", Constants.URL_INFO_02);
		
		third_intent = new Intent(this, ThreeActivity.class);
		third_intent.putExtra("url", Constants.URL_INFO_03);
		
		forth_intent = new Intent(this, FourActivity.class);
		forth_intent.putExtra("url", Constants.URL_INFO_04);
		
		fifth_intent = new Intent(this, FiveActivity.class);
		fifth_intent.putExtra("url", Constants.URL_INFO_05);
		
		sixth_intent = new Intent(this, SixActivity.class);
		sixth_intent.putExtra("url", Constants.URL_INFO_06);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FIRST).setIndicator(TAB_TAG_FIRST).setContent(first_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_SECOND).setIndicator(TAB_TAG_SECOND).setContent(second_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_THIRD).setIndicator(TAB_TAG_THIRD).setContent(third_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FORTH).setIndicator(TAB_TAG_FORTH).setContent(forth_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FIFTH).setIndicator(TAB_TAG_FIFTH).setContent(fifth_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_SIXTH).setIndicator(TAB_TAG_SIXTH).setContent(sixth_intent));
	
		////////////////////// find View ////////////////////////////
		btn_first = (RadioButton)this.findViewById(R.id.btn_info_first);
		btn_second = (RadioButton)this.findViewById(R.id.btn_info_second);
		btn_third = (RadioButton)this.findViewById(R.id.btn_info_third);
		btn_forth = (RadioButton)this.findViewById(R.id.btn_info_forth);
		btn_fifth = (RadioButton)this.findViewById(R.id.btn_info_fifth);
		btn_sixth = (RadioButton)this.findViewById(R.id.btn_info_sixth);
		
		/*修改二级栏目名称*/
		SharedPreferences sp = getSharedPreferences("top_name",MODE_PRIVATE);
		btn_first.setText(sp.getString("third1","人才"));
		btn_second.setText(sp.getString("third2", "房产"));
		btn_third.setText(sp.getString("third3","二手"));
		btn_forth.setText(sp.getString("third4", "促销"));
		btn_fifth.setText(sp.getString("third5","活动"));
		btn_sixth.setText(sp.getString("third6","团购"));
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_first.setOnClickListener(listener);
		btn_second.setOnClickListener(listener);
		btn_third.setOnClickListener(listener);
		btn_forth.setOnClickListener(listener);
		btn_fifth.setOnClickListener(listener);
		btn_sixth.setOnClickListener(listener);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("infoactivity", "info--resume");
		
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
					InfoActivity.this.getParent().showDialog(Constants.DIALOG_EXITAPP_ID);
				}else{
					InfoActivity.this.startActivityForResult(new Intent(InfoActivity.this, LoginActivity.class), 200);
				}
			}
		});
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_info_first:
				tabHost.setCurrentTabByTag(TAB_TAG_FIRST);
				break;
			case R.id.btn_info_second:
				tabHost.setCurrentTabByTag(TAB_TAG_SECOND);
				break;
			case R.id.btn_info_third:
				tabHost.setCurrentTabByTag(TAB_TAG_THIRD);
				break;
			case R.id.btn_info_forth:
				tabHost.setCurrentTabByTag(TAB_TAG_FORTH);
				break;
			case R.id.btn_info_fifth:
				tabHost.setCurrentTabByTag(TAB_TAG_FIFTH);
				break;
			case R.id.btn_info_sixth:
				tabHost.setCurrentTabByTag(TAB_TAG_SIXTH);
				break;
			}
		}
	}
}
