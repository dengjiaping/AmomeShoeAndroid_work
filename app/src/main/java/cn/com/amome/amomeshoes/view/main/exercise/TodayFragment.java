package cn.com.amome.amomeshoes.view.main.exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.util.BleShoes.shoesGetBatteryInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesReadDailyDataCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetHistDataCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.events.ExerciseEvaluateEvent;
import cn.com.amome.amomeshoes.events.ExerciseEvent;
import cn.com.amome.amomeshoes.events.ExerciseHistEvent;
import cn.com.amome.amomeshoes.events.ExerciseRadarEvent;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ExerciseDataInfo;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.ScreenUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.TimeUtils;
import cn.com.amome.amomeshoes.util.WriteFile;
import cn.com.amome.amomeshoes.view.main.bind.BindActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.shoeservice.BleService;
import cn.com.amome.shoeservice.com.pushDailyProfile.DailyData;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodayFragment extends Fragment implements OnClickListener,
		OnChartValueSelectedListener {
	private String TAG = "TodayFragment";
	private Context mContext;
	private View view;
	private PieChart pie_chart_with_line;
	private PieChart pie_chart_with_line2;
	private PieChart pie_chart_with_line3;
	private Typeface tf;
	private RelativeLayout rl_exercise_bind;
	private ImageView iv_exercise_bind;
	private int steps, go_time, sta_time, sit_time, slp_time;// 步数，坐站走时间，休眠时间
	private float stpfqc;// 步频
	private int lastSteps, last_go_time, last_sta_time, last_sit_time;
	private TextView tv_step, tv_sit, tv_stand, tv_walk, tv_step_fre,
			tv_total_cal;
	private float sit_cal, sta_cal, go_cal, total_cal;
	private PieData data1;
	float sit_one, sit_two, sta_one, sta_two, go_one, go_two;
	private SimpleDateFormat formatter;
	/**成功连接后会置为false*/
	private boolean sucConnect = true;
	private int bleFlag = BleConstants.MSG_CONNECT;
	private static final int MSG_CAL_HIST_DATA = 3;
	private static final int MSG_RE_CONNECT = 19;
	private static final int MSG_SET_DAILYDATA = 20;
	private static final int MSG_GET_DAILYDATA = 21;
	private static final int MSG_GET_HISTDATA = 22;
	private static final int MSG_SET_DAILYDATA_FAIL = 23;
	private static final int MSG_GET_DAILYDATA_FAIL = 24;
	private static final int MSG_GET_HISTDATA_FAIL = 25;
	private static final int MSG_SET_HISTDATA = 26;
	private static final int MSG_SET_HISTDATA_FAIL = 27;
	private static final int MSG_CAL_TODAY_DATA_DOUBLE = 68;
	private float[] cal_evaluate = new float[3];
	private float[] radar = new float[6];
	private List<ExerciseDataInfo> ExerciseList;
	public static ExerciseDataInfo exerDataInfo;
	private List<ExerciseDataInfo> exerciseList;// 服务端返回的历史数据list
	private List<ExerciseDataInfo> modHistDataList = new ArrayList<ExerciseDataInfo>(); // 生成指定天数的list，并补齐未来五天
	private Gson gson = new Gson();
	private int[][] leftHistData, rightHistData;
	private boolean getLeftHist = false, getRightHist = false;
	private DailyData leftData, rightData;
	private DailyData lastLeftData, lastRightData;
	private boolean handleData = true;
	private boolean connectSuc = false;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CAL_HIST_DATA:
				DialogUtil.hideProgressDialog();
				Log.i(TAG, "连接成功，获取蓝牙历史数据成功");
				if (msg.arg1 == 0) {
					getRightHist = true;
					rightHistData = (int[][]) msg.obj;
					if (rightHistData != null) {
						Log.i(TAG, "右脚一共有" + rightHistData.length + "天的历史数据");
					} else {
						Log.i(TAG, "右脚没历史数据");
					}
				} else if (msg.arg1 == 1) {
					getLeftHist = true;
					leftHistData = (int[][]) msg.obj;
					if (leftHistData != null) {
						Log.i(TAG, "左脚一共有" + leftHistData.length + "天的历史数据");
					} else {
						Log.i(TAG, "左脚没历史数据");
					}
				}
				if (getLeftHist && getRightHist) {
					getLeftHist = false;
					getRightHist = false;
					if (rightHistData != null && leftHistData != null) {
						Log.i(TAG, "左右脚历史数据均不为空");
						if (leftHistData.length >= rightHistData.length) {
							calHistData(leftHistData, rightHistData);
						} else {
							calHistData(rightHistData, leftHistData);
						}
					} else if (rightHistData != null && leftHistData == null) {
						Log.i(TAG, "右脚历史数据不为空");
						for (int i = 0; i < rightHistData.length; i++) {
							int steps = rightHistData[i][1];
							int go_time = rightHistData[i][2];
							int sta_time = rightHistData[i][3];
							int sit_time = rightHistData[i][4];
							int slp_time = rightHistData[i][5];
							int time = rightHistData[i][6];
							setHistData(sit_time, sta_time, go_time, slp_time,
									steps, time);
						}
					} else if (rightHistData == null && leftHistData != null) {
						Log.i(TAG, "左脚历史数据不为空");
						for (int i = 0; i < leftHistData.length; i++) {
							int steps = leftHistData[i][1];
							int go_time = leftHistData[i][2];
							int sta_time = leftHistData[i][3];
							int sit_time = leftHistData[i][4];
							int slp_time = leftHistData[i][5];
							int time = leftHistData[i][6];
							setHistData(sit_time, sta_time, go_time, slp_time,
									steps, time);
						}
					}
					DialogUtil.showCancelProgressDialog(mContext, "",
							"正在获取日常数据...", true, true);
					Log.i(TAG, "准备和蓝牙建立日常数据连接");
					AmomeApp.bleShoes
							.getShoesDailyDataDouble(shoesReadDailyDataCallback);// 开始获取蓝牙的今日日常数据
				}
				break;
			case MSG_RE_CONNECT:
				if (AmomeApp.bleShoes != null) {
					Log.i(TAG, "AmomeApp.bleShoes 不为空 需要断开连接再重连");
					T.showToast(mContext, "正在断开连接", 0);
					AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback); // 先断开连接
					BleService.mSleep(1000);
					openBle();
				} else {
					Log.i(TAG, "AmomeApp.bleShoes 为空 不需要断开连接 开始准备连接");
					openBle();
				}
				break;
			case MSG_SET_DAILYDATA:
				Log.i(TAG, "今日数据上传到服务端成功");
				Log.i(TAG, "准备从服务端获取历史数据");
				getHistData(); // 今日数据上传完成后，准备获取所有的历史数据
				break;
			case MSG_GET_DAILYDATA:
				Log.i(TAG, "服务端获取今日数据成功");
				String exerJson = (String) msg.obj;
				if (exerJson.equals("[{}]")) {
					Log.i(TAG, "从服务端获取到今日数据为空");
					T.showToast(mContext, "未从服务端获取到今日数据", 0);
				} else {
					Log.i(TAG, "从服务端获取到今日数据不为空");
					ExerciseList = gson.fromJson(exerJson,
							new TypeToken<List<ExerciseDataInfo>>() {
							}.getType());
					exerDataInfo = ExerciseList.get(0);
					sit_cal = exerDataInfo.sitclr;
					sta_cal = exerDataInfo.stdclr;
					go_cal = exerDataInfo.wlkclr;
					sit_time = exerDataInfo.sittme;
					sta_time = exerDataInfo.stdtme;
					go_time = exerDataInfo.wlktme;
					steps = exerDataInfo.stpnum;
					stpfqc = exerDataInfo.stpfqc;

					total_cal = sit_cal + sta_cal + go_cal;

					tv_sit.setText(formatter.format(sit_time * 1000));
					tv_stand.setText(formatter.format(sta_time * 1000));
					tv_step.setText(steps + " 步");
					tv_step_fre.setText("步速 " + stpfqc + "步/min");
					tv_total_cal.setText((float) (Math.round(total_cal * 100))
							/ 100 + "");

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

				Log.i(TAG, "准备从服务端获取历史数据");
				getHistData();
				break;

			case MSG_GET_HISTDATA: {
				Log.i(TAG, "服务端获取历史数据成功");
				String exerhistJson = (String) msg.obj;
				if (exerhistJson.equals("[{}]")) {
					Log.i(TAG, "从服务端获取到历史数据为空");
					T.showToast(mContext, "未从服务端获取到历史数据", 0);
				} else {
					Log.i(TAG, "从服务端获取到历史数据不为空");
					exerciseList = gson.fromJson(exerhistJson,
							new TypeToken<List<ExerciseDataInfo>>() {
							}.getType());
					Log.i(TAG, "准备计算");
					for (int i = 0; i < exerciseList.size(); i++) {
						for (int j = 0; j < modHistDataList.size(); j++) {
							if (exerciseList.get(i).date.equals(modHistDataList
									.get(j).date)) {
								modHistDataList.get(j).rsttme = exerciseList
										.get(i).rsttme;
								modHistDataList.get(j).sitclr = exerciseList
										.get(i).sitclr;
								modHistDataList.get(j).sittme = exerciseList
										.get(i).sittme;
								modHistDataList.get(j).stdclr = exerciseList
										.get(i).stdclr;
								modHistDataList.get(j).stdtme = exerciseList
										.get(i).stdtme;
								modHistDataList.get(j).stpfqc = exerciseList
										.get(i).stpfqc;
								modHistDataList.get(j).stpnum = exerciseList
										.get(i).stpnum;
								modHistDataList.get(j).useid = exerciseList
										.get(i).useid;
								modHistDataList.get(j).wlkclr = exerciseList
										.get(i).wlkclr;
								modHistDataList.get(j).wlktme = exerciseList
										.get(i).wlktme;
								break;
							}
						}
					}
					// for (int i = exerciseList.size(); i > 0; i--) {
					// modHistDataList.add(modHistDataList.get(i - 1));
					// }
					Log.i(TAG, "modHistDataList" + modHistDataList.size());
				}
				EventBus.getDefault().post(
						new ExerciseHistEvent("need to update HistoryUI",
								modHistDataList));
			}
				break;
			case MSG_SET_DAILYDATA_FAIL:
				Log.i(TAG, "今日数据上传到服务端失败");
				// T.showToast(mContext, "今日数据上传到服务端失败", 0);
				Log.i(TAG, "准备从服务端获取历史数据");
				getHistData();
				break;
			case MSG_GET_DAILYDATA_FAIL:
				Log.i(TAG, "服务端获取今日数据失败");
				Log.i(TAG, "准备从服务端获取历史数据");
				getHistData();
				break;
			case MSG_GET_HISTDATA_FAIL:
				Log.i(TAG, "服务端获取历史数据失败");
				EventBus.getDefault().post(
						new ExerciseHistEvent("need to update HistoryUI",
								modHistDataList));
				break;
			case MSG_SET_HISTDATA:
				Log.i(TAG, "历史数据上传到服务端成功");
				break;
			case MSG_SET_HISTDATA_FAIL:
				Log.i(TAG, "历史数据上传到服务端失败");
				break;
			case MSG_CAL_TODAY_DATA_DOUBLE:
				DialogUtil.hideProgressDialog();
				last_go_time = go_time;
				last_sit_time = sit_time;
				last_sta_time = sta_time;
				lastSteps = steps;
				if (msg.arg1 == 0) {
					lastRightData = rightData;
					rightData = (DailyData) msg.obj;
				} else if (msg.arg1 == 1) {
					lastLeftData = leftData;
					leftData = (DailyData) msg.obj;
				}
				if (leftData != null && rightData != null
						&& (lastLeftData == null || lastRightData == null)) {
					if (SpfUtil.readLeftSitTime(mContext).equals("")
							|| SpfUtil.readRightSitTime(mContext).equals("")
							|| SpfUtil.readTodaySitTime(mContext).equals("")
							|| SpfUtil.readLeftStandTime(mContext).equals("")
							|| SpfUtil.readRightStandTime(mContext).equals("")
							|| SpfUtil.readTodayStandTime(mContext).equals("")
							|| SpfUtil.readLeftWalkTime(mContext).equals("")
							|| SpfUtil.readRightWalkTime(mContext).equals("")
							|| SpfUtil.readTodayWalkTime(mContext).equals("")
							|| SpfUtil.readLeftSteps(mContext).equals("")
							|| SpfUtil.readRightSteps(mContext).equals("")
							|| SpfUtil.readTodaySteps(mContext).equals("")) {
						sit_time = (int) (leftData.sit_time >= rightData.sit_time ? leftData.sit_time
								: rightData.sit_time);
						sta_time = (int) (leftData.sta_time <= rightData.sta_time ? leftData.sta_time
								: rightData.sta_time);
						go_time = (int) (leftData.go_time >= rightData.go_time ? leftData.go_time
								: rightData.go_time);
						steps = leftData.back_steps >= rightData.back_steps ? leftData.back_steps * 2
								: rightData.back_steps * 2;
					} else {
						int time = (int) (System.currentTimeMillis() / 1000 - Long
								.valueOf(SpfUtil.readTime(mContext)));
						Log.i(TAG, "时间差 " + time);

						// 单脚差值
						int leftSitTime = (int) (leftData.sit_time - Integer
								.valueOf(SpfUtil.readLeftSitTime(mContext)));
						int rightSitTime = (int) (rightData.sit_time - Integer
								.valueOf(SpfUtil.readRightSitTime(mContext)));
						int leftStandTime = (int) (leftData.sta_time - Integer
								.valueOf(SpfUtil.readLeftStandTime(mContext)));
						int rightStandTime = (int) (rightData.sta_time - Integer
								.valueOf(SpfUtil.readRightStandTime(mContext)));
						int leftWalkTime = (int) (leftData.go_time - Integer
								.valueOf(SpfUtil.readLeftWalkTime(mContext)));
						int rightWalkTime = (int) (rightData.go_time - Integer
								.valueOf(SpfUtil.readRightWalkTime(mContext)));
						int leftSteps = leftData.back_steps
								- Integer.valueOf(SpfUtil
										.readLeftSteps(mContext));
						int rightSteps = rightData.back_steps
								- Integer.valueOf(SpfUtil
										.readRightSteps(mContext));
						// 左右脚比较后的值
						int sitMarginTime = leftSitTime >= rightSitTime ? leftSitTime
								: rightSitTime; // 不是最终的数据，需要另外求
						int walkMarginTime = leftWalkTime >= rightWalkTime ? leftWalkTime
								: rightWalkTime;
						int standMarginTime = leftStandTime <= rightStandTime ? leftStandTime
								: rightStandTime;
						int stepsMargin = leftSteps >= rightSteps ? leftSteps
								: rightSteps;

						int activeTime = leftSitTime;
						if (activeTime <= rightSitTime) {
							activeTime = rightSitTime;
						}
						if (activeTime <= leftStandTime) {
							activeTime = leftStandTime;
						}
						if (activeTime <= rightStandTime) {
							activeTime = rightStandTime;
						}
						if (activeTime <= leftWalkTime) {
							activeTime = leftWalkTime;
						}
						if (activeTime <= rightWalkTime) {
							activeTime = rightWalkTime;
						}
						activeTime = activeTime >= (sitMarginTime
								+ walkMarginTime + standMarginTime) ? activeTime
								: (sitMarginTime + walkMarginTime + standMarginTime);
						int sleepTime = time - activeTime;
						int finalSitTime = time - walkMarginTime
								- standMarginTime - sleepTime;
						sit_time = Integer.valueOf(SpfUtil
								.readTodaySitTime(mContext)) + finalSitTime;
						sta_time = Integer.valueOf(SpfUtil
								.readTodayStandTime(mContext))
								+ standMarginTime;
						go_time = Integer.valueOf(SpfUtil
								.readTodayWalkTime(mContext)) + walkMarginTime;
						steps = Integer.valueOf(SpfUtil
								.readTodaySteps(mContext)) + stepsMargin * 2;
					}

					// Log.i(TAG, "本地步数" + steps + "步");
					// Log.i(TAG, "本地坐时间：" + formatter.format(sit_time * 1000));
					// Log.i(TAG, "本地站时间：" + formatter.format(sta_time * 1000));
					// Log.i(TAG, "本地走时间：" + formatter.format(go_time * 1000));
				} else if (lastLeftData != null && lastRightData != null) {
					if (handleData) {
						Log.i(TAG, "****left:" + lastLeftData.sit_time + "  "
								+ leftData.sit_time);
						Log.i(TAG, "****right:" + lastRightData.sit_time + "  "
								+ rightData.sit_time);
						String time = String
								.valueOf(System.currentTimeMillis() / 1000);
						Log.i(TAG, "当前" + System.currentTimeMillis());
						SpfUtil.keepTime(mContext, time);
						if (lastLeftData.go_time != leftData.go_time
								|| lastRightData.go_time != rightData.go_time) {
							sit_time = last_sit_time;
							sta_time = last_sta_time;
						}

						else if (lastLeftData.sta_time != leftData.sta_time
								&& lastRightData.sta_time != rightData.sta_time) {
							if ((leftData.sta_time - lastLeftData.sta_time) <= (rightData.sta_time - lastRightData.sta_time)) {
								sta_time = (int) (last_sta_time
										+ leftData.sta_time - lastLeftData.sta_time);
							} else {
								sta_time = (int) (last_sta_time
										+ rightData.sta_time - lastRightData.sta_time);
							}
							go_time = last_go_time;
							steps = lastSteps;
							sit_time = last_sit_time;
						}

						else if (lastLeftData.sit_time != leftData.sit_time
								|| lastRightData.sit_time != rightData.sit_time) {
							if ((leftData.sit_time - lastLeftData.sit_time) >= (rightData.sit_time - lastRightData.sit_time)) {
								sit_time = (int) (last_sit_time
										+ leftData.sit_time - lastLeftData.sit_time);
							} else {
								sit_time = (int) (last_sit_time
										+ rightData.sit_time - lastRightData.sit_time);
							}
							go_time = last_go_time;
							steps = lastSteps;
							sta_time = last_sta_time;
						}

						else if (lastLeftData.sta_time != leftData.sta_time
								&& lastRightData.sit_time == rightData.sit_time
								&& lastRightData.sta_time == rightData.sta_time
								&& lastRightData.go_time == rightData.go_time) {
							Log.i(TAG, "left:" + lastLeftData.sit_time + "  "
									+ leftData.sit_time);
							Log.i(TAG, "right:" + lastRightData.sit_time + "  "
									+ rightData.sit_time);
							sta_time = last_sta_time;
							go_time = last_go_time;
							steps = lastSteps;
							sit_time = (int) (last_sit_time + leftData.sta_time - lastLeftData.sta_time);
						}

						else if (lastRightData.sta_time != rightData.sta_time
								&& lastLeftData.sit_time == leftData.sit_time
								&& lastLeftData.sta_time == leftData.sta_time
								&& lastLeftData.go_time == leftData.go_time) {
							sta_time = last_sta_time;
							go_time = last_go_time;
							steps = lastSteps;
							sit_time = (int) (last_sit_time
									+ rightData.sta_time - lastRightData.sta_time);
						} else {
						}

						if ((leftData.go_time - lastLeftData.go_time) >= (rightData.go_time - lastRightData.go_time)) {
							go_time = (int) (last_go_time + leftData.go_time - lastLeftData.go_time);
						} else {
							go_time = (int) (last_go_time + rightData.go_time - lastRightData.go_time);
						}

						/** 步数第二种算法 */
						steps = lastSteps + leftData.back_steps
								- lastLeftData.back_steps
								+ rightData.back_steps
								- lastRightData.back_steps;
						/** 步数第一种算法 */
						// if ((leftData.back_steps - lastLeftData.back_steps)
						// >= (rightData.back_steps - lastRightData.back_steps))
						// {
						// if (leftData.back_steps - lastLeftData.back_steps >=
						// 3
						// && rightData.back_steps != lastRightData.back_steps)
						// {
						// } else {
						// steps = lastSteps
						// + (leftData.back_steps - lastLeftData.back_steps)
						// * 2;
						// }
						// } else if ((leftData.back_steps -
						// lastLeftData.back_steps) < (rightData.back_steps -
						// lastRightData.back_steps)) {
						// if (rightData.back_steps - lastRightData.back_steps
						// >= 3
						// && leftData.back_steps != lastLeftData.back_steps) {
						// } else {
						// steps = lastSteps
						// + (rightData.back_steps - lastRightData.back_steps)
						// * 2;
						// }
						//
						// }

						// WriteFile.method1("/sdcard/amome/todayData.txt", "\n"
						// + "left:  " + leftData.back_steps + "  "
						// + lastLeftData.back_steps + "\n" + "right:  "
						// + rightData.back_steps + "  "
						// + lastRightData.back_steps + "\n" + "steps:  "
						// + steps + "\n" + System.currentTimeMillis()
						// + "\n" + "************************");

						SpfUtil.keepLeftSitTime(mContext, leftData.sit_time
								+ "");
						SpfUtil.keepLeftStandTime(mContext, leftData.sta_time
								+ "");
						SpfUtil.keepLeftWalkTime(mContext, leftData.go_time
								+ "");
						SpfUtil.keepLeftSteps(mContext, leftData.back_steps
								+ "");
						SpfUtil.keepRightSitTime(mContext, rightData.sit_time
								+ "");
						SpfUtil.keepRightStandTime(mContext, rightData.sta_time
								+ "");
						SpfUtil.keepRightWalkTime(mContext, rightData.go_time
								+ "");
						SpfUtil.keepRightSteps(mContext, rightData.back_steps
								+ "");
						SpfUtil.keepTodaySitTime(mContext, sit_time + "");
						SpfUtil.keepTodayStandTime(mContext, sta_time + "");
						SpfUtil.keepTodayWalkTime(mContext, go_time + "");
						SpfUtil.keepTodaySteps(mContext, steps + "");
						handleData = false;
					} else {
						handleData = true;
					}
				}
				if (go_time != 0) {
					stpfqc = 60 * steps / go_time;
				} else {
					stpfqc = 0;
				}
				tv_sit.setText(formatter.format(sit_time * 1000));
				tv_stand.setText(formatter.format(sta_time * 1000));
				tv_step.setText(steps + " 步");
				tv_walk.setText(formatter.format(go_time * 1000)); // 临时
				if (go_time != 0) {
					tv_step_fre.setText("步速 " + stpfqc + "步/min");
				} else {
					tv_step_fre.setText("步速 " + 0 + "步/min");
				}
				// 计算卡路里
				sit_cal = ((int) (sit_time / 5) * (1.4f / 12));
				sta_cal = ((int) (sta_time / 5) * (2f / 12));
				go_cal = ((int) (go_time / 5) * (3.7f / 12));
				total_cal = sit_cal + sta_cal + go_cal;
				tv_total_cal.setText((float) (Math.round(total_cal * 100))
						/ 100 + "");

				// 如果坐站走时间增加一分钟，就把卡路里数据传给评估页面
				if (((sit_time != 0) && (sit_time % 60 == 0))
						|| ((sta_time != 0) && (sta_time % 60 == 0))
						|| ((go_time != 0) && (go_time % 60 == 0))) {
					cal_evaluate[0] = sit_cal;
					cal_evaluate[1] = sta_cal;
					cal_evaluate[2] = go_cal;
					EventBus.getDefault().post(
							new ExerciseEvaluateEvent(
									"need to update EvaluateUI",
									CalEvaluate(total_cal), cal_evaluate));
				}
				// 饼状图数据
				sit_one = sit_cal / (sit_cal + sta_cal + go_cal) * 100f;
				sit_two = 100f - sit_one;
				sta_one = sta_cal / (sit_cal + sta_cal + go_cal) * 100f;
				sta_two = 100f - sta_one;
				go_one = go_cal / (sit_cal + sta_cal + go_cal) * 100f;
				go_two = 100f - go_one;

				if (sucConnect) {
					AmomeApp.exercise_flag = true; 
					sucConnect = false;

					// 传送坐站走卡路里给评估页面
					cal_evaluate[0] = sit_cal;
					cal_evaluate[1] = sta_cal;
					cal_evaluate[2] = go_cal;
					EventBus.getDefault().post(
							new ExerciseEvaluateEvent(
									"need to update EvaluateUI",
									CalEvaluate(total_cal), cal_evaluate));
					Log.i(TAG, "准备上传今日数据到服务端");
					setDailyData(); // 第一次成功链接后，把数据发送到服务端
				}

				// 传送步数，步频，坐时间，站时间，总消耗，走时间给雷达图页面
				radar[0] = steps;
				radar[1] = stpfqc;
				radar[2] = sit_time;
				radar[3] = sta_time;
				radar[4] = total_cal;
				radar[5] = go_time;
				EventBus.getDefault().post(
						new ExerciseRadarEvent("need to update EvaluateUI",
								radar));
				// 更新饼状图
				setData1(2, 100); // 坐
				setData2(2, 100); // 站
				setData3(2, 100); // 走
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
		EventBus.getDefault().register(this);
		mContext = getActivity();
		Log.i(TAG, System.currentTimeMillis() / 1000 + "");
		formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Log.i(TAG, SpfUtil.readDeviceToShoeLeft(mContext));
		Log.i(TAG, SpfUtil.readDeviceToShoeRight(mContext));
		view = inflater.inflate(R.layout.fragment_exercise_today, container,
				false);
		initView(view);
		initHistDataList();
		getHistData();
		// EventBus.getDefault().post(
		// new ExerciseHistEvent("need to update HistoryUI",
		// modHistDataList));
		return view;

	}

	/**
	 * 准备建立蓝牙连接
	 */
	private void openBle() {
		if (enableBluetooth()) {
			Log.i(TAG, "进入运动前蓝牙就打开的情况");
			preConnect();
		} else {
			Log.i(TAG, "未开启蓝牙");
		}
	}

	/**
	 * 判断蓝牙是否开启
	 */
	public boolean enableBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		// 判断是否有蓝牙功能
		if (adapter != null && !adapter.isEnabled()) {
			Log.i(TAG, "蓝牙未打开");
			return false;
		}
		Log.i(TAG, "蓝牙已打开");
		return true;
	}

	/**
	 * 建立蓝牙连接，（没绑定跳绑定，绑定了获取蓝牙历史数据）
	 */
	private void preConnect() {
		sucConnect = true;
		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			iv_exercise_bind.setVisibility(View.VISIBLE);
			DialogUtil.showAlertDialog(mContext, "绑定提示", "是否绑定智能鞋", "确定", "取消",
					new OnAlertViewClickListener() {
						@Override
						public void confirm() {
							startActivity(new Intent(mContext,
									BindActivity.class));
						}

						@Override
						public void cancel() {
						}
					});
		} else {
			iv_exercise_bind.setVisibility(View.GONE);
			try {
				if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
					Log.i(TAG, "智能鞋状态为已连接");
					AmomeApp.bleShoes.getHistData(shoesGetHistDataCallback); // 开始获取蓝牙的历史数据
				} else {
					Log.i(TAG, "智能鞋状态为未连接");
					bleFlag = BleConstants.MSG_GETBATTERY;
					connectShoes();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtil.hideProgressDialog();
				Log.i(TAG, "未找到智能鞋!!");
				e.printStackTrace();
			}
		}
	}

	private void connectShoes() {
		if (AmomeApp.bleShoes == null) {
			DialogUtil.showProgressDialog(mContext, "", "智能鞋连接中...");
			AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTING;
			AmomeApp.bleShoes = new BleShoes(
					SpfUtil.readDeviceToShoeLeft(mContext),
					SpfUtil.readDeviceToShoeRight(mContext), shoesCreCallback,
					mContext);
		} else {
			Log.i(TAG, "智能鞋已经连接了");
		}
	}

	BleShoes.shoesCreCallback shoesCreCallback = new shoesCreCallback() {

		@Override
		public void isCreSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋连接完成");
				AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTED;
				connectSuc = true;
				T.showToast(mContext, "智能鞋连接完成", 0);
				switch (bleFlag) {
				case BleConstants.MSG_GETBATTERY:
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
					break;
				case BleConstants.MSG_GETDAILY:
					AmomeApp.bleShoes
							.getShoesDailyDataDouble(shoesReadDailyDataCallback);
					break;
				case BleConstants.MSG_SYNCHIST:
					AmomeApp.bleShoes.getHistData(shoesGetHistDataCallback);
					break;
				default:
					break;
				}
			} else {
				Log.i(TAG, "智能鞋连接出错");
				// 增加处理
				try {
					AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback); // 断开连接
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

		@Override
		public void isConnect(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (!arg0) {
				Log.i(TAG, addr + "设备连接出错，准备断开连接触发重连提示框");
				DialogUtil.showAlertDialog(mContext, "", "连接失败，是否重连？", "确定",
						"取消", new OnAlertViewClickListener() {
							@Override
							public void confirm() {
								openBle();
							}

							@Override
							public void cancel() {
							}
						});
				if (addr.equals(SpfUtil.readDeviceToShoeLeft(mContext))) {
					AmomeApp.bleShoes.disSingleShoeConnect(
							shoesSingleDisconnectCallback,
							SpfUtil.readDeviceToShoeRight(mContext));
				} else {
					AmomeApp.bleShoes.disSingleShoeConnect(
							shoesSingleDisconnectCallback,
							SpfUtil.readDeviceToShoeRight(mContext));
				}
			}
		}

		@Override
		public void isRec(boolean arg0) {
			// TODO Auto-generated method stub

		}
	};

	BleShoes.shoesGetBatteryInfoCallback shoesGetBatteryInfoCallback = new shoesGetBatteryInfoCallback() {

		@Override
		public void isGetBatterySucc(boolean arg0, int leftVal, int rightVal) {
			// TODO Auto-generated method stub
			DialogUtil.hideProgressDialog();
			if (arg0) {
				Log.i(TAG, "电量" + leftVal + "," + rightVal);
				if (leftVal < 5 || rightVal < 5) {
					T.showToast(mContext, "电量不足请充电" + leftVal + " " + rightVal,
							0);
					DialogUtil.showAlertDialog(mContext, "", "电量不足，是否继续使用？",
							"确定", "取消", new OnAlertViewClickListener() {
								@Override
								public void confirm() {
									AmomeApp.bleShoes
											.getHistData(shoesGetHistDataCallback); // 开始获取蓝牙的历史数据
								}

								@Override
								public void cancel() {
									AmomeApp.bleShoes
											.disShoesConnect(shoesDisconnectCallback); // 断开连接
								}
							});
				} else {
					AmomeApp.bleShoes.getHistData(shoesGetHistDataCallback); // 开始获取蓝牙的历史数据
				}
			}
		}

	};

	BleShoes.shoesReadDailyDataCallback shoesReadDailyDataCallback = new shoesReadDailyDataCallback() {

		@Override
		public void readDailyData(String addr, DailyData dailyData) {
			// TODO Auto-generated method stub
			// Log.i(TAG, "readDailyData" + addr);
			DialogUtil.hideProgressDialog();
			Message msg = Message.obtain();
			// 双脚
			int flag = Integer.parseInt(addr.substring(16));
			if (flag % 2 == 1) {
				msg.arg1 = 1;
			} else if (flag % 2 == 0) {
				msg.arg1 = 0;
			}
			msg.obj = dailyData;
			msg.what = MSG_CAL_TODAY_DATA_DOUBLE;
			mHandler.sendMessage(msg);
		}
	};

	BleShoes.shoesGetHistDataCallback shoesGetHistDataCallback = new shoesGetHistDataCallback() {

		@Override
		public void isGetHistSucc(boolean arg0, int arg1, int[][] histData) {
			// TODO Auto-generated method stub
			if (arg0) {
				Message msg = Message.obtain();
				if (arg1 == 0) {
					msg.arg1 = 0;
				} else if (arg1 == 1) {
					msg.arg1 = 1;
				}
				msg.obj = histData;
				msg.what = MSG_CAL_HIST_DATA;
				mHandler.sendMessage(msg);
			} else {
				Log.i(TAG, "智能鞋历史数据获取失败");
				AmomeApp.bleShoes
						.getShoesDailyDataDouble(shoesReadDailyDataCallback);
				// 增加处理
			}
		}
	};

	BleShoes.shoesDisconnectCallback shoesDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋断开连接成功");
				T.showToast(mContext, "断开连接成功", 0);
				DialogUtil.hideProgressDialog();
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			} else {
				T.showToast(mContext, "断开连接失败", 0);
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				// 增加处理
			}
		}
	};

	BleShoes.shoesDisconnectCallback shoesSingleDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "另一只智能鞋断开连接成功");
				DialogUtil.hideProgressDialog();
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			} else {
				Log.i(TAG, "另一只智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			}
		}
	};

	/**
	 * 历史页生成日期List
	 */
	@SuppressLint("SimpleDateFormat")
	private void initHistDataList() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		// 生成日期list
		long oneDayTime = 1000 * 3600 * 24;
		Date todyDate = new Date();
		Date finishDate = new Date(todyDate.getTime() + 6 * oneDayTime);
		String finishDateStr = finishDate.getTime() + "";// 定义五天后的日期为柱状图截止时间
		String startDateStr = TimeUtils.getTime("2017年9月1日 00:00"); // 定义历史数据柱状图起始时间
		int dayNum = (int) ((Long.valueOf(finishDateStr) - Long
				.valueOf(startDateStr)) / (1000 * 3600 * 24));// 计算要生成的天数
		Date date = finishDate;
		for (int i = 0; i < dayNum; i++) {
			date = new Date(date.getTime() - oneDayTime);
			String dateString = simpleDateFormat.format(date);
			ExerciseDataInfo exerDataInfo = new ExerciseDataInfo();
			exerDataInfo.date = dateString;
			exerDataInfo.rsttme = 0;
			exerDataInfo.sitclr = 0;
			exerDataInfo.sittme = 0;
			exerDataInfo.stdclr = 0;
			exerDataInfo.stdtme = 0;
			exerDataInfo.stpfqc = 0;
			exerDataInfo.stpnum = 0;
			exerDataInfo.useid = " ";
			exerDataInfo.wlkclr = 0;
			exerDataInfo.wlktme = 0;
			modHistDataList.add(exerDataInfo);
			Log.i(TAG, dateString);
		}
	}

	/**
	 * 设置日常数据
	 */
	private void setDailyData() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		int time = (int) (System.currentTimeMillis() / 1000);
		params.put("calltype", ClientConstant.SETDAILYDATA);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("lble", SpfUtil.readDeviceToShoeLeft(mContext));
		params.put("rble", SpfUtil.readDeviceToShoeRight(mContext));
		params.put("sitclr", sit_cal + "");
		params.put("stdclr", sta_cal + "");
		params.put("wlkclr", go_cal + "");
		params.put("sittme", sit_time + "");
		params.put("stdtme", sta_time + "");
		params.put("wlktme", go_time + "");
		params.put("rsttme", slp_time + "");
		params.put("stpnm", steps + "");
		params.put("stpfqc", stpfqc + "");
		params.put("date", time + "");

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_SET_DAILYDATA, params,
				ClientConstant.DAILYDATA_URL);

	}

	/**
	 * 获取日常数据
	 */
	private void getDailyData() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETDAILYDATA);
		params.put("useid", SpfUtil.readUserId(mContext));

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_DAILYDATA, params,
				ClientConstant.DAILYDATA_URL);

	}

	/**
	 * 设置历史数据
	 */
	private void setHistData(int sit_time, int sta_time, int go_time,
			int slp_time, int steps, int time) {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		int stpfqc;
		float sit_cal, sta_cal, go_cal;
		if (go_time != 0) {
			stpfqc = 60 * steps / go_time;
		} else {
			stpfqc = 0;
		}
		sit_cal = (float) ((sit_time / 60) * 1.4);
		sta_cal = (float) ((sta_time / 60) * 2);
		go_cal = (float) ((go_time / 60) * 3.7);
		Log.i(TAG, "即将上传到服务器的历史数据：时间=" + time);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.SETDAILYDATA);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("lble", SpfUtil.readDeviceToShoeLeft(mContext));
		params.put("rble", SpfUtil.readDeviceToShoeRight(mContext));
		params.put("sitclr", sit_cal + "");
		params.put("stdclr", sta_cal + "");
		params.put("wlkclr", go_cal + "");
		params.put("sittme", sit_time + "");
		params.put("stdtme", sta_time + "");
		params.put("wlktme", go_time + "");
		params.put("rsttme", slp_time + "");
		params.put("stpnm", steps + "");
		params.put("stpfqc", stpfqc + "");
		params.put("date", time + "");

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_SET_HISTDATA, params,
				ClientConstant.HISTDATA_URL);

	}

	/**
	 * 获取所有的历史数据
	 */
	private void getHistData() {
		Log.i(TAG, "进入了getHistData");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETHISTDATA);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_HISTDATA, params,
				ClientConstant.HISTDATA_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			// DialogUtil.hideProgressDialog();
			String result;
			switch (type) {
			case MSG_SET_DAILYDATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
					} else {
						msg.what = MSG_SET_DAILYDATA;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_SET_DAILYDATA解析失败");
					Message msg = Message.obtain();
					msg.what = MSG_SET_DAILYDATA_FAIL;
					mHandler.sendMessage(msg);
				}
				break;
			case MSG_GET_DAILYDATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
					} else {
						msg.what = MSG_GET_DAILYDATA_FAIL;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_DAILYDATA解析失败");
					Message msg = Message.obtain();
					msg.what = MSG_GET_DAILYDATA_FAIL;
					mHandler.sendMessage(msg);
				}
				break;
			case MSG_GET_HISTDATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
						Log.i(TAG, return_msg);
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_HISTDATA解析失败");
				}
				break;
			case MSG_SET_HISTDATA:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						msg.what = type;
						msg.obj = return_msg;
					} else {
						msg.what = MSG_SET_DAILYDATA;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_SET_HISTDATA解析失败");
					Message msg = Message.obtain();
					msg.what = MSG_SET_HISTDATA_FAIL;
					mHandler.sendMessage(msg);
				}
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

	private void initView(View view) {
		Log.i(TAG, TAG + "initView");
		tv_sit = (TextView) view.findViewById(R.id.tv_sit);
		tv_stand = (TextView) view.findViewById(R.id.tv_stand);
		tv_step = (TextView) view.findViewById(R.id.tv_step);
		tv_step_fre = (TextView) view.findViewById(R.id.tv_step_fre);
		tv_step_fre.setText("步速 " + stpfqc + "步/min");
		tv_total_cal = (TextView) view.findViewById(R.id.tv_total_cal);

		tv_walk = (TextView) view.findViewById(R.id.tv_walk);
		rl_exercise_bind = (RelativeLayout) view
				.findViewById(R.id.rl_exercise_bind);
		iv_exercise_bind = (ImageView) view.findViewById(R.id.iv_exercise_bind);
		iv_exercise_bind.setOnClickListener(this);

		if (SpfUtil.readDeviceToShoeLeft(mContext) == null
				|| SpfUtil.readDeviceToShoeRight(mContext) == null
				|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext) == ""
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
				|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
			Log.i(TAG, "绑定为空，显示绑定按钮");
			iv_exercise_bind.setVisibility(View.VISIBLE);
		} else {
			Log.i(TAG, "绑定不为空，隐藏绑定按钮");
			iv_exercise_bind.setVisibility(View.GONE);
		}
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

		/**
		 * add a selection listener 值改变时候的监听
		 */
		pie_chart_with_line.setOnChartValueSelectedListener(this);

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

		/**
		 * add a selection listener 值改变时候的监听
		 */
		pie_chart_with_line2.setOnChartValueSelectedListener(this);

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
		/**
		 * add a selection listener 值改变时候的监听
		 */
		pie_chart_with_line3.setOnChartValueSelectedListener(this);
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
		EventBus.getDefault().post(
				new ExerciseEvaluateEvent("need to update EvaluateUI",
						CalEvaluate(total_cal), cal_evaluate));
		EventBus.getDefault().post(
				new ExerciseRadarEvent("need to update EvaluateUI", radar));
	}

	private void calHistData(int[][] bigHistData, int[][] smallHistData) {

		List<Integer> sameDateList = new ArrayList<Integer>();
		// 找出所有时间戳相同的点
		for (int i = 0; i < smallHistData.length; i++) {
			boolean jIsSameToj = false;
			for (int j = 0; j < bigHistData.length; j++) {
				if (smallHistData[i][6] == bigHistData[j][6]) {
					int steps = bigHistData[i][1] > smallHistData[i][1] ? bigHistData[i][1]
							: smallHistData[i][1];
					int go_time = bigHistData[i][2] > smallHistData[i][2] ? bigHistData[i][2]
							: smallHistData[i][2];
					int sta_time = bigHistData[i][3] < smallHistData[i][3] ? bigHistData[i][3]
							: smallHistData[i][3];
					int sit_time = bigHistData[i][4] > smallHistData[i][4] ? bigHistData[i][4]
							: smallHistData[i][4];
					int slp_time = bigHistData[i][5] > smallHistData[i][5] ? bigHistData[i][5]
							: smallHistData[i][5];
					int time = bigHistData[i][6];
					setHistData(sit_time, sta_time, go_time, slp_time, steps,
							time);
					sameDateList.add(smallHistData[i][6]);
					jIsSameToj = true;
					break;
				}
			}
			if (!jIsSameToj) {
				int steps = smallHistData[i][1];
				int go_time = smallHistData[i][2];
				int sta_time = smallHistData[i][3];
				int sit_time = smallHistData[i][4];
				int slp_time = smallHistData[i][5];
				int time = smallHistData[i][6];
				setHistData(sit_time, sta_time, go_time, slp_time, steps, time);
			}
		}
		for (int i = 0; i < bigHistData.length; i++) {
			boolean isSame = false;
			for (int j = 0; j < sameDateList.size(); j++) {
				if (sameDateList.get(j) == bigHistData[i][6]) {
					isSame = true;
					break;
				}
			}
			if (!isSame) {
				int steps = bigHistData[i][1];
				int go_time = bigHistData[i][2];
				int sta_time = bigHistData[i][3];
				int sit_time = bigHistData[i][4];
				int slp_time = bigHistData[i][5];
				int time = bigHistData[i][6];
				setHistData(sit_time, sta_time, go_time, slp_time, steps, time);
			}
		}

	}

	private void setData1(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// 认为设置数据，写死
		yVals1.add(new Entry(sit_one, 0));
		yVals1.add(new Entry(sit_two, 1));
		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度

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

		pie_chart_with_line.setData(data);

		// undo all highlights
		pie_chart_with_line.highlightValues(null);

		pie_chart_with_line.invalidate();
	}

	private void setData2(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

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

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 暂时屏蔽 设置颜色，两种
		colors.add(Color.rgb(66, 162, 255));
		colors.add(Color.rgb(159, 160, 160));
		dataSet.setColors(colors);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		// 设置X显示的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		pie_chart_with_line2.setData(data);

		// undo all highlights
		pie_chart_with_line2.highlightValues(null);

		pie_chart_with_line2.invalidate();
	}

	private void setData3(int count, float range) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		yVals1.add(new Entry(go_one, 0)); // 蓝色
		yVals1.add(new Entry(go_two, 1)); // 灰色

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++)
			xVals.add("");

		// PieDataSet dataSet = new PieDataSet(yVals1, "Election Results"); 不加文字
		PieDataSet dataSet = new PieDataSet(yVals1, "");
		dataSet.setSliceSpace(0f); // 设置各饼状图之间的距离
		dataSet.setSelectionShift(0f); // 选中态多出的长度

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 暂时屏蔽 设置颜色，两种
		colors.add(Color.rgb(66, 211, 255));
		colors.add(Color.rgb(181, 181, 182));
		dataSet.setColors(colors);

		data1 = new PieData(xVals, dataSet);
		// data.setValueFormatter(new PercentFormatter());
		data1.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		data1.setValueTextColor(Color.BLACK);
		data1.setValueTypeface(tf);
		// 设置X显的内容 这里为了满足UI 设置空
		ArrayList<String> a = new ArrayList<>();
		a.add("");

		pie_chart_with_line3.setData(data1);

		// undo all highlights
		pie_chart_with_line3.highlightValues(null);

		pie_chart_with_line3.invalidate();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_exercise_bind:
			startActivity(new Intent(mContext, BindActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====" + hidden); // 隐藏起来时，hidden为true
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
		Log.i(TAG, "此时蓝牙状态为" + AmomeApp.bleShoesState);
		if (!AmomeApp.hidden && !connectSuc
				&& AmomeApp.bleShoesState != BleShoesState.MSG_CONNECTING) {
			// 可能连接种中时用户换鞋，再进入此页面，此时不会触发连接，有问题的
			openBle();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, TAG + "=====onStop=====");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, TAG + "=====onDestroy=====");
		// AmomeApp.getInstance().finishBLEService();
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		System.exit(0);
	}

	/**
	 * 
	 * @param total_cal
	 *            总卡路里
	 * @return 角度
	 */
	public int CalEvaluate(float total_cal) {
		int degree = 0;
		Log.i(TAG, "total_cal=" + total_cal);
		if (total_cal >= 0 && total_cal < 200) {
			degree = (int) (total_cal * 45 / 200 + 225);
		} else if (total_cal >= 200 && total_cal < 450) {
			degree = (int) (total_cal * 45 / 250 + 270);
		} else if (total_cal >= 450 && total_cal < 890) {
			degree = (int) (total_cal * 45 / 440 + 315);
		} else if (total_cal >= 890 && total_cal < 1300) {
			degree = (int) ((total_cal - 890) * 45 / 410);
		} else if (total_cal >= 1300 && total_cal < 1770) {
			degree = (int) ((total_cal - 1300) * 45 / 470 + 45);
		} else if (total_cal >= 1770 && total_cal < 2220) {
			degree = (int) ((total_cal - 1770) * 45 / 450 + 90);
		} else if (total_cal >= 2220) {
			degree = 135;
		}
		Log.i(TAG, "旋转角度为" + degree + "");
		return degree;
	}

	public void onEventMainThread(ExerciseEvent event) {
		String msg = "onEventMainThread收到了消息：" + event.getMsg();
		Log.i(TAG, msg);
		mHandler.sendEmptyMessage(MSG_RE_CONNECT);
		lastLeftData = null;
		lastRightData = null;
		leftData = null;
		rightData = null;
		steps = 0;
		go_time = 0;
		sta_time = 0;
		sit_time = 0;
		slp_time = 0;
	}
}