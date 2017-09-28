package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;
/**
 * 删除鞋原因信息
 */
public class ShoesDelReason {
	public static final String TAG = "ShoesDelReason";
	@SerializedName("id")
	public String id;//id

	@SerializedName("reason")
	public String reason;//原因
}
