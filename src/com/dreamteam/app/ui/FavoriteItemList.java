package com.dreamteam.app.ui;

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

import com.dreamteam.app.adapter.ItemListAdapter;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.entity.FeedItem;
import com.special.ResideMenuDemo.R;

/**
 * rewrite by angela
 */
public class FavoriteItemList extends Activity
{
	private ListView favoriteLv;
	private ItemListAdapter mAdapter;
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private ArrayList<String> names = new ArrayList<String>();
	private Intent intent = new Intent();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView()
	{
		setContentView(R.layout.favorite_list);
		favoriteLv = (ListView) findViewById(R.id.favorite_list);
		favoriteLv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				FeedItem item = items.get(position);
				String title = item.getTitle();
				String content = item.getContent();
				String pubdate = item.getPubdate();
				//String link = item.getLink();
				//System.out.println("========================="+link);
				
				String sectionName=names.get(position);
				item.setFavorite(true);
				boolean isFavorite = item.isFavorite();
				//System.out.println("========================="+isFavorite);
				
				if(content != null && content.length() != 0)
				{
					intent.putExtra("item_detail", content);
				}
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				intent.putExtra("is_favorite", isFavorite);
				//==
				intent.putExtra("section_url",sectionName);
				System.out.println("========================="+sectionName);//
				
				//intent.putExtra("link", link);先保留这吧
				
				intent.setClass(FavoriteItemList.this,ItemDetail.class);
				FavoriteItemList.this.startActivity(intent);
			}
		});
		findViewById(R.id.favorite_list_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	private void initData()
	{
		DbManager mgr = new DbManager(this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbManager.FAVORITE_ITEM_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			for(int i = 0, n = cursor.getCount(); i < n; i++)
			{
				String name;//adapter
				FeedItem item = new FeedItem();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
				String itemDetail = cursor.getString(cursor.getColumnIndex("item_detail"));
				String sectionName = cursor.getString(cursor.getColumnIndex("table_name"));//变成url了试试
				System.out.println("favouriteItemList"+sectionName);//这里是对的
				//注意啦啦啦，这里获得的是板块的名称，并非是板块的url，要通过转换才能得到地址啊啊啊啊啊
				//用数据库的方法转换看看吧
				item.setTitle(title);
				item.setPubdate(pubdate);
				item.setContent(itemDetail);
				//=====
				name=sectionName;
				items.add(item);
				//======
				names.add(name);
				cursor.moveToNext();
				
			}
		}
		mAdapter = new ItemListAdapter(this, items, false);
		favoriteLv.setAdapter(mAdapter);
		db.close();
	}
}
