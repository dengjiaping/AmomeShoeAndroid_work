package cn.com.amome.amomeshoes.model;

/**
 * 存放站一站左右脚压力信息和病症
 */
public class StandDetectionModel {
	private float leftScale, rightScale;
	private String disease = "";
	private String disease2 = "";

	public void setLeftScale(float leftScale) {
		this.leftScale = leftScale;
	}

	public float getLeftScale() {
		return leftScale;
	}

	public void setRightScale(float rightScale) {
		this.rightScale = rightScale;
	}

	public float getRightScale() {
		return rightScale;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease2(String disease2) {
		this.disease2 = disease2;
	}

	public String getDisease2() {
		return disease2;
	}
}
