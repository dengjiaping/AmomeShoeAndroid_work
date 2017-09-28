package cn.com.amome.amomeshoes.view.main.my.setting;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.events.ExitEvent;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.Constants;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.account.ForgetPwActivity;
import cn.com.amome.amomeshoes.view.account.LoginActivity;

import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我-设置-账户管理
 * 
 * @author css
 */
public class AccountManagementActivity extends Activity implements
		OnClickListener {
	private String TAG = "AccountManagementActivity";
	private TextView tv_title, tv_left;
	private ImageView iv_left;
	private Context mContext;
	private static final int MSG_REQUIT = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_REQUIT:
					String re = (String) msg.obj;
					MobclickAgent.onProfileSignOff();
					SpfUtil.keepPw(mContext, "");
					SpfUtil.keepPhone(mContext, "");
					SpfUtil.keepNick(mContext, "");
					SpfUtil.keepMyAvatarUrl(mContext, "");
					SpfUtil.keepAmountNickname(mContext, "");
					SpfUtil.keepAmountSex(mContext, "");
					SpfUtil.keepFootShape(mContext, "");
					SpfUtil.keepFootType(mContext, "");
					SpfUtil.keepFtSize(mContext, "", Constants.REALSHOESIZE);
					SpfUtil.keepFtSize(mContext, "", Constants.LFTHEIGHT);
					SpfUtil.keepFtSize(mContext, "", Constants.LFTWIDTH);
					SpfUtil.keepFtSize(mContext, "", Constants.RFTHEIGHT);
					SpfUtil.keepFtSize(mContext, "", Constants.RFTWIDTH);
					SpfUtil.keepUserId(mContext, "");
					SpfUtil.keepRulerCreatetime(mContext, "");
					AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
					try {
						AmomeApp.bleShoes
								.disShoesConnect(shoesDisconnectCallback);
					} catch (Exception e) {
						// TODO: handle exception
						Log.i(TAG, "crash，大概是还没建立连接");
						e.printStackTrace();
					}
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					EventBus.getDefault().post(new ExitEvent("关闭主页面"));
					finish();
					break;
				default:
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
		setContentView(R.layout.activity_account_main);
		mContext = this;
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("帐号管理");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		// tv_left.setText("软件设置");
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.rl_logout).setOnClickListener(this);
		// findViewById(R.id.rl_sina).setOnClickListener(this);
		// findViewById(R.id.rl_qq).setOnClickListener(this);
		// findViewById(R.id.rl_wechat).setOnClickListener(this);
		findViewById(R.id.rl_changepw).setOnClickListener(this);
		findViewById(R.id.rl_forgetpw).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		// case R.id.rl_sina:
		// break;
		// case R.id.rl_qq:
		// break;
		// case R.id.rl_wechat:
		// break;
		case R.id.rl_changepw:
			startActivity(new Intent(mContext, ChangPwActivity.class));
			break;
		case R.id.rl_forgetpw:
			startActivity(new Intent(mContext, ForgetPwActivity.class)
					.putExtra("leftname", "帐号管理"));
			break;
		case R.id.rl_logout:
			MobclickAgent.onEvent(mContext, ClassType.logout);
			// MSG_REQUIT;
			DialogUtil.showAlertDialog(mContext, "", "是否真的要退出魔秘？", "残忍退出",
					"留在魔秘", new OnAlertViewClickListener() {
						@Override
						public void confirm() {
							requit();
						}

						@Override
						public void cancel() {
						}
					});

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
			case MSG_REQUIT:
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

	/** 退出登录操作 */
	private void requit() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.REQUIT);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_REQUIT, params,
				ClientConstant.LOGIN_URL);
		// PostAsyncTask postTask = new PostAsyncTask(mContext, mHandler,
		// ClientConstant.LOGIN_URL, "", "请稍等...", true, true);
		// postTask.startAsyncTask(MSG_REQUIT, params,
		// ClassType.AboutMomeActivity);
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.AccountManagementActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.AccountManagementActivity);
		MobclickAgent.onPause(mContext);
	}
}
