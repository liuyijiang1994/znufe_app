/**
 *  ClassName: TopicAllActivity.java
 *  created on 2012-3-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.board;

import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.TopicListViewAdapter;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.dao.FavoriteDao;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.forum.topic.SendTopicActivity;
import net.shopnc.android.ui.forum.topic.TopicDetailActivity;
import net.shopnc.android.ui.more.LoginActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 选定版块下的所有帖子列表的界面
 * @author qjyong
 */

public class TopicChildAllActivity extends ListActivity implements UpdateHandle {
	public static final String TAG = "TopicAllActivity";
	
	private TopicChildListActivity parent;
	private MyApp myApp;
	private ImageButton btn_right;
	private TextView txt_title;
	
	private PullView pv;
	private TopicListViewAdapter adapter;
	private ArrayList<Topic> datas;
	
	private ImageButton btn_pager_prev;
	private ImageButton btn_pager_next;
	private TextView txt_pager_info;
	private ImageButton btn_board_favorite;
	
	private String boardName;
	private long fid;
	private long fup;
	private int ispost;
	private int isreply;
	private int ispostimage;
	
	private int pagesize;
	private int pageno = 1;
	private long count = 0;
	private long totalpage= 0;
	
	private String url;
	
	private FavoriteDao dao;
	

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.board_topic_list);
		parent = (TopicChildListActivity)this.getParent();
		
		Intent intent = this.getIntent();
	    this.boardName = intent.getStringExtra("boardName");
	    this.fid = intent.getExtras().getLong("fid");
	    this.url = intent.getStringExtra("url");
	    this.ispost = intent.getExtras().getInt("ispost");
	    this.isreply = intent.getExtras().getInt("isreply");
	    this.ispostimage = intent.getExtras().getInt("ispostimage");
	    
	    Log.d(TAG, "wo * fid===" + fid +"FUP"+fup+ ",boardName=" + boardName + ",url==" + intent.getStringExtra("url"));
	    Log.d(TAG, "ispost=" + ispost +",isreply==" + isreply  + ",ispostimage=" + ispostimage);
	    
	    myApp = (MyApp)this.getApplication();
		pagesize = myApp.getPageSize();
		
		dao = new FavoriteDao(this);
		
		initTopButton();
		initPagerBar();
		
		initPullView();
		
		pageno = 1;
		pv.startUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
		
	}
	
	@Override
	public void onUpdate() {
		loadPage();
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(TopicChildAllActivity.this, TopicDetailActivity.class);
		
		Topic topic = (Topic)TopicChildAllActivity.this.adapter.getItem(position);
		Log.d(TAG, topic.toString());
		
		intent.putExtra(Topic.TOPIC_TAG, topic);
		intent.putExtra("isreply", TopicChildAllActivity.this.isreply);
		intent.putExtra("ispostimage", TopicChildAllActivity.this.ispostimage);
		
		TopicChildAllActivity.this.startActivity(intent);
	}
	
	private void initPullView(){
		pv = (PullView)this.findViewById(R.id.pv);
        this.pv.setUpdateHandle(this);
        
        datas = new ArrayList<Topic>();
		adapter = new TopicListViewAdapter(TopicChildAllActivity.this);
		setListAdapter(adapter);
	}
	
	
	private void initPagerBar(){
		txt_pager_info = (TextView)this.findViewById(R.id.txt_pager_info);
		
		btn_pager_prev = (ImageButton)this.findViewById(R.id.btn_pager_prev);
		btn_pager_next = (ImageButton)this.findViewById(R.id.btn_pager_next);
		btn_board_favorite = (ImageButton)this.findViewById(R.id.btn_board_favorite);
		Board temp = dao.get(TopicChildAllActivity.this.fid);
		if(temp != null){
			btn_board_favorite.setBackgroundResource(R.drawable.liked);
		}
		
		MyOnClickListener listener = new MyOnClickListener();
		btn_pager_prev.setOnClickListener(listener);
		txt_pager_info.setOnClickListener(listener);
		btn_pager_next.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
	}
	
	private void loadPage(){
		
		if(-1 == SystemHelper.getNetworkType(this)){
			pv.endUpdate(); 
			Toast.makeText(TopicChildAllActivity.this, "网络连接失败，请检查设备!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Log.d(TAG, "loadData...");
		
		RemoteDataHandler.asyncGet(url+fid, pagesize, pageno, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				
				pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
				
				if(data.getCode() == HttpStatus.SC_OK){
					Log.d(TAG, "RemoteDataHanlder---dataLoaded");
					String json = data.getJson();
					
					Log.d(TAG, json);
					
					//还需要设置分页信息...
					count = data.getCount();
					if(count > 0){
						totalpage = ((count + pagesize - 1) / pagesize);
						txt_pager_info.setText(pageno + "/" + totalpage);
					}
					
					datas.clear();
					datas.addAll(Topic.newInstanceList(json));
					Log.d(TAG, datas.toString());
					
					adapter.setDatas(datas);
					adapter.notifyDataSetChanged();
					
					setSelection(0);
				}
			}
		});

	}
	
	private void initTopButton(){
		
		txt_title = (TextView)parent.findViewById(R.id.txt_title);
		txt_title.setText(this.boardName);
		
		btn_right = (ImageButton)parent.findViewById(R.id.btn_right);
		
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){
			btn_right.setBackgroundResource(R.drawable.btn_sendtopic);
		}else{
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
		
		btn_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){//跳转到发帖
					Log.d(TAG, ispost+"");
					
					//QIUJY 发帖权限判断
					HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
					Log.d(TAG, subBoardMap.toString());
					if(subBoardMap != null){
						Board b = subBoardMap.get(Long.valueOf(fid));
						Log.d(TAG, b.toString());
						if(b.getIspost() == 1){
							Intent intent = new Intent(TopicChildAllActivity.this, SendTopicActivity.class);
							intent.putExtra("fid", TopicChildAllActivity.this.fid);
							intent.putExtra("boardName", TopicChildAllActivity.this.boardName);
							intent.putExtra("ispostimage", b.getIspostimage());
							
							TopicChildAllActivity.this.startActivityForResult(intent, 100);
						}else{
							Toast.makeText(TopicChildAllActivity.this, "你没有权限在本版发帖！", Toast.LENGTH_SHORT).show();
						}
					}
				}else{//未登录先跳转到登录
					Toast.makeText(TopicChildAllActivity.this, "发帖请先登录！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(TopicChildAllActivity.this, LoginActivity.class);
					TopicChildAllActivity.this.startActivityForResult(intent, 200);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode){
		case 100:  //发帖成功
        	Log.d(TAG, "post succ, load datas");
        	pageno = 1;
        	pv.startUpdate();
        	break;
        case 200: //登录成功 
        	btn_right.setBackgroundResource(R.drawable.btn_sendtopic);
        	break;
		}
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
					Toast.makeText(TopicChildAllActivity.this, "已经是第1页了!", 0).show();
				}
				break;
			case R.id.txt_pager_info:
				//输入页号
				break;
			case R.id.btn_pager_next:
				if(totalpage > 1 && pageno < totalpage){
					++pageno;
					pv.startUpdate();
				}else{
					Toast.makeText(TopicChildAllActivity.this, "已经是底页了!", 0).show();
				}
				break;
			case R.id.btn_board_favorite:
				//執行本地收藏(to SQLite)
				favorite();
				break;
			}
		}
	}
	
	private void favorite(){
		if(null != myApp.getSid()){
			Board temp = dao.get(this.fid);
			if(temp != null){
				Toast.makeText(TopicChildAllActivity.this, "您已经收藏了!", 0).show();
			}else{
				Board board = new Board();
				board.setFid(this.fid);
				board.setName(this.boardName);
				
				dao.save(board, myApp.getUid());
				
				Toast.makeText(TopicChildAllActivity.this, "收藏成功!", 0).show();
			}
			btn_board_favorite.setBackgroundResource(R.drawable.liked);
		}else{
			Toast.makeText(TopicChildAllActivity.this, "请先登录后再收藏!", 0).show();
		}
	}
}
