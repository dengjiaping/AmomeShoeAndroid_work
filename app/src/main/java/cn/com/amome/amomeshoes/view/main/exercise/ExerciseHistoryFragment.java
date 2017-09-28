/**
 * @Title:ExerciseFragmentViewOne.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-9 上午10:52:39
 */
package cn.com.amome.amomeshoes.view.main.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.events.ExerciseHistEvent;
import cn.com.amome.amomeshoes.model.ExerciseDataInfo;
import cn.com.amome.amomeshoes.util.MyViewPager;
import cn.com.amome.amomeshoes.util.ScreenUtil;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExerciseHistoryFragment extends Fragment implements
		OnClickListener {
	private String TAG = "ExerciseHistoryFragment";
	private Context mContext;
	private static final int MSG_HISTDATA = 1;
	private View view;
	private ScreenUtil screenparm;
	private int screenWidth, screenHeight;
	private int width;
	private LinearLayout rl_histdata;
	private TextView tv_date;
	private ArrayList<String> dateList = new ArrayList<String>(); // 条形图日期list
	private ArrayList<String> calHeiList = new ArrayList<String>(); // 条形图高度list
	private List<ExerciseDataInfo> modHistDataList = new ArrayList<ExerciseDataInfo>(); // 生成指定天数的list，并补齐未来五天
	public static ExerciseDataInfo exerDataInfo;
	private float sit_cal, sta_cal, go_cal, total_cal; // 卡路里
	private int steps, go_time, sta_time, sit_time, slp_time;// 步数，步频，坐站走时间，休眠时间
	private float stpfqc;
	private TextView tv_step, tv_sit, tv_stand, tv_step_fre, tv_total_cal;
	private PieChart pie_chart_with_line, pie_chart_with_line2,
			pie_chart_with_line3;
	private Typeface tf;
	private PieData data1;
	float sit_one, sit_two, sta_one, sta_two, go_one, go_two;
	private SimpleDateFormat formatter;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.i(TAG, "柱状图编号 num=" + msg.arg1 + "准备加载数据");
				exerDataInfo = modHistDataList.get(msg.arg1);
				tv_date.setText(exerDataInfo.date);
				sit_time = exerDataInfo.sittme;
				sta_time = exerDataInfo.stdtme;
				go_time = exerDataInfo.wlktme;
				steps = exerDataInfo.stpnum;
				stpfqc = exerDataInfo.stpfqc;
				tv_sit.setText(formatter.format(sit_time * 1000));
				tv_stand.setText(formatter.format(sta_time * 1000));
				tv_step.setText(steps + " 步");
				tv_step_fre.setText("步速 " + stpfqc + "步/min");
				// 计算卡路里
				sit_cal = exerDataInfo.sitclr;
				sta_cal = exerDataInfo.stdclr;
				go_cal = exerDataInfo.wlkclr;
				total_cal = sit_cal + sta_cal + go_cal;
				Log.i(TAG, total_cal + "total_cal");
				tv_total_cal.setText((float) (Math.round(total_cal * 100))
						/ 100 + "");
				// 饼状图数据
				sit_one = sit_cal / (sit_cal + sta_cal + go_cal) * 100f;
				sit_two = 100f - sit_one;
				sta_one = sta_cal / (sit_cal + sta_cal + go_cal) * 100f;
				sta_two = 100f - sta_one;
				go_one = go_cal / (sit_cal + sta_cal + go_cal) * 100f;
				go_two = 100f - go_one;

				// 更新饼状图
				setData1(2, 100); // 坐
				setData2(2, 100); // 站
				setData3(2, 100); // 走
				pie_chart_with_line.animateY(900,
						Easing.EasingOption.EaseInOutQuad); // 动画设置
				pie_chart_with_line2.animateY(900,
						Easing.EasingOption.EaseInOutQuad); // 动画设置
				pie_chart_with_line3.animateY(900,
						Easing.EasingOption.EaseInOutQuad); // 动画设置

				break;
			case 2:
				modHistDataList = (List<ExerciseDataInfo>) msg.obj;
				// ××××××××××××××××××××××××××××××××××××××××××××××××××××
				for (int i = 0; i < modHistDataList.size(); i++) {
					String str = "";
					String calHeiStr = "";
					int calHei = 0;
					if (modHistDataList.get(i).date.length() > 5) // 屏蔽年份，只展示月日
					{
						str = modHistDataList.get(i).date
								.substring(modHistDataList.get(i).date.length() - 5);
						calHei = (modHistDataList.get(i).sitclr
								+ modHistDataList.get(i).stdclr + modHistDataList
								.get(i).wlkclr) * 300 / 6000;
						Log.i(TAG, "calHei" + calHei);
						calHeiStr = String.valueOf(calHei);
					} else {
						str = "无";
						calHeiStr = "0";
					}
					dateList.add(str);
					calHeiList.add(calHeiStr);
				}

				MyViewPager mp = new MyViewPager(getActivity(), mHandler,
						width, dateList, calHeiList);
				rl_histdata.addView(mp);

				// ××××××××××××××××××××××××××××××××××××××××××××××××××××
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		mContext = getActivity();
		formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		view = inflater.inflate(R.layout.fragment_exercise_history, container,
				false);
		initView(view);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		screenparm = new ScreenUtil(getActivity());
		screenWidth = (int) screenparm.getScreenWidthPX();
		screenHeight = (int) screenparm.getScreenHeightPX();
		width = screenWidth / 11; // 每个条形图所占宽度含空隙 （屏幕共显示11个条形图）
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		rl_histdata = (LinearLayout) view.findViewById(R.id.rl_histdata);
		tv_sit = (TextView) view.findViewById(R.id.tv_sit);
		tv_stand = (TextView) view.findViewById(R.id.tv_stand);
		tv_step = (TextView) view.findViewById(R.id.tv_step);
		tv_step_fre = (TextView) view.findViewById(R.id.tv_step_fre);
		tv_total_cal = (TextView) view.findViewById(R.id.tv_total_cal);

		pie_chart_with_line = (PieChart) view
				.findViewById(R.id.pie_chart_with_line);
		pie_chart_with_line2 = (PieChart) view
				.findViewById(R.id.pie_chart_with_line2);
		pie_chart_with_line3 = (PieChart) view
				.findViewById(R.id.pie_chart_with_line3);

		/**
		 * 是否使用百分比
		 */
		pie_chart_with_line.setUsePercentValues(false);
		/**
		 * 描述信息
		 */
		pie_chart_with_line.setDescription("");
		pie_chart_with_line.setNoDataText("");
		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// pie_chart_with_line.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		/**
		 * 设置圆环中间的文字
		 */
		// pie_chart_with_line.setCenterText(generateCenterSpannableText());

		/**
		 * 圆环距离屏幕上下上下左右的距离
		 */
		pie_chart_with_line.setExtraOffsets(0f, 0f, 0f, 0f);

		/**
		 * 是否显示圆环中间的洞
		 */
		pie_chart_with_line.setDrawHoleEnabled(false); // 不显示
		/**
		 * 设置中间洞的颜色
		 */
		pie_chart_with_line.setHoleColor(Color.WHITE);

		// /**
		// * 设置圆环透明度及半径
		// */
		// pie_chart_with_line.setTransparentCircleColor(Color.YELLOW);
		// pie_chart_with_line.setTransparentCircleAlpha(110);
		// pie_chart_with_line.setTransparentCircleRadius(61f);

		/**
		 * 设置圆环中间洞的半径
		 */
		pie_chart_with_line.setHoleRadius(60f);

		/**
		 * 是否显示洞中间文本
		 */
		pie_chart_with_line.setDrawCenterText(false); // 不显示

		/**
		 * 触摸是否可以旋转以及松手后旋转的度数
		 */
		pie_chart_with_line.setRotationAngle(0); // 初始旋转角度
		// enable rotation of the chart by touch
		pie_chart_with_line.setRotationEnabled(false); // 不可以旋转

		// pie_chart_with_line.setUnit(" €");
		// pie_chart_with_line.setDrawUnitsInChart(true);

		/**
		 * add a selection listener 值改变时候的监听
		 */
		// pie_chart_with_line.setOnChartValueSelectedListener(this); 暂时屏蔽

		// setData1(2, 100);

		Legend l = pie_chart_with_line.getLegend();
		l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setEnabled(false); // 不显示图例
		l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		l.setForm(Legend.LegendForm.CIRCLE);

		l.setFormSize(8f);
		l.setFormToTextSpace(4f);
		l.setXEntrySpace(6f);

		pie_chart_with_line.animateY(900, Easing.EasingOption.EaseInOutQuad); // 动画设置

		// ×××××××××××××××××××××××××××××××××××××××××××× 2
		// ×××××××××××××××××××××××××××××××××××××××××××××××

		/**
		 * 是否使用百分比
		 */
		pie_chart_with_line2.setUsePercentValues(false);
		/**
		 * 描述信息
		 */
		pie_chart_with_line2.setDescription("");
		pie_chart_with_line2.setNoDataText("");
		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// pie_chart_with_line2.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		/**
		 * 设置圆环中间的文字
		 */
		// pie_chart_with_line2.setCenterText(generateCenterSpannableText());

		/**
		 * 圆环距离屏幕上下上下左右的距离
		 */
		pie_chart_with_line2.setExtraOffsets(0f, 0f, 0f, 0f); // 左，上，右，下

		/**
		 * 是否显示圆环中间的洞
		 */
		pie_chart_with_line2.setDrawHoleEnabled(false); // 不显示
		/**
		 * 设置中间洞的颜色
		 */
		pie_chart_with_line2.setHoleColor(Color.WHITE);

		// /**
		// * 设置圆环透明度及半径
		// */
		// pie_chart_with_line2.setTransparentCircleColor(Color.YELLOW);
		// pie_chart_with_line2.setTransparentCircleAlpha(110);
		// pie_chart_with_line2.setTransparentCircleRadius(61f);

		/**
		 * 设置圆环中间洞的半径
		 */
		pie_chart_with_line2.setHoleRadius(60f);

		/**
		 * 是否显示洞中间文本
		 */
		pie_chart_with_line2.setDrawCenterText(false); // 不显示

		/**
		 * 触摸是否可以旋转以及松手后旋转的度数
		 */
		pie_chart_with_line2.setRotationAngle(0); // 初始旋转角度
		// enable rotation of the chart by touch
		pie_chart_with_line2.setRotationEnabled(false); // 不可以旋转

		// pie_chart_with_line2.setUnit(" €");
		// pie_chart_with_line2.setDrawUnitsInChart(true);

		/**
		 * add a selection listener 值改变时候的监听
		 */
		// pie_chart_with_line2.setOnChartValueSelectedListener(this); 暂时屏蔽

		// setData2(2, 100);

		Legend l2 = pie_chart_with_line2.getLegend();
		l2.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
		l2.setEnabled(false);// 不显示图例
		l2.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		l2.setForm(Legend.LegendForm.CIRCLE);

		l2.setFormSize(8f);
		l2.setFormToTextSpace(4f);
		l2.setXEntrySpace(6f);

		pie_chart_with_line2.animateY(700, Easing.EasingOption.EaseInOutQuad); // 动画设置
		// ××××××××××××××××××××××××××××××××××××××× 3
		// ××××××××××××××××××××××××××××××××××××××××××
		/**
		 * 是否使用百分比
		 */
		pie_chart_with_line3.setUsePercentValues(false);
		/**
		 * 描述信息
		 */
		pie_chart_with_line3.setDescription("");
		pie_chart_with_line3.setNoDataText("");
		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// pie_chart_with_line3.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		/**
		 * 设置圆环中间的文字
		 */
		// pie_chart_with_line3.setCenterText(generateCenterSpannableText());

		/**
		 * 圆环距离屏幕上下上下左右的距离
		 */
		pie_chart_with_line3.setExtraOffsets(0f, 0f, 0f, 0f); // 左，上，右，下

		/**
		 * 是否显示圆环中间的洞
		 */
		pie_chart_with_line3.setDrawHoleEnabled(false); // 不显示
		/**
		 * 设置中间洞的颜色
		 */
		pie_chart_with_line3.setHoleColor(Color.WHITE);

		// /**
		// * 设置圆环透明度及半径
		// */
		// pie_chart_with_line3.setTransparentCircleColor(Color.YELLOW);
		// pie_chart_with_line3.setTransparentCircleAlpha(110);
		// pie_chart_with_line3.setTransparentCircleRadius(61f);

		/**
		 * 设置圆环中间洞的半径
		 */
		pie_chart_with_line3.setHoleRadius(60f);

		/**
		 * 是否显示洞中间文本
		 */
		pie_chart_with_line3.setDrawCenterText(false); // 不显示

		/**
		 * 触摸是否可以旋转以及松手后旋转的度数
		 */
		pie_chart_with_line3.setRotationAngle(0); // 初始旋转角度
		// enable rotation of the chart by touch
		pie_chart_with_line3.setRotationEnabled(false); // 不可以旋转

		// pie_chart_with_line3.setUnit(" €");
		// pie_chart_with_line3.setDrawUnitsInChart(true);

		/**
		 * add a selection listener 值改变时候的监听
		 */
		// pie_chart_with_line3.setOnChartValueSelectedListener(this); 暂时屏蔽

		// setData3(2, 100);

		Legend l3 = pie_chart_with_line3.getLegend();
		l3.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
		l3.setEnabled(false);// 不显示图例
		l3.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		l3.setForm(Legend.LegendForm.CIRCLE);

		l3.setFormSize(8f);
		l3.setFormToTextSpace(4f);
		l3.setXEntrySpace(6f);

		pie_chart_with_line3.animateY(500, Easing.EasingOption.EaseInOutQuad); // 动画设置
		// ************************************************************************************

	}

	private void setData1(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		// for (int i = 0; i < count; i++) {
		// yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
		// }
		// 认为设置数据，写死
		yVals1.add(new Entry(sit_one, 0));
		yVals1.add(new Entry(sit_two, 1));
		// yVals1.add(new Entry(20f, 0));
		// yVals1.add(new Entry(80f, 1));
		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度
		// dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		colors.add(Color.rgb(3, 110, 184));
		colors.add(Color.rgb(137, 137, 137));
		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		// 设置X显示的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		// data.setXVals(a);
		pie_chart_with_line.setData(data);

		// undo all highlights
		pie_chart_with_line.highlightValues(null);

		pie_chart_with_line.invalidate();
	}

	private void setData2(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		// for (int i = 0; i < count; i++) {
		// yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
		// }
		// 认为设置数据，写死
		yVals1.add(new Entry(sta_one, 0));
		yVals1.add(new Entry(sta_two, 1));
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度
		// dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 暂时屏蔽 设置颜色，两种
		colors.add(Color.rgb(66, 162, 255));
		colors.add(Color.rgb(159, 160, 160));
		dataSet.setColors(colors);

		// dataSet.setValueLinePart1OffsetPercentage(80.f);
		// dataSet.setValueLinePart1Length(0.3f);
		// dataSet.setValueLinePart2Length(0.4f);
		// // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
		// dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		// 设置X显示的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		// data.setXVals(a);
		pie_chart_with_line2.setData(data);

		// undo all highlights
		pie_chart_with_line2.highlightValues(null);

		pie_chart_with_line2.invalidate();
	}

	private void setData3(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		// for (int i = 0; i < count; i++) {
		// yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
		// }
		yVals1.add(new Entry(go_one, 0)); // 蓝色
		yVals1.add(new Entry(go_two, 1)); // 灰色

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度
		// dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 暂时屏蔽 设置颜色，两种
		colors.add(Color.rgb(66, 211, 255));
		colors.add(Color.rgb(181, 181, 182));
		dataSet.setColors(colors);

		// dataSet.setValueLinePart1OffsetPercentage(80.f);
		// dataSet.setValueLinePart1Length(0.3f);
		// dataSet.setValueLinePart2Length(0.4f);
		// // dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
		// dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		data1 = new PieData(xVals, dataSet);
		// data.setValueFormatter(new PercentFormatter());
		data1.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data1.setValueTextColor(Color.BLACK);
		data1.setValueTypeface(tf);
		// 设置X显的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		// data.setXVals(a);
		pie_chart_with_line3.setData(data1);

		// undo all highlights
		pie_chart_with_line3.highlightValues(null);

		pie_chart_with_line3.invalidate();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====" + hidden);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Log.i(TAG, TAG + "===setUserVisibleHint====" + isVisibleToUser);
		if (isVisibleToUser)
			onPause();
		else
			onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, TAG + "=====onPause=====");
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, TAG + "=====onResume=====");
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ExerciseHistEvent event) {
		Log.i(TAG, TAG + "收到了消息");
		Message message = Message.obtain();
		message.obj = event.getHistDataList();
		message.what = 2;
		mHandler.sendMessage(message);
	}
}
