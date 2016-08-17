/**
 *  ClassName: HeadlinesListViewAdapter.java
 *  created on 2012-3-2
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.model.PushData;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 头条ListView适配器
 * @author qjyong
 */
public class HeadlinesListViewAdapter extends BaseAdapter {
	private ArrayList<PushData> datas = new ArrayList<PushData>();
	private LayoutInflater inflater;

	private Context ctx;
	private MyApp myApp;
	private int screenWidth;
	private boolean img_invisible;
	
	
	/**
	 * 构造方法
	 * @param ctx
	 */
	@SuppressWarnings("static-access")
	public HeadlinesListViewAdapter(Context ctx){
		this.ctx = ctx;
		
		inflater = LayoutInflater.from(ctx);
		myApp = (MyApp)ctx.getApplicationContext();
		
		screenWidth = myApp.getScreenWidth();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	public Object getItem(int index) {
		return datas.get(index);
	}

	public long getItemId(int index) {
		return datas.get(index).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_topics_headlines, null);
		}

		TextView txt_id = (TextView)convertView.findViewById(R.id.topic_id);
		TextView txt_content = (TextView)convertView.findViewById(R.id.topic_content);
		ImageView txt_image = (ImageView)convertView.findViewById(R.id.topic_img);
		TextView txt_title = (TextView)convertView.findViewById(R.id.topic_title);
		
		img_invisible = myApp.isImg_invisible();
		
		PushData topic = datas.get(position);
		txt_id.setText(String.valueOf(topic.getId()));
		txt_title.setText(topic.getTitle());
		txt_content.setText(topic.getSummary());
	
		if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(ctx) || !img_invisible){
			//配图异步下载
			txt_image.setTag(topic.getPic());
			
			//loader.loadImage(position, topic.getPic(), imageLoadListener);
			
			async(txt_image);
		}else{
			txt_image.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	public void setDatas(ArrayList<PushData> datas) {
		this.datas = datas;
	}
	
	/*
	AsyncImageLoader.OnImageLoadListener imageLoadListener = new AsyncImageLoader.OnImageLoadListener(){
		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			//BookModel model = (BookModel) getItem(t);
			View view = lv.findViewWithTag(t);
			if(view != null){
				ImageView iv = (ImageView) view.findViewById(R.id.topic_img);
				iv.setImageDrawable(drawable);
			}
		}
		@Override
		public void onError(Integer t) {
			View view = lv.findViewWithTag(t);
			if(view != null){
				ImageView iv = (ImageView) view.findViewById(R.id.topic_img);
				iv.setImageResource(R.drawable.bg_topic_default);
			}
		}
		
	};
	
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					loader.lock();
					break;
					
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					int start = lv.getFirstVisiblePosition();
					int end = lv.getLastVisiblePosition();
					if(end >= getCount()){
						end = getCount() -1;
					}
					loader.setLoadLimit(start, end);
					loader.unlock();
					break;

				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					loader.lock();
					break;
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};*/

	private void async(final ImageView mv){
		ImageLoader.getInstance().asyncLoadBitmap((String)mv.getTag(), screenWidth, new ImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap bmp, String url) {
				if(bmp != null){
					mv.setImageBitmap(bmp);
				}
				/*
				ImageView temp = (ImageView)lv.findViewWithTag(url);
				if(null != temp && bmp != null){
					temp.setImageBitmap(bmp);
				}
				*/
			}
		});
	}	
}
