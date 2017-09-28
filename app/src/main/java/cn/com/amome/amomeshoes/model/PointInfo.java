package cn.com.amome.amomeshoes.model;

/**
 * 存放所画的点信息，坐标和颜色
 * @author jyw
 *
 */
public class PointInfo {

	private float x; // 圆心横坐标
	private float y; // 圆心纵坐标
	private int color; // 画笔的颜色

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
