package cn.com.amome.amomeshoes.view.main.health.service.look;

import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootLookInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SpfUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/** 看一看界面 */
public class LookMainActivity extends Activity implements OnClickListener {
	private String TAG = "LookMainActivity";
	private TextView tv_title;
	private Context mContext;
	private ImageView iv_size, iv_shap, iv_print, iv_color, iv_pain, iv_diseas;
	private TextView tv_painnum, tv_diseasnum;
	private Gson gson = new Gson();
	private static final int MSG_GET_CHECK_FOOT = 0;
	public static FootLookInfo footLookInfo;
	private String nickname = "";

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
						AmomeApp.FootList = null;
						iv_size.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footsize_gray));
						iv_shap.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footshap_gray));
						iv_print.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footprint_gray));
						iv_color.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footcolor_gray));
						iv_pain.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footpain_gray));
						tv_painnum.setText("0");
						iv_diseas.setImageDrawable(getResources().getDrawable(
								R.drawable.look_footdiseas_gray));
						tv_diseasnum.setText("0");
					} else {
						AmomeApp.FootList = gson.fromJson(footJson,
								new TypeToken<List<FootLookInfo>>() {
								}.getType());
						footLookInfo = AmomeApp.FootList.get(0);
						initData(footLookInfo);
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
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_main);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		initView();
		getCheckFoot();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_size = (ImageView) findViewById(R.id.iv_size);
		iv_shap = (ImageView) findViewById(R.id.iv_shap);
		iv_print = (ImageView) findViewById(R.id.iv_print);
		iv_color = (ImageView) findViewById(R.id.iv_color);
		iv_pain = (ImageView) findViewById(R.id.iv_pain);
		iv_diseas = (ImageView) findViewById(R.id.iv_diseas);
		tv_painnum = (TextView) findViewById(R.id.tv_painnum);
		tv_diseasnum = (TextView) findViewById(R.id.tv_diseasnum);
		tv_title.setText("看看脚");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		tv_painnum.setText("0");
		tv_diseasnum.setText("0");
		iv_size.setOnClickListener(this);
		iv_shap.setOnClickListener(this);
		iv_print.setOnClickListener(this);
		iv_color.setOnClickListener(this);
		iv_pain.setOnClickListener(this);
		iv_diseas.setOnClickListener(this);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.iv_look).setOnClickListener(this);
	}

	private void initData(FootLookInfo footLookInfo) {
		if (footLookInfo.ftsize.equals("") || footLookInfo.ftsize.equals("0")) {
			iv_size.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footsize_gray));
		} else {
			iv_size.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footsize));
		}

		if (footLookInfo.ftshape.equals("")) {
			iv_shap.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footshap_gray));
		} else {
			iv_shap.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footshap));
		}

		if (footLookInfo.ftprint.equals("")) {
			iv_print.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footprint_gray));
		} else {
			iv_print.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footprint));
		}

		if (footLookInfo.ftcolor.equals("")) {
			iv_color.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footcolor_gray));
		} else {
			iv_color.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footcolor));
		}

		if (footLookInfo.ftache.equals("")) {
			iv_pain.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footpain_gray));
			tv_painnum.setVisibility(View.GONE);
			tv_painnum.setText("0");
		} else {
			tv_painnum.setVisibility(View.VISIBLE);
			iv_pain.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footpain));
			int count = 0;
			if (AmomeApp.FootList.get(0).ftache.contains("脚趾")) {
				count++;
			}
			if (AmomeApp.FootList.get(0).ftache.contains("脚后跟")) {
				count++;
			}
			if (AmomeApp.FootList.get(0).ftache.contains("脚底板")) {
				count++;
			}
			if (AmomeApp.FootList.get(0).ftache.contains("脚心")) {
				count++;
			}
			if (AmomeApp.FootList.get(0).ftache.contains("前脚掌")) {
				count++;
			}
			tv_painnum.setText(count + "");
		}

		if (footLookInfo.ftdisease.equals("")) {
			iv_diseas.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footdiseas_gray));
			tv_diseasnum.setText("0");
			tv_diseasnum.setVisibility(View.GONE);
		} else {
			tv_diseasnum.setVisibility(View.VISIBLE);
			iv_diseas.setImageDrawable(getResources().getDrawable(
					R.drawable.look_footdiseas));
			String[] ary = ("," + footLookInfo.ftdisease + ",").split(",");
			tv_diseasnum.setText(ary.length - 1 + "");

		}
		DialogUtil.hideProgressDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.iv_look:
			Intent intent_ok = new Intent(mContext, FootLookActivity.class);
			intent_ok.putExtra("nickname", nickname);
			startActivity(intent_ok);
			break;
		case R.id.iv_size:
			Intent intent_size = new Intent(mContext, LookSizeActivity.class);
			intent_size.putExtra("nickname", nickname);
			startActivity(intent_size);
			break;
		case R.id.iv_shap:
			Intent intent_shap = new Intent(mContext, LookShapActivity.class);
			intent_shap.putExtra("nickname", nickname);
			startActivity(intent_shap);
			break;
		case R.id.iv_print:
			Intent intent_print = new Intent(mContext,
					LookFootprintsActivity.class);
			intent_print.putExtra("nickname", nickname);
			startActivity(intent_print);
			break;
		case R.id.iv_color:
			Intent intent_color = new Intent(mContext,
					LookFootcolorActivity.class);
			intent_color.putExtra("nickname", nickname);
			startActivity(intent_color);
			break;
		case R.id.iv_pain:
			Intent intent_pain = new Intent(mContext,
					LookFootpainActivity.class);
			intent_pain.putExtra("nickname", nickname);
			startActivity(intent_pain);
			break;
		case R.id.iv_diseas:
			Intent intent_diseas = new Intent(mContext,
					LookFootdiseasActivity.class);
			intent_diseas.putExtra("nickname", nickname);
			startActivity(intent_diseas);
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
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等",
		// true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETCHECKFOOT_TYPE);
		params.put("nickname", nickname);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_CHECK_FOOT, params,
				ClientConstant.GETCHECKFOOT_URL);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCheckFoot();
		MobclickAgent.onPageStart(ClassType.LookMainActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MobclickAgent.onPageEnd(ClassType.LookMainActivity);
		MobclickAgent.onPause(mContext);
	}

}
