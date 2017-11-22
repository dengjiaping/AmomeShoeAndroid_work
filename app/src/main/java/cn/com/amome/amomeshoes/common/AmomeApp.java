package cn.com.amome.amomeshoes.common;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.model.FootLookInfo;
import cn.com.amome.amomeshoes.model.FootRulerInfo;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.shoeservice.BleService;

public class AmomeApp extends Application {
	public static Typeface typeFace;
	public static BleService BleService = null;
	private static AmomeApp instance;
	private ImageLoader imageLoader;
	public static BleShoes bleShoes = null;// 智能鞋
	public static int bleShoesState = BleShoesState.MSG_NOT_CONNECT;
	public static List<FootLookInfo> FootList;
	public static List<FootRulerInfo> FootRulerList;
	/** 判断切换至运动页面后，是否已连接智能鞋 */
	public static boolean exercise_flag = false;
	public static boolean  yesterhist_flag = true;// 获取昨日数据失败后应重新获取，加一个标志判断是否重新获取
	/** 判断是否在运动页，默认不在 */
	public static boolean hidden = false;
	public static boolean pauseFlag = false;// 智能检测页面

	public static AmomeApp getInstance() {
		if (null == instance) {
			instance = new AmomeApp();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// setTypeface();
		final Intent serviceIntent = new Intent(this, HttpService.class);
		startService(serviceIntent);
		final Intent service = new Intent(this, BleService.class);
		startService(service);
		bindService(service, BleServiceConnection, Context.BIND_AUTO_CREATE);
		initImageLoader();
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		MobclickAgent.enableEncrypt(true);// 6.0.0版本及以后
		CrashReport
				.initCrashReport(getApplicationContext(), "db81603aa6", true);
	}

	private ServiceConnection BleServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			BleService = ((BleService.BleServiceBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			BleService = null;
		}
	};

	public void finishBLEService() {
		unbindService(BleServiceConnection);
		stopService(new Intent(this, BleService.class));

	}

	public void setTypeface() {
		// 加载外部字体
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/test.ttf");
		try {
			Field field_3 = Typeface.class.getDeclaredField("SANS_SERIF");
			field_3.setAccessible(true);
			field_3.set(null, typeFace);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化imageloader
	 */
	private void initImageLoader() {
		imageLoader = ImageLoader.getInstance();
		String cachePath = Environments
				.getImageCachPath(getApplicationContext());
		// String cachePath = Environments.getImageCachPath(this);
		File cacheDir = new File(cachePath);
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)
				// max width, max height
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache implementation
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// You can pass your own disc cache implementation
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.build();
		imageLoader.init(configuration);
	}

}
