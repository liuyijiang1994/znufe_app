package com.znufe.outside.bigclass;


import com.special.ResideMenuDemo.R;
import com.znufe.xnfs.MainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class News_Reading extends Activity{
	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		actionBar = getActionBar();
		actionBar = getActionBar();
		getActionBar().setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.BackBar));
		if (actionBar != null) {
			// icon 左侧小箭头
			actionBar.setDisplayHomeAsUpEnabled(true);
			//显示actionbar的标题文本
			actionBar.setDisplayShowTitleEnabled(true);
		}
		
		   
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
    
	}
	}

