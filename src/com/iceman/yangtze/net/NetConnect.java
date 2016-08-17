package com.iceman.yangtze.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.HandActivity;
import com.special.ResideMenuDemo.OnLineCourse;


public class NetConnect extends Thread{
	private String url;
	private ArrayList<NameValuePair> para;
	private int flag;
	private Handler hander;
   
	public NetConnect(String url,ArrayList<NameValuePair> para,int flag,Handler tempHandle){
		this.url=url;
		this.para=para;
		this.flag=flag;
		this.hander=tempHandle;
	}
	public void run(){
		String result;
		// 设置连接超时
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(url);
		HttpGet httpGet=new HttpGet(url);
		if(para!=null){
			try{
				httpPost.setHeader("Cookie",Globe.sCookieString);  
				httpPost.setEntity(new UrlEncodedFormEntity(para, "gb2312"));  
				/* 发出HTTP request */  
				HttpResponse httpResponse2 = new DefaultHttpClient().execute(httpPost);  
				/* 若状态码为200 ok */  
				if (httpResponse2.getStatusLine().getStatusCode() == 200) {  
					StringBuffer sb = new StringBuffer();  
					HttpEntity entity = httpResponse2.getEntity();  
					InputStream is = entity.getContent();  
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));  
					//是读取要改编码的源，源的格式是GB2312的，安源格式读进来，然后再对源码转换成想要的编码就行  
					String data = "";  
					while ((data = br.readLine()) != null) {  
						sb.append(data);  
					}  
					result = sb.toString();  //此时result中就是我们成绩的HTML的源代码了  
					System.out.println(result);
				} else { 

				}
			}catch (Exception e) {  
				e.printStackTrace();  
			}
		}else {
			try{
				System.out.println("在NetConnect里参数为"+para);
				httpGet.setHeader("Cookie",Globe.sCookieString);    
				HttpResponse httpResponse2 = new DefaultHttpClient().execute(httpGet);  
				/* 若状态码为200 ok */  
				if (httpResponse2.getStatusLine().getStatusCode() == 200) {  
					String strResult = EntityUtils.toString(httpResponse2.getEntity(),"UTF-8");  
					System.out.println(strResult);
					Message msg=new Message();
					msg.what=flag;
					msg.obj=strResult;
					hander.sendMessage(msg);
				} else { 

				}
			}catch (Exception e) {  
				e.printStackTrace();  
			}
		}
	}
}
