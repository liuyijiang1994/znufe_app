/**
 *  ClassName: RegActivity.java
 *  created on 2014-8-15
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author qjyong
 */
public class RegActivity extends Activity {
	private MyApp myApp;
	
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	
	private EditText txt_loginname;
	private EditText txt_pwd;
	private EditText txt_email;

	private Button btn_reg;
	
	private String result="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		setContentView(R.layout.register);
		
		initTitleBar();
		initContent();
	}
	
	private void initTitleBar(){
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.reg));
		
		//设置标题栏按钮
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegActivity.this.finish();
			}
		});
		
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.INVISIBLE);
	}
	
	private void initContent(){
		txt_loginname=(EditText)this.findViewById(R.id.txt_regname);
		txt_pwd=(EditText)this.findViewById(R.id.txt_pwd);
		txt_email=(EditText)this.findViewById(R.id.regemail);
		btn_reg=(Button)this.findViewById(R.id.btn_reg);
		
		btn_reg.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(txt_loginname.getText().toString()==null || txt_loginname.getText().length()==0
						|| txt_pwd.getText().toString()==null || txt_pwd.getText().length()==0
							|| txt_email.getText().toString()==null || txt_email.getText().length()==0)
				{
					Toast.makeText(getApplicationContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
				}
				else {
					new RegMachine(txt_loginname.getText().toString(), txt_pwd.getText().toString(), txt_email.getText().toString()).start();
				}			
			}
		});
	}
	
	private Handler mUIHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case  1:
					Toast.makeText(getApplicationContext(), "您的请求被外星人劫持了，请稍后再试", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
					if(result.indexOf("成功")>0)
					{
						RegActivity.this.finish();
					}
					break;
			}			
		}
	};
	
	class RegMachine extends Thread
	{
		private String name,pwd,email;
		
		public RegMachine(String name, String pwd,  String mail)
		{
			this.name=name;
			this.pwd=pwd;
			this.email=mail;
		}
		
		public void run()
		{
			InputStream is = null;
	        try{  
				HttpClient httpclient = new DefaultHttpClient();  
		        HttpPost httppost = new HttpPost("http://202.114.234.122/bbs/dev/examples/reg.php?username="+this.name+"&password="+this.pwd+"&email="+this.email);  
		        httppost.setEntity(null); 
		        Log.v("URL", "http://202.114.234.122/bbs/dev/examples/reg.php?username="+this.name+"&password="+this.pwd+"&email="+this.email);
		        
		        
		        System.out.println("开始连接");
		        HttpResponse response = httpclient.execute(httppost); 
		        System.out.println("连接成功");
		        HttpEntity entity = response.getEntity();  
		        is = entity.getContent(); 
		  }catch(Exception e){  
		          Log.e("log_tag", "Error in http connection "+e.toString()); 
		          System.out.println("连接失败");
		          Message message = new Message();
		          message.what=1;
		          mUIHandler.sendMessage(message);
		  }  
		  //将HttpEntity转化为String  
		  try{  
		          BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);  
		          StringBuilder sb = new StringBuilder();  
		          String line = null;  
		          while ((line = reader.readLine()) != null) {  
		                  sb.append(line + "\n");  
		          }  
		          is.close();  
		     
		          result=sb.toString();  
			        Log.v("result",result);
			        Message message = new Message();
			        message.what=2;
			        mUIHandler.sendMessage(message);
			        
		  }catch(Exception e){  
		          Log.e("log_tag", "Error converting result "+e.toString());  
		          Message message = new Message();
		          message.what=1;
		          mUIHandler.sendMessage(message);
		  }  
		}
	}
}
	
	