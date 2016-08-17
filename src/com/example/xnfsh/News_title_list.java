package com.example.xnfsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.final_me.News_db_detail;
import com.example.xnfsh.db.entity.News;
import com.example.xnfsh.db.service.NewsService;
import com.special.ResideMenuDemo.PullDownView;
import com.special.ResideMenuDemo.PullDownView.OnPullDownListener;
import com.special.ResideMenuDemo.R;


//import android.app.ActionBar;
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

public class News_title_list<E> extends Activity implements OnPullDownListener, OnItemClickListener{
	
	private static final String COUNT_ADD="http://202.114.234.122:8234/tecApp/news_count.php";
	String id="znuel_wellan";
	String name="gegz";
	ListView newstitle;
	List<News> newslist=new ArrayList<News>();
	List<News> tnewslist=new ArrayList<News>();
	//private ActionBar actionBar;
	
	private static final int WHAT_DID_LOAD_DATA = 0;//数据加载完毕
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int WHAT_DID_NoMore = 4;
	
	private PullDownView mPullDownView;
	private ListView mListView;
	private MyAdapter mAdapter;
	private Button btn_back;
	
	int visited=0;
	private int page=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pulldown_news);
		Log.v("初始化", "开始");
		
		id=getIntent().getStringExtra("id");
		name=getIntent().getStringExtra("name");
		
		btn_back=(Button)this.findViewById(R.id.title_bar_back);//这里是返回键，back，英语越来越差啦，受不鸟
		btn_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setDividerHeight(2);
		mListView.setFooterDividersEnabled(true);
		mListView.setDivider(new ColorDrawable(R.color.base_black));
		mListView.setOnItemClickListener(this);
	    //mAdapter = new ArrayAdapter<String>(this, R.layout.pulldown_item, mData);
		mAdapter=new MyAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
        
        mPullDownView.enableAutoFetchMore(false, 1);
        
        getApplicationContext();
		ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo(); 
		  if (info != null && info.isAvailable()){ 
			  if(visited==0)
			  {
				  loadData();
					new Count_add(name).start();
				  System.out.println("aaaaaaaaaaaaa");
				  visited=1;
			  }
		  }else{ 
			  Toast.makeText(getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
		  } 
	}
	
	private void loadData()
	{	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					tnewslist=NewsService.fetch_all_news(id,0);
					Log.v("MyTag", "将初始数据填充至list容器");
					Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					msg.sendToTarget();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
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

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {	
			@Override
			public void run() 
			{
				try {
					tnewslist=NewsService.fetch_all_news(id,0);
					page=0;
					Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					Log.v("MyTag", "刷新完成");
					msg.sendToTarget();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}).start();
	}
	
	@Override
	public void onMore() {
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				try {
					
					page+=10;
					if(tnewslist!=null)
						tnewslist.clear();
					tnewslist=NewsService.fetch_all_news(id,page);
					if(tnewslist==null)
					{
						Log.v("!1111", "!1");
						Message msg = mUIHandler.obtainMessage(WHAT_DID_NoMore);
						msg.sendToTarget();
					}
					else {
						Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
						Log.v("MyTag", "加载更多");
						msg.sendToTarget();
					}			
				} catch (Exception e) {
					// TODO: handle exception
				}			
			}
		}).start();
	}
	
	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WHAT_DID_LOAD_DATA:{
					newslist.addAll(tnewslist);
					mAdapter.notifyDataSetChanged();							
					//告诉它数据加载完毕;
					mPullDownView.notifyDidLoad();
					break;
				}
				case WHAT_DID_REFRESH :{
						newslist.clear();
						newslist.addAll(tnewslist);
						mAdapter.notifyDataSetChanged();
					
					// 告诉它更新完毕
					Log.v("MyTag", "刷新完成,更新adapter");
					mPullDownView.notifyDidRefresh();
					break;
				}
				
				case WHAT_DID_MORE:{
					newslist.addAll(tnewslist);
					mAdapter.notifyDataSetChanged();
					// 告诉它获取更多完毕
					Log.v("MyTag", "获取更多完毕,更新adapteer");
					mPullDownView.notifyDidMore();
					break;
				}
				case WHAT_DID_NoMore:{
					Toast.makeText(getApplicationContext(), "没有更多", Toast.LENGTH_SHORT).show();
					page-=10;
					mPullDownView.notifyDidMore();
					break;
				}
				}
			}	
	};
	
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
			return newslist.size();
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
			cache.Info.setText(newslist.get(position).getTitle());
			cache.Time.setText(newslist.get(position).getDate());
			return convertView;
		}
    }
	//元素的缓冲类,用于优化ListView
    private static class ItemViewCache{
    	public TextView Info;
    	public TextView Time;
		public ImageView Line;
	}
    
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		News news=newslist.get(position);
		Intent intent=new Intent(News_title_list.this,News_db_detail.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("newsentity", news);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
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
