package cn.com.amome.amomeshoes.view.main.health.service.shoebox;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.R.id;
import cn.com.amome.amomeshoes.R.layout;
import cn.com.amome.amomeshoes.R.menu;
import cn.com.amome.amomeshoes.adapter.ProblemAdapter;
import cn.com.amome.amomeshoes.adapter.ShoeDelReasonAdapter;
import cn.com.amome.amomeshoes.adapter.ShoeDelReasonConstantAdapter;
import cn.com.amome.amomeshoes.events.ExerciseHistEvent;
import cn.com.amome.amomeshoes.events.ShoeDelEvent;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.HttpService;
import cn.com.amome.amomeshoes.http.PostAsyncTask;
import cn.com.amome.amomeshoes.model.ShoesDelReason;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.SpfUtil;
import cn.com.amome.amomeshoes.util.T;
import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoesDelReasonActivity extends Activity implements OnClickListener {
	private String TAG = "ShoesDelReasonActivity";
	private Context mContext;
	private RelativeLayout ll_del_reason;
	private LinearLayout ll_bt;
	private GridView gridView_problem;
	private TextView tv_confirm, tv_cancel;
	private Gson gson = new Gson();
	private List<ShoesDelReason> shoeDelReaList;
	private List<String> tmpDelReaList;
	private ShoeDelReasonAdapter shoeDelAdapter;
	private ShoeDelReasonConstantAdapter shoeDelConstantAdapterAdapter;
	private int problem = 0;// 当前选中的问题使用10进制表示的数
	private int problem_num = 0;// 选中问题的个数
	private String reason = "";
	private static final int MSG_GET_DEL_REASON = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ClientConstant.HANDLER_SUCCESS:
				switch (msg.arg1) {
				case MSG_GET_DEL_REASON:// 鞋问题
					String problemJson = (String) msg.obj;
					if (TextUtils.isEmpty(problemJson)) {
					} else {
						shoeDelReaList = gson.fromJson(problemJson,
								new TypeToken<List<ShoesDelReason>>() {
								}.getType());
						if (shoeDelReaList != null && shoeDelReaList.size() > 0) {
							shoeDelAdapter = new ShoeDelReasonAdapter(mContext,
									shoeDelReaList);
							gridView_problem.setAdapter(shoeDelAdapter);
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
		setContentView(R.layout.activity_shoebox_del_reason);
		mContext = this;
		initView();
		getShoesDelReason();
	}

	private void initView() {
		ll_del_reason = (RelativeLayout) findViewById(R.id.ll_del_reason);
		ll_bt = (LinearLayout) findViewById(R.id.ll_bt);
		gridView_problem = (GridView) findViewById(R.id.gridView_problem);
		tv_confirm = (TextView) findViewById(R.id.tv_confirm);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_confirm.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.tv_confirm:
			Log.i(TAG, "准备删除");
			String binaryString = Integer.toBinaryString(problem);
			// Log.i(TAG, "binaryString=" + binaryString);
			for (int i = 0; i < binaryString.getBytes().length; i++) {
				if (get(problem, i) == 1) {
					reason += "," + (i + 1);
				}
			}
			if (reason.length() > 0) {
				reason = reason.substring(1);
				// Log.i(TAG, "截取后" + reason);
			}
			finish();
			EventBus.getDefault().post(new ShoeDelEvent(reason));
			break;
		case R.id.tv_cancel:
			finish();
			break;
		}
	}

	/**
	 * @param num
	 *            :要获取二进制值的数
	 * @param index
	 *            :位数
	 */
	public static int get(int num, int index) {
		return (num & (0x1 << index)) >> index;
	}

	/**
	 * 鞋问题
	 */
	private void getShoesDelReason() {
		RequestParams params = new RequestParams();
		params.put("calltype", ClientConstant.GETSHOEREASON_TYPE);
		params.put("useid", SpfUtil.readUserId(mContext));
		PostAsyncTask postTask = new PostAsyncTask(mHandler);
		postTask.startAsyncTask(mContext, callback, MSG_GET_DEL_REASON, params,
				ClientConstant.SHOEBOX_URL);
	}

	HttpService.ICallback callback = new HttpService.ICallback() {

		@Override
		public void onHttpPostSuccess(int type, int statusCode,
				Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			String result;
			switch (type) {
			case MSG_GET_DEL_REASON:
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
					Log.i(TAG, "MSG_GET_DEL_REASON解析失败");
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

	public void setSelect(int position, boolean isChecked) {
		int pronum = Integer.parseInt(shoeDelReaList.get(position).id);
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
		// Log.i(TAG, Integer.toBinaryString(problem));
		L.i("", problem + "----将int数据转换=" + getUnsignedIntt(problem));
	}

	public void setConstantSelect(int position, boolean isChecked) {
		int pronum = Integer.parseInt(shoeDelReaList.get(position).id);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

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
