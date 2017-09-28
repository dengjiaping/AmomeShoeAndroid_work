package cn.com.amome.amomeshoes.view.main.health.detection.shake;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import cn.com.amome.amomeshoes.model.BalanceInfo;
import cn.com.amome.amomeshoes.model.CopInfo;
import cn.com.amome.amomeshoes.model.LineInfo;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.report.BalanceReportActivity;
import cn.com.amome.amomeshoes.view.main.health.report.FootReportActivity;
import cn.com.amome.amomeshoes.widget.CopTrailView;
import cn.com.amome.amomeshoes.widget.MyView;
import cn.com.amome.shoeservice.BleService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * @author jyw
 * 
 */
public class ShakeReportActivity extends Activity implements OnClickListener {
	private String TAG = "ShakeReportActivity";
	private Context mContext;
	private TextView tv_title, tv_shake_result, tv_shake_double_open_result,
			tv_shake_double_close_result, tv_shake_single_open_result,
			tv_shake_single_close_result, tv_shake_tip;
	private ImageView iv_left;
	private RelativeLayout rl_shake_progress;
	private View view_shake_progress;
	public List<LineInfo> lineList = new ArrayList<LineInfo>();
	private static final int MSG_GET_SHAKE_INFO = 0;
	private Gson gson = new Gson();
	private List<BalanceInfo> BalanceList;
	private BalanceInfo BalanceInfo;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_SHAKE_INFO:
					String str = (String) msg.obj;
					if (str.equals("[{}]")) {
						Log.i(TAG, "为空");
					} else {
						BalanceList = gson.fromJson(str,
								new TypeToken<List<BalanceInfo>>() {
								}.getType());
						BalanceInfo = BalanceList.get(0);
						tv_shake_result.setText(BalanceInfo.compositeindex);
						tv_shake_tip.setText(BalanceInfo.compositeindexreport);
						tv_shake_double_open_result
								.setText(BalanceInfo.bo_balanceindex);
						tv_shake_double_close_result
								.setText(BalanceInfo.bc_balanceindex);
						tv_shake_single_open_result
								.setText(BalanceInfo.so_balanceindex);
						tv_shake_single_close_result
								.setText(BalanceInfo.sc_balanceindex);
						LayoutParams lp_view_shake_progress = (LayoutParams) view_shake_progress
								.getLayoutParams();
						if (BalanceInfo.compositeindex.equals("弱")) {
							lp_view_shake_progress.width = rl_shake_progress
									.getWidth() / 20;
							tv_shake_result.setTextColor(Color
									.rgb(255, 169, 67));
						} else if (BalanceInfo.compositeindex.equals("正常")) {
							lp_view_shake_progress.width = rl_shake_progress
									.getWidth() / 2;
							tv_shake_result.setTextColor(Color
									.rgb(255, 141, 74));
						} else if (BalanceInfo.compositeindex.equals("优秀")) {
							lp_view_shake_progress.width = rl_shake_progress
									.getWidth() * 19 / 20;
							tv_shake_result.setTextColor(Color
									.rgb(255, 64, 112));
						}
						view_shake_progress
								.setLayoutParams(lp_view_shake_progress);

						if (BalanceInfo.bo_balanceindex.equals("弱")) {
							tv_shake_double_open_result.setTextColor(Color.rgb(
									255, 169, 67));
						} else if (BalanceInfo.bo_balanceindex.equals("正常")) {
							tv_shake_double_open_result.setTextColor(Color.rgb(
									255, 141, 74));
						} else if (BalanceInfo.bo_balanceindex.equals("优秀")) {
							tv_shake_double_open_result.setTextColor(Color.rgb(
									255, 64, 112));
						}

						if (BalanceInfo.bc_balanceindex.equals("弱")) {
							tv_shake_double_close_result.setTextColor(Color
									.rgb(255, 169, 67));
						} else if (BalanceInfo.bc_balanceindex.equals("正常")) {
							tv_shake_double_close_result.setTextColor(Color
									.rgb(255, 141, 74));
						} else if (BalanceInfo.bc_balanceindex.equals("优秀")) {
							tv_shake_double_close_result.setTextColor(Color
									.rgb(255, 64, 112));
						}

						if (BalanceInfo.so_balanceindex.equals("弱")) {
							tv_shake_single_open_result.setTextColor(Color.rgb(
									255, 169, 67));
						} else if (BalanceInfo.so_balanceindex.equals("正常")) {
							tv_shake_single_open_result.setTextColor(Color.rgb(
									255, 141, 74));
						} else if (BalanceInfo.so_balanceindex.equals("优秀")) {
							tv_shake_single_open_result.setTextColor(Color.rgb(
									255, 64, 112));
						}

						if (BalanceInfo.sc_balanceindex.equals("弱")) {
							tv_shake_single_close_result.setTextColor(Color
									.rgb(255, 169, 67));
						} else if (BalanceInfo.sc_balanceindex.equals("正常")) {
							tv_shake_single_close_result.setTextColor(Color
									.rgb(255, 141, 74));
						} else if (BalanceInfo.sc_balanceindex.equals("优秀")) {
							tv_shake_single_close_result.setTextColor(Color
									.rgb(255, 64, 112));
						}

					}
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
		setContentView(R.layout.activity_shake_report);
		mContext = this;
		BleService.mSleep(500);
		initView();
		getBalanceInfo();
	}

	private void initView() {
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_shake_result = (TextView) findViewById(R.id.tv_shake_result);
		tv_shake_double_open_result = (TextView) findViewById(R.id.tv_shake_double_open_result);
		tv_shake_double_close_result = (TextView) findViewById(R.id.tv_shake_double_close_result);
		tv_shake_single_open_result = (TextView) findViewById(R.id.tv_shake_single_open_result);
		tv_shake_single_close_result = (TextView) findViewById(R.id.tv_shake_single_close_result);
		tv_shake_tip = (TextView) findViewById(R.id.tv_shake_tip);
		view_shake_progress = findViewById(R.id.view_shake_progress);
		rl_shake_progress = (RelativeLayout) findViewById(R.id.rl_shake_progress);
		tv_title.setText("摇一摇");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_shake_report_more).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_shake_report_more:
			startActivity(new Intent(mContext, BalanceReportActivity.class));
			finish();
			break;
		}
	}

	/**
	 * 获取摇一摇信息
	 */
	private void getBalanceInfo() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSHAKEINFO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_SHAKE_INFO, params,
				ClientConstant.SHAKE_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_SHAKE_INFO:
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
					Log.i(TAG, "MSG_GET_SHAKE_INFO解析失败");
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
