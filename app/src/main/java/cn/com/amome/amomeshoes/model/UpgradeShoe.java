package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 智能鞋固件升级接口使用
 */
public class UpgradeShoe {
	public static final String TAG = "UpgradeShoe";

	@SerializedName("newest_verison")
	public String newest_verison;// 新的版本号

	@SerializedName("urgent")
	public String urgent;// 是否必须升级，0非必升，1必升

	@SerializedName("secret")
	public String secret;// 密钥

	@SerializedName("md5")
	public String md5;// 摘要

	@SerializedName("firmware_path")
	public String firmware_path;// 下载路径

	@SerializedName("create_time")
	public String create_time;// 创建时间

	@SerializedName("pcb_version")
	public String pcb_version;// 创建时间

	@SerializedName("upgrade_describe")
	public String upgrade_describe;// 描述

	@SerializedName("platform_version")
	public String platform_version;// 平台版本
}
