package cn.com.amome.amomeshoes.model;

import com.google.gson.annotations.SerializedName;

/**
 * 运动数据信息
 * 
 * @author
 */
public class ExerciseDataInfo {

	public static final String TAG = "ExerciseDataInfo";

	@SerializedName("useid")
	public String useid;//名称

	@SerializedName("date")
	public String date;//日期

	@SerializedName("sitclr")
	public int sitclr;//坐卡路里

	@SerializedName("stdclr")
	public int stdclr;//站卡路里

	@SerializedName("wlkclr")
	public int wlkclr;//走卡路里

	@SerializedName("sittme")
	public int sittme;//坐时间

	@SerializedName("stdtme")
	public int stdtme;//站时间

	@SerializedName("wlktme")
	public int wlktme;//走时间

	@SerializedName("rsttme")
	public int rsttme;//休眠时间

	@SerializedName("stpnum")
	public int stpnum;//步数

	@SerializedName("stpfqc")
	public float stpfqc;//步频

}
