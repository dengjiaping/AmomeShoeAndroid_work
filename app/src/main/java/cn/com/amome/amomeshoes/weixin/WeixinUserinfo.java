package cn.com.amome.amomeshoes.weixin;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class WeixinUserinfo {

	@SerializedName("openid")
	public String openid;//授权用户唯一标识

	@SerializedName("nickname")
	public String nickname;//

	@SerializedName("sex")
	public String sex;//普通用户性别，1为男性，2为女性

	@SerializedName("province")
	public String province;//

	@SerializedName("city")
	public String city;//

	@SerializedName("country")
	public String country;//

	@SerializedName("headimgurl")
	public String headimgurl;//

	@SerializedName("privilege")
	public List<privilegeList> privilegeList;//用户特权信息，json数组，如微信沃卡用户为（chinaunicom）

	@SerializedName("unionid")
	public String unionid;//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的

	@SerializedName("errcode")
	public String errcode;//

	@SerializedName("errmsg")
	public String errmsg;//

	public class privilegeList{

	}
}
