/**
 *  ClassName: ImageLoader.java
 *  created on 2012-3-10
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.handler;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.HttpHelper;
import net.shopnc.android.common.ImageHelper;
import net.shopnc.android.common.MD5Encoder;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author qjyong
 */
public class ImageLoader {
	public static final String TAG = "ImageLoader";
	
	private final static ImageLoader imageLoader = new ImageLoader();
	
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> imageCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6,30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	private ImageLoader(){}
	
	public static ImageLoader getInstance(){
		return imageLoader;
	}
	
	public interface ImageCallback {
		/** 图片下载完成的回调方法 */
		public void imageLoaded(Bitmap bmp, String url);
	}
	
	public void downloadBitmap(final String url){
		String destFileName = MD5Encoder.encode(url);
		File dest = new File(Constants.CACHE_DIR_IMAGE, destFileName);
		if(!dest.exists()){
			try {
				HttpHelper.download(url, dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 异步加载指定url处的图片设置到imageView中<br/>
	 * 注意：以原宽度和目标宽度的缩放比对原图片进行了缩放。
	 * @param url
	 * @param width
	 * @param imageView
	 */
	public void asyncLoadBitmap(final String url, final int width, final ImageCallback callback) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (null != bitmap) { //内存
        	callback.imageLoaded(bitmap, url);
        } else if((bitmap = getBitmapFromDisk(url)) != null){ //磁盘
        	callback.imageLoaded(bitmap, url);
        }else {
        	Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg) {
    				if(HttpStatus.SC_OK == msg.what){
    					Bitmap bitmap = (Bitmap)msg.obj;
    					callback.imageLoaded(bitmap, url);
    				}
    			}
    		};
    		threadPool.execute(new LoadBitmapRunnable(url, width, handler));
        }
    }
	/**
	 * 异步加载指定url处的图片设置到imageView中<br/>
	 * @param url
	 * @param imageView
	 */
	public void asyncLoadBitmap(final String url, final ImageCallback callback) {
		Bitmap bitmap = getBitmapFromCache(url);
        if (null != bitmap) { //内存
        	callback.imageLoaded(bitmap, url);
        } else if((bitmap = getBitmapFromDisk(url)) != null){ //磁盘
        	callback.imageLoaded(bitmap, url);
        } else {
    		Handler handler = new Handler(){
    			@Override
    			public void handleMessage(Message msg) {
    				if(HttpStatus.SC_OK == msg.what){
    					Bitmap bitmap = (Bitmap)msg.obj;
    					callback.imageLoaded(bitmap, url);
    				}else{
    					callback.imageLoaded(null,url);
    				}
    			}
    		};
    		threadPool.execute(new LoadBitmapRunnable(url,-1, handler));
        }
    }
	/**
	 * @author hjgang
	 * */
	public void asyncLoadBitmap2(final String url, final int width, final Context context,final ImageCallback callback) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (null != bitmap) { //内存
        	callback.imageLoaded(bitmap, url);
        } else if((bitmap = getBitmapFromDisk(url)) != null){ //磁盘
        	callback.imageLoaded(bitmap, url);
        }else {

        	  Bitmap  bitmaptemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.bg_big_defalut);
        	  callback.imageLoaded(bitmaptemp, url);
        }
    }
	/**
	 * @author hjgang
	 * */
	public void asyncLoadBitmap3(final String url, final int width, final Context context,final ImageCallback callback) {
        	  Bitmap  bitmaptemp = BitmapFactory.decodeResource(context.getResources(),R.drawable.bg_big_defalut);
        	  callback.imageLoaded(bitmaptemp, url);
    }

	
	
	private static Bitmap getBitmapFromCache(String url) {
        SoftReference<Bitmap> bitmapReference = imageCache.get(url);
        if (bitmapReference != null && bitmapReference.get() != null){
        	Log.d(TAG, "in cache-->" + url);
            return bitmapReference.get();
        } else { //其bitmap对象已被回收释放，需要重新加载
        	imageCache.remove(url);
        }
        return null;
    }
	
	public static Bitmap getBitmapFromDisk(String url){
		Bitmap bmp = null;
		
		//String name = IOHelper.getName(url);
		//String ext = IOHelper.getExtension(name);
		String destFileName = MD5Encoder.encode(url);
		
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File dest = new File(Constants.CACHE_DIR_IMAGE, destFileName);
			if(dest.exists()){
				try {
					bmp = ImageHelper.loadFromFile(dest);
					Log.d(TAG, "in SD-->" + url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bmp;
	}
	
	private class LoadBitmapRunnable implements Runnable{
		private String url;
		private Handler handler;
		private int width;
		
		public LoadBitmapRunnable(final String url, final int width, final Handler handler){
			this.url = url;
			this.handler = handler;
			this.width = width;
		}
		@Override
		public void run() {
			Message msg = handler.obtainMessage(HttpStatus.SC_OK);
			Bitmap bmp = null; 
			try {
				/*
				if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
					//String name = IOHelper.getName(url);
					//String ext = IOHelper.getExtension(name);
					String destFileName = MD5Encoder.encode(url);
					
					File dest = new File(Constants.CACHE_DIR_IMAGE, destFileName);
					if(!dest.exists()){
						try {
							HttpHelper.download(url, dest);
							
							bmp = ImageHelper.loadFromFile(dest);
							
							imageCache.put(url, new SoftReference<Bitmap>(bmp)); //放入内存缓存
							msg.obj = bmp;
							handler.sendMessage(msg);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else{*/
					bmp = HttpHelper.downloadBitmap(url);
					if(null != bmp){
						if(width > 0){
							bmp = ImageHelper.zoom(bmp, width);
						}
						imageCache.put(url, new SoftReference<Bitmap>(bmp)); //放入内存缓存
						msg.obj = bmp;
						handler.sendMessage(msg);
						
						//写磁盘
						String destFileName = MD5Encoder.encode(url);
						File dest = new File(Constants.CACHE_DIR_IMAGE, destFileName);
						if(!dest.exists()){
							ImageHelper.write(bmp, dest);
						}
					}
			} catch (IOException e) {
				msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
				e.printStackTrace();
			}
			
//			if(null != bmp){
//				Log.d(TAG, "bmp.width=" + bmp.getWidth() +",bmp.height=" + bmp.getHeight() + "-->" + url);
//			}else{
//				Log.d(TAG, "Bitmap is null -->" + url);
//			}
		}
	} 
}
