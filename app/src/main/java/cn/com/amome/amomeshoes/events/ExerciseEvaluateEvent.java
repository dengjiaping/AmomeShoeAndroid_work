package cn.com.amome.amomeshoes.events;

/** 向运动评估页面传递相关数据 */
public class ExerciseEvaluateEvent {
	private String msg;//消息
	private float degree;// 旋转角度
	private float[] cal_evaluate;// 坐站走卡路里

	public ExerciseEvaluateEvent(String msg, int degree, float[] cal_evaluate) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
		this.degree = degree;
		this.cal_evaluate = cal_evaluate;//
	}

	public String getMsg() {
		return msg;
	}

	public float getDegree() {
		return degree;
	}

	public float[] getTime() {
		return cal_evaluate;
	}
}
