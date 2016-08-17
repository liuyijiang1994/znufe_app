/**
 *  ClassName: MyExpandableListAdapter.java
 *  created on 2012-3-7
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
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * 可展开的ListView的适配器
 * @author qjyong
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Board> boards;
	private int size;
	public Context context;
	private LayoutInflater childInflater;
	private LayoutInflater groupInflater;
	
	private GroupViewHolder gvh;
	private ChildViewHolder cvh;

	public MyExpandableListAdapter(Context ctx, ArrayList<Board> boards) {
		this.context = ctx;
		this.childInflater = LayoutInflater.from(ctx);
		this.groupInflater = LayoutInflater.from(ctx);
		this.boards = boards;
		this.size = boards == null ? 0 : boards.size();
	}

	public int getGroupCount() {
		return size;
	}

	public int getChildrenCount(int groupPosition) {
		return boards.get(groupPosition).getSubBoards().size();
	}

	public Object getGroup(int groupPosition) {
		return boards.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return boards.get(groupPosition).getSubBoards().get(childPosition);
	}

	public long getGroupId(int groupPosition) {
		return boards.get(groupPosition).getFid();
	}

	public long getChildId(int groupPosition, int childPosition) {
		return boards.get(groupPosition).getSubBoards().get(childPosition).getFid();
	}

	public boolean hasStableIds() {
		return false;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = this.groupInflater.inflate(R.layout.listview_item_board, null);
			gvh = new GroupViewHolder();
			gvh.txt_title  = (TextView)convertView.findViewById(R.id.father_title);
			gvh.txt_selector = (TextView)convertView.findViewById(R.id.board_selector);
			convertView.setTag(gvh);
		}else{
			gvh = (GroupViewHolder)convertView.getTag();
		}

	    gvh.txt_title.setText(this.boards.get(groupPosition).getName());
	    if(isExpanded){
	    	gvh.txt_selector.setBackgroundResource(R.drawable.selector_2);
	    }else{
	    	gvh.txt_selector.setBackgroundResource(R.drawable.selector_1);
	    }
	    return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		 if (convertView == null){
			 convertView = this.childInflater.inflate(R.layout.listview_item_sub_board, null);
			 cvh = new ChildViewHolder();
			 cvh.txt_title = (TextView)convertView.findViewById(R.id.sub_title);
			 convertView.setTag(cvh);
		}else{
			cvh = (ChildViewHolder)convertView.getTag();
		}
		 cvh.txt_title.setText(this.boards.get(groupPosition).getSubBoards().get(childPosition).getName());
		 
		 return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}
	
	class GroupViewHolder{
		TextView txt_title;
		TextView txt_selector;
	}
	
	class ChildViewHolder{
		TextView txt_title;
	}
}
