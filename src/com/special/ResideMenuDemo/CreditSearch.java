package com.special.ResideMenuDemo;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;

public class CreditSearch extends WindowActivity{
	public static  ArrayList<NowGradeInfo> nowGrade = new ArrayList<NowGradeInfo>();
	private NetClient mNetClient = Globe.sNetClient;
	private Button btnCreditBack;
	private double weightAve;
	private double average;
	private double GPA;
	private double sum=0;
	private double weightSum=0;
	private double credit=0;
	private int numOfSub=0;
	private ListView mResultListView;
	private TextView textGPA;
	private ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);
		System.out.println("cookie:" + Globe.sCookieString);
		System.out.println("进入成绩查询页面");
		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, NetConstant.URL_JIDIAN, null,
				true);
		req.setPipIndex(NetConstant.CJCX_HOMEPAGE);
		mNetClient.sendRequest(req);
		mResultListView=(ListView)findViewById(R.id.gradeList);
		textGPA=(TextView) findViewById(R.id.gpa);
		btnCreditBack=(Button) findViewById(R.id.gradebtn);
		btnCreditBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CreditSearch.this,GradeSearch.class));
				finish();
			}
		});
	}

	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {
		// TODO Auto-generated method stub
		Document doc = myHttpResponse.getData();
		doc.select("table");
		double tempGPA=0;
		Elements[] trs=new Elements[3];
		for(int i=0;i<3;i++){
			trs[i] = doc.select("table").get(i*3+2).select("tr");
			System.out.println(trs[i].size());
			for(int j = 3;j<trs[i].size();j++){//trs.size()
				Elements tds = trs[i].get(j).select("td");
				String textTable= tds.get(1).text()+"\t"+tds.get(5).text()+"\t"+tds.get(6).text()+"\t"+tds.get(7).text();
				//System.out.println(textTable);
				if(i<2){
					double tempScore=0;
					double tempCredit=0;
					if(!tds.get(7).text().isEmpty()) {
					 tempScore=Double.valueOf(tds.get(7).text());
					 tempCredit=Double.valueOf(tds.get(3).text());
					 numOfSub++;
					 }
					//DHCWQA
					System.out.println(tds.get(7).text()+""+tempScore);
					sum+=tempScore;					           
					weightSum+=tempScore*tempCredit;
					credit+=tempCredit;
					tempGPA+=credit(tempScore)*tempCredit;
				}
				GradeInfo bean=new GradeInfo();
				bean.setCourseName(tds.get(1).text());
				bean.setOrdinaryPoint(tds.get(5).text());
				bean.setPaperPoint(tds.get(6).text());
				bean.setFinalScore(tds.get(7).text());
				infoBeans.add(bean);	 
			}
		}
		for(NowGradeInfo tempNowGrade:nowGrade){
			sum+=tempNowGrade.getNum();
			credit+=tempNowGrade.getCredit();
			weightSum+=tempNowGrade.getNum()*tempNowGrade.getCredit();
			tempGPA+=credit(tempNowGrade.getNum())*tempNowGrade.getCredit();
			numOfSub++;
		}
		if(credit!=0&&numOfSub!=0){
		weightAve=weightSum/credit;
		GPA=tempGPA/credit;
		System.out.println("加权总分"+weightSum+"总学分"+credit);
		average=sum/numOfSub;
		System.out.println("加权平均分"+myround(weightAve));
		System.out.println("平均分"+myround(average));
		System.out.println("学分绩点"+GPA);
		textGPA.setText("加权平均"+myround(weightAve)+"\t\t"+"平均分"+myround(average)+"\t\t"+"绩点"+myround(GPA));
		}
		else textGPA.setText("加权平均"+0+"\t\t"+"平均分"+0+"\t\t"+"绩点"+0);
		adapter=new MyAdapter(this,infoBeans);
		mResultListView.setAdapter(adapter);
	}

	public static double myround(double num)
	{
		BigDecimal b=new BigDecimal(num);
		num=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	public static double credit(double score){
		int result=(int) score;
		if(result>=90) return 4.0;
		if(result>=86&&result<=89) return 3.7;
		if(result>=83&&result<=85) return 3.3;
		if(result>=80&&result<=82) return 3.0;
		if(result>=76&&result<=79) return 2.7;
		if(result>=73&&result<=75) return 2.3;
		if(result>=70&&result<=72) return 2.0;
		if(result>=66&&result<=69) return 1.7;
		if(result>=63&&result<=65) return 1.3;
		if(result>=60&&result<=62) return 1.0;
		return 0;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  

		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			startActivity(new Intent(CreditSearch.this,GradeSearch.class));
			finish();
			return true;  
		}  
		return super.onKeyDown(keyCode, event);  

	}  
}
