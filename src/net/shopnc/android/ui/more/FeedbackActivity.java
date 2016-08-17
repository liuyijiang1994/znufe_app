/**
 *  ClassName: FeedbackActivity.java
 *  created on 2012-2-24
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import org.apache.http.HttpStatus;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 意见反馈
 * @author qjyong
 */
public class FeedbackActivity extends Activity {
	private MyApp myApp;
	
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	
	private EditText txt_content;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_feedback);
		myApp = (MyApp)this.getApplication();
		
		initTitleBar();
		
		txt_content = (EditText)this.findViewById(R.id.more_feedback_content);
	}
	private void initTitleBar(){
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.more_feedback));
		
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.this.finish();
			}
		});
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				String content = txt_content.getText().toString();
				if(null == content || "".equals(content)){
					Toast.makeText(FeedbackActivity.this, "请填写内容！", Toast.LENGTH_SHORT).show();
					return ;
				}
				RemoteDataHandler.feedback(content, myApp.getUid(), myApp.getUseracc(), new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						if(data.getCode()==HttpStatus.SC_OK){
							Toast.makeText(FeedbackActivity.this, "提交成功，谢谢您的宝贵意见！", Toast.LENGTH_SHORT).show();
							FeedbackActivity.this.finish();
						}else{
							Toast.makeText(FeedbackActivity.this, "网络链接错误！请重试！", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
