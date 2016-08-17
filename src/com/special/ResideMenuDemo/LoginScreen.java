
package com.special.ResideMenuDemo;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testappweight.AppWidget;
import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * 登陆待完成
 * 
 * @author Administrator
 */
public class LoginScreen extends WindowActivity {
	private Button mLoginButton;

	private CheckBox mSavePasswordBox;

	private static EditText mUserName;

	private static EditText mPassword;

	private static SharedPreferences mLoginInfoPreferences;
	static boolean isPingjiao=true;

	@SuppressWarnings("static-access")
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		mLoginButton = (Button) findViewById(R.id.login_btn);
		mUserName = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		mUserName.setTextColor(R.color.color100);
		mPassword.setTextColor(R.color.color100);
		mSavePasswordBox = (CheckBox) findViewById(R.id.remember_password);
		mLoginInfoPreferences = getSharedPreferences("logininfo", MODE_PRIVATE);
		mUserName.setText(mLoginInfoPreferences.getString("loginname", ""));
		mPassword.setText(mLoginInfoPreferences.getString("loginpassword", ""));
		if(HomePageScreen.isExit)
		{
			mUserName.setText("");
			mPassword.setText("");
			HomePageScreen.isExit=false;
		}		
		this.getApplicationContext();
		ConnectivityManager cm=(ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo(); 

		if (info != null && info.isAvailable()){ 
		}else{ 
			Toast.makeText(this.getApplicationContext(), "未检测到可用网络，请检查网络连接。", Toast.LENGTH_LONG).show();
		} 			       	 

		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("stuid", mUserName.getEditableText().toString().trim()));
				params.add(new BasicNameValuePair("pwd", mPassword.getEditableText().toString().trim()));
				Evaluate.userId = new String(mUserName.getEditableText().toString().trim());
				Evaluate.pwd = new String(mPassword.getEditableText().toString().trim());
				OnLineCourse.stuid=new String(mUserName.getEditableText().toString().trim());
				AppWidget.stuidAppWidget=new String(mUserName.getEditableText().toString().trim());
				MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_POST, NetConstant.URL_LOGIN2, params, true);
				req.setPipIndex(NetConstant.LOGIN);
				mNetClient.sendRequest(req);
				new newUser(mUserName.getEditableText().toString().trim()).start();
				LoginScreen.this.showNetLoadingDialog();
				Intent intent=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
				LoginScreen.this.sendBroadcast(intent);
			}
		});
	}
	class newUser extends Thread
	{
		public String id;

		public newUser(String id)
		{
			this.id=id;
		}

		public void run()
		{
			try{  
				HttpClient httpclient = new DefaultHttpClient();  
				HttpPost httppost = new HttpPost("http://202.114.234.122:8234/tecApp/mb_newuser.php?userid="+id); 
				httppost.setEntity(null); 

				System.out.println("开始连接");
				HttpResponse response = httpclient.execute(httppost); 
				System.out.println("连接成功");
				response.getEntity();
			}catch(Exception e){  
				Log.e("log_tag", "Error in http connection "+e.toString()); 
				System.out.println("连接失败");
			}  
		}

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	    
	}

	public void handResponse(MyHttpResponse myHttpResponse) {
		System.out.println("处理一个响应");
		Document doc;
		if (myHttpResponse.getPipIndex() == NetConstant.LOGIN) {//
			doc = myHttpResponse.getData();
			dismissNetLoadingDialog();
			Elements info = doc.select("body");
			System.out.println("最后一晚上3");
			if(info.get(0).text().contains("教学评估"))
				isPingjiao=false;
			if (info.get(0).text().contains("学院")||info.get(0).text().contains("教学评估"))
			{
				System.out.println("最后一晚上4");
				System.out.println("登陆成功");
				info.text().split(" ");
				if (mSavePasswordBox.isChecked()) {
					System.out.println("最后一晚上5");
					Log.d("SharedPreferences", "保存数据...");
					mLoginInfoPreferences= this.getSharedPreferences("shared_file", 0);
					SharedPreferences.Editor edit = mLoginInfoPreferences.edit();
					edit.putString("loginname", mUserName.getEditableText().toString().trim());
					edit.putString("loginpassword", mPassword.getEditableText().toString().trim());
					edit.commit();
					Log.d("SharedPreferences", "保存数据成功");
				}
				onBackPressed();
				finish();
				System.out.println("12312412342352345");
			} 
			else {
				System.out.println("最后一晚上6");
				doc.select("font");
				showTipDialog("账号/密码错误");
				System.out.println("账号/密码错误");
			}  
		}
		else{
			System.out.println("最后一晚上7");
		}
	}

}
