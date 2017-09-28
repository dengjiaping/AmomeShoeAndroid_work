package cn.com.amome.amomeshoes.view.main.health.service.assistant;

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
import cn.com.amome.amomeshoes.adapter.AssistMemberListAdapter;
import cn.com.amome.amomeshoes.adapter.ShoesBoxChartsAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.AssistInfo;
import cn.com.amome.amomeshoes.model.UserShoesCategoryInfo;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AssistMainActivity extends Activity implements OnClickListener {
	private String TAG = "AssistMainActivity";
	private Context mContext;
	private TextView tv_title;
	private LinearLayout ll_option1, ll_option2, ll_option3, ll_option4,
			ll_option5, ll_option6, ll_option7, ll_option8, ll_option9;
	private ImageView iv_iv1, iv_iv2, iv_iv3, iv_iv4, iv_iv5, iv_iv6, iv_iv7,
			iv_iv8, iv_iv9;
	private LinearLayout ll_select1, ll_select2, ll_select3, ll_select4,
			ll_select5, ll_select6, ll_select7, ll_select8, ll_select9;
	private TextView tv_select1, tv_select2, tv_select3, tv_select4,
			tv_select5, tv_select6, tv_select7, tv_select8, tv_select9;
	private ListView lv_select_member;
	private List<String> memberList;
	private List<Integer> portraitList;
	private Gson gson = new Gson();
	private List<AssistInfo> assistList;
	private AssistMemberListAdapter maAdapter;
	private String stri = "";
	private static final int MSG_GET_ASSIST = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_ASSIST:
					String assistJson = (String) msg.obj;
					if (assistJson.equals("[{}]")) {
					} else {
						assistList = gson.fromJson(assistJson,
								new TypeToken<List<AssistInfo>>() {
								}.getType());
						for (int i = 0; i < assistList.size(); i++) {
							String str = assistList.get(i).nickname;
							if (!str.equals("")) {
								switch (str.substring(0, 1)) {
								case "1":
									tv_select1.setText(str.substring(1));
									setMember(str.substring(1), iv_iv1);
									break;
								case "2":
									tv_select2.setText(str.substring(1));
									setMember(str.substring(1), iv_iv2);
									break;
								case "3":
									tv_select3.setText(str.substring(1));
									setMember(str.substring(1), iv_iv3);
									break;
								case "4":
									tv_select4.setText(str.substring(1));
									setMember(str.substring(1), iv_iv4);
									break;
								case "5":
									tv_select5.setText(str.substring(1));
									setMember(str.substring(1), iv_iv5);
									break;
								case "6":
									tv_select6.setText(str.substring(1));
									setMember(str.substring(1), iv_iv6);
									break;
								case "7":
									tv_select7.setText(str.substring(1));
									setMember(str.substring(1), iv_iv7);
									break;
								case "8":
									tv_select8.setText(str.substring(1));
									setMember(str.substring(1), iv_iv8);
									break;
								case "9":
									tv_select9.setText(str.substring(1));
									setMember(str.substring(1), iv_iv9);
									break;
								default:
									break;
								}
							}
						}
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
		mContext = this;
		setContentView(R.layout.activity_assist_main);
		initView();
		getMemberList();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_title.setText("爱心助手");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		ll_option1 = (LinearLayout) findViewById(R.id.ll_option1);
		ll_option2 = (LinearLayout) findViewById(R.id.ll_option2);
		ll_option3 = (LinearLayout) findViewById(R.id.ll_option3);
		ll_option4 = (LinearLayout) findViewById(R.id.ll_option4);
		ll_option5 = (LinearLayout) findViewById(R.id.ll_option5);
		ll_option6 = (LinearLayout) findViewById(R.id.ll_option6);
		ll_option7 = (LinearLayout) findViewById(R.id.ll_option7);
		ll_option8 = (LinearLayout) findViewById(R.id.ll_option8);
		ll_option9 = (LinearLayout) findViewById(R.id.ll_option9);
		iv_iv1 = (ImageView) findViewById(R.id.iv_iv1);
		iv_iv2 = (ImageView) findViewById(R.id.iv_iv2);
		iv_iv3 = (ImageView) findViewById(R.id.iv_iv3);
		iv_iv4 = (ImageView) findViewById(R.id.iv_iv4);
		iv_iv5 = (ImageView) findViewById(R.id.iv_iv5);
		iv_iv6 = (ImageView) findViewById(R.id.iv_iv6);
		iv_iv7 = (ImageView) findViewById(R.id.iv_iv7);
		iv_iv8 = (ImageView) findViewById(R.id.iv_iv8);
		iv_iv9 = (ImageView) findViewById(R.id.iv_iv9);
		ll_select1 = (LinearLayout) findViewById(R.id.ll_select1);
		ll_select2 = (LinearLayout) findViewById(R.id.ll_select2);
		ll_select3 = (LinearLayout) findViewById(R.id.ll_select3);
		ll_select4 = (LinearLayout) findViewById(R.id.ll_select4);
		ll_select5 = (LinearLayout) findViewById(R.id.ll_select5);
		ll_select6 = (LinearLayout) findViewById(R.id.ll_select6);
		ll_select7 = (LinearLayout) findViewById(R.id.ll_select7);
		ll_select8 = (LinearLayout) findViewById(R.id.ll_select8);
		ll_select9 = (LinearLayout) findViewById(R.id.ll_select9);
		tv_select1 = (TextView) findViewById(R.id.tv_select1);
		tv_select2 = (TextView) findViewById(R.id.tv_select2);
		tv_select3 = (TextView) findViewById(R.id.tv_select3);
		tv_select4 = (TextView) findViewById(R.id.tv_select4);
		tv_select5 = (TextView) findViewById(R.id.tv_select5);
		tv_select6 = (TextView) findViewById(R.id.tv_select6);
		tv_select7 = (TextView) findViewById(R.id.tv_select7);
		tv_select8 = (TextView) findViewById(R.id.tv_select8);
		tv_select9 = (TextView) findViewById(R.id.tv_select9);

		iv_iv1.setOnClickListener(this);
		iv_iv2.setOnClickListener(this);
		iv_iv3.setOnClickListener(this);
		iv_iv4.setOnClickListener(this);
		iv_iv5.setOnClickListener(this);
		iv_iv6.setOnClickListener(this);
		iv_iv7.setOnClickListener(this);
		iv_iv8.setOnClickListener(this);
		iv_iv9.setOnClickListener(this);
		tv_select1.setOnClickListener(this);
		tv_select2.setOnClickListener(this);
		tv_select3.setOnClickListener(this);
		tv_select4.setOnClickListener(this);
		tv_select5.setOnClickListener(this);
		tv_select6.setOnClickListener(this);
		tv_select7.setOnClickListener(this);
		tv_select8.setOnClickListener(this);
		tv_select9.setOnClickListener(this);

		memberList = new ArrayList<String>();
		portraitList = new ArrayList<Integer>();
		memberList.add("爸爸");
		memberList.add("妈妈");
		memberList.add("老公");
		memberList.add("老婆");
		memberList.add("大宝");
		memberList.add("二宝");
		memberList.add("爷爷");
		memberList.add("奶奶");
		memberList.add("基友");
		memberList.add("闺蜜");
		memberList.add("死党");
		memberList.add("室友");
		memberList.add("同学");
		portraitList.add(R.drawable.assist_parents);
		portraitList.add(R.drawable.assist_parents);
		portraitList.add(R.drawable.assist_partner);
		portraitList.add(R.drawable.assist_partner);
		portraitList.add(R.drawable.assist_child);
		portraitList.add(R.drawable.assist_child);
		portraitList.add(R.drawable.assist_parents);
		portraitList.add(R.drawable.assist_parents);
		portraitList.add(R.drawable.assist_others);
		portraitList.add(R.drawable.assist_others);
		portraitList.add(R.drawable.assist_others);
		portraitList.add(R.drawable.assist_others);
		portraitList.add(R.drawable.assist_others);

		tv_select1.setText("请选择");
		tv_select2.setText("请选择");
		tv_select3.setText("请选择");
		tv_select4.setText("请选择");
		tv_select5.setText("请选择");
		tv_select6.setText("请选择");
		tv_select7.setText("请选择");
		tv_select8.setText("请选择");
		tv_select9.setText("请选择");
	}

	private PopupWindow pop;
	private View layout;
	private TextView tv_ok, tv_cancel;
	private TextView tv_name;

	/**
	 * 实例化并创建pop菜单
	 */
	@SuppressWarnings("deprecation")
	private void initPop(int layoutId) {
		// 获得layout布局
		layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				layoutId, null);
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		// 设置popwindow菜单大小位置
		pop = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		pop.setContentView(layout);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 实例化一个ColorDrawable颜色为半透明
		WindowManager.LayoutParams params = ((Activity) mContext).getWindow()
				.getAttributes();
		params.alpha = 0.7f;
		((Activity) mContext).getWindow().setAttributes(params);
		layout.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				closePopupWindow();
				return false;
			}
		});
		tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		tv_cancel = (TextView) layout.findViewById(R.id.tv_cancle);
		tv_ok.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
	}

	private void showPop(final int type) {
		pop.showAtLocation(this.findViewById(R.id.ll_assist_main),
				Gravity.BOTTOM, 0, 0);
		maAdapter = new AssistMemberListAdapter(mContext, memberList);
		lv_select_member = (ListView) layout
				.findViewById(R.id.lv_select_member);
		tv_name = (TextView) layout.findViewById(R.id.tv_name);
		tv_name.setText("请选择");
		lv_select_member.setAdapter(maAdapter);
		lv_select_member.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				switch (type) {
				case 1:
					tv_select1.setText(memberList.get(index));
					iv_iv1.setImageResource(portraitList.get(index));
					break;
				case 2:
					tv_select2.setText(memberList.get(index));
					iv_iv2.setImageResource(portraitList.get(index));
					break;
				case 3:
					tv_select3.setText(memberList.get(index));
					iv_iv3.setImageResource(portraitList.get(index));
					break;
				case 4:
					tv_select4.setText(memberList.get(index));
					iv_iv4.setImageResource(portraitList.get(index));
					break;
				case 5:
					tv_select5.setText(memberList.get(index));
					iv_iv5.setImageResource(portraitList.get(index));
					break;
				case 6:
					tv_select6.setText(memberList.get(index));
					iv_iv6.setImageResource(portraitList.get(index));
					break;
				case 7:
					tv_select7.setText(memberList.get(index));
					iv_iv7.setImageResource(portraitList.get(index));
					break;
				case 8:
					tv_select8.setText(memberList.get(index));
					iv_iv8.setImageResource(portraitList.get(index));
					break;
				case 9:
					tv_select9.setText(memberList.get(index));
					iv_iv9.setImageResource(portraitList.get(index));
					break;
				}

				closePopupWindow();
			}
		});

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

	private int type = -1;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_ok:
			pop.dismiss();
			break;
		case R.id.tv_cancle:
			pop.dismiss();
			break;
		case R.id.tv_select1:
			type = 1;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select2:
			type = 2;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select3:
			type = 3;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select4:
			type = 4;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select5:
			type = 5;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select6:
			type = 6;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select7:
			type = 7;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select8:
			type = 8;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.tv_select9:
			type = 9;
			initPop(R.layout.pop_assist);
			showPop(type);
			break;
		case R.id.iv_iv1:
			if (vertifyNull(R.id.iv_iv1)) {
				preAdd(R.id.iv_iv1);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv2:
			if (vertifyNull(R.id.iv_iv2)) {
				preAdd(R.id.iv_iv2);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv3:
			if (vertifyNull(R.id.iv_iv3)) {
				preAdd(R.id.iv_iv3);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv4:
			if (vertifyNull(R.id.iv_iv4)) {
				preAdd(R.id.iv_iv4);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv5:
			if (vertifyNull(R.id.iv_iv5)) {
				preAdd(R.id.iv_iv5);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv6:
			if (vertifyNull(R.id.iv_iv6)) {
				preAdd(R.id.iv_iv6);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv7:
			if (vertifyNull(R.id.iv_iv7)) {
				preAdd(R.id.iv_iv7);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv8:
			if (vertifyNull(R.id.iv_iv8)) {
				preAdd(R.id.iv_iv8);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		case R.id.iv_iv9:
			if (vertifyNull(R.id.iv_iv9)) {
				preAdd(R.id.iv_iv9);
			} else {
				T.showToast(mContext, "不能为空", 0);
			}
			break;
		}
	}

	private boolean vertifyNull(int id) {
		switch (id) {
		case R.id.iv_iv1:
			if (!TextUtils.isEmpty(tv_select1.getText().toString().trim())
					&& !tv_select1.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv2:
			if (!TextUtils.isEmpty(tv_select2.getText().toString().trim())
					&& !tv_select1.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv3:
			if (!TextUtils.isEmpty(tv_select3.getText().toString().trim())
					&& !tv_select3.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv4:
			if (!TextUtils.isEmpty(tv_select4.getText().toString().trim())
					&& !tv_select4.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv5:
			if (!TextUtils.isEmpty(tv_select5.getText().toString().trim())
					&& !tv_select5.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv6:
			if (!TextUtils.isEmpty(tv_select6.getText().toString().trim())
					&& !tv_select6.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv7:
			if (!TextUtils.isEmpty(tv_select7.getText().toString().trim())
					&& !tv_select7.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv8:
			if (!TextUtils.isEmpty(tv_select8.getText().toString().trim())
					&& !tv_select8.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		case R.id.iv_iv9:
			if (!TextUtils.isEmpty(tv_select9.getText().toString().trim())
					&& !tv_select9.getText().toString().equals("请选择")) {
				return true;
			}
			return false;
		default:
			break;
		}
		return false;
	}

	private void preAdd(int id) {
		switch (id) {
		case R.id.iv_iv1:
			Intent intent1 = new Intent(mContext, AssistOptionActivity.class);
			intent1.putExtra("nickname", "1"
					+ tv_select1.getText().toString().trim());
			startActivity(intent1);
			break;
		case R.id.iv_iv2:
			Intent intent2 = new Intent(mContext, AssistOptionActivity.class);
			intent2.putExtra("nickname", "2"
					+ tv_select2.getText().toString().trim());
			startActivity(intent2);
			break;
		case R.id.iv_iv3:
			Intent intent3 = new Intent(mContext, AssistOptionActivity.class);
			intent3.putExtra("nickname", "3"
					+ tv_select3.getText().toString().trim());
			startActivity(intent3);
			break;
		case R.id.iv_iv4:
			Intent intent4 = new Intent(mContext, AssistOptionActivity.class);
			intent4.putExtra("nickname", "4"
					+ tv_select4.getText().toString().trim());
			startActivity(intent4);
			break;
		case R.id.iv_iv5:
			Intent intent5 = new Intent(mContext, AssistOptionActivity.class);
			intent5.putExtra("nickname", "5"
					+ tv_select5.getText().toString().trim());
			startActivity(intent5);
			break;
		case R.id.iv_iv6:
			Intent intent6 = new Intent(mContext, AssistOptionActivity.class);
			intent6.putExtra("nickname", "6"
					+ tv_select6.getText().toString().trim());
			startActivity(intent6);
			break;
		case R.id.iv_iv7:
			Intent intent7 = new Intent(mContext, AssistOptionActivity.class);
			intent7.putExtra("nickname", "7"
					+ tv_select7.getText().toString().trim());
			startActivity(intent7);
			break;
		case R.id.iv_iv8:
			Intent intent8 = new Intent(mContext, AssistOptionActivity.class);
			intent8.putExtra("nickname", "8"
					+ tv_select8.getText().toString().trim());
			startActivity(intent8);
			break;
		case R.id.iv_iv9:
			Intent intent9 = new Intent(mContext, AssistOptionActivity.class);
			intent9.putExtra("nickname", "9"
					+ tv_select9.getText().toString().trim());
			startActivity(intent9);
			break;
		default:
			break;
		}
	}

	private void setMember(String mebStr, ImageView iv_iv) {
		switch (mebStr) {
		case "爸爸":
			iv_iv.setImageResource(R.drawable.assist_parents);
			break;
		case "妈妈":
			iv_iv.setImageResource(R.drawable.assist_parents);
			break;
		case "老公":
			iv_iv.setImageResource(R.drawable.assist_partner);
			break;
		case "老婆":
			iv_iv.setImageResource(R.drawable.assist_partner);
			break;
		case "大宝":
			iv_iv.setImageResource(R.drawable.assist_child);
			break;
		case "二宝":
			iv_iv.setImageResource(R.drawable.assist_child);
			break;
		case "爷爷":
			iv_iv.setImageResource(R.drawable.assist_parents);
			break;
		case "奶奶":
			iv_iv.setImageResource(R.drawable.assist_parents);
			break;
		case "基友":
			iv_iv.setImageResource(R.drawable.assist_others);
			break;
		case "闺蜜":
			iv_iv.setImageResource(R.drawable.assist_others);
			break;
		case "死党":
			iv_iv.setImageResource(R.drawable.assist_others);
			break;
		case "室友":
			iv_iv.setImageResource(R.drawable.assist_others);
			break;
		case "同学":
			iv_iv.setImageResource(R.drawable.assist_others);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取爱心助手成员列表
	 */
	private void getMemberList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETASSIST_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_ASSIST, params,
				ClientConstant.GETCHECKFOOT_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_ASSIST:
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
					Log.i(TAG, "MSG_GET_ASSIST解析失败");
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
