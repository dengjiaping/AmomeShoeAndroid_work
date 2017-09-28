package cn.com.amome.amomeshoes.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 *我的鞋信息
 */
public class ShoeBoxList implements Serializable{
	public static final String TAG = "RulerDataPerson";
	@SerializedName("shbox")
	public String shbox;//id

	@SerializedName("useid")
	public String useid;//id

	@SerializedName("heel")
	public String heel;//跟高

	@SerializedName("class")
	public String category;//类型

	@SerializedName("texture")
	public String texture;//材质

	@SerializedName("problem")
	public String problem;//问题

	@SerializedName("shoesize")
	public String shoesize;//鞋码

	@SerializedName("picture")
	public String picture;//图片地址

	@SerializedName("brand")
	public String brand;//品牌
	
	@SerializedName("price")
	public String price;//价格
}
