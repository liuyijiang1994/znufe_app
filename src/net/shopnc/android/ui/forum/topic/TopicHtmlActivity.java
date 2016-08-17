package net.shopnc.android.ui.forum.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.common.BBCodeHelper;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.DateAndTimeHepler;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.model.Board;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.more.LoginActivity;
import net.shopnc.android.widget.MyProcessDialog;

import org.apache.http.HttpStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hjgang
 */
public class TopicHtmlActivity extends Activity {
	public static final String TAG = "TopicHtmlActivity";
	private WebView html_web;
	private TopicDetailActivity parent;
	private MyApp myApp;
	private int pagesize;
	private int pageno = 1;
	private long count = 0;
	private long totalpage = 0;
	private MyProcessDialog mydialog;
	private TextView txt_title;
	private ImageButton btn_right;
	private ImageButton btn_pager_prev;
	private ImageButton btn_pager_next;
	private TextView txt_pager_info;
	private ImageButton btn_board_favorite;
	private TextView txt_topic_title;
	private String subject;
	private long tid;
	private long fid;
	private String intent_url;
	private String url;
	private boolean display_postStarter_only;
	@SuppressWarnings("unused")
	private int isreply;
	@SuppressWarnings("unused")
	private int ispostimage;
	private ArrayList<Topic> datas=new ArrayList<Topic>();
	private StringBuffer str;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toipc_html);
		parent = (TopicDetailActivity) this.getParent();
		myApp = (MyApp) this.getApplicationContext();
		pagesize = myApp.getPageSize();
		//pagesize = 2;
		display_postStarter_only = myApp.isDisplay_postStarter_only();

		Intent intent = parent.getIntent();
		Topic t = (Topic) intent.getExtras().get(Topic.TOPIC_TAG);
		tid = t.getTid();
		// fid= t.getFid();
		subject = t.getSubject();
		isreply = intent.getIntExtra("isreply", 0);
		ispostimage = intent.getIntExtra("ispostimage", 0);
		//Log.d(TAG, "isreply=" + isreply + ",ispostimage=" + ispostimage);

		// 设置主内容标题
		txt_topic_title = (TextView) this.findViewById(R.id.topic_title);
		txt_topic_title.setText(this.subject);
		
		html_web = (WebView) findViewById(R.id.htmlweb);
		html_web.getSettings().setJavaScriptEnabled(true);
		
		// 设置标题栏标题
		txt_title = (TextView) parent.findViewById(R.id.txt_title);
		//Log.d(TAG, t.getSubject());
		txt_title.setText(t.getSubject());

		intent_url = this.getIntent().getStringExtra("url");
		
		// 记录浏览历史
		myApp.getLastestBrowseDao().record(t);

		if (display_postStarter_only
				&& intent_url.equals(Constants.URL_TOPIC_DETAIL_DEFAULT)) {
			url = Constants.URL_TOPIC_DETAIL_LANDLOAD;
		} else {
			url = intent_url;
		}
		initPagerBar();
		loadPage(1);
		/******/
		// //////////////////////////////////////
		
	}
	@Override
	protected void onResume() {
		super.onResume();

		boolean temp = myApp.isDisplay_postStarter_only();
		// 如果只显示楼主帖子且是默认显示界面
		if (display_postStarter_only != temp
				&& url.equals(Constants.URL_TOPIC_DETAIL_DEFAULT)) {
			display_postStarter_only = temp;
			if (display_postStarter_only) {
				url = Constants.URL_TOPIC_DETAIL_LANDLOAD;
			} else {
				url = intent_url;
			}
//			pv.startUpdate();
			loadPage(pageno);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 100: // 回帖成功
			Log.d(TAG, "post succ, load datas");
			pageno = 1;
			loadPage(pageno);
			break;
		case 200: // 登录成功
			btn_right.setBackgroundResource(R.drawable.btn_replytopic);
			loadPage(pageno);
			break;
		case 300: // 引用回复成功
			pageno = 1;
			loadPage(pageno);
			break;
		}
	}
	private void initTitleBar() {
		btn_right = (ImageButton) parent.findViewById(R.id.btn_right);

		if (null != myApp.getUid() && !"".equals(myApp.getUid())
				&& null != myApp.getSid() && !"".equals(myApp.getSid())) {
			btn_right.setBackgroundResource(R.drawable.btn_replytopic);
		} else {
			btn_right.setBackgroundResource(R.drawable.btn_login);
		}
			btn_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != myApp.getUid() && !"".equals(myApp.getUid())
							&& null != myApp.getSid() && !"".equals(myApp.getSid())) {// 跳转到回帖
						// QIUJY 回帖权限判断
						HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
						//Log.d(TAG, subBoardMap.toString());
						System.out.println("subBoardMap.toString() ==>"+subBoardMap.toString());
						if (subBoardMap != null) {
							Board b = subBoardMap.get(Long.valueOf(fid));
							System.out.println("b.getIsreply()-->"+b.getIsreply());
							//if (b.getIsreply() == 1) {
								Intent intent = new Intent(
										TopicHtmlActivity.this,
										ReplyTopicActivity.class);
								intent.putExtra("fid",TopicHtmlActivity.this.fid);
								intent.putExtra("tid",TopicHtmlActivity.this.tid);
								intent.putExtra("ispostimage", b.getIspostimage());
								TopicHtmlActivity.this
										.startActivityForResult(intent, 100);
						/*	} else {
								Toast.makeText(TopicHtmlActivity.this,
										"你没有权限在本版回帖！", Toast.LENGTH_SHORT).show();
							}*/
						}
					} else {// 未登录先跳转到登录
							// Toast.makeText(TopicDetailDefaultActivity.this,
							// "回帖请先登录！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(TopicHtmlActivity.this,
								LoginActivity.class);
						TopicHtmlActivity.this.startActivityForResult(
								intent, 200);
					}
				}
			});
	}
	private void loadPage(final int pageno) {
		showDialog(Constants.ORDERBY_ID);
		mydialog.setMsg(getString(R.string.pull_to_refresh_refreshing));
		if (-1 == SystemHelper.getNetworkType(this)) {
			Toast.makeText(TopicHtmlActivity.this, "网络连接失败，请检查设备!",
					Toast.LENGTH_SHORT).show();
//			pv.endUpdate();
			dismissDialog(Constants.ORDERBY_ID);
			return;
		}
		RemoteDataHandler.asyncGet(url + tid, pagesize, pageno,
				new RemoteDataHandler.Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
//						pv.endUpdate(); // 更新完成后的回调方法,用于隐藏刷新面板
						dismissDialog(Constants.ORDERBY_ID);
						str=new StringBuffer();
						if (data.getCode() == HttpStatus.SC_OK) {
							String json = data.getJson();
							json=json.replaceAll("\\\\n", "<br />");
							datas.clear();
							ArrayList<Topic> topics = Topic
									.newInstanceList(json);
							datas.addAll(topics);
//							adapter.setTid(tid);

//							adapter.setDatas(datas);
//							adapter.notifyDataSetChanged();

							// 设置分页信息...
							count = data.getCount();

//							adapter.setPagesize(pagesize);
//							adapter.setPageno(pageno);
							
							if (count > 0) {
								fid = topics.get(0).getFid(); // 记录版块ID
								
								if(fid!=0){
									initTitleBar();
								}
								totalpage = ((count + pagesize - 1) / pagesize);
								txt_pager_info
										.setText(pageno + "/" + totalpage);
							}
							str.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
							str.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"format-detection\" content=\"telephone=no\"><style type=\"text/css\">body {background-color:f7f7f7; padding: 0; margin:0; font:14px / 1.5 \"Lucida Grande\",\"Lucida Sans Unicode\",Helvetica,Arial,Verdana,sans-serif;} img { border: 0;}table{border-collapse:collapse;border-spacing:0}.app{ width:300px; padding:4px;}.app1{ width:61px;}.app1 img{ width:48px; height:48px;}.app1_h1{color: #888; font-size: 12px;}.app1_h1 span{ color:#1D5796; display:block; font-size: 16px; line-height: 24px;}.app_con{ padding:4px 0;}.zoom{ padding:4px 0;}.app_con3{border-bottom:1px solid #F1F1F1;}.app1 img{border:1px solid #888; padding:2px;}.yinyong{ background:#F0F5FB; color:#1D5796; padding:4px;}.yinyong font{ background:url(/static/image/common/icon_quote_s.gif) no-repeat; padding-left: 20px; font-size: 12px; color: #888888;}.yinyong span{ background:url(/static/image/common/icon_quote_e.gif) no-repeat right; padding-right: 20px;}</style></head><body>");
							str.append("<div class=\"app\"><table border=0 width=\"100%\">");
							if(datas.size()!=0 && datas!=null){
								String louceng=new String();
								for(int i=0;i<datas.size();i++){
									Topic c=datas.get(i);
									long floor = (pageno - 1) * pagesize + i + 1;
									if(floor == 1){
										louceng="楼主";
									}else if(floor == 2){
										louceng="沙发";
									}else if(floor == 3){
										louceng="板凳";
									}else if(floor == 4){
										louceng="地板";
									}else{
										louceng=floor+"#";
									}
									if(url.equals(Constants.URL_TOPIC_DETAIL_LANDLOAD)){
										louceng="";
									}
									str.append("<tr><td style=\"padding-top:8px;\" class=\"app1\"><img src=\""+c.getAvatar()+"\"></td> <td class=\"app1_h1\"> <span>"+c.getAuthor()+"</span>发布日期:"+DateAndTimeHepler.friendly_time(TopicHtmlActivity.this,c.getDateline() * 1000)+"</td><td align=\"right\">"+louceng+"</td></tr>");
									String str2=c.getMessage();
									//判断帖子来源：iPhone Android
									String str3=new String();
									String yinyong="[/quote]";
									//static/image/
									int cc=c.getMessage().lastIndexOf(yinyong);
									int d=c.getMessage().lastIndexOf("本帖最后由");
									int e=c.getMessage().lastIndexOf("编辑");
									if(cc!=-1){
										str3=c.getMessage().substring(0,cc+8);
										str2=str2.replace(str3, "");
										str3=str3.replace("[img]"+"static/image/"+Constants.HUIFU+"[/img]","<br/>\t");
										str3=str3.replace("static/image/"+Constants.HUIFU,"<br/>\t");
										if(d!=-1 && e!=-1){
											str3=str3.replaceFirst(str3.substring(d, e+"编辑".length()),"");
										}
										str.append("<tr><td colspan=\"3\" class=\"app_con\"></td></tr><tr><td colspan=\"3\" class=\"app_con yinyong\"><img src= \"http://www.shopnctest.com/dx2app/static/image/common/icon_quote_s.gif\"></img><font>&nbsp  </font><br>"+"\t"+BBCodeHelper.processBBCode(str3)+"  &nbsp <img src= \"http://www.shopnctest.com/dx2app/static/image/common/icon_quote_e.gif\"></img><span></span></td></tr><tr><td colspan=\"3\" class=\"app_con\"></td></tr>");
									}else{
									}	
//									System.out.println("hjgang2211111---->"+c.getMessage());
 									String str4=BBCodeHelper.processBBCode(str2);
//									System.out.println("hjgang22222---->"+str4.toString());
									//str2=str2.replaceAll("<img", "<img style=\"width:100%;\"");
									str.append("<tr width=\"100%\"><td colspan=\"3\" class=\"app_con\">"+str4+"</td></tr>");
									boolean img_invisible = myApp.isImg_invisible();
									if (ConnectivityManager.TYPE_WIFI == SystemHelper
											.getNetworkType(TopicHtmlActivity.this)
											|| !img_invisible) {
										// 显示
										if(c.getPic_info()!=null){
											for(int j=0;j<c.getPic_info().length;j++){
												str.append("<tr><td colspan=\"3\" class=\"app_con\"><font color=blue>附件图片</font><br><img width=\"200\" src=\""+c.getPic_info()[j]+"\" /></td></tr>");
											}
										}
									} else {
//										str4 = BBCodeHelper
//												.parseHtmlExcludeImgTag(str4);
									}
									str.append("<tr class=\"app_con3\">");
									if(floor == 1){
										continue;
									}else{
										boolean f=isNumeric1(c.getStatus());
										if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
												&& null != myApp.getSid() && !"".equals(myApp.getSid())){
											HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
											//Log.d(TAG, subBoardMap.toString());
											if (subBoardMap != null) {
												Board b = subBoardMap.get(Long.valueOf(fid));
												if (b.getIsreply() == 1) {
												if(f!=true){
													str.append("<td align=\"left\" colspan=\"2\"><font size=\"2\" color=\"#888888\">"+c.getStatus()+"</font></td>");
													if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
															&& null != myApp.getSid() && !"".equals(myApp.getSid())){
														if(c.getSubject()!=null && !"".equals(c.getSubject())){
															
														}else{
															str.append("<td align=\"right\" style=\"padding-top:3px\" colspan=\"2\"><a href='#' onclick=\"window.demo.clickOnAndroid('"+String.valueOf(c.getPid())+"','"+String.valueOf(c.getTid())+"','"+String.valueOf(c.getFid())+"','"+String.valueOf(c.getAuthor())+"','"+String.valueOf(c.getMessage())+"','"+String.valueOf(c.getDateline())+"')\">引用回复</a></td></tr>");
														}
													}
												}else{
													if(f!=true){
														str.append("<td align=\"left\" colspan=\"2\"><font size=\"2\" color=\"#888888\">"+c.getStatus()+"</font></td>");
														if(null != myApp.getUid() && !"".equals(myApp.getUid()) 
																&& null != myApp.getSid() && !"".equals(myApp.getSid())){
															if(c.getSubject()!=null && !"".equals(c.getSubject())){
															}else{
																str.append("<td align=\"right\" style=\"padding-top:3px\" colspan=\"3\"><a href='#' onclick=\"window.demo.clickOnAndroid('"+String.valueOf(c.getPid())+"','"+String.valueOf(c.getTid())+"','"+String.valueOf(c.getFid())+"','"+String.valueOf(c.getAuthor())+"','"+String.valueOf(c.getMessage())+"','"+String.valueOf(c.getDateline())+"')\">引用回复</a></td></tr>");
															}
														}
													//<a href=\"quote.php?pid=%d&ptid=%d&author=%@&dateline=%@&message=%@\">引用回复</a>
												}	
											}}
										}
												}
									}
								}
							}	
							str.append("</table></div></body></html>");
//							DemoJSInterface localDemoJavaScriptInterface = new DemoJSInterface();
//							html_web.addJavascriptInterface(localDemoJavaScriptInterface, "apkshare");
							html_web.setHorizontalScrollBarEnabled(false);//水平不显示
							html_web.pageUp(true);
//							html_web.setVerticalScrollBarEnabled(false); //垂直不显示
							//html_web.getSettings().setUseWideViewPort(true); //双击放大
							final Handler  mHandler=new Handler();
							html_web.addJavascriptInterface(new Object() { 
					            @SuppressWarnings("unused")
								public void clickOnAndroid(final String pid,final String tid,final String fid,final String author,final String message,final String datetime) { 
					                mHandler.post(new Runnable() { 
					                    public void run() { 
					                       String mage=null;
					                        HashMap<Long, Board> subBoardMap = myApp.getSubBoardMap();
											if(subBoardMap != null){
												Board b2 = subBoardMap.get(Long.valueOf(fid));
												Log.d(TAG, b2.toString());
												if(b2.getIsreply() == 1){
													Intent it = new Intent(TopicHtmlActivity.this, QuoteTopicActivity.class);
													it.putExtra("author", author);
													String str="[/quote]";
													int a=message.lastIndexOf(str);
													if(a!=-1){
														mage=message.replace(message.substring(0,a), "");
														mage=mage.replace("[/quote]", "");
													}else{
														mage=message;
													}
													int b=mage.lastIndexOf("[img");
													int c=mage.lastIndexOf("[/img]");
													if(b!=-1&&c!=-1){
														mage=mage.replace(mage.substring(b,c+6), "");
													}
													it.putExtra("mage",mage);
													it.putExtra("tid", tid);
													it.putExtra("fid",fid);
													it.putExtra("pid",pid);
													it.putExtra("date",datetime);
													it.putExtra("ispostimage",String.valueOf(b2.getIspostimage()));
													TopicHtmlActivity.this.startActivityForResult(it, 300);
												}else{
													Toast.makeText(TopicHtmlActivity.this, "你没有权限在本版回帖！", Toast.LENGTH_SHORT).show();
												}
										}
					                    } 
					                }); 
					            } 
					        }, "demo");
							html_web.loadDataWithBaseURL(null, str.toString(), "text/html",
									"utf-8", null);

						} else if (data.getCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
							Toast.makeText(TopicHtmlActivity.this,
									"网络数据有误，请刷新重试!", Toast.LENGTH_SHORT).show();
						} else if (data.getCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
							Toast.makeText(TopicHtmlActivity.this,
									"网络状况不好，请刷新重试!", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	public static boolean isNumeric1(String str){
		  Pattern pattern = Pattern.compile("[0-9]*");
		  return pattern.matcher(str).matches();
		 }
	private void initPagerBar() {
		txt_pager_info = (TextView) this.findViewById(R.id.txt_pager_info);

		btn_pager_prev = (ImageButton) this.findViewById(R.id.btn_pager_prev);
		btn_pager_next = (ImageButton) this.findViewById(R.id.btn_pager_next);

		btn_board_favorite = (ImageButton) this
				.findViewById(R.id.btn_board_favorite);
		btn_board_favorite.setVisibility(View.VISIBLE);
		btn_board_favorite.setBackgroundResource(R.drawable.refresh2x);

		MyOnClickListener listener = new MyOnClickListener();
		btn_pager_prev.setOnClickListener(listener);
		btn_pager_next.setOnClickListener(listener);
		btn_board_favorite.setOnClickListener(listener);
	}
	class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Log.d(TAG, "" + v.getId());
			switch (v.getId()) {
			case R.id.btn_pager_prev:
				if (totalpage > 1 && pageno > 1) {
					--pageno;
					//pv.startUpdate();
					loadPage(pageno);
				} else {
					Toast.makeText(TopicHtmlActivity.this, "已经是第1页了!",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_pager_next:
				if (totalpage > 1 && pageno < totalpage) {
					++pageno;
					loadPage(pageno);
					//pv.startUpdate();
				} else {
					Toast.makeText(TopicHtmlActivity.this, "已经是底页了!",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_board_favorite:
				loadPage(pageno);
				break;
			}
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(Constants.ORDERBY_ID == id){
			return createProgressDialog();
		}
		return super.onCreateDialog(id);
	}
	
	private MyProcessDialog createProgressDialog(){
		mydialog = new MyProcessDialog(parent);
		return mydialog;
	}
}

