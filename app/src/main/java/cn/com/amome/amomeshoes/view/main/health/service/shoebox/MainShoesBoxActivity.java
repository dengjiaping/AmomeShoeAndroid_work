package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.MainShoeAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.UserShoesCategoryInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.charts.ShoeChartMainFragmentActivity;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainShoesBoxActivity extends Activity implements OnClickListener {
	private String TAG = "MainShoesBoxActivity";
	private TextView tv_title, tv_left;
	private ImageView iv_back, iv_right;
	private Context mContext;
	private GridView gridView;
	private Button btn_add;
	private TextView tv_shoenum;
	private int shoeNum;
	private List<Integer> imgList = new ArrayList<Integer>();
	private List<Integer> imgList_se = new ArrayList<Integer>();
	private List<Integer> shoeCount = new ArrayList<Integer>();
	private MainShoeAdapter adapter;
	private Gson gson = new Gson();
	private List<UserShoesCategoryInfo> userShoesCategoryList;
	private UserShoesCategoryInfo userShoesCategoryInfo;
	private static final int MSG_GET_CATEGORY = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_CATEGORY:
					String categoryJson = (String) msg.obj;
					if (categoryJson.equals("[{}]")) {
						tv_shoenum.setText("共0双");
					} else {
						userShoesCategoryList = gson.fromJson(categoryJson,
								new TypeToken<List<UserShoesCategoryInfo>>() {
								}.getType());
						shoeCount = null;
						shoeCount = new ArrayList<Integer>();
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("运动鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("帆布鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("休闲鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("皮鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("高跟鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type
									.equals("浅口蜜鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("凉鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("棉鞋")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							if (userShoesCategoryList.get(i).type.equals("靴子")) {
								shoeCount
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
								shoeNum = shoeNum
										+ Integer.valueOf(userShoesCategoryList
												.get(i).count);
							}
						}
						tv_shoenum.setText("共" + shoeNum + "双");
						shoeNum = 0;
					}

					adapter = new MainShoeAdapter(mContext, imgList, shoeCount,
							imgList_se);
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long arg3) {
							L.i("", position + "");
							adapter.setSelection(position);
							adapter.notifyDataSetChanged();
							Intent intent = new Intent(mContext,
									ShoesBoxListActivity.class);
							intent.putExtra("type", position);
							startActivity(intent);
						}
					});
					break;
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoebox_main);
		mContext = this;
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_back = (ImageView) findViewById(R.id.iv_left);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		tv_title.setText("我的鞋");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		tv_left.setText("服务");
		tv_left.setVisibility(View.INVISIBLE);
		iv_right.setImageResource(R.drawable.piechart_more);
		iv_right.setVisibility(View.VISIBLE);
		iv_right.setOnClickListener(this);
		findViewById(R.id.rl_left).setOnClickListener(this);

		btn_add = (Button) findViewById(R.id.btn_add);
		tv_shoenum = (TextView) findViewById(R.id.tv_shoenum);
		gridView = (GridView) findViewById(R.id.gridView);
		btn_add.setOnClickListener(this);
		// tv_shoenum.setText("45双");
		imgList.add(R.drawable.shoe_sport);
		imgList.add(R.drawable.shoe_canvas);
		imgList.add(R.drawable.shoe_casual);
		imgList.add(R.drawable.shoe_leather);
		imgList.add(R.drawable.shoe_highheel);
		imgList.add(R.drawable.shoe_pumps);
		imgList.add(R.drawable.shoe_sandals);
		imgList.add(R.drawable.shoe_catton);
		imgList.add(R.drawable.shoe_boots);

		imgList_se.add(R.drawable.shoe_sport_s);
		imgList_se.add(R.drawable.shoe_canvas_s);
		imgList_se.add(R.drawable.shoe_casual_s);
		imgList_se.add(R.drawable.shoe_leather_s);
		imgList_se.add(R.drawable.shoe_highheel_s);
		imgList_se.add(R.drawable.shoe_pumps_s);
		imgList_se.add(R.drawable.shoe_sandals_s);
		imgList_se.add(R.drawable.shoe_catton_s);
		imgList_se.add(R.drawable.shoe_boots_s);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.iv_right:
			startActivity(new Intent(mContext,
					ShoeChartMainFragmentActivity.class));
			break;
		case R.id.btn_add:
			startActivity(new Intent(mContext, AddShoesBoxActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		Log.i(TAG, "=====onResume=====");
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
		getShoesCategoryList();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "=====onPause=====");
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "=====onStop=====");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "=====onDestroy=====");
	}

	/**
	 * 获取鞋跟高
	 */
	private void getShoesCategoryList() {
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOECATEGORY_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CATEGORY, params,
				ClientConstant.SHOEBOX_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_CATEGORY:
				DialogUtil.hideProgressDialog();
				result = new String(responseBody);
				try {
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
					Log.i(TAG, "MSG_GET_CATEGORY解析失败");
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

		}
	};

}
