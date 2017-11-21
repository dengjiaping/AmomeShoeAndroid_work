package cn.com.amome.amomeshoes.view.main.health.detection.walk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.WalkingInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.report.WalkingReportActivity;
import cn.com.amome.shoeservice.BleService;

/**
 * 
 * @author jyw
 * 
 */
public class WalkReportActivity extends Activity implements OnClickListener {
	private String TAG = "WalkReportActivity";
	private Context mContext;
	private TextView tv_title, tv_walk_result, tv_step_result, tv_tip1,
			tv_tip2, tv_tip3;
	private Gson gson = new Gson();
	private List<WalkingInfo> WalkingInfoList;
	private WalkingInfo walkingInfo;
	private RelativeLayout rl_walk_progress, rl_step_progress;
	private View view_walk_progress, view_step_progress;
	private static final int MSG_GET_WALK_INFO = 99;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_WALK_INFO:
					String str = (String) msg.obj;
					if (str.equals("[{}]")) {
						Log.i(TAG, "为空");
					} else {
						WalkingInfoList = gson.fromJson(str,
								new TypeToken<List<WalkingInfo>>() {
								}.getType());
						walkingInfo = WalkingInfoList.get(0);
						tv_walk_result.setText(walkingInfo.symmetry);
						tv_step_result.setText(walkingInfo.gait);
						tv_tip1.setText(walkingInfo.symmetryreport);
						tv_tip2.setText(walkingInfo.gaitreport);
						LayoutParams lp_view_walk_progress = (LayoutParams) view_walk_progress
								.getLayoutParams();
						if (walkingInfo.symmetry.equals("正常")) {
							lp_view_walk_progress.width = rl_walk_progress
									.getWidth() * 19 / 20;
							tv_walk_result
									.setTextColor(Color.rgb(255, 64, 112));
						} else if (walkingInfo.symmetry.equals("一般")) {
							lp_view_walk_progress.width = rl_walk_progress
									.getWidth() / 2;
							tv_walk_result
									.setTextColor(Color.rgb(255, 141, 74));
						} else if (walkingInfo.symmetry.equals("异常")) {
							lp_view_walk_progress.width = rl_walk_progress
									.getWidth() / 20;
							tv_walk_result
									.setTextColor(Color.rgb(255, 169, 67));
						}
						view_walk_progress
								.setLayoutParams(lp_view_walk_progress);

						LayoutParams lp_view_step_progress = (LayoutParams) view_step_progress
								.getLayoutParams();
						if (walkingInfo.gait.equals("优秀")) {
							lp_view_step_progress.width = rl_step_progress
									.getWidth() * 19 / 20;
							tv_step_result
									.setTextColor(Color.rgb(255, 64, 112));
						} else if (walkingInfo.gait.equals("正常")) {
							lp_view_step_progress.width = rl_step_progress
									.getWidth() / 2;
							tv_step_result
									.setTextColor(Color.rgb(255, 141, 74));
						} else if (walkingInfo.gait.equals("弱")) {
							lp_view_step_progress.width = rl_step_progress
									.getWidth() / 20;
							tv_step_result
									.setTextColor(Color.rgb(255, 169, 67));
						}
						view_step_progress
								.setLayoutParams(lp_view_step_progress);
						//2017.11.08 ccf 修改添加o
						if (walkingInfo.l_bisptphase.equals("0.00") &&
								walkingInfo.l_pacetime.equals("0.00")&&
								walkingInfo.l_stdphase.equals("0.00")&&
								walkingInfo.l_stridefqc.equals("0.00")&&
								walkingInfo.l_stridetime.equals("0.00")&&
								walkingInfo.l_swingphase.equals("0.00")&&
								walkingInfo.l_unisptphase.equals("0.00")) {
							tv_tip3.setVisibility(View.VISIBLE);
						}
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
		setContentView(R.layout.activity_walk_report);
		mContext = this;
		BleService.mSleep(500);
		initView();
		getWalkInfo();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_walk_result = (TextView) findViewById(R.id.tv_walk_result);
		tv_step_result = (TextView) findViewById(R.id.tv_step_result);
		tv_tip1 = (TextView) findViewById(R.id.tv_tip1);
		tv_tip2 = (TextView) findViewById(R.id.tv_tip2);
		tv_tip3 = (TextView) findViewById(R.id.tv_tip3);
		rl_walk_progress = (RelativeLayout) findViewById(R.id.rl_walk_progress);
		rl_step_progress = (RelativeLayout) findViewById(R.id.rl_step_progress);
		view_walk_progress = findViewById(R.id.view_walk_progress);
		view_step_progress = findViewById(R.id.view_step_progress);
		tv_title.setText("走一走");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_walk_report_more).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_walk_report_more:
			startActivity(new Intent(mContext, WalkingReportActivity.class));
			finish();
			break;
		}
	}

	/**
	 * 获取走一走信息
	 */
	private void getWalkInfo() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETWALKINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_WALK_INFO, params,
				ClientConstant.WALK_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_WALK_INFO:
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
					Log.i(TAG, "MSG_GET_WALK_INFO解析失败");
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
