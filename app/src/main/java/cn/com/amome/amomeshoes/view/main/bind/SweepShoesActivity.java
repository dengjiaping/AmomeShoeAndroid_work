package cn.com.amome.amomeshoes.view.main.bind;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.zxing.camera.CameraManager;
import cn.com.amome.amomeshoes.zxing.decoding.InactivityTimer;
import cn.com.amome.amomeshoes.zxing.decoding.RGBLuminanceSource;
import cn.com.amome.amomeshoes.zxing.decoding.SweepShoesActivityHandler;
import cn.com.amome.amomeshoes.zxing.view.ViewfinderView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class SweepShoesActivity extends Activity implements Callback {

	// private Context mContext;
	private String TAG = "SweepShoesActivity";
	private Context mContext;
	private SweepShoesActivityHandler handler;
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
	private String resultString = "";
	Camera camera;
	Parameters parameter;
	private String left, right;
	private static final int MSG_ABLUM_SUC = 5;
	private static final int MSG_ABLUM_FAIL = 6;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case MSG_ABLUM_SUC:
				mProgress.dismiss();
				String resultString = msg.obj.toString();
				if (resultString.equals("")) {
					Toast.makeText(mContext, "扫描失败!", Toast.LENGTH_SHORT)
							.show();
					finish();
				} else if (resultString.length() != 17
						|| !resultString.substring(2, 3).equals(":")) {
					T.showToast(mContext, "二维码不匹配!", 0);
					finish();
				} else {
					int flag = Integer.parseInt(resultString.substring(16));
					if (flag % 2 == 0) {
						SpfUtil.keepDeviceToShoeRightTmp(mContext, resultString);
					} else if (flag % 2 == 1) {
						SpfUtil.keepDeviceToShoeLeftTmp(mContext, resultString);
					}
					finish();
					// addBindDevice();
				}
				break;

			case MSG_ABLUM_FAIL:
				mProgress.dismiss();
				Toast.makeText(mContext, "解析错误！", Toast.LENGTH_LONG).show();

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
		mContext = this;
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
		findViewById(R.id.tv_ablum).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打开手机中的相册
				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
				innerIntent.setType("image/*");
				Intent wrapperIntent = Intent.createChooser(innerIntent,
						"选择二维码图片");
				startActivityForResult(wrapperIntent, 1);
			}
		});

	}

	String photo_path;
	ProgressDialog mProgress;
	Bitmap scanBitmap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) 对相册获取的结果进行分析
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				try {
					Uri uri = data.getData();
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri,
								new String[] { MediaStore.Images.Media.DATA },
								null, null, null);
						if (null == cursor) {
							Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						cursor.moveToFirst();
						photo_path = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
					} else {
						photo_path = data.getData().getPath();
					}
					mProgress = new ProgressDialog(mContext);
					mProgress.setMessage("正在识别...");
					mProgress.setCancelable(false);
					mProgress.show();

					new Thread(new Runnable() {
						@Override
						public void run() {
							Result result = scanningImage(photo_path);
							if (result != null) {
								Message m = mHandler.obtainMessage();
								m.what = MSG_ABLUM_SUC;
								m.obj = result.getText();
								mHandler.sendMessage(m);
							} else {
								Message m = mHandler.obtainMessage();
								m.what = MSG_ABLUM_FAIL;
								m.obj = "Scan failed!";
								mHandler.sendMessage(m);
							}

						}
					}).start();
				} catch (Exception e) {
					Toast.makeText(mContext, "解析错误！", Toast.LENGTH_LONG).show();
				}

				break;

			default:
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 扫描二维码图片的方法
	 * 
	 * 目前识别度不高，有待改进
	 * 
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 100);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
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

			public void onClick(View v) {
				finish();
			}
		});
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
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
		if (resultString.equals("")) {
			Toast.makeText(mContext, "扫描失败!", Toast.LENGTH_SHORT).show();
			finish();
		} else if (resultString.length() != 17
				|| !resultString.substring(2, 3).equals(":")) {
			T.showToast(mContext, "二维码不匹配!", 0);
			finish();
		} else {
			int flag = Integer.parseInt(resultString.substring(16));
			if (flag % 2 == 0) {
				SpfUtil.keepDeviceToShoeRightTmp(mContext, resultString);
			} else if (flag % 2 == 1) {
				SpfUtil.keepDeviceToShoeLeftTmp(mContext, resultString);
			}
			finish();
			// addBindDevice();
		}
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

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new SweepShoesActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

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
