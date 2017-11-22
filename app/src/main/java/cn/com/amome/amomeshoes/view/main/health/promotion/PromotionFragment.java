package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.HealthPromotionAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.PromotionDataInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;

/**
 * @Title:PromotionFragment.java
 * @Description:健康促进界面
 * @author:jyw
 */
public class PromotionFragment extends Fragment implements OnClickListener {
    private String TAG = "PromotionFragment";
    private Context mContext;
    private RecyclerView re_promotion_foot, re_promotion_posture, re_promotion_balance, re_promotion_gait;
    private static final int MSG_GET_FOOT_DEATIL_DATA = 0;
    private Gson gson = new Gson();
    private List<PromotionDataInfo> footInfo, postrueInfo, balanceInfo, gaitInfo;
    private HealthPromotionAdapter footAdapter, postureAdapter, balanceAdapter, gaitAdapter;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_main, null, false);
        mContext = getActivity();
        initView(view);
        getAllInfo();
        return view;
    }

    private void initView(View view) {
        re_promotion_foot = view.findViewById(R.id.re_promotion_foot);
        re_promotion_posture = view.findViewById(R.id.re_promotion_posture);
        re_promotion_balance = view.findViewById(R.id.re_promotion_balance);
        re_promotion_gait = view.findViewById(R.id.re_promotion_gait);
    }

    //获取全部健康促进数据
    private void getAllInfo() {
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_PROMOTION_DETAIL_FINISH);
        params.put("certificate", HttpService.getToken());
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DEATIL_DATA,
                params, ClientConstant.PROMOTION_URL);

    }

    HttpService.ICallback callback = new HttpService.ICallback() {

        @Override
        public void onHttpPostSuccess(int type, int statusCode,
                                      Header[] headers, byte[] responseBody) {
            // TODO Auto-generated method stub
            String result;
            switch (type) {
                case MSG_GET_FOOT_DEATIL_DATA:
                    result = new String(responseBody);
                    try {
                        JSONObject obj = new JSONObject(result);
                        String return_msg = obj.getString("return_msg");
                        JSONArray msgInfo = obj.getJSONArray("return_msg");
                        int return_code = obj.getInt("return_code");
                        Message msg = Message.obtain();
                        if (return_code == 0 && HttpError.judgeError(return_msg, ClassType.PayActivity)) {
                            msg.what = ClientConstant.HANDLER_SUCCESS;
                            msg.arg1 = type;
                            msg.obj = msgInfo;
                        }
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        Log.i(TAG, "MSG_GET_FOOT_DEATIL_DATA解析失败");

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
                        case MSG_GET_FOOT_DEATIL_DATA:
                            //String str = (String) msg.obj;

                            if (msg.obj == null) {
                            } else {
                                JSONArray info = (JSONArray) msg.obj;
                                try {
                                    JSONObject all = (JSONObject) info.get(0);
                                    String foot = all.getString("foot");
                                    String posture = all.getString("posture");
                                    String balance = all.getString("balance");
                                    String gait = all.getString("gait");
                                    footInfo = gson.fromJson(foot, new TypeToken<List<PromotionDataInfo>>() {
                                    }.getType());
                                    postrueInfo = gson.fromJson(posture, new TypeToken<List<PromotionDataInfo>>() {
                                    }.getType());
                                    balanceInfo = gson.fromJson(balance, new TypeToken<List<PromotionDataInfo>>() {
                                    }.getType());
                                    gaitInfo = gson.fromJson(gait, new TypeToken<List<PromotionDataInfo>>() {
                                    }.getType());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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


    };

    private void initData() {
        if (footInfo.get(0).getType() == null) {
            footInfo.clear();
        }
        if (postrueInfo.get(0).getType() == null) {
            postrueInfo.clear();
        }
        if (balanceInfo.get(0).getType() == null) {
            balanceInfo.clear();
        }
        if (gaitInfo.get(0).getType() == null) {
            gaitInfo.clear();
        }


        StaggeredGridLayoutManager layoutManagerFoot = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManagerPosture = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManagerBalance = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManagerGait = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        re_promotion_foot.setLayoutManager(layoutManagerFoot);
        re_promotion_posture.setLayoutManager(layoutManagerPosture);
        re_promotion_balance.setLayoutManager(layoutManagerBalance);
        re_promotion_gait.setLayoutManager(layoutManagerGait);

        footInfo.add(new PromotionDataInfo());
        footAdapter = new HealthPromotionAdapter(mContext, footInfo, "足部", "foot");
        re_promotion_foot.setAdapter(footAdapter);


        postrueInfo.add(new PromotionDataInfo());
        postureAdapter = new HealthPromotionAdapter(mContext, postrueInfo, "姿态", "posture");
        re_promotion_posture.setAdapter(postureAdapter);


        balanceInfo.add(new PromotionDataInfo());
        balanceAdapter = new HealthPromotionAdapter(mContext, balanceInfo, "平衡", "balance");
        re_promotion_balance.setAdapter(balanceAdapter);


        gaitInfo.add(new PromotionDataInfo());
        gaitAdapter = new HealthPromotionAdapter(mContext, gaitInfo, "步态", "gait");
        re_promotion_gait.setAdapter(gaitAdapter);


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // case R.id.tv_foot_add:
            // startActivity(new Intent(mContext, PromotionFootAddActivity.class));
            // break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        Log.i(TAG, TAG + "===onHiddenChanged====");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getAllInfo();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

}
