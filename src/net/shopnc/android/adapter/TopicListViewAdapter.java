/**
 *  ClassName: TopicListViewAdapter.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.DateAndTimeHepler;
import net.shopnc.android.model.Topic;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 帖子列表适配器
 * 
 * @author qjyong
 */
public class TopicListViewAdapter extends BaseAdapter {
	/** 存放选项数据的集合 */
	private ArrayList<Topic> datas;
	/** 布局加载器 */
	private LayoutInflater inflater;
	private ViewHolder vh;
	/** 上下文 */
	private Context ctx;

	/**
	 * 构造方法
	 * 
	 * @param ctx
	 */
	public TopicListViewAdapter(Context ctx) {
		this.ctx = ctx;
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
		return datas.get(index).getTid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_topics, null);
			vh = new ViewHolder();
			vh.txt_id = (TextView) convertView.findViewById(R.id.topic_id);
			vh.txt_title = (TextView) convertView.findViewById(R.id.topic_title);
			vh.txt_author = (TextView) convertView.findViewById(R.id.topic_author);
			vh.txt_visit_replies = (TextView) convertView.findViewById(R.id.topic_visits_replies);
			vh.txt_pubtime = (TextView) convertView.findViewById(R.id.topic_pubtime);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		Topic topic = datas.get(position);
		vh.txt_id.setText(String.valueOf(topic.getTid()));
		vh.txt_title.setText(topic.getSubject());
		vh.txt_author.setText(topic.getAuthor());
		if(null == topic.getViews() || "".equals(topic.getViews())){
			vh.txt_visit_replies.setText("");
		}else{
			vh.txt_visit_replies.setText(ctx.getString(R.string.topic_visits_replies,
					topic.getReplies(), topic.getViews()));
		}
		if(topic.getDateline() > 0L){
			vh.txt_pubtime.setText(DateAndTimeHepler.friendly_time(ctx, topic.getDateline() * 1000));
		}else{
			vh.txt_pubtime.setText("");
		}
		return convertView;
	}

	public void setDatas(ArrayList<Topic> datas) {
		this.datas = datas;
	}
	
	class ViewHolder {
		TextView txt_id;
		TextView txt_title;
		TextView txt_author;
		TextView txt_visit_replies;
		TextView txt_pubtime;
	}
}
