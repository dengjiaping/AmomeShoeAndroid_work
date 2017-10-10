package cn.com.amome.amomeshoes.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.model.PressData;
import cn.com.amome.shoeservice.ble.GattPeripheral;
import cn.com.amome.shoeservice.ble.GattPeripheral.GattPeripheralCallback;
import cn.com.amome.shoeservice.com.bindProfile;
import cn.com.amome.shoeservice.com.bindProfile.BindCallback;
import cn.com.amome.shoeservice.com.controlProfile;
import cn.com.amome.shoeservice.com.controlProfile.ControlCallback;
import cn.com.amome.shoeservice.com.powerBatteryProfile;
import cn.com.amome.shoeservice.com.powerBatteryProfile.powerBatteryCallback;
import cn.com.amome.shoeservice.com.pushDailyProfile;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyCallback;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyData;
import cn.com.amome.shoeservice.com.pushPressProfile;
import cn.com.amome.shoeservice.com.pushPressProfile.PressCallback;
import cn.com.amome.shoeservice.com.readDeviceInfoProfile;
import cn.com.amome.shoeservice.com.readDeviceInfoProfile.DeviceInfoCallback;
import cn.com.amome.shoeservice.com.syncDailyProfile;
import cn.com.amome.shoeservice.com.syncDailyProfile.SyncDailyCallback;
import cn.com.amome.shoeservice.com.syncTimeProfile;
import cn.com.amome.shoeservice.com.syncTimeProfile.SyncTimeCallback;
import cn.com.amome.shoeservice.com.updateBleFirmwareProfile;
import cn.com.amome.shoeservice.com.updateBleFirmwareProfile.BleUpdateCallback;
import cn.com.amome.shoeservice.util.Constant;

public class BleDev {
	private String TAG = "BleDev";
	private GattPeripheral GP = null;
	private bindProfile BP = null;
	private controlProfile CP = null;
	private pushDailyProfile DP = null;
	private syncDailyProfile HP = null;
	private pushPressProfile PP = null;
	private syncTimeProfile TP = null;
	private readDeviceInfoProfile DIP = null;
	private updateBleFirmwareProfile BFP = null;
	private powerBatteryProfile PBP = null;

	private String address = "";

	private creCallback mCreCallback;
	private rebCallback mRebCallback;
	private resetCallback mResetCallback;
	private syncTimeCallback mSyncTimeCallback;
	private getPressCallback mPressCallback;
	private readPressDataCallback mReadPressDataCallback;
	private getHistCallback mHistCallback;
	private readDevInfoCallback mReadDevInfoCallback;
	private disDevConnectCallback mDisDevConnectCallback;
	private clickCallback mClickCallback;
	private getDevBatteryCallback mGetDevBatteryCallback;

	private int BPFailCount = 0;
	private int CPFailCount = 0;
	private int DPFailCount = 0;
	private int HPFailCount = 0;
	private int PPFailCount = 0;
	private int TPFailCount = 0;
	private int DIPFailCount = 0;
	private int BFPFailCount = 0;
	private int PBPFailCount = 0;

	private static final int MSG_GP_RECONNECTION = 0;
	private static final int MSG_BP_CONNECTION = 1;
	private static final int MSG_TP_CONNECTION = 2;
	private static final int MSG_CP_CONNECTION = 3;
	private static final int MSG_DP_CONNECTION = 4;
	private static final int MSG_PP_CONNECTION = 6;
	private static final int MSG_HP_CONNECTION = 5;
	private static final int MSG_DIP_CONNECTION = 7;
	private static final int MSG_BFP_CONNECTION = 8;
	private static final int MSG_PBP_CONNECTION = 9;
	private static final int MSG_CONNECTION_FIN = 10;
	private static final int MSG_RECONNECTION_FAIL = 11;

	private Handler mHandler;
	private boolean getPressFlag = false;
	private boolean getDailyFlag = false;
	private boolean getBatteryFlag = false;
	private int batteryValue = 100;
	private boolean isCharge=false;



	private static final int MSG_GET_PRESS = 0;
	private static final int MSG_GET_DAILY = 1;
	private static final int MSG_GET_HIST = 2;
	private static final int MSG_UPGRADE_PROGRESS = 3;
	private static final int MSG_STOP_PRESS = 4;

	public BleDev(String address, creCallback mCallback, Handler mHandler) {
		this.address = address;
		this.mHandler = mHandler;
		mCreCallback = mCallback;
		if (GP == null) {
			GP = AmomeApp.BleService.connectGattPeripheral(address, GPCallback, 1);
		}
		// 后加
		getPressFlag = false;
		getDailyFlag = false;
		getBatteryFlag = false;
	}

	public interface creCallback {
		public void isCreSuc(boolean arg0, String addr);

		public void isConnect(boolean arg0, String addr);

		public void isRec(boolean arg0, String addr);
	}

	public interface connectCallback {
		public void isConnectSuc(boolean arg0, String addr);
	}

	public interface rebCallback {
		public void isRebSuc(boolean arg0, String addr);
	}

	public interface resetCallback {
		public void isResetSuc(boolean arg0, String addr);
	}

	public interface syncTimeCallback {
		public void isSyncTimeSuc(boolean arg0, String addr);
	}

	public interface getPressCallback {
		public void isGetPressSuc(boolean arg0, String addr);
	}

	public interface readPressDataCallback {
		public void readPressData(String addr, short[] PressData);
	}

	public interface getHistCallback {
		public void isGetHistSuc(boolean arg0, String addr);
	}

	public interface readDevInfoCallback {
		public void isReadDevSuc(boolean arg0, String addr, String softVer);
	}

	public interface disDevConnectCallback {
		public void isDisDevSuc(boolean arg0, String addr);
	}

	public interface clickCallback {
		public void isClickSuc(boolean arg0, String addr);
	}

	public interface getDevBatteryCallback {
		public void isGetDevBatterySuc(boolean arg0, String addr, int value,boolean isCharge);
	}

	// GP
	private GattPeripheralCallback GPCallback = new GattPeripheralCallback() {
		@Override
		public void stateICallBack(final int msg, GattPeripheral mGattp, String address, final int ide) {
			Message message = Message.obtain();
			if (msg == Constant.MSG_BLE_CONNECTED) {
				// Log.i(TAG, mGattp.getAddress() + "GP成功链接");
			} else if (msg == Constant.MSG_BLE_DISCONNECT) {
				// Log.i(TAG, mGattp.getAddress() + "GP断开链接");
			} else if (msg == Constant.MSG_BLE_CONNECT_FAIL) {
				// Log.i(TAG, mGattp.getAddress() + "GP链接出错");
				mCreCallback.isConnect(false, mGattp.getAddress());
			} else if (msg == Constant.MSG_BLE_SERVICE_DISCOVERED) {
				// Log.i(TAG, mGattp.getAddress() + "GP发现蓝牙服务");
				message.what = MSG_TP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else if (msg == Constant.MSG_BLE_CLEANUP) {
				// Log.i(TAG, mGattp.getAddress() + "GP清理链接资源");
				mDisDevConnectCallback.isDisDevSuc(true, address);
			} else if (msg == Constant.MSG_BLE_RECONNECTED) {
				// Log.i(TAG, mGattp.getAddress() + "GP重连!");
				mCreCallback.isRec(true, address);
			}
			// Log.i(TAG, "stateICallBack #### msg: " + msg + " address: " +
			// address + " ide: " + ide);
		}

		@Override
		public void rssiICallBack(final int rssi, GattPeripheral mGattp, String address, final int ide) {
			// Log.i(TAG, "rssiICallBack #### msg: " + rssi + " address: " +
			// address + " ide: " + ide);
		}
	};

	// 解绑BP-单回调 可能要去掉了，暂时屏蔽
	private BindCallback unbindBPCallback = new BindCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_CP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				BPFailCount++;
				if (BPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (BPFailCount >= 2) {
				}
			}
		}

		@Override
		public void OnBindState(int arg0, int arg1, GattPeripheral arg2) {

		}
	};

	// CP
	private ControlCallback CPCallback = new ControlCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_DP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				CPFailCount++;
				if (CPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (CPFailCount >= 2) {
				}
			}
		}

		@Override
		public void OnControlState(int MSG, int Param, GattPeripheral mGattPeripheral) {
			// Log.i(TAG, "OnControlState MSG= " + MSG);
			// Log.i(TAG, "OnControlState Param=" + Param);
		}
	};

	// TP
	private SyncTimeCallback TPCallback = new SyncTimeCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				// message.what = MSG_BP_CONNECTION;
				message.what = MSG_CP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				TPFailCount++;
				if (TPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (TPFailCount >= 2) {
				}
			}
		}
	};

	// DP
	private DailyCallback DPCallback = new DailyCallback() {
		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_HP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				DPFailCount++;
				if (DPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (DPFailCount >= 2) {
				}
			}
		}

		@Override
		public void OnDailyData(DailyData mDD, GattPeripheral mGattPeripheral) {
			Message msg = Message.obtain();
			if (getDailyFlag) {
				int flag = Integer.parseInt(mGattPeripheral.getAddress().substring(16));
				if (flag == 0) {
					msg.arg1 = 0;
					// Log.i(TAG, "原始" + System.currentTimeMillis());
				} else if (flag == 1) {
					msg.arg1 = 1;
				}
				msg.obj = mDD;
				msg.what = MSG_GET_DAILY;
				mHandler.sendMessage(msg);
			}
		}

	};

	// 单脚历史数据HP
	private SyncDailyCallback HPCallback = new SyncDailyCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_PP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				HPFailCount++;
				if (HPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (HPFailCount >= 2) {
				}
			}
		}

		@Override
		public void GetSyncDailyData(int[][] arg0, GattPeripheral arg1) {
			// Log.i(TAG, "GetSyncDailyData");
			Message msg = Message.obtain();
			int flag = Integer.parseInt(arg1.getAddress().substring(16));
			if (flag % 2 == 0) {
				// Log.i(TAG, "右脚数组长度" + arg0.length);
				for (int i = 0; i < arg0.length; i++) {
					for (int j = 0; j < arg0[i].length; j++) {
						// Log.i(TAG, "右脚" + arg0[i][j]);
					}
				}
				msg.arg1 = 0;
			} else if (flag % 2 == 1) {
				// Log.i(TAG, "左脚数组长度" + arg0.length);
				for (int i = 0; i < arg0.length; i++) {
					for (int j = 0; j < arg0[i].length; j++) {
						// Log.i(TAG, "左脚" + arg0[i][j]);
					}
				}
				msg.arg1 = 1;
			}
			msg.obj = arg0;
			msg.what = MSG_GET_HIST;
			mHandler.sendMessage(msg);
		}

		@Override
		public void OnSyncDailyState(int arg0, int arg1, GattPeripheral arg2) {
			// Log.i(TAG, "OnSyncDailyState arg2+" + arg2.getAddress());
			if (arg1 == 0) {
				// Log.i(TAG, "OnSyncDailyState" + arg2.getAddress() +
				// "历史数据为空");
				Message msg = Message.obtain();
				int flag = Integer.parseInt(arg2.getAddress().substring(16));
				if (flag % 2 == 0) {
					msg.arg1 = 0;
				} else if (flag % 2 == 1) {
					msg.arg1 = 1;
				}
				msg.what = MSG_GET_HIST;
				msg.obj = null;
				mHandler.sendMessage(msg);
			}
		}

	};

	// PP
	private PressCallback PPCallback = new PressCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_DIP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				PPFailCount++;
				if (PPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (PPFailCount >= 2) {
				}
			}
		}

		@Override
		public void OnPressData(int num, long time, short[] PressData, GattPeripheral mGattPeripheral) {
			Message msg = Message.obtain();
			if (getPressFlag) {
				PressData pressData = new PressData(PressData, time);
				msg.obj = pressData;
				int flag = Integer.parseInt(mGattPeripheral.getAddress().substring(16));
				if (flag % 2 == 0) {
					// //Log.i(TAG, "right num " + num + "     time " + time);
					// WriteFile.method1("/sdcard/amome/right.txt", "\n" + num
					// + " " + time);
					msg.arg1 = 0;
				} else if (flag % 2 == 1) {
					// //Log.i(TAG, "left num" + num + "     time" + time);
					// WriteFile.method1("/sdcard/amome/left.txt", "\n" + num
					// + " " + time);
					msg.arg1 = 1;
				}
				msg.what = MSG_GET_PRESS;
				mHandler.sendMessage(msg);
			}
		}

		@Override
		public void OnPressState(int MSG1, int MSG2, GattPeripheral mGattPeripheral) {
			// Log.i(TAG, "OnPressState");
			Message message = Message.obtain();
			message.what = 72;
			message.arg1 = MSG1;
			message.arg2 = MSG2;
			message.obj = mGattPeripheral.getAddress();
			bleHandler.sendMessage(message);
		}

	};

	// DIP
	private DeviceInfoCallback DIPCallback = new DeviceInfoCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_BFP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				DIPFailCount++;
				if (DIPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (DIPFailCount >= 2) {
				}
			}
		}

	};

	// BFP
	private BleUpdateCallback BFPCallback = new BleUpdateCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_PBP_CONNECTION;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				BFPFailCount++;
				if (BFPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (BFPFailCount >= 2) {
				}
			}
		}

		@Override
		public void OnBleUpdateState(int MSG, GattPeripheral arg1) {
			// Log.i(TAG, "OnBleUpdateState");
			if (MSG == Constant.MSG_BLE_UPDATE_SUCC) {
				// Log.i(TAG, "固件升级成功");
				Message msg = Message.obtain();
				int flag = Integer.parseInt(arg1.getAddress().substring(16));
				if (flag % 2 == 0) {
					msg.arg1 = 0;
				} else if (flag % 2 == 1) {
					msg.arg1 = 1;
				}
				msg.what = MSG_UPGRADE_PROGRESS;
				msg.arg2 = 101;
				mHandler.sendMessage(msg);
			} else if (MSG == Constant.MSG_BLE_UPDATE_FAIL) {
				// Log.i(TAG, "固件升级失败");
			} else if (MSG == Constant.MSG_BLE_UPDATE_ING) {
				// Log.i(TAG, "固件升级中");
			} else if (MSG == Constant.MSG_BLE_UPDATE_CRC_ERR) {
				// Log.i(TAG, "固件CRC错误");
			}
		}

		@Override
		public void OnBleUpdateProgress(int MSG, GattPeripheral arg1) {
			// Log.i(TAG, "进度:" + MSG);
			Message msg = Message.obtain();
			int flag = Integer.parseInt(arg1.getAddress().substring(16));
			if (flag % 2 == 0) {
				msg.arg1 = 0;
			} else if (flag % 2 == 1) {
				msg.arg1 = 1;
			}
			msg.what = MSG_UPGRADE_PROGRESS;
			msg.arg2 = MSG;
			mHandler.sendMessage(msg);
		}
	};

	// 电量PBP
	private powerBatteryCallback PBPCallback = new powerBatteryCallback() {

		@Override
		public void isCreateSucc(boolean arg0, GattPeripheral mGattp) {
			Message message = Message.obtain();
			if (arg0) {
				message.what = MSG_CONNECTION_FIN;
				message.obj = mGattp;
				bleHandler.sendMessage(message);
			} else {
				PBPFailCount++;
				if (PBPFailCount <= 1) {
					// Log.i(TAG, mGattp.getAddress() + "GP准备重建");
				} else if (PBPFailCount >= 2) {
				}
			}

		}

		@Override
		public void OnPowerBatteryState(boolean arg0, int arg1, int arg2, GattPeripheral arg3) {
			// TODO Auto-generated method stub
			if (!getBatteryFlag) {
				batteryValue = arg1;
				getBatteryFlag = true;
				isCharge = arg0;
				// Log.i(TAG, address + "电量已赋值" + arg1);
			}
		}

	};

	private Handler bleHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_GP_RECONNECTION:
				AmomeApp.BleService.disconnectGattPeripheral(GP);
				GP = null;
				GP = AmomeApp.BleService.connectGattPeripheral((String) msg.obj, GPCallback, 1);
				break;
			case MSG_BP_CONNECTION:
				if (BP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "BP!=null，准备清理");
					BP.cleanup();
					BP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "BP=null，准备创建");
				BP = new bindProfile((GattPeripheral) msg.obj, unbindBPCallback);
				break;
			case MSG_TP_CONNECTION:
				if (TP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "TP!=null，准备清理");
					TP.cleanup();
					TP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "TP=null，准备创建");
				TP = new syncTimeProfile((GattPeripheral) msg.obj, TPCallback);
				break;
			case MSG_CP_CONNECTION:
				if (CP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "CP!=null，准备清理");
					CP.cleanup();
					CP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "CP=null，准备创建");
				CP = new controlProfile((GattPeripheral) msg.obj, CPCallback);
				break;
			case MSG_DP_CONNECTION:
				if (DP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "DP!=null，准备清理");
					DP.cleanup();
					DP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "DP=null，准备创建");
				DP = new pushDailyProfile((GattPeripheral) msg.obj, DPCallback);
				break;
			case MSG_HP_CONNECTION:
				if (HP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "HP!=null，准备清理");
					HP.cleanup();
					HP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "HP=null，准备创建");
				HP = new syncDailyProfile((GattPeripheral) msg.obj, HPCallback);
				break;
			case MSG_PP_CONNECTION:
				if (PP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "PP!=null，准备清理");
					PP.cleanup();
					PP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "PP=null，准备创建");
				PP = new pushPressProfile((GattPeripheral) msg.obj, PPCallback);
				break;
			case MSG_DIP_CONNECTION:
				if (DIP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "DIP!=null，准备清理");
					DIP.cleanup();
					DIP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "DIP=null，准备创建");
				DIP = new readDeviceInfoProfile((GattPeripheral) msg.obj, DIPCallback);
				break;
			case MSG_BFP_CONNECTION:
				if (BFP != null) {
					// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
					// "BFP!=null，准备清理");
					BFP.cleanup();
					BFP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "BFP=null，准备创建");
				BFP = new updateBleFirmwareProfile((GattPeripheral) msg.obj, BFPCallback);
				break;
			case MSG_PBP_CONNECTION:
				if (PBP != null) {
					// Log.i(TAG, "PBP!=null，准备清理");
					PBP.cleanup();
					PBP = null;
				}
				// Log.i(TAG, ((GattPeripheral) msg.obj).getAddress() +
				// "PBP=null，准备创建");
				PBP = new powerBatteryProfile((GattPeripheral) msg.obj, PBPCallback);
				break;
			case MSG_CONNECTION_FIN:
				if (TP != null && CP != null && DP != null && HP != null && PP != null && DIP != null && BFP != null && PBP != null) {
					try {
						// Log.i(TAG, GP.getAddress() + "同步时间前" +
						// TP.getSyncTime());
						if (TP.syncTime(0) == null) {
							// Log.i(TAG, GP.getAddress() + "同步时间失败");
						} else {
							// Log.i(TAG, GP.getAddress() + "同步时间成功");
							// Log.i(TAG, GP.getAddress() + "同步时间后" +
							// TP.getSyncTime());
						}
					} catch (Exception e) {
						// TODO: handle exception
						// Log.i(TAG, "崩了");
						e.printStackTrace();
						mSyncTimeCallback.isSyncTimeSuc(false, address);
					}
					// Log.i(TAG, address + "设备连接完成，所有准备工作都已建立");
					mCreCallback.isCreSuc(true, address);
				} else {
					mCreCallback.isCreSuc(false, address);
				}
				break;
			case MSG_RECONNECTION_FAIL:
				mCreCallback.isCreSuc(false, address);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 重启设备
	 */
	public void rebootDev(rebCallback mRebCallback) {
		this.mRebCallback = mRebCallback;
		if (GP != null) {
			if (CP != null) {
				// Log.i(TAG, GP.getAddress() + "CP不为空，准备重启设备");
				try {
					CP.sysReboot();
					mRebCallback.isRebSuc(true, address);
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mRebCallback.isRebSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "CP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}

	}

	/**
	 * 重置设备
	 */
	public void resetDev(resetCallback mResetCallback) {
		this.mResetCallback = mResetCallback;
		if (GP != null) {
			if (CP != null) {
				// Log.i(TAG, GP.getAddress() + "CP不为空，准备重置设备");
				try {
					CP.sysFactory();
					mResetCallback.isResetSuc(true, address);
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mResetCallback.isResetSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "CP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}

	}

	/**
	 * 固件升级
	 */
	public void upgradeDev(byte firmData[]) {
		if (GP != null) {
			if (BFP != null) {
				if (BFP.startBleUpdate(firmData, true) == null) {
					// Log.i(TAG, "升级失败");
				} else {
					// Log.i(TAG, "准备开始升级了！请留意进度");
				}
			}
		}
	}

	/**
	 * 同步设备时间
	 */
	public void syncTimeDev(syncTimeCallback mSyncTimeCallback) {
		this.mSyncTimeCallback = mSyncTimeCallback;
		if (GP != null) {
			if (TP != null) {
				// Log.i(TAG, GP.getAddress() + "TP不为空，准备同步时间");
				try {
					// Log.i(TAG, GP.getAddress() + "同步时间前" + TP.getSyncTime());
					if (TP.syncTime(0) == null) {
						// Log.i(TAG, GP.getAddress() + "同步时间失败");
						mSyncTimeCallback.isSyncTimeSuc(false, address);
					} else {
						// Log.i(TAG, GP.getAddress() + "同步时间成功");
						// Log.i(TAG, GP.getAddress() + "同步时间后" +
						// TP.getSyncTime());
						mSyncTimeCallback.isSyncTimeSuc(true, address);
					}
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mSyncTimeCallback.isSyncTimeSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "TP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}

	}

	/**
	 * 同步历史数据
	 */
	public void getHistData(getHistCallback mHistCallback) {
		this.mHistCallback = mHistCallback;
		if (GP != null) {
			if (HP != null) {
				// Log.i(TAG, GP.getAddress() + "HP不为空，准备同步历史数据");
				try {
					if (HP.syncDaily(false) == null) {
						// Log.i(TAG, GP.getAddress() + "获取历史数据失败");
						mHistCallback.isGetHistSuc(false, address);
					} else {
						// Log.i(TAG, GP.getAddress() + "获取历史数据成功");
						mHistCallback.isGetHistSuc(true, address);
					}
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mHistCallback.isGetHistSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "HP为空，重新创建GP"); //
				// 这里需要增加重建!!!!!!!!!!!!!!!!!!!!!!!
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}
	}

	/**
	 * 获取压力数据
	 */
	public void getPressData(getPressCallback mPressCallback) {
		this.mPressCallback = mPressCallback;
		if (GP != null) {
			if (PP != null) {
				// Log.i(TAG, GP.getAddress() + "PP不为空，准备获取压力数据");
				try {
					if (PP.startPressData(true) == null) {
						// Log.i(TAG, GP.getAddress() + "压力数据获取失败");
						mPressCallback.isGetPressSuc(false, address);
					} else {
						// Log.i(TAG, GP.getAddress() + "压力数据获取成功");
						mPressCallback.isGetPressSuc(true, address);
					}
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mPressCallback.isGetPressSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "PP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}
	}

	/**
	 * 获取压力数据
	 */
	public void readPressData() {
		getPressFlag = true;
	}

	/**
	 * 停止压力数据
	 */
	public void stopPressData() {
		if (GP != null) {
			if (PP != null) {
				PP.stopPressData();
				// Log.i(TAG, "停止压力数据"); // 这里并没有写回调成功或失败，以后看情况是否增加
				int flag = Integer.parseInt(address.substring(16));
				Message msg = Message.obtain();
				if (flag % 2 == 0) {
					msg.arg1 = 0;
				} else if (flag % 2 == 1) {
					msg.arg1 = 1;
				}
				msg.what = MSG_STOP_PRESS;
				mHandler.sendMessage(msg);
			}
		}
	}

	/**
	 * 停止压力数据
	 */
	public void stopPressDataNoCall() {
		if (GP != null) {
			if (PP != null) {
				PP.stopPressData();
				// Log.i(TAG, "停止压力数据"); // 这里并没有写回调成功或失败
			}
		}
	}

	/**
	 * 获取日常数据
	 */
	public void readDailyData() {
		getDailyFlag = true;
	}

	/**
	 * 读取设备信息
	 */
	public void readDevInfo(readDevInfoCallback mReadDevInfoCallback) {
		this.mReadDevInfoCallback = mReadDevInfoCallback;
		if (GP != null) {
			if (DIP != null) {
				// Log.i(TAG, GP.getAddress() + "DIP不为空，准备读取设备信息");
				try {
					if (DIP.getDeviceInfo() == null) {
						// Log.i(TAG, GP.getAddress() + "设备信息获取失败");
						mReadDevInfoCallback.isReadDevSuc(false, address, null);
					} else {
						// Log.i(TAG, GP.getAddress() + "设备信息获取成功");
						mReadDevInfoCallback.isReadDevSuc(true, address, DIP.getDeviceInfo().swVer);
					}
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, GP.getAddress() + "崩了");
					e.printStackTrace();
					mReadDevInfoCallback.isReadDevSuc(false, address, null);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "DIP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}
	}

	/**
	 * 断开连接
	 */
	public void disConnect(disDevConnectCallback mDisDevConnectCallback) {
		this.mDisDevConnectCallback = mDisDevConnectCallback;
		if (BP != null) {
			BP.cleanup();
		}
		if (CP != null) {
			CP.cleanup();
		}
		if (DP != null) {
			DP.cleanup();
		}
		if (HP != null) {
			HP.cleanup();
		}
		if (PP != null) {
			PP.cleanup();
		}
		if (TP != null) {
			TP.cleanup();
		}
		if (DIP != null) {
			DIP.cleanup();
		}
		if (BFP != null) {
			BFP.cleanup();
		}
		if (PBP != null) {
			PBP.cleanup();
		}
		if (GP != null) {
			AmomeApp.BleService.disconnectGattPeripheral(GP);
		}
		// BleService.mSleep(1000);
		// //Log.i(TAG, "休息了1秒");
	}

	/**
	 * 震动设备
	 */
	public void clickDev(clickCallback mClickCallback) {
		this.mClickCallback = mClickCallback;
		if (GP != null) {
			if (CP != null) {
				// Log.i(TAG, GP.getAddress() + "CP不为空，准备震动设备");
				try {
					if (CP.clickMotor() == null) {
						// Log.i(TAG, GP.getAddress() + "震动失败");
						mClickCallback.isClickSuc(false, address);
					} else {
						mClickCallback.isClickSuc(true, address);
					}
				} catch (Exception e) {
					// TODO: handle exception
					// Log.i(TAG, "崩了");
					e.printStackTrace();
					mClickCallback.isClickSuc(false, address);
				}
			} else {
				// Log.i(TAG, GP.getAddress() + "CP为空，重新创建GP"); // 这里需要增加重建
			}
		} else {
			reCreGP(address);// 重建的后续操作需要补充
		}
	}

	public void getDevBattery(getDevBatteryCallback mGetDevBatteryCallback) {
		this.mGetDevBatteryCallback = mGetDevBatteryCallback;
		mGetDevBatteryCallback.isGetDevBatterySuc(true, address, batteryValue,isCharge);
		// Log.i(TAG, address + "电量" + batteryValue);
	}

	private void reCreGP(String addr) {
		GP = null;
		GP = AmomeApp.BleService.connectGattPeripheral(addr, GPCallback, 1);
	}
}
