/**
 *  ClassName: UserListViewAdapter.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.model.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户ListView选项菜单样式的适配器<br/>
 * @author qjyong
 */
public class UserListViewAdapter extends BaseAdapter {
	private ArrayList<User> datas;
	private int size;
	private LayoutInflater inflater;
	private MyApp myApp;
	/**
	 * 构造方法
	 * @param ctx
	 */
	public UserListViewAdapter(Context ctx, ArrayList<User> datas){
		myApp =(MyApp)ctx.getApplicationContext();
		
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
		return index;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_user, null);
		}
		
		ImageView login_state = (ImageView)convertView.findViewById(R.id.item_image);
		TextView tv = (TextView)convertView.findViewById(R.id.item_text);
		//ImageView arrow = (ImageView)convertView.findViewById(R.id.item_arraw);
		//ImageView delete = (ImageView)convertView.findViewById(R.id.item_delete);
		
		User user = datas.get(position); 
		
		tv.setText(user.getAuthor());
		if(null != myApp.getSid() 
				&& !"".equals(myApp.getSid()) 
				&& user.getAuthorid().equals(myApp.getUid())){
			login_state.setImageResource(R.drawable.logined);
		}else{
			login_state.setImageResource(R.drawable.un_logined);
		}
		
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
}
