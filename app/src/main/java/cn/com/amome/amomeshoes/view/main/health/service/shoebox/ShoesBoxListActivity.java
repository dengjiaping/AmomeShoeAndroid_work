package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.ShoesBoxAdapter;
import cn.com.amome.amomeshoes.events.ExerciseRadarEvent;
import cn.com.amome.amomeshoes.events.ShoeDelEvent;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.ShoeBoxList;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.util.Util;
import cn.com.amome.amomeshoes.view.main.bind.PrepareScanActivity;
import cn.com.amome.shoeencrypt.AmomeEncrypt;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class ShoesBoxListActivity extends Activity implements OnClickListener {

	private String TAG = "ShoesBoxActivity";
	private TextView tv_title, tv_left;
	@SuppressWarnings("unused")
	private ImageView iv_back;
	private Context mContext;
	private int typeInt = -1;
	private SwipeMenuListView lv_shoes;
	private ShoesBoxAdapter sportAdapter;
	private Gson gson = new Gson();
	private List<ShoeBoxList> ShoeList;
	private int selectorPosition;
	private String sportShoeStr = "运动鞋";
	private String canvasShoeStr = "帆布鞋";
	private String casualShoeStr = "休闲鞋";
	private String leatherShoeStr = "皮鞋";
	private String highheelShoeStr = "高跟鞋";
	private String pumpsShoeStr = "浅口蜜鞋";
	private String sandalsShoeStr = "凉鞋";
	private String cattonShoeStr = "棉鞋";
	private String bootStr = "靴子";
	protected static String type = "";
	protected static boolean isGetList = false;
	private static final int MSG_GET_SHOELIST = 2;
	private static final int MSG_DEL_SHOE = 3;
	private final int REQ_DEL_REASON = 0x12;
	private String deletereason = "";
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_SHOELIST:// 鞋
					String shoeJson = (String) msg.obj;
					if (shoeJson.equals("[{}]")) {
						T.showToast(mContext, "尚未添加" + type, 0);
					} else {
						ShoeList = gson.fromJson(shoeJson,
								new TypeToken<List<ShoeBoxList>>() {
								}.getType());
						if (ShoeList != null && ShoeList.size() > 0) {
							sportAdapter = new ShoesBoxAdapter(mContext,
									ShoeList);
							lv_shoes.setAdapter(sportAdapter);
						}
					}
					break;
				case MSG_DEL_SHOE:
					String delStr = (String) msg.obj;
					if (delStr.equals("0x00")) {
						Log.i(TAG, "删除成功");
						T.showToast(mContext, "删除成功", 0);
						ShoeList.remove(selectorPosition);
						sportAdapter.notifyDataSetChanged();
					}
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
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_shoebox_list);
		mContext = this;
		typeInt = getIntent().getIntExtra("type", -1);
		switch (typeInt) {
		case 0:
			type = sportShoeStr;
			break;
		case 1:
			type = canvasShoeStr;
			break;
		case 2:
			type = casualShoeStr;
			break;
		case 3:
			type = leatherShoeStr;
			break;
		case 4:
			type = highheelShoeStr;
			break;
		case 5:
			type = pumpsShoeStr;
			break;
		case 6:
			type = sandalsShoeStr;
			break;
		case 7:
			type = cattonShoeStr;
			break;
		case 8:
			type = bootStr;
			break;

		default:
			break;
		}
		initView();
		// getToken();
		getShoesList(type);
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_back = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText(type);
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		// tv_left.setText("服务");
		findViewById(R.id.rl_left).setOnClickListener(this);
		ShoeList = new ArrayList<ShoeBoxList>();
		lv_shoes = (SwipeMenuListView) findViewById(R.id.lv_shoes);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(Util.dip2px(mContext, 90));
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);

				// SwipeMenuItem editItem = new SwipeMenuItem(
				// getApplicationContext());
				// editItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
				// 0xC9,
				// 0xCE)));
				// // set item width
				// editItem.setWidth(Util.dip2px(mContext, 90));
				// editItem.setTitle("修改");
				// editItem.setTitleSize(18);
				// editItem.setTitleColor(Color.WHITE);
				// menu.addMenuItem(editItem);
			}
		};
		lv_shoes.setMenuCreator(creator);
		lv_shoes.setOnItemClickListener(new MyListViewItemOnclckListener());
		lv_shoes.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:// delete
					selectorPosition = position;
					// delShoe(selectorPosition);
					startActivity(new Intent(mContext,
							ShoesDelReasonActivity.class));
					break;
				case 1:
					selectorPosition = position;
					editShoe(ShoeList, selectorPosition);
					break;
				}
			}
		});
	}

	protected void delShoe(int position) {
		L.i("", ShoeList.get(position).shbox);
		delShoeById(ShoeList.get(position).shbox);
	}

	/**
	 * 修改鞋
	 * 
	 * @param list
	 * @param position
	 */
	protected void editShoe(List<ShoeBoxList> list, int position) {
		ShoeBoxList shoeInfo = new ShoeBoxList();
		shoeInfo = list.get(position);
		// 暂时屏蔽
		// Intent intent = new Intent(mContext, EditShoesBoxActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("info", shoeInfo);
		// intent.putExtras(bundle);
		// startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 鞋列表
	 */
	private void getShoesList(String type) {
		L.i(ClassType.ShoesBoxActivity, "==鞋列表getShoesList==");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSHOEBOX_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("class", type);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_SHOELIST, params,
				ClientConstant.SHOEBOX_URL);
	}

	/**
	 * 删除鞋
	 */
	private void delShoeById(String id) {
		L.i(ClassType.ShoesBoxActivity, "==删除鞋delShoeById==");
		// Log.i(TAG, "deleterason" + deletereason);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.DELSHOE_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("shbox", id);
		params.put("deletereason", deletereason);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_DEL_SHOE, params,
				ClientConstant.SHOEBOX_URL);

	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_SHOELIST:
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
					Log.i(TAG, "MSG_GET_SHOELIST解析失败");
				}
				break;
			case MSG_DEL_SHOE:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					int return_code = obj.getInt("return_code");
					String return_msg = null;
					Message msg = Message.obtain();
					if (return_code == 0) {
						JSONArray jsonArray = obj.getJSONArray("return_msg");
						JSONObject jsonObj = jsonArray.getJSONObject(0);
						return_msg = jsonObj.getString("retval");
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_DEL_SHOE解析失败");
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

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ShoesBoxActivity);
		MobclickAgent.onResume(mContext);
		if (isGetList) {
			getShoesList(type);
			isGetList = false;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ShoesBoxActivity);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	class MyListViewItemOnclckListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			showDetailinfo(ShoeList, position);
		}

	}

	/**
	 * 鞋详情
	 * 
	 * @param list
	 * @param position
	 */
	public void showDetailinfo(List<ShoeBoxList> list, int position) {
		ShoeBoxList shoeInfo = new ShoeBoxList();
		shoeInfo = list.get(position);
		// Intent intent = new Intent(mContext, ShoesBoxInfoActivity.class);
		Intent intent = new Intent(mContext, EditShoesBoxActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", shoeInfo);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void onEventMainThread(ShoeDelEvent event) {
		// Log.i(TAG, "收到了消息" + event.getMsg());
		deletereason = event.getMsg();
		delShoe(selectorPosition);
	}
}
