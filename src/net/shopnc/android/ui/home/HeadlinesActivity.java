/**
 *  ClassName: HeadlinesActivity.java
 *  created on 2012-3-8
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.home;

import java.util.ArrayList;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.HeadlinesListViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.SystemHelper;
import net.shopnc.android.handler.ImageLoader;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.ImageLoader.ImageCallback;
import net.shopnc.android.model.PushData;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Topic;
import net.shopnc.android.ui.MainActivity;
import net.shopnc.android.ui.forum.topic.TopicDetailActivity;
import net.shopnc.android.widget.PullView;
import net.shopnc.android.widget.PullView.UpdateHandle;

import org.apache.http.HttpStatus;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**首页-->茶座界面
 * @author qjyong
 */
public class HeadlinesActivity 	extends ListActivity implements UpdateHandle{
	public static final String TAG = "HeadlinesActivity";

	private MyApp myApp;
	private HomeActivity myParent;
	
	private ImageButton btn_left;
	private ImageButton btn_sort;

	private PullView pv;
	private LinearLayout moreLayout;
	private Button moreBtn;
	private String txt_more_default;
	private String txt_more_wait;
	private HeadlinesListViewAdapter adapter;
	private ArrayList<PushData> datas;
		
	private int pageno = 1;
	private int pagesize;
	private boolean img_invisible;

	private RelativeLayout headerview_headlines;
	
	private TextView top_id;
	private ImageView  top_img ;
	private TextView top_text;
	
	private PushData top;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)this.getApplicationContext();
		myParent = (HomeActivity)this.getParent();
		pagesize = myApp.getPageSize();
		img_invisible = myApp.isImg_invisible();
		
		this.setContentView(R.layout.home_sub_banner);
		
		btn_sort=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    btn_sort.setVisibility(View.GONE);
	    
		initTitleBar();
		
		initBigImageView();
		
		initPullView();
        
		datas = new ArrayList<PushData>();
		adapter = new HeadlinesListViewAdapter(HeadlinesActivity.this);
		setListAdapter(adapter);
		
		pv.startUpdate(); //启用刷新功能-->会自动调用一次onUpdate()方法
	}
	
	
	public void onUpdate() {
		Log.d(TAG, "onUpdate-----");
		loadPage(pageno = 1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		btn_sort=(ImageButton)myParent.findViewById(R.id.ibtu) ;
	    btn_sort.setVisibility(View.GONE);

		boolean temp = myApp.isImg_invisible(); //是否不显示图片的新值
		if(img_invisible != temp){
			img_invisible = temp;
			pv.startUpdate();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("HeadlinesActivity", "back.....");
		if(keyCode == KeyEvent.KEYCODE_BACK){
			((MainActivity)myParent.getParent()).showDialog(Constants.DIALOG_EXITAPP_ID);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void initTitleBar(){
		//刷新
		
		btn_left = (ImageButton)myParent.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				pv.startUpdate();
			}
		});
	}
	
	private void initBigImageView(){
		this.headerview_headlines = ((RelativeLayout)LayoutInflater.from(this).inflate(R.layout.headerview_headlines, null));
		this.top_id = ((TextView)this.headerview_headlines.findViewById(R.id.topic_id_top));
		this.top_img = ((ImageView)this.headerview_headlines.findViewById(R.id.topic_img_top));
	    this.top_text = ((TextView)this.headerview_headlines.findViewById(R.id.topic_title_top));
	    
	    if(myParent.getIntent().hasExtra("top")){
	    	top = (PushData)myParent.getIntent().getExtras().get("top");
		}
	}
	
	private void initPullView(){
		pv = (PullView)this.findViewById(R.id.pv);
        this.pv.setUpdateHandle(this);
        
		this.getListView().addHeaderView(headerview_headlines);
        
        this.moreLayout = ((LinearLayout)LayoutInflater.from(this).inflate(R.layout.next_page_layout, null));
        this.getListView().addFooterView(this.moreLayout);
        
		this.moreBtn = ((Button) findViewById(R.id.show_more_btn));
		txt_more_default = this.getString(R.string.footview_more, pagesize);
		txt_more_wait = this.getString(R.string.footview_wait);
		
		this.moreBtn.setText(txt_more_default);
		this.moreBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				moreBtn.setText(txt_more_wait);
				loadPage(++pageno);
			}
		});
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//Toast.makeText(this, "position-->"+ position, 0).show();
		
		//查看帖子详细信息
		Intent intent = new Intent(HeadlinesActivity.this, TopicDetailActivity.class);
		PushData pd = null;
		if(position == 0){ //头条
			pd = top;
		}else{
			pd = (PushData)adapter.getItem(position - 1);
		}
		if(pd!=null){
			Topic t = new Topic();
			t.setTid(pd.getId());
			t.setSubject(pd.getTitle());
			intent.putExtra(Topic.TOPIC_TAG, t);
			HeadlinesActivity.this.startActivity(intent);
		}
	}
	private void loadPage(final int pageno){
		moreBtn.setEnabled(false);
		
		Log.d(TAG, "loadData...");
		
		RemoteDataHandler.asyncGet(Constants.URL_HOME_TOPS, pagesize, pageno, new RemoteDataHandler.Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				
				pv.endUpdate(); //更新完成后的回调方法,用于隐藏刷新面板
				moreBtn.setText(txt_more_default);
				
				if(data.getCode() == HttpStatus.SC_OK){
					Log.d(TAG, "RemoteDataHanlder---dataLoaded");
					String json = data.getJson();
					
					if(data.isHasMore()){
						moreLayout.setVisibility(View.VISIBLE);
				        moreBtn.setVisibility(View.VISIBLE);
				        moreBtn.setEnabled(true);
					}else{
						moreLayout.setVisibility(View.GONE);
				        moreBtn.setVisibility(View.GONE);
					}
					
					if(pageno == 1){
						datas.clear();
						loadTopData();
					}
					
					datas.addAll(PushData.newInstanceList(json));
					Log.d(TAG, datas.toString());
					
					adapter.setDatas(datas);
					adapter.notifyDataSetChanged();
				}
			}
		});

	}
	@SuppressWarnings("static-access")
	private void loadTopData(){
		//if(top == null){
			ResponseData data = RemoteDataHandler.get(Constants.URL_HOME_TOP, pagesize, 1);
			if(data.getCode() == HttpStatus.SC_OK){
				String json = data.getJson();
				ArrayList<PushData> temp = PushData.newInstanceList(json);
				if(temp != null){
					top = temp.get(0);
				}
			}
		//}
		top_id.setText(top.getId()+"");
		top_text.setText(top.getTitle());
		//String picName = MD5Encoder.encode(top.getPic());
		//Bitmap b = BitmapFactory.decodeFile(Constants.CACHE_DIR_IMAGE + "/" + picName);
		 if(ConnectivityManager.TYPE_WIFI == SystemHelper.getNetworkType(this) || !img_invisible){
			 ImageLoader.getInstance().asyncLoadBitmap(top.getPic(), myApp.getScreenWidth(), new ImageCallback(){
				@Override
				public void imageLoaded(Bitmap bmp, String url) {
					top_img.setImageBitmap(bmp);
				}
			});
//			 Bitmap bmp= ImageLoader.getBitmapFromDisk(top.getPic());
//			 if(bmp!=null){
//				 top_img.setImageBitmap(bmp);
//			 }
		}
//		 if(null != b){
//			top_img.setImageBitmap(b);
//		}
	}
}
