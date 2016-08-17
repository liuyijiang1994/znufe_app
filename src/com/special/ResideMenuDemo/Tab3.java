package com.special.ResideMenuDemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpResponse;
import com.lzf.emptyclassroom.GlobeEmptyClassroom;
import com.lzf.emptyclassroom.ReadTxt;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;

public class Tab3 extends WindowActivity{
	private ArrayList<FreeClassInfo> infoBeans= new ArrayList<FreeClassInfo>();
	private ArrayList<FreeClassInfo> infoBeans1= new ArrayList<FreeClassInfo>();
	private ArrayList<FreeClassInfo> infoBeans2= new ArrayList<FreeClassInfo>();
	private FreeClassAdapter adapter;
	private ListView mResultListView;
	private ListView listView1;
	private ListView listView2;
	InputStream inputStream = null;
	private int j,k,m;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab3);
		listView2=(ListView)findViewById(R.id.freeClassList2);
		AssetManager assetManager = getAssets();
		try {
			inputStream = assetManager.open("EmptyClassroom.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("输入流获取错误");
		}
		setClass();
	}

	public void setClass(){
		if(infoBeans2.size()>0){
			infoBeans2.clear();
			adapter.notifyDataSetChanged();
		}
		if(TimeActivity.curDay<6&&TimeActivity.curDay>0){

			/*com.lzf.emptyclassroom.GlobeEmptyClassroom.clear();
			Context context =  getApplicationContext();
			ReadTxt.readTxt(inputStream);*/
			System.out.println("Attention!"+GlobeEmptyClassroom.listOfEmptyClassrooms.size());
			//ReadTxt.getCurEmptyClassroom(TimeActivity.curWeek, TimeActivity.curDay, FreeClass.mode);

			System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			for(int i=0;i<5;i++){
				System.out.print(FreeClass.mode[i]+"\t");
			}
			String[] wenTai = new String[200];
			int[][] wenTaiMode=new int[200][5];
			int t=0;
			System.out.println("第"+TimeActivity.curWeek+"周"+"\t星期"+TimeActivity.curDay+"\t列表行数"+listView2.getCount());
			System.out.println("全天都有课的教室数目"+GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			if(GlobeEmptyClassroom.resultOfEmptyClassrooms.size()>0)
				System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(0));
			for(int i=0;i<GlobeEmptyClassroom.resultOfEmptyClassrooms.size();i++){
				String temp=new String(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(i).getEmptyClassroom());
				int[] tempMode=GlobeEmptyClassroom.resultOfEmptyClassrooms.get(i).getMode();
				System.out.println(temp.charAt(1));
				if(temp.charAt(1)=='泰') {wenTai[t]=new String(temp.substring(2));wenTaiMode[t]=tempMode;t++;}
				else continue;
			}
			Tab1.sortStringArray(wenTai, wenTaiMode, t);
			for(int i=0;i<t;i++){
				FreeClassInfo bean=new FreeClassInfo();
				bean.setT1(wenTai[i]);
				if(wenTaiMode[i][0]==1) bean.setT2("空");else  bean.setT2("满");
				if(wenTaiMode[i][1]==1) bean.setT3("空");else  bean.setT3("满");
				if(wenTaiMode[i][2]==1) bean.setT4("空");else  bean.setT4("满");
				if(wenTaiMode[i][3]==1) bean.setT5("空");else  bean.setT5("满");
				if(wenTaiMode[i][4]==1) bean.setT6("空");else  bean.setT6("满");
				infoBeans2.add(bean);
			}
			adapter=new FreeClassAdapter(this,infoBeans2);
			listView2.setAdapter(adapter);
			System.out.println("加到listview上"+listView2.getCount());
		}
		else {
			//showTipDialog("今天周末！");
		}
	}

	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {
		// TODO Auto-generated method stub

	}
}
