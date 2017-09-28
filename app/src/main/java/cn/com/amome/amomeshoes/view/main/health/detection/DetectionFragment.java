package cn.com.amome.amomeshoes.view.main.health.detection;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.bind.PrepareScanActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.shake.ShakeDetectionActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.squat.SquatDetectionActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.stand.StandDetectionActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.walk.WalkDetectionActivity;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
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
 * @Title:DetectionFragment.java
 * @Description:智能检测界面
 * @author:jyw
 */
public class DetectionFragment extends Fragment implements OnClickListener {
	private String TAG = "DetectionFragment";
	private Context mContext;
	private ImageView ll_detection_stand, ll_detection_squat,
			ll_detection_walk, ll_detection_shake;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detection_main, null,
				false);
		mContext = getActivity();
		initView(view);
		return view;
	}

	private void initView(View view) {
		ll_detection_stand = (ImageView) view
				.findViewById(R.id.ll_detection_stand);
		ll_detection_squat = (ImageView) view
				.findViewById(R.id.ll_detection_squat);
		ll_detection_shake = (ImageView) view
				.findViewById(R.id.ll_detection_shake);
		ll_detection_walk = (ImageView) view
				.findViewById(R.id.ll_detection_walk);
		LayoutParams lp_ll_detection_stand = (LayoutParams) ll_detection_stand
				.getLayoutParams();
		lp_ll_detection_stand.width = (int) (SpfUtil
				.readScreenWidthPx(mContext) * 11 / 12);
		lp_ll_detection_stand.height = lp_ll_detection_stand.width / 4;

		ll_detection_stand.setLayoutParams(lp_ll_detection_stand);
		ll_detection_squat.setLayoutParams(lp_ll_detection_stand);
		ll_detection_shake.setLayoutParams(lp_ll_detection_stand);
		ll_detection_walk.setLayoutParams(lp_ll_detection_stand);

		view.findViewById(R.id.ll_detection_stand).setOnClickListener(this);
		view.findViewById(R.id.ll_detection_squat).setOnClickListener(this);
		view.findViewById(R.id.ll_detection_shake).setOnClickListener(this);
		view.findViewById(R.id.ll_detection_walk).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_detection_stand:// 站一站
			if (enableBluetooth()) {
				if (SpfUtil.readDeviceToShoeLeft(mContext) == null
						|| SpfUtil.readDeviceToShoeRight(mContext) == null
						|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
					T.showToast(getActivity(), "请先绑定智能鞋", 0);
					startActivity(new Intent(mContext,
							PrepareScanActivity.class));
				} else {
					if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTING) {
						T.showToast(mContext, "智能鞋连接中，请稍候使用", 0);
					} else {
						startActivity(new Intent(mContext,
								StandDetectionActivity.class));
					}
				}
			} else {
				// T.showToast(mContext, "请开启蓝牙", 0);
			}
			break;
		case R.id.ll_detection_squat:
			if (enableBluetooth()) {
				if (SpfUtil.readDeviceToShoeLeft(mContext) == null
						|| SpfUtil.readDeviceToShoeRight(mContext) == null
						|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
					T.showToast(getActivity(), "请先绑定智能鞋", 0);
					startActivity(new Intent(mContext,
							PrepareScanActivity.class));
				} else {
					if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTING) {
						T.showToast(mContext, "智能鞋连接中，请稍候使用", 0);
					} else {
						startActivity(new Intent(mContext,
								SquatDetectionActivity.class));
					}
				}
			} else {
				// T.showToast(mContext, "请开启蓝牙", 0);
			}
			break;
		case R.id.ll_detection_shake:
			if (enableBluetooth()) {
				if (SpfUtil.readDeviceToShoeLeft(mContext) == null
						|| SpfUtil.readDeviceToShoeRight(mContext) == null
						|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext) == ""
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")
						|| SpfUtil.readDeviceToShoeRight(mContext).equals("")) {
					T.showToast(getActivity(), "请先绑定智能鞋", 0);
					startActivity(new Intent(mContext,
							PrepareScanActivity.class));
				} else {
					if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTING) {
						T.showToast(mContext, "智能鞋连接中，请稍候使用", 0);
					} else {
						startActivity(new Intent(mContext,
								ShakeDetectionActivity.class));
					}
				}
			} else {
				// T.showToast(mContext, "请开启蓝牙", 0);
			}
			break;
		case R.id.ll_detection_walk:
			int systemVersion = android.os.Build.VERSION.SDK_INT;
			if (systemVersion < 21) {
				T.showToast(mContext, "Android5.0以下系统版本无法使用此功能", 0);
			} else {
				if (enableBluetooth()) {
					if (SpfUtil.readDeviceToShoeLeft(mContext) == null
							|| SpfUtil.readDeviceToShoeRight(mContext) == null
							|| SpfUtil.readDeviceToShoeLeft(mContext) == ""
							|| SpfUtil.readDeviceToShoeRight(mContext) == ""
							|| SpfUtil.readDeviceToShoeRight(mContext).equals(
									"")
							|| SpfUtil.readDeviceToShoeRight(mContext).equals(
									"")) {
						T.showToast(getActivity(), "请先绑定智能鞋", 0);
						startActivity(new Intent(mContext,
								PrepareScanActivity.class));
					} else {
						if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTING) {
							T.showToast(mContext, "智能鞋连接中，请稍候使用", 0);
						} else {
							startActivity(new Intent(mContext,
									WalkDetectionActivity.class));
						}
					}
				} else {
					// T.showToast(mContext, "请开启蓝牙", 0);
				}
			}

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
		MobclickAgent.onPageEnd(ClassType.ServiceFragment);
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
}
