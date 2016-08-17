/**
 *  ClassName: ReplyTopicActivity.java
 *  created on 2012-3-6
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package net.shopnc.android.ui.forum.topic;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.special.ResideMenuDemo.R;
import net.shopnc.android.adapter.SmileyGridViewAdapter;
import net.shopnc.android.common.BBCodeHelper;
import net.shopnc.android.common.Constants;
import net.shopnc.android.common.ImageHelper;
import net.shopnc.android.common.MyApp;
import net.shopnc.android.common.Tools;
import net.shopnc.android.handler.RemoteDataHandler;
import net.shopnc.android.handler.SmileyImageGetter;
import net.shopnc.android.handler.UploadImageGetter;
import net.shopnc.android.handler.RemoteDataHandler.Callback;
import net.shopnc.android.model.ResponseData;
import net.shopnc.android.model.Smiley;
import net.shopnc.android.widget.MyProcessDialog;

import org.apache.http.HttpStatus;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 引用界面
 * 
 * @author hjgang
 */
public class QuoteTopicActivity extends Activity {
	private TextView tv;
	private static String TAG = "QuoteTopicActivity";
	private MyApp myApp;
	private TextView txt_title;
	private ImageButton btn_left;
	private ImageButton btn_right;

	private ImageButton btn_camera;
	private ImageButton btn_face;
	private ImageButton btn_photo;
	private ImageButton btn_keyboard;

	private EditText reply_content;
	private int ispostimage;
	// 输入法管理器
	private InputMethodManager imm;
	private GridView faces_gv;
	private ArrayList<Smiley> smileyList;
	private HashSet<String> uploading_img;

	private static Pattern pattern_html2code = Pattern.compile(
			"<img src=\"(http:[^<>]*)\"(/)?>", Pattern.MULTILINE);
	private static Pattern pattern_html2code2 = Pattern.compile(
			"<img src=\"[^(http)]([^<>]*)\"(/)?>", Pattern.MULTILINE);

	private String camera_fileName;
	private String author;
	private long tid;
	private long fid;
	private long pid;
	private long date;
	private String mage;

	// private ArrayList<String> delete_imageName=new
	// ArrayList<String>();//记录需要删除的图片的名字
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_quote);
		myApp = (MyApp) this.getApplication();
		smileyList = myApp.getFaceList();
		uploading_img = new HashSet<String>();
		Intent it = QuoteTopicActivity.this.getIntent();
		tid = Long.valueOf(it.getStringExtra("tid"));
		fid = Long.valueOf(it.getStringExtra("fid"));
		author = it.getStringExtra("author");
		pid = Long.valueOf(it.getStringExtra("pid"));
		ispostimage = Integer.parseInt(it.getStringExtra("ispostimage"));
		date = Long.parseLong(it.getStringExtra("date"));
		mage = it.getStringExtra("mage");
		int first = mage.indexOf("[ncsmiley]");
		int last = mage.lastIndexOf("[/ncsmiley]");
		int d=mage.lastIndexOf("本帖最后由");
		int e=mage.lastIndexOf("编辑");
		if (first != -1 && last != -1) {
			mage = mage.replace(mage.substring(first, last), "");
			mage = mage.replace("[/ncsmiley]", "");
		}
		if(d!=-1 && e!=-1){
			mage = mage.replaceFirst(mage.substring(d, e+"编辑".length()),"");
		}
		mage=BBCodeHelper.processBBCode(mage);
		if (mage.toCharArray().length > 48) {
			mage = Tools.length2(Html.fromHtml(mage).toString()).append("...")
					.toString();
		}
		// 显示引用的内容的Textview
		tv = (TextView) findViewById(R.id.quote_shangji);
		tv.setText(Html.fromHtml(
				"作者:"
						+ author
						+ "<br/>内容:"
						+ mage
						+ "<br/>时间:"
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
								new Date(date * 1000)).toString(),
				new SmileyImageGetter(QuoteTopicActivity.this), null));
		initTitleBar();

		initToolBar();

		reply_content = (EditText) this.findViewById(R.id.quote_content);
		reply_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (reply_content.isFocused()
						&& View.VISIBLE == faces_gv.getVisibility()) {
					imm.hideSoftInputFromWindow(reply_content.getWindowToken(),
							0);
				}
			}
		});
	}

	// public boolean onKeyUp(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_DEL){
	// if(delete_imageName!=null && delete_imageName.size()!=0){
	// uploading_img.clear();
	// System.out.println(delete_imageName.size());
	// System.out.println("删除前:"+delete_imageName.toString());
	// int count =delete_imageName.size()-1;
	// delete_imageName.remove(count);
	// System.out.println("删除后:"+delete_imageName.toString());
	// for(int i=delete_imageName.size()-1;i>=0;i--){
	// uploading_img.add(delete_imageName.get(i));
	// }
	// count--;
	// }
	// return false;
	// }
	// return super.onKeyUp(keyCode, event);
	// }
	protected Dialog onCreateDialog(int id) {
		if (id == Constants.DIALOG_SENT_TOPIC_ID) {
			return createProgressDialog();
		}

		return super.onCreateDialog(id);
	}

	private MyProcessDialog createProgressDialog() {
		MyProcessDialog dialog = new MyProcessDialog(this);
		dialog.setMsg(getString(R.string.topic_reply_wait));
		return dialog;
	}

	private void initToolBar() {
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
		BaseAdapter adapter = new SmileyGridViewAdapter(this,
				myApp.getFaceList());
		faces_gv.setAdapter(adapter);
		faces_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				Smiley s = smileyList.get(paramInt);
				String imag_html = MessageFormat.format("<img src=\"{0}\">",
						s.getPath());

				int index = reply_content.getSelectionStart();
				reply_content.getText()
						.insert(index,
								Html.fromHtml(imag_html,
										new SmileyImageGetter(QuoteTopicActivity.this), null));
			}
		});
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	private void initTitleBar() {
		// 设置标题
		txt_title = (TextView)this.findViewById(R.id.txt_title);
		txt_title.setText(this.getString(R.string.topic_quote_name));

		btn_left = (ImageButton) this.findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				QuoteTopicActivity.this.finish();
			}
		});
		btn_right = (ImageButton) this.findViewById(R.id.btn_right);
		btn_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				replyTopic();
			}
		});
	}

	private void replyTopic() {
		uploading_img.size();
		String content = reply_content.getText().toString();

		if (null == content || "".equals(content)) {
			Toast.makeText(QuoteTopicActivity.this, "请输入回复内容!", 0).show();
			return;
		}

		QuoteTopicActivity.this.showDialog(Constants.DIALOG_SENT_TOPIC_ID);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sid", myApp.getSid());
		params.put("authorid", myApp.getUid());
		params.put("author", myApp.getUseracc());
		// params.put("subject", title);
		params.put("message", content);
		Log.d(TAG, "转换前-->" + Html.toHtml(reply_content.getText()));
		String real_content = convertContent(reply_content.getText());
		Object[] paramValues = {
				author,
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
						new Date(date * 1000)).toString(), "\n<br/>" + mage};
		String quote = MessageFormat
				.format("[quote][size=2][color=#999999]{0} 发表于 {1}[/color] [url=forum.php?mod=redirect&goto=findpost&pid="
						+ pid
						+ "&ptid="
						+ tid
						+ "][img]static/image/common/back.gif[/img][/url][/size]{2}[/quote]",
						paramValues);
		quote += real_content;
		quote=Html.fromHtml(quote).toString();
		params.put("message", quote);
		RemoteDataHandler.quoteTopic(fid, tid, params, uploading_img,
				new Callback() {
					@Override
					public void dataLoaded(ResponseData data) {
						QuoteTopicActivity.this
								.dismissDialog(Constants.DIALOG_SENT_TOPIC_ID);
						if (data.getCode() == HttpStatus.SC_OK) {
							if (data.getJson().contains("dangerous word")) {
								Toast.makeText(QuoteTopicActivity.this,
										"对不起，您发的帖子包含敏感词汇！", 1).show();
							} else if (data.getJson()
									.contains("forbidden_time")) {
								Toast.makeText(QuoteTopicActivity.this,
										"对不起，该时段禁止回复！", 1).show();
							} else {
								Toast.makeText(QuoteTopicActivity.this,
										"回复成功！", 1).show();
								QuoteTopicActivity.this.setResult(300);
								QuoteTopicActivity.this.finish();
							}
						} else {
							Toast.makeText(QuoteTopicActivity.this,
									"回复失败，请稍后再试！", 1).show();

						}
					}
				});
	}

	// content中的图片还需要换成code码
	private String convertContent(Editable content) {
		String cont = Html.toHtml(content);
		// 把表情图片转换成code码
		for (Matcher matcher = pattern_html2code.matcher(cont); matcher.find(); matcher = pattern_html2code
				.matcher(cont)) {

			String pic_src = matcher.group(1);
			Log.d(TAG, "smiley==>" + pic_src);
			Smiley s = getSmiley(pic_src);
			Log.d(TAG, s.toString());

			cont = matcher.replaceFirst(s.getCode());
		}
		// 获取要上传的图片并从内容中删除掉
		String result = null;
		Matcher matcher = pattern_html2code2.matcher(cont);
		while (matcher.find()) {
			// uploading_img.add(matcher.group(1));
		}
		result = matcher.replaceAll("");

		// 把<p></p>标签去除掉，把回车换行都去掉
		result = result.replaceAll("(<(/)?[(^<>)a-zA-Z0-9]*(/)?>)|\\x0a|\\x0d",
				"");
		Log.d(TAG, "img-count==" + uploading_img.toString());

		return result;
	}

	private Smiley getSmiley(String url) {
		int size = smileyList == null ? 0 : smileyList.size();
		for (int i = 0; i < size; i++) {
			Smiley s = smileyList.get(i);
			if (url.equals(s.getPath())) {
				return s;
			}
		}
		return null;
	}

	class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// v.setBackgroundResource(R.drawable.send_toolbar_btn);
				switch (v.getId()) {
				case R.id.btn_photo:// 手机相册中选择

					if (ispostimage != 1) {
						Toast.makeText(QuoteTopicActivity.this, "您没有权限发图片", 0)
								.show();
						break;
					}
					if (uploading_img.size() >= 3) {
						Toast.makeText(QuoteTopicActivity.this, "一次最多只能上传3张图片",
								0).show();
						break;
					}
					Intent localIntent = new Intent(
							"android.intent.action.GET_CONTENT");
					localIntent.setType(Constants.IMAGE_UNSPECIFIED);
					startActivityForResult(
							Intent.createChooser(localIntent, "选择图片"),
							Constants.RESULT_CODE_PHOTO_PICKED);

					closeGridView();
					break;
				case R.id.btn_camera:// 拍照

					if (ispostimage != 1) {
						Toast.makeText(QuoteTopicActivity.this, "您没有权限发图片", 0)
								.show();
						break;
					}

					if (uploading_img.size() >= 3) {
						Toast.makeText(QuoteTopicActivity.this, "一次最多只能上传3张图片",
								0).show();
						break;
					}
					try {
						Intent cameraIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						File file = new File(Constants.CACHE_DIR_IMAGE, "nc_"
								+ System.currentTimeMillis() + ".jpg");

						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(file));
						camera_fileName = file.getAbsolutePath();
						startActivityForResult(cameraIntent,
								Constants.RESULT_CODE_CAMERA);
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
					imm.toggleSoftInput(
							InputMethodManager.RESULT_UNCHANGED_SHOWN,
							InputMethodManager.RESULT_SHOWN);
					break;
				}
			}
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.RESULT_CODE_CAMERA: // 拍照后进行裁剪
				Uri uri = Uri.fromFile(new File(camera_fileName));
				startPhotoZoom(uri);
				break;
			case Constants.RESULT_CODE_PHOTO_PICKED: // 从本地选择图片后
				startPhotoZoom(data.getData());
				break;
			case Constants.RESULT_CODE_PHOTO_CUT: // 裁剪后
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					// ByteArrayOutputStream stream = new
					// ByteArrayOutputStream();
					// photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
					// // (0-100)压缩文件

					String imgName = System.currentTimeMillis() + ".jpg";
					String imgPath = Constants.CACHE_DIR_UPLOADING_IMG + "/"
							+ imgName;

					Log.d(TAG, imgPath);

					ImageHelper.write(photo, imgPath);

					uploading_img.add(imgName);

					// delete_imageName.add(imgName);

					String imag_html = MessageFormat.format(
							"<img src=''{0}''/>", imgName);

					int index = reply_content.getSelectionStart();
					reply_content.getText().insert(
							index,
							Html.fromHtml(imag_html, new UploadImageGetter(),
									null));
				}
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, Constants.IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("noFaceDetection", true);
		// 宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, Constants.RESULT_CODE_PHOTO_CUT);
	}

	private void showGridView() {
		this.imm.hideSoftInputFromWindow(this.reply_content.getWindowToken(), 0);
		this.faces_gv.setVisibility(View.VISIBLE);
		// this.faces_gv.setClickable(true);
	}

	private void closeGridView() {
		this.faces_gv.setVisibility(View.GONE);
		// this.faces_gv.setClickable(false);
	}

}