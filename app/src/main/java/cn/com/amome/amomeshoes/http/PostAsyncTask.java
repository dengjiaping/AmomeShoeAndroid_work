package cn.com.amome.amomeshoes.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.shoeencrypt.AmomeEncrypt;

public class PostAsyncTask {
	private static final String TAG = "PostAsyncTask";
	private static final String CERT = "xxxxxxxxxxxxxxx";
	private HttpService.ICallback mCallback = null;
	private RequestParams mParams = new RequestParams();
	private RequestParams mOrgPm = new RequestParams();
	private Context mContext;
	private String mUrl = null;
	private int verify_count = 0;
	String return_msg;
	private Handler handler;

	// private String url;



	public PostAsyncTask() {
		verify_count = 0;
	}

	public PostAsyncTask(Handler handler) {
		verify_count = 0;
		this.handler = handler;
	}


	public void startAsyncTask(Context mCnt, HttpService.ICallback mCb, final int type, RequestParams params, String url) {
		mOrgPm = params;
		mParams = params;
		mParams.put("certificate", HttpService.getToken());
		mCallback = mCb;
		mUrl = url;
		mContext = mCnt;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			Message msg = Message.obtain();

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				String result = new String(responseBody);
				final String mark = null;
				// Log.i(TAG, "onSuccess (" + result + ")");

				if (statusCode == 200) {
					try {
						JSONObject json = new JSONObject(result);
						int return_code = json.getInt("return_code");
						String returnString = null;
						if (return_code == 0) {
							// Log.i(TAG, "return_code=0");
						} else if (return_code == 1) {
							// Log.i(TAG, "return_code=1");
							returnString = json.getString("return_msg");
							// Log.i(TAG, "return_msg=" + returnString);
							// 如果returnString等于其中的一个错误
							if (HttpError.unjudgeError(returnString)) {
								T.showToast(mContext, HttpError.judgeErrorString(returnString), 0);
							}
							try {
								// 如果是证书错误，再次发送请求
								if (json.getString("return_certificate").length() == 16) {
									return_msg = json.getString("return_msg");
									String tk = json.getString("return_certificate");
									AmomeEncrypt e = new AmomeEncrypt();
									String certificate = e.Encrypt(tk);
									HttpService.setToken(certificate);
									if (++verify_count <= 2) {
										startAsyncTask(mContext, mCallback, type, mOrgPm, mUrl);
									}
									return;
								}
							} catch (Exception e) {
								// TODO: handle exception

							}

						}

					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						Log.i(TAG, "无法解析");
						T.showToast(mContext, "服务器繁忙，请稍后再试", 0);
					}

				} else {
					Log.i(TAG, "请求失败");
					T.showToast(mContext, "服务器繁忙，请稍后再试！", 0);
				}

				mCallback.onHttpPostSuccess(type, statusCode, headers, responseBody);

			}

			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] responseBody, Throwable error) {
				super.onFailure(statusCode, arg1, responseBody, error);
				Log.i(TAG, "onFailure(" + statusCode + ")");
				if (statusCode == 0) {
					Log.i(TAG, "没网");
					T.showToast(mContext, "请检查网络连接！", 0);
				} else if (statusCode == 408) {
					Log.i(TAG, "statusCode == 408");
					T.showToast(mContext, "请求超时！", 0);
				} else if (statusCode == 503) {
					Log.i(TAG, "statusCode == 503");
					T.showToast(mContext, "服务器目前无法使用（维护中）！", 0);
				} else {
					Log.i(TAG, "其他Failure");
					T.showToast(mContext, "服务器繁忙！请稍后再试！", 0);
				}
				if (responseBody != null) {
					String re = new String(responseBody);
					Log.i(TAG, re);
				}
				error.printStackTrace();

				mCallback.onHttpPostFailure(type, statusCode, arg1, responseBody, // 可能有问题
						error);
			}
		});
	}
}
