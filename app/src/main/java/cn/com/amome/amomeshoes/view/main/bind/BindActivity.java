package cn.com.amome.amomeshoes.view.main.bind;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.adapter.BindShoeListAdapter;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.BleDeviceInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BindActivity extends Activity implements OnClickListener {
	private String TAG = "BindActivity";
	private Context mContext;
	private ImageView iv_bind_scan;
	private TextView tv_title, bt_scan, tv_skip;
	private ImageView iv_left;
	private static final int MSG_CHECK_BLEDEV = 1;
	private static final int MSG_BIND_ALREADY = 2;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_CHECK_BLEDEV:
					String str = (String) msg.obj;
					if (str.equals("Not bound")) {
						bt_scan.setVisibility(View.GONE);
						tv_skip.setVisibility(View.VISIBLE);
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								startActivity(new Intent(mContext,
										BindShockActivity.class));
								finish();
							}
						};
						Timer timer = new Timer();
						timer.schedule(task, 3000);
					} else {
						T.showToast(mContext, "没有绑定智能鞋", 0);
						finish();
					}
					break;
				}
				break;
			case MSG_BIND_ALREADY:
				T.showToast(mContext, "该智能鞋已经被绑定", 0);
				finish();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		SpfUtil.keepDeviceToShoeLeftTmp(mContext, "");
		SpfUtil.keepDeviceToShoeRightTmp(mContext, "");
		setContentView(R.layout.activity_bind_main);
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_skip = (TextView) findViewById(R.id.tv_skip);
		bt_scan = (TextView) findViewById(R.id.bt_scan);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_bind_scan = (ImageView) findViewById(R.id.iv_bind_scan);
		tv_title.setText("绑定");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.blue));
		iv_left.setImageResource(R.drawable.ic_back_blue);
		findViewById(R.id.bt_scan).setOnClickListener(this);
		findViewById(R.id.rl_left).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.bt_scan:
			startActivity(new Intent(mContext, SweepShoesActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!SpfUtil.readDeviceToShoeLeftTmp(mContext).equals("")
				&& SpfUtil.readDeviceToShoeRightTmp(mContext).equals("")) {
			iv_bind_scan.setImageResource(R.drawable.bind_scan_left);
			bt_scan.setText("点我开始扫描另一只");
		}
		if (!SpfUtil.readDeviceToShoeRightTmp(mContext).equals("")
				&& SpfUtil.readDeviceToShoeLeftTmp(mContext).equals("")) {
			iv_bind_scan.setImageResource(R.drawable.bind_scan_right);
			bt_scan.setText("点我开始扫描另一只");
		}
		if (!SpfUtil.readDeviceToShoeLeftTmp(mContext).equals("")
				&& !SpfUtil.readDeviceToShoeRightTmp(mContext).equals("")) {
			iv_bind_scan.setImageResource(R.drawable.bind_scan_double);
			// 检测蓝牙是否未绑定
			checkBleIsUsable();
		} else {
			Log.i(TAG, "还未完成扫码");
		}
	}

	/**
	 * 检查蓝牙设备是否被绑定
	 */
	private void checkBleIsUsable() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.BINDUSABLE_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("lble", SpfUtil.readDeviceToShoeLeftTmp(mContext));
		params.put("rble", SpfUtil.readDeviceToShoeRightTmp(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_CHECK_BLEDEV, params,
				ClientConstant.BLEDEVICE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_CHECK_BLEDEV:
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
					} else if (return_code == 1) {
						if (return_msg.equals("Bound already")) {
							msg.what = MSG_BIND_ALREADY;
							mHandler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_CHECK_BLEDEV解析失败");
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
