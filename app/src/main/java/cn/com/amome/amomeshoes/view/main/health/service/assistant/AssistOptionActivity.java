package cn.com.amome.amomeshoes.view.main.health.service.assistant;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.R.menu;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.view.main.health.service.look.LookMainActivity;
import cn.com.amome.amomeshoes.view.main.health.service.ruler.RulerFootRightActivity;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.charts.ShoeChartMainFragmentActivity;
import cn.com.amome.amomeshoes.view.main.my.secret.FootSecretActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AssistOptionActivity extends Activity implements OnClickListener {
	private String TAG = "AssistOptionActivity";
	private Context mContext;
	private String nickname = "";
	private TextView tv_title;
	private ImageView iv_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		setContentView(R.layout.activity_assist_option);
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		findViewById(R.id.rl_left).setOnClickListener(this);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		tv_title.setText(nickname.substring(1));
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		iv_right.setImageResource(R.drawable.assist_report);
		iv_right.setVisibility(View.VISIBLE);
		iv_right.setOnClickListener(this);
		findViewById(R.id.ll_service_look_shoe).setOnClickListener(this);
		findViewById(R.id.ll_service_measure_shoe).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.iv_right:
			Intent intent = new Intent(mContext, FootSecretActivity.class);
			intent.putExtra("nickname", nickname);
			startActivity(intent);
			break;
		case R.id.ll_service_look_shoe:// 看一看
			Intent intent1 = new Intent(mContext, LookMainActivity.class);
			intent1.putExtra("nickname", nickname);
			startActivity(intent1);
			break;
		case R.id.ll_service_measure_shoe:// 量一量
			MobclickAgent.onEvent(mContext, ClassType.rulerfoot_btn);
			Intent in = new Intent(mContext, RulerFootRightActivity.class);
			in.putExtra("leftValue", "服务");
			in.putExtra("btnValue", "开始测量");
			in.putExtra("nickname", nickname);
			startActivity(in);
			break;
		}
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
