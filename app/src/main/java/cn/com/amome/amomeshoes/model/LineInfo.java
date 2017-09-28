package cn.com.amome.amomeshoes.model;

/**
 *  线条起止坐标颜色信息
 */
public class LineInfo {

	private float x1; // 起始坐标
	private float y1; // 起始坐标
	private float x2; // 结束坐标
	private float y2; // 结束坐标
	private int color; // 画笔的颜色

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}
	
	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
