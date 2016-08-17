/**
 *  ClassName: MenuListViewAdapter.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * ListView选项菜单样式的适配器<br/>
 * 注意：此类使用ListView每个选项的文本内容在R类中的标识值（R.string.xxx）作为选项的ID值
 * @author qjyong
 */
public class MenuListViewAdapter extends BaseAdapter {
	/**  */
	public static final String TAG_ITEM_TEXT = "text";
	/**  */
	public static final String TAG_ITEM_CHECKED = "cbx";
	
	private ArrayList<HashMap<String, Object>> datas;
	private int size;
	private LayoutInflater inflater;
	private ViewHolder vh;
	private Context ctx;
	/**
	 * 构造方法
	 * @param ctx
	 */
	public MenuListViewAdapter(Context ctx, ArrayList<HashMap<String, Object>> datas){
		this.ctx = ctx;
		inflater = LayoutInflater.from(ctx);
		this.datas = datas;
		size = datas == null ? 0 : datas.size();
	}

	public int getCount() {
		return size;
	}

	public Object getItem(int index) {
		return datas.get(index);
	}

	public long getItemId(int index) {
		HashMap<String, Object> item = datas.get(index);
		
		Integer txt_id = (Integer)item.get(TAG_ITEM_TEXT);
		if(txt_id != null){
			return txt_id.intValue();
		}else{
			return index;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_text_checkbox, null);
			vh = new ViewHolder();
			vh.cbx = (CheckBox)convertView.findViewById(R.id.item_cbx);
			vh.tv = (TextView)convertView.findViewById(R.id.item_text);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		HashMap<String, Object> item = datas.get(position);
		
		vh.cbx.setChecked(((Boolean)item.get(TAG_ITEM_CHECKED)).booleanValue());
		vh.tv.setText(ctx.getString(((Integer)item.get(TAG_ITEM_TEXT)).intValue()));
		
		if(size == 1){
			convertView.setBackgroundResource(R.drawable.list_item_single);
		}else if(position == 0){
			convertView.setBackgroundResource(R.drawable.list_item_first);
		}else if(position == size - 1){
			convertView.setBackgroundResource(R.drawable.list_item_last);
		}else{
			convertView.setBackgroundResource(R.drawable.list_item_plain);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		CheckBox cbx;
		TextView tv;
	}
}
