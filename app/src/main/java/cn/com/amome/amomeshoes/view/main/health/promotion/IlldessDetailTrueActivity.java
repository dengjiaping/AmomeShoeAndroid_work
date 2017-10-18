package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

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

public class IlldessDetailTrueActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private static final String TAG = "IlldessDetailTrueActivity";
    private Gson mGson = new Gson();
    private static final int MSG_GET_DATA = 0;
    private String disease = null;

    private ImageView iv_first, iv_left, iv_right, iv_training, iv_training_enter, iv_fitting, iv_fitting_enter, iv_nursing, iv_nursing_enter;
    private TextView tv_name, tv_training_name, tv_training_num, tv_fitting_name, tv_fitting_num, tv_nursing_name, tv_nursing_num;
    private LinearLayout ll_training, ll_fitting, ll_nursing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illdess_detail_true);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        initView();
        getIllnessDetailInfo();
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
            Log.e(TAG, "IllnessDetailTrueActivity>onHttpPostFailure: 获取疾病完成数据失败");
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
                                list = mGson.fromJson(str, type);
                                //info = list.get(0);
                                //initData();
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


    @Override
    public void onClick(View view) {

    }
}
