
package com.special.ResideMenuDemo;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.iceman.yangtze.Globe;
import com.iceman.yangtze.NetHelper;
import com.iceman.yangtze.WindowActivity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenuDemo.R;
import com.umeng.analytics.MobclickAgent;
import com.iceman.yangtze.net.MyHttpResponse;


public class HomePageScreen extends Fragment {
	//boolean info=NetHelper.IsHaveInternet(getBaseContext());

	private ResideMenu resideMenu;
	public  static boolean  isExit=false;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main1, container, false);
		setHasOptionsMenu(true);
		int image_id[] = {R.id.library,R.id.mysorce,R.id.setting,R.id.schedule};
		ImageView v1=(ImageView)view.findViewById(image_id[0]);
		ImageView v2=(ImageView)view.findViewById(image_id[1]);
		ImageView v3=(ImageView)view.findViewById(image_id[2]);
		ImageView v4=(ImageView)view.findViewById(image_id[3]);

		MenuActivity parentActivity = (MenuActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		
		
		//===================================================================================			
		getActivity().getApplicationContext();
		ConnectivityManager cm=(ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo();		
		if (info != null && info.isAvailable()){ 
		}else{ 
			  Toast.makeText(getActivity().getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
		  } 			


		view.findViewById(R.id.title_bar_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu();
			}
		});

		view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
						startActivity(new Intent(getActivity(), LoginScreen.class));
			}
		});

		v1.setOnClickListener(new OnClickListener() {      
			@SuppressLint("NewApi")
			@Override  
			public void onClick(View arg0) {//OnLineCourse
				getActivity().getApplicationContext();
				ConnectivityManager cm=(ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
				NetworkInfo info = cm.getActiveNetworkInfo(); 
				if (info != null && info.isAvailable()&&Globe.sCookieString!=null){ 
					startActivity(new Intent(getActivity(), OnLineCourse.class));
				}else if(info==null)
				{ 
					  Toast.makeText(getActivity().getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
				  } 
				else {
					Toast.makeText(getActivity().getApplicationContext(), "你尚未登录", Toast.LENGTH_LONG).show();
				}
		
			}
			});
		v4.setOnClickListener(new OnClickListener() {      
			@Override   public void onClick(View arg0) {    
				// TODO Auto-generated method stub
				//if(Globe.sCookieString!=null)
				//===================================================================================			
				getActivity().getApplicationContext();
				ConnectivityManager cm=(ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
				NetworkInfo info = cm.getActiveNetworkInfo(); 
				if (info != null && info.isAvailable()&&Globe.sCookieString!=null){ 
					startActivity(new Intent(getActivity(), GradeSearch.class));
				}else if(info==null)
				{ 
					  Toast.makeText(getActivity().getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
				  } 
				else {
					Toast.makeText(getActivity().getApplicationContext(), "你尚未登录", Toast.LENGTH_LONG).show();
				}
			}
				//startActivity(new Intent(getActivity(), Evaluate.class));
				//else
				//{
				//Toast.makeText(getActivity(),"你尚未登录",Toast.LENGTH_SHORT).show();
				//}
			 });
		v3.setOnClickListener(new OnClickListener() {      
			@Override   public void onClick(View arg0) {// KaoShiAnPai   
				// TODO Auto-generated method stub 
				getActivity().getApplicationContext();
				ConnectivityManager cm=(ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
				NetworkInfo info = cm.getActiveNetworkInfo(); 
				if (info != null && info.isAvailable()){ //&&Globe.sCookieString!=null
					startActivity(new Intent(getActivity(), Map_Main.class));
				}else if(info==null)
				{ 
					  Toast.makeText(getActivity().getApplicationContext(), "连接超时，请检查网络连接。", Toast.LENGTH_LONG).show();
				  } 
				else {
					Toast.makeText(getActivity().getApplicationContext(), "你尚未登录", Toast.LENGTH_LONG).show();
				}
			}  
			});
		v2.setOnClickListener(new OnClickListener() {      
			@Override   public void onClick(View arg0) {    
				// TODO Auto-generated method stub
				//boolean info=NetHelper.IsHaveInternet(getBaseContext());
				//if(Globe.sCookieString!=null)
				startActivity(new Intent(getActivity(),SelectSection.class));
				/*else
				{
				Toast.makeText(getActivity(),"你尚未登录",Toast.LENGTH_SHORT).show();
				}*/
			}

		});
		/*v5.setOnClickListener(new OnClickListener() {      
			@Override   public void onClick(View arg0) {    
				// TODO Auto-generated method stub    
				Intent it5 = new Intent(HomePageScreen.this, ClassScoreSearchScreen.class);
                startActivity(it5);   }  });*/
		return view;




	}

	/*@Override 
     public  void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
		 //super.onCreateOptionsMenu(menu);
	        // menu.add(0, 1, 0,"   注销").setIcon(R.drawable.global_bg);
	     //return true;
	        //inflater.inflate(R.menu.option, menu); 
	         inflater.inflate(R.menu.option, menu);
    } 
    @Override 
     public  boolean onOptionsItemSelected(MenuItem item) { 
    	switch (item.getItemId()) {
        case R.id.menu_settings:
        {
        	 Intent backIntent = new Intent(getActivity(), LoginScreen.class);
    	     startActivity(backIntent);
    	     isExit=true;
        }
            break; //处理完以后这里也可以return true;
    }
    // TODO Auto-generated method stub
    return super.onOptionsItemSelected(item);

    } */
	/*public void onBackPressed() {
	       // TODO Auto-generated method stub
		 super.onBackPressed();
	     Intent backIntent = new Intent(HomePageScreen.this, MenuActivity.class);
	     startActivity(backIntent);
         finish();
         startActivity(new Intent(getActivity(),MenuActivity.class));


	    }*/
	/*public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.activity_main1, menu);
		return true;
	}*/

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HomePageScreen"); //统计页面
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HomePageScreen"); 
	}

}
