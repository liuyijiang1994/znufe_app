/**
 *  ClassName: BaseActivity.java
 *  created on 2012-2-26
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.TopicListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.forum.topic.TopicDetailActivity;
import net.shopnc.android.widget.MyProcessDialog;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author qjyong
 */
public class BaseActivity extends ListActivity implements UpdateHandle {
	public static final String TAG = "BaseActivity";

	private MyApp myApp;
	private Activity myParent;
	
	private MyProcessDialog mydialog;

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

	private String url;
	/**
	 * hjgang
	 * */
	private ImageButton  ibtu;
	private AlertDialog.Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplicationContext();
		myParent = this.getParent();
		pagesize = myApp.getPageSize();
		
		this.setContentView(R.layout.home_sub_banner);
		
		ibtu=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    ibtu.setVisibility(View.VISIBLE);
		
		initTitleBar();
		
		initPullView();
        
		datas = new ArrayList<Topic>();
		adapter = new TopicListViewAdapter(BaseActivity.this);
		setListAdapter(adapter);

		url = this.getIntent().getStringExtra("url");
		
		pv.startUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
		
		/**
		  * 排序
		  * */
			final String[] str={"发帖时间","回帖/查看","查看","最后发表","热门"};
			builder=new AlertDialog.Builder(myParent.getParent());
			final ArrayAdapter<String> adapter2=
					new ArrayAdapter<String>(myParent.getParent()
							,android.R.layout.simple_dropdown_item_1line
							,str);			
			ibtu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					builder.setTitle("选择排序方式")
					.setIcon(R.drawable.sort)
					.setAdapter(adapter2, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, final int which) {
										if (url != null) {
											if (str[which].equals("发帖时间")) {
												url += "&orderby=dateline";
											} else if (str[which]
													.equals("回帖/查看")) {
												url += "&orderby=replies";
											} else if (str[which].equals("查看")) {
												url += "&orderby=views";
											} else if (str[which]
													.equals("最后发表")) {
												url += "&orderby=lastpost";
											} else if (str[which].equals("热门")) {
												url += "&orderby=heats";
											}
											BaseActivity.this.setSelection(0);
											showDialog(Constants.ORDERBY_ID);
											mydialog.setMsg(getString(R.string.pull_to_refresh_refreshing));
											RemoteDataHandler.asyncGet(url,
													pagesize, pageno,
													new Callback() {
														@Override
														public void dataLoaded(
																ResponseData data) {
															dismissDialog(Constants.ORDERBY_ID);
															moreBtn.setText(txt_more_default);

															if (data.getCode() == HttpStatus.SC_OK) {
																Log.d(TAG,
																		"RemoteDataHanlder---dataLoaded");
																String json = data
																		.getJson();

																if (data.isHasMore()) {
																	moreLayout
																			.setVisibility(View.VISIBLE);
																	moreBtn.setVisibility(View.VISIBLE);
																	moreBtn.setEnabled(true);
																} else {
																	moreLayout
																			.setVisibility(View.GONE);
																	moreBtn.setVisibility(View.GONE);
																}

																if (pageno == 1) {
																	datas.clear();
																}

																datas.addAll(Topic
																		.newInstanceList(json));
																Log.d(TAG,
																		datas.toString());

																adapter.setDatas(datas);
																adapter.notifyDataSetChanged();
															}
														}
													});
										} else {
											Toast.makeText(BaseActivity.this,
													"排序失败，请刷新后尝试！", 0).show();
										}
									}
					}).create().show();
				}
			});
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(pageno = 1);
	}
	@Override
	protected void onResume() {
		super.onResume();
		ImageButton btn_sort=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    btn_sort.setVisibility(View.VISIBLE);

		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("BaseActivity", "back.....");
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
		Intent intent = new Intent(BaseActivity.this, TopicDetailActivity.class);
		Topic topic = (Topic)adapter.getItem(position);
		
		Log.d(TAG, position+"==" + topic.toString());
		
		intent.putExtra(Topic.TOPIC_TAG, topic);
		
		BaseActivity.this.startActivity(intent);
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
					
					datas.addAll(Topic.newInstanceList(json));
					Log.d(TAG, datas.toString());
					
					adapter.setDatas(datas);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(Constants.ORDERBY_ID == id){
			return createProgressDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private MyProcessDialog createProgressDialog(){
		mydialog = new MyProcessDialog(myParent.getParent());
		return mydialog;
	}
}
