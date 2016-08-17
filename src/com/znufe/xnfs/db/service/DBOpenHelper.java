package com.znufe.xnfs.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "db_xnfs", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table Big_class ( subject VARCHAR(20) PRIMARY KEY ,name VARCHAR(20)NOT NULL,brief VARCHAR(50) , other VARCHAR(50))");
		db.execSQL("create table Tiny_class(id  int identity(1,1) primary key,  t_subject  VARCHAR(20) NOT NULL,  selected  int not null,  name  VARCHAR(20) NOT NULL,  xml VARCHAR(1000) NOT NULL,  pic VARCHAR(100) NOT NULL,  other VARCHAR(50))");
		
		db.execSQL("insert into Big_class values('gnpd','国内频道','提供最新最全国内新闻','')");
		db.execSQL("insert into Big_class values('gjpd','国际频道','提供最新最全国际新闻','')");
		db.execSQL("insert into Big_class values('shpd','社会频道','社会百态,奇闻异事','')");
		db.execSQL("insert into Big_class  values('typd','体育频道','体坛精彩,争分夺秒','')");
		db.execSQL("insert into Big_class values('kjpd','科教频道','科教资讯,考试专题','')");
		db.execSQL("insert into Big_class values('cjpd','财经频道','权威财经,实时更新','')");
		
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gnpd',0,'腾讯网国内频道',' http://news.qq.com/newsgn/rss_newsgn.xml','txw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gjpd',0,'腾讯网国际频道',' http://news.qq.com/newsgj/rss_newswj.xml','txw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('shpd',0,'腾讯网社会频道',' http://news.qq.com/newssh/rss_newssh.xml','txw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('typd',0,'腾讯网体育频道',' http://sports.qq.com/others/rss_others.xml','txw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('kjpd',0,'腾讯网科教频道',' http://edu.qq.com/edunew/rss_edunew.xml','txw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('cjpd',0,'腾讯网财经频道',' http://finance.qq.com/financenews/domestic/rss_domestic.xml','txw',' ')");
		
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gnpd',0,'网易国内频道',' http://news.163.com/special/00011K6L/rss_gn.xml','wyxw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gjpd',0,'网易国际频道',' http://news.163.com/special/00011K6L/rss_gj.xml','wyxw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('shpd',0,'网易社会频道','  http://news.163.com/special/00011K6L/rss_sh.xml','wyxw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('kjpd',0,'网易科教频道','  http://news.163.com/special/00011K6L/rss_discovery.xml','wyxw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('cjpd',0,'网易财经频道','  http://money.163.com/special/00252EQ2/yaowenrss.xml','wyxw',' ' )");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('typd',0,'网易体育频道','  http://sports.qq.com/basket/nba/nbarep/rss_nbarep.xml','wyxw',' ')");
		
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gnpd',0,'凤凰网国内频道',' http://news.ifeng.com/rss/index.xml','fhw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gjpd',0,'凤凰网国际频道',' http://news.ifeng.com/rss/world.xml','fhw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('shpd',0,'凤凰网社会频道',' http://news.ifeng.com/rss/society.xml','fhw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('typd',0,'凤凰网体育频道',' http://news.ifeng.com/sports/rss/index.xml','fhw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('kjpd',0,'凤凰网科教频道',' http://news.ifeng.com/history/rss/index.xml','fhw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('cjpd',0,'凤凰网财经频道',' http://news.ifeng.com/opinion/rss/index.xml','fhw',' ')");
		
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gnpd',0,'大洋网国内频道',' http://rss.dayoo.com/feed/news/china.xml','dyw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gjpd',0,'大洋网国际频道',' http://rss.dayoo.com/feed/news/world.xml','dyw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('shpd',0,'大洋网社会频道',' http://rss.dayoo.com/feed/news/society.xml','dyw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('typd',0,'大洋网体育频道',' http://rss.dayoo.com/feed/life/auto.xml','dyw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('kjpd',0,'大洋网科教频道',' http://rss.dayoo.com/feed/life/edu.xml','dyw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('cjpd',0,'大洋网财经频道',' http://rss.dayoo.com/feed/news/finance.xml','dyw',' ')");
		
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gnpd',0,'人民网国内频道',' http://www.people.com.cn/rss/politics.xml','rmw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('gjpd',0,'人民网国际频道',' http://www.people.com.cn/rss/world.xml','rmw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('shpd',0,'人民网社会频道',' http://www.people.com.cn/rss/media.xml','rmw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('typd',0,'人民网体育频道',' http://www.people.com.cn/rss/sports.xml','rmw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('kjpd',0,'人民网科教频道',' http://www.people.com.cn/rss/edu.xml','rmw',' ')");
		db.execSQL("insert into Tiny_class(t_subject,selected,name,xml,pic,other) values('cjpd',0,'人民网财经频道',' http://www.people.com.cn/rss/finance.xml','rmw',' ')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
