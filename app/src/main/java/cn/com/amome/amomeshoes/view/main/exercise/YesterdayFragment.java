package cn.com.amome.amomeshoes.view.main.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ExerciseDataInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YesterdayFragment extends Fragment implements OnClickListener {
	private String TAG = "YesterdayFragment";
	private View view;
	private Context mContext;
	private RelativeLayout rl_exercise_bind;
	private TextView tv_step, tv_sit, tv_stand, tv_walk, tv_step_fre,
			tv_total_cal;
	private PieChart pie_chart_with_line, pie_chart_with_line2,
			pie_chart_with_line3;
	private Typeface tf;
	private PieData data1;
	float sit_one, sit_two, sta_one, sta_two, go_one, go_two;
	private static final int MSG_GET_YESTERDATA = 0;
	private List<ExerciseDataInfo> ExerciseList;
	public static ExerciseDataInfo exerDataInfo;
	private Gson gson = new Gson();
	private float sit_cal, sta_cal, go_cal, total_cal; // 卡路里
	private int steps, go_time, sta_time, sit_time, slp_time;// 步数，坐站走时间，休眠时间
	private float stpfqc;// 步频
	private SimpleDateFormat formatter;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_YESTERDATA:
					Log.i(TAG, "获取昨日数据成功");
					T.showToast(getActivity(), "获取昨日数据成功", 0);
					AmomeApp.yesterhist_flag = false;
					String exerJson = (String) msg.obj;
					if (exerJson.equals("[{}]")) {
						Log.i(TAG, "昨日数据为空");
						T.showToast(getActivity(), "昨日数据为空", 0);
					} else {
						ExerciseList = gson.fromJson(exerJson,
								new TypeToken<List<ExerciseDataInfo>>() {
								}.getType());
						exerDataInfo = ExerciseList.get(0);
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
						tv_total_cal.setText((float) (Math
								.round(total_cal * 100)) / 100 + "");
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
					}

					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		view = inflater.inflate(R.layout.fragment_exercise_yesterday,
				container, false);
		DialogUtil.showCancelProgressDialog(getActivity(), "", "加载中", true,
				true);
		initView(view);
		setData1(2, 100); // 坐 初始化，防止饼状图控件显示没有数据的提示
		setData2(2, 100); // 站
		setData3(2, 100); // 走
		getYesterdayData();
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		Log.i(TAG, "initView");

		rl_exercise_bind = (RelativeLayout) view
				.findViewById(R.id.rl_exercise_bind);
		LayoutParams lp_rl_exercise_bind = (LayoutParams) rl_exercise_bind
				.getLayoutParams();
		lp_rl_exercise_bind.height = (int) (SpfUtil
				.readScreenHeightPx(mContext) / 20);
		rl_exercise_bind.setLayoutParams(lp_rl_exercise_bind);

		tv_sit = (TextView) view.findViewById(R.id.tv_sit);
		tv_stand = (TextView) view.findViewById(R.id.tv_stand);
		tv_step = (TextView) view.findViewById(R.id.tv_step);
		tv_step_fre = (TextView) view.findViewById(R.id.tv_step_fre);
		tv_step_fre.setText("步速 " + stpfqc + "步/min");
		tv_total_cal = (TextView) view.findViewById(R.id.tv_total_cal);
		tv_total_cal.setText("0.0");
		pie_chart_with_line = (PieChart) view
				.findViewById(R.id.pie_chart_with_line);
		pie_chart_with_line2 = (PieChart) view
				.findViewById(R.id.pie_chart_with_line2);
		pie_chart_with_line3 = (PieChart) view
				.findViewById(R.id.pie_chart_with_line3);
		// ×××××××××××××××××××××××××××××××××××××××××××××××××× 设置饼状图参数
		// ×××××××××××××

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

	/**
	 * 获取昨日数据
	 */
	private void getYesterdayData() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		int time = (int) (System.currentTimeMillis() / 1000) - 86400 * 1;
		params.put("calltype", ClientConstant.GETYESTERDATA);
		params.put("useid", SpfUtil.readUserId(getActivity()));
		params.put("date", time + "");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(getActivity(), callback, MSG_GET_YESTERDATA,
				params, ClientConstant.HISTDATA_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			DialogUtil.hideProgressDialog();
			String result;
			switch (type) {
			case MSG_GET_YESTERDATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_YESTERDATA解析失败");
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onHttpPostFailure(int type, int statusCode, Header[] arg1,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			// DialogUtil.hideProgressDialog();
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====" + hidden);
		if (hidden) {

		} else {
			if (AmomeApp.yesterhist_flag) {
				DialogUtil.showCancelProgressDialog(getActivity(), "", "重新获取中",
						true, true);
				getYesterdayData();
			} else {

			}
		}

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
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, TAG + "=====onResume=====");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
