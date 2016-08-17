package net.shopnc.android.handler;

import net.shopnc.android.common.Constants;
import net.shopnc.android.common.IOHelper;
import net.shopnc.android.common.ImageZoom;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.ImageLoader.ImageCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

/**
 * 本地缓存图片获取器
 * @author hjgang
 */
public class SmileyImageGetter implements ImageGetter{ 
	private BitmapDrawable bd=null;
	@SuppressWarnings("unused")
	private Context c;
	public SmileyImageGetter(Context c){
		this.c=c;
	}
	@Override  
    public Drawable getDrawable(String source){ 
    	final int w=MyApp.getScreenWidth();
    	final int h=MyApp.getScreenHeight();
        String smiley = IOHelper.getName(source);
        Drawable d = Drawable.createFromPath(Constants.CACHE_DIR_SMILEY + "/" + smiley);
        if(d != null){
        	d.setBounds(0, 0, 35, 35);
        	return d;
        }else{
        	int idx1 = source.indexOf("<");
			int idx2 = source.indexOf(">");
			if (idx1 > -1 && idx2 > -1 && idx1 < idx2) {
				source = source.substring(0, idx1);
			}	
//			Bitmap b=BitmapFactory.decodeResource(c.getApplicationContext().getResources()
//					, R.drawable.bg_topic_default);
//			bd=new BitmapDrawable(b);
//			ImageZoom iz = new ImageZoom();
//    		int dw=b.getWidth();
//        	int dh=b.getHeight();
//            iz.zoomImage(dw-20, dh-20, w, h);
//        	bd.setBounds(0, 0,iz.getWidth(),iz.getHeight());
			//配图异步下载
			ImageLoader.getInstance().asyncLoadBitmap(source,new ImageCallback() {
				@Override
				public void imageLoaded(Bitmap bmp, String url) {
					if(bmp!=null && !"".equals(bmp)){
						bd = new BitmapDrawable(bmp);
		        		ImageZoom iz = new ImageZoom();
		        		int dw=bmp.getWidth();
		            	int dh=bmp.getHeight();
		                iz.zoomImage(dw-20, dh-20, w, h);
		            	bd.setBounds(0, 0,iz.getWidth(),iz.getHeight());
		        	}else{
	        		bd.setBounds(0,0,0,0);
		        	}  
				}
			});
        	return bd;
        }
    }  
}
