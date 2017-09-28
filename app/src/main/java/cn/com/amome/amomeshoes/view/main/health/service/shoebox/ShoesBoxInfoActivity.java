package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.ShoeInfoProblemAdapter;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpError;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.ShoeBoxList;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.shoeencrypt.AmomeEncrypt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;

public class ShoesBoxInfoActivity extends Activity implements OnClickListener {

	private TextView tv_title, tv_left;
	private Context mContext;
	private GridView gridView_problem;
	private ImageView iv_camera;
	private TextView tv_shoesize;
	private TextView tv_brand;
	private TextView tv_category;
	private TextView tv_hell;
	private TextView tv_material;
	private TextView tv_price;
	private TextView tv_pro_num;
	private Button btn_edit;
	private ShoeInfoProblemAdapter proAdapter;
	private Gson gson = new Gson();
	private List<ShoesProblem> shoeProblemList;
	private ShoeBoxList shoeInfo;
	private List<String> problemList = new ArrayList<String>();
	private ImageLoader loader;
	private DisplayImageOptions options;
	private String token = "";
	private int reqType = -1;
	private static final int MSG_GET_TOKEN = 0;
	private static final int MSG_VERIFY_TOKEN = 1;
	private static final int MSG_GET_PROBLEM = 2;
	private int GETTOKEN_MAX_NUM = 1;// 出现错误时自动获取次数
	private int getTokenTimes = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_PROBLEM:// 鞋
					String problemJson = (String) msg.obj;
					if (TextUtils.isEmpty(problemJson)) {
					} else {
						shoeProblemList = gson.fromJson(problemJson,
								new TypeToken<List<ShoesProblem>>() {
								}.getType());
						if (shoeProblemList != null
								&& shoeProblemList.size() > 0) {
							if (shoeProblemList != null
									&& shoeProblemList.size() > 0) {
								int problem = Integer
										.parseInt(shoeInfo.problem);
								int i = 0;
								while (i <= shoeProblemList.size()) {
									L.i("", "i=" + i + "--------" + "2进制为"
											+ getIndex(problem, i));
									if (getIndex(problem, i) == 1) {
										for (int j = 0; j < shoeProblemList
												.size(); j++) {
											if (shoeProblemList.get(j).prodesnum
													.equals("" + (i + 1))) {
												String desStr = shoeProblemList
														.get(j).prodes;
												problemList.add(desStr);
											}
										}
									}
									i++;
								}
								tv_pro_num.setText(problemList.size() + "");
								proAdapter = new ShoeInfoProblemAdapter(
										mContext, problemList);
								gridView_problem.setAdapter(proAdapter);
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
		setContentView(R.layout.activity_shoebox_info);
		mContext = this;
		token = SpfUtil.readToken(mContext);
		loader = ImageLoader.getInstance();
		setOptions();
		shoeInfo = (ShoeBoxList) getIntent().getExtras()
				.getSerializable("info");
		initView();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_title.setText("详细信息");
		// tv_left.setText("鞋小箱");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		findViewById(R.id.rl_left).setOnClickListener(this);
		gridView_problem = (GridView) findViewById(R.id.gridView_problem);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		tv_shoesize = (TextView) findViewById(R.id.tv_size);
		tv_brand = (TextView) findViewById(R.id.tv_brand);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_category = (TextView) findViewById(R.id.tv_category);
		tv_hell = (TextView) findViewById(R.id.tv_heel);
		tv_material = (TextView) findViewById(R.id.tv_material);
		tv_pro_num = (TextView) findViewById(R.id.tv_pro_num);
		btn_edit = (Button) findViewById(R.id.btn_edit);
		iv_camera.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		loader.displayImage(shoeInfo.picture, iv_camera, options);
		tv_brand.setText(shoeInfo.brand);
		tv_shoesize.setText(shoeInfo.shoesize);
		tv_price.setText(shoeInfo.price);
		tv_category.setText(shoeInfo.category);
		tv_hell.setText(shoeInfo.heel);
		tv_material.setText(shoeInfo.texture);
		tv_pro_num.setText("0");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.btn_edit:
			Intent intent = new Intent(mContext, EditShoesBoxActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", shoeInfo);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			break;
		}

	}

	private void setOptions() {
		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_default)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_default)
				// 设置图片加载或解码过程中发生错误显示的图片
				// .resetViewBeforeLoading(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 鞋问题
	 */
	private void getShoesProblem() {
		// L.i(ClassType.ShoesBoxInfoActivity, "==getShoesProblem==");
		// RequestParams params = new RequestParams();
		// params.put("certificate", token);
		// params.put("calltype", ClientConstant.GETSHOEPROBLEM_TYPE);
		// params.put("useid", SpfUtil.readUserId(mContext));
		// PostAsyncTask postTask = new PostAsyncTask(mContext, mHandler,
		// ClientConstant.SHOEBOX_URL, "", "请稍等...", true, true);
		// postTask.startAsyncTask(MSG_GET_PROBLEM, params,
		// ClassType.ShoesBoxInfoActivity);
	}

	public static int getIndex(int num, int index) {
		return (num & (0x1 << index)) >> index;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ShoesBoxInfoActivity);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ShoesBoxInfoActivity);
		MobclickAgent.onResume(mContext);
	}
}
