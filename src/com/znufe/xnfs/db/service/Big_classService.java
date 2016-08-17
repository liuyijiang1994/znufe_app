package com.znufe.xnfs.db.service;

import java.util.ArrayList;
import java.util.List;

import com.znufe.xnfs.db.entity.Big_class;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Big_classService {
	private DBOpenHelper dbOH;

	public Big_classService(Context context) 
	{
		this.dbOH = new DBOpenHelper(context);
	}
	
	
	public List<Big_class> fetchAll()
	{
		List<Big_class> bigClasses=new ArrayList<Big_class>();
		
		SQLiteDatabase db = dbOH.getWritableDatabase();
		String sql = "select * from Big_class";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext())
		{	
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String subject = cursor.getString(cursor.getColumnIndex("subject"));
			String brief = cursor.getString(cursor.getColumnIndex("brief"));
			String other=cursor.getString(cursor.getColumnIndex("other"));
			
			bigClasses.add(new Big_class(subject,name,brief,other));
		}
		
		return bigClasses;
	}

	
	
}
