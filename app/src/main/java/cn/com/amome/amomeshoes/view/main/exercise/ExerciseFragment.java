/**
 * @Title:ExerciseFragment.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-4 上午8:54:34
 */
package cn.com.amome.amomeshoes.view.main.exercise;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.MainViewPagerAdapter;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.events.ExerciseEvent;
import cn.com.amome.amomeshoes.util.SpfUtil;
import de.greenrobot.event.EventBus;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExerciseFragment extends Fragment implements OnClickListener {
	private String TAG = "ExerciseFragment";
	private Context mContext;
	private View rootView;
	private LinearLayout layout;
	private List<View> dots; // 图片标题正文的那些点
	private ArrayList<Fragment> fragmentsList;
	private MainViewPagerAdapter adapter;
	private ViewPager viewPagerLayout;
	private ExerciseMotionFragment exerciseMotionTodayFragment;
	private ExerciseRadarFragment exerciseFragmentData;
	private ExerciseEvaluateFragment exerciseFragmentAnalysis;
	private ExerciseHistoryFragment exerciseFragmentStatistics;
	private TextView tv_title;
	int[] mo;
	int Mark = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_exercise_main, null,
				false);
		mContext = getActivity();
		initView(rootView);
		initDotView(4);
		enableBluetooth();
		return rootView;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		tv_title = (TextView) view.findViewById(R.id.title_tv);
		tv_title.setText("运动");
		view.findViewById(R.id.rl_left).setVisibility(View.GONE);
		viewPagerLayout = (ViewPager) rootView
				.findViewById(R.id.viewpager_layout);
		fragmentsList = new ArrayList<Fragment>();
		exerciseMotionTodayFragment = new ExerciseMotionFragment();
		exerciseFragmentData = new ExerciseRadarFragment();
		exerciseFragmentAnalysis = new ExerciseEvaluateFragment();
		exerciseFragmentStatistics = new ExerciseHistoryFragment();
		fragmentsList.add(exerciseMotionTodayFragment);
		fragmentsList.add(exerciseFragmentAnalysis);
		fragmentsList.add(exerciseFragmentData);
		fragmentsList.add(exerciseFragmentStatistics);
		// adapter = new MainViewPagerAdapter(getFragmentManager(),
		// fragmentsList);
		adapter = new MainViewPagerAdapter(getChildFragmentManager(),
				fragmentsList);
		viewPagerLayout.setAdapter(adapter);
		viewPagerLayout.setCurrentItem(0); // 制定初始化的页面为第一个页面
		viewPagerLayout.setOnPageChangeListener(pageListener);
		viewPagerLayout.setFocusableInTouchMode(true);
		viewPagerLayout.setOffscreenPageLimit(4); // 表示三个界面之间来回切换都不会重新加载
	}

	public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(9, 9);
			lp.leftMargin = 8;
			lp.rightMargin = 8;
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(16,
					16);
			lp2.leftMargin = 8;
			lp2.rightMargin = 8;
			switch (position) {
			case 0:
				viewPagerLayout.setCurrentItem(0);
				if (null != dots) {
					dots.get(Mark).setBackgroundResource(
							R.drawable.exercise_hollowpoint);
					dots.get(position).setBackgroundResource(
							R.drawable.exercise_point);
					dots.get(Mark).setLayoutParams(lp);
					dots.get(position).setLayoutParams(lp2);
					// msgTv.setText(bList.get(i).title);
				}
				Mark = 0;
				break;
			case 1:
				viewPagerLayout.setCurrentItem(1);
				if (null != dots) {
					dots.get(Mark).setBackgroundResource(
							R.drawable.exercise_hollowpoint);
					dots.get(position).setBackgroundResource(
							R.drawable.exercise_point);
					dots.get(Mark).setLayoutParams(lp);
					dots.get(position).setLayoutParams(lp2);
					// msgTv.setText(bList.get(i).title);
				}
				Mark = 1;
				break;
			case 2:
				viewPagerLayout.setCurrentItem(2);
				if (null != dots) {
					dots.get(Mark).setBackgroundResource(
							R.drawable.exercise_hollowpoint);
					dots.get(position).setBackgroundResource(
							R.drawable.exercise_point);
					dots.get(Mark).setLayoutParams(lp);
					dots.get(position).setLayoutParams(lp2);
					// msgTv.setText(bList.get(i).title);
				}
				Mark = 2;
				break;
			case 3:
				viewPagerLayout.setCurrentItem(3);
				if (null != dots) {
					dots.get(Mark).setBackgroundResource(
							R.drawable.exercise_hollowpoint);
					dots.get(position).setBackgroundResource(
							R.drawable.exercise_point);
					dots.get(Mark).setLayoutParams(lp);
					dots.get(position).setLayoutParams(lp2);
					// msgTv.setText(bList.get(i).title);
				}
				Mark = 3;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

	private View view;

	/* 初始化图片圆点索引 */
	private void initDotView(int size) {
		dots = new ArrayList<View>();
		layout = (LinearLayout) rootView.findViewById(R.id.home_ll);
		layout.removeView(view);
		dots.clear();

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(9, 9);
		lp.leftMargin = 8;
		lp.rightMargin = 8;
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(16, 16);
		lp2.leftMargin = 8;
		lp2.rightMargin = 8;
		for (int i = 0; i < size; i++) {
			view = new View(getActivity());
			if (i == 0) {
				view.setBackgroundResource(R.drawable.exercise_point);
				view.setId(i);
				view.setLayoutParams(lp2);
				layout.addView(view);
				dots.add(view);
			} else {
				view.setBackgroundResource(R.drawable.exercise_hollowpoint);
				view.setId(i);
				view.setLayoutParams(lp);
				layout.addView(view);
				dots.add(view);
			}
		}
		// dots.get(0).setBackgroundResource(R.drawable.exercise_point);

	}

	public boolean enableBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		// 判断是否有蓝牙功能
		if (adapter != null && !adapter.isEnabled()) {
			Log.i(TAG, "enableBluetooth()蓝牙未打开，正在打开");
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 0x200);
			return false;
		}
		Log.i(TAG, "enableBluetooth()蓝牙已打开过了");
		return true;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Mark == 1) {
			viewPagerLayout.setCurrentItem(1);
		}
		if (Mark == 2) {
			viewPagerLayout.setCurrentItem(2);
		}
		if (Mark == 3) {
			viewPagerLayout.setCurrentItem(3);
		}
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====" + hidden);
		AmomeApp.hidden = hidden;
		if (hidden) {
		} else {
			// 切换设备后，会执行
			if (enableBluetooth()) {
				if (AmomeApp.exercise_flag == false) {
					Log.i(TAG, "运动页面需要重连");
					EventBus.getDefault().post(
							new ExerciseEvent("need to upgrade UI"));
				} else {
				}
			} else {
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Log.i(TAG, TAG + "===setUserVisibleHint====");
	}
}
