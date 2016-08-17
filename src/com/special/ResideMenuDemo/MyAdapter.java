package com.special.ResideMenuDemo;

import java.util.ArrayList;

import com.special.ResideMenuDemo.GradeInfo;
import com.special.ResideMenuDemo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<GradeInfo> gradeInfo;
	private LayoutInflater inflater;
	private ViewHolder holder;

	public MyAdapter(Context context,ArrayList<GradeInfo> musicInfoBeans) {
		this.context = context;
		this.gradeInfo = musicInfoBeans;
		inflater = LayoutInflater.from(context);
	}	
	
	public int getCount() {
		return gradeInfo.size();
	}
	public Object getItem(int position) {
		return null;
	}
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = inflater.inflate(com.special.ResideMenuDemo.R.layout.evalute_adapter, null);
			holder = new ViewHolder();
			
			holder.courseNameView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.courseName);
			holder.ordinaryPointView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.ordinaryPoint);
			holder.paperPointView = (TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.paperPoint);
			holder.finalScoreView=(TextView) convertView.findViewById(com.special.ResideMenuDemo.R.id.finalScore);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		GradeInfo infoBean= gradeInfo.get(position);
		
		holder.courseNameView.setText(infoBean.getCourseName());
		holder.ordinaryPointView.setText(infoBean.getOrdinaryPoint());
		holder.paperPointView.setText(infoBean.getPaperPoint());
		holder.finalScoreView.setText(infoBean.getFinalScore());
		return convertView;
	}
	private final class ViewHolder{
		private TextView courseNameView;
		private TextView ordinaryPointView;
		private TextView paperPointView;
		private TextView finalScoreView;
	}

}
