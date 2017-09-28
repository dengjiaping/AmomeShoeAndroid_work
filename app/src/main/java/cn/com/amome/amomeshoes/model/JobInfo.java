package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 职业信息
 */
public class JobInfo {
	public static final String TAG = "JobInfo";
	@SerializedName("jobnum")
	public String jobnum;// id

	@SerializedName("jobdes")
	public String jobdes;// 工种
}
