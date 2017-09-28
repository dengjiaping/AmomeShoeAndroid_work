package cn.com.amome.amomeshoes.view.main.my.user;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.JobListAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.JobInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class JobListActivity extends Activity implements OnClickListener {
	private String TAG = "JobListActivity";
	private TextView tv_title, tv_left, tv_right;
	private Context mContext;
	private ListView lv_job;
	private JobListAdapter adapter;
	private Gson gson = new Gson();
	private List<JobInfo> jobList;
	private JobInfo jobInfo;
	private String job;
	private int selectPosition;
	private String jobId;
	private String token = "";
	private static final int MSG_GET_TOKEN = 0;
	private static final int MSG_GET_JOBLIST = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_JOBLIST:
					String jobJson = (String) msg.obj;
					if (TextUtils.isEmpty(jobJson)) {
					} else {
						jobList = gson.fromJson(jobJson,
								new TypeToken<List<JobInfo>>() {
								}.getType());
						if (jobList != null && jobList.size() > 0) {
							adapter = new JobListAdapter(mContext, jobList, job);
							lv_job.setAdapter(adapter);
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
		setContentView(R.layout.activity_my_joblist);
		mContext = this;
		job = getIntent().getStringExtra("job");
		initView();
		getJobList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_right = (TextView) findViewById(R.id.right_tv);
		tv_title.setText("职业");
		// tv_left.setText("个人信息");
		findViewById(R.id.rl_left).setOnClickListener(this);
		lv_job = (ListView) findViewById(R.id.lv_job);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
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
			case MSG_GET_JOBLIST:
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
					Log.i(TAG, "MSG_GET_JOBLIST解析失败");
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
	 * 获取职业列表
	 */
	private void getJobList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETJOBLIST);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_JOBLIST, params,
				ClientConstant.USERINFO_URL);
	}

	public void setSelect(int position) {
		selectPosition = position;
		jobInfo = jobList.get(position);
		job = jobInfo.jobdes;
		jobId = jobInfo.jobnum;
		back();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void back() {
		Intent intent = new Intent();
		intent.putExtra("jobname", job);
		intent.putExtra("jobid", jobId);
		intent.putExtra("position", selectPosition);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.JobListActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.JobListActivity);
		MobclickAgent.onPause(mContext);
	}
}
