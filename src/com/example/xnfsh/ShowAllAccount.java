package com.example.xnfsh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import com.example.xnfsh.db.entity.Account;
import com.example.xnfsh.db.service.AccountService;
import com.special.ResideMenuDemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowAllAccount extends Activity {

	private List<Account> accounts;
	private List<String> names;
	private List<String> pirs;
	private List<String> ids;
	
	ListView accountlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_all_db_account);
		
		initweidgt();
		initAccounts();
		//initimformation();
		//initlistener();
		
	}
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 	
			{
				case 1:
					initimformation();
					break;
            }
	 }
	};

	public void initweidgt()
	{
		accountlist=(ListView) this.findViewById(R.id.listView1);
		accountlist.setOnItemClickListener(new account_click());
	}
	
	public void initAccounts()
	{
		accounts=new ArrayList<Account>();
		ids=new ArrayList<String>();
		names=new ArrayList<String>();
		pirs=new ArrayList<String>();
		
		new machine().start();	
	}
	
	public void initimformation()
	{
		accountlist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,names));
	}

	class account_click implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ShowAllAccount.this,News_title_list.class);
			intent.putExtra("account_id", ""+ids.get((int)arg3));
			Log.v("id", ids.get((int)arg3));
			startActivity(intent);
		}

	}
	
	class machine extends Thread
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			accounts=AccountService.fetch_all_acccount();
			for (Account account : accounts)
			{
				names.add(account.getAccount_name());
				pirs.add(account.getAccount_pir());
				ids.add(account.getAccount_id());
			}	
			Message msg=new Message();
			msg.what=1;
			mHandler.sendMessage(msg);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
