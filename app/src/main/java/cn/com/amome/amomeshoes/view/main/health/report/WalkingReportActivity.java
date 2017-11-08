package cn.com.amome.amomeshoes.view.main.health.report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.model.WalkingInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;

public class WalkingReportActivity extends Activity implements OnClickListener {
    private String TAG = "WalkingReportActivity";
    private Context mContext;
    private RelativeLayout ll_report_null;
    private LinearLayout ll_report_walk;
    private TextView tv_title, tv_report_foot_date, tv_walk_result,
            tv_step_result, tv_left_pacetime, tv_right_pacetime,
            tv_left_stridetime, tv_right_stridetime, tv_left_stridefqc,
            tv_right_stridefqc, tv_left_single_phase, tv_right_single_phase,
            tv_left_double_phase, tv_right_double_phase, tv_left_stand_phase,
            tv_right_stand_phase, tv_left_swing_phase, tv_right_swing_phase,
            tv_left_single_phase_percent, tv_right_single_phase_percent,
            tv_left_double_phase_percent, tv_right_double_phase_percent,
            tv_left_stand_phase_percent, tv_right_stand_phase_percent,
            tv_left_swing_phase_percent, tv_right_swing_phase_percent, tv_tip1,
            tv_tip2, tv_no_data_tip, tv_iserror;
    private RelativeLayout rl_walk_progress, rl_step_progress;
    private View view_walk_progress, view_step_progress;
    private Gson gson = new Gson();
    private List<WalkingInfo> WalkingInfoList;
    private WalkingInfo walkingInfo;
    private static final int MSG_GET_WALK_INFO = 0;
    private static final int MSG_GET_FOOT_DATA = 1;
    private static final int ONE_KEY_ADD = 2;

    private List<IllnessInfo> gaitInfoList;
    private RecyclerView re_gait;
    private Button btn_add;
    private PromotionAddAdapter promotionAddAdapter;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_WALK_INFO:
                            String str = (String) msg.obj;
                            if (str.equals("[{}]")) {
                                Log.i(TAG, "为空");
                                tv_no_data_tip.setVisibility(View.VISIBLE);
                            } else {
                                ll_report_null.setVisibility(View.GONE);
                                WalkingInfoList = gson.fromJson(str,
                                        new TypeToken<List<WalkingInfo>>() {
                                        }.getType());
                                walkingInfo = WalkingInfoList.get(0);
                                tv_report_foot_date.setText("报告日期："
                                        + walkingInfo.create_time);
                                //2017.11.08 ccf 修改添加异常的显示提示
                                if (walkingInfo.l_bisptphase.equals("0.00") &&
                                        walkingInfo.l_pacetime.equals("0.00") &&
                                        walkingInfo.l_stdphase.equals("0.00") &&
                                        walkingInfo.l_stridefqc.equals("0.00") &&
                                        walkingInfo.l_stridetime.equals("0.00") &&
                                        walkingInfo.l_swingphase.equals("0.00") &&
                                        walkingInfo.l_unisptphase.equals("0.00")) {
                                    tv_iserror.setVisibility(View.VISIBLE);
                                }
                                tv_walk_result.setText(walkingInfo.symmetry);
                                tv_step_result.setText(walkingInfo.gait);
                                tv_tip1.setText(walkingInfo.symmetryreport);
                                tv_tip2.setText(walkingInfo.gaitreport);
                                tv_left_pacetime.setText(walkingInfo.l_pacetime);
                                tv_right_pacetime.setText(walkingInfo.r_pacetime);
                                tv_left_stridetime.setText(walkingInfo.l_stridetime);
                                tv_right_stridetime.setText(walkingInfo.r_stridetime);
                                tv_left_stridefqc.setText(walkingInfo.l_stridefqc);
                                tv_right_stridefqc.setText(walkingInfo.r_stridefqc);
                                tv_left_single_phase.setText(walkingInfo.l_unisptphase);
                                tv_right_single_phase
                                        .setText(walkingInfo.r_unisptphase);
                                tv_left_double_phase.setText(walkingInfo.l_bisptphase);
                                tv_right_double_phase.setText(walkingInfo.r_bisptphase);
                                tv_left_stand_phase.setText(walkingInfo.l_stdphase);
                                tv_right_stand_phase.setText(walkingInfo.r_stdphase);
                                tv_left_swing_phase.setText(walkingInfo.l_swingphase);
                                tv_right_swing_phase.setText(walkingInfo.r_swingphase);
                                DecimalFormat df = new DecimalFormat("0.00");
                                tv_left_single_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.l_unisptphase) / Float
                                        .valueOf(walkingInfo.l_stridetime)) * 100)
                                        + "");
                                tv_right_single_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.r_unisptphase) / Float
                                        .valueOf(walkingInfo.r_stridetime)) * 100)
                                        + "");
                                tv_left_double_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.l_bisptphase) / Float
                                        .valueOf(walkingInfo.l_stridetime)) * 100)
                                        + "");
                                tv_right_double_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.r_bisptphase) / Float
                                        .valueOf(walkingInfo.r_stridetime)) * 100)
                                        + "");
                                tv_left_stand_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.l_stdphase) / Float
                                        .valueOf(walkingInfo.l_stridetime)) * 100)
                                        + "");
                                tv_right_stand_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.r_stdphase) / Float
                                        .valueOf(walkingInfo.r_stridetime)) * 100)
                                        + "");
                                tv_left_swing_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.l_swingphase) / Float
                                        .valueOf(walkingInfo.l_stridetime)) * 100)
                                        + "");
                                tv_right_swing_phase_percent.setText(df.format((Float
                                        .valueOf(walkingInfo.r_swingphase) / Float
                                        .valueOf(walkingInfo.r_stridetime)) * 100)
                                        + "");
                                LayoutParams lp_view_walk_progress = (LayoutParams) view_walk_progress
                                        .getLayoutParams();
                                if (walkingInfo.symmetry.equals("正常")) {
                                    lp_view_walk_progress.width = rl_walk_progress
                                            .getWidth() * 19 / 20;
                                    tv_walk_result
                                            .setTextColor(Color.rgb(255, 64, 112));
                                } else if (walkingInfo.symmetry.equals("一般")) {
                                    lp_view_walk_progress.width = rl_walk_progress
                                            .getWidth() / 2;
                                    tv_walk_result
                                            .setTextColor(Color.rgb(255, 141, 74));
                                } else if (walkingInfo.symmetry.equals("异常")) {
                                    lp_view_walk_progress.width = rl_walk_progress
                                            .getWidth() / 20;
                                    tv_walk_result
                                            .setTextColor(Color.rgb(255, 169, 67));
                                }
                                view_walk_progress
                                        .setLayoutParams(lp_view_walk_progress);

                                LayoutParams lp_view_step_progress = (LayoutParams) view_step_progress
                                        .getLayoutParams();
                                if (walkingInfo.gait.equals("优秀")) {
                                    lp_view_step_progress.width = rl_step_progress
                                            .getWidth() * 19 / 20;
                                    tv_step_result
                                            .setTextColor(Color.rgb(255, 64, 112));
                                } else if (walkingInfo.gait.equals("正常")) {
                                    lp_view_step_progress.width = rl_step_progress
                                            .getWidth() / 2;
                                    tv_step_result
                                            .setTextColor(Color.rgb(255, 141, 74));
                                } else if (walkingInfo.gait.equals("弱")) {
                                    lp_view_step_progress.width = rl_step_progress
                                            .getWidth() / 20;
                                    tv_step_result
                                            .setTextColor(Color.rgb(255, 169, 67));
                                }
                                view_step_progress
                                        .setLayoutParams(lp_view_step_progress);
                            }
                            break;
                        case MSG_GET_FOOT_DATA:
                            String string = (String) msg.obj;
                            if (TextUtils.isEmpty(string)) {
                            } else {

                                gaitInfoList = gson.fromJson(string, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                initGaitData();


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

        ;
    };

    private void initGaitData() {

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        re_gait.setLayoutManager(layoutManager);
        promotionAddAdapter = new PromotionAddAdapter(mContext, gaitInfoList);
        re_gait.setAdapter(promotionAddAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_walk);
        mContext = this;
        initView();
        getWalkInfo();
        getPromotionData();
    }

    private void initView() {
        ll_report_null = (RelativeLayout) findViewById(R.id.ll_report_null);
        ll_report_walk = (LinearLayout) findViewById(R.id.ll_report_walk);
        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_report_foot_date = (TextView) findViewById(R.id.tv_report_foot_date);
        tv_walk_result = (TextView) findViewById(R.id.tv_walk_result);
        tv_step_result = (TextView) findViewById(R.id.tv_step_result);
        tv_left_pacetime = (TextView) findViewById(R.id.tv_left_pacetime);
        tv_right_pacetime = (TextView) findViewById(R.id.tv_right_pacetime);
        tv_left_stridetime = (TextView) findViewById(R.id.tv_left_stridetime);
        tv_right_stridetime = (TextView) findViewById(R.id.tv_right_stridetime);
        tv_left_stridefqc = (TextView) findViewById(R.id.tv_left_stridefqc);
        tv_right_stridefqc = (TextView) findViewById(R.id.tv_right_stridefqc);
        tv_left_single_phase = (TextView) findViewById(R.id.tv_left_single_phase);
        tv_right_single_phase = (TextView) findViewById(R.id.tv_right_single_phase);
        tv_left_double_phase = (TextView) findViewById(R.id.tv_left_double_phase);
        tv_right_double_phase = (TextView) findViewById(R.id.tv_right_double_phase);
        tv_left_stand_phase = (TextView) findViewById(R.id.tv_left_stand_phase);
        tv_right_stand_phase = (TextView) findViewById(R.id.tv_right_stand_phase);
        tv_left_swing_phase = (TextView) findViewById(R.id.tv_left_swing_phase);
        tv_right_swing_phase = (TextView) findViewById(R.id.tv_right_swing_phase);
        tv_left_single_phase_percent = (TextView) findViewById(R.id.tv_left_single_phase_percent);
        tv_right_single_phase_percent = (TextView) findViewById(R.id.tv_right_single_phase_percent);
        tv_left_double_phase_percent = (TextView) findViewById(R.id.tv_left_double_phase_percent);
        tv_right_double_phase_percent = (TextView) findViewById(R.id.tv_right_double_phase_percent);
        tv_left_stand_phase_percent = (TextView) findViewById(R.id.tv_left_stand_phase_percent);
        tv_right_stand_phase_percent = (TextView) findViewById(R.id.tv_right_stand_phase_percent);
        tv_left_swing_phase_percent = (TextView) findViewById(R.id.tv_left_swing_phase_percent);
        tv_right_swing_phase_percent = (TextView) findViewById(R.id.tv_right_swing_phase_percent);
        tv_tip1 = (TextView) findViewById(R.id.tv_tip1);
        tv_tip2 = (TextView) findViewById(R.id.tv_tip2);
        tv_iserror = (TextView) findViewById(R.id.tv_iserror);
        tv_no_data_tip = (TextView) findViewById(R.id.tv_no_data_tip);
        rl_walk_progress = (RelativeLayout) findViewById(R.id.rl_walk_progress);
        rl_step_progress = (RelativeLayout) findViewById(R.id.rl_step_progress);
        view_walk_progress = findViewById(R.id.view_walk_progress);
        view_step_progress = findViewById(R.id.view_step_progress);
        tv_title.setText("步态报告");
        tv_title.setTextColor(mContext.getResources().getColor(
                R.color.turkeygreen));
        findViewById(R.id.rl_left).setOnClickListener(this);

        re_gait = findViewById(R.id.re_gait);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    /**
     * 获取走一走信息
     */
    private void getWalkInfo() {
        RequestParams params = new RequestParams();
        params.put("calltype", ClientConstant.GETWALKINFO_TYPE);
        params.put("useid", SpfUtil.readUserId(mContext));
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_WALK_INFO, params,
                ClientConstant.WALK_URL);
    }

    /**
     * 获取步态管家数据
     */
    private void getPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
        // true, true);
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.BUTLER_ALL);
        params.put("certificate", HttpService.getToken());
        params.put("type", "gait");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
                ClientConstant.PROMOTION_URL);
    }

    private void oneKeyAdd() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.ADD_MANAGER);
        params.put("certificate", HttpService.getToken());
        params.put("type", "gait");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, ONE_KEY_ADD, params,
                ClientConstant.PROMOTION_URL);
    }

    HttpService.ICallback callback = new HttpService.ICallback() {

        @Override
        public void onHttpPostSuccess(int type, int statusCode,
                                      Header[] headers, byte[] responseBody) {
            // TODO Auto-generated method stub
            String result;
            switch (type) {
                case MSG_GET_WALK_INFO:
                    result = new String(responseBody);
                    try {
                        JSONObject obj = new JSONObject(result);
                        String return_msg = obj.getString("return_msg");
                        int return_code = obj.getInt("return_code");
                        Message msg = Message.obtain();
                        if (return_code == 0) {
                            msg.what = ClientConstant.HANDLER_SUCCESS;
                            msg.arg1 = type;
                            msg.obj = return_msg;
                        }
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "MSG_GET_WALK_INFO解析失败");
                    }
                    break;
                case MSG_GET_FOOT_DATA:
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
                case ONE_KEY_ADD:
                    result = new String(responseBody);
                    try {

                        JSONObject obj = new JSONObject(result);
                        JSONArray return_msg = obj.getJSONArray("return_msg");
                        JSONObject msg = (JSONObject) return_msg.get(0);

                        int return_code = obj.getInt("return_code");
                        if (return_code == 0) {
                            if (msg.getString("retval").equals("0x00")) {
                                T.showToast(mContext, "添加成功", 0);
                                getPromotionData();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "MSG_DETAIL_DATA解析失败");

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

        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
            case R.id.btn_add:
                oneKeyAdd();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getPromotionData();
    }
}
