/**
 *  ClassName: User.java
 *  created on 2012-3-10
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 论坛用户
 * @author qjyong
 */
public class User {
	public static class Attr{
		public static final String AUTHOR_ID = "uid";
		public static final String SESSION_ID = "sessionid";
		public static final String GROUP_ID = "groupid";
		public static final String AUTHOR = "author";
		public static final String PWD = "pwd";
	}
	
	private String authorid;
	private String author;
	private String pwd;
	private String sessionid;
	private String groupid;
	
	public static User newInstance(String json){
		User user = null;
		try {
			JSONArray array = new JSONArray(json);
			if(array.length() > 0){
				JSONObject obj2 = array.getJSONObject(0);
				user = new User();
					user.setSessionid(obj2.optString(Attr.SESSION_ID));
					user.setAuthorid(obj2.optString(Attr.AUTHOR_ID));
					user.setGroupid(obj2.optString(Attr.GROUP_ID));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	@Override
	public String toString() {
		return "User [authorid=" + authorid + ", author=" + author + ", pwd="
				+ pwd + ", sessionid=" + sessionid + ", groupid=" + groupid
				+ "]";
	}
}
