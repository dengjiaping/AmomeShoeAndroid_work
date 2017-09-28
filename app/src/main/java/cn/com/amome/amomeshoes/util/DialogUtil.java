package cn.com.amome.amomeshoes.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class DialogUtil {
	private static String TAG = "DialogUtil";
	private static ProgressDialog mPd = null;

	/** 点击不消失的ProgressDialog */
	public static void showProgressDialog(Context c, String title,
			String message) {
		try {
			mPd = ProgressDialog.show(c, title, message);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				mPd = ProgressDialog.show(c, title, message);
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	/** 点击可消失的ProgressDialog */
	public static void showCancelProgressDialog(Context c, String title,
			String message, boolean indeterminate, boolean cancelable) {
		// Looper.prepare();
		// mPd = ProgressDialog.show(c, title, message, indeterminate,
		// cancelable);
		try {
			mPd = ProgressDialog.show(c, title, message, indeterminate,
					cancelable);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				mPd = ProgressDialog.show(c, title, message, indeterminate,
						cancelable);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public static void updateProgressDialog(String message) {
		if (mPd != null) {
			mPd.setMessage(message);
		}
	}

	public static void hideProgressDialog() {
		if (mPd != null)
			try {
				mPd.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
				try {
					mPd.dismiss();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

		mPd = null;
	}

	/**
	 * 实现2个按钮
	 * 
	 * @param mContext
	 *            上下文
	 * @param titleicon
	 *            头部图标
	 * @param title
	 *            头部文字
	 * @param message
	 *            内容
	 * @param positiveListener
	 *            回调监听
	 */
	public static void showAlertDialog(Context mContext, int titleicon,
			String title, String message,
			final OnAlertViewClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {

				if (null != positiveListener) {
					positiveListener.confirm();
				}
				dialog.dismiss();

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (null != positiveListener) {
					positiveListener.cancel();
				}
				dialog.dismiss();
			}
		});

		builder.show();
	}

	/**
	 * 实现2个按钮,diy按钮
	 *
	 * @param mContext
	 *            上下文
	 * @param titleicon
	 *            头部图标
	 * @param title
	 *            头部文字
	 * @param message
	 *            内容
	 * @param positiveListener
	 *            回调监听
	 */
	public static void showDiyAlertDialog(Context mContext, int titleicon,
			String title, String message, String button1, String button2,
			final OnAlertViewClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(button1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						if (null != positiveListener) {
							positiveListener.confirm();
						}
						dialog.dismiss();

					}
				});
		builder.setNegativeButton(button2,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (null != positiveListener) {
							positiveListener.cancel();
						}
						dialog.dismiss();
					}
				});

		builder.show();
	}

	/**
	 * 实现1个按钮
	 * 
	 * @param mContext
	 *            上下文
	 * @param titleicon
	 *            头部图标
	 * @param title
	 *            头部文字
	 * @param message
	 *            内容
	 * @param positiveListener
	 *            回调监听
	 */
	public static void showOneAlertDialog(Context mContext, int titleicon,
			String title, String message,
			final OnOneAlertViewClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {

				if (null != positiveListener) {
					positiveListener.confirm();
				}
				dialog.dismiss();

			}
		});
		builder.show();
	}

	/**
	 * 仿ios dialog 实现两个按钮
	 */
	public static void showAlertDialog(Context mContext, String title,
			String message, String button1, String button2,
			final OnAlertViewClickListener positiveListener) {
		CustomDialog.Builder ibuilder = new CustomDialog.Builder(mContext);
		ibuilder.setTitle(title);
		ibuilder.setMessage(message);
		ibuilder.setPositiveButton(button1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						if (null != positiveListener) {
							positiveListener.confirm();
						}
						dialog.dismiss();

					}
				});
		ibuilder.setNegativeButton(button2,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (null != positiveListener) {
							positiveListener.cancel();
						}
						dialog.dismiss();
					}
				});
		try {
			ibuilder.create().show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 仿ios dialog 实现一个按钮
	 */
	public static void showiosAlertDialog(Context mContext, String title,
			String message, String button1,
			final OnOneAlertViewClickListener positiveListener) {
		CustomDialog.Builder ibuilder = new CustomDialog.Builder(mContext);
		ibuilder.setTitle(title);
		ibuilder.setMessage(message);
		ibuilder.setPositiveButton(button1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						if (null != positiveListener) {
							positiveListener.confirm();
						}
						dialog.dismiss();

					}
				});
		ibuilder.create().show();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 */
	public interface OnAlertViewClickListener {
		public abstract void confirm();

		public abstract void cancel();
	}

	public interface OnOneAlertViewClickListener {
		public abstract void confirm();
	}

	/**
	 * 显示Choose窗口-方法1(带按钮选项,按钮事件,取消事件)
	 * 
	 * @param context
	 * @param button1Text
	 *            按钮1文字
	 * @param button2Text
	 *            按钮2文字
	 * @param button1ClickListener
	 *            按钮1点击事件
	 * @param button2ClickListener
	 *            按钮2点击事件
	 * @param cancleListener
	 *            取消dialog事件
	 */
	public static MenuDialog createMenuDialog(Context context,
			String button1Text, String button2Text,
			DialogInterface.OnClickListener button1ClickListener,
			DialogInterface.OnClickListener button2ClickListener,
			DialogInterface.OnCancelListener cancleListener) {
		MenuDialog dialog = new MenuDialog(context);
		dialog.setButton0(button1Text, button1ClickListener);
		dialog.setButton1(button2Text, button2ClickListener);
		if (cancleListener != null) {
			dialog.setOnCancelListener(cancleListener);
		}
		return dialog;
	}

	/**
	 * 关闭dialog
	 * 
	 * @param dialog
	 */
	public static void cancelDialog(Dialog dialog) {
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			dialog = null;
		}
	}
}
