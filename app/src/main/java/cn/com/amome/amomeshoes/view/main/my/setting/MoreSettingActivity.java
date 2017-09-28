package cn.com.amome.amomeshoes.view.main.my.setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SaveImageViewPic;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesStopPressDataCallback;
import cn.com.amome.amomeshoes.zxing.encoding.EncodingHandler;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MoreSettingActivity extends Activity implements OnClickListener {
	private String TAG = "MoreSettingActivity";
	private RadioGroup group;
	private RadioButton radio0, radio1;
	private EditText edt1, edt2;
	private Context mContext;
	private static final int MSG_BIND_BLEDEV = 1;
	private static final int MSG_BIND_ALREADY = 3;
	private static final int MSG_BIND_OTHERS = 2;

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
						SpfUtil.keepDeviceToShoeLeft(mContext, edt1.getText()
								.toString().trim());
						SpfUtil.keepDeviceToShoeRight(mContext, edt2.getText()
								.toString().trim());
					}
					break;
				}
				break;
			case MSG_BIND_OTHERS:
				T.showToast(mContext, "智能鞋已被其他人绑定", 0);
				Log.i(TAG, "智能鞋已被其他人绑定");
				break;
			case MSG_BIND_ALREADY:
				T.showToast(mContext, "智能鞋已被绑定", 0);
				Log.i(TAG, "智能鞋已被绑定");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_setting_more);
		initView();
	}

	private void initView() {
		edt1 = (EditText) this.findViewById(R.id.et_left_addr);
		edt2 = (EditText) this.findViewById(R.id.et_right_addr);
		group = (RadioGroup) this.findViewById(R.id.radioGroup1);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		findViewById(R.id.bt_bind).setOnClickListener(this);
		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_bind:
			addBindDevice();
			break;
		case R.id.button1:
			try {
				AmomeApp.bleShoes
						.stopShoesPressData(shoesStopPressDataCallback); // 停止获取压力数据
			} catch (Exception e) {
				// TODO: handle exception
				Log.i(TAG, "停止获取压力数据时崩了，大概是还没建立连接");
				e.printStackTrace();
			}
			break;
		}
	}
	

	BleShoes.shoesStopPressDataCallback shoesStopPressDataCallback = new shoesStopPressDataCallback() {

		@Override
		public void isStopPressSuc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
			} else {
			}
		}
	};

	/**
	 * 服务端添加
	 */
	private void addBindDevice() {
		Log.i(TAG, "被调用");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.BINDBLEDEV_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("name", "我的智能鞋");
		params.put("lble", edt1.getText().toString().trim());
		params.put("rble", edt2.getText().toString().trim());
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_BIND_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
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
						} else if (return_msg.equals("Bound by others")) {
							msg.what = MSG_BIND_OTHERS;
							mHandler.sendMessage(msg);
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

}
