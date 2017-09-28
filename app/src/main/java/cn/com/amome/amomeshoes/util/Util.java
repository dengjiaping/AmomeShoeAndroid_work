package cn.com.amome.amomeshoes.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Util {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 验证手机号
	 */
	public static boolean formatMob(String mob) {
		Pattern p1 = null;
		Pattern p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		p2 = Pattern.compile("^[1][7][0,7][0-9]{8}$");
		m = p1.matcher(mob);
		b = m.matches();
		if (b) {
			return b;
		}

		m = p2.matcher(mob);
		b = m.matches();

		return b;
	}

	/**
	 * IMEI：International Mobile Equipment Identity 国际移动设备身份码
	 * @param context
	 * @return
	 */
	public static final String getIMEI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTelephonyMgr.getDeviceId();
		return imei;
	}
}
