package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋种类信息
 */
public class UserShoesCategoryInfo {
	public static final String TAG = "UserShoesCategoryInfo";

	@SerializedName("type")
	public String type;// 品类

	@SerializedName("count")
	public String count;
}
