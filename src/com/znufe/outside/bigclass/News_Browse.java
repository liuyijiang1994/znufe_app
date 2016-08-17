package com.znufe.outside.bigclass;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.final_me.NewsDetail;
import com.example.final_me.TextExtract;
import com.example.final_me.UseDemo;
import com.special.ResideMenuDemo.PullDownView;
import com.special.ResideMenuDemo.PullDownView.OnPullDownListener;
import com.special.ResideMenuDemo.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class News_Browse extends Activity implements OnPullDownListener, OnItemClickListener{

	private static final String COUNT_ADD="http://202.114.234.122:8234/tecApp/news_count.php";

	String colurl;
	UseDemo usedemo;
	int currentPage=1;
	int visited=0;

	//ProgressBar progressBar;
	public HashMap<	String, String> Title_to_Link;
	private ArrayList<String> info=new ArrayList<String>();
	private ArrayList<String> date=new ArrayList<String>();
	List<String> resultList=new ArrayList<String>();


	private static final int WHAT_DID_LOAD_DATA = 0;//数据加载完毕
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private String magazine_name;
	private ListView mListView;
	private MyAdapter mAdapter;
	private Button btn_back;
	private Button btn_back1;
	private Button btn_back2;
	private PullDownView mPullDownView;
	private List<String> mData = new ArrayList<String>();
	private List<String> mDates = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.news_browser);

		//接收从TinyClass传来的值，依此来判断进入了哪一个新闻专刊
		magazine_name=getIntent().getStringExtra("magazine");
		setContentView(R.layout.pulldown_news);

		btn_back=(Button)this.findViewById(R.id.title_bar_back);
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Title_to_Link=new HashMap<String, String>();
		final Intent data = getIntent(); 
		colurl=data.getStringExtra("link");
		String bkname=data.getStringExtra("bkname");

		if(isTimeCorrect()&&(bkname.compareTo("wlxw")==0))//9.3之前
		{
			setContentView(R.layout.with_pic);
			btn_back1=(Button)this.findViewById(R.id.title_bar_back);
			btn_back1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		else
		{
			setContentView(R.layout.pulldown_news);
			btn_back2=(Button)this.findViewById(R.id.title_bar_back);
			btn_back2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		new Count_add(bkname).start();

		/*
		 * 1.使用PullDownView
		 * 2.设置OnPullDownListener
		 * 3.从mPullDownView里面获取ListView
		 */
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setDividerHeight(2);
		mListView.setFooterDividersEnabled(true);
		mListView.setDivider(new ColorDrawable(R.color.base_black));
		mListView.setOnItemClickListener(this);
		//mAdapter = new ArrayAdapter<String>(this, R.layout.pulldown_item, mData);
		mAdapter=new MyAdapter(News_Browse.this);
		mListView.setAdapter(mAdapter);

		mPullDownView.enableAutoFetchMore(false, 1);

		getApplicationContext();
		ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){ 
			if(visited==0)
			{
				loadData();
				System.out.println("aaaaaaaaaaaaa");
				visited=1;
			}
		}else{ 
			Toast.makeText(getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
		} 
	}

	class Count_add extends Thread
	{
		private String bkname;

		public Count_add(String name)
		{
			this.bkname=name;
		}

		public void run()
		{
			String content = null;  
			//创建一个httpClient对象   
			HttpClient httpClient = new DefaultHttpClient();  
			//创建请求方式对象  path   
			HttpPost httpPost = new HttpPost(COUNT_ADD);  
			System.out.println("创建请求方式对象  path");
			//封装请求的参数集合   
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();  
			parameters.add(new BasicNameValuePair("bkname",bkname+""));  
			UrlEncodedFormEntity entity = null;  
			System.out.print("开始try");
			try {  
				//封装请参数的实体对象
				entity = new UrlEncodedFormEntity(parameters, "UTF-8");
				System.out.print("封装请参数的实体对象");
				//把参数设置到 httpPost中 
				httpPost.setEntity(entity);  
				System.out.print(" 开始执行请求   ");
				//执行请求   
				HttpResponse httpResponse = httpClient.execute(httpPost);  
				//判断响应是否成功   

				System.out.print("判断响应是否成功   ");
				System.out.println(httpResponse.getStatusLine().getStatusCode()+"");
				if (httpResponse.getStatusLine().getStatusCode() == 200) {  
					//获取响应的内容   
					InputStream is = httpResponse.getEntity().getContent();  
					System.out.println("响应成功");
					//转换成字符串   
				}  
				else
				{
					System.out.println("响应失败");
				}
			} catch (UnsupportedEncodingException e) {  
				// TODO Auto-generated catch block   
				e.printStackTrace();  
			} catch (ClientProtocolException e) {  
				// TODO Auto-generated catch block   
				e.printStackTrace();  
				System.out.println("httpResponse.getStatusLine().getStatusCode()");
			} catch (IOException e) {  
				// TODO Auto-generated catch block   
				e.printStackTrace();  
			}   
		}
	}



	private void loadData()
	{

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int pages=1;
					String addString=colurl;						
					System.out.println(addString+"  !   "+pages);
					URL url = new URL(addString);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setConnectTimeout(60000);
					urlConnection.connect();
					if(urlConnection.getResponseCode() == 200)
					{
						String content = usedemo.getHTML(addString, "gbk");//得到html的代码	
						String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
						List<String > titleList = usedemo.dealToEachTitle(usedemo.getPassage(initPassage), ")");
						List<String>linkList1= usedemo.divideToTitle( titleList);//获取纯文本标题内容，为了给后面的那个抓链接用的
						List<String>linkTime=usedemo.divideToTime(titleList);//获取时间
						List<String>linkList=usedemo.dealToEachLink(linkList1,content);
						for(int i=0;i<linkList.size();i++){
							System.out.println(i+linkList.get(i));

							for(i=0;i<linkList.size();i++){
								String link=linkList.get(i);
								String result = linkList1.get(i);

								//System.out.println(i+linkList.get(i));
								String dateString=linkTime.get(i);
								info.add(result);
								date.add(dateString);
								Title_to_Link.put(result, link);	
							}    
						}
						System.out.println("=====================================================");


					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				resultList.addAll(info);
				mDates.addAll(date);

				Log.v("MyTag", "将初始数据填充至list容器");
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.sendToTarget();
			}
		}).start();
	}


	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() 
			{
				info.clear();
				date.clear();
				resultList.clear();
				Log.v(" 开始更新", "开始");
				try {
					String addString=colurl;						
					URL url = new URL(addString);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setConnectTimeout(60000);
					urlConnection.connect();
					if(urlConnection.getResponseCode() == 200)
					{
						String content = usedemo.getHTML(addString, "gbk");//得到html的代码	
						String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
						List<String > titleList = usedemo.dealToEachTitle(usedemo.getPassage(initPassage), ")");
						List<String>linkList1= usedemo.divideToTitle( titleList);//获取纯文本标题内容，为了给后面的那个抓链接用的
						List<String>linkTime=usedemo.divideToTime(titleList);//获取时间
						List<String>linkList=usedemo.dealToEachLink(linkList1,content);
						for(int i=0;i<linkList.size();i++){
							System.out.println(i+linkList.get(i));

							for(i=0;i<linkList.size();i++){
								String link=linkList.get(i);System.out.println(link);
								String result = linkList1.get(i);

								System.out.println(i+linkList.get(i));
								String dateString=linkTime.get(i);
								info.add(result);
								date.add(dateString);
								Title_to_Link.put(result, link);	
							}    
						}
						System.out.println("=====================================================");


					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				resultList.addAll(info);
				mDates.addAll(date);

				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				Log.v("MyTag", "刷新完成");
				msg.obj = mData;
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() 
			{
				currentPage++;
				info.clear();
				date.clear();
				resultList.clear();

				try {
					String addString=colurl;						
					if(currentPage>1)
					{
						addString=colurl.replace(".html", "_"+currentPage+".html");
					}
					System.out.println(addString+"  !   "+currentPage);				
					URL url = new URL(addString);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setConnectTimeout(60000);
					urlConnection.connect();
					if(urlConnection.getResponseCode() == 200)
					{
						String content = usedemo.getHTML(addString, "gbk");//得到html的代码	
						String initPassage=TextExtract.parse(content);//调用哪个类里面的方法	
						List<String > titleList = usedemo.dealToEachTitle(usedemo.getPassage(initPassage), ")");
						List<String>linkList1= usedemo.divideToTitle( titleList);//获取纯文本标题内容，为了给后面的那个抓链接用的
						List<String>linkTime=usedemo.divideToTime(titleList);//获取时间
						List<String>linkList=usedemo.dealToEachLink(linkList1,content);
						for(int i=0;i<linkList.size();i++){
							System.out.println(i+linkList.get(i));

							for(i=0;i<linkList.size();i++){
								String link=linkList.get(i);
								System.out.println(link);
								String result = linkList1.get(i);
								String dateString=linkTime.get(i);
								info.add(result);
								date.add(dateString);
								Title_to_Link.put(result, link);	
								System.out.println(result+"   "+link);
							}    
						}
						System.out.println("=====================================================");

					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//将数据填充至list容器
				resultList.addAll(info);
				mDates.addAll(date);

				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				Log.v("MyTag", "加载更多");
				msg.sendToTarget();
			}
		}).start();
	}

	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:{
				if(!resultList.isEmpty()){
					mData.addAll(resultList);
					Log.v("MyTag", "加载初始数据，更新adapter");
					mAdapter.notifyDataSetChanged();							
				}
				//告诉它数据加载完毕;
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH :{

				int p=0;
				for (String news : resultList) {
					if(news.equals(mData.get(p)))
					{
						break;
					}
					else
					{
						mData.add(p, news);
						p++;
					}
					mAdapter.notifyDataSetChanged();
				}

				// 告诉它更新完毕
				Log.v("MyTag", "刷新完成,更新adapter");
				mPullDownView.notifyDidRefresh();
				break;
			}

			case WHAT_DID_MORE:{
				mData.addAll(resultList);
				//resultList=mData;

				for(int i=0;i<mData.size();i++)
				{
					Log.v(mData.get(i)+" "+i, Title_to_Link.get(mData.get(i)));
				}

				mAdapter.notifyDataSetChanged();
				// 告诉它获取更多完毕
				Log.v("MyTag", "获取更多完毕,更新adapteer");
				mPullDownView.notifyDidMore();
				break;
			}
			}

		}		
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//	Toast.makeText(this, "啊，你点中我了 " + position, Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(News_Browse.this,NewsDetail.class);
		intent.putExtra("title", mData.get(position));
		System.out.println("=============================================");
		System.out.println(mData.get(position));
		intent.putExtra("linkURL", Title_to_Link.get(mData.get(position)));
		//Log.v("位置 "+position, info.get(position)+"   "+Title_to_Link.get(info.get(position)));
		startActivity(intent);
	}

	//自定义adapter
	private class MyAdapter extends BaseAdapter{
		private Context mContext;
		public MyAdapter(Context context) {
			this.mContext=context;
		}
		/**
		 * 元素的个数
		 */

		public int getCount() {
			return mData.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
		//用以生成在ListView中展示的一个个元素View
		public View getView(int position, View convertView, ViewGroup parent) {
			//优化ListView

			//index记录点击的listview中item的位置，并进行相应的点击反应
			final int index=position;
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.news_browser, null);
				ItemViewCache viewCache=new ItemViewCache();
				viewCache.Info=(TextView)convertView.findViewById(R.id.news_info);
				viewCache.Time=(TextView)convertView.findViewById(R.id.time);
				viewCache.Line=(ImageView) convertView.findViewById(R.id.line);
				convertView.setTag(viewCache);
			}
			ItemViewCache cache=(ItemViewCache)convertView.getTag();
			//设置文本，然后返回这个View，用于ListView的Item的展示
			cache.Info.setText(mData.get(position));
			cache.Time.setText(mDates.get(position));//这里一定要%30，因为刷新以后，第二页的12345就变成了31 32 33 34 35，当然是从0开始的
			return convertView;
		}
	}
	//元素的缓冲类,用于优化ListView
	private static class ItemViewCache{
		public TextView Info;
		public TextView Time;
		public ImageView Line;
	}




	//	private ArrayList<String> data;	

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


	//		    public boolean onCreateOptionsMenu(Menu menu) { 
	//		        MenuInflater inflater = getMenuInflater(); 
	//		        inflater.inflate(R.menu.option, menu); 
	//		        return true; 
	//		    } 

	//用来判断时间的，看看要不要该死的老师的图
	public static boolean isTimeCorrect(){//系统时间在给定时间之前，就返回true
		Date nowDate=new Date();
		Date date=null;
		Date dataSt=null;
		String dateStr = "2014-10-4";
		String dataBeg = "2014-9-30";
		String[ ]  dateDivide = dateStr.split("-");
		String[ ]  dataBegDiv = dataBeg.split("-");
		if(dateDivide.length==3){
			int year = Integer.parseInt(dateDivide [0].trim());//去掉空格
			int month = Integer.parseInt(dateDivide [1].trim());
			int day = Integer.parseInt(dateDivide [2].trim());
			Calendar c = Calendar.getInstance();//获取一个日历实例
			c.set(year, month-1, day);//设定日历的日期
			date = c.getTime();
		}
		if(dataBegDiv.length==3){
			int year = Integer.parseInt(dataBegDiv [0].trim());//去掉空格
			int month = Integer.parseInt(dataBegDiv [1].trim());
			int day = Integer.parseInt(dataBegDiv [2].trim());
			Calendar c = Calendar.getInstance();//获取一个日历实例
			c.set(year, month-1, day);//设定日历的日期
			dataSt = c.getTime();
		}
		if(nowDate.before(date)&&nowDate.after(dataSt)){
			return true;//如果在这个日期之前有
		}else{
			return false;
		}
	}
}
