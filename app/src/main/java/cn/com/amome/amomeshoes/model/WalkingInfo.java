package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 步态报告接口使用，存放相关信息
 */
public class WalkingInfo {
	public static final String TAG = "WalkingInfo";

	@SerializedName("create_time")
	public String create_time;

	@SerializedName("useid")
	public String useid;

	@SerializedName("nickname")
	public String nickname;

	@SerializedName("symmetry")
	public String symmetry;

	@SerializedName("gait")
	public String gait;

	@SerializedName("l_pacetime")
	public String l_pacetime;

	@SerializedName("l_stridetime")
	public String l_stridetime;

	@SerializedName("l_stridefqc")
	public String l_stridefqc;

	@SerializedName("l_unisptphase")
	public String l_unisptphase;

	@SerializedName("l_bisptphase")
	public String l_bisptphase;

	@SerializedName("l_stdphase")
	public String l_stdphase;

	@SerializedName("l_swingphase")
	public String l_swingphase;

	@SerializedName("r_pacetime")
	public String r_pacetime;

	@SerializedName("r_stridetime")
	public String r_stridetime;

	@SerializedName("r_stridefqc")
	public String r_stridefqc;

	@SerializedName("r_unisptphase")
	public String r_unisptphase;

	@SerializedName("r_bisptphase")
	public String r_bisptphase;

	@SerializedName("r_stdphase")
	public String r_stdphase;

	@SerializedName("r_swingphase")
	public String r_swingphase;

	@SerializedName("symmetryreport")
	public String symmetryreport;

	@SerializedName("gaitreport")
	public String gaitreport;
}
