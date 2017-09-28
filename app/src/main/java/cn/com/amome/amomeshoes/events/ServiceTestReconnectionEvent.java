package cn.com.amome.amomeshoes.events;

public class ServiceTestReconnectionEvent {
	private String msg;

	/** 3.1版本已弃用 */
	public ServiceTestReconnectionEvent(String msg) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
