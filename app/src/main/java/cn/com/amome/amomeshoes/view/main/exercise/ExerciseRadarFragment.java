/**
 * @Title:ExerciseFragmentViewOne.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-9 上午10:52:39
 */
//第三个页面
package cn.com.amome.amomeshoes.view.main.exercise;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.github.mikephil.charting.radar.animation.Easing;
import com.github.mikephil.charting.radar.charts.RadarChart;
import com.github.mikephil.charting.radar.components.AxisBase;
import com.github.mikephil.charting.radar.components.Legend;
import com.github.mikephil.charting.radar.components.XAxis;
import com.github.mikephil.charting.radar.components.YAxis;
import com.github.mikephil.charting.radar.data.RadarData;
import com.github.mikephil.charting.radar.data.RadarDataSet;
import com.github.mikephil.charting.radar.data.RadarEntry;
import com.github.mikephil.charting.radar.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.radar.interfaces.datasets.IRadarDataSet;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.events.ExerciseRadarEvent;
import cn.com.amome.amomeshoes.util.SpfUtil;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseRadarFragment extends Fragment {
	private String TAG = "ExerciseRadarFragment";
	private RadarChart mChart;
	private TextView tv_exercise_radar_steps, tv_exercise_radar_sittime,
			tv_exercise_radar_gotime, tv_exercise_radar_stpfqc,
			tv_exercise_radar_statime, tv_exercise_radar_totalcal;
	private float steps_progress, stpfqc_progress, sit_time_progress,
			sta_time_progress, total_cal_progress, go_time_progress;
	private View view;
	private float low, med, high;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				float[] radar = (float[]) msg.obj;
				// 传送 步数，步频，坐时间，站时间，总消耗，走时间
				// Log.i(TAG, "case0" + "radar[0]:" + radar[0] + "radar[1]:"
				// + radar[1] + "radar[2]:" + radar[2] + "radar[3]:"
				// + radar[3] + "radar[4]:" + radar[4] + "radar[5]:"
				// + radar[5]);
				tv_exercise_radar_steps.setText((int) radar[0] + "");
				tv_exercise_radar_stpfqc.setText((int) radar[1] + "");
				tv_exercise_radar_sittime.setText(Math
						.round(100 * radar[2] / 60) / 100 + "");
				tv_exercise_radar_statime.setText(Math
						.round(100 * radar[3] / 60) / 100 + "");
				tv_exercise_radar_totalcal.setText((float) (Math
						.round(radar[4] * 100)) / 100 + "");
				tv_exercise_radar_gotime.setText(Math
						.round(100 * radar[5] / 60) / 100 + "");

				// 雷达图数据计算
				// 坐时间
				if (radar[2] < 3 * 3600) {
					sit_time_progress = (radar[2] / 3600 - 0) * (29 - 0)
							/ (3 - 0) + 0;
				} else if (radar[2] >= 3 * 3600 && radar[2] <= 6 * 3600) {
					sit_time_progress = (radar[2] / 3600 - 3) * (80 - 54)
							/ (6 - 3) + 54;
				} else if (radar[2] > 6 * 3600 && radar[2] <= 10 * 3600) {
					sit_time_progress = (radar[2] / 3600 - 10) * (54 - 29)
							/ (6 - 10) + 29;
				} else if (radar[2] > 10 * 3600) {
					sit_time_progress = (radar[2] / 3600 - 24) * (29 - 0)
							/ (10 - 24) + 0;
				}

				// 站时间
				if (radar[3] < 1 * 3600) {
					sta_time_progress = (radar[3] / 3600 - 0) * (29 - 0)
							/ (1 - 0) + 0;
				} else if (radar[3] >= 1 * 3600 && radar[3] <= 4 * 3600) {
					sta_time_progress = (radar[3] / 3600 - 1) * (80 - 54)
							/ (4 - 1) + 54;
				} else if (radar[3] > 4 * 3600 && radar[3] <= 9 * 3600) {
					sta_time_progress = (radar[3] / 3600 - 9) * (54 - 29)
							/ (4 - 9) + 29;
				} else if (radar[3] > 9 * 3600) {
					sta_time_progress = (radar[3] / 3600 - 24) * (29 - 0)
							/ (9 - 24) + 0;
				}

				// 走时间
				if (radar[5] < 1 * 3600) {
					go_time_progress = (radar[5] / 3600 - 0) * (29 - 0)
							/ (1 - 0) + 0;
				} else if (radar[5] >= 1 * 3600 && radar[5] <= 4 * 3600) {
					go_time_progress = (radar[5] / 3600 - 1) * (54 - 29)
							/ (4 - 1) + 29;
				} else if (radar[5] > 4 * 3600 && radar[5] <= 8 * 3600) {
					go_time_progress = (radar[5] / 3600 - 4) * (80 - 54)
							/ (8 - 4) + 54;
				} else if (radar[5] > 8 * 3600) {
					go_time_progress = (radar[5] / 3600 - 24) * (29 - 0)
							/ (8 - 24) + 0;
				}

				// 步数
				if (radar[0] < 1000) {
					steps_progress = (radar[0] - 0) * (29 - 0) / ((1000 - 0))
							+ 0;
				} else if (radar[0] >= 1000 && radar[0] <= 7000) {
					steps_progress = (radar[0] - 1000) * (54 - 29)
							/ ((7000 - 1000)) + 29;
				} else if (radar[0] > 7000 && radar[0] <= 60000) {
					steps_progress = (radar[0] - 7000) * (80 - 54)
							/ ((60000 - 7000)) + 54;
				} else if (radar[0] > 60000 && radar[0] < 110000) {
					steps_progress = (radar[0] - 110000) * (29 - 0)
							/ ((60000 - 110000)) + 0;
				}

				// 步频
				if (radar[1] < 50) {
					stpfqc_progress = (radar[1] - 0) * (29 - 0) / ((50 - 0))
							+ 0;
				} else if (radar[1] >= 50 && radar[1] <= 110) {
					stpfqc_progress = (radar[1] - 50) * (54 - 29)
							/ ((110 - 50)) + 29;
				} else if (radar[1] > 110 && radar[1] <= 140) {
					stpfqc_progress = (radar[1] - 110) * (80 - 54)
							/ ((140 - 110)) + 54;
				} else if (radar[1] > 140 && radar[1] < 200) {
					stpfqc_progress = (radar[1] - 200) * (29 - 0)
							/ ((140 - 200)) + 0;
				}

				// 总消耗
				if (radar[4] < 450) {
					total_cal_progress = (radar[4] - 0) * (29 - 0)
							/ ((450 - 0)) + 0;
				} else if (radar[4] >= 450 && radar[4] <= 1300) {
					total_cal_progress = (radar[4] - 450) * (54 - 29)
							/ ((1300 - 450)) + 29;
				} else if (radar[4] > 1300 && radar[4] <= 2220) {
					total_cal_progress = (radar[4] - 1300) * (80 - 54)
							/ ((2220 - 1300)) + 54;
				} else if (radar[4] > 2220 && radar[4] < 5328) {
					total_cal_progress = (radar[4] - 5328) * (29 - 0)
							/ ((2220 - 5328)) + 0;
				}

				// 29,54,80
				setData(steps_progress, stpfqc_progress, sit_time_progress,
						sta_time_progress, total_cal_progress, go_time_progress);

				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		high = SpfUtil.readScreenWidthPx(getActivity()) * 80 / 1080;
		view = inflater.inflate(R.layout.fragment_exercise_radar, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		tv_exercise_radar_steps = (TextView) view
				.findViewById(R.id.tv_exercise_radar_steps);
		tv_exercise_radar_sittime = (TextView) view
				.findViewById(R.id.tv_exercise_radar_sittime);
		tv_exercise_radar_gotime = (TextView) view
				.findViewById(R.id.tv_exercise_radar_gotime);
		tv_exercise_radar_stpfqc = (TextView) view
				.findViewById(R.id.tv_exercise_radar_stpfqc);
		tv_exercise_radar_statime = (TextView) view
				.findViewById(R.id.tv_exercise_radar_statime);
		tv_exercise_radar_totalcal = (TextView) view
				.findViewById(R.id.tv_exercise_radar_totalcal);

		mChart = (RadarChart) view.findViewById(R.id.chart1);
		// mChart.setBackgroundColor(Color.rgb(255, 255, 255)); //设置背景色
		mChart.setAlpha(100);
		mChart.setNoDataText("");
		mChart.setRotationAngle(60); // 设置初始旋转角度
		mChart.setRotationEnabled(false); // 禁止手动旋转
		mChart.setNoDataText("");
		mChart.getDescription().setEnabled(false);

		mChart.setWebLineWidth(1f);
		mChart.setWebColor(Color.argb(0, 255, 255, 255)); // 中心到角的连线颜色
		mChart.setWebLineWidthInner(1f);
		mChart.setWebColorInner(Color.argb(100, 100, 100, 100)); // 网线的颜色
		mChart.setWebAlpha(0); // 设置网线为透明

		setData(0, 0, 0, 0, 0, 0);

		mChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad,
				Easing.EasingOption.EaseInOutQuad);

		XAxis xAxis = mChart.getXAxis();
		// xAxis.setTypeface(mTfLight);
		xAxis.setTextSize(9f);
		xAxis.setYOffset(0f);
		xAxis.setXOffset(0f);
		xAxis.setValueFormatter(new IAxisValueFormatter() {

			private String[] mActivities = new String[] { "", "", "", "", "",
					"" };

			public String getFormattedValue(float value, AxisBase axis) {
				return mActivities[(int) value % mActivities.length];
			}
		});
		xAxis.setTextColor(Color.BLACK); // 说明文字的颜色

		YAxis yAxis = mChart.getYAxis();
		// yAxis.setTypeface(mTfLight);
		yAxis.setLabelCount(4, true);
		yAxis.setTextSize(9f);
		yAxis.setAxisMinimum(0f);
		yAxis.setAxisMaximum(80f);
		yAxis.setDrawLabels(false);
		// 图例
		Legend l = mChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
		l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
		l.setDrawInside(false);
		// l.setTypeface(mTfLight);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(5f);
		l.setTextColor(Color.BLACK); // 图例颜色
		l.setEnabled(false); // 不显示图例

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
		Log.d(TAG, TAG + "=====onDestroy=====");
		EventBus.getDefault().unregister(this);
	}

	public void setData(float steps, float stpfqc, float sit_time,
			float sta_time, float total_cal, float go_time) {

		ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
		entries1.add(new RadarEntry(steps));
		entries1.add(new RadarEntry(go_time));
		entries1.add(new RadarEntry(sit_time));
		entries1.add(new RadarEntry(sta_time));
		entries1.add(new RadarEntry(total_cal));
		entries1.add(new RadarEntry(stpfqc));

		RadarDataSet set1 = new RadarDataSet(entries1, "区域1");
		set1.setColor(Color.rgb(66, 162, 255)); // 边颜色
		set1.setFillColor(Color.rgb(66, 162, 255)); // 内部填充的颜色
		set1.setDrawFilled(true);
		set1.setFillAlpha(150);
		set1.setLineWidth(0.5f);
		set1.setDrawHighlightCircleEnabled(true);
		set1.setDrawHighlightIndicators(false);

		// RadarDataSet set2 = new RadarDataSet(entries2, "区域2");
		// set2.setColor(Color.rgb(121, 162, 175));
		// set2.setFillColor(Color.rgb(121, 162, 175));
		// set2.setDrawFilled(true);
		// set2.setFillAlpha(180);
		// set2.setLineWidth(2f);
		// set2.setDrawHighlightCircleEnabled(true);
		// set2.setDrawHighlightIndicators(false);

		ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
		sets.add(set1);
		// sets.add(set2);

		RadarData data = new RadarData(sets);
		// data.setValueTypeface(mTfLight);
		data.setValueTextSize(8f);
		data.setDrawValues(false);
		data.setValueTextColor(Color.WHITE);

		mChart.setData(data);
		mChart.invalidate();
	}

	public void onEventMainThread(ExerciseRadarEvent event) {
		// Log.i(TAG, TAG + "收到了消息");
		Message message = Message.obtain();
		message.obj = event.getRadar();
		message.what = 0;
		mHandler.sendMessage(message);
		// initView(view);
	}
}
