package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 *  获取蓝牙订单信息接口使用
 * 
 * @author
 */
public class BleDeviceInfo {

	public static final String TAG = "BleDeviceInfo";

	@SerializedName("id")
	public String id;//id

	@SerializedName("useid")
	public String useid;//用户名

	@SerializedName("create_time")
	public String create_time;//绑定日期

	@SerializedName("lble")
	public String lble;//左脚蓝牙地址

	@SerializedName("rble")
	public String rble;//右脚蓝牙地址

	@SerializedName("state")
	public String state;//绑定状态。1：绑定 0：未绑定

	@SerializedName("name")
	public String name;//绑定鞋的名称

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
