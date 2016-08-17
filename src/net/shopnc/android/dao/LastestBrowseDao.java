/**
 *  ClassName: LastestBrowseDao.java
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
import net.shopnc.android.model.Topic;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 操作最近浏览表的Dao
 * @author qjyong
 */
public class LastestBrowseDao {
	private DbHelper dbHelper;
	
	public LastestBrowseDao(Context context){
		dbHelper = new DbHelper(context);
	}
	
	public void deleteAll(){
		SQLiteDatabase db = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
	
			db.execSQL(Constants.SQL_LASTEST_BROWSE_DELETE);
			db.execSQL(Constants.SQL_LASTEST_BROWSE_RESET_SEQUENCE);
			
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
	public void record(Topic last_browse_topic){
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
	
			//String sql = MessageFormat.format(Constants.SQL_LASTEST_BROWSE_SELECT_BY_TID, last_browse_topic.getTid());
			String sql = Constants.SQL_LASTEST_BROWSE_SELECT_BY_TID + last_browse_topic.getTid();
			c = db.rawQuery(sql, null);
			
			if(c.moveToNext()){
				db.execSQL(Constants.SQL_LASTEST_BROWSE_DELETE_BY_TID, new Object[]{last_browse_topic.getTid()});
			}
			
			Object [] paramValues = {Long.valueOf(last_browse_topic.getTid()), 
					last_browse_topic.getSubject(), 
					Long.valueOf(last_browse_topic.getDateline()), 
					last_browse_topic.getAuthor(), 
					last_browse_topic.getViews(), 
					last_browse_topic.getReplies()};
			db.execSQL(Constants.SQL_LASTEST_BROWSE_INSERT, paramValues);
			
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
	}
	
	public ArrayList<Topic> findByPager(int pageNo, int pageSize){
		ArrayList<Topic> favos = new ArrayList<Topic>();
		
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			//db.beginTransaction();
			
			String sql = MessageFormat.format(Constants.SQL_LASTEST_BROWSE_SELECT_LIMIT, (pageNo - 1) * pageSize, pageSize);
			c = db.rawQuery(sql, null);
			while(c.moveToNext()){
				Topic topic = new Topic();
				topic.setTid(c.getLong(c.getColumnIndex("tid")));
				topic.setSubject(c.getString(c.getColumnIndex("subject")));
				topic.setDateline(c.getLong(c.getColumnIndex("dateline")));
				topic.setAuthor(c.getString(c.getColumnIndex("author")));
				topic.setViews(c.getString(c.getColumnIndex("views")));
				topic.setReplies(c.getString(c.getColumnIndex("replies")));
				
				favos.add(topic);
			}
			//db.setTransactionSuccessful();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null != c){
				c.close();
			}
			if(db != null){
				//db.endTransaction();
				db.close();
			}
		}
		return favos;
	}
}
