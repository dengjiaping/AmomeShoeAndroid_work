package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.util.List;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.CategoryShoesListAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.CategoryShoesInfo;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.SpfUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class ShoeCategoryListActivity extends Activity implements
		OnClickListener {
	private String TAG = "ShoeCategoryListActivity";
	private TextView tv_title, tv_left;
	private Context mContext;
	private ListView lv_category;
	private CategoryShoesListAdapter adapter;
	private Gson gson = new Gson();
	private List<CategoryShoesInfo> categoryList;
	private CategoryShoesInfo categoryShoesInfo;
	private String category;
	private String classcount;
	private int selectPosition;
	private static final int MSG_GET_CATEGORY = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_CATEGORY:
					String categoryJson = (String) msg.obj;
					if (TextUtils.isEmpty(categoryJson)) {
					} else {
						categoryList = gson.fromJson(categoryJson,
								new TypeToken<List<CategoryShoesInfo>>() {
								}.getType());
						if (categoryList != null && categoryList.size() > 0) {
							adapter = new CategoryShoesListAdapter(mContext,
									categoryList, category);
							lv_category.setAdapter(adapter);
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
		setContentView(R.layout.activity_shoebox_category);
		mContext = this;
		category = getIntent().getStringExtra("category");
		initView();
		getCategoryList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("种类");
		findViewById(R.id.rl_left).setOnClickListener(this);
		lv_category = (ListView) findViewById(R.id.lv_category);
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
			case MSG_GET_CATEGORY:
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
					Log.i(TAG, "MSG_GET_CATEGORY解析失败");
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
	 * 获取鞋类型
	 */
	private void getCategoryList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSHOECATEGORY_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CATEGORY, params,
				ClientConstant.SHOEBOX_URL);
	}

	public void setSelect(int position) {
		selectPosition = position;
		categoryShoesInfo = categoryList.get(position);
		category = categoryShoesInfo.category;
		classcount = categoryShoesInfo.classcount;
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
		intent.putExtra("categoryname", category);
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