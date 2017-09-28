package cn.com.amome.amomeshoes.view.main.health.service;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.service.assistant.AssistMainActivity;
import cn.com.amome.amomeshoes.view.main.health.service.look.LookMainActivity;
import cn.com.amome.amomeshoes.view.main.health.service.ruler.RulerFootRightActivity;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.MainShoesBoxActivity;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @Title:ServiceFragment.java
 * @Description:日常自诊界面
 * @author:jyw
 * @data: 2016-3-3 上午10:43:24
 */
public class ServiceFragment extends Fragment implements OnClickListener {
	private String TAG = "ServiceFragment";
	private Context mContext;
	private ImageView iv_service_measure_shoe, iv_service_look_shoe,
			iv_service_my_shoe, iv_service_helper;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_service_main, null,
				false);
		mContext = getActivity();
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		iv_service_measure_shoe = (ImageView) view
				.findViewById(R.id.iv_service_measure_shoe);
		iv_service_look_shoe = (ImageView) view
				.findViewById(R.id.iv_service_look_shoe);
		iv_service_my_shoe = (ImageView) view
				.findViewById(R.id.iv_service_my_shoe);
		iv_service_helper = (ImageView) view
				.findViewById(R.id.iv_service_helper);

		iv_service_measure_shoe.setOnClickListener(this);
		iv_service_look_shoe.setOnClickListener(this);
		iv_service_my_shoe.setOnClickListener(this);
		iv_service_helper.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_service_look_shoe:// 看一看
			Intent intent1 = new Intent(mContext, LookMainActivity.class);
			intent1.putExtra("nickname", "0自己");
			startActivity(intent1);
			break;
		case R.id.iv_service_measure_shoe:// 量一量
			MobclickAgent.onEvent(mContext, ClassType.rulerfoot_btn);
			Intent in = new Intent(mContext, RulerFootRightActivity.class);
			in.putExtra("leftValue", "服务");
			in.putExtra("btnValue", "开始测量");
			in.putExtra("nickname", "0自己");
			startActivity(in);
			break;
		case R.id.iv_service_my_shoe:
			startActivity(new Intent(mContext, MainShoesBoxActivity.class));
			break;
		case R.id.iv_service_helper:
			startActivity(new Intent(mContext, AssistMainActivity.class));
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
