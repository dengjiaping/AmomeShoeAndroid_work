package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.IllnessDetailTrueInfo;
import cn.com.amome.amomeshoes.model.VideoIconInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;

public class IllnessDetailTrueActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private static final String TAG = "IllnessDetailTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private static final int GET_VIDEO = 1;
    private static final int REMOVE_DIS = 2;
    private String disease = null;
    private IllnessDetailTrueInfo info;

    private ImageView iv_first, iv_left, iv_right, iv_training, iv_training_enter, iv_fitting, iv_fitting_enter, iv_nursing, iv_nursing_enter;
    private TextView tv_name, tv_training_name, tv_training_num, tv_fitting_name, tv_fitting_num, tv_nursing_name, tv_nursing_num;
    private LinearLayout ll_training, ll_fitting, ll_nursing;
    private VideoIconInfo mVideoIconInfo;
    private String mNewest_id_training;
    private String mDone_times;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illdess_detail_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        initView();
        getIllnessDetailInfo();
        getVideoData();
    }

    private void initView() {
        iv_first = findViewById(R.id.iv_first);
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);
        iv_training = findViewById(R.id.iv_training);
        iv_training_enter = findViewById(R.id.iv_training_enter);
        iv_fitting = findViewById(R.id.iv_fitting);
        iv_fitting_enter = findViewById(R.id.iv_fitting_enter);
        iv_nursing = findViewById(R.id.iv_nursing);
        iv_nursing_enter = findViewById(R.id.iv_nursing_enter);
        tv_name = findViewById(R.id.tv_name);
        tv_training_name = findViewById(R.id.tv_training_name);
        tv_training_num = findViewById(R.id.tv_training_num);
        tv_fitting_name = findViewById(R.id.tv_fitting_name);
        tv_fitting_num = findViewById(R.id.tv_fitting_num);
        tv_nursing_name = findViewById(R.id.tv_nursing_name);
        tv_nursing_num = findViewById(R.id.tv_nursing_num);
        ll_training = findViewById(R.id.ll_training);
        ll_fitting = findViewById(R.id.ll_fitting);
        ll_nursing = findViewById(R.id.ll_nursing);

        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        iv_training.setOnClickListener(this);
        iv_training_enter.setOnClickListener(this);
        iv_fitting.setOnClickListener(this);
        iv_fitting_enter.setOnClickListener(this);
        iv_nursing.setOnClickListener(this);
        iv_nursing_enter.setOnClickListener(this);
    }

    private void getIllnessDetailInfo() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_FINISH_DATA);
        params.put("disease", disease);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_DATA, params,
                ClientConstant.PROMOTION_URL);
    }


    private void getVideoData() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_TRAINING_VIDEO);
        params.put("disease", disease);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, GET_VIDEO, params,
                ClientConstant.PROMOTION_URL);
    }

    private void removeDisease() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.REMOVE_DISEASE);
        params.put("disease", disease);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, REMOVE_DIS, params,
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
                case GET_VIDEO:
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
                        Log.i(TAG, "GET_VIDEO解析失败");
                    }
                    break;
                case REMOVE_DIS:
                    result = new String(responseBody);
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONArray return_msg = obj.getJSONArray("return_msg");
                        JSONObject msg = (JSONObject) return_msg.get(0);
                        //JSONObject msg=obj.getJSONObject("return_msg");

                        int return_code = obj.getInt("return_code");
                        if (return_code == 0) {
                            if (msg.getString("retval").equals("0x00")) {
                                T.showToast(mContext, "移除成功", 0);
                                Intent intent = new Intent(mContext, IllnessDetailActivity.class);
                                intent.putExtra("name", disease);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "GET_VIDEO解析失败");
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
            Log.e(TAG, "IllnessDetailTrueActivity>onHttpPostFailure: 获取疾病完成数据失败");
        }
    };


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    String str = (String) msg.obj;
                    switch (msg.arg1) {

                        case MSG_GET_DATA:
                            if (TextUtils.isEmpty(str)) {
                            } else {
                                Type type = new TypeToken<List<IllnessDetailTrueInfo>>() {
                                }.getType();
                                List<IllnessDetailTrueInfo> list;
                                list = mGson.fromJson(str, type);
                                info = list.get(0);
                                initData();
                            }
                            break;

                        case GET_VIDEO:
                            Type type = new TypeToken<List<VideoIconInfo>>() {
                            }.getType();
                            List<VideoIconInfo> list;
                            list = mGson.fromJson(str, type);
                            mVideoIconInfo = list.get(0);

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

    private void dowmloadVideo() {
        int total_size = mVideoIconInfo.getTotal_size();
        List<VideoIconInfo.VideosBean> videos = mVideoIconInfo.getVideos();
        for (VideoIconInfo.VideosBean iconInfo :
                videos) {
            //取得文件名
            String down_url = iconInfo.getIcon();
            String filename = down_url.substring(down_url.lastIndexOf('/') + 1);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Amome/video/";
            try {
                AsynHttpDowanloadFile.downloadVideo(down_url, path, filename,
                        mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initData() {
        String type = info.getType();
        String icon = info.getIcon();

        List<IllnessDetailTrueInfo.UserdailyBean> userdaily = info.getUserdaily();
        IllnessDetailTrueInfo.UserdailyBean userdailyBean = userdaily.get(0);
        List<IllnessDetailTrueInfo.UserdailyBean.TrainingBean> training = userdailyBean.getTraining();
        List<IllnessDetailTrueInfo.UserdailyBean.NursingBean> nursing = userdailyBean.getNursing();
        IllnessDetailTrueInfo.UserdailyBean.TrainingBean trainingBean = training.get(0);
        IllnessDetailTrueInfo.UserdailyBean.NursingBean nursingBean = nursing.get(0);

        String num_training = trainingBean.getNum();
        String name_training = trainingBean.getName();
        String num_done_training = trainingBean.getNum_done();
        mNewest_id_training = trainingBean.getNewest_id();
        mDone_times = trainingBean.getDone_times();
        String num_nursing = nursingBean.getNum();
        String name_nursing = nursingBean.getName();
        String num_done_nursing = nursingBean.getNum_done();

        tv_name.setText(type);
        Picasso.with(mContext).load(icon)
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(iv_first);
        if (Integer.parseInt(num_done_training) < Integer.parseInt(num_training)) {
            Picasso.with(mContext).load(R.drawable.begin_btn)
                    .into(iv_training_enter);
            Picasso.with(mContext).load(R.drawable.xunlian)
                    .into(iv_training);
        } else if (Integer.parseInt(num_done_training) == Integer.parseInt(num_training)) {
            Picasso.with(mContext).load(R.drawable.again_btn)
                    .into(iv_training_enter);
            Picasso.with(mContext).load(R.drawable.xunlian_true)
                    .into(iv_training);
        }

        if (Integer.parseInt(num_done_nursing) < Integer.parseInt(num_nursing)) {
            Picasso.with(mContext).load(R.drawable.begin_btn)
                    .into(iv_fitting_enter);
            Picasso.with(mContext).load(R.drawable.yanghu)
                    .into(iv_fitting);
        } else if (Integer.parseInt(num_done_nursing) == Integer.parseInt(num_nursing)) {
            Picasso.with(mContext).load(R.drawable.again_btn)
                    .into(iv_fitting_enter);
            Picasso.with(mContext).load(R.drawable.yanghu_true)
                    .into(iv_fitting);
        }

        tv_training_name.setText(name_training);
        tv_training_num.setText(num_done_training);
        tv_nursing_name.setText(name_nursing);
        tv_nursing_num.setText(num_done_nursing);

        //这里先将健康配件部分隐藏
        ll_fitting.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getIllnessDetailInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                openOptionsMenu();
                //TODO:Dialog
                break;
            case R.id.iv_training:
                break;
            case R.id.iv_training_enter:
                if (mVideoIconInfo != null) {
                    //判断最后一个视频是否存在
                    List<VideoIconInfo.VideosBean> videos = mVideoIconInfo.getVideos();
                    VideoIconInfo.VideosBean videosBean = videos.get(videos.size() - 1);
                    String down_url = videosBean.getIcon();
                    String filename = down_url.substring(down_url.lastIndexOf('/') + 1);
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Amome/video/";
                    File file = new File(path + filename);
                    if (file.exists()) {
                        Intent intent_training_true = new Intent(mContext, DetailTrainingTrueActivity.class);
                        intent_training_true.putExtra("disease", disease);
                        intent_training_true.putExtra("type", "training");
                        intent_training_true.putExtra("newest_id", mNewest_id_training);
                        intent_training_true.putExtra("done_times", mDone_times);
                        startActivity(intent_training_true);
                    } else {
                        //TODO:dialog
                        dowmloadVideo();
                    }
                }


                break;
            case R.id.iv_nursing:
                Intent intent_nursing = new Intent(mContext, DetailNursingActivity.class);
                intent_nursing.putExtra("disease", disease);
                intent_nursing.putExtra("type", "nursing");
                startActivity(intent_nursing);
                break;
            case R.id.iv_nursing_enter:
                Intent intent_nursing_true = new Intent(mContext, DetailNursingTrueActivity.class);
                intent_nursing_true.putExtra("disease", disease);
                intent_nursing_true.putExtra("type", "nursing");
                startActivity(intent_nursing_true);
                break;
            case R.id.iv_fitting:
                //隐藏的配件界面
                break;
            case R.id.iv_fitting_enter:
                //隐藏的配件界面
                break;
            default:
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_video_remove, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download_video:

                if (mVideoIconInfo != null) {
                    dowmloadVideo();
                }
                break;
            case R.id.remove:
                removeDisease();
                break;
            default:
                break;
        }
        return true;
    }


}
