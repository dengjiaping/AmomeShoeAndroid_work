package cn.com.amome.amomeshoes.widget;

import cn.com.amome.amomeshoes.view.main.health.detection.stand.StandDetectionActivity;
import cn.com.amome.amomeshoes.view.main.health.report.BalanceReportActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class StandLineView extends View {

	public static int cop_y = 168, cop_x = 46;
	private float heightScale = 1.5f; // 比例

	public StandLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.argb(0, 255, 255, 255));
		// canvas.drawColor(Color.argb(150, 150, 150, 255));
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth((float) 1.0);

		canvas.drawLine(
				0,
				(float) (6.5 * heightScale * StandDetectionActivity.m_fGridLong),
				1000 * StandDetectionActivity.m_fGridLong,
				(float) (6.5 * heightScale * StandDetectionActivity.m_fGridLong),
				paint);

		canvas.drawLine(
				0,
				(float) (10.5 * heightScale * StandDetectionActivity.m_fGridLong),
				1000 * StandDetectionActivity.m_fGridLong,
				(float) (10.5 * heightScale * StandDetectionActivity.m_fGridLong),
				paint);
	}

}
