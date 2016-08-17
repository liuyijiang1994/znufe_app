package com.special.ResideMenuDemo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.GlobeLesson;
import com.iceman.yangtze.Lesson;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConstant;

public class ShowTodayCourse extends WindowActivity {
	public static String lessonName;
	public static String location;
	public static String singleOrDoubleWeek;

	private NetClient mNetClient = Globe.sNetClient;

	private ArrayList<String[]> hide_params = new ArrayList<String[]>();

	private String[] mYearStrs;

	private String[] mTermStrs;

	private int mCurSelectYearIndex, mCurSelectTermIndex;

	private Spinner mYearSpinner, mTermSpinner;

	private ArrayAdapter<String> mYesrSpinnerAdapter;

	private ArrayAdapter<String> mTermSpinnerAdapter;

	private Button mScoreSearchBtn, mAllScoreBtn, mExSearchBtnOne, mExSearchBtnTwo;

	private ListView mResultListView;

	private ArrayList<String[]> mScoreListResults = new ArrayList<String[]>();
	private ArrayList<String[]> mInputStrs = new ArrayList<String[]>();
	TextView[] text=new TextView[25];
	private View view1, view2, view3,view4,view5;
	private ViewPager viewPager;
	private PagerTitleStrip pagerTitleStrip;
	private PagerTabStrip pagerTabStrip;
	private List<View> viewList;
	private List<String> titleList;
	TextView[] t=new TextView[25];


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.special.ResideMenuDemo.R.layout.activity_view_pager_demo);
		System.out.println("cookie:" + Globe.sCookieString);
		System.out.println("进入显示当天课表页面");

		MyHttpRequest req = new MyHttpRequest(NetConstant.TYPE_GET, NetConstant.URL_KBCX, null,
				true);
		req.setPipIndex(NetConstant.CJCX_HOMEPAGE);
		mNetClient.sendRequest(req); 
		view1 = findViewById(com.special.ResideMenuDemo.R.layout.layout1);
		view2 = findViewById(com.special.ResideMenuDemo.R.layout.layout2);
		view3 = findViewById(com.special.ResideMenuDemo.R.layout.layout3);
		view4 = findViewById(com.special.ResideMenuDemo.R.layout.layout4);
		view5 = findViewById(R.layout.layout5);

		LayoutInflater lf = getLayoutInflater().from(this);
		view1 = lf.inflate(R.layout.layout1, null);
		view2 = lf.inflate(R.layout.layout2, null);
		view3 = lf.inflate(R.layout.layout3, null);
		view4 = lf.inflate(R.layout.layout4, null);
		view5 = lf.inflate(R.layout.layout5, null);
		t[0]= (TextView) view1.findViewById(R.id.text1001);
		t[5]= (TextView) view1.findViewById(R.id.text1002);
		t[10]= (TextView) view1.findViewById(R.id.text1003);
		t[15] = (TextView) view1.findViewById(R.id.text1004);
		t[20]= (TextView) view1.findViewById(R.id.text1005);
		t[1]= (TextView) view2.findViewById(R.id.text1006);
		t[6]= (TextView) view2.findViewById(R.id.text1007);
		t[11]= (TextView) view2.findViewById(R.id.text1008);
		t[16] = (TextView) view2.findViewById(R.id.text1009);
		t[21]= (TextView) view2.findViewById(R.id.text1010);
		t[2]= (TextView) view3.findViewById(R.id.text1011);
		t[7]= (TextView) view3.findViewById(R.id.text1012);
		t[12]= (TextView) view3.findViewById(R.id.text1013);
		t[17] = (TextView) view3.findViewById(R.id.text1014);
		t[22]= (TextView) view3.findViewById(R.id.text1015);
		t[3]= (TextView) view4.findViewById(R.id.text1016);
		t[8]= (TextView) view4.findViewById(R.id.text1017);
		t[13]= (TextView) view4.findViewById(R.id.text1018);
		t[18] = (TextView) view4.findViewById(R.id.text1019);
		t[23]= (TextView) view4.findViewById(R.id.text1020);
		t[4]= (TextView) view5.findViewById(R.id.text1021);
		t[9]= (TextView) view5.findViewById(R.id.text1022);
		t[14]= (TextView) view5.findViewById(R.id.text1023);
		t[19] = (TextView) view5.findViewById(R.id.text1024);
		t[24]= (TextView) view5.findViewById(R.id.text1025);
		text[0]= (TextView) view1.findViewById(R.id.text11);
		text[1] = (TextView) view2.findViewById(R.id.text21);
		text[2] = (TextView) view3.findViewById(R.id.text31);
		text[3] = (TextView) view4.findViewById(R.id.text41);
		text[4] = (TextView) view5.findViewById(R.id.text51);
		text[5] = (TextView) view1.findViewById(R.id.text12);
		text[6] = (TextView) view2.findViewById(R.id.text22);
		text[7] = (TextView) view3.findViewById(R.id.text32);
		text[8] = (TextView) view4.findViewById(R.id.text42);
		text[9] = (TextView) view5.findViewById(R.id.text52);
		text[10] = (TextView) view1.findViewById(R.id.text13);
		text[11] = (TextView) view2.findViewById(R.id.text23);
		text[12] = (TextView) view3.findViewById(R.id.text33);
		text[13] = (TextView) view4.findViewById(R.id.text43);
		text[14] = (TextView) view5.findViewById(R.id.text53);
		text[15] = (TextView) view1.findViewById(R.id.text14);
		text[16] = (TextView) view2.findViewById(R.id.text24);
		text[17] = (TextView) view3.findViewById(R.id.text34);
		text[18] = (TextView) view4.findViewById(R.id.text44);
		text[19] = (TextView) view5.findViewById(R.id.text54);
		text[20] = (TextView) view1.findViewById(R.id.text15);
		text[21] = (TextView) view2.findViewById(R.id.text25);
		text[22] = (TextView) view3.findViewById(R.id.text35);
		text[23] = (TextView) view4.findViewById(R.id.text45);
		text[24] = (TextView) view5.findViewById(R.id.text55);
		Globe globe=new Globe();

	}

	@Override
	public void handResponse(MyHttpResponse myHttpResponse){
		Document doc = myHttpResponse.getData();
		Elements table = doc.select("table");
		System.out.println(table.size());
		String textTable;
		int m=0;
		int order=Globe.getWeekOrder();
		Elements trs = doc.select("table").get(9).select("tr");
		System.out.println(trs.size());
		for(int i = 1;i<trs.size();i++){   	
			Elements tds = trs.get(i).select("td");
			int week=tds.get(7).text().charAt(2)-48;
			int lesson=tds.get(7).text().charAt(4)-48;
			lessonName=tds.get(0).text().trim();
			location=tds.get(6).text().trim();
			singleOrDoubleWeek=tds.get(9).text().trim();
			System.out.print(lessonName+"\n"+location+"\n"+singleOrDoubleWeek+"\n"+week+"\n"+lesson+"\n");
			if(tds.get(9).text().charAt(order-1)=='1'){
				if(week<6){
					if(tds.get(0).text().length()<10) textTable= tds.get(0).text()+"\n"+tds.get(6).text();
					else  textTable= tds.get(0).text().substring(0, 9)+"\n"+tds.get(6).text();
					//		System.out.println(textTable);
					text[week-1+(lesson-1)*5].setText(textTable);
					text[week-1+(lesson-1)*5].setBackgroundResource(R.color.color1);
					text[week-1+(lesson-1)*5].setBackgroundColor(Color.argb(170, 201, 199, 185));
					if(textTable!=null){
						t[week-1+(lesson-1)*5].setBackgroundResource(R.color.color1);
						t[week-1+(lesson-1)*5].setBackgroundColor(Color.argb(170, 201, 199, 185));
						}
				}
				GlobeLesson.lessons[m]=new Lesson(lessonName,location,singleOrDoubleWeek,week,lesson);
				System.out.println(m);
				m++;
			}
		}
		GlobeLesson.writeTimeTableDoc();
		initView();

	}
	 
	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		//pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.gold)); 
		pagerTabStrip.setDrawFullUnderline(false);
		//pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTabStrip.setTextSpacing(50);

		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewList.add(view5);

		titleList = new ArrayList<String>();
		titleList.add("星期一");
		titleList.add("星期二");
		titleList.add("星期三");
		titleList.add("星期四");
		titleList.add("星期五");
		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList.get(position));

			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}

			@Override
			public CharSequence getPageTitle(int position) {

				return titleList.get(position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));

				return viewList.get(position);
			}

		};
		viewPager.setAdapter(pagerAdapter);
	}



}
