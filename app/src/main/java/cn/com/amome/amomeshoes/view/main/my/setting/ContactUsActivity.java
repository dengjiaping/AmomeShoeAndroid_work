package cn.com.amome.amomeshoes.view.main.my.setting;

import com.umeng.analytics.MobclickAgent;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;
import cn.com.amome.amomeshoes.util.DialogUtil;
import cn.com.amome.amomeshoes.util.SaveImageViewPic;
import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.main.health.MarketWebviewActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ContactUsActivity extends Activity implements OnClickListener {

	private Context mContext;
	private TextView tv_title;
	private ImageView iv_erweima;
	private Dialog mIsSaveImageDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_contact);
		mContext = this;
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_title.setText("联系我们");
		tv_title.setTextColor(mContext.getResources().getColor(
				R.color.turkeygreen));
		iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
		iv_erweima.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				saveImageDialog();
				return false;
			}
		});
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.tv_youzan).setOnClickListener(this);
		findViewById(R.id.tv_phone_contact).setOnClickListener(this);
		findViewById(R.id.tv_weibo_contact).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_youzan:
			MobclickAgent.onEvent(mContext, ClassType.market_btn);
			startActivity(new Intent(mContext, JDWebviewActivity.class));
			break;
		case R.id.tv_weibo_contact:
			startActivity(new Intent(mContext, WeiboWebviewActivity.class));
			break;
		case R.id.tv_phone_contact:
			MobclickAgent.onEvent(mContext, ClassType.call_amome);
			String number = getResources()
					.getString(R.string.app_company_phone);
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ number));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		MobclickAgent.onPageStart(ClassType.ContactUsActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.ContactUsActivity);
		MobclickAgent.onPause(mContext);
	}

	/**
	 * 长按图片保存事件处理
	 */
	private void saveImageDialog() {
		// DialogUtil.cancelDialog(mIsSaveImageDialog);
		mIsSaveImageDialog = DialogUtil.createMenuDialog(mContext, "保存", "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogUtil.cancelDialog(mIsSaveImageDialog);
						saveImage();
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DialogUtil.cancelDialog(mIsSaveImageDialog);
					}
				}, new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});
		mIsSaveImageDialog.show();
	}

	/**
	 * 将字符串转为二维码并保存
	 */
	private void saveImage() {
		Bitmap bm = ((BitmapDrawable) ((ImageView) iv_erweima).getDrawable())
				.getBitmap();
		SaveImageViewPic.saveBitmap(mContext, bm,
				Environment.getExternalStorageDirectory()
						+ "/Amome/image/", "魔秘微信公众号"
						+ ".png");
		T.showToast(mContext,
				"二维码已保存至" + Environment.getExternalStorageDirectory()
						+ "/Amome/image/", 0);
	}
}
