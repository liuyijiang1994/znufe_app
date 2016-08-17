/**
 *  ClassName: FocusListViewAdapter.java
 *  created on 2012-3-3
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;

import net.shopnc.android.model.PushData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 焦点ListView的适配器
 * @author qjyong
 */
public class FocusListViewAdapter extends BaseAdapter {
	private ArrayList<PushData> datas;
	private LayoutInflater inflater;
	private ViewHolder vh;
	
	/**
	 * 构造方法
	 * @param ctx
	 */
	public FocusListViewAdapter(Context ctx){
		inflater = LayoutInflater.from(ctx);
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
			convertView = inflater.inflate(R.layout.listview_item_topics_focus, null);
			vh = new ViewHolder();
			vh.txt_id = (TextView)convertView.findViewById(R.id.topic_id);
			vh.txt_title  = (TextView)convertView.findViewById(R.id.topic_title);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		PushData pd = datas.get(position);
		vh.txt_id.setText(String.valueOf(pd.getId()));
		vh.txt_title.setText(pd.getTitle());
		
		return convertView;
	}
	
	public void setDatas(ArrayList<PushData> datas) {
		this.datas = datas;
	}
	
	class ViewHolder{
		TextView txt_id;
		TextView txt_title;
	}
}
