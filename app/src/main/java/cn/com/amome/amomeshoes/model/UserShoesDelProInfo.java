package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 删除鞋的原因的信息
 */
public class UserShoesDelProInfo {
	public static final String TAG = "UserShoesDelProInfo";

	@SerializedName("reason")
	public String reason;// 原因

	@SerializedName("count")
	public String count;
}
