
package com.special.ResideMenuDemo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.layout;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SeekBar;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;

public class EvaluateOpen extends WindowActivity {

	private static final Object[] GradeInfo = null;
	ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
    private MyAdapter adapter;
    private ListView listView;
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.evaluatesubmit);
        
		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET,"http://202.114.224.81:8088/jxpg/pg.jsp?wj_num="+LoginedEvaluate.num, null,true);
        req.setPipIndex(NetConstant.LOGIN);
        mNetClient.sendRequest(req);
        
       
		System.out.print("已经登录网上评教。。。。");
		
	}
	
	
    public void handResponse(MyHttpResponse myHttpResponse) {
    	 Document doc = myHttpResponse.getData();
         System.out.println(doc);
         Intent it = new Intent(EvaluateOpen.this, EvaluateSubmit.class);
         startActivity(it);
         finish();
    }

}
