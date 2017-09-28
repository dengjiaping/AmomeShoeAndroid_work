package cn.com.amome.amomeshoes.util;

import cn.com.amome.amomeshoes.model.PressData;
import cn.com.amome.amomeshoes.util.BleDev.getPressCallback;
import cn.com.amome.amomeshoes.util.BleDev.readDevInfoCallback;
import cn.com.amome.amomeshoes.util.BleDev.syncTimeCallback;
import cn.com.amome.amomeshoes.util.BleDev.resetCallback;
import cn.com.amome.amomeshoes.util.BleDev.rebCallback;
import cn.com.amome.amomeshoes.util.BleDev.creCallback;
import cn.com.amome.amomeshoes.util.BleDev.getHistCallback;
import cn.com.amome.amomeshoes.util.BleDev.disDevConnectCallback;
import cn.com.amome.amomeshoes.util.BleDev.clickCallback;
import cn.com.amome.amomeshoes.util.BleDev.getDevBatteryCallback;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyData;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BleShoes {
	private static String TAG = "BleShoes";
	private Context mContext;

	private BleDev left, right;
	private String address1 = "", address2 = "";
	private shoesCreCallback mShoesCreCallback;
	private shoesRebCallback mShoesRebCallback;
	private shoesResetCallback mShoesResetCallback;
	private shoesSyncTimeCallback mShoesSyncTimeCallback;
	private shoesGetPressCallback mShoesGetPressCallback;
	private shoesReadPressDataCallback mShoesReadPressDataCallback;
	private shoesStopPressDataCallback mShoesStopPressDataCallback;
	private shoesReadDailyDataCallback mShoesReadDailyDataCallback;
	private shoesGetHistDataCallback mShoesGetHistDataCallback;
	private shoesReadInfoCallback mShoesReadInfoCallback;
	private shoesDisconnectCallback mShoesDisconnectCallback,
			mShoesSingleDisconnectCallback;
	private shoesClickCallback mShoesClickCallback;
	private shoesGetBatteryInfoCallback mShoesGetBatteryInfoCallback;
	private shoesUpgradeCallback mShoesUpgradeCallback;
	private int leftConnect = -1, rightConnect = -1; // 0 成功 1 失败
	private int leftReboot = -1, rightReboot = -1;
	private int leftReset = -1, rightReset = -1;
	private int leftSyncTime = -1, rightSyncTime = -1;
	private int leftGetPress = -1, rightGetPress = -1;
	private int leftDevInfo = -1, rightDevInfo = -1;
	private int leftGetHist = -1, rightGetHist = -1;
	private int leftDisCon = -1, rightDisCon = -1;
	private int leftGetBattery = -1, rightGetBattery = -1;
	private int leftStopPress = -1, rightStopPress = -1;

	private static final int MSG_GET_PRESS = 0;
	private static final int MSG_GET_DAILY = 1;
	private static final int MSG_GET_HIST = 2;
	private static final int MSG_UPGRADE_PROGRESS = 3;
	private static final int MSG_STOP_PRESS = 4;

	private int leftValue = -1, rightValue = -1;
	private boolean areadlyDisConnect = false;
	private String[] softVerArr = new String[2]; // 用来存放版本号，[0]存放右脚 [1]存放左脚

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_GET_PRESS:
				PressData pressData = (PressData) msg.obj;
				if (msg.arg1 == 0) {
					mShoesReadPressDataCallback.readPressData(address2,
							pressData.getPressData(), pressData.getTime());
				} else if (msg.arg1 == 1) {
					mShoesReadPressDataCallback.readPressData(address1,
							pressData.getPressData(), pressData.getTime());
				}
				break;
			case MSG_GET_DAILY:
				if (msg.arg1 == 0) {
					mShoesReadDailyDataCallback.readDailyData(address2,
							(DailyData) msg.obj);
				} else if (msg.arg1 == 1) {
					mShoesReadDailyDataCallback.readDailyData(address1,
							(DailyData) msg.obj);
				}
				break;
			case MSG_GET_HIST:
				if (msg.obj == null) {
					mShoesGetHistDataCallback.isGetHistSucc(true, msg.arg1,
							null);
					if (msg.arg1 == 0) {
						Log.i(TAG, "历史数据为空" + msg.arg1);
					} else if (msg.arg1 == 1) {
						Log.i(TAG, "历史数据为空" + msg.arg1);
					}
				} else {
					mShoesGetHistDataCallback.isGetHistSucc(true, msg.arg1,
							(int[][]) msg.obj);
					if (msg.arg1 == 0) {
						Log.i(TAG, "历史数据不为空" + msg.arg1);
					} else if (msg.arg1 == 1) {
						Log.i(TAG, "历史数据不为空" + msg.arg1);
					}
				}
				break;
			case MSG_UPGRADE_PROGRESS:
				if (msg.arg1 == 0) {
					mShoesUpgradeCallback.readUpgradeProgress(address2,
							msg.arg2);
				} else if (msg.arg1 == 1) {
					mShoesUpgradeCallback.readUpgradeProgress(address1,
							msg.arg2);
				}
				break;
			case MSG_STOP_PRESS:
				if (msg.arg1 == 0) {
					rightStopPress = 0;
				} else if (msg.arg1 == 1) {
					leftStopPress = 0;
				}
				if (rightStopPress == 0 && leftStopPress == 0) {
					rightStopPress = -1;
					leftStopPress = -1;
					mShoesStopPressDataCallback.isStopPressSuc(true);
				}
				break;
			default:
				break;
			}
		};
	};

	public BleShoes(String address1, String address2,
			shoesCreCallback mShoesCreCallback, Context mContext) {
		this.mContext = mContext;
		this.mShoesCreCallback = mShoesCreCallback;
		this.address1 = address1;
		this.address2 = address2;
		left = new BleDev(address1, mCreCallback, mHandler);
		right = new BleDev(address2, mCreCallback, mHandler);
	}

	public interface shoesCreCallback {
		public void isCreSucc(boolean arg0);

		public void isConnect(boolean arg0, String addr);

		public void isRec(boolean arg0);
	}

	public interface shoesRebCallback {
		public void isRebSucc(boolean arg0);
	}

	public interface shoesResetCallback {
		public void isResetSucc(boolean arg0);
	}

	public interface shoesSyncTimeCallback {
		public void isSyncTimeSucc(boolean arg0);
	}

	public interface shoesGetPressCallback {
		public void isGetPressSucc(boolean arg0);
	}

	public interface shoesReadPressDataCallback {
		public void readPressData(String addr, short[] PressData, long time);
	}

	public interface shoesStopPressDataCallback {
		public void isStopPressSuc(boolean arg0);
	}

	public interface shoesGetDailyCallback {
		public void isGetDailySucc(boolean arg0);
	}

	public interface shoesReadDailyDataCallback {
		public void readDailyData(String addr, DailyData dailyData);
	}

	public interface shoesGetHistDataCallback {
		public void isGetHistSucc(boolean arg0, int arg1, int[][] arg2);
	}

	public interface shoesReadInfoCallback {
		public void isReadInfoSucc(boolean arg0, String[] sofVerArr);
	}

	public interface shoesDisconnectCallback {
		public void isDisconnectSucc(boolean arg0);
	}

	public interface shoesClickCallback {
		public void isClickSucc(boolean arg0, String addr);
	}

	public interface shoesGetBatteryInfoCallback {
		public void isGetBatterySucc(boolean arg0, int leftVal, int rightVal);
	}

	public interface shoesUpgradeCallback {
		public void readUpgradeProgress(String addr, int progress);
	}

	// 智能鞋连接结果
	BleDev.creCallback mCreCallback = new creCallback() {

		@Override
		public void isCreSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));
			if (arg0) {
				Log.i(TAG, addr + "连接成功");
				if (flag % 2 == 0) {
					rightConnect = 0;
				} else if (flag % 2 == 1) {
					leftConnect = 0;
				}
			} else {
				Log.i(TAG, addr + "连接失败");
				if (flag % 2 == 0) {
					rightConnect = 1;
				} else if (flag % 2 == 1) {
					leftConnect = 1;
				}
			}
			if (leftConnect == 0 && rightConnect == 0) {
				Log.i(TAG, "智能鞋建立连接成功");
				mShoesCreCallback.isCreSucc(true);
				leftConnect = -1;
				rightConnect = -1;
			} else if ((leftConnect == 1 && rightConnect != -1)
					|| (rightConnect == 1 && leftConnect != -1)) {
				Log.i(TAG, "智能鞋建立连接失败");
				mShoesCreCallback.isCreSucc(false);
				leftConnect = -1;
				rightConnect = -1;
			}

		}

		@Override
		public void isConnect(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (!arg0 && !areadlyDisConnect) {
				Log.i(TAG, addr + "意外断开连接");
				mShoesCreCallback.isConnect(false, addr);
				areadlyDisConnect = true;
			}
		}

		@Override
		public void isRec(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (arg0) {
				mShoesCreCallback.isRec(true);
			}
		}
	};

	// 智能鞋重启结果
	BleDev.rebCallback mRebCallback = new rebCallback() {

		@Override
		public void isRebSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));

			if (arg0) {
				Log.i(TAG, addr + "重启成功");
				if (flag % 2 == 0) {
					rightReboot = 0;
				} else if (flag % 2 == 1) {
					leftReboot = 0;
				}
			} else {
				Log.i(TAG, addr + "重启失败");
				if (flag % 2 == 0) {
					rightReboot = 1;
				} else if (flag % 2 == 1) {
					leftReboot = 1;
				}
			}
			if (leftReboot == 0 && rightReboot == 0) {
				Log.i(TAG, "智能鞋重启成功");
				mShoesRebCallback.isRebSucc(true);
			} else if ((leftReboot == 1 && rightReboot != -1)
					|| (rightReboot == 1 && leftReboot != -1)) {
				Log.i(TAG, "智能鞋重启失败");
				mShoesRebCallback.isRebSucc(false);
			}
		}
	};

	// 智能鞋重置结果
	BleDev.resetCallback mResetCallback = new resetCallback() {

		@Override
		public void isResetSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));

			if (arg0) {
				Log.i(TAG, addr + "重置成功");
				if (flag % 2 == 0) {
					rightReset = 0;
				} else if (flag % 2 == 1) {
					leftReset = 0;
				}
			} else {
				Log.i(TAG, addr + "重置失败");
				if (flag % 2 == 0) {
					rightReset = 1;
				} else if (flag % 2 == 1) {
					leftReset = 1;
				}
			}
			if (leftReset == 0 && rightReset == 0) {
				Log.i(TAG, "智能鞋重置成功");
				mShoesResetCallback.isResetSucc(true);
			} else if ((leftReset == 1 && rightReset != -1)
					|| (rightReset == 1 && leftReset != -1)) {
				Log.i(TAG, "智能鞋重置失败");
				mShoesResetCallback.isResetSucc(false);
			}
		}
	};

	// 智能鞋同步时间结果
	BleDev.syncTimeCallback mSyncTimeCallback = new syncTimeCallback() {

		@Override
		public void isSyncTimeSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));

			if (arg0) {
				Log.i(TAG, addr + "同步时间成功");
				if (flag % 2 == 0) {
					rightSyncTime = 0;
				} else if (flag % 2 == 1) {
					leftSyncTime = 0;
				}
			} else {
				Log.i(TAG, addr + "同步时间失败");
				if (flag % 2 == 0) {
					rightSyncTime = 1;
				} else if (flag % 2 == 1) {
					leftSyncTime = 1;
				}
			}
			if (leftSyncTime == 0 && rightSyncTime == 0) {
				Log.i(TAG, "智能鞋同步时间成功");
				mShoesSyncTimeCallback.isSyncTimeSucc(true);
			} else if ((leftSyncTime == 1 && rightSyncTime != -1)
					|| (rightSyncTime == 1 && leftSyncTime != -1)) {
				Log.i(TAG, "智能鞋同步时间失败");
				mShoesSyncTimeCallback.isSyncTimeSucc(false);
			}
		}
	};

	// 智能鞋历史数据获取结果
	BleDev.getHistCallback mHistCallback = new getHistCallback() {

		@Override
		public void isGetHistSuc(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			int flag = Integer.parseInt(addr.substring(16));
			if (arg0) {
				Log.i(TAG, addr + "获取历史数据成功");
				if (flag % 2 == 0) {
					rightGetHist = 0;
				} else if (flag % 2 == 1) {
					leftGetHist = 0;
				}
			} else {
				Log.i(TAG, addr + "获取历史数据失败");
				if (flag % 2 == 0) {
					rightGetHist = 1;
				} else if (flag % 2 == 1) {
					leftGetHist = 1;
				}
			}
			// if (leftGetHist == 0 && rightGetHist == 0) {
			// Log.i(TAG, "智能鞋历史数据获取成功");
			// mShoesGetHistDataCallback.isGetHistSucc(true, 0, null);
			// } else if ((leftGetHist == 1 && rightGetHist != -1)
			// || (rightGetHist == 1 && leftGetHist != -1)) {
			// Log.i(TAG, "智能鞋历史数据获取失败");
			// mShoesGetHistDataCallback.isGetHistSucc(false, 0, null);
			// }

		}
	};

	// 智能鞋压力数据获取结果
	BleDev.getPressCallback mPressCallback = new getPressCallback() {

		@Override
		public void isGetPressSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));

			if (arg0) {
				Log.i(TAG, addr + "获取压力数据成功");
				if (flag % 2 == 0) {
					rightGetPress = 0;
				} else if (flag % 2 == 1) {
					leftGetPress = 0;
				}
			} else {
				Log.i(TAG, addr + "获取压力数据失败");
				if (flag % 2 == 0) {
					rightGetPress = 1;
				} else if (flag % 2 == 1) {
					leftGetPress = 1;
				}
			}
			if (leftGetPress == 0 && rightGetPress == 0) {
				Log.i(TAG, "智能鞋压力数据获取成功");
				mShoesGetPressCallback.isGetPressSucc(true);
			} else if ((leftGetPress == 1 && rightGetPress != -1)
					|| (rightGetPress == 1 && leftGetPress != -1)) {
				Log.i(TAG, "智能鞋压力数据获取失败");
				mShoesGetPressCallback.isGetPressSucc(false);
			}
		}
	};

	// 智能鞋版本信息读取结果
	BleDev.readDevInfoCallback mDevInfoCallback = new readDevInfoCallback() {

		@Override
		public void isReadDevSuc(boolean arg0, String addr, String softVer) {
			int flag = Integer.parseInt(addr.substring(16));

			if (arg0) {
				Log.i(TAG, addr + "获取版本信息成功");
				if (flag % 2 == 0) {
					rightDevInfo = 0;
					softVerArr[0] = softVer;
				} else if (flag % 2 == 1) {
					leftDevInfo = 0;
					softVerArr[1] = softVer;
				}
				Log.i(TAG, addr + " softVer:" + softVer);
			} else {
				Log.i(TAG, addr + "获取版本信息失败");
				if (flag % 2 == 0) {
					rightDevInfo = 1;
				} else if (flag % 2 == 1) {
					leftDevInfo = 1;
				}
			}
			if (leftDevInfo == 0 && rightDevInfo == 0) {
				Log.i(TAG, "智能鞋版本信息获取成功");
				mShoesReadInfoCallback.isReadInfoSucc(true, softVerArr);
			} else if ((leftDevInfo == 1 && rightDevInfo != -1)
					|| (rightDevInfo == 1 && leftDevInfo != -1)) {
				Log.i(TAG, "智能鞋版本信息获取失败");
				mShoesReadInfoCallback.isReadInfoSucc(false, null);
			}
		}
	};

	// 智能鞋断开连接情况
	BleDev.disDevConnectCallback mDisDevConnectCallback = new disDevConnectCallback() {

		@Override
		public void isDisDevSuc(boolean arg0, String addr) {
			int flag = Integer.parseInt(addr.substring(16));
			if (arg0) {
				Log.i(TAG, addr + "断开设备成功");
				if (flag % 2 == 0) {
					rightDisCon = 0;
				} else if (flag % 2 == 1) {
					leftDisCon = 0;
				}
			} else {
				Log.i(TAG, addr + "断开设备失败");
				if (flag % 2 == 0) {
					rightDisCon = 1;
				} else if (flag % 2 == 1) {
					leftDisCon = 1;
				}
			}
			if (leftDisCon == 0 && rightDisCon == 0) {
				Log.i(TAG, "智能鞋断开连接成功");
				mShoesDisconnectCallback.isDisconnectSucc(true);
			} else if ((leftDisCon == 1 && rightDisCon != -1)
					|| (rightDisCon == 1 && leftDisCon != -1)) {
				Log.i(TAG, "智能鞋断开连接失败");
				mShoesDisconnectCallback.isDisconnectSucc(false);
			}
		}
	};

	// 断开一只鞋
	BleDev.disDevConnectCallback mDisSingleDevConnectCallback = new disDevConnectCallback() {

		@Override
		public void isDisDevSuc(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, addr + "单脚断开设备成功");
				mShoesSingleDisconnectCallback.isDisconnectSucc(true);
			} else {
				Log.i(TAG, addr + "单脚断开设备失败");
				mShoesSingleDisconnectCallback.isDisconnectSucc(false);
			}

		}
	};

	// 智能鞋震动结果
	BleDev.clickCallback mClickCallback = new clickCallback() {

		@Override
		public void isClickSuc(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (arg0) {
				mShoesClickCallback.isClickSucc(true, addr);
			}
		}
	};

	// 智能鞋电量获取结果
	BleDev.getDevBatteryCallback mGetDevBatteryCallback = new getDevBatteryCallback() {

		@Override
		public void isGetDevBatterySuc(boolean arg0, String addr, int value) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			int flag = Integer.parseInt(addr.substring(16));
			if (arg0) {
				Log.i(TAG, addr + "获取电量成功");
				if (flag % 2 == 0) {
					rightGetBattery = 0;
					rightValue = value;
				} else if (flag % 2 == 1) {
					leftGetBattery = 0;
					leftValue = value;
				}
			} else {
				Log.i(TAG, addr + "获取电量失败");
				if (flag % 2 == 0) {
					rightGetBattery = 1;
				} else if (flag % 2 == 1) {
					leftGetBattery = 1;
				}
			}
			if (leftGetBattery == 0 && rightGetBattery == 0) {
				Log.i(TAG, "智能鞋电量获取成功俩个设备电量" + leftValue + "," + rightValue);
				mShoesGetBatteryInfoCallback.isGetBatterySucc(true, leftValue,
						rightValue);
				leftGetBattery = -1;
				rightGetBattery = -1;
			} else if ((leftGetBattery == 1 && rightGetBattery != -1)
					|| (rightGetBattery == 1 && leftGetBattery != -1)) {
				Log.i(TAG, "智能鞋电量获取失败");
				mShoesGetBatteryInfoCallback.isGetBatterySucc(false, -1, -1);
			}
		}

	};

	public void rebootShoes(shoesRebCallback mShoesRebCallback) {
		this.mShoesRebCallback = mShoesRebCallback;
		left.rebootDev(mRebCallback);
		right.rebootDev(mRebCallback);
	}

	public void resetShoes(shoesResetCallback mShoesResetCallback) {
		this.mShoesResetCallback = mShoesResetCallback;
		left.resetDev(mResetCallback);
		right.resetDev(mResetCallback);
	}

	public void syncTimeShoes(shoesSyncTimeCallback mShoesSyncTimeCallback) {
		this.mShoesSyncTimeCallback = mShoesSyncTimeCallback;
		left.syncTimeDev(mSyncTimeCallback);
		right.syncTimeDev(mSyncTimeCallback);
	}

	public void getShoesPressData(shoesGetPressCallback mShoesGetPressCallback) {
		this.mShoesGetPressCallback = mShoesGetPressCallback;
		left.getPressData(mPressCallback);
		right.getPressData(mPressCallback);
	}

	public void stopShoesPressData(
			shoesStopPressDataCallback mShoesStopPressCallback) {
		this.mShoesStopPressDataCallback = mShoesStopPressCallback;
		left.stopPressData();
		right.stopPressData();
	}

	public void stopShoesPressDataNoCall() {
		left.stopPressDataNoCall();
		right.stopPressDataNoCall();
	}

	public void readShoesPressData(
			shoesReadPressDataCallback mShoesReadPressDataCallback) {
		this.mShoesReadPressDataCallback = mShoesReadPressDataCallback;
		left.readPressData();
		right.readPressData();
	}

	public void getShoesDailyData(
			shoesReadDailyDataCallback mShoesReadDailyDataCallback, String addr) {
		this.mShoesReadDailyDataCallback = mShoesReadDailyDataCallback;
		int flag = Integer.parseInt(addr.substring(16));
		if (flag == 1) {
			left.readDailyData();
		} else if (flag == 0) {
			right.readDailyData();
		}
	}

	public void getShoesDailyDataDouble(
			shoesReadDailyDataCallback mShoesReadDailyDataCallback) {
		this.mShoesReadDailyDataCallback = mShoesReadDailyDataCallback;
		left.readDailyData();
		right.readDailyData();
	}

	public void getHistData(shoesGetHistDataCallback mShoesGetHistDataCallback) {
		this.mShoesGetHistDataCallback = mShoesGetHistDataCallback;
		left.getHistData(mHistCallback);
		right.getHistData(mHistCallback);
	}

	public void getShoesInfo(shoesReadInfoCallback mShoesReadInfoCallback) {
		this.mShoesReadInfoCallback = mShoesReadInfoCallback;
		left.readDevInfo(mDevInfoCallback);
		right.readDevInfo(mDevInfoCallback);
	}

	public void upgradeShoes(shoesUpgradeCallback mShoesUpgradeCallback,
			String addr, byte[] firmData) {
		this.mShoesUpgradeCallback = mShoesUpgradeCallback;
		int flag = Integer.parseInt(addr.substring(16));
		if (flag == 1) {
			left.upgradeDev(firmData);
		} else if (flag == 0) {
			right.upgradeDev(firmData);
		}
	}

	public void disShoesConnect(shoesDisconnectCallback mShoesDisconnectCallback) {
		this.mShoesDisconnectCallback = mShoesDisconnectCallback;
		left.disConnect(mDisDevConnectCallback);
		right.disConnect(mDisDevConnectCallback);
	}

	public void disSingleShoeConnect(
			shoesDisconnectCallback mShoesSingleDisconnectCallback, String addr) {
		this.mShoesSingleDisconnectCallback = mShoesSingleDisconnectCallback;
		int flag = Integer.parseInt(addr.substring(16));
		if (flag == 1) {
			left.disConnect(mDisSingleDevConnectCallback);
		} else if (flag == 0) {
			right.disConnect(mDisSingleDevConnectCallback);
		}
	}

	public void clickShoe(shoesClickCallback mShoesClickCallback, String addr) {
		this.mShoesClickCallback = mShoesClickCallback;
		int flag = Integer.parseInt(addr.substring(16));
		if (flag == 1) {
			left.clickDev(mClickCallback);
		} else if (flag == 0) {
			right.clickDev(mClickCallback);
		}
	}

	public void getShoesBattery(
			shoesGetBatteryInfoCallback mShoesGetBatteryInfoCallback) {
		this.mShoesGetBatteryInfoCallback = mShoesGetBatteryInfoCallback;
		left.getDevBattery(mGetDevBatteryCallback);
		right.getDevBattery(mGetDevBatteryCallback);
	}
}
