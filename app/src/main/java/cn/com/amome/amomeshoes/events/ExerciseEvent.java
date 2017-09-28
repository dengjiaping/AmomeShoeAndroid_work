package cn.com.amome.amomeshoes.events;

/** 通知运动页面智能鞋重连 */
public class ExerciseEvent {
	private String msg;//消息

	public ExerciseEvent(String msg) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
