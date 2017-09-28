package cn.com.amome.amomeshoes.events;

/** 我的鞋刪除鞋原因使用，传消息给鞋列表 */
public class ShoeDelEvent {
	private String msg;//消息

	public ShoeDelEvent(String msg) {
		// TODO Auto-generated constructor stub
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
