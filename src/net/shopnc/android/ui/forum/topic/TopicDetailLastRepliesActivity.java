/**
 *  ClassName: TopicDetailLastRepliesActivity.java
 *  created on 2012-3-7
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.topic;

import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.TopicDetailLastRepliesListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.more.LoginActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 选定帖子的"最新回复"显示方式界面
 * @author qjyong
 */
public class TopicDetailLastRepliesActivity extends ListActivity implements UpdateHandle {
	public static final String TAG = "TopicDetailLastRepliesActivity";
	private TopicDetailActivity parent;
	private MyApp myApp;
	private int pagesize;
	private int pageno = 1;
	private long count = 0;
	private long totalpage= 0;
	
	private TextView txt_title;
	private ImageButton btn_right;
	
	private ImageButton btn_pager_prev;
	private ImageButton btn_pager_next;
	private TextView txt_pager_info;
	private ImageButton btn_board_favorite;
	
	private TextView txt_topic_title;
	private PullView pv;
	private ArrayList<Topic> datas;
	private TopicDetailLastRepliesListViewAdapter adapter;
	
	private String subject;
	private long tid;
	private long fid;
	private String intent_url;
	private String url;
	
	private boolean display_postStarter_only;
	
	private int isreply;
	private int ispostimage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_detail);
		parent = (TopicDetailActivity)this.getParent();
		myApp = (MyApp)this.getApplicationContext();
		pagesize = myApp.getPageSize();
		display_postStarter_only = myApp.isDisplay_postStarter_only();
		
		
		Intent intent = parent.getIntent();
		Topic t = (Topic)intent.getExtras().get(Topic.TOPIC_TAG);
		tid = t.getTid();
		subject = t.getSubject();
		isreply = intent.getIntExtra("isreply", 0);
		ispostimage = intent.getIntExtra("ispostimage", 0);
		Log.d(TAG, "isreply=" + isreply + ",ispostimage=" + ispostimage);
		
		//设置主内容标题
		txt_topic_title = (TextView)this.findViewById(R.id.topic_title);
		txt_topic_title.setText(this.subject);
		
		
		//设置标题栏标题
		txt_title = (TextView)parent.findViewById(R.id.txt_title);
		Log.d(TAG, t.getSubject());
		txt_title.setText(t.getSubject());
		
		initPagerBar();
		
		initPullView();
		
		//记录浏览历史
		myApp.getLastestBrowseDao().record(t);
		
		datas = new ArrayList<Topic>();
		adapter = new TopicDetailLastRepliesListViewAdapter(this);
		adapter.setPagesize(pagesize);
		setListAdapter(adapter);

		intent_url = this.getIntent().getStringExtra("url");
		
		if(display_postStarter_only && intent_url.equals(Constants.URL_TOPIC_DETAIL_DEFAULT)){
			url = Constants.URL_TOPIC_DETAIL_LANDLOAD;
		}else{
			url = intent_url;
		}
		pv.startUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		boolean temp = myApp.isDisplay_postStarter_only(); 
		//如果只显示楼主帖子且是默认显示界面
		if(display_postStarter_only != temp && url.equals(Constants.URL_TOPIC_DETAIL_DEFAULT)){
			display_postStarter_only = temp;
			if(display_postStarter_only){
				url = Constants.URL_TOPIC_DETAIL_LANDLOAD;
			}else{
				url = intent_url;
			}
			pv.startUpdate();
		}
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(pageno);
	}
	
	private void initTitleBar(){
		btn_right = (ImageButton)parent.findViewById(R.id.btn_right);
		
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){
			btn_right.setBackgroundResource(R.drawable.btn_replytopic);
		}else{
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
		
		btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){//跳转到回帖
					//QIUJY 回帖权限判断
					HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
					Log.d(TAG, subBoardMap.toString());
					if(subBoardMap != null){
						Board b = subBoardMap.get(Long.valueOf(fid));
						Log.d(TAG, b.toString());
						//if(b.getIsreply() == 1){
							Intent intent = new Intent(TopicDetailLastRepliesActivity.this, ReplyTopicActivity.class);
							intent.putExtra("fid", TopicDetailLastRepliesActivity.this.fid);
							intent.putExtra("tid", TopicDetailLastRepliesActivity.this.tid);
							intent.putExtra("ispostimage", b.getIspostimage());
							
							TopicDetailLastRepliesActivity.this.startActivityForResult(intent, 100);
						/*}else{
							Toast.makeText(TopicDetailLastRepliesActivity.this, "你没有权限在本版回帖！", Toast.LENGTH_SHORT).show();
						}*/
					}
				}else{//未登录先跳转到登录
					//Toast.makeText(TopicDetailLastRepliesActivity.this, "回帖请先登录！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(TopicDetailLastRepliesActivity.this, LoginActivity.class);
					TopicDetailLastRepliesActivity.this.startActivityForResult(intent, 200);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode){
		case 100:  //回帖成功
        	Log.d(TAG, "post succ, load datas");
        	pageno = 1;
        	pv.startUpdate();
        	break;
        case 200: //登录成功 
        	btn_right.setBackgroundResource(R.drawable.btn_replytopic);
        	pv.startUpdate();
        	break;
        case 300: //引用回复成功 
        	pageno = 1;
        	pv.startUpdate();
        	break;
		}
	}
	
	private void initPullView(){
		pv = (PullView)this.findViewById(R.id.pv);
        this.pv.setUpdateHandle(this);
	}
	
	private void loadPage(final int pageno){
		if(-1 == SystemHelper.getNetworkType(this)){
			Toast.makeText(TopicDetailLastRepliesActivity.this, "网络连接失败，请检查设备!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		RemoteDataHandler.asyncGet(url + tid, pagesize, pageno, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
				
				if(data.getCode() == HttpStatus.SC_OK){
					String json = data.getJson();
				
					datas.clear();
					
					ArrayList<Topic> topics = Topic.newInstanceList(json);
					datas.addAll(topics);
					adapter.setTid(tid);
					
					Topic t = datas.get(datas.size() - 1);
					if(t.getSubject() != null && !t.getSubject().equals("")){
						datas.remove(datas.size() - 1);
					}
					
					adapter.setDatas(datas);
					adapter.notifyDataSetChanged();
					
					//设置分页信息...
					TopicDetailLastRepliesActivity.this.setSelection(0);
					count = data.getCount() - 1;
					totalpage = ((count + pagesize - 1) / pagesize);
					
					adapter.setPagesize(pagesize);
					adapter.setPageno(pageno);
					adapter.setCount(count);
					
					if(count > 0){
						fid = topics.get(0).getFid(); //记录版块ID
						if(fid!=0){
							initTitleBar();
						}
						txt_pager_info.setText(pageno + "/" + totalpage);
					}
					
				}else if(data.getCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR){
					Toast.makeText(TopicDetailLastRepliesActivity.this, "网络数据有误，请刷新重试!", Toast.LENGTH_SHORT).show();
				}else if(data.getCode() == HttpStatus.SC_REQUEST_TIMEOUT){
					Toast.makeText(TopicDetailLastRepliesActivity.this, "网络状况不好，请刷新重试!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void initPagerBar(){
		txt_pager_info = (TextView)this.findViewById(R.id.txt_pager_info);
		
		btn_pager_prev = (ImageButton)this.findViewById(R.id.btn_pager_prev);
		btn_pager_next = (ImageButton)this.findViewById(R.id.btn_pager_next);
		
		btn_board_favorite = (ImageButton)this.findViewById(R.id.btn_board_favorite);
		btn_board_favorite.setVisibility(View.INVISIBLE);
		
		MyOnClickListener listener = new MyOnClickListener();
		btn_pager_prev.setOnClickListener(listener);
		btn_pager_next.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
	}
	
	class MyOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			Log.d(TAG, ""+v.getId());
			switch(v.getId()){
			case R.id.btn_pager_prev:
				if(totalpage > 1 && pageno > 1){
					--pageno;
					pv.startUpdate();
				}else{
					Toast.makeText(TopicDetailLastRepliesActivity.this, "已经是第1页了!", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_pager_next:
				if(totalpage > 1 && pageno < totalpage){
					++pageno;
					pv.startUpdate();
				}else{
					Toast.makeText(TopicDetailLastRepliesActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_board_favorite:
				break;
			}
		}
	}
}