package cn.com.amome.amomeshoes.view.main.health.report;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @Title:ReportFragment.java
 * @Description:健康报告界面
 * @author:jyw
 */
public class ReportFragment extends Fragment implements OnClickListener {
	private String TAG = "ReportFragment";

	private Context mContext;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_report_main, null, false);
		mContext = getActivity();
		initView(view);
		return view;
	}

	private void initView(View view) {
		view.findViewById(R.id.iv_foot).setOnClickListener(this);
		view.findViewById(R.id.iv_posture).setOnClickListener(this);
		view.findViewById(R.id.iv_balance).setOnClickListener(this);
		view.findViewById(R.id.iv_walk).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_foot:
			startActivity(new Intent(mContext, FootReportActivity.class));
			break;
		case R.id.iv_posture:
			startActivity(new Intent(mContext, PostureReportActivity.class));
			break;
		case R.id.iv_balance:
			startActivity(new Intent(mContext, BalanceReportActivity.class));
			break;
		case R.id.iv_walk:
			startActivity(new Intent(mContext, WalkingReportActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
