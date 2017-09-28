/**
 * @Title:MyFragment.java 我
 * @Description:TODO<请描述此文件是做什么的>
 * @author:css
 * @data:  2015-12-4 上午9:01:13
 */
package cn.com.amome.amomeshoes.view.main.my;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.common.AmomeApp;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.UserInfo;
//import cn.com.amome.amomeshoes.util.DateUtils;                 暂时屏蔽
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.detection.stand.StandReportActivity;
import cn.com.amome.amomeshoes.view.main.health.detection.walk.WalkReportActivity;
import cn.com.amome.amomeshoes.view.main.my.secret.FootSecretActivity;
import cn.com.amome.amomeshoes.view.main.my.secret.ShoeSecretActivity;
import cn.com.amome.amomeshoes.view.main.my.setting.AppSettingActivity;
import cn.com.amome.amomeshoes.view.main.my.user.MyInfomationActivity;
import cn.com.amome.amomeshoes.widget.RoundRecImageView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

public class MyFragment extends Fragment implements OnClickListener {

	private TextView tv_nickname;
	private RoundRecImageView iv_my_avatar;
	private LinearLayout ll_my_top;
	private LinearLayout ll_sport, ll_health, ll_social, ll_game;
	private ImageView iv_my_secret_sport, iv_my_secret_shoe,
			iv_my_secret_posture, iv_my_secret_foot;
	private RelativeLayout rl_setting, rl_feedback;

	private String TAG = "MyFragment";
	private Context mContext;
	private TextView tv_lv_motion, tv_lv_healthy, tv_my_bind, tv_my_line;
	private TextView tv_checkin;
	private PieChart pieSport, pieHealth, pieSocial, pieGame;

	private List<UserInfo> userInfoList;
	private UserInfo userinfo;
	// 下载图片
	private ImageLoader loader;
	private DisplayImageOptions options;
	private String token = "";
	private int reqType = -1;
	private Gson gson = new Gson();
	private static final int MSG_GET_INTEGRAL = 2;
	private static final int MSG_CHECKIN = 3;
	private static final int MSG_GET_USERINFO = 1;
	private AmomeApp app;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_USERINFO:
					String json = (String) msg.obj;
					if (TextUtils.isEmpty(json)) {
					} else {
						userInfoList = gson.fromJson(json,
								new TypeToken<List<UserInfo>>() {
								}.getType());
						if (userInfoList != null && userInfoList.size() > 0) {
							userinfo = userInfoList.get(0);
							SpfUtil.keepMyAvatarUrl(mContext, userinfo.icon);
							SpfUtil.keepNick(mContext, userinfo.nickname);
							tv_nickname.setText(userinfo.nickname);
							if (!TextUtils.isEmpty(userinfo.icon)) {
								try {
									String avatarPath = Environments
											.getImageCachPath(mContext)
											+ "/"
											+ SpfUtil.readUserId(mContext)
											+ "_avatar.jpg";// 头像存储
									AsynHttpDowanloadFile.downloadImageFile(
											userinfo.icon, avatarPath);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							initAvatar();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_my_main, null, false);
		mContext = getActivity();
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(mContext));
		setOptions();
		getInfo();
		initView(view);
		// token = SpfUtil.readToken(mContext);

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub

		// 头像+昵称
		tv_nickname = (TextView) view.findViewById(R.id.tv_my_nickname);
		iv_my_avatar = (RoundRecImageView) view
				.findViewById(R.id.iv_my_avatar1);
		iv_my_avatar.setOnClickListener(this);
		initAvatar();
		setOptions();

		// 秘密图片
		iv_my_secret_sport = (ImageView) view
				.findViewById(R.id.iv_my_secret_sport);
		iv_my_secret_shoe = (ImageView) view
				.findViewById(R.id.iv_my_secret_shoe);
		iv_my_secret_posture = (ImageView) view
				.findViewById(R.id.iv_my_secret_posture);
		iv_my_secret_foot = (ImageView) view
				.findViewById(R.id.iv_my_secret_foot);
		iv_my_secret_sport.setOnClickListener(this);
		iv_my_secret_shoe.setOnClickListener(this);
		iv_my_secret_posture.setOnClickListener(this);
		iv_my_secret_foot.setOnClickListener(this);

		// 圆环外层布局
		ll_sport = (LinearLayout) view.findViewById(R.id.ll_sport);
		ll_health = (LinearLayout) view.findViewById(R.id.ll_health);
		ll_social = (LinearLayout) view.findViewById(R.id.ll_social);
		ll_game = (LinearLayout) view.findViewById(R.id.ll_game);
		ll_sport.setOnClickListener(this);
		ll_health.setOnClickListener(this);
		ll_social.setOnClickListener(this);
		ll_game.setOnClickListener(this);

		// 圆环
		pieSport = (PieChart) view.findViewById(R.id.pie_chart_sport);
		pieHealth = (PieChart) view.findViewById(R.id.pie_chart_health);
		pieSocial = (PieChart) view.findViewById(R.id.pie_chart_social);
		pieGame = (PieChart) view.findViewById(R.id.pie_chart_game);
		PieData mPieData1 = getPieData1(100, 100, 0, Color.rgb(255, 154, 74));
		showChart(pieSport, mPieData1, "运动力\nv0");
		PieData mPieData2 = getPieData1(100, 100, 0, Color.rgb(255, 74, 197));
		showChart(pieHealth, mPieData2, "健康力\nv0");
		PieData mPieData3 = getPieData1(100, 100, 0, Color.rgb(255, 64, 112));
		showChart(pieSocial, mPieData3, "社交力\nv0");
		PieData mPieData4 = getPieData1(100, 100, 0, Color.rgb(66, 162, 255));
		showChart(pieGame, mPieData4, "游戏力\nv0");

		// “设置”和“问题反馈”
		rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
		rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
		view.findViewById(R.id.rl_setting).setOnClickListener(this);
		view.findViewById(R.id.rl_feedback).setOnClickListener(this);
	}

	private void showChart(PieChart pieChart, PieData pieData, String descri) {
		pieChart.setHoleColorTransparent(true);

		// pieChart.setTransparentCircleRadius(64f); // 半透明圈 不需要

		/**
		 * 是否使用百分比
		 */
		pieChart.setUsePercentValues(false);
		/**
		 * 描述信息
		 */
		pieChart.setDescription("");

		// mChart.setDrawYValues(true);
		/**
		 * 设置圆环中间的文字
		 */
		pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字
		/**
		 * 是否显示圆环中间的洞
		 */
		pieChart.setDrawHoleEnabled(true);
		/**
		 * 设置中间洞的颜色
		 */
		// pieChart.setHoleColor(Color.WHITE);
		/**
		 * 设置圆环中间洞的半径
		 */
		pieChart.setHoleRadius(88f);

		// pieChart.setRotationAngle(-90); // 初始旋转角度

		pieChart.setRotationEnabled(false); // 可以手动旋转

		pieChart.setUsePercentValues(true); // 显示成百分比

		pieChart.setCenterText(descri); // 饼状图中间的文字

		pieChart.setTouchEnabled(true); // 设置能否触摸

		// 设置数据
		pieChart.setData(pieData);

		Legend mLegend = pieChart.getLegend(); // 设置比例图
		mLegend.setPosition(LegendPosition.RIGHT_OF_CHART); // 最右边显示
		mLegend.setEnabled(false); // 不显示图例
		// mLegend.setForm(LegendForm.LINE); //设置比例图的形状，默认是方形
		mLegend.setXEntrySpace(7f);
		mLegend.setYEntrySpace(5f);

		pieChart.animateXY(1000, 1000); // 设置动画

	}

	/**
	 * 
	 * @param count
	 *            分成几部分
	 * @param range
	 */
	private PieData getPieData1(float range, int one, int two, int color) {

		ArrayList<String> xValues = new ArrayList<String>(); // xVals用来表示每个饼块上的内容

		for (int i = 0; i < 2; i++) {
			xValues.add(""); // 饼块上显示成Quarterly1, Quarterly2, Quarterly3,
								// Quarterly4
		}

		ArrayList<Entry> yValues = new ArrayList<Entry>(); // yVals用来表示封装每个饼块的实际数据

		// 饼图数据
		/**
		 * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14%
		 */
		float quarterly1 = one;
		float quarterly2 = two;

		yValues.add(new Entry(quarterly1, 0));
		yValues.add(new Entry(quarterly2, 1));

		// y轴的集合
		// PieDataSet pieDataSet = new PieDataSet(yValues,
		// "Quarterly Revenue 2014"/*显示在比例图上*/); 不加文字
		PieDataSet pieDataSet = new PieDataSet(yValues, "");
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px); // 选中态多出的长度
		pieDataSet.setSelectionShift(0f); // 选中态多出的长度

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 饼图颜色
		colors.add(Color.rgb(201, 201, 201));
		colors.add(color);

		pieDataSet.setColors(colors);

		PieData pieData = new PieData(xValues, pieDataSet);
		pieData.setValueTextSize(0f); // 设置0，间接屏蔽比例数值 //不然饼转图显示10 90比例值

		return pieData;
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			// DialogUtil.hideProgressDialog();
			String result;
			switch (type) {
			case MSG_GET_USERINFO:
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
					Log.i(TAG, "MSG_GET_USERINFO解析失败");
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
	 * 获取个人信息
	 */
	private void getInfo() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETINFOMATION);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_USERINFO, params,
				ClientConstant.USERINFO_URL);

	}

	private void initAvatar() {
		String imgpath = Environments.getImageCachPath(mContext) + "/"
				+ SpfUtil.readUserId(mContext) + "_avatar.jpg";// 头像存储
		File file = new File(imgpath);
		if (file.exists() && file.length() > 0) {
			FileInputStream f;
			try {
				f = new FileInputStream(imgpath);
				Bitmap bm = null;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;// 图片的长宽都是原来的1/1
				BufferedInputStream bis = new BufferedInputStream(f);
				bm = BitmapFactory.decodeStream(bis, null, options);
				iv_my_avatar.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			loader.displayImage(SpfUtil.readMyAvatarUrl(mContext),
					iv_my_avatar, options);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_avatar1:
			startActivity(new Intent(mContext, MyInfomationActivity.class)
					.putExtra("leftvalue", "我"));
			break;
		case R.id.rl_setting:
			startActivity(new Intent(mContext, AppSettingActivity.class));
			break;
		case R.id.rl_feedback:
			startActivity(new Intent(mContext, SendOpinionActivity.class));
			break;
		case R.id.iv_my_secret_foot:
			Intent intent = new Intent(mContext, FootSecretActivity.class);
			intent.putExtra("nickname", "0自己");
			startActivity(intent);
			break;
		case R.id.iv_my_secret_sport:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.iv_my_secret_shoe:
			Intent intent1 = new Intent(mContext, ShoeSecretActivity.class);
			intent1.putExtra("nickname", "0自己");
			startActivity(intent1);
			break;
		case R.id.iv_my_secret_posture:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.pie_chart_sport:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.pie_chart_health:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.pie_chart_social:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.pie_chart_game:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.ll_sport:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.ll_health:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.ll_social:
			T.showToast(mContext, "暂未开通", 0);
			break;
		case R.id.ll_game:
			T.showToast(mContext, "暂未开通", 0);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void setOptions() {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_avatar)
				// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_avatar)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_avatar)
				// 设置图片加载或解码过程中发生错误显示的图片
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(0)).build();// 是否图片加载好后渐入的动画时间，可能会出现闪动
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Log.i(TAG, TAG + "===onHiddenChanged====");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		L.i(TAG, TAG + "==onResume==");
		MobclickAgent.onPageStart(ClassType.MyFragment);
		if (SpfUtil.readIsUpdataAvatar(mContext)) {
			loader.clearMemoryCache();
			loader.clearDiskCache();
			initAvatar();
			SpfUtil.keepIsUpdataAvatar(mContext, false);
		}
		if (SpfUtil.readNick(mContext).equals("")) {
			String userid = SpfUtil.readUserId(mContext);
			tv_nickname.setText(userid);
		} else {
			tv_nickname.setText(SpfUtil.readNick(mContext));
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.MyFragment);
	}
}
