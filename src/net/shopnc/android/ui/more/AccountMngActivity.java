/**
 *  ClassName: AccountMngActivity.java
 *  created on 2012-2-24
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpStatus;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.SubmenuListViewAdapter;
import net.shopnc.android.adapter.UserListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.dao.UserDao;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.User;
import net.shopnc.android.widget.MyProcessDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 账号管理
 * @author qjyong
 */
public class AccountMngActivity extends Activity {
	public static final String TAG = "AccountMngActivity";
	private MyApp myApp;
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	private ListView lv_users;
	private UserListViewAdapter adapter2;
	
	private ListView lv;
	private SubmenuListViewAdapter adapter;
	
	private UserDao dao;
	private User curr_select_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.more_accmng);
		myApp = (MyApp)this.getApplication();
		
		dao = new UserDao(this);
		
		initTitleBar();
		
		lv_users = (ListView)this.findViewById(R.id.lv_user);
		lv_users.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				curr_select_user = (User)adapter2.getItem(position);
				
				showDialog(Constants.DIALOG_USER_CHANGE_ID);
			}
		});
		loadUserListData();

		
		lv = (ListView)this.findViewById(R.id.lv);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(id == R.drawable.ic_list_accadd){
					Intent intent = new Intent(AccountMngActivity.this, LoginActivity.class);
					
					AccountMngActivity.this.startActivityForResult(intent, 200);
				}
			}
		});
		
		//init listview data
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(SubmenuListViewAdapter.TAG_ITEM_TEXT, this.getString(R.string.more_accmng_add));
		map.put(SubmenuListViewAdapter.TAG_ITEM_ICON, Integer.valueOf(R.drawable.ic_list_accadd));
		datas.add(map);
		
		adapter = new SubmenuListViewAdapter(AccountMngActivity.this, datas);
		lv.setAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == Constants.DIALOG_USER_CHANGE_ID){
			return createChangeUserDialog();
		}else if(id == Constants.DIALOG_LOGIN_ID){
			return createLoginWaitDialog();
		}
		return super.onCreateDialog(id);
	}
	
	public Dialog createLoginWaitDialog(){
		MyProcessDialog dialog = new MyProcessDialog(this);
		dialog.setMsg(getString(R.string.login_wait));
		return dialog;
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 switch (resultCode){  
	        case 200:  
	        	Log.d(TAG, "load...userListData");
	        	loadUserListData();
	        	break;
		 }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private Dialog createChangeUserDialog(){
		final AlertDialog dialog = new AlertDialog.Builder(AccountMngActivity.this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("选择操作")
			.setItems(new String[]{"切换登录状态", "从列表中删除"}, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(which == 0){
					String author = curr_select_user.getAuthor();
					String sessionid = curr_select_user.getSessionid();
					String pwd = curr_select_user.getPwd();
					
					//当前选中的用户为登录状态,切换成离线状态
					if(curr_select_user.getAuthorid().equals(myApp.getUid()) 
							&& sessionid.equals(myApp.getSid())){
						myApp.setSid("");
						myApp.setUid("");
						myApp.setGroupid("");
						myApp.setSubBoardMap(null);
						loadUserListData();
					}else{
						doLogin(author, pwd);
					}
				}else if(which == 1){
					dao.delete(curr_select_user.getAuthor());
					if(curr_select_user.getAuthorid().equals(myApp.getUid())){//删除是当前登录的用户
						myApp.setSid("");
						myApp.setUid("");
						myApp.setGroupid("");
						myApp.setSubBoardMap(null);
						//Toast.makeText(AccountMngActivity.this, "当前登录用户，不能删除！", Toast.LENGTH_SHORT).show();
					}
					loadUserListData();
				}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		
		return dialog;
	}
	
	private void doLogin(String author, String pwd){
		showDialog(Constants.DIALOG_LOGIN_ID);
		RemoteDataHandler.asyncLogin(author, pwd, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				String str = "登录成功！";
				if(data.getCode()==HttpStatus.SC_OK){
					String json = data.getJson();
					User user = User.newInstance(json);
					String sessionid = user.getSessionid();	
					if(null != sessionid && !"aperror".equals(sessionid)){
						//成功
						myApp.setUseracc(curr_select_user.getAuthor());
						myApp.setSid(user.getSessionid());
						myApp.setUid(user.getAuthorid());
						myApp.setGroupid(user.getGroupid());
						
						loadUserListData();
						
						//QIUJY 还需要更新版块权限
						myApp.setSubBoardMap(RemoteDataHandler.loadSubBoardMap(user.getAuthorid()));
					}else{
						//失败
						str = "切换登录状态失败！";
					}
				}else{
					//失败
					str = "切换登录状态失败！";
				}
				dismissDialog(Constants.DIALOG_LOGIN_ID);
				
				Toast.makeText(AccountMngActivity.this, str, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void initTitleBar(){
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.more_accmng));
		
		//设置标题栏按钮
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountMngActivity.this.finish();
			}
		});
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.INVISIBLE);
	}
	
	private void loadUserListData(){
		ArrayList<User> users = dao.findAll();
		Log.d(TAG, users.toString());
		adapter2 = new UserListViewAdapter(this, users);
		lv_users.setAdapter(adapter2);
	}
}
