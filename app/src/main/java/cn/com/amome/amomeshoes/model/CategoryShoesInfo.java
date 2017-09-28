package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 鞋种类信息
 */
public class CategoryShoesInfo {
	public static final String TAG = "CategoryShoes";

	@SerializedName("class")
	public String category;// 品类

	@SerializedName("classcount")
	public String classcount;
}
