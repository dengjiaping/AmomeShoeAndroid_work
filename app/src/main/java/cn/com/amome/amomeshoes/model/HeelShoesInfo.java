package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋跟信息
 */
public class HeelShoesInfo {
	public static final String TAG = "HellheightShoes";
	@SerializedName("heel")
	public String heel;//品类

	@SerializedName("heelcount")
	public String heelcount;
}
