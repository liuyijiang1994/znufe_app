package net.shopnc.android.ui.forum.topic;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.widget.MyProcessDialog;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 图片显示的Gallery界面
 * @author qjyong
 */
public class ShowImageActivity extends Activity {
	//private Gallery gallery_img;
	private MyProcessDialog dialog;
	private Gallery gallery_img_big;
	private String[] img_urls;
	private TextView txt_info;
	private int total;
	private MyApp myApp;
	@SuppressWarnings("unused")
	private int screenWidth;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplicationContext();
		screenWidth = myApp.getScreenWidth();
		setContentView(R.layout.img_activity);
			
		this.img_urls = getIntent().getExtras().getStringArray("img_urls");
		
		this.total = img_urls == null ? 0 : img_urls.length;
		
		this.gallery_img_big = ((Gallery) findViewById(R.id.gallery_img_big));
		this.gallery_img_big.setAnimationDuration(1000);
		this.txt_info = ((TextView) findViewById(R.id.txt_info));
		this.txt_info.setText(this.getString(R.string.topic_pic_info, 1, total));
		this.gallery_img_big.setAdapter(new BaseAdapter() {
			public int getCount() {
				return total;
			}

			public Object getItem(int paramInt) {
				return null;
			}

			public long getItemId(int paramInt) {
				return 0L;
			}

			public View getView(int paramInt, View paramView,
					ViewGroup paramViewGroup) {

				final ImageView localImageView = new ImageView(ShowImageActivity.this);
				localImageView.setImageResource(R.drawable.pic_bg);
				
				String url = img_urls[paramInt];
				
//				showDialog(Constants.DIALOG_LOCATION_ID);
//				dialog.setMsg(getString(R.string.search_wait));
				if(url != null){
					//加载远程图片
					ImageLoader.getInstance().asyncLoadBitmap(url, new ImageLoader.ImageCallback() {
						@Override
						public void imageLoaded(Bitmap bmp, String url) {
//							dismissDialog(Constants.DIALOG_LOCATION_ID);
//							dialog.cancel();
							if(bmp!=null){
								localImageView.setImageBitmap(bmp);
							}else{
								Drawable d=getResources().getDrawable(R.drawable.bg_topic_default);
								localImageView.setImageDrawable(d);
							}
						}
					});
					
					localImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					localImageView.setLayoutParams(new Gallery.LayoutParams(-1, -1));
				}
				txt_info.setText(ShowImageActivity.this.getString(R.string.topic_pic_info, paramInt + 1, total));
				return localImageView;
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(Constants.DIALOG_LOCATION_ID == id){
			return createProgressDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private MyProcessDialog createProgressDialog(){
		dialog = new MyProcessDialog(this);
		return dialog;
	}
}
