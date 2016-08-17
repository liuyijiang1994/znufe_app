package com.example.final_me;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.special.ResideMenuDemo.R;

import android.annotation.SuppressLint;
//import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ZoomControls;

@SuppressLint("HandlerLeak")
public class NewsDetail extends Activity {
	//private ActionBar actionBar;
	final String BASE="http://wellan.znufe.edu.cn";
	String result="";
	String title="";
	String newsinfo="";
	WebView mWebView;
	private ZoomControls zoomc=null;
	int screenWidth;
	final Activity MyActivity = this;  
	int fontSize=1;
	private Button btn_back;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.news_detail2);
		
		// Makes Progress bar Visible  
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);  
		
		
	       // Sets the Chrome Client, and defines the onProgressChanged  
	       // This makes the Progress bar be updated.    
		btn_back=(Button)this.findViewById(R.id.title_bar_back);//这里是返回键，back，英语越来越差啦，受不鸟
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String tttt=getIntent().getStringExtra("title");
		
		System.out.println("00000000000000000000000000000000000000000");
		System.out.println(tttt);
		
		//tttt=tttt.substring(0,tttt.length()-13);
		title=tttt;
		zoomc=(ZoomControls)this.findViewById(R.id.zoomControls1);
		mWebView=(WebView)this.findViewById(R.id.webView1);
		WebSettings mWebSettings=mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		new ContentCatcher().start();
		DisplayMetrics  dm = new DisplayMetrics();  
		
		//*************************************修改webview的字体大小
		mWebSettings.setSupportZoom(true);
		mWebSettings.setTextSize(WebSettings.TextSize.NORMAL);
	      //取得窗口属性  
	      getWindowManager().getDefaultDisplay().getMetrics(dm);  
	       
	      //窗口的宽度  
	      screenWidth = dm.widthPixels;  
	      
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

	
	class ContentCatcher extends Thread
	{
		public void run()
		{
			try {
				URL url = new URL(getIntent().getStringExtra("linkURL"));
				InputStreamReader isInputStreamReader=new InputStreamReader(url.openStream(),"GBK");
				BufferedReader brBufferedReader=new BufferedReader(isInputStreamReader);
				
				String inputlineString="";
				StringBuffer strBuf = new StringBuffer() ;
				
				while ((inputlineString = brBufferedReader.readLine()) != null)
				{
					strBuf.append(inputlineString);
				}
				
				brBufferedReader.close();
				
				String strAll=strBuf.toString();
				String startString="<div id=\"ArticleCnt\">";
				String endString="</table></div>";
				int start=0,end=0;
				start = strAll.indexOf(startString,end);
				end = strAll.indexOf(endString,start);
				result="";
				result = strAll.substring(start+21,end);
				result=result.replaceAll("#", "%23");
				result= result.replaceAll("%", "%25");
				result= result.replaceAll("\\\\", "%27");
		        result = result.replaceAll("\\?", "%3f");
		        result = result.replaceAll("<P>&nbsp;</P><P>&nbsp;</P>", "<P>&nbsp;</P>");
		        result = result.replaceAll("<P>&nbsp;</P><P>&nbsp;</P>", "<P>&nbsp;</P>");
		        result=result.replaceAll("<?xml:namespace prefix = (.*?)/>", "");
		        result=result.replaceAll("<%3f", "");
		        result=result.replaceAll("<a(.*?)>(.*?)</a>", "");
		        result=result.replaceAll("<A(.*?)>(.*?)</A>", "");

				
		        System.out.println(result);
		        
		        start=0;end=0;
		        startString="来源：<span";
				endString="</span>";
				start = strAll.indexOf(startString,end);
				end = strAll.indexOf(endString,start);
				newsinfo+=strAll.substring(start+27,end)+"   ";
				
				startString="发布时间：<span";
				endString="</span>";
				start = strAll.indexOf(startString,end);
				end = strAll.indexOf(endString,start);
				newsinfo+=strAll.substring(start+29,end)+"   ";

				Message msg= new Message();
				msg.what=1;
				mHandler.sendMessage(msg);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case 1:
				mWebView.getSettings().setDefaultTextEncodingName("UTF-8") ;
				result="<p style=\"font-weight:bold; font-size:26px\">"+title+"</p><p style=\" font-size:22px\">"+newsinfo+"</p>"+result;
				mWebView.loadDataWithBaseURL(BASE, result, "text/html", "utf-8", "");
			
					break;
            }
	 }
	};

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

}
