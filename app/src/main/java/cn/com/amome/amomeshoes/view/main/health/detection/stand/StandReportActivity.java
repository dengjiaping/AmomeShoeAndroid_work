package cn.com.amome.amomeshoes.view.main.health.detection.stand;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.FootStandInfo;
import cn.com.amome.amomeshoes.model.StandDetectionModel;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesStopPressDataCallback;
import cn.com.amome.amomeshoes.view.main.health.report.FootReportActivity;
import cn.com.amome.amomeshoes.view.main.health.report.PostureReportActivity;
import cn.com.amome.shoeservice.BleService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author jyw
 * 
 */
public class StandReportActivity extends Activity implements OnClickListener {
	private String TAG = "StandReportActivity";
	private Context mContext;
	private TextView tv_title, tv_disease1_result, tv_disease2_result,
			tv_shoulder_report, tv_spine_report, tv_shoulder_name,
			tv_spine_name, tv_single_disease1_result,
			tv_single_disease2_result, tv_title_posture_report;
	private ImageView iv_left, iv_disease1_result, iv_disease2_result,
			iv_single_disease1_result, iv_single_disease2_result;
	private DisplayImageOptions options;
	private ImageLoader loader;
	private LinearLayout ll_double_disease, ll_single_disease1,
			ll_single_disease2, ll_no_disease, ll_title_posture_report;
	// private String dise = "", dise2 = "";
	private static final int MSG_GET_STANDINFO = 0;
	private Gson gson = new Gson();
	private List<FootStandInfo> FootStandList;
	private FootStandInfo FootStandInfo;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_STANDINFO:
					String str = (String) msg.obj;
					if (str.equals("[{}]")) {
						Log.i(TAG, "为空");
					} else {
						FootStandList = gson.fromJson(str,
								new TypeToken<List<FootStandInfo>>() {
								}.getType());
						FootStandInfo = FootStandList.get(0);
						if (!FootStandInfo.shoulderreport.equals("")) {
							tv_shoulder_name.setVisibility(View.VISIBLE);
							tv_shoulder_report.setVisibility(View.VISIBLE);
							tv_shoulder_name.setText(FootStandInfo.shoulder);
							tv_shoulder_report
									.setText(FootStandInfo.shoulderreport);
						}
						if (!FootStandInfo.spinereport.equals("")) {
							tv_spine_name.setVisibility(View.VISIBLE);
							tv_spine_report.setVisibility(View.VISIBLE);
							tv_spine_name.setText(FootStandInfo.spine);
							tv_spine_report.setText(FootStandInfo.spinereport);
						}
						if (!FootStandInfo.shoulder.equals("正常")
								&& !FootStandInfo.spine.equals("正常")) {

							tv_disease1_result.setText(FootStandInfo.shoulder);
							tv_disease2_result.setText(FootStandInfo.spine);

							if (FootStandInfo.shoulder.contains("高低肩-左肩高")) {
								iv_disease1_result
										.setImageResource(R.drawable.stand_dise_left_high);
							} else if (FootStandInfo.shoulder
									.contains("高低肩-右肩高")) {
								iv_disease1_result
										.setImageResource(R.drawable.stand_dise_right_high);
							} else {
								// iv_disease1_result
								// .setImageResource(R.drawable.stand_dise_normal);
							}

							if (FootStandInfo.spine.contains("重心靠前--颈椎痛")) {
								iv_disease2_result
										.setImageResource(R.drawable.stand_dise2_front);
							} else if (FootStandInfo.spine
									.contains("右侧--骨盆前悬，脊柱右弯")) {
								iv_disease2_result
										.setImageResource(R.drawable.stand_dise2_right_turn);
							} else if (FootStandInfo.spine
									.contains("左侧--骨盆后悬，脊柱左弯")) {
								iv_disease2_result
										.setImageResource(R.drawable.stand_dise2_left_turn);
							} else if (FootStandInfo.spine
									.contains("重心靠后--腰椎痛")) {
								iv_disease2_result
										.setImageResource(R.drawable.stand_dise2_back);
							} else {
								// iv_disease2_result
								// .setImageResource(R.drawable.stand_dise2_normal);
							}
						} else if (!FootStandInfo.shoulder.equals("正常")) {
							ll_double_disease.setVisibility(View.GONE);
							ll_single_disease1.setVisibility(View.VISIBLE);
							if (FootStandInfo.shoulder.contains("高低肩-左肩高")) {
								iv_single_disease1_result
										.setImageResource(R.drawable.stand_dise_left_high);
								tv_single_disease1_result
										.setText(FootStandInfo.shoulder);
							} else if (FootStandInfo.shoulder
									.contains("高低肩-右肩高")) {
								iv_single_disease1_result
										.setImageResource(R.drawable.stand_dise_right_high);
								tv_single_disease1_result
										.setText(FootStandInfo.shoulder);
							} else {
								// iv_single_disease1_result
								// .setImageResource(R.drawable.stand_dise_normal);
								// tv_single_disease1_result
								// .setText("双肩："+FootStandInfo.shoulder);
							}
						} else if (!FootStandInfo.spine.equals("正常")) {
							ll_double_disease.setVisibility(View.GONE);
							ll_single_disease2.setVisibility(View.VISIBLE);
							if (FootStandInfo.spine.contains("重心靠前--颈椎痛")) {
								iv_single_disease2_result
										.setImageResource(R.drawable.stand_dise2_front);
								tv_single_disease2_result
										.setText(FootStandInfo.spine);
							} else if (FootStandInfo.spine
									.contains("右侧--骨盆前悬，脊柱右弯")) {
								iv_single_disease2_result
										.setImageResource(R.drawable.stand_dise2_right_turn);
								tv_single_disease2_result
										.setText(FootStandInfo.spine);
							} else if (FootStandInfo.spine
									.contains("左侧--骨盆后悬，脊柱左弯")) {
								iv_single_disease2_result
										.setImageResource(R.drawable.stand_dise2_left_turn);
								tv_single_disease2_result
										.setText(FootStandInfo.spine);
							} else if (FootStandInfo.spine
									.contains("重心靠后--腰椎痛")) {
								iv_single_disease2_result
										.setImageResource(R.drawable.stand_dise2_back);
								tv_single_disease2_result
										.setText(FootStandInfo.spine);
							} else {
								// iv_single_disease2_result
								// .setImageResource(R.drawable.stand_dise2_normal);
								// tv_single_disease2_result
								// .setText("脊柱："+FootStandInfo.spine);
							}
						} else if (FootStandInfo.shoulder.equals("正常")
								&& FootStandInfo.spine.equals("正常")) {
							tv_shoulder_report
									.setText("您现在身体很健康，请继续保持，生活中注意规避不健康的饮食，坐姿，站姿等情况。");
							tv_title_posture_report.setText("恭喜您");
							tv_shoulder_report.setVisibility(View.VISIBLE);
							tv_title_posture_report.setVisibility(View.VISIBLE);
							ll_double_disease.setVisibility(View.GONE);
							ll_no_disease.setVisibility(View.VISIBLE);
							ll_title_posture_report.setVisibility(View.VISIBLE);// 足部报告/恭喜您两种情况，后显示
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
		setContentView(R.layout.activity_stand_report);
		mContext = this;
		BleService.mSleep(500);
		initView();
		getStandInfo();
	}

	private void initView() {
		ll_double_disease = (LinearLayout) findViewById(R.id.ll_double_disease);
		ll_single_disease1 = (LinearLayout) findViewById(R.id.ll_single_disease1);
		ll_single_disease2 = (LinearLayout) findViewById(R.id.ll_single_disease2);
		ll_no_disease = (LinearLayout) findViewById(R.id.ll_no_disease);
		ll_title_posture_report = (LinearLayout) findViewById(R.id.ll_title_posture_report);
		tv_single_disease1_result = (TextView) findViewById(R.id.tv_single_disease1_result);
		tv_single_disease2_result = (TextView) findViewById(R.id.tv_single_disease2_result);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_disease1_result = (ImageView) findViewById(R.id.iv_disease1_result);
		iv_disease2_result = (ImageView) findViewById(R.id.iv_disease2_result);
		iv_single_disease1_result = (ImageView) findViewById(R.id.iv_single_disease1_result);
		iv_single_disease2_result = (ImageView) findViewById(R.id.iv_single_disease2_result);
		tv_disease1_result = (TextView) findViewById(R.id.tv_disease1_result);
		tv_disease2_result = (TextView) findViewById(R.id.tv_disease2_result);
		tv_shoulder_report = (TextView) findViewById(R.id.tv_shoulder_report);
		tv_spine_report = (TextView) findViewById(R.id.tv_spine_report);
		tv_shoulder_name = (TextView) findViewById(R.id.tv_shoulder_name);
		tv_spine_name = (TextView) findViewById(R.id.tv_spine_name);
		tv_title_posture_report = (TextView) findViewById(R.id.tv_title_posture_report);
		tv_title.setText("站一站");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_stand_report_more).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_stand_report_more:
			startActivity(new Intent(mContext, PostureReportActivity.class));
			finish();
			break;
		}
	}

	/**
	 * 获取站一站信息
	 */
	private void getStandInfo() {
		Log.i(TAG, "执行getStandInfo()");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSTANDINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_STANDINFO, params,
				ClientConstant.STAND_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_STANDINFO:
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		try {
//			AmomeApp.bleShoes.stopShoesPressData(shoesStopPressDataCallback); // 停止获取压力数据
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.i(TAG, "停止获取压力数据时崩了，大概是还没建立连接");
//			e.printStackTrace();
//		}
	}
}
