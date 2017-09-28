package cn.com.amome.amomeshoes.http;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import android.app.Service;
import android.os.Bundle;
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;
import android.os.Message;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.HandlerThread;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONObject;

public class HttpService extends Service {
	private final String TAG = "HttpService";
	private final Binder m_Binder = new HttpServiceBinder();
	private static final String PACKAGE_NAME = HttpService.class.getPackage()
			.getName();
	private List<PostAsyncTask> mPostAsyncTask = new ArrayList<PostAsyncTask>();

	public static String mToken = "";
	public static String HTTP_SUCC = "-0x0000";

	public static final String MSM_VERIFY_REGISTER = "0x01";
	public static final String MSM_VERIFY_GETFORPW = "0x02";
	public static final String MSM_VERIFY_LOGIN = "0x03";

	public static final String USER_START_REGISTER = "0x01";

	public static final String USER_START_LOGIN = "0x01";
	public static final String USER_QUIT_LOGIN = "0x02";
	public static final String USER_IS_LOGIN = "0x03";

	public static final String USER_FIX_PASSWORD = "0x01";
	public static final String USER_FOR_PASSWORD = "0x02";

	private static final String URL_USRR_BASE = "";
	public static final String URL_USER_REGISTER = URL_USRR_BASE + "";
	public static final String URL_SEND_VERIFY = URL_USRR_BASE + "";
	public static final String URL_USER_LOGIN = URL_USRR_BASE + "";
	public static final String URL_USER_FIXPASSWORD = URL_USRR_BASE + "";

	/** Called when the service is first created. */
	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		if (intent == null) {
			Log.i(TAG, "intent: " + intent);
			return super.onStartCommand(intent, flags, startId);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return m_Binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	public static final void mSleep(int val) {
		try {
			Thread.sleep(val);
		} catch (InterruptedException e) {
		}
	}

	public final class HttpServiceBinder extends Binder {

		public HttpService getService() {
			return HttpService.this;
		}
	}

	public static String getToken() {
		return mToken;
	}

	public static void setToken(String token) {
		mToken = token;
	}

	public void postHttpAsync(Context mContext, ICallback mCallback, int type,
			RequestParams params, String url) {

		PostAsyncTask mPost = new PostAsyncTask();
		mPost.startAsyncTask(mContext, mCallback, type, params, url);
		mPostAsyncTask.add(mPost);
	}

	public interface ICallback {
		public void onHttpPostSuccess(final int type, int statusCode,
				Header[] headers, byte[] responseBody);

		public void onHttpPostFailure(final int type, int arg0, Header[] arg1,
				byte[] responseBody, Throwable error);

	}
}