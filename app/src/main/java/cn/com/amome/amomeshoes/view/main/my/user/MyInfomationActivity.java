package cn.com.amome.amomeshoes.view.main.my.user;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.AsynHttpDowanloadFile;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.model.HobbyInfo;
import cn.com.amome.amomeshoes.model.JobInfo;
import cn.com.amome.amomeshoes.model.UserInfo;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.DialogUtil.OnAlertViewClickListener;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.SensitivewordFilter;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.widget.CircleImageView;
import cn.com.amome.amomeshoes.widget.RoundRecImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

/**
 * 我-个人信息
 * 
 * @author css
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MyInfomationActivity extends Activity implements OnClickListener {
	private String TAG = "MyInfomationActivity";
	private TextView tv_title, tv_left, tv_right;
	private ImageView iv_left;
	private Context mContext;
	private RelativeLayout rl_avtar;
	private TextView tv_id, tv_integral, tv_birth, tv_weight, tv_height,
			tv_job, tv_hobby;
	private EditText et_tel, et_address, et_realname, et_postalcode;
	private EditText et_name;
	private RadioButton rb_man, rb_female;
	private RoundRecImageView iv_my_avatar;
	private RadioGroup rg_group;
	private int type = -1;
	private int sexStr = 1;// 1男，0女
	private String iconurl = "";
	private String jobId = "";
	private String jobName = "";
	private String hobbyId = "";
	private String hobbyName = "";
	private String name, birth, height, weight, tel, address, realname,
			postalcode;
	private Gson gson = new Gson();
	private List<UserInfo> userInfoList;
	private UserInfo userinfo;
	private List<JobInfo> jobList;
	private List<HobbyInfo> hobbyList;
	private final int REQ_SELECT_JOB = 0x13;
	private final int REQ_SET_AVATAR = 0x14;
	private final int REQ_SELECT_HOBBY = 0x15;
	private boolean isSetAatar;
	private boolean isEdit;// 是否正在修改个人信息
	private boolean isSave = false;
	private String token = "";
	private int reqType = -1;
	private static final int MSG_GET_HOBBYLIST = 0;
	private static final int MSG_GET_JOBLIST = 2;
	private static final int MSG_GET_USERINFO = 3;
	private static final int MSG_SET_USERINFO = 4;
	private static final int MSG_GET_MD5 = 1;
	private static final int MSG_GET_MD5_modified = 5;
	private static final int MSG_GET_MD5_not_modified = 6;
	// 下载图片
	private ImageLoader loader;
	private DisplayImageOptions options;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_MD5_modified:
					String sentivejson = (String) msg.obj;
					try {
						JSONArray jsonArray = new JSONArray(sentivejson);
						JSONObject jsonObject = (JSONObject) jsonArray
								.getJSONObject(0);
						SpfUtil.keepMD5(mContext, jsonObject.getString("md5"));
						String lexicon = jsonObject.getString("lexicon");
						lexicon = lexicon.replace("\",\"", "\n");
						lexicon = lexicon.replace("[\"", "\n");
						lexicon = lexicon.replace("\"]", "\n");
						save(lexicon);
						Log.i(TAG, "md5解析成功");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i(TAG, "md5解析失败");
						e.printStackTrace();
					}
					if (isSave) {
						SensitivewordFilter filter = new SensitivewordFilter();
						String string = et_name.getText().toString().trim();
						Set<String> set = filter.getSensitiveWord(string, 1);
						if (set.size() == 0) {
							setInfo();
						} else {
							T.showToast(mContext, "用户名输入不合法", 0);
							isEdit = true;
							tv_right.setText("保存");
							tv_right.setTextColor(Color.WHITE);
							tv_right.setBackground(getResources().getDrawable(
									R.drawable.conner_red_bg_rectangle));
							rl_avtar.setClickable(true);
							et_name.setEnabled(true);
							et_tel.setEnabled(true);
							et_address.setEnabled(true);
							et_realname.setEnabled(true);
							et_postalcode.setEnabled(true);
							tv_job.setEnabled(true);
							tv_hobby.setEnabled(true);
							rb_man.setClickable(true);
							rb_female.setClickable(true);
							tv_birth.setClickable(true);
							tv_height.setClickable(true);
							tv_weight.setClickable(true);

							et_name.setFocusable(true);
							et_name.setFocusableInTouchMode(true);
							et_name.requestFocus();
							et_name.setTextColor(Color.BLACK);
							et_tel.setTextColor(Color.BLACK);
							et_address.setTextColor(Color.BLACK);
							et_realname.setTextColor(Color.BLACK);
							et_postalcode.setTextColor(Color.BLACK);
							tv_birth.setTextColor(Color.BLACK);
							tv_height.setTextColor(Color.BLACK);
							tv_weight.setTextColor(Color.BLACK);
							tv_job.setTextColor(Color.BLACK);
							tv_hobby.setTextColor(Color.BLACK);
						}
					}
					break;
				case MSG_GET_MD5_not_modified:
					SensitivewordFilter filter = new SensitivewordFilter();
					String string = et_name.getText().toString().trim();
					Set<String> set = filter.getSensitiveWord(string, 1);
					if (set.size() == 0) {
						setInfo();
					} else {
						T.showToast(mContext, "用户名输入不合法", 0);
						isEdit = true;
						tv_right.setText("保存");
						tv_right.setTextColor(Color.WHITE);
						tv_right.setBackground(getResources().getDrawable(
								R.drawable.conner_red_bg_rectangle));
						rl_avtar.setClickable(true);
						et_name.setEnabled(true);
						et_tel.setEnabled(true);
						et_address.setEnabled(true);
						et_realname.setEnabled(true);
						et_postalcode.setEnabled(true);
						tv_job.setEnabled(true);
						tv_hobby.setEnabled(true);
						rb_man.setClickable(true);
						rb_female.setClickable(true);
						tv_birth.setClickable(true);
						tv_height.setClickable(true);
						tv_weight.setClickable(true);
						et_name.setFocusable(true);
						et_name.setFocusableInTouchMode(true);
						et_name.requestFocus();
						et_name.setTextColor(Color.BLACK);
						et_tel.setTextColor(Color.BLACK);
						et_address.setTextColor(Color.BLACK);
						et_realname.setTextColor(Color.BLACK);
						et_postalcode.setTextColor(Color.BLACK);
						tv_birth.setTextColor(Color.BLACK);
						tv_height.setTextColor(Color.BLACK);
						tv_weight.setTextColor(Color.BLACK);
						tv_job.setTextColor(Color.BLACK);
						tv_hobby.setTextColor(Color.BLACK);
					}
					break;
				case MSG_GET_USERINFO:
					String json = (String) msg.obj;
					if (isEdit) {
						tv_right.setText("编辑");
						tv_right.setTextColor(mContext.getResources().getColor(
								R.color.rosered));
						// Resources resource = (Resources) mContext
						// .getResources();
						// ColorStateList csl = (ColorStateList) resource
						// .getColorStateList(R.color.register_blue);
						// tv_right.setTextColor(csl);
						tv_right.setBackground(getResources().getDrawable(
								R.drawable.conner_gray_bg_rectangle));
					}
					isEdit = false;
					if (TextUtils.isEmpty(json)) {
					} else {
						userInfoList = gson.fromJson(json,
								new TypeToken<List<UserInfo>>() {
								}.getType());
						if (userInfoList != null && userInfoList.size() > 0) {
							userinfo = userInfoList.get(0);
							SpfUtil.keepMyAvatarUrl(mContext, userinfo.icon);
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
							// initAvatar();
							// initView();
							initData();
						}
					}
					break;
				case MSG_SET_USERINFO:
					// DialogUtil.hideProgressDialog();
					T.showToast(mContext, "添加成功 ！", 0);
					getInfo();
					break;
				case MSG_GET_JOBLIST:
					String jobJson = (String) msg.obj;
					if (TextUtils.isEmpty(jobJson)) {
						T.showToast(mContext, "获取失败 ！", 0);
					} else {
						jobList = gson.fromJson(jobJson,
								new TypeToken<List<JobInfo>>() {
								}.getType());
						getHobbyList();
					}
					break;
				case MSG_GET_HOBBYLIST:
					String hobbyJson = (String) msg.obj;
					if (TextUtils.isEmpty(hobbyJson)) {
						T.showToast(mContext, "获取失败 ！", 0);
					} else {
						hobbyList = gson.fromJson(hobbyJson,
								new TypeToken<List<HobbyInfo>>() {
								}.getType());
						getInfo();
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_personinfomation);
		mContext = this;
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(mContext));
		setOptions();
		initView();
		getInfo();
		// getJobList();

	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		tv_right = (TextView) findViewById(R.id.right_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText("个人信息");
		tv_title.setTextColor(mContext.getResources().getColor(R.color.rosered));
		iv_left.setImageResource(R.drawable.ic_back_rosered);
		// tv_left.setText(getIntent().getStringExtra("leftvalue"));
		tv_right.setText("编辑");
		tv_right.setTextColor(mContext.getResources().getColor(R.color.rosered));
		findViewById(R.id.rl_left).setOnClickListener(this);
		tv_right.setOnClickListener(this);
		// tv_integral = (TextView) findViewById(R.id.tv_integral); 屏蔽积分
		rl_avtar = (RelativeLayout) findViewById(R.id.rl_avtar);
		iv_my_avatar = (RoundRecImageView) findViewById(R.id.iv_my_avatar);
		tv_id = (TextView) findViewById(R.id.tv_id);
		rb_man = (RadioButton) findViewById(R.id.rb_sex_protab1);
		rb_female = (RadioButton) findViewById(R.id.rb_sex_protab2);
		et_name = (EditText) findViewById(R.id.et_nickname);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_address = (EditText) findViewById(R.id.et_address);
		et_realname = (EditText) findViewById(R.id.et_realname);
		et_postalcode = (EditText) findViewById(R.id.et_postalcode);
		tv_birth = (TextView) findViewById(R.id.tv_birth);
		tv_weight = (TextView) findViewById(R.id.tv_wei);
		tv_height = (TextView) findViewById(R.id.tv_heig);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_hobby = (TextView) findViewById(R.id.tv_hobby);
		rl_avtar.setOnClickListener(this);
		tv_job.setOnClickListener(this);
		tv_hobby.setOnClickListener(this);
		tv_birth.setOnClickListener(this);
		tv_height.setOnClickListener(this);
		tv_weight.setOnClickListener(this);
		tv_title.setFocusable(true);
		tv_title.setFocusableInTouchMode(true);
		tv_title.requestFocus();
		initAvatar();

		rg_group = (RadioGroup) findViewById(R.id.rg_sex_protabs);
		rb_man = (RadioButton) findViewById(R.id.rb_sex_protab1);
		rb_female = (RadioButton) findViewById(R.id.rb_sex_protab2);
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_sex_protab1:
					sexStr = 1;
					break;
				case R.id.rb_sex_protab2:
					sexStr = 0;
					break;

				default:
					break;
				}
			}
		});
		et_name.setEnabled(false);
		et_tel.setEnabled(false);
		et_address.setEnabled(false);
		et_realname.setEnabled(false);
		et_postalcode.setEnabled(false);
		tv_job.setEnabled(false);
		tv_hobby.setEnabled(false);
		rb_man.setClickable(false);
		rb_female.setClickable(false);
		tv_birth.setClickable(false);
		tv_height.setClickable(false);
		tv_weight.setClickable(false);
		rl_avtar.setClickable(false);
		// tv_integral.setText("积分:" + SpfUtil.readIntegral(mContext)); 屏蔽积分
		tv_id.setText("ID:" + SpfUtil.readUserId(mContext));
	}

	protected void initData() {

		jobId = userinfo.job;
		hobbyId = userinfo.hobby;
		if (isSetAatar) {
			SpfUtil.keepIsUpdataAvatar(mContext, isSetAatar);
			loader.clearMemoryCache();
			loader.clearDiskCache();
		}
		SpfUtil.keepMyAvatarUrl(mContext, userinfo.icon);
		SpfUtil.keepNick(mContext, userinfo.nickname);
		if (!TextUtils.isEmpty(userinfo.icon)) {
			try {
				String avatarPath = Environments.getImageCachPath(mContext)
						+ "/" + SpfUtil.readUserId(mContext) + "_avatar.jpg";// 头像存储
				AsynHttpDowanloadFile.downloadImageFile(userinfo.icon,
						avatarPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// if(!TextUtils.isEmpty(userinfo.icon)) {
		// try {
		// String avatarPath = Environments
		// .getImageCachPath(mContext) + "/"
		// + SpfUtil.readUserId(mContext) + "_avatar.jpg";// 头像存储
		// AsynHttpDowanloadFile.downloadImageFile(userinfo.icon, avatarPath);
		// try {
		// FileInputStream fis = new FileInputStream(avatarPath);
		// iv_my_avatar.setImageBitmap(BitmapFactory.decodeStream(fis));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// initAvatar(); 屏蔽
		et_name.setText(userinfo.nickname);
		et_tel.setText(userinfo.telephone);
		et_address.setText(userinfo.location);
		et_realname.setText(userinfo.realname);
		et_postalcode.setText(userinfo.postalcode);
		if (userinfo.sex.equals("0")) {
			rb_female.setChecked(true);
			rb_man.setChecked(false);
		} else if (userinfo.sex.equals("1")) {
			rb_man.setChecked(true);
			rb_female.setChecked(false);
		}
		tv_birth.setText(userinfo.birthday);
		tv_height.setText(userinfo.height + "cm");
		tv_weight.setText(userinfo.weight + "kg");
		if (jobList != null && jobList.size() > 0) {
			for (int i = 0; i < jobList.size(); i++) {
				if (userinfo.job.equals(jobList.get(i).jobnum))
					tv_job.setText(jobList.get(i).jobdes);
			}
		}
		if (hobbyList != null && hobbyList.size() > 0) {
			for (int i = 0; i < hobbyList.size(); i++) {
				if (userinfo.hobby.equals(hobbyList.get(i).hobbynum))
					tv_hobby.setText(hobbyList.get(i).hobbysdes);
			}
		}
		et_name.setTextColor(Color.GRAY);
		et_tel.setTextColor(Color.GRAY);
		et_address.setTextColor(Color.GRAY);
		et_realname.setTextColor(Color.GRAY);
		et_postalcode.setTextColor(Color.GRAY);
		tv_birth.setTextColor(Color.GRAY);
		tv_height.setTextColor(Color.GRAY);
		tv_weight.setTextColor(Color.GRAY);
		tv_job.setTextColor(Color.GRAY);
		tv_hobby.setTextColor(Color.GRAY);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			String str1 = tv_right.getText().toString().trim();
			if (str1.equals("编辑"))
				finish();
			else if (str1.equals("保存"))
				finishActivity();
			break;
		case R.id.right_tv:
			String str = tv_right.getText().toString().trim();
			if (str.equals("编辑")) {
				isEdit = true;
				tv_right.setText("保存");
				tv_right.setTextColor(Color.WHITE);
				tv_right.setBackground(getResources().getDrawable(
						R.drawable.conner_red_bg_rectangle));
				rl_avtar.setClickable(true);
				et_name.setEnabled(true);
				et_tel.setEnabled(true);
				et_address.setEnabled(true);
				et_realname.setEnabled(true);
				et_postalcode.setEnabled(true);
				tv_job.setEnabled(true);
				tv_hobby.setEnabled(true);
				rb_man.setClickable(true);
				rb_female.setClickable(true);
				tv_birth.setClickable(true);
				tv_height.setClickable(true);
				tv_weight.setClickable(true);
				et_name.setFocusable(true);
				et_name.setFocusableInTouchMode(true);
				et_name.requestFocus();
				// ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				// 屏蔽键盘自动弹出
				// .showSoftInput(et_name, 0);
				et_name.setTextColor(Color.BLACK);
				et_tel.setTextColor(Color.BLACK);
				et_address.setTextColor(Color.BLACK);
				et_realname.setTextColor(Color.BLACK);
				et_postalcode.setTextColor(Color.BLACK);
				tv_birth.setTextColor(Color.BLACK);
				tv_height.setTextColor(Color.BLACK);
				tv_weight.setTextColor(Color.BLACK);
				tv_job.setTextColor(Color.BLACK);
				tv_hobby.setTextColor(Color.BLACK);
			} else if (str.equals("保存")) {
				verify();
			}
			break;
		case R.id.tv_birth:
			type = 1;
			initPop(R.layout.pop_birthday);
			showPop(type);
			break;
		case R.id.tv_wei:
			type = 2;
			initPop(R.layout.pop_weight);
			showPop(type);
			break;
		case R.id.tv_heig:
			type = 3;
			initPop(R.layout.pop_height);
			showPop(type);
			break;
		case R.id.tv_ok:
			UpdateUi(type);
			break;
		case R.id.tv_cancle:
			pop.dismiss();
			break;
		case R.id.tv_job:
			Intent intent = new Intent(mContext, JobListActivity.class);
			intent.putExtra("job", tv_job.getText().toString().trim());
			startActivityForResult(intent, REQ_SELECT_JOB);
			break;
		case R.id.tv_hobby:
			Intent intent_hob = new Intent(mContext, HobbyListActivity.class);
			intent_hob.putExtra("hobby", tv_hobby.getText().toString().trim());
			startActivityForResult(intent_hob, REQ_SELECT_HOBBY);
			break;
		case R.id.rl_avtar:
			initPopAvtar(R.layout.pop_takephoto);
			break;
		// 相册
		case R.id.rl_album:
			Intent i = new Intent(mContext, ClipAvatarActivity.class);
			i.putExtra("avatar", "album");
			startActivityForResult(i, REQ_SET_AVATAR);
			pop.dismiss();
			break;
		// 拍照
		case R.id.rl_takephoto:
			Intent intent2 = new Intent(mContext, ClipAvatarActivity.class);
			intent2.putExtra("avatar", "takephoto");
			startActivityForResult(intent2, REQ_SET_AVATAR);
			pop.dismiss();
			break;
		case R.id.rl_cancle:
			pop.dismiss();
			break;
		case R.id.rl_mian:// 点击外部pop消失
			pop.dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 * 保存、验证是否有数据
	 */
	private void verify() {
		name = et_name.getText().toString().trim();
		tel = et_tel.getText().toString().trim();
		address = et_address.getText().toString().trim();
		realname = et_realname.getText().toString().trim();
		postalcode = et_postalcode.getText().toString().trim();
		birth = tv_birth.getText().toString().trim();
		height = tv_height.getText().toString().trim();
		weight = tv_weight.getText().toString().trim();
		jobName = tv_job.getText().toString().trim();
		hobbyName = tv_hobby.getText().toString().trim();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(birth)
				|| TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
			T.showToast(mContext, "有东西忘记填写啦!", 0);
			return;
		} else {
			if (name.length() < 2) {
				T.showToast(mContext, "请输入2-12个字符的昵称!", 0);
				return;
			}
			if (name.length() > 12) {
				T.showToast(mContext, "请输入2-12个字符的昵称!", 0);
				return;
			}
		}

		et_name.setEnabled(false);
		et_tel.setEnabled(false);
		et_address.setEnabled(false);
		et_realname.setEnabled(false);
		et_postalcode.setEnabled(false);
		tv_job.setEnabled(false);
		tv_hobby.setEnabled(false);
		rb_man.setClickable(false);
		rb_female.setClickable(false);
		tv_birth.setClickable(false);
		tv_height.setClickable(false);
		tv_weight.setClickable(false);
		rl_avtar.setClickable(false);
		// setInfo();
		getlexicon();

	}

	/**
	 * 更新UI
	 * 
	 * @param type
	 *            1性别；2体重；3身高
	 */
	private void UpdateUi(int type) {
		if (type == 1) {
			int year = datePick1.getYear();
			int month = datePick1.getMonth() + 1;
			int day = datePick1.getDayOfMonth();
			String birthday = year + "-" + month + "-" + day;
			int age = Calendar.getInstance().get(Calendar.YEAR) - year;
			if (age > 0) {
				tv_birth.setText(birthday);
				pop.dismiss();
			} else
				T.showToast(mContext, "不可能出生在未来!", 0);
		} else if (type == 2) {
			int weightValue = numPick1.getValue();
			tv_weight.setText(weightValue + "kg");
			pop.dismiss();
		} else if (type == 3) {
			int heightValue = numPick2.getValue();
			tv_height.setText(heightValue + "cm");
			pop.dismiss();
		}
	}

	private PopupWindow pop;
	private View layout;
	private DatePicker datePick1;
	private TextView tv_ok, tv_cancle;
	private NumberPicker numPick1, numPick2;
	private RelativeLayout rl_takephoto, rl_album, rl_cancle, rl_mian;

	/**
	 * 实例化并创建pop菜单
	 */
	@SuppressWarnings("deprecation")
	private void initPop(int layoutId) {
		// 获得layout布局
		layout = (RelativeLayout) LayoutInflater
				.from(MyInfomationActivity.this).inflate(layoutId, null);
		// 设置popwindow菜单大小位置
		pop = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		pop.setContentView(layout);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.showAtLocation(this.findViewById(R.id.rl_person_mian),
				Gravity.BOTTOM, 0, 0);
		tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		tv_cancle = (TextView) layout.findViewById(R.id.tv_cancle);
		tv_ok.setOnClickListener(this);
		tv_cancle.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void initPopAvtar(int layoutId) {
		// 获得layout布局
		layout = (RelativeLayout) LayoutInflater
				.from(MyInfomationActivity.this).inflate(layoutId, null);
		// 设置popwindow菜单大小位置
		pop = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		pop.setContentView(layout);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style.PopupAnimation);
		pop.showAtLocation(this.findViewById(R.id.rl_person_mian),
				Gravity.BOTTOM, 0, 0);
		rl_mian = (RelativeLayout) layout.findViewById(R.id.rl_mian);
		rl_cancle = (RelativeLayout) layout.findViewById(R.id.rl_cancle);
		rl_takephoto = (RelativeLayout) layout.findViewById(R.id.rl_takephoto);
		rl_album = (RelativeLayout) layout.findViewById(R.id.rl_album);
		rl_mian.setOnClickListener(this);
		rl_cancle.setOnClickListener(this);
		rl_takephoto.setOnClickListener(this);
		rl_album.setOnClickListener(this);
	}

	private void showPop(int type) {
		if (type == 1) {
			datePick1 = (DatePicker) layout.findViewById(R.id.datePicker);
			datePick1
					.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
			datePick1.setCalendarViewShown(false);// 去掉右边的日历
			String birStr = tv_birth.getText().toString().trim();
			if (TextUtils.isEmpty(birStr)) {
				birStr = "1991-5-16";
			}
			String[] birArr = birStr.split("-");
			int year = Integer.parseInt(birArr[0]);
			int month = Integer.parseInt(birArr[1]);
			int day = Integer.parseInt(birArr[2]);
			datePick1.init(year, month, day, new OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// 设置当前日期
					Calendar calendar = Calendar.getInstance();
					calendar.set(year, monthOfYear, dayOfMonth);
				}
			});
			datePick1
					.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		} else if (type == 2) {
			String weightString = tv_weight.getText().toString().trim();
			String weightStr = "";
			if (weightString.length() > 2) {
				weightStr = weightString
						.substring(0, weightString.length() - 2);

			}
			if (TextUtils.isEmpty(weightStr)) {
				weightStr = "55";
			}

			numPick1 = (NumberPicker) layout.findViewById(R.id.weight_Picker);
			numPick1.setMinValue(10);
			numPick1.setMaxValue(150);
			numPick1.setValue(Integer.parseInt(weightStr));
			numPick1.setFocusable(false);
			numPick1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		} else if (type == 3) {
			String heightString = tv_height.getText().toString().trim();
			String heightStr = "";
			if (heightString.length() > 2) {
				heightStr = heightString
						.substring(0, heightString.length() - 2);
			}
			if (TextUtils.isEmpty(heightStr)) {
				heightStr = "170";
			}
			numPick2 = (NumberPicker) layout.findViewById(R.id.height_Picker);
			numPick2.setMinValue(50);
			numPick2.setMaxValue(300);
			numPick2.setValue(Integer.parseInt(heightStr));
			numPick2.setFocusable(false);
			numPick2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		}
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			// DialogUtil.hideProgressDialog();
			String result;
			switch (type) {
			case MSG_GET_HOBBYLIST:
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
					Log.i(TAG, "MSG_GET_HOBBYLIST解析失败");
				}
				break;
			case MSG_GET_JOBLIST:
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
					Log.i(TAG, "MSG_GET_JOBLIST解析失败");
				}
				break;
			case MSG_SET_USERINFO:
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
					Log.i(TAG, "MSG_SET_USERINFO解析失败");
				}
				break;
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
			case MSG_GET_MD5:
				result = new String(responseBody);
				try {
					JSONObject obj = new JSONObject(result);

					int return_code = obj.getInt("return_code");
					Message msg = Message.obtain();
					if (return_code == 0) {
						String return_msg = obj.getString("return_msg");
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = MSG_GET_MD5_modified;
						msg.obj = return_msg;
					}
					if (return_code == 2) {
						msg.what = ClientConstant.HANDLER_SUCCESS;
						msg.arg1 = MSG_GET_MD5_not_modified;
						msg.obj = null;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Log.i(TAG, "MSG_GET_MD5解析失败");
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
			// DialogUtil.hideProgressDialog();
		}
	};

	/**
	 * 获取个人信息
	 */
	private void getInfo() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		Log.i(TAG, ClientConstant.USERINFO_URL);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETINFOMATION);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_USERINFO, params,
				ClientConstant.USERINFO_URL);

	}

	/**
	 * 获取与更新敏感词词库
	 */
	private void getlexicon() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETLEXICON);
		params.put("md5", SpfUtil.readMD5(mContext));
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_MD5, params,
				ClientConstant.USERINFO_URL);
	}

	/**
	 * 添加个人信息
	 */
	private void setInfo() {
		// DialogUtil.showCancelProgressDialog(mContext, "", "请稍等", true, true);
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.ADDINFOMATION);
		params.put("useid", SpfUtil.readUserId(mContext));
		params.put("nickname", name);
		params.put("sex", sexStr + "");
		params.put("birthday", birth);
		params.put("height", height);
		params.put("weight", weight);
		params.put("job", jobId);
		params.put("hobby", hobbyId);
		params.put("telephone", tel);
		params.put("location", address);
		params.put("name", realname);
		params.put("postalcode", postalcode);
		if (isSetAatar) {
			try {
				File file = new File(iconurl);
				if (file.exists() && file.length() > 0) {
					params.put("icon", "avatar");
					params.put("avatar", file);
				} else {
					params.put("icon", "");
					T.showToast(mContext, "头像上传失败", 0);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			params.put("icon", "");
		}
		// L.i("",
		// name+"\n"+sexStr+"\n"+birth+"\n"+height+"\n"+weight+"\n"+jobId+"\n"+iconurl);

		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_SET_USERINFO, params,
				ClientConstant.USERINFO_URL);
	}

	/**
	 * 获取职业列表
	 */
	private void getJobList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETJOBLIST);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_JOBLIST, params,
				ClientConstant.USERINFO_URL);
	}

	/**
	 * 获取兴趣列表
	 */

	private void getHobbyList() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETHOBBYLIST);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_HOBBYLIST, params,
				ClientConstant.USERINFO_URL);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_SELECT_JOB) {
			if (resultCode == RESULT_OK) {
				jobName = data.getStringExtra("jobname");
				jobId = data.getStringExtra("jobid");
				tv_job.setText(jobName);
			}
		}

		else if (requestCode == REQ_SELECT_HOBBY) {
			if (resultCode == RESULT_OK) {
				hobbyName = data.getStringExtra("hobbyname");
				hobbyId = data.getStringExtra("hobbyid");
				tv_hobby.setText(hobbyName);
			}
		}

		else if (requestCode == REQ_SET_AVATAR && data != null
				&& data.getExtras() != null) {
			if (resultCode == RESULT_OK) {
				iconurl = data.getStringExtra("url");
				// L.i("", iconurl);
				String imageUri = "file://" + iconurl;
				loader.clearMemoryCache();
				loader.clearDiskCache();
				loader.displayImage(imageUri, iv_my_avatar);
				isSetAatar = true;
				return;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String str1 = tv_right.getText().toString().trim();
			if (str1.equals("编辑"))
				finish();
			else if (str1.equals("保存"))
				finishActivity();
		}
		return false;
	}

	private void finishActivity() {
		DialogUtil.showAlertDialog(mContext, "提示", "您在编辑状态还未保存，确认返回吗？", "确定",
				"取消", new OnAlertViewClickListener() {
					@Override
					public void confirm() {
						finish();
					}

					@Override
					public void cancel() {
					}
				});
	}

	private void initAvatar() {
		String imgpath = Environments.getImagePath() + "/avatar.jpg";
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

	/**
	 * @author chenzheng_Java 保存用户输入的内容到文件
	 */
	private boolean save(String content) {
		try {
			/*
			 * 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
			 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 public
			 * abstract FileOutputStream openFileOutput(String name, int mode)
			 * throws FileNotFoundException; openFileOutput(String name, int
			 * mode); 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
			 * 该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 第二个参数，代表文件的操作模式
			 * MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 MODE_APPEND 私有
			 * 重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 MODE_WORLD_READABLE 公用 可读
			 * MODE_WORLD_WRITEABLE 公用 可读写
			 */
			FileOutputStream outputStream = openFileOutput("SensitiveWord.txt",
					Activity.MODE_PRIVATE);
			outputStream.write(content.getBytes());
			outputStream.flush();
			outputStream.close();
			isSave = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSave;

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.MyInfomationActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.MyInfomationActivity);
		MobclickAgent.onPause(mContext);
	}
}
