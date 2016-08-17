package com.special.ResideMenuDemo;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.testappweight.AppWidget;
import com.iceman.yangtze.Globe;
import com.iceman.yangtze.NetHelper;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LoginService extends Service{
	public static boolean isLogin=false;
	private int flag=0;
	private boolean quit=false;
	public  Handler tempServiceHandle=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				System.out.println("运行停止service");
				stopSelf();
				onDestroy();
				break;
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		// name = StartPage.settings.getString("loginname", null);
		//pwd= StartPage.settings.getString("loginpassword",null);
		newUser thread = new newUser();
		thread.start();
		// Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it
		// background priority so CPU-intensive work will not disrupt our UI.

		// Get the HandlerThread's Looper and use it for our Handler
		//mServiceLooper = thread.getLooper();
		//mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	/*@SuppressWarnings("deprecation")
	@Override 
	public void onStart(Intent intent, int startId) {
		 super.onStart(intent, startId);
		 name = StartPage.settings.getString("loginname", null);
		 pwd= StartPage.settings.getString("loginpassword",null);
		 if(NetHelper.IsHaveInternet(getBaseContext())==true&&name!=null&&pwd!=null){
				flag=1;
		 newUser thread = new newUser();
			thread.start();
		 }
		   // if(isLogin) 
		    //	stopSelf() ;
	        Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();

	    }
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	     // Message msg = mServiceHandler.obtainMessage();
	     // msg.arg1 = startId;
	    //  mServiceHandler.sendMessage(msg);

	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }*/

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		stopSelf();
		//Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	class newUser extends Thread
	{

		public void run()
		{
			Looper.prepare();  
			System.out.println("haizaizhixing service");
			while (!quit) {  
				try {  
					Thread.sleep(1000);//每5分钟检查一次  
				} catch (InterruptedException e) {  
					e.printStackTrace();  
				}  					

				if(NetHelper.IsHaveInternet(getBaseContext())==true&&StartPage.name!=null&&StartPage.pwd!=null&&Globe.sCookieString==null){
					//Log.d("SharedPreferences", "name:" + name + ", age:" + pwd);
					Log.d("SharedPreferences", "获取数据成功");
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("stuid", StartPage.name));
					params.add(new BasicNameValuePair("pwd", StartPage.pwd));
					Evaluate.userId = new String(StartPage.name);
					Evaluate.pwd = new String(StartPage.pwd);
					OnLineCourse.stuid=new String(StartPage.name);
					System.out.println("在OnLineCourse里学号："+OnLineCourse.stuid+"在AppWidget里学号为："+AppWidget.stuidAppWidget);
					//StartPage.isShowNetDialog=false;
					MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_POST, NetConstant.URL_LOGIN2, params, true);
					req.setPipIndex(NetConstant.LOGIN);						
					WindowActivity.mNetClient.sendRequest(req);
					flag=1;
					quit=true;
					//new newUser(name).start();
				}
				Message msg=new Message();
				msg.what=flag;						
				tempServiceHandle.sendMessage(msg);
			}
			
			//StartPage.isShowNetDialog=true;

		}

	}
}
