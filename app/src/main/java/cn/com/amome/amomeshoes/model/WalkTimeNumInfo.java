package cn.com.amome.amomeshoes.model;

/**
 * 存放走一走步数、起止时间信息
 */
public class WalkTimeNumInfo {
	private int stepCount;
	private long startTime, finishTime;

	public int getStepCount() {
		return stepCount;
	}

	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
