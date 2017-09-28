package cn.com.amome.amomeshoes.view.main.health.detection.squat;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.FootMeaInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.report.FootReportActivity;
import cn.com.amome.shoeservice.BleService;
import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author jyw
 * 
 */
public class SquatReportActivity extends Activity implements OnClickListener {
	private String TAG = "SquatReportActivity";
	private Context mContext;
	private TextView tv_title, tv_instep, tv_spin, tv_instep_result,
			tv_spin_result, tv_instep_tip, tv_spin_tip, tv_instep_name,
			tv_spin_name, tv_title_foot_report;
	private LinearLayout ll_title_foot_report;
	private ImageView iv_left, iv_instep, iv_spin;
	private Gson gson = new Gson();
	private List<FootMeaInfo> FootMeaList;
	private FootMeaInfo FootMeaInfo;
	private static final int MSG_GET_MEAINFO = 66;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_MEAINFO:
					String str = (String) msg.obj;
					if (str.equals("[{}]")) {
						Log.i(TAG, "为空");
					} else {
						FootMeaList = gson.fromJson(str,
								new TypeToken<List<FootMeaInfo>>() {
								}.getType());
						FootMeaInfo = FootMeaList.get(0);
						tv_instep_result.setText("足弓:" + FootMeaInfo.turn);
						tv_spin_result.setText("旋前/后:" + FootMeaInfo.spin);
						if (!FootMeaInfo.turnreport.equals("")) {
							tv_instep_tip.setVisibility(View.VISIBLE);
							tv_instep_name.setVisibility(View.VISIBLE);
							tv_instep_tip.setText(FootMeaInfo.turnreport);
							tv_instep_name.setText(FootMeaInfo.turn);
						}
						if (!FootMeaInfo.spinreport.equals("")) {
							tv_spin_tip.setVisibility(View.VISIBLE);
							tv_spin_name.setVisibility(View.VISIBLE);
							tv_spin_tip.setText(FootMeaInfo.spinreport);
							tv_spin_name.setText(FootMeaInfo.spin);
						}
						if (FootMeaInfo.turn.contains("高弓")) {
							iv_instep
									.setImageResource(R.drawable.squat_instep_high);
						} else if (FootMeaInfo.turn.contains("扁平")) {
							iv_instep
									.setImageResource(R.drawable.squat_instep_flat);
						} else if (FootMeaInfo.turn.contains("正常")) {
							iv_instep
									.setImageResource(R.drawable.squat_instep_normal);
						} else {
						}
						if (FootMeaInfo.spin.contains("旋前")) {
							iv_spin.setImageResource(R.drawable.squat_spin_front);
						} else if (FootMeaInfo.spin.contains("旋后")) {
							iv_spin.setImageResource(R.drawable.squat_spin_back);
						} else if (FootMeaInfo.spin.contains("正常")) {
							iv_spin.setImageResource(R.drawable.squat_spin_normal);
						} else {
						}
						if (FootMeaInfo.turn.contains("正常")
								&& FootMeaInfo.spin.contains("正常")) {
							tv_instep_tip.setVisibility(View.VISIBLE);
							tv_title_foot_report.setText("恭喜您");
							tv_instep_tip
									.setText("您现在身体很健康，请继续保持，生活中注意规避不健康的饮食，坐姿，站姿等情况。");
						}
						ll_title_foot_report.setVisibility(View.VISIBLE);// 足部报告/恭喜您两种情况，后显示
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_squat_report);
		mContext = this;
		BleService.mSleep(500);
		initView();
		getSquatInfo();
	}

	private void initView() {
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_instep = (TextView) findViewById(R.id.tv_instep);
		tv_spin = (TextView) findViewById(R.id.tv_spin);
		iv_instep = (ImageView) findViewById(R.id.iv_instep_result);
		iv_spin = (ImageView) findViewById(R.id.iv_spin_result);
		tv_instep_result = (TextView) findViewById(R.id.tv_instep_result);
		tv_spin_result = (TextView) findViewById(R.id.tv_spin_result);
		tv_instep_tip = (TextView) findViewById(R.id.tv_instep_tip);
		tv_spin_tip = (TextView) findViewById(R.id.tv_spin_tip);
		tv_instep_name = (TextView) findViewById(R.id.tv_instep_name);
		tv_spin_name = (TextView) findViewById(R.id.tv_spin_name);
		tv_title_foot_report = (TextView) findViewById(R.id.tv_title_foot_report);
		ll_title_foot_report = (LinearLayout) findViewById(R.id.ll_title_foot_report);
		tv_title.setText("蹲一蹲");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_squat_report_more).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_squat_report_more:
			startActivity(new Intent(mContext, FootReportActivity.class));
			finish();
			break;
		}
	}

	/**
	 * 获取蹲一蹲信息
	 */
	private void getSquatInfo() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETMEAINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_MEAINFO, params,
				ClientConstant.MEASURE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_MEAINFO:
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
					Log.i(TAG, "MSG_GET_MEAINFO解析失败");
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
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}
}
