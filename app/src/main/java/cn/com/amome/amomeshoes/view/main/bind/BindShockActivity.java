package cn.com.amome.amomeshoes.view.main.bind;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.R.menu;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.util.BleConstants;
import cn.com.amome.amomeshoes.util.BleShoes;
import cn.com.amome.amomeshoes.util.BleShoesState;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.BleShoes.shoesCreCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesGetBatteryInfoCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesClickCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesDisconnectCallback;
import cn.com.amome.amomeshoes.util.BleShoes.shoesSyncTimeCallback;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.view.main.health.detection.ReconnectionActivity;
import cn.com.amome.shoeservice.BleService;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BindShockActivity extends Activity implements OnClickListener {
	private String TAG = "BindShockActivity";
	private Context mContext;
	private RelativeLayout rl_bind_finish, rl_bind_shock,
			rl_bind_left_click_tip, rl_bind_left_clicking,
			rl_bind_right_click_tip, rl_bind_right_clicking,
			rl_shock_left_fail, rl_shock_right_fail, rl_shock_suc;
	private TextView tv_title, tv_step_one, tv_step_two, tv_left_not_clicked,
			tv_right_not_clicked;
	private ImageView iv_bind_right_clicking, iv_bind_left_clicking,
			iv_step_one, iv_step_two, iv_bind_result_logo_left_fail,
			iv_bind_result_logo_right_fail, iv_bind_result_logo_suc;
	private int count = 0; // 用来统计触摸次数，只有为0时触摸才会生效
	private int bleFlag = BleConstants.MSG_CONNECT;
	private ObjectAnimator animator;
	private Timer timer;
	private TimerTask task;
	private int leftShockCount = 0, rightShockCount = 0;
	private boolean disBleCon = false; // 判断是否退出页面断开连接
	private String bindError = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_shock);
		mContext = this;
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_step_one = (TextView) findViewById(R.id.tv_step_one);
		tv_step_two = (TextView) findViewById(R.id.tv_step_two);
		iv_step_one = (ImageView) findViewById(R.id.iv_step_one);
		iv_step_two = (ImageView) findViewById(R.id.iv_step_two);
		iv_step_one.setImageResource(R.drawable.bind_step_complete);
		iv_step_two.setImageResource(R.drawable.bind_step_now);
		tv_step_one
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_step_two
				.setTextColor(mContext.getResources().getColor(R.color.blue));
		tv_title.setText("震动测试");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		rl_bind_finish = (RelativeLayout) findViewById(R.id.rl_bind_finish);
		rl_bind_shock = (RelativeLayout) findViewById(R.id.rl_bind_shock);
		rl_bind_left_click_tip = (RelativeLayout) findViewById(R.id.rl_bind_left_click_tip);
		rl_bind_left_clicking = (RelativeLayout) findViewById(R.id.rl_bind_left_clicking);
		rl_bind_right_click_tip = (RelativeLayout) findViewById(R.id.rl_bind_right_click_tip);
		rl_bind_right_clicking = (RelativeLayout) findViewById(R.id.rl_bind_right_clicking);
		iv_bind_left_clicking = (ImageView) findViewById(R.id.iv_bind_left_clicking);
		iv_bind_right_clicking = (ImageView) findViewById(R.id.iv_bind_right_clicking);
		tv_left_not_clicked = (TextView) findViewById(R.id.tv_left_not_clicked);
		tv_right_not_clicked = (TextView) findViewById(R.id.tv_right_not_clicked);
		rl_shock_left_fail = (RelativeLayout) findViewById(R.id.rl_shock_left_fail);
		rl_shock_right_fail = (RelativeLayout) findViewById(R.id.rl_shock_right_fail);
		rl_shock_suc = (RelativeLayout) findViewById(R.id.rl_shock_suc);
		iv_bind_result_logo_left_fail = (ImageView) findViewById(R.id.iv_bind_result_logo_left_fail);
		iv_bind_result_logo_right_fail = (ImageView) findViewById(R.id.iv_bind_result_logo_right_fail);
		iv_bind_result_logo_suc = (ImageView) findViewById(R.id.iv_bind_result_logo_suc);
		rl_shock_suc = (RelativeLayout) findViewById(R.id.rl_shock_suc);
		findViewById(R.id.iv_bind_next_step).setOnClickListener(this);
		findViewById(R.id.iv_bind_left_click_tip).setOnClickListener(this);
		findViewById(R.id.tv_left_clicked).setOnClickListener(this);
		findViewById(R.id.tv_left_not_clicked).setOnClickListener(this);
		findViewById(R.id.iv_bind_right_click_tip).setOnClickListener(this);
		findViewById(R.id.tv_right_clicked).setOnClickListener(this);
		findViewById(R.id.tv_right_not_clicked).setOnClickListener(this);
		findViewById(R.id.iv_left_next_step).setOnClickListener(this);
		findViewById(R.id.iv_right_next_step).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_bind_next_step:
			openBle();
			break;
		case R.id.iv_bind_left_click_tip:
			if (AmomeApp.bleShoes != null) {
				AmomeApp.bleShoes.clickShoe(shoesClickCallback,
						SpfUtil.readDeviceToShoeLeftTmp(mContext));
			} else {
				bleFlag = BleConstants.MSG_CLICK_LEFT;
				connectShoes();
			}
			leftShockCount++;
			if (leftShockCount == 3) {
				tv_left_not_clicked.setText("没感受到震动");
			}
			rl_bind_left_click_tip.setVisibility(View.GONE);
			rl_bind_left_clicking.setVisibility(View.VISIBLE);
			animator = tada(iv_bind_left_clicking);
			animator.setRepeatCount(ValueAnimator.INFINITE);
			animator.start();
			break;
		case R.id.tv_left_clicked:
			animator.cancel();
			rl_bind_left_clicking.setVisibility(View.GONE);
			rl_bind_right_click_tip.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_left_not_clicked:
			if (leftShockCount < 3) {
				rl_bind_left_clicking.setVisibility(View.GONE);
				rl_bind_left_click_tip.setVisibility(View.VISIBLE);
			} else {
				animator.cancel();
				bindError = "左脚震动失败,";
				rl_bind_left_clicking.setVisibility(View.GONE);
				rl_shock_left_fail.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_bind_right_click_tip:
			if (AmomeApp.bleShoes != null) {
				AmomeApp.bleShoes.clickShoe(shoesClickCallback,
						SpfUtil.readDeviceToShoeRightTmp(mContext));
			} else {
				bleFlag = BleConstants.MSG_CLICK_RIGHT;
				connectShoes();
			}
			rightShockCount++;
			if (rightShockCount == 3) {
				tv_right_not_clicked.setText("没感受到震动");
			}
			rl_bind_right_click_tip.setVisibility(View.GONE);
			rl_bind_right_clicking.setVisibility(View.VISIBLE);
			animator = tada(iv_bind_right_clicking);
			animator.setRepeatCount(ValueAnimator.INFINITE);
			animator.start();
			break;
		case R.id.tv_right_clicked:
			animator.cancel();
			rl_bind_right_clicking.setVisibility(View.GONE);
			rl_shock_suc.setVisibility(View.VISIBLE);
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Intent intent = new Intent(mContext,
							BindStressActivity.class);
					intent.putExtra("bindError", bindError);
					startActivity(intent);
					finish();
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, 2000);
			break;
		case R.id.tv_right_not_clicked:
			if (rightShockCount < 3) {
				rl_bind_right_clicking.setVisibility(View.GONE);
				rl_bind_right_click_tip.setVisibility(View.VISIBLE);
			} else {
				animator.cancel();
				bindError = bindError + "右脚震动失败,";
				rl_bind_right_clicking.setVisibility(View.GONE);
				rl_shock_right_fail.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_left_next_step:
			rl_shock_left_fail.setVisibility(View.GONE);
			rl_bind_right_click_tip.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_right_next_step:
			rl_shock_right_fail.setVisibility(View.GONE);
			Intent intent = new Intent(mContext, BindStressActivity.class);
			intent.putExtra("bindError", bindError);
			startActivity(intent);
			finish();
			break;
		}
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
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Log.i(TAG, "再次判断是否开启蓝牙");
					if (enableBluetooth()) {
						Log.i(TAG, "刚打开蓝牙的情况");
						preConnect();
					} else {
						// T.showToast(mContext, "蓝牙连接错误，请退出程序重新进入", 0);
						// 存在问题屏蔽
					}
				}
			};
			Timer timer = new Timer();
			timer.schedule(task, 5000);
		}
	}

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

	private void preConnect() {
		DialogUtil.showCancelProgressDialog(mContext, "", "正在连接智能鞋...", true,
				true);
		try {
			if (AmomeApp.bleShoesState == BleShoesState.MSG_CONNECTED) {
				Log.i(TAG, "智能鞋状态为已连接，需要线断开连接");
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
			} else {
				Log.i(TAG, "智能鞋状态为未连接");
				bleFlag = BleConstants.MSG_GETBATTERY;
				connectShoes();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			DialogUtil.hideProgressDialog();
			T.showToast(mContext, "未找到智能鞋", 0);
		}
	}

	private void connectShoes() {
		if (AmomeApp.bleShoes == null) {
			AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTING;
			AmomeApp.bleShoes = new BleShoes(
					SpfUtil.readDeviceToShoeLeftTmp(mContext),
					SpfUtil.readDeviceToShoeRightTmp(mContext),
					shoesCreCallback, mContext);
		} else {
			Log.i(TAG, "智能鞋已经连接了");
		}
	}

	BleShoes.shoesDisconnectCallback shoesDisconnectCallback = new shoesDisconnectCallback() {

		@Override
		public void isDisconnectSucc(boolean arg0) {
			// TODO Auto-generated method stub
			DialogUtil.hideProgressDialog();
			if (arg0) {
				Log.i(TAG, "智能鞋断开连接成功");
				AmomeApp.exercise_flag = false;
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				BleService.mSleep(2000);
				preConnect();
			} else {
				Log.i(TAG, "智能鞋断开连接失败");
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
				preConnect();
			}
		}
	};

	BleShoes.shoesCreCallback shoesCreCallback = new shoesCreCallback() {

		@Override
		public void isCreSucc(boolean arg0) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, "智能鞋连接完成");
				AmomeApp.bleShoesState = BleShoesState.MSG_CONNECTED;
				T.showToast(mContext, "智能鞋连接完成", 0);
				switch (bleFlag) {
				case BleConstants.MSG_GETBATTERY:
					AmomeApp.bleShoes
							.getShoesBattery(shoesGetBatteryInfoCallback);
					break;
				case BleConstants.MSG_CLICK_LEFT:
					AmomeApp.bleShoes.clickShoe(shoesClickCallback,
							SpfUtil.readDeviceToShoeLeftTmp(mContext));
					break;
				case BleConstants.MSG_CLICK_RIGHT:
					AmomeApp.bleShoes.clickShoe(shoesClickCallback,
							SpfUtil.readDeviceToShoeRightTmp(mContext));
					break;
				default:
					break;
				}
			} else {
				Log.i(TAG, "智能鞋连接出错");
				// 需要增加弹出重连提示框
				AmomeApp.bleShoes = null;
				AmomeApp.bleShoesState = BleShoesState.MSG_NOT_CONNECT;
			}
		}

		@Override
		public void isConnect(boolean arg0, String addr) {
			// TODO Auto-generated method stub

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
					T.showToast(mContext, "电量不足请充电", 0);
					DialogUtil.showAlertDialog(mContext, "", "电量不足，是否继续使用？",
							"确定", "取消", new OnAlertViewClickListener() {
								@Override
								public void confirm() {
									if (count == 0) {
										count++;
										rl_bind_finish.setVisibility(View.GONE);
										rl_bind_shock
												.setVisibility(View.VISIBLE);
									}
								}

								@Override
								public void cancel() {
									disBleCon = true;
									finish();
								}
							});
				} else {
					if (count == 0) {
						count++;
						rl_bind_finish.setVisibility(View.GONE);
						rl_bind_shock.setVisibility(View.VISIBLE);
					}
				}
			}
		}

	};

	BleShoes.shoesClickCallback shoesClickCallback = new shoesClickCallback() {

		@Override
		public void isClickSucc(boolean arg0, String addr) {
			// TODO Auto-generated method stub
			if (arg0) {
				Log.i(TAG, addr + "震动完成");
			} else {
				Log.i(TAG, addr + "震动失败");
			}
		}
	};

	public static ObjectAnimator tada(View view) {
		return tada(view, 1f);
	}

	public static ObjectAnimator tada(View view, float shakeFactor) {

		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
				View.SCALE_X, Keyframe.ofFloat(0f, 1f),
				Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
				Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
				Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
				Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
				Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
				View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
				Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
				Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
				Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
				Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
				Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

		PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
				View.ROTATION, Keyframe.ofFloat(0f, 0f),
				Keyframe.ofFloat(.1f, -3f * shakeFactor),
				Keyframe.ofFloat(.2f, -3f * shakeFactor),
				Keyframe.ofFloat(.3f, 3f * shakeFactor),
				Keyframe.ofFloat(.4f, -3f * shakeFactor),
				Keyframe.ofFloat(.5f, 3f * shakeFactor),
				Keyframe.ofFloat(.6f, -3f * shakeFactor),
				Keyframe.ofFloat(.7f, 3f * shakeFactor),
				Keyframe.ofFloat(.8f, -3f * shakeFactor),
				Keyframe.ofFloat(.9f, 3f * shakeFactor),
				Keyframe.ofFloat(1f, 0));

		return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
				pvhScaleY, pvhRotate).setDuration(1000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		if (disBleCon) {
			try {
				AmomeApp.bleShoes.disShoesConnect(shoesDisconnectCallback);
			} catch (Exception e) {
				// TODO: handle exception
				Log.i(TAG, "断开连接时崩了，大概是还没建立连接");
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		DialogUtil.showAlertDialog(mContext, "", "正在绑定中，是否退出？", "确定", "取消",
				new OnAlertViewClickListener() {
					@Override
					public void confirm() {
						disBleCon = true;
						finish();
					}

					@Override
					public void cancel() {
					}
				});
	}
}
