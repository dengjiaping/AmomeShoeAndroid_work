package cn.com.amome.amomeshoes.view.main.health.service.look;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.LookFootdiseasAdapter;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.LookModel;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 看一看-脚疾
 * 
 * @author css
 * 
 */
public class LookFootdiseasActivity extends Activity implements OnClickListener {
	private String TAG = "LookFootdiseasActivity";
	private TextView tv_title;
	private Context mContext;
	private GridView gridView;
	private List<LookModel> list = new ArrayList<LookModel>();
	private LookFootdiseasAdapter mAdapter;
	private static final int MSG_UDPATE_CHECK_FOOT = 0;
	private String nickname = "";
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_UDPATE_CHECK_FOOT:
					String re = (String) msg.obj;
					if (re.equals("0x00")) {
						Log.i(TAG, "更新成功");
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
		setContentView(R.layout.activity_look_foot_diseas);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_title.setText("脚疾");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);

		findViewById(R.id.btn_clean).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.gridView);

		initAdapter();

		mAdapter = new LookFootdiseasAdapter(mContext, list);
		gridView.setAdapter(mAdapter);

	}

	private void initAdapter() {
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_hongkong);
			model.setBgLightResource(R.drawable.look_foot_hongkong_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("脚藓")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("脚藓");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_beriberi);
			model.setBgLightResource(R.drawable.look_foot_beriberi_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("脚气")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("脚气");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_hv);
			model.setBgLightResource(R.drawable.look_foot_hv_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("拇外翻")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("拇外翻");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_odor);
			model.setBgLightResource(R.drawable.look_foot_odor_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("脚臭")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("脚臭");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_corn);
			model.setBgLightResource(R.drawable.look_foot_corn_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("鸡眼")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("鸡眼");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_peeling);
			model.setBgLightResource(R.drawable.look_foot_peeling_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("脚底脱皮")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("脚底脱皮");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_pf);
			model.setBgLightResource(R.drawable.look_foot_pf_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("足底筋膜炎")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("足底筋膜炎");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_at);
			model.setBgLightResource(R.drawable.look_foot_at_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("跟腱周围炎")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("跟腱周围炎");
			list.add(model);
		}
		{
			LookModel model = new LookModel();
			model.setBgDarkResource(R.drawable.look_foot_periostitis);
			model.setBgLightResource(R.drawable.look_foot_periostitis_s);
			if (null == AmomeApp.FootList) {
				model.setSelect(false);
			} else if (AmomeApp.FootList.get(0).ftdisease.contains("跟骨骨膜炎")) {
				model.setSelect(true);
			} else {
				model.setSelect(false);
			}
			model.setTag("跟骨骨膜炎");
			list.add(model);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.btn_clean:
			unSelectedAll();
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_ok:
			updateCheckFoot();
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
			case MSG_UDPATE_CHECK_FOOT:
				result = new String(responseBody);
				Log.i(TAG, result);
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
					Log.i(TAG, "MSG_UDPATE_CHECK_FOOT解析失败");

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
	 * 更新看一看数据
	 */
	private void updateCheckFoot() {
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		String ftdisease = mAdapter.getSelectedTag();

		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("nickname", nickname);
		params.put("calltype", ClientConstant.UPDATECHECKFOOT_TYPE);
		params.put("ftsize", "");
		params.put("ftwidthtype", "");
		params.put("ftshape", "");
		params.put("ftprint", "");
		params.put("ftcolor", "");
		params.put("ftache", "");
		params.put("ftdisease", ftdisease);
		params.put("isftache", "0");

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_UDPATE_CHECK_FOOT,
				params, ClientConstant.UPDATECHECKFOOT_URL);
	}

	public void unSelectedAll() {
		for (LookModel model : list) {
			model.setSelect(false);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.LookFootdiseasActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.LookFootdiseasActivity);
		MobclickAgent.onPause(mContext);
	}
}
