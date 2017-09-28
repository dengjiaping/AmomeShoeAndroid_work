package cn.com.amome.amomeshoes.model;

/**
 * 存放蓝牙上报的64点数据和时间戳
 * @author jyw
 *
 */
public class PressData {
	private short[] pressData = new short[64];
	private long time;

	public PressData(short[] pressData, long time) {
		if (pressData.length >= 64) {
			for (int i = 0; i < 64; i++) {
				this.pressData[i] = pressData[i];
			}
		}
		this.time = time;
	}

	public PressData() {

	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public short[] getPressData() {
		return pressData;
	}

	public void setPressData(short[] pressData) {
		this.pressData = pressData;
	}
}
