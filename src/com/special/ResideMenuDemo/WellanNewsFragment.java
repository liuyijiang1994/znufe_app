package com.special.ResideMenuDemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.xnfsh.News_title_list;
import com.example.xnfsh.ShowAllAccount;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenuDemo.WellanNewsFragment.GridAdapter.ViewHolder;
import com.umeng.analytics.MobclickAgent;
import com.znufe.outside.bigclass.Freshman;
import com.znufe.outside.bigclass.News_Browse;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:31
 * Mail: specialcyci@gmail.com
 */
public class WellanNewsFragment extends Fragment {
	
	private ResideMenu resideMenu;
	private GridAdapter adapter;
	private ViewHolder holder;
	private GridView gridView;
	private View v;
	private int W;
	private int H;

	public List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
	
	String[] znuelLinks=new String[]{
			 "http://wellan.znufe.edu.cn/ttxw/2.html"
			,"http://wellan.znufe.edu.cn/zhxw/7.html"
			,"http://wellan.znufe.edu.cn/zhxw/8.html"
	        , "http://wellan.znufe.edu.cn/zhxw/10.html"
	        //, "http://wellan.znufe.edu.cn/rdjj/41.html"
	        , "http://wellan.znufe.edu.cn/mtbd/4.html"
	        , "http://wellan.znufe.edu.cn/zhxw/60.html"
	        , "http://wellan.znufe.edu.cn/zhxw/9.html"
	        , "http://wellan.znufe.edu.cn/xycq/12.html"
	       };
	String bkname[]={"wlxw","xzfc","jydt","rwzf",/*"rdjj",*/"mtbd","zhxw","sdbd","xycq","gegz","xszdc"};
	//定义一个list容器，用来存放图片地址，方便动态添加
	public List<Object> pic=new ArrayList<Object>();
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view=inflater.inflate(R.layout.pulldownwellannews, container, false);
    	v=view;
    	Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();  
		W = mDisplay.getWidth();  
		H = mDisplay.getHeight();  
    	gridView=(GridView) view.findViewById(R.id.gridView1);
    	gridView.setPadding((int) (W*0.02), 0, (int) (W*0.02),LayoutParams.WRAP_CONTENT);
    	gridView.setHorizontalSpacing((int) (W*0.02));
    	gridView.setVerticalSpacing((int) (H*0.006));

    	Log.v("已经绑定了gridview", "创建adapter");
	    adapter=new GridAdapter();
    	Log.v("创建adapter", "成功");
    	
    	MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        view.findViewById(R.id.title_bar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });
        
    	return view;
        
    }
   		
  	 @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
String column[]={"文澜要闻","学子风采","教研动态","人物专访","热点聚焦","媒体报道","综合新闻","深度报道","校园春秋","广而告之","新生招待处"};

		
		
		pic.add(R.drawable.wlxw);
		pic.add(R.drawable.xzfc);
		pic.add(R.drawable.jydt);
		pic.add(R.drawable.rwzf);
		//pic.add(R.drawable.rdjj);
		pic.add(R.drawable.mtbd);
		pic.add(R.drawable.zhxw);
		pic.add(R.drawable.sdbd);
		pic.add(R.drawable.xycq);
		pic.add(R.drawable.gegz);
		pic.add(R.drawable.xszdcc);
		
		for(int i=0;i<pic.size();i++)
		{
			Map<String, Object> cell=new HashMap<String, Object>();
			cell.put("imageView",pic.get(i));
			cell.put("column_name", column[i]);
			list.add(cell);
		}
		Log.v("绑定adapter", "开始");
		gridView.setAdapter(adapter);
		Log.v("绑定adapter", "成功");
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
  				Log.v("获取cell","kaishi ");
  				convertView= LayoutInflater.from(getActivity()).inflate(
  						R.layout.cell, null);
  				Log.v("获取cell","end ");
  				holder.view = (ImageView)convertView.findViewById(R.id.img);
  		//		holder.title=(TextView) convertView.findViewById(R.id.tv);
  				convertView.setTag(holder);
  			} 
  			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
  		  //动态改变图片大小
  			LayoutParams layoutParams=viewHolder.view.getLayoutParams();
  			layoutParams.width=(int) (W*0.47);
  			layoutParams.height=(int) ((W*0.47)/1.5);
  			viewHolder.view.setLayoutParams(layoutParams);


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
  						
  						//若点击的图片是倒数第二张图片,进入通知公告
  						if(position==list.size()-2)
  						{			
  							Intent intent=new Intent(getActivity(),News_title_list.class);
  							intent.putExtra("id", "znuel_wellan");
  							intent.putExtra("name", "gegz");
  							startActivity(intent);
  						}	
  					     //若点击的图片是最后一张图片,进入新生版块
  						else if (position==list.size()-1) 
  						{
  							Intent intent=new Intent(getActivity(),News_title_list.class);
  							intent.putExtra("id", "znuel_freshman");
  							intent.putExtra("name", "xszd");
  							startActivity(intent);
						}
  						else
  						{
  							Intent intent=new Intent(getActivity(),News_Browse.class);
  							intent.putExtra("link", znuelLinks[position]);
  							intent.putExtra("bkname", bkname[position]);
  							startActivity(intent);
  						}
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
  	
  	public void onResume() {
  	    super.onResume();
  	    MobclickAgent.onPageStart("WellanNewsFragment"); //统计页面
  	}
  	public void onPause() {
  	    super.onPause();
  	    MobclickAgent.onPageEnd("WellanNewsFragment"); 
  	}
  }
