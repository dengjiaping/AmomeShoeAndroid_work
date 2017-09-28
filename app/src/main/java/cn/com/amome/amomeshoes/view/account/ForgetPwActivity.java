package cn.com.amome.amomeshoes.view.account;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.Sec;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.Util;
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

/**
 * 忘记密码
 * 
 * @author css
 */
public class ForgetPwActivity extends Activity implements OnClickListener {

	private String TAG = "ForgetPwActivity";
	private TextView tv_title, tv_left, tv_code;
	private ImageView iv_left;
	private RelativeLayout rl_left;
	private String leftValue = "";
	private Context mContext;
	private EditText et_phone, et_verify, et_pw, et_aginpw;
	private String mob, verifyCode, pw, aginPw;
	private int seconds = -1;
	private Timer timer;
	private TimerTask timerTask;
	private int reqType = -1;
	private static final int MSG_SEND_SMSMSG = 0;
	private static final int MSG_GET_SMSMSG = 2;
	private static final int MSG_FORPW = 3;

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
					tv_code.setEnabled(true);
					tv_code.setText("重新获取");
				} else {
					tv_code.setText("重新获取(" + seconds + ")");
				}
				break;
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_SMSMSG:
					T.showToast(mContext, "已发送成功！", 1);
					tv_code.setEnabled(false);
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
				case MSG_FORPW:
					SpfUtil.keepPw(mContext, "");
					T.showToast(mContext, "修改成功！请重新登录！", 0);
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
					break;
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
		setContentView(R.layout.activity_account_forgetpw);
		mContext = this;
		leftValue = getIntent().getStringExtra("leftname");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("忘记密码");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		if (!TextUtils.isEmpty(leftValue)) {
			tv_left.setText(leftValue);
		}
		tv_left.setVisibility(View.INVISIBLE);
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_left.setOnClickListener(this);

		et_phone = (EditText) findViewById(R.id.et_phonenum);
		et_verify = (EditText) findViewById(R.id.et_reset_code);
		et_pw = (EditText) findViewById(R.id.et_reset_pw);
		et_aginpw = (EditText) findViewById(R.id.et_reset_pw_agin);
		tv_code = (TextView) findViewById(R.id.tv_reset_getcode);
		findViewById(R.id.iv_complete).setOnClickListener(this);
		tv_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_complete:
			reqType = MSG_FORPW;
			verifyPw();
			break;
		case R.id.rl_left:
			finish();
			break;
		// 获取验证码
		case R.id.tv_reset_getcode:
			mob = et_phone.getText().toString().trim();
			if (!TextUtils.isEmpty(mob)) {
				if (!Util.formatMob(mob)) {
					T.showToast(mContext, "手机号格式错误！", 0);
					return;
				}
				reqType = MSG_GET_SMSMSG;
				GetCode();
			} else {
				T.showToast(mContext, "请输入正确手机号啦！", 0);
			}
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
			case MSG_GET_SMSMSG:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0
							&& HttpError.judgeError(return_msg,
									ClassType.ForgetPwActivity)) {
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
			case MSG_FORPW:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0
							&& HttpError.judgeError(return_msg,
									ClassType.LoginActivity)) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_FORPW解析失败");
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
		// tv_code.setEnabled(false);
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
		if (reqType == MSG_GET_SMSMSG)
			getIdentify();
		else if (reqType == MSG_FORPW)
			changePw();

	}

	// 获取验证码
	private void getIdentify() {
		RequestParams params = new RequestParams();
		params.put("phone", mob);
		params.put("msgid", ClientConstant.FORGETPW_ID);

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_SMSMSG, params,
				ClientConstant.verification_Url);

	}

	/**
	 * 验证密码
	 */
	private void verifyPw() {
		pw = et_pw.getText().toString().trim();
		aginPw = et_aginpw.getText().toString().trim();
		verifyCode = et_verify.getText().toString().trim();
		mob = et_phone.getText().toString().trim();
		if (!TextUtils.isEmpty(pw) && !TextUtils.isEmpty(verifyCode)
				&& !TextUtils.isEmpty(aginPw) && !TextUtils.isEmpty(mob)) {
			if (pw.length() >= 6 && pw.length() <= 16) {
				if (pw.equals(aginPw)) {
					if (reqType == MSG_GET_SMSMSG)
						getIdentify();
					else if (reqType == MSG_FORPW)
						changePw();
				} else {
					T.showToast(mContext, "2次密码输入不同!", 0);
				}
			} else
				T.showToast(mContext, "请输入6-16位的密码!", 0);
		} else
			T.showToast(mContext, "输入不能为空!", 0);
	}

	/**
	 * 修改密码
	 */
	private void changePw() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.FORGETPW_TYPE);
		params.put("phone", mob);
		params.put("password", Sec.MD5Encrypt(pw));
		params.put("verify", verifyCode);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_FORPW, params,
				ClientConstant.FORGETPW_URL);
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ForgetPwActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ForgetPwActivity);
		MobclickAgent.onPause(mContext);
	}
}
