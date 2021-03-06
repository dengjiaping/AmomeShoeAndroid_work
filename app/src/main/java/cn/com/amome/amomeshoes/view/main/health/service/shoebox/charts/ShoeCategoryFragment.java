package cn.com.amome.amomeshoes.view.main.health.service.shoebox.charts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.MainShoeAdapter;
import cn.com.amome.amomeshoes.adapter.ShoesBoxChartsAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.UserShoesCategoryInfo;
import cn.com.amome.amomeshoes.util.ColorTemplate;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoesBoxListActivity;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 鞋类别饼图
 * 
 * @author css
 */
public class ShoeCategoryFragment extends Fragment implements OnClickListener {
	private String TAG = "ShoeCategoryFragment";
	private Context mContext;
	private View rootView;
	private PieChart mChart;
	private ListView lv_detail;
	private TextView tv_nodatatip;
	private TextView tv_left_tip;
	private ArrayList<String> typeList;
	private ArrayList<Entry> countList;
	private ArrayList<Integer> numList;
	private ArrayList<Integer> newNumList;
	private ArrayList<String> newTypeList;
	private ArrayList<String> scaleList;
	private Gson gson = new Gson();
	private List<UserShoesCategoryInfo> userShoesCategoryList;
	private ShoesBoxChartsAdapter shoesBoxChartsAdapter;
	private static final int MSG_GET_CATEGORY = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_CATEGORY:
					String categoryJson = (String) msg.obj;
					if (categoryJson.equals("[{}]")) {
						typeList.add("");
						countList.add(new Entry(1, 0));
						setDataNull(typeList, countList);
					} else {
						userShoesCategoryList = gson.fromJson(categoryJson,
								new TypeToken<List<UserShoesCategoryInfo>>() {
								}.getType());
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (Integer
									.valueOf(userShoesCategoryList.get(i).count) != 0) {
								typeList.add(userShoesCategoryList.get(i).type);
								numList.add(Integer
										.valueOf(userShoesCategoryList.get(i).count));
							}
						}
						bubbleSort();
						if (numList.size() > 16) {
							newTypeList = new ArrayList<String>();
							newNumList = new ArrayList<Integer>();
							int num = 0;
							for (int i = 15; i < numList.size(); i++) {
								num = num + numList.get(i);
							}
							numList.set(15, num);
							typeList.set(15, "其他");
							float total = 0;
							for (int i = 0; i < 16; i++) {
								newNumList.add(numList.get(i));
								newTypeList.add(typeList.get(i));
								total = total + newNumList.get(i);
							}
							DecimalFormat df = new DecimalFormat("0.00%");
							for (int i = 0; i < newNumList.size(); i++) {
								String scale = df.format(newNumList.get(i)
										/ total);
								scaleList.add(scale);
								countList.add(new Entry(newNumList.get(i), 0));
							}
							setData(newTypeList, countList);
							shoesBoxChartsAdapter = new ShoesBoxChartsAdapter(
									mContext, newTypeList, newNumList,
									scaleList);
							lv_detail.setAdapter(shoesBoxChartsAdapter);
						} else if (numList.size() == 0) {
							tv_nodatatip.setText("暂无可统计数");
							typeList.add("");
							countList.add(new Entry(1, 0));
							setDataNull(typeList, countList);
						} else {
							float total = 0;
							for (int i = 0; i < numList.size(); i++) {
								total = total + numList.get(i);
							}
							DecimalFormat df = new DecimalFormat("0.00%");
							for (int i = 0; i < numList.size(); i++) {
								String scale = df
										.format(numList.get(i) / total);
								scaleList.add(scale);
								countList.add(new Entry(numList.get(i), 0));
							}
							setData(typeList, countList);
							shoesBoxChartsAdapter = new ShoesBoxChartsAdapter(
									mContext, typeList, numList, scaleList);
							lv_detail.setAdapter(shoesBoxChartsAdapter);
						}

					}
					break;
				}
				break;
			default:
				break;
			}
		};
	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_shoebox_brand, null,
				false);
		initView(rootView);
		getUserShoesCategoryList();
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			ShoeChartMainFragmentActivity sm = (ShoeChartMainFragmentActivity) getActivity();
			sm.finishActivity();
			break;

		default:
			break;
		}
	}

	private void initView(View root) {
		lv_detail = (ListView) root.findViewById(R.id.lv_detail);
		tv_nodatatip = (TextView) root.findViewById(R.id.tv_nodatatip);
		tv_left_tip = (TextView) root.findViewById(R.id.tv_left_tip);
		tv_left_tip.setVisibility(View.GONE);
		mChart = (PieChart) root.findViewById(R.id.chart);
		mChart.setUsePercentValues(false);// 设置显示百分比
		mChart.setDescription("");// 描述
		mChart.setExtraOffsets(0, 0, 0, 0);// 圆环距离屏幕上下上下左右的距离 -----------改
		mChart.setDrawSliceText(false);// 设置隐藏饼图上文字，只显示百分比
		mChart.setDrawHoleEnabled(true);// 是否显示圆环中间的洞-----------改
		// mChart.setHoleColorTransparent(true);
		// mChart.setTransparentCircleColor(getResources().getColor(R.color.blue));//
		// 中间圆环的颜色
		// mChart.setTransparentCircleAlpha(110);
		// mChart.setOnChartValueSelectedListener(this);//设置表格上的点，被点击的时候，的回调函数
		mChart.setHoleRadius(30f); // 半径 -----------改
		// mChart.setHoleRadius(0) //实心圆
		mChart.setTransparentCircleRadius(30f);// 半透明圈 -----------改
		mChart.setDrawCenterText(true);// 饼状图中间可以添加文字
		// 如果没有数据的时候，会显示这个，类似ListView的EmptyView
		mChart.setNoDataText(""); // -----------改
		mChart.setRotationAngle(0); // 初始旋转角度
		mChart.setRotationEnabled(false); // 可以手动旋转
		// mChart.setHighlightPerTapEnabled(false); // -----------改
		mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad); // 设置动画
		Legend mLegend = mChart.getLegend(); // 设置比例图
		mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER); // 比例图显示位置
		// 屏蔽-----------改
		// mLegend.setFormSize(20f);// 比例块字体大小
		// mLegend.setXEntrySpace(2f);// 设置距离饼图的距离，防止与饼图重合
		// mLegend.setYEntrySpace(2f);
		// // 设置比例块换行
		// mLegend.setWordWrapEnabled(true);
		// mLegend.setYEntrySpace(10f);
		// mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
		//
		// mLegend.setTextColor(getResources().getColor(R.color.dark_red));
		// mLegend.setForm(Legend.LegendForm.SQUARE);// 设置比例块形状，默认为方块
		mLegend.setEnabled(false);// 设置禁用比例块 -----------改
		typeList = new ArrayList<String>();
		countList = new ArrayList<Entry>();
		numList = new ArrayList<Integer>();
		scaleList = new ArrayList<String>();

		// typeList.add("");
		// countList.add(new Entry(1, 0));
		// setDataNull(typeList, countList);
	}

	/**
	 * 设置饼图的数据
	 * 
	 * @param names
	 *            饼图上显示的比例名称
	 * @param counts
	 *            百分比
	 */
	private void setData(ArrayList<String> names, ArrayList<Entry> counts) {
		PieDataSet dataSet = new PieDataSet(counts, "");
		dataSet.setSliceSpace(0);
		dataSet.setSelectionShift(0);

		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int c : ColorTemplate.COLORFUL_COLORS)
			colors.add(c);

		dataSet.setColors(colors);
		PieData data = new PieData(names, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(12f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		mChart.setData(data);
		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	/**
	 * 设置饼图为空的数据
	 * 
	 * @param names
	 *            饼图上显示的比例名称
	 * @param counts
	 *            百分比
	 */
	private void setDataNull(ArrayList<String> names, ArrayList<Entry> counts) {
		PieDataSet dataSet = new PieDataSet(counts, "");
		dataSet.setSliceSpace(0f);
		dataSet.setSelectionShift(0);// 选中态多出的长度

		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(Color.rgb(203, 203, 206));
		// colors.add(getResources().getColor(R.color.stastic_team));
		dataSet.setColors(colors);
		// dataSet.setSelectionShift(0f);
		PieData data = new PieData(names, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(12f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTextSize(0f); // 设置0，间接屏蔽比例数值
		mChart.setData(data);
		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	/**
	 * 获取鞋种类的数量
	 */
	private void getUserShoesCategoryList() {
		Log.i(TAG, "getUserShoesCategoryList");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOECATEGORY_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CATEGORY, params,
				ClientConstant.SHOEBOX_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_CATEGORY:
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
					Log.i(TAG, "MSG_GET_CATEGORY解析失败");
					typeList.add("");
					countList.add(new Entry(1, 0));
					setDataNull(typeList, countList);
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
			typeList.add("");
			countList.add(new Entry(1, 0));
			setDataNull(typeList, countList);
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ShoeCategoryFragment);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ShoeCategoryFragment);
	}

	public void bubbleSort() {
		int temp; // 记录临时中间值
		String str;
		int size = numList.size(); // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (numList.get(i) < numList.get(j)) { // 交换两数的位置
					temp = numList.get(i);
					str = typeList.get(i);
					typeList.set(i, typeList.get(j));
					typeList.set(j, str);
					numList.set(i, numList.get(j));
					numList.set(j, temp);
				}
			}
		}
	}
}
