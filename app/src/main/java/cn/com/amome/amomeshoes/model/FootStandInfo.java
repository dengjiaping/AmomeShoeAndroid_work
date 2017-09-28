package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 站一站报告接口使用
 */
public class FootStandInfo {
	public static final String TAG = "FootStandInfo";

	@SerializedName("createtime")
	public String createtime;

	@SerializedName("useid")
	public String useid;

	@SerializedName("nickname")
	public String nickname;

	@SerializedName("spine")
	public String spine;

	@SerializedName("shoulder")
	public String shoulder;

	@SerializedName("ldot64")
	public String ldot64;

	@SerializedName("rdot64")
	public String rdot64;

	@SerializedName("spinereport")
	public String spinereport;

	@SerializedName("shoulderreport")
	public String shoulderreport;
}
