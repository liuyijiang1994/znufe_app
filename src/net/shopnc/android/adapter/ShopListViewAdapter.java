/**
 *  ClassName: HeadlinesListViewAdapter.java
 *  created on 2012-3-2
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.model.Shop;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商家列表ListView适配器
 * @author qjyong
 */
public class ShopListViewAdapter extends BaseAdapter {
	private ArrayList<Shop> datas = new ArrayList<Shop>();
	private LayoutInflater inflater;
	private ViewHolder vh;
	
	private Context ctx;
	private MyApp myApp;
	private int screenWidth;
	private boolean img_invisible;
	private NumberFormat nf;
	
	
	/**
	 * 构造方法
	 * @param ctx
	 */
	@SuppressWarnings("static-access")
	public ShopListViewAdapter(Context ctx){
		this.ctx = ctx;
		
		inflater = LayoutInflater.from(ctx);
		myApp = (MyApp)ctx.getApplicationContext();
		
		nf = new DecimalFormat("#,##0.0km");
		
		screenWidth = myApp.getScreenWidth();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	public Object getItem(int index) {
		return datas.get(index);
	}

	public long getItemId(int index) {
		return datas.get(index).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_item_shops, null);
			vh = new ViewHolder();
			vh.txt_id = (TextView)convertView.findViewById(R.id.shop_id);
			vh.txt_pic = (ImageView)convertView.findViewById(R.id.shop_pic);
			vh.txt_name = (TextView)convertView.findViewById(R.id.shop_name);
			vh.txt_phone = (TextView)convertView.findViewById(R.id.shop_phone);
			vh.txt_distance = (TextView)convertView.findViewById(R.id.shop_distance);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		img_invisible = myApp.isImg_invisible();
		
		Shop shop = datas.get(position);
		//vh.txt_id.setText(shop.getId());
		vh.txt_name.setText(shop.getName());
		vh.txt_phone.setText("电话：" + shop.getPhone());
		vh.txt_distance.setText(nf.format(shop.getDistance()/1000));
		
		if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(ctx) || !img_invisible){
			if(null != shop.getPic() && !"".equals(shop.getPic())){
				// 配图异步下载
				vh.txt_pic.setTag(shop.getPic());
			
				async(vh.txt_pic);
			}
		}else{
			vh.txt_pic.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	public void setDatas(ArrayList<Shop> datas) {
		this.datas = datas;
	}

	private void async(final ImageView mv){
		ImageLoader.getInstance().asyncLoadBitmap((String)mv.getTag(), screenWidth / 4, new ImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap bmp, String url) {
				/*ImageView temp = (ImageView)lv.findViewWithTag(url);
				if(null != temp && bmp != null){
					temp.setImageBitmap(bmp);
				}*/
				if(null != bmp){
					mv.setImageBitmap(bmp);
				}
			}
		});
	}
	
	class ViewHolder{
		TextView txt_id;
		ImageView txt_pic;
		TextView txt_name;
		TextView txt_phone; 
		TextView txt_distance;
	}
}
