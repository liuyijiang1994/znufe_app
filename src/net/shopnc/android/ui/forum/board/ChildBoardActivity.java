/**
 *  ClassName: BaseActivity.java
 *  created on 2012-2-26
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.board;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.SonListViewAdapter;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.model.Board;
import net.shopnc.android.ui.forum.topic.SendTopicActivity;
import net.shopnc.android.ui.more.LoginActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;
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
 * @author hjgang
 * 显示子版块列表
 */
@SuppressWarnings("unused")
public class ChildBoardActivity extends ListActivity implements UpdateHandle{
	public static final String TAG = "Son";

	private MyApp myApp;
	private PullView pv;
	private TopicListActivity parent;
	private SonListViewAdapter adapter;
	private ArrayList<Board> datas;
	private ImageButton btn_right;
	private TextView txt_title;
	
	private ImageButton btn_pager_prev;
	private ImageButton btn_pager_next;
	private TextView txt_pager_info;
	private ImageButton btn_board_favorite;
	
	
	private int pagesize;
	private int pageno = 1;
	private long count = 0;
	private long totalpage= 0;
		
	private String boardName;
	private long fid;
	private long fup;
	private int ispost;
	private int isreply;
	private int ispostimage;
	private String type;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		pagesize = myApp.getPageSize();
		parent = (TopicListActivity)this.getParent();
		this.setContentView(R.layout.forum_main);
		
		Bundle localBundle = getIntent().getExtras();
	     this.boardName = localBundle.getString("boardName");
	     this.fid = localBundle.getLong("fid");
	     this.ispost = localBundle.getInt("ispost");
	     this.isreply = localBundle.getInt("isreply");
	     this.ispostimage = localBundle.getInt("ispostimage");
	     this.fup = localBundle.getLong("fup");
	     this.type=localBundle.getString("type");
	     this.url = localBundle.getString("url");
	    
		
	     initTopButton();
	     initPagerBar();
	     
	     initPullView();
		
		datas = new ArrayList<Board>();
		adapter = new SonListViewAdapter(ChildBoardActivity.this);
		setListAdapter(adapter);
		
		loadPage(1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		onUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(1);
	}
	
	private void initPagerBar(){
		txt_pager_info = (TextView)this.findViewById(R.id.txt_pager_info);
		
		btn_pager_prev = (ImageButton)this.findViewById(R.id.btn_pager_prev);
		btn_pager_next = (ImageButton)this.findViewById(R.id.btn_pager_next);
		btn_board_favorite = (ImageButton)this.findViewById(R.id.btn_board_favorite);
		btn_board_favorite.setVisibility(View.INVISIBLE);
		
		MyOnClickListener listener = new MyOnClickListener();
		btn_pager_prev.setOnClickListener(listener);
		txt_pager_info.setOnClickListener(listener);
		btn_pager_next.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
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
					Intent intent = new Intent(ChildBoardActivity.this, SendTopicActivity.class);
					intent.putExtra("fid", ChildBoardActivity.this.fid);
					intent.putExtra("boardName", ChildBoardActivity.this.boardName);
					ChildBoardActivity.this.startActivityForResult(intent, 100);
				}else{//未登录先跳转到登录
					//Toast.makeText(Son.this, "发帖请先登录！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ChildBoardActivity.this, LoginActivity.class);
					ChildBoardActivity.this.startActivityForResult(intent, 200);
				}
			}
		});
	}
	
	
	private void initPullView(){
		pv = (PullView)this.findViewById(R.id.pv2);
        this.pv.setUpdateHandle(this);
	}
	
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//查看帖子详细信息
		Intent localIntent = new Intent(ChildBoardActivity.this, TopicChildListActivity.class);
		Board temp = (Board)adapter.getItem(position);
	    localIntent.putExtra("boardName", temp.getName());
	    localIntent.putExtra("fid", temp.getFid());
	    ChildBoardActivity.this.startActivity(localIntent);
	}
	
private void loadPage(int pn){
		
		if(-1 == SystemHelper.getNetworkType(this)){
			pv.endUpdate(); 
			Toast.makeText(ChildBoardActivity.this, "网络连接失败，请检查设备!", Toast.LENGTH_SHORT).show();
			return;
		}
		if(pageno == 1){
			datas.clear();
		}
					pv.endUpdate(); 
					ArrayList<Board> board_arr =myApp.getBoards();
					//设置分页信息...
					count = board_arr.size();
					if(count > 0){
						totalpage = ((count + pagesize - 1) / pagesize);
						txt_pager_info.setText(pageno + "/" + totalpage);
					}
					
					if(board_arr!=null && board_arr.size()!=0){
						
						datas.clear();
						datas.addAll(board_arr);
						Log.d(TAG, datas.toString());
						adapter.setDatas(datas);
						adapter.notifyDataSetChanged();
						ChildBoardActivity.this.setSelection(0);
						
					}else{
						Toast.makeText(this,"加在失败，请刷新！",Toast.LENGTH_SHORT).show();
					}
				}
class MyOnClickListener implements View.OnClickListener{
	@Override
	public void onClick(View v) {
		Log.d(TAG, ""+v.getId());
		switch(v.getId()){
		case R.id.btn_pager_prev:
			if(totalpage > 1 && pageno > 1){
				pageno -= 1;
				loadPage(--pageno);
			}else{
				Toast.makeText(ChildBoardActivity.this, "已经是第1页了!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.txt_pager_info:
			//TODO 输入页号
			break;
		case R.id.btn_pager_next:
			if(totalpage > 1 && pageno < totalpage){
				pageno += 1;
				loadPage(++pageno);
			}else{
				Toast.makeText(ChildBoardActivity.this, "已经是底页了!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_board_favorite:
			break;
		}
	}

}
}
