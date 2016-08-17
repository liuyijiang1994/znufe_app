package com.dreamteam.app.ui;

//这一页是大类

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.adapter.FeedCategoryAdapter;
import com.special.ResideMenuDemo.R;


public class FeedCategory extends Activity
{

	private ListView categoryList;
	private ImageButton btn_add;
	private String[] categories;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView()
	{
		//初始分类名称
		//文件在string里面
		categories = getResources().getStringArray(R.array.feed_category_en);
		
		
		setContentView(R.layout.feed_category);
		categoryList = (ListView) findViewById(R.id.feed_category_lsit);
		
		//返回键
		findViewById(R.id.feed_category_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		final FeedCategoryAdapter adapter = new FeedCategoryAdapter(this);
		categoryList.setAdapter(adapter);
		categoryList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent();
				intent.putExtra("category", categories[position]);
				intent.setClass(FeedCategory.this, CategoryDetail.class);
				FeedCategory.this.startActivity(intent);
			}
		});
	}
}
