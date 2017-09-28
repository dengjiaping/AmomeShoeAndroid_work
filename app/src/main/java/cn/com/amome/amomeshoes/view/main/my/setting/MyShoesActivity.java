package cn.com.amome.amomeshoes.view.main.my.setting;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.BindShoeListAdapter;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.BleDeviceInfo;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SaveImageViewPic;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.Util;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.view.main.bind.BindActivity;
import cn.com.amome.amomeshoes.view.main.bind.PrepareScanActivity;
import cn.com.amome.amomeshoes.view.main.bind.SweepShoesActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoesDelReasonActivity;
import cn.com.amome.amomeshoes.zxing.encoding.EncodingHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyShoesActivity extends Activity implements OnClickListener {
	private String TAG = "MyShoesActivity";
	private Context mContext;
	private TextView tv_title, tv_add_shoes;
	private ImageView iv_left;
	private SwipeMenuListView lv_bind_list;
	private static final int MSG_GET_BLEDEV = 0;
	private static final int MSG_CHANGE_BLEDEV = 1;
	private static final int MSG_CHANGE = 2;
	private static final int MSG_LONG_CLICK = 3;
	private final int REQ_ADD_SHOE = 0x13;
	private int state = -1;
	private int selectPosition = -1;
	private List<BleDeviceInfo> DeviceList;
	private Gson gson = new Gson();
	private BindShoeListAdapter shoeAdapter;
	private String contentStringLeft = "";
	private String contentStringRight = "";
	private Dialog mIsSaveImageDialog;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_BLEDEV:
					String deviceJson = (String) msg.obj;
					if (deviceJson.equals("[{}]")) {
						T.showToast(mContext, "没有绑定智能鞋", 0);
						tv_add_shoes.setVisibility(View.VISIBLE);
						DialogUtil.hideProgressDialog();
					} else {
						DeviceList = gson.fromJson(deviceJson,
								new TypeToken<List<BleDeviceInfo>>() {
								}.getType());
						Log.i(TAG, DeviceList.size() + "list长度");
						if (DeviceList != null && DeviceList.size() > 0) {
							for (int i = 0; i < DeviceList.size(); i++) {
								shoeAdapter = new BindShoeListAdapter(mContext,
										DeviceList, mHandler);
								lv_bind_list.setAdapter(shoeAdapter);
							}
						}
						DialogUtil.hideProgressDialog();
					}
					break;
				case MSG_CHANGE_BLEDEV:
					String re = (String) msg.obj;
					if (re.equals("0x00")) {
						DialogUtil.hideProgressDialog();
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								shoeAdapter.notifyDataSetChanged();
							}
						}, 100);
						try {
							AmomeApp.bleShoes
									.disShoesConnect(shoesDisconnectCallback);
						} catch (Exception e) {
							// TODO: handle exception
							Log.i(TAG, "断开连接时崩了，大概是还没建立连接");
							e.printStackTrace();
						}
						if (state == 0) {
							Log.i(TAG, "解绑成功");
							T.showToast(mContext, "解绑成功", 0);
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
							Log.i(TAG, "切换成功");
							T.showToast(mContext, "切换成功", 0);
							SpfUtil.keepDeviceToShoeLeft(mContext,
									DeviceList.get(selectPosition).lble);
							SpfUtil.keepDeviceToShoeRight(mContext,
									DeviceList.get(selectPosition).rble);
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
						}
						AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
						AmomeApp.exercise_flag = false;
						AmomeApp.yesterhist_flag = true;
						AmomeApp.bleShoes = null;
						AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
					}
					break;
				}
				break;
			case MSG_CHANGE:
				selectPosition = msg.arg1;
				state = msg.arg2;
				changeBindDevice(selectPosition, state);
				break;
			case MSG_LONG_CLICK:
				contentStringLeft = DeviceList.get(msg.arg1).lble;
				contentStringRight = DeviceList.get(msg.arg1).rble;
				saveImageDialog();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_shoes);
		mContext = this;
		initView();
		DialogUtil.showCancelProgressDialog(mContext, "", "加载中", true, true);
		getBindDevice();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("我的智能鞋");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		tv_add_shoes = (TextView) findViewById(R.id.tv_add_shoes);
		lv_bind_list = (SwipeMenuListView) findViewById(R.id.lv_bind_list);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(Util.dip2px(mContext, 90));
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);
			}
		};
		lv_bind_list.setMenuCreator(creator);
		lv_bind_list.setOnItemClickListener(new MyListViewItemOnclckListener());
		lv_bind_list.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				// case 0:// delete
				// DialogUtil.showCancelProgressDialog(mContext, "", "解绑中",
				// true, true);
				// selectorPosition = position;
				// unbindDevice(selectorPosition);
				// break;
				}
			}
		});
		findViewById(R.id.tv_add_shoes).setOnClickListener(this);
		findViewById(R.id.rl_left).setOnClickListener(this);
	}

	class MyListViewItemOnclckListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TextView tv = (TextView) arg1.findViewById(R.id.tv_bind_name);
			// if (DeviceList.get(position).getState().equals("1")) {
			// Log.i(TAG, "需要取消");
			// DeviceList.get(position).setState("0");
			// } else {
			// for (int i = 0; i < DeviceList.size(); i++) {
			// DeviceList.get(i).setState("0");
			// }
			// Log.i(TAG, "需要添加");
			// DeviceList.get(position).setState("1");
			// }
			// for (int i = 0; i < DeviceList.size(); i++) {
			// if (DeviceList.get(position).getState().equals("1")) {
			// tv.setTextColor(Color.YELLOW);
			// } else {
			// tv.setTextColor(Color.BLACK);
			// }
			// }
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_add_shoes:
			Intent intent = new Intent(mContext, BindActivity.class);
			startActivityForResult(intent, REQ_ADD_SHOE);
			break;
		}
	}

	private void getBindDevice() {
		Log.i(TAG, "getBindDevice");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETBLEDEV_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
	}

	private void changeBindDevice(int position, int state) {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.CHANGEBLEDEV_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("lble", DeviceList.get(position).lble);
		params.put("rble", DeviceList.get(position).rble);
		params.put("state", state + "");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_CHANGE_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {
		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
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
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_BLEDEV解析失败");
				}
				break;

			case MSG_CHANGE_BLEDEV:
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
					}

					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_CHANGE_BLEDEV解析失败");
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
		if (!SpfUtil.readDeviceToShoeLeftTmp(mContext).equals("")
				&& !SpfUtil.readDeviceToShoeRightTmp(mContext).equals("")) {
			getBindDevice();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

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
	 * 长按图片保存事件处理
	 */
	private void saveImageDialog() {
		// DialogUtil.cancelDialog(mIsSaveImageDialog);
		mIsSaveImageDialog = DialogUtil.createMenuDialog(mContext, "保存", "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogUtil.cancelDialog(mIsSaveImageDialog);
						saveImage();
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogUtil.cancelDialog(mIsSaveImageDialog);
					}
				}, new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});
		mIsSaveImageDialog.show();
	}

	/**
	 * 将字符串转为二维码并保存
	 */
	private void saveImage() {
		if (!contentStringLeft.equals("")) {
			// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			Bitmap qrCodeBitmap_left = EncodingHandler.createQRCode(
					contentStringLeft, 350);
			if (qrCodeBitmap_left != null)
				SaveImageViewPic.saveBitmap(mContext, qrCodeBitmap_left,
						Environment.getExternalStorageDirectory()
								+ "/Amome/image/", "左脚" + contentStringLeft
								+ ".png");
			else
				T.showToast(mContext, "解析错误", 0);
		} else {
			T.showToast(mContext, "Text can not be empty", 0);
		}
		if (!contentStringRight.equals("")) {
			// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
			Bitmap qrCodeBitmap_right = EncodingHandler.createQRCode(
					contentStringRight, 350);
			if (qrCodeBitmap_right != null)
				SaveImageViewPic.saveBitmap(mContext, qrCodeBitmap_right,
						Environment.getExternalStorageDirectory()
								+ "/Amome/image/", "右脚" + contentStringRight
								+ ".png");
			else
				T.showToast(mContext, "解析错误", 0);
			T.showToast(mContext,
					"二维码已保存至" + Environment.getExternalStorageDirectory()
							+ "/Amome/image/", 0);
		} else {
			T.showToast(mContext, "Text can not be empty", 0);
		}
	}

}
