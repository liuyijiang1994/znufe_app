package com.example.testappweight;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.iceman.yangtze.Globe;
import com.kelly.util.ToDoDB;
import com.special.ResideMenuDemo.R;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider{
	public static String stuidAppWidget;
	Map<Integer,String> weekMap=new HashMap<Integer,String>();
	private ToDoDB toDoDB;
	private Cursor mCursor;
	private SQLiteDatabase db;
	private String sql;
	private int order=Globe.getWeekOrder();
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews views=new RemoteViews(context.getPackageName(),
				R.layout.main_4x1);
		SharedPreferences settings = context.getSharedPreferences("shared_file", 0);
		stuidAppWidget = settings.getString("loginname", "noname").trim();
		weekMap.put(1, "星期一");
		weekMap.put(2, "星期二");
		weekMap.put(3, "星期三");
		weekMap.put(4, "星期四");
		weekMap.put(5, "星期五");
		weekMap.put(6, "星期六");
		weekMap.put(0, "星期日");
		if(order>16) {
			order-=16;}
		int weekDay=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
		System.out.println("第"+String.valueOf(order)+"周"+"\t"+weekMap.get(weekDay));
		views.setTextViewText(R.id.textView100, "第"+String.valueOf(order)+"周"+"\t"+weekMap.get(weekDay));
		views.setViewVisibility(R.id.ll1, View.VISIBLE);	
		views.setTextViewText(R.id.schedule_1, "");
		views.setTextViewText(R.id.add_1, "");
		views.setViewVisibility(R.id.ll2, View.VISIBLE);	
		views.setTextViewText(R.id.schedule_2, "");
		views.setTextViewText(R.id.add_2, "");
		views.setViewVisibility(R.id.ll3, View.VISIBLE);	
		views.setTextViewText(R.id.schedule_3,"");
		views.setTextViewText(R.id.add_3, "");
		views.setViewVisibility(R.id.ll4, View.VISIBLE);	
		views.setTextViewText(R.id.schedule_4, "");
		views.setTextViewText(R.id.add_4, "");
		views.setViewVisibility(R.id.ll5, View.VISIBLE);	
		views.setTextViewText(R.id.schedule_5, "");
		views.setTextViewText(R.id.add_5,"");
		toDoDB=new ToDoDB(context);
		db=toDoDB.getReadableDatabase();
		sql="select * from todo_schedule where todo_week='"+String.valueOf(weekDay)+"' and todo_stuid='"+stuidAppWidget+"' and todo_dbVersion='"+Globe.dbVersion+"'";
		mCursor =db.rawQuery(sql, null);
		System.out.println(stuidAppWidget+"当日课程"+mCursor.getCount());
		mCursor.moveToFirst();
		while (!mCursor.isAfterLast()) {
			System.out.println(stuidAppWidget+"查询字符串"+mCursor.getString(2)+"单双周"+mCursor.getString(4).charAt(order-1));
			if(mCursor.getString(4).charAt(order-1)=='1'){
				if(mCursor.getString(2).equals("1")){
					views.setViewVisibility(R.id.ll1, View.VISIBLE);	
					views.setTextViewText(R.id.schedule_1, mCursor.getString(5));
					views.setTextViewText(R.id.add_1, mCursor.getString(6));
					System.out.println("1"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("2")){
					views.setViewVisibility(R.id.ll2, View.VISIBLE);	
					views.setTextViewText(R.id.schedule_2, mCursor.getString(5));
					views.setTextViewText(R.id.add_2, mCursor.getString(6));
					System.out.println("2"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("3")){
					views.setViewVisibility(R.id.ll3, View.VISIBLE);	
					views.setTextViewText(R.id.schedule_3, mCursor.getString(5));
					views.setTextViewText(R.id.add_3, mCursor.getString(6));
					System.out.println("3"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("4")){
					views.setViewVisibility(R.id.ll4, View.VISIBLE);	
					views.setTextViewText(R.id.schedule_4, mCursor.getString(5));
					views.setTextViewText(R.id.add_4, mCursor.getString(6));
					System.out.println("4"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("5")){
					views.setViewVisibility(R.id.ll5, View.VISIBLE);	
					views.setTextViewText(R.id.schedule_5, mCursor.getString(5));
					views.setTextViewText(R.id.add_5,mCursor.getString(6));
					System.out.println("5"+mCursor.getString(4));
				}
				System.out.println("在update里面，判断是否为1");
			}
			mCursor.moveToNext();
		}
		mCursor.close();
		db.close();
		appWidgetManager.updateAppWidget(new ComponentName(context,AppWidget.class), views);
	}

	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		RemoteViews rviews=new RemoteViews(context.getPackageName(),
				R.layout.main_4x1);
		SharedPreferences settings = context.getSharedPreferences("shared_file", 0);
		stuidAppWidget = settings.getString("loginname", "noname").trim();
		weekMap.put(1, "星期一");
		weekMap.put(2, "星期二");
		weekMap.put(3, "星期三");
		weekMap.put(4, "星期四");
		weekMap.put(5, "星期五");
		weekMap.put(6, "星期六");
		weekMap.put(0, "星期日");
		if(order>16) {
			order-=16;
			}
		int weekDay=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
		System.out.println("第"+String.valueOf(order)+"周"+"\t"+weekMap.get(weekDay));
		rviews.setTextViewText(R.id.textView100, "第"+String.valueOf(order)+"周"+"\t"+weekMap.get(weekDay));
		rviews.setViewVisibility(R.id.ll1, View.VISIBLE);	
		rviews.setTextViewText(R.id.schedule_1, "");
		rviews.setTextViewText(R.id.add_1, "");
		rviews.setViewVisibility(R.id.ll2, View.VISIBLE);	
		rviews.setTextViewText(R.id.schedule_2, "");
		rviews.setTextViewText(R.id.add_2, "");
		rviews.setViewVisibility(R.id.ll3, View.VISIBLE);	
		rviews.setTextViewText(R.id.schedule_3,"");
		rviews.setTextViewText(R.id.add_3, "");
		rviews.setViewVisibility(R.id.ll4, View.VISIBLE);	
		rviews.setTextViewText(R.id.schedule_4, "");
		rviews.setTextViewText(R.id.add_4, "");
		rviews.setViewVisibility(R.id.ll5, View.VISIBLE);	
		rviews.setTextViewText(R.id.schedule_5, "");
		rviews.setTextViewText(R.id.add_5,"");
		toDoDB=new ToDoDB(context);
		db=toDoDB.getReadableDatabase();
		sql="select * from todo_schedule where todo_week="+String.valueOf(weekDay)+" and todo_stuid='"+stuidAppWidget+"' and todo_dbVersion="+Globe.dbVersion;
		mCursor =db.rawQuery(sql, null);
		System.out.println("当日课程"+mCursor.getCount());
		mCursor.moveToFirst();
		System.out.println("onreceive中周数"+order);
		while (!mCursor.isAfterLast()) {
			System.out.println(stuidAppWidget+"查询字符串"+mCursor.getString(2)+"单双周"+mCursor.getString(4).charAt(order-1));
			if(mCursor.getString(4).charAt(order-1)=='1'){
				if(mCursor.getString(2).equals("1")){
					rviews.setViewVisibility(R.id.ll1, View.VISIBLE);	
					rviews.setTextViewText(R.id.schedule_1, mCursor.getString(5));
					rviews.setTextViewText(R.id.add_1, mCursor.getString(6));
					System.out.println("1r"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("2")){
					rviews.setViewVisibility(R.id.ll2, View.VISIBLE);	
					rviews.setTextViewText(R.id.schedule_2, mCursor.getString(5));
					rviews.setTextViewText(R.id.add_2, mCursor.getString(6));
					System.out.println("2r"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("3")){
					rviews.setViewVisibility(R.id.ll3, View.VISIBLE);	
					rviews.setTextViewText(R.id.schedule_3, mCursor.getString(5));
					rviews.setTextViewText(R.id.add_3, mCursor.getString(6));
					System.out.println("3r"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("4")){
					rviews.setViewVisibility(R.id.ll4, View.VISIBLE);	
					rviews.setTextViewText(R.id.schedule_4, mCursor.getString(5));
					rviews.setTextViewText(R.id.add_4, mCursor.getString(6));
					System.out.println("4r"+mCursor.getString(4));
				}
				if(mCursor.getString(2).equals("5")){
					rviews.setViewVisibility(R.id.ll5, View.VISIBLE);	
					rviews.setTextViewText(R.id.schedule_5, mCursor.getString(5));
					rviews.setTextViewText(R.id.add_5,mCursor.getString(6));
					System.out.println("5r"+mCursor.getString(4));
				}
				System.out.println("在onreceive里面，判断是否为1");
			}
			mCursor.moveToNext();
		}
		mCursor.close();
		db.close();
		AppWidgetManager appWidgetManger=AppWidgetManager.getInstance(context);
		int[] appIds=appWidgetManger.getAppWidgetIds(new ComponentName(context,AppWidget.class));
		appWidgetManger.updateAppWidget(appIds, rviews);
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
}
