/**
 *  ClassName: MyApp.java
 *  created on 2011-12-27
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.shopnc.android.dao.LastestBrowseDao;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Smiley;

import org.apache.http.HttpStatus;
import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.DisplayMetrics;

/**
 * 全局变量类
 * @author qjyong
 */
public class MyApp extends Application {
	/** 系统初始化配置文件操作器 */
	private SharedPreferences sysInitSharedPreferences;
	/** 用户的会话ID对应服务器的sessionid */
	private String sid;
	/** 用户id,对应服务器的authorid。如果sid和uid不为空，代表是已登录的用户 */
	private String uid;
	/** 用户组ID */
	private String groupid;
	/** 登录时的用户名对应服务器的author */
	private String useracc;
	/** 登录时的原始密码 */
	private String userpw;
	/** 是否记住密码 */
	private boolean remember_pwd;
	/** 是否自动登录 */
	private boolean auto_login;
	/**引用上下文*/
	private static Context context;
	/** 帖子列表每页显示的记录条数 */
	private int pageSize;
	/** 帖子页默认只看楼主 */
	private boolean display_postStarter_only;
	/** GPRS(2G|3G)下禁止图片显示 */
	private boolean img_invisible;
	private int screenWidth;
	private int screenHeight;
	private static int sw;
	private static int sh;
	/**用于记录用户的浏览记录到本地数据库表中的Dao*/
	private LastestBrowseDao lastestBrowseDao;
	/** 表情 */
	private ArrayList<Smiley> faceList;
	private HashMap<Long, Board> subBoardMap;
	private ArrayList<Board> boards;
	@Override
	public void onCreate() {
		super.onCreate();
		createCacheDir();
		init();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}
	public static int getScreenWidth() {
		return sw;
	}
	public static int getScreenHeight() {
		return sh;
	}
	public static Context getContext() {
		return context;
	}
	private void init(){
		sysInitSharedPreferences = getSharedPreferences(
				Constants.SYSTEM_INIT_FILE_NAME, MODE_PRIVATE);

		remember_pwd = sysInitSharedPreferences.getBoolean(
				"remember_pwd", true);
		
		auto_login = sysInitSharedPreferences.getBoolean("auto_login", false);

		useracc = sysInitSharedPreferences.getString("useracc", "");
		
		if(remember_pwd){
			userpw = sysInitSharedPreferences.getString("upw", "");
		}else{
			userpw = "";
		}
		pageSize = 20;
		display_postStarter_only = sysInitSharedPreferences.getBoolean(
				"display_postStarter_only", false);
		img_invisible = sysInitSharedPreferences.getBoolean("img_invisible",
				true);
		DisplayMetrics dm = SystemHelper.getScreenInfo(this);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		sw = screenWidth;
		sh = screenHeight;
		
		lastestBrowseDao = new LastestBrowseDao(this);
		
		subBoardMap = new HashMap<Long, Board>();
		
		boards=new ArrayList<Board>();
	}

	/***
	 * 获取用于记录用户浏览记录到本地历史数据库表中的Dao
	 * @return
	 */
	public LastestBrowseDao getLastestBrowseDao(){
		return this.lastestBrowseDao;
	}
	
	/**
	 * 获取系统初始化文件操作器
	 * 
	 * @return
	 */
	public SharedPreferences getSysInitSharedPreferences() {
		return sysInitSharedPreferences;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isAuto_login() {
		return auto_login;
	}

	public void setAuto_login(boolean auto_login) {
		this.auto_login = auto_login;

		sysInitSharedPreferences.edit()
				.putBoolean("auto_login", this.auto_login).commit();
	}


	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 帖子页默认只看楼主
	 * @return
	 */
	public boolean isDisplay_postStarter_only() {
		return display_postStarter_only;
	}

	public void setDisplay_postStarter_only(boolean display_postStarter_only) {
		this.display_postStarter_only = display_postStarter_only;
		sysInitSharedPreferences.edit()
				.putBoolean("display_postStarter_only", this.display_postStarter_only)
				.commit();
	}

	/**
	 * GPRS(2G|3G)下禁止图片显示
	 * @return
	 */
	public boolean isImg_invisible() {
		return img_invisible;
	}

	public void setImg_invisible(boolean img_invisible) {
		this.img_invisible = img_invisible;
		sysInitSharedPreferences.edit()
				.putBoolean("img_invisible", this.img_invisible)
				.commit();
	}

	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public String getUseracc() {
		return useracc;
	}


	public void setUseracc(String useracc) {
		this.useracc = useracc;
		sysInitSharedPreferences.edit().putString("useracc", this.useracc).commit();
	}

	public boolean isRemember_pwd() {
		return remember_pwd;
	}


	public void setRemember_pwd(boolean remember_pwd) {
		this.remember_pwd = remember_pwd;
		sysInitSharedPreferences.edit().putBoolean("remember_pwd", this.remember_pwd)
				.commit();
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
		
		if (remember_pwd) {
			sysInitSharedPreferences.edit().putString("upw", this.userpw).commit();
		}
	}
	
	public ArrayList<Smiley> getFaceList() {
		if(faceList == null){
			this.setFaceList(RemoteDataHandler.loadSmiley());
		}
		return faceList;
	}

	public void setFaceList(ArrayList<Smiley> faceList) {
		this.faceList = faceList;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	/**
	 * 当前用户的所有子版块Map表，key为版块的id，value为版块对象
	 * @return
	 */
	public HashMap<Long, Board> getSubBoardMap() {
		return subBoardMap;
	}

	public void setSubBoardMap(HashMap<Long, Board> subBoardMap) {
		this.subBoardMap = subBoardMap;
	}
	
	

	public ArrayList<Board> getBoards() {
		return boards;
	}

	public void setBoards(ArrayList<Board> boards) {
		this.boards = boards;
	}

	//创建SD卡缓存目录
	private void createCacheDir(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File f = new File(Constants.CACHE_DIR);
			if(f.exists()){
				System.out.println("SD卡缓存目录:已存在!");
			}else{
				if(f.mkdirs()){
					System.out.println("SD卡缓存目录:" + f.getAbsolutePath() + "已创建!");
					/**
					 * 查看是否是第一次安装
					 */
					RemoteDataHandler.intall("new", "android", new Callback() {
						@Override
						public void dataLoaded(ResponseData data) {
							if(data.getCode() == HttpStatus.SC_OK){
								System.out.println("第一次安装");
							}else{
								System.out.println("data"+data.getJson());
							}
						}
					});
				}else{
					System.out.println("SD卡缓存目录:创建失败!");
				}
			}
			
			File ff = new File(Constants.CACHE_DIR_SMILEY);
			if(ff.exists()){
				System.out.println("SD卡表情缓存目录:已存在!");
			}else{
				if(ff.mkdirs()){
					System.out.println("SD卡表情缓存目录:" + ff.getAbsolutePath() + "已创建!");
				}else{
					System.out.println("SD卡表情缓存目录:创建失败!");
				}
			}
			
			File fff = new File(Constants.CACHE_DIR_IMAGE);
			if(fff.exists()){
				System.out.println("SD卡图片缓存目录:已存在!");
			}else{
				if(fff.mkdirs()){
					System.out.println("SD卡图片缓存目录:" + fff.getAbsolutePath() + "已创建!");
				}else{
					System.out.println("SD卡图片缓存目录:创建失败!");
				}
			}
			
			File ffff = new File(Constants.CACHE_DIR_UPLOADING_IMG);
			if(ffff.exists()){
				System.out.println("SD卡上传图片缓存目录:已存在!");
			}else{
				if(ffff.mkdirs()){
					System.out.println("SD卡上传图片缓存目录:" + ffff.getAbsolutePath() + "已创建!");
				}else{
					System.out.println("SD卡上传图片缓存目录:创建失败!");
				}
			}
		}
	}
}
