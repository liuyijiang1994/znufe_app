
package com.special.ResideMenuDemo;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.testappweight.AppWidget;
import com.iceman.yangtze.Globe;
import com.iceman.yangtze.net.NetClient;
import com.iceman.yangtze.net.NetConnect;
import com.iceman.yangtze.net.NetConstant;
import com.kelly.entity.DateDay;
import com.kelly.util.ToDoDB;
import com.special.ResideMenuDemo.ScheduleView.OnItemClassClickListener;


public class OnLineCourse extends Activity {

	public static String stuid;
	private NetClient mNetClient = Globe.sNetClient;

	private ArrayList<String[]> hide_params = new ArrayList<String[]>();

	private String[] mYearStrs;
	static int w;
	static int h;
	private String[] mTermStrs;

	private int mCurSelectYearIndex, mCurSelectTermIndex;

	private Spinner mYearSpinner, mTermSpinner;

	private ArrayAdapter<String> mYesrSpinnerAdapter;

	private ArrayAdapter<String> mTermSpinnerAdapter;

	private Button mScoreSearchBtn, mAllScoreBtn, mExSearchBtnOne, mExSearchBtnTwo;

	private ListView mResultListView;

	private ArrayList<String[]> mScoreListResults = new ArrayList<String[]>();

	//private ScoreListAdapter mScoreListAdapter;

	private ArrayList<String[]> mInputStrs = new ArrayList<String[]>();
	private ScheduleView scheduleView;
	private ArrayList<ClassInfo> classList;
	private TextView weekTxt;
	private TextView mJdTextView;
	private TextView[] text=new TextView[25];
	private Button btnBack;
	private Button btnin;
	public  String lessonName;
	public  String location;
	public  String singleOrDoubleWeek;

	private ToDoDB toDoDB;
	private Cursor mCursor;
	private SQLiteDatabase db;
	private int _id;
	private Dialog progressDialog = null;
	private ClassInfo []classInfo=new ClassInfo[25];
	public  Handler tempCourseHandle=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				System.out.println("在tempCourseHandle里");
				progressDialog.dismiss();
				handUiCourse(msg.obj.toString());
				break;
			}
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		scheduleView = (ScheduleView) this.findViewById(R.id.scheduleView);
		classList = new ArrayList<ClassInfo>();
		//overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		System.out.println("cookie:" + Globe.sCookieString);
		System.out.println("进入成绩查询页面");
		toDoDB=new ToDoDB(this);
		db=toDoDB.getReadableDatabase();
		btnBack=(Button) findViewById(R.id.btn);

		//屏幕宽高
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		w = metric.widthPixels;     // 屏幕宽度（像素）
		h = metric.heightPixels;   // 屏幕高度（像素）
		//float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
		//int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

		btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}

		});
		btnin = (Button) findViewById(R.id.button1);
		btnin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent it = new Intent(OnLineCourse.this, ScheduleInsert.class);
				startActivity(it);
				finish();

			}

		});

		scheduleView.setOnItemClassClickListener(new OnItemClassClickListener() {

			@Override
			public void onClick(final ClassInfo classInfo) {


				/* 初始化普通对话框。并设置样式 */
				final Dialog selectDialog = new Dialog(OnLineCourse.this, R.style.dialog);
				selectDialog.setCancelable(true);
				/* 设置普通对话框的布局 */
				selectDialog.setContentView(R.layout.dialog);

				/* +2+取得布局中的文本控件，并赋值需要显示的内容+2+ */
				TextView textView01 = (TextView) selectDialog
						.findViewById(R.id.TextView01);
				textView01
				.setText("确定要删除该课程吗？");

				Button btnItem1 = (Button) selectDialog.findViewById(R.id.ly1btn2);
				btnItem1.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						toDoDB=new ToDoDB(getBaseContext());
						db=toDoDB.getReadableDatabase();
						String sql = "delete from todo_schedule where todo_course ='"+classInfo.getClassname()+"' and todo_week='"+Integer.toString(classInfo.getWeekday())+"'";	
						// String sql="select * from todo_schedule where todo_course ='"+classInfo.getClassname()+"' and todo_week='"+Integer.toString(classInfo.getWeekday())+"'";
						db.execSQL(sql);
						db.close();
						Intent it = new Intent(OnLineCourse.this, OnLineCourse.class);
						startActivity(it);
						finish();
					}
				});
				Button btnItem2 = (Button) selectDialog.findViewById(R.id.ly1btn1);
				btnItem2.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectDialog.dismiss();//隐藏对话框
					}
				});
				selectDialog.show();//显示对话框
			}
		});
		weekTxt=(TextView) findViewById(R.id.text100);
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
		progressDialog = new Dialog(this,R.style.laoding_progress_dialog_style);
		progressDialog.setContentView(R.layout.loading_progress_bar_layout);
		progressDialog.setCancelable(true);
		progressDialog.show();
		new NetConnect(NetConstant.URL_KBCX,null,1,tempCourseHandle).start();
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	                               
	}

	@SuppressLint("ResourceAsColor")
	public void handUiCourse(String html){
		for(int i=0;i<25;i++)
			classInfo[i] = new ClassInfo();
		Document doc = Jsoup.parse(html);
		try {
			Elements table = doc.select("table");
			System.out.println(table.size());
			String textTable;
			int m=0;
			int order=Globe.getWeekOrder();
			if(order>16) order-=16;
			weekTxt.setText("第"+String.valueOf(order)+"周");
			int week = 0;
			int lesson;
			int classLen=2;
			DateDay dd = new DateDay(this);
			if(table.size()==0){
				String strTemp=doc.select("p").get(0).text().toString(); 
				System.out.println("显示信息"+strTemp);
				if(strTemp.length()>6) strTemp=strTemp.substring(0,6)+"……";
				//showTipDialog(strTemp);
			}
			else{
				Elements trs = doc.select("table").get(9).select("tr");
				System.out.println(trs.size());
				String sql;				
				sql="select * from todo_schedule where todo_stuid ='"+stuid+"' and todo_dbVersion='"+Globe.dbVersion+"'";
				mCursor=db.rawQuery(sql, null);
				int rowCount=mCursor.getCount();
				System.out.println("当前数据库的元组个数"+rowCount+"从第几行"+mCursor.getPosition());
				for(int i = 1;i<trs.size();i++){   	
					Elements tds = trs.get(i).select("td");
					week=tds.get(7).text().charAt(2)-48;
					lesson=tds.get(7).text().charAt(4)-48;
					classLen=tds.get(8).text().charAt(0)-48;
					lessonName=tds.get(0).text().trim();
					location=tds.get(6).text().trim();
					singleOrDoubleWeek=tds.get(9).text().trim();						
					if(week<6){
						if(tds.get(0).text().length()<10) 
							textTable= tds.get(0).text()+"\n"+tds.get(6).text();
						else  
							textTable= tds.get(0).text().substring(0, 9)+"\n"+tds.get(6).text();
						if(rowCount==0|dd.getDays2().equals("Mon")){
							/*toDoDB.updateCourse(i,lessonName);  ////////////////////////////////////
								toDoDB.updateAdd(i, location); 
								toDoDB.updateWeek(i, Integer.toString(week));
								toDoDB.updatSection(i, Integer.toString(lesson));
								toDoDB.updateSingleOrDouble(i,singleOrDoubleWeek);
								toDoDB.updateStuid(i, stuid);
								System.out.println(singleOrDoubleWeek+"插入数据库中"+i);*/
							db.execSQL("insert into todo_schedule(todo_week,todo_section,todo_classLen,todo_singleOrDouble,todo_course,todo_add,todo_stuid,todo_dbVersion) values(?,?,?,?,?,?,?,?)", new Object[]{Integer.toString(week),Integer.toString(lesson),Integer.toString(classLen),singleOrDoubleWeek,lessonName,location,stuid,Globe.dbVersion});
						}					
					}		
				}
				mCursor.close();
				System.out.println("进行广播的stuid"+stuid);
				AppWidget.stuidAppWidget=new String(stuid);
				Intent intent=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
				OnLineCourse.this.sendBroadcast(intent);
				sql="select * from todo_schedule where todo_stuid ="+stuid+" and todo_dbVersion="+Globe.dbVersion;
				mCursor=db.rawQuery(sql, null);
				mCursor.moveToFirst();
				int xun=0;
				while(!mCursor.isAfterLast()){
					System.out.println(mCursor.getString(4)+"序号"+xun);
					mCursor.moveToNext();
					xun++;
				}
				mCursor.moveToFirst();
				while (!mCursor.isAfterLast()) {
					if(!mCursor.getString(1).equals("")){
						int j=Integer.parseInt(mCursor.getString(1));
						int p=Integer.parseInt(mCursor.getString(2));
						System.out.println(mCursor.getString(4)+"..........."+mCursor.getString(5));
						if(mCursor.getString(4).charAt(order-1)=='1'){    				
							/*text[j-1+(p-1)*5].setText(mCursor.getString(4)+"\n"+mCursor.getString(5));
							System.out.println(mCursor.getString(4)+"\t"+mCursor.getString(5));
							text[j-1+(p-1)*5].setTextColor(R.color.color100);
							text[j-1+(p-1)*5].setBackgroundColor(Color.argb(170, 201, 199, 185));*/
							System.out.println(mCursor.getString(4)+"..........."+mCursor.getString(5));
							int classNum=(p-1)*2+1;
							if(p==5)
								classNum+=1;
							classInfo[j-1+(p-1)*5].setClassname(mCursor.getString(5));
							classInfo[j-1+(p-1)*5].setFromClassNum(classNum);
							classInfo[j-1+(p-1)*5].setClassNumLen(Integer.parseInt(mCursor.getString(3)));
							classInfo[j-1+(p-1)*5].setClassRoom(mCursor.getString(6));
							classInfo[j-1+(p-1)*5].setWeekday(j);
							classList.add(classInfo[j-1+(p-1)*5]);
						}
					}
					//  p++;
					mCursor.moveToNext();
				}
				mCursor.close();
				db.close();
				scheduleView.setClassList(classList);// 将课程信息导入到课表中
			}
			//db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 
