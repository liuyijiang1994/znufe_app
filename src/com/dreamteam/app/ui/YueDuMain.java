package com.dreamteam.app.ui;
//这一部分应该就是呈现出来的主界面
//rewrite by angela liu
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.GridAdapter;
import com.dreamteam.app.adapter.MPagerAdapter;
import com.dreamteam.app.commons.AppConfig;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.commons.UIHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FeedDBManager;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.app.entity.Section;
import com.dreamteam.app.utils.ImageUtils;
import com.dreamteam.custom.ui.PathAnimations;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenuDemo.HomePageScreen;
import com.special.ResideMenuDemo.MenuActivity;
import com.special.ResideMenuDemo.R;

import edu.swust.iweather.Weather;

public class YueDuMain extends FragmentActivity
{
	public static final String tag = "Main";
	private ViewPager mPager;
	private MPagerAdapter mPagerAdapter;
	private RelativeLayout composerWrapper;
	private RelativeLayout composerShowHideBtn;
	private RelativeLayout bgLayout;
	private ImageView composerShowHideIconIv;
	private TextView pageTv;
	private ImageButton switchModeBtn;
	private RelativeLayout homeLoadingLayout;
	private ArrayList<GridView> gridViews = new ArrayList<GridView>();
	private ArrayList<GridAdapter> gridAdapters = new ArrayList<GridAdapter>();
	private BroadcastReceiver mReceiver;
	private boolean arePathMenuShowing;
	public static final int PAGE_SECTION_SIZE = 8;// 一页8个section
	public static final String ACTION_ADD_SECTION = "com.dreamteam.app.action.add_section";
	public static final String ACTION_DELETE_SECTION = "com.dreamteam.app.action.delete_section";
	public static final int PAGE_SIZE_INCREASE = 1;
	public static final int PAGE_SIZE_NOT_CHANGE = 0;
	public static final int PAGE_SIZE_DECREASE = -1;
	private Intent mIntent;
	private boolean exit = false;//双击退出
	private boolean isEdting = false;//是否编辑section中
	private boolean isNight;//是否为夜间模式
	
	private Button btn_back;


	//在创建后的函数里面调用相关功能
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		checkShortcutMsg();
		initView();
		writeDB();
		//写入计算缓存天数的文件
		writeSaveDaysFile();
		//删除之前的缓存文件
		AppContext.clearSdCache();
		initPathMenu();
		initPager();
		initBroadcast();
		checkDeprecated();
	}



	private void writeSaveDaysFile()
	{
		String fileName = getFilesDir().getAbsolutePath() + File.separator 
				+ AppConfig.PREF_DEPRECATED;
		File file = new File(fileName);
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeDB()
	{
		InputStream inputStream = null;
		try
		{
			inputStream = getAssets().open("feed.db");
			FeedDBManager helper = new FeedDBManager(this, FeedDBManager.DB_NAME, null, 1);
			SQLiteDatabase db = helper.getWritableDatabase();
			File dbFile = new File(db.getPath());
			if(dbFile.exists())
			{
				dbFile.delete();
			}
			FileOutputStream fos = null;

			try
			{
				fos = new FileOutputStream(dbFile);
				byte buffer[] = new byte[1024 * 4];
				while((inputStream.read(buffer)) != -1)
				{
					fos.write(buffer);
				}
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(inputStream != null)
					{
						inputStream.close();
						inputStream = null;
					}
					if(fos != null)
					{
						fos.close();
						fos = null;
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				db.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	//检查是否来自shortcut的动作
	private void checkShortcutMsg()
	{
		Intent intent = getIntent();
		if(intent != null)
		{
			String action = intent.getAction();
			if(action != null && action.equals(GridAdapter.ACTION_ENTER_BY_SHORTCUT))
			{
				Intent indirectIntent = new Intent();
				indirectIntent.putExtra("section_title", intent.getStringExtra("section_title"));
				indirectIntent.putExtra("url", intent.getStringExtra("url"));
				indirectIntent.setClass(this, ItemList.class);
				startActivity(indirectIntent);
			}
		}
	}

	private void initBroadcast()
	{
		mReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				if (action.equals(ACTION_ADD_SECTION))
				{
					// 最后一个adapter为空或已满，新生一个gridView
					Log.d(tag, gridAdapters.size() + "adapters");
					Log.d(tag, gridViews.size() + "views");
					GridAdapter lastGridAdapter = getLastGridAdapter();
					if (lastGridAdapter == null || lastGridAdapter.isFull())
					{
						addGridView();
					} else
					{
						// 最后一个gridAdapter添加section
						lastGridAdapter.addItem(getNewSection());
					}
				} else if (action.equals(ACTION_DELETE_SECTION))
				{
					// 根据移除此section
					GridAdapter deCreaseAdapter = null;

					String url = intent.getStringExtra("url");
					for (int i = 0; i < gridAdapters.size(); i++)
					{
						deCreaseAdapter = gridAdapters.get(i);
						if (deCreaseAdapter.removeItem(url))
						{
							break;
						}
					}
					GridAdapter lastAdapter = getLastGridAdapter();
					if (lastAdapter.isEmpty())
					{
						if (gridViews.size() <= 1)
						{
							return;
						}
						removeLastGridView();
						return;
					}
					if (!lastAdapter.equals(deCreaseAdapter))
					{
						Section section = lastAdapter.getLastItem();
						deCreaseAdapter.addItem(section);
						lastAdapter.removeItem(section.getUrl());
					}
					if (lastAdapter.isEmpty())
					{
						if (gridViews.size() <= 1)
						{
							return;
						}
						removeLastGridView();
						removeLastGridAdapter();
					}
				}
				/*else if(action.equals(SwitchBg.SWITCH_HOME_BG))
				{
					int resid = intent.getIntExtra("home_bg_id", R.drawable.home_bg_default);
					bgLayout.setBackgroundResource(resid);
					Editor editor = AppContext.getPrefrences(YueDuMain.this).edit();
					editor.putInt("home_bg_id", resid);
					editor.commit();
				}*/
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_ADD_SECTION);
		filter.addAction(ACTION_DELETE_SECTION);
		//filter.addAction(SwitchBg.SWITCH_HOME_BG);
		registerReceiver(mReceiver, filter);
	}

	// 获取表新加入的section
	private Section getNewSection()
	{
		Section section = new Section();
		DbManager mgr = new DbManager(YueDuMain.this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbManager.SECTION_TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToLast())
		{
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String tableName = cursor.getString(cursor
					.getColumnIndex("table_name"));
			section.setTitle(title);
			section.setUrl(url);
			section.setTableName(tableName);
		}
		db.close();
		return section;
	}

	private void initPathMenu()
	{
		PathAnimations.initOffset(this);
		composerWrapper = (RelativeLayout) findViewById(R.id.composer_wrapper);
		composerShowHideBtn = (RelativeLayout) findViewById(R.id.composer_show_hide_button);
		composerShowHideIconIv = (ImageView) findViewById(R.id.composer_show_hide_button_icon);
		composerShowHideBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!arePathMenuShowing)
				{
					PathAnimations.startAnimationsIn(composerWrapper, 300);
					composerShowHideIconIv.startAnimation(PathAnimations
							.getRotateAnimation(0, -270, 300));
				} else
				{
					PathAnimations.startAnimationsOut(composerWrapper, 300);
					composerShowHideIconIv.startAnimation(PathAnimations
							.getRotateAnimation(-270, 0, 300));
				}
				arePathMenuShowing = !arePathMenuShowing;
			}
		});

		// Buttons事件处理
		for (int i = 0; i < composerWrapper.getChildCount(); i++)
		{
			composerWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							switch (v.getId())
							{					
							case R.id.composer_btn_favorite:
								openFavorite();
								//收藏列表
								break;
							case R.id.composer_btn_add:
								openSubscribeCenter();
								//订阅中心
								break;
							case R.id.composer_btn_moon:
								showWeather();
								//这里把天气放进来
								break;
							case R.id.composer_btn_setting:
								openSetting();
								//设置
								break;
							}
						}

					});
		}
		composerShowHideBtn.startAnimation(PathAnimations.getRotateAnimation(0,
				360, 200));
	}


	//进入天气板块
	private void showWeather()
	{
		Intent intent = new Intent();
		intent.setClass(YueDuMain.this, Weather.class);
		YueDuMain.this.startActivity(intent);		
	}

	//收藏列表
	private void openFavorite()
	{
		Intent intent = new Intent();
		intent.setClass(YueDuMain.this, FavoriteItemList.class);
		YueDuMain.this.startActivity(intent);
	}

	//打开设置界面
	//在这里我应该要修改好多东西，哈哈，悦哥加油噢
	private void openSetting()
	{
		Intent intent = new Intent();
		intent.setClass(YueDuMain.this, Setting.class);
		YueDuMain.this.startActivity(intent);
	}

	// 打开订阅中心
	private void openSubscribeCenter()
	{
		Intent intent = new Intent();
		intent.setClass(this, FeedCategory.class);
		startActivity(intent);
	}

	private void initPager()
	{
		int pageSize = getPageSize();
		for (int i = 0; i < pageSize; i++)
		{
			gridViews.add(newGridView(i));
			mPagerAdapter.notifyDataSetChanged();
		}
	}

	//调用这个方法，进入到主界面，这里得好好改改，不能有太深的印迹，而且注意原作者留下的后门
	private void initView()
	{
		UIHelper.initTheme(this);
		setContentView(R.layout.yue_du_main);
		switchModeBtn = (ImageButton) findViewById(R.id.composer_btn_moon);//夜间模式
		pageTv = (TextView) findViewById(R.id.home_page_tv);
		homeLoadingLayout = (RelativeLayout) findViewById(R.id.home_loading_layout);//加载
		bgLayout = (RelativeLayout) findViewById(R.id.home_bg_layout);
		mPager = (ViewPager) findViewById(R.id.home_pager);
		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				pageTv.setText(position + 1 + "");
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		mPagerAdapter = new MPagerAdapter(gridViews);
		mPager.setAdapter(mPagerAdapter);
		
		
		//back to residemenu
	/*	btn_back=(Button)this.findViewById(R.id.title_bar_menu);
		btn_back.findViewById(R.id.title_bar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        			if(isEdting)
        			{
        				//在编辑，取消编辑
        				outSectionEdit();
        			}
        			else
        			{
        				//直接退出
        				 Intent intent=new Intent();   
        		            intent.setClass(YueDuMain.this, MenuActivity.class);   
        		            startActivity(intent);   
        				YueDuMain.this.finish();
        			}
        		}
        });*/
	}

	private GridView newGridView(int currentPage)
	{
		GridView grid = new GridView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		grid.setLayoutParams(params);
		int right = ImageUtils.dip2px(this, 50);
		int left = ImageUtils.dip2px(this, 20);
		int top = ImageUtils.dip2px(this, 20);
		int bottom = ImageUtils.dip2px(this, 20);
		grid.setPadding(left, top, right, bottom);
		grid.setNumColumns(2);
		grid.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				GridAdapter adapter = gridAdapters.get(
						mPager.getCurrentItem());
				Section section = (Section) adapter.getItem(position);
				String title = section.getTitle();
				String url = section.getUrl();
				Log.d(tag, url);
				//初始intent
				mIntent = new Intent();
				mIntent.putExtra("section_title", title);
				mIntent.putExtra("url", url);
				mIntent.setClass(YueDuMain.this, ItemList.class);

				//读取缓存
				File cache = SectionHelper.getSdCache(url);
				if(cache.exists())
				{

					YueDuMain.this.startActivity(mIntent);
				}
				else
				{
					if(!AppContext.isNetworkAvailable(YueDuMain.this))
					{
						Toast.makeText(YueDuMain.this, R.string.no_network, Toast.LENGTH_SHORT).show();
						return;
					}
					//异步加载数据
					Log.d(tag, "" + url);
					new LoadDataTask().execute(url);
				}
			}
		});
		//长按进入删除section状态
		grid.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				inSectionEdit();
				return false;
			}
		});

		ArrayList<Section> sections = null;
		try
		{
			sections = readSections(currentPage);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		GridAdapter gridAdapter = new GridAdapter(this, sections);
		gridAdapters.add(gridAdapter);
		grid.setAdapter(gridAdapter);
		return grid;
	}

	private void inSectionEdit()
	{
		isEdting = true;
		int isVisble = 1;
		//section不可再点击
		for(int i = 0; i < gridViews.size(); i++)
		{
			gridViews.get(i).setEnabled(false);
		}
		for(int i = 0; i < gridAdapters.size(); i++)
		{
			gridAdapters.get(i).changeDelBtnState(isVisble);
		}
		Toast.makeText(this, "按下返回键退出编辑模式", Toast.LENGTH_SHORT).show();
	}

	//退出编辑模式
	private void outSectionEdit()
	{
		isEdting = false;
		int isVisble = 0;

		for(int i = 0; i < gridViews.size(); i++)
		{
			gridViews.get(i).setEnabled(true);
		}
		for(int i = 0; i < gridAdapters.size(); i++)
		{
			gridAdapters.get(i).changeDelBtnState(isVisble);
		}
	}

	private ArrayList<Section> readSections(int page) throws Exception
	{
		ArrayList<Section> sections = null;
		int len = 0;// 表长
		int start = 0;// 其实读
		int end = 0;// 结尾
		Log.i(tag, "page = " + page);
		// 从数据库读数据
		DbManager mgr = new DbManager(YueDuMain.this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbManager.SECTION_TABLE_NAME, 
				null, null, null, null, null, null);
		len = cursor.getCount();
		db.close();

		start = page * YueDuMain.PAGE_SECTION_SIZE;
		if (cursor.moveToPosition(start))
		{
			sections = new ArrayList<Section>();

			int offset = start + YueDuMain.PAGE_SECTION_SIZE;
			end = len < offset ? len : offset;
			for (int i = start; i < end; i++)
			{
				Section s = new Section();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				String tableName = cursor.getString(cursor.getColumnIndex("table_name"));
				s.setTitle(title);
				s.setUrl(url);
				s.setTableName(tableName);
				sections.add(s);
				cursor.moveToNext();
			}
		}
		return sections;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// 销毁广播接收器
		unregisterReceiver(mReceiver);
	}

	private void addGridView()
	{
		int lastPage = getPageSize() - 1;
		GridView grid = newGridView(lastPage);
		gridViews.add(grid);
		mPagerAdapter.notifyDataSetChanged();
	}

	private void removeLastGridView()
	{
		if (gridViews.isEmpty())
			return;
		gridViews.remove(gridViews.size() - 1);
		mPagerAdapter.notifyDataSetChanged();
	}

	private GridAdapter getLastGridAdapter()
	{
		if (gridAdapters.isEmpty())
			return null;
		return gridAdapters.get(gridAdapters.size() - 1);
	}

	private void removeLastGridAdapter()
	{
		if (gridAdapters.isEmpty())
			return;
		gridAdapters.remove(gridAdapters.size() - 1);
	}

	// 从1记
	private int getPageSize()
	{
		// 从数据库读数据
		DbManager mgr = new DbManager(YueDuMain.this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbManager.SECTION_TABLE_NAME, 
				null, null, null, null, null, null);
		// pager分页
		int pageSize = 0;
		int sectionCount = cursor.getCount();
		db.close();

		if (sectionCount % PAGE_SECTION_SIZE == 0)
			pageSize = sectionCount / PAGE_SECTION_SIZE;
		else
			pageSize = sectionCount / PAGE_SECTION_SIZE + 1;
		return pageSize;
	}

	private class LoadDataTask extends AsyncTask<String, Integer, ItemListEntity>
	{

		@Override
		protected void onPreExecute()
		{
			homeLoadingLayout.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ItemListEntity result)
		{
			homeLoadingLayout.setVisibility(View.GONE);
			//跳转界面
			if(result != null || mIntent != null || !result.getItemList().isEmpty())
			{
				YueDuMain.this.startActivity(mIntent);
			}
			else
			{
				Toast.makeText(YueDuMain.this, "网络异常！", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected ItemListEntity doInBackground(String... params)
		{
			ItemListEntityParser parser = new ItemListEntityParser();
			ItemListEntity entity = parser.parse(params[0]);
			if(entity != null)
			{
				SeriaHelper helper = SeriaHelper.newInstance();
				File cache = SectionHelper.newSdCache(params[0]);
				helper.saveObject(entity, cache);
			}
			return entity;
		}
	}

	//返回true时，表示已经完整地处理了这个事件，并不希望其他的回调方法再次进行处理，
	//而当返回false时，表示并没有完全处理完该事件，更希望其他回调方法继续对其进行处理，
	//例如Activity中的回调方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(isEdting)
			{
				//在编辑，取消编辑
				outSectionEdit();
			}
			else
			{
				//直接退出
				 Intent intent=new Intent();   
		            intent.setClass(YueDuMain.this, MenuActivity.class);   
		            startActivity(intent);   
				YueDuMain.this.finish();
				/*if(exit)
				{
					Log.d(tag, "exit");
					finish();
					return true;
				}
				Toast.makeText(this, "再按下退出程序", Toast.LENGTH_SHORT).show();
				exit = true;
				Log.d(tag, "after toast");*/
			}
		}
		return true;
	}

	/**
	 * @description 检查缓存文件是否过期
	 */
	private void checkDeprecated()
	{
		String fileName = getFilesDir().getAbsolutePath() + File.separator 
				+ AppConfig.PREF_DEPRECATED;
		File file = new File(fileName);
		int day = (int) (System.currentTimeMillis() - file.lastModified())/(24*60*60*1000);
		Log.d(tag, "day = " + day);
		if(day >= 7)
		{
			AppContext.clearCache(this);
			file.setLastModified(System.currentTimeMillis());
		}
	}
}