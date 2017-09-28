package cn.com.amome.amomeshoes.common;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragmentActivity extends FragmentActivity {
	@SuppressWarnings("unused")
	private String TAG = "BaseFragmentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		MobclickAgent.setDebugMode(false);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);
		initView();
		initData();
	}

	public abstract void setContentView();

	public abstract void initView();

	public abstract void initData();

	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onResume(this);//统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}

	public void initBroadcast() {}

}
