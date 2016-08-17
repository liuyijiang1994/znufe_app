/**
 *  ClassName: FacesGridViewAdapter.java
 *  created on 2012-3-7
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.adapter;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.model.Smiley;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 表情网格适配器
 * 
 * @author qjyong
 */
public class SmileyGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Smiley> faces = new ArrayList<Smiley>();

	public SmileyGridViewAdapter(Context ctx, ArrayList<Smiley> faces) {
		this.faces = faces;
		
		inflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		return this.faces.size();
	}

	public Smiley getItem(int paramInt) {
		return (Smiley) this.faces.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int position, View convertView, ViewGroup paramViewGroup) {
		if (convertView == null){
			convertView = inflater.inflate(R.layout.grid_item_face, null);
		}
		 ImageView localImageView = (ImageView) convertView.findViewById(R.id.iv);
		String localName = this.faces.get(position).getLocalName();
		//final String name = this.faces.get(position).getName();
		Bitmap pic = BitmapFactory.decodeFile(Constants.CACHE_DIR_SMILEY+"/"+localName);
		localImageView.setImageBitmap(pic);
		
		/*3
		convertView.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				Toast.makeText(FacesGridViewAdapter.this.context, "表情：" + name, 0)
						.show();
				return false;
			}
		});
		*/
		return convertView;
	}
}
