package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 量量脚接口使用
 */
public class FootRulerInfo {
	public static final String TAG = "RulerModel";
	@SerializedName("createtime")
	public String createtime;
	
	@SerializedName("useid")
	public String useid;//
	
	@SerializedName("nickname")
	public String nickname;//
	
	@SerializedName("rftlong")
	public String rftlong;//
	
	@SerializedName("rftwidth")
	public String rftwidth;//
	
	@SerializedName("lftlong")
	public String lftlong;//
	
	@SerializedName("lftwidth")
	public String lftwidth;//
}
