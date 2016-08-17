/**
 *  ClassName: PushData.java
 *  created on 2012-3-3
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 论坛推送数据实体类：主要是用在首页头条及焦点
 * @author qjyong
 */
public class PushData implements Serializable {
	
	private static final long serialVersionUID = -5305445268093247545L;
	/**ID(tid的值)*/
	private long id;
	/** 标题 */
	private String title;
	/** 配图的路径 */
	private String pic;
	/** 摘要 */
	private String summary;
	/** ID的类型 */
	private String idtype;
	
	
	/**
	 * 所有属性字段名
	 * @author qjyong
	 */
	public static final class Attr {
		/** ID */
		public static final String ID = "id";
		/**ID的类型 */
		public static final String IDTYPE = "idtype";
		/** 标题 */
		private static final String TITLE = "title";
		/** 摘要 */
		public static final String SUMMARY = "summary";
		/** 配图路径 */
		public static final String PIC = "pic";
		
	}
	
	public static synchronized ArrayList<PushData> newInstanceList(String jsonDatas){
		ArrayList<PushData> pushDatas = new ArrayList<PushData>();
		
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			for(int i = 0; i < size; i++){
				JSONObject obj = arr.getJSONObject(i);
				long id = obj.optLong(Attr.ID);
				String idtype = obj.optString(Attr.IDTYPE);
				String title = obj.optString(Attr.TITLE);
				String pic = obj.optString(Attr.PIC);
				String summary = obj.optString(Attr.SUMMARY);
				
				pushDatas.add(new PushData(id,title, pic, summary, idtype));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return pushDatas;
	}
	
	public static synchronized PushData newInstance(String jsonDatas){
		PushData pd = null;
		try {
			JSONArray arr = new JSONArray(jsonDatas);
			int size = null == arr ? 0 : arr.length();
			if(size > 0){
				JSONObject obj = arr.getJSONObject(0);
				long id = obj.optLong(Attr.ID);
				String idtype = obj.optString(Attr.IDTYPE);
				String title = obj.optString(Attr.TITLE);
				String pic = obj.optString(Attr.PIC);
				String summary = obj.optString(Attr.SUMMARY);
				
				pd = new PushData(id,title, pic, summary, idtype);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return pd;
	}
	
	public PushData() {}
	
	public PushData(long id,String title, String pic, String summary) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.summary = summary;
	}
	
	
	public PushData(long id,String title, String pic, String summary, String idtype) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.summary = summary;
		this.idtype = idtype;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	@Override
	public String toString() {
		return "PushData [id=" + id + ", title=" + title + ", pic=" + pic
				+ ", summary=" + summary + ", idtype=" + idtype + "]";
	}
}
