package com.special.ResideMenuDemo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class FreeClassAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<FreeClassInfo> freeClassInfo;
	private LayoutInflater inflater;
	private ViewHolder holder;
	public FreeClassAdapter(Context context,ArrayList<FreeClassInfo> classInfo){
		this.context = context;
		this.freeClassInfo = classInfo;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return freeClassInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.freeclassadapter, null);
			holder=new ViewHolder();

			holder.t1View = (TextView) convertView.findViewById(R.id.t1);


			holder.t2View = (TextView) convertView.findViewById(R.id.t2);
			holder.t3View = (TextView) convertView.findViewById(R.id.t3);
			holder.t4View = (TextView) convertView.findViewById(R.id.t4);
			holder.t5View = (TextView) convertView.findViewById(R.id.t5);
			holder.t6View = (TextView) convertView.findViewById(R.id.t6);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		FreeClassInfo infoBean= freeClassInfo.get(position);
		System.out.println("已到60行"+infoBean.getT1());

		holder.t1View.setText(infoBean.getT1());
		if(infoBean.getT2()=="空")		
			holder.t2View.setBackgroundResource(R.drawable.empty);
		else
			holder.t2View.setBackgroundResource(R.drawable.busy);
		//holder.t2View.setText(infoBean.getT2());
		if(infoBean.getT3()=="空")		
			holder.t3View.setBackgroundResource(R.drawable.empty);
		else
			holder.t3View.setBackgroundResource(R.drawable.busy);
		//holder.t3View.setText(infoBean.getT3());
		if(infoBean.getT4()=="空")		
			holder.t4View.setBackgroundResource(R.drawable.empty);
		else
			holder.t4View.setBackgroundResource(R.drawable.busy);
		//holder.t4View.setText(infoBean.getT4());
		if(infoBean.getT5()=="空")		
			holder.t5View.setBackgroundResource(R.drawable.empty);
		else
			holder.t5View.setBackgroundResource(R.drawable.busy);
		//holder.t5View.setText(infoBean.getT5());
		if(infoBean.getT6()=="空")		
			holder.t6View.setBackgroundResource(R.drawable.empty);
		else
			holder.t6View.setBackgroundResource(R.drawable.busy);
		//holder.t6View.setText(infoBean.getT6());
		System.out.println("已到69行");
		return convertView;
	}
	private final  class ViewHolder{
		private TextView t1View;
		private TextView t2View;
		private TextView t3View;
		private TextView t4View;
		private TextView t5View;
		private TextView t6View;
	}
}
