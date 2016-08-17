/**
 *  ClassName: Favorite.java
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
import net.shopnc.android.model.Board;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 本地收藏
 * @author qjyong
 */
public class FavoriteDao {
	private DbHelper dbHelper;
	
	public FavoriteDao(Context context){
		dbHelper = new DbHelper(context);
	}
	
	public void deleteAll(){
		SQLiteDatabase db = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
	
			db.execSQL(Constants.SQL_FAVORITE_DELETE);
			db.execSQL(Constants.SQL_FAVORITE_RESET_SEQUENCE);
			
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
	
	public Board get(long fid){
		Board board = null;
		
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			//db.beginTransaction();
			
			String sql = MessageFormat.format(Constants.SQL_FAVORITE_SELECT_BY_FID, fid);
			c = db.rawQuery(sql, null);
			if(c.moveToNext()){
				board = new Board();
				board.setFid(c.getLong(c.getColumnIndex("fid")));
				board.setFup(c.getLong(c.getColumnIndex("fup")));
				board.setName(c.getString(c.getColumnIndex("name")));
				board.setType(c.getString(c.getColumnIndex("type")));
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
		
		return board;
	}
	
	public void save(Board board, String owner){
		SQLiteDatabase db = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			db.beginTransaction();
			
			Object [] paramValues = {Long.valueOf(board.getFid()),
					Long.valueOf(board.getFup()), 
					board.getName(), board.getType(), owner};
			
			db.execSQL(Constants.SQL_FAVORITE_INSERT, paramValues);
			
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
	
	public ArrayList<Board> findByOwner(String owner){
		ArrayList<Board> favos = new ArrayList<Board>();
		
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			//db.beginTransaction();
			
			String sql = MessageFormat.format(Constants.SQL_FAVORITE_SELECT_OWNER, owner);
			c = db.rawQuery(sql, null);
			while(c.moveToNext()){
				Board board = new Board();
				board.setFid(c.getLong(c.getColumnIndex("fid")));
				board.setFup(c.getLong(c.getColumnIndex("fup")));
				board.setName(c.getString(c.getColumnIndex("name")));
				board.setType(c.getString(c.getColumnIndex("type")));
				
				favos.add(board);
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
	
	public ArrayList<Board> findByPager(String owner, int pageNo, int pageSize){
		ArrayList<Board> favos = new ArrayList<Board>();
		
		SQLiteDatabase db = null;
		Cursor c = null;
		try{
			db =dbHelper.getSQLiteDatabase();
			//db.beginTransaction();
			
			String sql = MessageFormat.format(Constants.SQL_FAVORITE_SELECT_LIMIT, owner, (pageNo - 1) * pageSize, pageSize);
			c = db.rawQuery(sql, null);
			while(c.moveToNext()){
				Board board = new Board();
				board.setFid(c.getLong(c.getColumnIndex("fid")));
				board.setFup(c.getLong(c.getColumnIndex("fup")));
				board.setName(c.getString(c.getColumnIndex("name")));
				board.setType(c.getString(c.getColumnIndex("type")));
				
				favos.add(board);
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
