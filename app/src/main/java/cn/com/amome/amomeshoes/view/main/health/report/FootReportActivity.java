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
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.FootSecretOtherAdapter;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootLookInfo;
import cn.com.amome.amomeshoes.model.FootMeaInfo;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.util.CalculationCoordinate;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.widget.CircleView;
import cn.com.amome.amomeshoes.widget.CircleView.RecInfo;

/**
 * @author jyw
 */
public class FootReportActivity extends Activity implements OnClickListener {
    private String TAG = "FootReportActivity";
    private Context mContext;
    private RelativeLayout ll_report_null;
    private TextView tv_title, tv_report_foot_date, tv_instep_result,
            tv_spin_result, tv_instep_tip, tv_spin_tip, tv_instep_name,
            tv_spin_name, tv_title_foot_report, tv_no_data_tip;
    private ImageView iv_left, iv_instep, iv_spin, iv_leftshoe, iv_rightshoe;
    private LinearLayout ll_shoepic, ll_report_foot_print,
            ll_report_foot_shape, ll_report_foot_dise, ll_title_foot_report;
    private CircleView mCircleCanvasLeft;// 左边画布
    private CircleView mCircleCanvasRight;// 右边画布
    private GridView gv_footother;
    private double canvesWidthLeft;// 左边画布宽
    private double canvesHeightLeft;// 左边画布高
    private double canvesWidthRight;// 右边画布宽
    private double canvesHeightRight;// 右边画布高
    private double screenWidth;// 屏幕宽度
    private double canvasConsultX, canvasConsultY;// 分辨率不同，参数不同
    double RDr;// 直径
    private static final int MSG_GET_MEAINFO = 0;
    private static final int MSG_GET_CHECK_FOOT = 1;
    private static final int MSG_GET_FOOT_DATA = 2;
    private static final int ONE_KEY_ADD = 3;
    private Gson gson = new Gson();
    private List<FootMeaInfo> FootMeaList;
    private FootMeaInfo FootMeaInfo;
    private List<FootLookInfo> FootList;
    private FootLookInfo footLookInfo;
    private FootSecretOtherAdapter foAdapter;
    private List<Integer> otherList = new ArrayList<Integer>();
    private List<String> otherdatalist = new ArrayList<String>();
    private Map<Object, String> map = new HashMap<Object, String>();
    private int i = 0;
    private short[] leftTotalPress = new short[64];
    private short[] rightTotalPress = new short[64];
    List<RecInfo> mCircleListLeft = new ArrayList<RecInfo>();// 左边的点坐标数据
    List<RecInfo> mCircleListRight = new ArrayList<RecInfo>();// 右边的点坐标数据

    private List<IllnessInfo> footInfoList;
    private RecyclerView re_foot;
    private Button btn_add;
    private PromotionAddAdapter promotionAddAdapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_MEAINFO:
                            String str = (String) msg.obj;
                            if (str.equals("[{}]")) {
                                Log.i(TAG, "为空");
                                tv_no_data_tip.setVisibility(View.VISIBLE);
                            } else {
                                ll_report_null.setVisibility(View.GONE);
                                FootMeaList = gson.fromJson(str,
                                        new TypeToken<List<FootMeaInfo>>() {
                                        }.getType());
                                FootMeaInfo = FootMeaList.get(0);
                                tv_report_foot_date.setText("报告日期："
                                        + FootMeaInfo.createtime);
                                if (!FootMeaInfo.ldot64.equals("")) {
                                    String[] leftStr = FootMeaInfo.ldot64.split(",");
                                    for (int i = 0; i < leftStr.length; i++) {
                                        leftTotalPress[i] = Short
                                                .parseShort(leftStr[i]);
                                    }
                                    mCircleCanvasLeft
                                            .setLeftCircleInfoColor(leftTotalPress);
                                }
                                if (!FootMeaInfo.rdot64.equals("")) {
                                    String[] rightStr = FootMeaInfo.rdot64.split(",");
                                    for (int i = 0; i < rightStr.length; i++) {
                                        rightTotalPress[i] = Short
                                                .parseShort(rightStr[i]);
                                    }
                                    mCircleCanvasRight
                                            .setRightCircleInfoColor(rightTotalPress);
                                }
                                tv_instep_result.setText("足弓:" + FootMeaInfo.turn);
                                tv_spin_result.setText("旋前/后:" + FootMeaInfo.spin);
                                if (!FootMeaInfo.turnreport.equals("")) {
                                    tv_instep_tip.setVisibility(View.VISIBLE);
                                    tv_instep_name.setVisibility(View.VISIBLE);
                                    tv_instep_tip.setText(FootMeaInfo.turnreport);
                                    tv_instep_name.setText(FootMeaInfo.turn);
                                }
                                if (!FootMeaInfo.spinreport.equals("")) {
                                    tv_spin_tip.setVisibility(View.VISIBLE);
                                    tv_spin_name.setVisibility(View.VISIBLE);
                                    tv_spin_tip.setText(FootMeaInfo.spinreport);
                                    tv_spin_name.setText(FootMeaInfo.spin);
                                }
                                if (FootMeaInfo.turn.contains("高弓")) {
                                    iv_instep
                                            .setImageResource(R.drawable.squat_instep_high);
                                } else if (FootMeaInfo.turn.contains("扁平")) {
                                    iv_instep
                                            .setImageResource(R.drawable.squat_instep_flat);
                                } else if (FootMeaInfo.turn.contains("正常")) {
                                    iv_instep
                                            .setImageResource(R.drawable.squat_instep_normal);
                                } else {
                                }
                                if (FootMeaInfo.spin.contains("旋前")) {
                                    iv_spin.setImageResource(R.drawable.squat_spin_front);
                                } else if (FootMeaInfo.spin.contains("旋后")) {
                                    iv_spin.setImageResource(R.drawable.squat_spin_back);
                                } else if (FootMeaInfo.spin.contains("正常")) {
                                    iv_spin.setImageResource(R.drawable.squat_spin_normal);
                                } else {
                                }
                                if (FootMeaInfo.turn.contains("正常")
                                        && FootMeaInfo.spin.contains("正常")) {
                                    tv_instep_tip.setVisibility(View.VISIBLE);
                                    // tv_title_foot_report.setText("恭喜您");
                                    tv_instep_tip
                                            .setText("您现在身体很健康，请继续保持，生活中注意规避不健康的饮食，坐姿，站姿等情况。");
                                }
                                ll_title_foot_report.setVisibility(View.VISIBLE);// 足部报告/恭喜您两种情况，后显示
                                getCheckFoot();
                            }
                            break;
                        case MSG_GET_CHECK_FOOT:
                            String footJson = (String) msg.obj;
                            if (footJson.equals("[{}]")) {
                            } else {
                                FootList = gson.fromJson(footJson,
                                        new TypeToken<List<FootLookInfo>>() {
                                        }.getType());
                                footLookInfo = FootList.get(0);
                                // ****其他情况******************
                                if (null == FootList) {
                                } else if (footLookInfo.ftache.equals("")
                                        && footLookInfo.ftdisease.equals("")) {
                                } else {
                                    ll_report_foot_dise.setVisibility(View.VISIBLE);
                                    if (footLookInfo.ftache.contains("脚趾")) {
                                        otherList.add(R.drawable.look_foot_toe_s);
                                        otherdatalist.add("脚趾");
                                        map.put(i, "脚趾");
                                        i++;
                                    }
                                    if (footLookInfo.ftache.contains("脚后跟")) {
                                        otherList.add(R.drawable.look_foot_heel_s);
                                        otherdatalist.add("脚后跟");
                                        map.put(i, "脚后跟");
                                        i++;
                                    }
                                    if (footLookInfo.ftache.contains("脚底板")) {
                                        otherList.add(R.drawable.look_foot_sole_s);
                                        otherdatalist.add("脚底板");

                                        map.put(i, "脚底板");
                                        i++;
                                    }
                                    if (footLookInfo.ftache.contains("脚心")) {
                                        otherList.add(R.drawable.look_foot_arch_s);
                                        otherdatalist.add("脚心");

                                        map.put(i, "脚心");
                                        i++;
                                    }
                                    if (footLookInfo.ftache.contains("前脚掌")) {
                                        otherList.add(R.drawable.look_foot_palm_s);
                                        otherdatalist.add("前脚掌");

                                        map.put(i, "前脚掌");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("脚藓")) {
                                        otherList.add(R.drawable.look_foot_hongkong_s);
                                        otherdatalist.add("脚藓");

                                        map.put(i, "脚藓");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("脚气")) {
                                        otherList.add(R.drawable.look_foot_beriberi_s);
                                        otherdatalist.add("脚气");

                                        map.put(i, "脚气");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("拇外翻")) {
                                        otherList.add(R.drawable.look_foot_hv_s);
                                        otherdatalist.add("拇外翻");

                                        map.put(i, "拇外翻");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("脚臭")) {
                                        otherList.add(R.drawable.look_foot_odor_s);
                                        otherdatalist.add("脚臭");

                                        map.put(i, "脚臭");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("鸡眼")) {
                                        otherList.add(R.drawable.look_foot_corn_s);
                                        otherdatalist.add("鸡眼");

                                        map.put(i, "鸡眼");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("脚底脱皮")) {
                                        otherList.add(R.drawable.look_foot_peeling_s);
                                        otherdatalist.add("脚底脱皮");

                                        map.put(i, "脚底脱皮");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("足底筋膜炎")) {
                                        otherList.add(R.drawable.look_foot_pf_s);
                                        otherdatalist.add("足底筋膜炎");

                                        map.put(i, "足底筋膜炎");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("跟腱周围炎")) {
                                        otherList.add(R.drawable.look_foot_at_s);
                                        otherdatalist.add("跟腱周围炎");

                                        map.put(i, "跟腱周围炎");
                                        i++;
                                    }
                                    if (footLookInfo.ftdisease.contains("跟骨骨膜炎")) {
                                        otherList
                                                .add(R.drawable.look_foot_periostitis_s);
                                        otherdatalist.add("跟骨骨膜炎");

                                        map.put(i, "跟骨骨膜炎");
                                        i++;
                                    }
                                }
                                foAdapter = new FootSecretOtherAdapter(mContext,
                                        otherList, otherdatalist, mHandler);
                                gv_footother.setAdapter(foAdapter);
                            }
                            break;
                        case MSG_GET_FOOT_DATA:
                            String string = (String) msg.obj;
                            if (TextUtils.isEmpty(string)) {
                            } else {

                                footInfoList = gson.fromJson(string, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                initFootData();


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

    private void initFootData() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        re_foot.setLayoutManager(layoutManager);
        promotionAddAdapter = new PromotionAddAdapter(mContext, footInfoList);
        re_foot.setAdapter(promotionAddAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_foots);
        mContext = this;
        initView();
        getWidgetL();
        getSquatInfo();
        //getPromotionData();
    }

    private void initView() {
        ll_report_null = (RelativeLayout) findViewById(R.id.ll_report_null);
        mCircleCanvasLeft = (CircleView) findViewById(R.id.canves_left);
        mCircleCanvasRight = (CircleView) findViewById(R.id.canves_right);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_report_foot_date = (TextView) findViewById(R.id.tv_report_foot_date);
        tv_instep_tip = (TextView) findViewById(R.id.tv_instep_tip);
        tv_spin_tip = (TextView) findViewById(R.id.tv_spin_tip);
        tv_instep_name = (TextView) findViewById(R.id.tv_instep_name);
        tv_spin_name = (TextView) findViewById(R.id.tv_spin_name);
        tv_instep_result = (TextView) findViewById(R.id.tv_instep_result);
        tv_spin_result = (TextView) findViewById(R.id.tv_spin_result);
        tv_title_foot_report = (TextView) findViewById(R.id.tv_title_foot_report);
        tv_no_data_tip = (TextView) findViewById(R.id.tv_no_data_tip);
        iv_instep = (ImageView) findViewById(R.id.iv_instep);
        iv_spin = (ImageView) findViewById(R.id.iv_spin);
        iv_leftshoe = (ImageView) findViewById(R.id.iv_leftshoe);
        iv_rightshoe = (ImageView) findViewById(R.id.iv_rightshoe);
        ll_report_foot_print = (LinearLayout) findViewById(R.id.ll_report_foot_print);
        ll_report_foot_shape = (LinearLayout) findViewById(R.id.ll_report_foot_shape);
        ll_report_foot_dise = (LinearLayout) findViewById(R.id.ll_report_foot_dise);
        ll_title_foot_report = (LinearLayout) findViewById(R.id.ll_title_foot_report);
        gv_footother = (GridView) findViewById(R.id.gv_other);

        tv_title.setText("足部报告");
        tv_title.setTextColor(mContext.getResources().getColor(
                R.color.turkeygreen));

        findViewById(R.id.rl_left).setOnClickListener(this);
        re_foot = findViewById(R.id.re_foot);
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
     * 获取蹲一蹲信息
     */
    private void getSquatInfo() {
        RequestParams params = new RequestParams();
        params.put("calltype", ClientConstant.GETMEAINFO_TYPE);
        params.put("useid", SpfUtil.readUserId(mContext));
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_MEAINFO, params,
                ClientConstant.MEASURE_URL);
    }

    /**
     * 获取看一看数据
     */
    private void getCheckFoot() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GETCHECKFOOT_TYPE);
        params.put("nickname", "0自己");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_CHECK_FOOT, params,
                ClientConstant.GETCHECKFOOT_URL);

    }

    /**
     * 获取足部管家数据
     */
    private void getPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
        // true, true);
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.BUTLER_ALL);
        params.put("certificate", HttpService.getToken());
        params.put("type", "foot");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
                ClientConstant.PROMOTION_URL);
    }

    private void oneKeyAdd() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.ADD_MANAGER);
        params.put("certificate", HttpService.getToken());
        params.put("type", "foot");
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
                case MSG_GET_MEAINFO:
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
                        Log.i(TAG, "MSG_GET_MEAINFO解析失败");
                    }
                    break;
                case MSG_GET_CHECK_FOOT:
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
                        Log.i(TAG, "MSG_GET_CHECK_FOOT解析失败");
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getPromotionData();
    }
}
