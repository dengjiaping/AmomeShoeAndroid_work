package cn.com.amome.amomeshoes.view.main.health.detection.squat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.PointInfo;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetBatteryInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetPressCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadPressDataCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesStopPressDataCallback;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.DetectionConvertPress;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.main.bind.PrepareScanActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.walk.WalkReportActivity;
import cn.com.amome.amomeshoes.widget.CircleView;
import cn.com.amome.amomeshoes.widget.GifView;
import cn.com.amome.amomeshoes.widget.MyView;
import cn.com.amome.amomeshoes.widget.CircleView.RecInfo;
import cn.com.amome.shoeservice.BleService;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SquatDetectionActivity extends Activity implements OnClickListener {
	private String TAG = "SquatDetectionActivity";
	private Context mContext;
	private LinearLayout ll_scan_shoes, ll_stand, ll_squat_tip,
			ll_squat_countdown, ll_foot_print;
	private TextView tv_title, tv_scanshoes_tip, tv_stand_tip;
	private ImageView iv_scan_shoes, iv_stand_posture, iv_squat_countdown;
	private GifView iv_squat_posture;
	private ObjectAnimator retateAnim;
	private static final int MSG_START_TEST = 0;
	/** 准备阶段播放321 */
	private static final int MSG_COUNTDOWN_START = 1;
	private static final int MSG_COUNTDOWN_TEXT = 2;
	private static final int MSG_PRESS_SUC = 3;
	/** 播放检测开始 */
	private static final int MSG_SQUAT_SOUND_START_TEST = 4;
	/** 站立阶段播放321 */
	private static final int MSG_SQUAT_SOUND_STAND_KEEP_3S = 5;
	/** 开始下蹲 */
	private static final int MSG_SQUAT_SOUND_START_SQUAT = 6;
	/** 下蹲阶段播放321 */
	private static final int MSG_SQUAT_SOUND_SQUAT_KEEP_3S = 7;
	/** 恢复站立 */
	private static final int MSG_SQUAT_SOUND_START_STAND = 8;
	/** 恢复站立阶段播放54321 */
	private static final int MSG_SQUAT_SOUND_STAND_KEEP_5S = 9;
	/** 播放检测结束 */
	private static final int MSG_SQUAT_SOUND_STOP_TEST = 10;
	private static final int MSG_SQUAT_TEST_END = 11;
	private static final int MSG_ADD_MEAINFO = 15;
	private final int REQ_SQUAT_TEACHING = 16;
	private final int REQ_RECONNECTION = 17;
	private Timer timer;
	private TimerTask task;
	// 定义相关变量
	private int bleFlag = BleConstants.MSG_CONNECT;
	private int seconds = 3;
	private boolean jump_stand_view = false;
	private boolean jump_draw_view = false;
	/** 判断是否成功连接，一旦成功连接，变为false。目的是只判断一次 */
	private boolean connect_success = true;
	private boolean touch_flag = false;
	/** 第一次成功连接标志，用来第一次画图的 */
	private boolean successflag = true;
	/** 用来进行统计正式测试压力期间的数据 */
	private boolean preCalFlag = false;
	/** 用来判断页面是否关闭 */
	private boolean alreadlyDestory = false;

	private MediaPlayer soundSquatTip, soundCountdown1, soundCountdown2,
			soundCountdown54, soundTestStart, soundSquatStartSquat,
			soundSquatStartStand, soundTestStop;
	// 画图
	private MyView canves_left;// 左边画布
	private MyView canves_right;// 右边画布
	private int DEV_ROW = 15, DEV_LINE = 8;
	private short[] leftPress = new short[64];
	private short[] rightPress = new short[64];
	private short[] leftPressAll = new short[120];
	private short[] rightPressAll = new short[120];
	private short g_Dot = 30;
	private int m_fGridLong = 61;
	private int jg = MyView.jg;
	private float heightScale = 1.5f; // 比例 最好做成常量
	private List<PointInfo> leftPointList = new ArrayList<PointInfo>();
	private List<PointInfo> rightPointList = new ArrayList<PointInfo>();
	double RDr;// 直径
	float lineX, lineY;
	List<RecInfo> mCircleListLeft = new ArrayList<RecInfo>();// 左边的点坐标数据
	List<RecInfo> mCircleListRight = new ArrayList<RecInfo>();// 右边的点坐标数据
	private boolean isPermitDraw = true; // 是否允许画图，检测时间结束后为false
	// 面积计算和截图
	private short[] leftTotalPress = new short[64];
	private short[] rightTotalPress = new short[64];

	@SuppressLint("HandlerLeak")
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
				if (connect_success) {
					Log.i(TAG, "切换到站姿页面");
					preTestFinish();
				}
				// 画图
				if (!jump_draw_view && jump_stand_view && touch_flag) {
					// 用来触发预热期间的语音
					if (successflag) {
						mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_START_TEST);
						successflag = false;
					}
					// 开始统计正式测试期间的数据 true 为正式测试阶段
					if (preCalFlag) {
						if (msg.arg1 == 1) {
							leftPress = (short[]) msg.obj;
							for (int i = 0; i < 8; i++) {
								// Log.i(TAG, " 左脚" + leftPress[i * 8 + 0] + " "
								// + leftPress[i * 8 + 1] + " "
								// + leftPress[i * 8 + 2] + " "
								// + leftPress[i * 8 + 3] + " "
								// + leftPress[i * 8 + 4] + " "
								// + leftPress[i * 8 + 5] + " "
								// + leftPress[i * 8 + 6] + " "
								// + leftPress[i * 8 + 7] + " ");

								leftTotalPress[i * 8 + 0] = leftTotalPress[i * 8 + 0] >= leftPress[i * 8 + 0] ? leftTotalPress[i * 8 + 0]
										: leftPress[i * 8 + 0];
								leftTotalPress[i * 8 + 1] = leftTotalPress[i * 8 + 1] >= leftPress[i * 8 + 1] ? leftTotalPress[i * 8 + 1]
										: leftPress[i * 8 + 1];
								leftTotalPress[i * 8 + 2] = leftTotalPress[i * 8 + 2] >= leftPress[i * 8 + 2] ? leftTotalPress[i * 8 + 2]
										: leftPress[i * 8 + 2];
								leftTotalPress[i * 8 + 3] = leftTotalPress[i * 8 + 3] >= leftPress[i * 8 + 3] ? leftTotalPress[i * 8 + 3]
										: leftPress[i * 8 + 3];
								leftTotalPress[i * 8 + 4] = leftTotalPress[i * 8 + 4] >= leftPress[i * 8 + 4] ? leftTotalPress[i * 8 + 4]
										: leftPress[i * 8 + 4];
								leftTotalPress[i * 8 + 5] = leftTotalPress[i * 8 + 5] >= leftPress[i * 8 + 5] ? leftTotalPress[i * 8 + 5]
										: leftPress[i * 8 + 5];
								leftTotalPress[i * 8 + 6] = leftTotalPress[i * 8 + 6] >= leftPress[i * 8 + 6] ? leftTotalPress[i * 8 + 6]
										: leftPress[i * 8 + 6];
								leftTotalPress[i * 8 + 7] = leftTotalPress[i * 8 + 7] >= leftPress[i * 8 + 7] ? leftTotalPress[i * 8 + 7]
										: leftPress[i * 8 + 7];
							}
							leftPressAll = DetectionConvertPress
									.setLeftPress(leftPress);
							initData(1);

						} else if (msg.arg1 == 0) {
							rightPress = (short[]) msg.obj;
							for (int i = 0; i < 8; i++) {
								// Log.i(TAG, "右脚" + rightPress[i * 8 + 0] + " "
								// + rightPress[i * 8 + 1] + " "
								// + rightPress[i * 8 + 2] + " "
								// + rightPress[i * 8 + 3] + " "
								// + rightPress[i * 8 + 4] + " "
								// + rightPress[i * 8 + 5] + " "
								// + rightPress[i * 8 + 6] + " "
								// + rightPress[i * 8 + 7] + " ");

								rightTotalPress[i * 8 + 0] = rightTotalPress[i * 8 + 0] >= rightPress[i * 8 + 0] ? rightTotalPress[i * 8 + 0]
										: rightPress[i * 8 + 0];
								rightTotalPress[i * 8 + 1] = rightTotalPress[i * 8 + 1] >= rightPress[i * 8 + 1] ? rightTotalPress[i * 8 + 1]
										: rightPress[i * 8 + 1];
								rightTotalPress[i * 8 + 2] = rightTotalPress[i * 8 + 2] >= rightPress[i * 8 + 2] ? rightTotalPress[i * 8 + 2]
										: rightPress[i * 8 + 2];
								rightTotalPress[i * 8 + 3] = rightTotalPress[i * 8 + 3] >= rightPress[i * 8 + 3] ? rightTotalPress[i * 8 + 3]
										: rightPress[i * 8 + 3];
								rightTotalPress[i * 8 + 4] = rightTotalPress[i * 8 + 4] >= rightPress[i * 8 + 4] ? rightTotalPress[i * 8 + 4]
										: rightPress[i * 8 + 4];
								rightTotalPress[i * 8 + 5] = rightTotalPress[i * 8 + 5] >= rightPress[i * 8 + 5] ? rightTotalPress[i * 8 + 5]
										: rightPress[i * 8 + 5];
								rightTotalPress[i * 8 + 6] = rightTotalPress[i * 8 + 6] >= rightPress[i * 8 + 6] ? rightTotalPress[i * 8 + 6]
										: rightPress[i * 8 + 6];
								rightTotalPress[i * 8 + 7] = rightTotalPress[i * 8 + 7] >= rightPress[i * 8 + 7] ? rightTotalPress[i * 8 + 7]
										: rightPress[i * 8 + 7];
							}
							rightPressAll = DetectionConvertPress
									.setRightPress(rightPress);
							initData(0);
						}

					} else {
						if (msg.arg1 == 1) {
							leftPress = (short[]) msg.obj;
							leftPressAll = DetectionConvertPress
									.setLeftPress(leftPress);
							initData(1);
						} else if (msg.arg1 == 0) {
							rightPress = (short[]) msg.obj;
							rightPressAll = DetectionConvertPress
									.setRightPress(rightPress);
							initData(0);
						}
					}
					if (isPermitDraw) {
						if (msg.arg1 == 0) {
							// Log.i(TAG, "正在画右脚");
							initData(0);
						} else if (msg.arg1 == 1) {
							// Log.i(TAG, "正在画左脚");
							initData(1);
						}
					}
				}
				break;
			case MSG_SQUAT_SOUND_START_TEST:
				// 语音：检测开始
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
							public void onCompletion(MediaPlayer soundSquatStart) {
								soundSquatStart.release();
								soundSquatStart = null;
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_STAND_KEEP_3S);
							}
						});
				break;
			case MSG_SQUAT_SOUND_STAND_KEEP_3S:
				// 语音：321 （站立阶段）
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
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_START_SQUAT);
							}
						});
				break;
			case MSG_SQUAT_SOUND_START_SQUAT:
				// 语音：双手平举，下蹲只大腿与地面平行，下蹲
				preCalFlag = true;
				soundSquatStartSquat = MediaPlayer.create(mContext,
						R.raw.squat_start_squat);
				try {
					soundSquatStartSquat.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundSquatStartSquat
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundSquatStartSquat) {
								soundSquatStartSquat.release();
								soundSquatStartSquat = null;
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_SQUAT_KEEP_3S);
							}
						});
				break;
			case MSG_SQUAT_SOUND_SQUAT_KEEP_3S:
				// 语音：321 （下蹲阶段）
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
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_START_STAND);
							}
						});
				break;
			case MSG_SQUAT_SOUND_START_STAND:
				// 语音：请缓慢恢复到站立姿势
				soundSquatStartStand = MediaPlayer.create(mContext,
						R.raw.squat_start_stand);
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
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_STAND_KEEP_5S);
							}
						});
				break;
			case MSG_SQUAT_SOUND_STAND_KEEP_5S:
				// 语音：54321
				soundCountdown54 = MediaPlayer.create(mContext,
						R.raw.squat_countdown54);
				try {
					soundCountdown54.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				soundCountdown54
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(
									MediaPlayer soundCountdown54) {
								soundCountdown54.release();
								soundCountdown54 = null;
								mHandler.sendEmptyMessage(MSG_SQUAT_SOUND_STOP_TEST);
							}
						});
				break;
			case MSG_SQUAT_SOUND_STOP_TEST:
				// 语音：检测结束
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
								mHandler.sendEmptyMessage(MSG_SQUAT_TEST_END);
							}
						});
				break;
			case MSG_SQUAT_TEST_END:
				preCalFlag = false; // 停止统计压力数据
				isPermitDraw = false;
				// 足弓判断
				int left_instep_code = CalDetection
						.calLeftInstep(leftTotalPress);
				int right_instep_code = CalDetection
						.calRightInstep(rightTotalPress);
				String instep = CalDetection.calDoubleInstep(left_instep_code,
						right_instep_code);
				// 前后旋判断
				String LeftSpin = CalDetection.calLeftSpin(leftTotalPress);
				String RightSpin = CalDetection.calRightSpin(rightTotalPress);
				String spin = CalDetection.calDoubleSpin(LeftSpin, RightSpin);
				// 左右脚压力值
				String leftPressStr = changePress(leftTotalPress);
				String rightPressStr = changePress(rightTotalPress);
				// 添加蹲一蹲信息
				Log.i(TAG, "准备添加蹲一蹲信息");
				addSquatInfo(instep, spin, leftPressStr, rightPressStr);
				break;
			case MSG_ADD_MEAINFO:
				String re = (String) msg.obj;
				if (re.equals("0x00")) {
					Log.i(TAG, "添加蹲一蹲信息成功");
					try {
						Log.i(TAG, "停止获取压力数据");
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
		setContentView(R.layout.activity_squat_detection);
		initData();
		initView();
		openBle();
	}

	private void initData() {
		bleFlag = BleConstants.MSG_CONNECT;
		seconds = 3;
		jump_stand_view = false;
		jump_draw_view = false;
		connect_success = true;
		touch_flag = false;
		successflag = true;
		isPermitDraw = true;
		preCalFlag = false;
		alreadlyDestory = false;
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
		canves_left = (MyView) findViewById(R.id.canves_left);
		canves_right = (MyView) findViewById(R.id.canves_right);

		tv_title.setText("蹲一蹲");
		tv_scanshoes_tip.setText("智能鞋检测中");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));

		LayoutParams lp_iv_squat_posture = (LayoutParams) iv_squat_posture
				.getLayoutParams();
		lp_iv_squat_posture.width = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
		lp_iv_squat_posture.height = lp_iv_squat_posture.width;
		iv_squat_posture.setLayoutParams(lp_iv_squat_posture);

		LayoutParams lp_canves_left = (LayoutParams) canves_left
				.getLayoutParams();
		lp_canves_left.width = (int) (SpfUtil.readScreenWidthPx(mContext) * 2 / 5);
		lp_canves_left.height = (int) (lp_canves_left.width * 15 * heightScale / 8);
		canves_left.setLayoutParams(lp_canves_left);
		canves_right.setLayoutParams(lp_canves_left);
		m_fGridLong = lp_canves_left.width / 8;

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
					SquatTeachingActivity.class), REQ_SQUAT_TEACHING);
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
			Log.i(TAG, "停止获取压力数据");
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
		public void isGetBatterySucc(boolean arg0, int leftVal, int rightVal) {
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
				if (arg0) {
					if (!alreadlyDestory) {
						Log.i(TAG, "停止压力数据成功，准备跳转");
						startActivity(new Intent(mContext,
								SquatReportActivity.class));
						finish();
					} else {
						Log.i(TAG, "页面退出，停止压力数据成功");
					}
				} else {
					if (!alreadlyDestory) {
						Log.i(TAG, "停止压力数据失败，准备跳转");
						startActivity(new Intent(mContext,
								SquatReportActivity.class));
						finish();
					}
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
		connect_success = false;
	}

	// 第三步
	/**
	 * 进入注意事项
	 */
	private void getSquatTip() {
		ll_stand.setVisibility(View.GONE);
		ll_squat_tip.setVisibility(View.VISIBLE);
		soundSquatTip = MediaPlayer.create(mContext, R.raw.squat_tip);
		try {
			soundSquatTip.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundSquatTip.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundSquatTip) {
				soundSquatTip.release();
				soundSquatTip = null;
			}
		});
	}

	// 第四步 进入倒计时
	/**
	 * 倒计时
	 */
	private void startCountdownTip() {
		if (soundSquatTip != null) {
			soundSquatTip.release();
			soundSquatTip = null;
		}
		ll_squat_tip.setVisibility(View.GONE);
		ll_squat_countdown.setVisibility(View.VISIBLE);
		// 检测开始
		soundCountdown1 = MediaPlayer.create(mContext, R.raw.squat_countdown1);
		try {
			soundCountdown1.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		soundCountdown1.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer soundCountdown1) {
				soundCountdown1.release();
				soundCountdown1 = null;
				mHandler.sendEmptyMessage(MSG_COUNTDOWN_START);
			}
		});
	}

	// 第五步 画图
	private void initData(int flag) {
		switch (flag) {
		case 0:
			rightPointList.clear();
			for (int i = 0; i < 111; i++) {
				if (rightPressAll[i] > g_Dot || rightPressAll[i + 1] > g_Dot
						|| rightPressAll[i + DEV_LINE] > g_Dot
						|| rightPressAll[i + DEV_LINE + 1] > g_Dot) {
					InterPolationData(rightPressAll[i], rightPressAll[i
							+ DEV_LINE], rightPressAll[i + 1], rightPressAll[i
							+ DEV_LINE + 1], i % DEV_LINE, i / DEV_LINE, 0, 1);
				} else {
					InterPolationData(rightPressAll[i], rightPressAll[i
							+ DEV_LINE], rightPressAll[i + 1], rightPressAll[i
							+ DEV_LINE + 1], i % DEV_LINE, i / DEV_LINE, 1, 0);
				}
			}
			canves_right.rightPointList.clear();
			canves_right.rightPointList.addAll(rightPointList);
			break;
		case 1:
			leftPointList.clear();
			for (int i = 0; i < 111; i++) {
				if (leftPressAll[i] > g_Dot || leftPressAll[i + 1] > g_Dot
						|| leftPressAll[i + DEV_LINE] > g_Dot
						|| leftPressAll[i + DEV_LINE + 1] > g_Dot) {
					InterPolationData(leftPressAll[i], leftPressAll[i
							+ DEV_LINE], leftPressAll[i + 1], leftPressAll[i
							+ DEV_LINE + 1], i % DEV_LINE, i / DEV_LINE, 1, 1);
				} else {
					InterPolationData(leftPressAll[i], leftPressAll[i
							+ DEV_LINE], leftPressAll[i + 1], leftPressAll[i
							+ DEV_LINE + 1], i % DEV_LINE, i / DEV_LINE, 1, 0);
				}
			}
			canves_left.leftPointList.clear();
			canves_left.leftPointList.addAll(leftPointList);
			break;
		default:
			break;
		}

	}

	private void InterPolationData(float leftup, float rightup, float leftdown,
			float rightdown, int nRow, int nLine, int flag, int value) {
		float data;
		switch (flag) {
		case 0:
			switch (value) {
			case 1:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * heightScale; j += jg) {
						data = (m_fGridLong * heightScale - j)
								* (m_fGridLong - i) * leftup
								+ (m_fGridLong - i) * j * rightup
								+ (m_fGridLong * heightScale - j) * i
								* leftdown + i * j * rightdown;
						data = (float) (data * 1.0 / (m_fGridLong * m_fGridLong * heightScale));
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * heightScale + j
								+ m_fGridLong * heightScale / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						rightPointList.add(pointInfo);
					}
				}
				break;
			case 0:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * heightScale; j += jg) {
						data = 0;
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * heightScale + j
								+ m_fGridLong * heightScale / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						rightPointList.add(pointInfo);
					}
				}
				break;
			default:
				break;
			}

			break;
		case 1:
			switch (value) {
			case 1:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * heightScale; j += jg) {
						data = (m_fGridLong * heightScale - j)
								* (m_fGridLong - i) * leftup
								+ (m_fGridLong - i) * j * rightup
								+ (m_fGridLong * heightScale - j) * i
								* leftdown + i * j * rightdown;
						data = (float) (data * 1.0 / (m_fGridLong * m_fGridLong * heightScale));
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * heightScale + j
								+ m_fGridLong * heightScale / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						leftPointList.add(pointInfo);
					}
				}
				break;
			case 0:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * heightScale; j += jg) {
						data = 0;
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * heightScale + j
								+ m_fGridLong * heightScale / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						leftPointList.add(pointInfo);
					}
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 添加蹲一蹲信息
	 */
	private void addSquatInfo(String spin, String instep, String leftPressStr,
			String rightPressStr) {
		Log.i(TAG, "添加蹲一蹲");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.ADDMEAINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("spin", spin);
		params.put("turn", instep);
		params.put("ldot64", leftPressStr);
		params.put("rdot64", rightPressStr);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_MEAINFO, params,
				ClientConstant.MEASURE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_ADD_MEAINFO:
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
					Log.i(TAG, "MSG_ADD_MEAINFO解析失败");
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
			soundSquatTip.release();
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
		try {
			soundCountdown54.release();
		} catch (Exception e) {
			// TODO: handle exception
		}
		soundSquatTip = null;
		soundCountdown1 = null;
		soundCountdown2 = null;
		soundCountdown54 = null;
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
				initData();
				try {
					soundSquatTip.release();
					soundCountdown1.release();
					soundCountdown2.release();
					soundCountdown54.release();
					soundTestStart.release();
					soundSquatStartSquat.release();
					soundSquatStartStand.release();
					soundTestStop.release();
				} catch (Exception e) {
					// TODO: handle exception
				}
				soundSquatTip = null;
				soundCountdown1 = null;
				soundCountdown2 = null;
				soundCountdown54 = null;
				soundTestStart = null;
				soundSquatStartSquat = null;
				soundSquatStartStand = null;
				soundTestStop = null;
				initView();
				openBle();
			}
		} else if (requestCode == REQ_SQUAT_TEACHING) {
			if (resultCode == RESULT_OK) {
				getSquatTip();
			}
		}
	}

	private String changePress(short[] mPress) {
		String str = "";
		for (int i = 0; i < mPress.length; i++) {
			str = str + "," + mPress[i];
		}
		str = str.substring(1, str.length());
		return str;
	}

	public static void main(String[] args) {
		BleService.mSleep(500);
	}
}
