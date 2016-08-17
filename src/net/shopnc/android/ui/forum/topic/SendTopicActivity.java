/**
 *  ClassName: SendTopicActivity.java
 *  created on 2012-3-6
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.topic;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.SmileyGridViewAdapter;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.ImageHelper;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.SmileyImageGetter;
import net.shopnc.android.handler.UploadImageGetter;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Smiley;
import net.shopnc.android.widget.MyProcessDialog;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发帖界面
 * 
 * @author hjgang
 */
public class SendTopicActivity extends Activity {
	public static final String TAG ="SendTopicActivity";
	private MyApp myApp;
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;

	private ImageButton btn_camera;
	private ImageButton btn_face;
	private ImageButton btn_photo;
	private ImageButton btn_keyboard;

	private long fid;
	private Long typeid;
	private String boardName;
	private int ispostimage;
	
	private EditText topic_title;
	private Spinner topic_type;
	private String topic_type_yn;
	private TextView board_name;
	private EditText topic_content;
	
	private	JSONObject object;
	private JSONArray array;
	private JSONArray name;
	private List<String> list = new ArrayList<String>();
	private List<Long> list2 = new ArrayList<Long>();

	// 输入法管理器
	private InputMethodManager imm;
	private GridView faces_gv;
	private ArrayList<Smiley> smileyList;
	private HashSet<String> uploading_img;
	
	private static Pattern pattern_html2code = Pattern.compile("<img src=\"(http:[^<>]*)\"(/)?>", Pattern.MULTILINE);
	private static Pattern pattern_html2code2 = Pattern.compile("<img src=\"([^<>]*)\"(/)?>", Pattern.MULTILINE);
	
	private String camera_fileName;
	
	//private ArrayList<String> delete_imageName=new ArrayList<String>();//记录需要删除的图片的名字
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_send_new);
		myApp = (MyApp)this.getApplication();
		smileyList = myApp.getFaceList();
		
		uploading_img = new HashSet<String>();
		fid = this.getIntent().getExtras().getLong("fid");
		boardName = this.getIntent().getExtras().getString("boardName");
		ispostimage = this.getIntent().getExtras().getInt("ispostimage");
		
		initTitleBar();

		initToolBar();

		topic_title = (EditText)this.findViewById(R.id.topic_title);
		topic_type = (Spinner)this.findViewById(R.id.topic_type);
		topic_type_yn = "no";
		typeid = (long) 0;
		/*加载版块的主题分类*/		
		list.clear();
		list2.clear();
		try {
			object = new JSONObject(RemoteDataHandler.loadtopictype(fid));
			if (object.getString("code").equals("200")) {
				list.add("选择主题分类");
				array=object.getJSONArray("datas");		
				JSONObject item=array.getJSONObject(0);
				topic_type_yn = item.getString("required");				
				Log.d(TAG, topic_type_yn);
				item.remove("required");
				name = item.names();
				for(int j = 0 ;j<item.length();j++)
				{							
					list.add(item.getString(name.optString(j)));
					list2.add(name.getLong(j));
				}	
			}else {
				list.add("没有主题分类");
			}						
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
		//将可选内容与ArrayAdapter连接
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//设置下拉列表的风格
		topic_type.setAdapter(adapter);		  
		topic_type.setPrompt("主题分类");
		
		//为spinner对象绑定监听器
		topic_type.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				topic_type.setSelection((int)(topic_type.getSelectedItemId()));
				if((int)(topic_type.getSelectedItemId())>0)
					typeid = list2.get((int)(topic_type.getSelectedItemId())-1);
			} 
			public void onNothingSelected(AdapterView<?> arg0) {
			}
 
		});
		
		
		
		board_name = (TextView)this.findViewById(R.id.board_name);
		board_name.setText("所属版块：" + boardName);
		topic_content = (EditText) this.findViewById(R.id.topic_content);
		topic_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(topic_content.isFocused() && View.VISIBLE == faces_gv.getVisibility()){
					imm.hideSoftInputFromWindow(topic_content.getWindowToken(), 0);
				}
			}
		});
	}
	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_DEL){
//			if(delete_imageName!=null && delete_imageName.size()!=0){
//				uploading_img.clear();
//				System.out.println(delete_imageName.size());
//				System.out.println("删除前:"+delete_imageName.toString());
//				int count =delete_imageName.size()-1;
//				delete_imageName.remove(count);
//				System.out.println("删除后:"+delete_imageName.toString());
//				for(int i=delete_imageName.size()-1;i>=0;i--){
//					uploading_img.add(delete_imageName.get(i));
//				}
//				count--;
//			}
//			return false;
//		}
//		return super.onKeyUp(keyCode, event);
//	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == Constants.DIALOG_SENT_TOPIC_ID){
			MyProcessDialog dialog = new MyProcessDialog(this);
			dialog.setMsg(getString(R.string.topic_send_wait));
			return dialog;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK 
				&& null != this.faces_gv 
				&& this.faces_gv.getVisibility() == View.VISIBLE){
			this.faces_gv.setVisibility(View.GONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initToolBar(){
		btn_camera = (ImageButton) this.findViewById(R.id.btn_camera);
		btn_photo = (ImageButton) this.findViewById(R.id.btn_photo);
		btn_face = (ImageButton) this.findViewById(R.id.btn_face);
		btn_keyboard = (ImageButton) this.findViewById(R.id.btn_keyboard);

		MyOnTouchListener touch = new MyOnTouchListener();
		btn_camera.setOnTouchListener(touch);
		btn_photo.setOnTouchListener(touch);
		btn_face.setOnTouchListener(touch);
		btn_keyboard.setOnTouchListener(touch);
		
		faces_gv = (GridView) this.findViewById(R.id.faces_gv);
		BaseAdapter adapter = new SmileyGridViewAdapter(this, smileyList);
		faces_gv.setAdapter(adapter);
		faces_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				Smiley s = smileyList.get(paramInt);				
				String imag_html = MessageFormat.format("<img src=\"{0}\">", s.getPath());
				
				int index = topic_content.getSelectionStart();
				topic_content.getText().insert(index, Html.fromHtml(imag_html, new SmileyImageGetter(SendTopicActivity.this), null));
			}
		});
		
		imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	private void initTitleBar(){
		// 设置标题
		txt_title = (TextView) this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.topic_send_name));

		btn_left = (ImageButton) this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SendTopicActivity.this.finish();
			}
		});
		btn_right = (ImageButton) this.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendTopic();
			}
		});
	}
	
	private void sendTopic(){
		String title = topic_title.getText().toString();
		String content = topic_content.getText().toString();
		
		if(null == title || "".equals(title)){
			Toast.makeText(SendTopicActivity.this, "请输入帖子标题!", 0).show();
			return;
		}
		if(null == content || "".equals(content)){
			Toast.makeText(SendTopicActivity.this, "请输入帖子内容!", 0).show();
			return;
		}
		if(topic_type_yn.equals("yes")&&(topic_type.getSelectedItemPosition()==0))
		{
			Toast.makeText(SendTopicActivity.this, "请选择主题分类!", 0).show();
			return;
		}

		
		SendTopicActivity.this.showDialog(Constants.DIALOG_SENT_TOPIC_ID);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sid", myApp.getSid());
		params.put("authorid", myApp.getUid());
		params.put("author", myApp.getUseracc());
		params.put("subject", title);
		
		//Log.d(TAG,"转换前-->" + Html.toHtml(topic_content.getText()));
		String real_content = convertContent(topic_content.getText());
		//System.out.println("send_real_content===>"+real_content);
		//Log.d(TAG, "内容-->" + real_content);
		real_content=Html.fromHtml(real_content).toString();
		params.put("message", real_content);
		
		RemoteDataHandler.sendTopic(fid, params, uploading_img,typeid ,new Callback() {
			@Override
			public void dataLoaded(ResponseData data) {
				SendTopicActivity.this.dismissDialog(Constants.DIALOG_SENT_TOPIC_ID);
				if(data.getCode() == HttpStatus.SC_OK){
					if(data.getJson().contains("dangerous word")){
						Toast.makeText(SendTopicActivity.this, "对不起，您发的帖子包含敏感词汇！", 1).show();
					}else if (data.getJson().contains("forbidden_time")) {
						Toast.makeText(SendTopicActivity.this, "对不起，该时段禁止发帖！", 1).show();
					}else{
						Toast.makeText(SendTopicActivity.this, "发帖成功！", 1).show();
						SendTopicActivity.this.setResult(100);
						SendTopicActivity.this.finish();
					}
				}else{					
					Toast.makeText(SendTopicActivity.this, "发帖失败，请稍后再试！", 1).show();
				}
			}
		});
	}
	
	//content中的图片还需要换成code码
	private String convertContent(Editable content){
		String cont = Html.toHtml(content);
		//把表情图片转换成code码
		for(Matcher matcher = pattern_html2code.matcher(cont);
				matcher.find();matcher = pattern_html2code.matcher(cont)){
			
			String pic_src = matcher.group(1);
			Smiley s = getSmiley(pic_src);

			cont = matcher.replaceFirst(s.getCode());
		}
		
		//获取要上传的图片并从内容中删除掉
		String result = null;
		Matcher matcher = pattern_html2code2.matcher(cont);
		while(matcher.find()){
			uploading_img.add(matcher.group(1));
		}
		result = matcher.replaceAll("");
		
		//把标签去除掉
		result = result.replaceAll("(<(/)?[(^<>)a-zA-Z0-9]*(/)?>)|\\x0a|\\x0d","");
		Log.d(TAG, "img-count==" + uploading_img.toString());
		
		return result;
		//把上传的图片路径删除掉
		//return pattern_html2code2.matcher(cont).replaceAll("");
	}
	
	private Smiley getSmiley(String url){
		int size = smileyList == null ? 0 : smileyList.size();
		for(int i = 0; i<size; i++){
			Smiley s = smileyList.get(i);
			if(url.equals(s.getPath())){
				return s;
			}
		}
		return null;
	}

	class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				//v.setBackgroundResource(R.drawable.send_toolbar_btn);
				switch (v.getId()) {
					case R.id.btn_photo://手机相册中选择
						if(ispostimage != 1){
							Toast.makeText(SendTopicActivity.this, "您没有权限发图片", 0).show();
							break;
						}
						if(uploading_img.size() >=3){
							Toast.makeText(SendTopicActivity.this, "一次最多只能上传3张图片", 0).show();
							break;
						}
						Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
				        localIntent.setType(Constants.IMAGE_UNSPECIFIED);  
				        startActivityForResult(Intent.createChooser(localIntent, "选择图片"), Constants.RESULT_CODE_PHOTO_PICKED);
				        
				        closeGridView();
						break;
					case R.id.btn_camera://拍照
						if(ispostimage != 1){
							Toast.makeText(SendTopicActivity.this, "您没有权限发图片", 0).show();
							break;
						}
						if(uploading_img.size() >=3){
							Toast.makeText(SendTopicActivity.this, "一次最多只能上传3张图片", 0).show();
							break;
						}
						try {  
				            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				            
				            File file = new File(Constants.CACHE_DIR_IMAGE, "nc_" + System.currentTimeMillis() + ".jpg");
				            
				            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				            camera_fileName = file.getAbsolutePath();
				            startActivityForResult(cameraIntent, Constants.RESULT_CODE_CAMERA);  
				        } catch (ActivityNotFoundException e) {  
				            e.printStackTrace();  
				        }  
						closeGridView();
						break;
					case R.id.btn_face:
						if (faces_gv.getVisibility() == View.VISIBLE) {
							closeGridView();
						} else {
							showGridView();
						}
						break;
					case R.id.btn_keyboard:
						closeGridView();
						imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
								InputMethodManager.RESULT_SHOWN);
						break;
				}
			}
			return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){  
	        switch (requestCode) {  
	        case Constants.RESULT_CODE_CAMERA:  //拍照后进行裁剪
	        	Uri uri = Uri.fromFile(new File(camera_fileName));
	        	startPhotoZoom(uri);
		        break;
	        case Constants.RESULT_CODE_PHOTO_PICKED: //从本地选择图片后 
	        	startPhotoZoom(data.getData());
		        break;
			case Constants.RESULT_CODE_PHOTO_CUT: //裁剪后
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					//ByteArrayOutputStream stream = new ByteArrayOutputStream();
					//photo.compress(Bitmap.CompressFormat.JPEG, 75, stream); // (0-100)压缩文件
					
					String imgName = System.currentTimeMillis() + ".jpg";
					String imgPath = Constants.CACHE_DIR_UPLOADING_IMG + "/" + imgName;
					
					Log.d(TAG, imgPath);
					ImageHelper.write(photo, imgPath);
					
					
					uploading_img.add(imgName);
					
					//delete_imageName.add(imgName);
					
					String imag_html = MessageFormat.format("<img src=''{0}''/>", imgName);
					
					int index = topic_content.getSelectionStart();
					topic_content.getText().insert(index, Html.fromHtml(imag_html, new UploadImageGetter(), null));
				}
				break;
	        }
        }
    }
	
	/**  
     * 裁剪图片方法实现  
     * @param uri  
     */ 
    public void startPhotoZoom(Uri uri) {  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, Constants.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("noFaceDetection", true);
         //宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
         //裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        
        startActivityForResult(intent, Constants.RESULT_CODE_PHOTO_CUT);
    }  
	
	private void showGridView() {
		this.imm.hideSoftInputFromWindow(this.topic_content.getWindowToken(), 0);
		this.faces_gv.setVisibility(View.VISIBLE);
		//this.faces_gv.setClickable(true);
	}

	private void closeGridView() {
		this.faces_gv.setVisibility(View.GONE);
		//this.faces_gv.setClickable(false);
	}
}
