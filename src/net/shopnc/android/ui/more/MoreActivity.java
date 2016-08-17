/**
 *  ClassName: MoreActivity.java
 *  created on 2012-2-23
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;


import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.SubmenuListViewAdapter;
import net.shopnc.android.common.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 更多
 */
public class MoreActivity extends Activity {
	private TextView txt_title;
	private ImageButton btn_left;
	private ListView lv;
	private SubmenuListViewAdapter adapter;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.more);
		
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.main_more));
		
		btn_left = (ImageButton) this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		lv = (ListView)this.findViewById(R.id.lv);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = null;
				switch((int)id){
				case R.drawable.ic_list_sysset:
					intent = new Intent(MoreActivity.this, SystemSetActivity.class);
					break;
				case R.drawable.ic_list_accmng:
					intent = new Intent(MoreActivity.this, AccountMngActivity.class);
					break;	
				case R.drawable.ic_list_feedback:
					intent = new Intent(MoreActivity.this, FeedbackActivity.class);
					break;
				case R.drawable.ic_list_aboutus:
					intent = new Intent(MoreActivity.this, AboutUsActivity.class);
					break;
				}
				
				if(null != intent){
					MoreActivity.this.startActivity(intent);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//Init ListView Item Data
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put(SubmenuListViewAdapter.TAG_ITEM_TEXT, this.getString(R.string.more_sysset));
		map1.put(SubmenuListViewAdapter.TAG_ITEM_ICON, Integer.valueOf(R.drawable.ic_list_sysset));
		datas.add(map1);
		
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put(SubmenuListViewAdapter.TAG_ITEM_TEXT, this.getString(R.string.more_accmng));
		map2.put(SubmenuListViewAdapter.TAG_ITEM_ICON, Integer.valueOf(R.drawable.ic_list_accmng));
		datas.add(map2);
		
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put(SubmenuListViewAdapter.TAG_ITEM_TEXT, this.getString(R.string.more_feedback));
		map3.put(SubmenuListViewAdapter.TAG_ITEM_ICON, Integer.valueOf(R.drawable.ic_list_feedback));
		datas.add(map3);
		
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put(SubmenuListViewAdapter.TAG_ITEM_TEXT, this.getString(R.string.more_aboutus));
		map4.put(SubmenuListViewAdapter.TAG_ITEM_ICON, Integer.valueOf(R.drawable.ic_list_aboutus));
		datas.add(map4);
		
		adapter = new SubmenuListViewAdapter(MoreActivity.this, datas);
		lv.setAdapter(adapter);
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("MoreActivity", "back.....");
		if(keyCode == KeyEvent.KEYCODE_BACK){
			getParent().showDialog(Constants.DIALOG_EXITAPP_ID);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}*/
}