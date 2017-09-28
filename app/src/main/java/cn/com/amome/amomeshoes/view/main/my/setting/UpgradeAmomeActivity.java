package cn.com.amome.amomeshoes.view.main.my.setting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.http.ShoesHttpClient;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.UpgradeApp;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 软件设置-关于
 * 
 * @author css
 */
public class UpgradeAmomeActivity extends Activity implements OnClickListener {
	private static String TAG = "UpgradeAmomeActivity";
	private TextView tv_title, tv_left;
	private ImageView iv_left;
	private Context mContext;
	private TextView tv_newversion, tv_versionnum, tv_copyright_tip;
	private Gson gson = new Gson();
	private List<UpgradeApp> upgradeAppList;
	private UpgradeApp upgradeApp;
	private String downloadUrl;
	private int currVer_code;// 本地获取的版本code
	private String currVer_name;// 本地获取的版本名
	private int webVer_code;// web获取的版本code
	private String webVer_name;// web获取的版本名
	private String must_update = "0";// 0不强制更新,1强制更新
	private String apkName;
	private String path;
	private String token = "";
	private int reqType = -1;
	private static final int MSG_GET_TOKEN = 0;
	private static final int MSG_VERIFY_TOKEN = 1;
	private static final int MSG_REQUIT = 2;
	private static final int MSG_GET_APPINFO = 3;
	private static final int MSG_UPDATA_APP = 4;
	private int GETTOKEN_MAX_NUM = 1;// 出现错误时自动获取次数
	private int getTokenTimes = 0;
	private static ProgressDialog mDownloadProgress;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_APPINFO:
					String json = (String) msg.obj;
					if (json.equals("[{}]")) {
						T.showToast(mContext, "已是最新版本", 0);
					} else if (TextUtils.isEmpty(json)) {
						T.showToast(mContext, "服务器返回值异常", 0);
					} else {
						upgradeAppList = gson.fromJson(json,
								new TypeToken<List<UpgradeApp>>() {
								}.getType());
						if (upgradeAppList != null && upgradeAppList.size() > 0) {
							upgradeApp = upgradeAppList.get(0);
							downloadUrl = upgradeApp.firmware_path;
							String version = upgradeApp.newest_verison;// v1.0.4_5
							String[] versionArr = version.split("_");// [0]=v1.0.4
																		// [1]=5
							webVer_name = versionArr[0]; // v1.0.4
							webVer_code = Integer.parseInt(versionArr[1]); // 5
							must_update = upgradeApp.urgent;
							apkName = "Amome" + webVer_name + ".apk"; // Amomev1.0.4.apk
							if (currVer_code < webVer_code) {
								updateApp();
								// tv_newversion.setText("发现新版本"); //屏蔽
								// tv_newversion.setText("已是最新版本");
							} else {
								T.showToast(mContext, "已是最新版本", 0);
							}
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_about);
		mContext = this;
		currVer_code = Environments.getVersionCode(mContext);
		currVer_name = Environments.getVersionName(mContext);
		Log.i(TAG, "currVer_code" + currVer_code);
		Log.i(TAG, "currVer_name" + currVer_name);
		token = SpfUtil.readToken(mContext);
		reqType = MSG_GET_APPINFO;

		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("魔秘升级");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		iv_left = (ImageView) findViewById(R.id.iv_left);
		findViewById(R.id.rl_left).setOnClickListener(this);

		tv_versionnum = (TextView) findViewById(R.id.tv_versionnum);
		tv_copyright_tip = (TextView) findViewById(R.id.copyright_tip);
		findViewById(R.id.iv_update).setOnClickListener(this);
		tv_versionnum.setText("当前版本：" + currVer_name);
		tv_copyright_tip.setText(getString(R.string.app_copyright1, "2016"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.iv_update:
			MobclickAgent.onEvent(mContext, ClassType.updata_app);
			reqType = MSG_UPDATA_APP;
			getApp(); // 暂时处理
			// updateApp();
			break;
		default:
			break;
		}

	}

	private void updateApp() {
		if (must_update.equals("0")) {// 0:不强制更新
			if (webVer_code > currVer_code) {// 比本地大更新
				DialogUtil.showAlertDialog(mContext, "有新版本app", "是否更新？", "确定",
						"取消", new OnAlertViewClickListener() {
							@Override
							public void confirm() {
								if (Environments.getNetworkState(mContext) == Environments.Wifi_State) {
									downloadApp(downloadUrl);
								} else if (Environments
										.getNetworkState(mContext) == Environments.Mobile_State) {
									DialogUtil.showAlertDialog(mContext, "",
											"确定使用手机流量更新app？", "确定", "取消",
											new OnAlertViewClickListener() {
												@Override
												public void confirm() {
													downloadApp(downloadUrl);
												}

												@Override
												public void cancel() {
												}
											});
								}
							}

							@Override
							public void cancel() {
							}
						});
			}
		} else {// 强制更新
			if (webVer_code > currVer_code) {// 比本地大更新
				DialogUtil.showAlertDialog(mContext, "更新新版本app",
						"有重要内容更新，请务必更新app！否则影响使用!", "确定", "取消",
						new OnAlertViewClickListener() {
							@Override
							public void confirm() {
								if (Environments.getNetworkState(mContext) == Environments.Wifi_State) {
									downloadApp(downloadUrl);
								} else if (Environments
										.getNetworkState(mContext) == Environments.Mobile_State) {
									DialogUtil.showAlertDialog(mContext, "",
											"确定使用手机流量更新app？", "确定", "取消",
											new OnAlertViewClickListener() {
												@Override
												public void confirm() {
													downloadApp(downloadUrl);
												}

												@Override
												public void cancel() {
													finish();
												}
											});
								}

							}

							@Override
							public void cancel() {
								finish();
							}
						});
			}
		}
	}

	/**
	 * 下载app
	 * 
	 * @param down_url
	 */
	private void downloadApp(String down_url) {
		path = Environments.getSDPath(mContext) + "/" + apkName;
		try {
			downloadApkFile("http://" + down_url, path, mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_APPINFO:
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
					Log.i(TAG, "MSG_GET_APPINFO解析失败");
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

	private void getApp() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETAPPINFO);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_APPINFO, params,
				ClientConstant.APP_DOWNLOAD);
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.UpgradeAmomeActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.UpgradeAmomeActivity);
		MobclickAgent.onPause(mContext);
	}

	/**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	private void downloadApkFile(final String url, final String path,
			final Context mContext) throws Exception {
		// 获取二进制数据如图片和其他文件
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				L.d(TAG, "downloadFile=" + url + "\n" + path);
				L.i("url", url);
				L.i("path", path);
				initFwDownloadProgress(mContext);
				showDownloadProgress();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {
				String tempPath = path;
				L.e(TAG, "共下载了：" + binaryData.length);
				File file = new File(tempPath);
				final File to = new File(file.getAbsolutePath()
						+ System.currentTimeMillis());
				file.renameTo(to);
				to.delete();
				try {
					// 若存在则删除
					if (file.exists()) {
						file.delete();
					}
					// 创建文件
					file.createNewFile();
					InputStream inputstream = new ByteArrayInputStream(
							binaryData);
					if (inputstream != null) {
						write2SDFromInput(path, inputstream);
						inputstream.close();
					}
					L.d(TAG, "下载成功" + tempPath);
					dismissDownloadProgress();
					// 安装文件
					Environments.installApk(mContext, file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
				L.e(TAG, "下载失败");
				dismissDownloadProgress();
				T.showToast(mContext, "下载失败", 1);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				// 下载进度显示
				mDownloadProgress.setProgress(count);
				// L.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
			}
		});
	}

	public void initFwDownloadProgress(Context context) {
		if (mDownloadProgress == null) {
			mDownloadProgress = new ProgressDialog(context);
			mDownloadProgress.setMax(100);
			mDownloadProgress.setProgress(0);
			mDownloadProgress.setTitle("魔秘下载");
			mDownloadProgress.setMessage("下载进度");
			mDownloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDownloadProgress.setIndeterminate(false);
			mDownloadProgress.setCancelable(false);
		}
	}

	public static void showDownloadProgress() {
		if (mDownloadProgress != null) {
			try {
				mDownloadProgress.show();
			} catch (Exception e) {
				// TODO: handle exception
//				Log.i(TAG, "mDownloadProgress崩了");
			}

		}
	}

	/**
	 * 将一个inputstream里面的数据写入SD卡中 第一个参数为目录名 第二个参数为文件名
	 */
	public File write2SDFromInput(String path, InputStream inputstream) {
		File file = null;
		OutputStream output = null;
		try {
			file = new File(path);
			output = new FileOutputStream(file);
			// 4k为单位，每4K写一次
			byte buffer[] = new byte[4 * 1024];
			int temp = 0;
			while ((temp = inputstream.read(buffer)) != -1) {
				// 获取指定信,防止写入没用的信息
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public static void dismissDownloadProgress() {
		if (mDownloadProgress != null) {
			mDownloadProgress.dismiss();
		}
	}

}
