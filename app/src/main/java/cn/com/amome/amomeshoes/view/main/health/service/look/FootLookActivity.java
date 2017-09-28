package cn.com.amome.amomeshoes.view.main.health.service.look;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.FootSecretBasicAdapter;
import cn.com.amome.amomeshoes.adapter.FootSecretOtherAdapter;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootAcheInfo;
import cn.com.amome.amomeshoes.model.FootDiseaseInfo;
import cn.com.amome.amomeshoes.model.FootLookInfo;
import cn.com.amome.amomeshoes.model.FootOtherInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 看一看
 * 
 * @author css
 * 
 */
public class FootLookActivity extends Activity implements OnClickListener {
	private String TAG = "FootLookActivity";
	private Context mContext;
	private TextView tv_title;
	private RelativeLayout rl_left;
	private GridView gv_footbasic, gv_footother;
	private FootSecretBasicAdapter fbAdapter;
	private FootSecretOtherAdapter foAdapter;
	private List<Integer> basicList = new ArrayList<Integer>();
	private List<Integer> otherList = new ArrayList<Integer>();
	private Gson gson = new Gson();
	private static final int MSG_GET_RULERDATA = 0;
	private static final int MSG_GET_CHECK_FOOT = 1;
	private static final int MSG_GET_FOOT_ACHE_INFO = 3;
	private static final int MSG_FOOT_SUGGEST = 4;
	private static final int MSG_GET_FOOT_DISEASE_INFO = 5;
	private AmomeApp app;
	public static FootLookInfo footLookInfo;
	private FootAcheInfo footAcheInfo;
	private FootDiseaseInfo footDiseaseInfo;
	private List<FootAcheInfo> FootAcheList;
	private List<FootDiseaseInfo> FootDiseaseList;
	private String stri = "";
	private String nickname = "";
	private int i = 0;
	private List<String> basicdatalist = new ArrayList<String>();
	private List<String> otherdatalist = new ArrayList<String>();
	private List<FootOtherInfo> footOtherList = new ArrayList<FootOtherInfo>();

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_CHECK_FOOT:
					String footJson = (String) msg.obj;
					if (footJson.equals("[{}]")) {

					} else {
						AmomeApp.FootList = gson.fromJson(footJson,
								new TypeToken<List<FootLookInfo>>() {
								}.getType());
						footLookInfo = AmomeApp.FootList.get(0);
						// initData(footLookInfo);
					}
					initData();
					break;
				case MSG_GET_FOOT_ACHE_INFO:
					String acheJson = (String) msg.obj;
					if (acheJson.equals("[{}]")) {

					} else {
						FootAcheList = gson.fromJson(acheJson,
								new TypeToken<List<FootAcheInfo>>() {
								}.getType());
						footAcheInfo = FootAcheList.get(0);
						initPop();
						showPop("ftache");
					}
					break;
				case MSG_GET_FOOT_DISEASE_INFO:
					String diseaseJson = (String) msg.obj;
					if (diseaseJson.equals("[{}]")) {

					} else {
						FootDiseaseList = gson.fromJson(diseaseJson,
								new TypeToken<List<FootDiseaseInfo>>() {
								}.getType());
						footDiseaseInfo = FootDiseaseList.get(0);
						initPop();
						showPop("ftdisease");
					}
					break;
				default:
					break;
				}
				break;
			case MSG_FOOT_SUGGEST:
				Log.i(TAG, "msg.obj=" + msg.obj);
				for (int i = 0; i < footOtherList.size(); i++) {
					if (msg.obj.equals(footOtherList.get(i).getName())) {
						stri = (String) msg.obj;
						if (footOtherList.get(i).getType().equals("ftache")) {
							getFootAcheInfo();
						} else if (footOtherList.get(i).getType()
								.equals("ftdisease")) {
							getFootDiseaseInfo();
						}
					}
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_foot_secret);
		mContext = this;
		initView();
		nickname = getIntent().getStringExtra("nickname");
		getCheckFoot();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_title.setText("看看脚");
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_left.setOnClickListener(this);
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		gv_footbasic = (GridView) findViewById(R.id.gv_footbasic);
		gv_footother = (GridView) findViewById(R.id.gv_other);
	}

	private void initData() {
		// ****脚长******************
		if (null == AmomeApp.FootList) {
			basicList.add(R.drawable.look_long_bg2); // 暂时用高亮
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftsize.equals("")
				|| footLookInfo.ftsize.equals("0")) {
			basicList.add(R.drawable.look_long_bg2); // 暂时用高亮
			basicdatalist.add("未设置");
		} else {
			basicList.add(R.drawable.look_long_bg2);
			basicdatalist.add(footLookInfo.ftsize + "mm");
		}
		// ****脚宽******************
		if (null == AmomeApp.FootList) {
			basicList.add(R.drawable.look_width_bg2); // 暂时用高亮
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftwidthtype.equals("")
				|| footLookInfo.ftwidthtype.equals("0")) {
			basicList.add(R.drawable.look_width_bg2); // 暂时用高亮
			basicdatalist.add("未设置");
		} else {
			basicList.add(R.drawable.look_width_bg2);
			basicdatalist.add(footLookInfo.ftwidthtype);
		}

		// ****脚色******************
		if (null == AmomeApp.FootList) {
			basicList.add(R.drawable.footcolor_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftcolor.equals("")
				|| footLookInfo.ftcolor.equals("0")) {
			basicList.add(R.drawable.footcolor_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftcolor.equals("白")) {
			basicList.add(R.drawable.look_foot_white_s);
			basicdatalist.add("白");
		} else if (footLookInfo.ftcolor.equals("偏黄")) {
			basicList.add(R.drawable.look_foot_littleyellow_s);
			basicdatalist.add("偏黄");
		} else if (footLookInfo.ftcolor.equals("偏红")) {
			basicList.add(R.drawable.look_foot_littlered_s);
			basicdatalist.add("偏红");
		} else if (footLookInfo.ftcolor.equals("小麦色")) {
			basicList.add(R.drawable.look_foot_wheat_s);
			basicdatalist.add("小麦色");
		} else if (footLookInfo.ftcolor.equals("古铜色")) {
			basicList.add(R.drawable.look_foot_tan_s);
			basicdatalist.add("古铜色");
		} else if (footLookInfo.ftcolor.equals("巧克力色")) {
			basicList.add(R.drawable.look_foot_chocolate_s);
			basicdatalist.add("巧克力色");
		}

		// ****脚型******************
		if (null == AmomeApp.FootList) {
			basicList.add(R.drawable.footshap_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftshape.equals("")
				|| footLookInfo.ftshape.equals("0")) {
			basicList.add(R.drawable.footshap_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftshape.equals("埃及脚")) {
			basicList.add(R.drawable.look_foot_egypt_s);
			basicdatalist.add("埃及脚");
		} else if (footLookInfo.ftshape.equals("罗马脚")) {
			basicList.add(R.drawable.look_foot_roman_s);
			basicdatalist.add("罗马脚");
		} else if (footLookInfo.ftshape.equals("希腊脚")) {
			basicList.add(R.drawable.look_foot_greek_s);
			basicdatalist.add("希腊脚");
		} else if (footLookInfo.ftshape.equals("德意志脚")) {
			basicList.add(R.drawable.look_foot_deutsche_s);
			basicdatalist.add("德意志脚");
		}

		// ****脚印******************
		if (null == AmomeApp.FootList) {
			basicList.add(R.drawable.footprint_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftprint.equals("")
				|| footLookInfo.ftprint.equals("0")) {
			basicList.add(R.drawable.footprint_gray);
			basicdatalist.add("未设置");
		} else if (footLookInfo.ftprint.equals("较窄足印")) {
			basicList.add(R.drawable.look_foot_narrow_s);
			basicdatalist.add("较窄足印");
		} else if (footLookInfo.ftprint.equals("正常足印")) {
			basicList.add(R.drawable.look_foot_normal_s);
			basicdatalist.add("正常足印");
		} else if (footLookInfo.ftprint.equals("较宽足印")) {
			basicList.add(R.drawable.look_foot_wide_s);
			basicdatalist.add("较宽足印");
		}
		// ****其他情况******************
		if (null == app.FootList) {
		} else {
			if (footLookInfo.ftache.contains("脚趾")) {
				otherList.add(R.drawable.look_foot_toe_s);
				otherdatalist.add("脚趾");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚趾");
				footOtherInfo.setType("ftache");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftache.contains("脚后跟")) {
				otherList.add(R.drawable.look_foot_heel_s);
				otherdatalist.add("脚后跟");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚后跟");
				footOtherInfo.setType("ftache");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftache.contains("脚底板")) {
				otherList.add(R.drawable.look_foot_sole_s);
				otherdatalist.add("脚底板");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚底板");
				footOtherInfo.setType("ftache");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftache.contains("脚心")) {
				otherList.add(R.drawable.look_foot_arch_s);
				otherdatalist.add("脚心");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚心");
				footOtherInfo.setType("ftache");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftache.contains("前脚掌")) {
				otherList.add(R.drawable.look_foot_palm_s);
				otherdatalist.add("前脚掌");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("前脚掌");
				footOtherInfo.setType("ftache");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("脚藓")) {
				otherList.add(R.drawable.look_foot_hongkong_s);
				otherdatalist.add("脚藓");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚藓");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("脚气")) {
				otherList.add(R.drawable.look_foot_beriberi_s);
				otherdatalist.add("脚气");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚气");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("拇外翻")) {
				otherList.add(R.drawable.look_foot_hv_s);
				otherdatalist.add("拇外翻");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("拇外翻");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("脚臭")) {
				otherList.add(R.drawable.look_foot_odor_s);
				otherdatalist.add("脚臭");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚臭");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("鸡眼")) {
				otherList.add(R.drawable.look_foot_corn_s);
				otherdatalist.add("鸡眼");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("鸡眼");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("脚底脱皮")) {
				otherList.add(R.drawable.look_foot_peeling_s);
				otherdatalist.add("脚底脱皮");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("脚底脱皮");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("足底筋膜炎")) {
				otherList.add(R.drawable.look_foot_pf_s);
				otherdatalist.add("足底筋膜炎");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("足底筋膜炎");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("跟腱周围炎")) {
				otherList.add(R.drawable.look_foot_at_s);
				otherdatalist.add("跟腱周围炎");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("跟腱周围炎");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
			if (footLookInfo.ftdisease.contains("跟骨骨膜炎")) {
				otherList.add(R.drawable.look_foot_periostitis_s);
				otherdatalist.add("跟骨骨膜炎");
				FootOtherInfo footOtherInfo = new FootOtherInfo();
				footOtherInfo.setName("跟骨骨膜炎");
				footOtherInfo.setType("ftdisease");
				footOtherList.add(footOtherInfo);
			}
		}
		fbAdapter = new FootSecretBasicAdapter(mContext, basicList,
				basicdatalist);
		gv_footbasic.setAdapter(fbAdapter);
		foAdapter = new FootSecretOtherAdapter(mContext, otherList,
				otherdatalist, mHandler);
		gv_footother.setAdapter(foAdapter);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_RULERDATA:
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
					Log.i(TAG, "MSG_GET_RULERDATA解析失败");

				}
				break;
			case MSG_GET_CHECK_FOOT:
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
					Log.i(TAG, "MSG_GET_CHECK_FOOT解析失败");

				}
				break;
			case MSG_GET_FOOT_ACHE_INFO:
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
					Log.i(TAG, "MSG_GET_FOOT_ACHE_INFO解析失败");

				}
				break;
			case MSG_GET_FOOT_DISEASE_INFO:
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
					Log.i(TAG, "MSG_GET_FOOT_DISEASE_INFO解析失败");

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
	 * 获取看一看数据
	 */
	private void getCheckFoot() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETCHECKFOOT_TYPE);
		params.put("nickname", nickname);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CHECK_FOOT, params,
				ClientConstant.GETCHECKFOOT_URL);

	}

	/**
	 * 获取某种脚痛提示
	 */
	private void getFootAcheInfo() {
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_FOOT_ACHE_INFO_TYPE);
		params.put("ftache", stri);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_ACHE_INFO,
				params, ClientConstant.GETCHECKFOOT_URL);
	}

	/**
	 * 获取某种脚疾提示
	 */
	private void getFootDiseaseInfo() {
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GET_FOOT_DISEASE_INFO_TYPE);
		params.put("ftdisease", stri);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_FOOT_DISEASE_INFO,
				params, ClientConstant.GETCHECKFOOT_URL);
	}

	private PopupWindow pop;
	private View layout;
	private TextView tv_info1, tv_info2, tv_info3, tv_info4, tv_tip;
	private FrameLayout fl_popmian;

	@SuppressWarnings("deprecation")
	private void initPop() {
		// 获得layout布局
		layout = (FrameLayout) LayoutInflater.from(mContext).inflate(
				R.layout.pop_footsecret_info, null);
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
	}

	private void showPop(String type) {
		pop.showAtLocation(this.findViewById(R.id.ll_mian), Gravity.CENTER, 0,
				0);
		fl_popmian = (FrameLayout) layout.findViewById(R.id.fl_popmian);
		fl_popmian.setOnClickListener(this);
		// tv_pop_title = (TextView) layout.findViewById(R.id.tv_pop_title);
		tv_info1 = (TextView) layout.findViewById(R.id.tv_info1);
		tv_info2 = (TextView) layout.findViewById(R.id.tv_info2);
		tv_info3 = (TextView) layout.findViewById(R.id.tv_info3);
		tv_info4 = (TextView) layout.findViewById(R.id.tv_info4);
		tv_tip = (TextView) layout.findViewById(R.id.tv_tip);

		if (type.equals("ftache")) {
			tv_tip.setText(stri);
			tv_info2.setText(footAcheInfo.ftacherea);
			tv_info4.setText(footAcheInfo.ftacheadv);
		} else if (type.equals("ftdisease")) {
			tv_tip.setText(stri);
			tv_info2.setText(footDiseaseInfo.ftdiseaserea);
			tv_info4.setText(footDiseaseInfo.ftdiseaseadv);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.fl_popmian:
			closePopupWindow();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.FootLookActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.FootLookActivity);
		MobclickAgent.onPause(mContext);
	}
}
