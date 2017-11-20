package cn.com.amome.amomeshoes.view.main.health.promotion.intelligent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import cn.com.amome.amomeshoes.R;

public class PrimaryBalanceActivity extends Activity {
    private static final String TAG = "PrimaryBalanceActivity";
    private Context mContext;
    private String disease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_balance);
        mContext = this;
    }
}
