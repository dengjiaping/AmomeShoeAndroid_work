package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋跟信息
 */
public class UserShoesHeelInfo {
	public static final String TAG = "UserShoesHeelInfo";

	@SerializedName("heel")
	public String heel;// 鞋跟

	@SerializedName("count")
	public String count;
}
