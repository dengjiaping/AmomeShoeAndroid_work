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
import cn.com.amome.amomeshoes.adapter.HobbyListAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.HobbyInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class HobbyListActivity extends Activity implements OnClickListener {
	private String TAG = "HobbyListActivity";
	private TextView tv_title, tv_left, tv_right;
	private Context mContext;
	private ListView lv_hobby;
	private HobbyListAdapter adapter;
	private Gson gson = new Gson();
	private List<HobbyInfo> hobbyList;
	private HobbyInfo hobbyInfo;
	private String hobby;
	private int selectPosition;
	private String hobbyId;
	private String token = "";
	private static final int MSG_GET_TOKEN = 0;
	private static final int MSG_GET_HOBBYLIST = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_HOBBYLIST:
					String hobbyJson = (String) msg.obj;
					if (TextUtils.isEmpty(hobbyJson)) {
					} else {
						hobbyList = gson.fromJson(hobbyJson,
								new TypeToken<List<HobbyInfo>>() {
								}.getType());
						if (hobbyList != null && hobbyList.size() > 0) {
							adapter = new HobbyListAdapter(mContext, hobbyList,
									hobby);
							lv_hobby.setAdapter(adapter);
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
		setContentView(R.layout.activity_my_hobbylist);
		mContext = this;
		hobby = getIntent().getStringExtra("hobby");
		initView();
		getHobbyList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_right = (TextView) findViewById(R.id.right_tv);
		tv_title.setText("爱好");
		findViewById(R.id.rl_left).setOnClickListener(this);
		lv_hobby = (ListView) findViewById(R.id.lv_hobby);
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
			case MSG_GET_HOBBYLIST:
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
					Log.i(TAG, "MSG_GET_HOBBYLIST解析失败");
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
	private void getHobbyList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETHOBBYLIST);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_HOBBYLIST, params,
				ClientConstant.USERINFO_URL);
	}

	public void setSelect(int position) {
		selectPosition = position;
		hobbyInfo = hobbyList.get(position);
		hobby = hobbyInfo.hobbysdes;
		hobbyId = hobbyInfo.hobbynum;
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
		intent.putExtra("hobbyname", hobby);
		intent.putExtra("hobbyid", hobbyId);
		intent.putExtra("position", selectPosition);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.HobbyListActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.HobbyListActivity);
		MobclickAgent.onPause(mContext);
	}
}
