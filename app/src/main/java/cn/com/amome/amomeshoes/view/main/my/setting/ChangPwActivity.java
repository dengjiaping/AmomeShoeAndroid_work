package cn.com.amome.amomeshoes.view.main.my.setting;

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
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.Sec;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.account.LoginActivity;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 我-设置-账户管理-修改密码
 * 
 * @author css
 *
 */
public class ChangPwActivity extends Activity implements OnClickListener {

	private String TAG = "ChangPwActivity";
	private TextView tv_title, tv_left;
	private ImageView iv_left;
	private Context mContext;
	private EditText et_old_pw, et_new_pw, et_new_pw_agin;
	private String oldPw, newPw, newPwagin;
	private String token = "";
	private static final int MSG_CHANGEPW = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_CHANGEPW:
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
			case ClientConstant.HANDLER_FAILED:
				String error = (String) msg.obj;
				if (error.equals(HttpError.CertificateErrorStr)
						|| error.equals(HttpError.OverdueTokenStr))
					T.showToast(mContext, "操作超时，请重新操作！", 0);
				else if (error.equals(HttpError.DatabaseError))
					T.showToast(mContext, "修改失败！", 0);
				else if (error.equals(HttpError.PWErrorOrNotRegister))
					T.showToast(mContext, "旧密码错误！", 0);
				else
					T.showToast(mContext, error, 0);
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
		setContentView(R.layout.activity_account_changepw);
		mContext = this;
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("修改密码");
		tv_left.setText("帐号管理");
		tv_left.setVisibility(View.INVISIBLE);
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.iv_change_complete).setOnClickListener(this);
		et_old_pw = (EditText) findViewById(R.id.et_old_pw);
		et_new_pw = (EditText) findViewById(R.id.et_new_pw);
		et_new_pw_agin = (EditText) findViewById(R.id.et_new_pw_agin);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.iv_change_complete:
			oldPw = et_old_pw.getText().toString().trim();
			newPw = et_new_pw.getText().toString().trim();
			newPwagin = et_new_pw_agin.getText().toString().trim();
			verify();
			break;
		}
	}

	private void verify() {
		if (!TextUtils.isEmpty(newPw) && !TextUtils.isEmpty(newPwagin)
				&& !TextUtils.isEmpty(oldPw)) {
			if (newPw.length() >= 6 && newPw.length() <= 16) {
				if (newPw.equals(newPwagin)) {
					SendPwd();
				} else {
					T.showToast(mContext, "2次新密码输入不同!", 0);
					return;
				}
			} else {
				T.showToast(mContext, "请输入6-16位的密码!", 0);
				return;
			}
		} else {
			T.showToast(mContext, "输入不能为空!", 0);
			return;
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_CHANGEPW:
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
					Log.i(TAG, "解析失败");
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

	private void SendPwd() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.CHANGEPW_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("password", Sec.MD5Encrypt(newPw));
		params.put("oldpassword", Sec.MD5Encrypt(oldPw));
		// PostAsyncTask postTask = new PostAsyncTask(mContext, handler,
		// ClientConstant.CHANGEPW_URL, "", "请稍等...", true, true);
		// postTask.startAsyncTask(MSG_CHANGEPW, params,
		// ClassType.ChangPwActivity);

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_CHANGEPW, params,
				ClientConstant.CHANGEPW_URL);
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ChangPwActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ChangPwActivity);
		MobclickAgent.onPause(mContext);
	}
}
