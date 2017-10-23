package cn.com.amome.amomeshoes.http;

public class ClientConstant {
	public static final int TIMEOUT = 5 * 1000;
	public static final int HANDLER_SUCCESS = 1;
	public static final int HANDLER_FAILED = 2;
	// 开发环境
	// public static final String BASE_URL =
	// "http://www.iamome.cn/AmomeWebApp/";
	// 发布版本
	public static final String BASE_URL = "http://www.iamome.cn/AmomeWebApp/";

	// //////////////短信类型码表////////////////////
	public static final String REGISTER_ID = "SMS_8205301";// 获取注册验证码id
	public static final String FORGETPW_ID = "SMS_8215367";// 获取忘记密码验证码id
	public static final String AUTHENTICATION_ID = "SMS_8185341";// 获取验证身份验证码id，已取消
	// //////////////访问类型码表//////////////////
	public static final String GET_TOKEN_TYPE = "0x01";// 获取验证码token
	public static final String VERIFY_TOKEN_TYPE = "0x02";// 验证token是否过期
	public static final String REGISTER_TYPE = "0x01";// 注册
	public static final String LOGIN_PHONE_TYPE = "0x01";// 登录-手机号
	public static final String CHANGEPW_TYPE = "0x02";// 修改密码
	public static final String FORGETPW_TYPE = "0x80";// 忘记密码
	public static final String GETFOOTSHAP_TYPE = "0x01";// 获取脚形数据
	public static final String GETFOOTTYPE_TYPE = "0x02";// 获取足型数据
	public static final String GETINTEGRAL = "0x20";// 获取积分
	public static final String SETINTEGRAL = "0x40";// 设置积分
	public static final String GETJOBLIST = "0x01";// 获取职业类型
	public static final String GETHOBBYLIST = "0x08";// 获取兴趣类型
	public static final String ADDINFOMATION = "0x02";// 添加个人信息类型
	public static final String GETLEXICON = "0x20";// 添加敏感词库
	public static final String GETINFOMATION = "0x04";// 获取个人信息类型
	public static final String REQUIT = "0x02";// 退出
	public static final String GETAPPINFO = "0x01";// 获取最新app信息
	public static final String GETSHOEBOX_TYPE = "0x10";// 获取鞋列表
	public static final String DELSHOE_TYPE = "0x20";// 删除鞋
	public static final String GETSHOECATEGORY_TYPE = "0x01";// 获取鞋类型
	public static final String GETSHOEHELL_TYPE = "0x02";// 获取鞋跟高
	public static final String GETSHOEMATERIAL_TYPE = "0x04";// 获取鞋材质
	public static final String GETSHOEPROBLEM_TYPE = "0x100";// 获取鞋问题
	public static final String GETSHOEREASON_TYPE = "0x10000";// 获取删除鞋的原因
	public static final String GETUSERSHOECATEGORY_TYPE = "0x08";// 获取用户鞋种类鞋的个数
	public static final String GETUSERSHOEBRAND_TYPE = "0x400";// 获取用户鞋品牌鞋的个数
	public static final String GETUSERSHOEPRO_TYPE = "0x800";// 获取用户鞋问题的个数
	public static final String GETUSERSHOEHEEL_TYPE = "0x8000";// 获取用户鞋问题的个数
	public static final String GETUSERSHOEPRICE_TYPE = "0x1000";// 获取用户鞋消费的情况
	public static final String GETUSERSHOEDELPROBLEM_TYPE = "0x4000";// 获取用户删除鞋问题的个数
	public static final String GETUSERSHOEDELCATEGORY_TYPE = "0x2000";// 获取用户删除种类鞋的个数
	public static final String ADDSHOE_TYPE = "0x40";// 添加鞋
	public static final String AUTHENTICATION_TYPE = "0x04";// 验证身份，已取消
	public static final String SHOEBRAND_TYPE = "0x200";// 鞋柜鞋品牌
	public static final String EDITSHOE_TYPE = "0x80";// 修改鞋信息
	public static final String GETSHOESIZETABLE_TYPE = "0x04";// 获取获取鞋码表
	public static final String ADDRULERDATA = "0x40";// 更新或添加量脚数据
	public static final String GETRULERDATABYID_TYPE = "0x80";// 根据userid获取量一量数据
	public static final String GETRULERDATABYNAME_TYPE = "0x10";// 根据姓名获取量脚数据
	public static final String GETBRAND_TYPE = "0x08";// 获取品牌对照表
	public static final String GETSUITABLESHOE_TYPE = "0x100";// 获取指定的足型数据
	public static final String GETGOODS_LIST_TYPE = "0x01";// 获取所有商品
	public static final String GETGOODS_BYSERVERID_TYPE = "0x02";// 根据服务权限id获取商品
	public static final String GETGOODS_SOFTWARE_TYPE = "0x04";// 根据服务权限id获取商品
	public static final String ADD_ORDER_TYPE = "0x01";// 产生支付订单
	public static final String INTEFRAL_PAY_TYPE = "0x02";// 积分支付
	public static final String GET_ORDER_BYSERVERID_TYPE = "0x01";// 根据服务权限id获取订单
	public static final String GETASSIST_TYPE = "0x10";// 获取爱心助手数据

	public static final String UPDATECHECKFOOT_TYPE = "0x01";// 更新或添加看一看数据
	public static final String GETCHECKFOOT_TYPE = "0x02";// 获取看一看数据
	public static final String GET_FOOT_ACHE_INFO_TYPE = "0x04";// 获取某种脚痛提示
	public static final String GET_FOOT_DISEASE_INFO_TYPE = "0x08";// 获取某种脚疾提示

	public static final String SOFTWAREORDER_TYPE = "0x02";// 获取软件订单
	public static final String SOFT_FREE_TYPE = "0x10";// 推送软件免费
	public static final String SWEEPCODE_TYPE = "0x08"; // 二维码验证软件使用权
	public static final String FEEDBACK_TYPE = "0x10"; // 问题反馈
	public static final String SETDAILYDATA = "0x01";// 上传日常数据
	public static final String GETDAILYDATA = "0x02";// 获取日常数据
	public static final String GETHISTDATA = "0x02";// 获取历史数据
	public static final String GETYESTERDATA = "0x04";// 获取昨日历史数据
	public static final String ADDMEAINFO_TYPE = "0x01";// 新增或更新用户蹲一蹲数据
	public static final String GETMEAINFO_TYPE = "0x02";// 获取用户蹲一蹲数据
	public static final String ADDSTANDINFO_TYPE = "0x01";// 新增或更新用户站一站数据
	public static final String GETSTANDINFO_TYPE = "0x02";// 获取用户站一站数据
	public static final String ADDSHAKEINFO_TYPE = "0x01";// 新增或更新用户摇一摇数据
	public static final String GETSHAKEINFO_TYPE = "0x02";// 获取用户摇一摇数据
	public static final String ADDWALKINFO_TYPE = "0x01";// 新增或更新用户走一走数据
	public static final String GETWALKINFO_TYPE = "0x02";// 获取用户走一走数据
	public static final String GET_PROMOTION_INFO_TYPE = "0x01";// 获取用户促进列表
	public static final String GET_PROMOTION_DETAIL_INFO_TYPE = "0x04";// 获取某种病症的配件养护训练方案的详细信息
	public static final String GET_PROMOTION_DETAIL = "0x40";// 获取某种病症的配件养护训练方案的(训练/配件/养护)信息
	public static final String ONE_KEY_ADD = "0x80";// 一键加入健康促进
	public static final String GET_FINISH_DATA = "0x100";// 获取训练、配件、养护的完成情况（加入健康促进后的）
	public static final String GET_DETAIL_DATA = "0x200";// 获取训练、配件、养护的详细数据（加入健康促进后的）
	public static final String GET_FINISH = "0x400";// 获取训练、配件、养护的完成数据（加入健康促进后的）
	public static final String GET_TRAINING_VIDEO = "0x800";// 获取训练的视频数据（加入健康促进后的）
	public static final String REMOVE_DISEASE = "0x1000";// 移除某项病症
	public static final String UPLOAD_NURSING = "0x2000";// 上传穿戴效果

	public static final String GETBLEDEV_TYPE = "0x08";// 获取某用户所有的蓝牙设备列表
	public static final String UNBINDBLEDEV_TYPE = "0x02";// 蓝牙解绑
	public static final String BINDBLEDEV_TYPE = "0x01";// 蓝牙绑定
	public static final String BINDERROR_TYPE = "0x10"; // 绑定失败
	public static final String BINDUSABLE_TYPE = "0x20"; // 蓝牙可用检测
	public static final String CHANGEBLEDEV_TYPE = "0x04";// 蓝牙切换
	public static final String UPBLEDEV_TYPE = "0x01";// 蓝牙升级
	// //////////////URL////////////////////
	/**
	 * 获取/验证token
	 */
	public static final String gettoken_Url = BASE_URL
			+ "amome.shoes.user.identify.php";
	/**
	 * 获取短信验证码地址
	 */
	public static final String verification_Url = BASE_URL
			+ "amome.shoes.send.verifycode.php";
	/**
	 * 注册地址
	 */
	public static final String REGISTER_URL = BASE_URL
			+ "amome.shoes.user.register.php";
	/**
	 * 登录/退出登录
	 */
	public static final String LOGIN_URL = BASE_URL
			+ "amome.shoes.user.login.php";
	/**
	 * 验证身份
	 */
	public static final String SETUSER_URL = BASE_URL
			+ "amome.shoes.user.setting.php";
	/**
	 * 修改密码地址
	 */
	public static final String CHANGEPW_URL = BASE_URL
			+ "amome.shoes.user.setting.php";
	/**
	 * 忘记密码地址
	 */
	public static final String FORGETPW_URL = BASE_URL
			+ "amome.shoes.user.setting.php";
	/**
	 * 获取积分/签到地址
	 */
	// public static final String INTEGRAL_URL = BASE_URL +
	// "amome.shoes.user.setting.php"; 暂时屏蔽
	/**
	 * 获取脚数据/脚形/脚型/量一量数据地址
	 */
	public static final String GET_FOOTDATA_URL = BASE_URL
			+ "amome.shoes.amount.php";
	/**
	 * 获取和设置用户信息/获取职业列表地址
	 */
	public static final String USERINFO_URL = BASE_URL
			+ "amome.shoes.user.info.php";
	/**
	 * app的下载地址
	 */
	public static final String APP_DOWNLOAD = BASE_URL
			+ "amome.shoes.android.upgrade.php";
	/**
	 * 鞋柜列表地址/添加鞋/鞋问题/鞋类型/鞋跟高/鞋材质/鞋品牌
	 */
	public static final String SHOEBOX_URL = BASE_URL
			+ "amome.shoes.shoebox.php";
	/**
	 * 获取商品
	 */
	public static final String GETGOODS_URL = BASE_URL
			+ "amome.shoes.goods.list.php";
	/**
	 * 添加支付订单
	 */
	public static final String ADDORDER_URL = BASE_URL
			+ "amome.shoes.goods.pay.php";
	/**
	 * 获取订单
	 */
	public static final String GETORDER_URL = BASE_URL
			+ "amome.shoes.goods.orders.php";

	/**
	 * 获取用户看一看数据
	 */
	public static final String GETCHECKFOOT_URL = BASE_URL
			+ "amome.shoes.checkfoot.php";
	/**
	 * 更新或添加看一看数据
	 */
	public static final String UPDATECHECKFOOT_URL = BASE_URL
			+ "amome.shoes.checkfoot.php";
	/**
	 * 获取软件订单
	 */
	public static final String SOFTWAREORDER_URL = BASE_URL
			+ "amome.shoes.goods.orders.php";
	/**
	 * 获取软件商品列表
	 */
	public static final String MSG_GETSOFTWAREGOODSLIST_URL = BASE_URL
			+ "amome.shoes.goods.list.php";
	/**
	 * 插入或更新用户日常数据
	 */
	public static final String DAILYDATA_URL = BASE_URL
			+ "amome.shoes.actvty.daily.php";
	/**
	 * 获取历史数据
	 */
	public static final String HISTDATA_URL = BASE_URL
			+ "amome.shoes.actvty.hist.php";
	/**
	 * 蓝牙模块接口
	 */
	public static final String BLEDEVICE_URL = BASE_URL
			+ "amome.shoes.blebind.php";
	/**
	 * 用户蹲一蹲接口
	 */
	public static final String MEASURE_URL = BASE_URL
			+ "amome.shoes.measure.php";
	/**
	 * 用户站一站接口
	 */
	public static final String STAND_URL = BASE_URL + "amome.shoes.stand.php";
	/**
	 * 用户摇一摇接口
	 */
	public static final String SHAKE_URL = BASE_URL + "amome.shoes.shake.php";
	/**
	 * 用户走一走接口
	 */
	public static final String WALK_URL = BASE_URL + "amome.shoes.walk.php";
	/**
	 * 健康促进接口
	 */
	public static final String PROMOTION_URL = BASE_URL
			+ "amome.shoes.health.promotion.php";
	/**
	 * 蓝牙固件升级接口
	 */
	public static final String BLEUPGRADE_URL = BASE_URL
			+ "amome.shoes.bluetooth.upgrade.php";

	/**
	 * activity页面活动模块接口
	 */
	public static final String ILLNESSINFO_URL=BASE_URL+"amome.shoes.health.promotion.php";
}
