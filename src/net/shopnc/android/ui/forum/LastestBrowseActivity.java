/**
 *  ClassName: LastestBrowseActivity.java
 *  created on 2012-3-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.TopicListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.dao.LastestBrowseDao;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.forum.topic.TopicDetailActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 最后浏览
 * @author qjyong
 */
public class LastestBrowseActivity extends ListActivity implements UpdateHandle {
	public static final String TAG = "LastestBrowseActivity";

	private MyApp myApp;
	private Activity myParent;
	
	private ImageButton btn_left;

	private PullView pv;
	private LinearLayout moreLayout;
	private Button moreBtn;
	private String txt_more_default;
	private String txt_more_wait;
	private TopicListViewAdapter adapter;
	private ArrayList<Topic> datas;
		
	private int pageno = 1;
	private int pagesize;

	private LastestBrowseDao lbDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		myParent = this.getParent();
		pagesize = myApp.getPageSize();
		lbDao = myApp.getLastestBrowseDao();
		
		this.setContentView(R.layout.home_sub_banner);
		
		initTitleBar();
		
		initPullView();
        
		datas = new ArrayList<Topic>();
		adapter = new TopicListViewAdapter(LastestBrowseActivity.this);
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		onUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(pageno = 1);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("HeadlinesActivity", "back.....");
		if(keyCode == KeyEvent.KEYCODE_BACK){
			((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_EXITAPP_ID);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void initTitleBar(){
		//刷新
		btn_left = (ImageButton)myParent.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				pv.startUpdate();
			}
		});
	}
	
	private void initPullView(){
		pv = (PullView)this.findViewById(R.id.pv);
        this.pv.setUpdateHandle(this);
        
        this.moreLayout = ((LinearLayout)LayoutInflater.from(this).inflate(R.layout.next_page_layout, null));
        this.getListView().addFooterView(this.moreLayout);
        
		this.moreBtn = ((Button) findViewById(R.id.show_more_btn));
		txt_more_default = this.getString(R.string.footview_more, pagesize);
		txt_more_wait = this.getString(R.string.footview_wait);
		
		this.moreBtn.setText(txt_more_default);
		this.moreBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				moreBtn.setText(txt_more_wait);
				loadPage(++pageno);
			}
		});
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//Toast.makeText(this, "position-->"+ position, 0).show();
		
		//查看帖子详细信息
		Intent intent = new Intent(LastestBrowseActivity.this, TopicDetailActivity.class);
		Topic topic = (Topic)adapter.getItem(position);
		
		Log.d(TAG, position+"==" + topic.toString());
		intent.putExtra(Topic.TOPIC_TAG, topic);
		
		LastestBrowseActivity.this.startActivity(intent);
	}

	private void loadPage(final int pageno){
		moreBtn.setEnabled(false);

		pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
		moreBtn.setText(txt_more_default);
		
		ArrayList<Topic> temp = lbDao.findByPager(pageno, pagesize);
		if(temp.size() == 20){
		
			moreLayout.setVisibility(View.VISIBLE);
	        moreBtn.setVisibility(View.VISIBLE);
	        moreBtn.setEnabled(true);
		}else{
			moreLayout.setVisibility(View.GONE);
	        moreBtn.setVisibility(View.GONE);
		}
			
		if(pageno == 1){
			datas.clear();
		}
		
		datas.addAll(temp);
		Log.d(TAG, datas.toString());
		
		adapter.setDatas(datas);
		adapter.notifyDataSetChanged();
	}
}
