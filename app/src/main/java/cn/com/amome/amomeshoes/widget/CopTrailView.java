package cn.com.amome.amomeshoes.widget;

import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.model.LineInfo;
import cn.com.amome.amomeshoes.view.main.health.report.BalanceReportActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CopTrailView extends View {
	private String TAG = "MyView";
	public List<LineInfo> lineList = new ArrayList<LineInfo>();
	private Handler mHandler = new Handler();
	private int i = 0;

	public CopTrailView(Context context) {
		super(context);
	}

	public CopTrailView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// Log.i(TAG, "在画吗？？");
		// 设置画布的背景颜色
		canvas.drawColor(Color.WHITE);
		// Log.i(TAG, "pointList的长度" + lineList.size());
		if (lineList != null && lineList.size() > 0) {
			drawPoint(canvas, lineList);
		}
		// mHandler.postDelayed(mRunnable, 200);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth((float) 1.0);
		canvas.drawLine(46 * BalanceReportActivity.scale, 0,
				46 * BalanceReportActivity.scale,
				1000 * BalanceReportActivity.scale, paint);
		if (i < 10) {
			mHandler.postDelayed(mRunnable, 200);
		}
		i++;
	}

	private Runnable mRunnable = new Runnable() {
		public void run() {
			// Log.i(TAG, ">>>>>> mRunnable");
			invalidate();
		}
	};

	public void drawPoint(Canvas canvas, List<LineInfo> pointList) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		for (int i = 0; i < pointList.size(); i++) {
			LineInfo LineInfo = pointList.get(i);
			paint.setColor(Color.RED);
			paint.setStrokeWidth((float) 3.0);
			canvas.drawLine(LineInfo.getX1(), LineInfo.getY1(),
					LineInfo.getX2(), LineInfo.getY2(), paint);
		}
	}
}
