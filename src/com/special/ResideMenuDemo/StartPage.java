package com.special.ResideMenuDemo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.testappweight.AppWidget;
import com.iceman.yangtze.Globe;
import com.iceman.yangtze.NetHelper;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;
import com.special.ResideMenuDemo.LoginScreen.newUser;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class StartPage extends WindowActivity {
	public static String name;
	public static String pwd;
	public static  boolean isShowNetDialog;
	public static SharedPreferences settings;
	
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startpage);
		Intent intent = new Intent(this, LoginService.class);
		startService(intent);
		final Intent it = new Intent(this, MenuActivity.class); //你要转向的Activity  
		Timer startPageTimer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(it); //执行
				finish();
			}
		};
		startPageTimer.schedule(task, 1000 * 2); 
		Log.d("SharedPreferences", "获取数据...");
		settings = this.getSharedPreferences("shared_file", 0);
		name = settings.getString("loginname", null);
		pwd= settings.getString("loginpassword",null);
		/*if(NetHelper.IsHaveInternet(getBaseContext())==true&&name!=null&&pwd!=null){
			Log.d("SharedPreferences", "name:" + name + ", age:" + pwd);
			Log.d("SharedPreferences", "获取数据成功");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("stuid", name));
			params.add(new BasicNameValuePair("pwd", pwd));
			Evaluate.userId = new String(name);
			Evaluate.pwd = new String(pwd);
			OnLineCourse.stuid=new String(name);
			System.out.println("在OnLineCourse里学号："+OnLineCourse.stuid+"在AppWidget里学号为："+AppWidget.stuidAppWidget);
			isShowNetDialog=false;
			MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_POST, NetConstant.URL_LOGIN2, params, true);
			req.setPipIndex(NetConstant.LOGIN);
			mNetClient.sendRequest(req);
			//new newUser(name).start();
			}
		isShowNetDialog=true;*/
	}

	/*class newUser extends Thread
	{
		public String id;

		public newUser(String id)
		{
			this.id=id;
		}

		public void run()
		{
			try{  
				
				String name = settings.getString("loginname", null);
				String pwd= settings.getString("loginpassword",null);
				if(NetHelper.IsHaveInternet(getBaseContext())==true&&name!=null&&pwd!=null){
					Log.d("SharedPreferences", "name:" + name + ", age:" + pwd);
					Log.d("SharedPreferences", "获取数据成功");
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("stuid", name));
					params.add(new BasicNameValuePair("pwd", pwd));
					Evaluate.userId = new String(name);
					Evaluate.pwd = new String(pwd);
					OnLineCourse.stuid=new String(name);
					System.out.println("在OnLineCourse里学号："+OnLineCourse.stuid+"在AppWidget里学号为："+AppWidget.stuidAppWidget);
					isShowNetDialog=false;
					MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_POST, NetConstant.URL_LOGIN2, params, true);
					req.setPipIndex(NetConstant.LOGIN);
					mNetClient.sendRequest(req);
					//new newUser(name).start();
					}
				isShowNetDialog=true;
			}catch(Exception e){  
				Log.e("log_tag", "Error in http connection "+e.toString()); 
				System.out.println("连接失败");
			}  
		}

	}*/
	
	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {
		// TODO Auto-generated method stub
		
	}
	
	public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("StartPage"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MenuActivity"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
        MobclickAgent.onPause(this);
    }

}
