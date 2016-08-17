package net.shopnc.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
import net.shopnc.android.ui.forum.ForumActivity;
import net.shopnc.android.ui.info.FourActivity;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
/**
 * 欢迎界面
 * @author hjgang
 */
public class StartActivity extends Activity {
	private static final String TAG = "StartActivity";
	private MyApp myApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApp)this.getApplication();
        
        setContentView(R.layout.start);     
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

               
        if(-1 == SystemHelper.getNetworkType(StartActivity.this)){
        	createSetNetworkDialog().show();
        }else{
        	new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					initDataAndStartMain();
				}
			}, 1);
		}
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
        	final User db_user = new UserDao(StartActivity.this).get(myApp.getUseracc());
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
       
        //启动主界面
        Intent intent = new Intent(StartActivity.this, ForumActivity.class);
        //加载第一屏数据
        PushData top = loadFirstHeadlineData();
        intent.putExtra("top", top);
        StartActivity.this.startActivity(intent);
        StartActivity.this.finish();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == 0){
    		if(-1 != SystemHelper.getNetworkType(StartActivity.this)){
    			initDataAndStartMain();
            }else{
            	Toast.makeText(StartActivity.this, "网络连接仍不可用，无法访问服务器数据！", Toast.LENGTH_SHORT).show();
//            	Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            	//intent.putExtra("top", );
//            	StartActivity.this.startActivity(intent);
        	StartActivity.this.finish();
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
				StartActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
		        StartActivity.this.finish();
//            	Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            	StartActivity.this.startActivity(intent);
			}
		});
		return builder.create();
	}
}