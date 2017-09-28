/**
 * @Title:ExerciseAllMotionFragment.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-29 上午9:42:23
 */
package cn.com.amome.amomeshoes.view.main.exercise;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * TODO<请描述这个类是干什么的>
 */
public class ExerciseMotionFragment extends Fragment implements OnClickListener {
	private String TAG = "ExerciseMotionFragment";
	private YesterdayFragment yesterdayFragment;
	private TodayFragment todayFragment;
	private ImageView tab1Rb, tab2Rb;
	private FragmentTransaction fragtran;
	private FragmentManager fm = getFragmentManager();
	private List<Fragment> lsFragment = new ArrayList<Fragment>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_exercise_motion, null,
				false);
		initView(view);
		fm = getFragmentManager();
		changeFragment(1);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		tab1Rb = (ImageView) view.findViewById(R.id.tabone);
		tab2Rb = (ImageView) view.findViewById(R.id.tabtwo);
		tab1Rb.setOnClickListener(this);
		tab2Rb.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tabone:
			tab1Rb.setBackgroundResource(R.drawable.rb_today);
			tab2Rb.setBackgroundResource(R.drawable.rb_gray_yesterday);
			changeFragment(1);
			break;
		case R.id.tabtwo:
			tab1Rb.setBackgroundResource(R.drawable.rb_gray_today);
			tab2Rb.setBackgroundResource(R.drawable.rb_yesterday);
			changeFragment(2);
			break;
		default:
			break;
		}
	}

	private void changeFragment(int which) {
		fragtran = fm.beginTransaction();
		switch (which) {
		case 1:
			if (null == todayFragment) {
				todayFragment = new TodayFragment();
				fragtran.add(R.id.fragment_exercisemotion, todayFragment);
				lsFragment.add(todayFragment);
			}
			show(fragtran, todayFragment);
			fragtran.commit();
			break;
		case 2:
			if (null == yesterdayFragment) {
				yesterdayFragment = new YesterdayFragment();
				fragtran.add(R.id.fragment_exercisemotion, yesterdayFragment);
				lsFragment.add(yesterdayFragment);
			}
			show(fragtran, yesterdayFragment);
			fragtran.commit();
			break;
		}
	}

	/**
	 * 显示fragment
	 * 
	 * @param f
	 * @param fragment
	 */
	public void show(FragmentTransaction ft, Fragment fragment) {
		for (Fragment _fragment : lsFragment) {
			if (_fragment != fragment) {
				ft.hide(_fragment);
				continue;
			}
			ft.show(_fragment);
		}
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
}
