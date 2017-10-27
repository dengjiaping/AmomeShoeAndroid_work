package cn.com.amome.amomeshoes.view.main.health.promotion;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;

public class PromotionFootAddActivity extends Activity implements
        OnClickListener {
    private String TAG = "PromotionFootAddActivity";
    private Context mContext;
    private RecyclerView recycler_promotion_add;
    private TextView tv_title;
    private PromotionAddAdapter promotionAddAdapter;
    private static final int MSG_GET_FOOT_DATA = 0;
    private Gson gson = new Gson();
    private String title = null;
    private List<IllnessInfo> mInfoList = null;
    private String type = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_add);
        mContext = this;
/*
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            title = (String) bundle.get("title");
            mInfoList = (List<IllnessInfo>) bundle.get("info");

        }*/

      /*  if (mInfoList != null) {

            initData();
        }*/

        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        initView();

        getPromotionData();
    }

    private void initData() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recycler_promotion_add.setLayoutManager(layoutManager);
        promotionAddAdapter = new PromotionAddAdapter(mContext, mInfoList);
        recycler_promotion_add.setAdapter(promotionAddAdapter);
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.title_tv);
        recycler_promotion_add = (RecyclerView) findViewById(R.id.recycler_promotion_add);
        tv_title.setText(title);
        findViewById(R.id.rl_left).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;
        }
    }


    /**
     * 获取脚长数据
     */
    private void getPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
        // true, true);
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_PROMOTION_INFO_TYPE);
        params.put("certificate", HttpService.getToken());
        params.put("type", type);
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
                ClientConstant.PROMOTION_URL);
    }

    HttpService.ICallback callback = new HttpService.ICallback() {


        @Override
        public void onHttpPostSuccess(int type, int statusCode,
                                      Header[] headers, byte[] responseBody) {
            // TODO Auto-generated method stub
            String result;
            switch (type) {
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

                default:
                    break;
            }

        }

        @Override
        public void onHttpPostFailure(int type, int statusCode, Header[] arg1,
                                      byte[] responseBody, Throwable error) {
            // TODO Auto-generated method stub
            DialogUtil.hideProgressDialog();
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_FOOT_DATA:
                            String str = (String) msg.obj;
                            if (TextUtils.isEmpty(str)) {
                            } else {

                                mInfoList = gson.fromJson(str, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
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


    @Override
    protected void onRestart() {
        super.onRestart();
        getPromotionData();
    }
}
