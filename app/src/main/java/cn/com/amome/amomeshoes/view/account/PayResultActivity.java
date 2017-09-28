package cn.com.amome.amomeshoes.view.account;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.view.main.MainFragmentActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PayResultActivity extends Activity implements OnClickListener {

	private static Context mContext;
	private TextView tv_title, tv_left, tv_right;
	private ImageView iv_left;
	private String payresult = "";
	private TextView tv_payresult;
	private ImageView iv_payresult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		mContext = this;
		payresult = getIntent().getStringExtra("payresult");
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_right = (TextView) findViewById(R.id.right_tv);
		tv_title.setText("支付结果");
		tv_right.setText("完成");
		tv_left.setVisibility(View.GONE);
		iv_left.setVisibility(View.GONE);
		tv_payresult = (TextView) findViewById(R.id.tv_payresult);
		iv_payresult = (ImageView) findViewById(R.id.iv_payresult);
		if (payresult.equals("0")) {
			tv_payresult.setText("支付成功");
			iv_payresult.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_paysuce));
		} else if (payresult.equals("1")) {
			tv_payresult.setText("支付失败");
			iv_payresult.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_payfail));
		}
		tv_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_tv:
			if (payresult.equals("0")) {
				Intent intent = new Intent(mContext, MainFragmentActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			} else if (payresult.equals("1")) {
				finish(); // 暂时屏蔽
				// Intent intent = new Intent(mContext, //暂时增加。用于支付失败时也可进入
				// MainFragmentActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				// | Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(intent);
				// finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.PayResultActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.PayResultActivity);
		MobclickAgent.onPause(mContext);
	}

}
