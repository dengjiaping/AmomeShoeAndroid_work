package cn.com.amome.amomeshoes.view.main.my.secret;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.UserShoesBrandInfo;
import cn.com.amome.amomeshoes.model.UserShoesCategoryInfo;
import cn.com.amome.amomeshoes.model.UserShoesHeelInfo;
import cn.com.amome.amomeshoes.model.UserShoesProInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoeSecretActivity extends Activity implements OnClickListener {
	private String TAG = "ShoeSecretActivity";
	private Context mContext;
	private TextView tv_title, tv_shoe_look, tv_love1, tv_love2, tv_love3,
			tv_trust_top1, tv_trust_top2, tv_trust_top3, tv_trust_bottom1,
			tv_trust_bottom2, tv_trust_bottom3, tv_pro_top1, tv_pro_top2,
			tv_pro_top3, tv_pro_bottom1, tv_pro_bottom2, tv_pro_bottom3,
			tv_heel_top1, tv_heel_top2, tv_heel_top3, tv_heel_bottom1,
			tv_heel_bottom2, tv_heel_bottom3;
	private ImageView iv_left, iv_love1, iv_love2, iv_love3;
	private Gson gson = new Gson();
	private List<UserShoesCategoryInfo> userShoesCategoryList;
	private List<UserShoesBrandInfo> userShoesBrandList;
	private List<UserShoesProInfo> userShoesProList;
	private List<UserShoesHeelInfo> userShoesHeelList;
	private ArrayList<String> typeList;
	private ArrayList<Integer> typeNumList;
	private ArrayList<String> brandList;
	private ArrayList<Integer> brandNumList;
	private ArrayList<String> proList;
	private ArrayList<Integer> proNumList;
	private ArrayList<String> heelList;
	private ArrayList<Integer> heelNumList;
	private int shoeNum;
	private static final int MSG_GET_CATEGORY = 0;
	private static final int MSG_GET_BRAND = 1;
	private static final int MSG_GET_PROBLEM = 2;
	private static final int MSG_GET_HEEL = 3;

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
						T.showToast(mContext, "数据为空", 0);
					} else {
						userShoesCategoryList = gson.fromJson(categoryJson,
								new TypeToken<List<UserShoesCategoryInfo>>() {
								}.getType());
						for (int i = 0; i < userShoesCategoryList.size(); i++) {
							shoeNum = shoeNum
									+ Integer.valueOf(userShoesCategoryList
											.get(i).count);
							if (Integer
									.valueOf(userShoesCategoryList.get(i).count) != 0) {
								typeList.add(userShoesCategoryList.get(i).type);
								typeNumList
										.add(Integer
												.valueOf(userShoesCategoryList
														.get(i).count));
							}
						}
						if (typeNumList.size() == 0) {

						} else {
							tv_shoe_look.setText("已收录" + shoeNum + "双");
						}

						bubbleSort(typeList, typeNumList);
						if (typeNumList.size() >= 1) {
							tv_love1.setText(typeNumList.get(0) + "双");
							setImage(typeList.get(0), iv_love1);
						}
						if (typeNumList.size() >= 2) {
							tv_love2.setText(typeNumList.get(1) + "双");
							setImage(typeList.get(1), iv_love2);
						}
						if (typeNumList.size() >= 3) {
							tv_love3.setText(typeNumList.get(2) + "双");
							setImage(typeList.get(2), iv_love3);
						}
					}
					getUserShoesBrandList();
					break;
				case MSG_GET_BRAND:
					String brandJson = (String) msg.obj;
					if (brandJson.equals("[{}]")) {
					} else {
						userShoesBrandList = gson.fromJson(brandJson,
								new TypeToken<List<UserShoesBrandInfo>>() {
								}.getType());
						for (int i = 0; i < userShoesBrandList.size(); i++) {
							if (Integer
									.valueOf(userShoesBrandList.get(i).count) != 0) {
								brandList.add(userShoesBrandList.get(i).brand);
								brandNumList.add(Integer
										.valueOf(userShoesBrandList

										.get(i).count));
							}
						}
						bubbleSort(brandList, brandNumList);
						if (brandNumList.size() >= 1) {
							tv_trust_top1.setText(brandList.get(0));
							tv_trust_bottom1.setText(brandNumList.get(0) + "双");
						}
						if (brandNumList.size() >= 2) {
							tv_trust_top2.setText(brandList.get(1));
							tv_trust_bottom2.setText(brandNumList.get(1) + "双");
						}
						if (brandNumList.size() >= 3) {
							tv_trust_top3.setText(brandList.get(2));
							tv_trust_bottom3.setText(brandNumList.get(2) + "双");
						}
					}
					getUserShoesProList();
					break;
				case MSG_GET_PROBLEM:
					String problemJson = (String) msg.obj;
					if (problemJson.equals("[{}]")) {
					} else {
						userShoesProList = gson.fromJson(problemJson,
								new TypeToken<List<UserShoesProInfo>>() {
								}.getType());
						for (int i = 0; i < userShoesProList.size(); i++) {
							if (Integer.valueOf(userShoesProList.get(i).count) != 0) {
								proList.add(userShoesProList.get(i).prodes);
								proNumList.add(Integer.valueOf(userShoesProList
										.get(i).count));
							}
						}
						bubbleSort(proList, proNumList);
						if (proNumList.size() >= 1) {
							tv_pro_top1.setText(proList.get(0));
							tv_pro_bottom1.setText(proNumList.get(0) + "双");
						}
						if (proNumList.size() >= 2) {
							tv_pro_top2.setText(proList.get(1));
							tv_pro_bottom2.setText(proNumList.get(1) + "双");
						}
						if (proNumList.size() >= 3) {
							tv_pro_top3.setText(proList.get(2));
							tv_pro_bottom3.setText(proNumList.get(2) + "双");
						}
					}
					getUserShoesHeelList();
					break;
				case MSG_GET_HEEL:
					String heelJson = (String) msg.obj;
					if (heelJson.equals("[{}]")) {
					} else {
						userShoesHeelList = gson.fromJson(heelJson,
								new TypeToken<List<UserShoesHeelInfo>>() {
								}.getType());
						for (int i = 0; i < userShoesHeelList.size(); i++) {
							if (Integer.valueOf(userShoesHeelList.get(i).count) != 0) {
								heelList.add(userShoesHeelList.get(i).heel);
								heelNumList
										.add(Integer.valueOf(userShoesHeelList
												.get(i).count));
							}
						}
						bubbleSort(heelList, heelNumList);
						if (heelNumList.size() >= 1) {
							tv_heel_top1.setText(heelList.get(0));
							tv_heel_bottom1.setText(heelNumList.get(0) + "双");
						}
						if (heelNumList.size() >= 2) {
							tv_heel_top2.setText(heelList.get(1));
							tv_heel_bottom2.setText(heelNumList.get(1) + "双");
						}
						if (heelNumList.size() >= 3) {
							tv_heel_top3.setText(heelList.get(2));
							tv_heel_bottom3.setText(heelNumList.get(2) + "双");
						}
					}
					break;
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shoe_secret);
		mContext = this;
		initView();
		getUserShoesCategoryList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_shoe_look = (TextView) findViewById(R.id.tv_shoe_look);
		tv_love1 = (TextView) findViewById(R.id.tv_love1);
		tv_love2 = (TextView) findViewById(R.id.tv_love2);
		tv_love3 = (TextView) findViewById(R.id.tv_love3);
		iv_love1 = (ImageView) findViewById(R.id.iv_love1);
		iv_love2 = (ImageView) findViewById(R.id.iv_love2);
		iv_love3 = (ImageView) findViewById(R.id.iv_love3);
		tv_trust_top1 = (TextView) findViewById(R.id.tv_trust_top1);
		tv_trust_top2 = (TextView) findViewById(R.id.tv_trust_top2);
		tv_trust_top3 = (TextView) findViewById(R.id.tv_trust_top3);
		tv_trust_bottom1 = (TextView) findViewById(R.id.tv_trust_bottom1);
		tv_trust_bottom2 = (TextView) findViewById(R.id.tv_trust_bottom2);
		tv_trust_bottom3 = (TextView) findViewById(R.id.tv_trust_bottom3);
		tv_heel_top1 = (TextView) findViewById(R.id.tv_heel_top1);
		tv_heel_top2 = (TextView) findViewById(R.id.tv_heel_top2);
		tv_heel_top3 = (TextView) findViewById(R.id.tv_heel_top3);
		tv_heel_bottom1 = (TextView) findViewById(R.id.tv_heel_bottom1);
		tv_heel_bottom2 = (TextView) findViewById(R.id.tv_heel_bottom2);
		tv_heel_bottom3 = (TextView) findViewById(R.id.tv_heel_bottom3);
		tv_pro_top1 = (TextView) findViewById(R.id.tv_pro_top1);
		tv_pro_top2 = (TextView) findViewById(R.id.tv_pro_top2);
		tv_pro_top3 = (TextView) findViewById(R.id.tv_pro_top3);
		tv_pro_bottom1 = (TextView) findViewById(R.id.tv_pro_bottom1);
		tv_pro_bottom2 = (TextView) findViewById(R.id.tv_pro_bottom2);
		tv_pro_bottom3 = (TextView) findViewById(R.id.tv_pro_bottom3);
		tv_trust_top1.setSelected(true);
		tv_trust_top2.setSelected(true);
		tv_trust_top3.setSelected(true);
		tv_heel_top1.setSelected(true);
		tv_heel_top2.setSelected(true);
		tv_heel_top3.setSelected(true);
		tv_pro_top1.setSelected(true);
		tv_pro_top2.setSelected(true);
		tv_pro_top3.setSelected(true);
		tv_title.setText("鞋的秘密");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		findViewById(R.id.rl_left).setOnClickListener(this);

		typeList = new ArrayList<String>();
		typeNumList = new ArrayList<Integer>();
		brandList = new ArrayList<String>();
		brandNumList = new ArrayList<Integer>();
		proList = new ArrayList<String>();
		proNumList = new ArrayList<Integer>();
		heelList = new ArrayList<String>();
		heelNumList = new ArrayList<Integer>();
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

	/**
	 * 获取鞋种类的数量
	 */
	private void getUserShoesCategoryList() {
		Log.i(TAG, "getUserShoesCategoryList");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOECATEGORY_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CATEGORY, params,
				ClientConstant.SHOEBOX_URL);
	}

	/**
	 * 获取鞋品牌的数量
	 */
	private void getUserShoesBrandList() {
		Log.i(TAG, "getUserShoesBrandList");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOEBRAND_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_BRAND, params,
				ClientConstant.SHOEBOX_URL);
	}

	/**
	 * 获取鞋问题的数量
	 */
	private void getUserShoesProList() {
		Log.i(TAG, "getUserShoesProList");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOEPRO_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_PROBLEM, params,
				ClientConstant.SHOEBOX_URL);
	}

	/**
	 * 获取不同鞋跟的鞋的数量
	 */
	private void getUserShoesHeelList() {
		Log.i(TAG, "getUserShoesHeelList");
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETUSERSHOEHEEL_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_HEEL, params,
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
			case MSG_GET_BRAND:
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
					Log.i(TAG, "MSG_GET_BRAND解析失败");
				}
				break;
			case MSG_GET_PROBLEM:
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
					Log.i(TAG, "MSG_GET_PROBLEM解析失败");
				}
				break;
			case MSG_GET_HEEL:
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
					Log.i(TAG, "MSG_GET_HEEL解析失败");
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

	public void bubbleSort(ArrayList<String> nameList,
			ArrayList<Integer> numList) {
		int temp; // 记录临时中间值
		String str;
		int size = numList.size(); // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (numList.get(i) < numList.get(j)) { // 交换两数的位置
					temp = numList.get(i);
					str = nameList.get(i);
					nameList.set(i, nameList.get(j));
					nameList.set(j, str);
					numList.set(i, numList.get(j));
					numList.set(j, temp);
				}
			}
		}
	}

	private void setImage(String type, ImageView iv_love) {
		switch (type) {
		case "运动鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_sport);
			break;
		case "帆布鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_canvas);
			break;
		case "休闲鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_casual);
			break;
		case "皮鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_leather);
			break;
		case "高跟鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_highheel);
			break;
		case "浅口蜜鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_pumps);
			break;
		case "凉鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_sandals);
			break;
		case "棉鞋":
			iv_love.setImageResource(R.drawable.shoe_nobg_catton);
			break;
		case "靴子":
			iv_love.setImageResource(R.drawable.shoe_nobg_boots);
			break;
		default:
			break;
		}
	};

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}
}
