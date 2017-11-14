package cn.com.amome.amomeshoes.view.main.health.promotion.detail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.DetailTrainingInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView.TrainingVideoFragment;
import cn.jzvd.JZVideoPlayer;

public class DetailTrainingActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String TAG = "DetailTrainingActivity";
    private Context mContext;
    private Gson gson = new Gson();
    private String disease = null;
    private String type = null;
    private static final int MSG_GET_DATA = 0;
    private List<DetailTrainingInfo> mTrainingInfo;
    private List<TrainingVideoFragment> fragmentList = new ArrayList<>();
    private int pagerNum = 0;
    private int currentNum = 0;

    private ImageView iv_back, iv_left, iv_right;
    private TextView tv_num;
    private ViewPager vp_detail_training;
    private int toNum = 0;

    //private DetailTrainingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training);
        mContext = this;
        Intent intent = getIntent();
        disease = intent.getStringExtra("disease");
        type = intent.getStringExtra("type");
        toNum = intent.getIntExtra("toNum", 0);
        initView();
        getTrainingData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_num = (TextView) findViewById(R.id.tv_num);
        vp_detail_training = (ViewPager) findViewById(R.id.vp_detail_training);

        iv_back.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);

        vp_detail_training.addOnPageChangeListener(this);
        vp_detail_training.setOffscreenPageLimit(0);
    }


    /**
     * 获取训练详情的信息
     */
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
                        Log.i(TAG, "MSG_GET_FOOT_DATA解析失败");

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
            Log.e(TAG, "DetailTrainingActivity>onHttpPostFailure: 获取训练详细数据失败");

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
        pagerNum = mTrainingInfo.size();
        tv_num.setText((currentNum + 1) + "/" + pagerNum);
        for (int i = 0; i < mTrainingInfo.size(); i++) {
            fragmentList.add(new TrainingVideoFragment().setIndex(i, mTrainingInfo.get(i), mContext));
        }
        TrainingVideoAdapter adapter = new TrainingVideoAdapter(getSupportFragmentManager());


        //adapter = new DetailTrainingAdapter(mTrainingInfo, mContext);
        vp_detail_training.setAdapter(adapter);
        //adapter.start_video();

        if (toNum != 0) {
            vp_detail_training.setCurrentItem(toNum);
            currentNum=toNum;
            tv_num.setText((currentNum + 1) + "/" + pagerNum);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_left:
                if (currentNum > 0 && currentNum <= pagerNum - 1) {
                    currentNum--;
                    vp_detail_training.setCurrentItem(currentNum);
                    tv_num.setText((currentNum + 1) + "/" + pagerNum);

                }
                break;
            case R.id.iv_right:
                if (currentNum < pagerNum - 1) {
                    currentNum++;
                    vp_detail_training.setCurrentItem(currentNum);
                    tv_num.setText((currentNum + 1) + "/" + pagerNum);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        JZVideoPlayer.releaseAllVideos();
        currentNum = position;
        tv_num.setText((currentNum + 1) + "/" + pagerNum);


    }

    @Override
    public void onPageScrollStateChanged(int state) {


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
        //JZMediaManager.instance().mediaPlayer.release();
    }


    public class TrainingVideoAdapter extends FragmentPagerAdapter {

        public TrainingVideoAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}

