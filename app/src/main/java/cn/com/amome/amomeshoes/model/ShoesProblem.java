package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋问题信息
 */
public class ShoesProblem {
	public static final String TAG = "ShoesProblem";
	@SerializedName("prodes")
	public String prodes;//描述

	@SerializedName("metering")
	public String metering;//分数

	@SerializedName("procount")
	public String procount;//计数

	@SerializedName("prodesnum")
	public String prodesnum;//id
}
