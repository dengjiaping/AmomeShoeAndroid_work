package cn.com.amome.amomeshoes.view.main.health.detection.shake;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.CopInfo;
import cn.com.amome.amomeshoes.model.ShakeInfo;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetBatteryInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetPressCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadPressDataCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesStopPressDataCallback;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.bind.BindActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.amomeshoes.widget.GifView;

public class ShakeDetectionActivity extends Activity implements OnClickListener {
	private String TAG = "ShakeDetectionActivity";
	private Context mContext;
	private LinearLayout ll_scan_shoes, ll_stand, ll_stand_tip, ll_stand_pre,
			ll_foot_print, ll_double_open, ll_single_open, ll_double_close,
			ll_single_close;
	private RelativeLayout ll_shoepic;
	private TextView tv_title, tv_scanshoes_tip, tv_stand_tip,
			tv_stand_countdown, tv_test_tip, tv_shake_test_countdown;
	private ImageView iv_scan_shoes, iv_stand_countdown, iv_shake_double_tip,
			iv_shake_open_tip, iv_shake_close_tip, iv_shake_detection_double,
			iv_shake_detection_single, iv_shake_test_eye;
	private GifView gv_shake_posture, gv_shake_single_tip;
	private Button btn_shake_start_double_open, btn_shake_start_single_open,
			btn_shake_start_double_close, btn_shake_start_single_close;
	private PieChart pie_chart_shake;
	private AnimationDrawable ad = null;
	private static final int MSG_START_TEST = 0;
	private static final int MSG_PRESS_SUC = 1;
	/**三秒倒计时文本显示 */
	private static final int MSG_COUNTDOWN_TEXT = 2;
	/**播放检测开始*/
	private static final int MSG_SHAKE_SOUND_START_TEST = 3;
	/**10秒倒计时 */
	private static final int MSG_SHAKE_COUNTDOWN_TEST = 4;
	/**10秒倒计时文本显示 */
	private static final int MSG_SHAKE_COUNTDOWN_TEXT = 5; 
	/**停止双足睁眼检测*/
	private static final int MSG_SHAKE_SOUND_STOP_DOUBLE_OPEN_TEST = 6;
	/**停止单足睁眼检测*/
	private static final int MSG_SHAKE_SOUND_STOP_SINGLE_OPEN_TEST = 7;
	/**停止双足闭眼检测*/
	private static final int MSG_SHAKE_SOUND_STOP_DOUBLE_CLOSE_TEST = 8;
	/**停止单足闭眼检测*/
	private static final int MSG_SHAKE_SOUND_STOP_SINGLE_CLOSE_TEST = 9;
	/**测试结束*/
	private static final int MSG_SHAKE_TEST_END = 10;
	private static final int MSG_ADD_SHAKE_INFO = 11;
	private ObjectAnimator retateAnim;
	private final int REQ_STAND_TEACHING = 12;
	private final int REQ_RECONNECTION = 13;
	// 定义相关变量
	private int parse = 1;
	private int bleFlag = BleConstants.MSG_CONNECT;
	private int seconds = -1;
	private boolean jump_stand_view = false;
	/** 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次 */
	private boolean connect_success = true;
	private boolean touch_flag = false;
	/** 第一次成功连接标志，用来第一次画图的 */
	private boolean successflag = true;
	/** 用来进行统计正式测试压力期间的数据 */
	private boolean preCalFlag = false;
	/** 用来判断页面是否关闭 */
	private boolean alreadlyDestory = false;
	private Timer timer;
	private TimerTask task;
	private MediaPlayer soundShakeTip, soundCountdown1, soundCountdown2,
			soundTestStart, soundTestStop, soundShakeDoubleOpen,
			soundShakeDoubleClose, soundShakeSingleOpen, soundShakeSingleClose;
	private short[] rightPress = new short[64];//数据只取右脚的
	private List<CopInfo> doubleOpenCopList = new ArrayList<CopInfo>();
	private List<CopInfo> calDoubleOpenCopList = new ArrayList<CopInfo>();
	private List<CopInfo> singleOpenCopList = new ArrayList<CopInfo>();
	private List<CopInfo> calSingleOpenCopList = new ArrayList<CopInfo>();
	private List<CopInfo> doubleCloseCopList = new ArrayList<CopInfo>();
	private List<CopInfo> calDoubleCloseCopList = new ArrayList<CopInfo>();
	private List<CopInfo> singleCloseCopList = new ArrayList<CopInfo>();
	private List<CopInfo> calSingleCloseCopList = new ArrayList<CopInfo>();

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_TEST:
				touch_flag = true;
				successflag = true; // 准备播报语音
				ll_stand_pre.setVisibility(View.GONE);
				ll_foot_print.setVisibility(View.VISIBLE);
				break;
			case MSG_PRESS_SUC:
				// 双脚连接成功则会切换到站姿页面
				if (connect_success) {
					preTestFinish();
				}
				if (jump_stand_view && touch_flag) {
					// 用来触发预热期间的语音
					if (successflag) {
						mHandler.sendEmptyMessage(MSG_SHAKE_SOUND_START_TEST);
						successflag = false;
					}
					// 开始统计正式测试期间的数据 true 为正式测试阶段
					if (preCalFlag) {
						if (msg.arg1 == 1) {
							// 空
						} else if (msg.arg1 == 0) {
							rightPress = (short[]) msg.obj;
							// CopInfo copinfo = new CopInfo();
							CopInfo copinfo = CalDetection
									.calRightCopZuobiao(rightPress);
							if (copinfo != null) {
								if (parse == 1) {
									doubleOpenCopList.add(copinfo);
									calDoubleOpenCopList.add(copinfo);
								} else if (parse == 2) {
									singleOpenCopList.add(copinfo);
									calSingleOpenCopList.add(copinfo);
								} else if (parse == 3) {
									doubleCloseCopList.add(copinfo);
									calDoubleCloseCopList.add(copinfo);
								} else if (parse == 4) {
									singleCloseCopList.add(copinfo);
									calSingleCloseCopList.add(copinfo);
								}
							}
						}
					} else {
						if (msg.arg1 == 1) {
						} else if (msg.arg1 == 0) {
							rightPress = (short[]) msg.obj;
						}
					}
				}
				break;
			case MSG_COUNTDOWN_TEXT:
				if (seconds > 0) {
					tv_shake_test_countdown.setText(seconds + "");
				} else {
					seconds = -1;
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
				}
				break;
			case MSG_SHAKE_SOUND_START_TEST:
				// 语音：检测开始
				setData1(1, 100);
				tv_test_tip.setText("检测中");
				soundTestStart = MediaPlayer.create(mContext, R.raw.test_start);
				try {
					soundTestStart.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundTestStart
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer soundTestStart) {
								soundTestStart.release();
								soundTestStart = null;
								mHandler.sendEmptyMessage(MSG_SHAKE_COUNTDOWN_TEST);
							}
						});
				break;
			case MSG_SHAKE_COUNTDOWN_TEST:
				preCalFlag = true;
				pie_chart_shake.setVisibility(View.VISIBLE);
				pie_chart_shake.animateXY(10000, 10000); // 动画设置
				seconds = 11;
				timer = new Timer();
				task = new TimerTask() {
					@Override
					public void run() {
						seconds--;
						mHandler.sendEmptyMessage(MSG_SHAKE_COUNTDOWN_TEXT);
					}
				};
				if (seconds > 0) {
					timer = new Timer();
					timer.schedule(task, 0, 1000);
				}
				break;
			case MSG_SHAKE_COUNTDOWN_TEXT:
				if (seconds > 0) {
					tv_shake_test_countdown.setText(seconds + "");
				} else {
					seconds = -1;
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
					if (parse == 1) {
						mHandler.sendEmptyMessage(MSG_SHAKE_SOUND_STOP_DOUBLE_OPEN_TEST);
					} else if (parse == 2) {
						mHandler.sendEmptyMessage(MSG_SHAKE_SOUND_STOP_SINGLE_OPEN_TEST);
					} else if (parse == 3) {
						mHandler.sendEmptyMessage(MSG_SHAKE_SOUND_STOP_DOUBLE_CLOSE_TEST);
					} else if (parse == 4) {
						mHandler.sendEmptyMessage(MSG_SHAKE_SOUND_STOP_SINGLE_CLOSE_TEST);
					}
				}
				break;
			case MSG_SHAKE_SOUND_STOP_DOUBLE_OPEN_TEST:
				// 语音：检测结束
				parse = 0;
				soundTestStop = MediaPlayer.create(mContext, R.raw.test_stop);
				try {
					soundTestStop.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundTestStop
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatTestStop) {
								soundSquatTestStop.release();
								soundSquatTestStop = null;
								ll_stand_tip.setVisibility(View.GONE);
								ll_stand_pre.setVisibility(View.VISIBLE);
								ll_double_open.setBackgroundColor(Color.argb(0,
										255, 255, 255));
								ll_single_open.setBackgroundColor(mContext
										.getResources().getColor(
												R.color.green_grass));
								tv_stand_countdown.setText("抬左足睁眼检测");
								iv_shake_double_tip.setVisibility(View.GONE);
								gv_shake_single_tip.setVisibility(View.VISIBLE);
							}
						});
				break;
			case MSG_SHAKE_SOUND_STOP_SINGLE_OPEN_TEST:
				parse = 0;
				soundTestStop = MediaPlayer.create(mContext, R.raw.test_stop);
				try {
					soundTestStop.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundTestStop
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatTestStop) {
								soundSquatTestStop.release();
								soundSquatTestStop = null;
								ll_stand_tip.setVisibility(View.GONE);
								ll_stand_pre.setVisibility(View.VISIBLE);
								ll_single_open.setBackgroundColor(Color.argb(0,
										255, 255, 255));
								ll_double_close.setBackgroundColor(mContext
										.getResources().getColor(
												R.color.green_grass));
								tv_stand_countdown.setText("双足闭眼检测");
								iv_shake_test_eye
										.setImageResource(R.drawable.shake_close);
								gv_shake_single_tip.setVisibility(View.GONE);
								iv_shake_double_tip.setVisibility(View.VISIBLE);
								iv_shake_open_tip.setVisibility(View.GONE);
								iv_shake_close_tip.setVisibility(View.VISIBLE);
							}
						});
				break;
			case MSG_SHAKE_SOUND_STOP_DOUBLE_CLOSE_TEST:
				parse = 0;
				soundTestStop = MediaPlayer.create(mContext, R.raw.test_stop);
				try {
					soundTestStop.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundTestStop
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatTestStop) {
								soundSquatTestStop.release();
								soundSquatTestStop = null;
								ll_stand_tip.setVisibility(View.GONE);
								ll_stand_pre.setVisibility(View.VISIBLE);
								ll_double_close.setBackgroundColor(Color.argb(
										0, 255, 255, 255));
								ll_single_close.setBackgroundColor(mContext
										.getResources().getColor(
												R.color.green_grass));
								tv_stand_countdown.setText("抬左足闭眼检测");
								iv_shake_double_tip.setVisibility(View.GONE);
								gv_shake_single_tip.setVisibility(View.VISIBLE);
							}
						});
				break;
			case MSG_SHAKE_SOUND_STOP_SINGLE_CLOSE_TEST:
				parse = 0;
				soundTestStop = MediaPlayer.create(mContext, R.raw.test_stop);
				try {
					soundTestStop.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundTestStop
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatTestStop) {
								soundSquatTestStop.release();
								soundSquatTestStop = null;
								mHandler.sendEmptyMessage(MSG_SHAKE_TEST_END);
							}
						});
				break;
			case MSG_SHAKE_TEST_END:
				preCalFlag = false; // 停止统计压力数据
				addShakeInfo();
				break;
			case MSG_ADD_SHAKE_INFO:
				String re = (String) msg.obj;
				if (re.equals("0x00")) {
					Log.i(TAG, "添加摇一摇信息成功");
					try {
						AmomeApp.bleShoes
								.stopShoesPressData(shoesStopPressDataCallback); // 停止获取压力数据
					} catch (Exception e) {
						// TODO: handle exception
						Log.i(TAG, "停止获取压力数据时崩了，大概是还没建立连接");
						e.printStackTrace();
					}
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		mContext = this;
		setContentView(R.layout.activity_shake_detection);
		initData();
		initView();
		openBle();
	}

	private void initData() {
		doubleOpenCopList.clear();
		singleOpenCopList.clear();
		doubleCloseCopList.clear();
		singleCloseCopList.clear();
		parse = 1;
		bleFlag = BleConstants.MSG_CONNECT;
		seconds = 3;
		jump_stand_view = false;
		connect_success = true; // 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次。
		touch_flag = false;
		successflag = true; // 第一次成功连接标志，用来第一次画图的
		alreadlyDestory = false; // 用来判断页面是否关闭
	}

	private void initView() {
		ll_scan_shoes = (LinearLayout) findViewById(R.id.ll_scan_shoes);
		ll_stand = (LinearLayout) findViewById(R.id.ll_stand);
		ll_stand_tip = (LinearLayout) findViewById(R.id.ll_stand_tip);
		ll_foot_print = (LinearLayout) findViewById(R.id.ll_foot_print);
		ll_stand_pre = (LinearLayout) findViewById(R.id.ll_stand_pre);
		ll_double_open = (LinearLayout) findViewById(R.id.ll_double_open);
		ll_single_open = (LinearLayout) findViewById(R.id.ll_single_open);
		ll_double_close = (LinearLayout) findViewById(R.id.ll_double_close);
		ll_single_close = (LinearLayout) findViewById(R.id.ll_single_close);
		ll_shoepic = (RelativeLayout) findViewById(R.id.ll_shoepic);
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_scanshoes_tip = (TextView) findViewById(R.id.tv_scanshoes_tip);
		tv_stand_tip = (TextView) findViewById(R.id.tv_stand_tip);
		tv_stand_countdown = (TextView) findViewById(R.id.tv_stand_countdown);
		tv_test_tip = (TextView) findViewById(R.id.tv_test_tip);
		tv_shake_test_countdown = (TextView) findViewById(R.id.tv_shake_test_countdown);
		gv_shake_single_tip = (GifView) findViewById(R.id.gv_shake_single_tip);
		gv_shake_posture = (GifView) findViewById(R.id.gv_shake_posture);
		iv_stand_countdown = (ImageView) findViewById(R.id.iv_stand_countdown);
		iv_scan_shoes = (ImageView) findViewById(R.id.iv_scan_shoes);
		iv_shake_double_tip = (ImageView) findViewById(R.id.iv_shake_double_tip);
		iv_shake_open_tip = (ImageView) findViewById(R.id.iv_shake_open_tip);
		iv_shake_close_tip = (ImageView) findViewById(R.id.iv_shake_close_tip);
		iv_shake_detection_double = (ImageView) findViewById(R.id.iv_shake_detection_double);
		iv_shake_detection_single = (ImageView) findViewById(R.id.iv_shake_detection_single);
		iv_shake_test_eye = (ImageView) findViewById(R.id.iv_shake_test_eye);
		btn_shake_start_double_open = (Button) findViewById(R.id.btn_shake_start_double_open);
		btn_shake_start_single_open = (Button) findViewById(R.id.btn_shake_start_single_open);
		btn_shake_start_double_close = (Button) findViewById(R.id.btn_shake_start_double_close);
		btn_shake_start_single_close = (Button) findViewById(R.id.btn_shake_start_single_close);
		pie_chart_shake = (PieChart) findViewById(R.id.pie_chart_shake);

		ll_double_open.setBackgroundColor(mContext.getResources().getColor(
				R.color.green_grass));
		tv_title.setText("摇一摇");
		tv_scanshoes_tip.setText("智能鞋检测中");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));

		LayoutParams lp_gv_shake_posture = (LayoutParams) gv_shake_posture
				.getLayoutParams();
		lp_gv_shake_posture.width = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
		lp_gv_shake_posture.height = lp_gv_shake_posture.width;
		gv_shake_posture.setLayoutParams(lp_gv_shake_posture);

		LayoutParams lp_iv_shake_double_tip = (LayoutParams) iv_shake_double_tip
				.getLayoutParams();
		lp_iv_shake_double_tip.width = (int) (SpfUtil
				.readScreenWidthPx(mContext) / 3);
		lp_iv_shake_double_tip.height = lp_iv_shake_double_tip.width * 728 / 384;
		iv_shake_double_tip.setLayoutParams(lp_iv_shake_double_tip);
		gv_shake_single_tip.setLayoutParams(lp_iv_shake_double_tip);

		LayoutParams lp_iv_shake_open_tip = (LayoutParams) iv_shake_open_tip
				.getLayoutParams();
		lp_iv_shake_open_tip.width = (int) (SpfUtil.readScreenWidthPx(mContext) / 2.5);
		lp_iv_shake_open_tip.height = lp_iv_shake_open_tip.width * 166 / 499;
		iv_shake_open_tip.setLayoutParams(lp_iv_shake_open_tip);
		iv_shake_close_tip.setLayoutParams(lp_iv_shake_open_tip);

		iv_shake_close_tip
				.setBackgroundResource(R.drawable.animation_shake_close_eyes);
		ad = (AnimationDrawable) iv_shake_close_tip.getBackground();
		ad.start();

		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_stand_start_tip).setOnClickListener(this);
		findViewById(R.id.btn_stand_guidance).setOnClickListener(this);
		findViewById(R.id.btn_shake_start_pre).setOnClickListener(this);
		btn_shake_start_double_open.setOnClickListener(this);
		btn_shake_start_single_open.setOnClickListener(this);
		btn_shake_start_double_close.setOnClickListener(this);
		btn_shake_start_single_close.setOnClickListener(this);

		/**
		 * 是否使用百分比
		 */
		pie_chart_shake.setUsePercentValues(false);
		/**
		 * 描述信息
		 */
		pie_chart_shake.setDescription("");
		pie_chart_shake.setNoDataText("");
		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// pie_chart_shake.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		/**
		 * 设置圆环中间的文字
		 */
		// pie_chart_shake.setCenterText(generateCenterSpannableText());

		/**
		 * 圆环距离屏幕上下上下左右的距离
		 */
		pie_chart_shake.setExtraOffsets(0f, 0f, 0f, 0f);

		/**
		 * 是否显示圆环中间的洞
		 */
		pie_chart_shake.setDrawHoleEnabled(false); // 不显示
		/**
		 * 设置中间洞的颜色
		 */
		pie_chart_shake.setHoleColor(Color.WHITE);

		/**
		 * 设置圆环中间洞的半径
		 */
		pie_chart_shake.setHoleRadius(60f);

		/**
		 * 是否显示洞中间文本
		 */
		pie_chart_shake.setDrawCenterText(false); // 不显示

		/**
		 * 触摸是否可以旋转以及松手后旋转的度数
		 */
		pie_chart_shake.setRotationAngle(0); // 初始旋转角度
		// enable rotation of the chart by touch
		pie_chart_shake.setRotationEnabled(false); // 不可以旋转

		/**
		 * add a selection listener 值改变时候的监听
		 */
		// pie_chart_shake.setOnChartValueSelectedListener(this);

		// setData1(2, 100);

		Legend l = pie_chart_shake.getLegend();
		l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setEnabled(false); // 不显示图例
		l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		l.setForm(Legend.LegendForm.CIRCLE);

		l.setFormSize(8f);
		l.setFormToTextSpace(4f);
		l.setXEntrySpace(6f);

		// pie_chart_shake.animateY(6000, Easing.EasingOption.EaseInOutQuad); //
		// 动画设置
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.btn_stand_start_tip:
			getStandTip();
			break;
		case R.id.btn_shake_start_pre:
			startShakeTip();
			break;
		case R.id.btn_stand_guidance:
			startActivityForResult(new Intent(mContext,
					ShakeTeachingActivity.class), REQ_STAND_TEACHING);
			break;
		case R.id.btn_shake_start_double_open:
			parse = 1;
			Log.i(TAG, "parse----" + parse);
			pie_chart_shake.setVisibility(View.GONE);
			ll_stand_pre.setVisibility(View.GONE);
			ll_foot_print.setVisibility(View.VISIBLE);
			tv_shake_test_countdown.setText(3 + "");
			btn_shake_start_double_open.setVisibility(View.GONE);
			btn_shake_start_single_open.setVisibility(View.VISIBLE);
			soundShakeDoubleOpen = MediaPlayer.create(mContext,
					R.raw.shake_pre_double_open);
			try {
				soundShakeDoubleOpen.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			soundShakeDoubleOpen
					.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(
								MediaPlayer soundShakeDoubleOpen) {
							soundShakeDoubleOpen.release();
							soundShakeDoubleOpen = null;
							startCountdownTip();
						}
					});
			break;
		case R.id.btn_shake_start_single_open:
			parse = 2;
			Log.i(TAG, "parse----" + parse);
			pie_chart_shake.setVisibility(View.GONE);
			iv_shake_detection_double.setVisibility(View.GONE);
			iv_shake_detection_single.setVisibility(View.VISIBLE);
			ll_stand_pre.setVisibility(View.GONE);
			ll_foot_print.setVisibility(View.VISIBLE);
			tv_shake_test_countdown.setText(3 + "");
			btn_shake_start_single_open.setVisibility(View.GONE);
			btn_shake_start_double_close.setVisibility(View.VISIBLE);
			soundShakeSingleOpen = MediaPlayer.create(mContext,
					R.raw.shake_pre_single_open);
			try {
				soundShakeSingleOpen.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			soundShakeSingleOpen
					.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(
								MediaPlayer soundShakeSingleOpen) {
							soundShakeSingleOpen.release();
							soundShakeSingleOpen = null;
							startCountdownTip();
						}
					});
			break;
		case R.id.btn_shake_start_double_close:
			parse = 3;
			Log.i(TAG, "parse----" + parse);
			pie_chart_shake.setVisibility(View.GONE);
			iv_shake_detection_double.setVisibility(View.VISIBLE);
			iv_shake_detection_single.setVisibility(View.GONE);
			ll_stand_pre.setVisibility(View.GONE);
			ll_foot_print.setVisibility(View.VISIBLE);
			tv_shake_test_countdown.setText(3 + "");
			btn_shake_start_double_close.setVisibility(View.GONE);
			btn_shake_start_single_close.setVisibility(View.VISIBLE);
			soundShakeDoubleClose = MediaPlayer.create(mContext,
					R.raw.shake_pre_double_close);
			try {
				soundShakeDoubleClose.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			soundShakeDoubleClose
					.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(
								MediaPlayer soundShakeDoubleClose) {
							soundShakeDoubleClose.release();
							soundShakeDoubleClose = null;
							startCountdownTip();
						}
					});
			break;
		case R.id.btn_shake_start_single_close:
			parse = 4;
			Log.i(TAG, "parse----" + parse);
			pie_chart_shake.setVisibility(View.GONE);
			iv_shake_detection_double.setVisibility(View.GONE);
			iv_shake_detection_single.setVisibility(View.VISIBLE);
			ll_stand_pre.setVisibility(View.GONE);
			ll_foot_print.setVisibility(View.VISIBLE);
			tv_shake_test_countdown.setText(3 + "");
			soundShakeSingleClose = MediaPlayer.create(mContext,
					R.raw.shake_pre_single_close);
			try {
				soundShakeSingleClose.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			soundShakeSingleClose
					.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(
								MediaPlayer soundShakeSingleClose) {
							soundShakeSingleClose.release();
							soundShakeSingleClose = null;
							startCountdownTip();
						}
					});
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		alreadlyDestory = true;
		cleanSoundAndTimer();
		try {
			AmomeApp.bleShoes.stopShoesPressData(shoesStopPressDataCallback); // 停止获取压力数据
		} catch (Exception e) {
			// TODO: handle exception
			Log.i(TAG, "停止获取压力数据时崩了，大概是还没建立连接");
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
		if (AmomeApp.pauseFlag) {
			initData();
			openBle();
			AmomeApp.pauseFlag = false;
			ll_double_close.setBackgroundColor(Color.argb(0, 255, 255, 255));
			ll_single_open.setBackgroundColor(Color.argb(0, 255, 255, 255));
			ll_double_close.setBackgroundColor(Color.argb(0, 255, 255, 255));
			ll_double_open.setBackgroundColor(mContext.getResources().getColor(
					R.color.green_grass));
			tv_stand_countdown.setText("双足睁眼检测");
			gv_shake_single_tip.setVisibility(View.GONE);
			iv_shake_double_tip.setVisibility(View.VISIBLE);
			iv_shake_close_tip.setVisibility(View.GONE);
			iv_shake_open_tip.setVisibility(View.VISIBLE);
			btn_shake_start_double_close.setVisibility(View.GONE);
			btn_shake_start_single_close.setVisibility(View.GONE);
			btn_shake_start_single_open.setVisibility(View.GONE);
			btn_shake_start_double_open.setVisibility(View.VISIBLE);
			iv_shake_detection_single.setVisibility(View.GONE);
			iv_shake_detection_double.setVisibility(View.VISIBLE);
			iv_shake_test_eye.setImageResource(R.drawable.shake_open);
		}
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
		AmomeApp.pauseFlag = true;
		cleanSoundAndTimer();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	// 第一步 检测和连接
	private void startRotateAnimation() {
		retateAnim = ObjectAnimator
				.ofFloat(iv_scan_shoes, "rotation", 0f, 360f);
		retateAnim.setDuration(5000);
		retateAnim.setInterpolator(new LinearInterpolator());// not stop
		retateAnim.setRepeatCount(-1);// set repeat time forever
		retateAnim.start();
	}

	public boolean enableBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		// 判断是否有蓝牙功能
		if (adapter != null && !adapter.isEnabled()) {
			Log.i(TAG, "蓝牙未打开");
			return false;
		}
		Log.i(TAG, "蓝牙已打开");
		return true;
	}

	/**
	 * 开启蓝牙
	 */
	private void openBle() {
		if (enableBluetooth()) {
			Log.i(TAG, "进入前蓝牙就打开的情况");
			preConnect();
		} else {
			Log.i(TAG, "未开启蓝牙");
			finish();
		}
	}

	/**
	 * 建立连接
	 */
	private void preConnect() {
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			finish();
			startActivity(new Intent(mContext, BindActivity.class));
		} else {
			startRotateAnimation();
			try {
				if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
					Log.i(TAG, "智能鞋状态为已连接");
					AmomeApp.bleShoes.getShoesPressData(shoesGetPressCallback);
				} else {
					Log.i(TAG, "智能鞋状态为未连接");
					bleFlag = BleConstants.MSG_GETBATTERY;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
			}
		}
	}

	private void connectShoes() {
		if (AmomeApp.bleShoes == null) {
			AmomeApp.bleShoes = new BleShoes(
					SpfUtil.readDeviceToShoeLeft(mContext),
					SpfUtil.readDeviceToShoeRight(mContext), shoesCreCallback,
					mContext);
		} else {
			Log.i(TAG, "智能鞋已经连接了");
		}
	}

	BleShoes.shoesCreCallback shoesCreCallback = new shoesCreCallback() {

		@Override
		public void isCreSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋连接完成");
				AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTED;
				T.showToast(mContext, "智能鞋连接完成", 0);
				switch (bleFlag) {
				case BleConstants.MSG_GETPRESS:
					AmomeApp.bleShoes.getShoesPressData(shoesGetPressCallback);
					break;
				case BleConstants.MSG_GETBATTERY:
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
					break;
				default:
					break;
				}
			} else {
				Log.i(TAG, "该双智能鞋连接失败，准备断开连接触发重连提示框");
				// 建立xP后，如果某xP为空会执行这里
				try {
					AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				} catch (Exception e) {
					// TODO: handle exception
					Log.i(TAG, "断开连接时崩了，大概是一只脚都没建立成功");
					e.printStackTrace();
				}
				Intent intent = new Intent(mContext, ReconnectionActivity.class);
				startActivityForResult(intent, REQ_RECONNECTION);
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				Log.i(TAG, "AmomeApp.bleShoes置空");
			}
		}

		@Override
		public void isConnect(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (!arg0) {
				Log.i(TAG, addr + "设备连接出错，准备断开连接触发重连提示框");
				if (!alreadlyDestory) {
					Intent intent = new Intent(mContext,
							ReconnectionActivity.class);
					startActivityForResult(intent, REQ_RECONNECTION);
				} else {
					Log.i(TAG, "页面退出了，不需要打开重连提示框");
				}

				if (addr.equals(SpfUtil.readDeviceToShoeLeft(mContext))) {
					Log.i(TAG,
							"有一只断开了，准备断开另一只"
									+ SpfUtil.readDeviceToShoeRight(mContext));
					try {
						AmomeApp.bleShoes.disSingleShoeConnect(
								shoesSingleDisconnectCallback,
								SpfUtil.readDeviceToShoeRight(mContext));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				} else {
					Log.i(TAG,
							"有一只断开了，准备断开另一只"
									+ SpfUtil.readDeviceToShoeLeft(mContext));
					try {
						AmomeApp.bleShoes.disSingleShoeConnect(
								shoesSingleDisconnectCallback,
								SpfUtil.readDeviceToShoeLeft(mContext));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				Log.i(TAG, "AmomeApp.bleShoes置空");
			}
		}

		@Override
		public void isRec(boolean arg0) {
			// TODO Auto-generated method stub
			if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
				T.showToast(mContext, "连接已断开", 0);
				Log.i(TAG, "连接已断开");
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				try {
					AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				} catch (Exception e) {
					// TODO: handle exception
					Log.i(TAG, "断开连接时崩了，大概是还没建立连接");
					e.printStackTrace();
				}
				finish();
			}
		}
	};

	BleShoes.shoesGetBatteryInfoCallback shoesGetBatteryInfoCallback = new shoesGetBatteryInfoCallback() {

		@Override
		public void isGetBatterySucc(boolean arg0, int leftVal, int rightVal,boolean leftCharge, boolean rightCharge) {
			// TODO Auto-generated method stub
			DialogUtil.hideProgressDialog();
			if (arg0) {
				Log.i(TAG, "电量" + leftVal + "," + rightVal);
				if (!alreadlyDestory) {
					if (leftVal < 5 || rightVal < 5) {
						T.showToast(mContext, "电量不足请充电" + leftVal + " "
								+ rightVal, 0);
						DialogUtil.showAlertDialog(mContext, "",
								"电量不足，是否继续使用？", "确定", "取消",
								new OnAlertViewClickListener() {
									@Override
									public void confirm() {
										AmomeApp.bleShoes
												.getShoesPressData(shoesGetPressCallback);
									}

									@Override
									public void cancel() {
										AmomeApp.bleShoes
												.disShoesConnect(shoesDisconnectCallback); // 断开连接
										finish();
									}
								});
					} else {
						AmomeApp.bleShoes
								.getShoesPressData(shoesGetPressCallback);
					}
				} else {
					Log.i(TAG, "页面退出了，不需要获取电池电量");
				}
			}
		}
	};

	BleShoes.shoesGetPressCallback shoesGetPressCallback = new shoesGetPressCallback() {

		@Override
		public void isGetPressSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋压力数据获取成功");
				AmomeApp.bleShoes
						.readShoesPressData(shoesReadPressDataCallback);
			} else {
				Log.i(TAG, "智能鞋压力数据获取失败,准备断开连接触发重连提示框");
				try {
					AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				} catch (Exception e) {
					// TODO: handle exception
					Log.i(TAG, "断开连接时崩了，大概是一只脚都没建立成功");
					e.printStackTrace();
				}
				Intent intent = new Intent(mContext, ReconnectionActivity.class);
				startActivityForResult(intent, REQ_RECONNECTION);
				AmomeApp.bleShoes = null;
				Log.i(TAG, "AmomeApp.bleShoes置空");
			}
		}
	};

	BleShoes.shoesReadPressDataCallback shoesReadPressDataCallback = new shoesReadPressDataCallback() {

		@Override
		public void readPressData(String addr, short[] PressData, long time) {
			// TODO Auto-generated method stub
			// 在这里处理压力数据
			Message msg = Message.obtain();
			int flag = Integer.parseInt(addr.substring(16));
			if (flag == 0) {
				msg.arg1 = 0;
			} else if (flag == 1) {
				msg.arg1 = 1;
			}
			msg.obj = PressData;
			msg.what = MSG_PRESS_SUC;
			mHandler.sendMessage(msg);
		}
	};

	BleShoes.shoesStopPressDataCallback shoesStopPressDataCallback = new shoesStopPressDataCallback() {

		@Override
		public void isStopPressSuc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				if (!alreadlyDestory) {
					Log.i(TAG, "停止压力数据成功，准备跳转");
					startActivity(new Intent(mContext,
							ShakeReportActivity.class));
					finish();
				} else {
					Log.i(TAG, "页面退出，停止压力数据成功");
				}
			} else {
				if (!alreadlyDestory) {
					Log.i(TAG, "停止压力数据失败，准备跳转");
					startActivity(new Intent(mContext,
							ShakeReportActivity.class));
					finish();
				}
			}
		}
	};

	BleShoes.shoesDisconnectCallback shoesDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋断开连接成功");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				Log.i(TAG, "AmomeApp.bleShoes置空");
			} else {
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				Log.i(TAG, "AmomeApp.bleShoes置空");
			}
		}
	};

	BleShoes.shoesDisconnectCallback shoesSingleDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "另一只智能鞋断开连接成功");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			} else {
				Log.i(TAG, "另一只智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			}
		}
	};

	// 第二步 开始界面
	/**
	 * 进入开始页面（双脚已连接成功后调用）
	 */
	private void preTestFinish() {
		Log.i(TAG, "准备开始");
		retateAnim.cancel();
		ll_scan_shoes.setVisibility(View.GONE);
		ll_stand.setVisibility(View.VISIBLE);
		jump_stand_view = true; // 切换到站姿页面
		connect_success = false;
	}

	// 第三步 进入注意事项
	/**
	 * 进入注意事项
	 */
	private void getStandTip() {
		ll_stand.setVisibility(View.GONE);
		ll_stand_tip.setVisibility(View.VISIBLE);
		soundShakeTip = MediaPlayer.create(mContext, R.raw.shake_tip);
		try {
			soundShakeTip.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundShakeTip.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundShakeTip) {
				soundShakeTip.release();
				soundShakeTip = null;
			}
		});
	}


	// 第四步 进入准备测试页面（四项）
	private void startShakeTip() {
		if (soundShakeTip != null) {
			soundShakeTip.release();
			soundShakeTip = null;
		}
		ll_stand_tip.setVisibility(View.GONE);
		ll_stand_pre.setVisibility(View.VISIBLE);
	}
	
	// 第五步 进入倒计时
	/**
	 * 倒计时
	 */
	private void startCountdownTip() {
		soundCountdown2 = MediaPlayer.create(mContext, R.raw.squat_countdown2);
		try {
			soundCountdown2.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundCountdown2.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundCountdown2) {
				soundCountdown2.release();
				soundCountdown2 = null;
				touch_flag = true;
				successflag = true; // 准备播报语音
			}
		});
		seconds = 3;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				seconds--;
				mHandler.sendEmptyMessage(MSG_COUNTDOWN_TEXT);
			}
		};
		if (seconds > 0) {
			timer = new Timer();
			timer.schedule(task, 0, 1000);
		}
	}


	/**
	 * 添加摇一摇信息
	 */
	private void addShakeInfo() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.ADDSHAKEINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		ShakeInfo shakeInfo = CalDetection.calShakeInfo(doubleOpenCopList,
				singleOpenCopList, doubleCloseCopList, singleCloseCopList,
				calDoubleOpenCopList, calSingleOpenCopList,
				calDoubleCloseCopList, calSingleCloseCopList, 10);

		params.put("bo_outerarea", shakeInfo.getBoOuterArea() + "");
		params.put("so_outerarea", shakeInfo.getSoOuterArea() + "");
		params.put("bc_outerarea", shakeInfo.getBcOuterArea() + "");
		params.put("sc_outerarea", shakeInfo.getScOuterArea() + "");

		params.put("bo_totaltrail", shakeInfo.getBoTotalTrail() + "");
		params.put("so_totaltrail", shakeInfo.getSoTotalTrail() + "");
		params.put("bc_totaltrail", shakeInfo.getBcTotalTrail() + "");
		params.put("sc_totaltrail", shakeInfo.getScTotalTrail() + "");

		params.put("bo_trailperarea", shakeInfo.getBoTrailperarea() + "");
		params.put("so_trailperarea", shakeInfo.getSoTrailperarea() + "");
		params.put("bc_trailperarea", shakeInfo.getBcTrailperarea() + "");
		params.put("sc_trailperarea", shakeInfo.getScTrailperarea() + "");

		params.put("bo_romberg", shakeInfo.getBoRomberg() + "");
		params.put("so_romberg", shakeInfo.getSoRomberg() + "");
		params.put("bc_romberg", shakeInfo.getBcRomberg() + "");
		params.put("sc_romberg", shakeInfo.getScRomberg() + "");

		params.put("bo_trailpersecond", shakeInfo.getBoTrailpersecond() + "");
		params.put("so_trailpersecond", shakeInfo.getSoTrailpersecond() + "");
		params.put("bc_trailpersecond", shakeInfo.getBcTrailpersecond() + "");
		params.put("sc_trailpersecond", shakeInfo.getScTrailpersecond() + "");

		params.put("bo_cop", shakeInfo.getBoCopStr());
		params.put("so_cop", shakeInfo.getSoCopStr());
		params.put("bc_cop", shakeInfo.getBcCopStr());
		params.put("sc_cop", shakeInfo.getScCopStr());

		params.put("bo_xoffset", shakeInfo.getBoXoffset() + "");
		params.put("bo_yoffset", shakeInfo.getBoYoffset() + "");
		params.put("so_xoffset", shakeInfo.getSoXoffset() + "");
		params.put("so_yoffset", shakeInfo.getSoYoffset() + "");
		params.put("bc_xoffset", shakeInfo.getBcXoffset() + "");
		params.put("bc_yoffset", shakeInfo.getBcYoffset() + "");
		params.put("sc_xoffset", shakeInfo.getScXoffset() + "");
		params.put("sc_yoffset", shakeInfo.getScYoffset() + "");

		params.put("bo_balanceindex", shakeInfo.getBoBalanceindex());
		params.put("bc_balanceindex", shakeInfo.getBcBalanceindex());
		params.put("so_balanceindex", shakeInfo.getSoBalanceindex());
		params.put("sc_balanceindex", shakeInfo.getScBalanceindex());
		params.put("compositeindex", shakeInfo.getCompositeindex());

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_SHAKE_INFO, params,
				ClientConstant.SHAKE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_ADD_SHAKE_INFO:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					int return_code = obj.getInt("return_code");
					String return_msg = null;
					Message msg = Message.obtain();
					if (return_code == 0) {
						JSONArray jsonArray = obj.getJSONArray("return_msg");
						JSONObject jsonObj = jsonArray.getJSONObject(0);
						return_msg = jsonObj.getString("retval");
						msg.what = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_ADD_SHAKE_INFO解析失败");
				}
				break;
			default:
				break;
			}

		}

		@Override
		public void onHttpPostFailure(int type, int statusCode, Header[] arg1,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		DialogUtil.showAlertDialog(mContext, "", "正在测试中，确定退出？", "确定", "取消",
				new OnAlertViewClickListener() {
					@Override
					public void confirm() {
						finish();
					}

					@Override
					public void cancel() {
					}
				});
	}

	private void cleanSoundAndTimer() {
		try {
			soundShakeTip.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundCountdown1.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundCountdown2.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundShakeDoubleOpen.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundShakeDoubleClose.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundShakeSingleOpen.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundShakeSingleClose.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		soundShakeTip = null;
		soundCountdown1 = null;
		soundCountdown2 = null;
		soundTestStart = null;
		soundTestStop = null;
		soundShakeDoubleOpen = null;
		soundShakeDoubleClose = null;
		soundShakeSingleOpen = null;
		soundShakeSingleClose = null;
		if (timer != null) {
			timer.cancel();
		}
		if (task != null) {
			task.cancel();
		}
		timer = null;
		task = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_RECONNECTION) {
			if (resultCode == RESULT_OK) {
				Log.i(TAG, TAG + "收到了消息");
				jump_stand_view = false;
				connect_success = true; // 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次。
				touch_flag = false;
				successflag = true; // 第一次成功连接标志，用来第一次画图的
				preCalFlag = false; // 用来进行统计压力图形倒计时的
				try {
					soundShakeTip.release();
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					soundCountdown1.release();
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					soundCountdown2.release();
				} catch (Exception e) {
					// TODO: handle exception
				}
				soundShakeTip = null;
				soundCountdown1 = null;
				soundCountdown2 = null;
				soundTestStart = null;
				soundTestStop = null;
				if (timer != null) {
					timer.cancel();
				}
				if (task != null) {
					task.cancel();
				}
				timer = null;
				task = null;
				initView();
				openBle();
			}
		} else if (requestCode == REQ_STAND_TEACHING) {
			if (resultCode == RESULT_OK) {
				getStandTip();
			}
		}
	}

	private void setData1(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		yVals1.add(new Entry(1f, 0));
		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度

		ArrayList<Integer> colors = new ArrayList<Integer>();

		colors.add(Color.argb(100, 0, 0, 0));
		colors.add(Color.rgb(137, 137, 137));
		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data.setValueTextColor(Color.BLACK);
		// data.setValueTypeface(tf);
		// 设置X显示的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		pie_chart_shake.setData(data);

		// undo all highlights
		pie_chart_shake.highlightValues(null);

		pie_chart_shake.invalidate();
	}
}
