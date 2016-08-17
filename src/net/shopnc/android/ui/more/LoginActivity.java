/**
 *  ClassName: LoginActivity.java
 *  created on 2012-2-25
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.more;

import org.apache.http.HttpStatus;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MD5Encoder;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.dao.UserDao;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.User;
import net.shopnc.android.widget.MyProcessDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录
 * @author qjyong
 */
public class LoginActivity extends Activity {
	private MyApp myApp;
	
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;
	
	private EditText txt_loginname;
	private EditText txt_pwd;
	private CheckBox cbx_rememberpwd;
	private CheckBox cbx_autologin;
	private Button btn_login;
	private Button btn_reg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplication();
		this.setContentView(R.layout.login);
		
		initTitleBar();
		
		initContent();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == Constants.DIALOG_LOGIN_ID){
			return createProgressDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private MyProcessDialog createProgressDialog(){
		MyProcessDialog dialog = new MyProcessDialog(this);
		dialog.setMsg(getString(R.string.login_wait));
		return dialog;
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
	
	
	private void doLogin(final String author, final String pwd, final boolean rememberpwd, final boolean autologin){
		final String md5_pwd = MD5Encoder.encode(pwd);
		this.showDialog(Constants.DIALOG_LOGIN_ID);
		
		if(rememberpwd){
			myApp.setRemember_pwd(true);
		}else{
			myApp.setRemember_pwd(false);
		}
		if(autologin){
			myApp.setAuto_login(true);
		}else{
			myApp.setAuto_login(false);
		}
		RemoteDataHandler.asyncLogin(author, md5_pwd, new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				if(data.getCode()==HttpStatus.SC_OK){
					String json = data.getJson();
					User user = User.newInstance(json);
					String sessionid = user.getSessionid();
					if(null != sessionid){
						if(!"aperror".equals(sessionid)){
							user.setAuthor(author);
							user.setPwd(md5_pwd);
							//成功
							if(autologin || rememberpwd){ //自动登录或记住密码
								myApp.setUserpw(pwd);
							}
							myApp.setUseracc(author);
							myApp.setSid(user.getSessionid());
							myApp.setUid(user.getAuthorid());
							myApp.setGroupid(user.getGroupid());
							
							UserDao userDao = new UserDao(LoginActivity.this);
							if(null == userDao.get(author)){
								userDao.save(user);
							}
							//QIUJY 还需要更新版块列表的权限
							myApp.setSubBoardMap(RemoteDataHandler.loadSubBoardMap(user.getAuthorid()));
							
							LoginActivity.this.setResult(200);
							LoginActivity.this.finish();
						}else{
							Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试！", Toast.LENGTH_SHORT).show();
						}
					}else{
						//失败
						//myApp.setSid("");
						//myApp.setUid("");
						//myApp.setGroupid("");
						Toast.makeText(LoginActivity.this, "登陆失败，请重试！", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(LoginActivity.this, "网络错误，请重试！", Toast.LENGTH_SHORT).show();
				}
				dismissDialog(Constants.DIALOG_LOGIN_ID);
			}
		});
		//this.dismissDialog(Constants.DIALOG_LOGIN_ID);
	}
	
	private void initContent(){
		txt_loginname = (EditText)this.findViewById(R.id.txt_loginname);
		txt_loginname.setText(myApp.getUseracc());
		
		txt_pwd = (EditText)this.findViewById(R.id.txt_pwd);
		if(myApp.isRemember_pwd()){
			txt_pwd.setText(myApp.getUserpw());
		}
		cbx_rememberpwd = (CheckBox)this.findViewById(R.id.cbx_rememberpwd);
		cbx_autologin = (CheckBox)this.findViewById(R.id.cbx_autologin);
		btn_login = (Button)this.findViewById(R.id.btn_login);
		btn_reg = (Button)this.findViewById(R.id.btn_reg);
		
		btn_reg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),RegActivity.class);
				startActivity(intent);
			}
		});
		
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String author = txt_loginname.getText().toString();
				String pwd = txt_pwd.getText().toString();
				
				if(null == author || "".equals(author)){
					Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(null == pwd || "".equals(pwd)){
					Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				doLogin(author, pwd, cbx_rememberpwd.isChecked(), cbx_autologin.isChecked());
			}
		});
	}
	
	private void initTitleBar(){
		//设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.login));
		
		//设置标题栏按钮
		btn_left = (ImageButton)this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
		
		btn_right = (ImageButton)this.findViewById(R.id.btn_right);
		btn_right.setVisibility(View.INVISIBLE);
	}
}
