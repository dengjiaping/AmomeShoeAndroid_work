package cn.com.amome.amomeshoes.view.main.my.setting;

import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetBatteryInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesRebCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesResetCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesUpgradeCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.events.ExitEvent;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.UpgradeShoe;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DataCleanManager;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.bind.BindActivity;
import cn.com.amome.shoeservice.BleService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 软件设置
 * 
 * @author css
 */
public class AppSettingActivity extends Activity implements OnClickListener {
	private String TAG = "AppSettingActivity";
	private TextView tv_title, tv_left, tv_upgradeprogress;
	private ImageView iv_left;
	private Context mContext;
	private ToggleButton view_switch_shake, view_switch_alarm;
	private TextView tv_cach, tv_left_upgradeprogress,
			tv_right_upgradeprogress;
	private String cachSize;
	private boolean flag = false;
	private int flagnum;
	// private BleDevice bleshoes;
	private static final int MSG_BIND_BLEDEV = 68;
	private String leftVerStr = "", rightVerStr = "";// 蓝牙版本 eg. 1.3
	private static final int MSG_UPSHOES_LEFT = 6;
	private static final int MSG_UPSHOES_RIGHT = 7;
	private static final int MSG_DIS_BLE_CONNECT = 8;
	private Gson gson = new Gson();
	private List<UpgradeShoe> upgradeShoeList;
	private UpgradeShoe upgradeShoe;
	private String downloadUrl;
	private String leftPath, rightPath;
	private int bleFlag = BleConstants.MSG_CONNECT;
	private boolean leftNew = false, rightNew = false;
	private boolean leftUpSuc = false, rightUpSuc = false;
	private int getBatteryCount = 0; // 记录获取电量的次数，第一次获取的不展示，第二次获取的展示
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_UPSHOES_LEFT:
				String json = (String) msg.obj;
				if (json.equals("[{}]")) {
					leftNew = true;
					leftUpSuc = true;
					if (leftNew && rightNew) {
						T.showToast(mContext, "已是最新版本", 0);
					}
					Log.i(TAG, "左脚未检测到新版本");
				} else if (TextUtils.isEmpty(json)) {
					T.showToast(mContext, "服务器返回值异常", 0);
				} else {
					Log.i(TAG, "左脚检测到新版本");
					upgradeShoeList = gson.fromJson(json,
							new TypeToken<List<UpgradeShoe>>() {
							}.getType());
					if (upgradeShoeList != null && upgradeShoeList.size() > 0) {
						upgradeShoe = upgradeShoeList.get(0);
						downloadUrl = upgradeShoe.firmware_path;
						downloadApp(downloadUrl, "left");
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								AmomeApp.bleShoes.upgradeShoes(
										shoesUpgradeCallback,
										SpfUtil.readDeviceToShoeLeft(mContext),
										getBytes(leftPath));
							}
						};
						timer = new Timer();
						timer.schedule(task, 5000);
					}
				}
				break;
			case MSG_UPSHOES_RIGHT:
				String json_right = (String) msg.obj;
				if (json_right.equals("[{}]")) {
					rightNew = true;
					rightUpSuc = true;
					if (leftNew && rightNew) {
						T.showToast(mContext, "已是最新版本", 0);
					}
					Log.i(TAG, "右脚未检测到新版本");
				} else if (TextUtils.isEmpty(json_right)) {
					T.showToast(mContext, "服务器返回值异常", 0);
				} else {
					Log.i(TAG, "右脚检测到新版本");
					upgradeShoeList = gson.fromJson(json_right,
							new TypeToken<List<UpgradeShoe>>() {
							}.getType());
					if (upgradeShoeList != null && upgradeShoeList.size() > 0) {
						upgradeShoe = upgradeShoeList.get(0);
						downloadUrl = upgradeShoe.firmware_path;
						downloadApp(downloadUrl, "right");
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								AmomeApp.bleShoes
										.upgradeShoes(
												shoesUpgradeCallback,
												SpfUtil.readDeviceToShoeRight(mContext),
												getBytes(rightPath));
							}
						};
						timer = new Timer();
						timer.schedule(task, 5000);
					}
				}
				break;
			case MSG_DIS_BLE_CONNECT:
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_main);
		EventBus.getDefault().register(this);
		mContext = this;
		try {
			cachSize = DataCleanManager.getCacheSize(new File(Environments
					.getDiskCacheDir(mContext)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_left_upgradeprogress = (TextView) findViewById(R.id.tv_left_upgradeprogress);
		tv_right_upgradeprogress = (TextView) findViewById(R.id.tv_right_upgradeprogress);
		tv_cach = (TextView) findViewById(R.id.tv_cach);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("设置");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		tv_cach.setText(cachSize);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.rl_update_mome).setOnClickListener(this);
		findViewById(R.id.rl_clear_cache).setOnClickListener(this);
		findViewById(R.id.rl_account_management).setOnClickListener(this);
		findViewById(R.id.rl_aboutmome).setOnClickListener(this);
		findViewById(R.id.rl_userhelp).setOnClickListener(this);
		findViewById(R.id.contract_us).setOnClickListener(this);
		findViewById(R.id.rl_bind_shoes).setOnClickListener(this);
		findViewById(R.id.rl_setting_restart_shoes).setOnClickListener(this);
		findViewById(R.id.rl_setting_reset_shoes).setOnClickListener(this);
		findViewById(R.id.rl_setting_upgrade_shoes).setOnClickListener(this);
		findViewById(R.id.rl_setting_get_battery).setOnClickListener(this);
		findViewById(R.id.rl_setting_my_shoes).setOnClickListener(this);
		findViewById(R.id.rl_setting_more_shoes).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.rl_account_management:
			startActivity(new Intent(mContext, AccountManagementActivity.class));
			break;
		case R.id.rl_clear_cache:
			MobclickAgent.onEvent(mContext, ClassType.clean_cache);
			if (tv_cach.getText().toString().equals("0.0Byte")) {
				T.showToast(mContext, "已经很干净啦!", 0);
			} else {
				if (cleanCach())
					tv_cach.setText("0.0Byte");
			}
			break;
		case R.id.rl_update_mome:
			startActivity(new Intent(mContext, UpgradeAmomeActivity.class));
			break;
		case R.id.contract_us:
			startActivity(new Intent(mContext, ContactUsActivity.class));
			break;
		case R.id.rl_userhelp:
			Intent intent = new Intent(mContext, UserHelpActivity.class);
			intent.putExtra("title", "用户帮助");
			startActivity(intent);
			break;
		case R.id.rl_setting_my_shoes:
			startActivity(new Intent(mContext, MyShoesActivity.class));
			break;
		case R.id.rl_setting_restart_shoes:
			restartShoes();
			break;
		case R.id.rl_setting_reset_shoes:
			resetShoes();
			break;
		case R.id.rl_setting_upgrade_shoes:
			getShoesVersion();
			break;
		case R.id.rl_setting_get_battery:
			getBatteryShoes();
			break;
		case R.id.rl_bind_shoes:
			startActivity(new Intent(mContext, BindActivity.class));
			break;
		case R.id.rl_setting_more_shoes:
			startActivity(new Intent(mContext, MoreSettingActivity.class));
			break;
		default:
			break;
		}

	}

	/** 重启智能鞋 */
	private void restartShoes() {
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			T.showToast(mContext, "没有绑定智能鞋，请先绑定", 0);
		} else if (enableBluetooth()) {
			// DialogUtil.showCancelProgressDialog(mContext, "", "智能鞋连接中...",
			// true, true);
			try {
				Log.i(TAG, "准备和蓝牙建立连接");
				if (AmomeApp.bleShoes != null) {
					Log.i(TAG, "AmomeApp.bleShoes不为空");
					AmomeApp.bleShoes.rebootShoes(shoesRebCallback);
				} else {
					Log.i(TAG, "AmomeApp.bleShoes为空");
					bleFlag = BleConstants.MSG_REBOOT;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
				T.showToast(mContext, "未找到蓝牙设备", 0);
			}
		}
	}

	/** 重置智能鞋 */
	private void resetShoes() {
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			T.showToast(mContext, "没有绑定智能鞋，请先绑定", 0);
		} else if (enableBluetooth()) {
			try {
				Log.i(TAG, "准备和蓝牙建立连接");
				if (AmomeApp.bleShoes != null) {
					Log.i(TAG, "AmomeApp.bleShoes不为空");
					AmomeApp.bleShoes.resetShoes(shoesResetCallback);
				} else {
					Log.i(TAG, "AmomeApp.bleShoes为空");
					bleFlag = BleConstants.MSG_RESET;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
				T.showToast(mContext, "未找到蓝牙设备", 0);
			}
		}
	}

	/** 获取智能鞋版本 */
	private void getShoesVersion() {
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			T.showToast(mContext, "没有绑定智能鞋，请先绑定", 0);
		} else if (enableBluetooth()) {
			// DialogUtil.showCancelProgressDialog(mContext, "", "智能鞋连接中...",
			// true, true);
			leftNew = false;
			rightNew = false;
			leftUpSuc = false;
			rightUpSuc = false;
			try {
				Log.i(TAG, "准备和蓝牙建立连接");
				if (AmomeApp.bleShoes != null) {
					Log.i(TAG, "AmomeApp.bleShoes不为空");
					AmomeApp.bleShoes.getShoesInfo(shoesReadInfoCallback);
				} else {
					Log.i(TAG, "AmomeApp.bleShoes为空，准备创建");
					bleFlag = BleConstants.MSG_GETINFO;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
				T.showToast(mContext, "未找到蓝牙设备", 0);
			}
		}
	}

	/** 获取智能鞋电量 */
	private void getBatteryShoes() {
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			T.showToast(mContext, "没有绑定智能鞋，请先绑定", 0);
		} else if (enableBluetooth()) {
			// DialogUtil.showCancelProgressDialog(mContext, "", "智能鞋连接中...",
			// true, true);
			try {
				Log.i(TAG, "准备和蓝牙建立连接");
				if (AmomeApp.bleShoes != null) {
					Log.i(TAG, "AmomeApp.bleShoes不为空");
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
				} else {
					Log.i(TAG, "AmomeApp.bleShoes为空");
					bleFlag = BleConstants.MSG_GETBATTERY;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
				T.showToast(mContext, "未找到蓝牙设备", 0);
			}
		}
	}

	private boolean cleanCach() {
		DialogUtil.showProgressDialog(mContext, "", "清理中...");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				DataCleanManager.cleanInternalCache(mContext);
				DataCleanManager.cleanExternalCache(mContext);
				DataCleanManager.cleanFiles(mContext);
				DataCleanManager.cleanCustomCache(Environments
						.getImageCachPath(mContext));
				DialogUtil.hideProgressDialog();
			}
		}, 1 * 1000);
		return true;
	}

	/**
	 * 判断蓝牙是否开启
	 */
	public boolean enableBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		// 判断是否有蓝牙功能
		if (adapter != null && !adapter.isEnabled()) {
			Log.i(TAG, "蓝牙未打开");
			T.showToast(mContext, "蓝牙未打开", 0);
			return false;
		}
		Log.i(TAG, "蓝牙已打开");
		return true;
	}

	private void connectShoes() {
		if (AmomeApp.bleShoes == null) {
			DialogUtil.showCancelProgressDialog(mContext, "", "智能鞋开始连接...",
					true, true);
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
				DialogUtil.hideProgressDialog();
				T.showToast(mContext, "智能鞋连接完成", 0);
				switch (bleFlag) {
				case BleConstants.MSG_REBOOT:
					AmomeApp.bleShoes.rebootShoes(shoesRebCallback);
					break;
				case BleConstants.MSG_RESET:
					AmomeApp.bleShoes.resetShoes(shoesResetCallback);
					break;
				case BleConstants.MSG_GETINFO:
					AmomeApp.bleShoes.getShoesInfo(shoesReadInfoCallback);
					bleFlag = BleConstants.MSG_CONNECT;
					break;
				case BleConstants.MSG_GETBATTERY:
					BleService.mSleep(1000);
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
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

	BleShoes.shoesRebCallback shoesRebCallback = new shoesRebCallback() {

		@Override
		public void isRebSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋重启完成");
				T.showToast(mContext, "智能鞋重启完成", 0);
				bleFlag = BleConstants.MSG_CONNECT;
				AmomeApp.exercise_flag = false;
				DialogUtil.hideProgressDialog();
				DialogUtil.showCancelProgressDialog(mContext, "",
						"智能鞋重新连接中...", true, true);
			} else {
				Log.i(TAG, "智能鞋重启出错");
				T.showToast(mContext, "智能鞋重启出错", 0);
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				DialogUtil.hideProgressDialog();
			}
		}
	};

	BleShoes.shoesResetCallback shoesResetCallback = new shoesResetCallback() {

		@Override
		public void isResetSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋重置完成");
				T.showToast(mContext, "智能鞋重置完成", 0);
				AmomeApp.exercise_flag = false;
				DialogUtil.hideProgressDialog();
			} else {
				Log.i(TAG, "智能鞋重置出错");
				T.showToast(mContext, "智能鞋重置出错", 0);
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				DialogUtil.hideProgressDialog();
			}
		}
	};

	BleShoes.shoesReadInfoCallback shoesReadInfoCallback = new shoesReadInfoCallback() {

		@Override
		public void isReadInfoSucc(boolean arg0, String[] softVerArr) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋版本信息读取成功");
				DialogUtil.hideProgressDialog();
				Log.i(TAG, softVerArr[0] + softVerArr[1]);
				rightVerStr = softVerArr[0];
				leftVerStr = softVerArr[1];
				getNewVersion(rightVerStr, MSG_UPSHOES_RIGHT);
				getNewVersion(leftVerStr, MSG_UPSHOES_LEFT);
			} else {
				Log.i(TAG, "智能鞋版本信息读取失败");
				T.showToast(mContext, "智能鞋版本信息读取失败", 0);
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
				DialogUtil.hideProgressDialog();
			}
		}
	};

	BleShoes.shoesUpgradeCallback shoesUpgradeCallback = new shoesUpgradeCallback() {

		@Override
		public void readUpgradeProgress(String addr, int progress) {
			// TODO Auto-generated method stub
			int flag = Integer.parseInt(addr.substring(16));
			if (flag % 2 == 0) {
				if (progress <= 100) {
					tv_right_upgradeprogress.setVisibility(View.VISIBLE);
					tv_right_upgradeprogress.setText("右脚：" + progress + "%");
				} else {
					tv_right_upgradeprogress.setVisibility(View.GONE);
					Log.i(TAG, "右脚升级完成");
					T.showToast(mContext, "升级完成", 0);
					leftUpSuc = true;
				}
			} else if (flag % 2 == 1) {
				if (progress <= 100) {
					tv_left_upgradeprogress.setVisibility(View.VISIBLE);
					tv_left_upgradeprogress.setText("左脚：" + progress + "%");
				} else {
					tv_left_upgradeprogress.setVisibility(View.GONE);
					Log.i(TAG, "左脚升级完成");
					T.showToast(mContext, "升级完成", 0);
					rightUpSuc = true;
				}
			}
			if (leftUpSuc && rightUpSuc) {
				DialogUtil.showCancelProgressDialog(mContext, "",
						"智能鞋重新连接中...", true, true);
				leftUpSuc = false;
				rightUpSuc = false;
				downloadUrl = "";
				leftPath = "";
				rightPath = "";
			}
		}
	};

	BleShoes.shoesGetBatteryInfoCallback shoesGetBatteryInfoCallback = new shoesGetBatteryInfoCallback() {

		@Override
		public void isGetBatterySucc(boolean arg0, int leftVal, int rightVal) {
			// TODO Auto-generated method stub
			DialogUtil.hideProgressDialog();
			if (arg0) {
				Log.i(TAG, "电量获取第" + getBatteryCount + "次" + leftVal + ","
						+ rightVal);
				if (getBatteryCount != 0) {
					if (leftVal < 5 || rightVal < 5) {
						T.showToast(mContext, "左鞋电量：" + leftVal + "%，右鞋电量："
								+ rightVal + "%\n" + "电量不足请充电", 0);
					} else {
						T.showToast(mContext, "左鞋电量：" + leftVal + "%，右鞋电量："
								+ rightVal + "%", 0);
					}
				} else {
					getBatteryCount++;
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
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

	/**
	 * 检查是否有新版本
	 */
	private void getNewVersion(String ver, int type) {
		if (type == MSG_UPSHOES_RIGHT) {
			Log.i(TAG, "右脚准备检查更新");
		} else if (type == MSG_UPSHOES_LEFT) {
			Log.i(TAG, "左脚准备检查更新");
		}
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.UPBLEDEV_TYPE);
		params.put("firm_version", ver);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, type, params,
				ClientConstant.BLEUPGRADE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_UPSHOES_LEFT:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
						Log.i(TAG, type + " : returncode = 0 , "
								+ "return_msg = " + return_msg);
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_UPSHOES_LEFT解析失败");
				}
				break;
			case MSG_UPSHOES_RIGHT:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
						Log.i(TAG, type + " : returncode = 0 , "
								+ "return_msg = " + return_msg);
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_UPSHOES_RIGHT解析失败");
				}
				break;
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
						msg.what = 68;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);
					} else if (return_code == 1) {
						return_msg = obj.getString("return_msg");
						if (return_msg.equals("Binded already")) {
							Log.i(TAG, "已经被绑定");
						}
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_BIND_BLEDEV解析失败");
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

	/**
	 * 下载固件
	 * 
	 * @param down_url
	 */
	private void downloadApp(String down_url, String name) {
		if (name.equals("left")) {
			// leftPath = Environments.getSDPath(mContext) + "/" + name
			// + "upgrade.bin";
			leftPath = Environments.getSDRootPath() + "/" + name
					+ "upgrade.bin";
			Log.i(TAG, "下载路径:" + leftPath);
			try {
				AsynHttpDowanloadFile.downloadOtherFile(down_url, leftPath,
						mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (name.equals("right")) {
			// rightPath = Environments.getSDPath(mContext) + "/" + name
			// + "upgrade.bin";
			rightPath = Environments.getSDRootPath() + "/" + name
					+ "upgrade.bin";
			Log.i(TAG, "下载路径:" + rightPath);
			try {
				AsynHttpDowanloadFile.downloadOtherFile(down_url, rightPath,
						mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	/**
	 * 获得指定文件的byte数组
	 */
	private byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	// 接收帐号管理退出消息
	public void onEventMainThread(ExitEvent event) {
		Log.i(TAG, TAG + "收到了消息");
		finish();
	}
}
