package cn.com.amome.amomeshoes.view.account;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.GoodsInfo;
import cn.com.amome.amomeshoes.util.Constants;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosePayActivity extends Activity implements OnClickListener {
	private String TAG = "ChoosePayActivity";
	private Context mContext;
	private TextView tv_title, tv_left;
	private ImageView iv_left;
	private Button twocode_btn, pay_btn;
	private static final int MSG_GETSOFTWAREGOODSLIST = 0;
	private Gson gson = new Gson();
	private List<GoodsInfo> GoodsList;
	private GoodsInfo goodsInfo;
	private String isqrspt = "";
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GETSOFTWAREGOODSLIST:
					String re = (String) msg.obj;
					if (re.equals("[{}]")) {
						T.showToast(mContext, "服务异常", 0);
					} else {
						GoodsList = gson.fromJson(re,
								new TypeToken<List<GoodsInfo>>() {
								}.getType());
						if (GoodsList != null && GoodsList.size() > 0) {
							goodsInfo = GoodsList.get(0);
							// initData(goodsInfo);
							isqrspt = goodsInfo.isqrspt;
							DialogUtil.hideProgressDialog();
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_choose);
		mContext = this;
		initView();
		getSoftwareGoodsList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("支付方式");
		tv_left.setVisibility(View.GONE);
		// iv_left.setVisibility(View.GONE); 暂时屏蔽
		twocode_btn = (Button) findViewById(R.id.apptwocode_btn);
		pay_btn = (Button) findViewById(R.id.apppay_btn);
		pay_btn.setOnClickListener(this);
		twocode_btn.setOnClickListener(this);
		iv_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apptwocode_btn:
			// startActivity(new Intent(mContext, SweepActivity.class));
			if (isqrspt.equals("1")) {
				startActivity(new Intent(mContext, SweepActivity.class));
			} else {
				T.showToast(mContext, "不支持二维码扫描", 0);
			}
			break;
		case R.id.apppay_btn:
			startActivity(new Intent(mContext, PayActivity.class));
			// finish();
			break;
		case R.id.iv_left:
			SpfUtil.keepPw(mContext, "");
			SpfUtil.keepPhone(mContext, "");
			SpfUtil.keepNick(mContext, "");
			SpfUtil.keepMyAvatarUrl(mContext, "");
			// SpfUtil.keepCheckDate(mContext, ""); 暂时屏蔽
			SpfUtil.keepAmountNickname(mContext, "");
			SpfUtil.keepAmountSex(mContext, "");
			SpfUtil.keepFootShape(mContext, "");
			SpfUtil.keepFootType(mContext, "");
			SpfUtil.keepFtSize(mContext, "", Constants.REALSHOESIZE);
			SpfUtil.keepFtSize(mContext, "", Constants.LFTHEIGHT);
			SpfUtil.keepFtSize(mContext, "", Constants.LFTWIDTH);
			SpfUtil.keepFtSize(mContext, "", Constants.RFTHEIGHT);
			SpfUtil.keepFtSize(mContext, "", Constants.RFTWIDTH);
			SpfUtil.keepUserId(mContext, "");
			SpfUtil.keepRulerCreatetime(mContext, "");
			// SpfUtil.keepChooseProdes(mContext, ""); 暂时屏蔽
			// SpfUtil.keepIntegral(mContext, "1");
			// SpfUtil.keepDeviceToShoeLeft(mContext, "");
			// SpfUtil.keepDeviceToShoeRight(mContext, "");
			// SpfUtil.keepBindedInfo(mContext, false);
			T.showToast(mContext, "请重新登录", 0);
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
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
			// TODO 自动生成的方法存根
			String result;
			switch (type) {
			case MSG_GETSOFTWAREGOODSLIST:
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
					Log.i(TAG, "MSG_GETSOFTWAREGOODSLIST解析失败");

				}
				break;

			default:
				break;
			}
		}

		@Override
		public void onHttpPostFailure(int type, int arg0, Header[] arg1,
				byte[] responseBody, Throwable error) {
			// TODO 自动生成的方法存根

		}
	};

	/**
	 * 获取商品数据列表
	 */
	private void getSoftwareGoodsList() {
		L.v("", "==getsoftwareGoodsList===");
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETGOODS_SOFTWARE_TYPE);// 0x01是所有商品
																		// 0x02根据服务权限id获取商品
		params.put("goods_id", "6");
		params.put("body", "魔秘");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GETSOFTWAREGOODSLIST,
				params, ClientConstant.MSG_GETSOFTWAREGOODSLIST_URL);
	}

}
