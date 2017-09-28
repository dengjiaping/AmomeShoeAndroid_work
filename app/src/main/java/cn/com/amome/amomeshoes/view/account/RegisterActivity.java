package cn.com.amome.amomeshoes.view.account;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.Sec;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.Util;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;
import com.umeng.analytics.MobclickAgent;

/**
 * 注册
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private String TAG = "RegisterActivity";
	private TextView tv_title;
	private ImageView iv_left;
	private RelativeLayout rl_left;
	private Context mContext;
	private TextView tv_reister_getcode;
	private EditText et_phone, et_verify, et_pw;
	private String mob, verifyCode, pw;
	private int seconds = -1;
	private Timer timer;
	private TimerTask timerTask;
	private static final int MSG_SEND_SMSMSG = 0;
	private static final int MSG_GET_SMSMSG = 1;
	private static final int MSG_REGISTER = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SEND_SMSMSG:// 获取验证码倒计时
				if (seconds <= 0) {
					seconds = -1;
					timer.cancel();
					timerTask.cancel();
					timer = null;
					timerTask = null;
					tv_reister_getcode.setEnabled(true);
					tv_reister_getcode.setText("重新获取");
				} else {
					tv_reister_getcode.setText("重新获取(" + seconds + ")");
				}
				break;
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_SMSMSG:// 获取验证码
					T.showToast(mContext, "已发送成功！", 1);
					tv_reister_getcode.setEnabled(false);
					seconds = 60;
					timer = new Timer();
					timerTask = new TimerTask() {
						@Override
						public void run() {
							seconds--;
							Message msg = Message.obtain();
							msg.what = MSG_SEND_SMSMSG;
							mHandler.sendMessage(msg);
						}
					};
					if (seconds > 0) {
						timer.schedule(timerTask, 0, 1000);
					}
					break;
				case MSG_REGISTER:// 注册
					SpfUtil.keepPhone(mContext, mob);
					SpfUtil.keepPw(mContext, pw);
					T.showToast(mContext, "注册成功！请登录！", 0);
					finish();
					break;
				}
				break;
			case ClientConstant.HANDLER_FAILED:
				String error = (String) msg.obj;
				if (error.equals(HttpError.CertificateErrorStr)
						|| error.equals(HttpError.OverdueTokenStr)) {
					T.showToast(mContext, "操作超时，请重新操作！", 0);
				} else
					T.showToast(mContext, error, 0);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_register);
		mContext = this;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("注册");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.blue));
		iv_left.setImageResource(R.drawable.ic_back_blue);
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_left.setOnClickListener(this);

		tv_reister_getcode = (TextView) findViewById(R.id.tv_reister_getcode);
		findViewById(R.id.tv_login_now).setOnClickListener(this);
		findViewById(R.id.iv_register_complete).setOnClickListener(this);
		findViewById(R.id.tv_sla).setOnClickListener(this);
		tv_reister_getcode.setOnClickListener(this);
		et_phone = (EditText) findViewById(R.id.et_phonenum);
		et_verify = (EditText) findViewById(R.id.et_reister_code);
		et_pw = (EditText) findViewById(R.id.et_reister_pw);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_login_now:
			finish();
			break;
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_sla:
			Intent intent = new Intent(mContext, RegProtoActivity.class);
			intent.putExtra("title", "魔秘网络服务协议");
			startActivity(intent);
			break;
		// 获取验证码
		case R.id.tv_reister_getcode:
			mob = et_phone.getText().toString().trim();
			if (!TextUtils.isEmpty(mob)) {
				if (!Util.formatMob(mob)) {
					T.showToast(mContext, "手机号格式错误！", 0);
					return;
				}
				GetCode();
			} else {
				T.showToast(mContext, "请输入手机号啦！", 0);
			}
			break;
		case R.id.iv_register_complete:
			verifyPw();
			break;

		default:
			break;
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {

			case MSG_REGISTER:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0
							&& HttpError.judgeError(return_msg,
									ClassType.RegisterActivity)) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_REGISTER解析失败");
				}
				break;
			case MSG_GET_SMSMSG:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0
							&& HttpError.judgeError(return_msg,
									ClassType.RegisterActivity)) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);

					}

				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_SMSMSG解析失败");
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onHttpPostFailure(int type, int arg0, Header[] arg1,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 获取验证码
	 */
	private void GetCode() {
		L.i("", "获取验证码");
		// tv_reister_getcode.setEnabled(false);
		// seconds = 60;
		// timer = new Timer();
		// timerTask = new TimerTask() {
		// @Override
		// public void run() {
		// seconds--;
		// Message msg = Message.obtain();
		// msg.what = MSG_SEND_SMSMSG;
		// mHandler.sendMessage(msg);
		// }
		// };
		// if (seconds > 0) {
		// timer.schedule(timerTask, 0, 1000);
		// }
		getIdentify();
	}

	/**
	 * 验证密码
	 */
	private void verifyPw() {
		pw = et_pw.getText().toString().trim();
		verifyCode = et_verify.getText().toString().trim();
		mob = et_phone.getText().toString().trim();
		if (!TextUtils.isEmpty(pw) && !TextUtils.isEmpty(verifyCode)
				&& !TextUtils.isEmpty(mob)) {
			if (pw.length() >= 6 && pw.length() <= 16) {
				if (!Util.formatMob(mob)) {
					T.showToast(mContext, "手机号格式错误！", 0);
				} else
					register();
			} else
				T.showToast(mContext, "请输入6-16位的密码", 0);
		} else
			T.showToast(mContext, "输入不能为空", 0);
	}

	/**
	 * 获取验证码
	 */
	private void getIdentify() {
		L.i("", "===getIdentify===");
		RequestParams params = new RequestParams();
		params.put("phone", mob);
		params.put("msgid", ClientConstant.REGISTER_ID);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_SMSMSG, params,
				ClientConstant.verification_Url);
	}

	/**
	 * 注册
	 */
	private void register() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.REGISTER_TYPE);
		params.put("phone", mob);
		params.put("password", Sec.MD5Encrypt(pw));
		params.put("verify", verifyCode);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_REGISTER, params,
				ClientConstant.REGISTER_URL);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ClassType.RegisterActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.RegisterActivity);
		MobclickAgent.onPause(mContext);
	}
}
