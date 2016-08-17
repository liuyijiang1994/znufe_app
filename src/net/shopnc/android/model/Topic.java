/**
 *  ClassName: Topic.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 板块下的帖子实体类
 * @author qjyong
 */
public class Topic implements Serializable {
	public static final String TOPIC_TAG = "topic";
	public static final class Attr {
		/** ID */
		public static final String TID = "tid";
		/** 所属版块ID */
		public static final String FID = "fid";
		/** 主题（标题） */
		public static final String SUBJECT = "subject";
		/** 发帖人ID */
		public static final String UNAME = "uname";
		/** 发贴人昵称 */
		public static final String AUTHOR = "author";
		
		public static final String AVATAR = "avatar_url";
		/** 摘要 */
		public static final String SUMMARY = "summary";
		/** 配图路径 */
		public static final String IMAGE = "image";
		/** 浏览数 */
		public static final String VIEWS = "views";
		/** 回复数 */
		public static final String REPLIES = "replies";
		/** 发表时间毫秒值 */
		public static final String DATALINE = "dateline";
		/** 帖子的正文内容 */
		public static final String MESSAGE = "message";
		/** 帖子中带的图片附件attachment */
		public static final String PIC_INFO = "attachment";
		/**帖子来自那个客户端 */
		public static final String STATUS = "status";
		/** 帖子pid */
		public static final String PID = "pid";
	}

	private static final long serialVersionUID = -5929666465360214813L;
	/** ID */
	private long tid;
	/** 所属板块ID */
	private long fid;
	/** 主题（标题） */
	private String subject;
	/** 发帖人ID */
	private String uname;
	/** 发帖人昵称 */
	private String author;
	/** 发帖人头像 */
	private String avatar;
	/** 摘要 */
	private String summary;
	/** 配图路径 */
	private String image;
	/** 浏览数 */
	private String views;
	/** 回复数 */
	private String replies;
	/** 发表时间的秒值 */
	private long dateline;
	/** 帖子的正文内容 */
	private String message;
	/**获取手机来自那*/
	private String status;
	
	private long pid;
	private String[] pic_info;
	public Topic() {
	}
	
	public static ArrayList<Topic> newInstanceList(String jsonDatas){
		ArrayList<Topic> pushDatas = new ArrayList<Topic>();
		
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				long tid = obj.optLong(Attr.TID);
				long fid = obj.optLong(Attr.FID);
				long pid = obj.optLong(Attr.PID);
				String subject = obj.optString(Attr.SUBJECT);
				String uname = obj.optString(Attr.UNAME);
				String author = obj.optString(Attr.AUTHOR);
				String image = obj.optString(Attr.IMAGE);
				String summary = obj.optString(Attr.SUMMARY);
				String views = obj.optString(Attr.VIEWS);
				String replies = obj.optString(Attr.REPLIES);
				long dateline = obj.optLong(Attr.DATALINE);
				String message = obj.optString(Attr.MESSAGE);
				String avatar = obj.optString(Attr.AVATAR);
				String pics = obj.optString(Attr.PIC_INFO);
				String[] pic_info = null;
				if(null != pics && !"".equals(pics) && !"null".equalsIgnoreCase(pics)){
					pic_info = pics.split(",");
				}
				String status=obj.optString(Attr.STATUS);
				pushDatas.add(new Topic(tid, fid,pid, subject, uname, author, 
						summary, image, views, replies, dateline, message, avatar, pic_info,status));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pushDatas;
	}

	public Topic(long tid, String subject, String summary, String image) {
		this.tid = tid;
		this.subject = subject;
		this.summary = summary;
		this.image = image;
	}

	public Topic(long tid, String subject, String author, String views,
			String replies, long dateline) {
		this.tid = tid;
		this.subject = subject;
		this.author = author;
		this.views = views;
		this.replies = replies;
		this.dateline = dateline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Topic(long tid, long fid, long pid,String subject,String uname, String author,
			String summary, String image, String views, String replies,
			long dateline, String message, String avatar, String [] pic_info,String status) {
		this.tid = tid;
		this.fid = fid;
		this.pid=pid;
		this.subject = subject;
		this.uname = uname;
		this.author = author;
		this.summary = summary;
		this.image = image;
		this.views = views;
		this.replies = replies;
		this.dateline = dateline;
		this.message = message;
		this.avatar = avatar;
		this.pic_info = pic_info;
		this.status=status;
	}
	

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	/**
	 * 时间秒值
	 * @return
	 */
	public long getDateline() {
		return dateline;
	}

	public void setDateline(long dateline) {
		this.dateline = dateline;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String[] getPic_info() {
		return pic_info;
	}
	public void setPic_info(String[] pic_info) {
		this.pic_info = pic_info;
	}

	@Override
	public String toString() {
		return "Topic [tid=" + tid + ", fid=" + fid + ", subject=" + subject
				+ ", uname=" + uname + ", author=" + author + ", avatar="
				+ avatar + ", summary=" + summary + ", image=" + image
				+ ", views=" + views + ", replies=" + replies + ", dateline="
				+ dateline + ", message=" + message + ", pid=" + pid
				+ ", pic_info=" + Arrays.toString(pic_info) + "]";
	}

}
