package com.example.final_me;

import javax.crypto.spec.IvParameterSpec;

import com.example.xnfsh.db.entity.News;
import com.special.ResideMenuDemo.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

@SuppressLint("HandlerLeak")
public class News_db_detail extends Activity {
	final String BASE="http://wellan.znufe.edu.cn";
	String result="";
	String title="";
	String newsinfo="";
	WebView mWebView;
	int screenWidth;
	final Activity MyActivity = this;  
	int fontSize=1;
	private ZoomControls zoomc=null;
	private int W;
	private int H;
	News news;
	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.news_detail);
		
		Display mDisplay = this.getWindowManager().getDefaultDisplay();  
		W = mDisplay.getWidth();  
		H = mDisplay.getHeight();
		// Makes Progress bar Visible  
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);  
		btn_back=(Button)this.findViewById(R.id.title_bar_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();
			}
		});
		
		
		zoomc=(ZoomControls)this.findViewById(R.id.zoomControls1);
		
		news=(News) getIntent().getExtras().getSerializable("newsentity");

		mWebView=(WebView)this.findViewById(R.id.webView1);
		WebSettings mWebSettings=mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		DisplayMetrics  dm = new DisplayMetrics();  
		
		//*************************************修改webview的字体大小
		mWebSettings.setSupportZoom(true);
		mWebSettings.setTextSize(WebSettings.TextSize.NORMAL);
		fontSize=3;
	      //取得窗口属性  
	      getWindowManager().getDefaultDisplay().getMetrics(dm);  
	       
	      //窗口的宽度  
	      screenWidth = dm.widthPixels;  
	      initview();
	      mWebView.setWebChromeClient(new WebChromeClient() {  
		        public void onProgressChanged(WebView view, int progress)     
		        {  
		         //Make the bar disappear after URL is loaded, and changes string to Loading...  
		         MyActivity.setTitle("掌上中南大");  
		         MyActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded  
		         
		         // Return the app name after finish loading  
		            if(progress == 100)  
		               MyActivity.setTitle(R.string.app_name);  
		          }  
		        });  
	      setImageViewClick(); 
	}

	 private void setImageViewClick() {

	        /**
	         * 缩小按钮
	         */
		 
		 	zoomc.setOnZoomOutClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {

	                fontSize--;
	             
	                if (fontSize < 0) {
	                    fontSize = 1;
	                }
	                switch (fontSize) {

	                case 1:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
	                    break;
	                case 2:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
	                    break;
	                case 3:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
	                    break;
	                case 4:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
	                    break;
	                case 5:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
	                    break;
	                }

	            }
	        });
	   
	        /**
	         * 放大按钮
	         */
	        zoomc.setOnZoomInClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {

	                fontSize++;
	             
	                if (fontSize > 5) {
	                    fontSize = 5;
	                }
	                switch (fontSize) {

	                case 1:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
	                	
	                    break;
	                case 2:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
	                	
	                    break;
	                case 3:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
	                	
	                    break;
	                case 4:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
	                	
	                    break;
	                case 5:
	                	mWebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
	                	
	                    break;
	                }
	            }
	        });
	        
	        
	final String mimeType = "text/html";
	final String encoding = "UTF-8";
	mWebView.loadDataWithBaseURL(BASE, result, "text/html", "utf-8", "");
	/*content = getContentFromNewWork(newsId);
	mWebView.loadDataWithBaseURL("", content, mimeType, encoding, "");*/
}

	
	public void initview()
	{
		newsinfo=news.getDate();
		title=news.getTitle();	
		result=news.getText();
	
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8") ;
		result="<p style=\"font-weight:bold; font-size:26px\">"+title+"</p><p style=\" font-size:22px\">"+newsinfo+"</p>"+result;

		mWebView.loadDataWithBaseURL(BASE, result, "text/html", "utf-8", "");
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);  
	}
	
}
