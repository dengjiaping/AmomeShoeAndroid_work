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
import cn.com.amome.amomeshoes.adapter.BrandListAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.Brand;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.shoeencrypt.AmomeEncrypt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class ShoeBrandListActivity extends Activity implements OnClickListener {
	private String TAG = "ShoeBrandListActivity";
	private TextView tv_title, tv_left;
	private Context mContext;
	private ListView lv_brand;
	private List<Brand> brandList;
	private Brand brandInfo;
	private BrandListAdapter adapter;
	private Gson gson = new Gson();
	private String brand;
	private int selectPosition;
	private String token = "";
	private static final int MSG_GET_TOKEN = 0;
	private static final int MSG_VERIFY_TOKEN = 1;
	private static final int MSG_GET_BRAND = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_BRAND:
					String brandJson = (String) msg.obj;
					if (TextUtils.isEmpty(brandJson)) {
						T.showToast(mContext, "未获取到数据", 0);
					} else {
						brandList = gson.fromJson(brandJson,
								new TypeToken<List<Brand>>() {
								}.getType());
						if (brandList != null && brandList.size() > 0) {
							adapter = new BrandListAdapter(mContext, brandList,
									brand);
							lv_brand.setAdapter(adapter);
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
		brand = getIntent().getStringExtra("brand");
		initView();
		getBrandList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("品牌");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		lv_brand = (ListView) findViewById(R.id.lv_job);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setSelect(int position) {
		Log.i(TAG, "setSelect");
		selectPosition = position;
		brandInfo = brandList.get(selectPosition);
		brand = brandInfo.brand;
		back();
	}

	private void back() {
		Intent intent = new Intent();
		intent.putExtra("brandname", brand);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void getBrandList() {
		RequestParams params = new RequestParams();
		params.put("certificate", token);
		params.put("calltype", ClientConstant.SHOEBRAND_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_BRAND, params,
				ClientConstant.SHOEBOX_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_BRAND:
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
					Log.i(TAG, "MSG_GET_BRAND解析失败");
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
		MobclickAgent.onPageStart(ClassType.ShoeBrandListActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ShoeBrandListActivity);
		MobclickAgent.onPause(mContext);
	}
}
