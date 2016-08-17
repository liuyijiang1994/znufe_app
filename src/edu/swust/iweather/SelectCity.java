package edu.swust.iweather;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.special.ResideMenuDemo.R;

import edu.swust.iweather.util.Utils;

public class SelectCity extends Activity {

	private String[] citys;
	private ImageView back;
	private GridView cityList;
	private Intent intent;
	private EditText inputCity;
	private Button search;
	private ProgressDialog dialog;
	private Builder builder;
	private String city;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_city);

		// 声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);
		// 设置定位参数
		setLocationOption();
		dialog = new ProgressDialog(SelectCity.this);
		dialog.setMessage("正在定位...");
		dialog.setCanceledOnTouchOutside(false);

		citys = getResources().getStringArray(R.array.citys);
		cityList = (GridView) findViewById(R.id.city_list);
		back = (ImageView) findViewById(R.id.back);
		inputCity = (EditText) findViewById(R.id.input_city);
		search = (Button) findViewById(R.id.search);
		back.setOnClickListener(new ButtonListener());
		search.setOnClickListener(new ButtonListener());
		inputCity.addTextChangedListener(new Watcher());
		cityList.setAdapter(new MyAdapter(SelectCity.this));
		cityList.setOnItemClickListener(new ClickListener());
	}

	class MyAdapter extends BaseAdapter {

		private Context mContext;

		private MyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return citys.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.city_item, null);
				holder = new ViewHolder();
				holder.city = (TextView) convertView.findViewById(R.id.city);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.city.setText(citys[position]);
			return convertView;
		}

	}

	class ViewHolder {
		TextView city;
	}

	class ClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			city = citys[arg2];
			if ("自动定位".equals(city)) {
				if (Utils.checkNetwork(SelectCity.this) == false) {
					Toast.makeText(SelectCity.this, "网络异常,请检查网络设置",
							Toast.LENGTH_SHORT).show();
					return;
				}
				dialog.show();
				requestLocation();
			} else {
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCity.this.setResult(1, intent);
				SelectCity.this.finish();
			}
		}

	}

	/**
	 * 监听编辑框内容，输入内容，显示搜索按键
	 */
	class Watcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (inputCity.getText().toString().length() == 0) {
				search.setVisibility(View.GONE);
			} else {
				search.setVisibility(View.VISIBLE);
			}
		}
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				back();
				break;
			case R.id.search:
				city = inputCity.getText().toString();
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCity.this.setResult(1, intent);
				SelectCity.this.finish();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 实现BDLocationListener接口
	 * 
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 * 
	 * @author 晨彦
	 * 
	 */
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(location.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(location.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation) {
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			dialog.cancel();
			int code = location.getLocType();
			String addr = location.getAddrStr();
			if (code == 161 && addr != null) {
				// 定位成功
				System.out.println(addr);
				city = formatCity(addr);
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCity.this.setResult(1, intent);
				SelectCity.this.finish();
			} else {
				// 定位失败
				builder = new Builder(SelectCity.this);
				builder.setTitle("提示");
				builder.setMessage("自动定位失败。");
				builder.setPositiveButton("重试",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (Utils.checkNetwork(SelectCity.this) == false) {
									Toast.makeText(SelectCity.this,
											"网络异常,请检查网络设置", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								SelectCity.this.dialog.show();
								requestLocation();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.setCancelable(false);
				builder.show();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}

	}

	/**
	 * 设置定位参数。 定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(24 * 60 * 60 * 1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}

	/**
	 * 请求位置信息
	 */
	private void requestLocation() {
		if (mLocationClient.isStarted() == false) {
			mLocationClient.start();
		} else {
			mLocationClient.requestLocation();
		}
	}

	/**
	 * 将位置信息转换为城市
	 * 
	 * @param addr
	 *            位置
	 * @return 城市名称
	 */
	private String formatCity(String addr) {
		String city = null;
		if (addr.contains("北京市") && addr.contains("区")) {
			city = addr.substring(addr.indexOf("市") + 1, addr.indexOf("区"));
		} else if (addr.contains("县")) {
			city = addr.substring(addr.indexOf("市") + 1, addr.indexOf("县"));
		} else {
			int start = addr.indexOf("市");
			int end = addr.lastIndexOf("市");
			if (start == end) {
				if (addr.contains("省")) {
					city = addr.substring(addr.indexOf("省") + 1,
							addr.indexOf("市"));
				} else if (addr.contains("市")) {
					city = addr.substring(0, addr.indexOf("市"));
				}
			} else {
				city = addr.substring(addr.indexOf("市") + 1,
						addr.lastIndexOf("市"));
			}
		}
		return city;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * finish Activity前判断是否结束主Activity
	 */
	private void back() {
		intent = getIntent();
		if ("".equals(intent.getStringExtra("city"))) {
			Weather.context.finish();
		}
		SelectCity.this.finish();
		System.exit(0);
	}

}
