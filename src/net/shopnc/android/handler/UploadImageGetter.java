/**
 *  ClassName: UploadImageGetter.java
 *  created on 2012-3-15
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.handler;

import net.shopnc.android.common.Constants;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

/**
 * @author qjyong
 */
public class UploadImageGetter implements ImageGetter{  
    @Override  
    public Drawable getDrawable(String source){
        Drawable d = Drawable.createFromPath(Constants.CACHE_DIR_UPLOADING_IMG + "/" + source);
        if(d != null){
        	d.setBounds(0, 0, 240,	240);
        }
        return d;
    }  
}
