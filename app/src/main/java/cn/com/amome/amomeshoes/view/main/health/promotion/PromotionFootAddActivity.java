package cn.com.amome.amomeshoes.view.main.health.promotion;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.R.menu;
import cn.com.amome.amomeshoes.adapter.HealthPromotionMainAdapter;
import cn.com.amome.amomeshoes.adapter.ProblemAdapter;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootLookInfo;
import cn.com.amome.amomeshoes.model.FootMeaInfo;
import cn.com.amome.amomeshoes.model.FootRulerInfo;
import cn.com.amome.amomeshoes.model.PromotionInfo;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.widget.MyGridView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotionFootAddActivity extends Activity implements
		OnClickListener {
	private String TAG = "PromotionFootAddActivity";
	private Context mContext;
	private MyGridView gv_foot_all;
	private TextView tv_title;
	private PromotionAddAdapter promotionAddAdapter;
	private static final int MSG_GET_FOOT_DATA = 0;
	private Gson gson = new Gson();
	private List<PromotionInfo> footPromotionList;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_FOOT_DATA:
					String str = (String) msg.obj;
					if (TextUtils.isEmpty(str)) {
					} else {
						footPromotionList = gson.fromJson(str,
								new TypeToken<List<PromotionInfo>>() {
								}.getType());
						if (footPromotionList != null
								&& footPromotionList.size() > 0) {
							promotionAddAdapter = new PromotionAddAdapter(
									mContext, footPromotionList);
							gv_foot_all.setAdapter(promotionAddAdapter);
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
		setContentView(R.layout.activity_promotion_add);
		mContext = this;
		initView();
		getFootPromotionData();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		gv_foot_all = (MyGridView) findViewById(R.id.gv_foot_all);
		tv_title.setText("足部");
		gv_foot_all.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				T.showToast(mContext, "点的是"
						+ footPromotionList.get(position).type, 0);
			}
		});
		findViewById(R.id.rl_left).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		}
	}

	/**
	 * 获取脚长数据
	 */
	private void getFootPromotionData() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
		// true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_PROMOTION_INFO_TYPE);
		params.put("addtype", "foot");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
				ClientConstant.PROMOTION_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_FOOT_DATA:
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
					Log.i(TAG, "MSG_GET_FOOT_DATA解析失败");

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
}
