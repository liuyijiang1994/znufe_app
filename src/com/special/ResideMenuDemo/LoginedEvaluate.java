
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

public class LoginedEvaluate extends WindowActivity {

	private static final Object[] GradeInfo = null;
	ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
	private MyAdapter adapter;
	private ListView listView;
	static int num=0;
	static boolean isLast=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate);

		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET,NetConstant.URL_LOGIN_EVALUATE_END, null,true);
		req.setPipIndex(NetConstant.LOGIN);
		mNetClient.sendRequest(req);

		ArrayList<GradeInfo> infoBeans = new ArrayList<GradeInfo>();
		ListView listview;//声明listview
		MyAdapter adapter;//声明adapter
		listView=(ListView)findViewById(R.id.evaluteListView);
		Button btnBack = (Button) findViewById(R.id.btn);
		btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}

		});
		System.out.print("已经登录网上评教。。。。");
		/*Intent it = new Intent(LoginedEvaluate.this, EvaluateSubmit.class);
        startActivity(it);
        finish();*/
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	 
		finish();
	}
	public void handResponse(MyHttpResponse myHttpResponse) {
		int i=0;
		GradeInfo bean[]= new GradeInfo[100];
		Document doc;
		Element trs[]=new Element[100];
		Element teachers[]=new Element[100]; //老师
		Element courses[]=new Element[100];//课程名
		Element evaluates[]=new Element[100];//是否已评估
		Element operates[]=new Element[100];//操作
		doc = myHttpResponse.getData();
		System.out.println(doc.toString()+"哈哈");
		if(infoBeans.size()>0){
			infoBeans.clear();
			adapter.notifyDataSetChanged();
		}
		GradeInfo beanInti=new GradeInfo();
		beanInti.setCourseName("	  教师");//1
		beanInti.setOrdinaryPoint("		课程");//3
		beanInti.setPaperPoint("	 是否评估");//2
		beanInti.setFinalScore("操作");//4
		infoBeans.add(beanInti);

		Elements length=doc.select("tr");

		/*
        List<String> listOfTeacherName = new ArrayList<String>();
        List<String> listOfCourseName = new ArrayList<String>();


        Element trs1=doc.select("tr").get(7);
        Element td1=trs1.select("td").get(1);
        listOfTeacherName.add(td1.text());
        Element td2=trs1.select("td").get(2);

        listOfCourseName.add(td2.text());
		 */
		for(int k=0;k<100;k++){ //每个人的课程数不一样，但最后一个<tr>都对应最后一行
			bean[k]=new GradeInfo();//有问题
			System.out.println("初始化");
		}

		System.out.println("?????");

		for(i=0;i<length.size()-7;i++)
		{   

			trs[i]=doc.select("tr").get(7+i);
			teachers[i]=trs[i].select("td").get(4);//课程   
			courses[i]=trs[i].select("td").get(1);//是否评估
			evaluates[i]=trs[i].select("td").get(2);//操作
			operates[i]=trs[i].select("td").get(3);//教师
			System.out.println(teachers[i].text()+courses[i].text());

			bean[i].setCourseName(courses[i].text());//teachers[i].text()
			bean[i].setOrdinaryPoint(evaluates[i].text());//courses[i].text()
			bean[i].setPaperPoint(operates[i].text());//evaluates[i].text()
			bean[i].setFinalScore(teachers[i].text());//operates[i].text(
			infoBeans.add(bean[i]);
			System.out.println("asdasfas");
		}



		//@SuppressWarnings("deprecation")
		//LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//layout.addView(mResultListView[m],layoutParams);

		adapter=new MyAdapter(this,infoBeans);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2>0){
					num=arg2-1;
					if(listView.getCount()-1==arg2)
						isLast=true;
					System.out.println("第"+arg2+"个老师"+"状态"+infoBeans.get(arg2).getFinalScore().toString());
					if(infoBeans.get(arg2).getFinalScore().toString().trim().equals("查看")){
						showTipDialog("这位老师已经评过了！");
						System.out.println("第"+arg2+"个老师"+"状态"+infoBeans.get(arg2).getCourseName().toString());
					}
					else{
						Intent it = new Intent(LoginedEvaluate.this, EvaluateOpen.class);
						startActivity(it);
						finish();
					}
				}
			}			

		});

	}

}
