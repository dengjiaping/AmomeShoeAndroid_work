package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.DetailTrainingInfo;
import cn.com.amome.amomeshoes.util.DateUtils;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView.TrainingVideoView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;

public class DetailTrainingTrueActivity extends Activity implements TrainingVideoView.FinishListener, View.OnClickListener {
    private Context mContext;
    private String TAG = "DetailTrainingTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private String disease, type;
    private Gson gson = new Gson();
    private List<DetailTrainingInfo> mTrainingInfo;

    private TrainingVideoView trainingVideoView;
    private ImageView iv_back, iv_more, iv_detail, iv_middle, iv_left, iv_right;
    private TextView tv_name, tv_time;
    private ProgressBar progressBar;
    //private LinkedHashMap mMap;
    private List<String> nameList = new ArrayList();
    private List<String> urlList = new ArrayList();
    private int size = 0;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        type = getIntent().getStringExtra("type");
        initView();
        getTrainingData();
    }

    private void initView() {
        trainingVideoView = findViewById(R.id.trainingVideoView);
        iv_back = findViewById(R.id.iv_back);
        iv_more = findViewById(R.id.iv_more);
        iv_detail = findViewById(R.id.iv_detail);
        iv_middle = findViewById(R.id.iv_middle);
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);
        tv_name = findViewById(R.id.tv_name);
        tv_time = findViewById(R.id.tv_time);
        progressBar = findViewById(R.id.progressBar);

        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_middle.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }


    private void getTrainingData() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_PROMOTION_DETAIL);
        params.put("disease", disease);
        params.put("type", type);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_DATA, params,
                ClientConstant.PROMOTION_URL);
    }


    HttpService.ICallback callback = new HttpService.ICallback() {

        @Override
        public void onHttpPostSuccess(int type, int statusCode,
                                      Header[] headers, byte[] responseBody) {
            // TODO Auto-generated method stub
            String result;
            switch (type) {
                case MSG_GET_DATA:
                    result = new String(responseBody);
                    try {
                        JSONObject obj = new JSONObject(result);
                        String return_msg = obj.getString("return_msg");
                        int return_code = obj.getInt("return_code");
                        Message msg = Message.obtain();
                        if (return_code == 0
                                && HttpError.judgeError(return_msg,
                                ClassType.PayActivity)) {
                            msg.what = ClientConstant.HANDLER_SUCCESS;
                            msg.arg1 = type;
                            msg.obj = return_msg;
                        }
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "MSG_GET_DATA解析失败");

                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onHttpPostFailure(int type, int statusCode, Header[] arg1,
                                      byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            //DialogUtil.hideProgressDialog();
            Log.e(TAG, "DetailTrainingTrueActivity>onHttpPostFailure: 获取训练详细数据失败");

        }


    };


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_DATA:
                            String str = (String) msg.obj;
                            if (TextUtils.isEmpty(str)) {
                            } else {
                                Type type = new TypeToken<List<DetailTrainingInfo>>() {
                                }.getType();
                                mTrainingInfo = gson.fromJson(str, type);
                                initData();
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }


    };

    private void initData() {


        //先转换成本地的path,判断本地文件是否存在
        for (DetailTrainingInfo info :
                mTrainingInfo) {
            String down_url = info.getIcon();
            String filename = down_url.substring(down_url.lastIndexOf('/') + 1);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Amome/video/";
            File file = new File(path + filename);
            String name = info.getName();
            Log.e(TAG, "initData: " + path + filename);
            if (file.exists()) {
                nameList.add(name);
                urlList.add(path + filename);
            } else {
                nameList.add(name);
                urlList.add(down_url);
            }
        }

        //播放视频

        trainingVideoView.setUp(urlList.get(size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
        trainingVideoView.startVideo();
        trainingVideoView.setOnFinishListener(this);
        startTimerToSetTextAndProgress();
        tv_name.setText(nameList.get(size));
    }

    private void startTimerToSetTextAndProgress() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new ProgressTimerTask(), 0, 300);
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


    @Override
    public void setOnFinish() {
        if (size < (nameList.size() - 1)) {
            trainingVideoView.setUp(urlList.get(++size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
            trainingVideoView.startVideo();
            startTimerToSetTextAndProgress();
            tv_name.setText(nameList.get(size));
        } else {
            T.showToast(mContext, "播放完成", 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_more:
                break;
            case R.id.iv_detail:
                break;
            case R.id.iv_middle:
                if (JZMediaManager.instance().mediaPlayer.isPlaying()) {
                    JZMediaManager.instance().mediaPlayer.pause();
                } else {
                    JZMediaManager.instance().mediaPlayer.start();

                }
                break;
            case R.id.iv_left:
                break;
            case R.id.iv_right:
                break;
            default:
                break;
        }

    }


    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    long position = trainingVideoView.getCurrentPositionWhenPlaying();
                    long duration = trainingVideoView.getDuration();
                    int progress = (int) (position * 100 / (duration == 0 ? 1 : duration));
                    //setProgressAndText(progress, position, duration);
                    progressBar.setProgress(progress);
                    tv_time.setText(DateUtils.changeToS(position) + "/" + DateUtils.changeToS(duration) + "s");
                }
            });

        }
    }
}
