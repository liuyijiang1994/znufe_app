/**
 *  ClassName: FocusActivity.java
 *  created on 2012-3-8
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.home;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.FocusListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.PushData;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.forum.topic.TopicDetailActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

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

/**首页-->焦点界面
 * @author qjyong
 */
public class FocusActivity extends ListActivity  implements UpdateHandle {
	public static final String TAG = "FocusActivity";

	private MyApp myApp;
	private HomeActivity myParent;
	
	private ImageButton btn_left;
	private ImageButton btn_sort;
	
	private PullView pv;
	private LinearLayout moreLayout;
	private Button moreBtn;
	private String txt_more_default;
	private String txt_more_wait;
	private FocusListViewAdapter adapter;
	private ArrayList<PushData> datas;
		
	private int pageno = 1;
	private int pagesize;

	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplicationContext();
		myParent = (HomeActivity)this.getParent();
		pagesize = myApp.getPageSize();
		
		this.setContentView(R.layout.home_sub_banner);
		
		btn_sort=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    btn_sort.setVisibility(View.GONE);
		initTitleBar();
		
		initPullView();
        
		datas = new ArrayList<PushData>();
		adapter = new FocusListViewAdapter(FocusActivity.this);
		setListAdapter(adapter);

		url = this.getIntent().getStringExtra("url");
		
		pv.startUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(pageno = 1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// TODO 好像没管用
		btn_sort=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    btn_sort.setVisibility(View.GONE);

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
		Intent intent = new Intent(FocusActivity.this, TopicDetailActivity.class);
		PushData pd = (PushData)adapter.getItem(position);
		
		Log.d(TAG, position+"==" + pd.toString());
		
		Topic t = new Topic();
		t.setTid(pd.getId());
		t.setSubject(pd.getTitle());
		intent.putExtra(Topic.TOPIC_TAG, t);
		
		FocusActivity.this.startActivity(intent);
	}

	private void loadPage(final int pageno){
		moreBtn.setEnabled(false);
		
		Log.d(TAG, "loadData...");
		
		RemoteDataHandler.asyncGet(url, pagesize, pageno, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				
				pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
				moreBtn.setText(txt_more_default);
				
				if(data.getCode() == HttpStatus.SC_OK){
					Log.d(TAG, "RemoteDataHanlder---dataLoaded");
					String json = data.getJson();
					
					if(data.isHasMore()){
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
					
					datas.addAll(PushData.newInstanceList(json));
					Log.d(TAG, datas.toString());
					
					adapter.setDatas(datas);
					adapter.notifyDataSetChanged();
				}
			}
		});

	}
}
