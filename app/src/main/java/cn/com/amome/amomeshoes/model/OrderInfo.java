package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;


/**
 * 订单信息
 * @author css
 */
public class OrderInfo {

	public static final String TAG = "OrderInfo";
	@SerializedName("order_id")
	public String order_id;//订单id

	@SerializedName("goods_id")
	public String goods_id;//id

	@SerializedName("useid")
	public String useid;//

	@SerializedName("state")
	public String state;//付款状态

	@SerializedName("create_time")
	public String create_time;//

	@SerializedName("money")
	public String money;//钱

	@SerializedName("exchange")
	public String exchange;//积分兑换

	@SerializedName("trade")
	public String trade;//订单编号

	@SerializedName("experiences")
	public String experiences;//体验

	@SerializedName("address")
	public String address;//收货地址

	@SerializedName("ret_address")
	public String ret_address;//退货地址

	@SerializedName("ret_money")
	public String ret_money;//退款

	@SerializedName("ret_exper")
	public String ret_exper;//退回积分

	@SerializedName("ret_problem")
	public String ret_problem;//退货问题

	@SerializedName("ret_score")
	public String ret_score;//退货评分

	@SerializedName("liable_num")
	public String liable_num;//服务人员

	@SerializedName("available")
	public String available;//

}
