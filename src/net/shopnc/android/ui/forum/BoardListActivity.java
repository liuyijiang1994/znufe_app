/**
 *  ClassName: BoardListActivity.java
 *  created on 2012-3-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.MyExpandableListAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.forum.board.TopicListActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 版块列表
 * @author qjyong
 */
public class BoardListActivity extends Activity implements UpdateHandle{
	public static final String TAG = "BoardListActivity";
	
	private MyApp myApp;
	private ImageButton btn_left;
	
	protected ForumActivity myParent;
	
	private PullView pv;
	private ExpandableListView lv_boards;
	private MyExpandableListAdapter adapter;
	
	private int pageno = 1;
	private int pagesize;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.board_list);
		myApp = (MyApp)this.getApplication();
		pagesize = myApp.getPageSize();
		myParent = (ForumActivity)this.getParent();
		
		initTitleBar();
		
		pv = (PullView)this.findViewById(R.id.pv);
        this.pv.setUpdateHandle(this);
		
		initExpandableListView();
		
		pv.startUpdate();
		
		loadData();
	}
	
	public void onUpdate() {
		loadData();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_EXITAPP_ID);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void initExpandableListView(){
		lv_boards = (ExpandableListView)this.findViewById(R.id.lv_boards);
		lv_boards.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < BoardListActivity.this.adapter.getGroupCount(); i++){
		          if (groupPosition != i){
		        	  BoardListActivity.this.lv_boards.collapseGroup(i);
		          }
		        }
			}
		});
		lv_boards.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//启动“本版块下的帖子列表”界面
				Intent localIntent = new Intent(BoardListActivity.this, TopicListActivity.class);
				
				Board temp = (Board)BoardListActivity.this.adapter.getChild(groupPosition, childPosition);
			    localIntent.putExtra("boardName", temp.getName());
			    localIntent.putExtra("fid", temp.getFid());
			    localIntent.putExtra("ispost", temp.getIspost());
			    localIntent.putExtra("isreply", temp.getIsreply());
			    localIntent.putExtra("ispostimage", temp.getIspostimage());
			    localIntent.putExtra("fup", temp.getFup());
			    localIntent.putExtra("type", temp.getType());
			    
			    
			    BoardListActivity.this.startActivity(localIntent);
				
				return true;
			}
		});
		
		
	}
	
	
	private void initTitleBar(){
		//刷新
		btn_left = (ImageButton)myParent.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				pv.startUpdate();
				//loadData();
			}
		});
		/*退出
		btn_right = (ImageButton)myParent.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				myParent.showDialog(HomeActivity.DIALOG_EXITAPP_ID);
			}
		});
		*/
	}
	
	private void loadData(){
		//((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_LOADDATA_ID);
		
		String url = Constants.URL_BOARD;
		if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
				&& null != myApp.getSid() && !"".equals(myApp.getSid())){//登录用户
			url += myApp.getUid();
		}

		RemoteDataHandler.asyncGet(url, pagesize, pageno, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				pv.endUpdate();
				//((MainActivity)myParent.getParent()).dismissDialog(Constants.DIALOG_LOADDATA_ID);
				if(data.getCode() == HttpStatus.SC_OK){
					String json = data.getJson();
					ArrayList<Board> group = Board.newBoardList(json);
					/*
					 * 加载全部数据
					 * */
					ArrayList<Board> group2 = Board.new_SB_list(json);
					
					myApp.setBoards(group2);
					
					adapter = new MyExpandableListAdapter(BoardListActivity.this, group);
					lv_boards.setAdapter(adapter);
				}else{
					Toast.makeText(BoardListActivity.this, "网络故障！", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
}
