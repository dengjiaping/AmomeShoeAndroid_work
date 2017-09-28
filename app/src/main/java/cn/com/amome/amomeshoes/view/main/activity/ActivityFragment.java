/**
 * @Title:MotionDataFragment.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-4 上午8:54:34
 */
package cn.com.amome.amomeshoes.view.main.activity;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.util.T;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityFragment extends Fragment implements OnClickListener {
	private String TAG = "ActivityFragment";
	private Context mContext;
	private View rootView;
	private TextView tv_title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_activity_main, null, false);
		initView(rootView);
		T.showToast(mContext, "敬请期待", 0);
		return rootView;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.title_tv);
		tv_title.setText("活动");
		view.findViewById(R.id.rl_left).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			Log.i(TAG, TAG + "===onHiddenChanged====");
		} else {
			T.showToast(mContext, "敬请期待", 0);
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Log.i(TAG, TAG + "===setUserVisibleHint====");
	}
}
