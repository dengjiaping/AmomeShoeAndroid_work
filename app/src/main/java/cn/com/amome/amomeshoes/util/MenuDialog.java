package cn.com.amome.amomeshoes.util;

import cn.com.amome.amomeshoes.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 从底部弹出的有确定取消按钮的对话框
 * 
 * @author hhsun@iflytek.com
 * @version Create Time：Jul 14, 2015 3:33:22 PM
 */
public class MenuDialog extends Dialog implements
		android.view.View.OnClickListener {

	/**
	 * 左侧按钮
	 */
	private Button menuDialogButton1;
	/**
	 * 右侧按钮
	 */
	private Button menuDialogButton2;

	private Context mContext;

	private OnClickListener listener1;

	private OnClickListener listener2;

	private String btnText1;
	private String btnText2;

	public MenuDialog(Context context) {
		super(context, R.style.transparentFrameWindowStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_menu,
				null);
		setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		menuDialogButton1 = (Button) findViewById(R.id.button1);
		menuDialogButton2 = (Button) findViewById(R.id.button2);
		menuDialogButton1.setOnClickListener(this);
		menuDialogButton2.setOnClickListener(this);
		init();
		Window window = getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		// 下述代码应该放在Activity中
		if (mContext instanceof Activity) {
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = ((Activity) mContext).getWindowManager().getDefaultDisplay()
					.getHeight();
			// 以下这两句是为了保证按钮可以水平满屏
			wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
			wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			onWindowAttributesChanged(wl);
		}
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}

	private void init() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(btnText1)) {
			menuDialogButton1.setText(btnText1);
		}
		if (!TextUtils.isEmpty(btnText2)) {
			menuDialogButton2.setText(btnText2);
		}
	}

	public void setButton0(String text, OnClickListener l) {
		btnText1 = text;
		listener1 = l;
	}

	public void setButton1(String text, OnClickListener l) {
		btnText2 = text;
		listener2 = l;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			if (listener1 != null) {
				listener1.onClick(this, 0);
			}
			break;
		case R.id.button2:
			if (listener2 != null) {
				listener2.onClick(this, 1);
			}
			break;

		default:
			break;
		}
	}

}
