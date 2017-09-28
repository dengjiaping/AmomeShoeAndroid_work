package cn.com.amome.amomeshoes.view.main.bind;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.color;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadDailyDataCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesStopPressDataCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.main.my.setting.UserHelpActivity;
import cn.com.amome.amomeshoes.widget.GifView;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BindWalkActivity extends Activity implements OnClickListener {
	private String TAG = "BindWalkActivity";
	private Context mContext;
	private LinearLayout ll_bind_walk, ll_bind_finish, ll_bind_fail_shock,
			ll_bind_fail_stress, ll_bind_fail_walk;
	private LinearLayout ll_bind_fail;
	private TextView tv_title, tv_step_one, tv_step_two, tv_step_three,
			tv_step_four;
	private ImageView iv_left, iv_step_one, iv_step_two, iv_step_three,
			iv_step_four, iv_walk_progress, iv_walk_tip_background;
	private GifView gv_walk_tip;
	private Button btn_walk_start;
	private int bleFlag = BleConstants.MSG_CONNECT;
	private Timer timer;
	private TimerTask task;
	private static final int MSG_START_BIND = 2;
	private static final int MSG_BIND_BLEDEV = 1;
	private static final int MSG_BIND_ALREADY = 3;
	private static final int MSG_BIND_UPLOAD_ERROR = 4;
	private static final int MSG_DATA_COMPUTER = 5;
	private boolean leftSucFlag = true, rightSucFlag = true;
	private DailyData leftData, rightData;
	private int leftFirstSteps = -1, rightFirstSteps = -1, leftSteps = -1,
			rightSteps = -1;
	private boolean finishFlag = false;// 判断所走时间是否大于10
	private String bindError = "";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_BIND_BLEDEV:
					String re2 = (String) msg.obj;
					if (re2.equals("0x00")) {
						Log.i(TAG, "绑定成功");
						T.showToast(mContext, "绑定成功", 0);
						SpfUtil.keepDeviceToShoeLeft(mContext,
								SpfUtil.readDeviceToShoeLeftTmp(mContext));
						SpfUtil.keepDeviceToShoeRight(mContext,
								SpfUtil.readDeviceToShoeRightTmp(mContext));
						SpfUtil.keepLeftSitTime(mContext, "");
						SpfUtil.keepLeftStandTime(mContext, "");
						SpfUtil.keepLeftWalkTime(mContext, "");
						SpfUtil.keepLeftSteps(mContext, "");
						SpfUtil.keepRightSitTime(mContext, "");
						SpfUtil.keepRightStandTime(mContext, "");
						SpfUtil.keepRightWalkTime(mContext, "");
						SpfUtil.keepRightSteps(mContext, "");
						SpfUtil.keepTodaySitTime(mContext, "");
						SpfUtil.keepTodayStandTime(mContext, "");
						SpfUtil.keepTodayWalkTime(mContext, "");
						SpfUtil.keepTodaySteps(mContext, "");
						SpfUtil.keepTime(mContext, "");
						ll_bind_walk.setVisibility(View.GONE);
						ll_bind_finish.setVisibility(View.VISIBLE);
					}
					break;
				case MSG_BIND_UPLOAD_ERROR:
					String re = (String) msg.obj;
					if (re.equals("0x00")) {
						Log.i(TAG, "上传绑定错误成功 " + bindError);
						T.showToast(mContext, "上传绑定错误成功", 0);
						ll_bind_walk.setVisibility(View.GONE);
						if (bindError.contains("震动")) {
							ll_bind_fail_shock.setVisibility(View.VISIBLE);
						}
						if (bindError.contains("压力")) {
							ll_bind_fail_stress.setVisibility(View.VISIBLE);
						}
						if (bindError.contains("计步")) {
							ll_bind_fail_walk.setVisibility(View.VISIBLE);
						}
						ll_bind_fail.setVisibility(View.VISIBLE);
					}
					break;
				}
				break;
			case MSG_START_BIND:
				addBindDevice();
				break;
			case MSG_BIND_ALREADY:
				T.showToast(mContext, "智能鞋已被绑定", 0);
				Log.i(TAG, "智能鞋已被绑定");
				finish();
				break;
			case MSG_DATA_COMPUTER:
				Log.i(TAG, "case MSG_DATA_COMPUTER");
				DialogUtil.hideProgressDialog();
				if (msg.arg1 == 0) {
					rightData = (DailyData) msg.obj;
					if (rightSucFlag) {
						rightFirstSteps = rightData.back_steps;
						rightSucFlag = false;
					}
					rightSteps = rightData.back_steps;
				} else if (msg.arg1 == 1) {
					leftData = (DailyData) msg.obj;
					if (leftSucFlag) {
						leftFirstSteps = leftData.back_steps;
						leftSucFlag = false;
					}
					leftSteps = leftData.back_steps;
				}
				if (leftFirstSteps >= 0 && rightFirstSteps >= 0) {
					if (leftSteps + rightSteps > leftFirstSteps
							+ rightFirstSteps) {
						updateWalkUI(leftSteps + rightSteps - leftFirstSteps
								- rightFirstSteps);
					} else if (leftSteps + rightSteps == leftFirstSteps
							+ rightFirstSteps) {
						updateWalkUI(leftSteps + rightSteps - leftFirstSteps
								- rightFirstSteps);
					}
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_walk);
		AmomeApp.bleShoes.stopShoesPressData(shoesStopPressDataCallback); // 停止获取压力数据
		mContext = this;
		Intent intent = getIntent();
		bindError = intent.getStringExtra("bindError");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_bind_walk = (LinearLayout) findViewById(R.id.ll_bind_walk);
		ll_bind_finish = (LinearLayout) findViewById(R.id.ll_bind_finish);
		ll_bind_fail_shock = (LinearLayout) findViewById(R.id.ll_bind_fail_shock);
		ll_bind_fail_stress = (LinearLayout) findViewById(R.id.ll_bind_fail_stress);
		ll_bind_fail_walk = (LinearLayout) findViewById(R.id.ll_bind_fail_walk);
		ll_bind_fail = (LinearLayout) findViewById(R.id.ll_bind_fail);
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_step_one = (ImageView) findViewById(R.id.iv_step_one);
		iv_step_two = (ImageView) findViewById(R.id.iv_step_two);
		iv_step_three = (ImageView) findViewById(R.id.iv_step_three);
		iv_step_four = (ImageView) findViewById(R.id.iv_step_four);
		iv_walk_progress = (ImageView) findViewById(R.id.iv_walk_progress);
		iv_walk_tip_background = (ImageView) findViewById(R.id.iv_walk_tip_background);
		tv_step_one = (TextView) findViewById(R.id.tv_step_one);
		tv_step_two = (TextView) findViewById(R.id.tv_step_two);
		tv_step_three = (TextView) findViewById(R.id.tv_step_three);
		tv_step_four = (TextView) findViewById(R.id.tv_step_four);
		gv_walk_tip = (GifView) findViewById(R.id.gv_walk_tip);
		btn_walk_start = (Button) findViewById(R.id.btn_walk_start);
		LayoutParams lp_iv_walk_progress = (LayoutParams) iv_walk_progress
				.getLayoutParams();
		lp_iv_walk_progress.width = (int) (SpfUtil.readScreenWidthPx(mContext) * 2 / 3);
		lp_iv_walk_progress.height = lp_iv_walk_progress.width;
		iv_walk_progress.setLayoutParams(lp_iv_walk_progress);

		LayoutParams lp_gv_walk_tip = (LayoutParams) gv_walk_tip
				.getLayoutParams();
		lp_gv_walk_tip.width = (int) (lp_iv_walk_progress.width / 1.7);
		lp_gv_walk_tip.height = lp_gv_walk_tip.width;
		gv_walk_tip.setLayoutParams(lp_gv_walk_tip);
		iv_walk_tip_background.setLayoutParams(lp_gv_walk_tip);

		tv_title.setText("计步测试");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		iv_step_one.setImageResource(R.drawable.bind_step_complete);
		iv_step_two.setImageResource(R.drawable.bind_step_complete);
		iv_step_three.setImageResource(R.drawable.bind_step_complete);
		iv_step_four.setImageResource(R.drawable.bind_step_now);
		tv_step_one
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_step_two
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_step_three.setTextColor(mContext.getResources().getColor(
				R.color.blue));
		tv_step_four.setTextColor(mContext.getResources()
				.getColor(R.color.blue));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_walk_start).setOnClickListener(this);
		findViewById(R.id.iv_more_help).setOnClickListener(this);
		ll_bind_finish.setOnClickListener(this);
		ll_bind_fail.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.ll_bind_finish:
			finish();
			break;
		case R.id.ll_bind_fail:
			finish();
			break;
		case R.id.iv_more_help:
			Intent intent = new Intent(mContext, UserHelpActivity.class);
			intent.putExtra("title", "用户帮助");
			startActivity(intent);
			break;
		case R.id.btn_walk_start:
			btn_walk_start.setClickable(false);
			btn_walk_start.setBackgroundColor(color.bg_gray);
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
		}
	}

	private void preConnect() {
		DialogUtil.showCancelProgressDialog(mContext, "", "加载中", true, true);
		try {
			if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
				Log.i(TAG, "智能鞋状态为已连接");
				AmomeApp.bleShoes
						.getShoesDailyDataDouble(shoesReadDailyDataCallback); // 直接获取今日蓝牙坐站走数据
			} else {
				Log.i(TAG, "智能鞋状态为未连接");
				bleFlag = BleConstants.MSG_GETDAILY;
				connectShoes();
			}
		} catch (Exception e) {
			// TODO: handle exception
			DialogUtil.hideProgressDialog();
			T.showToast(mContext, "未找到智能鞋", 0);
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
				case BleConstants.MSG_GETDAILY:
					AmomeApp.bleShoes
							.getShoesDailyDataDouble(shoesReadDailyDataCallback);
					break;
				default:
					break;
				}
			} else {
				Log.i(TAG, "智能鞋连接出错");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				// 增加处理
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

	BleShoes.shoesStopPressDataCallback shoesStopPressDataCallback = new shoesStopPressDataCallback() {

		@Override
		public void isStopPressSuc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {

			}
		}
	};

	BleShoes.shoesReadDailyDataCallback shoesReadDailyDataCallback = new shoesReadDailyDataCallback() {

		@Override
		public void readDailyData(String addr, DailyData dailyData) {
			// TODO Auto-generated method stub
			Log.i(TAG, "address:" + addr + ", 步数:" + dailyData.back_steps);
			DialogUtil.hideProgressDialog();
			Message msg = Message.obtain();
			int flag = Integer.parseInt(addr.substring(16));
			if (flag % 2 == 0) {
				msg.arg1 = 0;
			} else if (flag % 2 == 1) {
				msg.arg1 = 1;
			}
			msg.obj = dailyData;
			msg.what = MSG_DATA_COMPUTER;
			mHandler.sendMessage(msg);
		}
	};

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

	private void updateWalkUI(int steps) {
		switch (steps) {
		case 1:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_1);
			break;
		case 2:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_2);
			break;
		case 3:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_3);
			break;
		case 4:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_4);
			break;
		case 5:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_5);
			break;
		case 6:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_6);
			break;
		case 7:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_7);
			break;
		case 8:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_8);
			break;
		case 9:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_9);
			break;
		case 10:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_10);
			break;
		case 11:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_11);
			break;
		case 12:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_12);
			break;
		case 13:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_13);
			break;
		case 14:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_14);
			break;
		case 15:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_15);
			break;
		case 16:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_16);
			break;
		case 17:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_17);
			break;
		case 18:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_18);
			break;
		case 19:
			iv_walk_progress.setImageResource(R.drawable.bind_walk_19);
			break;
		default:
			break;
		}
		if (steps >= 20 && !finishFlag) {
			if (leftSteps == leftFirstSteps) {
				bindError = bindError + "左脚计步失败,";
			}
			if (rightSteps == rightFirstSteps) {
				bindError = bindError + "右脚计步失败,";
			}
			finishFlag = true; // 只执行一次判断
			iv_walk_progress.setImageResource(R.drawable.bind_walk_20);
			if (!bindError.equals("")) {
				if (bindError.length() > 0) {
					bindError = bindError.substring(0, bindError.length() - 1);
				}
				uploadBindError();
			} else {
				timer = new Timer();
				task = new TimerTask() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(MSG_START_BIND);
					}
				};
				timer = new Timer();
				timer.schedule(task, 1000);
			}

		}
	}

	/**
	 * 服务端添加
	 */
	private void addBindDevice() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.BINDBLEDEV_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("name", "我的智能鞋");
		params.put("lble", SpfUtil.readDeviceToShoeLeftTmp(mContext));
		params.put("rble", SpfUtil.readDeviceToShoeRightTmp(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_BIND_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
	}

	/**
	 * 上传绑定失败错误
	 */
	private void uploadBindError() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.BINDERROR_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("lble", SpfUtil.readDeviceToShoeLeftTmp(mContext));
		params.put("rble", SpfUtil.readDeviceToShoeRightTmp(mContext));
		params.put("errmsg", bindError);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_BIND_UPLOAD_ERROR,
				params, ClientConstant.BLEDEVICE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_BIND_BLEDEV:
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
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);
					} else if (return_code == 1) {
						return_msg = obj.getString("return_msg");
						if (return_msg.equals("Bound already")) {
							msg.what = MSG_BIND_ALREADY;
							mHandler.sendMessage(msg);
						}
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_BIND_BLEDEV解析失败");
				}
				break;
			case MSG_BIND_UPLOAD_ERROR:
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
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_BIND_UPLOAD_ERROR解析失败");
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
		try {
			AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
		} catch (Exception e) {
			// TODO: handle exception
			Log.i(TAG, "断开连接时崩了，大概是还没建立连接");
			AmomeApp.exercise_flag = false;
			AmomeApp.bleShoes = null;
			AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			e.printStackTrace();
		}
	}

	BleShoes.shoesDisconnectCallback shoesDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋断开连接成功");
				AmomeApp.exercise_flag = false;
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			} else {
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			}
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
		DialogUtil.showAlertDialog(mContext, "", "正在绑定中，是否退出？", "确定", "取消",
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
}
