package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 爱好接口使用
 */
public class HobbyInfo {
	public static final String TAG = "JobInfo";
	@SerializedName("hobbynum")
	public String hobbynum;// id

	@SerializedName("hobbysdes")
	public String hobbysdes;// 兴趣
}
