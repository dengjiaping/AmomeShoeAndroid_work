package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
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
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
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
import cn.com.amome.amomeshoes.view.main.health.promotion.finish.TrainingFinishActivity;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;

public class DetailTrainingTrueActivity extends Activity implements TrainingVideoView.FinishListener, View.OnClickListener {
    private Context mContext;
    private String TAG = "DetailTrainingTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private static final int UPLOAD = 1;
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
    private int size;
    private Timer mTimer;
    private String done_times;
    private int group;
    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        type = getIntent().getStringExtra("type");
        size = Integer.parseInt(getIntent().getStringExtra("newest_id"));
        done_times = getIntent().getStringExtra("done_times");
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

        trainingVideoView.setOnFinishListener(this);
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


    /**
     * 向服务器返回训练的数据
     */
    private void upLoadTraining() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.UPLOAD_NURSING);
        params.put("disease", disease);
        params.put("type", type);
        params.put("item_name", nameList.get(size));
        params.put("switcher", "yes");
        params.put("done_times", done_times);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, UPLOAD, params,
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
                case UPLOAD:
                    result = new String(responseBody);
                    try {

                        JSONObject obj = new JSONObject(result);
                        JSONArray return_msg = obj.getJSONArray("return_msg");
                        JSONObject msg = (JSONObject) return_msg.get(0);
                        int return_code = obj.getInt("return_code");
                        if (return_code == 0) {
                            if (msg.getString("retval").equals("0x00")) {
                                Log.i(TAG, "onHttpPostSuccess: 上传成功");
                            }
                        }
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "UPLOAD解析失败");


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
                        case UPLOAD:

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
            Log.i(TAG, "initData: " + path + filename);
            if (file.exists()) {
                nameList.add(name);
                urlList.add(path + filename);
            } else {
                nameList.add(name);
                urlList.add(down_url);
            }
        }

        if (size == nameList.size() - 1) {
            size = 0;
        }
        //播放视频

        goOnRedio();
        time = trainingVideoView.currentDuration;
    }

    private void goOnRedio() {
        trainingVideoView.setUp(urlList.get(size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
        trainingVideoView.startVideo();

        startTimerToSetTextAndProgress();
        tv_name.setText(nameList.get(size) + (size + 1) + "/" + nameList.size());
        Picasso.with(mContext).load(R.drawable.zanting)
                .fit()
                .into(iv_middle);
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
        //super.onBackPressed();

        //做成和左上角返回一样的效果
        JZMediaManager.instance().mediaPlayer.pause();
        Picasso.with(mContext).load(R.drawable.bofang)
                .fit()
                .into(iv_middle);
        new android.app.AlertDialog.Builder(this)
                .setMessage("训练仍在进行中，确定要结束当前训练吗？结束训练后将无法保存训练数据")
                .setPositiveButton("结束训练", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        time = time + trainingVideoView.currentDuration;
                        if (Integer.parseInt(getIntent().getStringExtra("newest_id")) > 0) {
                            Intent intent = new Intent(mContext, TrainingFinishActivity.class);
                            intent.putExtra("disease", disease);
                            intent.putExtra("type", type);
                            intent.putExtra("time", time);
                            intent.putExtra("group", group);
                            startActivity(intent);
                        }

                        finish();
                    }
                })
                .setNegativeButton("再练会儿", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JZMediaManager.instance().mediaPlayer.start();
                        Picasso.with(mContext).load(R.drawable.zanting)
                                .fit()
                                .into(iv_middle);
                    }
                })
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " + size + progressBar.getProgress());
        if (size == (nameList.size()) && progressBar.getProgress() == 0) {
            try {
                JZVideoPlayer.releaseAllVideos();
            } catch (RuntimeException e) {
                Log.e(TAG, "onPause: 这个地方偶尔会崩溃,估计原因是mediaplayer对象回收了");
            }
        } else {
            try {
                JZMediaManager.instance().mediaPlayer.pause();
            } catch (RuntimeException e) {
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        goOnRedio();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }



    @Override
    public void setOnFinish() {
        if (size < (nameList.size() - 1)) {
            group += 1;
            upLoadTraining();
            trainingVideoView.setUp(urlList.get(++size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
            trainingVideoView.startVideo();
            startTimerToSetTextAndProgress();
            tv_name.setText(nameList.get(size) + (size + 1) + "/" + nameList.size());


            time = time + trainingVideoView.currentDuration;
        } else {
            T.showToast(mContext, "播放完成", 0);
            mTimer.cancel();
            time = time + trainingVideoView.currentDuration;
            group += 1;
            upLoadTraining();
            size++;
            Intent intent = new Intent(mContext, TrainingFinishActivity.class);
            intent.putExtra("disease", disease);
            intent.putExtra("type", type);
            intent.putExtra("time", time);
            intent.putExtra("group", group);
            startActivity(intent);
            Log.e(TAG, "setOnFinish: " + size);
            progressBar.setProgress(0);
            finish();
        }

        Log.i(TAG, "setOnFinish: 自动播放完" + time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                JZMediaManager.instance().mediaPlayer.pause();
                Picasso.with(mContext).load(R.drawable.bofang)
                        .fit()
                        .into(iv_middle);
                new android.app.AlertDialog.Builder(this)
                        .setMessage("训练仍在进行中，确定要结束当前训练吗？结束训练后将无法保存训练数据")
                        .setPositiveButton("结束训练", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                time = time + trainingVideoView.currentDuration;
                                if (Integer.parseInt(getIntent().getStringExtra("newest_id")) > 0) {
                                    Intent intent = new Intent(mContext, TrainingFinishActivity.class);
                                    intent.putExtra("disease", disease);
                                    intent.putExtra("type", type);
                                    intent.putExtra("time", time);
                                    intent.putExtra("group", group);
                                    startActivity(intent);
                                }

                                finish();
                            }
                        })
                        .setNegativeButton("再练会儿", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JZMediaManager.instance().mediaPlayer.start();
                                Picasso.with(mContext).load(R.drawable.zanting)
                                        .fit()
                                        .into(iv_middle);
                            }
                        })
                        .show();
                break;
            case R.id.iv_more:
                if (mTimer != null) {
                    mTimer.cancel();
                }
                if (JZMediaManager.instance().mediaPlayer.isPlaying()) {
                    JZMediaManager.instance().mediaPlayer.pause();
                    Picasso.with(mContext).load(R.drawable.bofang)
                            .fit()
                            .into(iv_middle);
                }
                Intent intent_more = new Intent(mContext, TrainingListActivity.class);
                intent_more.putExtra("disease", disease);
                intent_more.putExtra("type", type);
                startActivity(intent_more);

                break;
            case R.id.iv_detail:
                if (mTimer != null) {
                    mTimer.cancel();
                }
                if (JZMediaManager.instance().mediaPlayer.isPlaying()) {
                    JZMediaManager.instance().mediaPlayer.pause();
                    Picasso.with(mContext).load(R.drawable.bofang)
                            .fit()
                            .into(iv_middle);
                }
                Intent intent_detail = new Intent(mContext, DetailTrainingActivity.class);
                intent_detail.putExtra("disease", disease);
                intent_detail.putExtra("type", type);
                intent_detail.putExtra("toNum", size);
                startActivity(intent_detail);
                break;
            case R.id.iv_middle:
                if (JZMediaManager.instance().mediaPlayer == null) {
                    JZMediaManager.instance().mediaPlayer = new MediaPlayer();
                }
                if (JZMediaManager.instance().mediaPlayer.isPlaying()) {
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                    JZMediaManager.instance().mediaPlayer.pause();
                    Picasso.with(mContext).load(R.drawable.bofang)
                            .fit()
                            .into(iv_middle);
                } else {
                    JZMediaManager.instance().mediaPlayer.start();
                    Picasso.with(mContext).load(R.drawable.zanting)
                            .fit()
                            .into(iv_middle);
                    startTimerToSetTextAndProgress();
                }
                break;
            case R.id.iv_left:
                if (size > 0) {
                    trainingVideoView.setUp(urlList.get(--size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
                    trainingVideoView.startVideo();
                    startTimerToSetTextAndProgress();
                    tv_name.setText(nameList.get(size) + (size + 1) + "/" + nameList.size());

                }
                break;
            case R.id.iv_right:
                if (size < (nameList.size() - 1)) {
                    trainingVideoView.setUp(urlList.get(++size), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
                    trainingVideoView.startVideo();
                    startTimerToSetTextAndProgress();
                    tv_name.setText(nameList.get(size) + (size + 1) + "/" + nameList.size());
                } else {
                    //TODO:视频直接跳过
                }
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
