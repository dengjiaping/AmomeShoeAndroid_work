package cn.com.amome.amomeshoes.view.account;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.Util;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout.LayoutParams;

public class IntroductionActivity extends Activity {
	private static final String TAG = "IntroductionActivity";

	private List<Integer> lsImg = new ArrayList<Integer>();
	private List<RadioButton> lsRbt = new ArrayList<RadioButton>();
	private ViewPager viewPager;
	private RadioGroup rg;
	private boolean isScrolled = false;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_introduction);
		mContext = this;
		SpfUtil.keepIsFirstRun(mContext, false);
		initView();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		rg = (RadioGroup) findViewById(R.id.introduct_rg);
		// 将展示图片的id放入集合中
		lsImg.add(R.drawable.guide_1);//
		lsImg.add(R.drawable.guide_2);
		lsImg.add(R.drawable.guide_3);
		lsImg.add(R.drawable.guide_4);
		// 加载RadioButton
		initRadiobutton();

		IntroductionAdapter adapter = new IntroductionAdapter(this, lsImg);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				lsRbt.get(position).setChecked(true);
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					isScrolled = false;
					break;
				case ViewPager.SCROLL_STATE_SETTLING:
					isScrolled = true;
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					if (viewPager.getCurrentItem() == viewPager.getAdapter()
							.getCount() - 1 && !isScrolled) {
						startActivity(new Intent(IntroductionActivity.this,
								LoginActivity.class));
						finish();
					}
					isScrolled = true;
					break;
				}
			}
		});
	}

	/**
	 * 加载radiobutton
	 */
	private void initRadiobutton() {
		for (int i = 0; i < lsImg.size(); i++) {
			RadioButton rbt = new RadioButton(this);
			rbt.setButtonDrawable(R.drawable.introduct_rbt_bg);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					Util.dip2px(this, 12), LayoutParams.WRAP_CONTENT);
			rbt.setLayoutParams(params);
			rbt.setEnabled(false);
			rg.addView(rbt);
			lsRbt.add(rbt);
		}
		lsRbt.get(0).setChecked(true);
	}

	class IntroductionAdapter extends PagerAdapter {
		private Context context;
		private List<Integer> ls;

		public IntroductionAdapter(Context context, List<Integer> ls) {
			this.context = context;
			this.ls = ls;
		}

		@Override
		public int getCount() {
			return ls.size();
		}

		@Override
		public int getItemPosition(Object position) {
			return super.getItemPosition(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return super.getPageTitle(position);
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(context,
					R.layout.item_viewpager_introduction, null);
			ImageView iv = (ImageView) view.findViewById(R.id.introduction_iv);

			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			InputStream is = getResources().openRawResource(ls.get(position));
			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
			iv.setBackgroundDrawable(bd);
			((ViewPager) container).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View convertView, Object arg1) {
			return (convertView == arg1);
		}
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.IntroductionActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.IntroductionActivity);
		MobclickAgent.onPause(mContext);
	}
}
