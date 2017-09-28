package cn.com.amome.amomeshoes.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

public class SaveImageViewPic {
	private static MediaScannerConnection msc;

	public static void saveBitmap(Context context, Bitmap bitmap,
			final String filePath, String fileName) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream outStream = null;
		File file = new File(filePath, fileName);
		try {
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			bitmap.recycle();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// // 其次把文件插入到系统图库
		// try {
		// MediaStore.Images.Media.insertImage(context.getContentResolver(),
		// file.getAbsolutePath(), filePath + fileName, null);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// 最后通知图库更新
		// context.sendBroadcast(new Intent(
		// Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
		// Uri.parse("file://" + Environment.getExternalStorageDirectory())));

	}
}
