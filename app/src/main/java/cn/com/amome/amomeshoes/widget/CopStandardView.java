package cn.com.amome.amomeshoes.widget;

import cn.com.amome.amomeshoes.view.main.health.report.BalanceReportActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CopStandardView extends View {

	public static int cop_y = 168, cop_x = 46;

	public CopStandardView(Context context, AttributeSet attrs) {
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
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth((float) 1.0);

		canvas.drawLine(0, 168 * BalanceReportActivity.scale,
				1000 * BalanceReportActivity.scale,
				168 * BalanceReportActivity.scale, paint);
	}

}
