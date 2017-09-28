/**
 * @Title:MainFragmentActivity.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-2 下午4:48:04
 */
package cn.com.amome.amomeshoes.view.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.common.BaseFragmentActivity;
import cn.com.amome.amomeshoes.events.ExitEvent;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.UserInfo;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.activity.ActivityFragment;
import cn.com.amome.amomeshoes.view.main.exercise.ExerciseFragment;
import cn.com.amome.amomeshoes.view.main.health.HealthFragment;
import cn.com.amome.amomeshoes.view.main.health.detection.squat.SquatDetectionActivity;
import cn.com.amome.amomeshoes.view.main.my.MyFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class MainFragmentActivity extends BaseFragmentActivity implements View.OnClickListener {
	private String TAG = "MainFragmentActivity";
	private Context mContext;
	private LinearLayout ll_tab;
	private RadioButton tab1Rb, tab2Rb, tab3Rb, tab4Rb;
	@SuppressWarnings("unused")
	private RadioButton beforeRb;
	@SuppressWarnings("unused")
	private RelativeLayout rlLayout;
	private HealthFragment serviceFragment;
	private ExerciseFragment exerciseFragment;
	private ActivityFragment activityFragment;
	private MyFragment myFragment;
	private List<Fragment> lsFragment = new ArrayList<Fragment>();
	private FragmentManager fm;
	private FragmentTransaction fragtran;
	@SuppressWarnings("unused")
	private boolean isFirst = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Log.i(TAG, "======onCreate3=======");
		mContext = this;
		EventBus.getDefault().register(mContext);
		// getInfo();
	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		Log.i(TAG, "======setContentView1=======");
		setContentView(R.layout.fragment_ui_main);
		fm = getSupportFragmentManager();

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		Log.i(TAG, "======initView2=======");
		tab1Rb = (RadioButton) findViewById(R.id.tab1);
		tab2Rb = (RadioButton) findViewById(R.id.tab2);
		tab3Rb = (RadioButton) findViewById(R.id.tab3);
		tab4Rb = (RadioButton) findViewById(R.id.tab4);
		tab1Rb.setOnClickListener(this);
		tab2Rb.setOnClickListener(this);
		tab3Rb.setOnClickListener(this);
		tab4Rb.setOnClickListener(this);
		rlLayout = (RelativeLayout) findViewById(R.id.fragment_layout);
		ll_tab = (LinearLayout) findViewById(R.id.ll_tab);

		changeFragment(1);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab1:
			changeFragment(1);
			break;
		case R.id.tab2:
			if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTING) {
				T.showToast(mContext, "智能鞋连接中，请稍候使用", 0);
			} else {
				changeFragment(2);
			}
			break;
		case R.id.tab3:
			changeFragment(3);
			break;
		case R.id.tab4:
			changeFragment(4);
			break;
		default:
			break;
		}
	}

	private void changeFragment(int which) {
		fragtran = fm.beginTransaction();
		switch (which) {
		case 1:
			if (null == serviceFragment) {
				serviceFragment = new HealthFragment();
				fragtran.add(R.id.fragment_layout, serviceFragment);
				lsFragment.add(serviceFragment);
			}
			show(fragtran, serviceFragment);
			fragtran.commit();
			break;
		case 2:
			if (null == exerciseFragment) {
				exerciseFragment = new ExerciseFragment();
				fragtran.add(R.id.fragment_layout, exerciseFragment);
				lsFragment.add(exerciseFragment);
			}
			show(fragtran, exerciseFragment);
			fragtran.commit();
			break;
		case 3:
			if (null == activityFragment) {
				activityFragment = new ActivityFragment();
				fragtran.add(R.id.fragment_layout, activityFragment);
				lsFragment.add(activityFragment);
			}
			show(fragtran, activityFragment);
			fragtran.commit();
			break;
		case 4:
			if (null == myFragment) {
				myFragment = new MyFragment();
				fragtran.add(R.id.fragment_layout, myFragment);
				lsFragment.add(myFragment);
			}
			show(fragtran, myFragment);
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
	protected void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
	}

	/**
	 * 退出时间
	 */
	private long mExitTime;
	/**
	 * 退出间隔
	 */
	private static final int INTERVAL = 2000;

	/**
	 * 退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > INTERVAL) {
				T.showToast(mContext, "再次点击，退出应用", 0);
				mExitTime = System.currentTimeMillis();
			} else {
				MobclickAgent.onKillProcess(mContext);
				System.exit(0);
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 接收帐号管理退出消息
	public void onEventMainThread(ExitEvent event) {
		Log.i(TAG, TAG + "收到了消息" + event.getMsg());
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "=====onDestroy=====");
		EventBus.getDefault().unregister(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "=====onPause=====");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "=====onResume=====");
	}
}
