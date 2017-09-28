package cn.com.amome.amomeshoes.view.main.my;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.DialogUtil.OnOneAlertViewClickListener;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 软件设置-关于-意见与反馈
 * 
 * @author css
 */
public class SendOpinionActivity extends Activity implements OnClickListener {
	private String TAG = "SendOpinionActivity";
	private TextView tv_title;
	private ImageView iv_left;
	private Context mContext;
	private EditText et_content;
	private static final int MSG_FEEDBACK = 0;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_FEEDBACK:
					String re = (String) msg.obj;
					if (re.equals("0x00")) {
						Log.i(TAG, "更新成功");
						DialogUtil.showiosAlertDialog(mContext, "提示",
								"提交成功，感谢您对小秘的支持", "确定",
								new OnOneAlertViewClickListener() {

									@Override
									public void confirm() {
										// TODO Auto-generated method stub
										et_content.setText("");
									}

								});
					}
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
		setContentView(R.layout.activity_my_sendopinion);
		mContext = this;
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("问题反馈");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_submit).setOnClickListener(this);
		et_content = (EditText) findViewById(R.id.et_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_submit:
			String content = et_content.getText().toString().trim();
			if (content == null || content.isEmpty()) {
				T.showToast(mContext, "还未输入内容呢!", 0);
			} else {
				sumbit();
			}
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
			case MSG_FEEDBACK:
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
					Log.i(TAG, "MSG_FEEDBACK解析失败");
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

	/**
	 * APP内的登录
	 */
	private void sumbit() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
		// true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.FEEDBACK_TYPE);
		params.put("feedback", et_content.getText().toString().trim());
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_FEEDBACK, params,
				ClientConstant.USERINFO_URL);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.SendOpinionActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.SendOpinionActivity);
		MobclickAgent.onPause(mContext);
	}

}
