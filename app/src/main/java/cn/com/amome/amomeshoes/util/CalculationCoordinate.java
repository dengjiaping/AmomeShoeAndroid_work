/**
 * @Title:CalculationCoordinate.java
 * @Description:TODO<计算点的坐标位置>
 * @author:css
 * @data:  2015-7-14 下午3:32:22
 */
package cn.com.amome.amomeshoes.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import cn.com.amome.amomeshoes.widget.CircleView;
import cn.com.amome.amomeshoes.widget.CircleView.CircleInfo;
import cn.com.amome.amomeshoes.widget.CircleView.LineInfo;
import cn.com.amome.amomeshoes.widget.CircleView.RecInfo;

public class CalculationCoordinate {
	private float randomX, randomX1;// 圆心横坐标
	private float randomY, randomY1;// 圆心纵坐标
	private float randomRadius;// 圆半径
	private double canvasConsultX;
	private double canvasConsultY;
	private int randomColor = Color.GRAY;// 画笔颜色
	float lineX, lineY;

	List<RecInfo> mCircleListLeftPoint = new ArrayList<RecInfo>();// 左边的点坐标数据
	List<RecInfo> mCircleListRightPoint = new ArrayList<RecInfo>();// 右边的点坐标数据

	/** 确定左边矩形坐标 */
	public void drawCircleLeft(int x, int y, double canvesHeightLeft,
			double ConsultX, double ConsultY, CircleView mCircleCanvasLeft) {
		// TODO Auto-generated method stub
		canvasConsultX = ConsultX;
		canvasConsultY = ConsultY;
		double yjg = 5 * canvasConsultY;
		int jg = 0; // 6
		float width_bc = 5;
		float height_bc = 5;
		// 第一行
		randomX = Float.parseFloat((x - 4) * canvasConsultX + "");
		randomY = Float.parseFloat(y * canvasConsultY + "");
		drawSevenRec(4, jg, randomX, randomY, 25 + width_bc, 40 + height_bc,
				"left");
		// 第二行
		randomX1 = Float.parseFloat((x - 40) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 24 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 28 + width_bc, 44 + height_bc,
				"left");
		// 第三行
		randomX = Float.parseFloat((x - 61) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 27 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX, randomY, 28 + width_bc, 44 + height_bc,
				"left");
		// 第4行
		randomX1 = Float.parseFloat((x - 83) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 27 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX1, randomY1, 35 + width_bc, 51 + height_bc,
				"left");
		// 第5行
		randomX = Float.parseFloat((x - 83) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 32 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX, randomY, 35 + width_bc, 50 + height_bc,
				"left");
		// 第6行
		randomX1 = Float.parseFloat((x - 74) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 31 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 39 + width_bc, 46 + height_bc,
				"left");
		// 第7行
		randomX = Float.parseFloat((x - 64) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 28 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 36 + width_bc, 46 + height_bc,
				"left");
		// 第8行
		randomX1 = Float.parseFloat((x - 47) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 28 * canvasConsultY) + "");
		drawSevenRec(3, jg, randomX1, randomY1, 30 + width_bc, 47 + height_bc,
				"left"); // +4
		// 第9行
		randomX = Float.parseFloat((x - 36) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 29 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 30 + width_bc, 45 + height_bc,
				"left"); // +3
		// 第10行
		randomX1 = Float.parseFloat((x - 22) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 28 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 28 + width_bc, 45 + height_bc,
				"left");
		// 第11行
		randomX = Float.parseFloat((x - 14) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 28 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 27 + width_bc, 44 + height_bc,
				"left");
		// 第12行
		randomX1 = Float.parseFloat((x - 2) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 27 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 26 + width_bc, 42 + height_bc,
				"left");
		// 第13行
		randomX = Float.parseFloat((x + 15) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 25 * canvasConsultY) + "");
		drawSevenRec(4, jg, randomX, randomY, 26 + width_bc, 40 + height_bc,
				"left");
	}

	/** 确定右边矩形坐标 */
	public void drawCircleRight(int x, int y, double canvesHeightRight,
			double ConsultX, double ConsultY, CircleView mCircleCanvasRight) {
		// TODO Auto-generated method stub
		canvasConsultX = ConsultX;
		canvasConsultY = ConsultY;
		double yjg = 5 * canvasConsultY;
		float width_bc = 5;
		float height_bc = 4;
		int jg = 0; // 6
		// 第一行
		randomX = Float.parseFloat((x - 25) * canvasConsultX + "");
		randomY = Float.parseFloat(y * canvasConsultY + "");
		drawSevenRec(4, jg, randomX, randomY, 25 + width_bc, 40 + height_bc,
				"right");
		// 第二行
		randomX1 = Float.parseFloat((x - 32) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 24 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 28 + width_bc, 44 + height_bc,
				"right");
		// 第三行
		randomX = Float.parseFloat((x - 45) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 27 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX, randomY, 28 + width_bc, 44 + height_bc,
				"right");
		// 第4行
		randomX1 = Float.parseFloat((x - 61) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 27 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX1, randomY1, 35 + width_bc, 51 + height_bc,
				"right");
		// 第5行
		randomX = Float.parseFloat((x - 60) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 32 * canvasConsultY) + "");
		drawSevenRec(6, jg, randomX, randomY, 35 + width_bc, 50 + height_bc,
				"right");
		// 第6行
		randomX1 = Float.parseFloat((x - 48) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 31 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 39 + width_bc, 46 + height_bc,
				"right");
		// 第7行
		randomX = Float.parseFloat((x - 44) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 28 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 36 + width_bc, 46 + height_bc,
				"right");
		// 第8行
		randomX1 = Float.parseFloat((x + 43) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 28 * canvasConsultY) + "");
		drawSevenRec(3, jg, randomX1, randomY1, 30 + width_bc, 47 + height_bc,
				"right"); // +4
		// 第9行
		randomX = Float.parseFloat((x - 38) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 29 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 30 + width_bc, 45 + height_bc,
				"right"); // +3
		// 第10行
		randomX1 = Float.parseFloat((x - 41) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 27 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 28 + width_bc, 46 + height_bc,
				"right");
		// 第11行
		randomX = Float.parseFloat((x - 46) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 28 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX, randomY, 27 + width_bc, 44 + height_bc,
				"right");
		// 第12行
		randomX1 = Float.parseFloat((x - 50) * canvasConsultX + "");
		randomY1 = Float.parseFloat((yjg + randomY + 27 * canvasConsultY) + "");
		drawSevenRec(5, jg, randomX1, randomY1, 26 + width_bc, 42 + height_bc,
				"right");
		// 第13行
		randomX = Float.parseFloat((x - 36) * canvasConsultX + "");
		randomY = Float.parseFloat((yjg + randomY1 + 25 * canvasConsultY) + "");
		drawSevenRec(4, jg, randomX, randomY, 26 + width_bc, 40 + height_bc,
				"right");
	}

	// /** for循环重复画点 */
	// private void drawSevenPoint(int num, int jg, float randomX, float
	// randomY,
	// CircleView mCircleCanvas, String type) {
	// // TODO Auto-generated method stub
	// for (int i = 1; i <= num; i++) {
	// float xjg = Float.parseFloat(jg * canvasConsultX + "");
	// CircleInfo circleInfo = new CircleInfo();
	// circleInfo.setX(randomX + (xjg * (i - 1)));
	// circleInfo.setY(randomY);
	// circleInfo.setRadius(randomRadius);
	// circleInfo.setColor(randomColor);
	// if (type.equals("left")) {
	// mCircleListLeftPoint.add(circleInfo);
	// } else if (type.equals("right")) {
	// mCircleListRightPoint.add(circleInfo);
	// }
	// }
	// }

	/**
	 * 添加矩形
	 * 
	 * @param jg
	 *            间隔
	 * */
	private void drawSevenRec(int num, int jg, float leftX, float topY,
			float width, float height, String type) {
		// TODO Auto-generated method stub
		for (int i = 1; i <= num; i++) {
			float xjg = Float.parseFloat(jg * canvasConsultX + "");
			float xwidth = Float.parseFloat(width * canvasConsultX + "");
			float xheight = Float.parseFloat(height * canvasConsultX + "");
			RecInfo recInfo = new RecInfo();
			recInfo.setleft(leftX + (xjg + xwidth) * (i - 1));
			recInfo.settop(topY);
			recInfo.setright(leftX + (xjg + xwidth) * (i - 1) + xwidth);
			recInfo.setbottom(topY + xheight);
			recInfo.setColor(randomColor);
			if (type.equals("left")) {
				mCircleListLeftPoint.add(recInfo);
			} else if (type.equals("right")) {
				mCircleListRightPoint.add(recInfo);
			}
		}
	}

	// 保存所有的数据点位置
	private void savePoint() {
		if (mCircleListLeftPoint != null && mCircleListLeftPoint.size() > 0) {

		}
	}

	public List<RecInfo> getCircleListLeft() {
		return mCircleListLeftPoint;
	}

	public List<RecInfo> getCircleListRight() {
		return mCircleListRightPoint;
	}

	/*
	 * public List<LineInfo> getLineListLeft() { return mLineListLeftPoint; }
	 * 
	 * public List<LineInfo> getLineListRight() { return mLineListRightPoint; }
	 */
}
