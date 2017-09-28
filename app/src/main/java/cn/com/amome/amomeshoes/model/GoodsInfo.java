package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 商品信息
 * @author css
 */
public class GoodsInfo {
	public static final String TAG = "GoodsInfo";
	@SerializedName("goods_id")
	public String goods_id;//id

	@SerializedName("goods_sc")
	public String goods_sc;//评分

	@SerializedName("body")
	public String body;//商品名称

	@SerializedName("attach")
	public String attach;//收款方

	@SerializedName("detail")
	public String detail;//详情

	@SerializedName("tag")
	public String tag;//支付标签

	@SerializedName("price")
	public String price;//价格

	@SerializedName("term")
	public String term;//期限

	@SerializedName("state")
	public String state;//0下架，1上架

	@SerializedName("exchange")
	public String exchange;//积分兑换

	@SerializedName("discount")
	public String discount;//折扣

	@SerializedName("trial")
	public String trial;//试用天数

	@SerializedName("recommend")
	public String recommend;//推荐

	@SerializedName("limitime")
	public String limitime;//现时，0是不现时，1是现时

	@SerializedName("icon")
	public String icon;//展示图片
	
	@SerializedName("isqrspt")
	public String isqrspt;//是否允许二维码支付。0，不支持。1，支持
}
