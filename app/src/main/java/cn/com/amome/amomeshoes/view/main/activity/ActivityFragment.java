/**
 * @Title:MotionDataFragment.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data: 2015-12-4 上午8:54:34
 */
package cn.com.amome.amomeshoes.view.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.ActivityillnessAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.promotion.PromotionFootAddActivity;

public class ActivityFragment extends Fragment implements OnClickListener {
    private String TAG = "ActivityFragment";
    private Context mContext;
    private View rootView;
    private TextView tv_title, checkmore_foot, checkmore_posture, checkmore_balance, checkmore_gait;
    private RecyclerView recycle_activity_foot, recycle_activity_posture, recycle_activity_balance, recycle_activity_gait;
    private Gson gson = new Gson();
    private List<IllnessInfo> footInfo, postureInfo, balanceInfo, gaitInfo;
    private ActivityillnessAdapter footadapter, postureadapter, balanceadapter, gaitadapter;
    private static final String GET_TYPE_FOOT = "foot";//获取信息的类型
    private static final String GET_TYPE_POSTURE = "posture";//获取信息的类型
    private static final String GET_TYPE_BALANCE = "balance";//获取信息的类型
    private static final String GET_TYPE_GAIT = "gait";//获取信息的类型
    private static final int MSG_GET_ILLNESS_FOOT = 0;
    private static final int MSG_GET_ILLNESS_POSTURE = 1;
    private static final int MSG_GET_ILLNESS_BALANCE = 2;
    private static final int MSG_GET_ILLNESS_GAIT = 3;
    //String test="空";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ClientConstant.HANDLER_SUCCESS:
                    switch (msg.arg1) {
                        case MSG_GET_ILLNESS_FOOT:
                            String jsonfoot = (String) msg.obj;
                            if (jsonfoot.equals("[{}]")) {

                            } else {
                                //通过gson的TypeToken将字符串转为list
                                footInfo = gson.fromJson(jsonfoot, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                if (footInfo != null) {
                                    initData(MSG_GET_ILLNESS_FOOT);
                                }

                            }
                            break;
                        case MSG_GET_ILLNESS_POSTURE:
                            String jsonposture = (String) msg.obj;
                            if (jsonposture.equals("[{}]")) {

                            } else {
                                postureInfo = gson.fromJson(jsonposture, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                if (postureInfo != null) {
                                    initData(MSG_GET_ILLNESS_POSTURE);
                                }
                            }
                            break;
                        case MSG_GET_ILLNESS_BALANCE:
                            String jsonbalance = (String) msg.obj;
                            if (jsonbalance.equals("[{}]")) {

                            } else {
                                balanceInfo = gson.fromJson(jsonbalance, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                if (balanceInfo != null) {
                                    initData(MSG_GET_ILLNESS_BALANCE);
                                }
                            }
                            break;
                        case MSG_GET_ILLNESS_GAIT:
                            String jsongait = (String) msg.obj;
                            if (jsongait.equals("[{}]")) {

                            } else {
                                gaitInfo = gson.fromJson(jsongait, new TypeToken<List<IllnessInfo>>() {
                                }.getType());
                                if (gaitInfo != null) {
                                    initData(MSG_GET_ILLNESS_GAIT);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        getIllnessInfo(GET_TYPE_FOOT, MSG_GET_ILLNESS_FOOT);
        getIllnessInfo(GET_TYPE_POSTURE, MSG_GET_ILLNESS_POSTURE);
        getIllnessInfo(GET_TYPE_BALANCE, MSG_GET_ILLNESS_BALANCE);
        getIllnessInfo(GET_TYPE_GAIT, MSG_GET_ILLNESS_GAIT);
    }

    /**
     * 将数据放置到recycleView中显示
     */
    private void initData(int code) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        switch (code) {
            case MSG_GET_ILLNESS_FOOT:
                recycle_activity_foot.setLayoutManager(linearLayoutManager);
                footadapter = new ActivityillnessAdapter(mContext, footInfo);
                recycle_activity_foot.setAdapter(footadapter);
                break;
            case MSG_GET_ILLNESS_POSTURE:
                recycle_activity_posture.setLayoutManager(linearLayoutManager);
                postureadapter = new ActivityillnessAdapter(mContext, postureInfo);
                recycle_activity_posture.setAdapter(postureadapter);
                break;
            case MSG_GET_ILLNESS_BALANCE:
                recycle_activity_balance.setLayoutManager(linearLayoutManager);
                balanceadapter = new ActivityillnessAdapter(mContext, balanceInfo);
                recycle_activity_balance.setAdapter(balanceadapter);
                break;
            case MSG_GET_ILLNESS_GAIT:
                recycle_activity_gait.setLayoutManager(linearLayoutManager);
                gaitadapter = new ActivityillnessAdapter(mContext, gaitInfo);
                recycle_activity_gait.setAdapter(gaitadapter);
                break;
            default:
                break;
        }


    }


    //联网请求获取数据
    HttpService.ICallback callback = new HttpService.ICallback() {
        @Override
        public void onHttpPostSuccess(int type, int statusCode, Header[] headers, byte[] responseBody) {
            String result;
            switch (type) {
                case MSG_GET_ILLNESS_FOOT:
                    result = new String(responseBody);
                    try {
                        //Log.e(TAG, "onHttpPostSuccess: ");
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
                        Log.i(TAG, "MSG_GET_ILLNESS_FOOT解析失败");
                    }
                    break;
                case MSG_GET_ILLNESS_POSTURE:
                    result = new String(responseBody);
                    try {
                        //Log.e(TAG, "onHttpPostSuccess: ");
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
                        Log.i(TAG, "MSG_GET_ILLNESS_POSTURE解析失败");
                    }
                case MSG_GET_ILLNESS_BALANCE:
                    result = new String(responseBody);
                    try {
                        //Log.e(TAG, "onHttpPostSuccess: ");
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
                        Log.i(TAG, "MSG_GET_ILLNESS_BALANCE解析失败");
                    }
                case MSG_GET_ILLNESS_GAIT:
                    result = new String(responseBody);
                    try {
                        //Log.e(TAG, "onHttpPostSuccess: ");
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
                        Log.i(TAG, "MSG_GET_ILLNESS_GAIT解析失败");
                    }
                default:
                    break;
            }
        }

        @Override
        public void onHttpPostFailure(int type, int arg0, Header[] arg1, byte[] responseBody, Throwable error) {
            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_activity_main, null, false);
        initView(rootView);
        T.showToast(mContext, "游戏敬请期待", 0);
        getIllnessInfo(GET_TYPE_FOOT, MSG_GET_ILLNESS_FOOT);
        getIllnessInfo(GET_TYPE_POSTURE, MSG_GET_ILLNESS_POSTURE);
        getIllnessInfo(GET_TYPE_BALANCE, MSG_GET_ILLNESS_BALANCE);
        getIllnessInfo(GET_TYPE_GAIT, MSG_GET_ILLNESS_GAIT);
        Log.e(TAG, "onCreateView: ");
        return rootView;
    }

    private void initView(View view) {
        tv_title = (TextView) view.findViewById(R.id.title_tv);
        tv_title.setText("活动");
        view.findViewById(R.id.rl_left).setVisibility(View.GONE);

        checkmore_foot = (TextView) view.findViewById(R.id.checkmore_foot);
        checkmore_posture = (TextView) view.findViewById(R.id.checkmore_posture);
        checkmore_balance = (TextView) view.findViewById(R.id.checkmore_balance);
        checkmore_gait = (TextView) view.findViewById(R.id.checkmore_gait);

        checkmore_foot.setOnClickListener(this);
        checkmore_posture.setOnClickListener(this);
        checkmore_balance.setOnClickListener(this);
        checkmore_gait.setOnClickListener(this);

        recycle_activity_foot = (RecyclerView) view.findViewById(R.id.recycle_activity_foot);
        recycle_activity_posture = (RecyclerView) view.findViewById(R.id.recycle_activity_posture);
        recycle_activity_balance = (RecyclerView) view.findViewById(R.id.recycle_activity_balance);
        recycle_activity_gait = (RecyclerView) view.findViewById(R.id.recycle_activity_gait);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, PromotionFootAddActivity.class);
//        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.checkmore_foot:
               /* bundle.clear();
                if (footInfo!=null) {
                    bundle.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) footInfo);
                }
                bundle.putString("title", "足部");

                intent.putExtras(bundle);*/
                intent.putExtra("type", "foot");
                intent.putExtra("title", "足部");
                startActivity(intent);

                break;
            case R.id.checkmore_posture:
                /*bundle.clear();
                if (postureInfo!=null) {
                    bundle.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) postureInfo);
                }
                bundle.putString("title", "姿态");

                intent.putExtras(bundle);*/
                intent.putExtra("type", "posture");
                intent.putExtra("title", "姿态");
                startActivity(intent);
                break;
            case R.id.checkmore_balance:
                /*bundle.clear();
                if (balanceInfo != null) {
                    bundle.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) balanceInfo);
                }
                bundle.putString("title", "平衡");

                intent.putExtras(bundle);*/
                intent.putExtra("type", "balance");
                intent.putExtra("title", "平衡");
                startActivity(intent);
                break;
            case R.id.checkmore_gait:
                /*bundle.clear();
                if (gaitInfo != null) {
                    bundle.putParcelableArrayList("info", (ArrayList<? extends Parcelable>) gaitInfo);
                }
                bundle.putString("title", "步态");

                intent.putExtras(bundle);*/
                intent.putExtra("type", "gait");
                intent.putExtra("title", "步态");
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //发送请求的部分
    public void getIllnessInfo(String type, int code) {
        Log.e(TAG, "getIllnessInfo: ");
        RequestParams params = new RequestParams();
        params.put("useid", SpfUtil.readUserId(mContext));
        params.put("calltype", ClientConstant.GET_PROMOTION_INFO_TYPE);
        params.put("certificate", HttpService.getToken());
        params.put("type", type);
        PostAsyncTask postTask = new PostAsyncTask(mHandler);
        postTask.startAsyncTask(mContext, callback, code,
                params, ClientConstant.ILLNESSINFO_URL);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i(TAG, TAG + "===onHiddenChanged====");
        } else {
            T.showToast(mContext, "敬请期待", 0);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, TAG + "===setUserVisibleHint====");
    }


}
