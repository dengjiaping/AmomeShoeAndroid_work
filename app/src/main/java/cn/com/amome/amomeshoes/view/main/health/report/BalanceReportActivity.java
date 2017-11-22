package cn.com.amome.amomeshoes.view.main.health.report;

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

import com.github.mikephil.charting.radar.animation.Easing;
import com.github.mikephil.charting.radar.charts.RadarChart;
import com.github.mikephil.charting.radar.components.AxisBase;
import com.github.mikephil.charting.radar.components.Legend;
import com.github.mikephil.charting.radar.components.XAxis;
import com.github.mikephil.charting.radar.components.YAxis;
import com.github.mikephil.charting.radar.data.RadarData;
import com.github.mikephil.charting.radar.data.RadarDataSet;
import com.github.mikephil.charting.radar.data.RadarEntry;
import com.github.mikephil.charting.radar.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.radar.interfaces.datasets.IRadarDataSet;
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
import cn.com.amome.amomeshoes.model.BalanceInfo;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.CopInfo;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.model.LineInfo;
import cn.com.amome.amomeshoes.util.CalDetection;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.widget.CopStandardView;
import cn.com.amome.amomeshoes.widget.CopTrailView;

public class BalanceReportActivity extends Activity implements OnClickListener {
    private String TAG = "BalanceReportActivity";
    private Context mContext;
    private RelativeLayout ll_report_null;
    private LinearLayout ll_report_balance;
    private TextView tv_title, tv_report_balance_date, tv_bo_totaltrail,
            tv_bc_totaltrail, tv_so_totaltrail, tv_sc_totaltrail,
            tv_bo_outerarea, tv_bc_outerarea, tv_so_outerarea, tv_sc_outerarea,
            tv_bo_trailperarea, tv_bc_trailperarea, tv_so_trailperarea,
            tv_sc_trailperarea, tv_bo_trailpersecond, tv_bc_trailpersecond,
            tv_so_trailpersecond, tv_sc_trailpersecond, tv_bo_xoffset,
            tv_bc_xoffset, tv_so_xoffset, tv_sc_xoffset, tv_bo_yoffset,
            tv_bc_yoffset, tv_so_yoffset, tv_sc_yoffset, tv_b_romberg,
            tv_s_romberg, tv_shake_double_open_result,
            tv_shake_double_close_result, tv_shake_single_open_result,
            tv_shake_single_close_result, tv_shake_result, tv_shake_tip,
            tv_no_data_tip;
    private CopTrailView mv_draw_double_open, mv_draw_single_open,
            mv_draw_double_close, mv_draw_single_close;
    private RadarChart mChart;
    private CopStandardView cv_draw_double_open, cv_draw_single_open,
            cv_draw_double_close, cv_draw_single_close;
    private RelativeLayout rl_shake_progress;
    private View view_shake_progress;
    private List<CopInfo> doubleOpenCopList = new ArrayList<CopInfo>();
    private List<CopInfo> singleOpenCopList = new ArrayList<CopInfo>();
    private List<CopInfo> doubleCloseCopList = new ArrayList<CopInfo>();
    private List<CopInfo> singleCloseCopList = new ArrayList<CopInfo>();

    public static float scale;
    private float x_zuobiao1, y_zuobiao1, x_zuobiao2, y_zuobiao2;
    private static final int MSG_GET_SHAKE_INFO = 0;
    private static final int MSG_GET_FOOT_DATA = 1;
    private static final int ONE_KEY_ADD = 2;
    private Gson gson = new Gson();
    private List<BalanceInfo> BalanceList;
    private BalanceInfo BalanceInfo;

    private List<IllnessInfo> balanceInfoList;
    private RecyclerView re_balance;
    private Button btn_add;
    private PromotionAddAdapter promotionAddAdapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_SHAKE_INFO:
                            String str = (String) msg.obj;
                            if (str.equals("[{}]")) {
                                Log.i(TAG, "为空");
                                tv_no_data_tip.setVisibility(View.VISIBLE);
                            } else {
                                ll_report_null.setVisibility(View.GONE);
                                BalanceList = gson.fromJson(str,
                                        new TypeToken<List<BalanceInfo>>() {
                                        }.getType());
                                BalanceInfo = BalanceList.get(0);
                                tv_report_balance_date.setText("报告日期："
                                        + BalanceInfo.create_time);
                                tv_bo_totaltrail.setText(BalanceInfo.bo_totaltrail);
                                tv_bc_totaltrail.setText(BalanceInfo.bc_totaltrail);
                                tv_so_totaltrail.setText(BalanceInfo.so_totaltrail);
                                tv_sc_totaltrail.setText(BalanceInfo.sc_totaltrail);
                                tv_bo_outerarea.setText(BalanceInfo.bo_outerarea);
                                tv_bc_outerarea.setText(BalanceInfo.bc_outerarea);
                                tv_so_outerarea.setText(BalanceInfo.so_outerarea);
                                tv_sc_outerarea.setText(BalanceInfo.sc_outerarea);
                                tv_bo_trailperarea.setText(BalanceInfo.bo_trailperarea);
                                tv_bc_trailperarea.setText(BalanceInfo.bc_trailperarea);
                                tv_so_trailperarea.setText(BalanceInfo.so_trailperarea);
                                tv_sc_trailperarea.setText(BalanceInfo.sc_trailperarea);
                                tv_bo_trailpersecond
                                        .setText(BalanceInfo.bo_trailpersecond);
                                tv_bc_trailpersecond
                                        .setText(BalanceInfo.bc_trailpersecond);
                                tv_so_trailpersecond
                                        .setText(BalanceInfo.so_trailpersecond);
                                tv_sc_trailpersecond
                                        .setText(BalanceInfo.sc_trailpersecond);
                                tv_bo_xoffset.setText(BalanceInfo.bo_xoffset);
                                tv_bc_xoffset.setText(BalanceInfo.bc_xoffset);
                                tv_so_xoffset.setText(BalanceInfo.so_xoffset);
                                tv_sc_xoffset.setText(BalanceInfo.sc_xoffset);
                                tv_bo_yoffset.setText(BalanceInfo.bo_yoffset);
                                tv_bc_yoffset.setText(BalanceInfo.bc_yoffset);
                                tv_so_yoffset.setText(BalanceInfo.so_yoffset);
                                tv_sc_yoffset.setText(BalanceInfo.sc_yoffset);
                                tv_b_romberg.setText(BalanceInfo.bo_romberg);
                                tv_s_romberg.setText(BalanceInfo.so_romberg);
                                tv_shake_result.setText(BalanceInfo.compositeindex);
                                tv_shake_tip.setText(BalanceInfo.compositeindexreport);
                                tv_shake_double_open_result
                                        .setText(BalanceInfo.bo_balanceindex);
                                tv_shake_double_close_result
                                        .setText(BalanceInfo.bc_balanceindex);
                                tv_shake_single_open_result
                                        .setText(BalanceInfo.so_balanceindex);
                                tv_shake_single_close_result
                                        .setText(BalanceInfo.sc_balanceindex);
                                LayoutParams lp_view_shake_progress = (LayoutParams) view_shake_progress
                                        .getLayoutParams();
                                if (BalanceInfo.compositeindex.equals("弱")) {
                                    lp_view_shake_progress.width = rl_shake_progress
                                            .getWidth() / 20;
                                    tv_shake_result.setTextColor(Color
                                            .rgb(255, 169, 67));
                                } else if (BalanceInfo.compositeindex.equals("正常")) {
                                    lp_view_shake_progress.width = rl_shake_progress
                                            .getWidth() / 2;
                                    tv_shake_result.setTextColor(Color
                                            .rgb(255, 141, 74));
                                } else if (BalanceInfo.compositeindex.equals("优秀")) {
                                    lp_view_shake_progress.width = rl_shake_progress
                                            .getWidth() * 19 / 20;
                                    tv_shake_result.setTextColor(Color
                                            .rgb(255, 64, 112));
                                }
                                view_shake_progress
                                        .setLayoutParams(lp_view_shake_progress);

                                if (BalanceInfo.bo_balanceindex.equals("弱")) {
                                    tv_shake_double_open_result.setTextColor(Color.rgb(
                                            255, 169, 67));
                                } else if (BalanceInfo.bo_balanceindex.equals("正常")) {
                                    tv_shake_double_open_result.setTextColor(Color.rgb(
                                            255, 141, 74));
                                } else if (BalanceInfo.bo_balanceindex.equals("优秀")) {
                                    tv_shake_double_open_result.setTextColor(Color.rgb(
                                            255, 64, 112));
                                }

                                if (BalanceInfo.bc_balanceindex.equals("弱")) {
                                    tv_shake_double_close_result.setTextColor(Color
                                            .rgb(255, 169, 67));
                                } else if (BalanceInfo.bc_balanceindex.equals("正常")) {
                                    tv_shake_double_close_result.setTextColor(Color
                                            .rgb(255, 141, 74));
                                } else if (BalanceInfo.bc_balanceindex.equals("优秀")) {
                                    tv_shake_double_close_result.setTextColor(Color
                                            .rgb(255, 64, 112));
                                }

                                if (BalanceInfo.so_balanceindex.equals("弱")) {
                                    tv_shake_single_open_result.setTextColor(Color.rgb(
                                            255, 169, 67));
                                } else if (BalanceInfo.so_balanceindex.equals("正常")) {
                                    tv_shake_single_open_result.setTextColor(Color.rgb(
                                            255, 141, 74));
                                } else if (BalanceInfo.so_balanceindex.equals("优秀")) {
                                    tv_shake_single_open_result.setTextColor(Color.rgb(
                                            255, 64, 112));
                                }

                                if (BalanceInfo.sc_balanceindex.equals("弱")) {
                                    tv_shake_single_close_result.setTextColor(Color
                                            .rgb(255, 169, 67));
                                } else if (BalanceInfo.sc_balanceindex.equals("正常")) {
                                    tv_shake_single_close_result.setTextColor(Color
                                            .rgb(255, 141, 74));
                                } else if (BalanceInfo.sc_balanceindex.equals("优秀")) {
                                    tv_shake_single_close_result.setTextColor(Color
                                            .rgb(255, 64, 112));
                                }

                                if (!BalanceInfo.bo_cop.equals("")) {
                                    String[] bo_copArr = BalanceInfo.bo_cop.split("-");
                                    for (int i = 0; i < bo_copArr.length; i++) {
                                        String[] bo_copArrSon = bo_copArr[i].split(",");
                                        Log.i(TAG, bo_copArrSon[0] + ","
                                                + bo_copArrSon[1]);
                                        CopInfo copInfo = new CopInfo();
                                        copInfo.setX(Float.parseFloat(bo_copArrSon[0]));
                                        copInfo.setY(Float.parseFloat(bo_copArrSon[1]));
                                        doubleOpenCopList.add(copInfo);
                                    }
                                }
                                if (!BalanceInfo.bc_cop.equals("")) {
                                    String[] bc_copArr = BalanceInfo.bc_cop.split("-");
                                    for (int i = 0; i < bc_copArr.length; i++) {
                                        String[] bc_copArrSon = bc_copArr[i].split(",");
                                        CopInfo copInfo = new CopInfo();
                                        copInfo.setX(Float.parseFloat(bc_copArrSon[0]));
                                        copInfo.setY(Float.parseFloat(bc_copArrSon[1]));
                                        doubleCloseCopList.add(copInfo);
                                    }
                                }
                                if (!BalanceInfo.so_cop.equals("")) {
                                    String[] so_copArr = BalanceInfo.so_cop.split("-");
                                    for (int i = 0; i < so_copArr.length; i++) {
                                        String[] so_copArrSon = so_copArr[i].split(",");
                                        CopInfo copInfo = new CopInfo();
                                        copInfo.setX(Float.parseFloat(so_copArrSon[0]));
                                        copInfo.setY(Float.parseFloat(so_copArrSon[1]));
                                        singleOpenCopList.add(copInfo);
                                    }
                                }
                                if (!BalanceInfo.sc_cop.equals("")) {
                                    String[] sc_copArr = BalanceInfo.sc_cop.split("-");
                                    for (int i = 0; i < sc_copArr.length; i++) {
                                        String[] sc_copArrSon = sc_copArr[i].split(",");
                                        CopInfo copInfo = new CopInfo();
                                        copInfo.setX(Float.parseFloat(sc_copArrSon[0]));
                                        copInfo.setY(Float.parseFloat(sc_copArrSon[1]));
                                        singleCloseCopList.add(copInfo);
                                    }
                                }
                                initData();
                                // 53 78 28
                                // 单位时间轨迹长

                                float trailpersecond = (Float
                                        .valueOf(BalanceInfo.bo_trailpersecond)
                                        + Float.valueOf(BalanceInfo.bc_trailpersecond)
                                        + Float.valueOf(BalanceInfo.so_trailpersecond) + Float
                                        .valueOf(BalanceInfo.sc_trailpersecond)) / 4;
                                float xoffset = (Float.valueOf(BalanceInfo.bo_xoffset)
                                        + Float.valueOf(BalanceInfo.bc_xoffset)
                                        + Float.valueOf(BalanceInfo.so_xoffset) + Float
                                        .valueOf(BalanceInfo.sc_xoffset)) / 4;
                                float yoffset = (Float.valueOf(BalanceInfo.bo_yoffset)
                                        + Float.valueOf(BalanceInfo.bc_yoffset)
                                        + Float.valueOf(BalanceInfo.so_yoffset) + Float
                                        .valueOf(BalanceInfo.sc_yoffset)) / 4;
                                float romberg = (Float.valueOf(BalanceInfo.bo_romberg) + Float
                                        .valueOf(BalanceInfo.so_romberg)) / 2;
                                float outerarea = (Float
                                        .valueOf(BalanceInfo.bo_outerarea)
                                        + Float.valueOf(BalanceInfo.bc_outerarea)
                                        + Float.valueOf(BalanceInfo.so_outerarea) + Float
                                        .valueOf(BalanceInfo.sc_outerarea)) / 4;
                                float trailperarea = (Float
                                        .valueOf(BalanceInfo.bo_trailperarea)
                                        + Float.valueOf(BalanceInfo.bc_trailperarea)
                                        + Float.valueOf(BalanceInfo.so_trailperarea) + Float
                                        .valueOf(BalanceInfo.sc_trailperarea)) / 4;
                                float[] radarData = new float[6];
                                radarData = CalDetection.calShakeRadar(trailpersecond,
                                        xoffset, yoffset, romberg, outerarea,
                                        trailperarea, 78, 53, 28);
                                // Log.i(TAG, "radarData[0]=" + radarData[0]);
                                // Log.i(TAG, "radarData[1]=" + radarData[1]);
                                // Log.i(TAG, "radarData[2]=" + radarData[2]);
                                // Log.i(TAG, "radarData[3]=" + radarData[3]);
                                // Log.i(TAG, "radarData[4]=" + radarData[4]);
                                // Log.i(TAG, "radarData[5]=" + radarData[5]);
                                setData(radarData[0], radarData[1], radarData[2],
                                        radarData[3], radarData[4], radarData[5]);
                            }
                            break;
                        case MSG_GET_FOOT_DATA:
                            String string = (String) msg.obj;
                            if (TextUtils.isEmpty(string)) {
                            } else {

                                balanceInfoList = gson.fromJson(string, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                initBalanceData();


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

    private void initBalanceData() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        re_balance.setLayoutManager(layoutManager);
        promotionAddAdapter = new PromotionAddAdapter(mContext, balanceInfoList);
        re_balance.setAdapter(promotionAddAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_balance);
        mContext = this;
        initView();
        getBalanceInfo();
        getPromotionData();
    }

    private void initView() {
        ll_report_null = (RelativeLayout) findViewById(R.id.ll_report_null);
        ll_report_balance = (LinearLayout) findViewById(R.id.ll_report_balance);
        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_report_balance_date = (TextView) findViewById(R.id.tv_report_balance_date);
        tv_bo_totaltrail = (TextView) findViewById(R.id.tv_bo_totaltrail);
        tv_bc_totaltrail = (TextView) findViewById(R.id.tv_bc_totaltrail);
        tv_so_totaltrail = (TextView) findViewById(R.id.tv_so_totaltrail);
        tv_sc_totaltrail = (TextView) findViewById(R.id.tv_sc_totaltrail);
        tv_bo_outerarea = (TextView) findViewById(R.id.tv_bo_outerarea);
        tv_bc_outerarea = (TextView) findViewById(R.id.tv_bc_outerarea);
        tv_so_outerarea = (TextView) findViewById(R.id.tv_so_outerarea);
        tv_sc_outerarea = (TextView) findViewById(R.id.tv_sc_outerarea);
        tv_bo_trailperarea = (TextView) findViewById(R.id.tv_bo_trailperarea);
        tv_bc_trailperarea = (TextView) findViewById(R.id.tv_bc_trailperarea);
        tv_so_trailperarea = (TextView) findViewById(R.id.tv_so_trailperarea);
        tv_sc_trailperarea = (TextView) findViewById(R.id.tv_sc_trailperarea);
        tv_bo_trailpersecond = (TextView) findViewById(R.id.tv_bo_trailpersecond);
        tv_bc_trailpersecond = (TextView) findViewById(R.id.tv_bc_trailpersecond);
        tv_so_trailpersecond = (TextView) findViewById(R.id.tv_so_trailpersecond);
        tv_sc_trailpersecond = (TextView) findViewById(R.id.tv_sc_trailpersecond);
        tv_no_data_tip = (TextView) findViewById(R.id.tv_no_data_tip);
        tv_shake_double_open_result = (TextView) findViewById(R.id.tv_shake_double_open_result);
        tv_shake_double_close_result = (TextView) findViewById(R.id.tv_shake_double_close_result);
        tv_shake_single_open_result = (TextView) findViewById(R.id.tv_shake_single_open_result);
        tv_shake_single_close_result = (TextView) findViewById(R.id.tv_shake_single_close_result);
        tv_shake_result = (TextView) findViewById(R.id.tv_shake_result);
        tv_bo_xoffset = (TextView) findViewById(R.id.tv_bo_xoffset);
        tv_bc_xoffset = (TextView) findViewById(R.id.tv_bc_xoffset);
        tv_so_xoffset = (TextView) findViewById(R.id.tv_so_xoffset);
        tv_sc_xoffset = (TextView) findViewById(R.id.tv_sc_xoffset);
        tv_bo_yoffset = (TextView) findViewById(R.id.tv_bo_yoffset);
        tv_bc_yoffset = (TextView) findViewById(R.id.tv_bc_yoffset);
        tv_so_yoffset = (TextView) findViewById(R.id.tv_so_yoffset);
        tv_sc_yoffset = (TextView) findViewById(R.id.tv_sc_yoffset);
        tv_b_romberg = (TextView) findViewById(R.id.tv_b_romberg);
        tv_s_romberg = (TextView) findViewById(R.id.tv_s_romberg);
        tv_shake_tip = (TextView) findViewById(R.id.tv_shake_tip);
        mChart = (RadarChart) findViewById(R.id.chart1);
        mv_draw_double_open = (CopTrailView) findViewById(R.id.mv_draw_double_open);
        mv_draw_single_open = (CopTrailView) findViewById(R.id.mv_draw_single_open);
        mv_draw_double_close = (CopTrailView) findViewById(R.id.mv_draw_double_close);
        mv_draw_single_close = (CopTrailView) findViewById(R.id.mv_draw_single_close);
        cv_draw_double_open = (CopStandardView) findViewById(R.id.cv_draw_double_open);
        cv_draw_single_open = (CopStandardView) findViewById(R.id.cv_draw_single_open);
        cv_draw_double_close = (CopStandardView) findViewById(R.id.cv_draw_double_close);
        cv_draw_single_close = (CopStandardView) findViewById(R.id.cv_draw_single_close);
        view_shake_progress = findViewById(R.id.view_shake_progress);
        rl_shake_progress = (RelativeLayout) findViewById(R.id.rl_shake_progress);

        tv_title.setText("平衡报告");
        tv_title.setTextColor(mContext.getResources().getColor(
                R.color.turkeygreen));
        findViewById(R.id.rl_left).setOnClickListener(this);

        scale = (float) (((SpfUtil.readScreenHeightPx(mContext) / 2.5) / 3.4) / 70);

        mChart.setAlpha(1);
        mChart.setNoDataText("");
        mChart.setRotationAngle(60); // 设置初始旋转角度
        mChart.setRotationEnabled(false); // 禁止手动旋转
        mChart.setNoDataText("");
        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.argb(0, 255, 255, 255)); // 中心到角的连线颜色
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.argb(100, 100, 100, 100)); // 网线的颜色
        mChart.setWebAlpha(0); // 设置网线为透明

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        // MarkerView mv = new RadarMarkerView(this, R.layout.radar_markerview);
        // mv.setChartView(mChart); // For bounds control
        // mChart.setMarker(mv); // Set the marker to the chart

        setData(0, 0, 0, 0, 0, 0);

        mChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        // xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            // private String[] mActivities = new String[] { "单位时间轨迹长",
            // "X轴平均重心偏移", "Y轴平均重心偏移",
            // "Romberg率", "外围面积", "单位面积轨迹长" };
            private String[] mActivities = new String[]{"", "", "", "", "",
                    ""};

            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK); // 说明文字的颜色

        YAxis yAxis = mChart.getYAxis();
        // yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(4, true);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);
        // 图例
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        // l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK); // 图例颜色
        l.setEnabled(false); // 不显示图例

        re_balance = findViewById(R.id.re_balance);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    private void initData() {
        for (int i = 0; i < doubleOpenCopList.size(); i++) {
            LineInfo lineInfo = new LineInfo();
            x_zuobiao1 = x_zuobiao2;
            y_zuobiao1 = y_zuobiao2;
            x_zuobiao2 = doubleOpenCopList.get(i).getX() * scale;
            y_zuobiao2 = doubleOpenCopList.get(i).getY() * scale;
            if (i > 0) {
                lineInfo.setX1(x_zuobiao1);
                lineInfo.setY1(y_zuobiao1);
                lineInfo.setX2(x_zuobiao2);
                lineInfo.setY2(y_zuobiao2);
                lineInfo.setColor(Color.BLACK);
                mv_draw_double_open.lineList.add(lineInfo);
            }
        }
        x_zuobiao2 = 0;
        y_zuobiao2 = 0;
        for (int i = 0; i < singleOpenCopList.size(); i++) {
            LineInfo lineInfo = new LineInfo();
            x_zuobiao1 = x_zuobiao2;
            y_zuobiao1 = y_zuobiao2;
            x_zuobiao2 = singleOpenCopList.get(i).getX() * scale;
            y_zuobiao2 = singleOpenCopList.get(i).getY() * scale;
            if (i > 0) {
                lineInfo.setX1(x_zuobiao1);
                lineInfo.setY1(y_zuobiao1);
                lineInfo.setX2(x_zuobiao2);
                lineInfo.setY2(y_zuobiao2);
                lineInfo.setColor(Color.BLACK);
                mv_draw_single_open.lineList.add(lineInfo);
            }
        }
        x_zuobiao2 = 0;
        y_zuobiao2 = 0;
        for (int i = 0; i < doubleCloseCopList.size(); i++) {
            LineInfo lineInfo = new LineInfo();
            x_zuobiao1 = x_zuobiao2;
            y_zuobiao1 = y_zuobiao2;
            x_zuobiao2 = doubleCloseCopList.get(i).getX() * scale;
            y_zuobiao2 = doubleCloseCopList.get(i).getY() * scale;
            if (i > 0) {
                lineInfo.setX1(x_zuobiao1);
                lineInfo.setY1(y_zuobiao1);
                lineInfo.setX2(x_zuobiao2);
                lineInfo.setY2(y_zuobiao2);
                lineInfo.setColor(Color.BLACK);
                mv_draw_double_close.lineList.add(lineInfo);
            }
        }
        x_zuobiao2 = 0;
        y_zuobiao2 = 0;
        for (int i = 0; i < singleCloseCopList.size(); i++) {
            LineInfo lineInfo = new LineInfo();
            x_zuobiao1 = x_zuobiao2;
            y_zuobiao1 = y_zuobiao2;
            x_zuobiao2 = singleCloseCopList.get(i).getX() * scale;
            y_zuobiao2 = singleCloseCopList.get(i).getY() * scale;
            if (i > 0) {
                lineInfo.setX1(x_zuobiao1);
                lineInfo.setY1(y_zuobiao1);
                lineInfo.setX2(x_zuobiao2);
                lineInfo.setY2(y_zuobiao2);
                lineInfo.setColor(Color.BLACK);
                mv_draw_single_close.lineList.add(lineInfo);
            }
        }
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
     * 获取摇一摇信息
     */
    private void getBalanceInfo() {
        RequestParams params = new RequestParams();
        params.put("calltype", ClientConstant.GETSHAKEINFO_TYPE);
        params.put("useid", SpfUtil.readUserId(mContext));
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_SHAKE_INFO, params,
                ClientConstant.SHAKE_URL);
    }

    /**
     * 获取平衡管家数据
     */
    private void getPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
        // true, true);
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.BUTLER_ALL);
        params.put("certificate", HttpService.getToken());
        params.put("type", "balance");
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
                ClientConstant.PROMOTION_URL);
    }

    private void oneKeyAdd() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.ADD_MANAGER);
        params.put("certificate", HttpService.getToken());
        params.put("type", "balance");
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
                case MSG_GET_SHAKE_INFO:
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
                        Log.i(TAG, "MSG_GET_SHAKE_INFO解析失败");
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

    public void setData(float steps, float go_time, float sit_time,
                        float sta_time, float total_cal, float stpfqc) {

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        entries1.add(new RadarEntry(steps));
        entries1.add(new RadarEntry(go_time));
        entries1.add(new RadarEntry(sit_time));
        entries1.add(new RadarEntry(sta_time));
        entries1.add(new RadarEntry(total_cal));
        entries1.add(new RadarEntry(stpfqc));

        RadarDataSet set1 = new RadarDataSet(entries1, "区域1");
        set1.setColor(Color.rgb(66, 162, 255)); // 边颜色
        set1.setFillColor(Color.rgb(66, 162, 255)); // 内部填充的颜色
        set1.setDrawFilled(true);
        set1.setFillAlpha(150);
        set1.setLineWidth(0.5f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        // RadarDataSet set2 = new RadarDataSet(entries2, "区域2");
        // set2.setColor(Color.rgb(121, 162, 175));
        // set2.setFillColor(Color.rgb(121, 162, 175));
        // set2.setDrawFilled(true);
        // set2.setFillAlpha(180);
        // set2.setLineWidth(2f);
        // set2.setDrawHighlightCircleEnabled(true);
        // set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        // sets.add(set2);

        RadarData data = new RadarData(sets);
        // data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getPromotionData();
    }
}
