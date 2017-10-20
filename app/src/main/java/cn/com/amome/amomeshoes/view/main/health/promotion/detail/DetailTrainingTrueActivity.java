package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView.TrainingVideoView;
import cn.jzvd.JZVideoPlayer;

public class DetailTrainingTrueActivity extends Activity {
    private Context mContext;
    private String TAG = "DetailTrainingTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private String disease;

    private TrainingVideoView trainingVideoView;
    private ImageView iv_back, iv_more, iv_detail, iv_middle, iv_left, iv_right;
    private TextView tv_name, tv_time;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        initView();

    }

    private void initView() {
        trainingVideoView=findViewById(R.id.trainingVideoView);
        iv_back=findViewById(R.id.iv_back);
        iv_more=findViewById(R.id.iv_more);
        iv_detail=findViewById(R.id.iv_detail);
        iv_middle=findViewById(R.id.iv_middle);
        iv_left=findViewById(R.id.iv_left);
        iv_right=findViewById(R.id.iv_right);
        tv_name = findViewById(R.id.tv_name);
        tv_time = findViewById(R.id.tv_time);
        progressBar = findViewById(R.id.progressBar);
    }







    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
