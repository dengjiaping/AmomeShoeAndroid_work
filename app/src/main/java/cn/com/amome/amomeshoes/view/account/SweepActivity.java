package cn.com.amome.amomeshoes.view.account;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.MainFragmentActivity;
import cn.com.amome.amomeshoes.zxing.camera.CameraManager;
import cn.com.amome.amomeshoes.zxing.decoding.InactivityTimer;
import cn.com.amome.amomeshoes.zxing.decoding.SweepActivityHandler;
import cn.com.amome.amomeshoes.zxing.view.ViewfinderView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 二维码扫描界面
 */
public class SweepActivity extends Activity implements Callback {

	// private Context mContext;
	private String TAG = "SweepActivity";
	private SweepActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageButton cancelScanButton;
	private ImageButton shanguangdengBtn;
	private static final int MSG_SWEEPCODE = 0;
	private String resultString = "";
	Camera camera;
	Parameters parameter;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_SWEEPCODE:
					// DialogUtil.hideProgressDialog();
					String orderJson = (String) msg.obj;
					String nulllist = "123";
					if (nulllist.equals("123")) {
						try {
							JSONArray jsonArray = new JSONArray(orderJson);
							JSONObject jsonObject = (JSONObject) jsonArray
									.getJSONObject(0);
							nulllist = jsonObject.getString("state");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Log.i(TAG, "order_id解析失败");
							e.printStackTrace();
						}
					}
					if (nulllist.equals("SUCCESS")) {
						T.showToast(SweepActivity.this, "扫描成功", 0);
						Intent intent = new Intent(SweepActivity.this,
								MainFragmentActivity.class);
						// intent.putExtra("payresult", "0");
						startActivity(intent);
					} else {
						Intent intent = new Intent(SweepActivity.this,
								PayResultActivity.class);
						intent.putExtra("payresult", "1");
						startActivity(intent);
					}
					SweepActivity.this.finish();
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_sweep);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (ImageButton) this
				.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		shanguangdengBtn = (ImageButton) findViewById(R.id.btn_shanguangdeng);
		shanguangdengBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				turnHandler();
			}
		});

	}

	public void turnHandler() {
		if (camera == null) {
			camera = CameraManager.get().getCamera();
		}
		camera.startPreview();
		Parameters parameters = camera.getParameters();
		// Ɛ׏ʁڢֆձǰ״̬
		if (Parameters.FLASH_MODE_OFF.equals(parameters.getFlashMode())) {
			turnOn(parameters);

		} else if (Parameters.FLASH_MODE_TORCH
				.equals(parameters.getFlashMode())) {
			turnOff(parameters);
		}
	}

	private void turnOn(Parameters parameters) {
		shanguangdengBtn.setBackgroundResource(R.drawable.light_open_btn);
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
	}

	private void turnOff(Parameters parameters) {
		shanguangdengBtn.setBackgroundResource(R.drawable.light_close_btn);
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(parameters);
		// camera.release();
		// camera = null;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		// quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SweepActivity.this.finish();
			}
		});
		MobclickAgent.onPageStart(ClassType.SweepActivity);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		MobclickAgent.onPageEnd(ClassType.SweepActivity);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			T.showToast(SweepActivity.this, "Scan failed!", 0);
		} else {
			System.out.println("Result:" + resultString); // 扫描结果
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}
		sweepresult();
		// SweepActivity.this.finish();
	}

	private void continuePreview() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		initCamera(surfaceHolder);
		if (handler != null) {
			handler.restartPreviewAndDecode();
		}
		try {
			Thread.currentThread();
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO 自动生成的方法存根
			String result;
			switch (type) {
			case MSG_SWEEPCODE:
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
					if (return_code == 1) {
						T.showToast(SweepActivity.this, "二维码无法使用", 0);
						continuePreview();
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					T.showToast(SweepActivity.this, "二维码无法识别", 0);
					e.printStackTrace();
					Log.i(TAG, "MSG_SWEEPCODE解析失败");
					continuePreview();
					// SweepActivity.this.finish();
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
	 * 二维码微信支付信息
	 */
	private void sweepresult() {
		L.v("", "==sweepresult===");
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
		// true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(SweepActivity.this));
		params.put("calltype", ClientConstant.SWEEPCODE_TYPE);
		params.put("goods_id", "6");
		params.put("body", "魔秘");
		params.put("qrcode", resultString);

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(SweepActivity.this, callback, MSG_SWEEPCODE,
				params, ClientConstant.ADDORDER_URL);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new SweepActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		// /////////////////////////////////
		CameraManager.get().stopPreview();
		// ///////////////////////////////
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}