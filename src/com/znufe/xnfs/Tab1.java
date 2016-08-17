package com.znufe.xnfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.final_me.znuelNewsTitle;
import com.example.xnfsh.ShowAllAccount;
import com.special.ResideMenuDemo.R;
import com.znufe.outside.bigclass.News_Browse;
import com.znufe.xnfs.Tab1.GridAdapter.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Tab1 extends Activity{
	
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	private GridAdapter adapter;
	private ViewHolder holder;
	public List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
	
	String[] znuelLinks=new String[]{
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
	
	//定义一个list容器，用来存放图片地址，方便动态添加
	public List<Object> pic=new ArrayList<Object>();
	
//	public SimpleAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1);
		GridView gridView=(GridView) this.findViewById(R.id.gridView1);
	    adapter=new GridAdapter();
		String column[]={"文澜要闻","学子风采","教研动态","人物专访","热点聚焦","媒体报道","综合新闻","深度报道","校园春秋"};
		
		
		pic.add(R.drawable.wlxw);
		pic.add(R.drawable.xzfc);
		pic.add(R.drawable.jydt);
		pic.add(R.drawable.rwzf);
		pic.add(R.drawable.rdjj);
		pic.add(R.drawable.mtbd);
		pic.add(R.drawable.zhxw);
		pic.add(R.drawable.sdbd);
		pic.add(R.drawable.xycq);
//		pic.add(R.drawable.add);
		
		for(int i=0;i<pic.size();i++)
		{
			Map<String, Object> cell=new HashMap<String, Object>();
			cell.put("imageView",pic.get(i));
			cell.put("column_name", column[i]);
			list.add(cell);
		}
		gridView.setAdapter(adapter);
		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(Tab1.this, "你单击了", Toast.LENGTH_SHORT).show();
			}
		});
		*/
	}
	
	//实现，按两次返回键退出
		Timer tExit = new Timer();
		TimerTask task = new TimerTask(){

			@Override
			public void run() {
				isExit = false;
				hasTask = true;
			}
			
		};
		
		@Override
		 public boolean onKeyDown(int keyCode, KeyEvent event) {
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
			  if(!isExit){
				  isExit = true;
				  Toast.makeText(Tab1.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
				  if(!hasTask){
					  tExit.schedule(task, 3000);
				  }
				  return true;
			  }else{
				  finish();
				  System.exit(0);
			  }
		  }
		  return false;
		 }
		
	 public static void removeListElement(List<Map<String,Object>> list) {  
         for(int i=0;i<list.size();i++) {  
             if("添加".equals(list.get(i).get("column_name"))) {  
                 list.remove(i);  
                 --i;//删除了元素，迭代的下标也跟着改变  
             }  
         }  
     }  
 
	class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView== null) {
				holder = new ViewHolder();
				convertView= LayoutInflater.from(Tab1.this).inflate(
						R.layout.cell, null);
				holder.view = (ImageView)convertView.findViewById(R.id.img);
		//		holder.title=(TextView) convertView.findViewById(R.id.tv);
				convertView.setTag(holder);
			} 
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		//	viewHolder.view=(ImageView) findViewById(R.id.img);
		//	viewHolder.title=(TextView) findViewById(R.id.tv);
		//	viewHolder.title.setText(list.get(position).get("column_name").toString());
		//	viewHolder.title.setText(list.get(position).get("column_name").toString());
			viewHolder.view.setImageResource((Integer) list.get(position).get("imageView"));
			viewHolder.view.setOnTouchListener(new View.OnTouchListener() {
		//		int location=position;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						changeLight((ImageView) v, 0);
				//		Toast.makeText(Tab1.this, "你单击了"+position, Toast.LENGTH_SHORT).show();
						
						//若点击的图片是最后一张图片,则动态添加一张图片
						/*if(position==list.size()-1)
						{
							
							removeListElement(list);
							//向pic中添加图片
						//	pic.add(R.drawable.logo);
							Map<String, Object> cell=new HashMap<String, Object>();
							cell.put("imageView",R.drawable.logo);
							cell.put("column_name", "xxx");
							list.add(cell);
					//		pic.add(R.drawable.logo);
							Map<String, Object> cell_add=new HashMap<String, Object>();
							cell_add.put("imageView",R.drawable.add);
							cell_add.put("column_name", "添加");
							list.add(cell_add);
							//更新
							adapter.notifyDataSetChanged();
							
							
							Intent intent=new Intent(Tab1.this,ShowAllAccount.class);
							startActivity(intent);
						}	
						else
						{
							Intent intent=new Intent(Tab1.this,News_Browse.class);
							intent.putExtra("link", znuelLinks[position]);
							startActivity(intent);
						}*/
						Intent intent=new Intent(Tab1.this,News_Browse.class);
						intent.putExtra("link", znuelLinks[position]);
						startActivity(intent);
						// onclick
						break;
					case MotionEvent.ACTION_DOWN:
						changeLight((ImageView) v, -80);
						break;
					case MotionEvent.ACTION_MOVE:
						// changeLight(view, 0);
						break;
					case MotionEvent.ACTION_CANCEL:
						changeLight((ImageView) v, 0);
						break;
					default:
						break;
					}
					return true;
				}
			});
			
			return convertView;
		}

		class ViewHolder {
			ImageView view;
		//	TextView title;
		}
		

	}

	private void changeLight(ImageView imageview, int brightness) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));

	}
	
	
}



