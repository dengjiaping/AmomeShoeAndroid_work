package cn.com.amome.amomeshoes.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class ScreenUtil {
	private String TAG = "ScreenUtil";
	private Context context;
	private float width;
	private float height;
	private float density;
	private float densityDpi;
	private float screenWidth;
	private float screenHeight;

	public ScreenUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;// 屏幕宽度（像素）
		height = dm.heightPixels; // 屏幕高度（像素）

		density = dm.density;// 屏幕密度（0.75 / 1.0 / 1.5）
		densityDpi = dm.densityDpi;// 屏幕密度dpi（120 / 160 / 240）
		// 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
		screenWidth = width / density;// 屏幕宽度(dp)
		screenHeight = height / density;// 屏幕高度(dp)
	}

	public float getScreenWidthPX() {
		return width;
	}

	public float getScreenHeightPX() {
		return height;
	}

	public float getScreenWidthDP() {
		return screenWidth;
	}

	public float getScreenHeightDP() {
		return screenHeight;
	}

	public float getScreenDensity() {
		return density;
	}

}
