package com.dreamteam.app.ui;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.HtmlFilter;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.commons.UIHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FavoItemDbHelper;
import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.entity.ItemListEntity;
import com.special.ResideMenuDemo.R;

@SuppressLint("JavascriptInterface")
@SuppressWarnings("deprecation")
public class ItemDetail extends FragmentActivity
{
	private ImageButton collectBtn;
	private ImageButton shareBtn;
	private static WebView mWebView;
	private String sectionTitle;
	private String sectionUrl;
	private String title;
	private String pubdate;
	private String itemDetail;
	private String link;
	private String firstImgUrl;
	private boolean isFavorite;//文章是否已收藏
	private String css = UIHelper.WEB_STYLE;
	private int[] favoIcons = {
			R.drawable.btn_favorite_empty,
			R.drawable.btn_favorite_full
	};//0为空
	
	private ImageButton backBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initView();
		loadData();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			Intent intent = new Intent();
			intent.setAction(ItemList.ACTION_UPDATE_ITEM_LIST);
			sendBroadcast(intent);
			super.handleMessage(msg);
		}
	};
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView()
	{		
		SharedPreferences prefs = AppContext.getPrefrences(this);
		if(prefs.getBoolean("day_night_mode", false))
		{
			setTheme(R.style.AppNightTheme);
			css = UIHelper.WEB_STYLE_NIGHT;
			favoIcons = new int[]{
					R.drawable.btn_favorite_empty_night,
					R.drawable.btn_favorite_full_night
			};
		}		
		isFavorite = getIntent().getBooleanExtra("is_favorite", false);
		setContentView(R.layout.feed_item_detail);
		backBtn = (ImageButton) findViewById(R.id.fil_back_btn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		shareBtn = (ImageButton) findViewById(R.id.fid_btn_share);
		shareBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//mController.openShare(ItemDetail.this, false);
				//Toast.makeText(getApplicationContext(), "悦哥在思考。。。换一种方式做分享",
					//	Toast.LENGTH_SHORT).show();
				//从天气那边拿过来的分享方法，不造可以不
				Intent intent;
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"悦读分享，精彩感动");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ItemDetail.this
						.startActivity(Intent.createChooser(intent, "好友分享"));				
				
			}
			
		});
		collectBtn = (ImageButton) findViewById(R.id.fid_btn_collecte);
		if(isFavorite)
			collectBtn.setImageResource(R.drawable.btn_favorite_full);
		collectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				DbManager helper = new DbManager(ItemDetail.this, DbManager.DB_NAME, null, 1);
				final SQLiteDatabase db = helper.getWritableDatabase();
				//已收藏，取消收藏
				if(isFavorite)
				{
					collectBtn.setImageResource(favoIcons[0]);
					Toast.makeText(ItemDetail.this, "取消了收藏", Toast.LENGTH_SHORT).show();
					FavoItemDbHelper.removeRecord2(db, title);
					isFavorite = false;
				}
				else
				{
					//加入收藏
					isFavorite = true;
					collectBtn.setImageResource(favoIcons[1]);
					Toast.makeText(ItemDetail.this, "收藏成功!", Toast.LENGTH_SHORT)
							.show();
					FavoItemDbHelper
							.insert(db, title, pubdate, itemDetail,
									link, firstImgUrl, sectionTitle);//最后一个变成url了
					//最后一个值，原来是板块的名称如：国内新闻啥的，现在是这个的url
					System.out.println(sectionTitle);
				}
				Intent intent = new Intent();
				intent.putExtra("link", link);
				intent.putExtra("is_favorite", isFavorite);
				intent.setAction(ItemList.ACTION_UPDATE_ITEM_LIST);
				//将某个行为放到intent中，当intent被触发以后，就执行这个行为
				sendBroadcast(intent);
				
				new Thread()
				{
					@Override
					public void run()
					{
						Intent intent = getIntent();
						sectionUrl = intent.getStringExtra("section_url");//rss的url
						SeriaHelper helper = SeriaHelper.newInstance();
						File cache = AppContext.getSectionCache(sectionUrl);
						System.out.println("cache"+sectionUrl);//这里还是null//别的页的输出都是对的
						ItemListEntity entity = (ItemListEntity) helper
								.readObject(cache);
						ArrayList<FeedItem> items = entity.getItemList();
						for (FeedItem f : items)
						{
							if (f.getLink().equals(link))
								f.setFavorite(isFavorite);
						}
						entity.setItemList(items);
						helper.saveObject(entity, cache);
					}
				}.start();
			}
		});
		//countTv = (TextView) findViewById(R.id.fid_tv_comment_count);
		mWebView = (WebView) findViewById(R.id.my_web_view);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private void loadData()
	{
		Intent intent = getIntent();
		sectionTitle = intent.getStringExtra("section_url");//这里本来是rss的名称。现在改为插入url试试吧
		System.out.println("-------------------------"+sectionTitle);
		sectionUrl = intent.getStringExtra("section_url");//rss的url
		firstImgUrl = intent.getStringExtra("first_img_url");
		
		StringBuffer sb = new StringBuffer();
		title = intent.getStringExtra("title");
		pubdate = intent.getStringExtra("pubdate");
		itemDetail = intent.getStringExtra("item_detail");
		Log.e("ItemDetail", itemDetail);
		link = intent.getStringExtra("link");
		//过滤style
		itemDetail = itemDetail.replaceAll(HtmlFilter.regexpForStyle, "");
		//过滤img宽和高
		itemDetail = itemDetail.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		itemDetail = itemDetail.replaceAll(
				"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		itemDetail = itemDetail.replaceAll("<a (.*?)>", "");
		itemDetail = itemDetail.replaceAll("</a>", "");
		itemDetail = itemDetail.replaceAll("<A (.*?)>", "");
		itemDetail = itemDetail.replaceAll("</A>", "");
//		//图片双击
//		 itemDetail = itemDetail.replaceAll("(<img[^>]+src=\")(\\S+)\"",
//					"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
//		 mWebView.addJavascriptInterface(this, "mWebViewImageListener");
		 //是否加载图片
		SharedPreferences pref = AppContext.getPrefrences(this);
		if(!pref.getBoolean("pref_imageLoad", true))//默认是加载图片的方式
		{
			itemDetail = itemDetail.replaceAll("(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)", "");
		}
		sb.append("<h1>" + title + "</h1>");
		sb.append("<body>" + itemDetail + "</body>");
		mWebView.loadDataWithBaseURL(null, css + sb.toString(), "text/html", "UTF-8", null);
	}
	
	public void onImageClick(String url)
	{
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.setClass(this, ImageDialog.class);
		startActivity(intent);
	}
}

