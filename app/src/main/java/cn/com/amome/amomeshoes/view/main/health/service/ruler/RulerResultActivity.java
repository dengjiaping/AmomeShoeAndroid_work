package cn.com.amome.amomeshoes.view.main.health.service.ruler;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootRulerInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 量一量-结果
 * 
 * @author css
 */
public class RulerResultActivity extends Activity implements OnClickListener {
	private String TAG = "RulerResultActivity";
	private static Context mContext;
	private TextView tv_title, tv_left;
	private TextView tv_info1, tv_info2, tv_info3, tv_info4, tv_tip;
	private Gson gson = new Gson();
	private static final int MSG_GET_RULERDATA = 0;
	private AmomeApp app;
	public static FootRulerInfo footRulerInfo;
	private String nickname = "";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_RULERDATA:
					String footRulerJson = (String) msg.obj;
					app.FootRulerList = gson.fromJson(footRulerJson,
							new TypeToken<List<FootRulerInfo>>() {
							}.getType());
					footRulerInfo = app.FootRulerList.get(0);
					initView();
					initData(footRulerInfo);
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
		setContentView(R.layout.activity_ruler_result);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		getNetData();
	}

	private void initData(FootRulerInfo footRulerInfo) {
		String lftlong = footRulerInfo.lftlong;
		String lftwidth = footRulerInfo.lftwidth;
		String rftlong = footRulerInfo.rftlong;
		String rftwidth = footRulerInfo.rftwidth;
		Log.i(TAG, "rftlong" + rftlong + "lftlong" + lftlong);
		tv_info1.setText(lftlong + "mm");
		tv_info2.setText(lftwidth + "mm");
		tv_info3.setText(rftlong + "mm");
		tv_info4.setText(rftwidth + "mm");
		if (Integer.parseInt(lftlong) > Integer.parseInt(rftlong))
			tv_tip.setBackgroundResource(R.drawable.ruler_left);
		else
			tv_tip.setBackgroundResource(R.drawable.ruler_right);
		DialogUtil.hideProgressDialog();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("量量脚");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		tv_info1 = (TextView) findViewById(R.id.tv_info1);
		tv_info2 = (TextView) findViewById(R.id.tv_info2);
		tv_info3 = (TextView) findViewById(R.id.tv_info3);
		tv_info4 = (TextView) findViewById(R.id.tv_info4);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
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
			case MSG_GET_RULERDATA:
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
					Log.i(TAG, "MSG_GET_RULERDATA解析失败");

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
			DialogUtil.hideProgressDialog();
		}
	};

	private void getNetData() {
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETRULERDATABYNAME_TYPE);
		params.put("nickname", nickname);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_RULERDATA, params,
				ClientConstant.GET_FOOTDATA_URL);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.RulerResultActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.RulerResultActivity);
		MobclickAgent.onPause(mContext);
	}
}
