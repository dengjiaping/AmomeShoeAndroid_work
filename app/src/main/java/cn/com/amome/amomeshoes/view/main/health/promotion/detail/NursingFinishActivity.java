package cn.com.amome.amomeshoes.view.main.health.promotion.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
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

public class NursingFinishActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Context mContext;
    private static final String TAG = "NursingFinishActivity";
    private String disease;
    private String type;
    private static final int MSG_GET_DATA = 0;

    private TextView tv_nursing_num, tv_nursing_day, tv_25, tv_50, tv_75, tv_100;
    private SeekBar seekbar;
    private Button btn_bottom;
    private int num, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_finish);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        type = getIntent().getStringExtra("type");
        initView();
        getFinishData();
    }


    private void initView() {
        tv_nursing_num = findViewById(R.id.tv_nursing_num);
        tv_nursing_day = findViewById(R.id.tv_nursing_day);
        tv_25 = findViewById(R.id.tv_25);
        tv_50 = findViewById(R.id.tv_50);
        tv_75 = findViewById(R.id.tv_75);
        tv_100 = findViewById(R.id.tv_100);
        seekbar = findViewById(R.id.seekbar);
        btn_bottom = findViewById(R.id.btn_bottom);

        tv_25.setOnClickListener(this);
        tv_50.setOnClickListener(this);
        tv_75.setOnClickListener(this);
        tv_100.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(this);
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
                            num=Integer.valueOf(msg.getString("done_items"));
                            day=Integer.valueOf(msg.getString("weardays"));
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
            // TODO Auto-generated method stub
            //DialogUtil.hideProgressDialog();
            Log.e(TAG, "NursingFinishActivity>onHttpPostFailure: 获取训练详细数据失败");

        }


    };

    private void initData() {
        tv_nursing_num.setText(num+"项");
        tv_nursing_day.setText(day+"天");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_25:
                seekbar.setProgress(25);
                break;
            case R.id.tv_50:
                seekbar.setProgress(50);
                break;
            case R.id.tv_75:
                seekbar.setProgress(75);
                break;
            case R.id.tv_100:
                seekbar.setProgress(100);
                break;
            case R.id.btn_bottom:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //TODO:改变字体颜色的方法
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
