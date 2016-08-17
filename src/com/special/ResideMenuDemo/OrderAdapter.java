package com.special.ResideMenuDemo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdapter extends BaseAdapter{
	@SuppressWarnings("unused")
	private Context context;
	private ArrayList<OrderInfo> orderInfo;
	private LayoutInflater inflater;
	private ViewHolder holder;

	public OrderAdapter(Context context,ArrayList<OrderInfo> musicInfoBeans) {
		this.context = context;
		this.orderInfo = musicInfoBeans;
		inflater = LayoutInflater.from(context);
	}	
	
	public int getCount() {
		return orderInfo.size();
	}
	public Object getItem(int position) {
		return null;
	}
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = inflater.inflate(com.special.ResideMenuDemo.R.layout.paimingadapter, null);
			holder = new ViewHolder();
			holder.courseNameView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.courseName);
			holder.ordinaryPointView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.ordinaryPoint);
			holder.paperPointView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.paperPoint);
			holder.finalScoreView=(TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.finalScore);
			holder.orderView=(TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.order);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		OrderInfo infoBean= orderInfo.get(position);	
		holder.courseNameView.setText(infoBean.getCourseName());
		holder.ordinaryPointView.setText(infoBean.getOrdinaryPoint());
		holder.paperPointView.setText(infoBean.getPaperPoint());
		holder.finalScoreView.setText(infoBean.getFinalScore());
		holder.orderView.setText(infoBean.getOrder());
		return convertView;
	}
	private final class ViewHolder{
		private TextView courseNameView;
		private TextView ordinaryPointView;
		private TextView paperPointView;
		private TextView finalScoreView;
		private TextView orderView;
	}

}
