/**
 * @Title:ExerciseFragmentViewOne.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-9 上午10:52:39
 */
//第二个页面
package cn.com.amome.amomeshoes.view.main.exercise;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.events.ExerciseEvaluateEvent;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ExerciseEvaluateFragment extends Fragment {
	private String TAG = "ExerciseEvaluateFragment";
	private View view;
	private ImageView iv_exercise_2_disk;
	private RotateAnimation retateAnim;
	private float fromDegree = 0f;
	private RelativeLayout rl_sit_progress, rl_sta_progress, rl_go_progress;
	private View view_sit_progress, view_sta_progress, view_go_progress;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				float[] cal_evaluate = (float[]) msg.obj;
				Log.i(TAG, "case0" + "cal_evaluate[0]:" + cal_evaluate[0]
						+ "cal_evaluate[1]:" + cal_evaluate[1]
						+ "cal_evaluate[2]:" + cal_evaluate[2]);
				startRotateAnimationJavaCode(fromDegree, msg.arg1);
				fromDegree = msg.arg1;
				RelativeLayout.LayoutParams lp_view_sit_progress = (LayoutParams) view_sit_progress
						.getLayoutParams();
				int total_cal = (int) (cal_evaluate[0] + cal_evaluate[1] + cal_evaluate[2]);

				// 坐时间判断
				Log.i(TAG,
						"rl_sit_progress.getWidth()"
								+ rl_sit_progress.getWidth());
				lp_view_sit_progress.width = (int) (rl_sit_progress.getWidth()
						* cal_evaluate[0] / total_cal);
				Log.i(TAG, "view_sit_progress.width"
						+ lp_view_sit_progress.width);
				view_sit_progress.setLayoutParams(lp_view_sit_progress);

				// 站时间判断
				RelativeLayout.LayoutParams lp_view_sta_progress = (LayoutParams) view_sta_progress
						.getLayoutParams();
				Log.i(TAG,
						"rl_sta_progress.getWidth()"
								+ rl_sta_progress.getWidth());
				lp_view_sta_progress.width = (int) (rl_sta_progress.getWidth()
						* cal_evaluate[1] / total_cal);
				Log.i(TAG, "view_sta_progress.width"
						+ lp_view_sta_progress.width);
				view_sta_progress.setLayoutParams(lp_view_sta_progress);

				// 走时间判断
				RelativeLayout.LayoutParams lp_view_go_progress = (LayoutParams) view_go_progress
						.getLayoutParams();
				Log.i(TAG,
						"rl_go_progress.getWidth()" + rl_go_progress.getWidth());
				lp_view_go_progress.width = (int) (rl_go_progress.getWidth()
						* cal_evaluate[2] / total_cal);
				Log.i(TAG, "view_go_progress.width" + lp_view_go_progress.width);
				view_go_progress.setLayoutParams(lp_view_go_progress);
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		view = inflater.inflate(R.layout.fragment_exercise_evaluate, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		iv_exercise_2_disk = (ImageView) view
				.findViewById(R.id.iv_exercise_2_disk);
		rl_sit_progress = (RelativeLayout) view
				.findViewById(R.id.rl_sit_progress);
		rl_sta_progress = (RelativeLayout) view
				.findViewById(R.id.rl_sta_progress);
		rl_go_progress = (RelativeLayout) view
				.findViewById(R.id.rl_go_progress);
		view_sit_progress = view.findViewById(R.id.view_sit_progress);
		view_sta_progress = view.findViewById(R.id.view_sta_progress);
		view_go_progress = view.findViewById(R.id.view_go_progress);
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, TAG + "=====onDestroy=====");
		EventBus.getDefault().unregister(this);
	}

	private void startRotateAnimationJavaCode(float fromDegree, float toDegree) {
		// 旋转动画 参数分别是 起始的角度 转多少度 相对于自身的多少旋转
		retateAnim = new RotateAnimation(fromDegree, toDegree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		retateAnim.setDuration(0);
		retateAnim.setInterpolator(new LinearInterpolator());
		retateAnim.setFillAfter(true); // 旋转停止
		iv_exercise_2_disk.startAnimation(retateAnim);

		retateAnim.setAnimationListener(new AnimationListener() { // 设置动画监听事件
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						Log.i(TAG, "旋转开始");
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					// 图片旋转结束后触发事件
					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						Log.i(TAG, "旋转结束");
					}
				});
	}

	public void onEventMainThread(ExerciseEvaluateEvent event) {
		Log.i(TAG, TAG + "收到了消息");
		Message message = Message.obtain();
		message.arg1 = (int) event.getDegree();
		message.obj = event.getTime();
		message.what = 0;
		mHandler.sendMessage(message);
	}

}
