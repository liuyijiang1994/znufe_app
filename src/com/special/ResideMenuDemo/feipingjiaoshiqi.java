package com.special.ResideMenuDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iceman.yangtze.WindowActivity;
import com.iceman.yangtze.net.MyHttpResponse;


public class feipingjiaoshiqi extends WindowActivity{
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.feipingjiaoshiqi);
	        Button btnBack = (Button) findViewById(R.id.btn);
			btnBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onBackPressed();
				}
				
			});
	    }
	    public void onBackPressed() {
		       // TODO Auto-generated method stub
			 super.onBackPressed();	                               
		    }
		@Override
		public void handResponse(MyHttpResponse myHttpResponse) {
			// TODO Auto-generated method stub
			
		}
}