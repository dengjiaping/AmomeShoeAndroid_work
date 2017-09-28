package cn.com.amome.amomeshoes.util;

import android.content.Context;
import android.widget.Toast;

public class T {
	private static final String TAG = "T";
	private static Toast toast = null;

	/** 防止重复toast */
	public static void showToast(Context context, String msg, int length) {
		if (toast == null) {

			toast = Toast.makeText(context, msg, length);

		} else {
			toast.setText(msg);
		}
		toast.show();

	}

	/** 防止重复toast */
	public static void showToast(Context context, int id, int length) {
		if (toast == null) {
			toast = Toast.makeText(context, id, length);
		} else {
			toast.setText(id);
		}
		toast.show();
	}

}
