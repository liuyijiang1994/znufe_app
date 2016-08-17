/**
 *  ClassName: BoardFavoriteListViewAdapter.java
 *  created on 2012-3-10
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;

import net.shopnc.android.model.Board;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 收藏版块的适配器
 * @author qjyong
 */
public class BoardFavoriteListViewAdapter extends BaseAdapter {
	/** 存放选项数据的集合 */
	private ArrayList<Board> datas;
	/** 布局加载器 */
	private LayoutInflater inflater;

	/**
	 * 构造方法
	 * 
	 * @param ctx
	 */
	public BoardFavoriteListViewAdapter(Context ctx) {
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
		return datas.get(index).getFid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_board_favorite, null);
		}
		TextView txt_title = (TextView) convertView.findViewById(R.id.title);

		Board board = datas.get(position);
		txt_title.setText(board.getName());

		return convertView;
	}

	public void setDatas(ArrayList<Board> datas) {
		this.datas = datas;
	}
}
