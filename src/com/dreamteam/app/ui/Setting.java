package com.dreamteam.app.ui;

//rewrite by angela liu
//这个是设置模块

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.commons.AppConfig;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.utils.FileUtils;
import com.special.ResideMenuDemo.R;


public class Setting extends PreferenceActivity
{
	private SharedPreferences mPreferences;
	private CheckBoxPreference imageLoadCb;
	private Preference clearCachePref;
	private Preference feedbackPref;
	
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initPreference();
	}

	private void initView()
	{
		addPreferencesFromResource(R.xml.preference);
		ListView mLv = getListView();
		mLv.setBackgroundColor(0);
		mLv.setCacheColorHint(0);
		((ViewGroup) mLv.getParent()).removeView(mLv);
		ViewGroup localViewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.setting, null);
		//setting里面是包含所有的这里出现的选项的
		((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
				.addView(mLv, -1, -1);
		setContentView(localViewGroup);
	
		//return btn
		localViewGroup.findViewById(R.id.setting_return_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});;
		
	}
	
	private void initPreference()
	{
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		feedbackPref = findPreference("pref_feedback");
		//用户反馈模块，暂留，信息发到刘悦的邮箱
		feedbackPref.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent();
				intent.setClass(Setting.this, FeedbackUI.class);
				Setting.this.startActivity(intent);
				return false;
			}
		});
		imageLoadCb = (CheckBoxPreference) findPreference("pref_imageLoad");
		imageLoadCb.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				if(mPreferences.getBoolean("imageLoad", true))
				{
					//显示图片
					imageLoadCb.setSummary("加载图片（WIFI默认加载图片）");
				}
				else
				{
					imageLoadCb.setSummary("不加载图片（WIFI默认加载图片）");
				}
				return false;
			}
		});
		//缓存
		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File cacheDir = getCacheDir();
		File imageCacheDir = new File(AppConfig.APP_IMAGE_CACHE_DIR);
		File sectionCacheDir = new File(AppConfig.APP_SECTION_DIR);
		fileSize += FileUtils.getDirSize(cacheDir);
		fileSize += FileUtils.getDirSize(imageCacheDir);
		fileSize += FileUtils.getDirSize(sectionCacheDir);
		if(fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		
		clearCachePref = findPreference("pref_clearCache");
		clearCachePref.setSummary(cacheSize);
		clearCachePref.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				new AsyncTask<Integer, Integer, Integer>()
				{
					@Override
					protected void onPostExecute(Integer result)
					{
						Toast.makeText(Setting.this, "清理完毕！", Toast.LENGTH_SHORT).show();
						clearCachePref.setSummary("0KB");
					}

					@Override
					protected Integer doInBackground(Integer... params)
					{
						AppContext.clearCache(Setting.this);
						return 0;
					}
				}.execute(0);
				return false;
			}
		});
		
		//about这个是关于开源团队的介绍，不要
		/*
		findPreference("pref_about").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent intent = new Intent();
				intent.setClass(Setting.this, About.class);
				Setting.this.startActivity(intent);
				return true;
			}
		});*/
	}
}
