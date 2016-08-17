package com.example.final_me;

import java.util.HashMap;

import com.special.ResideMenuDemo.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class specific extends Activity {
	// TODO Auto-generated method stub
	
	String foreItem;
	ListView lv;
	String[] schoolstrs;
	String[] znuelLinks;
	int number;
	public HashMap<	String, String> eachTitle_to_Link;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.titles);
		lv=(ListView)findViewById(R.id.listView1);
		final Intent data = getIntent(); 
		foreItem=data.getStringExtra("item");
		number=data.getIntExtra("number", 1);
		
		znuelLinks=new String[]{
				 "http://wellan.znufe.edu.cn/ttxw/2.html"
				,"http://wellan.znufe.edu.cn/zhxw/7.html"
				,"http://wellan.znufe.edu.cn/zhxw/8.html"
		        , "http://wellan.znufe.edu.cn/zhxw/10.html"
		        , "http://wellan.znufe.edu.cn/rdjj/41.html"
		        , "http://wellan.znufe.edu.cn/mtbd/4.html"
		        , "http://wellan.znufe.edu.cn/zhxw/60.html"
		        , "http://wellan.znufe.edu.cn/zhxw/9.html"
		        , "http://wellan.znufe.edu.cn/xycq/12.html"
		       };
		if(foreItem.compareTo("哈哈校园")==0){	
			/*eachTitle_to_Link.put("教研动态", "http://wellan.znufe.edu.cn/zhxw/8.html");
			eachTitle_to_Link.put("学子风采", "http://wellan.znufe.edu.cn/zhxw/7.html");
			eachTitle_to_Link.put("综合新闻", "http://wellan.znufe.edu.cn/zhxw/60.html");
			eachTitle_to_Link.put("人物专访", "http://wellan.znufe.edu.cn/zhxw/10.html");
			eachTitle_to_Link.put("深度报道", "http://wellan.znufe.edu.cn/zhxw/9.html");
			eachTitle_to_Link.put("媒体报道", "http://wellan.znufe.edu.cn/mtbd/4.html");
			eachTitle_to_Link.put("热点聚焦", "http://wellan.znufe.edu.cn/rdjj/41.html");
			eachTitle_to_Link.put("校园春秋", "http://wellan.znufe.edu.cn/xycq/12.html");
			eachTitle_to_Link.put("文澜要闻", "http://wellan.znufe.edu.cn/ttxw/2.html");*/
		
			schoolstrs = new String[] {"教研动态", "学子风采", "综合新闻", "人物专访", 
					"深度报道","媒体报道","热点聚焦","校园春秋" ,"文澜要闻"};	 
			
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(specific.this, znuelNewsTitle.class);
					intent.putExtra("link", znuelLinks[(int)arg3]);
					startActivity(intent);
				}
				
			});
			
		}
		else{
			//schoolstrs = new String[] { "腾讯网"+foreItem, "网易网"+foreItem,
					//"凤凰网"+foreItem,"大洋网"+foreItem};
			
			//先用两个网站做示范好了，还是得放到数据库才行的啊
			schoolstrs = new String[] { "腾讯网"+foreItem, "网易网"+foreItem};
			
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					//if(arg2==0)//点击的频道为腾讯的话，直接打到腾讯的那个页面上，qqlist
					//{
						Intent intent=new Intent();
						intent.setClass(specific.this, specific.class);
						intent.putExtra("whichWeb", arg2);
						intent.putExtra("columNumber", number);
						startActivity(intent);		
						}			
				//}
				});
		}
		lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, schoolstrs));
		
	}

}
