package cn.com.amome.amomeshoes.view.main.health.detection.walk;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.com.amome.amomeshoes.model.PressCharacterModel;
import cn.com.amome.amomeshoes.model.PressData;
import cn.com.amome.amomeshoes.model.StepModel;
import cn.com.amome.amomeshoes.model.WalkInfo;
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
import cn.com.amome.amomeshoes.view.main.bind.PrepareScanActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.amomeshoes.widget.GifView;

public class WalkDetectionActivity extends Activity implements OnClickListener {
	private String TAG = "WalkDetectionActivity";
	private Context mContext;
	private LinearLayout ll_scan_shoes, ll_stand, ll_squat_tip,
			ll_squat_countdown, ll_foot_print;
	private TextView tv_title, tv_scanshoes_tip, tv_stand_tip;
	private ImageView iv_scan_shoes, iv_stand_posture, iv_squat_countdown;
	private GifView iv_squat_posture, gv_walk_tip;
	private ObjectAnimator retateAnim;
	private static final int MSG_START_TEST = 0;
	/** 准备阶段播放321 */
	private static final int MSG_COUNTDOWN_START = 1;
	private static final int MSG_COUNTDOWN_TEXT = 2;
	private static final int MSG_PRESS_SUC = 3;
	/** 播放检测开始 */
	private static final int MSG_WALK_SOUND_START_TEST = 4;
	/** 播放检测即将结束，请在3S内停止行走 */
	private static final int MSG_WALK_SOUND_STOP_WALK = 5;
	/** 测试结束前播放321 */
	private static final int MSG_WALK_SOUND_STOP_3S = 6;
	/** 播放检测结束 */
	private static final int MSG_WALK_SOUND_STOP_TEST = 7;
	/** 测试结束 */
	private static final int MSG_WALK_TEST_END = 8;
	private static final int MSG_ADD_WALK_INFO = 9;
	private final int REQ_WALK_TEACHING = 10;
	private final int REQ_RECONNECTION = 11;
	private Timer timer;
	private TimerTask task;
	private MediaPlayer soundWalkTip, soundWalkCountdown1, soundCountdown2,
			soundTestStart, soundSquatStartSquat, soundSquatStartStand,
			soundTestStop;
	// 定义相关变量
	private int bleFlag = BleConstants.MSG_CONNECT;
	private int seconds = -1;
	private boolean jump_stand_view = false;
	/** 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次 */
	private boolean judge_success = true;
	private boolean touch_flag = false;
	/** 第一次成功连接标志，用来触发语音 */
	private boolean successflag = true;
	/** 用来进行统计正式测试压力期间的数据 */
	private boolean preCalFlag = false;
	/** 用来判断页面是否关闭 */
	private boolean alreadlyDestory = false;

	private List<PressData> leftDataList = new ArrayList<PressData>();
	private List<PressData> rightDataList = new ArrayList<PressData>();

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_START_TEST:
				touch_flag = true;
				successflag = true; // 准备播报语音
				ll_squat_countdown.setVisibility(View.GONE);
				ll_foot_print.setVisibility(View.VISIBLE);
				break;
			case MSG_COUNTDOWN_START:
				// 语音：321
				soundCountdown2 = MediaPlayer.create(mContext,
						R.raw.squat_countdown2);
				try {
					soundCountdown2.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundCountdown2
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer soundCountdown2) {
								soundCountdown2.release();
								soundCountdown2 = null;
								mHandler.sendEmptyMessage(MSG_START_TEST);
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
				break;
			case MSG_COUNTDOWN_TEXT:
				if (seconds == 2) {
					iv_squat_countdown
							.setImageResource(R.drawable.squat_countdown2);
				} else if (seconds == 1) {
					iv_squat_countdown
							.setImageResource(R.drawable.squat_countdown1);
				}
				break;
			case MSG_PRESS_SUC:
				// 双脚连接成功则会切换到站姿页面
				if (judge_success) {
					preTestFinish();
				}
				if (jump_stand_view && touch_flag) {
					// 用来触发预热期间的语音
					if (successflag) {
						mHandler.sendEmptyMessage(MSG_WALK_SOUND_START_TEST);
						successflag = false;
					}
					// 开始统计正式测试期间的数据 true 为正式测试阶段
					if (preCalFlag) {
						if (msg.arg1 == 1) {
							PressData leftData = (PressData) msg.obj;
							leftDataList.add(leftData);
						} else if (msg.arg1 == 0) {
							PressData rightData = (PressData) msg.obj;
							rightDataList.add(rightData);
						}
					} else {
						// Log.i(TAG, "还没开始");
					}
				}
				break;
			case MSG_WALK_SOUND_START_TEST:
				// 语音：检测开始
				Log.i(TAG, "语音：检测开始");
				soundTestStart = MediaPlayer.create(mContext,
						R.raw.walk_test_start);
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
								preCalFlag = true;
							}
						});
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(MSG_WALK_SOUND_STOP_WALK);
					}
				};
				timer = new Timer();
				timer.schedule(task, 15000);
				break;
			case MSG_WALK_SOUND_STOP_WALK:
				// 语音：检测即将结束，请在3S内停止行走
				Log.i(TAG, "语音：检测即将结束，请在3S内停止行走");
				preCalFlag = false; // 停止统计压力数据
				soundSquatStartStand = MediaPlayer.create(mContext,
						R.raw.walk_stop_walk);
				try {
					soundSquatStartStand.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundSquatStartStand
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatStartStand) {
								soundSquatStartStand.release();
								soundSquatStartStand = null;
								mHandler.sendEmptyMessage(MSG_WALK_SOUND_STOP_3S);
							}
						});
				break;
			case MSG_WALK_SOUND_STOP_3S:
				// 语音：321
				Log.i(TAG, "语音：321");
				soundCountdown2 = MediaPlayer.create(mContext,
						R.raw.squat_countdown2);
				try {
					soundCountdown2.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundCountdown2
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer soundCountdown2) {
								soundCountdown2.release();
								soundCountdown2 = null;
								mHandler.sendEmptyMessage(MSG_WALK_SOUND_STOP_TEST);
							}
						});
				break;
			case MSG_WALK_SOUND_STOP_TEST:
				// 语音：检测结束
				Log.i(TAG, "语音：检测结束");
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
							public void onCompletion(MediaPlayer soundTestStop) {
								soundTestStop.release();
								soundTestStop = null;
								mHandler.sendEmptyMessage(MSG_WALK_TEST_END);
							}
						});
				break;
			case MSG_WALK_TEST_END:
				StepModel leftStepModel = CalDetection.calCharacterValue(
						leftDataList, "left");
				StepModel rightStepModel = CalDetection.calCharacterValue(
						rightDataList, "right");
				List<PressCharacterModel> leftCharacterList = leftStepModel
						.getCharacterList();
				List<PressCharacterModel> rightCharacterList = rightStepModel
						.getCharacterList();

				if (leftCharacterList.size() > 2
						&& rightCharacterList.size() > 2) {
					WalkInfo walkInfo = CalDetection.calStepTime(
							leftCharacterList, rightCharacterList);
					int leftSteps = leftStepModel.getStepNum();
					int rightSteps = rightStepModel.getStepNum();
					if (leftSteps > rightSteps) {
						rightSteps = rightSteps + 1;
					} else if (leftSteps < rightSteps) {
						leftSteps = leftSteps + 1;
					}
					int leftStepsFrc = leftSteps * 60 / 15;
					int rightStepsFrc = rightSteps * 60 / 15;
					if (walkInfo != null) {
						// 添加走一走信息
						addWalkInfo(walkInfo, leftStepsFrc, rightStepsFrc);
					} else {
						Log.i(TAG, "数据错误，walkInfo为空");
						//T.showToast(mContext, "数据异常，请重新测试", 0);
						//finish();
						//修改不报异常toast，改为设置正常/优秀/0/0...
						addWalkInfo();
					}
				} else {
					Log.i(TAG, "数据错误，CharacterList为空");
					//T.showToast(mContext, "数据异常，请重新测试", 0);
					//finish();
					//修改不报异常toast，改为设置正常/优秀/0/0...
					addWalkInfo();
				}

				break;
			case MSG_ADD_WALK_INFO:
				String re = (String) msg.obj;
				if (re.equals("0x00")) {
					Log.i(TAG, "添加走一走信息成功");
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
		mContext = this;
		setContentView(R.layout.activity_walk_detection);
		initData();
		initView();
		openBle();
	}

	private void initData() {
		bleFlag = BleConstants.MSG_CONNECT;
		seconds = 3;
		jump_stand_view = false;
		judge_success = true; // 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次。
		touch_flag = false;
		successflag = true; // 第一次成功连接标志，用来触发语音
		preCalFlag = false; // 用来进行统计正式测试压力期间的数据
		alreadlyDestory = false; // 用来判断页面是否关闭
	}

	private void initView() {
		ll_scan_shoes = (LinearLayout) findViewById(R.id.ll_scan_shoes);
		ll_stand = (LinearLayout) findViewById(R.id.ll_stand);
		ll_squat_tip = (LinearLayout) findViewById(R.id.ll_squat_tip);
		ll_foot_print = (LinearLayout) findViewById(R.id.ll_foot_print);
		ll_squat_countdown = (LinearLayout) findViewById(R.id.ll_squat_countdown);
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_scan_shoes = (ImageView) findViewById(R.id.iv_scan_shoes);
		iv_stand_posture = (ImageView) findViewById(R.id.iv_stand_posture);
		tv_scanshoes_tip = (TextView) findViewById(R.id.tv_scanshoes_tip);
		tv_stand_tip = (TextView) findViewById(R.id.tv_stand_tip);
		iv_squat_countdown = (ImageView) findViewById(R.id.iv_squat_countdown);
		iv_squat_posture = (GifView) findViewById(R.id.iv_squat_posture);
		gv_walk_tip = (GifView) findViewById(R.id.gv_walk_tip);
		tv_title.setText("走一走");
		tv_scanshoes_tip.setText("智能鞋检测中");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));

		LayoutParams lp_iv_squat_posture = (LayoutParams) iv_squat_posture
				.getLayoutParams();
		lp_iv_squat_posture.width = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
		lp_iv_squat_posture.height = lp_iv_squat_posture.width;
		iv_squat_posture.setLayoutParams(lp_iv_squat_posture);

		LayoutParams lp_gv_walk_tip = (LayoutParams) gv_walk_tip
				.getLayoutParams();
		lp_gv_walk_tip.width = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
		lp_gv_walk_tip.height = lp_gv_walk_tip.width * 385 / 214;
		gv_walk_tip.setLayoutParams(lp_gv_walk_tip);

		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_squat_start_tip).setOnClickListener(this);
		findViewById(R.id.btn_squat_guidance).setOnClickListener(this);
		findViewById(R.id.btn_squat_start_countdown_tip).setOnClickListener(
				this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.btn_squat_start_tip:
			getSquatTip();
			break;
		case R.id.btn_squat_start_countdown_tip:
			startCountdownTip();
			break;
		case R.id.btn_squat_guidance:
			startActivityForResult(new Intent(mContext,
					WalkTeachingActivity.class), REQ_WALK_TEACHING);
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
			iv_squat_countdown.setImageResource(R.drawable.squat_countdown3);
			openBle();
			AmomeApp.pauseFlag = false;
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
			startActivity(new Intent(mContext, PrepareScanActivity.class));
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
			AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTING;
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
				// 这里有问题
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
			msg.obj = new PressData(PressData, time);
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
					startActivity(new Intent(mContext, WalkReportActivity.class));
					finish();
				} else {
					Log.i(TAG, "页面退出，停止压力数据成功");
				}
			} else {
				if (!alreadlyDestory) {
					Log.i(TAG, "停止压力数据失败，准备跳转");
					startActivity(new Intent(mContext, WalkReportActivity.class));
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
			} else {
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
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
		judge_success = false;
	}

	// 第三步 进入注意事项
	/**
	 * 进入注意事项
	 */
	private void getSquatTip() {
		ll_stand.setVisibility(View.GONE);
		ll_squat_tip.setVisibility(View.VISIBLE);
		soundWalkTip = MediaPlayer.create(mContext, R.raw.walk_tip);
		try {
			soundWalkTip.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundWalkTip.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundWalkTip) {
				soundWalkTip.release();
				soundWalkTip = null;
			}
		});
	}

	// 第四步 进入倒计时
	/**
	 * 倒计时
	 */
	private void startCountdownTip() {
		if (soundWalkTip != null) {
			soundWalkTip.release();
			soundWalkTip = null;
		}
		ll_squat_tip.setVisibility(View.GONE);
		ll_squat_countdown.setVisibility(View.VISIBLE);
		soundWalkCountdown1 = MediaPlayer.create(mContext,
				R.raw.walk_countdown1);
		try {
			soundWalkCountdown1.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundWalkCountdown1.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundWalkCountdown1) {
				soundWalkCountdown1.release();
				soundWalkCountdown1 = null;
				mHandler.sendEmptyMessage(MSG_COUNTDOWN_START);
			}
		});
	}

	/**
	 * 添加走一走信息
	 */
	private void addWalkInfo(WalkInfo walkInfo, int leftStepsFrc,
			int rightStepsFrc) {
		Log.i(TAG,
				"准备上传" + walkInfo.getLeftPaceTime() + "#"
						+ walkInfo.getLeftStrideTime() + "#"
						+ walkInfo.getLeftSinglePhaseTime() + "#"
						+ walkInfo.getLeftDoublePhaseTime() + "#"
						+ walkInfo.getLeftStandPhaseTime() + "#"
						+ walkInfo.getLeftSwingPhaseTime() + "###"
						+ walkInfo.getRightPaceTime() + "#"
						+ walkInfo.getRightStrideTime() + "#"
						+ walkInfo.getRightSinglePhaseTime() + "#"
						+ walkInfo.getRightDoublePhaseTime() + "#"
						+ walkInfo.getRightStandPhaseTime() + "#"
						+ walkInfo.getRightSwingPhaseTime() + "#");
		RequestParams params = new RequestParams();
		float leftStandPhasePercent = walkInfo.getLeftStandPhaseTime()
				/ walkInfo.getLeftStrideTime();
		float rightStandPhasePercent = walkInfo.getRightStandPhaseTime()
				/ walkInfo.getRightStrideTime();
		float diffValue = Math.abs(leftStandPhasePercent
				- rightStandPhasePercent); // 双足对称性
		float avgStandPhasePercent = (leftStandPhasePercent + rightStandPhasePercent) / 2;// 步态情况

		if (diffValue <= 0.03) {
			params.put("symmetry", "正常");
		} else if (diffValue <= 0.05) {
			params.put("symmetry", "一般");
		} else {
			params.put("symmetry", "异常");
		}

		if (avgStandPhasePercent < 0.58) {
			params.put("gait", "优秀");
		} else if (avgStandPhasePercent < 0.7) {
			params.put("gait", "正常");
		} else {
			params.put("gait", "弱");
		}

		params.put("calltype", ClientConstant.ADDWALKINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("l_pacetime", walkInfo.getLeftPaceTime() / 1000 + "");
		params.put("l_stridetime", walkInfo.getLeftStrideTime() / 1000 + "");
		params.put("l_stridefqc", leftStepsFrc + "");
		params.put("l_unisptphase", walkInfo.getLeftSinglePhaseTime() / 1000
				+ "");
		params.put("l_bisptphase", walkInfo.getLeftDoublePhaseTime() / 1000
				+ "");
		params.put("l_stdphase", walkInfo.getLeftStandPhaseTime() / 1000 + "");
		params.put("l_swingphase", walkInfo.getLeftSwingPhaseTime() / 1000 + "");

		params.put("r_pacetime", walkInfo.getRightPaceTime() / 1000 + "");
		params.put("r_stridetime", walkInfo.getRightStrideTime() / 1000 + "");
		params.put("r_stridefqc", rightStepsFrc + "");
		params.put("r_unisptphase", walkInfo.getRightSinglePhaseTime() / 1000
				+ "");
		params.put("r_bisptphase", walkInfo.getRightDoublePhaseTime() / 1000
				+ "");
		params.put("r_stdphase", walkInfo.getRightStandPhaseTime() / 1000 + "");
		params.put("r_swingphase", walkInfo.getRightSwingPhaseTime() / 1000
				+ "");

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_WALK_INFO, params,
				ClientConstant.WALK_URL);
	}

	/**
	 * 当获取不到数据时做的处理修改为（正常/优秀/0/0/0/0/0/0...）
	 */
	public void addWalkInfo(){
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.ADDWALKINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("symmetry", "正常");
		params.put("gait", "优秀");

		params.put("l_pacetime", 0 + "");
		params.put("l_stridetime", 0 + "");
		params.put("l_stridefqc", 0 + "");
		params.put("l_unisptphase", 0 + "");
		params.put("l_bisptphase", 0 + "");
		params.put("l_stdphase", 0 + "");
		params.put("l_swingphase", 0 + "");

		params.put("r_pacetime", 0 + "");
		params.put("r_stridetime", 0 + "");
		params.put("r_stridefqc", 0 + "");
		params.put("r_unisptphase", 0 + "");
		params.put("r_bisptphase", 0 + "");
		params.put("r_stdphase", 0 + "");
		params.put("r_swingphase", 0 + "");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_WALK_INFO, params,
				ClientConstant.WALK_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_ADD_WALK_INFO:
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
					Log.i(TAG, "MSG_ADD_WALK_INFO解析失败");
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
			soundWalkTip.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundWalkCountdown1.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundCountdown2.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundTestStart.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundSquatStartSquat.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundSquatStartStand.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			soundTestStop.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		soundWalkTip = null;
		soundWalkCountdown1 = null;
		soundCountdown2 = null;
		soundTestStart = null;
		soundSquatStartSquat = null;
		soundSquatStartStand = null;
		soundTestStop = null;
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
				judge_success = true; // 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次。
				touch_flag = false;
				successflag = true; // 第一次成功连接标志，用来触发语音
				preCalFlag = false; // 用来进行统计压力图形倒计时的
				try {
					soundWalkTip.release();
					soundWalkCountdown1.release();
					soundCountdown2.release();
					soundTestStart.release();
					soundSquatStartSquat.release();
					soundSquatStartStand.release();
					soundTestStop.release();
				} catch (Exception e) {
					// TODO: handle exception
				}
				soundWalkTip = null;
				soundWalkCountdown1 = null;
				soundCountdown2 = null;
				soundTestStart = null;
				soundSquatStartSquat = null;
				soundSquatStartStand = null;
				soundTestStop = null;
				initView();
				openBle();
			}
		} else if (requestCode == REQ_WALK_TEACHING) {
			if (resultCode == RESULT_OK) {
				getSquatTip();
			}
		}
	}
}
