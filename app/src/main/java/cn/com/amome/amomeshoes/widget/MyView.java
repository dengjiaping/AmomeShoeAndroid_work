//package cn.com.amome.amomeshoes.widget;
//
//import java.util.ArrayList;
//import java.util.List;
//import cn.com.amome.amomeshoes.model.PointInfo;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//
//public class MyView extends SurfaceView implements SurfaceHolder.Callback {
//	private String TAG = "MyView";
//	public List<PointInfo> leftPointList = new ArrayList<PointInfo>();
//	public List<PointInfo> rightPointList = new ArrayList<PointInfo>();
//	public List<RectF> leftRectfList = new ArrayList<RectF>();
//	public List<RectF> rightRectfList = new ArrayList<RectF>();
//	private int jg = 8;
//	int size = 0;
//	private int i = 0;
//	private int j = 0;
//	private Handler mHandler = new Handler();
//	SurfaceHolder holder;
//	boolean mbLoop = false;
//
//	public MyView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		leftRectfList.clear();
//		rightRectfList.clear();
//		mbLoop = true;
//		holder = this.getHolder();// 获取holder
//		holder.addCallback(this);
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		new Thread(new MyThread()).start();
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		mbLoop = false;
//	}
//
//	@SuppressLint("WrongCall")
//	protected void doDrawLeft() {
//		// 设置画布的背景颜色
//		Canvas canvas = holder.lockCanvas();
//		canvas.drawColor(Color.WHITE);
//		if (leftPointList != null && leftPointList.size() > 0) {
//			if (i == 0) {
//				drawPointLeft(canvas, leftPointList);
//			} else {
//				setLeftRecColor(canvas, leftPointList, leftRectfList);
//			}
//		}
//		holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
//
//		// mHandler.postDelayed(mRunnable, 0);
//		mHandler.post(mRunnable);
//	}
//
//	@SuppressLint("WrongCall")
//	protected void doDrawRight() {
//		Canvas canvas = holder.lockCanvas();
//		// 设置画布的背景颜色
//		canvas.drawColor(Color.WHITE);
//		if (rightPointList != null && rightPointList.size() > 0) {
//			if (j == 0) {
//				drawPointRight(canvas, rightPointList);
//			} else {
//				setRightRecColor(canvas, rightPointList, rightRectfList);
//			}
//		}
//		holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
//
//		// mHandler.postDelayed(mRunnable, 0);
//		mHandler.post(mRunnable);
//	}
//
//	private Runnable mRunnable = new Runnable() {
//		public void run() {
//			// Log.i(TAG, ">>>>>> mRunnable");
//			invalidate();
//		}
//	};
//
//	public void drawPointLeft(Canvas canvas, List<PointInfo> pointList) {
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setAntiAlias(true);
//		Log.i(TAG, "leftPointList的長度" + pointList.size());
//		for (int i = 0; i < pointList.size(); i++) {
//			PointInfo pointInfo = pointList.get(i);
//			RectF rectf = new RectF(pointInfo.getX(), pointInfo.getY(),
//					pointInfo.getX() + jg, pointInfo.getY() + jg);
//			leftRectfList.add(rectf);
//			paint.setColor(pointInfo.getColor());
//			canvas.drawRect(rectf, paint);
//		}
//	}
//
//	public void drawPointRight(Canvas canvas, List<PointInfo> pointList) {
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setAntiAlias(true);
//		Log.i(TAG, "rightPointList的長度" + pointList.size());
//		for (int i = 0; i < pointList.size(); i++) {
//			PointInfo pointInfo = pointList.get(i);
//			RectF rectf = new RectF(pointInfo.getX(), pointInfo.getY(),
//					pointInfo.getX() + jg, pointInfo.getY() + jg);
//			rightRectfList.add(rectf);
//			paint.setColor(pointInfo.getColor());
//			canvas.drawRect(rectf, paint);
//
//		}
//	}
//
//	private void setLeftRecColor(Canvas canvas, List<PointInfo> pointList,
//			List<RectF> recfList) {
//		Log.i(TAG, "设置左脚颜色" + pointList.size());
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setAntiAlias(true);
//		for (int i = 0; i < pointList.size(); i++) {
//			PointInfo pointInfo = pointList.get(i);
//			paint.setColor(pointInfo.getColor());
//			canvas.drawRect(recfList.get(i), paint);
//		}
//	}
//
//	private void setRightRecColor(Canvas canvas, List<PointInfo> pointList,
//			List<RectF> recfList) {
//		Log.i(TAG, "设置右脚颜色" + pointList.size());
//		Paint paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setAntiAlias(true);
//		for (int i = 0; i < pointList.size(); i++) {
//			PointInfo pointInfo = pointList.get(i);
//			paint.setColor(pointInfo.getColor());
//			canvas.drawRect(recfList.get(i), paint);
//		}
//	}
//
//	// 内部类的内部类
//	class MyThread implements Runnable {
//		@Override
//		public void run() {
//			while (mbLoop) {
//				synchronized (holder) {
//					doDrawLeft();
//					doDrawLeft();
//				}
//				try {
//					Thread.sleep(200);
//				} catch (Exception e) {
//				}
//
//			}
//		}
//	}
//}

package cn.com.amome.amomeshoes.widget;

import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.model.PointInfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View {
	private String TAG = "MyView";
	public List<PointInfo> leftPointList = new ArrayList<PointInfo>();
	public List<PointInfo> rightPointList = new ArrayList<PointInfo>();
	public static int jg = 8;
	int size = 0;
	private Handler mHandler = new Handler();

	public MyView(Context context) {
		super(context);
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// 设置画布的背景颜色
		canvas.drawColor(Color.argb(0, 255, 255, 255));
		// canvas.drawColor(Color.WHITE);
		if (leftPointList != null && leftPointList.size() > 0) {
			size = 0;
			drawPointLeft(canvas, leftPointList);
		}
		if (rightPointList != null && rightPointList.size() > 0) {
			size = 0;
			drawPointRight(canvas, rightPointList);
		}
		mHandler.postDelayed(mRunnable, 40);
	}

	private Runnable mRunnable = new Runnable() {
		public void run() {
			// Log.i(TAG, ">>>>>> mRunnable");
			invalidate();
		}
	};

	public void drawPointLeft(Canvas canvas, List<PointInfo> pointList) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		for (int i = 0; i < pointList.size(); i++) {
			PointInfo pointInfo = pointList.get(i);
			paint.setColor(pointInfo.getColor());
			canvas.drawRect(pointInfo.getX(), pointInfo.getY(),
					pointInfo.getX() + jg, pointInfo.getY() + jg, paint);
		}
	}

	public void drawPointRight(Canvas canvas, List<PointInfo> pointList) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		for (int i = 0; i < pointList.size(); i++) {
			PointInfo pointInfo = pointList.get(i);
			paint.setColor(pointInfo.getColor());
			canvas.drawRect(pointInfo.getX(), pointInfo.getY(),
					pointInfo.getX() + jg, pointInfo.getY() + jg, paint);
		}
	}
}
