package cn.com.amome.amomeshoes.view.main.health;

import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.ScreenUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.detection.DetectionFragment;
import cn.com.amome.amomeshoes.view.main.health.promotion.PromotionFragment;
import cn.com.amome.amomeshoes.view.main.health.report.ReportFragment;
import cn.com.amome.amomeshoes.view.main.health.service.ServiceFragment;
import cn.com.amome.amomeshoes.view.main.my.setting.JDWebviewActivity;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * @Title:HealthFragment.java
 * @Description:健康界面
 * @author:jyw
 */
public class HealthFragment extends Fragment implements OnClickListener {
	private String TAG = "ServiceFragment";

	private Context mContext;
	private ServiceFragment servicetmpFragment;
	private DetectionFragment serviceFragment;
	private ReportFragment reportFragment;
	private PromotionFragment promotionFragment;
	private FragmentTransaction fragtran;
	private FragmentManager fm;
	private List<Fragment> lsFragment = new ArrayList<Fragment>();
	private LinearLayout ll_health_top;
	private ImageView iv_health_detection, iv_health_service, iv_health_report,
			iv_health_promotion;
	private ImageView iv_market;
	private ImageView iv_scroll;
	private float screenWidthPx, screenHeightPx, screenWidthDp, screenHeightDp,
			screenDensity;
	TranslateAnimation ta;
	private float fromX, toX;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		screenWidthPx = SpfUtil.readScreenWidthPx(mContext);
		screenHeightPx = SpfUtil.readScreenHeightPx(mContext);
		screenWidthDp = SpfUtil.readScreenWidthDp(mContext);
		screenHeightDp = SpfUtil.readScreenHeightDp(mContext);
		screenDensity = SpfUtil.readScreenDensity(mContext);
		View view = inflater
				.inflate(R.layout.fragment_health_main, null, false);
		initView(view);
		fm = getFragmentManager();
		changeFragment(1);
		// 初始滚动的位置
		toX = 10 * screenDensity;
		ta = new TranslateAnimation(fromX, toX, 0, 0);
		ta.setDuration(0);
		ta.setFillAfter(true);
		fromX = toX;
		iv_scroll.startAnimation(ta);

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		ll_health_top = (LinearLayout) view.findViewById(R.id.ll_health_top);
		iv_health_service = (ImageView) view
				.findViewById(R.id.iv_health_service);
		iv_health_detection = (ImageView) view
				.findViewById(R.id.iv_health_detection);
		iv_health_report = (ImageView) view.findViewById(R.id.iv_health_report);
		iv_health_promotion = (ImageView) view
				.findViewById(R.id.iv_health_promotion);
		iv_market = (ImageView) view.findViewById(R.id.iv_market);
		iv_market.setOnClickListener(this);
		iv_health_service.setOnClickListener(this);
		iv_health_detection.setOnClickListener(this);
		iv_health_report.setOnClickListener(this);
		iv_health_promotion.setOnClickListener(this);
		iv_scroll = (ImageView) view.findViewById(R.id.iv_scroll);
		LayoutParams lp_iv_scroll = (LayoutParams) iv_scroll.getLayoutParams();
		lp_iv_scroll.width = (int) (screenWidthPx / 4 - 10 * screenDensity * 2);
		iv_scroll.setLayoutParams(lp_iv_scroll);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_health_service:
			iv_health_service
					.setImageResource(R.drawable.health_service_selected);
			iv_health_detection.setImageResource(R.drawable.health_detection);
			iv_health_report.setImageResource(R.drawable.health_report);
			iv_health_promotion.setImageResource(R.drawable.health_promotion);
			toX = 10 * screenDensity;
			ta = new TranslateAnimation(fromX, toX, 0, 0);
			ta.setDuration(100);
			ta.setFillAfter(true);
			fromX = toX;
			iv_scroll.startAnimation(ta);
			changeFragment(1);
			break;
		case R.id.iv_health_detection:
			iv_health_service.setImageResource(R.drawable.health_service);
			iv_health_detection
					.setImageResource(R.drawable.health_detection_selected);
			iv_health_report.setImageResource(R.drawable.health_report);
			iv_health_promotion.setImageResource(R.drawable.health_promotion);
			toX = 10 * screenDensity * 3 + iv_scroll.getWidth() * 1;
			ta = new TranslateAnimation(fromX, toX, 0, 0);
			ta.setDuration(100);
			ta.setFillAfter(true);
			fromX = toX;
			iv_scroll.startAnimation(ta);
			changeFragment(2);
			break;
		case R.id.iv_health_report:
			iv_health_service.setImageResource(R.drawable.health_service);
			iv_health_detection.setImageResource(R.drawable.health_detection);
			iv_health_report
					.setImageResource(R.drawable.health_report_selected);
			iv_health_promotion.setImageResource(R.drawable.health_promotion);
			toX = 10 * screenDensity * 5 + iv_scroll.getWidth() * 2;
			ta = new TranslateAnimation(fromX, toX, 0, 0);
			ta.setDuration(100);
			ta.setFillAfter(true);
			fromX = toX;
			iv_scroll.startAnimation(ta);
			changeFragment(3);
			break;
		case R.id.iv_health_promotion:
			iv_health_service.setImageResource(R.drawable.health_service);
			iv_health_detection.setImageResource(R.drawable.health_detection);
			iv_health_report.setImageResource(R.drawable.health_report);
			iv_health_promotion
					.setImageResource(R.drawable.health_promotion_selected);
			toX = 10 * screenDensity * 7 + iv_scroll.getWidth() * 3;
			ta = new TranslateAnimation(fromX, toX, 0, 0);
			ta.setDuration(100);
			ta.setFillAfter(true);
			fromX = toX;
			iv_scroll.startAnimation(ta);
			changeFragment(4);
			break;
		case R.id.iv_market:
			startActivity(new Intent(mContext, JDWebviewActivity.class));
			break;
		default:
			break;
		}
	}

	private void changeFragment(int which) {
		fragtran = fm.beginTransaction();
		switch (which) {
		case 1:
			Log.i(TAG, "切换到自诊页面");
			if (null == servicetmpFragment) {
				servicetmpFragment = new ServiceFragment();
				fragtran.add(R.id.fragment_health, servicetmpFragment);
				lsFragment.add(servicetmpFragment);
			}
			show(fragtran, servicetmpFragment);
			fragtran.commit();
			break;
		case 2:
			Log.i(TAG, "切换到检测页面");
			if (null == serviceFragment) {
				serviceFragment = new DetectionFragment();
				fragtran.add(R.id.fragment_health, serviceFragment);
				lsFragment.add(serviceFragment);
			}
			show(fragtran, serviceFragment);
			fragtran.commit();
			break;
		case 3:
			Log.i(TAG, "切换到报告页面");
			if (null == reportFragment) {
				reportFragment = new ReportFragment();
				fragtran.add(R.id.fragment_health, reportFragment);
				lsFragment.add(reportFragment);
			}
			show(fragtran, reportFragment);
			fragtran.commit();
			break;
		case 4:
			Log.i(TAG, "切换到促进页面");
			if (null == promotionFragment) {
				promotionFragment = new PromotionFragment();
				fragtran.add(R.id.fragment_health, promotionFragment);
				lsFragment.add(promotionFragment);
			}
			show(fragtran, promotionFragment);
			fragtran.commit();
			break;
		default:
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
		Log.i(TAG, TAG + "===onHiddenChanged====");
	}

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// MobclickAgent.onPageStart(ClassType.ServiceFragment);
	// SpfUtil.keepFootType(mContext, "");
	// SpfUtil.keepFootShape(mContext, "");
	// SpfUtil.keepFtSize(mContext, "", Constants.RFTWIDTH);
	// SpfUtil.keepFtSize(mContext, "", Constants.RFTHEIGHT);
	// SpfUtil.keepFtSize(mContext, "", Constants.LFTHEIGHT);
	// SpfUtil.keepFtSize(mContext, "", Constants.LFTWIDTH);
	// }

}
