package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋品牌信息
 */
public class UserShoesBrandInfo {
	public static final String TAG = "UserShoesBrandInfo";

	@SerializedName("brand")
	public String brand;// 品牌

	@SerializedName("count")
	public String count;
}
