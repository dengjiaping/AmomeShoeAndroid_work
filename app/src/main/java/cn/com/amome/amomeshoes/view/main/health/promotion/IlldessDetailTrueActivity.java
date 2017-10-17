package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;

import cn.com.amome.amomeshoes.R;

public class IlldessDetailTrueActivity extends Activity {
    private Context mContext;
    private static final String TAG = "IlldessDetailTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private String disease = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illdess_detail_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        initView();
        getIllnessDetailInfo();
    }
    private void initView() {
    }
    private void getIllnessDetailInfo() {
    }


}
