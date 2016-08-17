/**
 *  ClassName: TopicDetailActivity.java
 *  created on 2012-3-7
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.topic;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.model.Topic;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * 帖子详细的Tab界面
 * @author qjyong
 */
public class TopicDetailActivity extends TabActivity {
	public final static String TAG = "TopicDetailActivity";
	private ImageButton btn_left;
	
	/** tab标签名*/
	public final static String TAB_TAG_DEFAULT = "topic_display_default";
	public final static String TAB_TAG_ONLYLANDORD = "topic_display_onlylandord";
	public final static String TAB_TAG_LASTREPLIES = "topic_display_lastreplies";
	
	private static TabHost tabHost;
	private RadioButton btn_topic_display_default;
	private RadioButton btn_topic_display_onlylandlord;
	private RadioButton btn_topic_display_lastreplies;
	
	private Intent topic_default_intent;
	private Intent topic_onlylandlord_intent;
	private Intent topic_lastreplies_intent;
	
	
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		this.setContentView(R.layout.tab_topic);
		
		Topic t = (Topic)this.getIntent().getExtras().get(Topic.TOPIC_TAG);
		int isreply = this.getIntent().getIntExtra("isreply", 0);
		int ispostimage = this.getIntent().getIntExtra("ispostimage", 0);
		Log.d(TAG, t.toString());
		
		topic_default_intent = new Intent(this, TopicHtmlActivity.class);
		topic_default_intent.putExtra(Topic.TOPIC_TAG, t);
		topic_default_intent.putExtra("url", Constants.URL_TOPIC_DETAIL_DEFAULT);
		topic_default_intent.putExtra("isreply", isreply);
		topic_default_intent.putExtra("ispostimage", ispostimage);
		
		topic_onlylandlord_intent = new Intent(this, TopicDetailOnlyLandlordActivity.class);
		topic_onlylandlord_intent.putExtra(Topic.TOPIC_TAG, t);
		topic_onlylandlord_intent.putExtra("url", Constants.URL_TOPIC_DETAIL_LANDLOAD);
		topic_onlylandlord_intent.putExtra("isreply", isreply);
		topic_onlylandlord_intent.putExtra("ispostimage", ispostimage);
		
		topic_lastreplies_intent = new Intent(this, TopicDetailLastRepliesHtmlActivity.class);
		topic_lastreplies_intent.putExtra(Topic.TOPIC_TAG, t);
		topic_lastreplies_intent.putExtra("url", Constants.URL_TOPIC_DETAIL_REPLY);
		topic_lastreplies_intent.putExtra("isreply", isreply);
		topic_lastreplies_intent.putExtra("ispostimage", ispostimage);
		
		tabHost = this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_DEFAULT).setIndicator(TAB_TAG_DEFAULT).setContent(topic_default_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_ONLYLANDORD).setIndicator(TAB_TAG_ONLYLANDORD).setContent(topic_onlylandlord_intent));
		tabHost.addTab(tabHost.newTabSpec(TAB_TAG_LASTREPLIES).setIndicator(TAB_TAG_LASTREPLIES).setContent(topic_lastreplies_intent));
	
		////////////////////// find View ////////////////////////////
		btn_topic_display_default = (RadioButton)this.findViewById(R.id.btn_topic_display_default);
		btn_topic_display_onlylandlord = (RadioButton)this.findViewById(R.id.btn_topic_display_onlylandlord);
		btn_topic_display_lastreplies = (RadioButton)this.findViewById(R.id.btn_topic_display_lastestreplies);
		
		MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
		btn_topic_display_default.setOnClickListener(listener);
		btn_topic_display_onlylandlord.setOnClickListener(listener);
		btn_topic_display_lastreplies.setOnClickListener(listener);
		
		
		ImageView iv_divider = (ImageView)this.findViewById(R.id.img_divider);
		iv_divider.setVisibility(View.GONE);
		
		//设置标题
		//txt_title = (TextView)this.findViewById(R.id.txt_title);
		//txt_title.setText("帖子的标题");
		
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TopicDetailActivity.this.finish();
			}
		});
		
		//btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		//btn_right.setBackgroundResource(R.drawable.btn_exit_normal);
		
	}
	
	class MyRadioButtonClickListener implements View.OnClickListener{
		public void onClick(View v) {
			RadioButton btn = (RadioButton)v;
			switch(btn.getId()){
			case R.id.btn_topic_display_default:
				tabHost.setCurrentTabByTag(TAB_TAG_DEFAULT);
				break;
			case R.id.btn_topic_display_onlylandlord:
				tabHost.setCurrentTabByTag(TAB_TAG_ONLYLANDORD);
				break;
			case R.id.btn_topic_display_lastestreplies:
				tabHost.setCurrentTabByTag(TAB_TAG_LASTREPLIES);
				break;
			}
		}
	}
}
