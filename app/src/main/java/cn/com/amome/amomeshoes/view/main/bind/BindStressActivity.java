package cn.com.amome.amomeshoes.view.main.bind;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.model.PointInfo;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.CalculationCoordinate;
import cn.com.amome.amomeshoes.util.DetectionConvertPress;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetPressCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadPressDataCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.amomeshoes.widget.CircleView;
import cn.com.amome.amomeshoes.widget.MyView;
import cn.com.amome.amomeshoes.widget.CircleView.RecInfo;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BindStressActivity extends Activity implements OnClickListener {
	private String TAG = "BindStressActivity";
	private Context mContext;
	private LinearLayout ll_shoepic, ll_stress_start, ll_foot_print;
	private RelativeLayout rl_stress_fail, rl_stress_suc;
	private TextView tv_title, tv_step_one, tv_step_two, tv_step_three,
			tv_test_tip;
	private ImageView iv_left, iv_step_one, iv_step_two, iv_step_three;
	private MyView canves_left;// 左边画布
	private MyView canves_right;// 右边画布
	private short[] leftPress = new short[64];
	private short[] rightPress = new short[64];
	private short[] leftPressAll = new short[120];
	private short[] rightPressAll = new short[120];
	private short g_Dot = 30;
	private int m_fGridLong = 61;
	private int jg = 8;
	private float heightScale = 1.5f; // 比例
	private List<PointInfo> leftPointList = new ArrayList<PointInfo>();
	private List<PointInfo> rightPointList = new ArrayList<PointInfo>();
	private int DEV_ROW = 15, DEV_LINE = 8;
	List<RecInfo> mCircleListLeft = new ArrayList<RecInfo>();// 左边的点坐标数据
	List<RecInfo> mCircleListRight = new ArrayList<RecInfo>();// 右边的点坐标数据
	private boolean leftTest = true, rightTest = true;
	private int bleFlag = BleConstants.MSG_CONNECT;
	private int seconds = -1;
	private Timer timer;
	private TimerTask task;
	private boolean leftflag = true, rightflag = true; // 用来标志只画一次
	private boolean successflag = true; // 第一次成功连接标志，用来第一次画图的
	private boolean disBleCon = false; // 判断是否退出页面断开连接
	private String bindError = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (successflag) {
					mHandler.sendEmptyMessage(11);
					successflag = false;
				}
				// 如果有数据才展示
				if (leftflag && msg.arg1 == 1) {

					leftflag = false;
				}
				if (rightflag && msg.arg1 == 0) {

					rightflag = false;
				}
				DialogUtil.hideProgressDialog();

				if (msg.arg1 == 0) {
					Log.i(TAG, "画右脚");
					rightPress = (short[]) msg.obj;
					if (rightTest) {
						for (int i = 0; i < rightPress.length; i++) {
							if (rightPress[i] != 0) {
								bindError = bindError.replace("右脚压力失败,", "");
								Log.i(TAG, "bindError=" + bindError);
								rightTest = false;
								return;
							}
						}
					}
					rightPressAll = DetectionConvertPress
							.setRightPress(rightPress);
					initData(0);
				} else if (msg.arg1 == 1) {
					Log.i(TAG, "画左脚");
					leftPress = (short[]) msg.obj;
					if (leftTest) {
						for (int i = 0; i < leftPress.length; i++) {
							if (leftPress[i] != 0) {
								bindError = bindError.replace("左脚压力失败,", "");
								Log.i(TAG, "bindError=" + bindError);
								leftTest = false;
								return;
							}
						}
					}
					leftPressAll = DetectionConvertPress
							.setLeftPress(leftPress);
					initData(1);
				}
				break;
			case 11:
				seconds = 11;
				timer = new Timer();
				task = new TimerTask() {
					@Override
					public void run() {
						seconds--;
						mHandler.sendEmptyMessage(12);
					}
				};
				if (seconds > 0) {
					timer = new Timer();
					timer.schedule(task, 0, 1000);
				}
				break;
			case 12:
				if (seconds <= 0) {
					seconds = -1;
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
					tv_test_tip.setText("测试完成，2秒后自动跳转");
					if (bindError.contains("压力")) {
						ll_foot_print.setVisibility(View.GONE);
						rl_stress_fail.setVisibility(View.VISIBLE);
					} else {
						ll_foot_print.setVisibility(View.GONE);
						rl_stress_suc.setVisibility(View.VISIBLE);
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								Intent intent = new Intent(mContext,
										BindWalkActivity.class);
								intent.putExtra("bindError", bindError);
								startActivity(intent);
								finish();
							}
						};
						Timer timer = new Timer();
						timer.schedule(task, 2000);
					}
				} else {
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_stress);
		mContext = this;
		Intent intent = getIntent();
		bindError = intent.getStringExtra("bindError");
		bindError = bindError + "左脚压力失败,右脚压力失败,";
		initView();

	}

	private void preConnect() {
		DialogUtil.showCancelProgressDialog(mContext, "", "加载中", true, true);
		try {
			if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
				Log.i(TAG, "智能鞋状态为已连接");
				AmomeApp.bleShoes.getShoesPressData(shoesGetPressCallback);
			} else {
				Log.i(TAG, "智能鞋状态为未连接");
				bleFlag = BleConstants.MSG_GETPRESS;
				connectShoes();
			}
		} catch (Exception e) {
			// TODO: handle exception
			DialogUtil.hideProgressDialog();
		}
	}

	private void connectShoes() {
		if (AmomeApp.bleShoes == null) {
			AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTING;
			AmomeApp.bleShoes = new BleShoes(
					SpfUtil.readDeviceToShoeLeftTmp(mContext),
					SpfUtil.readDeviceToShoeRightTmp(mContext),
					shoesCreCallback, mContext);
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
				default:
					break;
				}
			} else {
				Log.i(TAG, "智能鞋连接出错");
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
			}
		}

		@Override
		public void isConnect(boolean arg0, String addr) {
			// TODO Auto-generated method stub

		}

		@Override
		public void isRec(boolean arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	BleShoes.shoesGetPressCallback shoesGetPressCallback = new shoesGetPressCallback() {

		@Override
		public void isGetPressSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋压力数据获取成功");
				ll_stress_start.setVisibility(View.GONE);
				ll_foot_print.setVisibility(View.VISIBLE);
				AmomeApp.bleShoes
						.readShoesPressData(shoesReadPressDataCallback);
			} else {
				Log.i(TAG, "智能鞋压力数据获取失败");
				// 出现此情况断开就完事了？？
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
			}
		}
	};

	BleShoes.shoesReadPressDataCallback shoesReadPressDataCallback = new shoesReadPressDataCallback() {

		@Override
		public void readPressData(String addr, short[] PressData, long time) {
			// TODO Auto-generated method stub
			// 在这里处理压力数据
			Log.i(TAG, "addr+" + addr);
			Message msg = Message.obtain();
			int flag = Integer.parseInt(addr.substring(16));
			if (flag == 0) {
				msg.arg1 = 0;
			} else if (flag == 1) {
				msg.arg1 = 1;
			}
			msg.obj = PressData;
			msg.what = 0;
			mHandler.sendMessage(msg);
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
				AmomeApp.exercise_flag = false;
				// 需要增加处理
			} else {
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				// 需要增加处理
			}
		}
	};

	private void initView() {
		// TODO Auto-generated method stub
		ll_shoepic = (LinearLayout) findViewById(R.id.ll_shoepic);
		ll_stress_start = (LinearLayout) findViewById(R.id.ll_stress_start);
		ll_foot_print = (LinearLayout) findViewById(R.id.ll_foot_print);
		rl_stress_fail = (RelativeLayout) findViewById(R.id.rl_stress_fail);
		rl_stress_suc = (RelativeLayout) findViewById(R.id.rl_stress_suc);
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_step_one = (ImageView) findViewById(R.id.iv_step_one);
		iv_step_two = (ImageView) findViewById(R.id.iv_step_two);
		iv_step_three = (ImageView) findViewById(R.id.iv_step_three);
		tv_step_one = (TextView) findViewById(R.id.tv_step_one);
		tv_step_two = (TextView) findViewById(R.id.tv_step_two);
		tv_step_three = (TextView) findViewById(R.id.tv_step_three);
		tv_test_tip = (TextView) findViewById(R.id.tv_test_tip);
		tv_title.setText("压力测试");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		iv_step_one.setImageResource(R.drawable.bind_step_complete);
		iv_step_two.setImageResource(R.drawable.bind_step_complete);
		iv_step_three.setImageResource(R.drawable.bind_step_now);
		tv_step_one
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_step_two
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_step_three.setTextColor(mContext.getResources().getColor(
				R.color.blue));
		canves_left = (MyView) findViewById(R.id.canves_left);
		canves_right = (MyView) findViewById(R.id.canves_right);
		LayoutParams lp_canves_left = (LayoutParams) canves_left
				.getLayoutParams();
		lp_canves_left.width = (int) (SpfUtil.readScreenWidthPx(mContext) * 2 / 5);
		lp_canves_left.height = (int) (lp_canves_left.width * 15 * heightScale / 8);
		canves_left.setLayoutParams(lp_canves_left);
		canves_right.setLayoutParams(lp_canves_left);
		m_fGridLong = lp_canves_left.width / 8;
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_stress_start).setOnClickListener(this);
		findViewById(R.id.iv_stress_next_step).setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.btn_stress_start:
			if (enableBluetooth()) {
				Log.i(TAG, "点击屏幕前蓝牙就打开的情况");
				preConnect();
			} else {
				Log.i(TAG, "未开启蓝牙");
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						Log.i(TAG, "再次判断是否开启蓝牙");
						if (enableBluetooth()) {
							Log.i(TAG, "刚打开蓝牙的情况");
							preConnect();
						} else {
							// T.showToast(mContext, "蓝牙连接错误，请退出程序重新进入", 0);
							// 存在问题屏蔽
						}
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, 5000);
			}
			break;
		case R.id.iv_stress_next_step:
			Intent intent = new Intent(mContext, BindWalkActivity.class);
			intent.putExtra("bindError", bindError);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		if (timer != null) {
			timer.cancel();
		}
		if (task != null) {
			task.cancel();
		}
		timer = null;
		task = null;
		if (disBleCon) {
			try {
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
			} catch (Exception e) {
				// TODO: handle exception
				Log.i(TAG, "断开连接时崩了，大概是还没建立连接");
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");

	}

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

	private void InterPolationDataLine(float leftup, float rightup,
			float leftdown, float rightdown, int nRow, int nLine, float bc,
			int flag, int value) {
		float data;
		switch (flag) {
		case 0:
			switch (value) {
			case 1:
				for (int i = 0; i < m_fGridLong + bc; i += jg) {
					for (int j = 0; j < m_fGridLong * 1.2f; j += jg) {
						data = (m_fGridLong * 1.2f - j)
								* (m_fGridLong + bc - i) * leftup
								+ (m_fGridLong + bc - i) * j * rightup
								+ (m_fGridLong * 1.2f - j) * i * leftdown + i
								* j * rightdown;
						data = (float) (data * 1.0 / ((m_fGridLong + bc)
								* m_fGridLong * 1.2f));
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * 1.2f + j
								+ m_fGridLong / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						rightPointList.add(pointInfo);
					}
				}
				break;
			case 0:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * 1.2f; j += jg) {
						data = 0;
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * 1.2f + j
								+ m_fGridLong / 2);
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
				for (int i = 0; i < m_fGridLong + bc; i += jg) {
					for (int j = 0; j < m_fGridLong * 1.2f; j += jg) {
						data = (m_fGridLong * 1.2f - j)
								* (m_fGridLong + bc - i) * leftup
								+ (m_fGridLong + bc - i) * j * leftup
								+ (m_fGridLong * 1.2f - j) * i * leftdown + i
								* j * leftdown;
						data = (float) (data * 1.0 / ((m_fGridLong + bc)
								* m_fGridLong * 1.2f));
						PointInfo pointInfo = new PointInfo();
						pointInfo.setX(nRow * m_fGridLong - bc + i
								+ m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * 1.2f + j
								+ m_fGridLong / 2);
						pointInfo.setColor(CircleView
								.getCircleColor((short) data));
						leftPointList.add(pointInfo);
					}
				}
				break;
			case 0:
				for (int i = 0; i < m_fGridLong; i += jg) {
					for (int j = 0; j < m_fGridLong * 1.2f; j += jg) {
						data = 0;
						PointInfo pointInfo = new PointInfo();
						pointInfo
								.setX(nRow * m_fGridLong + i + m_fGridLong / 2);
						pointInfo.setY(nLine * m_fGridLong * 1.2f + j
								+ m_fGridLong / 2);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		DialogUtil.showAlertDialog(mContext, "", "正在绑定中，是否退出？", "确定", "取消",
				new OnAlertViewClickListener() {
					@Override
					public void confirm() {
						disBleCon = true;
						finish();
					}

					@Override
					public void cancel() {
					}
				});
	}
}
