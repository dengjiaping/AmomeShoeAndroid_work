package cn.com.amome.amomeshoes.events;

/** 软件退出时通知主页面关闭 */
public class ExitEvent {
	private String msg;//消息

	public ExitEvent(String msg) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
