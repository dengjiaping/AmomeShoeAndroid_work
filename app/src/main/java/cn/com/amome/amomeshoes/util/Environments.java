package cn.com.amome.amomeshoes.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import com.nostra13.universalimageloader.utils.L;

public class Environments {
	public static final int Wifi_State = 2;// wifi
	public static final int Mobile_State = 1;// 3G
	public static final int No_Connection = 0;// 网络无连接
	public static int Net_State = 1;// 默认3G状态

	/**
	 * 获取当前网络连接状态
	 * @return
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		L.d(context.getPackageName(), mobile.toString()); // 显示3G网络连接状态
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		L.d(context.getPackageName(), wifi.toString()); // 显示wifi连接状态
		// 如果4G、3G、wifi、2G等网络状态是连接的，否则显示无连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			Net_State = Mobile_State;
		} else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			Net_State = Wifi_State;
		} else {
			Net_State = No_Connection;
		}
		return Net_State;
	}

	/**
	 * 获取当前版本号code码
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		try {
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Integer) null;
	}

	/**
	 * 获取当前版本号
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		try {
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检测SD卡是否有效
	 * @return
	 */
	public static boolean isSDEnable() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
	}

	/**
	 * 获取SD卡根路径
	 * @return  /mnt/sdcard
	 */
	public static String getSDRootPath(){
		File sdDir = null;
		if (isSDEnable()) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			return sdDir.toString();
		}
		return null;
	}

	/**
	 * 获取SD卡路径(应用卸载时此路径下文件也被删除)
	 * @return   /mnt/sdcard/Android/data/com.my.app/files/Amome
	 */
	public static String getSDPath(Context context){
		String path = null;
		File file = context.getExternalFilesDir("Amome");
		if(file != null){
			path = file.getPath();
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append(Environments.getSDRootPath())
					.append("/Android/data/")
					.append(context.getPackageName()).append("/files/Amome")
					.toString();
			path = sb.toString();
		}
		File outfile = new File(path);
		if (!outfile.isDirectory()) {
			try {
				outfile.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return path;
	}

	/**
	 * 获取缓存地址
	 * @param context
	 * @return 有sd卡:/sdcard/Android/data/<application package>/cache，
	 * 无sd卡:/data/data/<application package>/cache
	 */
	public static String getDiskCacheDir(Context context) {
	    String cachePath = null;
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {// SD卡存在或者SD卡不可被移除的时候
			File file = context.getExternalCacheDir();
			if (file != null) {
				cachePath = file.getPath();
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(Environments.getSDRootPath())
						.append("/Android/data/")
						.append(context.getPackageName()).append("/cache")
						.toString();
				cachePath = sb.toString();
			}
		} else {
	        cachePath = context.getCacheDir().getPath();
	    }
	    File outfile = new File(cachePath);
		if (!outfile.isDirectory()) {
			try {
				outfile.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    return cachePath;
	}

	/**
	 * 获取图片存储地址
	 * @return /mnt/sdcard/Amome/image
	 */
	public static String getImagePath(){
		String imagePath = Environments.getSDRootPath()+"/Amome/image";
		File outfile = new File(imagePath);
		// 如果文件不存在，则创建一个新文件
		if (!outfile.isDirectory()) {
			try {
				outfile.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imagePath;
	}

	/**
	 * 获取缓存图片存储地址
	 * @return 有sd卡:/sdcard/Android/data/<application package>/cache/image，
	 *         无sd卡:/data/data/<application package>/cache/image
	 */
	public static String getImageCachPath(Context context) {
		String cachePath = Environments.getDiskCacheDir(context) + "/image";
		File cacheDir = new File(cachePath);
		if (!cacheDir.isDirectory())
			try {
				cacheDir.mkdirs();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return cachePath;
	}

	/**
	 * 安装文件
	 * @param file
	 */
	public static void installApk(Context context,File file) {
		Activity activity = (Activity)context;
		if(!file.exists()){
			return;
		}
		// 隐式意图
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);// 设置意图的动作
		// 设置意图的数据与类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.finish();
		activity.startActivity(intent);// 激活该意图
	}
}
