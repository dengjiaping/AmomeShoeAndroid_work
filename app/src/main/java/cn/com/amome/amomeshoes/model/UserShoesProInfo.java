package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋问题信息
 */
public class UserShoesProInfo {
	public static final String TAG = "UserShoesProInfo";

	@SerializedName("prodes")
	public String prodes;// 问题描述

	@SerializedName("count")
	public String count;
}
