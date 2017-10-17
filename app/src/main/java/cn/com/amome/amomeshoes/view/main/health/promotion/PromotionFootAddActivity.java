package cn.com.amome.amomeshoes.view.main.health.promotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.PromotionAddAdapter;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.model.PromotionInfo;

public class PromotionFootAddActivity extends Activity implements
        OnClickListener {
    private String TAG = "PromotionFootAddActivity";
    private Context mContext;
    private RecyclerView recycler_promotion_add;
    private TextView tv_title;
    private PromotionAddAdapter promotionAddAdapter;
    private static final int MSG_GET_FOOT_DATA = 0;
    private Gson gson = new Gson();
    private List<PromotionInfo> footPromotionList;
    private String title = null;
    private List<IllnessInfo> mInfoList = null;

	/*private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_FOOT_DATA:
					String str = (String) msg.obj;
					if (TextUtils.isEmpty(str)) {
					} else {
						footPromotionList = gson.fromJson(str,
								new TypeToken<List<PromotionInfo>>() {
								}.getType());
						if (footPromotionList != null
								&& footPromotionList.size() > 0) {
							promotionAddAdapter = new PromotionAddAdapter(
									mContext, footPromotionList);
							gv_foot_all.setAdapter(promotionAddAdapter);
						}
					}
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		};
	};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_add);
        mContext = this;
        initView();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            title = (String) bundle.get("title");
            mInfoList = (List<IllnessInfo>) bundle.get("info");
        }
        if (mInfoList != null) {

            initData();
        }

        //getFootPromotionData();
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


       /* recycler_promotion_add.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                T.showToast(mContext, "点的是"
                        + footPromotionList.get(position).type, 0);
            }
        });*/


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

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    //promotionAddAdapter.changeSelectStatus(data.getBooleanExtra("isSelect", false), data.getIntExtra("position", 0));
                    //promotionAddAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }*/

    /**
     * 获取脚长数据
     */
    /*private void getFootPromotionData() {
        // DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
		// true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_PROMOTION_INFO_TYPE);
		params.put("addtype", "foot");
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DATA, params,
				ClientConstant.PROMOTION_URL);
	}*/

	/*HttpService.ICallback callback = new HttpService.ICallback() {

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
	};*/
}
