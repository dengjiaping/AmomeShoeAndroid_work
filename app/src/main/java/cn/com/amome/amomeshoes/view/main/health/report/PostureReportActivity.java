package cn.com.amome.amomeshoes.view.main.health.report;

import android.app.Activity;
import android.content.Context;
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
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootStandInfo;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.model.StandDetectionModel;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.CalculationCoordinate;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.widget.CircleView;
import cn.com.amome.amomeshoes.widget.CircleView.RecInfo;

public class PostureReportActivity extends Activity implements OnClickListener {
    private String TAG = "PostureReportActivity";
    private Context mContext;
    private RelativeLayout ll_report_null;
    private CircleView mCircleCanvasLeft, mCircleCanvasRight;// 左右画布
    private TextView tv_title, tv_report_foot_date, tv_disease1_result,
            tv_disease2_result, tv_shoulder_report, tv_spine_report,
            tv_shoulder_name, tv_spine_name, tv_left_scale, tv_right_scale,
            tv_single_disease1_result, tv_single_disease2_result,
            tv_no_data_tip;
    private ImageView iv_left, iv_leftshoe, iv_rightshoe, iv_disease1_result,
            iv_disease2_result, iv_single_disease1_result,
            iv_single_disease2_result;
    private LinearLayout ll_report_posture_value, ll_report_double_disease,
            ll_report_single_disease1, ll_report_single_disease2,
            ll_no_disease, ll_left_scale, ll_right_scale, ll_posture_evaluate;
    private View view_left_scale, view_right_scale;
    private double canvesWidthLeft;// 左边画布宽
    private double canvesHeightLeft;// 左边画布高
    private double canvesWidthRight;// 右边画布宽
    private double canvesHeightRight;// 右边画布高
    private double screenWidth;// 屏幕宽度
    private double canvasConsultX, canvasConsultY;// 分辨率不同，参数不同
    double RDr;// 直径
    private short[] leftTotalPress = new short[64];
    private short[] rightTotalPress = new short[64];
    List<RecInfo> mCircleListLeft = new ArrayList<RecInfo>();// 左边的点坐标数据
    List<RecInfo> mCircleListRight = new ArrayList<RecInfo>();// 右边的点坐标数据
    private static final int MSG_GET_STAND_INFO = 0;
    private static final int MSG_GET_FOOT_DATA = 1;
    private static final int ONE_KEY_ADD = 2;
    private Gson gson = new Gson();
    private List<FootStandInfo> FootStandList;
    private FootStandInfo FootStandInfo;

    private List<IllnessInfo> postureInfoList;
    private RecyclerView re_posture;
    private Button btn_add;
    private PromotionAddAdapter promotionAddAdapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_STAND_INFO:
                            String str = (String) msg.obj;
                            if (str.equals("[{}]")) {
                                Log.i(TAG, "为空");
                                tv_no_data_tip.setVisibility(View.VISIBLE);
                            } else {
                                ll_report_null.setVisibility(View.GONE);
                                FootStandList = gson.fromJson(str,
                                        new TypeToken<List<FootStandInfo>>() {
                                        }.getType());
                                FootStandInfo = FootStandList.get(0);

                                tv_report_foot_date.setText("报告日期："
                                        + FootStandInfo.createtime);
                                if (!FootStandInfo.ldot64.equals("")) {
                                    String[] leftStr = FootStandInfo.ldot64.split(",");
                                    for (int i = 0; i < leftStr.length; i++) {
                                        leftTotalPress[i] = Short
                                                .parseShort(leftStr[i]);
                                    }
                                    mCircleCanvasLeft
                                            .setLeftCircleInfoColor(leftTotalPress);
                                }
                                if (!FootStandInfo.rdot64.equals("")) {
                                    String[] rightStr = FootStandInfo.rdot64.split(",");
                                    for (int i = 0; i < rightStr.length; i++) {
                                        rightTotalPress[i] = Short
                                                .parseShort(rightStr[i]);
                                    }
                                    mCircleCanvasRight
                                            .setRightCircleInfoColor(rightTotalPress);

                                }

                                StandDetectionModel standDetectionModel = new StandDetectionModel();
                                standDetectionModel = CalDetection.CalPressScale(
                                        leftTotalPress, rightTotalPress);
                                float leftScale = standDetectionModel.getLeftScale();
                                float rightScale = standDetectionModel.getRightScale();
                                tv_left_scale
                                        .setText(Math.round(leftScale * 100) + "%");
                                tv_right_scale.setText(Math.round(rightScale * 100)
                                        + "%");
                                // 设置比例图高度
                                LayoutParams lp_view_left_scale = (LayoutParams) view_left_scale
                                        .getLayoutParams();
                                lp_view_left_scale.height = (int) (ll_left_scale
                                        .getHeight() * leftScale);
                                view_left_scale.setLayoutParams(lp_view_left_scale);

                                LayoutParams lp_view_right_scale = (LayoutParams) view_right_scale
                                        .getLayoutParams();
                                lp_view_right_scale.height = (int) (ll_right_scale
                                        .getHeight() * rightScale);
                                view_right_scale.setLayoutParams(lp_view_right_scale);
                                if (!FootStandInfo.shoulderreport.equals("")) {
                                    tv_shoulder_name.setVisibility(View.VISIBLE);
                                    tv_shoulder_report.setVisibility(View.VISIBLE);
                                    tv_shoulder_name.setText(FootStandInfo.shoulder);
                                    tv_shoulder_report
                                            .setText(FootStandInfo.shoulderreport);
                                }
                                if (!FootStandInfo.spinereport.equals("")) {
                                    tv_spine_name.setVisibility(View.VISIBLE);
                                    tv_spine_report.setVisibility(View.VISIBLE);
                                    tv_spine_name.setText(FootStandInfo.spine);
                                    tv_spine_report.setText(FootStandInfo.spinereport);
                                }

                                if (!FootStandInfo.shoulder.equals("正常")
                                        && !FootStandInfo.spine.equals("正常")) {
                                    if (FootStandInfo.shoulder.contains("高低肩-左肩高")) {
                                        iv_disease1_result
                                                .setImageResource(R.drawable.stand_dise_left_high);
                                        tv_disease1_result
                                                .setText(FootStandInfo.shoulder);
                                    } else if (FootStandInfo.shoulder
                                            .contains("高低肩-右肩高")) {
                                        iv_disease1_result
                                                .setImageResource(R.drawable.stand_dise_right_high);
                                        tv_disease1_result
                                                .setText(FootStandInfo.shoulder);
                                    } else {
                                        // iv_disease1_result
                                        // .setImageResource(R.drawable.stand_dise_normal);
                                        // tv_disease1_result.setText("双肩："+FootStandInfo.shoulder);
                                    }

                                    if (FootStandInfo.spine.contains("重心靠前--颈椎痛")) {
                                        iv_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_front);
                                        tv_disease2_result.setText(FootStandInfo.spine);
                                    } else if (FootStandInfo.spine
                                            .contains("右侧--骨盆前悬，脊柱右弯")) {
                                        iv_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_right_turn);
                                        tv_disease2_result.setText(FootStandInfo.spine);
                                    } else if (FootStandInfo.spine
                                            .contains("左侧--骨盆后悬，脊柱左弯")) {
                                        iv_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_left_turn);
                                        tv_disease2_result.setText(FootStandInfo.spine);
                                    } else if (FootStandInfo.spine
                                            .contains("重心靠后--腰椎痛")) {
                                        iv_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_back);
                                        tv_disease2_result.setText(FootStandInfo.spine);
                                    } else {
                                        // iv_disease2_result
                                        // .setImageResource(R.drawable.stand_dise2_normal);
                                        // tv_disease2_result.setText("脊柱："+FootStandInfo.spine);
                                    }
                                } else if (!FootStandInfo.shoulder.equals("正常")) {
                                    ll_report_double_disease.setVisibility(View.GONE);
                                    ll_report_single_disease1
                                            .setVisibility(View.VISIBLE);
                                    tv_single_disease1_result
                                            .setText(FootStandInfo.shoulder);
                                    if (FootStandInfo.shoulder.contains("高低肩-左肩高")) {
                                        iv_single_disease1_result
                                                .setImageResource(R.drawable.stand_dise_left_high);
                                    } else if (FootStandInfo.shoulder
                                            .contains("高低肩-右肩高")) {
                                        iv_single_disease1_result
                                                .setImageResource(R.drawable.stand_dise_right_high);
                                    } else {
                                        // iv_single_disease1_result
                                        // .setImageResource(R.drawable.stand_dise_normal);
                                    }
                                } else if (!FootStandInfo.spine.equals("正常")) {
                                    ll_report_double_disease.setVisibility(View.GONE);
                                    ll_report_single_disease2
                                            .setVisibility(View.VISIBLE);
                                    tv_single_disease2_result
                                            .setText(FootStandInfo.spine);
                                    if (FootStandInfo.spine.contains("重心靠前--颈椎痛")) {
                                        iv_single_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_front);
                                    } else if (FootStandInfo.spine
                                            .contains("右侧--骨盆前悬，脊柱右弯")) {
                                        iv_single_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_right_turn);
                                    } else if (FootStandInfo.spine
                                            .contains("左侧--骨盆后悬，脊柱左弯")) {
                                        iv_single_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_left_turn);
                                    } else if (FootStandInfo.spine
                                            .contains("重心靠后--腰椎痛")) {
                                        iv_single_disease2_result
                                                .setImageResource(R.drawable.stand_dise2_back);
                                    } else {
                                        // iv_single_disease2_result
                                        // .setImageResource(R.drawable.stand_dise2_normal);
                                    }
                                } else if (FootStandInfo.shoulder.equals("正常")
                                        && FootStandInfo.spine.equals("正常")) {
                                    ll_report_double_disease.setVisibility(View.GONE);
                                    ll_no_disease.setVisibility(View.VISIBLE);
                                    tv_shoulder_report
                                            .setText("您现在身体很健康，请继续保持，生活中注意规避不健康的饮食，坐姿，站姿等情况。");
                                    tv_shoulder_report.setVisibility(View.VISIBLE);
                                }
                            }
                            break;
                        case MSG_GET_FOOT_DATA:
                            String string = (String) msg.obj;
                            if (TextUtils.isEmpty(string)) {
                            } else {

                                postureInfoList = gson.fromJson(string, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                initPostureData();


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

    private void initPostureData() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        re_posture.setLayoutManager(layoutManager);
        promotionAddAdapter = new PromotionAddAdapter(mContext, postureInfoList);
        re_posture.setAdapter(promotionAddAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_posture);
        mContext = this;
        initView();
        getWidgetL();
        getStandInfo();
        getPromotionData();
    }

    private void initView() {
        ll_report_null = (RelativeLayout) findViewById(R.id.ll_report_null);
        mCircleCanvasLeft = (CircleView) findViewById(R.id.canves_left);
        mCircleCanvasRight = (CircleView) findViewById(R.id.canves_right);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_leftshoe = (ImageView) findViewById(R.id.iv_leftshoe);
        iv_rightshoe = (ImageView) findViewById(R.id.iv_rightshoe);
        iv_disease1_result = (ImageView) findViewById(R.id.iv_disease1_result);
        iv_disease2_result = (ImageView) findViewById(R.id.iv_disease2_result);
        iv_single_disease1_result = (ImageView) findViewById(R.id.iv_single_disease1_result);
        iv_single_disease2_result = (ImageView) findViewById(R.id.iv_single_disease2_result);
        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_report_foot_date = (TextView) findViewById(R.id.tv_report_foot_date);
        tv_disease1_result = (TextView) findViewById(R.id.tv_disease1_result);
        tv_disease2_result = (TextView) findViewById(R.id.tv_disease2_result);
        tv_shoulder_name = (TextView) findViewById(R.id.tv_shoulder_name);
        tv_spine_name = (TextView) findViewById(R.id.tv_spine_name);
        tv_shoulder_report = (TextView) findViewById(R.id.tv_shoulder_report);
        tv_spine_report = (TextView) findViewById(R.id.tv_spine_report);
        tv_left_scale = (TextView) findViewById(R.id.tv_left_scale);
        tv_right_scale = (TextView) findViewById(R.id.tv_right_scale);
        tv_single_disease1_result = (TextView) findViewById(R.id.tv_single_disease1_result);
        tv_single_disease2_result = (TextView) findViewById(R.id.tv_single_disease2_result);
        tv_no_data_tip = (TextView) findViewById(R.id.tv_no_data_tip);
        ll_report_posture_value = (LinearLayout) findViewById(R.id.ll_report_posture_value);
        ll_report_double_disease = (LinearLayout) findViewById(R.id.ll_report_double_disease);
        ll_report_single_disease1 = (LinearLayout) findViewById(R.id.ll_report_single_disease1);
        ll_report_single_disease2 = (LinearLayout) findViewById(R.id.ll_report_single_disease2);
        ll_no_disease = (LinearLayout) findViewById(R.id.ll_no_disease);
        ll_posture_evaluate = (LinearLayout) findViewById(R.id.ll_posture_evaluate);
        ll_left_scale = (LinearLayout) findViewById(R.id.ll_left_scale);
        ll_right_scale = (LinearLayout) findViewById(R.id.ll_right_scale);
        view_left_scale = findViewById(R.id.view_left_scale);
        view_right_scale = findViewById(R.id.view_right_scale);

        tv_title.setText("姿态报告");
        tv_title.setTextColor(mContext.getResources().getColor(
                R.color.turkeygreen));

        findViewById(R.id.rl_left).setOnClickListener(this);

        re_posture = findViewById(R.id.re_posture);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

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

    /**
     * 获取控件的长宽
     */
    private void getWidgetL() {
        ViewTreeObserver vto = mCircleCanvasLeft.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCircleCanvasLeft.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                canvesHeightLeft = mCircleCanvasLeft.getHeight();
                canvesWidthLeft = mCircleCanvasLeft.getWidth();
                // 在720*1280基准下的画布大小为549*254
                canvasConsultX = canvesWidthLeft / 254;
                canvasConsultY = canvesHeightLeft / 549;
                drawCircle(1);
                mCircleCanvasLeft.setLeftCircleInfoColor(leftTotalPress);
            }
        });

        ViewTreeObserver vto1 = mCircleCanvasRight.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCircleCanvasRight.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                canvesHeightRight = mCircleCanvasRight.getHeight();
                canvesWidthRight = mCircleCanvasRight.getWidth();
                canvasConsultX = canvesWidthRight / 254;
                canvasConsultY = canvesHeightRight / 549;
                drawCircle(2);
                mCircleCanvasRight.setRightCircleInfoColor(rightTotalPress);
            }
        });
    }

    /**
     * 画圆
     */
    private void drawCircle(int type) {
        // 在竖排圆个数，定死的
        // 在横排圆个数,如果第一行为4个圆，2行6个，15行为5个.其他都为7个
        RDr = 14;
        if (screenWidth != 720) {
            RDr = RDr * canvasConsultX;
        }
        switch (type) {
            case 1:// 左边
                if (mCircleListLeft.size() <= 0) {
                    CalculationCoordinate cc = new CalculationCoordinate();
                    cc.drawCircleLeft(90, 70, canvesHeightLeft, canvasConsultX,
                            canvasConsultY, mCircleCanvasLeft); // 确定了所有的点
                    mCircleListLeft = cc.getCircleListLeft();

                    mCircleCanvasLeft.mCircleListLeftInfo.addAll(mCircleListLeft); // 把所有的点添加到控件里去准备使用

                } else {
                    mCircleCanvasLeft.mCircleListLeftInfo.addAll(mCircleListLeft);

                }
                break;
            case 2:// 右边
                if (mCircleListRight.size() <= 0) {
                    CalculationCoordinate cc1 = new CalculationCoordinate();
                    cc1.drawCircleRight(70, 70, canvesHeightRight, canvasConsultX,
                            canvasConsultY, mCircleCanvasRight);
                    mCircleListRight = cc1.getCircleListRight();

                    mCircleCanvasRight.mCircleListRightInfo
                            .addAll(mCircleListRight);

                } else {
                    mCircleCanvasRight.mCircleListRightInfo
                            .addAll(mCircleListRight);

                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取站一站信息
     */
    private void getStandInfo() {
        RequestParams params = new RequestParams();
        params.put("calltype", ClientConstant.GETSTANDINFO_TYPE);
        params.put("useid", SpfUtil.readUserId(mContext));
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_STAND_INFO, params,
                ClientConstant.STAND_URL);
    }

    /**
     * 获取姿态管家数据
     */
    private void getPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
        // true, true);
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.BUTLER_ALL);
        params.put("certificate", HttpService.getToken());
        params.put("type", "posture");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
                ClientConstant.PROMOTION_URL);
    }

    private void oneKeyAdd() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.ADD_MANAGER);
        params.put("certificate", HttpService.getToken());
        params.put("type", "posture");
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
                case MSG_GET_STAND_INFO:
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
                        Log.i(TAG, "MSG_GET_STAND_INFO解析失败");
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
    protected void onRestart() {
        super.onRestart();
        getPromotionData();
    }

}
