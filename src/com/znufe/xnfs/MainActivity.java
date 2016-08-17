package com.znufe.xnfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.special.ResideMenuDemo.R;


import android.R.layout;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Context context = null;
	LocalActivityManager manager = null;
	ViewPager pager = null;
	TabHost tabHost = null;
	Button btn1,btn2,btn3;
	
	private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView cursor;// 动画图片


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmain);
		
		context = MainActivity.this;
        manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);

//        //屏幕适配开始
//       RelativeLayout layout = new RelativeLayout(this);
//        
//       int screenWidth = getScreenWidth(this);
//       int screenHeight = getScreenHeight(this);
//        
//       ImageView iv = new ImageView(this);
//       //指定50%的宽
//       LayoutParams params1 = new LayoutParams((int)(screenWidth * 0.5));
//       //设置id属性，后面相对布局时需要用到
//       iv.setId(0x7f0d0032);
//       iv.setBackgroundColor(0xff123456);
//       
//       layout.addView(iv, params1);
//       this.setContentView(layout);
//       //以上为屏幕适配代码
//       
        InitImageView();
        initButton();
        initPagerViewer();

	}
	/**
     * 初始化标题
     */
    private void initButton() {
        btn1 = (Button) findViewById(R.id.tab1);
        btn2 = (Button) findViewById(R.id.tab2);
        btn3 = (Button) findViewById(R.id.tab3);

        btn1.setOnClickListener(new MyOnClickListener(0));
        btn2.setOnClickListener(new MyOnClickListener(1));
        btn3.setOnClickListener(new MyOnClickListener(2));
        
    } 
    /**
     * 初始化PageViewer
     */
    private void initPagerViewer() {
        pager = (ViewPager) findViewById(R.id.viewpage);
        final ArrayList<View> list = new ArrayList<View>();
        Intent intent = new Intent(context, Tab1.class);
        list.add(getView("A", intent));
        Intent intent2 = new Intent(context, Tab2.class);
        list.add(getView("B", intent2));
        Intent intent3 = new Intent(context, Tab3.class);
        list.add(getView("C", intent3));

        pager.setAdapter(new MyPagerAdapter(list));
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
        .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	  /**
     * 通过activity获取视图
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }

    /**
     * Pager适配器
     */
    public class MyPagerAdapter extends PagerAdapter{
        List<View> list =  new ArrayList<View>();
        public MyPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                Object object) {
            ViewPager pViewPager = ((ViewPager) container);
            pViewPager.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ViewPager pViewPager = ((ViewPager) arg0);
            pViewPager.addView(list.get(arg1));
            return list.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);    
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            
        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            pager.setCurrentItem(index);
        }
    };
    private static Boolean isQuit = false;  
    private long mExitTime = 0;  
  
    Timer timer = new Timer(); 
    
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		
	if (keyCode == KeyEvent.KEYCODE_BACK) {  
		  
	   if ((System.currentTimeMillis() - mExitTime) > 2000) {
	       // 如果两次按键时间间隔大于2000毫秒，则不退出  
		   Toast.makeText(this, "再按一次 退出程序", Toast.LENGTH_SHORT).show();  
	       mExitTime = System.currentTimeMillis();// 更新mExitTime  
	   	} else {  
	       System.exit(0);// 否则退出程序  
	   	}  
	   		return true;  
		}  
		return super.onKeyDown(keyCode, event);  
	
	}  

//	public int getScreenWidth(Context context) {
//        int screenWidth;
//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        screenWidth = dm.widthPixels;
//        return screenWidth;
//    }
//     
//    /**
//     * 得到屏幕高度
//     * @return 单位:px
//     */
//    public int  getScreenHeight(Context context) {
//        int screenHeight;
//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        screenHeight = dm.widthPixels;
//        return screenHeight;
//    }
}



