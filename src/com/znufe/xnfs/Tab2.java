package com.znufe.xnfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.special.ResideMenuDemo.R;
import com.znufe.xnfs.db.entity.Tiny_class;
import com.znufe.xnfs.db.service.Tiny_classService;

import edu.swust.iweather.Weather;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Tab2 extends Activity{

	private MyAdapter mAdapter;
	private ViewHolder holder;
	public List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
	
	public HashMap<String, Object> subject_to_pic=new HashMap<String,Object>();
	
	List<Tiny_class> selectedTiny=new ArrayList<Tiny_class>();
	
	Tiny_classService tcs;
	
	//定义一个list容器，用来存放图片地址，方便动态添加
	public List<Object> pic=new ArrayList<Object>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1);
		GridView gridView=(GridView) this.findViewById(R.id.gridView1);
		
		tcs=new Tiny_classService(getApplicationContext());
		
		pic.add(R.drawable.logo);
		pic.add(R.drawable.add);
		
		
		String column[]={"天气","添加"};
		for(int i=0;i<pic.size();i++)
		{
			Map<String, Object> cell=new HashMap<String, Object>();
			cell.put("imageView",pic.get(i));
			cell.put("column_name", column[i]);
			list.add(cell);
		}
		mAdapter=new MyAdapter();
		gridView.setAdapter(mAdapter);
		
		new GetSelected().start();
	}
	
	 public static void removeListElement(List<Map<String,Object>> list) {  
         for(int i=0;i<list.size();i++) {  
             if("添加".equals(list.get(i).get("column_name"))) {  
                 list.remove(i);  
                 --i;//删除了元素，迭代的下标也跟着改变  
             }  
         }  
     }
	
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case 1:
					removeListElement(list);
					Iterator<Tiny_class> it=selectedTiny.iterator();
					while(it.hasNext()){
						   // System.out.println(it.next().name);
					//	System.out.println("订阅了");
					//	Log.v("tag","订阅了");
						Map<String, Object> item=new HashMap<String, Object>();
					//	pic.add(R.drawable.logo);
						item.put("imageView",R.drawable.logo);
						String name=it.next().getName();
						item.put("column_name", name);
						list.add(item);
						}
					Map<String, Object> item=new HashMap<String, Object>();
					//	pic.add(R.drawable.logo);
						item.put("imageView",R.drawable.add);
						item.put("column_name","添加");
						list.add(item);
					mAdapter.notifyDataSetChanged();
					break;
				
				case 10:
					Toast.makeText(getApplicationContext(), "当前有"+selectedTiny.size()+"个板块", Toast.LENGTH_SHORT).show();
					System.out.println("当前有"+pic.size()+"个板块");
					break;
            }
	 }
	};
	
	public class GetSelected extends Thread
	{
		public void run()
		{	
			System.out.println("开始  获取所有定有号");
		//	tcs=new Tiny_classService(getApplicationContext());
			//Tiny_classService tcs=new Tiny_classService(getApplicationContext());
			selectedTiny=tcs.fetchSelected();
			//Toast.makeText(getApplicationContext(), "~", Toast.LENGTH_SHORT).show();
			System.out.println("获取所有定有号完毕"+selectedTiny.size());
			
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
		}
	}
	
	
	
	//自定义adapter

	public class MyAdapter extends BaseAdapter {

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
				convertView= LayoutInflater.from(Tab2.this).inflate(
						R.layout.cell_tab2, null);
				holder.view = (ImageView)convertView.findViewById(R.id.img);
				holder.title=(TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} 
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		//	viewHolder.view=(ImageView) findViewById(R.id.img);
		//	viewHolder.title=(TextView) findViewById(R.id.tv);
		//	viewHolder.title.setText(list.get(position).get("column_name").toString());
			//若为最后一张添加图片，则不显示文字
			if(position!=list.size()-1)
			viewHolder.title.setText(list.get(position).get("column_name").toString());
			else viewHolder.title.setText("");
			viewHolder.view.setImageResource((Integer) list.get(position).get("imageView"));
			viewHolder.view.setOnTouchListener(new View.OnTouchListener() {
			//	int location=position;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						changeLight((ImageView) v, 0);
				//		Toast.makeText(Tab1.this, "你单击了"+position, Toast.LENGTH_SHORT).show();
						
						if(position==list.size()-1)
						{

							//当点击最后一张图片（即添加）的时候，实现activity的跳转
							Intent intent=new Intent(Tab2.this,Tab2.class);
							//开始跳转
							startActivityForResult(intent, 1);
							//finish();
						}
						else
						{
							if(position==0)
							{
								Intent intent=new Intent(Tab2.this,Weather.class);
								//开始跳转
								startActivity(intent);								
							}
							
							else
							{
								Toast.makeText(Tab2.this, list.get(position).get("column_name").toString(), Toast.LENGTH_SHORT).show();
								//change 
								//Intent intent=new Intent(Tab2.this,forDingyue.class);
								Intent intent=new Intent(Tab2.this,Tab2.class);
								intent.putExtra("urlFromtab2", list.get(position).get("column_name").toString());
								System.out.println( "*********************************************************");
								System.out.println(list.get(position).get("column_name").toString());
								//开始跳转
								startActivity(intent);	
							}
						}
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


	}

	private void changeLight(ImageView imageview, int brightness) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));

	}
	class ViewHolder {
		ImageView view;
		TextView title;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(getApplicationContext(), "dd~~~", Toast.LENGTH_SHORT).show();
	}
}
