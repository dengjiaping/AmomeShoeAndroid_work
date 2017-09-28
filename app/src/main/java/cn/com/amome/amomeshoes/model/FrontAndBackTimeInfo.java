package cn.com.amome.amomeshoes.model;

/**
 * 存放前脚尖和后脚跟信息
 */
public class FrontAndBackTimeInfo {
	private long frontTime, backTime;

	public void setFrontTime(long frontTime) {
		this.frontTime = frontTime;
	}

	public void setBackTime(long backTime) {
		this.backTime = backTime;
	}

	public long getFrontTime() {
		return frontTime;
	}

	public long getBackTime() {
		return backTime;
	}
}
