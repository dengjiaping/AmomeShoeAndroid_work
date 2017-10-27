package cn.com.amome.amomeshoes.view.main.health.promotion.finish;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.util.DateUtils;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;

public class TrainingFinishActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Context mContext;
    private String TAG = "TrainingFinishActivity";
    private Gson mGson = new Gson();
    private String disease;
    private String type;
    private static final int MSG_GET_DATA = 0;
    private static final int UPLOAD = 1;

    private TextView tv_title, tv_num, tv_action, tv_day, tv_25, tv_50, tv_75, tv_100;
    private SeekBar seekbar;
    private Button btn_bottom;
    private String result = "不习惯";

    private int times, num, day;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_finish);
        mContext = this;
        disease = getIntent().getStringExtra("disease");
        type = getIntent().getStringExtra("type");

        time = getIntent().getLongExtra("time", 0);
        num = getIntent().getIntExtra("group", 0);

        initView();
        getFinishData();
    }


    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_num = findViewById(R.id.tv_num);
        tv_action = findViewById(R.id.tv_action);
        tv_day = findViewById(R.id.tv_day);
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
        seekbar.setOnSeekBarChangeListener(this);
        btn_bottom.setOnClickListener(this);

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

    private void upLoad() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.UPLOAD_NURSING);
        params.put("disease", disease);
        params.put("type", type);
        params.put("switcher", "effect");
        params.put("effect", result);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask();
        postTask.startAsyncTask(mContext, callback, UPLOAD, params,
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
                            //num = Integer.parseInt(msg.getString("done_items"));
                            day = Integer.parseInt(msg.getString("weardays"));
                            times = Integer.parseInt(msg.getString("done_times"));
                            initData();
                        }
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "GET_FINISH解析失败");

                    }
                    break;
                case UPLOAD:
                    result = new String(responseBody);
                    try {

                        JSONObject obj = new JSONObject(result);
                        JSONArray return_msg = obj.getJSONArray("return_msg");
                        JSONObject msg = (JSONObject) return_msg.get(0);

                        int return_code = obj.getInt("return_code");
                        if (return_code == 0) {
                            if (msg.getString("retval").equals("0x00")) {
                                T.showToast(mContext,"上传成功",0);
                                finish();
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
            //DialogUtil.hideProgressDialog();
            Log.e(TAG, "NursingFinishActivity>onHttpPostFailure: 获取训练详细数据失败");

        }


    };

    private void initData() {
        tv_title.setText("您完成了今日第"+times+"次的训练");
        tv_num.setText(DateUtils.changeToM(time)+"分");
        tv_action.setText(num+"组");
        tv_day.setText(day+"天");
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
                upLoad();
                break;

            default:
                break;

        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //TODO:改变字体颜色的方法
        if (0 <= i && i <= 25) {
            result = "不习惯";
        } else if (25 < i && i <= 50) {
            result = "有感觉";
        } else if (50 < i && i <= 75) {
            result = "一般般";
        } else if (75 < i && i <= 100) {
            result = "很不错";
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
