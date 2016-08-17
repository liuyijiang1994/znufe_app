
package com.special.ResideMenuDemo;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.text.InputType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iceman.yangtze.Globe;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.special.ResideMenuDemo.R;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;

/**
 * 登陆待完成
 * 
 * @author Administrator
 */
public class EvaluateSubmit extends WindowActivity {
	private Button mLoginButton;

	private CheckBox mSavePasswordBox;

	private static EditText mUserName;

	private static EditText mPassword;

	private ArrayList<String[]> hide_params = Globe.sHideParams;

	private static SharedPreferences mLoginInfoPreferences;
	private RadioButton Button1 = null; 
	private RadioButton Button2 = null;
	private RadioButton Button3 = null;
	private RadioButton Button4 = null;
	private RadioGroup radiogroup =null;
	private String goodOrNot="1.0";
	private EditText pingjiao;
	private boolean isChecked=false;
	private String evaluate=null;
	private Button backBtn;
	private  MyHttpRequest req;
	@SuppressWarnings("static-access")
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//if(PreferenceTestMain.isFirst)
		setContentView(R.layout.evaluatesubmit);

		mLoginInfoPreferences = getSharedPreferences("logininfo", MODE_PRIVATE);

		/*MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, "http://202.114.224.81:8088/jxpg/main.jsp", null, false);
        req.setPipIndex(NetConstant.LOGIN);
        mNetClient.sendRequest(req);      */        
		Button1 = (RadioButton)findViewById(R.id.radioButton1); 
		Button2 = (RadioButton)findViewById(R.id.radioButton2); 
		Button3 = (RadioButton)findViewById(R.id.radioButton3); 
		Button4 = (RadioButton)findViewById(R.id.radioButton4); 
		radiogroup = (RadioGroup)findViewById(R.id.radiogroup); 
		pingjiao = (EditText) findViewById(R.id.pingjiao);
		backBtn=(Button) findViewById(R.id.btn);
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EvaluateSubmit.this,LoginedEvaluate.class));
				finish();
			}

		});
		//pingjiao.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
		//为RadioGroup设置监听器，需要注意的是，这里的监听器和Button控件的监听器有所不同 

		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { 

			public void onCheckedChanged(RadioGroup group, int checkedId) { 
				// TODO Auto-generated method stub 
				if(Button1.getId() == checkedId){ 

					goodOrNot="1.0";

				} 
				else if(Button2.getId() == checkedId){ 

					goodOrNot="0.8";                	

				} 
				else if(Button3.getId() == checkedId){ 

					goodOrNot="0.6";

				} 
				else if(Button4.getId() == checkedId){ 

					goodOrNot="0.4";

				} 

			} 
		}); 


		Button ok = (Button) findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!LoginedEvaluate.isLast)
				{
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("0000000008", goodOrNot));
					params.add(new BasicNameValuePair("0000000009", goodOrNot));
					params.add(new BasicNameValuePair("0000000010", goodOrNot));
					params.add(new BasicNameValuePair("0000000011", goodOrNot));
					params.add(new BasicNameValuePair("0000000013", goodOrNot));
					params.add(new BasicNameValuePair("0000000015", goodOrNot));
					params.add(new BasicNameValuePair("0000000020", goodOrNot));
					params.add(new BasicNameValuePair("0000000021", goodOrNot));
					params.add(new BasicNameValuePair("0000000022", goodOrNot));
					params.add(new BasicNameValuePair("0000000023", goodOrNot));
					params.add(new BasicNameValuePair("0000000024", goodOrNot));
					params.add(new BasicNameValuePair("0000000025", goodOrNot));
					params.add(new BasicNameValuePair("0000000070", goodOrNot));
					params.add(new BasicNameValuePair("0000000071", goodOrNot));
					params.add(new BasicNameValuePair("zgpj", pingjiao.getEditableText().toString()));
					params.add(new BasicNameValuePair("kg", "是"));
					req = new MyHttpRequest(NetConstant.TYPE_POST,"http://202.114.224.81:8088/jxpg/answer.jsp?wj_num="+LoginedEvaluate.num, params, true);
					req.setPipIndex(NetConstant.LOGIN);
					mNetClient.sendRequest(req);
				}
				else
				{
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("0000000026", goodOrNot));
					params.add(new BasicNameValuePair("0000000027", goodOrNot));
					params.add(new BasicNameValuePair("0000000028", goodOrNot));
					params.add(new BasicNameValuePair("0000000029", goodOrNot));
					params.add(new BasicNameValuePair("0000000030", goodOrNot));
					params.add(new BasicNameValuePair("0000000031", goodOrNot));
					params.add(new BasicNameValuePair("0000000032", goodOrNot));
					params.add(new BasicNameValuePair("0000000033", goodOrNot));
					params.add(new BasicNameValuePair("0000000034", goodOrNot));
					params.add(new BasicNameValuePair("0000000035", goodOrNot));
					params.add(new BasicNameValuePair("0000000072", goodOrNot));
					params.add(new BasicNameValuePair("0000000073", goodOrNot));
					params.add(new BasicNameValuePair("0000000074", goodOrNot));
					params.add(new BasicNameValuePair("0000000075", goodOrNot));
					params.add(new BasicNameValuePair("zgpj", pingjiao.getEditableText().toString()));
					params.add(new BasicNameValuePair("kg", "是"));
					req = new MyHttpRequest(NetConstant.TYPE_POST,"http://202.114.224.81:8088/jxpg/answer.jsp?wj_num="+LoginedEvaluate.num, params, true);
					req.setPipIndex(NetConstant.LOGIN);
					mNetClient.sendRequest(req);
				}
				//onBackPressed();
				startActivity(new Intent(EvaluateSubmit.this,LoginedEvaluate.class));
				EvaluateSubmit.this.showNetLoadingDialog();
				mNetLoadingDialog.dismiss();
				finish();
			}

		});
		
		System.out.print("网上评教提交");
	}


	public void handResponse(MyHttpResponse myHttpResponse) {
		System.out.println("处理一个响应");
		Document doc;
		if (myHttpResponse.getPipIndex() == NetConstant.LOGIN) {//
			doc = myHttpResponse.getData();
			System.out.println(doc);
			dismissNetLoadingDialog();
			// Element info = doc.getElementById("lbPrompt");
			Elements info = doc.select("body");
			System.out.println("最后一晚上3");
			//有问题？3333333333

		}

		else{//
			System.out.println("最后一晚上7");
		}                  

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {  

		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			startActivity(new Intent(EvaluateSubmit.this,LoginedEvaluate.class));
			finish();
			return true;  
		}  
		return super.onKeyDown(keyCode, event);  

	}  

}
