
package com.special.ResideMenuDemo;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;

/**
 * 登陆待完成
 * 
 * @author Administrator
 */
public class GradeSubmit extends WindowActivity {
	private ListView orderGradeList;
	private MyHttpRequest req;
	private Button btnGPABack;
	private OrderAdapter orderAdapter;
	private ArrayList<OrderInfo> infoBeans= new ArrayList<OrderInfo>();
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_grade);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("p_pm", GradeSearch.aa));
		req = new MyHttpRequest(NetConstant.TYPE_GET,"http://202.114.224.81:7777/pls/wwwbks/bkscjcx.cursco?jym2005=%22+time+%22"+GradeSearch.aa, null, true);
		req.setPipIndex(NetConstant.LOGIN);
		mNetClient.sendRequest(req);
		orderGradeList=(ListView) findViewById(R.id.orderList);
		btnGPABack=(Button) findViewById(R.id.gradebtn);
		btnGPABack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GradeSubmit.this,GradeSearch.class));
				finish();
			}
		});
	}


	public void handResponse(MyHttpResponse myHttpResponse) {
		System.out.println("进入排名解析");
		Document doc=myHttpResponse.getData();
		Elements table = doc.select("table");
		Elements trs=table.get(4).select("tr");
		Elements temp=trs.get(0).select("td");
		System.out.println(temp.get(2).text());
		for(int j = 1;j<trs.size();j++){
			Elements tds = trs.get(j).select("td");
			OrderInfo ele=new OrderInfo();
			ele.setCourseName(tds.get(1).text());
			ele.setOrdinaryPoint(tds.get(2).text());
			ele.setPaperPoint(tds.get(4).text());
			ele.setFinalScore(tds.get(5).text());
			ele.setOrder(tds.get(6).text()+"/"+tds.get(3).text());
			infoBeans.add(ele);
		}
		orderAdapter=new OrderAdapter(this,infoBeans);
		orderGradeList.setAdapter(orderAdapter);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {  

		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			startActivity(new Intent(GradeSubmit.this,GradeSearch.class));
			finish();
			return true;  
		}  
		return super.onKeyDown(keyCode, event);  

	}  

}
