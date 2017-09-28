package cn.com.amome.amomeshoes.events;

/** 向运动雷达图页面传递相关数据 */
public class ExerciseRadarEvent {
	private String msg;//消息
	private float[] radar;//雷达图所需数据（步数、步频、坐时间、站时间、总消耗、走时间）

	public ExerciseRadarEvent(String msg, float[] radar) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
		this.radar = radar;
	}

	public String getMsg() {
		return msg;
	}

	public float[] getRadar() {
		return radar;
	}
}
