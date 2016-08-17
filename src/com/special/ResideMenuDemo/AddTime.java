package com.special.ResideMenuDemo;

import java.util.Calendar;

import kankan.wheel.widget.NumericWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

import com.iceman.yangtze.Globe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("NewApi") public class AddTime extends Activity{
	private boolean timeChanged = false;

	private boolean timeScrolled = false;
	private RadioButton m_Button1;  
	private RadioButton m_Button2;  
	private RadioButton m_Button3; 
	private RadioGroup m_RadioGroup;
	private int curHours,curMinutes;
	private  int firstWeek=1,lastWeek=16;
	@SuppressWarnings("null")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_choose);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		m_Button1 = (RadioButton) this.findViewById(R.id.radioButton1);  
		m_Button2 = (RadioButton) this.findViewById(R.id.radioButton2);  
		m_Button3 = (RadioButton) this.findViewById(R.id.radioButton3);
		m_RadioGroup = (RadioGroup) this.findViewById(R.id.RadioGroup01);
		m_RadioGroup.setOnCheckedChangeListener(m_RadioGroupChangeListener);
		Button butn1=(Button)findViewById(R.id.button2);
		Button butn2=(Button)findViewById(R.id.button1);
		butn1.setOnClickListener(new OnClickListener(){  
			public void onClick(View v) {  
				//System.out.println("第"+curWeek+"周"+"\n星期"+curDay);
				/*Intent it=new Intent();
            	it.setClass(TimeActivity.this, SelectSection.class);
            	startActivity(it);*/

				AddTime.this.finish();     //结束Activity。调用其OnDestroy()方法      
				onBackPressed();
			}});
		butn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddTime.this.finish();
			}

		});



		final WheelView hours = (WheelView) findViewById(R.id.hour);
		hours.setAdapter(new NumericWheelAdapter(1, 17));
		hours.setLabel(" 至");

		final WheelView mins = (WheelView) findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(1, 17));
		mins.setLabel("周");
		mins.setCyclic(true);


		//final TimePicker picker = new TimePicker(null);
		//picker.setIs24HourView(true);

		/*Calendar c = Calendar.getInstance();
		curHours = c.get(Calendar.HOUR_OF_DAY);
		curMinutes = c.get(Calendar.DAY_OF_WEEK);*/

		hours.setCurrentItem(0);
		mins.setCurrentItem(15);

		///picker.setCurrentHour(curHours+1);
		//picker.setCurrentMinute(curMinutes);

		// add listeners
		addChangingListener(mins, " 至");
		addChangingListener(hours, "周");

		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					//picker.setCurrentHour(hours.getCurrentItem());
					hours.getCurrentItem();
					//picker.setCurrentMinute(mins.getCurrentItem());
					mins.getCurrentItem();
					timeChanged = false;
				}
			}
		};

		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				//picker.setCurrentHour(hours.getCurrentItem());
				firstWeek=hours.getCurrentItem()+1;
				//picker.setCurrentMinute(mins.getCurrentItem());
				lastWeek=mins.getCurrentItem()+1;
				//System.out.println("第"+curWeek+"周"+"\n星期"+curDay);
				timeChanged = false;
			}
		};

		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		/*picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			public void onTimeChanged(TimePicker  view, int hourOfDay, int minute) {
				if (!timeChanged) {
					hours.setCurrentItem(hourOfDay, true);
					mins.setCurrentItem(minute, true);
				}
			}
		});*/


	}
	private RadioGroup.OnCheckedChangeListener m_RadioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {  
        public void onCheckedChanged(RadioGroup group, int checkedId) {  
            // TODO Auto-generated method stub  
            if (group.getId() == R.id.RadioGroup01) { 
            	if(!(ScheduleInsert.weekday).isEmpty()) ScheduleInsert.weekday = new String();
                if (checkedId == R.id.radioButton2) {  
                  for(int i=0;i<firstWeek-1;i++)
                	  ScheduleInsert.weekday+="0";
                  for(int i=firstWeek-1;i<lastWeek-1;i++)
                	  ScheduleInsert.weekday+="1";
                  for(int i=lastWeek-1;i<18;i++)
                	  ScheduleInsert.weekday+="0";
                } else if (checkedId == R.id.radioButton3){  
                	for(int i=firstWeek-1;i<=lastWeek;i++)
                		 if(i%2==0)
                   	  ScheduleInsert.weekday+="1";
                		 else
                	  ScheduleInsert.weekday+="0"; 
                }
                else{
                	for(int i=firstWeek-1;i<=lastWeek;i++)
               		 if(i%2!=0)
                  	  ScheduleInsert.weekday+="1";
               		else
                  	  ScheduleInsert.weekday+="0"; 
                }
            }  
        }
    };
	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();	                               
	}
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
				//wheel.setLabel(label);
			}
		});
	}

}
