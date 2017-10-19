package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import cn.com.amome.amomeshoes.adapter.DetailNursingTrueAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.NursingTrueInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;

public class DetailNursingTrueActivity extends Activity implements View.OnClickListener, DetailNursingTrueAdapter.MyItemClickListener {
    private String disease = null;
    private String type = null;
    private Context mContext;
    private static final String TAG = "DetailNursingTrueActivity";
    private static final int MSG_GET_DATA = 0;

    private ImageView iv_left;
    private RecyclerView rv_nursing_true;
    private FrameLayout fl_red, fl_blue;
    private TextView tv_red, tv_blue, tv_bottom;
    private List<NursingTrueInfo> infoList;
    private DetailNursingTrueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nursing_true);
        mContext = this;
        Intent intent = getIntent();
        disease = intent.getStringExtra("disease");
        type = intent.getStringExtra("type");
        initView();
        getNursingTureData();
    }


    private void initView() {
        iv_left = findViewById(R.id.iv_left);
        rv_nursing_true = findViewById(R.id.rv_nursing_true);
        fl_red = findViewById(R.id.fl_red);
        fl_blue = findViewById(R.id.fl_blue);
        tv_red = findViewById(R.id.tv_red);
        tv_blue = findViewById(R.id.tv_blue);
        tv_bottom = findViewById(R.id.tv_bottom);

        iv_left.setOnClickListener(this);
        fl_red.setOnClickListener(this);
        fl_blue.setOnClickListener(this);
    }

    private void getNursingTureData() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_DETAIL_DATA);
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
            //DialogUtil.hideProgressDialog();
            Log.e(TAG, "DetailNursingTrueActivity>onHttpPostFailure: 获取训练详细数据失败");

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
                                Type type = new TypeToken<List<NursingTrueInfo>>() {
                                }.getType();
                                Gson gson = new Gson();
                                infoList = gson.fromJson(str, type);
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_nursing_true.setLayoutManager(layoutManager);
        adapter = new DetailNursingTrueAdapter(mContext, infoList, infoList.size());
        rv_nursing_true.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.fl_red:
                break;
            case R.id.fl_blue:
                int num = 0;
                for (int i = 0; i < infoList.size(); i++) {
                    if (infoList.get(i).isChecked()) {
                        num++;
                    }
                }
                if (num == 0) {
                    T.showToast(mContext, "请至少选择一项！", 0);
                } else {
                    //TODO:页面的跳转
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(View view, int postion) {

        if (postion < infoList.size()) {

            if (infoList.get(postion).isChecked()) {
                infoList.get(postion).setChecked(false);
            } else {
                infoList.get(postion).setChecked(true);
            }
            if (infoList.get(postion).isChecked()) {
                infoList.get(postion).setIs_done("1");
            } else {
                infoList.get(postion).setIs_done("0");
            }
            //adapter.reLoading(infoList);
            adapter.notifyDataSetChanged();

        }


    }
}
