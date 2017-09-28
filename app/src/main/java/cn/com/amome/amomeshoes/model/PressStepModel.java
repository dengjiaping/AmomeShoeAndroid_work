package cn.com.amome.amomeshoes.model;

/**
 * 存放脚落地抬起和步幅时间信息
 * @author jyw
 *
 */
public class PressStepModel {
	private float strideTime;// 步幅时间
	private long startTime, strideTimeL;// 落地起始时间

	public float getStrideTime() {
		return strideTime;
	}

	public void setStrideTime(float strideTime) {
		this.strideTime = strideTime;
	}

	public long getStrideTimeL() {
		return strideTimeL;
	}

	public void setStrideTimeL(long strideTimeL) {
		this.strideTimeL = strideTimeL;
		this.strideTime = strideTimeL/1000;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
