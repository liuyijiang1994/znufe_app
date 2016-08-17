package com.special.ResideMenuDemo;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;

public class KaoShiAnPai extends WindowActivity {
	private NetClient mNetClient = Globe.sNetClient;
	private ListView mResultListView;
	TextView[] text=new TextView[200];
	private ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
	private MyAdapter adapter;
	private Button gradeBtnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kaoshianpai);
		System.out.println("cookie:" + Globe.sCookieString);
		System.out.println("进入考试安排查询页面");

		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, NetConstant.KAO_SHI, null,
				true);
		req.setPipIndex(NetConstant.CJCX_HOMEPAGE);
		mNetClient.sendRequest(req);

		mResultListView=(ListView)findViewById(R.id.gradeList);
		gradeBtnBack=(Button)findViewById(R.id.gradebtn);
		gradeBtnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	                               
	}
	public void handResponse(MyHttpResponse myHttpResponse) {

		Document doc = myHttpResponse.getData();
		Elements table = doc.select("table"); 
		if(table.size()==0){
			String strTemp=doc.select("p").get(0).text().toString();
			System.out.println("显示信息"+strTemp);
			if(strTemp.length()>6) strTemp=strTemp.substring(0,6)+"……";
			showTipDialog(strTemp);
		}
		else {
		Elements trs=table.get(4).select("tr");
		System.out.println(trs.size());
		for(int j = 1;j<trs.size();j++){//trs.size()
			Elements tds = trs.get(j).select("td");
			String textTable= tds.get(1).text()+"\t"+tds.get(3).text()+"\t"+tds.get(8).text()+"\t"+tds.get(9).text();
			System.out.println(textTable);
			GradeInfo bean=new GradeInfo();
			bean.setCourseName(tds.get(1).text());
			bean.setOrdinaryPoint(tds.get(3).text());
			bean.setPaperPoint(tds.get(8).text());
			bean.setFinalScore(tds.get(9).text());
			infoBeans.add(bean);	    
		}
		adapter=new MyAdapter(this,infoBeans);
		mResultListView.setAdapter(adapter);
	} 
	}
}
