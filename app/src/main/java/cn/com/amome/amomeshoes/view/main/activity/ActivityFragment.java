/**
 * @Title:MotionDataFragment.java
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-4 上午8:54:34
 */
package cn.com.amome.amomeshoes.view.main.activity;

import android.content.Context;
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

public class ActivityFragment extends Fragment implements OnClickListener {
	private String TAG = "ActivityFragment";
	private Context mContext;
	private View rootView;
	private TextView tv_title;
	private RecyclerView recycle_activity_foot;
	private Gson gson = new Gson();
	private List<IllnessInfo> footInfo;
	private ActivityillnessAdapter footadapter;
	private static final String GET_TYPE_FOOT="foot";//获取信息的类型
	private static final int MSG_GET_ILLNESS_FOOT=0;//
	String test="空";

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case ClientConstant.HANDLER_SUCCESS:
					switch (msg.arg1) {
						case MSG_GET_ILLNESS_FOOT:
							String json=(String)msg.obj;
							if (json.equals("[{}]")) {

							} else {
								//通过gson的TypeToken将字符串转为list
								footInfo = gson.fromJson(json,new TypeToken<List<IllnessInfo>>(){}.getType());
								Log.e(TAG, footInfo.toString());
								initData();
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

	/**
	 * 将数据放置到recycleView中显示
	 */
	private void initData() {
		/*footadapter=new ActivityillnessAdapter(mContext,footInfo);
		gv_activity_foot.setAdapter(footadapter);*/
		//recycleview使用时需要设置LinearLayoutManager
		LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		//gv_activity_foot.addItemDecoration(new  SpaceItemDecoration(30));
		recycle_activity_foot.setLayoutManager(linearLayoutManager);
		footadapter=new ActivityillnessAdapter(mContext,footInfo);
		recycle_activity_foot.setAdapter(footadapter);
	}


	//联网请求获取数据
	HttpService.ICallback callback=new HttpService.ICallback() {
		@Override
		public void onHttpPostSuccess(int type, int statusCode, Header[] headers, byte[] responseBody) {
			String result;
			switch(type){
				case MSG_GET_ILLNESS_FOOT:
					result = new String(responseBody);
					try {
						//Log.e(TAG, "onHttpPostSuccess: ");
						JSONObject obj = new JSONObject(result);
						String return_msg = obj.getString("return_msg");
						Log.e(TAG, return_msg);
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
		getIllnessInfo(GET_TYPE_FOOT);
		Log.e(TAG, "onCreateView: ");
		Toast.makeText(mContext, test, Toast.LENGTH_SHORT).show();
		return rootView;
	}

	private void initView(View view) {
		tv_title = (TextView) view.findViewById(R.id.title_tv);
		tv_title.setText("活动");
		view.findViewById(R.id.rl_left).setVisibility(View.GONE);
		recycle_activity_foot = (RecyclerView) view.findViewById(R.id.recycle_activity_foot);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

//发送请求的部分
	public void getIllnessInfo(String type){
		Log.e(TAG, "getIllnessInfo: ");
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_TOKEN_TYPE);
		params.put("certificate", HttpService.getToken());
		params.put("type", type);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_ILLNESS_FOOT,
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
