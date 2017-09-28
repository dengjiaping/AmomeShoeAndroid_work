package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋品牌信息
 */
public class Brand {
	public static final String TAG = "Brand";
	@SerializedName("brand")
	public String brand;//

	@SerializedName("brandcount")
	public String brandcount;//计数
}
