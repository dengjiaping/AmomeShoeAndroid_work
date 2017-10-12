package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.amome.amomeshoes.R;

public class DetailDescribeActivity extends Activity implements View.OnClickListener {
    private ImageView iv_left;
    private TextView tv_definition, tv_reason, tv_influence;
    private String definition;
    private String reason;
    private String influence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_describe);
        Intent intent = getIntent();
        definition = intent.getStringExtra("definition");
        reason = intent.getStringExtra("reason");
        influence = intent.getStringExtra("influence");
        initView();
        initData();
    }

    private void initData() {
        tv_definition.setText(definition);
        tv_reason.setText(reason);
        tv_influence.setText(influence);
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_definition = (TextView) findViewById(R.id.tv_definition);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        tv_influence = (TextView) findViewById(R.id.tv_influence);

        iv_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
