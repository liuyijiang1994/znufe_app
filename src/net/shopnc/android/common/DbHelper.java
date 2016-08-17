/**
 *  ClassName: DbHelper.java
 *  created on 2011-12-27
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android系统中操作SQLite数据库的帮助类
 * 主要用于数据库版本的管理，并提供SQLiteDatabase实例
 * @author hjgang
 */
public class DbHelper extends SQLiteOpenHelper {
	private static String db_name;
	
	static{
		/*
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			db_name = Constants.CACHE_DIR +"/"+Constants.DB_NAME;
		}else{
		*/
		db_name = Constants.DB_NAME;
	}
	
	public DbHelper(Context context){
		super(context, db_name, null, Constants.DB_VERSION);
	}

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	//在第一个安装本应用程序时，会回调的方法，主要用于执行数据库表的创建和数据初始化
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("执行oncreate-------");
		
		db.execSQL(Constants.SQL_USER_CREATE);
		db.execSQL(Constants.SQL_FAVORITE_CREATE);
		db.execSQL(Constants.SQL_LASTEST_BROWSE_CREATE);
		
		//注意这个SqliteDatabase实例不需要关闭
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("执行onUpgrade-------");
		
		//清理旧版本中的表
		db.execSQL(Constants.SQL_USER_DROP);
		db.execSQL(Constants.SQL_FAVORITE_DROP);
		db.execSQL(Constants.SQL_LASTEST_BROWSE_DROP);
		
		//重新创建新的表
		onCreate(db);
	}

	public SQLiteDatabase getSQLiteDatabase(){
		SQLiteDatabase db = null;
		
		try{
			db = this.getWritableDatabase();
		}catch (Exception e) {
			db = this.getReadableDatabase();
		}
		
		return db;
	}
}
