package com.dreamteam.app.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.ItemListAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.HtmlFilter;
import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.commons.UIHelper;
import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.custom.ui.PullToRefreshListView;
import com.dreamteam.custom.ui.PullToRefreshListView.OnRefreshListener;
import com.special.ResideMenuDemo.R;



public class ItemList extends Activity
{

	public static final String tag = "ItemList";
	private PullToRefreshListView itemLv;
	private ImageButton backBtn;
	private TextView feedTitleTv;
	private ItemListAdapter mAdapter;
	private SeriaHelper seriaHelper;
	private ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();
	private ArrayList<String> speechTextList = new ArrayList<String>();
	private String sectionTitle;
	private String sectionUrl;
	private BroadcastReceiver mReceiver;
	public static final String ACTION_UPDATE_ITEM_LIST = "com.dreamteam.action.update_item_list";
	private boolean isNight;// 是否夜间

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
		//initTts();
		initBroadeCast();
	}

	private void initBroadeCast()
	{
		mReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				String link = intent.getStringExtra("link");
				boolean isFavorite = intent.getBooleanExtra("is_favorite",
						false);
				for (FeedItem i : mItems)
				{
					if (i.getLink().equals(link))
					{
						i.setFavorite(isFavorite);
						break;
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_UPDATE_ITEM_LIST);
		registerReceiver(mReceiver, filter);
	}


	private void initView()
	{
		UIHelper.initTheme(this);
		setContentView(R.layout.feed_item_list);
		feedTitleTv = (TextView) findViewById(R.id.fil_feed_title);
		backBtn = (ImageButton) findViewById(R.id.fil_back_btn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ItemList.this.finish();
			}
		});
		itemLv = (PullToRefreshListView) findViewById(R.id.fil_lv_feed_item);
		itemLv.setOnRefreshListener(new OnRefreshListener()
		{
			public void onRefresh()
			{
				if (!AppContext.isNetworkAvailable(ItemList.this))
				{
					itemLv.onRefreshComplete();
					Toast.makeText(ItemList.this, R.string.no_network,
							Toast.LENGTH_SHORT).show();
					return;
				}
				new RefreshTask().execute(sectionUrl);
			}
		});
		itemLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent();
				FeedItem item = mItems.get(position - 1);
				final String link = item.getLink();
				// 改变阅读状态
				if (!item.isReaded())
				{
					item.setReaded(true);
					mAdapter.notifyDataSetChanged();
					
					new Thread()
					{
						@Override
						public void run()
						{
							SeriaHelper helper = SeriaHelper.newInstance();
							File cache = SectionHelper.getSdCache(sectionUrl);
							ItemListEntity entity = new ItemListEntity();
							for (FeedItem i : mItems)
							{
								if (i.getLink().equals(link))
								{
									i.setReaded(true);
								}
							}
							entity.setItemList(mItems);
							helper.saveObject(entity, cache);
						}

					}.start();
				}
				String title = item.getTitle();
				String contentEncoded = item.getContent();
				String pubdate = item.getPubdate();
				boolean isFavorite = item.isFavorite();
				String firstImgUrl = item.getFirstImageUrl();
				if (contentEncoded != null && contentEncoded.length() != 0)
				{
					intent.putExtra("item_detail", contentEncoded);
				}
				intent.putExtra("section_title", sectionTitle);
				intent.putExtra("section_url", sectionUrl);
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				intent.putExtra("link", link);
				intent.putExtra("is_favorite", isFavorite);
				intent.putExtra("first_img_url", firstImgUrl);
				intent.setClass(ItemList.this, ItemDetail.class);
				ItemList.this.startActivity(intent);
			}

		});
	}

	private void initData()
	{
		Intent intent = getIntent();
		sectionTitle = intent.getStringExtra("section_title");
		sectionUrl = intent.getStringExtra("url");
		feedTitleTv.setText(sectionTitle);

		File file = SectionHelper.getSdCache(sectionUrl);
		if (file.exists())
		{
			seriaHelper = SeriaHelper.newInstance();
			ItemListEntity itemListEntity = (ItemListEntity) seriaHelper
					.readObject(file);
			mItems = itemListEntity.getItemList();
			if (mItems != null)
			{
				mAdapter = new ItemListAdapter(this, mItems, isNight);
				itemLv.setAdapter(mAdapter);
				for (int i = 0, n = mItems.size(); i < n; i++)
				{
					FeedItem item = mItems.get(i);
					String input = item.getTitle() + item.getContent();
					speechTextList.add(HtmlFilter.filterHtml(input));
				}
			}
		}
	}

	private class RefreshTask extends
			AsyncTask<String, Integer, ItemListEntity>
	{
		@Override
		protected void onPostExecute(ItemListEntity result)
		{
			if (result == null)
			{
				itemLv.onRefreshComplete();
				Toast.makeText(ItemList.this, R.string.network_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<FeedItem> newItems = new ArrayList<FeedItem>();
			File cache = SectionHelper.getSdCache(sectionUrl);
			SeriaHelper helper = SeriaHelper.newInstance();
			ArrayList<FeedItem> items = result.getItemList();
			ItemListEntity old = (ItemListEntity) helper.readObject(cache);
			String oldFirstDate = old.getFirstItem().getPubdate();
			int newCount = 0;
			for (FeedItem i : items)
			{
				if (i.getPubdate().equals(oldFirstDate))
				{
					itemLv.onRefreshComplete();
					Toast.makeText(ItemList.this, R.string.no_update,
							Toast.LENGTH_SHORT).show();
					return;
				}
				newCount++;
				newItems.add(i);
			}
			helper.saveObject(result, cache);
			mAdapter.addItemsToHead(newItems);
			Toast.makeText(ItemList.this, "更新了" + newCount + "条",
					Toast.LENGTH_SHORT).show();
			itemLv.onRefreshComplete();
		}

		@Override
		protected ItemListEntity doInBackground(String... params)
		{
			ItemListEntityParser parser = new ItemListEntityParser();
			return parser.parse(params[0]);
		}
	}

}
