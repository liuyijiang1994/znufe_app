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

public class Tab1_shouyi extends WindowActivity{
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
		setContentView(R.layout.tab1_shouyi);
		mResultListView=(ListView)findViewById(R.id.freeClassList);
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
		if(infoBeans.size()>0){
			infoBeans.clear();
			adapter.notifyDataSetChanged();
		}
		if(TimeActivity.curDay<6&&TimeActivity.curDay>0){
			com.lzf.emptyclassroom.GlobeEmptyClassroom.clear();
			Context context =  getApplicationContext();
			ReadTxt.readTxt(inputStream);
			System.out.println("Attention!"+GlobeEmptyClassroom.listOfEmptyClassrooms.size());
			ReadTxt.getCurEmptyClassroom(TimeActivity.curWeek, TimeActivity.curDay, FreeClass.mode);
			System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			for(int i=0;i<5;i++){
				System.out.print(FreeClass.mode[i]+"\t");
			}
			String[] wenBo=new String[200];
			int[][] wenBoMode=new int[200][5];
			int b=0;
			System.out.println("第"+TimeActivity.curWeek+"周"+"\t星期"+TimeActivity.curDay+"\t列表行数"+mResultListView.getCount());
			System.out.println("全天都有课的教室数目"+GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			if(GlobeEmptyClassroom.resultOfEmptyClassrooms.size()>0)
				System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(0));
			for(int i=0;i<GlobeEmptyClassroom.resultOfEmptyClassrooms.size();i++){
				String temp=new String(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(i).getEmptyClassroom());
				int[] tempMode=GlobeEmptyClassroom.resultOfEmptyClassrooms.get(i).getMode();
				System.out.println(temp.charAt(4));
				if(temp.charAt(4)=='津') {wenBo[b]=temp.substring(5);wenBoMode[b]=tempMode;b++;}
				else continue;
			}
			/*for(int i=0;i<b;i++){
				System.out.print("教室"+wenBo[i]);
				for(int p=0;p<5;p++){
					System.out.print("\t"+wenBoMode[i][p]);
				}
				System.out.println();
			}*/
			sortStringArray(wenBo,wenBoMode,b);
			for(int i=0;i<b;i++){
				System.out.println("文jing"+wenBo[i]);
			}
			for(int i=0;i<b;i++){
				FreeClassInfo bean=new FreeClassInfo();
				bean.setT1(wenBo[i]);
				if(wenBoMode[i][0]==1) bean.setT2("空");else  bean.setT2("满");
				if(wenBoMode[i][1]==1) bean.setT3("空");else  bean.setT3("满");
				if(wenBoMode[i][2]==1) bean.setT4("空");else  bean.setT4("满");
				if(wenBoMode[i][3]==1) bean.setT5("空");else  bean.setT5("满");
				if(wenBoMode[i][4]==1) bean.setT6("空");else  bean.setT6("满");
				infoBeans.add(bean);
			}
			adapter=new FreeClassAdapter(this,infoBeans);
			mResultListView.setAdapter(adapter);
			System.out.println("加到mResultListView上"+mResultListView.getCount());
		}
		else {
			showTipDialog("今天周末！");
		}
	}

	public static void sortStringArray(String[] arrStr,int[][] arr,int num) {
		String temp;
		int[] tempInt;
		for (int i = 0; i < num; i++) {
			for (int j = num - 1; j > i; j--) {
				if(arrStr[i].compareTo(arrStr[j])>0) {
					temp = arrStr[i];
					tempInt=arr[i];
					arrStr[i] = arrStr[j];
					arr[i]=arr[j];
					arrStr[j] = temp;
					arr[j]=tempInt;
				}
			}
		}
	}

	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {
		// TODO Auto-generated method stub

	}
}
