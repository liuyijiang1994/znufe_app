package com.dreamteam.app.ui;

//小类
//数据都在数据库中，这样做就很合理，就是这个数据库快搞死姐姐啦

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.CategoryDetailAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.db.FeedDBManager;
import com.dreamteam.app.entity.Feed;
import com.dreamteam.app.utils.CategoryNameExchange;
import com.special.ResideMenuDemo.R;
/**
 rewrite by angela liu
 */
public class CategoryDetail extends Activity
{
	public static final String tag = "CategoryDetail";
	
	private ListView detailList;
	private TextView titleTv;
	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	private CategoryDetailAdapter mAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData()
	{
		Intent intent = getIntent();
		String tableName = intent.getStringExtra("category");//得到栏目的名称
		CategoryNameExchange exchange = new CategoryNameExchange(this);
		titleTv.setText(exchange.en2zh(tableName) + "");
		//读取数据库
		FeedDBManager helper = new FeedDBManager(this, FeedDBManager.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(tableName, null, null, null, null, null, null);
		if (cursor.moveToFirst())
		{
			for (int i = 0, n = cursor.getCount(); i < n; i++)
			{
				Feed f = new Feed();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				int selectStatus = cursor.getInt(cursor
						.getColumnIndex("select_status"));
				f.setTitle(title);
				f.setUrl(url);
				f.setSelectStatus(selectStatus);
				feeds.add(f);
				cursor.moveToNext();
			}
		}
		db.close();
		//设置适配器
		mAdapter = new CategoryDetailAdapter(this, feeds, tableName);
		detailList.setAdapter(mAdapter);
	}

	//程序运行时候，按照顺序，先调用这个方法
	private void initView()
	{
		setContentView(R.layout.category_detail);
		titleTv = (TextView) findViewById(R.id.cd_title_tv);
		//返回键
		findViewById(R.id.category_detail_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		detailList = (ListView) findViewById(R.id.catagory_detail_lv_feed);
		detailList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(!AppContext.isNetworkAvailable(CategoryDetail.this))
				{
					Toast.makeText(CategoryDetail.this, "请检查网络设置！", Toast.LENGTH_SHORT).show();
					return;
				}
				//feed预览
			}
		});
	}
}

