/**
 *  ClassName: TopicListActivity.java
 *  created on 2012-3-4
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.board;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.model.Board;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * 指定版块下的帖子列表Tab界面
 * @author qjyong
 */
public class TopicListActivity extends TabActivity {
	private ImageButton btn_left;
	
	/** tab标签名*/
	public final static String TAB_TAG_ALL = "topic_all";
	public final static String TAB_TAG_TOP = "topic_top";
	public final static String TAB_TAG_DIGEST = "topic_digest";
	//public final static String TAB_TAG_SEARCH = "topic_search";
	public final static String TAB_TAG_SON = "topic_son";
	
	private static TabHost tabHost;
	private RadioButton btn_topic_all;
	private RadioButton btn_topic_top;
	private RadioButton btn_topic_digest;
	//private RadioButton btn_topic_search;
	private RadioButton btn_topic_son;
	
	private Intent topic_all_intent;
	private Intent topic_top_intent;
	private Intent topic_digest_intent;
	//private Intent topic_search_intent;
	private Intent topic_son_intent;
	
	
	private Board board=new Board();
	private MyApp myApp;
	
	private ArrayList<Board> son_board=new ArrayList<Board>();
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		this.setContentView(R.layout.tab_topic_list);
		
		 myApp = (MyApp)this.getApplication();
		
		Bundle localBundle = getIntent().getExtras();
	    String boardName = localBundle.getString("boardName");
	    long fid = localBundle.getLong("fid");
	    int ispost = localBundle.getInt("ispost");
	    int isreply = localBundle.getInt("isreply");
	    int ispostimage = localBundle.getInt("ispostimage");
	    long fup = localBundle.getLong("fup");
	    String type=localBundle.getString("type");
	    
		topic_all_intent = new Intent(this, TopicAllActivity.class);
		topic_all_intent.putExtra("url", Constants.URL_BOARD_TOPIC_LIST);
		topic_all_intent.putExtra("fid", fid);
		topic_all_intent.putExtra("boardName", boardName);
		topic_all_intent.putExtra("ispost", ispost);
		topic_all_intent.putExtra("isreply", isreply);
		topic_all_intent.putExtra("ispostimage", ispostimage);
		topic_all_intent.putExtra("fup", fup);
		topic_all_intent.putExtra("type", type);
		
		
		topic_top_intent = new Intent(this, TopicTopActivity.class);
		topic_top_intent.putExtra("fid", fid);
		topic_top_intent.putExtra("boardName", boardName);
		topic_top_intent.putExtra("url", Constants.URL_BOARD_TOPIC_TOP);
		topic_top_intent.putExtra("ispost", ispost);
		topic_top_intent.putExtra("isreply", isreply);
		topic_top_intent.putExtra("ispostimage", ispostimage);
		
		topic_digest_intent = new Intent(this, TopicDigestActivity.class);
		topic_digest_intent.putExtra("fid", fid);
		topic_digest_intent.putExtra("boardName", boardName);
		topic_digest_intent.putExtra("url", Constants.URL_BOARD_TOPIC_DIGEST);
		topic_digest_intent.putExtra("ispost", ispost);
		topic_digest_intent.putExtra("isreply", isreply);
		topic_digest_intent.putExtra("ispostimage", ispostimage);
//		
//		topic_search_intent = new Intent(this, TopicSearchActivity.class);
//		topic_search_intent.putExtra("fid", fid);
//		topic_search_intent.putExtra("boardName", boardName);
//		topic_search_intent.putExtra("url", Constants.URL_BOARD_TOPIC_SEARCH);
//		topic_search_intent.putExtra("ispost", ispost);
//		topic_search_intent.putExtra("isreply", isreply);
//		topic_search_intent.putExtra("ispostimage", ispostimage);
		
		topic_son_intent = new Intent(this, ChildBoardActivity.class);
		topic_son_intent.putExtra("fid", fid);
		topic_son_intent.putExtra("boardName", boardName);
		topic_son_intent.putExtra("url", Constants.URL_BOARD_TOPIC_TOP);
		topic_son_intent.putExtra("ispost", ispost);
		topic_son_intent.putExtra("isreply", isreply);
		topic_son_intent.putExtra("ispostimage", ispostimage);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_ALL).setIndicator(TAB_TAG_ALL).setContent(topic_all_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_TOP).setIndicator(TAB_TAG_TOP).setContent(topic_top_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_DIGEST).setIndicator(TAB_TAG_DIGEST).setContent(topic_digest_intent));
		//tabHost.addTab(tabHost.newTabSpec(TAB_TAG_SEARCH).setIndicator(TAB_TAG_SEARCH).setContent(topic_search_intent));
	
		////////////////////// find View ////////////////////////////
		btn_topic_all = (RadioButton)this.findViewById(R.id.btn_topic_all);
		btn_topic_top = (RadioButton)this.findViewById(R.id.btn_topic_top);
		btn_topic_digest = (RadioButton)this.findViewById(R.id.btn_topic_digest);
		//btn_topic_search = (RadioButton)this.findViewById(R.id.btn_topic_search);
		btn_topic_son=(RadioButton)this.findViewById(R.id.btn_topic_son);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_topic_all.setOnClickListener(listener);
		btn_topic_top.setOnClickListener(listener);
		btn_topic_digest.setOnClickListener(listener);
		//btn_topic_search.setOnClickListener(listener);
		btn_topic_son.setOnClickListener(listener);
		
		/*** 子版块**/
	    ArrayList<Board> boards=myApp.getBoards();
	    for(int i=0;i<boards.size();i++){
	    	board=boards.get(i);
	    	if(board.getType().equals("sub") && fid==board.getFup()){
	    		son_board.add(board);
		    }else{
		    	continue;
		    }
	    }
	    if(son_board!=null && son_board.size()!=0){
	    	btn_topic_son.setVisibility(View.VISIBLE);
			tabHost.addTab(tabHost.newTabSpec(TAB_TAG_SON)
	    			.setIndicator(TAB_TAG_SON).setContent(topic_son_intent));
			myApp.setBoards(son_board);
	    }
		
		
		ImageView iv_divider = (ImageView)this.findViewById(R.id.img_divider);
		iv_divider.setVisibility(View.GONE);
		
		/**设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText("版块的标题");
		*/
		/**
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setBackgroundResource(R.drawable.btn_exit_normal);
		*/
		
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TopicListActivity.this.finish();
			}
		});
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_topic_all:
				tabHost.setCurrentTabByTag(TAB_TAG_ALL);
				break;
			case R.id.btn_topic_top:
				tabHost.setCurrentTabByTag(TAB_TAG_TOP);
				break;
			case R.id.btn_topic_digest:
				tabHost.setCurrentTabByTag(TAB_TAG_DIGEST);
				break;
//			case R.id.btn_topic_search:
//				tabHost.setCurrentTabByTag(TAB_TAG_SEARCH);
//				break;
			case R.id.btn_topic_son:
				tabHost.setCurrentTabByTag(TAB_TAG_SON);
				break;
			}
		}
	}
}

