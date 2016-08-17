package com.special.ResideMenuDemo;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpResponse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SelectSection extends WindowActivity{
	private Button classSearchBtn;
	private Button backBtn;
	private CheckBox[] mBox=new CheckBox[5];
	private Button enterBtn;
	private ArrayAdapter adapter1;
	private int xiaoQu=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_section);
		Spinner spinner1=(Spinner)findViewById(R.id.spinnerXiaoqu);
		adapter1 = ArrayAdapter.createFromResource(this, R.array.xiaoqu, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(R.layout.drop_down_item);
		spinner1.setAdapter(adapter1);  
		spinner1.setVisibility(View.VISIBLE);
		spinner1.setOnItemSelectedListener(
				new OnItemSelectedListener() 
				{
					@SuppressLint("ResourceAsColor")
					public void onItemSelected(
							AdapterView<?> parent, View view, int position, long id)
					{
						xiaoQu=position+1;
						TextView tv = (TextView)view; 
						tv.setTextColor(R.color.spinner_color);
					}

					public void onNothingSelected(AdapterView<?> parent)
					{

					}
				});

		backBtn=(Button)findViewById(R.id.classback);
		backBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
		enterBtn=(Button) findViewById(R.id.enterBtn);
		enterBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isValid(FreeClass.mode)==false){showTipDialog("你还没有选择课程节数！");}
				else if(Globe.getWeekOrder()>17){
					showTipDialog("本学期已结束，好好复习，下学期再见！");
				}
				else {
					if(xiaoQu==1){
						startActivity(new Intent(SelectSection.this,FreeClass.class));
						finish();
					}else{
						Intent it=new Intent();
						it.setClass(SelectSection.this,FreeClass_shouyi.class);
						startActivity(it);
						finish();}
				}
			}
		});
		classSearchBtn=(Button)findViewById(R.id.classsearch);
		classSearchBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent();
				it.setClass(SelectSection.this,TimeActivity.class);
				startActivity(it);

			}
		});
		mBox[0]=(CheckBox) findViewById(R.id.checkBox1);
		mBox[2]=(CheckBox) findViewById(R.id.checkBox2);
		mBox[3]=(CheckBox) findViewById(R.id.checkBox3);
		mBox[1]=(CheckBox) findViewById(R.id.checkBox4);
		mBox[4]=(CheckBox) findViewById(R.id.checkBox5);
		mBox[0].setOnCheckedChangeListener(myListener0);
		mBox[1].setOnCheckedChangeListener(myListener1);
		mBox[2].setOnCheckedChangeListener(myListener2);
		mBox[3].setOnCheckedChangeListener(myListener3);
		mBox[4].setOnCheckedChangeListener(myListener4);
		for(int i=0;i<5;i++){
			if(FreeClass.mode[i]==1){mBox[i].setChecked(true);}
			else mBox[i].setChecked(false);
		}
	}

	OnCheckedChangeListener myListener0=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				FreeClass.mode[0]=1;
			}
			else {FreeClass.mode[0]=0;}
		}
	};
	OnCheckedChangeListener myListener1=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				FreeClass.mode[1]=1; 

			}
			else {FreeClass.mode[1]=0;
			}
		}
	};
	OnCheckedChangeListener myListener2=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				FreeClass.mode[2]=1;

			}
			else {FreeClass.mode[2]=0;

			}
		}
	};
	OnCheckedChangeListener myListener3=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				FreeClass.mode[3]=1;
			}
			else {FreeClass.mode[3]=0;

			}
		}
	};
	OnCheckedChangeListener myListener4=new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				FreeClass.mode[4]=1;
			}
			else {FreeClass.mode[4]=0;

			}
		}
	};

	private boolean isValid(int[] arr){
		for(int i=0;i<5;i++){
			if(arr[i]==1) return true;
		}
		return false;
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	                               
	}
	@Override
	public void handResponse(MyHttpResponse myHttpResponse) {
		// TODO Auto-generated method stub

	}
}
