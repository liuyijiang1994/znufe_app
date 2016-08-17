package com.kelly.util; 
import com.example.testappweight.AppWidget;

import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
public class ToDoDB extends SQLiteOpenHelper
{
	private final static  String DATABASE_NAME = "todo_db";
	private final static  int DATABASE_VERSION = 3;

	private final  String REMIND_TABLE = "todo_table";
	private final  String SCHEDULE_TABLE = "todo_schedule"; 

	public final  String FIELD_id = "_id";

	public final  String REMIND_TV = "todo_remind"; 
	public final  String REMIND_TIME = "todo_remind_time"; 
	public final  String REMIND_TIME_ID = "todo_remind_timeId"; 

	public final  String SCHEDULE_WEEK = "todo_week";
	public final  String SCHEDULE_TV1 = "todo_section";
	public final  String SCHEDULE_TV2 = "todo_course"; 
	public final  String SCHEDULE_TV3 = "todo_add";
	public final  String SCHEDULE_TV4 = "todo_singleOrDouble";
	public final  String SCHEDULE_TV5 = "todo_stuid";
	public final  String SCHEDULE_TV6 = "todo_classLen";
	public final  String SCHEDULE_TV7 = "todo_dbVersion";
	//  public SQLiteDatabase db;


	public ToDoDB(Context context) 
	{ 
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//  	 this.db=db;
		//  	 
		//  	 intiTable();
		//  }
		//  
		//  public void intiTable() {
		//	// TODO Auto-generated method stub

		/* ��btable */ 
		String sql = "CREATE TABLE " 
				+ REMIND_TABLE 
				+ " ("
				+ FIELD_id +" INTEGER primary key autoincrement, "
				+ REMIND_TV + " text, "
				+ REMIND_TIME + " text, "
				+ REMIND_TIME_ID + " text "
				+" )"; 
		Log.i("ToDoDB", "sql1��������:"+sql);
		db.execSQL(sql); 

		sql =     "CREATE TABLE " 
				+ SCHEDULE_TABLE 
				+ " (" 
				+ FIELD_id +" INTEGER primary key autoincrement, "+ " " 
				+ SCHEDULE_WEEK + " text, " 
				+ SCHEDULE_TV1 + " text, " 
				+ SCHEDULE_TV2 + " text, "
				+ SCHEDULE_TV4 + " text, "
				+ SCHEDULE_TV5 + " text, "
				+ SCHEDULE_TV6 + " text, "
				+ SCHEDULE_TV7 + " text, "
				+ SCHEDULE_TV3 + " text )"; 
		Log.i("ToDoDB", "sql2��������:"+sql);
		db.execSQL(sql); 
		try{
			//ToDoDB_Schedule toDoDB_Schedule=new ToDoDB_Schedule(ScheduleInsert.this, "Schedule");
			//SQLiteDatabase db=toDoDB_Schedule.getWritableDatabase();
			db.execSQL("drop table todo_schedule");
			db.execSQL("create table if not exists todo_schedule(_id int primary key,todo_week int,todo_section int,todo_classLen int,todo_singleOrDouble varchar,todo_course varchar,todo_add varchar,todo_stuid varchar,todo_dbVersion int)"); 
			/*db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(1,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(2,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(3,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(4,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(5,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(6,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(7,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(8,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(9,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(10,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(11,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(12,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(13,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(14,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(15,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(16,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(17,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(18,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(19,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(20,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(21,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(22,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(23,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(24,'','','','','','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_singleOrDouble,todo_course,todo_add,todo_stuid) values(25,'','','','','','')");*/
		}catch (Exception e) {
			// TODO: handle exception
		}
	} 

	//  @Override
	//  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	//  { 
	//    String sql = "DROP TABLE IF EXISTS " 
	//      + REMIND_TABLE; 
	//    db.execSQL(sql);
	//      onCreate(db); 
	//      }
	public Cursor selectRemind() 
	{ 
		SQLiteDatabase db = this.getReadableDatabase();
		String orderBy = REMIND_TIME_ID+" desc";
		Cursor cursor = db.query(REMIND_TABLE, null, null, null, null, null, orderBy);
		return cursor;
	}
	public long insertRemind(String text,String time,String timeStr)
	{ 
		SQLiteDatabase db = this.getWritableDatabase();
		/* �������ֵ����ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(REMIND_TV, text);
		cv.put(REMIND_TIME, time);
		cv.put(REMIND_TIME_ID, timeStr); 
		long row = db.insert(REMIND_TABLE, null, cv); 

		return row; 
	} 
	public void delete(int id,String table) 
	{ 
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?"; 
		String[] whereValue = { Integer.toString(id) };
		db.delete(table, where, whereValue); 
	} 
	public void updateRemind(int id, String text, String time,String timeStr) 
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(REMIND_TV, text); 
		cv.put(REMIND_TIME, time); 
		cv.put(REMIND_TIME_ID, timeStr); 
		db.update(REMIND_TABLE, cv, where, whereValue); 
	}

	public void updateCourse(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV2, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updateSingleOrDouble(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV4, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updateStuid(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV5, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updateClassLen(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV6, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updateWeek(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_WEEK, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updatSection(int id, String text) 
	{ 

		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV1, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}
	public void updateAdd(int id, String text) 
	{ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) }; 
		/* ���޸ĵ�ֵ����ContentValues */ 
		ContentValues cv = new ContentValues(); 
		cv.put(SCHEDULE_TV3, text); 
		db.update(SCHEDULE_TABLE, cv, where, whereValue); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS todo_schedule");  
        onCreate(db); 
	}

}