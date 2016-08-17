/**
 *  ClassName: ForumActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.special.ResideMenuDemo.MenuActivity;
import com.special.ResideMenuDemo.R;

import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.dao.UserDao;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.PushData;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Smiley;
import net.shopnc.android.model.User;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.StartActivity;
import net.shopnc.android.ui.info.FourActivity;
import net.shopnc.android.ui.more.LoginActivity;
import net.shopnc.android.ui.more.MoreActivity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 论坛Tab界面
 * @author qjyong
 */
public class ForumActivity extends TabActivity {
	/** tab标签名*/
	private static final String TAG = "StartActivity";
	private MyApp myApp;
	public final static String TAB_TAG_LIST = "board_list";
	public final static String TAB_TAG_FAVORITE = "board_favorite";
	public final static String TAB_TAG_BROWSE = "lastest_browse";
	
	public static TabHost tabHost;
	public static TextView titleleft_btn;
	
	private RadioButton btn_board_list;
	private RadioButton btn_board_favorite;
	private RadioButton btn_lastest_browse;
	
	private Intent board_list_intent;
	private Intent board_favorite_intent;
	private Intent lastest_browse_intent;
	private Intent more_intent;
	
	protected MainActivity myParent;
	
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.tab_forum);
		 myApp = (MyApp)this.getApplication();
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
						.detectDiskReads().detectDiskWrites().detectNetwork()
						.penaltyLog().build());

				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
						.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
						.build());
	        /*获取二级栏目名称*/       
	       SharedPreferences sp = getSharedPreferences("top_name", MODE_PRIVATE);
	       Editor editor = sp.edit();//获取编辑器
	       editor.clear();
	        JSONObject object;
	        JSONArray array;
			try {
				object = new JSONObject(RemoteDataHandler.loadTopName());
				array=object.getJSONArray("datas");
				
				JSONObject item=array.getJSONObject(0);
				for(int j =1;j<6;j++)
				{					
					String index=item.getString("index"+j);
					editor.putString("index"+j, index);
					String second=item.getString("second"+j);
					editor.putString("second"+j, second);
					String third=item.getString("third"+j);
					editor.putString("third"+j, third);
				}		
				String second=item.getString("second"+6);
				editor.putString("second"+6, second);
				String third=item.getString("third"+6);
				editor.putString("third"+6, third);			
				editor.commit();
			} catch (JSONException e) {
				e.printStackTrace();
			}	

	               
	        if(-1 == SystemHelper.getNetworkType(ForumActivity.this)){
	        	createSetNetworkDialog().show();
	        }else{
	        	new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						initDataAndStartMain();
					}
				}, 1);
			}
		
		myParent = (MainActivity)this.getParent();
		
		//////////////////// init ///////////////////////////
		board_list_intent = new Intent(this, BoardListActivity.class);
		board_favorite_intent = new Intent(this, BoardFavoriteActivity.class);
		lastest_browse_intent = new Intent(this, LastestBrowseActivity.class);
		more_intent = new Intent(this,MoreActivity.class);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_LIST).setIndicator(TAB_TAG_LIST).setContent(board_list_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_FAVORITE).setIndicator(TAB_TAG_FAVORITE).setContent(board_favorite_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_BROWSE).setIndicator(TAB_TAG_BROWSE).setContent(lastest_browse_intent));
	
		////////////////////// find View ////////////////////////////
		btn_board_list = (RadioButton)this.findViewById(R.id.btn_forum_board_list);
		btn_board_favorite = (RadioButton)this.findViewById(R.id.btn_forum_board_favorite);
		btn_lastest_browse = (RadioButton)this.findViewById(R.id.btn_forum_lastest_browse);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_board_list.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
		btn_lastest_browse.setOnClickListener(listener);
	}
	
	private PushData loadFirstHeadlineData(){
    	ResponseData data = RemoteDataHandler.get(Constants.URL_HOME_TOP, 1, 1);
		
		if(data.getCode() == HttpStatus.SC_OK){
			String json = data.getJson();
			
			ArrayList<PushData> topDatas = PushData.newInstanceList(json);
			Log.d(TAG, topDatas.toString());
			
			if(topDatas.size() > 0){
				PushData pd = topDatas.get(0);
				ImageLoader.getInstance().downloadBitmap(pd.getPic());
				return pd;
			}
		}
		
		return null;
    }
    
    private void initDataAndStartMain(){
    	//自动登录
        if(myApp.isAuto_login()){ 
        	final User db_user = new UserDao(ForumActivity.this).get(myApp.getUseracc());
        	if(null != db_user){
	        	
	        	HashMap<String, String> params = new HashMap<String, String>();
	    		params.put("useracc", db_user.getAuthor());
	    		params.put("userpw", db_user.getPwd());
	    		
	    		ResponseData data = RemoteDataHandler.post(Constants.URL_LOGIN, params);

				String json = data.getJson();
				
				User user = User.newInstance(json);
				String sessionid = user.getSessionid();
				
				if(null != sessionid && !"aperror".equals(sessionid)){
					myApp.setUseracc(db_user.getAuthor());
					myApp.setSid(user.getSessionid());
	        		myApp.setUid(user.getAuthorid());
	        		myApp.setGroupid(user.getGroupid());
	        		
	        		//QIUJY 还需要更新版块列表的权限
	        		myApp.setSubBoardMap(RemoteDataHandler.loadSubBoardMap(user.getAuthorid()));
	        	}else{
	        		//myApp.setSid("");
	        		//myApp.setUid("");
	        		//myApp.setUseracc("");
	        		//myApp.setGroupid("");
	        	}
        	}
        }
    
        //加载表情
        ArrayList<Smiley> faceList = RemoteDataHandler.loadSmiley();
        myApp.setFaceList(faceList);
       

        //加载第一屏数据
        PushData top = loadFirstHeadlineData();

    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == 0){
    		if(-1 != SystemHelper.getNetworkType(ForumActivity.this)){
    			initDataAndStartMain();
            }else{
            	Toast.makeText(ForumActivity.this, "网络连接仍不可用，无法访问服务器数据！", Toast.LENGTH_SHORT).show();
//            	Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            	//intent.putExtra("top", );
//            	StartActivity.this.startActivity(intent);
            	ForumActivity.this.finish();
            }
    	}
    }
    
    /**
	 * 弹出设置网络连接的提示
	 * @param activity
	 */
	public AlertDialog createSetNetworkDialog(){
		Builder builder = new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("请打开网络连接")
			.setMessage("网络连接不可用，立即设置？");
		
		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
				startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);//进入无线网络配置界面
		        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //进入手机中的wifi网络设置界面
				ForumActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				ForumActivity.this.finish();
//            	Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            	StartActivity.this.startActivity(intent);
			}
		});
		return builder.create();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
		&& event.getAction() == KeyEvent.ACTION_DOWN
		&& event.getRepeatCount() == 0) { 
				Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
				startActivity(intent);
				ForumActivity.this.finish();
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Toast.makeText(getApplicationContext(), "on resume", Toast.LENGTH_SHORT ).show();
		Log.d("homeactivity", "home--resume");
		//退出
		ImageButton btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		final MyApp myApp = (MyApp)getApplication();
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
			btn_right.setBackgroundResource(R.drawable.ic_menu_setting);

		}else{
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
		
		btn_right.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录
						startActivity(more_intent);
				}else{
					ForumActivity.this.startActivityForResult(new Intent(ForumActivity.this, LoginActivity.class), 200);
				}
			}
		});
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_forum_board_list:
				tabHost.setCurrentTabByTag(TAB_TAG_LIST);
				break;
			case R.id.btn_forum_board_favorite:
				tabHost.setCurrentTabByTag(TAB_TAG_FAVORITE);
				break;
			case R.id.btn_forum_lastest_browse:
				tabHost.setCurrentTabByTag(TAB_TAG_BROWSE);
				break;
			}
		}
	}
}
