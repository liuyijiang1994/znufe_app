package com.special.ResideMenuDemo;

import com.umeng.analytics.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class PreferenceTestMain extends Activity {
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_RUN = "first";
    private boolean first;
    public static boolean isFirst=true;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	   MobclickAgent.openActivityDurationTrack(false);
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.startpage);
            // Restore preferences
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            first = settings.getBoolean(FIRST_RUN, true);
            if (first) {
                    //Toast.makeText(this, "The Application is first run",
                     //               Toast.LENGTH_LONG).show();
                    
                    System.out.println("the first run");
            } else {
                    //Toast.makeText(this, "The Application is not first run",
                    //                Toast.LENGTH_LONG).show();
                    System.out.println("not the first run");
            }
            Intent it=new Intent(PreferenceTestMain.this,StartPage.class);
            startActivity(it);
            
            System.out.println(isFirst);
            finish();
    }

    @Override
    protected void onStop() {
            super.onStop();

            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            if (first) {
                    editor.putBoolean(FIRST_RUN, false);
            }
            // Commit the edits!
            editor.commit();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PreferenceTestMain"); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StartPage"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
        MobclickAgent.onPause(this);
    }
    
}