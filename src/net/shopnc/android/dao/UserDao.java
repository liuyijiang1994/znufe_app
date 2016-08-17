/**
 *  ClassName: UserDao.java
 *  created on 2012-3-10
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.dao;

import java.text.MessageFormat;
import java.util.ArrayList;

import net.shopnc.android.common.Constants;
import net.shopnc.android.common.DbHelper;
import net.shopnc.android.model.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author qjyong
 */
public class UserDao {
	private DbHelper dbHelper;
	
	public UserDao(Context context){
		dbHelper = new DbHelper(context);
	}
	
	public void delete(String author){
		SQLiteDatabase db = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			
			Object [] paramValues = {author};
			
			db.execSQL(Constants.SQL_USER_DELETE_BY, paramValues);
			
			db.setTransactionSuccessful();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public User get(String author){
		User user = null;
				
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			
			String sql = MessageFormat.format(Constants.SQL_USER_SELECT_BY, author);
			c = db.rawQuery(sql, null);
			if(c.moveToNext()){
				String authorid = c.getString(c.getColumnIndex("authorid"));
				String author2 = c.getString(c.getColumnIndex("author"));
				String pwd = c.getString(c.getColumnIndex("pwd"));
				String sessionid = c.getString(c.getColumnIndex("sessionid"));
				
				user = new User();
				user.setAuthorid(authorid);
				user.setAuthor(author2);
				user.setPwd(pwd);
				user.setSessionid(sessionid);
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != c){
				c.close();
			}
			if(db != null){
				db.endTransaction();
				db.close();
			}
		}
		return user;
	}
	
	public void save(User user){
		SQLiteDatabase db = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			
			Object [] paramValues = {user.getAuthorid(), user.getAuthor(), user.getPwd(), user.getSessionid()};
			
			db.execSQL(Constants.SQL_USER_INSERT, paramValues);
			
			db.setTransactionSuccessful();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public ArrayList<User> findAll(){
		ArrayList<User> users = new ArrayList<User>();
		
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			
			c = db.rawQuery(Constants.SQL_USER_SELECT_ALL, null);
			while(c.moveToNext()){
				String authorid = c.getString(c.getColumnIndex("authorid"));
				String author = c.getString(c.getColumnIndex("author"));
				String pwd = c.getString(c.getColumnIndex("pwd"));
				String sessionid = c.getString(c.getColumnIndex("sessionid"));
				
				User user = new User();
				user.setAuthorid(authorid);
				user.setAuthor(author);
				user.setPwd(pwd);
				user.setSessionid(sessionid);
				
				users.add(user);
			}
			db.setTransactionSuccessful();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != c){
				c.close();
			}
			if(db != null){
				db.endTransaction();
				db.close();
			}
		}
		return users;
	}
}
