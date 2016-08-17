package com.special.ResideMenuDemo;


import java.util.ArrayList;

import com.iceman.yangtze.Globe;
import com.kelly.*;
import com.kelly.util.ToDoDB;

import android.annotation.SuppressLint;
import android.app.Activity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ScheduleInsert extends Activity implements android.view.View.OnClickListener {

	private String WEEK[]={"星期一","星期二","星期三","星期四","星期五"};
	//private static EditText et1_2,et1_3,et2_2,et2_3,et3_2,et3_3,et4_2,et4_3,et5_2,et5_3;
	private ArrayList<EditText> et_2EditTexts = new ArrayList<EditText>();
	private ArrayList<EditText> et_3EditTexts = new ArrayList<EditText>();
	public static String weekday=new String();
	private ArrayAdapter adapter1;
	private ArrayAdapter adapter2;
	private int week=1;
	private EditText course;
	private EditText locate;
	private ToDoDB toDoDB;
	private Cursor mCursor;
	private SQLiteDatabase db;
	private int id;
	private int lesson;
	private int classLength;
	private  String lessonName;
	private  String location;
	private boolean isCorrect=true;
	private RadioButton m_Button1;  
	private RadioButton m_Button2;  
	private RadioButton m_Button3;
	private RadioButton m_Button4;
	private RadioGroup m_RadioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_schedule_insert);
		classLength=2;
		Spinner spinner1=(Spinner)findViewById(R.id.spinner1);
		adapter1 = ArrayAdapter.createFromResource(this, R.array.week, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(R.layout.drop_down_item);
		spinner1.setAdapter(adapter1);  
		spinner1.setVisibility(View.VISIBLE); 
		Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
		adapter2 = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);       
		adapter2.setDropDownViewResource(R.layout.drop_down_item);
		spinner2.setAdapter(adapter2); 
		spinner2.setVisibility(View.VISIBLE);
		course=(EditText)findViewById(R.id.inputcourse);
		locate=(EditText)findViewById(R.id.inputlocation);
		Button addtime=(Button)findViewById(R.id.addtime);
		Button ok=(Button)findViewById(R.id.btn2);
		Button back=(Button)findViewById(R.id.btn);
		m_Button1 = (RadioButton) this.findViewById(R.id.radioButton1);  
		m_Button2 = (RadioButton) this.findViewById(R.id.radioButton2);  
		m_Button3 = (RadioButton) this.findViewById(R.id.radioButton3);
		m_Button3 = (RadioButton) this.findViewById(R.id.radioButton4);
		m_RadioGroup = (RadioGroup) this.findViewById(R.id.ataawGroup);
		m_RadioGroup.setOnCheckedChangeListener(m_RadioGroupChangeListener);
		ok.setOnClickListener(this);
		back.setOnClickListener(this);


		toDoDB=new ToDoDB(this);
		db=toDoDB.getReadableDatabase();

		spinner2.setOnItemSelectedListener(
				new OnItemSelectedListener() 
				{
					@SuppressLint("ResourceAsColor")
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id)
					{
						TextView tv = (TextView)view; 
						lesson=position+1;
						tv.setTextColor(R.color.spinner_color);
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		spinner1.setOnItemSelectedListener(
				new OnItemSelectedListener() 
				{
					@SuppressLint("ResourceAsColor")
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id)
					{
						week=position+1;
						TextView tv = (TextView)view; 
						tv.setTextColor(R.color.spinner_color);
					}

					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});
		addtime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent it = new Intent(ScheduleInsert.this, AddTime.class);
				startActivity(it);

			}
		});
	}

	private RadioGroup.OnCheckedChangeListener m_RadioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {  
		public void onCheckedChanged(RadioGroup group, int checkedId) {  
			// TODO Auto-generated method stub  
			if (group.getId() == R.id.ataawGroup) {  
				if (checkedId == R.id.radioButton1) {  
					classLength=2;
				} else if (checkedId == R.id.radioButton2){  
					classLength=3;
				}
				else if (checkedId == R.id.radioButton3){  
					classLength=4;
				}
				else{
					classLength=8;
				}
			}  
		}
	};

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.btn2) {
			String singleOrDouble=new String(weekday);
			lessonName=course.getText().toString();
			location=locate.getText().toString();
			System.out.println(lessonName+"。。。。。");
			System.out.println(location+"。。。。。");
			System.out.println(lessonName+"添加课程里"+location);
			if(!lessonName.equals("")&&!location.equals("")){
				String sql="select * from todo_schedule where todo_stuid="+OnLineCourse.stuid+" and todo_dbVersion="+Globe.dbVersion;
				mCursor=db.rawQuery(sql, null);		  
				mCursor.moveToFirst();		       
				while (!mCursor.isAfterLast()){
					if(mCursor.getString(1).equals(Integer.toString(week))&&mCursor.getString(2).equals(Integer.toString(lesson)))
						if(singleOrDouble.charAt(0)==mCursor.getString(4).charAt(0)||singleOrDouble.charAt(1)==mCursor.getString(4).charAt(1)||singleOrDouble.charAt(15)==mCursor.getString(4).charAt(15))
						{	
							Toast.makeText(ScheduleInsert.this, "添加失败！你所选时间段与课表冲突，请重新选择", Toast.LENGTH_SHORT).show();
							isCorrect=false; 
							break;
						}
					mCursor.moveToNext();						
				}
				if(isCorrect)
				{
					int n=0;	  
					mCursor.moveToFirst();		       
					while (!mCursor.isAfterLast()){
						n++;
						if(mCursor.getString(1).equals(""))
							break;
						mCursor.moveToNext();
					}
					mCursor.close();
					db.execSQL("insert into todo_schedule(todo_week,todo_section,todo_classLen,todo_singleOrDouble,todo_course,todo_add,todo_stuid,todo_dbVersion) values(?,?,?,?,?,?,?,?)", new Object[]{Integer.toString(week),Integer.toString(lesson),Integer.toString(classLength),singleOrDouble,lessonName,location,OnLineCourse.stuid,Globe.dbVersion});
					System.out.println("添加课程的单双周为"+singleOrDouble);
					Toast.makeText(ScheduleInsert.this, "添加成功！", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent("android.appwidget.action.APPWIDGET_UPDATE");
					ScheduleInsert.this.sendBroadcast(intent);
					startActivity(new Intent(ScheduleInsert.this, OnLineCourse.class));
					finish();
				}
			}else{
				Toast.makeText(ScheduleInsert.this, "信息没有添加完全！", Toast.LENGTH_SHORT).show();
			}
		}
		else if (v.getId()==R.id.btn) {
			//back
			//mCursor.close();
			//toDoDB.close();
			Intent it = new Intent(ScheduleInsert.this, OnLineCourse.class);
			startActivity(it);
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);

		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//����µ��Ƿ��ؼ���û���ظ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}


}
