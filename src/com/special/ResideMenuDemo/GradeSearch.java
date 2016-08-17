
package com.special.ResideMenuDemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLEncoder;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iceman.yangtze.Globe;
import com.special.ResideMenuDemo.R;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;



public class GradeSearch extends WindowActivity {
	private NetClient mNetClient = Globe.sNetClient;
	private ListView mResultListView;
	TextView[] text=new TextView[200];
	private ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
	private MyAdapter adapter;
	private Button gradeBtnBack;
	public static String aa="";
	private String strStart = "value=\"" ;
	private String strEnd = "\" />";
	private int start;
	private int end;
	private String result=null;
	private Button btnOrder;
	private Button btnGPA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentgrade);
		//overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		System.out.println("cookie:" + Globe.sCookieString);
		System.out.println("进入成绩查询页面");

		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, NetConstant.URL_CJCX, null,
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

		btnOrder=(Button) findViewById(R.id.btn1);
		btnOrder.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GradeSearch.this,GradeSubmit.class));
				finish();
			}
		});
		btnGPA=(Button) findViewById(R.id.btn2);
		btnGPA.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GradeSearch.this,CreditSearch.class));
				finish();
			}
		});
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	                               
	}
	public void handResponse(MyHttpResponse myHttpResponse) {
		//if (myHttpResponse.getPipIndex() == NetConstant.CJCX_HOMEPAGE) {
		Document doc = myHttpResponse.getData();
		try {
			Elements table = doc.select("table");        
			Elements trs=table.get(4).select("tr");
			//------------------------------------------------
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
			//<input type="checkbox" name="p_pm" value="B0600270       100022014062380        管理学通论" />
			String str=doc.toString();
			// StringBuffer strBuf = new StringBuffer() ;
			String regex="<input type=\"checkbox\" name=\"p_pm\" value=(.*?)/>";		
			Matcher matcher = Pattern.compile(regex).matcher(str);
			aa=new String();
			while(matcher.find()){
				for (int i = 0; i <= matcher.groupCount(); i=i+2) 
				{ 
					System.out.println(matcher.group(i)+"哈哈");
					//str="";
					//str = strBuf.append(matcher.group(i)).toString() ;
					start=(matcher.group(i).toString()).indexOf(strStart);
					end=(matcher.group(i).toString()).indexOf(strEnd);
					result=(matcher.group(i).toString()).substring(start, end);
					try {
						aa+="&p_pm="+change(result.substring(7));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println(aa+"哈哈");
			//----------------------------------------------
			/*GradeInfo beanInti=new GradeInfo();
			beanInti.setCourseName("课程名");
			beanInti.setOrdinaryPoint("平时");
			beanInti.setPaperPoint("考试");
			beanInti.setFinalScore("总分");
			infoBeans.add(beanInti);*/
			//for(int i=0;i<3;i++){
			//trs[i] = doc.select("table").get(i*3+2).select("tr");
			System.out.println(trs.size());
			if(CreditSearch.nowGrade.size()!=0){
				CreditSearch.nowGrade.clear();
			}
			for(int j = 1;j<trs.size();j++){//trs.size()
				Elements tds = trs.get(j).select("td");
				String textTable= tds.get(2).text()+"\t"+tds.get(6).text()+"\t"+tds.get(7).text()+"\t"+tds.get(8).text();
				System.out.println(textTable);
				GradeInfo bean=new GradeInfo();
				bean.setCourseName(tds.get(2).text());
				bean.setOrdinaryPoint(tds.get(6).text());
				bean.setPaperPoint(tds.get(7).text());
				bean.setFinalScore(tds.get(8).text());
				infoBeans.add(bean);
				NowGradeInfo nowGradeInfo=new NowGradeInfo();
				if(!tds.get(9).text().equals("任选")&&!tds.get(9).text().isEmpty()){
					if(tds.get(4).text().isEmpty()){nowGradeInfo.setCredit(0);}
					else nowGradeInfo.setCredit(Double.parseDouble(tds.get(4).text()));
					if(tds.get(8).text().isEmpty()){nowGradeInfo.setNum(0);}
					else nowGradeInfo.setNum(Double.parseDouble(tds.get(8).text()));
					CreditSearch.nowGrade.add(nowGradeInfo);
					System.out.println(tds.get(4).text()+"总分"+tds.get(8).text());
				}
			}
			//}   
			adapter=new MyAdapter(this,infoBeans);
			mResultListView.setAdapter(adapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mResultListView = new ListView(this);
		//mResultListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data));
		//setContentView(mResultListView);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {  

		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			onBackPressed();
			return true;  
		}  
		return super.onKeyDown(keyCode, event);  

	}    

	public String change(String str) throws UnsupportedEncodingException{
		return URLEncoder.encode(str,"gb2312");
	}
}
