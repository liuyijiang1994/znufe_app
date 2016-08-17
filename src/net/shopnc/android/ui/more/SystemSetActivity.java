/**
 *  ClassName: SystemSetActivity.java
 *  created on 2012-2-24
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.MenuListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.IOHelper;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.dao.FavoriteDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 系统设置
 * @author qjyong
 */
public class SystemSetActivity extends Activity {
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	
	private ListView lv_flow;
	private MenuListViewAdapter adapter;
	private RelativeLayout clear_cache;
	private TextView txt_cache_size;
	
	/** 恢复默认值 */
	private Button btn_reset;
	
	private MyApp myApp;
	private File cacheDir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.more_sysset);
		
		myApp = (MyApp)this.getApplicationContext();
		
		initTitleBar();
		
		//
		lv_flow = (ListView)this.findViewById(R.id.lv_flow);
		clear_cache = (RelativeLayout)this.findViewById(R.id.clear_cache);
		txt_cache_size = (TextView)this.findViewById(R.id.txt_cache_size);
		
		cacheDir = new File(Constants.CACHE_DIR);
		
		totalCacheSize();
		
		btn_reset = (Button)this.findViewById(R.id.btn_reset);
		
		initFlowClick();
		
		initCacheClick();
		
		initResetClick();
	}
	
	private void totalCacheSize(){
		long size = IOHelper.totalFileSize(cacheDir);
		NumberFormat nf = new DecimalFormat("#,##0.00");
		txt_cache_size.setText(this.getString(R.string.more_sysset_cache_size, nf.format(size/1048576.0)));
		
	}
	
	private void initResetClick(){
		btn_reset.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				HashMap<String, Object> item = (HashMap<String, Object>)adapter.getItem(0);
				item.put(MenuListViewAdapter.TAG_ITEM_CHECKED, false);
				myApp.setDisplay_postStarter_only(false);
				
				HashMap<String, Object> item2 = (HashMap<String, Object>)adapter.getItem(1);
				item2.put(MenuListViewAdapter.TAG_ITEM_CHECKED, true);
				myApp.setImg_invisible(true);
				
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initCacheClick(){
		clear_cache.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(Constants.DIALOG_CLEAR_CACHE_ID);
			}
		});
	}
	
	private void initFlowClick(){
		lv_flow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				final HashMap<String, Object> item = (HashMap<String, Object>)adapter.getItem(position);
				
				boolean flag = ((Boolean)item.get(MenuListViewAdapter.TAG_ITEM_CHECKED)).booleanValue();
				boolean result = !flag;
				
				item.put(MenuListViewAdapter.TAG_ITEM_CHECKED, Boolean.valueOf(result));
				adapter.notifyDataSetChanged();
				
				if(0 == position){
					myApp.setDisplay_postStarter_only(result);
				}else if(1 == position){
					myApp.setImg_invisible(result);
				}
			}
		});
	}
	
	private void initTitleBar(){
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.more_sysset));
		
		//设置标题栏按钮
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SystemSetActivity.this.finish();
			}
		});
		
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.INVISIBLE);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		/////////////////////////////////////
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put(MenuListViewAdapter.TAG_ITEM_TEXT, Integer.valueOf(R.string.more_sysset_display_poststarter_only));
		map1.put(MenuListViewAdapter.TAG_ITEM_CHECKED, Boolean.valueOf(myApp.isDisplay_postStarter_only()));
		datas.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put(MenuListViewAdapter.TAG_ITEM_TEXT, Integer.valueOf(R.string.more_sysset_img_invisible));
		map2.put(MenuListViewAdapter.TAG_ITEM_CHECKED, Boolean.valueOf(myApp.isImg_invisible()));
		datas.add(map2);
		
		adapter = new MenuListViewAdapter(SystemSetActivity.this, datas);
		lv_flow.setAdapter(adapter);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == Constants.DIALOG_CLEAR_CACHE_ID){
			return createDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private Dialog createDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(
				SystemSetActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("提示信息")
				.setMessage("是否清除缓存？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//還需要刪除本地收藏及本地流覽歷史
						int count = IOHelper.clearCacheFolder(cacheDir);
						Log.d("SystemSetActivity", "清除了" + count + "个文件");
						
						myApp.getLastestBrowseDao().deleteAll();
						FavoriteDao fd = new FavoriteDao(SystemSetActivity.this);
						fd.deleteAll();
						
						totalCacheSize();
						
						dialog.dismiss();
						Toast.makeText(SystemSetActivity.this, "缓存已清除", 0).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		return dialog;
	}
}
