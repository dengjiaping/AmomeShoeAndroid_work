package cn.com.amome.amomeshoes.view.main.health.detection.squat;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.util.SpfUtil;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SquatTeachingActivity extends Activity implements OnClickListener {
	private String TAG = "SquatTeachingActivity";
	private Context mContext;
	private LinearLayout ll_squat_teaching1, ll_squat_teaching2,
			ll_squat_teaching3, ll_squat_teaching4, ll_squat_teaching5,
			ll_squat_teaching6;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_squat_teaching);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.btn_start_squat:
			AmomeApp.pauseFlag = false;
			setResult(RESULT_OK, new Intent());
			finish();
			break;
		}
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_title.setText("蹲一蹲");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));

		ll_squat_teaching1 = (LinearLayout) findViewById(R.id.ll_squat_teaching1);
		ll_squat_teaching2 = (LinearLayout) findViewById(R.id.ll_squat_teaching2);
		ll_squat_teaching3 = (LinearLayout) findViewById(R.id.ll_squat_teaching3);
		ll_squat_teaching4 = (LinearLayout) findViewById(R.id.ll_squat_teaching4);
		ll_squat_teaching5 = (LinearLayout) findViewById(R.id.ll_squat_teaching5);
		ll_squat_teaching6 = (LinearLayout) findViewById(R.id.ll_squat_teaching6);
		LayoutParams lp_squat_teaching = (LayoutParams) ll_squat_teaching1
				.getLayoutParams();
		lp_squat_teaching.height = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
		lp_squat_teaching.width = (int) (lp_squat_teaching.height * 1.9);
		ll_squat_teaching1.setLayoutParams(lp_squat_teaching);
		ll_squat_teaching2.setLayoutParams(lp_squat_teaching);
		ll_squat_teaching3.setLayoutParams(lp_squat_teaching);
		ll_squat_teaching4.setLayoutParams(lp_squat_teaching);
		ll_squat_teaching5.setLayoutParams(lp_squat_teaching);
		ll_squat_teaching6.setLayoutParams(lp_squat_teaching);

		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_start_squat).setOnClickListener(this);
	}
}
