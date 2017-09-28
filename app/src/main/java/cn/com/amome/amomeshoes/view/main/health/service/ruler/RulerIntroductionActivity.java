package cn.com.amome.amomeshoes.view.main.health.service.ruler;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 量一量-介绍量脚板
 * 
 * @author css
 */
public class RulerIntroductionActivity extends Activity implements
		OnClickListener {

	@SuppressWarnings("unused")
	private TextView tv_title, tv_left;
	@SuppressWarnings("unused")
	private ImageView iv_back;
	@SuppressWarnings("unused")
	private String leftValue, btnValue;
	private Button bt_ruler_start;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ruler_introuction);
		mContext = this;
		leftValue = getIntent().getStringExtra("leftValue");
		btnValue = getIntent().getStringExtra("btnValue");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_back = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("如何使用量脚板");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		bt_ruler_start = (Button) findViewById(R.id.bt_ruler_start);
		bt_ruler_start.setText(btnValue);
		bt_ruler_start.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.bt_ruler_start:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.RulerIntroductionActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.RulerIntroductionActivity);
		MobclickAgent.onPause(mContext);
	}
}
