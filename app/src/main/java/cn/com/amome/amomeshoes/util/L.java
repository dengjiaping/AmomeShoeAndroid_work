package cn.com.amome.amomeshoes.util;

import android.util.Log;

/**
 * 日志的过滤
 * @author css LOGLEVEL = 0 时,日志将不显示在控制台上
 */
public class L {
	// debug mode
	public static int LOGLEVEL = 6;
	public static int VERBOSE = 1;
	public static int DEBUG = 2;
	public static int INFO = 3;
	public static int WARN = 4;
	public static int ERROR = 5;

	public static void v(String tag, String msg) {
		if (LOGLEVEL > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOGLEVEL > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOGLEVEL > INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOGLEVEL > WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOGLEVEL > ERROR) {
			Log.e(tag, msg);
		}
	}
}
