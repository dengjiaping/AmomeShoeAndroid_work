package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 暂时未用
 */
public class PromotionInfo {
	public static final String TAG = "PromotionInfo";

	@SerializedName("type")
	public String type;

	@SerializedName("content")
	public String content;

	@SerializedName("is_user_related")
	public String is_user_related;
}
