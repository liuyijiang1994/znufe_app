package com.special.ResideMenuDemo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.GlobeLesson;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpRequest;
import com.iceman.yangtze.net.MyHttpResponse;
import com.iceman.yangtze.net.NetConstant;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class OffLineTodayCourse extends Activity{

	static TextView[] text=new TextView[25];
	static TextView[] t=new TextView[25];
    private View view1, view2, view3,view4,view5;
	private ViewPager viewPager;
	private PagerTitleStrip pagerTitleStrip;
	private PagerTabStrip pagerTabStrip;
	private List<View> viewList;
	private List<String> titleList;
	public ResideMenu resideMenu;
    private OffLineTodayCourse mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemWellanNews;
    private ResideMenuItem itemNews;
   // private ResideMenuItem itemSettings;
    private ResideMenuItem itemTopics;
    private ResideMenuItem itemClassTable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(PreferenceTestMain.isFirst)
        setContentView(R.layout.activity_view_pager_demo);
	    System.out.println("cookie:" + Globe.sCookieString);
	    System.out.println("进入显示当天课表页面");
	    
	    view1 = findViewById(R.layout.layout1);
		view2 = findViewById(R.layout.layout2);
		view3 = findViewById(R.layout.layout3);
		view4 = findViewById(R.layout.layout4);
		view5 = findViewById(R.layout.layout5);
		getLayoutInflater();
		LayoutInflater lf = LayoutInflater.from(this);
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
		text[0] = (TextView) view1.findViewById(R.id.text11);
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
	    mContext=this;
		File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/文澜网络/课表.txt");

		if(!myDir.exists()){
			showTipDialog("未检测到离线课表，请先登录使用在线课表！");
		}else{
	    try {
			showWeekCourse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
	    initView();
	    int day=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
	    if(day>0&&day<6){viewPager.setCurrentItem(day-1);}
	    else showTipDialog("今天周末啊，没有双学位的可以好好休息一下了！");
	    
		
	    Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//resideMenu.openMenu();
				Intent it=new Intent(OffLineTodayCourse.this,MenuActivity.class);
				it.putExtra("b", "1");
				startActivity(it);
				finish();
			}
		});
		//onBackPressed();
	}
	private void showTipDialogToday(String str) {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("小伙伴们").setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		} );
    	builder.create().show();

    }

	public void onBackPressed() {
	       // TODO Auto-generated method stub
		 super.onBackPressed();
		 Intent it=new Intent(OffLineTodayCourse.this,MenuActivity.class);
		 it.putExtra("b", "1");
		 startActivity(it);
		 finish();
		   	                    
	    }
	
	public  void showWeekCourse() throws IOException{
		int order=Globe.getWeekOrder();
		if(order>16) {showTipDialogToday("本学期课程已结束，咱们下学期再见！");order-=16;}
		String textTable;
		new GlobeLesson();
		try {
			for(int i=0;i<GlobeLesson.lessons.length;i++){
				int week=GlobeLesson.lessons[i].getWeekId();
				int lesson=GlobeLesson.lessons[i].getSectionId();
				String lessonName=GlobeLesson.lessons[i].getLessonName();
				String location=GlobeLesson.lessons[i].getLocation();
				String singleOrDoubleWeek=GlobeLesson.lessons[i].getSingleOrDoubleWeek();
				System.out.print(lessonName+"\n"+location+"\n"+singleOrDoubleWeek+"\n"+week+"\n"+lesson+"\n");
				if(singleOrDoubleWeek.charAt(order-1)=='1'){
					if(week<6){
						if(lessonName.length()<10) textTable= lessonName+"\n"+location;
						else  textTable= lessonName.substring(0, 9)+"\n"+location;
						//		System.out.println(textTable);
						text[week-1+(lesson-1)*5].setText(textTable);
						text[week-1+(lesson-1)*5].setBackgroundResource(R.color.color100);
						text[week-1+(lesson-1)*5].setBackgroundColor(Color.argb(170, 201, 199, 185)); 
						if(textTable!=null){
							t[week-1+(lesson-1)*5].setBackgroundResource(R.color.color100);
							t[week-1+(lesson-1)*5].setBackgroundColor(Color.argb(170, 201, 199, 185));
							}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void showTipDialog(String str) {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("小伙伴们").setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		} );
    	builder.create().show();

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
