package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 脚疾接口使用
 */
public class FootDiseaseInfo {
	public static final String TAG = "FootDiseaseInfo";

	@SerializedName("ftdiseaserea")
	public String ftdiseaserea;

	@SerializedName("ftdiseaseadv")
	public String ftdiseaseadv;
}
