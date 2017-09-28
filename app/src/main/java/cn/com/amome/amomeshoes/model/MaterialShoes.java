package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 *  鞋材质信息
 *
 */
public class MaterialShoes {
	public static final String TAG = "MaterialShoes";
	@SerializedName("texture")
	public String material;//材质

	@SerializedName("texturecount")
	public String texturecount;
}
