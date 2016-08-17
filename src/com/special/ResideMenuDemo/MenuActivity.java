package com.special.ResideMenuDemo;

import java.util.Timer;

import net.shopnc.android.ui.forum.ForumActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dreamteam.app.ui.YueDuMain;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    public ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemWellanNews;
    private ResideMenuItem itemNews;
   // private ResideMenuItem itemSettings;
    private ResideMenuItem itemTopics;
   // private ResideMenuItem itemClassTable;
    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.main);
        mContext = this;
        setUpMenu();
        Intent it=getIntent();
        if(it.getStringExtra("b")!=null)
        {        	
        	changeFragment(new HomePageScreen());
        	resideMenu.openMenu();
        }
        else{
        	if(it.getStringExtra("v")!=null)
        	{//==============angela
        		changeFragment(new HomePageScreen());
            	resideMenu.openMenu();
        		//Intent it1 = new Intent(MenuActivity.this, YueDuMain.class);
                //startActivity(it1);
                //finish();
        	}
        	else
        	{	
        		/*if(it.getStringExtra("bigclass")!=null)
        		{//==============angela
        			Intent it1 = new Intent(MenuActivity.this, YueDuMain.class);
                    startActivity(it1);
                    finish();
            		resideMenu.openMenu();
        		}
        		else {*/
        			changeFragment(new HomePageScreen());
            		resideMenu.openMenu();
				//}
        	}
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "指尖中南");
        //itemClassTable  = new ResideMenuItem(this, R.drawable.icon_profile,  "我的课表");
        itemWellanNews  = new ResideMenuItem(this, R.drawable.xysh,  "校园动态");
        itemNews = new ResideMenuItem(this, R.drawable.zxqy, "资讯前沿");
        //itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "轻话题");
        itemTopics   = new ResideMenuItem(this, R.drawable.tlq, "茶话希贤");
        
        itemHome.setOnClickListener(this);
        itemWellanNews.setOnClickListener(this);
        itemNews.setOnClickListener(this);
      //  itemSettings.setOnClickListener(this);
        //itemClassTable.setOnClickListener(this);
        itemTopics.setOnClickListener(this);
        
        resideMenu.addMenuItem(itemHome);
        //resideMenu.addMenuItem(itemClassTable);
        resideMenu.addMenuItem(itemWellanNews);
        resideMenu.addMenuItem(itemNews);
        //resideMenu.addMenuItem(itemSettings);
        resideMenu.addMenuItem(itemTopics);

        
        /*findViewById(R.id.title_bar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu();
            }
        });*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

    	
    	
    	/* if (view == itemClassTable ){
         	Intent it = new Intent(MenuActivity.this, OffLineTodayCourse.class);
             startActivity(it);
             finish();
         }else*/ if (view == itemWellanNews){
            changeFragment(new WellanNewsFragment());
        }else if (view == itemNews){
        	//angela===============
        	Intent it = new Intent(MenuActivity.this, YueDuMain.class);
            startActivity(it);
            finish();
        }else if (view == itemHome ){
            changeFragment(new HomePageScreen());
        }else if (view == itemTopics){
        	Intent it = new Intent(MenuActivity.this, ForumActivity.class);  
        	
            startActivity(it);
            finish();
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
        	//Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // What good method is to access resideMenu锛�    
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
    

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    
}
