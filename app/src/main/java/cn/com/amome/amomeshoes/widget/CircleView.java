/**
 * @Title:CircleView.java
 * @Description:自定义画圆
 * @author:css
 * @data:  2015-6-3 上午11:55:48
 */
package cn.com.amome.amomeshoes.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleView extends View {

	private static final String TAG = "CircleView";
	public List<RecInfo> mCircleListLeftInfo = new ArrayList<RecInfo>();// 左边的点坐标数据

	public List<RecInfo> mCircleListRightInfo = new ArrayList<RecInfo>();// 右边的点坐标数据
	private int num = 0;
	int size = 0;
	boolean initInfo = false;
	private Handler mHandler = new Handler();

	public CircleView(Context context) {
		super(context);
		Log.e(TAG, "####### CircleView(Context context)");
		// TODO Auto-generated constructor stub
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e(TAG, "####### CircleView(Context context, AttributeSet attrs)");
		// TODO Auto-generated constructor stub
		// mPressData = new PressData();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/* 设置背景为白色 */
		canvas.drawColor(Color.TRANSPARENT);

		if (mCircleListLeftInfo != null && mCircleListLeftInfo.size() > 0) {
			size = 0;
			drawCilrcleLeft(canvas, mCircleListLeftInfo);
		}

		if (mCircleListRightInfo != null && mCircleListRightInfo.size() > 0) {
			size = 0;
			drawCilrcleRight(canvas, mCircleListRightInfo);
		}

		mHandler.postDelayed(mRunnable, 100);

	}

	private Runnable mRunnable = new Runnable() {
		public void run() {
			// Log.i(TAG, ">>>>>> mRunnable");
			invalidate();
		}
	};

	public void drawCilrcleLeft(Canvas canvas, List<RecInfo> mCircleInfos) {
		// Log.e("222", "---------drawCilrcleLeft-----------");
		Paint paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		/* 设置paint的 style STROKE为空心 FILL为实心 */
		paint.setStyle(Paint.Style.FILL);
		byte Press[] = null; // cca.mPressData.getPressDataLeft();
		for (int i = 0; i < 64 /* Press.length */; i++) {
			RecInfo circleInfo = mCircleInfos.get(i);
			// 设置画笔颜色
			paint.setColor(circleInfo.getColor());
			// Log.i(TAG, "L circl color: " + circleInfo.getColor());
			// 绘制实心圆
			canvas.drawRect(circleInfo.getleft(), circleInfo.gettop(),
					circleInfo.getright(), circleInfo.getbottom(), paint);
		}
	}

	public void drawCilrcleRight(Canvas canvas, List<RecInfo> mCircleInfos) {
		// Log.e("222", "---------drawCilrcleRight-----------");
		Paint paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		/* 设置paint的 style STROKE为空心 FILL为实心 */
		paint.setStyle(Paint.Style.FILL);
		byte Press[] = null;

		for (int i = 0; i < 64; i++) {
			RecInfo circleInfo = mCircleInfos.get(i);
			paint.setColor(circleInfo.getColor());
			// 绘制实心圆
			// canvas.drawCircle(circleInfo.getX(),
			// circleInfo.getY(),circleInfo.getRadius(), paint); 临时屏蔽
			canvas.drawRect(circleInfo.getleft(), circleInfo.gettop(),
					circleInfo.getright(), circleInfo.getbottom(), paint);
		}
	}

	// 20--380开始变193.0.0
	// public static final short mMaxAdcVal = 720;
	// public static final short mUnitADC = mMaxAdcVal / 36;// 17
	// public static int[] mCircleTable = { Color.argb(0, 255, 255, 255),
	// Color.rgb(0, 0, 255), Color.rgb(0, 64, 255),
	// Color.rgb(0, 129, 255), Color.rgb(0, 193, 255),
	// Color.rgb(0, 255, 255), Color.rgb(0, 255, 193),
	// Color.rgb(0, 255, 129), Color.rgb(0, 255, 0),
	// Color.rgb(129, 255, 0), Color.rgb(193, 255, 0),
	// Color.rgb(255, 255, 0), Color.rgb(255, 193, 0),
	// Color.rgb(255, 129, 0), Color.rgb(255, 129, 0),
	// Color.rgb(255, 64, 0), Color.rgb(255, 64, 0), Color.rgb(255, 0, 0),
	// Color.rgb(255, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0) };
	//
	// public static int getCircleColor(short cl) {
	// int ind = cl / mUnitADC;
	// if (ind >= 35)
	// ind = 35;
	// return mCircleTable[ind];
	// }

	// 50-400开始255.0.0
	public static final short mMaxAdcVal = 720;
	public static final short mUnitADC = mMaxAdcVal / 28;// 17
	// private static int[] mCircleTable = { Color.argb(0, 255, 255, 255),
	// Color.argb(0, 255, 255, 255), Color.rgb(0, 0, 255),
	// Color.rgb(0, 64, 255), Color.rgb(0, 129, 255),
	// Color.rgb(0, 193, 255), Color.rgb(0, 255, 255),
	// Color.rgb(0, 255, 193), Color.rgb(0, 255, 129),
	// Color.rgb(0, 255, 0), Color.rgb(129, 255, 0),
	// Color.rgb(193, 255, 0), Color.rgb(255, 255, 0),
	// Color.rgb(255, 193, 0), Color.rgb(255, 129, 0),
	// Color.rgb(255, 129, 0), Color.rgb(255, 64, 0),
	// Color.rgb(255, 64, 0), Color.rgb(255, 0, 0), Color.rgb(255, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0) };
	// 去除了两种颜色
	public static int[] mCircleTable = { Color.argb(0, 255, 255, 255),
			Color.argb(0, 255, 255, 255), Color.rgb(0, 0, 255),
			Color.rgb(0, 64, 255), Color.rgb(0, 129, 255),
			Color.rgb(0, 193, 255), Color.rgb(0, 255, 255),
			Color.rgb(0, 255, 193), Color.rgb(129, 255, 0),
			Color.rgb(193, 255, 0), Color.rgb(255, 255, 0),
			Color.rgb(255, 193, 0), Color.rgb(255, 129, 0),
			Color.rgb(255, 129, 0), Color.rgb(255, 64, 0),
			Color.rgb(255, 64, 0), Color.rgb(255, 0, 0), Color.rgb(255, 0, 0),
			Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
			Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
			Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
			Color.rgb(193, 0, 0) };

	public static int getCircleColor(short cl) {
		if (cl < 0) {
			return mCircleTable[0];
		} else {
			int ind = cl / mUnitADC;
			if (ind >= 27)
				ind = 27;
			return mCircleTable[ind];
		}
	}

	// private static final short mMaxAdcVal = 720;
	// private static final short mUnitADC = mMaxAdcVal / 24;// 17
	// private static int[] mCircleTable = { Color.argb(0, 255, 255, 255),
	// Color.rgb(0, 0, 255), Color.rgb(0, 64, 255),
	// Color.rgb(0, 129, 255), Color.rgb(0, 193, 255),
	// Color.rgb(0, 255, 255), Color.rgb(0, 255, 193),
	// Color.rgb(0, 255, 129), Color.rgb(0, 255, 0),
	// Color.rgb(129, 255, 0), Color.rgb(193, 255, 0),
	// Color.rgb(255, 255, 0), Color.rgb(255, 193, 0),
	// Color.rgb(255, 129, 0), Color.rgb(255, 129, 0),
	// Color.rgb(255, 64, 0), Color.rgb(255, 64, 0), Color.rgb(255, 0, 0),
	// Color.rgb(255, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0),
	// Color.rgb(193, 0, 0), Color.rgb(193, 0, 0), Color.rgb(193, 0, 0) };
	//
	// private int getCircleColor(short cl) {
	// int ind = cl / mUnitADC;
	// if (ind >= 23)
	// ind = 23;
	// return mCircleTable[ind];
	// }
	public void setLeftCircleInfoColor(short[] pd) {
		int i = 0;
		if (pd.length == mCircleListLeftInfo.size()) {
			for (RecInfo mCirclInfo : mCircleListLeftInfo) {
				mCirclInfo.setColor(getCircleColor(pd[i++]));
			}
		}
	}

	public void setRightCircleInfoColor(short[] pd) {
		int i = 0;
		if (pd.length == mCircleListRightInfo.size()) {
			for (RecInfo mCirclInfo : mCircleListRightInfo) {
				mCirclInfo.setColor(getCircleColor(pd[i++]));
			}
		}
	}

	// 保存实心矩形相关信息的类
	public static class RecInfo {
		private float left; // 左边距y轴距离
		private float top; // 上边距x轴距离
		private float right; // 右边距y轴距离
		private float bottom; // 下边距x轴距离
		private int color; // 画笔的颜色

		public float getleft() {
			return left;
		}

		public void setleft(float left) {
			this.left = left;
		}

		public float gettop() {
			return top;
		}

		public void settop(float top) {
			this.top = top;
		}

		public float getright() {
			return right;
		}

		public void setright(float right) {
			this.right = right;
		}

		public float getbottom() {
			return bottom;
		}

		public void setbottom(float bottom) {
			this.bottom = bottom;
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public RecInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// 保存实心圆相关信息的类
	public static class CircleInfo {
		private float x; // 圆心横坐标
		private float y; // 圆心纵坐标
		private float radius; // 半径
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

		public float getRadius() {
			return radius;
		}

		public void setRadius(float radius) {
			this.radius = radius;
		}

		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public CircleInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	// 线的实体类
	public static class LineInfo {
		private float x; // 横坐标
		private float y; // 纵坐标
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

		public LineInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}