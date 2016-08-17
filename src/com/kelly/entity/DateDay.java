package com.kelly.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;


public class DateDay {
	
	
	private String weekDay,SetStr;
	private int NowInt,SetInt;
	private String timeStr;
	private Time timeNow=new Time();
	
	private static String[] months = {"һ��", "����", "����", "����",
        "����", "����", "����", "����",
        "����", "ʮ��", "ʮһ��", "ʮ����"};
	private static String[] months2 = {"Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sept", "Oct", "Nov", "Dec"};
    private static String[] days1 = {"������", "����һ", "���ڶ�", "������",
        "������", "������", "������"};
    private static String[] days2 = {"SUN", "MON", "TUE", "WED",
        "THU", "FRI", "SAT"};
	
	public DateDay(Context context) {
		// TODO Auto-generated constructor stub
		
		SharedPreferences share = context.getSharedPreferences("INIT",Context.MODE_WORLD_READABLE);
        SetStr =share.getString("SET", "0");
        timeNow.setToNow();
        timeStr=timeNow.toString();
	    FindDayOfYear fDayOfYear=new FindDayOfYear();
        NowInt=fDayOfYear .getDayOfYear(timeNow.year, timeNow.month, timeNow.monthDay);
	    SetInt=Integer.parseInt(SetStr);
	    
	   
	}
	/**
	 * ���"����һ"
	 * @return
	 */
    public String getDays1(){
    	return days1[timeNow.weekDay];
    }
    /**
	 *���"MON"
	 * @return
	 */
    public String getDays2(){
    	return days2[timeNow.weekDay];
    }
    
    public String getDays22(){
    	return days2[timeNow.weekDay];
    }
    /**
	 *���"����"
	 * @return
	 */
    public String getMonth(){ 
    	return months[timeNow.month];
    }
    /**
	 *���"Feb"
	 * @return
	 */
    public String getMonth2(){ 
    	return months2[timeNow.month];//
    }
    /**
	 *���"2"
	 * @return
	 */
    public String getMonth3(){ 
    	return timeNow.month+1+"";//
    }
    /**
	 *���"20"
	 * @return
	 */
    public String getDate(){
    	return Integer.toString(timeNow.monthDay);//
    }
    /**
	 *���"1991"
	 * @return
	 */
    public String getYear(){
    	return Integer.toString(timeNow.year);
    }
    /**
	 *���"k"
	 * @return
	 */
	public String  getWeedDay(){
            if (SetStr.equals("0")) {
			    weekDay ="δ����";
		    }
		    else {
			    weekDay =((NowInt-SetInt)/7+1)+"";
		    }
		return weekDay;//���"k"
    }

	public String getCurrentTime() {
		// TODO Auto-generated method stub
		return timeStr;
	}
}
