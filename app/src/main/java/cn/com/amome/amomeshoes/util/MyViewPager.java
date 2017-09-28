package cn.com.amome.amomeshoes.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 装载柱状图
 */
public class MyViewPager extends ViewGroup {
	private String TAG = "MyViewPager";
	private Handler mHandler;
	private Scroller mScroller;
	private int lastX;
	private int mStart, mEnd;
	private int mScreenWidth;
	private int scrWidth; // 一个条形图所占大小
	private int centerX, centerNum; // centerX：记录滑动结束后所指的坐标X，centerNum：所指的柱状图的编号。
	private ArrayList<String> dateList = new ArrayList<String>(); // 日期
	private ArrayList<String> calHeiList = new ArrayList<String>(); // 高度

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, Handler mHandler, int scrWidth, ArrayList<String> dateList, ArrayList<String> calHeiList) {
		super(context);
		this.mHandler = mHandler;
		this.scrWidth = scrWidth;
		this.dateList = dateList;
		this.calHeiList = calHeiList;
		init(context);
		Message msg = Message.obtain();
		msg.what = 0;
		msg.arg1 = 5;
		mHandler.sendMessage(msg);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
		LayoutParams lp = new LinearLayout.LayoutParams(scrWidth, LayoutParams.MATCH_PARENT);
		LayoutParams lp1 = new LinearLayout.LayoutParams(scrWidth * 1 / 2, 300); // 柱状图宽度和高度
		for (int i = 0; i < dateList.size(); i++) {
			LinearLayout l = new LinearLayout(context);
			l.setLayoutParams(lp);
			l.setOrientation(LinearLayout.VERTICAL);
			l.setGravity(Gravity.CENTER_HORIZONTAL);
			// 上半部分
			LinearLayout l1 = new LinearLayout(context);
			l1.setLayoutParams(lp1);
			l1.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
			l1.setGravity(Gravity.BOTTOM);
			if (i >= 5 && i < dateList.size()) {
				ImageView iv_s = new ImageView(context);
				iv_s.setLayoutParams(new LayoutParams(scrWidth * 1 / 2, Integer.parseInt(calHeiList.get(i)))); // 蓝色区域宽度和高度
				iv_s.setBackgroundColor(Color.rgb(66, 162, 255));
				l1.addView(iv_s);
			}

			// 下半部分
			TextView tv_date = new TextView(context);
			tv_date.setText(dateList.get(i));
			tv_date.setTextSize(8);
			tv_date.setWidth(LayoutParams.MATCH_PARENT);
			tv_date.setGravity(Gravity.CENTER_HORIZONTAL);
			l.addView(l1);
			l.addView(tv_date);

			addView(l);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			child.layout(10 * scrWidth - i * scrWidth, 0, 11 * scrWidth - i * scrWidth, child.getMeasuredHeight());

			// child.setOnClickListener(new myOnClick(i));

		}
	}

	public class myOnClick implements OnClickListener {
		private int i;

		public myOnClick(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			// Log.i(TAG, i + "被点击了");
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录下起时点的位置
			mStart = getScrollX();
			// 滑动结束，停止动画
			if (mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			// 记录位置
			lastX = x;
			Log.i("lastX", lastX + "");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("x", "x" + x);
			int deltaX = lastX - x;
			Log.e("deltaX", "deltaX=  lastX - x =" + deltaX);
			if (isMove(deltaX)) {
				// 在滑动范围内，允许滑动
				scrollBy(deltaX, 0);
			}
			Log.e("getScrollX", getScrollX() + "");
			lastX = x;
			break;
		case MotionEvent.ACTION_UP:
			// 判断滑动的距离
			int dScrollX = checkAlignment();
			Log.i("dScrollX", "dScrollX" + dScrollX);
			// 弹性滑动开启
			if (dScrollX > 0) {// 从左向右滑动
				if (dScrollX < scrWidth / 2) {
					Log.e("dScrollX", dScrollX + "");
					mScroller.startScroll(getScrollX(), 0, -dScrollX, 0, 500);
					centerX = getScrollX() - dScrollX;
					// Log.i(TAG, "从左向右滑动，停下来了centerX=" + centerX);
				} else {
					mScroller.startScroll(getScrollX(), 0, scrWidth - dScrollX, 0, 500);
					centerX = getScrollX() + scrWidth - dScrollX;
					// Log.i(TAG, "从左向右滑动，停下来了centerX=" + centerX);
				}
			} else {// 从右向左滑动
				if (-dScrollX < scrWidth / 2) {
					// 滑到下一个
					mScroller.startScroll(getScrollX(), 0, -dScrollX, 0, 500);
					centerX = getScrollX() - dScrollX;
					// Log.i(TAG, "从右向左滑动，停下来了centerX=" + centerX);
				} else {
					// 滑回上一个
					mScroller.startScroll(getScrollX(), 0, -scrWidth - dScrollX, 0, 500);
					Log.i("从右向左", "222");
					centerX = getScrollX() - scrWidth - dScrollX;
					// Log.i(TAG, "从右向左滑动，停下来了centerX=" + centerX);
				}
			}
			centerNum = -centerX / scrWidth;
			// Log.i(TAG, "最终指向右侧数第" + (centerNum + 5) + "个");
			Message msg = Message.obtain();
			msg.what = 0;
			msg.arg1 = centerNum + 5;
			mHandler.sendMessage(msg);
			break;
		}
		// 重绘
		invalidate();
		return true;
	}

	// deltaX = lastX - x;
	private boolean isMove(int deltaX) {
		int scrollX = getScrollX();
		// 滑动到最后一屏，不能在向左滑动了
		if (deltaX >= 0) {// 如果为正，证明用户从右向左滑动
			if (scrollX >= 0) {
				return false;
			} else if (deltaX + scrollX > 0) {
				scrollTo(0, 0);
				return false;
			}
		}
		// 滑动到第一屏，不能在向右滑动
		int leftX = -(getChildCount() - 6) * scrWidth;
		if (deltaX < 0) { // 如果为负，证明用户从左往右滑动
			if (scrollX <= leftX) {
				return false;
			} else if (scrollX + deltaX < leftX) {
				scrollTo(leftX, 0);
				return false;
			}
		}
		return true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		}
	}

	private int checkAlignment() {
		// 判断滑动的趋势，是向左还是向右，滑动的偏移量是多少
		mEnd = getScrollX();
		boolean isUp = ((mEnd - mStart) > 0) ? true : false;
		int lastPrev = mEnd % scrWidth;
		int lastNext = -lastPrev;
		Log.i("checkAlignment", "mEnd" + mEnd);
		Log.i("checkAlignment", "mStart" + mStart);
		Log.i("checkAlignment", "isUp" + isUp);
		Log.i("checkAlignment", "scrWidth" + scrWidth);
		Log.i("checkAlignment", "lastPrev" + lastPrev);
		Log.i("checkAlignment", "lastNext" + lastNext);

		if (isUp) {
			return lastPrev;
		} else {
			return -lastNext;
		}
	}

}