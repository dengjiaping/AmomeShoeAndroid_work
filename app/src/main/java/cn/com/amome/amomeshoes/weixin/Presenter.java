package cn.com.amome.amomeshoes.weixin;

import com.google.gson.annotations.SerializedName;

public class Presenter {

	@SerializedName("access_token")
	public String access_token;//接口调用凭证

	@SerializedName("expires_in")
	public String expires_in;//access_token接口调用凭证超时时间，单位（秒)

	@SerializedName("refresh_token")
	public String refresh_token;//用户刷新access_token

	@SerializedName("openid")
	public String openid;//授权用户唯一标识

	@SerializedName("scope")
	public String scope;//用户授权的作用域，使用逗号（,）分隔

	@SerializedName("errcode")
	public int errcode;//

	@SerializedName("errmsg")
	public String errmsg;//
}
