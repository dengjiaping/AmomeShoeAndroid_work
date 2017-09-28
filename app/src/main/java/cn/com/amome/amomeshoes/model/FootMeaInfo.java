package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 足部报告相关信息
 */
public class FootMeaInfo {
	public static final String TAG = "FootMeaInfo";
	
	@SerializedName("createtime") 
	public String createtime;
	
	@SerializedName("useid")
	public String useid;
	
	@SerializedName("nickname")
	public String nickname;
	
	@SerializedName("spin")
	public String spin;
	
	@SerializedName("turn")
	public String turn;
	
	@SerializedName("ldot64")
	public String ldot64;
	
	@SerializedName("rdot64")
	public String rdot64;
	
	@SerializedName("turnreport")
	public String turnreport;
	
	@SerializedName("spinreport")
	public String spinreport;
}
