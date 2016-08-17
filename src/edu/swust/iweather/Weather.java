package edu.swust.iweather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.weixvn.wae.manager.EngineManager;



import com.special.ResideMenuDemo.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import edu.swust.iweather.util.Utils;
import edu.swust.iweather.web.UpdateWeather;

public class Weather extends Activity {

	final private String DATE_KEY[] = { "date_0", "date_1", "date_2", "date_3" };
	final private String WEATHER_KEY[] = { "weather_0", "weather_1",
			"weather_2", "weather_3" };
	final private String WIND_KEY[] = { "wind_0", "wind_1", "wind_2", "wind_3" };
	final private String TEMPERATURE_KEY[] = { "temperature_0",
			"temperature_1", "temperature_2", "temperature_3" };
	public static Handler handler;
	public static Weather context;
	private String[] dateArray, weatherArray, windArray, temperatureArray;
	private SharedPreferences sp;
	private LinearLayout weatherBg;
	private LinearLayout titleBarLayout;
	private LinearLayout changeCity;
	private TextView cityText;
	private ImageView share;
	private ImageView about;
	private ImageView refresh;
	private ProgressBar refreshing;
	private TextView updateTimeText;
	private ScrollView scrollView;
	private LinearLayout currentWeatherLayout;
	private ImageView weatherIcon;
	private TextView currentTemperatureText;
	private TextView currentWeatherText;
	private TextView temperatureText;
	private TextView windText;
	private TextView dateText;
	private ListView weatherForecastList;
	private Intent intent;
	private Time time;
	private Runnable run;
	private Builder builder;
	private String currentWeekDay;
	private String city;
	private String currentTemperature;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);

		EngineManager.getInstance().setContext(this.getApplicationContext())
				.setDB(null);
		weatherBg = (LinearLayout) findViewById(R.id.weather_bg);
		titleBarLayout = (LinearLayout) findViewById(R.id.title_bar_layout);
		changeCity = (LinearLayout) findViewById(R.id.change_city_layout);
		cityText = (TextView) findViewById(R.id.city);
		share = (ImageView) findViewById(R.id.share);
		about = (ImageView) findViewById(R.id.about);
		refresh = (ImageView) findViewById(R.id.refresh);
		refreshing = (ProgressBar) findViewById(R.id.refreshing);
		updateTimeText = (TextView) findViewById(R.id.update_time);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		currentWeatherLayout = (LinearLayout) findViewById(R.id.current_weather_layout);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		currentTemperatureText = (TextView) findViewById(R.id.current_temperature);
		currentWeatherText = (TextView) findViewById(R.id.current_weather);
		temperatureText = (TextView) findViewById(R.id.temperature);
		windText = (TextView) findViewById(R.id.wind);
		dateText = (TextView) findViewById(R.id.date);
		weatherForecastList = (ListView) findViewById(R.id.weather_forecast_list);
		changeCity.setOnClickListener(new ButtonListener());
		share.setOnClickListener(new ButtonListener());
		about.setOnClickListener(new ButtonListener());
		refresh.setOnClickListener(new ButtonListener());
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTPro-Lt.ttf");
		currentTemperatureText.setTypeface(face);
		setCurrentWeatherLayoutHight();
		handler = new MyHandler();
		context = this;
		time = new Time();
		run = new Runnable() {

			@Override
			public void run() {
				refreshing(false);
				Toast.makeText(Weather.this, "网络超时,请稍候再试", Toast.LENGTH_SHORT)
						.show();
			}
		};
		sp = getSharedPreferences("weather", Context.MODE_PRIVATE);
		if ("".equals(sp.getString("city", ""))) {
			intent = new Intent();
			intent.setClass(Weather.this, SelectCity.class);
			intent.putExtra("city", "");
			Weather.this.startActivityForResult(intent, 100);
			updateTimeText.setText("— — 更新");
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			scrollView.setVisibility(View.GONE);
		} else {
			initData();
			updateWeatherImage();
			updateWeatherInfo();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1 && !data.getStringExtra("city").equals(city)) {
			city = data.getStringExtra("city");
			cityText.setText(city);
			updateTimeText.setText("— — 更新");
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			scrollView.setVisibility(View.GONE);
			if (Utils.checkNetwork(Weather.this) == false) {
				Toast.makeText(Weather.this, "网络异常,请检查网络设置", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			updateWeather();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 主线程与更新天气的线程间通讯
	 * 
	 * @author 晨彦
	 * 
	 */
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			refreshing(false);
			switch (msg.what) {
			case 1:
				handler.removeCallbacks(run);
				Bundle bundle = msg.getData();
				dateArray = bundle.getStringArray("date");
				weatherArray = bundle.getStringArray("weather");
				windArray = bundle.getStringArray("wind");
				temperatureArray = bundle.getStringArray("temperature");
				city = bundle.getString("city");
				currentTemperature = bundle.getString("current_temperature");
				saveData();
				initData();
				updateWeatherImage();
				updateWeatherInfo();
				break;
			case 2:
				builder = new Builder(Weather.this);
				builder.setTitle("提示");
				builder.setMessage("没有查询到[" + city + "]的天气信息。");
				builder.setPositiveButton("重试",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								intent = new Intent();
								intent.setClass(Weather.this, SelectCity.class);
								Weather.this
										.startActivityForResult(intent, 100);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						});
				builder.setCancelable(false);
				builder.show();
				break;
			case 0:
				Toast.makeText(Weather.this, "更新失败,请稍候再试", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dateArray = new String[4];
		weatherArray = new String[4];
		windArray = new String[4];
		temperatureArray = new String[4];
		for (int i = 0; i < 4; i++) {
			dateArray[i] = sp.getString(DATE_KEY[i], "");
			weatherArray[i] = sp.getString(WEATHER_KEY[i], "");
			windArray[i] = sp.getString(WIND_KEY[i], "");
			temperatureArray[i] = sp.getString(TEMPERATURE_KEY[i], "");
		}
		city = sp.getString("city", "");
		currentTemperature = sp.getString("current_temperature", "");
		time.setToNow();
		switch (time.weekDay) {
		case 0:
			currentWeekDay = "周日";
			break;
		case 1:
			currentWeekDay = "周一";
			break;
		case 2:
			currentWeekDay = "周二";
			break;
		case 3:
			currentWeekDay = "周三";
			break;
		case 4:
			currentWeekDay = "周四";
			break;
		case 5:
			currentWeekDay = "周五";
			break;
		case 6:
			currentWeekDay = "周六";
			break;
		default:
			break;
		}
		for (int i = 0; i < 4; i++) {
			if (dateArray[i].equals(currentWeekDay)) {
				index = i;
			}
		}
	}

	/**
	 * 更新背景图片和天气图标
	 */
	private void updateWeatherImage() {
		scrollView.setVisibility(View.VISIBLE);
		String currentWeather = weatherArray[index];
		if (currentWeather.contains("转")) {
			currentWeather = currentWeather.substring(0,
					currentWeather.indexOf("转"));
		}
		time.setToNow();
		if (currentWeather.contains("晴")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_fine_day);
				weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
			} else {
				weatherBg.setBackgroundResource(R.drawable.bg_fine_night);
				weatherIcon.setImageResource(R.drawable.weather_img_fine_night);
			}
		} else if (currentWeather.contains("多云")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_cloudy_day);
				weatherIcon.setImageResource(R.drawable.weather_img_cloudy_day);
			} else {
				weatherBg.setBackgroundResource(R.drawable.bg_cloudy_night);
				weatherIcon
						.setImageResource(R.drawable.weather_img_cloudy_night);
			}
		} else if (currentWeather.contains("阴")) {
			weatherBg.setBackgroundResource(R.drawable.bg_overcast);
			weatherIcon.setImageResource(R.drawable.weather_img_overcast);
		} else if (currentWeather.contains("雷")) {
			weatherBg.setBackgroundResource(R.drawable.bg_thunder_storm);
			weatherIcon.setImageResource(R.drawable.weather_img_thunder_storm);
		} else if (currentWeather.contains("雨")) {
			weatherBg.setBackgroundResource(R.drawable.bg_rain);
			if (currentWeather.contains("小雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_small);
			} else if (currentWeather.contains("中雨")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			} else if (currentWeather.contains("大雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_big);
			} else if (currentWeather.contains("暴雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_storm);
			} else if (currentWeather.contains("雨夹雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_snow);
			} else if (currentWeather.contains("冻雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_sleet);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			}
		} else if (currentWeather.contains("雪")
				|| currentWeather.contains("冰雹")) {
			weatherBg.setBackgroundResource(R.drawable.bg_snow);
			if (currentWeather.contains("小雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_small);
			} else if (currentWeather.contains("中雪")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			} else if (currentWeather.contains("大雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_big);
			} else if (currentWeather.contains("暴雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_storm);
			} else if (currentWeather.contains("冰雹")) {
				weatherIcon.setImageResource(R.drawable.weather_img_hail);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			}
		} else if (currentWeather.contains("雾")) {
			weatherBg.setBackgroundResource(R.drawable.bg_fog);
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("霾")) {
			weatherBg.setBackgroundResource(R.drawable.bg_haze);
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("沙尘暴")
				|| currentWeather.contains("浮尘")
				|| currentWeather.contains("扬沙")) {
			weatherBg.setBackgroundResource(R.drawable.bg_sand_storm);
			weatherIcon.setImageResource(R.drawable.weather_img_sand_storm);
		} else {
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
		}
	}

	/**
	 * 更新界面（天气信息）
	 */
	private void updateWeatherInfo() {
		cityText.setText(city);
		currentTemperatureText.setText(currentTemperature);
		currentWeatherText.setText(weatherArray[index]);
		temperatureText.setText(temperatureArray[index]);
		windText.setText(windArray[index]);
		Time time = new Time();
		time.setToNow();
		String date = new SimpleDateFormat("MM/dd").format(new Date());
		dateText.setText(currentWeekDay + " " + date);
		String updateTime = sp.getString("update_time", "");
		if (Integer.parseInt(updateTime.substring(0, 4)) == time.year
				&& Integer.parseInt(updateTime.substring(5, 7)) == time.month + 1
				&& Integer.parseInt(updateTime.substring(8, 10)) == time.monthDay) {
			updateTime = "今天" + updateTime.substring(updateTime.indexOf(" "));
			updateTimeText.setTextColor(getResources().getColor(R.color.white));
		} else {
			updateTime = updateTime.substring(5).replace("-", "月")
					.replace(" ", "日 ");
			updateTimeText.setTextColor(getResources().getColor(R.color.red));
			// 超过一天没有更新天气，自动帮用户更新
			if (Utils.checkNetwork(Weather.this) == true) {
				updateWeather();
			}
		}
		updateTimeText.setText(updateTime + " 更新");
		weatherForecastList.setAdapter(new MyAdapter(Weather.this));
		Utils.setListViewHeightBasedOnChildren(weatherForecastList);
	}

	/**
	 * 设置布局的高度（铺满屏幕）
	 */
	private void setCurrentWeatherLayoutHight() {
		// 通知栏高度
		int statusBarHeight = 0;
		try {
			statusBarHeight = getResources().getDimensionPixelSize(
					Integer.parseInt(Class
							.forName("com.android.internal.R$dimen")
							.getField("status_bar_height")
							.get(Class.forName("com.android.internal.R$dimen")
									.newInstance()).toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 屏幕高度
		int displayHeight = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getHeight();
		// title bar LinearLayout高度
		titleBarLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		int titleBarHeight = titleBarLayout.getMeasuredHeight();

		LayoutParams linearParams = (LayoutParams) currentWeatherLayout
				.getLayoutParams();
		linearParams.height = displayHeight - statusBarHeight - titleBarHeight;
		currentWeatherLayout.setLayoutParams(linearParams);
	}

	/**
	 * 更新天气
	 */
	private void updateWeather() {
		refreshing(true);
		handler.postDelayed(run, 60 * 1000);
		EngineManager.getInstance().getWebPageMannger()
				.getWebPage(UpdateWeather.class).setHtmlValue("city", city);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				EngineManager.getInstance().getWebPageMannger()
						.updateWebPage(UpdateWeather.class, true);
			}
		});
		thread.start();
	}

	/**
	 * 保存天气信息
	 */
	private void saveData() {
		String updateTime = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		Time time = new Time();
		time.setToNow();
		String hour, minute;
		hour = time.hour + "";
		minute = time.minute + "";
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		updateTime = updateTime + " " + hour + ":" + minute;
		Editor editor = sp.edit();
		editor.putString("update_time", updateTime);
		for (int i = 0; i < 4; i++) {
			editor.putString(DATE_KEY[i], dateArray[i]);
			editor.putString(WEATHER_KEY[i], weatherArray[i]);
			editor.putString(WIND_KEY[i], windArray[i]);
			editor.putString(TEMPERATURE_KEY[i], temperatureArray[i]);
		}
		editor.putString("city", city);
		editor.putString("current_temperature", currentTemperature);
		editor.commit();
	}

	/**
	 * 刷新时显示进度条
	 * 
	 * @param isRefreshing
	 *            是否正在刷新
	 */
	private void refreshing(boolean isRefreshing) {
		if (isRefreshing) {
			refresh.setVisibility(View.GONE);
			refreshing.setVisibility(View.VISIBLE);
		} else {
			refresh.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
		}
	}

	class MyAdapter extends BaseAdapter {

		private Context mContext;

		private MyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return getData().size();
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
						R.layout.weather_forecast_item, null);
				holder = new ViewHolder();
				holder.date = (TextView) convertView
						.findViewById(R.id.weather_forecast_date);
				holder.img = (ImageView) convertView
						.findViewById(R.id.weather_forecast_img);
				holder.weather = (TextView) convertView
						.findViewById(R.id.weather_forecast_weather);
				holder.temperature = (TextView) convertView
						.findViewById(R.id.weather_forecast_temperature);
				holder.wind = (TextView) convertView
						.findViewById(R.id.weather_forecast_wind);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/fangzhenglantingxianhe_GBK.ttf");
			holder.date.setText(getData().get(position).get("date").toString());
			holder.img.setImageResource((Integer) getData().get(position).get(
					"img"));
			holder.weather.setText(getData().get(position).get("weather")
					.toString());
			holder.temperature.setText(getData().get(position)
					.get("temperature").toString());
			holder.temperature.setTypeface(face);
			holder.wind.setText(getData().get(position).get("wind").toString());
			return convertView;
		}

	}

	class ViewHolder {
		TextView date;
		ImageView img;
		TextView weather;
		TextView temperature;
		TextView wind;
	}

	/**
	 * 获取天气预报信息
	 * 
	 * @return 天气预报list
	 */
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (dateArray[i].equals(currentWeekDay)) {
				map.put("date", "今天");
			} else {
				map.put("date", dateArray[i]);
			}
			map.put("img", getWeatherImg(weatherArray[i]));
			map.put("weather", weatherArray[i]);
			map.put("temperature", temperatureArray[i]);
			map.put("wind", windArray[i]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据天气信息设置天气图片
	 * 
	 * @param weather
	 *            天气信息
	 * @return 对应的天气图片id
	 */
	private int getWeatherImg(String weather) {
		int img = 0;
		if (weather.contains("转")) {
			weather = weather.substring(0, weather.indexOf("转"));
		}
		if (weather.contains("晴")) {
			img = R.drawable.weather_icon_fine;
		} else if (weather.contains("多云")) {
			img = R.drawable.weather_icon_cloudy;
		} else if (weather.contains("阴")) {
			img = R.drawable.weather_icon_overcast;
		} else if (weather.contains("雷")) {
			img = R.drawable.weather_icon_thunder_storm;
		} else if (weather.contains("小雨")) {
			img = R.drawable.weather_icon_rain_small;
		} else if (weather.contains("中雨")) {
			img = R.drawable.weather_icon_rain_middle;
		} else if (weather.contains("大雨")) {
			img = R.drawable.weather_icon_rain_big;
		} else if (weather.contains("暴雨")) {
			img = R.drawable.weather_icon_rain_storm;
		} else if (weather.contains("雨夹雪")) {
			img = R.drawable.weather_icon_rain_snow;
		} else if (weather.contains("冻雨")) {
			img = R.drawable.weather_icon_sleet;
		} else if (weather.contains("小雪")) {
			img = R.drawable.weather_icon_snow_small;
		} else if (weather.contains("中雪")) {
			img = R.drawable.weather_icon_snow_middle;
		} else if (weather.contains("大雪")) {
			img = R.drawable.weather_icon_snow_big;
		} else if (weather.contains("暴雪")) {
			img = R.drawable.weather_icon_snow_storm;
		} else if (weather.contains("冰雹")) {
			img = R.drawable.weather_icon_hail;
		} else if (weather.contains("雾") || weather.contains("霾")) {
			img = R.drawable.weather_icon_fog;
		} else if (weather.contains("沙尘暴") || weather.contains("浮尘")
				|| weather.contains("扬沙")) {
			img = R.drawable.weather_icon_sand_storm;
		} else {
			img = R.drawable.weather_icon_fine;
		}
		return img;
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_city_layout:
				intent = new Intent();
				intent.setClass(Weather.this, SelectCity.class);
				Weather.this.startActivityForResult(intent, 100);
				break;
			case R.id.share:
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						"感谢您的支持与厚爱，我们是wellanTec移动应用开发实验室");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Weather.this
						.startActivity(Intent.createChooser(intent, "好友分享"));
				break;
			case R.id.about:
				LayoutInflater inflater = getLayoutInflater();
				View dialogLayout = inflater.inflate(R.layout.weather_dialog,
						(ViewGroup) findViewById(R.layout.weather_dialog));
				TextView version = (TextView) dialogLayout
						.findViewById(R.id.version);
				version.setText("V " + Utils.getVersion(Weather.this));
				builder = new Builder(Weather.this);
				builder.setTitle("关于");
				builder.setView(dialogLayout);
				builder.setPositiveButton("确定", null);
				builder.setCancelable(false);
				builder.show();
				break;
			case R.id.refresh:
				if (Utils.checkNetwork(Weather.this) == false) {
					Toast.makeText(Weather.this, "网络异常,请检查网络设置",
							Toast.LENGTH_SHORT).show();
					return;
				}
				updateWeather();
				break;
			default:
				break;
			}
		}

	}

}
