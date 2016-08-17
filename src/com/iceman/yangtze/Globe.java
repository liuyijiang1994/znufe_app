
package com.iceman.yangtze;

import java.util.ArrayList;
import java.util.Calendar;

import com.iceman.yangtze.net.NetClient;


/**
 * @author iceman 一些静态全局变量
 */
public class Globe {
	public static NetClient sNetClient;

	public static int dbVersion=1;
	public static String sCookieString;

	public static int sVersionInt = 3;// 表示2.2版本

	public static String sName, sId, sClassName;

	public static ArrayList<String[]> sHideParams = new ArrayList<String[]>();

	public static int yearInit=2014;
	public static int monthInit=9;
	public static int dayInit=9;
	private static int weekOrder;

	public static void clearAll() {
		sNetClient = null;
		sCookieString = null;
		sName = null;
		sId = null;
		sClassName = null;
		sHideParams.clear();
	}

	public static int getWeekOrder(){
		return getWeekOrder(yearInit,monthInit,dayInit);
	}
	
	public static int getWeekOrder(int yearInit,int monthInit,int dayInit){
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = Integer.parseInt(((10000 + calendar.get(Calendar.YEAR)) + "").substring(1));
		int month = Integer.parseInt(((101 + calendar.get(Calendar.MONTH)) + "").substring(1));
		int day = Integer.parseInt(((100 + calendar.get(Calendar.DAY_OF_MONTH)) + "")
				.substring(1));
		int sum=0;
		for(int i=yearInit;i<year;i++)
		{
			if(monthInit<month)
			{
			  if(i%4==0&&i%100!=0||i%400==0) sum+=366;
			  else sum+=365;
			}
		}
		if(monthInit>month&&year>yearInit)
		{
			
			for(int j=monthInit;j<=12;j++){
				if(j==monthInit)
					  sum+=21;
				else
				{
				if(j==2){
					if(year%4==0&&year%100!=0||year%400==0)
						sum+=29; else sum+=28;
				}
				else if(j==4||j==6||j==9||j==11) sum+=30;
				else sum+=31;
				}
			}
			
			for(int j=1;j<month;j++){
				if(j==2){
					if(year%4==0&&year%100!=0||year%400==0)
						sum+=29; else sum+=28;
				}
				else if(j==4||j==6||j==9||j==11) sum+=30;
				else sum+=31;
				
			}
			sum+=day;
		}
		else
		{
		  for(int j=monthInit+1;j<month;j++){
			if(j==2){
				if(year%4==0&&year%100!=0||year%400==0)
					sum+=29; else sum+=28;
			}
			else if(j==4||j==6||j==9||j==11) sum+=30;
			else sum+=31;
		  }
		  if(month==(monthInit+1))
			  sum+=day+21;
		  else if(month==monthInit)
			  sum+=day-dayInit;
		}
		System.out.println(sum);
		return (sum+2)/7+1;
	}
}
