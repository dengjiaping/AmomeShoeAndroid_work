package cn.com.amome.amomeshoes.view.main.health.detection.walk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.util.SpfUtil;

public class WalkTeachingActivity extends Activity implements OnClickListener {
    private String TAG = "WalkTeachingActivity";
    private Context mContext;
    private LinearLayout ll_squat_teaching1;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_walk_teaching);
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
        tv_title.setText("走一走");
        tv_title.setTextColor(mContext.getResources().getColor(
                R.color.turkeygreen));

        ll_squat_teaching1 = (LinearLayout) findViewById(R.id.ll_squat_teaching1);
        LayoutParams lp_squat_teaching = (LayoutParams) ll_squat_teaching1
                .getLayoutParams();
        lp_squat_teaching.height = (int) (SpfUtil.readScreenWidthPx(mContext) / 2);
        lp_squat_teaching.width = (int) (lp_squat_teaching.height * 1.9);
        ll_squat_teaching1.setLayoutParams(lp_squat_teaching);

        findViewById(R.id.rl_left).setOnClickListener(this);
        findViewById(R.id.btn_start_squat).setOnClickListener(this);
    }
}
