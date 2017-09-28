package cn.com.amome.amomeshoes.model;

/**
 * 走一走特征数据，存放压力值、名称和时间
 * @author jyw
 *
 */
public class PressCharacterModel {
	private short backValue, medValue, frontValue;
	private long time;
	private String name;

	public short getBackValue() {
		return backValue;
	}

	public void setBackValue(short backValue) {
		this.backValue = backValue;
	}

	public short getMedValue() {
		return medValue;
	}

	public void setMedValue(short medValue) {
		this.medValue = medValue;
	}

	public short getFrontValue() {
		return frontValue;
	}

	public void setFrontValue(short frontValue) {
		this.frontValue = frontValue;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
