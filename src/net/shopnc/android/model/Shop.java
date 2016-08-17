/**
 *  ClassName: Shop.java
 *  created on 2012-3-17
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商家
 * @author qjyong
 */
public class Shop {
	
	public static class Attr{
		public static final String ID = "shop_id";
		public static final String NAME = "shop_name";
		/** 配图路径 */
		public static final String PIC = "shop_pic";
		public static final String INFO = "shop_info";
		public static final String WEBSITE = "shop_website";
		public static final String ADDRESS = "shop_address";
		public static final String PHONE = "shop_phone";
		/** 纬度 */
		public static final String LATITUDE = "shop_lat";
		/** 经度 */
		public static final String LONGITUDE = "shop_lng";
		/** 距离 */
		public static final String DISTANCE = "distance";
	}
	private int id;
	private String name;
	private String pic;
	private String info;
	private String website;
	private String phone;
	private String address;
	private double latitude;
	private double longitude;
	private double distance;

	public static ArrayList<Shop> newStanceList(String json){
		ArrayList<Shop> shops = new ArrayList<Shop>();
		
		try {
			JSONArray arr = new JSONArray(json);
			
			int length = arr == null ? 0 : arr.length();
			for(int i = 0; i < length; i++){
				Shop shop = new Shop();
				JSONObject obj = arr.getJSONObject(i);
				shop.setId(obj.optInt(Attr.ID));
				shop.setName(obj.optString(Attr.NAME));
				shop.setPic(obj.optString(Attr.PIC));
				shop.setInfo(obj.optString(Attr.INFO));
				shop.setWebsite(obj.optString(Attr.WEBSITE));
				shop.setAddress(obj.optString(Attr.ADDRESS));
				shop.setPhone(obj.optString(Attr.PHONE));
				shop.setLatitude(obj.getDouble(Attr.LATITUDE));
				shop.setLongitude(obj.getDouble(Attr.LONGITUDE));
				shop.setDistance(obj.getDouble(Attr.DISTANCE));
				
				shops.add(shop);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return shops;
	}
	
	public Shop(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Shop [id=" + id + ", name=" + name + ", pic=" + pic + ", info="
				+ info + ", website=" + website + ", phone=" + phone
				+ ", address=" + address + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", distance=" + distance + "]";
	}
	
}
