package net.shopnc.android.common;

import android.os.Environment;

/**
 * 常量类
 * @author qjyong
 */
public final class Constants {
	/** 系统初始化配置文件名 */
	public static final String SYSTEM_INIT_FILE_NAME = "sysini";
	/** 本地缓存目录 */
	public static final String CACHE_DIR;
	/** 表情缓存目录 */
	public static final String CACHE_DIR_SMILEY;
	/** 图片缓存目录 */
	public static final String CACHE_DIR_IMAGE;
	/** 待上传图片缓存目录 */
	public static final String CACHE_DIR_UPLOADING_IMG;

	static {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VeryNC/";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/VeryNC/";
		}
		
		CACHE_DIR_SMILEY = CACHE_DIR + "/smiley";
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
	}
	private Constants(){}
	/**************************************************************************/
	/** 数据库版本号 */
	public static final int DB_VERSION = 2;
	/** 数据库名 */
	public static final String DB_NAME = "verync.db";
	
	/** 用户信息 */
	public static final String SQL_USER_CREATE = "CREATE TABLE user(authorid varchar(64) primary key, author varchar(64), pwd varchar(64), sessionid varchar(64))";
	public static final String SQL_USER_DROP = "DROP TABLE user";
	public static final String SQL_USER_SELECT_ALL = "SELECT * FROM user";
	public static final String SQL_USER_SELECT_BY = "SELECT * FROM user WHERE author=''{0}''";
	public static final String SQL_USER_INSERT = "INSERT INTO user(authorid, author, pwd, sessionid) VALUES(?,?,?,?)";
	public static final String SQL_USER_DELETE_BY = "DELETE FROM user WHERE author=?";
	
	/** 本地收藏 */
	public static final String SQL_FAVORITE_CREATE = "CREATE TABLE favorite(fid long primary key, fup long, name varchar(255), type varchar(64), owner varchar(64))";
	public static final String SQL_FAVORITE_DROP = "DROP TABLE favorite";
	public static final String SQL_FAVORITE_SELECT_BY_FID = "SELECT * FROM favorite WHERE fid={0}";
	public static final String SQL_FAVORITE_SELECT_OWNER = "SELECT * FROM favorite WHERE owner=''{0}''";
	public static final String SQL_FAVORITE_SELECT_LIMIT = "SELECT * FROM favorite WHERE owner=''{0}'' limit {1}, {2}";
	public static final String SQL_FAVORITE_INSERT = "INSERT INTO favorite(fid, fup, name, type, owner) VALUES (?,?,?,?,?)";
	/** 刪除表中記錄，SQLite没有trunct语句*/
	public static final String SQL_FAVORITE_DELETE = "DELETE FROM favorite";
	public static final String SQL_FAVORITE_RESET_SEQUENCE = "UPDATE sqlite_sequence SET seq=0 WHERE name='favorite'";
	
	/**最近浏览*/
	public static final String SQL_LASTEST_BROWSE_CREATE = "CREATE TABLE lastest_browse(id integer primary key autoincrement, tid long, subject varchar(255), dateline long, author varchar(64), views varchar(32), replies varchar(32))";
	public static final String SQL_LASTEST_BROWSE_DROP = "DROP TABLE lastest_browse";
	public static final String SQL_LASTEST_BROWSE_SELECT_BY_TID = "SELECT * FROM lastest_browse WHERE tid=";
	public static final String SQL_LASTEST_BROWSE_SELECT_LIMIT = "SELECT * FROM lastest_browse ORDER BY id DESC LIMIT {0},{1}";
	public static final String SQL_LASTEST_BROWSE_INSERT = "INSERT INTO lastest_browse(tid, subject, dateline, author, views, replies) VALUES(?,?,?,?,?,?)";
	public static final String SQL_LASTEST_BROWSE_DELETE_BY_TID = "DELETE FROM lastest_browse WHERE tid=?";
	public static final String SQL_LASTEST_BROWSE_UPDATE_BY_TID = "UPDATE lastest_browse SET id=last_insert_rowid()+1 WHERE tid=?"; 
	public static final String SQL_LASTEST_BROWSE_DELETE = "DELETE FROM lastest_browse";
	public static final String SQL_LASTEST_BROWSE_RESET_SEQUENCE = "UPDATE sqlite_sequence SET seq=0 WHERE name='lastest_browse'";
	
	/**************************************************************************/
	/** 用于标识请求照相功能的activity结果码*/    
	public static final int RESULT_CODE_CAMERA = 1;    
	/** 用来标识请求相册gallery的activity结果码*/    
	public static final int RESULT_CODE_PHOTO_PICKED = 2; 
	/** 用来标识图片剪辑的结果码*/
	public static final int RESULT_CODE_PHOTO_CUT = 3;
	/** 图片类型 */
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	/** 回复图片的匹配路径*/
	public static final String HUIFU = "common/back.gif";
	/**************************************************************************/
	/** 远程加载数据的提示框ID */
	public final static int DIALOG_LOADDATA_ID = 1;
	/** 关闭应用程序的提示框ID */
	public final static int DIALOG_EXITAPP_ID = 2;
	/** 登录等待时的提示框ID */
	public final static int DIALOG_LOGIN_ID = 3;
	/** 切换用户的提示框ID */
	public final static int DIALOG_USER_CHANGE_ID = 4;
	/** 发帖和回帖的提示框ID */
	public final static int DIALOG_SENT_TOPIC_ID = 5;
	/** GPS定位的提示框ID */
	public final static int DIALOG_LOCATION_ID = 6;
	/** 清除緩存的提示框ID */
	public final static int DIALOG_CLEAR_CACHE_ID = 7;
	/** 帖子排序的提示框ID */
	public static final int ORDERBY_ID = 8;
	/** 帖子加载数据提示框ID */
	//public static final int ORDERBY_LOADING_ID = 9;
	/** 最后帖子加载数据提示框ID */
	//public static final int LAST_ORDERBY_LOADING_ID = 10;
	/**************************************************************************/
	/** 与服务器端连接的协议名 */
	public static final String PROTOCOL = "http://";
	/** 服务器IP */
	public static final String HOST = "202.114.234.122:8234";//www.shopnctest.com//"10.0.2.2";//AVD test
	/** 服务器端口号 */
	public static final String PORT = "8234";
	/** 应用上下文名 */
	public static final String APP = "/lt";///dx2app/app2//"/app_mob";//
	/** 应用上下文完整路径 */
	public static final String URL_CONTEXTPATH = PROTOCOL + HOST + APP;
	
	/** 通用帖子列表URL */
	public static final String URL_COMMON_TOPIC_LIST = URL_CONTEXTPATH + "/topiclist.php?";

	/** 请求参数名：类型 */
	public static final String PARAM_TYPE = "type";
	/** 请求参数名：当前页号 */
	public static final String PARAM_PAGENO = "pageno";
	/** 请求参数名：每页显示记录数 */
	public static final String PARAM_PAGESIZE = "pagesize";
	
	/************************ 二级栏目名称 ***************************************/
	public static final String URL_TOP_NAME = URL_COMMON_TOPIC_LIST + PARAM_TYPE + "=top_name_list";
	/************************ 版块主题分类 ***************************************/
	public static final String URL_TOPIC_TYPE = URL_CONTEXTPATH + "/topicpost.php?" + PARAM_TYPE + "=thread_class&fid=";

	
	/** 统计安装数量*/
	public static final String URL_INSTALL=URL_CONTEXTPATH+"/advice.php?"+ PARAM_TYPE +"=count";
	
	/************************ 首页 **********************************************/
	/** 头条大图 pagesize=1,pageno=1 */
	public static final String URL_HOME_TOP = URL_COMMON_TOPIC_LIST + PARAM_TYPE + "=top";
	/** 头条列表pagesize=20,pageno=1 */
	public static final String URL_HOME_TOPS = URL_COMMON_TOPIC_LIST + PARAM_TYPE + "=tops";
	/** 茶座 */
	public static final String URL_HOME_TEAHOUSE = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=index2";
	/** 焦点 */
	public static final String URL_HOME_FOCUS = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=index3";
	/** 娱乐 */
	public static final String URL_HOME_ENTERTAINMENT = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=index4";
	/** 情感 */
	public static final String URL_HOME_EMOTIONAL = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=index5";

	/************************ 生活 **********************************************/
	/** 家装 */
	public static final String URL_LIVE_FIRST = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=second1";
	/** 美食 */
	public static final String URL_LIVE_SECOND = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=second2";
	/** 汽车 */
	public static final String URL_LIVE_THIRD = URL_COMMON_TOPIC_LIST + PARAM_TYPE 
			+ "=second3";		
	/** 婚嫁 */
	public static final String URL_LIVE_FORTH = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=second4";
	/** 母婴 */
	public static final String URL_LIVE_FIFTH = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=second5";
	/** 女性 */
	public static final String URL_LIVE_SIXTH = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=second6";
	
	
	/************************ 信息 **********************************************/
	/** 人才 */
	public static final String URL_INFO_01 = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=third1";
	/** 房产 */
	public static final String URL_INFO_02 = URL_COMMON_TOPIC_LIST + PARAM_TYPE 
			+ "=third2";
	/** 二手 */
	public static final String URL_INFO_03 = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=third3";
	/** 促销 */
	public static final String URL_INFO_04 = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=third4";
	/** 活动 */
	public static final String URL_INFO_05 = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=third5";
	/** 团购 */
	public static final String URL_INFO_06 = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=third6";
	

	/************************ 商圈 **********************************************/
	/**
	 * 搜索指定范围内的商家<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/zuobiao.php?type=shop_list&lat=39.1178&lng=117.15&r=2000<br/>
	 * GET请求参数: lat（使用者纬度） lng（使用者经度） r（搜索半径范围，单位：米）500米、1000米、2000米或5000米<br/>
	 * 响应数据：从近到远的商家信息包括shop_id,shop_name,shop_pic,shop_website,shop_lat,shop_lng,distance(距离，单位：米)<br/>
	 */
	public static final String URL_DISTRICT_SHOP_LIST = URL_CONTEXTPATH + "/zuobiao.php?type=shop_list";
	
	/**
	 * 谷歌提供的地址反向解析URL路径
	 */
	public static final String URL_GOOGLE_REVERSE_GEOCODING = "http://maps.googleapis.com/maps/api/geocode/json?latlng={0},{1}&sensor=true&language=zh_CN";
	/**
	 * 商家详细信息<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/zuobiao.php?type=shop_detail&shop_id=1<br/>
	 * GET请求参数: shop_id（商家ID）<br/>
	 * 响应数据: 商家的所有信息包括shop_id,shop_name,shop_pic,shop_info,shop_address,shop_phone,shop_website,shop_lat,shop_lng<br/>
	 */
	public static final String URL_DISTRICT_SHOP_DETAIL = URL_CONTEXTPATH + "/zuobiao.php?type=shop_detail";
	
	/************************ 论坛 **********************************************/
	/**
	 * 版块列表<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=forum_list&uid=【用户ID】
	 */
	public static final String URL_BOARD = URL_COMMON_TOPIC_LIST + PARAM_TYPE + "=forum_list&uid=";

	/**
	 * 选定版块下的帖子列表<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=thread_list&fid=2&pageno=1&pagesize=20
	 */
	public static final String URL_BOARD_TOPIC_LIST = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=thread_list&fid=";

	/**
	 * 选定版块下的置顶帖子列表 <br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=top_thread&fid=2&pageno=1&pagesize=20
	 */
	public static final String URL_BOARD_TOPIC_TOP = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=top_thread&fid=";
	/**
	 * 选定版块下的精华帖子列表<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=digest_thread&fid=2&pageno=1&pagesize=20
	 */
	public static final String URL_BOARD_TOPIC_DIGEST = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=digest_thread&fid=";

	/**
	 * 选定版块下的帖子搜索:keyword字段为中文，需要经过url编码 <br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=search_thread&fid=2&keyword=1&pageno=1&pagesize=20<br/>
	 * 说明：字符串中除了 -_. 之外的所有非字母数字字符都将被替换成百分号（%）后跟两位十六进制数，空格则编码为加号（+）。此编码与 WWW 表单 POST 数据的编码方式是一样的，同时与 application/x-www-form-urlencoded 的媒体类型编码方式一样。
	 */
	public static final String URL_BOARD_TOPIC_SEARCH = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=search_thread&fid=";

	/**
	 * 帖子详情-默认全部显示<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=thread_detail&tid=3&pageno=1&pagesize=20<br/>
	 * 响应数据：<br/>
	 *  "subject": "茶座测试1", subject为空的就是回复<br/>
	 *  "message": "aaa ", <br/>
	 *  "dateline": "1330658210", <br/>
	 *  "author": "shopnc_admin"<br/>
	 *  "avatar_url": "xxx.jpg"
	 */
	public static final String URL_TOPIC_DETAIL_DEFAULT = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=thread_detail&tid=";
	
	/**
	 * 帖子详情-只看楼主<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=thread_detail&tid=3&pageno=1&pagesize=20&addtype=only_owner
	 */
	public static final String URL_TOPIC_DETAIL_LANDLOAD = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=thread_detail&addtype=only_owner&tid=";
	
	/**
	 * 帖子详情-最新回复<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topiclist.php?type=thread_detail&tid=3&pageno=1&pagesize=20&addtype=last_post
	 */
	public static final String URL_TOPIC_DETAIL_REPLY = URL_COMMON_TOPIC_LIST + PARAM_TYPE
			+ "=thread_detail&addtype=last_post&tid=";
	
	/**************************************************************************/
	/**
	 * 登录<br/>
	 * 请求URL: BASEPATH/login.php?type=onlinedo<br/>
	 * POST请求数据：发送useracc和userpw两个字段，userpw字段需要md5，分别代表用户名和密码。<br/>
	 * 返回数据：<br/>
	 *  成功返回200，分为两种情况：<br/>
	 *  登陆成功返回sessionid字符串；{"code":"200","datas":[{"sessionid":"7e54a80ab3ecbddfbd8e5e1a09ad1a35"}]}<br/>
	 *  账号或密码错误，返回sessionid字段，值为aperror。<br/>{"code":"200","datas":[{"sessionid":"aperror"}]}<br/>
	 *  请求地址type错误，或规定字段不符合要求都会返回404错误。{"code":"404","datas":[]}<br/>
	 *  请求正确，服务器处理失败或服务器错误会返回500错误。{"code":"500","datas":[]}<br/>
	 */
	public static final String URL_LOGIN = URL_CONTEXTPATH + "/login.php?" + PARAM_TYPE + "=onlinedo";

	/**
	 * 发帖<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topicpost.php?type=post&fid=板块id<br/>
	 * 请求参数: <br/>
	 *  GET参数：type=post,fid=板块ID,img=1（当需要上传图片时，img需设为1，没图时候img字段可省略）<br/>
	 *  POST参数：subject(标题),author(发帖用户名),authorid(发帖用户ID),message(帖子主体信息),img[](上传图片信息，可上传多图)<br/>
	 * 响应数据：<br/>
	 *  发帖成功 code:200, result:post succ<br/>
     *  发帖失败 code:500, result:post failed<br/>
     *  错误 code:404
	 */
	public static final String URL_TOPIC_SEND = URL_CONTEXTPATH + "/topicpost.php?" + PARAM_TYPE + "=post&fid=";

	/**
	 * 回帖<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topicpost.php?type=reply&fid=板块id<br/>
	 * GET参数：type=reply,fid=板块ID,tid=帖子ID,img=1（发图片时添加此参数，可选）<br/>
	 * POST参数：subject(标题),author(发帖用户名),authorid(发帖用户ID),message(帖子主体信息),img[](上传图片信息，可上传多图)<br/>
	 * 响应：<br/>
	 * 回复成功 code:200, result:reply succ<br/>
	 * 回复失败 code:200, result:reply failed<br/>
	 * 错误 code:404<br/>
	 */
	public static final String URL_TOPIC_REPLY = URL_CONTEXTPATH + "/topicpost.php?" + PARAM_TYPE + "=reply&fid=";
	
	/** 
	 * 表情路径<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/topicpost.php?type=smiley 
	 */
	public static final String URL_SMILEY = URL_CONTEXTPATH + "/topicpost.php?" + PARAM_TYPE +"=smiley";
	
	/**************************************************************************/
	/** 
	 * 意见反馈<br/>
	 * 请求URL: http://www.shopnctest.com/dx2app/app2/advice.php?type=submit
	 */
	public static final String URL_FEEDBACK = URL_CONTEXTPATH + "/advice.php?"+ PARAM_TYPE +"=submit";
}
