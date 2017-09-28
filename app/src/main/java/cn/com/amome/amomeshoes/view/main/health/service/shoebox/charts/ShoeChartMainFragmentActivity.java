package cn.com.amome.amomeshoes.view.main.health.service.shoebox.charts;

import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.MainViewPagerAdapter;
import cn.com.amome.amomeshoes.common.BaseFragmentActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoeChartMainFragmentActivity extends BaseFragmentActivity
		implements OnClickListener {
	private String TAG = "ShoeChartMainFragmentActivity";
	private Context mContext;
	private TextView tv_title, tv_left;
	private RelativeLayout fl_mian;
	private ViewPager viewPagerLayout;
	private ArrayList<Fragment> fragmentsList;
	private MainViewPagerAdapter adapter;
	private ShoeBrandFragment shoeBrandFragment;// 鞋品牌种类饼图
	private ShoeCategoryFragment shoeCategoryFragment;// 鞋类别饼图
	private ShoeProFragment shoeProFragment;// 鞋问题饼图
	private ShoePriceFragment shoePriceFragment;// 各个类别鞋子总价钱饼图
	private DelShoeCategoryFragment delShoeCategoryFragment;// 已删除鞋子种类饼图
	private DelShoeProFragment delShoeProFragment;// 已删除鞋子问题饼图

	@Override
	public void setContentView() {
		setContentView(R.layout.fragment_shoebox_chart);
		mContext = this;
	}

	@Override
	public void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("鞋子全明星");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		tv_left.setText("");
		tv_left.setVisibility(View.INVISIBLE);
		findViewById(R.id.rl_left).setOnClickListener(this);

		fl_mian = (RelativeLayout) findViewById(R.id.fl_mian);
		viewPagerLayout = (ViewPager) findViewById(R.id.viewpager_layout);

		shoeBrandFragment = new ShoeBrandFragment();
		shoeCategoryFragment = new ShoeCategoryFragment();
		shoeProFragment = new ShoeProFragment();
		shoePriceFragment = new ShoePriceFragment();
		delShoeCategoryFragment = new DelShoeCategoryFragment();
		delShoeProFragment = new DelShoeProFragment();

		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(shoeCategoryFragment);
		fragmentsList.add(shoeProFragment);
		fragmentsList.add(shoePriceFragment);
		fragmentsList.add(shoeBrandFragment);
		fragmentsList.add(delShoeCategoryFragment);
		fragmentsList.add(delShoeProFragment);
		adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
				fragmentsList);
		viewPagerLayout.setAdapter(adapter);
		viewPagerLayout.setCurrentItem(0);
		viewPagerLayout.setOnPageChangeListener(pageListener);
		viewPagerLayout.setFocusableInTouchMode(true);
		// viewPagerLayout.setOffscreenPageLimit(6);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void initData() {

	}

	public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				tv_title.setText("鞋子全明星");
				viewPagerLayout.setCurrentItem(0);
				break;
			case 1:
				tv_title.setText("奇葩聚集地");
				viewPagerLayout.setCurrentItem(1);
				break;
			case 2:
				tv_title.setText("身价排行榜");
				viewPagerLayout.setCurrentItem(2);
				break;
			case 3:
				tv_title.setText("人气排行榜");
				viewPagerLayout.setCurrentItem(3);
				break;
			case 4:
				tv_title.setText("过气排行榜");
				viewPagerLayout.setCurrentItem(4);
				break;
			case 5:
				tv_title.setText("过气盘点");
				viewPagerLayout.setCurrentItem(5);
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	public void finishActivity() {
		finish();// 在此处控制关闭
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}
}
