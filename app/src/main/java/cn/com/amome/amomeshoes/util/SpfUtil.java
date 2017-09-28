package cn.com.amome.amomeshoes.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpfUtil {
	private static SharedPreferences sp;

	// 暂时屏蔽
	// /** 保存是否绑定信息 */
	// public static void keepBindedInfo(Context c, boolean isBind) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// e.putBoolean(Constants.KEY_KEEP_SHOES_BINDED_INFO, isBind);
	// e.commit();
	// }
	//
	// /** 读取是否绑定信息 */
	// public static boolean readBindedInfo(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getBoolean(Constants.KEY_KEEP_SHOES_BINDED_INFO, false);
	// }
	//
	// /** 保存选中的设备信息 */
	// public static void keepCheckedDevice(Context c, String address) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// e.putString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_CHECKED, address);
	// e.commit();
	// }
	//
	// /** 读取选中的设备信息 */
	// public static String readCheckedDevice(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_CHECKED, "");
	// }

	/** 保存左脚设备信息 */
	public static void keepDeviceToShoeLeft(Context c, String address) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_LEFT, address);
		e.commit();
	}

	/** 读取左脚设备信息 */
	public static String readDeviceToShoeLeft(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_LEFT, "");
	}

	/** 保存右脚设备信息 */
	public static void keepDeviceToShoeRight(Context c, String address) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_RIGHT, address);
		e.commit();
	}

	/** 读取右脚设备信息 */
	public static String readDeviceToShoeRight(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_RIGHT, "");
	}

	/** 保存左脚临时设备信息 */
	public static void keepDeviceToShoeLeftTmp(Context c, String address) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_LEFT_TMP, address);
		e.commit();
	}

	/** 读取左脚临时设备信息 */
	public static String readDeviceToShoeLeftTmp(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_LEFT_TMP, "");
	}

	/** 保存右脚临时设备信息 */
	public static void keepDeviceToShoeRightTmp(Context c, String address) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_RIGHT_TMP, address);
		e.commit();
	}

	/** 读取右脚临时设备信息 */
	public static String readDeviceToShoeRightTmp(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_SHOES_MAC_ADDRESS_RIGHT_TMP, "");
	}

	/** 保存当日日期 */
	public static void keepTodayDate(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TODAY_DATE, date);
		e.commit();
	}

	/** 读取当日日期 */
	public static String readTodayDate(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TODAY_DATE, "");
	}

	/** 保存时间戳 */
	public static void keepTime(Context c, String time) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TIME, time);
		e.commit();
	}

	/** 读取时间戳 */
	public static String readTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TIME, "");
	}

	/** 保存今日坐 */
	public static void keepTodaySitTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TODAY_SIT, date);
		e.commit();
	}

	/** 读取今日坐 */
	public static String readTodaySitTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TODAY_SIT, "");
	}

	/** 保存今日站 */
	public static void keepTodayStandTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TODAY_STAND, date);
		e.commit();
	}

	/** 读取今日站 */
	public static String readTodayStandTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TODAY_STAND, "");
	}

	/** 保存今日走 */
	public static void keepTodayWalkTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TODAY_WALK, date);
		e.commit();
	}

	/** 读取今日走 */
	public static String readTodayWalkTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TODAY_WALK, "");
	}

	/** 保存今日步数 */
	public static void keepTodaySteps(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_TODAY_STEPS, date);
		e.commit();
	}

	/** 读取今日步数 */
	public static String readTodaySteps(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_TODAY_STEPS, "");
	}

	/** 保存左脚坐 */
	public static void keepLeftSitTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_LEFT_SIT, date);
		e.commit();
	}

	/** 读取左脚坐 */
	public static String readLeftSitTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_LEFT_SIT, "");
	}

	/** 保存左脚站 */
	public static void keepLeftStandTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_LEFT_STAND, date);
		e.commit();
	}

	/** 读取左脚站 */
	public static String readLeftStandTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_LEFT_STAND, "");
	}

	/** 保存左脚走 */
	public static void keepLeftWalkTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_LEFT_WALK, date);
		e.commit();
	}

	/** 读取左脚走 */
	public static String readLeftWalkTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_LEFT_WALK, "");
	}

	/** 保存左脚步数 */
	public static void keepLeftSteps(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_LEFT_STEPS, date);
		e.commit();
	}

	/** 读取左脚步数 */
	public static String readLeftSteps(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_LEFT_STEPS, "");
	}

	/** 保存右脚坐 */
	public static void keepRightSitTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_RIGHT_SIT, date);
		e.commit();
	}

	/** 读取右脚坐 */
	public static String readRightSitTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_RIGHT_SIT, "");
	}

	/** 保存右脚站 */
	public static void keepRightStandTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_RIGHT_STAND, date);
		e.commit();
	}

	/** 读取右脚站 */
	public static String readRightStandTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_RIGHT_STAND, "");
	}

	/** 保存右脚走 */
	public static void keepRightWalkTime(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_RIGHT_WALK, date);
		e.commit();
	}

	/** 读取右脚走 */
	public static String readRightWalkTime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_RIGHT_WALK, "");
	}

	/** 保存右脚步数 */
	public static void keepRightSteps(Context c, String date) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.KEY_KEEP_RIGHT_STEPS, date);
		e.commit();
	}

	/** 读取右脚步数 */
	public static String readRightSteps(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.KEY_KEEP_RIGHT_STEPS, "");
	}

	/** 保存量一量脚形信息 */
	public static void keepFootShape(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.FOOTSHAPE, re);
		e.commit();
	}

	/** 读取量一量脚形信息 */
	public static String readFootShape(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.FOOTSHAPE, "");
	}

	/** 保存量一量足型信息 */
	public static void keepFootType(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.FOOTTYPE, re);
		e.commit();
	}

	/** 读取量一量足型信息 */
	public static String readFootType(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.FOOTTYPE, "");
	}

	/** 保存量一量脚size信息 */
	public static void keepFtSize(Context c, String re, String key) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(key, re);
		e.commit();
	}

	/** 读取量一量脚size信息 */
	public static String readFtSize(Context c, String key) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(key, "");
	}

	/** 保存量一量sex信息 */
	public static void keepAmountSex(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.AMOUNTSEX, re);
		e.commit();
	}

	/** 读取量一量sex信息 */
	public static String readAmountSex(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.AMOUNTSEX, "");
	}

	/** 保存量一量创建时间信息 */
	public static void keepRulerCreatetime(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.RULERCREATTIME, re);
		e.commit();
	}

	/** 读取量一量创建时间信息 */
	public static String readRulerCreatetime(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.RULERCREATTIME, "");
	}

	/** 保存量一量昵称信息 */
	public static void keepAmountNickname(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.RULERNICKNAME, re);
		e.commit();
	}

	/** 读取量一量昵称信息 */
	public static String readAmountNickname(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.RULERNICKNAME, "");
	}

	// 暂时屏蔽
	// /** 保存选选鞋问题信息 */
	// public static void keepChooseProdes(Context c, String re) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// e.putString(Constants.CHOOSEPRODES, re);
	// e.commit();
	// }
	//
	// /** 读取选选鞋问题信息 */
	// public static String readChooseProdes(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getString(Constants.CHOOSEPRODES, "");
	// }

	/** 保存是否量一量信息 */
	public static void keepIsRuler(Context c, boolean re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putBoolean(Constants.ISRULER, re);
		e.commit();
	}

	/** 读取是否量一量信息 */
	public static boolean readIsRuler(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getBoolean(Constants.ISRULER, false);
	}

	/** 保存帐号信息即号码 */
	public static void keepPhone(Context c, String phone) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.PHONE, phone);
		e.commit();
	}

	/** 读取帐号信息即号码 */
	public static String readPhone(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.PHONE, "");
	}

	/** 保存密码 */
	public static void keepPw(Context c, String pw) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.PW, pw);
		e.commit();
	}

	/** 读取pw */
	public static String readPw(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.PW, "");
	}

	/** 保存userid */
	public static void keepUserId(Context c, String id) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.USERID, id);
		e.commit();
	}

	/** 读取id */
	public static String readUserId(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.USERID, "");
	}

	/** 保存Nick */
	public static void keepNick(Context c, String nick) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.NICKNAME, nick);
		e.commit();
	}

	/** 读取昵称 */
	public static String readNick(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.NICKNAME, "");
	}

	// /** 保存体重 */
	// public static void keepWeight(Context c, String weight) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// e.putString(Constants.WEIGHT, weight);
	// e.commit();
	// }
	//
	// /** 读取体重 */
	// public static String readWeight(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getString(Constants.WEIGHT, "");
	// }

	/**
	 * 保存选鞋顾问选的什么
	 */
	public static void keepChooseShose(Context c, String str, String key) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(key, str);
		e.commit();
	}

	/**
	 * 获取选鞋顾问每个环节选的什么:体重、环境
	 */
	public static String getChooseShose(Context c, String key) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(key, "");
	}

	/**
	 * 保存选鞋顾问每个环节的分数
	 */
	public static void keepChooseProblemScore(Context c, int str, String key) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putInt(key, str);
		e.commit();
	}

	/**
	 * 获取选鞋顾问每个环节的分数
	 * 
	 * @return
	 */
	public static int getChooseProblemScore(Context c, String key) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getInt(key, 0);
	}

	/**
	 * 保存选鞋顾问的分数是否必须置为0， true,必须为0；false,不为0
	 */
	public static void keepIsSuitAmome(Context c, boolean re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putBoolean(Constants.ISSUITAMOME, re);
		e.commit();
	}

	/**
	 * 获取选鞋顾问的分数是否必须置为0，true,必须为0；false,不为0
	 */
	public static boolean getIsSuitAmome(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getBoolean(Constants.ISSUITAMOME, false);
	}

	// 暂时屏蔽
	// /**
	// * 保存签到日期
	// */
	// public static void keepCheckDate(Context c, String re) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// e.putString(Constants.ChECKDATE, re);
	// e.commit();
	// }
	// /**
	// * 读取签到日期
	// */
	// public static String readCheckDate(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getString(Constants.ChECKDATE, "");
	// }
	//
	// /**
	// * 保存签到积分
	// */
	// public static void keepIntegral(Context c, String re) {
	// Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
	// int inte = Integer.parseInt(re);
	// e.putInt(Constants.INTEGRAL, inte);
	// e.commit();
	// }
	// /**
	// * 读取签到积分
	// */
	// public static int readIntegral(Context c) {
	// sp = c.getSharedPreferences(Constants.USER_DATA, 0);
	// return sp.getInt(Constants.INTEGRAL, 0);
	// }

	/**
	 * 保存头像url
	 */
	public static void keepMyAvatarUrl(Context c, String re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.AVATARURL, re);
		e.commit();
	}

	/**
	 * 读取头像url
	 */
	public static String readMyAvatarUrl(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.AVATARURL, "");
	}

	/**
	 * 是否更新头像 true,更新；false,未更新
	 */
	public static void keepIsUpdataAvatar(Context c, boolean re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putBoolean(Constants.ISUPDATAAVATAR, re);
		e.commit();
	}

	/**
	 * 读取是否更新头像 true,更新；false,未更新
	 */
	public static boolean readIsUpdataAvatar(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getBoolean(Constants.ISUPDATAAVATAR, false);
	}

	/**
	 * 是否第一次运行 true,第一次；false,不是
	 */
	public static void keepIsFirstRun(Context c, boolean re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putBoolean(Constants.ISFIRSTRUN, re);
		e.commit();
	}

	/**
	 * 是否第一次运行 true,第一次；false,不是
	 */
	public static boolean readIsFirstRun(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getBoolean(Constants.ISFIRSTRUN, true);
	}

	/**
	 * APP版本
	 */
	public static void keepAppVerCode(Context c, int re) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putInt(Constants.APPVERCODE, re);
		e.commit();
	}

	/**
	 * APP版本
	 */
	public static int readAppVerCode(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getInt(Constants.APPVERCODE, -1);
	}

	/**
	 * Token
	 */
	public static void keepToken(Context c, String token) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.TOKEN, token);
		e.commit();
	}

	/**
	 * Token
	 */
	public static String readToken(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.TOKEN, "");
	}

	/** 保存md5 */
	public static void keepMD5(Context c, String id) {
		Editor e = c.getSharedPreferences(Constants.USER_DATA, 0).edit();
		e.putString(Constants.MD5, id);
		e.commit();
	}

	/**
	 * 获取md5
	 */
	public static String readMD5(Context c) {
		sp = c.getSharedPreferences(Constants.USER_DATA, 0);
		return sp.getString(Constants.MD5, "");
	}

	/**
	 * 保存屏幕宽-像素
	 */
	public static void keepScreenWidthPx(Context c, float widthPx) {
		Editor e = c.getSharedPreferences(Constants.SCREEN_DATA, 0).edit();
		e.putFloat(Constants.SCREEN_WIDTH_PX, widthPx);
		e.commit();
	}

	/**
	 * 获取屏幕宽-像素
	 */
	public static float readScreenWidthPx(Context c) {
		sp = c.getSharedPreferences(Constants.SCREEN_DATA, 0);
		return sp.getFloat(Constants.SCREEN_WIDTH_PX, 0);
	}

	/**
	 * 保存屏幕高-像素
	 */
	public static void keepScreenHeightPx(Context c, float heightPx) {
		Editor e = c.getSharedPreferences(Constants.SCREEN_DATA, 0).edit();
		e.putFloat(Constants.SCREEN_HEIGHT_PX, heightPx);
		e.commit();
	}

	/**
	 * 获取屏幕高-像素
	 */
	public static float readScreenHeightPx(Context c) {
		sp = c.getSharedPreferences(Constants.SCREEN_DATA, 0);
		return sp.getFloat(Constants.SCREEN_HEIGHT_PX, 0);
	}

	/**
	 * 保存屏幕宽-dp
	 */
	public static void keepScreenWidthDp(Context c, float widthDp) {
		Editor e = c.getSharedPreferences(Constants.SCREEN_DATA, 0).edit();
		e.putFloat(Constants.SCREEN_WIDTH_DP, widthDp);
		e.commit();
	}

	/**
	 * 获取屏幕宽-dp
	 */
	public static float readScreenWidthDp(Context c) {
		sp = c.getSharedPreferences(Constants.SCREEN_DATA, 0);
		return sp.getFloat(Constants.SCREEN_WIDTH_DP, 0);
	}

	/**
	 * 保存屏幕高-dp
	 */
	public static void keepScreenHeightDp(Context c, float heightDp) {
		Editor e = c.getSharedPreferences(Constants.SCREEN_DATA, 0).edit();
		e.putFloat(Constants.SCREEN_HEIGHT_DP, heightDp);
		e.commit();
	}

	/**
	 * 获取屏幕高-dp
	 */
	public static float readScreenHeightDp(Context c) {
		sp = c.getSharedPreferences(Constants.SCREEN_DATA, 0);
		return sp.getFloat(Constants.SCREEN_HEIGHT_DP, 0);
	}

	/**
	 * 保存屏幕密度-density
	 */
	public static void keepScreenDensity(Context c, float density) {
		Editor e = c.getSharedPreferences(Constants.SCREEN_DATA, 0).edit();
		e.putFloat(Constants.SCREEN_DENSITY, density);
		e.commit();
	}

	/**
	 * 获取屏幕高-density
	 */
	public static float readScreenDensity(Context c) {
		sp = c.getSharedPreferences(Constants.SCREEN_DATA, 0);
		return sp.getFloat(Constants.SCREEN_DENSITY, 0);
	}
}
