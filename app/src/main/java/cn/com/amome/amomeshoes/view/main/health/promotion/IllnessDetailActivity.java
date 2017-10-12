package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.IllnessDetailInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;

public class IllnessDetailActivity extends Activity implements View.OnClickListener {
    private static final int MSG_GET_DATA = 0;
    private String TAG = "IllnessDetailActivity";
    private Context mContext;
    private ImageView iv_illnessdetail_first, iv_left, iv_describe_detail, iv_training_detail,
            iv_fitting_detail, iv_nursing_detail;
    private TextView tv_detail_illness_name, tv_describe, tv_num_training, tv_name_training,
            tv_detail_training, tv_num_fitting, tv_name_fitting, tv_detail_fitting, tv_num_nursing,
            tv_name_nursing, tv_detail_nursing;
    private LinearLayout ll_training, ll_fitting, ll_nursing;
    private Button btn_onekey_add;

    private Gson gson = new Gson();
    private String disease = null;


    private IllnessDetailInfo info;
    private String mIllnessname;
    private String mDefinition;
    private String mReason;
    private String mInfluence;
    private String mIcon;
    private List<IllnessDetailInfo.HealthschemeBean> mHealthscheme;
    private IllnessDetailInfo.HealthschemeBean mHealthschemeBean;
    private List<IllnessDetailInfo.HealthschemeBean.NursingBean> mNursing;
    private IllnessDetailInfo.HealthschemeBean.NursingBean mNursingBean;
    private List<IllnessDetailInfo.HealthschemeBean.TrainingBean> mTraining;
    private IllnessDetailInfo.HealthschemeBean.TrainingBean mTrainingBean;
    private String mNum_training;
    private String mName_training;
    private String mDetail_training;
    private String mNum_nursing;
    private String mName_nursing;
    private String mDetail_nursing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illness_detail);
        mContext = this;
        initView();
        disease = getIntent().getStringExtra("name");
        getIllnessDetailInfo();
    }


    private void initView() {
        iv_illnessdetail_first = (ImageView) findViewById(R.id.iv_illnessdetail_first);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_describe_detail = (ImageView) findViewById(R.id.iv_describe_detail);
        iv_training_detail = (ImageView) findViewById(R.id.iv_training_detail);
        iv_fitting_detail = (ImageView) findViewById(R.id.iv_fitting_detail);
        iv_nursing_detail = (ImageView) findViewById(R.id.iv_nursing_detail);
        tv_detail_illness_name = (TextView) findViewById(R.id.tv_detail_illness_name);
        tv_describe = (TextView) findViewById(R.id.tv_describe);
        tv_num_training = (TextView) findViewById(R.id.tv_num_training);
        tv_name_training = (TextView) findViewById(R.id.tv_name_training);
        tv_detail_training = (TextView) findViewById(R.id.tv_detail_training);
        tv_num_fitting = (TextView) findViewById(R.id.tv_num_fitting);
        tv_name_fitting = (TextView) findViewById(R.id.tv_name_fitting);
        tv_detail_fitting = (TextView) findViewById(R.id.tv_detail_fitting);
        tv_num_nursing = (TextView) findViewById(R.id.tv_num_nursing);
        tv_name_nursing = (TextView) findViewById(R.id.tv_name_nursing);
        tv_detail_nursing = (TextView) findViewById(R.id.tv_detail_nursing);
        ll_training = (LinearLayout) findViewById(R.id.ll_training);
        ll_fitting = (LinearLayout) findViewById(R.id.ll_fitting);
        ll_nursing = (LinearLayout) findViewById(R.id.ll_nursing);
        btn_onekey_add = (Button) findViewById(R.id.btn_onekey_add);

        iv_left.setOnClickListener(this);
        iv_describe_detail.setOnClickListener(this);
        iv_training_detail.setOnClickListener(this);
        iv_fitting_detail.setOnClickListener(this);
        iv_nursing_detail.setOnClickListener(this);
    }

    /**
     * 获取足部详细数据
     */
    private void getIllnessDetailInfo() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_PROMOTION_DETAIL_INFO_TYPE);
        params.put("disease", disease);
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
            Log.e(TAG, "IllnessDetailActivity>onHttpPostFailure: 获取疾病详细数据失败");

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
                                Type type = new TypeToken<List<IllnessDetailInfo>>() {
                                }.getType();
                                List<IllnessDetailInfo> list;
                                list = gson.fromJson(str, type);
                                info = list.get(0);
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

        ;
    };

    private void initData() {
        mIllnessname = info.getType();
        mDefinition = info.getDefinition();
        mReason = info.getReason();
        mInfluence = info.getInfluence();
        mIcon = info.getIcon();

        mHealthscheme = info.getHealthscheme();
        mHealthschemeBean = mHealthscheme.get(0);
        mNursing = mHealthschemeBean.getNursing();
        mNursingBean = mNursing.get(0);
        mTraining = mHealthschemeBean.getTraining();
        mTrainingBean = mTraining.get(0);

        mNum_training = mTrainingBean.getNum();
        mName_training = mTrainingBean.getName();
        mDetail_training = mTrainingBean.getDetail();
        mNum_nursing = mNursingBean.getNum();
        mName_nursing = mNursingBean.getName();
        mDetail_nursing = mNursingBean.getDetail();


        Picasso.with(mContext).load(info.getIcon())
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(iv_illnessdetail_first);
        tv_detail_illness_name.setText(mIllnessname);
        tv_describe.setText(mDefinition);
        //这里先设置健康配件部分不显示
        //TODO:暂时没有数据，等待有数据了再添加
        ll_fitting.setVisibility(View.GONE);

        tv_num_training.setText(mNum_training);
        tv_name_training.setText(mName_training);
        tv_detail_training.setText(mDetail_training);
        tv_num_nursing.setText(mNum_nursing);
        tv_name_nursing.setText(mName_nursing);
        tv_detail_nursing.setText(mDetail_nursing);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_describe_detail:
                Intent intent_describe = new Intent(mContext, DetailDescribeActivity.class);
                intent_describe.putExtra("definition", mDefinition);
                intent_describe.putExtra("reason", mReason);
                intent_describe.putExtra("influence", mInfluence);
                startActivity(intent_describe);
                break;
            case R.id.iv_training_detail:
                Intent intent_training = new Intent(mContext, DetailTrainingActivity.class);
                break;
            case R.id.iv_fitting_detail:
                //TODO: 有配件的信息后显示配件的ll模块并完成点击事件的处理
                break;
            case R.id.iv_nursing_detail:
                break;
            default:
                break;
        }
    }
}
