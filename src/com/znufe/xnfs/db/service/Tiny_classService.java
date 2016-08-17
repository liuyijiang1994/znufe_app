package com.znufe.xnfs.db.service;

import java.util.ArrayList;
import java.util.List;

import com.znufe.xnfs.db.entity.Tiny_class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Tiny_classService {

	private DBOpenHelper dbOH;

	public Tiny_classService(Context context)
	{
		System.out.println("开始初始化service");
		this.dbOH = new DBOpenHelper(context);
		System.out.println("初始化service成功");
	}
	
	public List<Tiny_class> fetchBySubject(String subject)
	{
		List<Tiny_class> tiny_classes=new ArrayList<Tiny_class>();
		
		SQLiteDatabase db = dbOH.getWritableDatabase();
		String sql = "select * from Tiny_class where t_subject='"+subject+"'";
		System.out.println(sql);
		
		Cursor cursor = db.rawQuery(sql, null);
	
		while (cursor.moveToNext())
		{	
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int selected=cursor.getInt(cursor.getColumnIndex("selected"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String xml = cursor.getString(cursor.getColumnIndex("xml"));
			String pic = cursor.getString(cursor.getColumnIndex("pic"));
			
			tiny_classes.add(new Tiny_class(id,selected,name,xml,pic));
		}
		
		return tiny_classes;
	}
	
	public List<Tiny_class> fetchSelected()
	{
		System.out.println("按喝酒地方来");
		
		List<Tiny_class> tiny_classes=new ArrayList<Tiny_class>();
		SQLiteDatabase db = dbOH.getWritableDatabase();
		String sql = "select * from Tiny_class where selected=1";
		Cursor cursor = db.rawQuery(sql, null);
		System.out.println("开始！！！！");
		while (cursor.moveToNext())
		{	
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int selected=cursor.getInt(cursor.getColumnIndex("selected"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String xml = cursor.getString(cursor.getColumnIndex("xml"));
			String pic = cursor.getString(cursor.getColumnIndex("pic"));
			System.out.println(id+"");
			tiny_classes.add(new Tiny_class(id,selected,name,xml,pic));
		}

		return tiny_classes;
	}
	
	public void selected(String name)
	{
		SQLiteDatabase db = dbOH.getReadableDatabase();
		String sql="update Tiny_class set selected=1 where name='"+name+"'";
		db.execSQL(sql);
	}
	
	public void disSelected(String name)
	{
		SQLiteDatabase db = dbOH.getReadableDatabase();
		String sql="update Tiny_class set selected=0 where name='"+name+"'";
		db.execSQL(sql);
	}
}
