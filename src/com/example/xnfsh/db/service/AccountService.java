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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.xnfsh.db.entity.Account;

public class AccountService 
{
	static String GET_ALL_ACCOUNT="http://202.114.234.122:8234.gotoip3.com/znueler/fetch_all_account.php";
	
	public static List<Account> fetch_all_acccount()
	{
		InputStream is = null;
        String result="";
        List<Account> accounts = new ArrayList<Account>();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("year","1980"));
		try{  
				HttpClient httpclient = new DefaultHttpClient();  
		        HttpPost httppost = new HttpPost(GET_ALL_ACCOUNT);  
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
		        		        
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
		          BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);  
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
						Log.v("id~~~", jso.getString("account_id").toString());
						Log.v("name~~~", jso.getString("account_name").toString());
						Log.v("pir~~~", jso.getString("account_pir").toString());
						accounts.add( new Account(jso.getString("account_id").toString(), jso.getString("account_name").toString(), jso.getString("account_pir").toString()));
			        }  
					return accounts;
					
        		}	
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}
	
}