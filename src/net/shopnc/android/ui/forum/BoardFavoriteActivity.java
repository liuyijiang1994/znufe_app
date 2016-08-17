/**
 *  ClassName: BoardFavoriteActivity.java
 *  created on 2012-3-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.BoardFavoriteListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.dao.FavoriteDao;
import net.shopnc.android.model.Board;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.forum.board.TopicListActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 版块收藏
 * @author qjyong
 */
public class BoardFavoriteActivity extends ListActivity implements UpdateHandle {
	public static final String TAG = "BoardFavoriteActivity";

	private MyApp myApp;
	private Activity myParent;
	
	private ImageButton btn_left;

	private PullView pv;
	/*
	private LinearLayout moreLayout;
	private Button moreBtn;
	private String txt_more_default;
	private String txt_more_wait;
	private int pageno = 1;
	private int pagesize;
	*/
	private BoardFavoriteListViewAdapter adapter;
	private ArrayList<Board> datas;
		
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		myParent = this.getParent();
		
		this.setContentView(R.layout.home_sub_banner);
		
		initTitleBar();
		
		initPullView();
        
		datas = new ArrayList<Board>();
		adapter = new BoardFavoriteListViewAdapter(BoardFavoriteActivity.this);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		onUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
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
        
        /*
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
		});*/
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//查看帖子详细信息
		Intent localIntent = new Intent(BoardFavoriteActivity.this, TopicListActivity.class);
		Board temp = (Board)adapter.getItem(position);
	    localIntent.putExtra("boardName", temp.getName());
	    localIntent.putExtra("fid", temp.getFid());
	    
	    BoardFavoriteActivity.this.startActivity(localIntent);
	}
	
	private void loadData(){
		myApp.getUseracc();
		myApp.getUserpw();
		String uid = myApp.getUid();
		pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
		if(null != myApp.getSid()){
			FavoriteDao dao = new FavoriteDao(this);
			ArrayList<Board> boards = dao.findByOwner(uid);
			
			datas.clear();
			datas.addAll(boards);
			adapter.setDatas(datas);
			adapter.notifyDataSetChanged();
			if(datas == null || datas.size() == 0){
				Toast.makeText(this, "您还没有收藏!\n提示：在版块界面点击心图可收藏该版块", 1).show();
			}
		}else{
			Toast.makeText(this, "请登录后再查看您的收藏!\n提示：在版块界面点击心图可收藏该版块", 1).show();
		}
	}

}
