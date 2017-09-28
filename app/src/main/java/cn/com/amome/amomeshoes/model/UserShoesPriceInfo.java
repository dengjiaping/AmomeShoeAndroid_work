package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋价格信息
 */
public class UserShoesPriceInfo {
	public static final String TAG = "UserShoesPriceInfo";

	@SerializedName("class")
	public String category;// 问题描述

	@SerializedName("count")
	public String count;
}
