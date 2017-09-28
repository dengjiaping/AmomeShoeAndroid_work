package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 用户信息
 */
public class UserInfo {
	public static final String TAG = "UserInfo";
	@SerializedName("useid")
	public String useid;// id

	@SerializedName("nickname")
	public String nickname;// 昵称

	@SerializedName("birthday")
	public String birthday;// 生日

	@SerializedName("sex")
	public String sex;// 性别

	@SerializedName("height")
	public String height;// 身高

	@SerializedName("weight")
	public String weight;// 体重

	@SerializedName("job")
	public String job;// 职业

	@SerializedName("icon")
	public String icon;// 头像

	// @SerializedName("signature")
	// public String signature;//个人签名
	//
	@SerializedName("hobby")
	public String hobby;// 兴趣

	// @SerializedName("mail")
	// public String mail;//邮箱
	//
	@SerializedName("location")
	public String location;// 地址
	
	@SerializedName("telephone")
	public String telephone;// 联系电话
	
	@SerializedName("name")
	public String realname;// 真实姓名
	
	@SerializedName("postalcode")
	public String postalcode;// 邮编

}
