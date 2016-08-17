package com.example.final_me;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.special.ResideMenuDemo.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class znuelNewsTitle extends Activity 
{

	ListView title;
	String colurl;
	UseDemo usedemo;
	//ProgressBar progressBar;
	public HashMap<	String, String> Title_to_Link;
	private ArrayList<String> info=new ArrayList<String>();
	private ArrayList<String> date=new ArrayList<String>();
	//int progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		

		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.titles);
		title=(ListView) this.findViewById(R.id.listView1);
		Title_to_Link=new HashMap<String, String>();
		//progressBar=(ProgressBar)findViewById(R.id.progressBar1);
		//progressBar.setVisibility(View.VISIBLE);
		//progress=0;
		final Intent data = getIntent(); 
		colurl=data.getStringExtra("link");
		
		title.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				Intent intent=new Intent(znuelNewsTitle.this,NewsDetail.class);
				intent.putExtra("title", info.get((int)arg3));
				intent.putExtra("linkURL", Title_to_Link.get(info.get((int)arg3)));
				startActivity(intent);
			}
		});
		
		getApplicationContext();
		ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo(); 
		  if (info != null && info.isAvailable()){ 
			  new TitleCatcher().start();	
		  }else{ 
			  Toast.makeText(getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
		  } 
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case 1:
					Log.v("返回值","case111111111111");
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					for(int i=0;i<info.size();i++)
			        {
			        	HashMap<String, Object> map = new HashMap<String, Object>();
			        	map.put("ItemImage", R.drawable.read);
			        	map.put("ItemTitle", cut(info.get(i)));
			        	map.put("ItemText", date.get(i));
			        	listItem.add(map);
			        }
					
			        SimpleAdapter listItemAdapter = new SimpleAdapter(znuelNewsTitle.this,listItem,
			            R.layout.title_row,       
			            new String[] {"ItemImage","ItemTitle", "ItemText"}, 
			            new int[] {R.id.ItemImage,R.id.ItemTitle,R.id.ItemText}
			        );
			        //progressBar.setVisibility(View.GONE);
			        title.setAdapter(listItemAdapter);
					break;
				case 2:
					Log.v("返回值","case2222222222222");
					Toast.makeText(getApplicationContext(), "连接失败。", Toast.LENGTH_LONG).show();
					break;
            }
	 }
	};
	
	public static String cut(String str)
	{
		if(str.length()>=13)
			return str.substring(0,13)+"...";
		else
			return str;
	}
	
	
	public class TitleCatcher extends Thread
	{
		public  int pages=1;
		
		public void run()
		{
			try {
				//String addString="http://wellan.znufe.edu.cn/ttxw/2.html";
				String addString=colurl;
				if(pages>1)
				{
					addString=colurl+"_"+pages+".html";
				}
				URL url = new URL(addString);
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.setConnectTimeout(60000);
	            urlConnection.connect();
	            if(urlConnection.getResponseCode() == 200)
	            {
		            /*InputStreamReader inputStreamReader = null;
		            inputStreamReader= new InputStreamReader(urlConnection.getInputStream(),"GBK");
		            BufferedReader bufReader = new BufferedReader(inputStreamReader);
		            StringBuffer stringBuffer = new StringBuffer();
	
		            String inputLine = null;
		            while((inputLine= bufReader.readLine()) != null)
		            {
		            	stringBuffer.append(inputLine);
		            }
					
					String strAll=stringBuffer.toString();
					String startString="</td>";
					String endString="</a>";
					//String link="";
					int start=0,end=0;
					int n=30,begin=84;
					while(start>=0 && n-->0)
					{
						if(n!=29) begin=210;
						start = strAll.indexOf(startString,end) ;
						end = strAll.indexOf(endString,start) ;
						String result = strAll.substring(start+begin,end);
						int temp=strAll.indexOf(result,start);
						String link=strAll.substring(temp-36,temp-18) ;
						String dateString=strAll.substring(temp+result.length()+54,temp+result.length()+67);
						info.add(result);
						date.add(dateString);
						Title_to_Link.put(result, link);	
					}*/
	            	String content = usedemo.getHTML(addString, "gbk");//得到html的代码		
	        		String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
	        		//System.out.println(initPassage);
	        		List<String > titleList = usedemo.dealToEachTitle(usedemo.getPassage(initPassage), ")");
	        		
	        		
	        		List<String>linkList1= usedemo.divideToTitle( titleList);//获取纯文本标题内容，为了给后面的那个抓链接用的
	        		//for(int i=1;i<linkList1.size();i++){
	        		  //  System.out.println(i+linkList1.get(i));	}
	        		
	        		
	        		//都从1开始哈，0开始的那条怎么都是不对的
	        		//for(int i =0;i<titleList.size();i++){
	        		//System.out.println(i+titleList.get(i));	}
	        		//System.out.println("++++++++++++++++++++++++++++++++++++++");
	        		List<String>linkList=usedemo.dealToEachLink(linkList1,content);
	        		for(int i=0;i<linkList.size();i++){
	        		    System.out.println(i+linkList.get(i));
	        		    
	        		    for(i=0;i<linkList.size();i++){
	        		    	String link=linkList.get(i);System.out.println(link);
	        		    	String result = titleList.get(i);
							String dateString="";
							info.add(result);
							date.add(dateString);
							Title_to_Link.put(result, link);	
	        		    	}    
	        		    }
	        		System.out.println("=====================================================");
	            	
					Message msg= new Message();
					msg.what=1;
					mHandler.sendMessage(msg);	
	            }
	            
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.titles, menu);
		return true;
	}*/

}
