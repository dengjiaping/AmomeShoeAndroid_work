package cn.com.amome.amomeshoes.view.account;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.GoodsInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.weixin.WeiXinPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;

public class PayActivity extends Activity implements OnClickListener {
	private String TAG = "PayActivity";
	private Button appayBtn;
	private static Context mContext;
	private TextView tv_title, tv_left;
	private ImageView iv_left;
	private TextView tv_goods_val, tv_payamount_val, tv_introduction_val;
	private Gson gson = new Gson();
	private static final int MSG_GETSOFTWAREGOODSLIST = 1;
	private static final int MSG_GETPAYINFO = 2;
	private List<GoodsInfo> GoodsList;
	private GoodsInfo goodsInfo;
	private String payId = "";
	WeiXinPresenter wx;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GETSOFTWAREGOODSLIST:
					String re = (String) msg.obj;
					GoodsList = gson.fromJson(re,
							new TypeToken<List<GoodsInfo>>() {
							}.getType());
					if (GoodsList != null && GoodsList.size() > 0) {
						goodsInfo = GoodsList.get(0);
						initData(goodsInfo);
						DialogUtil.hideProgressDialog();
					}
					break;
				case MSG_GETPAYINFO:
					String payInfo = (String) msg.obj;
					if (!TextUtils.isEmpty(payInfo)) {
						try {
							JSONArray jsonArray = new JSONArray(payInfo);
							JSONObject json = jsonArray.getJSONObject(0);
							PayReq req = new PayReq();// 待修改
							req.appId = json.getString("appid");
							req.partnerId = json.getString("mch_id");
							req.prepayId = json.getString("prepay_id");
							req.nonceStr = json.getString("nonce_str");
							req.timeStamp = json.getString("timestamp");
							req.packageValue = json.getString("package");
							req.sign = json.getString("sign");
							payId = json.getString("trade_no");
							L.i("PayActivity", "appId: " + req.appId);
							L.i("PayActivity", "partnerId: " + req.partnerId);
							L.i("PayActivity", "prepayId: " + req.prepayId);
							L.i("PayActivity", "nonceStr: " + req.nonceStr);
							L.i("PayActivity", "timeStamp: " + req.timeStamp);
							L.i("PayActivity", "packageValue: "
									+ req.packageValue);
							L.i("PayActivity", "sign: " + req.sign);
							L.i("PayActivity", "extData: " + req.extData);
							L.i("PayActivity", "payId: " + payId);
							WeiXinPresenter.wxAPI.sendReq(req);
							L.i("ansen", "开始进行微信支付..");
							T.showToast(mContext, "开始进行微信支付..", 0);
						} catch (JSONException e) {
							e.printStackTrace();
							L.e("PAY_GET", "异常：" + e.getMessage());
							T.showToast(mContext, "异常：" + e.getMessage(), 0);
						}
					}
					break;
				}
				break;
			case ClientConstant.HANDLER_FAILED:
				String error = (String) msg.obj;
				if (error.equals(HttpError.CertificateErrorStr)
						|| error.equals(HttpError.OverdueTokenStr)) {
					T.showToast(mContext, "操作超时，请重新操作！", 0);
				} else
					T.showToast(mContext, error, 0);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_weixin);
		mContext = this;
		initView();
		getSoftwareGoodsList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("支付信息");
		tv_left.setVisibility(View.GONE);
		// iv_left.setVisibility(View.GONE);
		appayBtn = (Button) findViewById(R.id.appay_btn);
		tv_goods_val = (TextView) findViewById(R.id.tv_goods_val);
		tv_payamount_val = (TextView) findViewById(R.id.tv_payamount_val);
		tv_introduction_val = (TextView) findViewById(R.id.tv_introduction_val);
		appayBtn.setOnClickListener(this);
		iv_left.setOnClickListener(this);
	}

	private void initData(GoodsInfo goodsInfo) {
		tv_goods_val.setText(goodsInfo.body);
		float pric = (float) Integer.parseInt(goodsInfo.price) / 100;
		tv_payamount_val.setText(pric + "");
		tv_introduction_val.setText(goodsInfo.detail);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.appay_btn:
			wx = new WeiXinPresenter(mContext);
			if (wx.isPaySupported()) {
				wxPay();
			} else
				T.showToast(mContext, "当前微信版本不支持微信支付!", 0);
			break;
		case R.id.iv_left:
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
			case MSG_GETPAYINFO:
				DialogUtil.hideProgressDialog();
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
					Log.i(TAG, "MSG_GETPAYINFO解析失败");
				}
				break;

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
				params, ClientConstant.MSG_GETSOFTWAREGOODSLIST_URL); // 这个接口还没做
		// PostAsyncTask postTask = new PostAsyncTask(mContext, mHandler,
		// ClientConstant.GETGOODS_URL, "", "请稍等...", true, true);
		// postTask.startAsyncTask(MSG_GETGOODSLIST, params,
		// ClassType.PayActivity);

	}

	/**
	 * 获取微信支付信息
	 */
	private void wxPay() {
		L.v("", "==wxPay===");
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.ADD_ORDER_TYPE);
		params.put("goods_id", goodsInfo.goods_id);
		params.put("price", goodsInfo.price);
		// params.put("exchange", goodsInfo.exchange);
		// params.put("trial", goodsInfo.trial);
		// params.put("trade", "");// 交易单号
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GETPAYINFO, params,
				ClientConstant.ADDORDER_URL);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ClassType.PayActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.PayActivity);
		MobclickAgent.onPause(mContext);
	}

}