package cn.com.amome.amomeshoes.http;

public class HttpError {
	// //////////////////错误返回码//////////////////////////
	/** 正确 */
	public static final String AccessSuccess = "0x00";
	/**
	 * 参数不对 Parameter number is not correct
	 */
	public static final String ParameterError = "-0x01";
	/**
	 * 数据库错误 Accessing database errors
	 */
	public static final String DatabaseError = "-0x02";
	/**
	 * 证书错误 Certificate error
	 */
	public static final String CertificateError = "-0x08";
	/**
	 * 不正确的密码或用户未注册 Incorrect password or user not registered
	 */
	public static final String PWErrorOrNotRegister = "-0x10";
	/**
	 * 短信验证错误 SMS verification error
	 */
	public static final String SMSError = "-0x20";
	/**
	 * 消息认证码发送错误 Message authentication code sending error
	 */
	public static final String VerifyCodeSendError = "-0x40";
	/**
	 * 已经注册过了 User is already registered
	 */
	public static final String AlreadyRegistered = "-0x80";
	/**
	 * 访问类型码错误 Access type code error
	 */
	public static final String TypeCodeError = "-0x100";
	/**
	 * 用户未登录 User not logged in
	 */
	public static final String NotLoggedIn = "-0x200";
	/**
	 * 图片上传错误 Image upload failed
	 */
	public static final String ImageUploadFailed = "-0x800";
	/**
	 * 用户不在用户信息 Users are not on the user information
	 */
	public static final String UserNotExist = "-0x1000";
	/**
	 * 用户未注册 User not registered
	 */
	public static final String NotRegistered = "-0x2000";
	/**
	 * 无用户测量信息
	 * */
	public static final String NotRulerMeasureData = "-0x4000";
	/**
	 * 无鞋柜信息
	 * */
	public static final String NotShoeBoxData = "-0x10000";
	/**
	 * token过期
	 * */
	public static final String OverdueToken = "-0x40000";
	/**
	 * token未过期
	 * */
	public static final String SuccesToken = "-0x80000";
	/**
	 * 二维码无法使用
	 * */
	public static final String SweepCodeError = "-0x2000000";
	// //////////////////////////////////
	/** 参数不对 */
	public static final String ParameterErrorStr = "操作失败了，请再次操作！";
	/** 数据库错误 */
	public static final String DatabaseErrorStr = "操作失败了，请再次操作！";
	/** 证书错误 */
	public static final String CertificateErrorStr = "操作超时！";
	/** 不正确的密码或用户未注册 */
	public static final String PWErrorOrNotRegisteStrr = "密码错误或者此用户不存在！";
	/** 短信验证错误 */
	public static final String SMSErrorStr = "短信验证码错误！";
	/** 消息认证码发送错误 */
	public static final String VerifyCodeSendErrorStr = "短信验证码发送错误！请重新获取！";
	/** 已经注册过了 */
	public static final String AlreadyRegisteredStr = "手机号已注册，直接登录吧！";
	/** 访问类型码错误 */
	public static final String TypeCodeErrorStr = "访问出错啦！";
	/** 用户未登录 */
	public static final String NotLoggedInStr = "用户未登陆！";
	/** 图片上传错误 */
	public static final String ImageUploadFailedStr = "图片上传失败啦！请重新上传";
	/** 用户不在用户信息 */
	public static final String UserNotExistStr = "用户个人信息还是空哒！";
	/** 用户未注册 */
	public static final String NotRegisteredStr = "手机号尚未注册，快去注册入坑吧！";
	/**
	 * 无用户测量信息
	 * */
	public static final String NotRulerMeasureDataStr = "测试历史是空哒，快去测一测吧！";
	/**
	 * 无鞋柜信息
	 * */
	public static final String NotShoeBoxDataStr = "当前鞋柜的空空，萌不起来啦，请添加鞋子!";
	/**
	 * token过期
	 * */
	public static final String OverdueTokenStr = "token过期";
	/** 二维码无法使用 */
	public static final String SweepCodeErrorStr = "二维码无法使用";

	/**
	 * 判断是否正确返回
	 * 
	 * @param error
	 * @return true,正确；false,错误
	 */
	public static boolean judgeError(String error, String mark) {
		if (error.equals(ParameterError)) {
			return false;
		} else if (error.equals(DatabaseError)) {
			return false;
		} else if (error.equals(CertificateError)) {
			return false;
		} else if (error.equals(PWErrorOrNotRegister)) {
			return false;
		} else if (error.equals(SMSError)) {
			return false;
		} else if (error.equals(VerifyCodeSendError)) {
			return false;
		} else if (error.equals(AlreadyRegistered)) {
			return false;
		} else if (error.equals(TypeCodeError)) {
			return false;
		} else if (error.equals(NotLoggedIn)) {
			return false;
		} else if (error.equals(ImageUploadFailed)) {
			return false;
		} else if (error.equals(UserNotExist)) {
			return false;
		} else if (error.equals(NotRegistered)) {
			return false;
		} else if (error.equals(NotRulerMeasureData)) {
			return false;
		} else if (error.equals(NotShoeBoxData)) {
			return false;
		} else if (error.equals(OverdueToken)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断返回错误类型
	 */
	public static String judgeErrorString(String error) {
		String str = "";
		if (error.equals(ParameterError)) {
			str = ParameterErrorStr;
			return str;
		} else if (error.equals(DatabaseError)) {
			str = DatabaseErrorStr;
			return str;
		} else if (error.equals(CertificateError)) {
			str = CertificateErrorStr;
			return str;
		} else if (error.equals(PWErrorOrNotRegister)) {
			str = PWErrorOrNotRegisteStrr;
			return str;
		} else if (error.equals(SMSError)) {
			str = SMSErrorStr;
			return str;
		} else if (error.equals(VerifyCodeSendError)) {
			str = VerifyCodeSendErrorStr;
			return str;
		} else if (error.equals(AlreadyRegistered)) {
			str = AlreadyRegisteredStr;
			return str;
		} else if (error.equals(TypeCodeError)) {
			str = TypeCodeErrorStr;
			return str;
		} else if (error.equals(NotLoggedIn)) {
			str = NotLoggedInStr;
			return str;
		} else if (error.equals(ImageUploadFailed)) {
			str = ImageUploadFailedStr;
			return str;
		} else if (error.equals(UserNotExist)) {
			str = UserNotExistStr;
			return str;
		} else if (error.equals(NotRegistered)) {
			str = NotRegisteredStr;
			return str;
		} else if (error.equals(NotRulerMeasureData)) {
			str = NotRulerMeasureDataStr;
			return str;
		} else if (error.equals(NotShoeBoxData)) {
			str = NotShoeBoxDataStr;
			return str;
		} else if (error.equals(OverdueToken)) {
			str = OverdueTokenStr;
			return str;
		} else if (error.equals(SweepCodeError)) {
			str = SweepCodeErrorStr;
			return str;
		}
		return "";
	}

	/**
	 * 判断是否错误返回，不含-0x08证书错误
	 * 
	 * @param error
	 * @return true,正确；false,错误
	 */
	public static boolean unjudgeError(String error) {
		if (error.equals(ParameterError)) {
			return true;
		} else if (error.equals(DatabaseError)) {
			return true;
		} else if (error.equals(PWErrorOrNotRegister)) {
			return true;
		} else if (error.equals(SMSError)) {
			return true;
		} else if (error.equals(VerifyCodeSendError)) {
			return true;
		} else if (error.equals(AlreadyRegistered)) {
			return true;
		} else if (error.equals(TypeCodeError)) {
			return true;
		} else if (error.equals(NotLoggedIn)) {
			return true;
		} else if (error.equals(ImageUploadFailed)) {
			return true;
		} else if (error.equals(UserNotExist)) {
			return true;
		} else if (error.equals(NotRegistered)) {
			return true;
		} else if (error.equals(NotRulerMeasureData)) {
			return true;
		} else if (error.equals(NotShoeBoxData)) {
			return true;
		} else if (error.equals(OverdueToken)) {
			return true;
		}
		return false;
	}

}