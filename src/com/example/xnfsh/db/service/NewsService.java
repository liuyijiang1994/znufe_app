package com.example.xnfsh.db.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.xnfsh.db.entity.Account;
import com.example.xnfsh.db.entity.News;

public class NewsService {

	static String fETCH_all_NEWS="http://202.114.234.122:8234/tecApp/nw_fetchNewsbyID.php?accountID=";

	public static List<News> fetch_all_news(String id,int start)
	{
		InputStream is = null;
        String result="";
        List<News> news = new ArrayList<News>();
        Log.v("url",fETCH_all_NEWS+id+"&start="+start);
        try{  
			HttpClient httpclient = new DefaultHttpClient();  
	        HttpPost httppost = new HttpPost(fETCH_all_NEWS+id+"&start="+start);  
	        httppost.setEntity(null); 
	        		        
	        System.out.println("开始连接");
	        HttpResponse response = httpclient.execute(httppost); 
	        System.out.println("连接成功");
	        HttpEntity entity = response.getEntity();  
	        is = entity.getContent(); 
	  }catch(Exception e){  
	          Log.e("log_tag", "Error in http connection "+e.toString()); 
	          System.out.println("连接失败");
	  }  
	  //将HttpEntity转化为String  
	  try{  
	          BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
	          StringBuilder sb = new StringBuilder();  
	          String line = null;  
	          while ((line = reader.readLine()) != null) {  
	                  sb.append(line + "\n");  
	          }  
	          is.close();  
	     
	          result=sb.toString();  
	  }catch(Exception e){  
	          Log.e("log_tag", "Error converting result "+e.toString());  
	  }  
        Log.v("开始转换json数据", "加油~~~~~");
        Log.v("result",result);
        try { 
        	
        		JSONArray jsa =new JSONArray(result);
        		Log.v("jso的count",jsa.length()+ "                      ！！！");
        		if(jsa.length()>0)
        		{
			        for(int i=0;i<jsa.length();i++)
			        {
			        	
			            JSONObject jso;
						jso = jsa.getJSONObject(i);
						Log.v("title", jso.getString("NewsTitle").toString());
						System.out.println("text"+jso.getString("NewsText").toString());
						Log.v("date", jso.getString("NewsDate").toString());
						news.add( new News(jso.getString("NewsTitle").toString(), jso.getString("NewsText").toString(), jso.getString("NewsDate").toString()));
			        }  
					return news;
        		}	
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
	public News fetchNewsByID(int i)
	{
		
		
		return null;
	}
	
}
