package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.ProblemAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.T;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class AddShoesBoxActivity extends Activity implements OnClickListener {
	private String TAG = "AddShoesBoxActivity";
	private TextView tv_title, tv_left;
	@SuppressWarnings("unused")
	private ImageView iv_back;
	private Context mContext;
	private LinearLayout ll_camera, ll_option;
	private ImageView iv_camera;
	private EditText et_shoesize, et_price;
	private LinearLayout ll_brand, ll_category, ll_material, ll_heel;
	private TextView tv_brand, tv_category, tv_material, tv_heel;
	private TextView tv_pro_num;
	private ImageView img_more;
	private GridView gridView_problem;
	private final int REQ_SET_AVATAR = 0x14;
	private final int REQ_SELECT_BRAND = 0x13;
	private final int REQ_SELECT_CATEGORY = 0x12;
	private final int REQ_SELECT_MATERIAL = 0x11;
	private final int REQ_SELECT_HEEL = 0x10;
	private String imagePath;
	private boolean isShowPro;// 是否展示问题列表
	private String category, material, heel, brand, size, price;
	private String token = "";
	private Gson gson = new Gson();
	private List<ShoesProblem> shoeProblemList;
	private ProblemAdapter problemAdapter;
	private int problem = 0;// 当前选中的问题使用10进制表示的数
	private int problem_num = 0;// 选中问题的个数
	private String problemStr = ""; // 提交到服务端的问题字符串
	private static final int MSG_GET_PROBLEM = 1;
	private static final int MSG_ADD_SHOEINFO = 2;
	private static final int MSG_ADD_AVATAR = 6;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_PROBLEM:// 鞋问题
					String problemJson = (String) msg.obj;
					if (TextUtils.isEmpty(problemJson)) {
					} else {
						shoeProblemList = gson.fromJson(problemJson,
								new TypeToken<List<ShoesProblem>>() {
								}.getType());
						if (shoeProblemList != null
								&& shoeProblemList.size() > 0) {
							problemAdapter = new ProblemAdapter(mContext,
									shoeProblemList);
							gridView_problem.setAdapter(problemAdapter);
						}
					}
					break;
				case MSG_ADD_SHOEINFO:// 添加
					String addStr = (String) msg.obj;
					if (addStr.equals("0x00")) {
						Log.i(TAG, "提交成功");
						T.showToast(mContext, "提交成功！", 0);
						finish();
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
		setContentView(R.layout.activity_shoebox_add);
		mContext = this;
		initView();
		getShoesProblem();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_back = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("添加鞋");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
		findViewById(R.id.rl_problem).setOnClickListener(this);
		ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
		ll_option = (LinearLayout) findViewById(R.id.ll_option);
		ll_brand = (LinearLayout) findViewById(R.id.ll_brand);
		ll_category = (LinearLayout) findViewById(R.id.ll_category);
		ll_heel = (LinearLayout) findViewById(R.id.ll_heel);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		img_more = (ImageView) findViewById(R.id.img_more);
		// et_shoesize = (EditText) findViewById(R.id.et_size);
		et_price = (EditText) findViewById(R.id.et_price);
		tv_brand = (TextView) findViewById(R.id.tv_brand);
		tv_category = (TextView) findViewById(R.id.tv_category);
		// tv_material = (TextView) findViewById(R.id.tv_material);
		tv_heel = (TextView) findViewById(R.id.tv_heel);
		gridView_problem = (GridView) findViewById(R.id.gridView_problem);
		tv_pro_num = (TextView) findViewById(R.id.tv_pro_num);
		tv_pro_num.setText(problem_num + "");

		et_price.setOnClickListener(this);
		iv_camera.setOnClickListener(this);
		ll_brand.setOnClickListener(this);
		ll_category.setOnClickListener(this);
		ll_heel.setOnClickListener(this);
		img_more.setOnClickListener(this);
		gridView_problem.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.et_price:
			et_price.setCursorVisible(true);
			break;
		case R.id.ll_category:
			Intent intent_category = new Intent(mContext,
					ShoeCategoryListActivity.class);
			intent_category.putExtra("category", tv_category.getText()
					.toString().trim());
			startActivityForResult(intent_category, REQ_SELECT_CATEGORY);
			break;
		case R.id.ll_brand:
			Intent intent1 = new Intent(mContext, ShoeBrandListActivity.class);
			intent1.putExtra("brand", tv_brand.getText().toString().trim());
			startActivityForResult(intent1, REQ_SELECT_BRAND);
			break;
		case R.id.tv_material:
			Intent intent_material = new Intent(mContext,
					ShoeMaterialListActivity.class);
			intent_material.putExtra("material", tv_material.getText()
					.toString().trim());
			startActivityForResult(intent_material, REQ_SELECT_MATERIAL);
			break;
		case R.id.ll_heel:
			Intent intent_heel = new Intent(mContext,
					ShoeHeelListActivity.class);
			intent_heel.putExtra("heel", tv_heel.getText().toString().trim());
			startActivityForResult(intent_heel, REQ_SELECT_HEEL);
			break;
		case R.id.rl_problem:
			if (shoeProblemList != null && shoeProblemList.size() > 0) {
			} else {
				getShoesProblem();
			}
			if (!isShowPro) {
				isShowPro = true;
				img_more.setImageResource(R.drawable.arrow_up);
				gridView_problem.setVisibility(View.VISIBLE);
			} else {
				isShowPro = false;
				img_more.setImageResource(R.drawable.arrow_down);
				gridView_problem.setVisibility(View.GONE);
			}
			break;
		case R.id.img_more:
			if (shoeProblemList != null && shoeProblemList.size() > 0) {
			} else {
				getShoesProblem();
			}
			if (!isShowPro) {
				isShowPro = true;
				img_more.setImageResource(R.drawable.arrow_up);
				gridView_problem.setVisibility(View.VISIBLE);
			} else {
				isShowPro = false;
				img_more.setImageResource(R.drawable.arrow_down);
				gridView_problem.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_save:
			String binaryString = Integer.toBinaryString(problem);
			for (int i = 0; i < binaryString.getBytes().length; i++) {
				if (get(problem, i) == 1) {
					problemStr += "," + (i + 1);
				}
			}
			if (problemStr.length() > 0) {
				problemStr = problemStr.substring(1);
				// Log.i(TAG, "截取后" + problemStr);
			}
			if (verify()) {
				addShoes();
			}
			problemStr = "";
			break;
		case R.id.iv_camera:
			initPopAvtar(R.layout.pop_takephoto);
			showPop(MSG_ADD_AVATAR);
			break;
		case R.id.rl_album:
			Intent i = new Intent(mContext, ClipPictureActivity.class);
			i.putExtra("avatar", "album");
			startActivityForResult(i, REQ_SET_AVATAR);
			closePopupWindow();
			break;
		case R.id.rl_takephoto:
			Intent intent2 = new Intent(mContext, ClipPictureActivity.class);
			intent2.putExtra("avatar", "takephoto");
			startActivityForResult(intent2, REQ_SET_AVATAR);
			closePopupWindow();
			break;
		case R.id.rl_cancle:
			closePopupWindow();
			break;
		default:
			break;
		}

	}

	public void setSelect(int position, boolean isChecked) {
		int pronum = Integer.parseInt(shoeProblemList.get(position).prodesnum);
		if (isChecked) {
			problem_num++;
			problem = problem | 1 << (pronum - 1);
		} else {
			problem_num--;
			int backup = 1 << (pronum - 1);
			int temp = 0;
			temp = (~temp) ^ backup;
			problem = problem & temp;
		}
		// Log.i(TAG, "problem" + problem);
		tv_pro_num.setText(problem_num + "");
		// Log.i(TAG, Integer.toBinaryString(problem));
		L.i("", problem + "----将int数据转换=" + getUnsignedIntt(problem));
	}

	/**
	 * 转换无符号数
	 * 
	 * @param data
	 * @return
	 */
	public long getUnsignedIntt(int data) {// 将int数据转换为0~4294967295
		// (0xFFFFFFFF即DWORD)。
		return data & 0x0FFFFFFFFl;
	}

	/**
	 * 验证是否全部完成
	 * 
	 * @return true ,全部完成
	 */
	private boolean verify() {
		brand = tv_brand.getText().toString().trim();
		// size = et_shoesize.getText().toString().trim();
		// material = tv_material.getText().toString().trim();
		price = et_price.getText().toString().trim();
		heel = tv_heel.getText().toString().trim();
		category = tv_category.getText().toString().trim();
		if (!TextUtils.isEmpty(imagePath) && !TextUtils.isEmpty(category)) {
			return true;
		} else if (TextUtils.isEmpty(imagePath) && !TextUtils.isEmpty(category)) {
			T.showToast(mContext, "请添加图片", 0);
			return false;
		} else if (!TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(category)) {
			T.showToast(mContext, "请选择鞋种类", 0);
			return false;
		} else if (TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(category)) {
			T.showToast(mContext, "资料不完整", 0);
			return false;
		}
		return false;
	}

	private PopupWindow pop;
	private View layout;
	private RelativeLayout rl_takephoto, rl_album, rl_cancle;

	@SuppressWarnings("deprecation")
	private void initPopAvtar(int layoutId) {
		// 获得layout布局
		layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				layoutId, null);
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		// 设置popwindow菜单大小位置
		pop = new PopupWindow(layout, display.getWidth(), display.getHeight());
		pop.setContentView(layout);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		WindowManager.LayoutParams params = ((Activity) mContext).getWindow()
				.getAttributes();
		params.alpha = 0.7f;
		((Activity) mContext).getWindow().setAttributes(params);
		// 点击其他地方消失
		layout.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				closePopupWindow();
				return false;
			}
		});
	}

	@SuppressLint("CutPasteId")
	private void showPop(int type) {
		switch (type) {
		case MSG_ADD_AVATAR:// 头像
			pop.showAtLocation(this.findViewById(R.id.ll_addshoe_mian),
					Gravity.BOTTOM, 0, 0);
			rl_cancle = (RelativeLayout) layout.findViewById(R.id.rl_cancle);
			rl_takephoto = (RelativeLayout) layout
					.findViewById(R.id.rl_takephoto);
			rl_album = (RelativeLayout) layout.findViewById(R.id.rl_album);
			rl_cancle.setOnClickListener(this);
			rl_takephoto.setOnClickListener(this);
			rl_album.setOnClickListener(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 关闭pop窗口
	 */
	private void closePopupWindow() {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			pop = null;
			WindowManager.LayoutParams params = ((Activity) mContext)
					.getWindow().getAttributes();
			params.alpha = 1f;
			((Activity) mContext).getWindow().setAttributes(params);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_SET_AVATAR && data != null
				&& data.getExtras() != null) {
			if (resultCode == RESULT_OK) {
				imagePath = data.getStringExtra("url");
				L.i("", imagePath);
				iv_camera.setImageURI(Uri.fromFile(new File(imagePath)));
				return;
			}
		} else if (requestCode == REQ_SELECT_BRAND) {
			if (resultCode == RESULT_OK) {
				String brandName = data.getStringExtra("brandname");
				tv_brand.setText(brandName);
			}
		} else if (requestCode == REQ_SELECT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				String categoryName = data.getStringExtra("categoryname");
				tv_category.setText(categoryName);
			}
		} else if (requestCode == REQ_SELECT_MATERIAL) {
			if (resultCode == RESULT_OK) {
				String materialName = data.getStringExtra("materialname");
				tv_material.setText(materialName);
			}
		} else if (requestCode == REQ_SELECT_HEEL) {
			if (resultCode == RESULT_OK) {
				String heelName = data.getStringExtra("heelname");
				tv_heel.setText(heelName);
			}
		}
	}

	/**
	 * 鞋问题
	 */
	private void getShoesProblem() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSHOEPROBLEM_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_PROBLEM, params,
				ClientConstant.SHOEBOX_URL);
	}

	/**
	 * 添加鞋
	 */
	private void addShoes() {
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.ADDSHOE_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("heel", heel);
		params.put("class", category);
		// params.put("texture", material);
		// params.put("shoesize", size);
		params.put("problem", problemStr);
		params.put("brand", brand);
		params.put("price", price);
		if (!TextUtils.isEmpty(imagePath)) {
			try {
				File file = new File(imagePath);
				if (file.exists() && file.length() > 0) {
					params.put("picture", "shoeimg");
					params.put("shoeimg", file);
				} else {
					params.put("picture", "");
					T.showToast(mContext, "图片上传失败", 0);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			params.put("picture", "");
		}
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_ADD_SHOEINFO, params,
				ClientConstant.SHOEBOX_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
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
			case MSG_ADD_SHOEINFO:
				DialogUtil.hideProgressDialog();
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);
					String return_msg = obj.getString("return_msg");
					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						JSONArray jsonArray = obj.getJSONArray("return_msg");
						JSONObject jsonObj = jsonArray.getJSONObject(0);
						return_msg = jsonObj.getString("retval");
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = type;
						msg.obj = return_msg;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_ADD_SHOEINFO解析失败");
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

	/**
	 * @param num
	 *            :要获取二进制值的数
	 * @param index
	 *            :位数
	 */
	public static int get(int num, int index) {
		return (num & (0x1 << index)) >> index;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		brand = tv_brand.getText().toString().trim();
		if (!TextUtils.isEmpty(brand) || !TextUtils.isEmpty(category)
				|| !TextUtils.isEmpty(heel)) {
			DialogUtil.showAlertDialog(mContext, "", "您确认返回吗？", "确定", "取消",
					new OnAlertViewClickListener() {
						@Override
						public void confirm() {
							finish();
						}

						@Override
						public void cancel() {
						}
					});
		} else {
			finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.AddShoesBoxActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.AddShoesBoxActivity);
		MobclickAgent.onPause(mContext);
	}
}
