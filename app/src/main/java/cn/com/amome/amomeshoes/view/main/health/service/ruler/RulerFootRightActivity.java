package cn.com.amome.amomeshoes.view.main.health.service.ruler;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.FootRulerInfo;
import cn.com.amome.amomeshoes.util.Constants;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.service.look.LookFootprintsActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 量一量-右脚
 * 
 * @author css
 */
public class RulerFootRightActivity extends Activity implements OnClickListener {
	private String TAG = "RulerFootRightActivity";
	private TextView tv_title, tv_left;
	private TextView tv_long;
	private Context mContext;
	private TextView tv_width;
	private LinearLayout rulerlayout_long;
	private LinearLayout rulerlayout_width;
	private String height = "";
	private String width = "";
	private HorizontalScrollView ruler_long;
	private HorizontalScrollView ruler_width;
	private static final int MSG_GET_RULERDATA = 0;
	private Gson gson = new Gson();
	private AmomeApp app;
	public static FootRulerInfo footRulerInfo;
	private int[] sizeArr;
	private String realShoeSize;// 根据鞋码表得到的鞋码
	private String nickname = "";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_RULERDATA:
					String footRulerJson = (String) msg.obj;
					if (footRulerJson.equals("[{}]")) { // 如果查询结果为空（新用户）
						width = "100";
						height = "270";
						initView();
						tv_long.setText(height);
						tv_width.setText(width);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								scrollLong(Integer.parseInt(height) - 100);
								scrollWidth(Integer.parseInt(width) - 50);
							}
						}, 400);

						DialogUtil.hideProgressDialog();
					} else {
						app.FootRulerList = gson.fromJson(footRulerJson,
								new TypeToken<List<FootRulerInfo>>() {
								}.getType());
						footRulerInfo = app.FootRulerList.get(0);
						initView();
						initData(footRulerInfo);
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
		setContentView(R.layout.activity_ruler_foot_right);
		mContext = this;
		nickname = getIntent().getStringExtra("nickname");
		getNetData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("右脚");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.ruler_next).setOnClickListener(this);
		findViewById(R.id.ll_rultip).setOnClickListener(this);
		tv_long = (TextView) findViewById(R.id.tv_shownum_l); // 脚长
		tv_width = (TextView) findViewById(R.id.tv_shownum_w); // 脚宽

		ruler_width = (HorizontalScrollView) findViewById(R.id.hruler_w);
		ruler_width.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				Log.i(TAG, " ruler_width:" + ruler_width.getScrollX());
				tv_width.setText(String.valueOf((int) Math.ceil((ruler_width
						.getScrollX()) / 20 + 50)));
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							tv_width.setText(String.valueOf((int) Math
									.ceil((ruler_width.getScrollX()) / 20 + 50)));
						}
					}, 500);
					break;
				}
				return false;
			}

		});
		ruler_long = (HorizontalScrollView) findViewById(R.id.hruler_l);
		ruler_long.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				tv_long.setText(String.valueOf((int) Math.ceil((ruler_long
						.getScrollX()) / 20 + 100)));
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							tv_long.setText(String.valueOf((int) Math
									.ceil((ruler_long.getScrollX()) / 20 + 100)));
						}
					}, 500);
					break;
				}
				return false;
			}

		});

		rulerlayout_long = (LinearLayout) findViewById(R.id.hruler_layout_l);
		new Handler().postDelayed((new Runnable() {
			@Override
			public void run() {
				constructRulerLong();
			}
		}), 300);

		rulerlayout_width = (LinearLayout) findViewById(R.id.hruler_layout_w);
		new Handler().postDelayed((new Runnable() {
			@Override
			public void run() {
				constructRulerWidth(); // 生成刻度
			}
		}), 300);
	}

	private void initData(FootRulerInfo footRulerInfo) {
		height = SpfUtil.readFtSize(mContext, Constants.RFTHEIGHT);
		width = SpfUtil.readFtSize(mContext, Constants.RFTWIDTH);
		if (!footRulerInfo.rftwidth.equals("")) {
			width = footRulerInfo.rftwidth;
		} else {
			width = "100";
		}
		if (!footRulerInfo.rftlong.equals("")) {
			height = footRulerInfo.rftlong;
		} else {
			height = "270";
		}
		tv_long.setText(height);
		tv_width.setText(width);
		// 控件滑动到指定位置
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollLong(Integer.parseInt(height) - 100);
				scrollWidth(Integer.parseInt(width) - 50);
			}
		}, 400);

		DialogUtil.hideProgressDialog();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_left:
			finishActivity();
			break;
		case R.id.ruler_next:
			String lengthStr = tv_long.getText().toString().trim();
			String widthStr = tv_width.getText().toString().trim();
			SpfUtil.keepFtSize(mContext, widthStr, Constants.RFTWIDTH);
			SpfUtil.keepFtSize(mContext, lengthStr, Constants.RFTHEIGHT);
			finish();
			Intent intent = new Intent(mContext, RulerFootLeftActivity.class);
			intent.putExtra("nickname", nickname);
			startActivity(intent);
			break;
		case R.id.ll_rultip:
			Intent in = new Intent(mContext, RulerIntroductionActivity.class);
			in.putExtra("leftValue", "右脚");
			in.putExtra("btnValue", "继续测量");
			in.putExtra("nickname", nickname);
			startActivity(in);
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

	private void getNetData() {
		DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("calltype", ClientConstant.GETRULERDATABYNAME_TYPE);
		params.put("nickname", nickname);
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_RULERDATA, params,
				ClientConstant.GET_FOOTDATA_URL);
	}

	@SuppressLint("InflateParams")
	private void constructRulerLong() {
		int rulerWidth = ruler_long.getWidth();

		View topview = (View) LayoutInflater.from(this).inflate(
				R.layout.ruler_blankhrulerunit, null);
		topview.setLayoutParams(new LayoutParams(rulerWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout_long.addView(topview);

		for (int i = 10; i < 32; i++) {
			View view = (View) LayoutInflater.from(this).inflate(
					R.layout.hrulerunit, null);
			view.setLayoutParams(new LayoutParams(200,
					LayoutParams.MATCH_PARENT));
			TextView tv = (TextView) view.findViewById(R.id.hrulerunit);
			tv.setText(String.valueOf(i * 10));
			rulerlayout_long.addView(view);
		}
		View bottomview = (View) LayoutInflater.from(this).inflate(
				R.layout.ruler_blankhrulerunit, null);
		bottomview.setLayoutParams(new LayoutParams(rulerWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout_long.addView(bottomview);
	}

	@SuppressLint("InflateParams")
	private void constructRulerWidth() {

		int rulerWidth = ruler_width.getWidth();
		View topview = (View) LayoutInflater.from(this).inflate(
				R.layout.ruler_blankhrulerunit, null);
		topview.setLayoutParams(new LayoutParams(rulerWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout_width.addView(topview);

		for (int i = 5; i < 15; i++) { // 刻度上限
			View view = (View) LayoutInflater.from(this).inflate(
					R.layout.hrulerunit, null);
			view.setLayoutParams(new LayoutParams(200, // 刻度密度
					LayoutParams.MATCH_PARENT));
			TextView tv = (TextView) view.findViewById(R.id.hrulerunit);
			tv.setText(String.valueOf(i * 10));
			rulerlayout_width.addView(view);
		}
		View bottomview = (View) LayoutInflater.from(this).inflate(
				R.layout.ruler_blankhrulerunit, null);
		bottomview.setLayoutParams(new LayoutParams(rulerWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout_width.addView(bottomview);

	}

	private void scrollWidth(int w) {
		ruler_width.smoothScrollTo(w * 20, 0);// 100
	}

	private void scrollLong(int h) {
		ruler_long.smoothScrollTo(h * 20, 0);// 270x20
	}

	/**
	 * 得到真正的码数
	 */
	private void getRealShoeSize() {
		String rHeightStr = SpfUtil.readFtSize(mContext, Constants.RFTHEIGHT);
		String lHeightStr = SpfUtil.readFtSize(mContext, Constants.LFTHEIGHT);
		int rHeight = Integer.parseInt(rHeightStr);
		int lHeight = Integer.parseInt(lHeightStr);
		int ftHeight;
		if (rHeight >= lHeight) {
			ftHeight = rHeight;
		} else {
			ftHeight = lHeight;
		}
		L.i(ClassType.RulerFootRightActivity, "ftHeight=" + ftHeight);
		if (ftHeight > sizeArr[sizeArr.length - 1]) {// 选的码数大于鞋码表里最大的
			realShoeSize = "0";
		} else if (ftHeight < sizeArr[0]) {// 选的码数小于鞋码表里最小的
			realShoeSize = "0";
		} else {
			for (int i = 0; i < sizeArr.length; i++) {
				if (ftHeight > sizeArr[i]) {
				} else {
					realShoeSize = sizeArr[i] + "";
					L.i(ClassType.RulerFootRightActivity, "realShoeSize="
							+ realShoeSize);
					SpfUtil.keepFtSize(mContext, realShoeSize,
							Constants.REALSHOESIZE);
					finish();
					// startActivity(new Intent(this,
					// RulerResultActivity.class));
					T.showToast(mContext, "未实现", 0);
					return;
				}
			}
		}
		L.i(ClassType.RulerFootRightActivity, "realShoeSize=" + realShoeSize);
		SpfUtil.keepFtSize(mContext, realShoeSize, Constants.REALSHOESIZE);
		finish();
		// startActivity(new Intent(this, RulerResultActivity.class));
		T.showToast(mContext, "未实现", 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		DialogUtil.showAlertDialog(mContext, "", "是否放弃当前本次量脚数据？", "确定", "取消",
				new OnAlertViewClickListener() {
					@Override
					public void confirm() {
						finish();
					}

					@Override
					public void cancel() {
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.RulerFootRightActivity);
		MobclickAgent.onResume(mContext);
		// height = SpfUtil.readFtSize(mContext, Constants.RFTHEIGHT);
		// width = SpfUtil.readFtSize(mContext, Constants.RFTWIDTH);
		// Log.i(TAG, "onResume");

		// if(TextUtils.isEmpty(width)){
		// width = "100";
		// }
		// if(TextUtils.isEmpty(height)){
		// height = "270";
		// }
		// tv_long.setText(height);
		// tv_width.setText(width);
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// scrollLong(Integer.parseInt(height));
		// scrollWidth(Integer.parseInt(width));
		// }
		// }, 400);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.RulerFootRightActivity);
		MobclickAgent.onPause(mContext);
	}
}
