package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.PromotionFootInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;

/**
 * @Title:PromotionFragment.java
 * @Description:健康促进界面
 * @author:jyw
 */
public class PromotionFragment extends Fragment implements OnClickListener {
	private String TAG = "PromotionFragment";
	private Context mContext;
	//private MyGridView gv_foot, gv_posture, gv_balance;
	//private List<Integer> basicList = new ArrayList<Integer>();
	//private HealthPromotionMainAdapter healthPromotionMainAdapter;
	private static final int MSG_GET_FOOT_DEATIL_DATA = 0;
	private Gson gson = new Gson();
	private List<PromotionFootInfo> promotionFootList;

	private GridView gv_promotion_foot;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_FOOT_DEATIL_DATA:
					String str = (String) msg.obj;
					if (TextUtils.isEmpty(str)) {
					} else {
						promotionFootList = gson.fromJson(str,
								new TypeToken<List<PromotionFootInfo>>() {
								}.getType());
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

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_promotion_main, null,
				false);
		mContext = getActivity();
		initView(view);
		return view;
	}

	private void initView(View view) {
//		gv_foot = (MyGridView) view.findViewById(gv_foot);
//		gv_posture = (MyGridView) view.findViewById(gv_posture);
//		gv_balance = (MyGridView) view.findViewById(gv_balance);
//		basicList.add(1);
//		healthPromotionMainAdapter = new HealthPromotionMainAdapter(mContext,
//				basicList);
//		gv_foot.setAdapter(healthPromotionMainAdapter);
//		gv_posture.setAdapter(healthPromotionMainAdapter);
//		gv_balance.setAdapter(healthPromotionMainAdapter);
		gv_promotion_foot = (GridView) view.findViewById(R.id.gv_promotion_foot);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case R.id.tv_foot_add:
		// startActivity(new Intent(mContext, PromotionFootAddActivity.class));
		// break;
		default:
			break;
		}
	}

	//未使用的方法
	/*private void getFootInfo() {
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_PROMOTION_DETAIL_INFO_TYPE);
		params.put("disease", "高弓足");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DEATIL_DATA,
				params, ClientConstant.PROMOTION_URL);

	}*/

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_FOOT_DEATIL_DATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0 && HttpError.judgeError(return_msg, ClassType.PayActivity)) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_FOOT_DEATIL_DATA解析失败");

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

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
