package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 删除鞋的种类信息
 */
public class UserShoesDelCategoryInfo {
	public static final String TAG = "UserShoesDelCategoryInfo";

	@SerializedName("type")
	public String type;// 品类

	@SerializedName("count")
	public String count;
}
