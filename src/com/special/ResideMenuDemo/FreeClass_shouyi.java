package com.special.ResideMenuDemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.special.ResideMenuDemo.R;
import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpResponse;
import com.lzf.emptyclassroom.GlobeEmptyClassroom;
import com.lzf.emptyclassroom.ReadTxt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FreeClass_shouyi extends WindowActivity{
	private ArrayList<FreeClassInfo> infoBeans= new ArrayList<FreeClassInfo>();
	private ArrayList<FreeClassInfo> infoBeans1= new ArrayList<FreeClassInfo>();
	private ArrayList<FreeClassInfo> infoBeans2= new ArrayList<FreeClassInfo>();
	private FreeClassAdapter adapter;
	private ListView mResultListView;
	private ListView listView1;
	private ListView listView2;
	private Button enterBtn;
	private int j,k,m;
	public static int[] mode=new int[5];
	InputStream inputStream = null;
	Context context = null;
	LocalActivityManager manager = null;
	ViewPager pager = null;
	TabHost tabHost = null;
	Button btn1,btn2,btn3;
	private Button btnBack1;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageView cursor;// 动画图片
	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kongjiaoshi_shouyi);
		context = FreeClass_shouyi.this;
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);
        TextView tv=(TextView)findViewById(R.id.time);
        if(TimeActivity.curDay==0){tv.setText("第"+TimeActivity.curWeek+"周"+"  "+"星期日");}
        else  tv.setText("第"+TimeActivity.curWeek+"周"+"  "+"星期"+TimeActivity.curDay);
       // tv.setTextColor(R.color.color100);
		InitImageView();
		initButton();
		initPagerViewer();
		AssetManager assetManager = getAssets();
		try {
			inputStream = assetManager.open("EmptyClassroom.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("输入流获取错误");
		}
		btnBack1=(Button)findViewById(R.id.classback);
		btnBack1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(FreeClass_shouyi.this,SelectSection.class));
				finish();
			}
		});

		/*classSearchBtn=(Button)findViewById(R.id.classsearch);
		classSearchBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent();
				it.setClass(FreeClass.this,TimeActivity.class);
				startActivity(it);
			}
		});
		 */
	}

	private void initButton() {
		btn1 = (Button) findViewById(R.id.tab1);
		btn2 = (Button) findViewById(R.id.tab2);
		btn3 = (Button) findViewById(R.id.tab3);

		btn1.setOnClickListener(new MyOnClickListener(0));
		btn2.setOnClickListener(new MyOnClickListener(1));
		btn3.setOnClickListener(new MyOnClickListener(2));

	} 
	/**
	 * 初始化PageViewer
	 */
	 private void initPagerViewer() {
		 pager = (ViewPager) findViewById(R.id.viewpage);
		 final ArrayList<View> list = new ArrayList<View>();
		 Intent intent = new Intent(context, Tab1_shouyi.class);

		 list.add(getView("A", intent));
		 Intent intent2 = new Intent(context, Tab2_shouyi.class);

		 list.add(getView("B", intent2));
		 Intent intent3 = new Intent(context, Tab3_shouyi.class);

		 list.add(getView("C", intent3));

		 pager.setAdapter(new MyPagerAdapter(list));
		 pager.setCurrentItem(0);
		 pager.setOnPageChangeListener(new MyOnPageChangeListener());
	 }

	 /**
	  * 初始化动画
	  */
	 private void InitImageView() {
		 cursor = (ImageView) findViewById(R.id.cursor);
		 bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
				 .getWidth();// 获取图片宽度
		 DisplayMetrics dm = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(dm);
		 int screenW = dm.widthPixels;// 获取分辨率宽度
		 offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		 Matrix matrix = new Matrix();
		 matrix.postTranslate(offset, 0);
		 cursor.setImageMatrix(matrix);// 设置动画初始位置
	 }

	 private View getView(String id, Intent intent) {
		 return manager.startActivity(id, intent).getDecorView();
	 }

	 /**
	  * Pager适配器
	  */
	 public class MyPagerAdapter extends PagerAdapter{
		 List<View> list =  new ArrayList<View>();
		 public MyPagerAdapter(ArrayList<View> list) {
			 this.list = list;
		 }

		 @Override
		 public void destroyItem(ViewGroup container, int position,
				 Object object) {
			 ViewPager pViewPager = ((ViewPager) container);
			 pViewPager.removeView(list.get(position));
		 }

		 @Override
		 public boolean isViewFromObject(View arg0, Object arg1) {
			 return arg0 == arg1;
		 }

		 @Override
		 public int getCount() {
			 return list.size();
		 }
		 @Override
		 public Object instantiateItem(View arg0, int arg1) {
			 ViewPager pViewPager = ((ViewPager) arg0);
			 pViewPager.addView(list.get(arg1));
			 return list.get(arg1);
		 }

		 @Override
		 public void restoreState(Parcelable arg0, ClassLoader arg1) {

		 }

		 @Override
		 public Parcelable saveState() {
			 return null;
		 }

		 @Override
		 public void startUpdate(View arg0) {
		 }
	 }

	 /**
	  * 页卡切换监听
	  */
	 public class MyOnPageChangeListener implements OnPageChangeListener {

		 int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		 int two = one * 2;// 页卡1 -> 页卡3 偏移量

		 @Override
		 public void onPageSelected(int arg0) {
			 Animation animation = null;
			 switch (arg0) {
			 case 0:
				 if (currIndex == 1) {
					 animation = new TranslateAnimation(one, 0, 0, 0);
				 } else if (currIndex == 2) {
					 animation = new TranslateAnimation(two, 0, 0, 0);
				 }
				 break;
			 case 1:
				 if (currIndex == 0) {
					 animation = new TranslateAnimation(offset, one, 0, 0);
				 } else if (currIndex == 2) {
					 animation = new TranslateAnimation(two, one, 0, 0);    
				 }
				 break;
			 case 2:
				 if (currIndex == 0) {
					 animation = new TranslateAnimation(offset, two, 0, 0);
				 } else if (currIndex == 1) {
					 animation = new TranslateAnimation(one, two, 0, 0);
				 }
				 break;
			 }
			 currIndex = arg0;
			 animation.setFillAfter(true);// True:图片停在动画结束位置
			 animation.setDuration(300);
			 cursor.startAnimation(animation);
		 }

		 @Override
		 public void onPageScrollStateChanged(int arg0) {

		 }

		 @Override
		 public void onPageScrolled(int arg0, float arg1, int arg2) {

		 }
	 }

	 /**
	  * 头标点击监听
	  */
	 public class MyOnClickListener implements View.OnClickListener {
		 private int index = 0;

		 public MyOnClickListener(int i) {
			 index = i;
		 }

		 @Override
		 public void onClick(View v) {
			 pager.setCurrentItem(index);
		 }
	 };
	 private static Boolean isQuit = false;  
	 private long mExitTime = 0;  

	 Timer timer = new Timer(); 
	 @Override  
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  

		 if (keyCode == KeyEvent.KEYCODE_BACK) {  

			 if ((System.currentTimeMillis() - mExitTime) > 2000) {
				 // 如果两次按键时间间隔大于2000毫秒，则不退出  
				 Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
				 mExitTime = System.currentTimeMillis();// 更新mExitTime  
			 } else {  
				 System.exit(0);// 否则退出程序  
			 }  
			 return true;  
		 }  
		 return super.onKeyDown(keyCode, event);  

	 }  


	/* public void setClass(){


		 if(infoBeans.size()>0){
			 infoBeans.clear();
			 adapter.notifyDataSetChanged();
		 }
		 System.out.println(infoBeans.size());
		 if(infoBeans1.size()>0){
			 infoBeans1.clear();
			 adapter.notifyDataSetChanged();
		 }
		 if(infoBeans2.size()>0){
			 infoBeans2.clear();
			 adapter.notifyDataSetChanged();
		 }
		 //System.out.println(infoBeans2.size());
		 if(TimeActivity.curDay<6&&TimeActivity.curDay>0){

			 com.lzf.emptyclassroom.GlobeEmptyClassroom.clear();
			 Context context =  getApplicationContext();
			 ReadTxt.readTxt(inputStream);
			 System.out.println("Attention!"+GlobeEmptyClassroom.listOfEmptyClassrooms.size());
			 ReadTxt.getCurEmptyClassroom(TimeActivity.curWeek, TimeActivity.curDay, mode);

			 System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			 for(int i=0;i<5;i++){
				 System.out.print(mode[i]+"\t");
			 }
			 String[] wenBo=new String[200];
			 int b=0;
			 String[] wenLan = new String[200];
			 int l=0;
			 String[] wenTai = new String[200];
			 int t=0;
			 System.out.println("第"+TimeActivity.curWeek+"周"+"\t星期"+TimeActivity.curDay+"\t列表行数"+mResultListView.getCount());
			 System.out.println("全天都有课的教室数目"+GlobeEmptyClassroom.resultOfEmptyClassrooms.size());
			 if(GlobeEmptyClassroom.resultOfEmptyClassrooms.size()>0)
				 System.out.println(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(0));
			 for(int i=0;i<GlobeEmptyClassroom.resultOfEmptyClassrooms.size();i++){
				 String temp=new String(GlobeEmptyClassroom.resultOfEmptyClassrooms.get(i).getEmptyClassroom());
				 System.out.println(temp.charAt(1));
				 if(temp.charAt(1)=='泰') {wenTai[t]=new String(temp.substring(2));t++;}
				 if(temp.charAt(1)=='波') {wenBo[b]=new String(temp.substring(2));b++;}
				 if(temp.charAt(1)=='澜') {wenLan[l]=new String(temp.substring(2));l++;}
				 else continue;
			 }
			 for(j=0;j<b/7;j++)
			 {
				 FreeClassInfo bean=new FreeClassInfo();
				 System.out.println("已进入适配器");
				 bean.setT1(wenBo[j*7]);
				 bean.setT2(wenBo[j*7+1]);
				 bean.setT3(wenBo[j*7+2]);
				 bean.setT4(wenBo[j*7+3]);
				 bean.setT5(wenBo[j*7+4]);
				 bean.setT6(wenBo[j*7+5]);
				 bean.setT7(wenBo[j*7+6]);
				 System.out.println("已设置最后一个textview"+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7)+""+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+1)+""+
						 GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+2)+""+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+3)+""+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+4)
						 +""+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+5)+""+GlobeEmptyClassroom.resultOfEmptyClassrooms.get(j*7+6));
				 infoBeans.add(bean);
				 System.out.println("添加一个适配器");
			 }
			 FreeClassInfo bean1=new FreeClassInfo();
			 if(b%7==1){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(" ");
				 bean1.setT3(" ");
				 bean1.setT4(" ");
				 bean1.setT5(" ");
				 bean1.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(b%7==2){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(wenBo[j*7+1]);
				 bean1.setT3(" ");
				 bean1.setT4(" ");
				 bean1.setT5(" ");
				 bean1.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(b%7==3){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(wenBo[j*7+1]);
				 bean1.setT3(wenBo[j*7+2]);
				 bean1.setT4(" ");
				 bean1.setT5(" ");
				 bean1.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(b%7==4){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(wenBo[j*7+1]);
				 bean1.setT3(wenBo[j*7+2]);
				 bean1.setT4(wenBo[j*7+3]);
				 bean1.setT5(" ");
				 bean1.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(b%7==5){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(wenBo[j*7+1]);
				 bean1.setT3(wenBo[j*7+2]);
				 bean1.setT4(wenBo[j*7+3]);
				 bean1.setT5(wenBo[j*7+4]);
				 bean1.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(b%7==6){
				 bean1.setT1(wenBo[j*7]);
				 bean1.setT2(wenBo[j*7+1]);
				 bean1.setT3(wenBo[j*7+2]);
				 bean1.setT4(wenBo[j*7+3]);
				 bean1.setT5(wenBo[j*7+4]);
				 bean1.setT6(wenBo[j*7+5]);
				 bean1.setT7(" ");
			 }
			 infoBeans.add(bean1);
			 System.out.println("整除下的队列大小"+infoBeans.size());
			 adapter=new FreeClassAdapter(this,infoBeans);
			 mResultListView.setAdapter(adapter);
			 System.out.println("加到mResultListView上"+mResultListView.getCount());

			 for(j=0;j<l/7;j++)
			 {
				 FreeClassInfo bean=new FreeClassInfo();
				 System.out.println("已进入适配器");
				 bean.setT1(wenLan[j*7]);
				 bean.setT2(wenLan[j*7+1]);
				 bean.setT3(wenLan[j*7+2]);
				 bean.setT4(wenLan[j*7+3]);
				 bean.setT5(wenLan[j*7+4]);
				 bean.setT6(wenLan[j*7+5]);
				 bean.setT7(wenLan[j*7+6]);
				 infoBeans1.add(bean);
				 System.out.println("添加一个适配器");
			 }
			 FreeClassInfo bean2=new FreeClassInfo();
			 if(l%7==1){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(" ");
				 bean2.setT3(" ");
				 bean2.setT4(" ");
				 bean2.setT5(" ");
				 bean2.setT6(" ");
				 bean2.setT7(" ");
			 }
			 if(l%7==2){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(wenLan[j*7+1]);
				 bean2.setT3(" ");
				 bean2.setT4(" ");
				 bean2.setT5(" ");
				 bean2.setT6(" ");
				 bean2.setT7(" ");
			 }
			 if(l%7==3){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(wenLan[j*7+1]);
				 bean2.setT3(wenLan[j*7+2]);
				 bean2.setT4(" ");
				 bean2.setT5(" ");
				 bean2.setT6(" ");
				 bean2.setT7(" ");
			 }
			 if(l%7==4){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(wenLan[j*7+1]);
				 bean2.setT3(wenLan[j*7+2]);
				 bean2.setT4(wenLan[j*7+3]);
				 bean2.setT5(" ");
				 bean2.setT6(" ");
				 bean2.setT7(" ");
			 }
			 if(l%7==5){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(wenLan[j*7+1]);
				 bean2.setT3(wenLan[j*7+2]);
				 bean2.setT4(wenLan[j*7+3]);
				 bean2.setT5(wenLan[j*7+4]);
				 bean2.setT6(" ");
				 bean1.setT7(" ");
			 }
			 if(l%7==6){
				 bean2.setT1(wenLan[j*7]);
				 bean2.setT2(wenLan[j*7+1]);
				 bean2.setT3(wenLan[j*7+2]);
				 bean2.setT4(wenLan[j*7+3]);
				 bean2.setT5(wenLan[j*7+4]);
				 bean2.setT6(wenLan[j*7+5]);
				 bean2.setT7(" ");
			 }
			 infoBeans1.add(bean2);
			 System.out.println("整除下的队列大小"+infoBeans1.size());
			 adapter=new FreeClassAdapter(this,infoBeans1);
			 listView1.setAdapter(adapter);
			 System.out.println("加到listview1上"+listView1.getCount());

			 for(j=0;j<t/7;j++)
			 {
				 FreeClassInfo bean=new FreeClassInfo();
				 System.out.println("已进入适配器");
				 bean.setT1(wenTai[j*7]);
				 bean.setT2(wenTai[j*7+1]);
				 bean.setT3(wenTai[j*7+2]);
				 bean.setT4(wenTai[j*7+3]);
				 bean.setT5(wenTai[j*7+4]);
				 bean.setT6(wenTai[j*7+5]);
				 bean.setT7(wenTai[j*7+6]);
				 infoBeans2.add(bean);
				 System.out.println("添加一个适配器");
			 }
			 System.out.println("整除下的队列大小"+infoBeans.size());
			 FreeClassInfo bean3=new FreeClassInfo();
			 if(t%7==1){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(" ");
				 bean3.setT3(" ");
				 bean3.setT4(" ");
				 bean3.setT5(" ");
				 bean3.setT6(" ");
				 bean3.setT7(" ");
			 }
			 if(t%7==2){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(wenTai[j*7+1]);
				 bean3.setT3(" ");
				 bean3.setT4(" ");
				 bean3.setT5(" ");
				 bean3.setT6(" ");
				 bean3.setT7(" ");
			 }
			 if(t%7==3){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(wenTai[j*7+1]);
				 bean3.setT3(wenTai[j*7+2]);
				 bean3.setT4(" ");
				 bean3.setT5(" ");
				 bean3.setT6(" ");
				 bean3.setT7(" ");
			 }
			 if(t%7==4){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(wenTai[j*7+1]);
				 bean3.setT3(wenTai[j*7+2]);
				 bean3.setT4(wenTai[j*7+3]);
				 bean3.setT5(" ");
				 bean3.setT6(" ");
				 bean3.setT7(" ");
			 }
			 if(t%7==5){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(wenTai[j*7+1]);
				 bean3.setT3(wenTai[j*7+2]);
				 bean3.setT4(wenLan[j*7+3]);
				 bean3.setT5(wenLan[j*7+4]);
				 bean3.setT6(" ");
				 bean3.setT7(" ");
			 }
			 if(t%7==6){
				 bean3.setT1(wenTai[j*7]);
				 bean3.setT2(wenTai[j*7+1]);
				 bean3.setT3(wenTai[j*7+2]);
				 bean3.setT4(wenTai[j*7+3]);
				 bean3.setT5(wenTai[j*7+4]);
				 bean3.setT6(wenTai[j*7+5]);
				 bean3.setT7(" ");
			 }
			 infoBeans2.add(bean3);
			 System.out.println("整除下的队列大小"+infoBeans2.size());
			 adapter=new FreeClassAdapter(this,infoBeans2);
			 listView2.setAdapter(adapter);
			 System.out.println("加到listview上"+listView2.getCount());
		 }
		 else {
			 showTipDialog("今天周末！");
		 }
	 }*/

	 private void setListViewHeightBasedOnChildren(ListView listView) {

		 ListAdapter listAdapter = listView.getAdapter();
		 if (listAdapter == null) {
			 return;
		 }

		 int totalHeight = 0;
		 for (int i = 0; i < listAdapter.getCount(); i++) {
			 View listItem = listAdapter.getView(i, null, listView);
			 listItem.measure(0, 0);
			 totalHeight += listItem.getMeasuredHeight();
		 }

		 ViewGroup.LayoutParams params = listView.getLayoutParams();
		 params.height = totalHeight
				 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		 listView.setLayoutParams(params);
	 }
	 @Override
	 public void handResponse(MyHttpResponse myHttpResponse) {
		 // TODO Auto-generated method stub

	 }
}
