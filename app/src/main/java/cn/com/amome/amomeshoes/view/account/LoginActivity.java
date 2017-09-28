package cn.com.amome.amomeshoes.view.account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.BleDeviceInfo;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.GoodsInfo;
import cn.com.amome.amomeshoes.model.OrderInfo;
import cn.com.amome.amomeshoes.model.UpgradeApp;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.Sec;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.Util;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.main.MainFragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends Activity implements OnClickListener {

	private String TAG = "LoginActivity";
	private EditText et_phonenum, et_pw;
	private String pw, phone;
	private Context mContext;
	private static final int MSG_APPLOGIN = 0;
	private static final int MSG_GET_APPINFO = 1;
	private static final int MSG_GET_BLEDEV = 2;
	private static final int MSG_GETSOFTWAREGOODSLIST = 3;
	private static final int MSG_VERIFYPER = 4;
	private static final int MSG_ADD_SOFT_FREE = 5;
	private List<BleDeviceInfo> DeviceList;
	private List<UpgradeApp> upgradeAppList;
	private List<GoodsInfo> goodsList;
	private GoodsInfo goodsInfo;
	private UpgradeApp upgradeApp;
	private String downloadUrl;
	private int currVer_code;// 本地获取的版本code
	private String currVer_name;// 本地获取的版本名
	private int webVer_code;// web获取的版本code
	private String webVer_name;// web获取的版本名
	private String must_update = "0";// 0不强制更新,1强制更新
	private String apkName;
	private String path;
	private Gson gson = new Gson();
	private List<OrderInfo> orderInfoList;
	private boolean softFree = false;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_APPLOGIN:
					String re = (String) msg.obj; // 100091
					MobclickAgent.onProfileSignIn(re);
					SpfUtil.keepUserId(mContext, re);
					SpfUtil.keepPw(mContext, pw);
					SpfUtil.keepPhone(mContext, phone);
					SpfUtil.keepMyAvatarUrl(mContext, "");
					getApp();
					break;
				case MSG_GET_APPINFO:
					String json = (String) msg.obj;
					if (json.equals("[{}]")) {
						getBindDevice();
					} else if (TextUtils.isEmpty(json)) {
						T.showToast(mContext, "服务器繁忙", 0);
					} else {
						upgradeAppList = gson.fromJson(json,
								new TypeToken<List<UpgradeApp>>() {
								}.getType());
						if (upgradeAppList != null && upgradeAppList.size() > 0) {
							upgradeApp = upgradeAppList.get(0);
							downloadUrl = upgradeApp.firmware_path;
							String version = upgradeApp.newest_verison;// v1.0.4_5
							try {
								String[] versionArr = version.split("_");// [0]=v1.0.4
																			// [1]=5
								webVer_name = versionArr[0]; // v1.0.4
								webVer_code = Integer.parseInt(versionArr[1]); // 5
							} catch (Exception e) {
								// TODO: handle exception
							}
							must_update = upgradeApp.urgent;
							apkName = "Amome" + webVer_name + ".apk"; // Amomev1.0.4.apk
							if (currVer_code < webVer_code) {
								updateApp();
							} else {
								getBindDevice();
							}
						} else {
							getBindDevice();
						}
					}
					break;
				case MSG_GETSOFTWAREGOODSLIST:
					String goodsStr = (String) msg.obj;
					if (goodsStr.equals("[{}]")) {
						T.showToast(mContext, "服务异常", 0);
					} else {
						goodsList = gson.fromJson(goodsStr,
								new TypeToken<List<GoodsInfo>>() {
								}.getType());
						if (goodsList != null && goodsList.size() > 0) {
							goodsInfo = goodsList.get(0);
							// initData(goodsInfo);
							if (goodsInfo.price.equals("0")) {
								Log.i(TAG, "软件免费中");
								softFree = true;
							} else {
								Log.i(TAG, "软件付费中");
							}
							getSoftwareOrder();
							DialogUtil.hideProgressDialog();
						}
					}
					break;
				case MSG_GET_BLEDEV:
					String deviceJson = (String) msg.obj;
					if (deviceJson.equals("[{}]")) {
						Log.i(TAG, "没有绑定设备，这是一个新帐号，需要清空坐站走");
						SpfUtil.keepDeviceToShoeLeft(mContext, "");
						SpfUtil.keepDeviceToShoeRight(mContext, "");
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
					} else {
						DeviceList = gson.fromJson(deviceJson,
								new TypeToken<List<BleDeviceInfo>>() {
								}.getType());
						if (DeviceList != null && DeviceList.size() > 0) {
							for (int i = 0; i < DeviceList.size(); i++) {
								if (DeviceList.get(i).state.equals("1")) {
									Log.i(TAG, "左脚：" + DeviceList.get(i).lble);
									Log.i(TAG, "右脚：" + DeviceList.get(i).rble);
									if ((!DeviceList.get(i).lble.equals(SpfUtil
											.readDeviceToShoeLeft(mContext)))
											|| (!DeviceList.get(i).rble
													.equals(SpfUtil
															.readDeviceToShoeRight(mContext)))) {
										Log.i(TAG, "绑定的蓝牙不是本地存的蓝牙，需要清空坐站走");
										SpfUtil.keepDeviceToShoeLeft(mContext,
												DeviceList.get(i).lble);
										SpfUtil.keepDeviceToShoeRight(mContext,
												DeviceList.get(i).rble);
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
									} else {
										Log.i(TAG, "此蓝牙是本地蓝牙，即将判断是不是今天");
										Date date = new Date();
										date = new Date(date.getTime());
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
												"yyyy-MM-dd");// 初始化Formatter的转换格式。
										simpleDateFormat.setTimeZone(TimeZone
												.getTimeZone("GMT+00:00"));
										String dateString = simpleDateFormat
												.format(date);
										if (dateString.equals(SpfUtil
												.readTodayDate(mContext))) {
											Log.i(TAG, "本地存的等于今天,不清空坐站走");
										} else {
											Log.i(TAG, "本地存的不等于今天,需要清空坐站走数据");
											SpfUtil.keepTodayDate(mContext,
													dateString);
											SpfUtil.keepLeftSitTime(mContext,
													"");
											SpfUtil.keepLeftStandTime(mContext,
													"");
											SpfUtil.keepLeftWalkTime(mContext,
													"");
											SpfUtil.keepLeftSteps(mContext, "");
											SpfUtil.keepRightSitTime(mContext,
													"");
											SpfUtil.keepRightStandTime(
													mContext, "");
											SpfUtil.keepRightWalkTime(mContext,
													"");
											SpfUtil.keepRightSteps(mContext, "");
											SpfUtil.keepTodaySitTime(mContext,
													"");
											SpfUtil.keepTodayStandTime(
													mContext, "");
											SpfUtil.keepTodayWalkTime(mContext,
													"");
											SpfUtil.keepTodaySteps(mContext, "");
											SpfUtil.keepTime(mContext, "");
										}
									}
									break;
								}
								if (i == DeviceList.size() - 1
										&& DeviceList.get(i).state.equals("0")) {
									Log.i(TAG, "这个号没绑设备，清空蓝牙地址，但不清空坐站走");
									SpfUtil.keepDeviceToShoeLeft(mContext, "");
									SpfUtil.keepDeviceToShoeRight(mContext, "");
								}
							}
						}
					}
					getSoftwareGoodsList();
					break;
				case MSG_VERIFYPER:
					String orderJson = (String) msg.obj;
					if (orderJson.equals("[{}]")) {
						if (softFree) {
							// 推送一个订单
							addSoftFreeOrder();
						} else {
							startActivity(new Intent(mContext,
									ChoosePayActivity.class));
							finish();
						}
					} else {
						orderInfoList = gson.fromJson(orderJson,
								new TypeToken<List<OrderInfo>>() {
								}.getType());
						if (orderInfoList != null && orderInfoList.size() > 0) {
							verifyOrder();
						}
					}
					break;
				case MSG_ADD_SOFT_FREE:
					String softStr = (String) msg.obj;
					if (softStr.contains("SUCCESS")) {
						Log.i(TAG, "添加免费订单成功");
						startActivity(new Intent(mContext,
								MainFragmentActivity.class));
					} else {
						Log.i(TAG, "添加免费订单失败");
					}
					finish();
					break;
				default:
					break;
				}
				break;
			case ClientConstant.HANDLER_FAILED:
				switch (msg.arg1) {
				// case MSG_GET_USERINFO:
				// getSoftwareOrder();
				// break;
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		currVer_code = Environments.getVersionCode(mContext);
		currVer_name = Environments.getVersionName(mContext);
		Log.i(TAG, "currVer_code" + currVer_code);
		Log.i(TAG, "currVer_name" + currVer_name);
		setContentView(R.layout.activity_account_login);
		initUI();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_login:
			phone = et_phonenum.getText().toString().trim();
			pw = et_pw.getText().toString().trim();
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pw)) {
				if (!Util.formatMob(phone)) {
					T.showToast(mContext, "手机号格式错误！", 0);
					return;
				} else
					loginApp();
			} else
				T.showToast(mContext, "输入不能为空！", 0);
			break;
		case R.id.iv_register_now:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.iv_forgetpw:
			startActivity(new Intent(LoginActivity.this, ForgetPwActivity.class)
					.putExtra("leftname", "登录"));
			break;
		// case R.id.iv_weichat: 该版本屏蔽！！
		// L.i("111", "=======开始准备授权============");
		// // WeiXinPresenter wp = new
		// // WeiXinPresenter(getApplicationContext());
		// // wp.loginWeixin(mHandler);
		// break;
		// case R.id.iv_qq:
		// // ShareSDK.initSDK(mContext);
		// // Platform qq = ShareSDK.getPlatform(QQ.NAME);
		// // authorize(qq);
		// break;
		// case R.id.iv_weibo:
		// // ShareSDK.initSDK(mContext);
		// // Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
		// // 移除授权
		// // weibo.removeAccount(true);
		// // authorize(sina);
		// break;

		default:
			break;
		}
	}

	public void initUI() {
		findViewById(R.id.iv_login).setOnClickListener(this);
		findViewById(R.id.iv_register_now).setOnClickListener(this);
		findViewById(R.id.iv_forgetpw).setOnClickListener(this);
		// findViewById(R.id.iv_weichat).setOnClickListener(this); //该版本屏蔽
		// findViewById(R.id.iv_qq).setOnClickListener(this);
		// findViewById(R.id.iv_weibo).setOnClickListener(this);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		et_pw = (EditText) findViewById(R.id.et_password);
		phone = SpfUtil.readPhone(mContext);
		pw = SpfUtil.readPw(mContext);
		if (!TextUtils.isEmpty(phone))
			et_phonenum.setText(phone);
		if (!TextUtils.isEmpty(pw))
			et_pw.setText(pw);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_APPLOGIN:
				DialogUtil.hideProgressDialog();
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					int return_code = obj.getInt("return_code");
					String return_msg = null;

					Message msg = Message.obtain();
					if (return_code == 0) {
						JSONArray jsonArray = obj.getJSONArray("return_msg");
						JSONObject jsonObj = jsonArray.getJSONObject(0);
						return_msg = jsonObj.getString("useid");
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}

					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_APPLOGIN解析失败");
				}
				break;

			case MSG_VERIFYPER:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						Log.i(TAG, type + " : returncode = 0 , "
								+ "return_msg = " + return_msg);
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_VERIFYPER解析失败");
				}
				break;
			case MSG_GET_APPINFO:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_APPINFO解析失败");
				}
				break;
			case MSG_GET_BLEDEV:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						Log.i(TAG, type + " : returncode = 0 , "
								+ "return_msg = " + return_msg);
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_BLEDEV解析失败");
				}
				break;
			case MSG_GETSOFTWAREGOODSLIST:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0
							&& HttpError.judgeError(return_msg,
									ClassType.PayActivity)) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GETSOFTWAREGOODSLIST解析失败");

				}
				break;
			case MSG_ADD_SOFT_FREE:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);
					} else if (return_code == 1 && return_msg.equals("-0x200")) {
						msg.what = ClientConstant.HANDLER_FAILED;
						msg.arg1 = 0;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_ADD_SOFT_FREE解析失败");

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

	// /**
	// * 获取个人信息
	// */
	// private void getInfo() {
	// Log.i(TAG, "获取个人信息");
	// // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
	// RequestParams params = new RequestParams();
	// params.put("calltype", ClientConstant.GETINFOMATION);
	// params.put("useid", SpfUtil.readUserId(mContext));
	// PostAsyncTask postTask = new PostAsyncTask(mHandler);
	// postTask.startAsyncTask(mContext, callback, MSG_GET_USERINFO, params,
	// ClientConstant.USERINFO_URL);
	//
	// }

	/**
	 * 获取绑定的智能鞋
	 */
	private void getBindDevice() {
		Log.i(TAG, "获取绑定的设备");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETBLEDEV_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
	}

	/**
	 * APP内的登录
	 */
	private void loginApp() {
		Log.i(TAG, "登录");
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("password", Sec.MD5Encrypt(pw));
		params.put("calltype", ClientConstant.LOGIN_PHONE_TYPE);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_APPLOGIN, params,
				ClientConstant.LOGIN_URL);

	}

	/**
	 * 获取商品数据列表
	 */
	private void getSoftwareGoodsList() {
		L.v("", "==getsoftwareGoodsList===");
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETGOODS_SOFTWARE_TYPE);// 0x01是所有商品
																		// 0x02根据服务权限id获取商品
		params.put("goods_id", "6");
		params.put("body", "魔秘");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GETSOFTWAREGOODSLIST,
				params, ClientConstant.MSG_GETSOFTWAREGOODSLIST_URL);
	}

	/**
	 * 获取权限订单
	 */
	private void getSoftwareOrder() {
		Log.i(TAG, "获取权限订单");
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.SOFTWAREORDER_TYPE);
		params.put("goods_id", "6");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_VERIFYPER, params,
				ClientConstant.SOFTWAREORDER_URL);
	}

	/**
	 * 添加软件成功支付的订单(免费)
	 */
	private void addSoftFreeOrder() {
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.SOFT_FREE_TYPE);
		params.put("goods_id", "6");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_SOFT_FREE, params,
				ClientConstant.ADDORDER_URL);

	}

	/**
	 * 验证订单
	 */
	private void verifyOrder() {
		Log.i(TAG, "verifyOrder");
		for (int i = 0; i < orderInfoList.size(); i++) {
			OrderInfo orderInfo = orderInfoList.get(i);
			if (orderInfo.state.equals("SUCCESS")) {
				startActivity(new Intent(mContext, MainFragmentActivity.class));
				finish();
				return;
			} else if (i == orderInfoList.size() - 1
					&& !orderInfo.state.equals("SUCCESS")) {// 有订单但未支付成功
				startActivity(new Intent(mContext, ChoosePayActivity.class));
				finish();
			}
		}
	}

	/**
	 * 检查版本
	 */
	private void getApp() {
		Log.i(TAG, "检查版本");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETAPPINFO);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_APPINFO, params,
				ClientConstant.APP_DOWNLOAD);
	}

	private void updateApp() {
		if (must_update.equals("0")) {// 0:不强制更新
			if (webVer_code > currVer_code) {// 比本地大更新
				DialogUtil.showAlertDialog(mContext, "有新版本app", "是否更新？", "确定",
						"取消", new OnAlertViewClickListener() {
							@Override
							public void confirm() {
								if (Environments.getNetworkState(mContext) == Environments.Wifi_State) {
									downloadApp(downloadUrl);
								} else if (Environments
										.getNetworkState(mContext) == Environments.Mobile_State) {
									DialogUtil.showAlertDialog(mContext, "",
											"确定使用手机流量更新app？", "确定", "取消",
											new OnAlertViewClickListener() {
												@Override
												public void confirm() {
													downloadApp(downloadUrl);
												}

												@Override
												public void cancel() {
													getSoftwareGoodsList();
												}
											});
								}
							}

							@Override
							public void cancel() {
								getSoftwareGoodsList();
							}
						});
			}
		} else {// 强制更新
			if (webVer_code > currVer_code) {// 比本地大更新
				DialogUtil.showAlertDialog(mContext, "更新新版本app",
						"有重要内容更新，请务必更新app！否则影响使用!", "确定", "取消",
						new OnAlertViewClickListener() {
							@Override
							public void confirm() {
								if (Environments.getNetworkState(mContext) == Environments.Wifi_State) {
									downloadApp(downloadUrl);
								} else if (Environments
										.getNetworkState(mContext) == Environments.Mobile_State) {
									DialogUtil.showAlertDialog(mContext, "",
											"确定使用手机流量更新app？", "确定", "取消",
											new OnAlertViewClickListener() {
												@Override
												public void confirm() {
													downloadApp(downloadUrl);
												}

												@Override
												public void cancel() {
													finish();
												}
											});
								}

							}

							@Override
							public void cancel() {
								finish();
							}
						});
			}
		}
	}

	/**
	 * 下载app
	 * 
	 * @param down_url
	 */
	private void downloadApp(String down_url) {
		path = Environments.getSDPath(mContext) + "/" + apkName;
		try {
			AsynHttpDowanloadFile.downloadApkFile("http://" + down_url, path,
					mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.LoginActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.LoginActivity);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}