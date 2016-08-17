package net.shopnc.android.ui;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.model.PushData;
import net.shopnc.android.ui.forum.ForumActivity;
import net.shopnc.android.ui.home.HomeActivity;
import net.shopnc.android.ui.info.InfoActivity;
import net.shopnc.android.ui.live.LiveActivity;
import net.shopnc.android.ui.more.MoreActivity;
import net.shopnc.android.widget.MyProcessDialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * 主Tab界面
 * @author qjyong
 */
public class MainActivity extends TabActivity {
	/** tab标签名*/
	public final static String TAB_TAG_HOME = "home";
	public final static String TAB_TAG_LIVE = "live";
	public final static String TAB_TAG_FORUM = "forum";
	public final static String TAB_TAG_INFO = "info";
	public final static String TAB_TAG_MORE = "more";
	
	private TabHost tabHost;
	
	/** 启动每个操作项的Intent */
	private Intent homeIntent;
	private Intent liveIntent;
	private Intent forumIntent;
	private Intent info;
	private Intent moreIntent;
	
	/** 界面上的各个单选按钮 */
	private RadioButton btn_home;
	private RadioButton btn_live;
	private RadioButton btn_forum;
	private RadioButton btn_info;
	private RadioButton btn_more;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		
		//////////////////// init ///////////////////////////
		
		homeIntent = new Intent(this, HomeActivity.class);
		if(this.getIntent().hasExtra("top")){
			Object obj = this.getIntent().getExtras().get("top");
			homeIntent.putExtra("top", (PushData)obj);
		}
		
		liveIntent = new Intent(this, LiveActivity.class);
		forumIntent = new Intent(this, ForumActivity.class);
		info = new Intent(this, InfoActivity.class);
		moreIntent = new Intent(this, MoreActivity.class);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_HOME).setIndicator(TAB_TAG_HOME).setContent(homeIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_LIVE).setIndicator(TAB_TAG_LIVE).setContent(liveIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FORUM).setIndicator(TAB_TAG_FORUM).setContent(forumIntent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_INFO).setIndicator(TAB_TAG_INFO).setContent(info));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_MORE).setIndicator(TAB_TAG_MORE).setContent(moreIntent));
	
		////////////////////// find View ////////////////////////////
		btn_home = (RadioButton)this.findViewById(R.id.main_tab_home);
		btn_live = (RadioButton)this.findViewById(R.id.main_tab_live);
		btn_forum = (RadioButton)this.findViewById(R.id.main_tab_forum);
		btn_info = (RadioButton)this.findViewById(R.id.main_tab_info);
		btn_more = (RadioButton)this.findViewById(R.id.main_tab_more);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_home.setOnClickListener(listener);
		btn_live.setOnClickListener(listener);
		btn_forum.setOnClickListener(listener);
		btn_info.setOnClickListener(listener);
		btn_more.setOnClickListener(listener);
		
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.main_tab_home:
				tabHost.setCurrentTabByTag(TAB_TAG_HOME);
				break;
			case R.id.main_tab_live:
				tabHost.setCurrentTabByTag(TAB_TAG_LIVE);
				break;
			case R.id.main_tab_forum:
				tabHost.setCurrentTabByTag(TAB_TAG_FORUM);
				break;
			case R.id.main_tab_info:
				tabHost.setCurrentTabByTag(TAB_TAG_INFO);
				break;
			case R.id.main_tab_more:
				tabHost.setCurrentTabByTag(TAB_TAG_MORE);
				break;
			}
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == Constants.DIALOG_LOADDATA_ID){
			return createProgressDialog();
		}else if(id == Constants.DIALOG_EXITAPP_ID){
			return createExitDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private Dialog createProgressDialog(){
		/**
		ProgressDialog progressDialog =  new ProgressDialog(this,R.style.MyProgressDialog);
		progressDialog.setMessage("网络数据请求中，请耐心等候！");
		*/
		MyProcessDialog progressDialog = new MyProcessDialog(this);
		progressDialog.setMsg(this.getString(R.string.footview_wait));
		
		return progressDialog;
	}
	
	/**
	 * 弹出关闭Activity的提示
	 * @param activity
	 */
	public AlertDialog createExitDialog(){
		Builder builder = new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("确认退出")
			.setMessage("您真的要退出程序吗？");
		
//		final CheckBox cbx = new CheckBox(this);
//		if(!SystemHelper.hasShortCut(this)){
//			cbx.setText("是否创建桌面快捷方式？");
//			cbx.setChecked(true);
//			builder.setView(cbx);
//		}
		
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
//				if(cbx.isChecked()){
//					SystemHelper.createShortcut(MainActivity.this, StartActivity.class);
//				}
				
				MainActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
}
