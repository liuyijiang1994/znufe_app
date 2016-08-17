package com.special.ResideMenuDemo;

import java.util.Calendar;

import com.iceman.yangtze.Globe;
import com.lzf.emptyclassroom.*;

import kankan.wheel.widget.NumericWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeActivity extends Activity {
	// Time changed flag
	private boolean timeChanged = false;

	private boolean timeScrolled = false;
	
	private int curHours,curMinutes;
	public static int curWeek=Globe.getWeekOrder(),curDay=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
	@SuppressWarnings("null")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_layout);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Button butn1=(Button)findViewById(R.id.button1);
		Button butn2=(Button)findViewById(R.id.button2);
		butn1.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	System.out.println("第"+curWeek+"周"+"\n星期"+curDay);
            	/*Intent it=new Intent();
            	it.setClass(TimeActivity.this, SelectSection.class);
            	startActivity(it);*/
            	onBackPressed();
            	TimeActivity.this.finish();     //结束Activity。调用其OnDestroy()方法               
            }});
		butn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeActivity.this.finish();
			}
			
		});
		final WheelView hours = (WheelView) findViewById(R.id.hour);
		hours.setAdapter(new NumericWheelAdapter(1, 16));
		hours.setLabel("周");

		final WheelView mins = (WheelView) findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(1, 5));
		mins.setLabel("星期");
		mins.setCyclic(true);

		@SuppressWarnings("null")
		//final TimePicker picker = new TimePicker(null);
		//picker.setIs24HourView(true);

		Calendar c = Calendar.getInstance();
		curHours = c.get(Calendar.HOUR_OF_DAY);
		curMinutes = c.get(Calendar.DAY_OF_WEEK);

		hours.setCurrentItem(curHours);
		mins.setCurrentItem(curMinutes);

		///picker.setCurrentHour(curHours+1);
		//picker.setCurrentMinute(curMinutes);

		// add listeners
		addChangingListener(mins, "星期");
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
				curWeek=hours.getCurrentItem()+1;
				//picker.setCurrentMinute(mins.getCurrentItem());
				curDay=mins.getCurrentItem()+1;
				System.out.println("第"+curWeek+"周"+"\n星期"+curDay);
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
