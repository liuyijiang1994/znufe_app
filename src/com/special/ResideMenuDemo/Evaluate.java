
package com.special.ResideMenuDemo;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.iceman.yangtze.Globe;
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
public class Evaluate extends WindowActivity {
    private Button mLoginButton;

    private CheckBox mSavePasswordBox;

    private EditText mUserName, mPassword;

    private ArrayList<String[]> hide_params = Globe.sHideParams;

    private SharedPreferences mLoginInfoPreferences;
    public static String userId;
	public static String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.evaluate);
       
//        MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, NetConstant.URL_LOGIN, null, false);
//        req.setPipIndex(NetConstant.HOMEPAGE);
//        mNetClient.sendRequest(req);
        

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                
                params.add(new BasicNameValuePair("userId", userId));
                params.add(new BasicNameValuePair("pwd", pwd));
                params.add(new BasicNameValuePair("identity", "student"));
                params.add(new BasicNameValuePair("Submit","登  陆"));
                
                //Evaluate.userId = new String(mUserName.getEditableText().toString().trim());
                //Evaluate.pwd = new String(mPassword.getEditableText().toString().trim());
               // params.add(new BasicNameValuePair("selKind", "1"));
// 改的部分    for (String[] arr : hide_params) {
 //改的部分  params.add(new BasicNameValuePair(arr[0], arr[1]));
 //改的部分 System.out.println("放入参数:" + arr[0] + "\n" + arr[1]);
                //}
                MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_POST, NetConstant.URL_LOGIN_EVALUATE, params, true);
                req.setPipIndex(NetConstant.LOGIN);
                mNetClient.sendRequest(req);
                //LoginScreen.this.showNetLoadingDialog();
                // post.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
           
      
    }

    public void handResponse(MyHttpResponse myHttpResponse) {
        System.out.println("处理一个响应");
        Document doc;
        if (myHttpResponse.getPipIndex() == NetConstant.LOGIN) {
            doc = myHttpResponse.getData();
             System.out.println(doc);
  //          dismissNetLoadingDialog();
   //        // Element info = doc.getElementById("lbPrompt");
  //          Elements info = doc.select("H3");
 //           if (info.get(0).text().contains("学院")) {
                System.out.println("登陆成功");
  //              String[] userinfo = info.text().split(" ");
 //               Globe.sId = userinfo[0];
 //               Globe.sName = userinfo[2];
 //               Globe.sClassName = userinfo[1];
                
                Intent it = new Intent(Evaluate.this, LoginedEvaluate.class);
                startActivity(it);
                finish();
  //          } else {
  //              Elements errorinfo = doc.select("font");
   //             if (errorinfo.get(0).text().contains("错误的学号或密码")) {
   //                 showTipDialog("账号/密码错误");
  //                  System.out.println("账号/密码错误");
 //               }
  //          }

        }
    }

}
