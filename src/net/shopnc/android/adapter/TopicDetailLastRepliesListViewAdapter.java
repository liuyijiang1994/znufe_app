/**
 *  ClassName: TopicDetailListViewAdapter.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.BBCodeHelper;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.DateAndTimeHepler;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.handler.SmileyImageGetter;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.forum.topic.QuoteTopicActivity;
import net.shopnc.android.ui.forum.topic.ShowImageActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 帖子详细最后回复列表的适配器
 * 
 * @author qjyong
 */
public class TopicDetailLastRepliesListViewAdapter extends BaseAdapter {
	private static final String TAG = "TopicDetailLastRepliesListViewAdapter"; 
	/** 存放选项数据的集合 */
	private ArrayList<Topic> datas;
	/** 布局加载器 */
	private LayoutInflater inflater;
	private ViewHolder vh;
	/** 上下文 */
	private Context ctx;
	
	private MyApp myApp;
	private boolean img_invisible;

	private long count = 0;
	private long pageno = 1;
	private int pagesize = 20;
	
	private String mage;
	
	
	private String WAY="static/image/";//引用回复图片的匹配路径
	private long tid;//记录tid
	
	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}
	
	public TopicDetailLastRepliesListViewAdapter(Context ctx) {
		inflater = LayoutInflater.from(ctx);
		this.ctx = ctx;
		myApp = (MyApp)ctx.getApplicationContext();
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
			convertView = inflater.inflate(R.layout.listview_item_topic, null);
			vh = new ViewHolder();
			vh.txt_id = (TextView) convertView.findViewById(R.id.topic_id);
			vh.txt_floor = (TextView) convertView.findViewById(R.id.author_floor);
			vh.txt_author = (TextView) convertView.findViewById(R.id.author_id);
			vh.txt_visit_replies = (TextView) convertView.findViewById(R.id.view_replies);
			vh.txt_pubtime = (TextView) convertView.findViewById(R.id.dateline);
			vh.txt_content = (TextView)convertView.findViewById(R.id.topic_content);
			vh.btn_view_image = (Button) convertView.findViewById(R.id.btn_view_image);
			vh.avatar = (ImageView)convertView.findViewById(R.id.author_avatar);
			vh.txt_from=(TextView)convertView.findViewById(R.id.topic_from);
			vh.view_huifu=(TextView)convertView.findViewById(R.id.view_huifu);
			vh.topic_quote=(TextView)convertView.findViewById(R.id.topic_quote);
			
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
			convertView.setVisibility(View.VISIBLE);
		}
		
		final Topic topic = datas.get(position);
		
		long max = 0;
		if(pageno == 1){
			max = count;
		}else{
			max = count - (pageno - 1) * pagesize;
		}
		long floor = max - position;
		
		if(floor > 3){
			vh.txt_floor.setText(ctx.getString(R.string.topic_display_floor_num, floor));
		}else if(floor == 3){
			vh.txt_floor.setText(ctx.getString(R.string.topic_display_floor_3));
		}else if(floor == 2){
			vh.txt_floor.setText(ctx.getString(R.string.topic_display_floor_2));
		}else if(floor == 1){
			vh.txt_floor.setText(ctx.getString(R.string.topic_display_floor_1));
		}
		
		//需要把帖子内容中的code转换成html标签，再用HTML进行格式化显示
		//String str = BBCodeHelper.processBBCode(topic.getMessage());
		String str=topic.getMessage();
		//判断帖子来源：iPhone Android
		String str3=new String();
		String yinyong="[/quote]";
		//static/image/
		int c=topic.getMessage().lastIndexOf(yinyong);
		int d=topic.getMessage().lastIndexOf("本帖最后由");
		int e=topic.getMessage().lastIndexOf("编辑");
		if(c!=-1){
			vh.view_huifu.setVisibility(View.VISIBLE);
			str3=topic.getMessage().substring(0,c+8);
			str=str.replace(str3, "");
			str3=str3.replace("[img]"+WAY+Constants.HUIFU+"[/img]","<br/>\t");
			str3=str3.replace(WAY+Constants.HUIFU,"<br/>\t");
			if(d!=-1 && e!=-1){
				str3=str3.replaceFirst(str3.substring(d, e+"编辑".length()),"");
			}
			vh.view_huifu.setText(Html.fromHtml("\t"+BBCodeHelper.processBBCode(str3), new SmileyImageGetter(ctx), null));
		}else{
			vh.view_huifu.setVisibility(View.GONE);
		}
		/**判断来自那个客户端*/
		if(topic.getStatus()!=null&&topic.getStatus().length()>5 && !"".equals(topic.getStatus())){
			vh.txt_from.setVisibility(View.VISIBLE);
			vh.txt_from.setText(topic.getStatus());
		}else{
			vh.txt_from.setVisibility(View.GONE);
		}
		str=BBCodeHelper.processBBCode(str);
		img_invisible = myApp.isImg_invisible();
		if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(ctx) || !img_invisible){
		//显示
		}else{
			str=BBCodeHelper.parseHtmlExcludeImgTag(str);
		}
		vh.txt_content.setText(Html.fromHtml(str, new SmileyImageGetter(ctx), null));
				if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
						&& null != myApp.getSid() && !"".equals(myApp.getSid())){
					if(topic.getSubject()!=null && !"".equals(topic.getSubject())){
						vh.topic_quote.setVisibility(View.GONE);
					}else{
						vh.topic_quote.setVisibility(View.VISIBLE);
						vh.topic_quote.setText(Html.fromHtml("<a href=''>引用回复</a>"));
						vh.topic_quote.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								//QIUJY 回帖权限判断
								HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
								Log.d(TAG, subBoardMap.toString());
								if(subBoardMap != null){
									Board b2 = subBoardMap.get(Long.valueOf(topic.getFid()));
									Log.d(TAG, b2.toString());
									if(b2.getIsreply() == 1){
										Intent it = new Intent(ctx, QuoteTopicActivity.class);
										it.putExtra("author", topic.getAuthor());
										String str="[/quote]";
										int a=topic.getMessage().lastIndexOf(str);
										if(a!=-1){
											mage=topic.getMessage().replace(topic.getMessage().substring(0,a), "");
											mage=mage.replace("[/quote]", "");
										}else{
											mage=topic.getMessage();
										}
										int b=mage.lastIndexOf("[img");
										int c=mage.lastIndexOf("[/img]");
										if(b!=-1&&c!=-1){
											mage=mage.replace(mage.substring(b,c+6), "");
										}
										it.putExtra("mage",mage);
										it.putExtra("tid", String.valueOf(getTid()));
										it.putExtra("fid",  String.valueOf(topic.getFid()));
										it.putExtra("pid", String.valueOf(topic.getPid()));
										it.putExtra("date",String.valueOf(topic.getDateline()));
										it.putExtra("ispostimage",String.valueOf(b2.getIspostimage()));
										((Activity) ctx).startActivityForResult(it, 300);
									}else{
										Toast.makeText(ctx, "你没有权限在本版回帖！", Toast.LENGTH_SHORT).show();
									}
							}
						}});
						}
				}else{
					vh.topic_quote.setVisibility(View.GONE);
				}
		if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(ctx) || !img_invisible){
			Log.d("avatar-->", topic.getAvatar());
			if(!"".equals(topic.getAvatar())){
				vh.avatar.setTag(topic.getAvatar());
				final ImageView temp = vh.avatar;
				//配图异步下载
				ImageLoader.getInstance().asyncLoadBitmap(topic.getAvatar(), new ImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Bitmap bmp, String url) {
						/*
						ImageView temp = (ImageView)lv.findViewWithTag(url);
						if(null != temp && bmp != null){
							temp.setImageBitmap(bmp);
						}
						*/
						temp.setImageBitmap(bmp);
					}
				});
			}
			/////////////////////////////是否带图片////////////////////////////
			final String [] img_urls = topic.getPic_info();
			vh.btn_view_image.setVisibility(View.GONE);
			if(null !=  img_urls && img_urls.length > 0){
				vh.btn_view_image.setText(ctx.getString(R.string.topic_detail_show_pic, img_urls.length));
				vh.btn_view_image.setVisibility(View.VISIBLE);
				vh.btn_view_image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//启动图片显示的界面
					Intent intent = new Intent(ctx, ShowImageActivity.class);
					intent.putExtra("img_urls", img_urls);
					
					ctx.startActivity(intent);
				}
				});
			}
		}else{
			vh.avatar.setVisibility(View.GONE);
			vh.btn_view_image.setVisibility(View.GONE);
		}
		
		vh.txt_id.setText(String.valueOf(topic.getTid()));
		
		
		
		vh.txt_author.setText(topic.getAuthor());
		vh.txt_visit_replies.setText(ctx.getString(R.string.topic_visits_replies,
				topic.getReplies(), topic.getViews()));
		vh.txt_visit_replies.setVisibility(View.GONE);
		
		vh.txt_pubtime.setText(DateAndTimeHepler.friendly_time(ctx,
				topic.getDateline() * 1000));
		
		return convertView;
	}

	public ArrayList<Topic> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<Topic> datas) {
		this.datas = datas;
	}
	
	public long getPageno() {
		return pageno;
	}

	public void setPageno(long pageno) {
		this.pageno = pageno;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setCount(long count) {
		this.count = count;
	}

	class ViewHolder {
		ImageView avatar;
		TextView txt_id;
		TextView txt_floor;
		TextView txt_author;
		TextView txt_visit_replies;

		TextView txt_pubtime;
		TextView txt_content;
		TextView txt_from;
		TextView topic_quote;
		TextView view_huifu;
		Button btn_view_image;
	}
}
