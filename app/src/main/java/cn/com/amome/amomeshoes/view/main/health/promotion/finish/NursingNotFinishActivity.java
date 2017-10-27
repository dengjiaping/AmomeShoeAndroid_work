package cn.com.amome.amomeshoes.view.main.health.promotion.finish;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.SpfUtil;

public class NursingNotFinishActivity extends Activity {
    private Context mContext;
    private String TAG = "NursingNotFinishActivity";
    private int day;
    private int num;
    private Button button;
    private String disease;
    private String type;
    private static final int MSG_GET_DATA = 0;

    private TextView tv_main, tv_num, tv_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_not_finish);
        mContext = this;
        //day = getIntent().getStringExtra("day");
        disease = getIntent().getStringExtra("disease");
        type = getIntent().getStringExtra("type");
        button = (Button) findViewById(R.id.button);
        tv_main = findViewById(R.id.tv_main);
        tv_num = findViewById(R.id.tv_num);
        tv_bottom = findViewById(R.id.tv_bottom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFinishData();
    }


    private void getFinishData() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_FINISH);
        params.put("disease", disease);
        params.put("type", type);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask();
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
                        JSONArray return_msg = obj.getJSONArray("return_msg");
                        int return_code = obj.getInt("return_code");
                        if (return_code == 0) {
                            JSONObject msg = return_msg.getJSONObject(0);
                            num = Integer.valueOf(msg.getString("done_items"));
                            day = Integer.valueOf(msg.getString("weardays"));
                            initData();
                        }
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "GET_FINISH解析失败");

                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onHttpPostFailure(int type, int statusCode, Header[] arg1,
                                      byte[] responseBody, Throwable error) {

        }


    };

    private void initData() {
        if (type.equals("nursing")) {
            tv_main.setText("没有进行养护！");
            tv_bottom.setText("日常养护是一个长期过程，需要坚持呢。\t明天记得进行日常养护哦！");
            tv_num.setText("已养护" + num + "天");
        } else {
            tv_main.setText("没有穿戴健康配件！");
            tv_bottom.setText("穿戴配件是一个长期过程，需要坚持呢。\t明天记得进行穿戴配件哦！");
            tv_num.setText("已穿戴" + num + "天");
        }
    }
}
