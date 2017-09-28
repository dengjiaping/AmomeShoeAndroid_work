package cn.com.amome.amomeshoes.view.main.health.service.look;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.view.main.health.service.ruler.RulerFootRightActivity;

/**
 * 看一看-大小
 * 
 * @author css
 * 
 */
public class LookSizeActivity extends Activity implements OnClickListener {
	private String TAG = "LookSizeActivity";
	private TextView tv_title;
	private Context mContext;
	private TextView tv_size, tv_length;
	private RelativeLayout rl_size;
	RelativeLayout rl_normal, rl_narrow, rl_wide;
	private TextView tv_wide, tv_normal, tv_narrow;
	private TextView tv_widenum, tv_normalnum, tv_narrownum;
	private int type = -1;
	private static final int MSG_UDPATE_CHECK_FOOT = 0;
	private String width = "";
	private String nickname = "";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_UDPATE_CHECK_FOOT:
					String re = (String) msg.obj;
					if (re.equals("0x00")) {
						Log.i(TAG, "更新成功");
					}
					finish();
					break;
				default:
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
		setContentView(R.layout.activity_look_foot_size);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		initView();
		initData();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);

		rl_size = (RelativeLayout) findViewById(R.id.rl_size);
		tv_size = (TextView) findViewById(R.id.tv_size);
		tv_length = (TextView) findViewById(R.id.tv_length);
		tv_wide = (TextView) findViewById(R.id.tv_wide);
		tv_normal = (TextView) findViewById(R.id.tv_normal);
		tv_narrow = (TextView) findViewById(R.id.tv_narrow);
		tv_narrownum = (TextView) findViewById(R.id.tv_narrownum);
		tv_normalnum = (TextView) findViewById(R.id.tv_normalnum);
		tv_widenum = (TextView) findViewById(R.id.tv_widenum);
		rl_normal = (RelativeLayout) findViewById(R.id.rl_normal);
		rl_narrow = (RelativeLayout) findViewById(R.id.rl_narrow);
		rl_wide = (RelativeLayout) findViewById(R.id.rl_wide);
		tv_title.setText("脚大小");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		rl_size.setOnClickListener(this);
		rl_normal.setOnClickListener(this);
		rl_narrow.setOnClickListener(this);
		rl_wide.setOnClickListener(this);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.btn_clean).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.textView6).setOnClickListener(this);
		findViewById(R.id.tv_ruler).setOnClickListener(this);
	}

	private void initData() {
		if (null == AmomeApp.FootList) {
			tv_length.setText("0mm");
			tv_narrownum.setText("0mm");
			tv_normalnum.setText("0mm");
			tv_widenum.setText("0mm");
		} else if (AmomeApp.FootList.get(0).ftsize.equals("")) {
			tv_length.setText("0mm");
			tv_narrownum.setText("0mm");
			tv_normalnum.setText("0mm");
			tv_widenum.setText("0mm");
		} else {
			switch (Integer.valueOf(AmomeApp.FootList.get(0).ftsize)) {
			case 0:
				tv_length.setText("0mm");
				tv_narrownum.setText("0mm");
				tv_normalnum.setText("0mm");
				tv_widenum.setText("0mm");
				break;
			case 215:
				tv_length.setText("215mm");
				tv_narrownum.setText("<80mm");
				tv_normalnum.setText("80mm");
				tv_widenum.setText(">85mm");
				tv_size.setText("33");
				break;
			case 220:
				tv_length.setText("220mm");
				tv_narrownum.setText("<80mm");
				tv_normalnum.setText("80-85mm");
				tv_widenum.setText(">85mm");
				tv_size.setText("34");
				break;
			case 225:
				tv_length.setText("225mm");
				tv_narrownum.setText("<85mm");
				tv_normalnum.setText("85mm");
				tv_widenum.setText(">85mm");
				tv_size.setText("35");
				break;
			case 230:
				tv_length.setText("230mm");
				tv_narrownum.setText("<85mm");
				tv_normalnum.setText("85-90mm");
				tv_widenum.setText(">90mm");
				tv_size.setText("36");
				break;
			case 235:
				tv_length.setText("235mm");
				tv_narrownum.setText("<90mm");
				tv_normalnum.setText("90mm");
				tv_widenum.setText(">90mm");
				tv_size.setText("37");
				break;
			case 240:
				tv_length.setText("240mm");
				tv_narrownum.setText("<90mm");
				tv_normalnum.setText("90-95mm");
				tv_widenum.setText(">95mm");
				tv_size.setText("38");
				break;
			case 245:
				tv_length.setText("245mm");
				tv_narrownum.setText("<95mm");
				tv_normalnum.setText("95mm");
				tv_widenum.setText(">95mm");
				tv_size.setText("39");
				break;
			case 250:
				tv_length.setText("250mm");
				tv_narrownum.setText("<95mm");
				tv_normalnum.setText("95-100mm");
				tv_widenum.setText(">100mm");
				tv_size.setText("40");
				break;
			case 255:
				tv_length.setText("255mm");
				tv_narrownum.setText("<100mm");
				tv_normalnum.setText("100mm");
				tv_widenum.setText(">100mm");
				tv_size.setText("41");
				break;
			case 260:
				tv_length.setText("260mm");
				tv_narrownum.setText("<100mm");
				tv_normalnum.setText("100-105mm");
				tv_widenum.setText(">105mm");
				tv_size.setText("42");
				break;
			case 265:
				tv_length.setText("265mm");
				tv_narrownum.setText("<105mm");
				tv_normalnum.setText("105mm");
				tv_widenum.setText(">105mm");
				tv_size.setText("43");
				break;
			case 270:
				tv_length.setText("270mm");
				tv_narrownum.setText("<105mm");
				tv_normalnum.setText("105-110mm");
				tv_widenum.setText(">110mm");
				tv_size.setText("44");
				break;
			case 275:
				tv_length.setText("275mm");
				tv_narrownum.setText("<110mm");
				tv_normalnum.setText("110mm");
				tv_widenum.setText(">110mm");
				tv_size.setText("45");
				break;
			case 280:
				tv_length.setText("280mm");
				tv_narrownum.setText("<110mm");
				tv_normalnum.setText("110-115mm");
				tv_widenum.setText(">115mm");
				tv_size.setText("46");
				break;
			default:
				break;
			}
		}
		if (null == AmomeApp.FootList) {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
		} else if (AmomeApp.FootList.get(0).ftwidthtype.equals("")) {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
		} else if (AmomeApp.FootList.get(0).ftwidthtype.equals("偏窄")) {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
		} else if (AmomeApp.FootList.get(0).ftwidthtype.equals("偏宽")) {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
		} else if (AmomeApp.FootList.get(0).ftwidthtype.equals("正常")) {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
		} else {
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.rl_size:
			type = 1;
			initPop(R.layout.pop_foot);
			showPop(type);
			break;
		case R.id.btn_clean:
			tv_size.setText("");
			tv_size.setHint("请选择鞋码，皮鞋码数大一码");
			tv_length.setText("0mm");
			tv_narrownum.setText("0mm");
			tv_normalnum.setText("0mm");
			tv_widenum.setText("0mm");
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			width = "";
			break;
		case R.id.btn_ok:
			updateCheckFoot();
			break;
		case R.id.tv_ruler:
			finish();
			break;
		case R.id.rl_normal:
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			width = tv_normal.getText().toString();
			break;
		case R.id.rl_narrow:
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			width = tv_narrow.getText().toString();
			break;
		case R.id.rl_wide:
			tv_normal.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_narrow.setTextColor(mContext.getResources().getColor(
					R.color.tv_gray));
			tv_wide.setTextColor(mContext.getResources().getColor(
					R.color.turkeygreen));
			width = tv_wide.getText().toString();
			break;
		case R.id.tv_ok:
			UpdateUi(type);
			break;
		case R.id.tv_cancle:
			pop.dismiss();
			break;
		case R.id.textView6:
			Intent in = new Intent(mContext, RulerFootRightActivity.class);
			in.putExtra("leftValue", "服务");
			in.putExtra("btnValue", "开始测量");
			startActivity(in);
			// T.showToast(mContext, "没实现", 0);
			break;
		default:
			break;
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_UDPATE_CHECK_FOOT:
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
					}

					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_UDPATE_CHECK_FOOT解析失败");

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

	/**
	 * 更新看一看数据
	 */
	private void updateCheckFoot() {
		String ftsize = "";
		// if(tv_normal.getTextColors().equals(R.color.turkeygreen)&&tv_narrow.getTextColors().equals(R.color.tv_gray)&&tv_wide.getTextColors().equals(R.color.tv_gray)){
		// ftwidthtype = "正常";
		// }else if(tv_narrow.getTextColors().equals(R.color.turkeygreen)&&
		// tv_normal.getTextColors().equals(R.color.tv_gray)
		// &&tv_wide.getTextColors().equals(R.color.tv_gray)){
		// ftwidthtype = "偏窄";
		// }else if(tv_wide.getTextColors().equals(R.color.turkeygreen)&&
		// tv_normal.getTextColors().equals(R.color.tv_gray)&&tv_narrow.getTextColors().equals(R.color.tv_gray)){
		// ftwidthtype = "偏宽";
		// }else{
		// ftwidthtype = "";
		// }
		if (tv_length.getText().toString().equals("")) {
			ftsize = "0";
		} else {
			ftsize = tv_length.getText().toString()
					.substring(0, tv_length.getText().toString().length() - 2);
		}
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("nickname", nickname);
		params.put("calltype", ClientConstant.UPDATECHECKFOOT_TYPE);
		params.put("ftsize", ftsize);
		params.put("ftwidthtype", width);
		params.put("ftshape", "");
		params.put("ftprint", "");
		params.put("ftcolor", "");
		params.put("ftache", "");
		params.put("ftdisease", "");
		params.put("isftache", "");

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_UDPATE_CHECK_FOOT,
				params, ClientConstant.UPDATECHECKFOOT_URL);
	}

	private void UpdateUi(int type) {
		if (type == 1) {
			int heightValue = numPick2.getValue();
			tv_size.setText(heightValue + "");
			pop.dismiss();
		}
		switch (Integer.valueOf(tv_size.getText().toString())) {
		case 33:
			tv_length.setText("215mm");
			tv_narrownum.setText("<80mm");
			tv_normalnum.setText("80mm");
			tv_widenum.setText(">85mm");
			break;
		case 34:
			tv_length.setText("220mm");
			tv_narrownum.setText("<80mm");
			tv_normalnum.setText("80-85mm");
			tv_widenum.setText(">85mm");
			break;
		case 35:
			tv_length.setText("225mm");
			tv_narrownum.setText("<85mm");
			tv_normalnum.setText("85mm");
			tv_widenum.setText(">85mm");
			break;
		case 36:
			tv_length.setText("230mm");
			tv_narrownum.setText("<85mm");
			tv_normalnum.setText("85-90mm");
			tv_widenum.setText(">90mm");
			break;
		case 37:
			tv_length.setText("235mm");
			tv_narrownum.setText("<90mm");
			tv_normalnum.setText("90mm");
			tv_widenum.setText(">90mm");
			break;
		case 38:
			tv_length.setText("240mm");
			tv_narrownum.setText("<90mm");
			tv_normalnum.setText("90-95mm");
			tv_widenum.setText(">95mm");
			break;
		case 39:
			tv_length.setText("245mm");
			tv_narrownum.setText("<95mm");
			tv_normalnum.setText("95mm");
			tv_widenum.setText(">95mm");
			break;
		case 40:
			tv_length.setText("250mm");
			tv_narrownum.setText("<95mm");
			tv_normalnum.setText("95-100mm");
			tv_widenum.setText(">100mm");
			break;
		case 41:
			tv_length.setText("255mm");
			tv_narrownum.setText("<100mm");
			tv_normalnum.setText("100mm");
			tv_widenum.setText(">100mm");
			break;
		case 42:
			tv_length.setText("260mm");
			tv_narrownum.setText("<100mm");
			tv_normalnum.setText("100-105mm");
			tv_widenum.setText(">105mm");
			break;
		case 43:
			tv_length.setText("265mm");
			tv_narrownum.setText("<105mm");
			tv_normalnum.setText("105mm");
			tv_widenum.setText(">105mm");
			break;
		case 44:
			tv_length.setText("270mm");
			tv_narrownum.setText("<105mm");
			tv_normalnum.setText("105-110mm");
			tv_widenum.setText(">110mm");
			break;
		case 45:
			tv_length.setText("275mm");
			tv_narrownum.setText("<110mm");
			tv_normalnum.setText("110mm");
			tv_widenum.setText(">110mm");
			break;
		case 46:
			tv_length.setText("280mm");
			tv_narrownum.setText("<110mm");
			tv_normalnum.setText("110-115mm");
			tv_widenum.setText(">115mm");
			break;
		default:
			break;
		}
	}

	private PopupWindow pop;
	private View layout;
	private TextView tv_ok, tv_cancle;
	private NumberPicker numPick2;

	/**
	 * 实例化并创建pop菜单
	 */
	@SuppressWarnings("deprecation")
	private void initPop(int layoutId) {
		// 获得layout布局
		layout = (RelativeLayout) LayoutInflater.from(LookSizeActivity.this)
				.inflate(layoutId, null);
		// 设置popwindow菜单大小位置
		pop = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		pop.setContentView(layout);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.showAtLocation(this.findViewById(R.id.activity_service_look_size),
				Gravity.BOTTOM, 0, 0);
		tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		tv_cancle = (TextView) layout.findViewById(R.id.tv_cancle);
		tv_ok.setOnClickListener(this);
		tv_cancle.setOnClickListener(this);
	}

	private void showPop(int type) {
		if (type == 1) {
			String heightStr = tv_size.getText().toString().trim();
			if (TextUtils.isEmpty(heightStr)) {
				heightStr = "0";
			}
			numPick2 = (NumberPicker) layout.findViewById(R.id.height_Picker);
			numPick2.setMinValue(33);
			numPick2.setMaxValue(46);
			numPick2.setWrapSelectorWheel(false);
			numPick2.setValue(Integer.parseInt(heightStr));
			numPick2.setFocusable(false);
			numPick2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.LookSizeActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.LookSizeActivity);
		MobclickAgent.onPause(mContext);
	}

}
