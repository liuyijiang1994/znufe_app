package com.special.ResideMenuDemo;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.GlobeLesson;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpResponse;

public class OfflineWeekCourse extends WindowActivity{
	static GlobeLesson table;
	static TextView[] text=new TextView[25];
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		setContentView(R.layout.main1);
		System.out.println("进入显示课表页面");
		text[0] = (TextView) findViewById(R.id.text0);
		text[1] = (TextView) findViewById(R.id.text1);
		text[2] = (TextView) findViewById(R.id.text2);
		text[3] = (TextView) findViewById(R.id.text3);
		text[4] = (TextView) findViewById(R.id.text4);
		text[5] = (TextView) findViewById(R.id.text5);
		text[6] = (TextView) findViewById(R.id.text6);
		text[7] = (TextView) findViewById(R.id.text7);
		text[8] = (TextView) findViewById(R.id.text8);
		text[9] = (TextView) findViewById(R.id.text9);
		text[10] = (TextView) findViewById(R.id.text10);
		text[11] = (TextView) findViewById(R.id.text11);
		text[12] = (TextView) findViewById(R.id.text12);
		text[13] = (TextView) findViewById(R.id.text13);
		text[14] = (TextView) findViewById(R.id.text14);
		text[15] = (TextView) findViewById(R.id.text15);
		text[16] = (TextView) findViewById(R.id.text16);
		text[17] = (TextView) findViewById(R.id.text17);
		text[18] = (TextView) findViewById(R.id.text18);
		text[19] = (TextView) findViewById(R.id.text19);
		text[20] = (TextView) findViewById(R.id.text20);
		text[21] = (TextView) findViewById(R.id.text21);
		text[22] = (TextView) findViewById(R.id.text22);
		text[23] = (TextView) findViewById(R.id.text23);
		text[24] = (TextView) findViewById(R.id.text24);
		System.out.print("Globe已经执行");
		Globe globe=new Globe();
		try {
			showWeekCourse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {

	}

	@SuppressWarnings("static-access")
	public static void showWeekCourse() throws IOException{
		int order=Globe.getWeekOrder();
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
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
