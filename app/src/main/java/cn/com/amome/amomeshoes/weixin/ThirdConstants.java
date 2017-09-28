package cn.com.amome.amomeshoes.weixin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ThirdConstants {

	/**
	 *APP_ID 替换为你的应用从官方网站申请到的合法appId
	 *App_Secret 在微信开放平台提交应用审核通过后获得应用密钥AppSecret
	 */
	public static final String APP_ID = "wx876db6d066e927d3";
//	public static final String App_Secret = "8354e91d7b74fcbc173525a2127da290";
	public static final String App_Secret = "05ddaaad657c4d69a598937683374f34";
	public static final String THIRD_DATA = "thirddata";
	private static SharedPreferences sp;
	public static final String W_ACCESS_TOKEN = "w_access_token";
	public static final String W_OPENID = "w_openid";
	public static final String W_Code= "w_code";
	public static final String W_EXPIRESIN = "w_expires_in";
	public static final String W_AVATAR= "w_headimgurl";
	public static final String W_UNIONID = "w_unionid";

	/** 保存信息 */
	public static void saveStr(Context c, String key, String re) {
		Editor e = c.getSharedPreferences(THIRD_DATA, 0).edit();
		e.putString(key, re);
		e.commit();
	}

	/** 读取信息 */
	public static String readInfo(Context c, String key) {
		sp = c.getSharedPreferences(THIRD_DATA, 0);
		return sp.getString(key, "");
	}
}
